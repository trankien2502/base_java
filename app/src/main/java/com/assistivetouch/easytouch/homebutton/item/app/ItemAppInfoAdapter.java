package com.assistivetouch.easytouch.homebutton.item.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.databinding.ItemAppInforBinding;
import com.assistivetouch.easytouch.homebutton.databinding.ItemAppInforBinding;
import com.bumptech.glide.Glide;

import java.util.List;

public class ItemAppInfoAdapter extends RecyclerView.Adapter<ItemAppInfoAdapter.ItemAppInforViewHolder> {

    List<ItemAppInfo> iconStyleList;
    Context context;

    ItemAppCallBack itemFunctionCallBack;

    public ItemAppInfoAdapter(Context context, List<ItemAppInfo> iconStyleList, ItemAppCallBack itemFunctionCallBack) {
        this.iconStyleList = iconStyleList;
        this.context = context;
        this.itemFunctionCallBack = itemFunctionCallBack;
    }

    @NonNull
    @Override
    public ItemAppInforViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppInforBinding binding = ItemAppInforBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemAppInforViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAppInforViewHolder holder, int position) {
        ItemAppInfo functionIcon = iconStyleList.get(position);
        Glide.with(context).load(functionIcon.getIcon()).into(holder.binding.ivItemIcon);
        holder.binding.tvNameApp.setText(functionIcon.getName());
        holder.binding.layoutItem.setOnClickListener(v -> {
            itemFunctionCallBack.select(functionIcon);
        });
    }

    @Override
    public int getItemCount() {
        return iconStyleList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCheckIcon(ItemAppInfo icon) {
//        for (ItemAppInfo functionIcon : iconStyleList)
//            functionIcon.setSelect(functionIcon.getIcon() == icon.getIcon());
//        notifyDataSetChanged();
    }

    public static class ItemAppInforViewHolder extends RecyclerView.ViewHolder {

        ItemAppInforBinding binding;

        public ItemAppInforViewHolder(ItemAppInforBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
