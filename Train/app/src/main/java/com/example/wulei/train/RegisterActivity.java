package com.example.wulei.train;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class RegisterActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private EditText user, password, name;
    private RadioGroup radioGroup;
    private Spinner college, profession, date;
    private Button btn;
    private RadioButton rb;
    private LocalSqlite ls;
    private String[] colleges = null;
    private String[][] professions;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局全屏延伸至status bar
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getSupportActionBar().hide();
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_register);
        //初始化
        init();
        //适配器
        colleges = new String[]{"学院：", "信息工程学院", "土木学院", "机电学院", "医学院"};
        SpinnerAdapter col = new ArrayAdapter(this, android.R.layout.simple_list_item_1, colleges);
        college.setAdapter(col);
        professions = new String[][]{{"电子信息工程", "通信工程", "计算机科学"},
                {"土木工程", "工程造价", "工程管理"},
                {"生物医学", "测控", "机械制造", "电气"},
                {"临床医学", "医学影像", "药学"}};
        SpinnerAdapter pro = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new String[]{"专业："});
        profession.setAdapter(pro);
        String[] dates = {"入学学年:", "2010.9", "2011.9", "2012.9", "2013.9", "2014.9", "2015.9"};
        SpinnerAdapter da = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dates);
        date.setAdapter(da);
    }

    /**
     * 控件初始化
     */
    private void init() {
        user = (EditText) findViewById(R.id.user_register);
        password = (EditText) findViewById(R.id.password_register);
        name = (EditText) findViewById(R.id.name);
        radioGroup = (RadioGroup) findViewById(R.id.sex);
        college = (Spinner) findViewById(R.id.college);
        profession = (Spinner) findViewById(R.id.profession);
        date = (Spinner) findViewById(R.id.date);
        btn = (Button) findViewById(R.id.btn_register);
        btn.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        college.setOnItemSelectedListener(this);
        profession.setOnItemSelectedListener(this);
        ls = new LocalSqlite(this, "userInfo", null, 1);
    }

    /**
     * radioGroup的监听器
     *
     * @param radioGroup
     * @param i
     */
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int checkedId = radioGroup.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(checkedId);
    }

    /**
     * 点击
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
                break;
        }
    }

    /**
     * 注册信息录入数据库
     */
    private void register() {
        //获得注册信息
        String userStr = user.getText().toString();
        String passworodStr = password.getText().toString();
        String nameStr = name.getText().toString();
        String collegeStr = college.getSelectedItem().toString();
        String professionStr = profession.getSelectedItem().toString();
        String dateStr = date.getSelectedItem().toString();
        //判断
        if (TextUtils.isEmpty(userStr) || TextUtils.isEmpty(passworodStr) || TextUtils.isEmpty(nameStr) ||
                rb == null || collegeStr.equals("学院：") || professionStr.equals("专业：") ||
                dateStr.equals("入学学年:")) {
            Toast.makeText(this, "输入不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            String sexStr = rb.getText().toString();
            //写入数据库
            if (ls.register(userStr, passworodStr, nameStr, sexStr, collegeStr, professionStr, dateStr)) {
                Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "用户名或姓名已存在！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * spinner的选中监听器
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.college:
                Log.i("POSITION_ada", i + "");
                if (i > 0) {
                    SpinnerAdapter pro = new ArrayAdapter(this, android.R.layout.simple_list_item_1, professions[i - 1]);
                    profession.setAdapter(pro);
                } else {
                    SpinnerAdapter pro = new ArrayAdapter(this, android.R.layout.simple_list_item_1, new String[]{"专业："});
                    profession.setAdapter(pro);
                }
                break;
            case R.id.profession:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        switch (item.getItemId()) {
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
    private void showDialog(String ms) {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage(ms)
                .show();
    }

    private void showDialogQuite() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("提示")
                .setMessage("是否退出")
                .setPositiveButton("确定", new Dialog.OnClickListener() {

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
