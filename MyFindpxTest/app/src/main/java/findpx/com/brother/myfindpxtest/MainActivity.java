package findpx.com.brother.myfindpxtest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button mBtn;
    Button mBtn1, mBtn2, mBtn3, mBtn4, mBtn5;


    private TextView tv;
    private Bitmap bitM;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        tv = (TextView) findViewById(R.id.sample_text);
        imageView = (ImageView) findViewById(R.id.imge_bit);
        mBtn1 = (Button) findViewById(R.id.btn1);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn3 = (Button) findViewById(R.id.btn3);
        mBtn4 = (Button) findViewById(R.id.btn4);
        mBtn5 = (Button) findViewById(R.id.btn5);

        //init
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //权限获取
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                //图片处理
                cvtImg();

            }
        });


    }

    /**
     * 图片的处理
     */
    private void cvtImg() {


        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(intent, 1);


    }

    /**
     * 清除btn的背景色
     */
    private void clearBtnBg() {
//        mBtn.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn1.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn2.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn3.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn4.setBackgroundColor(Color.parseColor("#ffffff"));
        mBtn5.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clearBtnBg();

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            final String path = getPath(this, uri);
            //rgb
//            double[] rgb = colorFromJNI(path);
//            tv.setText(stringFromJNI());

            final ProgressDialog p = new ProgressDialog(this);
            p.setMessage("loading..");
            p.setCanceledOnTouchOutside(false);
            p.show();

            //开子线程
            new Thread(new Runnable() {
                @Override
                public void run() {

                    final List<Integer> colors = new ArrayList<>();
                    int ColorNum = 5;
                    ThemeColorPicker tcp = new ThemeColorPicker(path);
                    final MyVector vector = tcp.judgeColor();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            p.dismiss();

                            int r = vector.get(0).intValue();
                            int g = vector.get(1).intValue();
                            int b = vector.get(2).intValue();

                            mBtn.setBackgroundColor(Color.rgb(r, g, b));
                        }
                    });

                }
            }).start();

            //压缩显示图片
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            bitM = BitmapFactory.decodeFile(path, opt);
            opt.inSampleSize = calSampleSize(opt, 200, 200);
            opt.inJustDecodeBounds = false;
            bitM = BitmapFactory.decodeFile(path, opt);
            imageView.setImageBitmap(bitM);

        }

    }

    /**
     * 压缩
     *
     * @param opt
     * @param width
     * @param height
     * @return
     */
    private int calSampleSize(BitmapFactory.Options opt, int width, int height) {
        int sampleSize = 1;
        int realityWidth = opt.outWidth;
        int realityHeight = opt.outHeight;
        if (realityHeight > height || realityWidth > width) {
            int widthRate = Math.round(realityWidth * 1.0f / width);
            int heightRate = Math.round(realityHeight * 1.0f / height);
            sampleSize = Math.max(widthRate, heightRate);
        }
        return sampleSize;
    }

    /**
     * 获取路径
     *
     * @param context
     * @param uri
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}
