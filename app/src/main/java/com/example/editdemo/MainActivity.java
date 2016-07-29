package com.example.editdemo;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

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


    private String TAG = MainActivity.class.getSimpleName();
    private CommentConfig commentConfig;
    private int selectCircleItemH;
    private int selectCommentItemOffset;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initview();

        setViewTreeObserver();
    }


    private void initview() {
        edittextbody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        editText = (EditText) findViewById(R.id.circleEt);
        sendIv = (ImageView) findViewById(R.id.sendIv);
        sendIv.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.listview);
        initData();
        adapter = new ListBaseAdapter(this, listdata,this);
        mListView.setAdapter(adapter);



    }

    private void initData() {
        listdata = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            TestEntity entity = new TestEntity();
            entity.setName("test-" + i);
            listdata.add(entity);
        }

    }


    private void setViewTreeObserver() {

        bodyLayout = (RelativeLayout) findViewById(R.id.bodyLayout);
        final ViewTreeObserver swipeRefreshLayoutVTO = bodyLayout.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Log.e(TAG,"onGlobalLayout------");
                Rect r = new Rect();
                bodyLayout.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = bodyLayout.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.e(TAG, "screenH＝ " + screenH + " currentKeyboardH="+currentKeyboardH+" &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = edittextbody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
               /* //偏移listview
                if(layoutManager!=null && commentConfig != null){
                    layoutManager.scrollToPositionWithOffset(commentConfig.circlePosition + CircleAdapter.HEADVIEW_SIZE, getListviewOffset(commentConfig));
                }*/

                Log.e(TAG,"getListviewOffset:"+getListviewOffset(commentConfig));
                //mListView.smoothScrollBy(getListviewOffset(commentConfig),500);
                mListView.setSelectionFromTop(commentConfig.circlePosition,0);
            }
        });
    }

    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {
        this.commentConfig = commentConfig;
        edittextbody.setVisibility(visibility);

        measureCircleItemHighAndCommentItemOffset(commentConfig);

        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(), editText);

        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(editText.getContext(), editText);
        }
    }


    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = mListView.getFirstVisiblePosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = mListView.getChildAt(commentConfig.circlePosition-firstPosition);
        //View selectCircleItem = mListView.getSelectedView();

        if (selectCircleItem != null) {
            selectCircleItemH = selectCircleItem.getHeight();
        }

      /*  if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            LinearLayout commentLv = (LinearLayout) selectCircleItem.findViewById(R.id.item_pinglun_layout);
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                selectCommentItemOffset = 0;
                *//*if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    int subItemBottom = parentView.getBottom();
                    selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                }
*//*


                if(selectCommentItem != null){
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if(parentView != null){
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        }*/
    }


    /**
     * 测量偏移量
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if(commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectCircleItemH - currentKeyboardH - editTextBodyHeight;
        if(commentConfig.commentType == CommentConfig.Type.REPLY){
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
        return listviewOffset;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (edittextbody != null && edittextbody.getVisibility() == View.VISIBLE) {
                //edittextbody.setVisibility(View.GONE);
                updateEditTextBodyVisible(View.GONE, null);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private int selectedPostion=0;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_name:

                selectedPostion = (int) view.getTag();
                CommentConfig commentConfig = new CommentConfig();
                commentConfig.circlePosition = selectedPostion;
                //写死
                commentConfig.commentPosition=1;
                commentConfig.commentType = CommentConfig.Type.REPLY;
                updateEditTextBodyVisible(View.VISIBLE,commentConfig);


                break;
            case R.id.sendIv:
                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(MainActivity.this, "内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                listdata.get(selectedPostion).getPinglunList().add(content);
                adapter.notifyDataSetChanged();
                //updateEditTextBodyVisible(View.GONE, null);
                break;
        }

    }
}
