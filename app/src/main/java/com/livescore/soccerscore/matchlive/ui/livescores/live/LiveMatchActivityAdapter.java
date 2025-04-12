package com.livescore.soccerscore.matchlive.ui.livescores.live;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.model.ScoreModel;
import com.livescore.soccerscore.matchlive.databinding.ItemLiveMatchActivityBinding;
import com.livescore.soccerscore.matchlive.model.live.FixtureLiveModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LiveMatchActivityAdapter extends RecyclerView.Adapter<LiveMatchActivityAdapter.LiveMatchViewHolder> {
    Context context;
    List<FixtureLiveModel> list;
    LiveMatchClickCallBack callBack;

    @NonNull
    @Override
    public LiveMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLiveMatchActivityBinding binding = ItemLiveMatchActivityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveMatchViewHolder(binding);
    }

    public LiveMatchActivityAdapter(Context context, List<FixtureLiveModel> list, LiveMatchClickCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull LiveMatchViewHolder holder, int position) {
        FixtureLiveModel fixtureModel = list.get(position);
        if (fixtureModel.participants.get(0).getMeta().location.equals("home")) {
            Glide.with(context).load(fixtureModel.participants.get(0).getImage_path()).into(holder.binding.ivHome);
            Glide.with(context).load(fixtureModel.participants.get(1).getImage_path()).into(holder.binding.ivAway);
            holder.binding.tvHome.setText(fixtureModel.participants.get(0).getName());
            holder.binding.tvAway.setText(fixtureModel.participants.get(1).getName());
        } else {
            Glide.with(context).load(fixtureModel.participants.get(1).getImage_path()).into(holder.binding.ivHome);
            Glide.with(context).load(fixtureModel.participants.get(0).getImage_path()).into(holder.binding.ivAway);
            holder.binding.tvHome.setText(fixtureModel.participants.get(1).getName());
            holder.binding.tvAway.setText(fixtureModel.participants.get(0).getName());
        }
        holder.binding.tvHome.setSelected(true);
        holder.binding.tvAway.setSelected(true);
        holder.binding.tvStatus.setText("(" + fixtureModel.getState().short_name + ")");
        if (fixtureModel.periods.isEmpty()) {
            holder.binding.tvTime.setText("");
        } else
            holder.binding.tvTime.setText(fixtureModel.periods.get(fixtureModel.periods.size() - 1).minutes + "'");
        if (!fixtureModel.scores.isEmpty()) {
            int scoreHome = 0, scoreAway = 0;
            for (ScoreModel scoreModel : fixtureModel.scores) {
                if (scoreModel.description.equals("CURRENT")) {
                    if (scoreModel.getScore().participant.equals("home")) {
                        scoreHome = scoreModel.getScore().goals;
                    }
                    if (scoreModel.getScore().participant.equals("away")) {
                        scoreAway = scoreModel.getScore().goals;
                    }
                }
            }
            holder.binding.tvScore.setText(scoreHome + " - " + scoreAway);
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(fixtureModel.starting_at);
            assert date != null;
            String timeText = timeFormat.format(date);
            String dateText = dateFormat.format(date);
            holder.binding.tvDate.setText(dateText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.binding.btnDetail.setOnClickListener(v -> callBack.detail(fixtureModel));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class LiveMatchViewHolder extends RecyclerView.ViewHolder {
        ItemLiveMatchActivityBinding binding;

        public LiveMatchViewHolder(ItemLiveMatchActivityBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
