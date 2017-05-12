package findpx.com.brother.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import findpx.com.brother.myapplication.Cluster;
import findpx.com.brother.myapplication.KMeans;
import findpx.com.brother.myapplication.MyVector;

/**
 * Created by Mike on 12/9/2016.
 */
public class ThemeColorPicker {

    private Bitmap mBitmap;

    List<MyVector> vectors;

    private String path;

    private int colorNumber = 5;

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public ThemeColorPicker(String path) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        mBitmap = BitmapFactory.decodeFile(path, opt);
        opt.inSampleSize = calSampleSize(opt, 36, 36);
        opt.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(path, opt);

        vectors = generateColorVectors();
        this.path = path;
    }

    /**
     * bitmap 压缩
     *
     * @return
     */
    private int calSampleSize(BitmapFactory.Options opt, int i, int i1) {
        int width = opt.outWidth;
        int height = opt.outHeight;
        int inSampleSize = 1;
        if (width > i || height > i1) {
            int widthR = Math.round(width * 1.0f / i);
            int heightR = Math.round(height * 1.0f / i1);
            inSampleSize = Math.max(widthR, heightR);
        }
        return inSampleSize;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public List<MyVector> getThemeColor() {

        KMeans kMeans = new KMeans(vectors, colorNumber);
        kMeans.startClustering();
        List<Cluster> clusters = kMeans.getClusters();

        List<MyVector> vectors = new ArrayList<>();

        /**
         * 获取像素最多的值
         */
        int maxNum = 0;
        int index = 0;
        for (int i = 0; i < clusters.size(); i++) {
            int temp = clusters.get(i).size();
            if (temp > maxNum) {
                maxNum = temp;
                index = i;
            }
            Log.i("123", "各个聚类的像素点值: " + temp);
        }
        Log.i("123", "最大值max:" + maxNum);
        //获取像素最多的值
        vectors.add(clusters.get(index).getCenterVector());

        for (int i = 0; i < clusters.size(); i++) {
            if (i != index) {
                MyVector vector = clusters.get(i).getCenterVector();
                vectors.add(vector);
            }

        }
        for (Cluster cluster : clusters) {
            MyVector vector = cluster.getCenterVector();
//            vectors.add(vector);
            //size为0，因为当单色图片时，rgb三维向量完全一致，聚类也就只有一个，另一个为空。
            if (vector.size() != 0) {

                System.out.println(rgbToHex(vector));
            }
        }

        return vectors;
    }

    private String rgbToHex(MyVector vector) {

        int r = vector.get(0).intValue();
        int g = vector.get(1).intValue();
        int b = vector.get(2).intValue();
        String hex = String.format("#%02x%02x%02x", r, g, b);
        return hex;
    }

    private List<MyVector> generateColorVectors() {
        List<MyVector> vectors = new ArrayList<>();

        for (int i = 0; i < mBitmap.getWidth(); i++) {
            for (int j = 0; j < mBitmap.getHeight(); j++) {
                double r = getR(i, j);
                double g = getG(i, j);
                double b = getB(i, j);

                vectors.add(new MyVector(new double[]{r, g, b}));

            }
        }

        return vectors;

    }


    private double getR(int x, int y) {
        int rgb = mBitmap.getPixel(x, y);
        int r = (rgb & 0xff0000) >> 16;
        return r;
    }

    private double getG(int x, int y) {
        int rgb = mBitmap.getPixel(x, y);
        int g = (rgb & 0xff00) >> 8;
        return g;
    }

    private double getB(int x, int y) {
        int rgb = mBitmap.getPixel(x, y);
        int b = (rgb & 0xff);
        return b;
    }

}
