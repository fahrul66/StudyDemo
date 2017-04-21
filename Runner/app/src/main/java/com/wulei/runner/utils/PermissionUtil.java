package com.wulei.runner.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

/**
 * Created by wule on 2017/03/31.
 */

public class PermissionUtil {
    //返回码
    private int mRequestCode;
    //fragment
    private Fragment mFragment;
    //activity
    private Activity mActivity;
    //回调
    private OnPermissionResultListener mPermissonListener;
    //实例
    private static PermissionUtil p = new PermissionUtil();

    public static PermissionUtil getInstance() {
        return p;
    }

    /**
     * android 6.0运行时权限,fragment使用
     */
    public void runtimePermisson(Fragment fragment, String[] permissons, int requestCode, OnPermissionResultListener onPermissionResultListener) {

        //初始化
        this.mFragment = fragment;
        this.mRequestCode = requestCode;
        this.mPermissonListener = onPermissionResultListener;

        //如果当前的API版本大于等于23，即android6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查自身是否有定位权限
            if (isPermissonGranted(permissons)) {
                mFragment.requestPermissions(permissons, mRequestCode);
            } else {
                //权限全部获得
                mPermissonListener.permissionGranted();
            }
        }
    }

    /**
     * android 6.0权限，activity中使用，方法重载
     * @param activity
     * @param permissons
     * @param requestCode
     * @param onPermissionResultListener
     */
    public void runtimePermisson(Activity activity, String[] permissons, int requestCode, OnPermissionResultListener onPermissionResultListener) {

        //初始化
        this.mActivity = activity;
        this.mRequestCode = requestCode;
        this.mPermissonListener = onPermissionResultListener;

        //如果当前的API版本大于等于23，即android6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查自身是否有定位权限
            if (isPermissonGrantedActivity(permissons)) {
                mActivity.requestPermissions(permissons, mRequestCode);
            } else {
                //权限全部获得
                mPermissonListener.permissionGranted();
            }
        }
    }

    /**
     * 检查所有的权限
     *
     * @param permission
     * @return true call requestPermission,false otherwise
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissonGranted(String[] permission) {
        for (String per : permission) {
            if (mFragment.getActivity().checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }
    /**
     * 检查所有的权限
     *
     * @param permission
     * @return true call requestPermission,false otherwise
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isPermissonGrantedActivity(String[] permission) {
        for (String per : permission) {
            if (mActivity.checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 选择是否打开权限后，会调用这里的方法，处理接收或者不接受权限的响应
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //做相应的处理
        if (mRequestCode == requestCode) {

            if (isGranted(grantResults)) {
                mPermissonListener.permissionGranted();
            } else {
                mPermissonListener.permissionDenied();
            }
        }
    }

    /**
     * 判断返回的值，是否全部授权完成
     *
     * @param grantResults 结果值
     * @return true all is granted ，false otherwise
     */
    private boolean isGranted(int[] grantResults) {
        if (grantResults.length > 0) {
            for (int granted : grantResults) {
                if (granted != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 接口回调，返回值成功后者失败
     */
    public interface OnPermissionResultListener {
        void permissionGranted();

        void permissionDenied();
    }
}
