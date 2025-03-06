package com.assistivetouch.easytouch.homebutton.ui.home.touch.icon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.databinding.ItemIconBinding;

import java.util.List;

public class IconStyleAdapter extends RecyclerView.Adapter<IconStyleAdapter.IconStyleViewHolder> {

    List<IconStyle> iconStyleList;
    Context context;

    IconStyleCallBack iconStyleCallBack;

    public IconStyleAdapter(Context context, List<IconStyle> iconStyleList, IconStyleCallBack iconStyleCallBack) {
        this.iconStyleList = iconStyleList;
        this.context = context;
        this.iconStyleCallBack = iconStyleCallBack;
    }

    @NonNull
    @Override
    public IconStyleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIconBinding binding = ItemIconBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new IconStyleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IconStyleViewHolder holder, int position) {
        IconStyle iconStyle = iconStyleList.get(position);
        if (iconStyle.isSelect())
            holder.binding.layoutItem.setBackgroundResource(R.drawable.bg_item_icon_s);
        else holder.binding.layoutItem.setBackgroundResource(R.drawable.bg_item_icon_sn);
        holder.binding.ivItemIcon.setImageResource(iconStyle.getSource());
        holder.binding.layoutItem.setOnClickListener(v -> {
            setCheckIcon(iconStyle);
            iconStyleCallBack.select(iconStyle);
        });
    }

    @Override
    public int getItemCount() {
        return iconStyleList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCheckIcon(IconStyle icon) {
        for (IconStyle iconStyle : iconStyleList)
            iconStyle.setSelect(iconStyle.getSource() == icon.getSource());
        notifyDataSetChanged();
    }

    public static class IconStyleViewHolder extends RecyclerView.ViewHolder {

        ItemIconBinding binding;

        public IconStyleViewHolder(ItemIconBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
