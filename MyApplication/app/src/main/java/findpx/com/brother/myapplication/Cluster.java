package findpx.com.brother.myapplication;

import java.util.ArrayList;

/**
 * 聚类
 */
public class Cluster {
    /**
     * 本类维护的一个颜色集合--聚类
     */
    private ArrayList<MyVector> mVectors = new ArrayList<>();
    /**
     * 聚类的大小
     */
    private int mEachVectorSize = 0;

    /**
     * 返回当前的聚类
     * @return
     */
    public ArrayList<MyVector> getVectors() {
        return mVectors;
    }

    /**
     * 添加到集合中
     * @param vector
     */
    public void addToCluster(MyVector vector) {
        this.mVectors.add(vector);
        this.mEachVectorSize = vector.size();
    }

    /**
     * 计算当前聚类的的中点，也就是颜色的向量的平均值
     * @return
     */
    public MyVector getCenterVector() {
        //预先初始化定长Vector
        MyVector vector = new MyVector(mEachVectorSize);
        //遍历当前聚类
        for (MyVector tempVector : mVectors) {
            for (int i = 0; i < tempVector.size(); i++) {
                Double original = vector.get(i);
                //计算每一个聚类的RGB的各个值的和。
                vector.set(i, original + tempVector.get(i));
            }
        }
        //各个聚类的平均值
        for (int i = 0; i < vector.size(); i++) {
            //计算每个聚类的RGB平均值
            vector.set(i, vector.get(i) / this.mVectors.size());
        }
        return vector;
    }

    /**
     * 返回一个聚类中的颜色值
     * @return
     */
    public int size() {
        return mVectors.size();
    }

}
