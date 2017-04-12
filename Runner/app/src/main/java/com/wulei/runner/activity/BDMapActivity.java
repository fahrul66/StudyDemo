package com.wulei.runner.activity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.LogoPosition;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.db.LocalSql;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.model.LocalSqlRun;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DateUtils;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class BDMapActivity extends BaseActivity implements View.OnClickListener {

    //百度map
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.btn_start)
    Button mStart;
    @BindView(R.id.btn_stop)
    Button mStop;
    @BindView(R.id.run_data)
    LinearLayout mData;
    @BindView(R.id.time)
    Chronometer mTime;
    //    TextView mTime;
    @BindView(R.id.speed)
    TextView mSpeed;
    @BindView(R.id.km)
    TextView mKm;
    @BindView(R.id.cal)
    TextView mCal;

    //地图
    private BaiduMap mBaiduMap;
    //定位的客户端
    private LocationClient mLocationClient;
    //定位打开，结束的标记
    private boolean locationFlag;
    //标记，是否运动
    private boolean isRun;
    //数据库
    private LocalSqlHelper lh;
    //地址
    private String address;
    private String currentDay;


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
        //sql
        lh = new LocalSqlHelper(App.mAPPContext);
        //初始化toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("跑步记录");
        mToolbar.setNavigationIcon(R.mipmap.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否处于运动状态
                if (isRun) {
                    createDialog();
                } else {
                    finish();
                }
            }
        });

        //button
        mStart.setOnClickListener(this);
        mStart.setOnClickListener(this);

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
        //打开定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //关闭开关
        mMapView.showZoomControls(false);


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
            case R.id.btn_start:
                //加判断，是否在运动中
                mStart.setVisibility(View.GONE);
                mStop.setVisibility(View.VISIBLE);
                isRun = true;
                //可见
                mData.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_stop:
                //加判断，是否在运动中
                mStart.setVisibility(View.VISIBLE);
                mStop.setVisibility(View.GONE);
                isRun = false;
                mTime.stop();
                //确定,结束，生成图片，pic,保存
                //缩放整个map,可以看到轨迹，然后，分享，保存本地
                result();
                break;
        }
    }

    /**
     * dialog
     */
    private void createDialog() {
        DialogUtils.showAlert(this, null, "确定结束运动？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //停止运动
                isRun = false;
                //确定,结束，生成图片，pic,保存

                //弹出弹出框记录数据

                //缩放整个map,可以看到轨迹，然后，分享，保存本地
                result();
            }


        });
    }


    /**
     * 缩放整个map,可以看到轨迹，然后，分享，保存本地
     */
    private void result() {
        //数据保存到数据库中，
        currentDay = DateUtils.convertToStr(System.currentTimeMillis());
        List<LocalSqlRun> list = lh.queryPB("date", currentDay, null);
        //无数据，当天有无
        if (list == null && list.isEmpty()) {
            String time = mTime.getText().toString();
            double km = Double.parseDouble(mKm.getText().toString());
            double speed = Double.parseDouble(mSpeed.getText().toString());
//            String picUrl =....
            String date = currentDay;
            String addr = address;

            //插入本地数据库
//            lh.insert(new LocalSqlRun(time, km,speed,picUrl,date,addr));
        } else if (list.size() >= 1) {

        }
        //截图，作图，打开activty

    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        //判断是否在运动中
        if (isRun) {
            createDialog();
        }
        super.onBackPressed();
    }

    /**
     * 本地百度定位监听服务
     */
    class MyLocationListener implements BDLocationListener {
        //地图坐标
        private LatLng latLng;
        //坐标集合
        private List<LatLng> latLngList = new ArrayList<>();
        //第一次加载的标记
        private boolean isFirstLoc = true;

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
            Log.i("BaiduLocationApiDem", sb.toString() + "\r\n" + isFirstLoc + "\r\n" + mTime.getText());


            /**
             * 运动中，记录轨迹,填充数据
             */
            if (isRun) {
                //Gps 定位
                if (!(location.getLocType() == BDLocation.TypeGpsLocation)) {
                    ToastUtil.show("没有搜索到GPS信号");
                    isRun = false;
                    mStart.setVisibility(View.VISIBLE);
                    mStop.setVisibility(View.GONE);
                    mData.setVisibility(View.GONE);
                    mTime.stop();
                } else {
                    /*
                     * 记录数据，开始定位
                     */
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    judgeLatLng(latLng);

                    /*
                     *  数据填充
                     */
                    //开始计时
                    mTime.start();
                    //获取速度
                    mSpeed.setText(String.valueOf(location.getSpeed()));
                    float hour = DateUtils.strToHour((String) mTime.getText());
                    float km = hour * location.getSpeed();
                    mKm.setText(String.valueOf(km));
                    float cal = km * 50;
                    mCal.setText(String.valueOf(cal));


                }
            } else {
                //结束运动，添加marker
                //定义Maker坐标点
                LatLng start = latLngList.get(0);
                LatLng stop = latLngList.get(latLngList.size() - 1);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.activity_map_marker_green_dr);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(start)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
                //构建Marker图标
                BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                        .fromResource(R.mipmap.activity_map_marker_red_dr);
                OverlayOptions option1 = new MarkerOptions()
                        .position(stop)
                        .icon(bitmap1);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option1);
            }


            //第一次定位，加载自己的位置，和缩放级别
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng latF = new LatLng(location.getLatitude(), location.getLongitude());
                //坐标
                mBaiduMap.setMyLocationEnabled(true);
                //百度地图设置，定位位置
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(latF, 18);
                mBaiduMap.animateMapStatus(mapStatusUpdate);

                //设置，地址
                address = location.getAddrStr();
            }


            //判断gps定位开关打开没有,定位更加精确
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                MyLocationData mLData = new MyLocationData.Builder()
                        .accuracy(50)
                        .direction(location.getDirection())
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .speed(location.getSpeed())
                        .build();
                mBaiduMap.setMyLocationData(mLData);
                //定位Mode两种normal和Following都设置为中心，而normal则是地图不移动，不刷新
                MyLocationConfiguration mc = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, null);
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
                //移除所有的数
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
         *
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
                } else if ((lat > 0.000005 && lat < 1) || (lo > 0.000005 && lo < 1)) {

                    latLngList.add(latLng);
                }
            }
        }

        /**
         * 保留小数
         */
        private double keepNum(double v) {
            BigDecimal bd = new BigDecimal(v);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            return bd.doubleValue();
        }
    }

}
