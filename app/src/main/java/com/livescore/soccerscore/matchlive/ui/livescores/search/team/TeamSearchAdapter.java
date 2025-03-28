package com.livescore.soccerscore.matchlive.ui.livescores.search.team;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.livescore.soccerscore.matchlive.api_data.model.team.TeamModel;
import com.livescore.soccerscore.matchlive.databinding.ItemSearchBinding;

import java.util.List;

public class TeamSearchAdapter extends RecyclerView.Adapter<TeamSearchAdapter.TopSearchViewHolder> {
    Context context;
    List<TeamModel> list;
    TeamSearchClickCallBack callBack;

    public TeamSearchAdapter(Context context, List<TeamModel> list, TeamSearchClickCallBack callBack) {
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
        TeamModel teamModel = list.get(position);
        Glide.with(context).load(teamModel.getImage_path()).into(holder.binding.ivPic);
        holder.binding.tvName.setText(teamModel.getName());
        holder.binding.layoutItem.setOnClickListener(v -> callBack.select(teamModel));
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
