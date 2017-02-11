package com.example.wulei.baidumap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MapView mMapView = null;
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    public BDLocation location = null;
    private static final int REQUEST_CODE_LOCATION = 1;
    private Toolbar toolbar;
    //地图
    private BaiduMap baiduMap;
    //地图坐标
    private LatLng latLng;
    //坐标集合
    private List<LatLng> latLngList = new ArrayList<>();
    //定位打开，结束的标记
    private boolean locationFlag;
    //第一次加载的标记
    private boolean isFirstLoc = true;
    //打开GPS界面后，返回判断gps是否打开
//    private boolean gpsFlag;
    //菜单标记
    private boolean menuFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化地图
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        //权限处理
        runtimePermisson();
        //toolbar设置
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMapView = (MapView) findViewById(R.id.bmapView);
        //百度地图设置
        baiduMap = mMapView.getMap();
        baiduMap.setIndoorEnable(true);
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        baiduMap.setBaiduHeatMapEnabled(false);

        baiduMap.setTrafficEnabled(true);

        mLocationClient = new LocationClient(getApplicationContext());
        //初始化定位SDK设置
        initLocation();
        mLocationClient.registerLocationListener(myListener);
        //开始定位
        mLocationClient.start();

        //设置地图缩放级别,17为100米距离
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(18);
        baiduMap.animateMapStatus(mapStatusUpdate);
        //打开定位图层
        baiduMap.setMyLocationEnabled(true);

    }

    /**
     * 初始化定位SDK设置
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
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

    /**
     * android 6.0运行时权限
     */
    private void runtimePermisson() {
        //如果当前的API版本大于等于23，即android6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "runtimePermisson: go");
            //检查自身是否有定位权限
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "runtimePermisson: go1");
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION);
            }
        }
    }

    /**
     * 选择是否打开权限后，会调用这里的方法，处理接收或者不接受权限的响应
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                //做相应的处理
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(getWindow().getDecorView(), "权限处理成功！", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getWindow().getDecorView(), "权限处理失败！", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 在start中开始定位
     */
    @Override
    protected void onStart() {

        if (!mLocationClient.isStarted()) {
            mLocationClient.start();
        }
        //打开定位图层
        baiduMap.setMyLocationEnabled(true);
        //初始化gps是否打开
        super.onStart();

    }


    /**
     * 在stop中停止定位
     */
    @Override
    protected void onStop() {
        //避免耗电，在后台是
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    /**
     * 菜单栏
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.start:
                //开始记录，打开gps
                start();
                //菜单状态改变
                invalidateOptionsMenu();
                break;
            case R.id.menu3:
                //关闭定位图层
                locationFlag = false;
                break;
            case R.id.menu4:
                //打开定位图层
                locationFlag = true;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.start);
        //gps打开并且点击后变状态
        if (menuFlag && isGPSOpen()) {

            menuItem.setTitle("停止记录");
            menuFlag = false;
            //做一些停止定位操作，如截取轨迹图

        } else {
            menuItem.setTitle("开始记录");
            menuFlag = true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * 开始的点击事件
     */
    private void start() {
        //判断gps是否打开，打开则定位，不打开则跳转
        if (!isGPSOpen()) {

            Toast.makeText(this, "请打开Gps，在开始记录！", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE_LOCATION);
        }
    }

    /**
     * 判断gps是否打开
     *
     * @return
     */
    private boolean isGPSOpen() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnable;
    }

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                //再次判断gps是否打开
                if(isGPSOpen()){
                    gpsFlag = true;
                }
                break;
        }
    }*/

    /**
     * Created by wulei on 2016/12/21.
     */

    class MyLocationListener implements BDLocationListener {
        //location对象
        private static final String TAG = "MyLocationListener";

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceiveLocation(BDLocation location) {

            //判断mapView销毁则销毁定位服务
            if (location == null && mMapView == null) {
                return;
            }

            MainActivity.this.location = location;
            Log.i(TAG, "onReceiveLocation: " + location.getLongitude() + " ..." + MainActivity.this.location.getLatitude());

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
            latLngList.add(latLng);



            //第一次定位，加载自己的位置，和缩放级别
            if (isFirstLoc) {
                isFirstLoc = false;
                //坐标
                baiduMap.setMyLocationEnabled(true);
                //百度地图设置，定位位置
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(mapStatusUpdate);

//         baiduMap.setMapStatus(mapStatusUpdate);
                //icon
//                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(android.R.drawable.ic_notification_overlay);
                //覆盖物设置
//                OverlayOptions overlayOptions = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
                //添加覆盖物
//                baiduMap.addOverlay(overlayOptions);
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
                baiduMap.setMyLocationData(mLData);
                //定位Mode两种normal和Following都设置为中心，而normal则是地图不移动，不刷新
                MyLocationConfiguration mc = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS, true, null);
                baiduMap.setMyLocationConfigeration(mc);
            } else {
                //网络定位，精确度没有gps高，并且没有速度，方向等信息
                MyLocationData mLData1 = new MyLocationData.Builder()
                        .latitude(location.getLatitude())
                        .direction(location.getDirection())
                        .longitude(location.getLongitude())
                        .build();
                baiduMap.setMyLocationData(mLData1);
                //定位Mode两种normal和Following都设置为中心，而normal则是地图不移动，不刷新
                MyLocationConfiguration mc1 = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
                baiduMap.setMyLocationConfigeration(mc1);
            }

            if (latLngList.size() > 10) {

                OverlayOptions olo = new PolylineOptions()
                        .width(15).color(getResources().getColor(R.color.colorAccent, null))
                        .points(latLngList);
                baiduMap.addOverlay(olo);

                latLngList.removeAll(latLngList);
            }
        }
    }

}
