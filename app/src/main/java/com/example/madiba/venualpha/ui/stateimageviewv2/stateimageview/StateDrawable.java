package com.example.madiba.venualpha.ui.stateimageviewv2.stateimageview;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madiba on 3/8/2017.
 */

public class StateDrawable extends Drawable {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<ModelStateImgItem> items = new ArrayList<>();
    private List<Bitmap> bitmaps = new ArrayList<>();
    private List<String> bitmapsUrls = new ArrayList<>();
    Context context;

    public StateDrawable(List<String> bitmaps,Context context) {
        this.bitmapsUrls = bitmaps;
        this.context=context;
        init(getBounds());
    }

    private void init(Rect bounds) {
        items.clear();

        if (bitmapsUrls.size() == 1) {

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(0))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width(), bounds.height()) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(0, 0, bounds.width(), bounds.height())));
                        }
                    });

        } else if (bitmapsUrls.size() == 2) {

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(0))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width(),bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(0, 0, bounds.width() / 2, bounds.height())));
                        }
                    });

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(1))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width(), bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(bounds.width() / 2, 0, bounds.width(), bounds.height())));
                        }
                    });

        } else if (bitmapsUrls.size() == 3) {



            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(0))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width(),bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(0, 0, bounds.width() / 2, bounds.height())));
                        }
                    });

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(1))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width() / 2, bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)));
                        }
                    });

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(2))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width() / 2, bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())));
                        }
                    });
        }
        if (bitmapsUrls.size() == 4) {



            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(0))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>(  bounds.width() / 2, bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(0, 0, bounds.width() / 2, bounds.height())));
                        }
                    });

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(1))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width() / 2, bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(0, bounds.height() / 2, bounds.width() / 2, bounds.height())));
                        }
                    });

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(2))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width() / 2, bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(bounds.width() / 2, 0, bounds.width(), bounds.height() / 2)));
                        }
                    });

            Glide.with(context.getApplicationContext())
                    .load(bitmapsUrls.get(3))
                    .asBitmap()
                    .centerCrop()
                    .into(new SimpleTarget<Bitmap>( bounds.width() / 2, bounds.height() / 2) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            items.add(new ModelStateImgItem(bitmap,new Rect(bounds.width() / 2, bounds.height() / 2, bounds.width(), bounds.height())));
                        }
                    });


        }

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
