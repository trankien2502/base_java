package com.assistivetouch.easytouch.homebutton.item;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.databinding.ItemFunctionBinding;

import java.util.List;

public class ItemFunctionIconAdapter extends RecyclerView.Adapter<ItemFunctionIconAdapter.ItemFunctionViewHolder> {

    List<ItemFunctionIcon> iconStyleList;
    Context context;

    ItemFunctionCallBack itemFunctionCallBack;
    boolean isSelectEnable;

    public ItemFunctionIconAdapter(Context context, List<ItemFunctionIcon> iconStyleList, boolean isSelectEnable, ItemFunctionCallBack itemFunctionCallBack) {
        this.iconStyleList = iconStyleList;
        this.context = context;
        this.itemFunctionCallBack = itemFunctionCallBack;
        this.isSelectEnable = isSelectEnable;
    }

    @NonNull
    @Override
    public ItemFunctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFunctionBinding binding = ItemFunctionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemFunctionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemFunctionViewHolder holder, int position) {
        ItemFunctionIcon functionIcon = iconStyleList.get(position);
        if (isSelectEnable) {
            if (functionIcon.isSelect())
                holder.binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_item_s);
            else holder.binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_item_sn);
        } else holder.binding.layoutItem.setBackgroundResource(R.drawable.bg_lang_item_sn);
        holder.binding.ivIcon.setImageResource(functionIcon.getIcon());
        holder.binding.tvFunction.setText(functionIcon.getText());
        holder.binding.layoutItem.setOnClickListener(v -> {
            if (isSelectEnable) setCheckIcon(functionIcon);
            itemFunctionCallBack.select(functionIcon);
        });
    }

    @Override
    public int getItemCount() {
        return iconStyleList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCheckIcon(ItemFunctionIcon icon) {
        for (ItemFunctionIcon functionIcon : iconStyleList)
            functionIcon.setSelect(functionIcon.getIcon() == icon.getIcon());
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCheckIcon(int i) {
        for (ItemFunctionIcon functionIcon : iconStyleList)
            functionIcon.setSelect(functionIcon.getIcon() == i);
        notifyDataSetChanged();
    }

    public static class ItemFunctionViewHolder extends RecyclerView.ViewHolder {

        ItemFunctionBinding binding;

        public ItemFunctionViewHolder(ItemFunctionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
