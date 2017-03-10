package com.example.madiba.venualpha.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.madiba.venualpha.util.ViewUtils;


/**
 * Created by sgffsg on 16/6/1.
 */
public class PlusMinusView extends View{
    //选中的进度颜色
    private int DEFAULT_EDGE_COLOR = 0xffD9D9D9;
    //正常状态下的颜色
    private int DEFAULT_NORMAL_COLOR = 0xff353535;
    //加减号可点击颜色
    private int DEFAULT_SELECTON_COLOR = 0xff535353;
    //加减号不可点击
    private int DEFAULT_SELECTOFF_COLOR = 0xffEAEAEA;

    private Paint edgePaint;
    private Paint minusPaint;
    //加号画笔
    private Paint plusPaint;
    //文本画笔
    private Paint textPaint;
    //点击背景画笔
    private Paint bgPaint;

    private float width=308;
    private float height=104;
    private float edge=2;
    private float minusViewWidth=50;
    private float minusViewPaintSize=6;
    private float roundRadius=3;

    private int maxNum=10;
    //商品当前数量
    private int currentNum=0;

    //加号可点击
    private boolean pClickable=true;
    //减号可点击
    private boolean mClickable=false;

    //加号按下
    private boolean pSelected=false;
    //减号按下
    private boolean mSelected=false;


    private Rect mBound;
    private OnPlusMinusClickListener clickListener;


    public PlusMinusView(Context context) {
        this(context,null);
    }

    public PlusMinusView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public PlusMinusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    /**
     * 初始化宽高,画笔等
     */
    private void initView() {
        width= ViewUtils.dpToPx(getContext(),124);
        height=ViewUtils.dpToPx(getContext(),42);
        edge=ViewUtils.dpToPx(getContext(),1);
        minusViewWidth=ViewUtils.dpToPx(getContext(),20);
        minusViewPaintSize=ViewUtils.dpToPx(getContext(),3);
        roundRadius=ViewUtils.dpToPx(getContext(),2);

        edgePaint=new Paint();
        edgePaint.setAntiAlias(true);
        edgePaint.setColor(DEFAULT_EDGE_COLOR);

        minusPaint=new Paint();
        minusPaint.setAntiAlias(true);
        minusPaint.setColor(DEFAULT_SELECTOFF_COLOR);

        plusPaint=new Paint();
        plusPaint.setAntiAlias(true);
        plusPaint.setColor(DEFAULT_SELECTON_COLOR);

        textPaint=new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(DEFAULT_NORMAL_COLOR);

        bgPaint=new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(Color.WHITE);
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setStrokeWidth(4);
        bgPaint.setTextSize(16);

        mBound=new Rect();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        edgePaint.setStyle(Paint.Style.STROKE);
        edgePaint.setStrokeWidth(edge);
        canvas.drawRect(0,0,width,height,edgePaint);
        canvas.drawLine((width-edge*4)/3+edge,0,(width-edge*4)/3+edge,height,edgePaint);
        canvas.drawLine((width-edge*4)*2/3+edge*2,0,(width-edge*4)*2/3+edge*2,height,edgePaint);

        if (currentNum==0){
            minusPaint.setColor(DEFAULT_SELECTOFF_COLOR);
            mClickable=false;
        }else {
            minusPaint.setColor(DEFAULT_SELECTON_COLOR);
            mClickable=true;
        }
        if (mSelected){
            bgPaint.setColor(Color.GRAY);
            canvas.drawRect(edge,edge,height-edge*2,height-edge,bgPaint);
        }else {
            bgPaint.setColor(Color.WHITE);
            canvas.drawRect(edge,edge,height-edge*2,height-edge,bgPaint);
        }

        //画减号
        minusPaint.setStrokeWidth(roundRadius);
        canvas.drawRoundRect(new RectF(minusViewWidth/2,height/2-minusViewPaintSize/2,minusViewWidth*1.5f,height/2+minusViewPaintSize/2),roundRadius,roundRadius,minusPaint);

        if (currentNum>=maxNum){
            plusPaint.setColor(DEFAULT_SELECTOFF_COLOR);
            pClickable=false;
        }else {
            plusPaint.setColor(DEFAULT_SELECTON_COLOR);
            pClickable=true;
        }
        if (pSelected){
            bgPaint.setColor(Color.GRAY);
            canvas.drawRect(width*2/3,edge,width-edge,height-edge,bgPaint);
        }else {
            bgPaint.setColor(Color.WHITE);
            canvas.drawRect(width*2/3,edge,width-edge,height-edge,bgPaint);
        }
        //画加号
        plusPaint.setStrokeWidth(roundRadius);
        canvas.drawRoundRect(new RectF(width-minusViewWidth*3/2,height/2-minusViewPaintSize/2,width-minusViewWidth/2,height/2+minusViewPaintSize/2),roundRadius,roundRadius,plusPaint);
        canvas.drawRoundRect(new RectF(width-minusViewWidth-minusViewPaintSize/2,minusViewWidth/2,width-minusViewWidth+minusViewPaintSize/2,height-minusViewWidth/2),roundRadius,roundRadius,plusPaint);

        String value=currentNum+"";
        //计算字体的宽高
        textPaint.getTextBounds(value, 0, value.length(), mBound);
        //计算文字宽度偏移量
        float offsetTextWidthX = width/2-mBound.width() / 2.0f;
        float offsetTextWidthY = height/2+mBound.height() / 2.0f;
        textPaint.setTextSize(ViewUtils.spToPx(getContext(),14));
        canvas.drawText(value,offsetTextWidthX,offsetTextWidthY,textPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int)(width+edge),(int)(height+edge));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final int action = event.getAction();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (x<width&&x>width-100&&y<height){//点击加号
                    if (pClickable){
                        pSelected=true;
                        invalidate();
                    }
                }else if (x<100&&y<height) {//点击减号
                    if (mClickable) {
                        mSelected=true;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                pSelected=false;
                mSelected=false;
                if (x<width&&x>width-100&&y<height){//点击加号
                    if (pClickable){
                        currentNum++;
                        if (clickListener!=null){
                            clickListener.onPlusClick(currentNum);
                        }
                    }
                }else if (x<100&&y<height){//点击减号
                    if (mClickable){
                        currentNum--;
                        if (clickListener!=null){
                            clickListener.onMinusClick(currentNum);
                        }
                    }
                }
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                if (x>width||y>height) {
                    pSelected=false;
                    mSelected=false;
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                pSelected=false;
                mSelected=false;
                invalidate();
                break;


        }


        return true;
    }

    /**
     * 设置最大数
     * @param max
     */
    public void setMaxNum(int max){
        this.maxNum=max;
    }

    public void setPlusMinusListener(OnPlusMinusClickListener listener){
        this.clickListener=listener;
    }

    public interface OnPlusMinusClickListener{

        void onPlusClick(int currentNum);

        void onMinusClick(int currentNum);

    }


}
