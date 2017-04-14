package com.wulei.runner.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wulei.runner.R;
import com.wulei.runner.activity.base.BaseActivity;
import com.wulei.runner.utils.ConstantFactory;
import com.wulei.runner.utils.ShareUtils;

import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by wule on 2017/04/13.
 */

public class ShareActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.photo)
    PhotoView mPhoto;
    @BindView(R.id.coordinate_share)
    CoordinatorLayout mCoordinate;
    private Bitmap bit;
    private String uir;
    private ShareUtils shareUtils = new ShareUtils(this);
    private Drawable drawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            init();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * init
     */
    private void init() throws FileNotFoundException {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置图片
        uir = getIntent().getStringExtra(ConstantFactory.KEY);
        bit = BitmapFactory.decodeFile(uir);
        mPhoto.setImageBitmap(bit);
        //drawble
        drawable = BitmapDrawable.createFromPath(uir);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //分享到qq
            case R.id.share_qq:
                shareUtils.shareQQFriend("Runner share", "运动轨迹图", ShareUtils.DRAWABLE, drawable);
                break;
             //分享到微信
            case R.id.share_weixin:
                shareUtils.shareWeChatFriendCircle("Runner share", "运动轨迹图", drawable);
                break;
             //分享到朋友圈
            case R.id.share_weixin_fc:
                shareUtils.shareWeChatFriendCircle("Runner share","运动轨迹图",drawable);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 跳转分享
     *
     * @param packageName
     * @param share
     */
    private void toIntent(String packageName, String share) {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.parse(uir);
        i.putExtra(ConstantFactory.KEY, uri);
        i.setType("image/*");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setComponent(new ComponentName(packageName, share));
        startActivity(i);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_share;
    }

    @Override
    protected void hideShowFragment() {

    }

    @Override
    public String getFragmentTag() {
        return null;
    }
}
