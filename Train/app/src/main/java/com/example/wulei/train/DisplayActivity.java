package com.example.wulei.train;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends BaseActivity implements View.OnClickListener {
    private EditText trainCode;
    private Button btn;
    private ListView lv;
    private ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        init();
    }

    private void init() {
        trainCode= (EditText) findViewById(R.id.trainId);
        btn= (Button) findViewById(R.id.btn_display);
        btn.setOnClickListener(this);
        lv= (ListView) findViewById(R.id.listView);
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(!TextUtils.isEmpty(trainCode.getText().toString())){

            queryDict qd = new queryDict();
            qd.execute(trainCode.getText().toString());
        }else{
            Toast.makeText(this,"输入不能为空！",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 内部类，异步操作处理
     */
    class queryDict extends AsyncTask<String, Integer, ArrayList<TrainTimeForm>> {

        @Override
        protected void onPostExecute(ArrayList<TrainTimeForm> result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.i("restur",result.size()+"");
            //适配器实例化
            adapter=new ArrayAdapter(getApplicationContext(), R.layout.listview,R.id.textview1,getData(result));
            lv.setAdapter( adapter);
            //设置listView的layoutAnimation动画

            Animation animation= AnimationUtils.loadAnimation(DisplayActivity.this,R.anim.translate);
            LayoutAnimationController layoutAnimationController=new LayoutAnimationController(animation);
            layoutAnimationController.setDelay(0.5f);//延迟0.5
            layoutAnimationController.setOrder(LayoutAnimationController.ORDER_REVERSE);//倒序播放
            lv.setLayoutAnimation(layoutAnimationController); //设置布局子控件动画
        }

        private List<String> getData(ArrayList<TrainTimeForm> result) {
            // TODO Auto-generated method stub
            List<String> list=new ArrayList<>();
            for(TrainTimeForm ttf:result){
                //获取数据
                String arriveTime=ttf.getArriveTime();
                String startTime=ttf.getStartTime();
                String km=ttf.getKm();
                String station=ttf.getTrainStation();
                //判断
                if(arriveTime.equals("anyType{}")){
                    arriveTime="0";
                }else if(startTime.equals("anyType{}")){
                    startTime="0";
                }
                //添加到数组中
                list.add("车站："+station+"\r\n到达时间："+arriveTime
                            +"\r\n开车时间："+startTime+"\r\n距离始发站公里："+km);
            }
            return list;
        }

        @Override
        protected ArrayList<TrainTimeForm> doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            ArrayList<TrainTimeForm> al=null;
            try {
                al = webDict.getDictInfo(arg0[0]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return al;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()){
            case R.id.about:
//			Toast.makeText(getApplicationContext(), "何龙龙  列车查询", Toast.LENGTH_LONG).show();
                showDialog("作者：武磊 \r\n作品：列车查询");
                return true;
            case R.id.exit:
                showDialogQuite();
                return true;
        }
        return false;

    }
    /**
     * 弹出框提示
     */
    private void showDialog(String ms){
        AlertDialog.Builder ab=new AlertDialog.Builder(this);
        ab.setMessage(ms)
                .show();
    }
    private void showDialogQuite(){
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("提示")
                .setMessage("是否退出")
                .setPositiveButton("确定",new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        BaseActivity.finishAll();
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
