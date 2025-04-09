package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.lineup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.R;
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
        if (lineupDetail != null) {
            if (lineupDetail.getPlayer() != null && lineupDetail.getPlayer().image_path != null) {
                Glide.with(context)
                        .load(lineupDetail.getPlayer().image_path)
                        .into(holder.binding.ivHome);
            } else {
                // Load ảnh mặc định nếu null
                holder.binding.ivHome.setImageResource(R.drawable.img_no_result);
            }

            holder.binding.tvName.setText(
                    lineupDetail.player_name != null ? lineupDetail.player_name : context.getString(R.string.no_data));

            if (lineupDetail.getPosition() != null && lineupDetail.getPosition().name != null) {
                holder.binding.tvPosition.setText(lineupDetail.getPosition().name);
            } else {
                holder.binding.tvPosition.setText(context.getString(R.string.no_data));
            }
        }
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
