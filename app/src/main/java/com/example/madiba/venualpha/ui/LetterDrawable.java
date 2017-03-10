package com.example.madiba.venualpha.ui;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.TextUtils;

import java.util.regex.Pattern;

public class LetterDrawable extends Drawable {

    String mDisplayName;
    Paint mPaint;
    String mKeyName;
    char[] mFirstChar;
    TypedArray mColors;
    private static Pattern pattern = Pattern.compile("^(?i)\\s*(?:the |an |a )|(?:, the|, an|, a)\\s*$|[\\[\\]\\(\\)!\\?\\.,']");

    public LetterDrawable(String displayName, TypedArray colors) {

        mDisplayName = displayName;
        mColors = colors;
        mPaint = new TextPaint();
        mKeyName = keyFor(displayName);
        if (displayName != null && displayName.length() != 0) {
            String key = keyFor(displayName);
            if (key != null && key.length() != 0) {
                mFirstChar = new char[]{Character.toUpperCase(key.charAt(0))};
            }
        }

        mPaint.setColor(Color.WHITE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mFirstChar == null || mKeyName == null || mFirstChar.length == 0 || mKeyName.length() == 0) {
            return;
        }
        canvas.drawColor(pickColor(mDisplayName));
        if (mKeyName.length() > 0) {
            mPaint.setTextSize(canvas.getHeight() * 3 / 5);
            mPaint.getTextBounds(mFirstChar, 0, 1, getBounds());
            canvas.drawText(mFirstChar, 0, 1, canvas.getWidth() / 2, canvas.getHeight() / 2
                    + (getBounds().bottom - getBounds().top) / 2, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    /**
     * @param key The key used to generate the tile color
     * @return A new or previously chosen color for <code>key</code> used as the
     * tile background color
     */
    private int pickColor(String key) {
        // String.hashCode() is not supposed to change across java versions, so
        // this should guarantee the same key always maps to the same color
        final int color = Math.abs(key.hashCode()) % mColors.length();
        return mColors.getColor(color, Color.BLACK);
    }

    private String keyFor(String name) {
        if (!TextUtils.isEmpty(name)) {
            name = pattern.matcher(name)
                    .replaceAll("")
                    .trim()
                    .toLowerCase();
        } else {
            name = "";
        }

        return name;
    }
}
