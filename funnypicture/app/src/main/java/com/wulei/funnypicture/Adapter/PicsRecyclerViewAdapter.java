package com.wulei.funnypicture.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wulei.funnypicture.R;
import com.wulei.funnypicture.model.PicsGson;
import com.wulei.funnypicture.utils.ConstantUtils;
import com.wulei.funnypicture.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by wulei on 2017/2/10.
 */

public class PicsRecyclerViewAdapter extends RecyclerView.Adapter {
    /**
     * 上下文
     */
    private Context context;
    /**
     * gson数据
     */
    private List<PicsGson> list;

    FenYeCallBack fenYeCallBack;

    public PicsRecyclerViewAdapter(Context context, List<PicsGson> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //解析
        View view = LayoutInflater.from(context).inflate(R.layout.pics_item, parent, false);
        PicsRecyclerViewAdapter.PicsViewHolder picsViewHolder = new PicsRecyclerViewAdapter.PicsViewHolder(view);
        //解析footer
        View footer = LayoutInflater.from(context).inflate(R.layout.footer_recyclerview, parent, false);
        FooterViewHolder footerViewHolder = new FooterViewHolder(footer);
        //类型判断
        if (viewType == ConstantUtils.TYPE_CONTENT) {
            return picsViewHolder;
        } else {
            return footerViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //网络获取的笑话数据
        if (holder instanceof PicsViewHolder) {
            //设置标题
            String content = list.get(position).getContent();
            ((PicsViewHolder) holder).textView.setText(content);
            //设置网络图片
            String url = list.get(position).getUrl();
            ImageLoaderUtils.display(context, ((PicsViewHolder) holder).imageView, url, R.drawable.ic_image_loadfail, R.drawable.ic_image_loading);
        } else {
            //数据加载。。。
            fenYeCallBack.load();
        }



    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    class PicsViewHolder extends RecyclerView.ViewHolder {
        /**
         * textview
         */
        private TextView textView;
        private ImageView imageView;

        public PicsViewHolder(View itemView) {
            super(itemView);
            //实例化
            textView = (TextView) itemView.findViewById(R.id.textView_joke);
            imageView = (ImageView) itemView.findViewById(R.id.image_pics);
        }
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

    public void setFenYeCallBack(PicsRecyclerViewAdapter.FenYeCallBack fenYeCallBack) {
        this.fenYeCallBack = fenYeCallBack;
    }
}
