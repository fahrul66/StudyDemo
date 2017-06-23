package findpx.com.brother.myapplication;

import java.util.Vector;

/**
 * RGB颜色值保存，颜色向量
 */
public class MyVector extends Vector<Double> {

    public MyVector() {}

    public MyVector(double[] arr) {
        addByArray(arr);
    }

    public MyVector(int size) {
        for (int i = 0; i < size; i++) {
            add(0.0);
        }
    }
    /**
     * 添加RGB
     * @param arr 数组对象
     */
    public void addByArray(double[] arr) {
        for (Double val : arr) {
            add(val);
        }
    }

    /**
     * 判断是否是两个相同的vector颜色向量
     * @param vector 比较的对象
     * @return true 相同的，false 不同的
     */
    public boolean isSameVector(MyVector vector) {
        //向量大小是否相同
        if (vector.size() != size()) {
            return false;
        }
        boolean same = true;
        //比较每个R，G，B是否相同
        for (int i = 0; i < vector.size(); i++) {
            if (!this.get(i).equals(vector.get(i))) {
                same = false;
            }
        }
        return same;
    }

}
