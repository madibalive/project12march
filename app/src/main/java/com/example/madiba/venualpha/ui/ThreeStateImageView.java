package com.example.madiba.venualpha.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
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
 * Created by Madiba on 3/3/2017.
 */

public class ThreeStateImageView extends Drawable {

    private Path path ;
    private Rect rect ;
    private Drawable multiDrawable=null;
    //    List<PhotoItem> items =new ArrayList<PhotoItem>();
    Paint paint ;
    private List<Bitmap> bitmaps = new ArrayList<>();

    public ThreeStateImageView() {
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

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


    private void addImage(Bitmap bitmap) {
        bitmaps.add(bitmap);
        refresh();
    }

    private void  clear() {
        bitmaps.clear();
        refresh();
    }

    private void refresh() {
//        multiDrawable =new MultiDrawable();
//        setImageDrawable(multiDrawable);
    }

    private Bitmap scaleCenterCrop(Bitmap source,int newHeight,int newWidth) {
        return ThumbnailUtils.extractThumbnail(source, newWidth, newHeight);
    }

    //    public ThreeStateImageView(Context context) {
//        super(context);
//    }
//
//    public ThreeStateImageView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//
//        init();
//    }
//
//    private void init() {
//        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//    }
//
//    public ThreeStateImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//
//
//
//    private void MultiDrawable(){
//        items.clear();
//
//        if (bitmaps.size() == 1) {
//            Bitmap bitmap = scaleCenterCrop(bitmaps.get(0), getClipBounds().width(), getClipBounds().height());
//            items.add(PhotoItem(bitmap, Rect(0, 0, getClipBounds().width(), getClipBounds().height())));
//        } else if (bitmaps.size() == 2) {
//            Bitmap bitmap1 = scaleCenterCrop(bitmaps.get(0), getClipBounds().width(), getClipBounds().height() / 2)
//            Bitmap bitmap2 = scaleCenterCrop(bitmaps.get(0), getClipBounds().width(), getClipBounds().height() / 2)
//            items.add(PhotoItem(bitmap1, Rect(0, 0, getClipBounds().width() / 2, getClipBounds().height())))
//            items.add(PhotoItem(bitmap2, Rect(getClipBounds().width() / 2, 0, getClipBounds().width(), getClipBounds().height())))
//        } else if (bitmaps.size() == 3) {
//            Bitmap bitmap1 = scaleCenterCrop(bitmaps.get(0), getClipBounds().width(), getClipBounds().height() / 2)
//            Bitmap bitmap2 = scaleCenterCrop(bitmaps.get(0), getClipBounds().width() / 2, getClipBounds().height() / 2)
//            Bitmap bitmap3 = scaleCenterCrop(bitmaps.get(0), getClipBounds().width() / 2, getClipBounds().height() / 2)
//            items.add(PhotoItem(bitmap1, Rect(0, 0, getClipBounds().width() / 2, getClipBounds().height())))
//            items.add(PhotoItem(bitmap2, Rect(getClipBounds().width() / 2, 0, getClipBounds().width(), getClipBounds().height() / 2)))
//            items.add(PhotoItem(bitmap3, Rect(getClipBounds().width() / 2, getClipBounds().height() / 2, getClipBounds().width(), getClipBounds().height())))
//        }
//    }
//

//
//
//
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (canvas != null) {
//
//            for (PhotoItem it: items
//                    ) {
////                canvas.drawBitmap(it.bitmap, bounds, it.position, paint)
//
//            }
//        }
//    }
//
//
//
//
    public class PhotoItem {

        private Bitmap bitmap;
        private double width;
        private double height;

        public PhotoItem() {
        }

        public PhotoItem(Bitmap bitmap, double width, double height) {
            this.bitmap = bitmap;
            this.width = width;
            this.height = height;
        }
    }




}
