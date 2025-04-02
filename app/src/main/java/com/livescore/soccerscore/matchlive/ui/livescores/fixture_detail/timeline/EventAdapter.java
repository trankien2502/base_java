package com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.timeline;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livescore.soccerscore.matchlive.R;
import com.livescore.soccerscore.matchlive.databinding.ItemEventBinding;
import com.livescore.soccerscore.matchlive.ui.livescores.fixture_detail.EventDetail;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    Context context;
    List<EventDetail> list;
    long homeId;

    public EventAdapter(Context context, List<EventDetail> list, long homeId) {
        this.context = context;
        this.list = list;
        this.homeId = homeId;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventViewHolder(ItemEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventDetail eventDetail = list.get(position);
        int sourceImageEvent;
        String player_name = eventDetail.player_name;
        String player_related = "";
        if (eventDetail.related_player_name != null)
            player_related = eventDetail.related_player_name;
        holder.binding.tvMinute.setText(eventDetail.minute + "'");
        if (eventDetail.getType().developer_name.equals("PENALTY") || eventDetail.getType().developer_name.equals("GOAL")) {
            sourceImageEvent = R.drawable.event_goal;
        } else if (eventDetail.getType().developer_name.equals("YELLOWCARD")) {
            sourceImageEvent = R.drawable.event_yellow_card;
        } else if (eventDetail.getType().developer_name.equals("SUBSTITUTION")) {
            sourceImageEvent = R.drawable.event_substitution;
        } else {
            sourceImageEvent = R.drawable.img_logo;
        }
        if (eventDetail.participant_id == homeId) {
            holder.binding.eventAway.setVisibility(GONE);
            holder.binding.eventHome.setVisibility(VISIBLE);
            holder.binding.ivHomeEvent.setImageResource(sourceImageEvent);
            holder.binding.tvHomePlayerName.setText(player_name);
            if (player_related.equals("null") || player_related.isEmpty()) {
                holder.binding.tvHomePlayerNameRelate.setText(player_related);
            } else holder.binding.tvHomePlayerNameRelate.setVisibility(GONE);
        } else {
            holder.binding.eventAway.setVisibility(VISIBLE);
            holder.binding.eventHome.setVisibility(GONE);
            holder.binding.ivAwayEvent.setImageResource(sourceImageEvent);
            holder.binding.tvAwayPlayerName.setText(player_name);
            if (player_related.equals("null") || player_related.isEmpty()) {
                holder.binding.tvAwayPlayerNameRelate.setText(player_related);
            } else holder.binding.tvAwayPlayerNameRelate.setVisibility(GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ItemEventBinding binding;

        public EventViewHolder(ItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
