package com.brother.bottomsheetdemo;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mScroll, mDialog, mDialogFragment, mDialogScroll;
    private BottomSheetBehavior bsb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //init
        initData();
        //scroll normal bottomSheet
        scroll();
        mScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bsb != null) {
                    //click view collapsed
                    bsb.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        //dialog when you rotate your device will be destroyed
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bsd = new BottomSheetDialog(MainActivity.this);
                bsd.setContentView(R.layout.dialog);
                bsd.setCanceledOnTouchOutside(true);
                bsd.show();
            }
        });

        //dialog fragment can save it's state
        mDialogFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MyBottomSheetDialogFragment().show(getSupportFragmentManager(),null);
            }
        });
    }

    /**scroll btn*/
    private void scroll() {
        NestedScrollView n = (NestedScrollView) findViewById(R.id.bottom_sheet_view);
        bsb = BottomSheetBehavior.from(n);
        //inti height
        bsb.setPeekHeight(300);
        //view can hide
        bsb.setHideable(true);
        //call back
        bsb.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i(TAG, "STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i(TAG, "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i(TAG, "STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i(TAG, "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i(TAG, "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i(TAG, "slideOffset: " + slideOffset);
            }
        });


    }

    /**
     * init views
     */
    private void initData() {
        mScroll = (Button) findViewById(R.id.scroll);
        mDialog = (Button) findViewById(R.id.dialog);
        mDialogFragment = (Button) findViewById(R.id.dialog_fragment);
        mDialogScroll = (Button) findViewById(R.id.dialog_scroll);
    }
}
