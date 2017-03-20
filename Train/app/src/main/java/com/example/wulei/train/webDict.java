package com.example.wulei.train;

import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class webDict {


    public static ArrayList<TrainTimeForm> getDictInfo(String wordKey) throws Exception {
        final String nameSpaceDict = "http://WebXml.com.cn/";
        final String urlDict = "http://www.webxml.com.cn/WebServices/TrainTimeWebService.asmx";
        final String methodNameDict = "getDetailInfoByTrainCode";
        final String soapActioDict = "http://WebXml.com.cn/getDetailInfoByTrainCode";

        SoapObject request = new SoapObject(nameSpaceDict, methodNameDict);
        // 设置需调用WebService接口需要传入的入口参数
        request.addProperty("TrainCode", wordKey);
        request.addProperty("UserID", null);


        //创建SoapSerializationEnvelope 对象，同时指定soap版本号(之前在wsdl中看到的)
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapSerializationEnvelope.VER11);
        envelope.bodyOut = request;//由于是发送请求，所以是设置bodyOut
        envelope.dotNet = true;//由于是.net开发的webservice，所以这里要设置为true

        HttpTransportSE httpTransportSE = new HttpTransportSE(urlDict);
        httpTransportSE.call(soapActioDict, envelope);//调用
        //创建数组存储
        ArrayList<TrainTimeForm> al = null;
        if (envelope.getResponse() != null) {
            SoapObject object = (SoapObject) envelope.bodyIn;
            Log.d("get", "××5××获得服务数据成功");
            SoapObject soap1 = (SoapObject) object.getProperty("getDetailInfoByTrainCodeResult");
            SoapObject soap2 = (SoapObject) soap1.getProperty(1);
            SoapObject soap3 = (SoapObject) soap2.getProperty(0);
            //创建数组存储
            al = new ArrayList<TrainTimeForm>();
            //子节点数
            for (int i = 0; i < soap3.getPropertyCount(); i++) {
                SoapObject soap4 = (SoapObject) soap3.getProperty(i);
                Log.d("piont", soap4.toString());
                //获得子节点的数据
                String trainStation = soap4.getProperty(0).toString();
                String arriveTime = soap4.getProperty(1).toString();
                String startTime = soap4.getProperty(2).toString();
                String km = soap4.getProperty(3).toString();
                //传到对象中
                TrainTimeForm ttf = new TrainTimeForm(trainStation, arriveTime, startTime, km);
                //添加到集合中
                al.add(ttf);
            }
//				result=soap3.toString()+soap4.toString()+soap5.toString()+soap6.toString();
            //	Log.d("debug",result);
        }
        return al;

    }


}
