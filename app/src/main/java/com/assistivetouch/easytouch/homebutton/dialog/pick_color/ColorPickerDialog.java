package com.assistivetouch.easytouch.homebutton.dialog.pick_color;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.widget.SeekBar;

import androidx.annotation.NonNull;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.assistivetouch.easytouch.homebutton.R;
import com.assistivetouch.easytouch.homebutton.base.BaseDialog;
import com.assistivetouch.easytouch.homebutton.databinding.DialogColorPickerBinding;


public class ColorPickerDialog extends BaseDialog<DialogColorPickerBinding> {

    Context context;
    ColorSelectCallBack colorSelectCallBack;
    Drawable drawableAlpha, drawableBright;

    public ColorPickerDialog(@NonNull Context context, boolean canAble) {
        super(context, canAble);
        this.context = context;
    }

    @Override
    protected DialogColorPickerBinding setBinding() {
        return DialogColorPickerBinding.inflate(getLayoutInflater());
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        binding.pk.post(() -> {
            binding.pk.attachAlphaSlider(binding.alphaSlideBar);
            binding.pk.attachBrightnessSlider(binding.brightnessSlideBar);
            binding.tvColor.setText("#" + binding.pk.getColorEnvelope().getHexCode());
            binding.ivColor.setBackgroundColor(binding.pk.getColorEnvelope().getColor());
            binding.pk.setColorListener(new ColorEnvelopeListener() {
                @SuppressLint("UseCompatLoadingForDrawables")
                @Override
                public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                    binding.tvColor.setText("#" + envelope.getHexCode());
                    binding.ivColor.setBackgroundColor(envelope.getColor());

                    int selectedColor = envelope.getColor();

                    Drawable backgroundDrawable = getContext().getResources().getDrawable(R.drawable.thumb);
                    backgroundDrawable.setColorFilter(selectedColor, PorterDuff.Mode.SRC_IN);
                    Drawable foregroundDrawable = getContext().getResources().getDrawable(R.drawable.thumb2);

                    Drawable[] layers = new Drawable[2];
                    layers[0] = backgroundDrawable;
                    layers[1] = foregroundDrawable;

                    LayerDrawable layerDrawable = new LayerDrawable(layers);

                    binding.pk.setSelectorDrawable(layerDrawable);
                }
            });
        });
        drawableAlpha = binding.sbAlpha.getThumb();
        drawableAlpha.setAlpha(0);
        drawableBright = binding.sbBright.getThumb();
        drawableBright.setAlpha(0);
        binding.alphaSlideBar.post(() -> {
            Log.e("sizecheck", "alpha: " + binding.alphaSlideBar.getMeasuredWidth());
            binding.sbAlpha.setMax(binding.alphaSlideBar.getMeasuredWidth());
        });
        binding.brightnessSlideBar.post(() -> {
            Log.e("sizecheck", "bright: " + binding.brightnessSlideBar.getMeasuredWidth());
            binding.sbBright.setMax(binding.brightnessSlideBar.getMeasuredWidth());
        });


    }

    @Override
    protected void bindView() {
        binding.sbAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    drawableAlpha.setAlpha(255);
                    binding.alphaSlideBar.updateSelectorX(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                drawableAlpha.setAlpha(255);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.sbBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    drawableBright.setAlpha(255);
                    int x = binding.sbBright.getMax() - i;
                    binding.brightnessSlideBar.updateSelectorX(x);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                drawableBright.setAlpha(255);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.tvCancel.setOnClickListener(view -> dismiss());
        binding.tvOkay.setOnClickListener(view -> {
            if (colorSelectCallBack != null)
                colorSelectCallBack.select(binding.pk.getColorEnvelope().getColor());
            dismiss();
        });
    }

    public void init(ColorSelectCallBack colorSelectCallBack) {
        this.colorSelectCallBack = colorSelectCallBack;
    }
}
