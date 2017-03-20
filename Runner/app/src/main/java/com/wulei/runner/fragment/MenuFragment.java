package com.wulei.runner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wulei.runner.R;

/**
 * Created by wulei on 2017/1/13.
 */

public class MenuFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * fragment最好不要使用构造方法传递参数，传递参数时使用setArgument,在fragment中使用getArgument
         * 获取
         */
        int num = getArguments().getInt("PARA",0);
        //参数判断
        TextView t = (TextView) view.findViewById(R.id.fragment_text);
        //设置text数据
        t.setText(getTextString(view,num));
    }

    /**
     * 对构造方法参数判断，textview的取值
     */
    private String getTextString(View view,int num) {
        String textViewString = null;
        switch (num) {
            case 1:
                textViewString = "主页";
                view.setBackgroundColor(Color.BLUE);
            break;
            case 2:
                textViewString = "查询";
                view.setBackgroundColor(Color.RED);
            break;
            case 3:
                textViewString = "通知";
                view.setBackgroundColor(Color.YELLOW);
            break;
            default:
                textViewString = "default error";
                view.setBackgroundColor(Color.GREEN);
            break;
        }

        return textViewString;
    }
}
