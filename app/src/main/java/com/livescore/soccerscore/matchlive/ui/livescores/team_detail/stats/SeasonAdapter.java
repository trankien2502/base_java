package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.stats;

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
import com.livescore.soccerscore.matchlive.databinding.ItemSeasonBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats.SeasonDetail;

import java.util.List;

public class SeasonAdapter extends RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder> {
    Context context;
    List<SeasonDetail> list;
    SeasonClickCallBack callBack;

    public SeasonAdapter(Context context, List<SeasonDetail> list, SeasonClickCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public SeasonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SeasonViewHolder(ItemSeasonBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SeasonViewHolder holder, int position) {
        SeasonDetail seasonDetail = list.get(position);
        if (seasonDetail.isSelect) {
            holder.binding.ivTick.setVisibility(VISIBLE);
            holder.binding.main.setBackgroundResource(R.drawable.bg_item_season);
        } else {
            holder.binding.ivTick.setVisibility(GONE);
            holder.binding.main.setBackgroundResource(0);
        }
        Glide.with(context).load(seasonDetail.getLeague().image_path).into(holder.binding.ivLeague);
        holder.binding.tvLeague.setText(seasonDetail.getLeague().name + " " + seasonDetail.name);
        holder.binding.main.setOnClickListener(v -> {
            setCheck(seasonDetail);
            callBack.select(seasonDetail);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCheck(SeasonDetail seasonDetail) {
        for (SeasonDetail seasonDetail1 : list) {
            seasonDetail1.isSelect = seasonDetail.id == seasonDetail1.id;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SeasonViewHolder extends RecyclerView.ViewHolder {
        ItemSeasonBinding binding;

        public SeasonViewHolder(ItemSeasonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
