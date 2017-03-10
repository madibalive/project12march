package com.example.madiba.venualpha.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.madiba.venualpha.R;

/**
 * Created by Madiba on 11/1/2016.
 */
class VariableLayout extends RelativeLayout {

    private Boolean isSquare;

    public VariableLayout(Context context) {
        super(context);
    }

    public VariableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VariableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VariableLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);


        TypedArray array = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.VariableLayout, 0, 0);
        try {
            isSquare = array.getBoolean(R.styleable.VariableLayout_VariableLayoutMode, true);

        } finally {
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isSquare)
             super.onMeasure(widthMeasureSpec, 2*heightMeasureSpec);
        else
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}