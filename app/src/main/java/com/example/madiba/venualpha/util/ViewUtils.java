package com.example.madiba.venualpha.util;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Madiba on 12/10/2016.
 */

public class ViewUtils {


    public static Point getScreenResolution(Context context) {
        // get window managers
        WindowManager manager =  (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);

        return point;
    }

    public static void fadeInView(@Nullable final View view) {
        fadeInView(view, 1, new ValueAnimator().getDuration(), 0);
    }

    public static void fadeInView(@Nullable final View view, long duration) {
        fadeInView(view, 1, duration, 0);
    }

    public static void fadeInView(@Nullable final View view, final float targetAlpha, final long duration, final long startDelay) {
        if (view == null) {
            return;
        }
        view.animate()
                .alpha(targetAlpha)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .withStartAction(() -> view.setVisibility(View.VISIBLE))
                .start();
    }

    public static void fadeOutView(@Nullable final View view) {
        fadeOutView(view, true);
    }

    public static void fadeOutView(@Nullable final View view, boolean setToGoneWithEndAction) {
        fadeOutView(view, new ValueAnimator().getDuration(), 0, setToGoneWithEndAction);
    }

    public static void fadeOutView(@Nullable final View view, final long duration) {
        fadeOutView(view, duration, 0);
    }

    public static void fadeOutView(@Nullable final View view, final long duration, final long startDelay) {
        fadeOutView(view, duration, startDelay, true);
    }

    public static void fadeOutView(@Nullable final View view, final long duration, final long startDelay, final boolean setToGoneWithEndAction) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
            return;
        }

        view.animate()
                .alpha(0)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .withEndAction(() -> view.setVisibility(setToGoneWithEndAction ? View.GONE : View.INVISIBLE))
                .start();
    }
    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();       //请求重绘
        }
    }



    public static void setMarginTop(View v, int topMargin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin = topMargin;
        v.invalidate();
    }
    public static void setMarginBottom(View v, int bottomMargin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).bottomMargin = bottomMargin;
        v.invalidate();
    }

    public static void setMarginLeft(View v, int leftMargin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).leftMargin = leftMargin;
        v.invalidate();
    }

    public static void setMarginRight(View v, int rightMargin) {
        ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).rightMargin = rightMargin;
        v.invalidate();
    }

    public static AlertDialog showAlertDialog(Context context,
                                              CharSequence title,
                                              CharSequence message,
                                              CharSequence button,
                                              DialogInterface.OnClickListener onClickListener,
                                              boolean cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(cancelable)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(button, onClickListener)
                .create();
        dialog.show();
        return dialog;
    }

    public static AlertDialog showAlertDialog(Context context,
                                              CharSequence title,
                                              CharSequence message,
                                              CharSequence positiveButton,
                                              CharSequence negativeButton,
                                              DialogInterface.OnClickListener positiveAction,
                                              DialogInterface.OnClickListener negativeAction) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton, positiveAction)
                .setNegativeButton(negativeButton, negativeAction)
                .create();
        dialog.show();
        return dialog;
    }

    @SuppressLint("com.waz.ViewUtils")
    public static <T extends View> T getView(@NonNull View v, @IdRes int resId) {
        return (T) v.findViewById(resId);
    }

    @SuppressLint("com.waz.ViewUtils")
    public static <T extends View> T getView(@NonNull Dialog d, @IdRes int resId) {
        return (T) d.findViewById(resId);
    }


    @SuppressLint("com.waz.ViewUtils")
    public static <T extends View> T getView(@NonNull Activity activity, @IdRes int resId) {
        return  (T) activity.findViewById(resId);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static int getActionBarHeight(Activity activity) {
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
        }
        return 0;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();// 创建了一张白纸
        windowManager.getDefaultDisplay().getMetrics(outMetrics);// 给白纸设置宽高
        return outMetrics.heightPixels;
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int[] forceGetViewSize(View view) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthMeasureSpec, heightMeasureSpec);
        return new int[]{view.getMeasuredWidth(), view.getMeasuredHeight()};
    }

    public static View getContentView(Context context) {

        return ((Activity)context).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
    }

    public static void setHeightAndWidth(final View v, final int height,int width) {
        final ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp.height != height) {
            lp.height = height;
            v.setLayoutParams(lp);
        }

        if (lp.width != width) {
            lp.width = width;
            v.setLayoutParams(lp);
        }
        v.requestLayout();       //请求重绘

    }

    public static void setWidth(final View v, final int width) {
        final ViewGroup.LayoutParams lp = v.getLayoutParams();
        if (lp.width != width) {
            lp.width = width;
            v.setLayoutParams(lp);
        }
    }


    public static Point getDisplaySize(Activity activity) {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getSize(point);
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    /**
     * dp转换为px
     *
     * @param context
     * @param dp
     * @return
     */
    public static float dpToPx(Context context, float dp) {
        float px = getAbsValue(context, dp, TypedValue.COMPLEX_UNIT_DIP);
        return px;
    }

    /**
     * px转换为dp
     *
     * @param context
     * @param px
     * @return
     */
    public static float pxToDp(Context context, float px) {
        float dp = (px / context.getResources().getDisplayMetrics().density);
        return dp;
    }

    /**
     * sp转px
     *
     * @param context
     * @param sp
     * @return
     */
    public static float spToPx(Context context, float sp) {
        float px = getAbsValue(context, sp, TypedValue.COMPLEX_UNIT_SP);
        return px;
    }

    private static float getAbsValue(Context context, float value, int unit) {
        return TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
    }


    static private Drawable scaleDrawable(Drawable drawable, int width, int height) {

        int wi = drawable.getIntrinsicWidth();
        int hi = drawable.getIntrinsicHeight();
        int dimDiff = Math.abs(wi - width) - Math.abs(hi - height);
        float scale = (dimDiff > 0) ? width / (float)wi : height /
                (float)hi;
        Rect bounds = new Rect(0, 0, (int)(scale * wi), (int)(scale * hi));
        drawable.setBounds(bounds);
        return drawable;
    }



}
