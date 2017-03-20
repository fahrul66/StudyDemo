package com.example.wulei.retrofit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by wulei on 2017/1/9.
 */

public class AdapterR extends RecyclerView.Adapter<AdapterR.MyViewH> {

    private Context context;

    private List<Datum> list;

    public AdapterR(Context context, List<Datum> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewH onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        MyViewH m = new MyViewH(view);
        return m;
    }

    @Override
    public void onBindViewHolder(MyViewH holder, int position) {
        if (position == (getItemCount() - 1)) {
              LayoutInflater.from(context).inflate(R.layout.footer_view,null);
        }

        holder.textView.setText(list.get(position).getContent());

        Glide.with(context).load(list.get(position).getUrl()).error(R.drawable.ic_image_loadfail)
                .placeholder(R.drawable.ic_image_loading)
                .crossFade()
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewH extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public MyViewH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recyclerText);

            imageView = (ImageView) itemView.findViewById(R.id.recyclerImg);

        }

    }
}
