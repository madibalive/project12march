package com.example.madiba.venualpha.ui.stateimageview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 3/8/2017.
 */

public class StateDrawable extends Drawable {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<ModelStateImgItem> items = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();


    public StateDrawable(List<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    private void init(Rect bounds) {
        items.clear();
        if (bitmaps.size() == 1) {
            Bitmap bitmap = scaleCenterCrop(bitmaps.get(0), bounds.width(), bounds.height());
            items.add(new ModelStateImgItem(bitmap,new Rect(0, 0, bounds.width(), bounds.height())));
        } else if (bitmaps.size() == 2) {
            Bitmap bitmap1 = scaleCenterCrop(bitmaps.get(0), bounds.width(), bounds.height() / 2);
            Bitmap bitmap2 = scaleCenterCrop(bitmaps.get(1), bounds.width(), bounds.height() / 2);
            items.add(new ModelStateImgItem(bitmap1,new Rect(0, 0, bounds.width() / 2, bounds.height())));
            items.add(new ModelStateImgItem(bitmap2,new Rect(bounds.width() / 2, 0, bounds.width(), bounds.height())));
        } else if (bitmaps.size() == 3) {
            Bitmap bitmap1 = scaleCenterCrop(bitmaps.get(0), bounds.width(), bounds.height() / 2);
            Bitmap bitmap2 = scaleCenterCrop(bitmaps.get(1), bounds.width() / 2, bounds.height() / 2);
            Bitmap bitmap3 = scaleCenterCrop(bitmaps.get(2), bounds.width() / 2, bounds.height() / 2);
            items.add(new ModelStateImgItem(bitmap1,new Rect(0, 0, bounds.width() / 2, bounds.height())));
            items.add(new ModelStateImgItem(bitmap2,new Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)));
            items.add(new ModelStateImgItem(bitmap3,new Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())));
        }
        if (bitmaps.size() == 4) {
            Bitmap bitmap1 = scaleCenterCrop(bitmaps.get(0), bounds.width() / 2, bounds.height() / 2);
            Bitmap bitmap2 = scaleCenterCrop(bitmaps.get(1), bounds.width() / 2, bounds.height() / 2);
            Bitmap bitmap3 = scaleCenterCrop(bitmaps.get(2), bounds.width() / 2, bounds.height() / 2);
            Bitmap bitmap4 = scaleCenterCrop(bitmaps.get(3), bounds.width() / 2, bounds.height() / 2);
            items.add(new ModelStateImgItem(bitmap1,new Rect(0, 0, bounds.width() / 2, bounds.height() / 2)));
            items.add(new ModelStateImgItem(bitmap2,new Rect(0, bounds.height() / 2, bounds.width() / 2, bounds.height())));
            items.add(new ModelStateImgItem(bitmap3,new Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)));
            items.add(new ModelStateImgItem(bitmap4,new Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())));
        }
    }

    private Bitmap scaleCenterCrop(Bitmap source,  int newHeight,int newWidth){
        return ThumbnailUtils.extractThumbnail(source, newWidth, newHeight);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (canvas != null) {
            for (int i = 0; i <items.size() ; i++) {
                canvas.drawBitmap(items.get(i).bitmap,items.get(i).Bounds,items.get(i).Bounds,paint);
            }
           }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
