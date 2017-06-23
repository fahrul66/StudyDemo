package findpx.com.brother.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import findpx.com.brother.myapplication.Cluster;
import findpx.com.brother.myapplication.KMeans;
import findpx.com.brother.myapplication.MyVector;

/**
 * 用于获取图片的颜色
 */
public class MainColorPicker {
    /**
     * 所有图片的像素点
     */
    private List<MyVector> mVectors;
    /**
     * 聚类颜色数量
     */
    private int mColorNumber = 5;
    /**
     * 图片尺寸的大小，宽高
     */
    public static final int PIC_SIZE = 36;

    /**
     * 传入图片路径
     *
     * @param path 图片路径
     */
    public MainColorPicker(String path) {
        //bitmap对象
        Bitmap bitmap = null;
        //判断图片是否存在
        File file = new File(path);
        if (path != null && file.exists()) {
            //图片压缩36*36
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeFile(path, opt);
            opt.inSampleSize = calSampleSize(opt, PIC_SIZE, PIC_SIZE);
            opt.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, opt);
            //获取所有的像素点
            mVectors = getAllColorVectors(bitmap);
            //释放Bitmap内存
            if (bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

    /**
     * 聚类几个颜色值
     *
     * @param mColorNumber 聚类颜色数量，默认为5
     */
    public void setColorNumber(int mColorNumber) {
        this.mColorNumber = mColorNumber;
    }

    /**
     * bitmap 压缩
     *
     * @return 返回图片的压缩比例
     */
    private int calSampleSize(BitmapFactory.Options opt, int reqWidth, int reqHeight) {
        int width = opt.outWidth;
        int height = opt.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthR = Math.round(width * 1.0f / reqWidth);
            int heightR = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthR, heightR);
        }
        return inSampleSize;
    }

    /**
     * 获得聚类的结果颜色值
     *
     * @return 最终的颜色值
     */
    public List<Integer> getMainListColor() {
        //K-means聚类
        KMeans kMeans = new KMeans(mVectors, mColorNumber);
        kMeans.startClustering();
        //聚类产生的几个颜色类
        List<Cluster> clusters = kMeans.getClusters();
        //存储的像素点颜色值
        List<MyVector> mVectors = new ArrayList<>();

        //排序，按照聚类的像素点的值
        Collections.sort(clusters, new Comparator<Cluster>() {
            @Override
            public int compare(Cluster c1, Cluster c2) {
                //按照聚类的像素点的多少排序
                return (c2.size() - c1.size());
            }
        });
        //获取其他聚类的中心颜色值
        for (int i = 0; i < clusters.size(); i++) {
            MyVector vector = clusters.get(i).getCenterVector();
            mVectors.add(vector);
        }
        //返回颜色值
        final List<Integer> colors = new ArrayList<>();
        for (MyVector vector : mVectors) {
            if (vector.size() == 3) {
                int r = vector.get(0).intValue();
                int g = vector.get(1).intValue();
                int b = vector.get(2).intValue();
                colors.add(Color.rgb(r, g, b));
            }
        }
        return colors;
    }

    /**
     * 只获取主要的颜色值，不开启线程
     *
     * @return
     */
    public static int getMainColor(String imgPath) {
        //进行图片的K-means聚类
        MainColorPicker tcp = new MainColorPicker(imgPath);
        //获取main color
        final List<Integer> colors = tcp.getMainListColor();
        //返回main color
        return colors.get(0);
    }

    /**
     * 获取图片的所有像素点RGB
     *
     * @return 所有的像素点颜色值的集合
     */
    private List<MyVector> getAllColorVectors(Bitmap bitmap) {
        List<MyVector> vectors = new ArrayList<>();
        //循环遍历获取像素点RGB
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                double r = getR(bitmap, i, j);
                double g = getG(bitmap, i, j);
                double b = getB(bitmap, i, j);
                //添加到集合中
                vectors.add(new MyVector(new double[]{r, g, b}));
            }
        }
        return vectors;
    }

    /**
     * 获取R值
     *
     * @param x x像素点
     * @param y y像素点
     * @return Red值
     */
    private double getR(Bitmap bitmap, int x, int y) {
        int rgb = bitmap.getPixel(x, y);
        int r = (rgb & 0xff0000) >> 16;
        return r;
    }

    /**
     * 获取G
     *
     * @param x x像素点
     * @param y y像素点
     * @return Green值
     */
    private double getG(Bitmap bitmap, int x, int y) {
        int rgb = bitmap.getPixel(x, y);
        int g = (rgb & 0xff00) >> 8;
        return g;
    }

    /**
     * 获取B
     *
     * @param x x像素点
     * @param y y像素点
     * @return Blue值
     */
    private double getB(Bitmap bitmap, int x, int y) {
        int rgb = bitmap.getPixel(x, y);
        int b = (rgb & 0xff);
        return b;
    }

}
