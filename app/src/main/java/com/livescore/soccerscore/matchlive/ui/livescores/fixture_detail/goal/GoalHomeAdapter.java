package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.goal;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livescore.soccerscore.matchlive.databinding.ItemGoalHomeBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.EventDetail;

import java.util.List;

public class GoalHomeAdapter extends RecyclerView.Adapter<GoalHomeAdapter.GoalHomeViewHolder> {
    Context context;
    List<EventDetail> list;
    long homeId;

    public GoalHomeAdapter(Context context, long homeId, List<EventDetail> list) {
        this.context = context;
        this.list = list;
        this.homeId = homeId;
    }

    @NonNull
    @Override
    public GoalHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GoalHomeViewHolder(ItemGoalHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GoalHomeViewHolder holder, int position) {
        EventDetail eventDetail = list.get(position);
        String minute = String.valueOf(eventDetail.minute) + "'";
        holder.binding.tvMinute.setText(minute);
        holder.binding.tvPlayer.setText(eventDetail.player_name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class GoalHomeViewHolder extends RecyclerView.ViewHolder {
        ItemGoalHomeBinding binding;

        public GoalHomeViewHolder(ItemGoalHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
