package com.livescore.soccerscore.matchlive.ui.livescores.search.league;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.model.league.LeagueModel;
import com.livescore.soccerscore.matchlive.databinding.ItemSearchBinding;

import java.util.List;

public class LeagueSearchAdapter extends RecyclerView.Adapter<LeagueSearchAdapter.TopSearchViewHolder> {
    Context context;
    List<LeagueModel> list;
    LeagueSearchClickCallBack callBack;

    public LeagueSearchAdapter(Context context, List<LeagueModel> list, LeagueSearchClickCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public TopSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TopSearchViewHolder(ItemSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TopSearchViewHolder holder, int position) {
        LeagueModel leagueModel = list.get(position);
        Glide.with(context).load(leagueModel.getImage_path()).into(holder.binding.ivPic);
        holder.binding.tvName.setText(leagueModel.getName());
        holder.binding.layoutItem.setOnClickListener(v -> callBack.select(leagueModel));
        if (position == list.size() - 1) {
            holder.binding.tvLoadMore.setVisibility(VISIBLE);
        } else {
            holder.binding.tvLoadMore.setVisibility(GONE);
        }
        holder.binding.tvLoadMore.setOnClickListener(v -> callBack.load());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class TopSearchViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding binding;

        public TopSearchViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
