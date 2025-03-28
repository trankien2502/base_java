package com.livescore.soccerscore.matchlive.ui.livescores.search.recent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.livescore.soccerscore.matchlive.databinding.ItemRecentBinding;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;
    List<String> list;
    ItemRecentClickCallBack callBack;

    public SearchAdapter(Context context, List<String> list, ItemRecentClickCallBack callBack) {
        this.context = context;
        this.list = list;
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(ItemRecentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        String s = list.get(position);
        holder.binding.tvName.setText(s);
        holder.binding.layoutItem.setOnClickListener(v -> callBack.select(s));
        holder.binding.ivDelete.setOnClickListener(v -> callBack.delete(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        ItemRecentBinding binding;

        public SearchViewHolder(ItemRecentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
