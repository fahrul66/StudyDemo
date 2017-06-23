package findpx.com.brother.myapplication;

import java.util.ArrayList;
import java.util.List;

/**
 * KMeans聚类算法
 */
public class KMeans {
    /**
     * 所有的颜色点
     */
    private List<MyVector> mVectors = new ArrayList<>();
    /**
     * 质心
     */
    private List<MyVector> mCenters = new ArrayList<>();
    /**
     * 聚类的数量
     */
    private int mNumOfCluster = 0;
    /**
     * 储存所有的聚类
     */
    private List<Cluster> mClusters = new ArrayList<>();
    /**
     * 迭代的次数，超过即停止
     */
    private int mNumOfIteration = 100;

    /**
     * 构造方法初始化数据
     *
     * @param mVectors      图片上所有的像素点数
     * @param mNumOfCluster 要聚类颜色的数量
     */
    public KMeans(List<MyVector> mVectors, int mNumOfCluster) {
        this.mVectors = mVectors;
        this.mNumOfCluster = mNumOfCluster;
        //初始化中心点
        initCenters();
        //初始化聚类
        initClusters();
    }

    /**
     * 获得所有的聚类
     *
     * @return
     */
    public List<Cluster> getClusters() {
        return mClusters;
    }

    /**
     * 初始化
     */
    private void initClusters() {
        mClusters.clear();
        //预先初始化所有的簇
        for (int i = 0; i < mNumOfCluster; i++) {
            mClusters.add(new Cluster());
        }
    }

    /**
     * 开始聚类
     */
    public void startClustering() {

        //迭代次数
        int counter = 0;
        //上一次的中心点
        List<MyVector> lastCenters = new ArrayList<>();
        //不收敛
        boolean converged = false;
        //循环迭代
        while (!converged && counter < mNumOfIteration) {
            //RGB颜色向量值
            double[][] distanceMatrix = new double[mVectors.size()][mNumOfCluster];

            //生成距离矩阵
            for (int i = 0; i < mVectors.size(); i++) {
                for (int j = 0; j < mCenters.size(); j++) {
                    MyVector currentVector = mVectors.get(i);
                    MyVector centerVector = mCenters.get(j);
                    //每一个点都和种子点比较，得到种子点到其他点的距离。
                    double distance = Distance.getSimDistance(centerVector, currentVector);
                    distanceMatrix[i][j] = distance;
                }
            }

            //add mVectors to different mClusters
            for (int i = 0; i < distanceMatrix.length; i++) {
                //到K个点的距离数组
                double[] centerDistance = distanceMatrix[i];
                //获取相应的图片像素点
                MyVector vector = mVectors.get(i);
                //获取最小值的种子点，假设k=3,0，1,2
                int index = getMinDistanceIndex(centerDistance);
                //添加到相应的聚类中去
                mClusters.get(index).addToCluster(vector);
            }
            //次数
            counter++;

            //清除上次的中心点
            lastCenters.clear();
            lastCenters.addAll(mCenters);
            //Refresh mCenters
            for (int i = 0; i < mNumOfCluster; i++) {
//                判断，那个聚类没有分配到像素点。没有分配到的则移除。
//                避免干扰，中心点的取值。
                if (mClusters.get(i).getVectors().size() != 0) {
                    MyVector vector = mClusters.get(i).getCenterVector();
                    mCenters.set(i, vector);
                }
            }

            //是否收敛
            converged = isConverged(lastCenters);
            if (!converged && counter != mNumOfIteration) {
                initClusters();
            }
        }
    }

    /**
     * 检测是否收敛(中心点和上次比是否变化)
     *
     * @param lastCenters
     * @return true 收敛，false 不收敛
     */
    private boolean isConverged(List<MyVector> lastCenters) {

        if (lastCenters.size() != mNumOfCluster) {
            return false;
        }
        boolean converge = true;
        for (int i = 0; i < this.mCenters.size(); i++) {
            MyVector thisVector = this.mCenters.get(i);
            MyVector thatVector = lastCenters.get(i);
            if (!thisVector.isSameVector(thatVector)) {
                converge = false;
            }
        }

        return converge;

    }

    /**
     * 得到数组中最小值的下标
     *
     * @param arr 数组
     * @return 数组最小值的角标
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
     * 初始质心
     * 从所有的向量中挑选 mNumOfCluster 个
     */
    private void initCenters() {
        //获取所有的颜色数量
        int sizeOfVectors = mVectors.size();
        int size = sizeOfVectors / mNumOfCluster;
        //其他点要远离这个点
        for (int i = 0; i < mNumOfCluster; i++) {
            //初始点,随机值优化，分段进行区域选择，避免随机点都在一个区域
            int index = (int) (Math.random() * size) + size * i;
            mCenters.add(mVectors.get(index));
        }
    }


}
