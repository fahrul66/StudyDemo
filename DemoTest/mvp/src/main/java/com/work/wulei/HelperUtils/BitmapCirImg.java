package com.work.wulei.HelperUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.work.wulei.myapplication.R;

/**
 * Created by wulei on 2016/12/26.
 */

public class BitmapCirImg extends ImageView {
    private int type;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    //圆角默认值
    private static final int BORDER_RADIUS_DEFAULT = 10;

    //圆角大小
    private int mBorderRadius;
    //画笔
    private Paint mPaint;
    //圆角的半径
    private int mRadius;

    //矩阵，用于缩小和放大
    private Matrix mMatrix;

    //着色器
    private BitmapShader mBitmapShader;

    //view的宽度
    private int mWidth;
    private RectF mRectF;

    public BitmapCirImg(Context context) {
        super(context);
    }

    public BitmapCirImg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapCirImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMatrix = new Matrix();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);
        //applyDimension将dp转换成px
        mBorderRadius = a.getDimensionPixelSize(R.styleable.CustomImageView_borderRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) BORDER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        //获得type,默认circle
        type = a.getInt(R.styleable.CustomImageView_type, TYPE_CIRCLE);

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //如果是圆形的，则强制以小的一边为准
        if (type == TYPE_CIRCLE) {

            mWidth = Math.min(getMeasuredWidth(), getMeasuredWidth());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    private void setUpShader()
    {
        Drawable drawable = getDrawable();
        if (drawable == null)
        {
            return;
        }

        Bitmap bmp = drawableToBitmap(drawable);
        // 将bmp作为着色器，就是在指定区域内绘制bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE)
        {
            // 拿到bitmap宽或高的小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;

        } else if (type == TYPE_ROUND)
        {
            // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(), getHeight()
                    * 1.0f / bmp.getHeight());
        }
        // shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        // 设置shader
        mPaint.setShader(mBitmapShader);
    }

    /**
     * drawable转bitmap
     *
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable) {

//        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable  bit = (BitmapDrawable) drawable;
            return bit.getBitmap();
//        }
//        else {
//
//            //获得drawable的宽和高
//            int w = drawable.getIntrinsicWidth();
//            int h = drawable.getIntrinsicHeight();
//            //创建一个bitmap对象
//            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//            //在bitmap上创建画布
//            Canvas canvas = new Canvas(bitmap);
//
//            //给drawable设置绘制的区间
//            drawable.setBounds(0, 0, w, h);
//            //在canvas上绘制drawable
//            drawable.draw(canvas);
//
//            //返回此对象
//            return bitmap;
//        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //去掉父类的onDraw方法
        if (getDrawable() == null)
        {
            return;
        }

        setUpShader();

        if (type == TYPE_ROUND)
        {
            canvas.drawRoundRect(mRectF, mBorderRadius, mBorderRadius,mPaint);
        } else
        {
            canvas.drawCircle(mWidth/2, mWidth/2, mWidth/2, mPaint);

            // drawSomeThing(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //圆角图片范围
        mRectF = new RectF(0,0,getWidth(),getHeight());
    }
}
