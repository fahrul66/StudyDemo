package com.brother.xml2excel;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * wulei 2017/05/19
 */
public class MainActivity extends AppCompatActivity {

    //存储键值
    List<StringDemo> list = null;
    //对象
    StringDemo sd = null;
    //工作薄
    private WritableWorkbook book;
    //目录
    File file = null;
    //key对应的value值
    String value = null;

    //所有的string文件
    String[] s = new String[]{"values/strings.xml", "values/strings_v1.xml",
            "values-da/strings.xml", "values-da/strings_v1.xml",
            "values-de/strings.xml", "values-de/strings_v1.xml",
            "values-es/strings.xml", "values-es/strings_v1.xml",
            "values-fi/strings.xml", "values-fi/strings_v1.xml",
            "values-fr/strings.xml", "values-fr/strings_v1.xml",
            "values-it/strings.xml", "values-it/strings_v1.xml",
            "values-ja/strings.xml", "values-ja/strings_v1.xml",
            "values-ko/strings.xml", "values-ko/strings_v1.xml",
            "values-nb/strings.xml", "values-nb/strings_v1.xml",
            "values-nl/strings.xml", "values-nl/strings_v1.xml",
            "values-pt/strings.xml", "values-pt/strings_v1.xml",
            "values-sv/strings.xml", "values-sv/strings_v1.xml",
            "values-zh/strings.xml", "values-zh/strings_v1.xml",
            "values-zh-rCN/strings.xml", "values-zh-rCN/strings_v1.xml",
            "values-zh-rHK/strings.xml", "values-zh-rHK/strings_v1.xml",
            "values-zh-rMO/strings.xml", "values-zh-rMO/strings_v1.xml",
            "values-zh-rSG/strings.xml", "values-zh-rSG/strings_v1.xml",
            "values-zh-rTW/strings.xml", "values-zh-rTW/strings_v1.xml",
    };
    //所有的文件夹
    String[] sDir = new String[]{
            "values",
            "values-da",
            "values-de",
            "values-es",
            "values-fi",
            "values-fr",
            "values-it",
            "values-ja",
            "values-ko",
            "values-nb",
            "values-nl",
            "values-pt",
            "values-sv",
            "values-zh",
            "values-zh-rCN",
            "values-zh-rHK",
            "values-zh-rMO",
            "values-zh-rSG",
            "values-zh-rTW"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 循环遍历
                 */
                for (int j = 0, k = 0; j < s.length; j++) {
                    //跳过判断
                    if (j % 2 != 0) {
                        continue;
                    }

                    //数组重建
                    list = new ArrayList<>();
                    //读取xml的值，并保存到list中
                    readXML(s[j], null, true);
                    //遍历是否是引用的
                    for (int i = 0; i < list.size(); i++) {
                        String name = list.get(i).getName();
                        String key = list.get(i).getText();
                        //一直查找遍历，直到找到
                        while (key != null && key.startsWith("@")) {
                            //有@的话，去空格，防止干扰
                            String keyTrim = key.trim();
                            //拆分为两部分
                            String[] keyA = keyTrim.split("/");
                            //是否以v1开头，以v1开头的是引用v1的，否则是引用本地的
                            String fileDir = null;
                            if (keyA[1].startsWith("v1")) {
                                //查询旧文件
                                fileDir = s[j + 1];
                            } else {
                                //查询当前文件
                                fileDir = s[j];
                            }
                            //获取value值
                            value = readXML(fileDir/*s[j + 1]*/, keyA[1], false);
                            //将返回的text复制为key，key判断是否为@引用
                            //替换
                            list.set(i, new StringDemo(name, value));
                            key = value;
                        }
                    }
                    //生成不同的文件名
                    file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + sDir[k] + ".xls");
                    k++;
                    //将数据保存到excel中
                    write2Excel();
                }
            }

        }).start();
    }

    /**
     * 将数据写入excel中
     */
    private void write2Excel() {
        try {

            book = Workbook.createWorkbook(file);
            WritableSheet sheet = book.createSheet("xml获取", 0);
            //循环遍历
            for (int i = 0; i < list.size(); i++) {

                Label l = new Label(0, i + 1, list.get(i).getName());
                sheet.addCell(l);
                Label ll = new Label(1, i + 1, list.get(i).getText());
                sheet.addCell(ll);
            }
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加数据
     *
     * @param page
     * @param x    列
     * @param y    行
     * @param data
     */
    public void addData(int page, int x, int y, String data) {
        try {
            //Excel获得文件
            Workbook wb = Workbook.getWorkbook(file);
            //打开一个文件的副本，并且指定数据写回到原文件
            book = Workbook.createWorkbook(file, wb);
            WritableSheet sheet = book.getSheet(page);
            Label label = new Label(x, y, data);
            sheet.addCell(label);
            // 写入数据并关闭文件
            book.write();
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据并保存
     *
     * @param fileName 读取解析的文件名
     * @param key      根据key值来，寻找对应的value
     * @param flag     true list集合存储数据，false集合不存储数据
     * @return 返回key值对应的value值，如果flag为true,key不为null
     */
    private String readXML(String fileName, String key, boolean flag) {
        try {
            XmlPullParserFactory xpf = XmlPullParserFactory.newInstance();
            XmlPullParser parser = xpf.newPullParser();
            parser.setInput(getAssets().open(fileName), "UTF-8");
            //获取事件类型
            int eventType = parser.getEventType();
            //判断是否读取结束
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        //判断是否
                        if ("string".equals(parser.getName())) {
                            //创建对象
                            sd = new StringDemo();
                            //获取元素
                            String name = parser.getAttributeValue(0);
                            //当前时间是Start_tag,而不是TEXT，所以是nextText
                            String text = parser.nextText();

                            sd.setName(name);
                            sd.setText(text);
                            if (flag) {
                                //添加到集合中
                                list.add(sd);
                            }
                            //根据key获取value
                            if (key != null && key.equals(name)) {
                                //返回value
                                return text;
                            }

                        }
                        break;
                }

                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
