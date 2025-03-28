package com.livescore.soccerscore.matchlive.ui.livescores.live;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.ScoreModel;
import com.livescore.soccerscore.matchlive.api_data.model.fixture.FixtureModel;
import com.livescore.soccerscore.matchlive.databinding.ItemLiveMatchBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LiveMatchAdapter extends RecyclerView.Adapter<LiveMatchAdapter.LiveMatchViewHolder> {
    Context context;
    List<FixtureModel> list;
    LiveMatchClickCallBack callBack;

    @NonNull
    @Override
    public LiveMatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLiveMatchBinding binding = ItemLiveMatchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LiveMatchViewHolder(binding);
    }

    public LiveMatchAdapter(Context context, List<FixtureModel> list, LiveMatchClickCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull LiveMatchViewHolder holder, int position) {
        FixtureModel fixtureModel = list.get(position);
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
        holder.binding.tvStatus.setText(fixtureModel.getState().state);
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
        ItemLiveMatchBinding binding;

        public LiveMatchViewHolder(ItemLiveMatchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
