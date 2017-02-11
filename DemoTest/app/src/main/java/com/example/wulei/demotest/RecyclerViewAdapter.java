package com.example.wulei.demotest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wulei on 2016/12/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {
    //传递的数据
    private List<String> mData;
    //上下文
    private Context context;
    private OnMyClickListener onClickListener;

    //随机宽度
    private List<Integer> mHeights;

    //构造器
    public RecyclerViewAdapter(Context context, List<String> mData) {
        this.mData = mData;
        this.context = context;
        mHeights = new ArrayList<Integer>();
        for (int i = 0; i < mData.size(); i++)
        {
            mHeights.add( (int) (100 + Math.random() * 300));
        }
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder myHolder = new MyHolder(LayoutInflater.from(context).inflate(R.layout.recyclerview_item_layout, parent,false));
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        //为textview填充数据
        holder.tv.setText(mData.get(position));
        //设置随机高度
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        layoutParams.height = mHeights.get(position);
        holder.itemView.setLayoutParams(layoutParams);

        //响应接口
        if (onClickListener !=null) {

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClick(v, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickListener.onLongClick(v, position);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv;

        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.item_tv);
        }
    }

    /**
     * 接口注册
     * @param onClickListener
     */
    public void setMyOnclickListener(OnMyClickListener onClickListener){
           this.onClickListener = onClickListener;
    }
    /**
     * 接口回调
     */
    interface OnMyClickListener{
       public void onClick(View view,int position);
       public void onLongClick(View view,int position);
    }
}
