package com.brother.picturechoose;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    //request code
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_CODE_PHOTO = 2;
    private static final int REQUEST_CODE_CROP = 3;
    //widget
    private Button btn, btn1, btn2, btn3;
    private ImageView img;
    private Bitmap bitmap;

    //保存的拍照图片文件
    private File file;
    private boolean first = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //动画，如果作为启动项，只有exit和reenter
        getWindow().setExitTransition(new Fade());
        getWindow().setReenterTransition(new Slide(Gravity.BOTTOM));

        getWindow().setAllowEnterTransitionOverlap(false);
        getWindow().setAllowReturnTransitionOverlap(false);
        setContentView(R.layout.activity_main);
        //初始化
        initView();
        //点击事件
        btn.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

/*        AnimatedVectorDrawableCompat a = AnimatedVectorDrawableCompat.create(this,R.drawable.anim_vector);
        img.setImageDrawable(a);
        ((AnimatedVectorDrawableCompat)img.getDrawable()).start();*/

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAfterTransition();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        img = (ImageView) findViewById(R.id.img);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                //选择图片
                selectPicture();
                break;
            case R.id.btn1:
                //拍照
                takePhotos();
                break;
            case R.id.btn2:
                if (first) {
                    first = false;
                    hideCircularReveal();
                } else {
                    first = true;
                    showCircularReveal();
                }

                break;
            case R.id.btn3:
                //共享元素和动画
                startActivity(new Intent(this, Main2Activity.class)
                        , ActivityOptionsCompat.makeSceneTransitionAnimation(this, img, "img").toBundle());
                break;

        }
    }

    /**
     * 隐藏
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void hideCircularReveal() {
        int x = img.getWidth() / 2;
        int y = img.getHeight() / 2;
        int max = Math.max(x * 2, y * 2);
        Animator anim = ViewAnimationUtils.createCircularReveal(img, x, y, max, 0);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                img.setVisibility(View.GONE);
            }
        });
        anim.start();
    }

    /**
     * 隐藏
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showCircularReveal() {
        int x = img.getWidth() / 2;
        int y = img.getHeight() / 2;
        int max = Math.max(x * 2, y * 2);
        Animator anim = ViewAnimationUtils.createCircularReveal(img, x, y * 2, 0, max);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationEnd(animation);
                img.setVisibility(View.VISIBLE);
            }
        });
        anim.start();
    }

    /**
     * 选择图片
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void selectPicture() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
            } else {
                i.setAction(Intent.ACTION_GET_CONTENT);
            }
            i.setType("image/*");
            i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //可以被打开的
            i.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(i, REQUEST_CODE);
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * 拍照
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePhotos() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent i = new Intent();
            i.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            file = new File(getExternalCacheDir() + "/" + System.currentTimeMillis() + ".png");
            i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(i, REQUEST_CODE_PHOTO);
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    /**
     * 回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = Uri.decode(uri.toString());
            Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Log.i(TAG, "onActivityResult: " + path);

            //4.4以上
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                String wholeId = DocumentsContract.getDocumentId(uri);
                Log.i(TAG, "onActivityResult..wholeId: " + wholeId);
                String id = wholeId.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor c = getContentResolver().query(contentUri, column, sel, new String[]{id}, null);
                if (c.moveToFirst()) {
                    path = c.getString(c.getColumnIndexOrThrow(column[0]));
                }
            } else {
                ContentResolver c = getContentResolver();
                Cursor cursor = c.query(contentUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(index);
            }

            Log.i(TAG, "onActivityResult: " + path);
            //设置图片
            reSizeImg(path);
            //拍照
        } else if (requestCode == REQUEST_CODE_PHOTO && resultCode == RESULT_OK) {
            //如果没有设置EXTRA_OUTPUT,则会生成一个bitmap的small picture,
            //如果设置了的话，则会返回一个完整的图片URI，指向本地path
            /*Bundle b = data.getExtras();
            Bitmap bit = (Bitmap) b.get("data");
            img.setImageBitmap(bit);*/

//            img.setImageURI(Uri.fromFile(file));
            reSizeImg(file.getPath());
            //截图
//            cropImageUri(Uri.fromFile(file), 150, 150, REQUEST_CODE_CROP);
        } else if (requestCode == REQUEST_CODE_CROP && resultCode == RESULT_OK) {
            Toast.makeText(this, "crop", Toast.LENGTH_SHORT).show();
            Bundle b = data.getExtras();
            Bitmap bit = (Bitmap) b.get("data");
            img.setImageBitmap(bit);
        }
    }

    /**
     * 截图
     */
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent i = new Intent("com.android.camera.action.CROP");
        i.setDataAndType(uri, "image/*");
        i.putExtra("crop", "true");
        //1:1的截图框
        i.putExtra("aspectX", 1);
        i.putExtra("aspectY", 1);
        //输出图片大小
        i.putExtra("outputX", outputX);
        i.putExtra("outputY", outputY);
        //是否缩放
        i.putExtra("scale", true);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //返回数据
        i.putExtra("return-data", true);
        i.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        i.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(i, requestCode);
    }

    /**
     * 压缩图片的大小
     *
     * @param path
     */
    private void reSizeImg(String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(path, opt);
        opt.inSampleSize = CalSampleSize(opt, 300, 300);
        opt.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, opt);
        img.setImageBitmap(bitmap);
    }

    /**
     * 计算缩放比例
     *
     * @param opt
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int CalSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int width = opt.outWidth;
        int height = opt.outHeight;
        if (reqHeight < height || reqWidth < width) {
            double rateWidth = Math.round(width * 1.0f / reqWidth);
            double rateHeight = Math.round(height * 1.0f / reqHeight);
            sampleSize = (int) Math.max(rateHeight, rateWidth);
        }
        return sampleSize;
    }
}
