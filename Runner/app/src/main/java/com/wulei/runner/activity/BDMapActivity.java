package com.wulei.runner.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BDMapActivity extends BaseActivity implements View.OnClickListener {

    //百度map
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    //地图
    private BaiduMap mBaiduMap;
    //定位的客户端
    private LocationClient mLocationClient;
    //定位打开，结束的标记
    private boolean locationFlag;
    //第一次加载的标记
    private boolean isFirstLoc = true;
    //获取的坐标信息
    private LatLng mLatLng;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case 1:
                    mLatLng = bundle.getParcelable("Latlng");
                    //设置地图缩放级别,17为100米距离
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(mLatLng, 18);
                    if (mLatLng != null) {
                        mBaiduMap.animateMapStatus(mapStatusUpdate);
                    }
                    //打开定位图层
                    mBaiduMap.setMyLocationEnabled(true);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化
        initView();
    }

    @Override
    protected void hideShowFragment() {

    }

    @Override
    public String getFragmentTag() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bdmap;
    }


    /**
     * 数据初始化
     */
    private void initView() {
        //初始化toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //初始化mapview
        mBaiduMap = mMapView.getMap();
        //地图设置
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //打开交通图
        mBaiduMap.setTrafficEnabled(true);
        //初始化定位客户端
        mLocationClient = new LocationClient(App.mAPPContext);
        //初始化定位SDK设置
        initLocation();
        mLocationClient.registerLocationListener(new MyLocationListener());
        //开始定位
        mLocationClient.start();

        //设置地图缩放级别,17为100米距离
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(mLatLng, 18);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
        //打开定位图层
        mBaiduMap.setMyLocationEnabled(true);


    }


    /**
     * 初始化定位SDK设置
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 2000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setNeedDeviceDirect(true); //在网络定位时，是否需要设备方向 true:需要 ; false:不需要。默认为false
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        //关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:
                //加判断，是否在运动中
                finish();
                ToastUtil.show("123");
                break;
        }
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //判断是否在运动中
    }

    /**
     * 本地百度定位监听服务
     */
    class MyLocationListener implements BDLocationListener {
        //地图坐标
        private LatLng latLng;
        //坐标集合
        private List<LatLng> latLngList = new ArrayList<>();

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceiveLocation(BDLocation location) {

            //判断mapView销毁则销毁定位服务
            if (location == null && mMapView == null) {
                return;
            }

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());


            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            //添加到集合中
//            latLngList.add(latLng);
            judgeLatLng(latLng);


            //第一次定位，加载自己的位置，和缩放级别
            if (isFirstLoc) {
                isFirstLoc = false;
                //坐标
                mBaiduMap.setMyLocationEnabled(true);
                //百度地图设置，定位位置
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);

                Message m = new Message();
                m.what = 1;
                Bundle b = new Bundle();
                b.putParcelable("Latlng", latLng);
                m.setData(b);
                mHandler.sendMessage(m);
            }

            //判断gps定位开关打开没有,定位更加精确
            if (locationFlag) {
                MyLocationData mLData = new MyLocationData.Builder()
                        .accuracy(50)
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .speed(location.getSpeed())
                        .build();
                mBaiduMap.setMyLocationData(mLData);
                //定位Mode两种normal和Following都设置为中心，而normal则是地图不移动，不刷新
                MyLocationConfiguration mc = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, null);
                mBaiduMap.setMyLocationConfigeration(mc);
            } else {
                //网络定位，精确度没有gps高，并且没有速度，方向等信息
                MyLocationData mLData1 = new MyLocationData.Builder()
                        .latitude(location.getLatitude())
                        .direction(location.getDirection())
                        .longitude(location.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(mLData1);
                //定位Mode两种normal和Following都设置为中心，而normal则是地图不移动，不刷新
                MyLocationConfiguration mc1 = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
                mBaiduMap.setMyLocationConfigeration(mc1);
            }

            /*
             * 在地图上，添加覆盖物。动健网
             */
            if (latLngList.size() > 10) {

                OverlayOptions olo = new PolylineOptions()
                        .width(15).color(getResources().getColor(R.color.accent, null))
                        .points(latLngList);
                mBaiduMap.addOverlay(olo);

                latLngList.removeAll(latLngList);


            }

            /*
             *异常错误时，提示
             */
            if (location.getLocType() == BDLocation.TypeServerError) {
                ToastUtil.show("server定位失败，没有对应的位置信息");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                ToastUtil.show("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                ToastUtil.show("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
        }

        /**
         * 过滤坐标
         * @param latLng
         */
        private void judgeLatLng(LatLng latLng) {
            if (latLngList.isEmpty()) {
                latLngList.add(latLng);
            } else {
                //上一个值
                LatLng l = latLngList.get(latLngList.size() - 1);
                //差值
                double lat = Math.abs(l.latitude - latLng.latitude);
                double lo = Math.abs(l.longitude - latLng.longitude);
                //guolv
                if (l.longitude == latLng.longitude && l.latitude == latLng.latitude) {
                    return;
                } else if ((lat > 5 && lat < 20) || (lo > 5 && lo < 20)) {

                    latLngList.add(latLng);
                }
            }
        }
    }

}
