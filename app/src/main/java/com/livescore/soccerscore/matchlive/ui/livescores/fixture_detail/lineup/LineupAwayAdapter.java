package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.lineup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.databinding.ItemLineupAwayBinding;
import com.livescore.soccerscore.matchlive.databinding.ItemLineupHomeBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.LineupDetail;

import java.util.List;

public class LineupAwayAdapter extends RecyclerView.Adapter<LineupAwayAdapter.LineupHomeViewHolder> {
    Context context;
    List<LineupDetail> list;

    public LineupAwayAdapter(Context context, List<LineupDetail> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LineupHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LineupHomeViewHolder(ItemLineupAwayBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LineupHomeViewHolder holder, int position) {
        LineupDetail lineupDetail = list.get(position);
        Glide.with(context).load(lineupDetail.getPlayer().image_path).into(holder.binding.ivAway);
        holder.binding.tvName.setText(lineupDetail.player_name);
        holder.binding.tvPosition.setText(lineupDetail.getPosition().name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LineupHomeViewHolder extends RecyclerView.ViewHolder {
        ItemLineupAwayBinding binding;

        public LineupHomeViewHolder(ItemLineupAwayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
