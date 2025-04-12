package com.livescore.soccerscore.matchlive.ui.livescores.league_detail.league_table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livescore.soccerscore.matchlive.databinding.ItemStandingTableBinding;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingDetail;
import com.livescore.soccerscore.matchlive.model.league.detail.StandingModel;

import java.util.List;

public class StandingTableAdapter extends RecyclerView.Adapter<StandingTableAdapter.StandingViewHolder> {

    Context context;
    List<StandingModel> list;

    public StandingTableAdapter(Context context, List<StandingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StandingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StandingViewHolder(ItemStandingTableBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StandingViewHolder holder, int position) {
        StandingModel standingModel = list.get(position);
        holder.binding.tvClub.setSelected(true);
        holder.binding.tvRank.setText(String.valueOf(position + 1));
        holder.binding.tvClub.setText(standingModel.getParticipant().getName());
        holder.binding.tvPoint.setText(String.valueOf(standingModel.points));
        for (StandingDetail standingDetail : standingModel.details) {
            if (standingDetail.getType().developer_name.equals("OVERALL_MATCHES"))
                holder.binding.tvMP.setText(String.valueOf(standingDetail.value));
            if (standingDetail.getType().developer_name.equals("OVERALL_SCORED"))
                holder.binding.tvGF.setText(String.valueOf(standingDetail.value));
            if (standingDetail.getType().developer_name.equals("OVERALL_CONCEDED"))
                holder.binding.tvGA.setText(String.valueOf(standingDetail.value));
            if (standingDetail.getType().developer_name.equals("OVERALL_GOAL_DIFFERENCE"))
                holder.binding.tvGD.setText(String.valueOf(standingDetail.value));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StandingViewHolder extends RecyclerView.ViewHolder {
        ItemStandingTableBinding binding;

        public StandingViewHolder(ItemStandingTableBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
