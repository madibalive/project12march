package com.example.madiba.venualpha.ui.stateimageviewv2.stateimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 3/8/2017.
 */

public class StateImageView extends AppCompatImageView {

    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private List<String> bitmapsUrl = new ArrayList<String>();
    private Path path = new Path();
    private Rect rect = new Rect();
    private Drawable multiDrawable;


    public void addImages(List<String > urls){
            this.bitmapsUrl=urls;
        refresh();
    }

    private void refresh() {
        multiDrawable = new StateDrawable(bitmapsUrl,getContext());
        setImageDrawable(multiDrawable);
    }

    private void clear() {
        bitmapsUrl.clear();
        refresh();
    }


    public StateImageView(Context context) {
        super(context);
    }

    public StateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);



    }
}
