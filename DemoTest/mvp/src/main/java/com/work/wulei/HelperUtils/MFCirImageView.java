package com.work.wulei.HelperUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.work.wulei.myapplication.R;

/**
 * Created by wulei on 2016/12/26.
 */

public class MFCirImageView extends ImageView {
    private static final int DEFAULT_BODER_COLOR = Color.BLACK;
    private static final float DEFAULT_BODER_WIDTH = 2;

    private int mWidth;
    private int mHeight;

    private Bitmap mSrc;

    private int type;
    private int mRadius;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    public MFCirImageView(Context context) {
        super(context);
    }

    public MFCirImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MFCirImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++)
        {
            int attr = a.getIndex(i);
            switch (attr)
            {
                case R.styleable.CustomImageView_src:
                    mSrc = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomImageView_type:
                    type = a.getInt(attr, 0);// 默认为Circle
                    break;
                case R.styleable.CustomImageView_borderRadius:
                    mRadius= a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f,
                            getResources().getDisplayMetrics()));// 默认为10DP
                    break;
            }
        }
        a.recycle();
    }




    /**
     * 根据屏幕密度dp到px转换
     *
     * @param context
     * @param dpValue
     * @return
     */
    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {  //match_parent ,accurate
            mWidth = widthSize;
        } else {
            int desireByImg = getPaddingLeft() + getPaddingRight() + mSrc.getWidth();
            if (widthMode == MeasureSpec.AT_MOST) {  //wrap_content
                mWidth = Math.min(desireByImg, widthSize);
            } else {
                mWidth = desireByImg;
            }
        }

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int desireByImg = getPaddingTop() + getPaddingBottom() + mSrc.getHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(desireByImg, heightSize);
            } else {
                mHeight = desireByImg;
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (type) {
            // 如果是TYPE_CIRCLE绘制圆形
            case TYPE_CIRCLE:

                int min = Math.min(mWidth, mHeight);
                /**
                 * 长度如果不一致，按小的值进行压缩
                 */
                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);

                canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
                break;
            case TYPE_ROUND:
                canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
                break;

        }
    }

    private Bitmap createRoundConerImage(Bitmap mSrc) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        //创建一个位图，通过设置他的长宽，以及bitmap的config（pixels如何保存，ARGB_8888每个像素保存4个字节）
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

        //创建画布
        Canvas canvas = new Canvas(bitmap);

        //位图上画，圆角矩形
        RectF rectf = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectf, mRadius, mRadius, paint);

        //设置画笔
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(mSrc, 0, 0, paint);

        float scale = Math.max(mWidth/mSrc.getWidth(),mHeight/ mSrc.getHeight());
        canvas.scale(scale,scale);

        return bitmap;

    }

    private Bitmap createCircleImage(Bitmap mSrc, int min) {
        //创建画笔对象
        final Paint paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);

        //创建一个位图对象，尺寸为图片的最小值
        Bitmap bitmap = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        //在此位图上（持有像素）创建一个画布（持有画到位图的工具）
        Canvas canvas = new Canvas(bitmap);

        //位图上，先画一个圆(dest)
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);

        //设置画笔的fermode模式为Src_in（取两者的交集，图为src,不是Dest）
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //位图上，画一个bitmap（src）
        canvas.drawBitmap(mSrc, 0, 0, paint);

        //这句话很关键，在我们将view放大或者缩小时，也要讲画布放大缩小，不然view变小，
        //画布大小不变的话，我们只能看到一部分的bitmap视图
        canvas.scale(mWidth/mSrc.getWidth(),mHeight/ mSrc.getHeight());

        return bitmap;
    }


}
