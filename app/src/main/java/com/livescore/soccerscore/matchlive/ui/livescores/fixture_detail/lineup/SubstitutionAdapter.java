package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.lineup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livescore.soccerscore.matchlive.databinding.ItemSubstitutionBinding;
import com.livescore.soccerscore.matchlive.model.fixture.timeline.EventDetail;

import java.util.List;

public class SubstitutionAdapter extends RecyclerView.Adapter<SubstitutionAdapter.SubstitutionViewHolder> {
    Context context;
    List<EventDetail> list;

    public SubstitutionAdapter(Context context, List<EventDetail> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SubstitutionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubstitutionViewHolder(ItemSubstitutionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SubstitutionViewHolder holder, int position) {
        EventDetail eventDetail = list.get(position);
        holder.binding.tvMinute.setText(eventDetail.minute + "'");
        holder.binding.tvIn.setText(eventDetail.player_name);
        holder.binding.tvOut.setText(eventDetail.related_player_name);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SubstitutionViewHolder extends RecyclerView.ViewHolder {
        ItemSubstitutionBinding binding;

        public SubstitutionViewHolder(ItemSubstitutionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
