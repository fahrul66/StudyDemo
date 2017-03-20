package com.example.wulei.demotest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * LinearLayoutManager的分割线
 * Created by wulei on 2016/12/19.
 */

public class LinearLayoutItemDecoration extends RecyclerView.ItemDecoration {
    //读取系统的分割线属性
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };
    //定义水平和竖直方向的值
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
    //设置的分割线的图片
    private Drawable mDivider;
    //设置的分割线方向
    private int mOrientation;

    //构造函数
    public LinearLayoutItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * 设置方向，进行判断
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }

        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        //判断水平线的方向
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    /**
     * 画水平分割线
     *
     * @param c      画布
     * @param parent recyclerView
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        //遍历子view的数量
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //获得子View的margin布局
            RecyclerView.LayoutParams r = (RecyclerView.LayoutParams) child.getLayoutParams();
            //获得分割线的left和right坐标
            final int left = child.getRight() + r.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            //水平布局的高低，有父View设置的上下Pading,子View的margin。
            final int bottom = child.getHeight()+r.bottomMargin+parent.getPaddingBottom();
            final int top = parent.getPaddingTop() + r.topMargin;
            //分割线设置绘制的边界
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 画垂直分割线
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        //获得左边和右边的坐标
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        //获得子view的数量
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            //判断是否是最后一个item，是则不绘制
            if (isLastItem(parent,child)) {

                //不做任何操作

            } else {
                //获得子View的布局参数
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
                //获得item的高
                final int top = child.getBottom() + params.bottomMargin;
                //获得item + 分割线的高。
                final int bottom = top + mDivider.getIntrinsicHeight();
                //确定一个矩形的界限，当调用draw方法时在这个矩形中画。
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }

    /**
     * 判断是否是最后一行
     *
     * @return
     */
    private boolean isLastItem(RecyclerView recyclerView, View child) {
        if (recyclerView.getChildAdapterPosition(child) == recyclerView.getAdapter().getItemCount() - 1) {
            return true;
        }
        return false;
    }

    /**
     * 使RecyclerView的子View，每个item都设置一个偏移量，水平的就是偏移一个drawable的高，
     * 垂直的就是偏移一个drawable的宽
     *
     * @param outRect 设置偏移的矩形
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (isLastItem(parent,view)) {
            //do nothing
        }else {

            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(),0);
            }
        }
    }
}
