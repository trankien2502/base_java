package com.livescore.soccerscore.matchlive.ui.home.favourite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.databinding.ItemTeamBinding;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamLeagueViewHolder> {
    TeamClickCallBack teamClickCallBack;
    List<TeamModel> list;
    Context context;

    @NonNull
    @Override
    public TeamLeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeamBinding binding = ItemTeamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TeamLeagueViewHolder(binding);
    }

    public TeamAdapter(Context context, List<TeamModel> list, TeamClickCallBack teamClickCallBack) {
        this.teamClickCallBack = teamClickCallBack;
        this.context = context;
        this.list = list;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<TeamModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull TeamLeagueViewHolder holder, int position) {
        TeamModel teamModel = list.get(position);
        if (teamModel.isFavourite())
            holder.binding.ivFavourite.setImageResource(R.drawable.favorite_s);
        else holder.binding.ivFavourite.setImageResource(R.drawable.favorite_sn);
        holder.binding.tvName.setText(teamModel.getName());
        Glide.with(context).load(teamModel.getImage_path()).error(R.drawable.img_logo).into(holder.binding.ivPic);
        holder.binding.layoutItem.setOnClickListener(v -> teamClickCallBack.select(teamModel));
        holder.binding.ivFavourite.setOnClickListener(v -> teamClickCallBack.follow(teamModel));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TeamLeagueViewHolder extends RecyclerView.ViewHolder {
        ItemTeamBinding binding;

        public TeamLeagueViewHolder(ItemTeamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
