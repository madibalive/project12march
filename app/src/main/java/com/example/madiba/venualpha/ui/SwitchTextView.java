package com.example.madiba.venualpha.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import com.example.madiba.venualpha.R;

/**
 * Created by Madiba on 3/12/2017.
 */

public class SwitchTextView extends AppCompatTextView {
    private static final int DEFAULT_ANIMATION_DURATION = 300;
    private static final float DEFAULT_DISABLED_ALPHA = .5f;

    private PorterDuffColorFilter colorFilter;
    private final ArgbEvaluator colorEvaluator = new ArgbEvaluator();
    private  long animationDuration;
    private  float disabledStateAlpha;
    private  int iconTintColor;
    private  int disabledStateColor;
    private float fraction = 0f;
    private boolean enabled;

    Drawable icon;

    public SwitchTextView(Context context) {
        super(context);
    }

    public SwitchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SwitchTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchTextView, 0, 0);

        try {
            iconTintColor = array.getColor(R.styleable.SwitchTextView_st_tint_color, Color.WHITE);
            animationDuration = array.getInteger(R.styleable.SwitchTextView_st_animation_duration, DEFAULT_ANIMATION_DURATION);
            disabledStateAlpha = array.getFloat(R.styleable.SwitchTextView_st_disabled_alpha, DEFAULT_DISABLED_ALPHA);
            disabledStateColor = array.getColor(R.styleable.SwitchTextView_st_disabled_color, iconTintColor);
            enabled = array.getBoolean(R.styleable.SwitchTextView_st_disabled_alpha, true);
        } finally {
            array.recycle();
        }

        if (disabledStateAlpha < 0f || disabledStateAlpha > 1f) {
            throw new IllegalArgumentException("Wrong value for si_disabled_alpha [" + disabledStateAlpha + "]. "
                    + "Must be value from range [0, 1]");
        }

        colorFilter = new PorterDuffColorFilter(iconTintColor, PorterDuff.Mode.SRC_IN);
        setFraction(enabled ? 0f : 1f);
    }

    public void setIconEnabled(boolean enabled) {
        setIconEnabled(enabled, true);
    }

    private void setDrawableTop(Drawable drawableTop){
        icon = drawableTop;
    }

    public void setIconEnabled(boolean enabled, boolean animate) {
        if (this.enabled == enabled) return;
        switchState(animate);
    }


    public void switchState(boolean animate) {
        float newFraction;
        if (enabled) {
            newFraction = 1f;
        } else {
            newFraction = 0f;
        }
        enabled = !enabled;
        if (animate) {
            animateToFraction(newFraction);
        } else {
            setFraction(newFraction);
            updateNumber(fraction);
            invalidate();
        }
    }

    private void animateToFraction(float toFraction) {
        ValueAnimator animator = ValueAnimator.ofFloat(fraction, toFraction);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setFraction((float) animation.getAnimatedValue());
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(animationDuration);
        animator.start();
    }

    private void setFraction(float fraction) {
        this.fraction = fraction;
        updateColor(fraction);
        updateAlpha(fraction);
        postInvalidateOnAnimationCompat();
    }

    private void updateNumber(float fraction) {
        int currentNum = Integer.parseInt(getText().toString());
        if (fraction==1f){
            currentNum++;
            setText(String.format("%d", currentNum));

        }else {
            if (currentNum!=0 ){
                currentNum--;
                setText(String.format("%d", currentNum));
            }
        }
    }


    private void updateColor(float fraction) {
        if (iconTintColor != disabledStateColor) {
            final int color = (int) colorEvaluator.evaluate(fraction, iconTintColor, disabledStateColor);
            updateIconColor(color);
        }
    }

    private void updateAlpha(float fraction) {
        int alpha = (int) ((disabledStateAlpha + (1f - fraction) * (1f - disabledStateAlpha)) * 255);
        updateIconAlpha(alpha);
    }

    private void updateIconColor(int color) {
        colorFilter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        icon.setColorFilter(colorFilter);
    }

    @SuppressWarnings("deprecation")
    private void updateIconAlpha(int alpha) {
        icon.setAlpha(alpha);
    }
    private void postInvalidateOnAnimationCompat() {
        final long fakeFrameTime = 10;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            postInvalidateOnAnimation();
        } else {
            postInvalidateDelayed(fakeFrameTime);
        }
    }



}
