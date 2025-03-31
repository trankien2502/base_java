package com.livescore.soccerscore.matchlive.ui.livescores.team_detail.squad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.databinding.ItemSquadBinding;

import java.util.List;

public class SquadAdapter extends RecyclerView.Adapter<SquadAdapter.SquadViewHolder> {
    Context context;
    List<SquadModel> list;

    public SquadAdapter(Context context, List<SquadModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SquadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SquadViewHolder(ItemSquadBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SquadViewHolder holder, int position) {
        SquadModel squadModel = list.get(position);
        holder.binding.tvName.setSelected(true);
        if (squadModel != null) {
            if (squadModel.getPlayer() != null) {
                holder.binding.tvName.setText(squadModel.getPlayer().display_name);
                Glide.with(context).load(squadModel.getPlayer().image_path).into(holder.binding.ivSquad);
            }
            if (squadModel.getPosition() != null) {
                holder.binding.tvTeamAway.setText(squadModel.getPosition().name);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SquadViewHolder extends RecyclerView.ViewHolder {
        ItemSquadBinding binding;

        public SquadViewHolder(ItemSquadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
