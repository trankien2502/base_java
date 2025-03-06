package com.assistivetouch.easytouch.homebutton.ui.intro;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.assistivetouch.easytouch.homebutton.R;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private List<Integer> images;
    private Context context;
    private Activity activity;
    private boolean isCheckAds;
    private boolean isLoadAds = false;
    private OnClickItem onClickItem;
    private int height;

    public SlideAdapter(Context context, Activity activity, List<Integer> images, boolean isCheckAds, int height, OnClickItem onClickItem) {
        this.context = context;
        this.activity = activity;
        this.images = images;
        this.isCheckAds = isCheckAds;
        this.onClickItem = onClickItem;
        this.height = height;
    }

    public void setList(List<Integer> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.intro_slide_layout, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.img.setImageResource(images.get(position));
        ViewGroup.LayoutParams layoutParams = holder.scrollView.getLayoutParams();
        layoutParams.height = height;
        holder.scrollView.setLayoutParams(layoutParams);
//        if (IsNetWork.haveNetworkConnection(context) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full && CheckAds.getInstance().isShowAds(context)) {
//            if (position == 2) {
//                holder.rlAds.setVisibility(View.VISIBLE);
//                holder.ivClose.setVisibility(View.VISIBLE);
//                holder.img.setVisibility(View.GONE);
//                loadAds(holder.rlAds);
//            } else {
//                holder.rlAds.setVisibility(View.GONE);
//                holder.ivClose.setVisibility(View.GONE);
//                holder.img.setVisibility(View.VISIBLE);
//            }
//        } else {
//
//        }
        holder.rlAds.setVisibility(View.GONE);
        holder.ivClose.setVisibility(View.GONE);
        holder.img.setVisibility(View.VISIBLE);
        holder.ivClose.setOnClickListener(view -> {
            onClickItem.onClickItem();
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

//    private void loadAds(RelativeLayout rlAds) {
//        new Thread(() -> {
//            try {
//                if (IsNetWork.haveNetworkConnection(context) && !ConstantIdAds.listIDAdsNativeIntroFull.isEmpty() && ConstantRemote.native_intro_full && CheckAds.getInstance().isShowAds(context)) {
//                    Admob.getInstance().loadNativeAd(context, ConstantIdAds.listIDAdsNativeIntroFull, new AdCallback() {
//                        @Override
//                        public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
//                            activity.runOnUiThread(() -> {
//                                @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(context).inflate(R.layout.layout_native_show_full, null);
//                                rlAds.removeAllViews();
//                                rlAds.addView(adView);
//                                Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
//                                CheckAds.checkAds(adView, CheckAds.IN);
//                                isLoadAds = true;
//                            });
//                        }
//
//                        @Override
//                        public void onAdFailedToLoad(@Nullable LoadAdError i) {
//                            activity.runOnUiThread(() -> rlAds.setVisibility(View.INVISIBLE));
//                        }
//                    });
//                } else {
//                    activity.runOnUiThread(() -> rlAds.setVisibility(View.INVISIBLE));
//                }
//
//            } catch (Exception e) {
//                activity.runOnUiThread(() -> rlAds.setVisibility(View.INVISIBLE));
//            }
//        }).start();
//    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView img, ivClose;
        RelativeLayout rlAds;
        NestedScrollView scrollView;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.im_LogoSlide);
            ivClose = itemView.findViewById(R.id.ivClose);
            rlAds = itemView.findViewById(R.id.nativeTemplate);
            scrollView = itemView.findViewById(R.id.scrollViewIntro);
        }
    }

    public interface OnClickItem {
        void onClickItem();
    }
}
