package com.example.wulei.train;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText user,password;
    private Button btn;
    private TextView tv;
    private LocalSqlite ls;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //|View.SYSTEM_UI_FLAG_LAYOUT_STABLE 当隐藏了status bar或者navigation bar的时候要布局不会因为
        //system bar的出现而使布局发生变化
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_login);
        init();
    }
    /**
     * 初始化控件
      */
    private void init(){
        user= (EditText) findViewById(R.id.user);
        password= (EditText) findViewById(R.id.password);
        btn= (Button) findViewById(R.id.button);
        tv= (TextView) findViewById(R.id.register);
        btn.setOnClickListener(this);
        tv.setOnClickListener(this);
        ls = new LocalSqlite(this,"userInfo",null,1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                loginJudge();
                break;
            case R.id.register://跳转到登陆界面
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     *登陆判断
     */
    private void loginJudge() {
        //获得用户名和密码
        String user1 = user.getText().toString();
        String password1 = password.getText().toString();
        if(TextUtils.isEmpty(user1) || TextUtils.isEmpty(password1)){
            Toast.makeText(this,"账号或密码为空！",Toast.LENGTH_SHORT).show();
        }else if(ls.login(user1,password1)){
            Toast.makeText(this,"登陆成功",Toast.LENGTH_SHORT).show();
            //跳转
            Intent intent=new Intent(this,DisplayActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this,"找不到此账号或密码！",Toast.LENGTH_SHORT).show();
        }
    }
}
