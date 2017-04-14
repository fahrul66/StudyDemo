package com.wulei.runner.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wulei.runner.R;
import com.wulei.runner.model.GoalModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wule on 2017/04/14.
 */

public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.Holder> {

    private Context context;
    private List<GoalModel> list;

    public GoalAdapter(Context context, List<GoalModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.goal_recycler_item, parent, false);
        Holder h = new Holder(view);
        return h;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //数据填充
        holder.mTvGoal.setText(list.get(position).getGoal());
        holder.mTvInFact.setText(list.get(position).getInfact());
        //img
        if (list.get(position).isComplete()) {
            holder.mImg.setImageResource(R.mipmap.complete);
        } else {
            holder.mImg.setImageResource(R.mipmap.no_complete);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * viewholder
     */
    class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_goals_item)
        TextView mTvGoal;
        @BindView(R.id.tv_goals_normal)
        TextView mTvInFact;
        @BindView(R.id.img_goals)
        ImageView mImg;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
