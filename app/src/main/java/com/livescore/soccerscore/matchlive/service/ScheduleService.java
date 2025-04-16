package com.livescore.soccerscore.matchlive.service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.livescore.soccerscore.matchlive.MyApplication.CHANNEL_ID_SERVICE;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.livescore.soccerscore.matchlive.MyApplication;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.ApiDataService;
import com.livescore.soccerscore.matchlive.api_data.ConstantApiData;
import com.livescore.soccerscore.matchlive.model.ScoreModel;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.databinding.LayoutCancelPinBinding;
import com.livescore.soccerscore.matchlive.databinding.LayoutPinMatchBinding;
import com.livescore.soccerscore.matchlive.model.fixture.timeline.EventDetail;
import com.livescore.soccerscore.matchlive.model.fixture.timeline.FixtureDetailModel;
import com.livescore.soccerscore.matchlive.model.fixture.timeline.FixtureDetailResponse;
import com.livescore.soccerscore.matchlive.model.live.PeriodModel;
import com.livescore.soccerscore.matchlive.ui.splash.SplashActivity;
import com.livescore.soccerscore.matchlive.util.SystemUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleService extends Service {
    private Map<Long, Runnable> fixtureRunnables = new HashMap<>();
    private Map<Long, List<EventDetail>> mapEvent = new HashMap<>();
    public List<FixtureModel> listFixture;
    @SuppressLint("StaticFieldLeak")
    public static ScheduleService instance;
    private WindowManager.LayoutParams params;
    private WindowManager.LayoutParams paramsDelete;
    public View floatingView;
    public View deleteView;
    public LayoutPinMatchBinding floatingBinding;
    public LayoutCancelPinBinding deleteBinding;
    boolean isMoving;
    boolean isDelete;
    private int screenWidth;
    private int screenHeight;

    WindowManager windowManager;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        for (Runnable runnable : fixtureRunnables.values()) {
            handler.removeCallbacks(runnable);
        }
        fixtureRunnables.clear();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        listFixture = new ArrayList<>();
        startForeground(1, createNotification());
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
            Rect bounds = windowMetrics.getBounds();
            screenWidth = bounds.width();
            screenHeight = bounds.height();
        } else {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
            screenHeight = displayMetrics.heightPixels;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getBundleExtra(getString(R.string.bundle_alarm_obj));
            if (bundle != null) {
                FixtureModel fixtureModel = (FixtureModel) bundle.getSerializable(getString(R.string.arg_alarm_obj));
                if (fixtureModel != null) {
                    boolean isDup = false;
                    for (FixtureModel fixtureModel1 : listFixture) {
                        if (fixtureModel1.id == fixtureModel.id) {
                            stopTrackingMatch(fixtureModel1);
                            startTrackingMatch(fixtureModel);
                            isDup = true;
                        }
                    }
                    if (!isDup) {
                        listFixture.add(fixtureModel);
                        if (fixtureModel.isPin) {
                            if (deleteView != null) {
                                windowManager.removeView(deleteView);
                                deleteView = null;
                            }
                            if (floatingView != null) {
                                windowManager.removeView(floatingView);
                                floatingView = null;
                            }
                            addDeletePin();
                            addFloatingPin();
                            if (fixtureModel.participants.size() >= 2) {
                                if (fixtureModel.participants.get(0).getMeta().location.equals("home")) {
                                    Glide.with(this).load(fixtureModel.participants.get(0).getImage_path()).into(floatingBinding.ivHome);
                                    Glide.with(this).load(fixtureModel.participants.get(1).getImage_path()).into(floatingBinding.ivAway);
                                } else {
                                    Glide.with(this).load(fixtureModel.participants.get(1).getImage_path()).into(floatingBinding.ivHome);
                                    Glide.with(this).load(fixtureModel.participants.get(0).getImage_path()).into(floatingBinding.ivAway);
                                }
                            }
                            startTrackingMatch(fixtureModel);
                        }
                        if (fixtureModel.isAlarm) {
                            startTrackingMatch(fixtureModel);
                        }
                    }
                }
            }
        } else {
            Log.w("ScheduleService", "Intent is null, skipping...");
        }

        return START_STICKY;
    }


    private void onFloatingIconClick() {
        Log.e("service_check", "click");
//        if (floatingView != null) {
//            floatingBinding.tvHome.setText(String.valueOf(new Random().nextInt(10)));
//        }
    }


    public void startTrackingMatch(FixtureModel fixtureModel) {
        if (fixtureRunnables.containsKey(fixtureModel.id)) return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                fetchFixtureDetail(fixtureModel);
            }
        };
        fixtureRunnables.put(fixtureModel.id, runnable);
        handler.post(runnable);
    }

    public void stopTrackingMatch(FixtureModel fixtureModel) {
        Runnable runnable = fixtureRunnables.get(fixtureModel.id);
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            fixtureRunnables.remove(fixtureModel.id);
        }
        listFixture.remove(fixtureModel);
    }

    public void fetchFixtureDetail(FixtureModel fixtureModel) {//, long id
        if ((!fixtureModel.isPin || floatingView == null) && !fixtureModel.isAlarm) {
            stopTrackingMatch(fixtureModel);
            Log.e("check_service", "stop call api");
            return;
        }
        if (floatingView == null) Log.e("check_service", "null");
        try {
            ApiDataService.apiService.
                    callFixtureDetail(fixtureModel.id, ConstantApiData.KEY, ConstantApiData.TIMEZONE, "participants;periods;state;scores;events.type;events.period")
                    .enqueue(new Callback<FixtureDetailResponse>() {
                        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                        @Override
                        public void onResponse(@NonNull Call<FixtureDetailResponse> call, @NonNull Response<FixtureDetailResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.e("API_RESPONSE", "Raw JSON: " + new Gson().toJson(response.body()));
                                FixtureDetailResponse teamResponse = response.body();
                                if (teamResponse.data != null) {
                                    FixtureDetailModel fixtureDetailModel = teamResponse.getData();
                                    Log.e("API_RESPONSE", "data: " + fixtureDetailModel);
                                    Log.e("API_RESPONSE", "state: " + fixtureDetailModel.state);
                                    Log.e("call_api_data", "call true:");
                                    if (!fixtureDetailModel.scores.isEmpty()) {
                                        for (ScoreModel score : fixtureDetailModel.scores)
                                            Log.e("API_RESPONSE", "score: " + score);
                                    }
                                    if (!fixtureDetailModel.periods.isEmpty()) {
                                        for (PeriodModel periodModel : fixtureDetailModel.periods)
                                            Log.e("API_RESPONSE", "periods: " + periodModel);
                                        if (fixtureDetailModel.periods.size() == 2) {
                                            if (fixtureDetailModel.periods.get(1).minutes >= 90 && !fixtureDetailModel.periods.get(1).has_timer ||
                                                    fixtureDetailModel.getState().short_name.equals("FT") || fixtureDetailModel.getState().short_name.equals("AET")) {
                                                fixtureModel.isAlarm = false;
                                                fixtureModel.isPin = false;
                                            }
                                        }
                                    }
                                    if (!fixtureDetailModel.participants.isEmpty()) {
                                        for (TeamInMatch team : fixtureDetailModel.participants)
                                            Log.e("API_RESPONSE", "participants: " + team);
                                    }

                                    if (fixtureModel.isPin) {
                                        showView(fixtureDetailModel);
                                    }
                                    if (fixtureModel.isAlarm) {
                                        sendNotificationMatch(fixtureDetailModel, fixtureModel);
                                        if (!fixtureDetailModel.events.isEmpty()) {
                                            if (mapEvent.containsKey(fixtureModel.id)) {
                                                List<EventDetail> result = new ArrayList<>();
                                                Set<String> idsInList1 = new HashSet<>();

                                                for (EventDetail item : Objects.requireNonNull(mapEvent.get(fixtureModel.id))) {
                                                    idsInList1.add(item.id);
                                                }
                                                for (EventDetail item : fixtureDetailModel.events) {
                                                    if (!idsInList1.contains(item.id)) {
                                                        result.add(item);
                                                    }
                                                }
                                                sendNotificationEvent(result, fixtureModel);
                                                for (EventDetail eventDetail : result)
                                                    Log.e("API_RESPONSE", "events: " + eventDetail);
                                                mapEvent.put(fixtureModel.id, fixtureDetailModel.events);
                                            } else {
                                                mapEvent.put(fixtureModel.id, fixtureDetailModel.events);
                                                sendNotificationEvent(fixtureDetailModel.events, fixtureModel);
                                            }
                                        }
                                    }
                                    if (fixtureRunnables.get(fixtureModel.id) != null && floatingView != null && fixtureModel.isPin || fixtureModel.isAlarm) {
                                        handler.postDelayed(Objects.requireNonNull(fixtureRunnables.get(fixtureModel.id)), 60000);
                                    }

                                } else {
                                    Log.e("call_api_data", "data null");
                                    if (fixtureRunnables.get(fixtureModel.id) != null && floatingView != null && fixtureModel.isPin || fixtureModel.isAlarm) {
                                        handler.postDelayed(Objects.requireNonNull(fixtureRunnables.get(fixtureModel.id)), 60000);
                                    }
                                }
                            } else {
                                if (fixtureRunnables.get(fixtureModel.id) != null && floatingView != null && fixtureModel.isPin || fixtureModel.isAlarm) {
                                    handler.postDelayed(Objects.requireNonNull(fixtureRunnables.get(fixtureModel.id)), 60000);
                                }
                                Log.e("call_api_data", "call false: Code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<FixtureDetailResponse> call, @NonNull Throwable t) {
                            Log.e("call_api_data", "onfailure" + t);
                            if (fixtureRunnables.get(fixtureModel.id) != null && floatingView != null && fixtureModel.isPin || fixtureModel.isAlarm) {
                                handler.postDelayed(Objects.requireNonNull(fixtureRunnables.get(fixtureModel.id)), 60000);
                            }
                        }
                    });

        } catch (Exception e) {
            Log.e("call_api_data", "catch: ", e);
            if (fixtureRunnables.get(fixtureModel.id) != null && floatingView != null && fixtureModel.isPin || fixtureModel.isAlarm) {
                handler.postDelayed(Objects.requireNonNull(fixtureRunnables.get(fixtureModel.id)), 60000);
            }
        }
    }

    private void sendNotificationEvent(List<EventDetail> eventDetailList, FixtureModel fixtureModel) {
        if (!eventDetailList.isEmpty()) {
            for (EventDetail eventDetail : eventDetailList) {
                if (eventDetail.getType().developer_name.equals("PENALTY") || eventDetail.getType().developer_name.equals("GOAL") || eventDetail.getType().developer_name.equals("OWNGOAL") || eventDetail.getType().developer_name.equals("PENALTY_SHOOTOUT_GOAL")) {
                    if (fixtureModel.goals)
                        send(fixtureModel.name, "GOAL " + eventDetail.player_name);
                } else if (eventDetail.getType().developer_name.equals("YELLOWREDCARD")) {
                    if (fixtureModel.red_card)
                        send(fixtureModel.name, "YELLOWREDCARD " + eventDetail.player_name);
                } else if (eventDetail.getType().developer_name.equals("REDCARD")) {
                    if (fixtureModel.red_card)
                        send(fixtureModel.name, "REDCARD " + eventDetail.player_name);
                }
            }
        }
    }

    private void sendNotificationMatch(FixtureDetailModel fixtureDetailModel, FixtureModel fixtureModel) {
        if (!fixtureDetailModel.periods.isEmpty()) {
            if (fixtureModel.start_match) {
                if (fixtureDetailModel.periods.size() == 1) {
                    if (fixtureDetailModel.periods.get(0).has_timer && !fixtureModel.is_send_start_match) {
                        send(fixtureModel.name, getString(R.string.start_first_half));
                        fixtureModel.is_send_start_match = true;
                    }
                }
            }
            if (fixtureModel.end_first_half) {
                if (fixtureDetailModel.periods.size() == 1) {
                    if (fixtureDetailModel.periods.get(0).minutes > 1 && !fixtureDetailModel.periods.get(0).has_timer && !fixtureModel.is_send_end_first_half) {
                        send(fixtureModel.name, getString(R.string.at_the_end_of_first_half_match));
                        fixtureModel.is_send_end_first_half = true;
                    }
                }
            }
            if (fixtureModel.start_second_half) {
                if (fixtureDetailModel.periods.size() == 2) {
                    if (fixtureDetailModel.periods.get(1).minutes > 45 && fixtureDetailModel.periods.get(1).has_timer && !fixtureModel.is_send_start_second_half) {
                        send(fixtureModel.name, getString(R.string.at_the_start_of_second_half_match));
                        fixtureModel.is_send_start_second_half = true;
                    }
                }
            }
            if (fixtureModel.end_match) {
                if (fixtureDetailModel.periods.size() == 2) {
                    if (fixtureDetailModel.periods.get(1).minutes >= 90 && !fixtureDetailModel.periods.get(1).has_timer && !fixtureModel.is_send_end_match ||
                            fixtureDetailModel.getState().short_name.equals("FT") && !fixtureModel.is_send_end_match) {
                        send(fixtureModel.name, getString(R.string.end_match));
                        fixtureModel.is_send_end_match = true;
                    }
                }
            }
        }

    }

    private void send(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID_GENERAL)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.img_logo)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .build();

        notificationManager.notify((int) (System.currentTimeMillis() % 1000000000), notification);
    }

    @SuppressLint("SetTextI18n")
    private void showView(FixtureDetailModel fixtureDetailModel) {
        if (floatingView == null) return;
        if (fixtureDetailModel.participants.size() >= 2) {
            if (fixtureDetailModel.participants.get(0).getMeta().location.equals("home")) {
                Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(floatingBinding.ivHome);
                Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(floatingBinding.ivAway);
            } else {
                Glide.with(this).load(fixtureDetailModel.participants.get(1).getImage_path()).into(floatingBinding.ivHome);
                Glide.with(this).load(fixtureDetailModel.participants.get(0).getImage_path()).into(floatingBinding.ivAway);
            }
        }
        floatingBinding.tvStatus.setText("(" + fixtureDetailModel.getState().short_name + ")");
        if (fixtureDetailModel.periods.isEmpty()) {
            floatingBinding.tvTime.setText("");
        } else
            floatingBinding.tvTime.setText(fixtureDetailModel.periods.get(fixtureDetailModel.periods.size() - 1).minutes + "'");
        if (!fixtureDetailModel.scores.isEmpty()) {
            int scoreHome = 0, scoreAway = 0;
            for (ScoreModel scoreModel : fixtureDetailModel.scores) {
                if (scoreModel.description.equals("CURRENT")) {
                    if (scoreModel.getScore().participant.equals("home")) {
                        scoreHome = scoreModel.getScore().goals;
                    }
                    if (scoreModel.getScore().participant.equals("away")) {
                        scoreAway = scoreModel.getScore().goals;
                    }
                }
            }
            floatingBinding.tvHome.setText(String.valueOf(scoreHome));
            floatingBinding.tvAway.setText(String.valueOf(scoreAway));
        }
    }

    public void addFloatingPin() {
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 0;
        params.y = 100;

        floatingBinding = LayoutPinMatchBinding.inflate(LayoutInflater.from(this));
        floatingView = floatingBinding.getRoot();
        Rect deleteRect = new Rect((screenWidth - dpToPx(68)) / 2, screenHeight - dpToPx(148), (screenWidth + dpToPx(68)) / 2, screenHeight - dpToPx(80));
        // Xử lý sự kiện chạm kéo
        floatingView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        isMoving = false;
                        isDelete = false;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (Math.abs((event.getRawX() - initialTouchX)) > 25f || Math.abs((event.getRawY() - initialTouchY)) > 25f) {
                            isMoving = true;
                            Log.e("check_service", "move");
                            if (deleteView != null) {
                                deleteBinding.main.setVisibility(VISIBLE);
                                if (deleteRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                                    isDelete = true;
                                    floatingView.setBackgroundResource(R.drawable.bg_pin_delete);
                                } else {
                                    isDelete = false;
                                    floatingView.setBackgroundResource(R.drawable.bg_pin);
                                }
                            }
                        }
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingView, params);
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (isDelete) {
                            if (floatingView != null) {
                                windowManager.removeView(floatingView);
                                floatingView = null;
                            }
                            if (deleteView != null) {
                                windowManager.removeView(deleteView);
                                deleteView = null;
                            }
                        }
                        if (!isMoving) {
                            onFloatingIconClick();
                        }
                        if (floatingView != null) {
                            updatePositionAfterMove(floatingView, windowManager, params);
                        }
                        if (deleteView != null) {
                            deleteBinding.main.setVisibility(GONE);
                        }
                        return true;
                }
                return false;
            }
        });
        windowManager.addView(floatingView, params);
    }

    public void addDeletePin() {
        paramsDelete = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        paramsDelete.gravity = Gravity.TOP | Gravity.START;
        paramsDelete.x = (screenWidth - dpToPx(68)) / 2;
        paramsDelete.y = screenHeight - dpToPx(148);
        deleteBinding = LayoutCancelPinBinding.inflate(LayoutInflater.from(this));
        deleteView = deleteBinding.getRoot();
        deleteBinding.main.setVisibility(GONE);
        windowManager.addView(deleteView, paramsDelete);
    }

    public int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }

    private Notification createNotification() {
        SystemUtil.setLocale(this);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_SERVICE)
                .setContentTitle(getString(R.string.asisitive_touch_s_service_is_running))
                .setContentText(getString(R.string.tap_to_open))
                .setSmallIcon(R.drawable.img_logo)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW);
        return builder.build();
    }

    @SuppressLint({"RestrictedApi", "ObjectAnimatorBinding"})
    private void smoothMoveView(
            View view,
            WindowManager windowManager,
            WindowManager.LayoutParams params,
            int targetX,
            int targetY
    ) {
        ValueAnimator animatorX = ValueAnimator.ofInt(params.x, targetX);
        animatorX.setDuration(300);
        animatorX.setInterpolator(new DecelerateInterpolator());
        animatorX.addUpdateListener(animation -> {
            params.x = (int) animation.getAnimatedValue();
            windowManager.updateViewLayout(view, params);
        });

        ValueAnimator animatorY = ValueAnimator.ofInt(params.y, targetY);
        animatorY.setDuration(300);
        animatorY.setInterpolator(new DecelerateInterpolator());
        animatorY.addUpdateListener(animation -> {
            params.y = (int) animation.getAnimatedValue();
            windowManager.updateViewLayout(view, params);
        });

        animatorX.start();
        animatorY.start();
    }

    public void updatePositionAfterMove(
            View view,
            WindowManager windowManager,
            WindowManager.LayoutParams params
    ) {
        int centerX = params.x + (view.getWidth() / 2);
        int centerY = params.y + (view.getHeight() / 2);

        int targetX;
        int targetY;

        if (centerX > screenWidth / 2 && centerY > screenHeight / 2) { // Bottom-right quadrant
            if (screenHeight - centerY <= screenWidth - centerX) {
                targetX = Math.min(params.x, screenWidth);
                targetY = screenHeight - view.getHeight() - view.getHeight() / 2;
            } else {
                targetX = screenWidth - view.getWidth();
                targetY = Math.min(params.y, screenHeight);
            }
        } else if (centerX > screenWidth / 2 && centerY <= screenHeight / 2) { //top-right
            if (screenWidth - centerX <= centerY) {
                targetX = screenWidth - view.getWidth();
                targetY = Math.max(params.y, 0);
            } else {
                targetX = Math.min(params.x, screenWidth);
                targetY = 0;
            }
        } else if (centerX <= screenWidth / 2 && centerY > screenHeight / 2) { //bottom-lèt
            if (screenHeight - centerY <= centerX) {
                targetX = Math.max(params.x, 0);
                targetY = screenHeight - view.getHeight() - view.getHeight() / 2;
            } else {
                targetX = 0;
                targetY = Math.min(params.y, screenHeight);
            }
        } else { //top-le
            if (centerX <= centerY) {
                targetX = 0;
                targetY = Math.max(params.y, 0);
            } else {
                targetX = Math.max(params.x, 0);
                targetY = 0;
            }
        }

        smoothMoveView(view, windowManager, params, targetX, targetY);
    }
}
