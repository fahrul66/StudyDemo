package com.wulei.runner.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wulei.runner.R;
import com.wulei.runner.model.LocalSqlRun;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wulei on 2017/4/18.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.Holder> {
    private Context context;
    private List<LocalSqlRun> list;
    private List<Integer> heightList;//装产出的随机数

    public RecordAdapter(Context context, List<LocalSqlRun> list) {
        this.context = context;
        this.list = list;
        //记录为每个控件产生的随机高度,避免滑回到顶部出现空白
        heightList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            int height = new Random().nextInt(200) + 600;//[100,300)的随机数
            heightList.add(height);
        }

    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_recycler_item, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.img.setImageBitmap(BitmapFactory.decodeFile(list.get(position).getPicUrl()));
        holder.km.setText(list.get(position).getKm() + "公里");
        holder.date.setText(list.get(position).getDate());
        holder.addr.setText(list.get(position).getAddress());
        //由于需要实现瀑布流的效果,所以就需要动态的改变控件的高度了
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = heightList.get(position) + holder.itemView.getHeight();
        holder.itemView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * view holder
     */
    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_record)
        ImageView img;
        @BindView(R.id.tv_km_record)
        TextView km;
        @BindView(R.id.tv_date_record)
        TextView date;
        @BindView(R.id.tv_address_record)
        TextView addr;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
