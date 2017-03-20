package com.example.wulei.lastbmob;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextClock;

public class ViewTest extends AppCompatActivity {
    private static final String TAG = "ViewTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        test();


        gesture();
    }

    /**
     * 手势
     */
    private void gesture() {
        final GestureDetector ges = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                Log.i(TAG, "onSingleTapUp: ");
                return super.onSingleTapUp(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.i(TAG, "onLongPress: ");
                super.onLongPress(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i(TAG, "onScroll: ");
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i(TAG, "onFling: ");
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.i(TAG, "onShowPress: ");
                super.onShowPress(e);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                Log.i(TAG, "onDown: ");
                return super.onDown(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.i(TAG, "onDoubleTap: ");
                return super.onDoubleTap(e);
            }

        });
        Button b = (Button) findViewById(R.id.button2);
        b.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //手势
                ges.onTouchEvent(event);

                int actionIndex = event.getActionIndex();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onTouch: action down" + "..." + MotionEvent.ACTION_DOWN);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "onTouch: action up" + "..." + MotionEvent.ACTION_UP);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "onTouch: action move" + event.getX() + ".." + event.getY() + "..." + MotionEvent.ACTION_MOVE);
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        Log.i(TAG, "onTouch: action_pointer_down" + event.getX(actionIndex) + ".." + event.getY(actionIndex) + "..." + MotionEvent.ACTION_POINTER_DOWN);
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        Log.i(TAG, "onTouch: action_pointer_up" + "..." + MotionEvent.ACTION_POINTER_UP);
                        break;
                    case MotionEvent.ACTION_SCROLL:
                        Log.i(TAG, "onTouch: action scroll" + event.getX() + ".." + event.getY() + event.getX(actionIndex) + ".." + event.getY(actionIndex) + "..." + MotionEvent.ACTION_POINTER_UP);
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        Log.i(TAG, "onTouch: action_outside");
                        break;
                }

                return false;
            }
        });


    }


    @TargetApi(17)
    private void test() {
        TextClock t = (TextClock) findViewById(R.id.textClock2);
        final CheckedTextView c = (CheckedTextView) findViewById(R.id.checkedTextView2);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.toggle();
            }
        });

        final AutoCompleteTextView a = (AutoCompleteTextView) findViewById(R.id.autoCt);
        a.clearListSelection();
        a.clearComposingText();
        a.clearFocus();
        final String[] s = new String[]{
                "abbbbb", "b234", "a123", "baaaa", "addd"
        };
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s);
        a.setAdapter(aa);
        a.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                a.setText(s[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                a.setCompletionHint("nothing");
            }
        });
    }

}
