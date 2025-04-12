package com.livescore.soccerscore.matchlive.ui.livescores.home;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.model.ScoreModel;
import com.livescore.soccerscore.matchlive.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.databinding.ItemFixtureHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureViewHolder> {
    Context context;
    List<FixtureModel> list;
    FixtureClickCallBack callBack;
    int leagueTodayPos = -1;

    public FixtureAdapter(Context context, List<FixtureModel> list, FixtureClickCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    public FixtureAdapter(Context context, List<FixtureModel> list, FixtureClickCallBack callBack, int leagueTodayPos) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
        this.leagueTodayPos = leagueTodayPos;
    }


    @NonNull
    @Override
    public FixtureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFixtureHomeBinding binding = ItemFixtureHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FixtureViewHolder(binding);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull FixtureViewHolder holder, int position) {
        FixtureModel fixtureModel = list.get(position);
        if (fixtureModel.participants.get(0).getMeta().location.equals("home")) {
            Glide.with(context).load(fixtureModel.participants.get(0).getImage_path()).into(holder.binding.ivTeamHome);
            Glide.with(context).load(fixtureModel.participants.get(1).getImage_path()).into(holder.binding.ivTeamAway);
            holder.binding.tvTeamHome.setText(fixtureModel.participants.get(0).getName());
            holder.binding.tvTeamAway.setText(fixtureModel.participants.get(1).getName());
        } else {
            Glide.with(context).load(fixtureModel.participants.get(1).getImage_path()).into(holder.binding.ivTeamHome);
            Glide.with(context).load(fixtureModel.participants.get(0).getImage_path()).into(holder.binding.ivTeamAway);
            holder.binding.tvTeamHome.setText(fixtureModel.participants.get(1).getName());
            holder.binding.tvTeamAway.setText(fixtureModel.participants.get(0).getName());
        }
        holder.binding.tvState.setText(fixtureModel.getState().short_name);
        if (fixtureModel.isAlarm) holder.binding.ivAlarm.setImageResource(R.drawable.alarm_s);
        else holder.binding.ivAlarm.setImageResource(R.drawable.alarm_sn);
        if (fixtureModel.isPin) holder.binding.ivPin.setImageResource(R.drawable.pin_s);
        else holder.binding.ivPin.setImageResource(R.drawable.pin_sn);
        try {

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(fixtureModel.starting_at);
            assert date != null;
            String timeText = timeFormat.format(date);
            String dateText = dateFormat.format(date);

            holder.binding.tvTimeHour.setText(timeText);
            holder.binding.tvTimeDay.setText(dateText);
            long diffMillis = date.getTime() - System.currentTimeMillis();

            if (diffMillis <= 0) {
                if (!fixtureModel.scores.isEmpty()) {
                    holder.binding.llScore.setVisibility(VISIBLE);
                    holder.binding.llPin.setVisibility(GONE);
                    for (ScoreModel scoreModel : fixtureModel.scores) {
                        if (scoreModel.description.equals("CURRENT")) {
                            if (scoreModel.getScore().participant.equals("home")) {
                                holder.binding.tvHomeScore.setText("" + scoreModel.getScore().goals);
                            }
                            if (scoreModel.getScore().participant.equals("away")) {
                                holder.binding.tvAwayScore.setText("" + scoreModel.getScore().goals);
                            }
                        }
                    }
                } else {
                    holder.binding.llScore.setVisibility(GONE);
                    holder.binding.llPin.setVisibility(GONE);
                }
            } else {
                holder.binding.llScore.setVisibility(GONE);
                holder.binding.llPin.setVisibility(VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.binding.layoutItem.setOnClickListener(v -> callBack.select(position, fixtureModel));
        holder.binding.ivAlarm.setOnClickListener(v -> {
            if (leagueTodayPos == -1)
                callBack.alarm(position, fixtureModel);
            else callBack.alarm(leagueTodayPos, fixtureModel);
        });
        holder.binding.ivPin.setOnClickListener(v -> {
            if (leagueTodayPos == -1)
                callBack.pin(position, fixtureModel);
            else callBack.pin(leagueTodayPos, fixtureModel);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class FixtureViewHolder extends RecyclerView.ViewHolder {
        ItemFixtureHomeBinding binding;

        public FixtureViewHolder(ItemFixtureHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
