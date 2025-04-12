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
import com.livescore.soccerscore.matchlive.model.league.LeagueTodayModel;
import com.livescore.soccerscore.matchlive.databinding.ItemLeagueTodayBinding;

import java.util.List;

public class LeagueTodayAdapter extends RecyclerView.Adapter<LeagueTodayAdapter.LeaguaTodayViewHolder> {
    Context context;
    List<LeagueTodayModel> list;
    FixtureAdapter fixtureAdapter;
    LeagueHomeClickCallBack callBack;
    FixtureClickCallBack fixtureClickCallBack;

    public LeagueTodayAdapter(Context context, List<LeagueTodayModel> list, LeagueHomeClickCallBack callBack, FixtureClickCallBack fixtureClickCallBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
        this.fixtureClickCallBack = fixtureClickCallBack;
    }

    @NonNull
    @Override
    public LeaguaTodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLeagueTodayBinding binding = ItemLeagueTodayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new LeaguaTodayViewHolder(binding);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<LeagueTodayModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LeaguaTodayViewHolder holder, int position) {
        LeagueTodayModel leagueTodayModel = list.get(position);
        Glide.with(context).load(leagueTodayModel.getImage_path()).into(holder.binding.ivLeague);
        if (!leagueTodayModel.today.isEmpty()) {
            holder.binding.tvLeagueName.setText(leagueTodayModel.name + " (" + leagueTodayModel.today.size() + ")");
        }
        FixtureAdapter fixtureAdapter = new FixtureAdapter(context, leagueTodayModel.today, fixtureClickCallBack, position);
        holder.binding.rcvFixture.setAdapter(fixtureAdapter);
        holder.binding.ivHide.setOnClickListener(v -> {
            if (holder.binding.rcvFixture.getVisibility() == VISIBLE) {
                holder.binding.ivHide.setImageResource(R.drawable.expand_down);
                holder.binding.rcvFixture.setVisibility(GONE);
            } else {
                holder.binding.ivHide.setImageResource(R.drawable.expand_up);
                holder.binding.rcvFixture.setVisibility(VISIBLE);
            }
        });
        holder.binding.layoutItem.setOnClickListener(v -> callBack.select(leagueTodayModel));
        if (position == list.size() - 1) {
            holder.binding.tvLoadMore.setVisibility(VISIBLE);
        } else {
            holder.binding.tvLoadMore.setVisibility(GONE);
        }
        holder.binding.tvLoadMore.setOnClickListener(v -> {
            callBack.load();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class LeaguaTodayViewHolder extends RecyclerView.ViewHolder {
        ItemLeagueTodayBinding binding;

        public LeaguaTodayViewHolder(ItemLeagueTodayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
