package com.livescore.soccerscore.matchlive.ui.livescores.home;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.dialog.NoteDateTimeDialog;
import com.livescore.soccerscore.matchlive.model.PaginationModel;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureResponse;
import com.livescore.soccerscore.matchlive.model.league.LeagueTodayModel;
import com.livescore.soccerscore.matchlive.base.BaseFragment;
import com.livescore.soccerscore.matchlive.database.fixture.FixtureDatabase;
import com.livescore.soccerscore.matchlive.databinding.FragmentHomeBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.dialog.MatchPinWarnDialog;
import com.livescore.soccerscore.matchlive.dialog.MatchPinnedDialog;
import com.livescore.soccerscore.matchlive.dialog.notification.DialogNotificationCallBack;
import com.livescore.soccerscore.matchlive.dialog.notification.NotificationDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.HomeActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.MatchDetailActivity;
import com.livescore.soccerscore.matchlive.model.live.FixtureLiveModel;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveMatchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveMatchClickCallBack;
import com.livescore.soccerscore.matchlive.model.live.LiveResponse;
import com.livescore.soccerscore.matchlive.ui.livescores.live.LiveScoreActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.search.SearchActivity;
import com.livescore.soccerscore.matchlive.util.GoToSettingCallBack;
import com.livescore.soccerscore.matchlive.util.PermissionManager;
import com.livescore.soccerscore.matchlive.util.SPUtils;
import com.livescore.soccerscore.matchlive.util.SystemUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import kotlin.Unit;
import kotlin.jvm.functions.Function5;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    LoadingDialog loadingDialog;
    List<LeagueTodayModel> list = new ArrayList<>();
    List<FixtureLiveModel> listLive = new ArrayList<>();
    LeagueTodayAdapter adapter;
    private Calendar current;
    LiveMatchAdapter liveMatchAdapter;
    int currentPage = 1;
    String selectedDate = "";
    boolean isEnableToLoadMore = true;
    HorizontalCalendar.Builder builder;
    boolean isCreateCalendarHorizontal = false;
    private HorizontalCalendar horizontalCalendar;

    @Override
    public FragmentHomeBinding setBinding(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        return FragmentHomeBinding.inflate(getLayoutInflater());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void initView() {
        loadingDialog = new LoadingDialog(requireContext(), false);
        initAdapter();
        current = Calendar.getInstance();
        initHorizontalCalendarPicker();
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            selectedDate = sdf.format(new Date(System.currentTimeMillis()));
            if (!loadingDialog.isShowing())
                loadingDialog.show();
            list.clear();
            currentPage = 1;
            fetchFixtureDatePage(selectedDate, 1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            listLive.clear();
            binding.loadingLive.setVisibility(VISIBLE);
            fetchLiveMatch();
        } else {
            Log.e("call_api_data", "No internet to call api");
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void bindView() {
        binding.ivReload.setOnClickListener(v -> {
            if (IsNetWork.haveNetworkConnection(requireContext())) {
                listLive.clear();
                liveMatchAdapter.notifyDataSetChanged();
                binding.loadingLive.setVisibility(VISIBLE);
                fetchLiveMatch();
            } else {
                Log.e("call_api_data", "No internet to call api");
            }
        });
        binding.tvSeeMore.setOnClickListener(v -> {
            startArc(new Intent(requireContext(), LiveScoreActivity.class));
        });
        binding.ivSearch.setOnClickListener(v -> {
            startArc(new Intent(requireContext(), SearchActivity.class));
        });
        binding.btnChooseDate.setOnClickListener(v -> {
            NoteDateTimeDialog noteDateTimeDialog = new NoteDateTimeDialog(requireContext(), (day, month, year, hour, minute) -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 1);
                calendar.set(Calendar.MILLISECOND, 1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDate = sdf.format(calendar.getTimeInMillis());
                Log.e("check_date", "selectDate: " + selectedDate);
//                horizontalCalendar.selectDate(calendar, true);
                dateChoose(calendar, current);
                return null;
            });
            noteDateTimeDialog.show();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void dateChoose(Calendar calendarChoose, Calendar calendarCurrent) {
        current = calendarChoose;
        Log.e("check_date", "current date: " + date(current.getTimeInMillis()));
        if (calendarChoose.getTimeInMillis() < calendarCurrent.getTimeInMillis()) {
            horizontalCalendar.selectDate(calendarChoose, true, true);
        } else {
            calendarChoose.add(Calendar.DAY_OF_YEAR, -2);
            horizontalCalendar.selectDate(calendarChoose, true, true);
        }
        if (IsNetWork.haveNetworkConnection(requireContext())) {
            list.clear();
            adapter.notifyDataSetChanged();
            if (!loadingDialog.isShowing())
                loadingDialog.show();
            currentPage = 1;
            fetchFixtureDatePage(selectedDate, 1);
        } else {
            Log.e("call_api_data", "No internet to call api");
        }

    }

    public void startArc(Intent intent) {
        if (getContext() instanceof HomeActivity) {
            HomeActivity main = (HomeActivity) getContext();
            main.resultLauncher.launch(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initHorizontalCalendarPicker() {
        try {
//            if (!isCreateCalendarHorizontal){
//            SystemUtil.setLocale(requireContext());
            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.YEAR, -10);

            Calendar endDate = Calendar.getInstance();
            endDate.add(Calendar.YEAR, 8);
            horizontalCalendar = new HorizontalCalendar.Builder(requireActivity(), R.id.calendarView)
                    .range(startDate, endDate)
                    .datesNumberOnScreen(5)
                    .configure()
                    .formatTopText("MMM")
                    .formatMiddleText("dd")
                    .formatBottomText("EEE")
                    .showTopText(true)
                    .showBottomText(true)
                    .textSize(16, 16, 12)
                    .textColor(Color.BLACK, Color.BLACK)
                    .end()
                    .defaultSelectedDate(Calendar.getInstance())
                    .addEvents(new CalendarEventsPredicate() {
                        Random rnd = new Random();

                        @Override
                        public List<CalendarEvent> events(Calendar date) {
                            List<CalendarEvent> events = new ArrayList<>();
                            int count = rnd.nextInt(6); // 0 to 5
                            for (int i = 0; i <= count; i++) {
                                events.add(new CalendarEvent(
                                        Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)),
                                        "event"
                                ));
                            }
                            return events;
                        }
                    })
                    .build();

            horizontalCalendar.selectDate(Calendar.getInstance(), true, false);
            horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onDateSelected(Calendar date, int position, boolean isChoose) {
                    if (!isChoose) {
                        date.add(Calendar.DAY_OF_YEAR, 1);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        selectedDate = sdf.format(date.getTimeInMillis());
                        Log.e("check_date", "onDateSelect: " + selectedDate);
                        if (IsNetWork.haveNetworkConnection(requireContext())) {
                            list.clear();
                            adapter.notifyDataSetChanged();
                            if (!loadingDialog.isShowing())
                                loadingDialog.show();
                            currentPage = 1;
                            fetchFixtureDatePage(selectedDate, 1);
                        } else {
                            Log.e("call_api_data", "No internet to call api");
                        }
                        current = date;
                        Log.e("check_date", "current date: " + date(current.getTimeInMillis()));
                    }
                }
            });

//                isCreateCalendarHorizontal
//            }


        } catch (IllegalStateException e) {
            e.printStackTrace();
            Log.e("date_pick", "error: ", e);
        }
    }

    private String date(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date(millis));

        Log.d("TimeConvert", "Thời gian: " + formattedDate);
        return formattedDate;
    }

    public void fetchFixtureDatePage(String date, int page) {
        List<FixtureModel> fixtureModelList = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixturesByDate(date);
        Log.e("check_date", "list database " + date + fixtureModelList);
        try {
            ApiDataService.apiService.callFixtureToday(date, ConstantApiData.KEY, ConstantApiData.TIMEZONE, "today.participants;today.scores;today.state", page).enqueue(new Callback<FixtureResponse>() {
                @Override
                public void onResponse(@NonNull Call<FixtureResponse> call, @NonNull Response<FixtureResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "page: " + page);
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        FixtureResponse teamResponse = response.body();
                        int oldPos = list.size();
                        if (teamResponse.data != null) {
                            list.addAll(teamResponse.data);
                            for (LeagueTodayModel leagueTodayModel : teamResponse.data) {
                                List<FixtureModel> todayList = leagueTodayModel.getToday();
                                for (int i = 0; i < todayList.size(); i++) {
                                    FixtureModel fixtureModel = todayList.get(i);

                                    Iterator<FixtureModel> iterator = fixtureModelList.iterator();
                                    while (iterator.hasNext()) {
                                        FixtureModel fixtureModelDB = iterator.next();
                                        if (fixtureModel.id == fixtureModelDB.id) {
                                            todayList.set(i, fixtureModelDB);
                                            iterator.remove();
                                            break;
                                        }
                                    }
                                }
                            }

                            Log.e("API_RESPONSE", "data: " + teamResponse.data);
                            Log.e("API_RESPONSE", "pagination: " + teamResponse.pagination);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(teamResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isEnableToLoadMore = false;
                            } else {
                                isEnableToLoadMore = pagination.has_more;
                            }
                            if (oldPos - 1 >= 0)
                                adapter.notifyItemChanged(oldPos - 1);
                            adapter.notifyItemRangeInserted(oldPos, teamResponse.data.size());
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            if (list.isEmpty()) {
                                binding.noDataToday.setVisibility(VISIBLE);
                            } else binding.noDataToday.setVisibility(GONE);
//                            binding.rcvLeagueToday.post(() -> loadingDialog.dismiss());
                        } else {
                            if (loadingDialog.isShowing())
                                loadingDialog.dismiss();
                            if (list.isEmpty()) {
                                binding.noDataToday.setVisibility(VISIBLE);
                            } else binding.noDataToday.setVisibility(GONE);
                            Log.e("call_api_data", "data list null");
                        }
                    } else {
                        if (loadingDialog.isShowing())
                            loadingDialog.dismiss();
                        if (list.isEmpty()) {
                            binding.noDataToday.setVisibility(VISIBLE);
                        } else binding.noDataToday.setVisibility(GONE);
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FixtureResponse> call, @NonNull Throwable t) {
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    if (list.isEmpty()) {
                        binding.noDataToday.setVisibility(VISIBLE);
                    } else binding.noDataToday.setVisibility(GONE);
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
            if (list.isEmpty()) {
                binding.noDataToday.setVisibility(VISIBLE);
            } else binding.noDataToday.setVisibility(GONE);
            Log.e("call_api_data", "catch: ", e);
        }
    }

    public void fetchLiveMatch() {
        try {
            ApiDataService.apiService.callLiveMatch(ConstantApiData.KEY, ConstantApiData.TIMEZONE, "participants;scores;state;periods").enqueue(new Callback<LiveResponse>() {
                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                @Override
                public void onResponse(@NonNull Call<LiveResponse> call, @NonNull Response<LiveResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        LiveResponse teamResponse = response.body();
                        if (teamResponse.data != null) {
                            for (FixtureLiveModel fixtureLiveModel : teamResponse.data) {
                                if (!fixtureLiveModel.getState().short_name.equals("NS") && !fixtureLiveModel.getState().short_name.equals("FT"))
                                    listLive.add(fixtureLiveModel);
                            }
                            Log.e("API_RESPONSE", "data: " + teamResponse.data);
                            Log.e("call_api_data", "call true:");
                            for (FixtureLiveModel liveModel : listLive) {
                                Log.e("API_RESPONSE", "livemodel: " + liveModel.toString());
                            }
                            liveMatchAdapter.notifyDataSetChanged();
                            binding.rcvLeagueToday.post(() -> {
                                binding.loadingLive.setVisibility(GONE);
                                binding.tvLiveNow.setText(getString(R.string.live_now) + " (" + listLive.size() + ")");
                            });
                            if (listLive.isEmpty()) {
                                binding.noDataLive.setVisibility(VISIBLE);
                            } else binding.noDataLive.setVisibility(GONE);
                        } else {
                            binding.loadingLive.setVisibility(GONE);
                            Log.e("call_api_data", "data live null");
                            if (listLive.isEmpty()) {
                                binding.noDataLive.setVisibility(VISIBLE);
                            } else binding.noDataLive.setVisibility(GONE);
                        }
                    } else {
                        binding.loadingLive.setVisibility(GONE);
                        if (listLive.isEmpty()) {
                            binding.noDataLive.setVisibility(VISIBLE);
                        } else binding.noDataLive.setVisibility(GONE);
                        Log.e("call_api_data", "call false: Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LiveResponse> call, @NonNull Throwable t) {
                    binding.loadingLive.setVisibility(GONE);
                    if (listLive.isEmpty()) {
                        binding.noDataLive.setVisibility(VISIBLE);
                    } else binding.noDataLive.setVisibility(GONE);
                    Log.e("call_api_data", "onfailure" + t);
                }
            });

        } catch (Exception e) {
            binding.loadingLive.setVisibility(GONE);
            if (listLive.isEmpty()) {
                binding.noDataLive.setVisibility(VISIBLE);
            } else binding.noDataLive.setVisibility(GONE);
            Log.e("call_api_data", "catch: ", e);
        }
    }

    public void showPinnedMatch() {
        MatchPinnedDialog dialog = new MatchPinnedDialog(requireContext(), false);
        dialog.binding.btnOK.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void initAdapter() {
        adapter = new LeagueTodayAdapter(requireContext(), list, new LeagueHomeClickCallBack() {
            @Override
            public void select(LeagueTodayModel leagueTodayModel) {
            }

            @Override
            public void load() {
                if (!loadingDialog.isShowing())
                    loadingDialog.show();
                if (isEnableToLoadMore) {
                    currentPage++;
                    fetchFixtureDatePage(selectedDate, currentPage);
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }
            }
        }, new FixtureClickCallBack() {
            @Override
            public void select(int pos, FixtureModel fixtureModel) {
                Log.e("check_id", "select " + fixtureModel.id);
                Intent intent = new Intent(requireContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                startArc(intent);
            }

            @Override
            public void pin(int pos, FixtureModel fixtureModel) {
                if (!PermissionManager.checkOverlayPermission(requireContext())) {
                    SPUtils.showDialogGotoSetting(requireContext(), 2, new GoToSettingCallBack() {
                        @Override
                        public void goToSetting(Intent intent) {
                            startArc(intent);
                        }
                    });
                } else {
                    if (fixtureModel.isPin) {
                        fixtureModel.isPin = false;
                        if (fixtureModel.isAlarm) {
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                            fixtureModel.schedule(requireContext());
                        } else {
                            FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureModel.id);
                            fixtureModel.cancelNotification(requireContext());
                        }
                        for (FixtureModel fixtureModel2 : list.get(pos).getToday()) {
                            if (fixtureModel.id == fixtureModel2.id) {
                                fixtureModel2 = fixtureModel;
                                break;
                            }
                        }
                        adapter.notifyItemChanged(pos);
                    } else {
                        FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureByPin();
                        FixtureModel fixture23 = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel.id);

                        if (fixtureBase == null) {
                            if (fixture23 != null) {
                                fixtureModel.isPin = true;
                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                                fixtureModel.schedule(requireContext());
                                for (FixtureModel fixtureModel2 : list.get(pos).getToday()) {
                                    if (fixtureModel.id == fixtureModel2.id) {
                                        fixtureModel2 = fixtureModel;
                                        break;
                                    }
                                }
                                adapter.notifyItemChanged(pos);
                            } else {
                                fixtureModel.isPin = true;
                                fixtureModel.create_at = System.currentTimeMillis();
                                FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel);
                                fixtureModel.schedule(requireContext());
                                for (FixtureModel fixtureModel2 : list.get(pos).getToday()) {
                                    if (fixtureModel.id == fixtureModel2.id) {
                                        fixtureModel2 = fixtureModel;
                                        break;
                                    }
                                }
                                adapter.notifyItemChanged(pos);
                            }
                            showPinnedMatch();
                        } else {
                            MatchPinWarnDialog dialog = new MatchPinWarnDialog(requireContext(), false);
                            dialog.binding.btnCancel.setOnClickListener(v -> {
                                dialog.dismiss();
                            });
                            if (fixture23 != null) {
                                dialog.binding.btnReplace.setOnClickListener(v -> {
                                    fixtureModel.isPin = true;
                                    for (FixtureModel fixtureModel2 : list.get(pos).getToday()) {
                                        if (fixtureModel.id == fixtureModel2.id) {
                                            fixtureModel2 = fixtureModel;
                                            break;
                                        }
                                    }
                                    adapter.notifyItemChanged(pos);
                                    if (fixtureBase.isAlarm) {
                                        fixtureBase.isPin = false;
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureBase);
                                        fixtureBase.schedule(requireContext());
                                    } else {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().deletePin();
                                        fixtureBase.cancelNotification(requireContext());
                                    }
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel);
                                    fixtureModel.schedule(requireContext());
                                    dialog.dismiss();
                                    out:
                                    for (int i = 0; i < list.size(); i++) {
                                        for (FixtureModel fixtureModel1 : list.get(i).getToday()) {
                                            if (fixtureModel1.id != fixtureModel.id && fixtureModel1.isPin) {
                                                fixtureModel1.isPin = false;
                                                adapter.notifyItemChanged(i);
                                                break out;
                                            }
                                        }
                                    }
                                });
                            } else {
                                dialog.binding.btnReplace.setOnClickListener(v -> {
                                    fixtureModel.isPin = true;
                                    for (FixtureModel fixtureModel2 : list.get(pos).getToday()) {
                                        if (fixtureModel.id == fixtureModel2.id) {
                                            fixtureModel2 = fixtureModel;
                                            break;
                                        }
                                    }
                                    adapter.notifyItemChanged(pos);
                                    if (fixtureBase.isAlarm) {
                                        fixtureBase.isPin = false;
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureBase);
                                        fixtureBase.schedule(requireContext());
                                    } else {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().deletePin();
                                        fixtureBase.cancelNotification(requireContext());
                                    }
                                    fixtureModel.create_at = System.currentTimeMillis();
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel);
                                    fixtureModel.schedule(requireContext());
                                    dialog.dismiss();
                                    out:
                                    for (int i = 0; i < list.size(); i++) {
                                        for (FixtureModel fixtureModel1 : list.get(i).getToday()) {
                                            if (fixtureModel1.id != fixtureModel.id && fixtureModel1.isPin) {
                                                fixtureModel1.isPin = false;
                                                adapter.notifyItemChanged(i);
                                                break out;
                                            }
                                        }
                                    }
                                });
                            }
                            dialog.show();
                        }

                    }
                }
            }

            @Override
            public void alarm(int pos, FixtureModel fixtureModel) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    AlarmManager alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
                    if (!alarmManager.canScheduleExactAlarms()) {
                        Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                        startArc(intent);
                        return;
                    }
                }
                if (PermissionManager.checkNotificationPermission(requireContext())) {
                    FixtureModel fixtureBase = FixtureDatabase.getInstance(requireContext()).fixtureDAO().getFixtureById(fixtureModel.id);
                    NotificationDialog dialog = new NotificationDialog(requireContext(), false);
                    dialog.initFixture(fixtureModel);
                    dialog.initCallBack(new DialogNotificationCallBack() {
                        @Override
                        public void cancel() {
                            dialog.dismiss();
                        }

                        @Override
                        public void save(FixtureModel fixtureModel1) {
                            Log.d("alarmcheck", "save: " + fixtureModel1);
                            if (fixtureModel1.isAlarm) {
                                if (fixtureBase != null) {
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel1);
                                } else {
                                    fixtureModel1.create_at = System.currentTimeMillis();
                                    FixtureDatabase.getInstance(requireContext()).fixtureDAO().insert(fixtureModel1);
                                }
                                fixtureModel1.schedule(requireContext());
                            } else {
                                if (fixtureBase != null) {
                                    if (fixtureBase.isPin) {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().update(fixtureModel1);
                                        fixtureModel1.schedule(requireContext());
                                    } else {
                                        FixtureDatabase.getInstance(requireContext()).fixtureDAO().delete(fixtureModel1.id);
                                        fixtureModel1.cancelNotification(requireContext());
                                    }

                                }
                            }
                            for (FixtureModel fixtureModel2 : list.get(pos).getToday()) {
                                if (fixtureModel1.id == fixtureModel2.id) {
                                    fixtureModel2 = fixtureModel1;
                                    break;
                                }
                            }
                            adapter.notifyItemChanged(pos);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else {
                    SPUtils.showDialogGotoSetting(requireContext(), 1, new GoToSettingCallBack() {
                        @Override
                        public void goToSetting(Intent intent) {
                            startArc(intent);
                        }
                    });
                }
            }
        });
        liveMatchAdapter = new LiveMatchAdapter(requireContext(), listLive, new LiveMatchClickCallBack() {
            @Override
            public void detail(FixtureLiveModel fixtureModel) {
                Log.e("check_id", "select " + fixtureModel.id);
                Intent intent = new Intent(requireContext(), MatchDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_FIXTURE, fixtureModel.id);
                intent.putExtra(SPUtils.INTENT_LIVE_NOW, true);
                startArc(intent);
            }
        });
        binding.rcvLeagueToday.setAdapter(adapter);
        binding.rcvLive.setAdapter(liveMatchAdapter);
    }
}