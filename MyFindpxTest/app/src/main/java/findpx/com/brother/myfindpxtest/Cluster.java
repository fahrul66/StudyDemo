package findpx.com.brother.myfindpxtest;

import java.util.ArrayList;

/**
 * 聚类
 * Created by Mike on 12/9/2016.
 */
public class Cluster {

    String tag = "";

    private ArrayList<MyVector> vectors = new ArrayList<>();
    private int eachVectorSize = 0;

    public ArrayList<MyVector> getVectors(){
        return vectors;
    }

    public void addToCluster(MyVector vector){

//        vectors.clear();
        this.vectors.add(vector);
        this.eachVectorSize = vector.size();
    }

    public void printCluster(boolean withTag){
        if (withTag){
            System.out.println(tag);
        }
        for (int i=0;i<vectors.size();i++){
            MyVector vector = vectors.get(i);
            System.out.print("Vector "+i+": ");
            vector.printVector();
        }
    }

    public void clearCluster(){
        this.vectors.clear();
    }

    /**
     * 计算簇的中点
     * @return
     */
    public MyVector getCenterVector(){
        //预先初始化定长Vector
        MyVector vector = new MyVector(eachVectorSize);

        for (MyVector tempVector:vectors){
            for (int i=0; i < tempVector.size();i++){
                Double original = vector.get(i);
                //计算每一个聚类的RGB的各个值的和。
                vector.set(i,original+tempVector.get(i));
            }
        }

        for (int i=0;i<vector.size();i++){
            //计算每个聚类的RGB平均值
            vector.set(i,vector.get(i)/this.vectors.size());
        }

        return vector;
    }

    /**
     * 返回一个聚类中的颜色值
     * @return
     */
    public int size(){
        return vectors.size();
    }


//    public static void main(String[] args){
//        Cluster cluster = new Cluster();
//        MyVector myVector = new MyVector(new double[]{1.0, 2.0,3.0});
//        cluster.addToCluster(myVector);
//        MyVector myVector2 = new MyVector(new double[]{5.0, 8.0,55.0});
//        cluster.addToCluster(myVector2);
//
//        Cluster cluster2 = new Cluster();
//        cluster2.addToCluster(myVector2);
//        cluster2.addToCluster(myVector);
//
//
//
//
//    }

}
