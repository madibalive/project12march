package com.example.madiba.venualpha.ui.stateimageview;

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
    private Path path = new Path();
    private Rect rect = new Rect();
    private Drawable multiDrawable;


    public void addImages(List<String > urls){

    }

    public void addImage(Bitmap bitmap ) {
        bitmaps.add(bitmap);
        refresh();
    }

    private void refresh() {

        multiDrawable = new StateDrawable(bitmaps);
        setImageDrawable(multiDrawable);
    }

    private void clear() {
        bitmaps.clear();
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
