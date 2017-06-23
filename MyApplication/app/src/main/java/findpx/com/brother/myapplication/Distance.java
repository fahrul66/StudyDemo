package findpx.com.brother.myapplication;

/**
 * Created by Mike on 12/9/2016.
 */
public class Distance {


    /**
     * 计算欧式距离
     * @param vector1 颜色向量1
     * @param vector2 颜色向量2
     * @return 返回两个颜色向量之间的欧几里得距离
     */
    public static double getSimDistance(MyVector vector1, MyVector vector2) {
        //r,g,b的求差平方和
        double sumOfSquare = 0.0;
        //遍历R,G,B值
        for (int i = 0; i < vector1.size(); i++) {
            double vector1Score = vector1.get(i);
            double vector2Score = vector2.get(i);
            sumOfSquare += Math.pow(vector1Score - vector2Score, 2);
        }
        return Math.sqrt(sumOfSquare);
    }

}
