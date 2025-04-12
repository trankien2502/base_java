package com.livescore.soccerscore.matchlive.ui.livescores.favourite;

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
import com.livescore.soccerscore.matchlive.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.databinding.ItemTeamBinding;

import java.util.List;

public class LeagueAdapter extends RecyclerView.Adapter<LeagueAdapter.TeamLeagueViewHolder> {
    LeagueClickCallBack teamClickCallBack;
    List<LeagueModel> list;
    Context context;
    boolean isFavourite;

    @NonNull
    @Override
    public TeamLeagueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeamBinding binding = ItemTeamBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TeamLeagueViewHolder(binding);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<LeagueModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public LeagueAdapter(Context context, List<LeagueModel> list, boolean isFavourite, LeagueClickCallBack teamClickCallBack) {
        this.teamClickCallBack = teamClickCallBack;
        this.context = context;
        this.list = list;
        this.isFavourite = isFavourite;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamLeagueViewHolder holder, int position) {
        LeagueModel teamModel = list.get(position);
        if (teamModel.isFavourite())
            holder.binding.ivFavourite.setImageResource(R.drawable.favorite_s);
        else holder.binding.ivFavourite.setImageResource(R.drawable.favorite_sn);
        holder.binding.tvName.setText(teamModel.getName());
        holder.binding.tvName.setSelected(true);
        Glide.with(context).load(teamModel.getImage_path()).error(R.drawable.img_logo).into(holder.binding.ivPic);
        holder.binding.layoutItem.setOnClickListener(v -> teamClickCallBack.select(teamModel));
        holder.binding.ivFavourite.setOnClickListener(v -> teamClickCallBack.follow(position, teamModel));
        if (isFavourite) {
            holder.binding.tvLoadMore.setVisibility(GONE);
        } else {
            if (position == list.size() - 1) {
                holder.binding.tvLoadMore.setVisibility(VISIBLE);
            } else {
                holder.binding.tvLoadMore.setVisibility(GONE);
            }
            holder.binding.tvLoadMore.setOnClickListener(v -> teamClickCallBack.load());
        }
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
