package com.brother.pictureedit;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wule on 2017/06/09.
 */

public class RecyclerActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter;
    private List<Integer> mPicList = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_layout);
        Logger.addLogAdapter(new AndroidLogAdapter());
        initData();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        StaggeredGridLayoutManager sg = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(sg);
        mMyAdapter = new MyAdapter(this, mPicList);
        mRecyclerView.setAdapter(mMyAdapter);

        //recyclerview的滑动动画。
        ItemTouchHelper ih = new ItemTouchHelper(new MyCallBack());
        ih.attachToRecyclerView(mRecyclerView);


    }

    /**
     * initial picture data
     */
    private void initData() {
        mPicList.add(R.drawable.cs4);
        mPicList.add(R.drawable.cs1);
        mPicList.add(R.drawable.cs2);
        mPicList.add(R.drawable.cs3);
        mPicList.add(R.drawable.cs5);
        mPicList.add(R.drawable.cs6);
        mPicList.add(R.drawable.cs7);
        mPicList.add(R.drawable.cs8x);
        mPicList.add(R.drawable.j2);
        mPicList.add(R.drawable.j3);
        mPicList.add(R.drawable.j4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dialog, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_dialog:
                showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * dialog的创建
     */
    private void showDialog() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog, null);
        PopupWindow p = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//        p.setAnimationStyle();
        p.setTouchable(true);
        p.setOutsideTouchable(true);
        p.setBackgroundDrawable(new StateListDrawable());
        p.setAnimationStyle(R.style.pop);
        final Display d = getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        d.getMetrics(dm);
        //动画效果
        animateAlpha(1f,0.5f);
        //show
        p.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        //消失监听器
        p.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                animateAlpha(0.5f,1f);
            }
        });

    }

    /**
     * animate the alpha in window
     * @param start
     * @param end
     */
    private void animateAlpha(float start, float end) {
        final WindowManager.LayoutParams wl = getWindow().getAttributes();
        wl.alpha = 0.5f;
        ValueAnimator vv = ValueAnimator.ofFloat(start, end);
        vv.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                wl.alpha = f;
                getWindow().setAttributes(wl);
            }
        });
        vv.setDuration(1000).start();
    }

    /**
     * 适配器
     */
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private Context mContext;
        private List<Integer> mPicData;
        private List<Integer> mItemHeight = new ArrayList<>();

        public MyAdapter(Context mContext, List<Integer> mPicData) {
            this.mContext = mContext;
            this.mPicData = mPicData;
            for (int i = 0; i < mPicData.size(); i++) {
                mItemHeight.add((int) (Math.random() * 300) + 300);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewHolder vh = null;
            View v = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_layout, parent, false);
            vh = new ViewHolder(v);

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ViewGroup.LayoutParams ll = holder.itemView.getLayoutParams();
            ll.height = mItemHeight.get(position);
            holder.itemView.setLayoutParams(ll);
            Logger.i(position + "...." + ll.height);

            holder.mImage = (ImageView) holder.itemView.findViewById(R.id.imgView);
            holder.mImage.setImageResource(mPicData.get(position));
        }

        @Override
        public int getItemCount() {
            return mPicData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImage;

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    /**
     * 回调
     */
    private class MyCallBack extends ItemTouchHelper.Callback {
        private int dragFlags;
        private int swipeFlags;

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            dragFlags = 0;
            swipeFlags = 0;
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager
                    || recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                        | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            } else {
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (toPosition != 0) {
                if (fromPosition < toPosition)
                    //向下拖动
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mPicList, i, i + 1);
                    }
                else {
                    //向上拖动
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mPicList, i, i - 1);
                    }
                }
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            }
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mRecyclerView.getAdapter().notifyItemRemoved(position);
            mPicList.remove(position);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setPressed(true);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setPressed(false);
        }

        //处理动画
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                //滑动时改变 Item 的透明度，以实现滑动过程中实现渐变效果
                final float alpha = 1 - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                viewHolder.itemView.setAlpha(alpha);
                viewHolder.itemView.setTranslationX(dX);
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

    }
}
