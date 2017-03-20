package com.work.wulei.model;

import android.os.Handler;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wulei on 2016/12/29.
 */

public class RecyclerFindItemImpl implements IRecyclerFindItem {
    /**
     * 查找data的过程，延迟2s，完成后，接口回调到其内部类中，finish中完成数据获得。
     * @param finishedListener
     */
    @Override
    public void findItem(final onFinishedListener finishedListener) {
           //2s后数据加载完毕
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              finishedListener.finished(createList());
            }
        },2000);
    }


    private List<String> createList() {
        return Arrays.asList("Item 1",
                "Item 2",
                "Item 3",
                "Item 4",
                "Item 5",
                "Item 6",
                "Item 7");
    }
}
