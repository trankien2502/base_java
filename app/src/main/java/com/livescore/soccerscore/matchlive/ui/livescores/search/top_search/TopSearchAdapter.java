package com.livescore.soccerscore.matchlive.ui.livescores.search.top_search;

import static android.view.View.GONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livescore.soccerscore.matchlive.databinding.ItemSearchBinding;
import com.livescore.soccerscore.matchlive.model.search.TopSearchModel;

import java.util.List;

public class TopSearchAdapter extends RecyclerView.Adapter<TopSearchAdapter.TopSearchViewHolder> {
    Context context;
    List<TopSearchModel> list;
    TopSearchClickCallBack callBack;

    public TopSearchAdapter(Context context, List<TopSearchModel> list, TopSearchClickCallBack callBack) {
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
        TopSearchModel topSearchModel = list.get(position);
        holder.binding.ivPic.setImageResource(topSearchModel.image);
        holder.binding.tvName.setText(topSearchModel.name);
        holder.binding.layoutItem.setOnClickListener(v -> callBack.select(topSearchModel));
        holder.binding.tvLoadMore.setVisibility(GONE);
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
