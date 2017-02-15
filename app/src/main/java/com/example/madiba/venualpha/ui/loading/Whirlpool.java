package com.example.madiba.venualpha.ui.loading;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by Tuyen Nguyen on 2/13/17.
 */

public class Whirlpool extends LoaderView {
  private Arc[] arcs;
  private int numberOfArc;
  private float[] rotates;

  public Whirlpool() {
    numberOfArc = 3;
  }

  @Override public void initializeObjects() {
    float r = Math.min(width, height) / 2f;
    arcs = new Arc[numberOfArc];
    rotates = new float[numberOfArc];

    for (int i = 0; i < numberOfArc; i++) {
      float d = r / 4 + i * r / 4;
      arcs[i] = new Arc();
      arcs[i].setColor(color);
      arcs[i].setOval(new RectF(center.x - d, center.y - d, center.x + d, center.y + d));
      arcs[i].setStartAngle(i * 45);
      arcs[i].setSweepAngle(i * 45 + 90);
      arcs[i].setStyle(Paint.Style.STROKE);
      arcs[i].setWidth(r / 10f);
    }
  }

  @Override public void setUpAnimation() {
    for (int i = numberOfArc - 1; i >= 0; i--) {
      final int index = i;

      ValueAnimator fadeAnimator = ValueAnimator.ofFloat(arcs[i].getStartAngle(),
          arcs[i].getStartAngle() + 360* (i % 2 == 0 ? -1 : 1));
      fadeAnimator.setRepeatCount(ValueAnimator.INFINITE);
      fadeAnimator.setDuration((i + 1) * 500);
      fadeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        @Override public void onAnimationUpdate(ValueAnimator animation) {
          rotates[index] = (float)animation.getAnimatedValue();
          invalidateListener.reDraw();
        }
      });

      fadeAnimator.start();
    }
  }

  @Override public void draw(Canvas canvas) {
    for (int i = 0; i < numberOfArc; i++) {
      canvas.save();
      canvas.rotate(rotates[i], center.x, center.y);
      arcs[i].draw(canvas);
      canvas.restore();
    }
  }

  public class Arc extends GraphicObject {
    private RectF oval;
    private float startAngle;
    private float sweepAngle;
    private boolean useCenter;

    public void setOval(RectF oval) {
      this.oval = oval;
    }

    public void setStartAngle(float startAngle) {
      this.startAngle = startAngle;
    }

    public void setSweepAngle(float sweepAngle) {
      this.sweepAngle = sweepAngle;
    }

    public void setUseCenter(boolean useCenter) {
      this.useCenter = useCenter;
    }

    public float getStartAngle() {
      return startAngle;
    }

    @Override public void draw(Canvas canvas) {
      canvas.drawArc(oval, startAngle, sweepAngle, useCenter, paint);
    }
  }


  public abstract class GraphicObject {
    protected Paint paint;

    public GraphicObject() {
      paint = new Paint();
      paint.setAntiAlias(true);
    }

    public void setColor(int color) {
      paint.setColor(color);
    }

    public void setAlpha(int alpha) {
      paint.setAlpha(alpha);
    }

    public void setWidth(float width) {
      paint.setStrokeWidth(width);
    }

    public void setStyle(Paint.Style style) {
      paint.setStyle(style);
    }

    public abstract void draw(Canvas canvas);
  }
}
