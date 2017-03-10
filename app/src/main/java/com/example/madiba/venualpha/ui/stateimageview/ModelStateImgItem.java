package com.example.madiba.venualpha.ui.stateimageview;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Madiba on 3/8/2017.
 */

public class ModelStateImgItem {


    public Bitmap bitmap;
    Rect Bounds;

    public ModelStateImgItem(Bitmap bitmap, Rect Bounds) {
        this.bitmap = bitmap;
        this.Bounds = Bounds;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }



    public Rect getBounds() {
        return Bounds;
    }


}
