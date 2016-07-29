package com.example.editdemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Main1Activity extends Activity implements View.OnClickListener  ,ListBaseAdapter.IItemClick{

    private ListView mListView;
    private ListBaseAdapter adapter;
    private List<TestEntity> listdata;
    private LinearLayout edittextbody;
    private EditText editText;
    private ImageView sendIv;
    private RelativeLayout bodyLayout;

    private int currentKeyboardH;
    private int screenHeight;
    private int editTextBodyHeight;


    private String TAG = Main1Activity.class.getSimpleName();
    private CommentConfig commentConfig;
    private int selectCircleItemH;
    private int selectCommentItemOffset;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initview();


    }


    private void initview() {
        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        editText = (EditText) findViewById(R.id.circleEt);
        sendIv = (ImageView) findViewById(R.id.sendIv);
        sendIv.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listview);
        initData();
        adapter = new ListBaseAdapter(this, listdata, this);
        mListView.setAdapter(adapter);


        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                CommonUtils.hideSoftInput(context, editText);
                edittextbody.setVisibility(View.GONE);
                return false;
            }
        });

        edittextbody.postDelayed(new Runnable() {
            @Override
            public void run() {
                //16/5/31 计算移动距离
                int[] position1 = new int[2];
                bodyLayout.getLocationOnScreen(position1);
                Log.d(TAG, "srcViewRect.x = " + position1[0]);
                Log.d(TAG, "srcViewRect.y = " + position1[1]);

                int[] position2 = new int[2];
                edittextbody.getLocationOnScreen(position2);
                Log.d(TAG, "mEditTextBody.x = " + position2[0]);
                Log.d(TAG, "mEditTextBody.y = " + position2[1]);

                mListView.scrollBy(0, position1[1] - position2[1]);
            }
        }, 500);

    }

    private void initData() {
        listdata = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            TestEntity entity = new TestEntity();
            entity.setName("test-" + i);
            listdata.add(entity);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

        }
        return super.onKeyDown(keyCode, event);
    }

    private int selectedPostion = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_name:
                showSoftInput(Main1Activity.this,edittextbody);
               // edittextbody.setVisibility(View.VISIBLE);

                break;
            case R.id.sendIv:

        }

    }


    public static void showSoftInput(Context context,View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(view.getVisibility()==View.VISIBLE){
            view.setVisibility(View.GONE);

        }else{
            view.setVisibility(View.VISIBLE);
            //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    @Override
    public void itemClick(CommentConfig commentConfig) {

    }
}
