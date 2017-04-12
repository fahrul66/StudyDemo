package com.wulei.runner.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.text.TextUtils;
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
    private int strokeWidth = 50;
    //角度
    private float angle = 0f;
    private float angleSum;
    private float maxValue;
    private int progress;
    //text size
    private int textSize = 180;
    private int smallTextSize = 40;
    //text
    private String topText = "截止当前已走";
    private String bottomText = "步";
    //所有角度两种270,360
    private float angleMode;
    //增大的系数
    private float multiNum;

    //颜色属性值
    private boolean isClosedShader;
    private String arcColor;
    private String bgArcColor;
    private String midTextColor;
    private String topTextColor;

    //渐变色
    private int[] colors = {Color.parseColor("#FF0000"), Color.parseColor("#FFFF00"), Color.parseColor("#00FF00"),
            Color.parseColor("#0000FF"), Color.parseColor("#00FFFF"), Color.parseColor("#FFFFFF")};

    public ArcProgressBar(Context context) {
        this(context, null);
    }

    public ArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化xml属性值
        xmlAttrs(context, attrs);

        //初始化数据
        init();

    }

    /**
     * 从xml中解析自定义属性
     *
     * @param context 上下文
     * @param attrs   属性值
     */
    private void xmlAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.arcProgress);
            topText = ta.getString(R.styleable.arcProgress_topText);
            bottomText = ta.getString(R.styleable.arcProgress_bottomText);
            maxValue = ta.getInteger(R.styleable.arcProgress_maxProgress, 100);
            progress = ta.getInteger(R.styleable.arcProgress_currProgress, 0);
            angleMode = ta.getInteger(R.styleable.arcProgress_angle, 360);
            //color
            bgArcColor = ta.getString(R.styleable.arcProgress_arcBgColor);
            midTextColor = ta.getString(R.styleable.arcProgress_midTextColor);
            topTextColor = ta.getString(R.styleable.arcProgress_topTextColor);
            //进度的颜色，需关闭渲染器
            arcColor = ta.getString(R.styleable.arcProgress_arcColor);
            isClosedShader = ta.getBoolean(R.styleable.arcProgress_closable, false);
            //释放资源
            ta.recycle();
        }
    }


    public ArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }


    /**
     * 数据初始化
     */
    private void init() {
        /**
         * 颜色值初始化
         */
        if (TextUtils.isEmpty(bgArcColor)) {
            bgArcColor = "#D3D3D3";
        }
        if (TextUtils.isEmpty(midTextColor)) {
            midTextColor = "#000000";
        }
        if (TextUtils.isEmpty(topTextColor)) {
            topTextColor = "#000000";
        }
        if (TextUtils.isEmpty(arcColor)) {

            //默认颜色为系统的颜色
            int color = getResources().getColor(R.color.colorAccent);
            arcColor = "#" + Integer.toHexString(color);
        }

        /**
         * 背景arc
         */
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor(bgArcColor));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        /**
         * 进度arc
         */
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(strokeWidth);
        p.setStrokeJoin(Paint.Join.ROUND);
//        p.setStrokeCap(Paint.Cap.ROUND);

        //颜色值设置
        if (!isClosedShader) {
            SweepGradient s = new SweepGradient(0, 0, colors, null);
            p.setShader(s);
        } else {
            p.setColor(Color.parseColor(arcColor));
        }

        /**
         * 计数字体
         */
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setColor(Color.parseColor(midTextColor));
        mPaintText.setTextSize(textSize);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        /**
         * 小字体
         */
        mPaintSmallTv = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSmallTv.setColor(Color.parseColor(topTextColor));
        mPaintSmallTv.setTextSize(smallTextSize);
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

        //设置测量的尺寸
        setMeasuredDimension(mWidth, mHeight);

        /**
         * 默认的尺寸为200*200dp,故尺寸增大必然给字体也要曾大，同比例增加。
         * 增加倍数
         */
        multiNum = px2dp(mWidth) / 200.0f;
        //字体大小
        textSize = (int) (multiNum * textSize);
        mPaintText.setTextSize(textSize);
        //上下字体
        smallTextSize = (int) (multiNum * smallTextSize);
        mPaintSmallTv.setTextSize(smallTextSize);
        //arc的线宽
        strokeWidth *= multiNum;
        mPaint.setStrokeWidth(strokeWidth);
        //第二层的线宽
        p.setStrokeWidth(strokeWidth);
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

        //画圆弧
        canvas.drawArc(mRectF, angleMode / 2, angleSum, false, p);

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

            canvas.drawText(bottomText, length / 2, length / 2 + textSize - 40 * multiNum, mPaintSmallTv);
        }

        /**
         * 重新绘制
         */
//        if (angle < angleSum) {
//            angle = angle + 5;
//            //计算增加度数转换成progress
////            progress = (int) ((maxValue / angleMode) * angle);
//            //重新绘制
//            invalidate();
//        }
    }


    /**
     * 设置进度值
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {

        this.progress = progress;

        angleSum = ((angleMode / maxValue) * progress);
        invalidate();

    }

    /**
     * 获取当前的进度
     *
     * @return
     */
    public int getProgress() {
        return progress;
    }


    /**
     * 清除所有数据
     */
    public void clear() {
        //数据置空
        progress = 0;
        angleSum = 0;
        angle = 0;

        //重新绘制
        invalidate();
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
     * 获取当前的最大进度
     *
     * @return
     */
    public int getMaxProgress() {
        return (int) maxValue;
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
     * 获取当前的topText
     *
     * @return
     */
    public String getTopText() {
        return topText;
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
     * 获取当前的bottomText
     *
     * @return
     */
    public String getBottomText() {
        return bottomText;
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
        return (int) ((px / dpi) + 0.5f);
    }
}
