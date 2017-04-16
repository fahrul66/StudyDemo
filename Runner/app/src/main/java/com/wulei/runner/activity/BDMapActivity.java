package com.wulei.runner.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.app.App;
import com.wulei.runner.db.LocalSqlHelper;
import com.wulei.runner.model.LocalSqlRun;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.DateUtils;
import com.wulei.runner.utils.DialogUtils;
import com.wulei.runner.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
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
    //标记，是否运动
    private boolean isRun;
    //数据库
    private LocalSqlHelper lh;
    //地址
    private String address;
    private String currentDay;
    //照片截图
    private String picUrl;
    //截图的成功
    private boolean successCapture;
    //定位监听器
    private MyLocationListener locationListener;
    //截取的图片
    Bitmap bitmap = null;


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
        mStop.setOnClickListener(this);

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
        locationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(locationListener);
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
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(locationListener);
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
//                captureScreen();
//               加判断，是否在运动中
                mStart.setVisibility(View.GONE);
                mStop.setVisibility(View.VISIBLE);
                isRun = true;
                //可见
                mData.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_stop:
                //加判断，是否在运动中
                mStop.setVisibility(View.GONE);
                mStart.setVisibility(View.VISIBLE);
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
                mTime.stop();
                mStart.setVisibility(View.GONE);
                mStop.setVisibility(View.GONE);
                //缩放整个map,可以看到轨迹，然后，分享，保存本地
                result();


            }


        });
    }

    /**
     * 截图
     */
    public void captureScreen() {


        //
        mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {
            @Override
            public void onSnapshotReady(Bitmap bitmap) {
                picUrl = getPath();
                savePic(bitmap, picUrl);
                BDMapActivity.this.bitmap = bitmap;


                //获取image,弹出dialog 或者activity分享
                Intent intent = new Intent(BDMapActivity.this, ShareActivity.class);
                intent.putExtra(ConstantFactory.KEY, picUrl);
                startActivity(intent);
                //销毁当前
                finish();
            }
        });

    }

    /**
     * 获取图片路径
     */
    private String getPath() {
        String today = DateUtils.convertToStrAll(System.currentTimeMillis());
        return getExternalCacheDir() + File.separator + today + ".png";
    }

    /**
     * 保存图片路径
     *
     * @param b
     * @param strFileName
     */
    private void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                //质量压缩
                successCapture = b.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 缩放整个map,可以看到轨迹，然后，分享，保存本地
     */
    private void result() {
        //dialog
        final ProgressDialog p = new ProgressDialog(this);
        p.setMessage("loading...");
        p.show();

        //数据保存到数据库中，
        currentDay = DateUtils.convertToStr(System.currentTimeMillis());
        //无数据，当天有无
        String time = mTime.getText().toString();
        double km = Double.parseDouble(mKm.getText().toString());
        double speed = Double.parseDouble(mSpeed.getText().toString());
        String date = currentDay;
        String addr = address;
        //插入本地数据库
        lh.insert(new LocalSqlRun(time, km, speed, picUrl, date, addr));

        //确定,结束，生成图片，pic,保存
        captureScreen();
        //消失
        p.dismiss();
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        //判断是否在运动中
        if (isRun) {
            createDialog();
        } else {
            finish();
        }
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
        //定位数据
        private MyLocationData mLData;
        //speed集合
        private List<Float> speedList = new ArrayList<>();
        //平均速度
        private float mAvaSpeed;

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceiveLocation(BDLocation location) {

            //判断mapView销毁则销毁定位服务
            if (location == null && mMapView == null) {
                return;
            }

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
                    //获取速度,添加集合，最后填充
                    speedList.add(location.getSpeed());
                    mSpeed.setText(String.valueOf(keepNum(location.getSpeed())));


                }
            } else {
                //数据填充
                txtData();
                //结束运动，添加marker
                //定义Maker坐标点
                traceRecord();

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

           
              /*
                 * 数据初始化
                 */
            mLData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .speed(location.getSpeed())
                    .build();
            mBaiduMap.setMyLocationData(mLData);
            //定位Mode两种normal和Following都设置为中心，而normal则是地图不移动，不刷新
            MyLocationConfiguration mc = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
            mBaiduMap.setMyLocationConfigeration(mc);

            /*
             * 在地图上，添加覆盖物。动健网
             */
            if (latLngList.size() > 5) {

                OverlayOptions olo = new PolylineOptions()
                        .width(15).color(getColor(location.getSpeed()))
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
                } /*else if ((lat > 0.000005 && lat < 1) || (lo > 0.000005 && lo < 1)) {

                    latLngList.add(latLng);
                } */ else {
                    latLngList.add(latLng);

                }
            }
        }

        /**
         * 保留小数
         */
        private double keepNum(double v) {
            long l1 = Math.round(v*100); //四舍五入
            double ret = l1/100.0; //注意：使用 100.0 而不是 100
            return ret;
        }

        /**
         * speed depend  on  color
         */

        private int getColor(float speed) {
            int color = 0;
            if (speed > 0 && speed <= 5) {
                color = getResources().getColor(R.color.accent);
            } else if (speed > 5 && speed <= 10) {
                color = getResources().getColor(R.color.sample_yellow);
            } else if (speed > 10 && speed <= 20) {
                color = getResources().getColor(R.color.orange_red);
            } else if (speed > 20) {
                color = getResources().getColor(R.color.sample_red);
            }

            return color;
        }

        /**
         * 数据填充
         */
        private void txtData() {
            //数据填充
            for (Float f : speedList) {
                mAvaSpeed += f;
            }
            //平均值
            mAvaSpeed = mAvaSpeed / speedList.size();
            float hour = DateUtils.strToHour((String) mTime.getText());
            float km = hour * mAvaSpeed;
            mKm.setText(String.valueOf(keepNum(km)));
            float cal = km * 50;
            mCal.setText(String.valueOf(keepNum(cal)));
        }

        /**
         * 轨迹记录
         */
        private void traceRecord() {
            if (!latLngList.isEmpty()) {

                LatLng start = latLngList.get(0);
                LatLng stop = latLngList.get(latLngList.size() - 1);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.activity_map_marker_green_dr);
                //构建MarkerOption，用于在地图上添加Marker
                final OverlayOptions option = new MarkerOptions()
                        .position(start)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
                //构建Marker图标
                BitmapDescriptor bitmap1 = BitmapDescriptorFactory
                        .fromResource(R.mipmap.activity_map_marker_red_dr);
                final OverlayOptions option1 = new MarkerOptions()
                        .position(stop)
                        .icon(bitmap1);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option1);

                OverlayManager overlayManager = new OverlayManager(mBaiduMap) {
                    //重写，使marker自动缩放
                    @Override
                    public List<OverlayOptions> getOverlayOptions() {
                        List<OverlayOptions> over = new ArrayList<>();
                        over.add(option);
                        over.add(option1);
                        return over;
                    }

                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return false;
                    }

                    @Override
                    public boolean onPolylineClick(Polyline polyline) {
                        return false;
                    }
                };

                //合适的缩放范围
                overlayManager.addToMap();
                overlayManager.zoomToSpan();
            }
        }
    }

}
