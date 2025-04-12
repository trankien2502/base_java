package com.livescore.soccerscore.matchlive.model.fixture;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.room.Entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.model.ScoreModel;
import com.livescore.soccerscore.matchlive.model.team.TeamInMatch;
import com.livescore.soccerscore.matchlive.service.ScheduleBroadcastReceiver;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Entity(tableName = "fixture")
public class FixtureModel extends FixtureBase implements Serializable {
    @SerializedName("participants")
    public List<TeamInMatch> participants;
    @SerializedName("scores")
    public List<ScoreModel> scores;
    @SerializedName("state")
    public Object state;
    @SerializedName("start_match")
    public boolean start_match;
    @SerializedName("end_first_half")
    public boolean end_first_half;
    @SerializedName("start_second_half")
    public boolean start_second_half;
    @SerializedName("goals")
    public boolean goals;
    @SerializedName("red_card")
    public boolean red_card;
    @SerializedName("end_match")
    public boolean end_match;
    @SerializedName("is_before_match")
    public boolean is_before_match;
    @SerializedName("before_match")
    public int before_match = 60;
    @SerializedName("is_send_end_match")
    public boolean is_send_end_match;
    @SerializedName("is_send_end_first_half")
    public boolean is_send_end_first_half;
    @SerializedName("is_send_start_match")
    public boolean is_send_start_match;
    @SerializedName("is_send_start_second_half")
    public boolean is_send_start_second_half;

    public FixtureModel() {
    }

    public StateModel getState() {
        Gson gson = new Gson();
        if (state != null) {
            return gson.fromJson(new Gson().toJson(state), StateModel.class);
        } else return new StateModel();

    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(parseDateToInt(0));
    }

    public void setState(StateModel stateModel) {
        this.state = stateModel;
    }


    @Override
    public String toString() {
        return "FixtureModel{" +
                "id=" + id +
                "date=" + getDate() +
                "season_id=" + season_id +
                "state_id=" + state_id +
                ", league_id=" + league_id +
                ", name='" + name + '\'' +
                ", starting_at='" + starting_at + '\'' +
                ", result_info='" + result_info + '\'' +
                ", has_odds=" + has_odds +
                ", length=" + length +
                ", isAlarm=" + isAlarm +
                ", isPin=" + isPin +
                ", is_before_match=" + is_before_match +
                ", before_match=" + before_match +
                ", start_match=" + start_match +
                ", end_first_half=" + end_first_half +
                ", start_second_half=" + start_second_half +
                ", goals=" + goals +
                ", red_card=" + red_card +
                ", end_match=" + end_match +
                ", participants=" + participants +
                ", scores=" + scores +
                ", state=" + getState() +
                '}';
    }

    @SuppressLint("ScheduleExactAlarm")
    public void schedule(Context context) {
        cancelNotification(context);
        try {
            long now = System.currentTimeMillis();
            long time = parseDateToInt(0);
            if (time < now) {
                Log.e("alarmcheck", "time is past:");
                return;
            }
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ScheduleBroadcastReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(context.getString(R.string.arg_alarm_obj), this);
            intent.putExtra(context.getString(R.string.bundle_alarm_obj), bundle);
            intent.putExtra("type", "ontime");
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (int) ((id + time) % 1000000000), intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            Log.e("alarmcheck", "schedule fm: " + this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        time,
                        alarmPendingIntent
                );
            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        time,
                        alarmPendingIntent
                );
            }
            if (is_before_match) {
                long timeBefore = parseDateToInt(before_match);
                if (timeBefore >= now) {
                    Intent intentBefore = new Intent(context, ScheduleBroadcastReceiver.class);
                    Bundle bundleBefore = new Bundle();
                    bundleBefore.putSerializable(context.getString(R.string.arg_alarm_obj), this);
                    intentBefore.putExtra(context.getString(R.string.bundle_alarm_obj), bundle);
                    intentBefore.putExtra("type", "early");
                    intentBefore.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                    PendingIntent alarmPendingIntentBefore = PendingIntent.getBroadcast(context, (int) ((id + timeBefore) % 1000000000), intentBefore, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                    Log.e("alarmcheck", "schedule fm before: " + this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                timeBefore,
                                alarmPendingIntentBefore
                        );
                    } else {
                        alarmManager.setExact(
                                AlarmManager.RTC_WAKEUP,
                                timeBefore,
                                alarmPendingIntentBefore
                        );
                    }
                } else {
                    Log.e("alarmcheck", "time before is past:");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class StateModel {
        @SerializedName("id")
        public long id;
        @SerializedName("name")
        public String name;
        @SerializedName("state")
        public String state;
        @SerializedName("short_name")
        public String short_name;

        @Override
        public String toString() {
            return "StateModel{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", short_name='" + short_name + '\'' +
                    ", state='" + state + '\'' +
                    '}';
        }
    }

    public long parseDateToInt(int timeBefore) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date targetDate = sdf.parse(starting_at);
            long targetMillis = targetDate.getTime() - (long) timeBefore * 60 * 1000;
            long now = System.currentTimeMillis();
            Log.d("alarmcheck", "Đã đặt báo thức vào " + date(targetMillis));
            return targetMillis;


        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void cancelNotification(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ScheduleBroadcastReceiver.class);
            PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, (int) ((id + parseDateToInt(0)) % 1000000000), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            alarmManager.cancel(alarmPendingIntent);
            if (is_before_match) {
                cancelNotificationBefore(context);
            }
            Log.e("alarmcheck", "cancel noti: " + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelNotificationBefore(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intentBefore = new Intent(context, ScheduleBroadcastReceiver.class);
            PendingIntent alarmPendingIntentBefore = PendingIntent.getBroadcast(context, (int) ((id + parseDateToInt(before_match)) % 1000000000), intentBefore, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            alarmManager.cancel(alarmPendingIntentBefore);
//            Toast.makeText(context, "cancel noti before: " + name, Toast.LENGTH_SHORT).show();
            Log.e("alarmcheck", "cancel noti before: " + name);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String date(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(new Date(millis));

        Log.d("TimeConvert", "Thời gian: " + formattedDate);
        return formattedDate;
    }

}
