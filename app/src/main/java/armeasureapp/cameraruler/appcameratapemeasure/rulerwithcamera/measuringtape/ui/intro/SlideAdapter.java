package armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.ui.intro;

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

import armeasureapp.cameraruler.appcameratapemeasure.rulerwithcamera.measuringtape.R;

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
