package com.wulei.funnypicture.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wulei.funnypicture.R;
import com.wulei.funnypicture.model.JokeGson;
import com.wulei.funnypicture.utils.ConstantUtils;

import java.util.List;

/**
 * Created by wulei on 2017/2/9.
 */

public class JokeRecyclerViewAdapter extends RecyclerView.Adapter {
    /**
     * 上下文
     */
    private Context context;
    /**
     * gson数据
     */
    private List<JokeGson> list;
    FenYeCallBack fenYeCallBack;

    public JokeRecyclerViewAdapter(Context context, List<JokeGson> list) {
        this.context = context;
        this.list = list;
    }

    public void addData(List<JokeGson> list) {
        if (list != null) {
            list.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //解析item
        View view = LayoutInflater.from(context).inflate(R.layout.joke_item, parent, false);
        JokeViewHolder jokeViewHolder = new JokeViewHolder(view);
        //解析footer
        View footer = LayoutInflater.from(context).inflate(R.layout.footer_recyclerview, parent, false);
        FooterViewHolder footerViewHolder = new FooterViewHolder(footer);
        //类型判断
        if (viewType == ConstantUtils.TYPE_CONTENT) {
            return jokeViewHolder;
        } else {
            return footerViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //网络获取的笑话数据
        if (holder instanceof JokeViewHolder) {
            String content = list.get(position).getContent();
            ((JokeViewHolder) holder).textView.setText(content);
        } else {
            //数据加载。。。
            fenYeCallBack.load();
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return ConstantUtils.TYPE_FOOTER;
        } else {

            return ConstantUtils.TYPE_CONTENT;
        }
    }

    /**
     * item的viewHolder
     */
    class JokeViewHolder extends RecyclerView.ViewHolder {
        /**
         * textview
         */
        private TextView textView;

        public JokeViewHolder(View itemView) {
            super(itemView);
            //实例化
            textView = (TextView) itemView.findViewById(R.id.textView_joke);
        }
    }

    /**
     * footerview的viewHolder
     */
    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 分页加载回调
     */
    public interface FenYeCallBack {
        void load();
    }

    public void setFenYeCallBack(FenYeCallBack fenYeCallBack) {
        this.fenYeCallBack = fenYeCallBack;
    }
}
