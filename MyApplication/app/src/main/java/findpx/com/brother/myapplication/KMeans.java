package findpx.com.brother.myapplication;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * KMeans聚类算法
 * Created by Mike on 12/9/2016.
 */
public class KMeans {

    private List<MyVector> vectors = new ArrayList<>();
    private List<MyVector> centers = new ArrayList<>(); //质心

    private int numberOfCluster = 0;
    private List<Cluster> clusters = new ArrayList<>(); //储存所有的族

    private int numberOfIteration = 100;
    private boolean first;

    public KMeans(List<MyVector> vectors, int numberOfCluster) {
        this.vectors = vectors;
        this.numberOfCluster = numberOfCluster;
        initCenters();
        initClusters();
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    private void initClusters() {
        clusters.clear();
        //预先初始化所有的簇
        for (int i = 0; i < numberOfCluster; i++) {
            clusters.add(new Cluster());
        }
    }


    public void setNumberOfIteration(int numberOfIteration) {
        if (numberOfIteration > 0) {
            this.numberOfIteration = numberOfIteration;
        } else {
            System.out.println("numberOfIteration should be greater than 0");
        }
    }


    public void startClustering() {

        System.out.println("开始聚类");

        int counter = 0;
        List<MyVector> lastCenters = new ArrayList<>();

        boolean converged = false;

        while (!converged && counter < numberOfIteration) {
            //printCenters();
            System.out.println("第" + counter + "次迭代");

            double[][] distanceMatrix = new double[vectors.size()][numberOfCluster];

            //生成距离矩阵
            for (int i = 0; i < vectors.size(); i++) {
                for (int j = 0; j < centers.size(); j++) {
                    MyVector currentVector = vectors.get(i);
                    MyVector centerVector = centers.get(j);
                    //每一个点都和种子点比较，得到种子点到其他点的距离。
                    double distance = Distance.getSimDistance(centerVector, currentVector);
                    distanceMatrix[i][j] = distance;
                }
            }

            //打印距
//            if (!first) {
//                first = true;
//                printMatrix(distanceMatrix);
//            }

            //add vectors to different clusters
            for (int i = 0; i < distanceMatrix.length; i++) {
                //到K个点的距离数组
                double[] centerDistance = distanceMatrix[i];
                //获取相应的图片像素点
                MyVector vector = vectors.get(i);
                //后去最小值的种子点，假设k=3,0，1,2
                int index = getMinDistanceIndex(centerDistance);
                clusters.get(index).addToCluster(vector);
            }


            counter++;


            lastCenters.clear();
            lastCenters.addAll(centers);

            // printClusters();

            //Refresh centers
            for (int i = 0; i < numberOfCluster; i++) {

//                判断，那个聚类没有分配到像素点。没有分配到的则移除。
//                  避免干扰，中心点的取值。

                if (clusters.get(i).getVectors().size() != 0) {
                    MyVector vector = clusters.get(i).getCenterVector();
                    centers.set(i, vector);
                }
            }

            //打印中心点
//            printCenters();

            converged = isConverged(lastCenters);

            if (!converged && counter != numberOfIteration) {
                initClusters();
            }

        }

        System.out.println("聚类完成\n迭代次数" + counter);

        //printClusters();

    }

    /**
     * 检测是否收敛(中心点和上次比是否变化)
     *
     * @param lastCenters
     * @return
     */
    private boolean isConverged(List<MyVector> lastCenters) {

        if (lastCenters.size() != numberOfCluster) {
            return false;
        }

        boolean converge = true;

        for (int i = 0; i < this.centers.size(); i++) {

            MyVector thisVector = this.centers.get(i);
            MyVector thatVector = lastCenters.get(i);

            if (!thisVector.isSameVector(thatVector)) {
                converge = false;
            }

        }

        if (converge) {
            System.out.println("检测到收敛");
        }

        return converge;

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
     * 打印二维数组
     *
     * @param mat
     */
    private void printMatrix(double[][] mat) {

        int height = mat.length;
        int width = mat[0].length;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(mat[i][j] + "\t");
            }
            System.out.println();
        }
    }

    /**
     * 打印中心点
     */
    private void printCenters() {
        System.out.println("Printing Centers");
        for (MyVector vector : centers) {
            vector.printVector();
        }
    }

    /**
     * 打印所有聚类
     */
    private void printClusters() {
        for (Cluster cluster : clusters) {
            cluster.printCluster(false);
            System.out.println();
        }

    }

    /**
     * 初始质心
     * 从所有的向量中挑选 numberOfCluster 个
     */
    private void initCenters() {

        int sizeOfVectors = vectors.size();
        int size = sizeOfVectors / numberOfCluster;
        //其他点要远离这个点
        for (int i = 0; i < numberOfCluster; i++) {
            //初始点,随机值优化，分段进行区域选择，避免随机点都在一个区域
            int index = (int) (Math.random() * size) + size * i;
            Log.i("123", "initCenters: " + index + "all:" + sizeOfVectors);
            centers.add(vectors.get(index));
        }


        /**
         * 随机点选择，一张图片分成12份，然后平均取之间的颜色平均值。来最为中心点
         */
//        int counter = 12;
//        List<Cluster> list = new ArrayList<>();
//        List<MyVector> listV = new ArrayList<>();
//        int sizeNum = sizeOfVectors / counter;
//        for (int i = 0; i < counter; i++) {
//            Cluster c = new Cluster();
//            for (int j = i * sizeNum; j < i * sizeNum + sizeNum; j++) {
//                c.addToCluster(vectors.get(j));
//            }
//            list.add(c);
//        }
//
//        //获得每个类的平均RGB
//        for (Cluster c : list) {
//            MyVector myVector = c.getCenterVector();
//            listV.add(myVector);
//
//            int r = myVector.get(0).intValue();
//            int g = myVector.get(1).intValue();
//            int b = myVector.get(2).intValue();
//            //打印所有的pixel像素点大小
//            Log.i("123", "all pixel:" + sizeOfVectors);
//            Log.i("123", "R: " + r + "G: " + g + "B: " + b);
//        }
//
//        //然后从12个中获得K个中间值，进行聚类。
//        int colorSize = listV.size();
//        int colorNum = colorSize / numberOfCluster;
//        for (int i = 0; i < numberOfCluster; i++) {
//            int index = (int) (Math.random() * colorNum) + i * colorNum;
//            centers.add(listV.get(index));
//
//            int r = listV.get(index).get(0).intValue();
//            int g = listV.get(index).get(1).intValue();
//            int b = listV.get(index).get(2).intValue();
//
//            Log.i("123", "choose color ..R: " + r + "G: " + g + "B: " + b);
//        }
    }


}
