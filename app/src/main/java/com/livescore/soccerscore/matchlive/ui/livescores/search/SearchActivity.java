package com.livescore.soccerscore.matchlive.ui.livescores.search;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.ads.IsNetWork;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.api_data.model.PaginationModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.api_data.model.league.LeagueResponse;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamResponse;
import com.livescore.soccerscore.matchlive.base.BaseActivity;
import com.livescore.soccerscore.matchlive.databinding.ActivitySearchBinding;
import com.livescore.soccerscore.matchlive.dialog.LoadingDialog;
import com.livescore.soccerscore.matchlive.ui.livescores.league_detail.LeagueDetailActivity;
import com.livescore.soccerscore.matchlive.ui.livescores.search.league.LeagueSearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.league.LeagueSearchClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.recent.ItemRecentClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.recent.SearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.team.TeamSearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.team.TeamSearchClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.top_search.TopSearchAdapter;
import com.livescore.soccerscore.matchlive.ui.livescores.search.top_search.TopSearchClickCallBack;
import com.livescore.soccerscore.matchlive.ui.livescores.search.top_search.TopSearchModel;
import com.livescore.soccerscore.matchlive.ui.livescores.team_detail.TeamDetailActivity;
import com.livescore.soccerscore.matchlive.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {

    ArrayList<String> list = new ArrayList<>();
    ArrayList<TopSearchModel> listTopSearch = new ArrayList<>();
    SearchAdapter adapter;
    TopSearchAdapter topSearchAdapter;
    TeamSearchAdapter teamAdapter;
    LeagueSearchAdapter leagueAdapter;
    List<TeamModel> listTeamModel = new ArrayList<>();
    List<LeagueModel> listLeagueModel = new ArrayList<>();
    int currentPageTeam = 1;
    int currentPageLeague = 1;
    boolean isHasMoreTeam = true;
    boolean isHasMoreLeague = true;
    LoadingDialog loadingDialog;
    String strSearch = "";


    @Override
    public ActivitySearchBinding getBinding() {
        return ActivitySearchBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initView() {
        loadingDialog = new LoadingDialog(this, false);
        initDataTopSearch();
        list = SPUtils.getList(this, SPUtils.LIST_RECENT);
        initAdapter();
        if (list.isEmpty()) {
            binding.clRecent.setVisibility(GONE);
        } else {
            binding.clRecent.setVisibility(VISIBLE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void bindView() {
        binding.tvTeam.setOnClickListener(v -> {
            search(1, binding.edtText.getText().toString().trim());
        });
        binding.tvLeague.setOnClickListener(v -> {
            search(2, binding.edtText.getText().toString().trim());
        });
        binding.ivExitSearch.setOnClickListener(v -> {
            if (binding.edtText.getText().toString().trim().isEmpty())
                onBack();
            else {
                binding.edtText.setText("");
                binding.clRecentTopSearch.setVisibility(VISIBLE);
                binding.clResult.setVisibility(GONE);
                binding.llOption.setVisibility(GONE);
            }
        });
        binding.tvDeleteAll.setOnClickListener(v -> {
            list.clear();
            SPUtils.removeList(this, SPUtils.LIST_RECENT);
            adapter.notifyDataSetChanged();
            binding.clRecent.setVisibility(GONE);
        });
        binding.edtText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                    String text = binding.edtText.getText().toString().trim();
                    if (!text.isEmpty()) {
                        search(1, text);
                        list.add(0, text);
                        SPUtils.setList(getBaseContext(), SPUtils.LIST_RECENT, list);
                        adapter.notifyDataSetChanged();
                        if (list.isEmpty()) {
                            binding.clRecent.setVisibility(GONE);
                        } else {
                            binding.clRecent.setVisibility(VISIBLE);
                        }
                    }
                    binding.edtText.clearFocus();
                    SPUtils.hideKeyboard(getBaseContext(), binding.edtText);
                    return true;
                }
                return false;
            }
        });
        binding.edtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    binding.clRecentTopSearch.setVisibility(VISIBLE);
                    binding.clResult.setVisibility(GONE);
                    binding.llOption.setVisibility(GONE);
                }
            }
        });
        binding.edtText.setOnClickListener(v -> {
            SPUtils.showKeyboard(this, binding.edtText);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void search(int state, String str) {
        strSearch = str;
        binding.edtText.setText(str);
        binding.clRecentTopSearch.setVisibility(GONE);
        binding.clResult.setVisibility(VISIBLE);
        binding.llOption.setVisibility(VISIBLE);
        Log.e("check_search", "str: " + str);
        changeState(state);
        if (state == 1) {
            listTeamModel.clear();
            currentPageTeam = 1;
            loadingDialog.show();
            fetchTeamPage(str, currentPageTeam);
        } else {
            listLeagueModel.clear();
            currentPageLeague = 1;
            loadingDialog.show();
            fetchLeaguePage(str, currentPageLeague);
        }
    }

    private void resetChange() {
        binding.tvLeague.setBackgroundResource(0);
        binding.tvTeam.setBackgroundResource(0);
        binding.tvLeague.setTextColor(Color.parseColor("#a3a3a3"));
        binding.tvTeam.setTextColor(Color.parseColor("#a3a3a3"));
        binding.rcvResultTeam.setVisibility(GONE);
        binding.rcvResultLeague.setVisibility(GONE);
    }

    private void changeState(int state) {
        resetChange();
        if (state == 2) {
            binding.tvLeague.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvLeague.setTextColor(Color.parseColor("#0094FD"));
            binding.rcvResultLeague.setVisibility(VISIBLE);
        } else if (state == 1) {
            binding.tvTeam.setBackgroundResource(R.drawable.bg_select_favourite_item);
            binding.tvTeam.setTextColor(Color.parseColor("#0094FD"));
            binding.rcvResultTeam.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onBack() {
        setResult(RESULT_OK);
        finish();
    }

    private void initDataTopSearch() {
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "Arsenal", true));
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "Champions League", false));
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "La Liga", false));
        listTopSearch.add(new TopSearchModel(R.drawable.img_logo, "West Ham United", true));
    }

    private void initAdapter() {
        teamAdapter = new TeamSearchAdapter(this, listTeamModel, new TeamSearchClickCallBack() {
            @Override
            public void select(TeamModel teamModel) {
                Intent intent = new Intent(getBaseContext(), TeamDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_TEAM, teamModel);
                resultLauncher.launch(intent);
            }

            @Override
            public void load() {
                loadingDialog.show();
                if (isHasMoreTeam) {
                    if (IsNetWork.haveNetworkConnection(getBaseContext())) {
                        currentPageTeam++;
                        fetchTeamPage(strSearch, currentPageTeam);
                    } else {
                        Log.e("call_api_data", "No internet to call api");
                        new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                    }
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }loadingDialog.show();
                if (isHasMoreTeam) {
                    if (IsNetWork.haveNetworkConnection(getBaseContext())) {
                        currentPageTeam++;
                        fetchTeamPage(strSearch, currentPageTeam);
                    } else {
                        Log.e("call_api_data", "No internet to call api");
                        new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                    }
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }
            }
        });
        leagueAdapter = new LeagueSearchAdapter(this, listLeagueModel, new LeagueSearchClickCallBack() {
            @Override
            public void select(LeagueModel leagueModel) {
                Intent intent = new Intent(getBaseContext(), LeagueDetailActivity.class);
                intent.putExtra(SPUtils.INTENT_LEAGUE, leagueModel);
                resultLauncher.launch(intent);
            }

            @Override
            public void load() {
                loadingDialog.show();
                if (isHasMoreLeague) {
                    if (IsNetWork.haveNetworkConnection(getBaseContext())) {
                        currentPageLeague++;
                        fetchLeaguePage(strSearch, currentPageLeague);
                    } else {
                        Log.e("call_api_data", "No internet to call api");
                        new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                    }
                } else {
                    new Handler().postDelayed(() -> loadingDialog.dismiss(), 500);
                }
            }
        });
        topSearchAdapter = new TopSearchAdapter(this, listTopSearch, new TopSearchClickCallBack() {
            @Override
            public void select(TopSearchModel topSearchModel) {
                if (topSearchModel.isTeam)
                    search(1, topSearchModel.name);
                else search(2, topSearchModel.name);
            }
        });
        adapter = new SearchAdapter(this, list, new ItemRecentClickCallBack() {
            @Override
            public void select(String s) {
                search(1, s);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void delete(int position) {
                list.remove(position);
                SPUtils.setList(getBaseContext(), SPUtils.LIST_RECENT, list);
                adapter.notifyDataSetChanged();
                if (list.isEmpty()) {
                    binding.clRecent.setVisibility(GONE);
                } else {
                    binding.clRecent.setVisibility(VISIBLE);
                }
            }
        });
        binding.rcvResultTeam.setAdapter(teamAdapter);
        binding.rcvResultLeague.setAdapter(leagueAdapter);
        binding.rcvRecent.setAdapter(adapter);
        binding.rcvTopSearch.setAdapter(topSearchAdapter);
    }

    public ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            //ads
            Log.d("activity_check", "home");
        }
    });

    private void fetchTeamPage(String str, int page) {
        try {
            ApiDataService.apiService.callTeamSearch(str, ConstantApiData.KEY, page, "country").enqueue(new Callback<TeamResponse>() {
                @Override
                public void onResponse(@NonNull Call<TeamResponse> call, @NonNull Response<TeamResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        Log.e("API_RESPONSE", "page: " + page);
                        TeamResponse teamResponse = response.body();
                        if (teamResponse.data != null) {
                            int oldPos = listTeamModel.size();
                            listTeamModel.addAll(teamResponse.data);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(teamResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isHasMoreTeam = false;
                            } else isHasMoreTeam = pagination.has_more;
                            if (oldPos != 0) teamAdapter.notifyItemChanged(oldPos - 1);
                            teamAdapter.notifyItemRangeChanged(oldPos, teamResponse.data.size());
                            binding.rcvResultTeam.post(() -> {
                                loadingDialog.dismiss();
                                if (listTeamModel.isEmpty())
                                    binding.noResult.setVisibility(VISIBLE);
                                else binding.noResult.setVisibility(GONE);
                            });
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                            loadingDialog.dismiss();
                            if (listTeamModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
                            else binding.noResult.setVisibility(GONE);
                        }

                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                        loadingDialog.dismiss();
                        if (listTeamModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
                        else binding.noResult.setVisibility(GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TeamResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                    loadingDialog.dismiss();
                    if (listTeamModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
                    else binding.noResult.setVisibility(GONE);
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
            loadingDialog.dismiss();
            if (listTeamModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
            else binding.noResult.setVisibility(GONE);
        }
    }

    private void fetchLeaguePage(String str, int page) {
        try {
            ApiDataService.apiService.callLeagueSearch(str, ConstantApiData.KEY, page, "country").enqueue(new Callback<LeagueResponse>() {
                @Override
                public void onResponse(@NonNull Call<LeagueResponse> call, @NonNull Response<LeagueResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                        Log.e("API_RESPONSE", "page: " + page);
                        LeagueResponse leagueResponse = response.body();
                        if (leagueResponse.data != null) {
                            int oldPos = listTeamModel.size();
                            listLeagueModel.addAll(leagueResponse.data);
                            Log.e("call_api_data", "call true:");
                            Gson gson = new Gson();
                            PaginationModel pagination = gson.fromJson(new Gson().toJson(leagueResponse.pagination), PaginationModel.class);
                            if (pagination == null) {
                                isHasMoreLeague = false;
                            } else isHasMoreLeague = pagination.has_more;
                            if (oldPos != 0) leagueAdapter.notifyItemChanged(oldPos - 1);
                            leagueAdapter.notifyItemRangeChanged(oldPos, leagueResponse.data.size());
                            binding.rcvResultLeague.post(() -> {
                                loadingDialog.dismiss();
                                if (listLeagueModel.isEmpty())
                                    binding.noResult.setVisibility(VISIBLE);
                                else binding.noResult.setVisibility(GONE);
                            });
                        } else {
                            Log.e("call_api_data", "call false: Code: " + response.code());
                            loadingDialog.dismiss();
                            if (listLeagueModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
                            else binding.noResult.setVisibility(GONE);
                        }

                    } else {
                        Log.e("call_api_data", "call false: Code: " + response.code());
                        loadingDialog.dismiss();
                        if (listLeagueModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
                        else binding.noResult.setVisibility(GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LeagueResponse> call, @NonNull Throwable t) {
                    Log.e("call_api_data", "onfailure" + t);
                    loadingDialog.dismiss();
                    if (listLeagueModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
                    else binding.noResult.setVisibility(GONE);
                }
            });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
            loadingDialog.dismiss();
            if (listLeagueModel.isEmpty()) binding.noResult.setVisibility(VISIBLE);
            else binding.noResult.setVisibility(GONE);
        }
    }
}