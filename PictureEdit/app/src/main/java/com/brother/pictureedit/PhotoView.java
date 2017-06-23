package com.brother.pictureedit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;

/**
 * Created by wule on 2017/06/08.
 */

public class PhotoView extends View {
    /**
     * 本地的bitmap资源
     */
    private Bitmap mBitmap;
    /**
     * 获得bitmap的宽高
     */
    private float bitmapWidth, bitmapHeight;

    /**
     * 获得整个控件的宽高
     */
    private int width, height;
    /**
     * 移动的距离
     */
    private float initX, initY, totalX, totalY;
    /**
     * 缩放的比例
     */
    private float initRate;
    /**
     * 手指移动的触摸点
     */
    private float x, y, xx, yy;
    /**
     * 旋转的角度
     */
    private float rotation, rotation1;
    /**
     * 两指之间的距离，新的，距离差
     */
    private float distance, distanceTow;
    /**
     * 几种状态，缩放，旋转，移动，初始
     */
    public static final int SCALE = 1;
    public static final int ROTATE = 2;
    public static final int TRANSLATE = 3;
    public static final int INIT = -1;
    /**
     * 默认初始化状态为INIT
     */
    private int state = INIT;
    /**
     * 矩阵
     */
    private Matrix matrix = new Matrix();
    /**
     * 画笔
     */
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 点
     */
    private PointF p = new PointF();
    private float moveX, moveY;
    int degree = 10;

    private GestureDetector gd;
    private GestureDetector.SimpleOnGestureListener gs = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            initRate++;
            if (initRate <= 4) {
                matrix.reset();
                scaleCenter(initRate);
                invalidate();
            }
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {

            degree += 20;
            matrix.postSkew(2, 2, width / 2, height / 2);
            invalidate();
            return super.onSingleTapUp(e);
        }
    };

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gd = new GestureDetector(context, gs);
    }

    /**
     * 设置图片的bitmap
     */
    public void setImageBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    /**
     * 设置图片bitmap
     */
    public void setImageResource(@DrawableRes int id) {
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(id);
        mBitmap = bd.getBitmap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (state) {
            case INIT:
                //图像初始化
                initBimtap();
                //进行图片的变换
                canvas.drawBitmap(mBitmap, matrix, paint);
                break;
            case SCALE:
            case TRANSLATE:
            case ROTATE:
                //进行图片的变换
                canvas.drawBitmap(mBitmap, matrix, paint);
                break;
        }

    }

    /**
     * 初始化bitmap 计算中心的坐标位置
     */
    private void initBimtap() {
        if (mBitmap != null) {
            //获得图片的宽高
            bitmapWidth = mBitmap.getWidth();
            bitmapHeight = mBitmap.getHeight();
            width = getWidth();
            height = getHeight();
            //图片是否宽，高大于控件，缩放
            if (bitmapWidth > width || bitmapHeight > height) {
                //缩放的比例
                initRate = Math.min(width * 1.0f / bitmapWidth, height * 1.0f / bitmapHeight);
                //缩放后的宽，高
                bitmapWidth *= initRate;
                bitmapHeight *= initRate;
                //移动到中心点的位置
                initX = width / 2 - bitmapWidth / 2;
                initY = height / 2 - bitmapHeight / 2;
                //缩放，位移
                matrix.postScale(initRate, initRate);
                matrix.postTranslate(initX, initY);
            } else {
                initRate = 1;
                //图片小于，控件的宽高，居中显示
                initX = width / 2 - bitmapWidth / 2;
                initY = height / 2 - bitmapHeight / 2;
                matrix.postTranslate(initX, initY);
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                xx = event.getX(1);
                yy = event.getY(1);
                //两指的中心点
                pPoint(p, event);
                distance = distance(event);
                rotation1 = rotation(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //一根手指
                if (event.getPointerCount() == 1) {
                    float oneX = event.getX();
                    float oneY = event.getY();
                    //图片大于控件边界
                    if (bitmapWidth > width || bitmapHeight > height) {
                        Logger.i("width:%f,%f,%d,%d", bitmapWidth, bitmapHeight, width, height);
                        //获取最大的超出的边界，也就是可移动范围
                        float moveMax = Math.max(bitmapWidth - width, bitmapHeight - height);
                        //手指移动范围
                        moveX = oneX - x;
                        moveY = oneY - y;
                        x = oneX;
                        y = oneY;
                        //移动范围不能超过边界
                        if (moveX > moveMax) {
                            moveX = moveMax;
                        } else if (moveY > moveMax) {
                            moveY = moveMax;
                        }
                        totalX += moveX + initX;
                        totalY += moveY + initY;
                        //移动
                        matrix.postTranslate(moveX, moveY);
                        state = TRANSLATE;
                        invalidate();
                    }
                    //两根手指
                } else if (event.getPointerCount() == 2) {
                    float twoX = event.getX(1);
                    float twoY = event.getX(1);
                    //缩放，4个等级
                    distanceTow = distance(event);
                    //手指移动的比例
                    float scaleRate = distanceTow / distance;
                    //总的图片缩放的比例，不能超过5，最多放大5倍,缩小3倍
                    float rate = initRate * scaleRate;

                    Logger.i("width:%f,%f,%d,%d", bitmapWidth, bitmapHeight, width, height);
                    //存储上一次的比例和距离
                    initRate = rate;
                    distance = distanceTow;
                    Logger.i("tow:%f,old:%f,%f,%f", distanceTow, distance, scaleRate, rate);
                    if (rate <= 4 && rate >= 0.3) {
                        //清除原来的缩放比例，或者使用setScale也是清除
                        matrix.reset();
                        matrix.postTranslate(totalX, totalY);
                        //重新设置图片的宽高
                        bitmapWidth = mBitmap.getWidth() * rate;
                        bitmapHeight = mBitmap.getHeight() * rate;
                        //缩放时如果图片小于控件，则按照控件中心缩放，反之，按照手指的中心缩放
                        scaleCenter(rate);
                        //使其位移到指定位置
                        state = SCALE;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }

    /**
     * 判断图片是否大于控件的大小
     * 小于，按照控件大小缩放
     * 大于，按照手指的距离缩放
     */
    private void scaleCenter(float rate) {
//        if (bitmapWidth <= width && bitmapHeight <= height) {
            matrix.postScale(rate, rate, width / 2, height / 2);
//        } else {
//            matrix.postScale(rate, rate, p.x, p.y);
//        }
    }

    /**
     * 触碰两点间距离
     */
    private float distance(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    /**
     * 取手势中心点
     */
    private void pPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    // 取旋转角度
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        /**
         * 反正切函数
         * 计算两个坐标点的正切角度
         */
        double radians = Math.atan2(delta_y, delta_x);
        return (float) (Math.toDegrees(radians));
    }
}
