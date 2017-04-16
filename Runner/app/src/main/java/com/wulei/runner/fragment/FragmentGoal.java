package com.wulei.runner.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wulei.runner.R;
import com.wulei.runner.activity.MainActivity;
import com.wulei.runner.adapter.GoalAdapter;
import com.wulei.runner.app.App;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.fragment.base.BaseFragment;
import com.wulei.runner.model.GoalModel;
import com.wulei.runner.model.LocalSqlPedometer;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.DividerItemDecoration;
import com.wulei.runner.utils.FragmentUtils;
import com.wulei.runner.utils.SnackbarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wule on 2017/04/01.
 */

public class FragmentGoal extends BaseFragment {
    @BindView(R.id.recycler_goals)
    RecyclerView mRecyclerView;
    @BindView(R.id.fab_goals)
    FloatingActionButton mFab;
    @BindView(R.id.coordinate_goals)
    CoordinatorLayout mCoordi;
    @BindView(R.id.tv_goals)
    TextView mTv;
    @BindView(R.id.pro)
    ProgressBar mProgress;
    /*
     * 数据库设置
     */
    private LocalSqlHelper lsh;
    private FragmentRun mFragmentRun = (FragmentRun) FragmentUtils.newInstance(ConstantFactory.TAG_RUN);
    private List<GoalModel> list;

    @NonNull
    @Override
    protected int setContentView() {
        return R.layout.fragment_goals;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        //数据库
        lsh = new LocalSqlHelper(App.mAPPContext);
        //初始化progress
        mProgress.setMax(FragmentRun.goals);
        mProgress.setProgress(mFragmentRun.mArc.getProgress());
        //初始化
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));

    }

    /**
     * 从服务端获取数据,更新进度
     */
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstantFactory.INIT:
                    int step = (int) msg.getData().get("steps");
                    if (mFragmentRun.mArc != null && step >= 0) {
                        //更新mrc数据
                        mProgress.setProgress(step);

                    }
                    break;
                case ConstantFactory.SAVE:
                    int steps = (int) msg.getData().get("steps");
                    if (mFragmentRun.mArc != null && steps >= 0) {
                        mProgress.setProgress(steps);
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * fab编辑点击事件
     */
    @Override
    protected void setListener() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(mActivity);
                editText.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                editText.setHint("步数至少要大于5000");
                DialogUtils.showAlert(mActivity, "今日目标步数", editText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String et = editText.getText().toString();
                        //判断是否为空
                        if (!TextUtils.isEmpty(et) && Integer.parseInt(et) >= 5000) {

                            //设置text
                            mTv.setText(et);
                            mFragmentRun.mArc.setBottomText("目标：" + Integer.parseInt(et));
                            //更新pro
                            mProgress.setMax(Integer.parseInt(et));
                            FragmentRun.goals = Integer.parseInt(et);

                        } else {
                            SnackbarUtil.show(mCoordi, R.string.snack_error);
                        }
                    }
                });
            }
        });
    }

    /**
     * 逻辑处理
     *
     * @param savedInstanceState
     */
    @Override
    protected void processLogic(Bundle savedInstanceState) {
        //读取数据库，获取数据
        //实例化
        list = new ArrayList<>();
        getData();
        mRecyclerView.setAdapter(new GoalAdapter(mActivity, list));

        //判断是否无数据
        if (list.isEmpty() || list == null) {
            mAppCompatActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.coordinate_goals, FragmentEmpty.create("no data..."))
                    .addToBackStack(null)
                    .commit();
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        //网络存储。。。

        //本地存储
        List<LocalSqlPedometer> listP = lsh.queryJB(null);
        for (LocalSqlPedometer l : listP) {
            if (l.getSteps() >= l.getGoals()) {

                list.add(new GoalModel(l.getDate(), "目标：" + l.getGoals(), "实际：" + l.getSteps(), true));
            } else {

                list.add(new GoalModel(l.getDate(), "目标：" + l.getGoals(), "实际：" + l.getSteps(), false));
            }
        }


    }

    /**
     * 弹出栈
     */
    @Override
    protected void onBackPressed() {
        //默认行为,返回栈
        mAppCompatActivity.getSupportFragmentManager().popBackStack();
        //toolbar返回
        ((MainActivity)mActivity).mNavigationView.setCheckedItem(R.id.run);
    }
}
