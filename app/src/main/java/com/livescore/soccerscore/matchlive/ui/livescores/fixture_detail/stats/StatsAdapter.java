package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.stats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livescore.soccerscore.matchlive.databinding.ItemStatsBinding;
import com.livescore.soccerscore.matchlive.model.fixture.stats.StatsHomeAndAway;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {
    Context context;
    List<StatsHomeAndAway> list;

    public StatsAdapter(Context context, List<StatsHomeAndAway> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StatsViewHolder(ItemStatsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        StatsHomeAndAway stats = list.get(position);
        holder.binding.tvAway.setText(String.valueOf(stats.awayValue));
        holder.binding.tvHome.setText(String.valueOf(stats.homeValue));
        holder.binding.tvName.setText(stats.name);
        if (stats.homeValue == 0 && stats.awayValue == 0) {
            LinearLayout.LayoutParams paramsHome = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1

            );
            LinearLayout.LayoutParams paramsAway = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1

            );
            paramsHome.setMargins(0, 0, dpToPx(context, 4), 0);
            holder.binding.viewAway.setLayoutParams(paramsAway);
            holder.binding.viewHome.setLayoutParams(paramsHome);
        } else if (stats.homeValue == 0) {
            LinearLayout.LayoutParams paramsHome = new LinearLayout.LayoutParams(
                    dpToPx(context, 8),
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0

            );
            LinearLayout.LayoutParams paramsAway = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1

            );
            paramsHome.setMargins(0, 0, dpToPx(context, 4), 0);
            holder.binding.viewAway.setLayoutParams(paramsAway);
            holder.binding.viewHome.setLayoutParams(paramsHome);
        } else if (stats.awayValue == 0) {
            LinearLayout.LayoutParams paramsHome = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1

            );
            LinearLayout.LayoutParams paramsAway = new LinearLayout.LayoutParams(
                    dpToPx(context, 8),
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0

            );
            paramsHome.setMargins(0, 0, dpToPx(context, 4), 0);
            holder.binding.viewAway.setLayoutParams(paramsAway);
            holder.binding.viewHome.setLayoutParams(paramsHome);
        } else {
            LinearLayout.LayoutParams paramsHome = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    stats.homeValue

            );
            LinearLayout.LayoutParams paramsAway = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    stats.awayValue

            );
            paramsHome.setMargins(0, 0, dpToPx(context, 4), 0);
            holder.binding.viewAway.setLayoutParams(paramsAway);
            holder.binding.viewHome.setLayoutParams(paramsHome);
        }
    }

    public static int dpToPx(Context context, float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class StatsViewHolder extends RecyclerView.ViewHolder {
        ItemStatsBinding binding;

        public StatsViewHolder(ItemStatsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
