package com.example.wulei.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.tv)
    TextView textView;
    @BindView(R.id.bt)
    Button button;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            String s = bundle.getString("re", "error");


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        /*OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url("http://www.tuicool.com/articles/NnuIva")
                .build();

        //call
        Call call = okHttpClient.newCall(request);

        //异步请求,使用execute是同步阻塞式的。
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                errorWarn();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                Log.i(TAG, "onResponse: "+response.body().toString());
//                Bundle bundle = new Bundle();
//                bundle.putString("re",response.body().string());
//                 Message message = new Message();
//                message.setData(bundle);
//                handler.sendMessage(message);

                *
                 * imageGetter

                Html.ImageGetter imgGetter = new Html.ImageGetter() {
                    public Drawable getDrawable(String source) {
                        Drawable drawable = null;
                        URL url;
                        try {
                            url = new URL(source);
                            drawable = Drawable.createFromStream(url.openStream(), "");  //获取网路图片
                        } catch (Exception e) {
                            return null;
                        }
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
                                .getIntrinsicHeight());
                        return drawable;
                    }
                };

                *
                 * text设置html文本

                final Spanned s = Html.fromHtml(response.body().string(),imgGetter,null);

                *
                 * 传回主线程

                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                              textView.setText(s);
                      }
                  });
            }
        });*/
    }

    /**
     * 错误提示
     */
//    private void errorWarn() {
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//        AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
//        a.setTitle("Error").setMessage("发生错误！").show();
//            }
//        });
//
//    }

    /**
     * 接口，用来拼接url的查询参数
     * retrofit2.0后，返回值固定为Call<T>
     * T 可以是json的POJO(plain old java object),前提是在addConverterFactory(GsonConverterFactory.create())
     * T 如果不想参数化转换，也可以直接填入 RequestBody
     */
    interface Web {
        @GET("p/{id}")
        retrofit2.Call<ResponseBody> list(@Path("id") String s);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.jianshu.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        Web web = retrofit.create(Web.class);
        retrofit2.Call<ResponseBody> call = web.list("4a3177b57949");

        /**
         * 异步调度，主线程回调
         */
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                textView.append("\r\n response code................" + response.code() + Html.fromHtml(s));
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 跳转
     */
    @OnClick(R.id.bt)
     void onClick() {
       startActivity(new Intent(this,Second.class));
    }
}
