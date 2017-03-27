package com.wulei.runner.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.wulei.runner.R;

/**
 * Created by wule on 2017/03/27.
 */

public class ArcProgressBar extends View {
    //四只画笔
    private Paint mPaint;
    private Paint p;
    private Paint mPaintText;
    private Paint mPaintSmallTv;
    //尺寸
    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private int strokeWidth = 30;
    //角度
    private float angle = 0f;
    private float angleSum;
    private float maxValue;
    private int progress;
    //text size
    private int textSize = 200;
    //text
    private String topText = "截止当前已走";
    private String bottomText = "步";

    //所有角度两种270,360
    private float angleMode;

    //背景颜色
    private String firstArcColor = "#D3D3D3";
    //渐变色
    private int[] colors = {Color.parseColor("#FF0000"), Color.parseColor("#FFFF00"), Color.parseColor("#00FF00")
            };

    public ArcProgressBar(Context context) {
        this(context, null);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.arcProgress);
            topText = ta.getString(R.styleable.arcProgress_topText);
            bottomText = ta.getString(R.styleable.arcProgress_bottomText);
            maxValue = ta.getInteger(R.styleable.arcProgress_maxProgress, 100);
            progress = ta.getInteger(R.styleable.arcProgress_currProgress, 0);
            angleMode = ta.getInteger(R.styleable.arcProgress_angle, 360);
            //释放资源
            ta.recycle();
        }

        //初始化数据
        init();

    }


    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor(firstArcColor));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);


        /**
         * 字体
         */
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(textSize);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        mPaintSmallTv = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSmallTv.setColor(Color.BLACK);
        mPaintSmallTv.setTextSize(40);
        mPaintSmallTv.setTextAlign(Paint.Align.CENTER);
        mPaintSmallTv.setTypeface(Typeface.DEFAULT);


        /**
         * xml设置了progress后，代码中的初始化
         */
        angle = (angleMode / maxValue) * progress;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取属性值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int with = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);


        //根据模式测量
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            //取最小值给它，并设置宽高一致
            mWidth = mHeight = Math.min(with, height);
        } else {
            //当为wrap_content时，则为固定的大小200*200
            mHeight = mWidth = dp2px(200);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 绘制最初的底纹色
         */
        float length = Math.min(mWidth, mHeight);
        mRectF = new RectF(strokeWidth / 2, strokeWidth / 2, length - strokeWidth / 2, length - strokeWidth / 2);
        canvas.drawArc(mRectF, angleMode / 2, angleMode, false, mPaint);

        /**
         * 绘制上层的进度条
         */
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(strokeWidth);
        SweepGradient s = new SweepGradient(0, 0, colors, null);
        p.setShader(s);


        //画圆弧
        canvas.drawArc(mRectF, angleMode / 2, angle, false, p);

        /**
         * 绘制字体
         */
        //中心字体
        canvas.drawText(String.valueOf(progress), length / 2, length / 2 + textSize / 2, mPaintText);
        //上下的提示字体
        if (topText != null) {

            canvas.drawText(topText, length / 2, length / 2 - textSize / 2, mPaintSmallTv);
        }
        if (bottomText != null) {

            canvas.drawText(bottomText, length / 2, length / 2 + textSize - 50, mPaintSmallTv);
        }

        /**
         * 重新绘制
         */
        if (angle < angleSum) {
            angle = angle + 5;
            //计算增加度数转换成progress
            progress = (int) ((maxValue / angleMode) * angle);
            //重新绘制
            invalidate();
        }
    }


    /**
     * 设置进度值
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {

        angleSum = (angleMode / maxValue) * progress;
        invalidate();

    }

    /**
     * 清除所有数据
     */
    public void clear() {
        progress = 0;
        angleSum = 0;
        angle = 0;

    }

    /**
     * 设置总的进度
     *
     * @param maxValue 总的进度
     */
    public void setMaxProgress(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * 设置最上面的text值
     *
     * @param topText
     */
    public void setTopText(String topText) {
        this.topText = topText;
    }

    /**
     * 设置底部的text值
     *
     * @param bottomText
     */
    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
    }

    /**
     * dp和px的转化
     *
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        float dpi = getResources().getDisplayMetrics().density;
        return (int) ((dp * dpi) + 0.5f);
    }

    private int px2dp(int px) {
        float dpi = getResources().getDisplayMetrics().density;
        return (int) ((px / dpi) + 0.5);
    }
}
