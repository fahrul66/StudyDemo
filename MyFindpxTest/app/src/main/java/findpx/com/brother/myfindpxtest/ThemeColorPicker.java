package findpx.com.brother.myfindpxtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 12/9/2016.
 */
public class ThemeColorPicker {

    private Bitmap mBitmap;

    //所有的像素点
    List<MyVector> vectors;
    //用来比较的中心值
    List<MyVector> base;
    //存储的聚类
    List<Cluster> clu = new ArrayList<>();

    private int colorNumber = 5;

    public void setColorNumber(int colorNumber) {
        this.colorNumber = colorNumber;
    }

    public ThemeColorPicker(String path) {
        //加载图片
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        mBitmap = BitmapFactory.decodeFile(path, opt);
        opt.inSampleSize = calSampleSize(opt, 36, 36);
        opt.inJustDecodeBounds = false;
        mBitmap = BitmapFactory.decodeFile(path, opt);
        //获取图片所有的像素点RGB
        vectors = generateColorVectors();
        initBaseColor();
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


        for (Cluster cluster : clusters) {
            MyVector vector = cluster.getCenterVector();
//            vectors.add(vector);
            //size为0，因为当单色图片时，rgb三维向量完全一致，聚类也就只有一个，另一个为空。
            if (vector.size() != 0) {

                System.out.println(rgbToHex(vector));
            }
        }

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
        }
        //获取像素最多的值
        vectors.add(clusters.get(index).getCenterVector());

//        new PaletteGUI(vectors,path);

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

//    public static void main(String[] args){
//        ThemeColorPicker picker = new ThemeColorPicker("/Users/Mike/Desktop/1.jpg");
//        picker.setColorNumber(5);
//        picker.getThemeColor();
//    }

    /**
     * 初始化值
     */
    private void initBaseColor() {
        base = new ArrayList<>();
        //赤橙黄绿青蓝紫
        base.add(new MyVector(new double[]{255, 0, 0}));
        //橙
        base.add(new MyVector(new double[]{255, 128, 0}));
        //黄
        base.add(new MyVector(new double[]{255, 255, 0}));
        //绿
        base.add(new MyVector(new double[]{0, 255, 0}));
        //青
        base.add(new MyVector(new double[]{0, 255, 255}));
        //蓝
        base.add(new MyVector(new double[]{0, 0, 255}));
        //紫
        base.add(new MyVector(new double[]{128, 0, 255}));
    }

    /**
     * 遍历比较，误差小于50上下，添加如集合中Cluster。
     * 比较最多元素的Cluster，输出。
     */
    public MyVector judgeColor() {
        //初始化
        initClusters();
        //遍历比较
        //差值距离数组
        double[][] distanceArray = new double[vectors.size()][base.size()];
        //所有像素点
        for (int i = 0; i < vectors.size(); i++) {
            //7个基本色，比较
            for (int j = 0; j < clu.size(); j++) {

                MyVector v1 = vectors.get(i);
                MyVector v2 = base.get(j);

                double distance = Distance.getSimDistance(v1, v2);
                //二维数组，每一列代表和7个基本色的比较结果共7列，每一行代表当前像素点的比较值。
                distanceArray[i][j] = distance;
            }
        }

        //遍历所有的值，添加到相应的聚类，平分所有的像素点
        //add vectors to different clusters
        for (int i = 0; i < distanceArray.length; i++) {
            //到K个点的距离数组
            double[] centerDistance = distanceArray[i];
            //获取相应的图片像素点
            MyVector vector = vectors.get(i);
            //后去最小值的种子点，假设k=3,0，1,2
            int index = getMinDistanceIndex(centerDistance);
            clu.get(index).addToCluster(vector);
        }

        //获取所有的clu
        int maxIndex = getMaxDistanceIndex(clu);
        return base.get(maxIndex);

    }

    /**
     * 得到数组中最大值的下标
     *
     * @param clu
     * @return
     */
    private int getMaxDistanceIndex(List<Cluster> clu) {

        double max = 0;
        int index = 0;
        for (int i = 0; i < clu.size(); i++) {
            int v = clu.get(i).size();
            if (v > max) {
                index = i;
                max = v;
            }
        }

        return index;
    }

    /**
     * 得到数组中最小值的下标
     *
     * @param arr
     * @return
     */
    private int getMinDistanceIndex(double[] arr) {

        double min = 99999999999999999999999.0;
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                index = i;
                min = arr[i];
            }
        }

        return index;
    }

    /**
     * 初始化聚类
     */
    private void initClusters() {
        clu.clear();
        //预先初始化所有的簇,7类
        for (int i = 0; i < base.size(); i++) {
            clu.add(new Cluster());
        }
    }
}
