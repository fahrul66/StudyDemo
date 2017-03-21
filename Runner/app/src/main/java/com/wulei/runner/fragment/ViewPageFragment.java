package com.wulei.runner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wulei.runner.R;
import com.wulei.runner.adapter.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wulei on 2017/1/30.
 */

public class ViewPageFragment extends Fragment {

    List<Fragment> list = new ArrayList<>();


    @BindView(R.id.viewPage) ViewPager viewPager;

    @BindView(R.id.tabLayout)TabLayout tabLayout;
    @BindView(R.id.speed)TabItem speed;
    @BindView(R.id.map)TabItem map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ButterKnife.bind(this,getActivity());

        return inflater.inflate(R.layout.fragment_viewpage, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list.clear();

        /**
         * 传递参数
         */
//        Bundle bundle = new Bundle();
//        bundle.putInt("PARA",1);
//        menuOne.setArguments(bundle);
//        bundle.clear();
//
//        bundle.putInt("PARA",2);
//        menuTwo.setArguments(bundle);
//        bundle.clear();
//
//        setText(menuOne);
//
//        setText(menuTwo);
//
//        //fragment集合
//        list.add(menuOne);
//        list.add(menuTwo);

        viewPager.setAdapter(new ViewpagerAdapter(getChildFragmentManager(),list));


        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * fragment之间改变数据
     * @param fragment
     */
//    private void setText(Fragment fragment) {
//        TextView textView = (TextView) fragment.getView().findViewById(R.id.fragment_text);
//        if (textView.getText().equals("menuOne")) {
//
//            textView.setText("menuTwo");
//        }
//        textView.setText("menuOne");
//    }
}
