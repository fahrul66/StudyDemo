package findpx.com.brother.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    /**
     * 按钮
     */
    Button mBtn, mBtn1, mBtn2, mBtn3, mBtn4, mBtn5;
    /**
     * bitmap和imageView对象
     */
    private Bitmap mBitmap;
    private ImageView mImgv;
    /**
     * 总布局
     */
    RelativeLayout mRelativeLayout;
    /**
     * 跳转常量
     */
    public static final int REQUEST_CODE = 1;
    public static final int REQUEST_CODE_PIC = 2;
    /**
     * 图片大小
     */
    public static final int PIC_SIZE = 200;
    /**
     * 是否是4.4以上版本，打开相册的判断
     */
    private boolean isKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // init views
        mImgv = (ImageView) findViewById(R.id.imge_bit);
        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn3);
        mBtn4 = (Button) findViewById(R.id.btn4);
        mBtn5 = (Button) findViewById(R.id.btn5);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.activity_main);
        //init
        mBtn = (Button) findViewById(R.id.btn);
        //点击事件
        mBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (isKitkat) {
                    //权限获取
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                    } else {
                        //图片处理
                        openImgv();
                    }
                } else {
                    //图片处理
                    openImgv();
                }
            }
        });


    }

    /**
     * android 6.0权限获取
     *
     * @param requestCode  请求码
     * @param permissions  获得的权限
     * @param grantResults 权限的授权情况
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //获取权限后处理图片
                openImgv();
            }
        }
    }

    /**
     * 图片的处理
     */
    private void openImgv() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (isKitkat) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        startActivityForResult(intent, REQUEST_CODE_PIC);
    }

    /**
     * 清除btn的背景色
     */
    private void clearBtnBg() {
        mBtn1.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn2.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn3.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn4.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn5.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    /**
     * 回调，获取图片后的回调
     *
     * @param requestCode 请求码
     * @param resultCode  返回的结果
     * @param data        传递的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //清除button的背景颜色
        clearBtnBg();
        if (requestCode == REQUEST_CODE_PIC && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = Uri.decode(uri.toString());
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            //4.4以上获取图片的path
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String wholeId = DocumentsContract.getDocumentId(uri);
                String id = wholeId.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor c = getContentResolver().query(contentUri, column, sel, new String[]{id}, null);
                if (c.moveToFirst()) {
                    path = c.getString(c.getColumnIndexOrThrow(column[0]));
                }
                //4.4以下获取图片path
            } else {
                ContentResolver c = getContentResolver();
                Cursor cursor = c.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                cursor.moveToFirst();
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(index);
            }

            //进度条显示
            final ProgressDialog p = new ProgressDialog(this);
            p.setMessage("loading..");
            p.setCanceledOnTouchOutside(false);
            p.show();

            //开子线程
            final String finalPath = path;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //进行图片的K-means聚类
                    int ColorNum = 5;
                    MainColorPicker tcp = new MainColorPicker(finalPath);
                    tcp.setColorNumber(ColorNum);
                    //获取main color
                    final List<Integer> colors = tcp.getMainListColor();
                    //主线程更新UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //dialog消失
                            p.dismiss();
                            //循环遍历设置主要的颜色值
                            for (int i = 0; i < colors.size(); i++) {
                                //main color
                                mRelativeLayout.setBackgroundColor(colors.get(0));
                                mBtn1.setBackgroundColor(colors.get(0));
                                //其他聚类的颜色设置，判断
                                if (colors.size() >= 2) {

                                    mBtn2.setBackgroundColor(colors.get(1));
                                }
                                if (colors.size() >= 3) {

                                    mBtn3.setBackgroundColor(colors.get(2));
                                }
                                if (colors.size() >= 4) {

                                    mBtn4.setBackgroundColor(colors.get(3));
                                }
                                if (colors.size() >= 5) {

                                    mBtn5.setBackgroundColor(colors.get(4));
                                }
                            }
                        }
                    });

                }
            }).start();

            //压缩显示图片
            compressImgv(path);


        }

    }

    /**
     * 压缩并设置图片
     *
     * @param path 图片的路径
     */
    private void compressImgv(String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        mBitmap = BitmapFactory.decodeFile(path, opt);
        opt.inSampleSize = calSampleSize(opt, PIC_SIZE, PIC_SIZE);
        opt.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(path, opt);
        mImgv.setImageBitmap(mBitmap);
    }

    /**
     * 压缩
     *
     * @param opt       Options对象
     * @param reqWidth  期待的图片宽
     * @param reqHeight 期待的图片的高
     * @return 返回压缩的比例
     */
    private int calSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int realityWidth = opt.outWidth;
        int realityHeight = opt.outHeight;
        if (realityHeight > reqHeight || realityWidth > reqWidth) {
            int reqWidthRate = Math.round(realityWidth * 1.0f / reqWidth);
            int reqHeightRate = Math.round(realityHeight * 1.0f / reqHeight);
            sampleSize = Math.max(reqWidthRate, reqHeightRate);
        }
        return sampleSize;
    }

}
