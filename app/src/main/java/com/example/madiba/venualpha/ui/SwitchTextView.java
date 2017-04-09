package com.example.madiba.venualpha.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.example.madiba.venualpha.R;
import com.example.madiba.venualpha.util.ViewUtils;


/**
 * version :1.0.0
 * Created by Song on 2016/5/10.
 * Blog:http://blog.csdn.net/u013718120
 */
public class SwitchTextView extends AppCompatTextView {

    //image width、height
    private int imageWidth;
    private int imageHeight;

    private Drawable leftImage;
    private Drawable topImage;
    private Drawable rightImage;
    private Drawable bottomImage;
    private boolean enabled;

    public SwitchTextView(Context context) {
        this(context, null);
    }
    public SwitchTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SwitchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchTextView,0,0);
        int countNum = ta.getIndexCount();
        for (int i = 0; i < countNum; i++) {

            int attr = ta.getIndex(i);
            if (attr == R.styleable.SwitchTextView_leftImage) {
                leftImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.SwitchTextView_topImage) {
                topImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.SwitchTextView_rightImage) {
                rightImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.SwitchTextView_bottomImage) {
                bottomImage = ta.getDrawable(attr);
            } else if (attr == R.styleable.SwitchTextView_imageWidth) {
                imageWidth = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
            } else if (attr == R.styleable.SwitchTextView_imageHeight) {
                imageHeight = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()));
            }
        }

        ta.recycle();
        init();
    }

    /**
     * init views
     */
    private void init() {
        setCompoundDrawablesWithIntrinsicBounds(leftImage,topImage,rightImage,bottomImage);
    }



    public void setState(boolean enabled) {
        if (this.enabled == enabled)
            return;
        switchState();
    }

    private void switchState() {
        if (enabled)
           ViewUtils.getTintedDrawable(getContext(),topImage,R.color.venu_red);
        else
            ViewUtils.getTintedDrawable(getContext(),topImage,R.color.venu_yellow);

        setCompoundDrawablesWithIntrinsicBounds(leftImage,topImage,rightImage,bottomImage);
    }

    public void toggleSwitch(){
        enabled = !enabled;
        switchState();
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        if(left != null) {
            left.setBounds(0,0,imageWidth,imageHeight);
        }

        if(top != null) {
            top.setBounds(0,0,imageWidth,imageHeight);
        }

        if(right != null) {
            right.setBounds(0,0,imageWidth,imageHeight);
        }

        if(bottom != null) {
            bottom.setBounds(0,0,imageWidth,imageHeight);
        }

        setCompoundDrawables(left,top,right,bottom);
    }

}
