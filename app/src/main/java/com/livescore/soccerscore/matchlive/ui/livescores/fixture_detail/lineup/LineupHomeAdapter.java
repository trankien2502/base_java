package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.lineup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.databinding.ItemLineupHomeBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.LineupDetail;

import java.util.List;

public class LineupHomeAdapter extends RecyclerView.Adapter<LineupHomeAdapter.LineupHomeViewHolder> {
    Context context;
    List<LineupDetail> list;

    public LineupHomeAdapter(Context context, List<LineupDetail> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LineupHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LineupHomeViewHolder(ItemLineupHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineupHomeViewHolder holder, int position) {
        LineupDetail lineupDetail = list.get(position);
        Glide.with(context).load(lineupDetail.getPlayer().image_path).into(holder.binding.ivHome);
        holder.binding.tvName.setText(lineupDetail.player_name);
        holder.binding.tvPosition.setText(lineupDetail.getPosition().name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LineupHomeViewHolder extends RecyclerView.ViewHolder {
        ItemLineupHomeBinding binding;

        public LineupHomeViewHolder(ItemLineupHomeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
