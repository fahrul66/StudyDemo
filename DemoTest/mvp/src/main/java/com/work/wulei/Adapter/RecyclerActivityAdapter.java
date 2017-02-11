package com.work.wulei.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.work.wulei.myapplication.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wulei on 2016/12/29.
 */

public class RecyclerActivityAdapter extends RecyclerView.Adapter<RecyclerActivityAdapter.RecyclerActivityViewHolder>{
    private List<String> mList;
    private Context mContext;
    private onItemSelectListener mOnItemSelectListener;

    public RecyclerActivityAdapter(Context context, List<String> list) {
        this.mList = list;
        this.mContext = context;
    }

    @Override

    public RecyclerActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item_layout,parent,false);

        RecyclerActivityViewHolder rav = new RecyclerActivityViewHolder(itemView);

        return rav;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerActivityViewHolder holder, final int position) {

                holder.textView.setText(mList.get(position));

        //点击事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件
                if (mOnItemSelectListener != null) {

                    mOnItemSelectListener.onItemClick(position);
                }
            }
        });
    }

    class RecyclerActivityViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public RecyclerActivityViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textview_recycler_item);
        }
    }

    /**
     * 接口回调，item选中的点击事件
     */
    public   interface onItemSelectListener{
            void onItemClick(int position);
    }

    /**
     * 设置监听器
     */
    public  void  setOnItemSelectListener (onItemSelectListener mOnItemSelectListener){
        this.mOnItemSelectListener = mOnItemSelectListener;
    }
}
