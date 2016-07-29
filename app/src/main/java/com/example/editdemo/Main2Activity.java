package com.example.editdemo;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends Activity implements View.OnClickListener, ListBaseAdapter.IItemClick {

    private ListView listView2;
    private LinearLayout editBody;
    private RelativeLayout rootBody;

    private ListBaseAdapter adapter;
    private List<TestEntity> listdata;
    private List<TestEntity> final_listdata = new ArrayList<>();

    private EditText editText;
    private ImageView sendImg;
    private String TAG = Main2Activity.class.getSimpleName();
    private int currentKeyboardH;//当前软键盘高度
    private int screenHeight;//屏幕高度
    private int editTextBodyHeight;//底部editText布局高度
    private int selectItemH;//点击Item高度
    private int selectCommentItemOffset;//评论高度
    private CommentConfig commentConfig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {

        rootBody = (RelativeLayout) findViewById(R.id.root);
        editBody = (LinearLayout) findViewById(R.id.editTextBodyLl);
        editText = (EditText) findViewById(R.id.circleEt);
        sendImg = (ImageView) findViewById(R.id.sendIv);
        sendImg.setOnClickListener(this);

        listView2 = (ListView) findViewById(R.id.listview2);

        adapter = new ListBaseAdapter(this, final_listdata, this);
        listView2.setAdapter(adapter);
        initData();

        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) editBody.getLayoutParams();
        View footView = new View(this);
        AbsListView.LayoutParams alp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rlp.height);
        footView.setVisibility(View.INVISIBLE);
        footView.setLayoutParams(alp);
        listView2.addFooterView(footView);

        setViewTreeObserver();
    }


    private void initData() {
        listdata = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            TestEntity entity = new TestEntity();
            entity.setName("test-" + i);
            listdata.add(entity);
        }

        final_listdata.clear();
        final_listdata.addAll(listdata);
        adapter.notifyDataSetChanged();

    }

    private void setViewTreeObserver() {
        final ViewTreeObserver swipeRefreshLayoutVTO = rootBody.getViewTreeObserver();
        swipeRefreshLayoutVTO.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.e(TAG, "onGlobalLayout------");
                Rect r = new Rect();
                rootBody.getWindowVisibleDisplayFrame(r);
                int statusBarH = getStatusBarHeight();//状态栏高度
                int screenH = rootBody.getRootView().getHeight();
                if (r.top != statusBarH) {
                    //在这个demo中r.top代表的是状态栏高度，在沉浸式状态栏时r.top＝0，通过getStatusBarHeight获取状态栏高度
                    r.top = statusBarH;
                }
                int keyboardH = screenH - (r.bottom - r.top);
                Log.e(TAG, "screenH＝ " + screenH + " currentKeyboardH=" + currentKeyboardH + " &keyboardH = " + keyboardH + " &r.bottom=" + r.bottom + " &top=" + r.top + " &statusBarH=" + statusBarH);

                if (keyboardH == currentKeyboardH) {//有变化时才处理，否则会陷入死循环
                    return;
                }

                currentKeyboardH = keyboardH;
                screenHeight = screenH;//应用屏幕的高度
                editTextBodyHeight = editBody.getHeight();

                if (keyboardH < 150) {//说明是隐藏键盘的情况
                    updateEditTextBodyVisible(View.GONE, null);
                    return;
                }
                Log.e(TAG, "commentConfig＝ " + commentConfig.toString());
                Log.e(TAG, "getListviewOffset:" + getListviewOffset(commentConfig));
                listView2.setSelectionFromTop(commentConfig.circlePosition, getListviewOffset(commentConfig));
            }
        });

    }

    private int selectedPostion = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_name:
                selectedPostion = (int) view.getTag();
                commentConfig = new CommentConfig();
                commentConfig.circlePosition = selectedPostion;
                commentConfig.commentPosition = 0;
                updateEditTextBodyVisible(View.VISIBLE, commentConfig);
                break;
            case R.id.sendIv:
                String content = editText.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(Main2Activity.this, "内容为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                listdata.get(selectedPostion).getPinglunList().add(content);
                final_listdata.clear();
                final_listdata.addAll(listdata);
                adapter.notifyDataSetChanged();
                updateEditTextBodyVisible(View.GONE, null);

                break;
        }
    }

    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {

        editBody.setVisibility(visibility);
        this.commentConfig = commentConfig;
        measureCircleItemHighAndCommentItemOffset(commentConfig);
        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(), editText);

        } else if (View.GONE == visibility) {
            editText.setText("");
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

    /**
     * 测量偏移量
     *
     * @param commentConfig
     * @return
     */
    private int getListviewOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return 0;
        //这里如果你的listview上面还有其它占高度的控件，则需要减去该控件高度，listview的headview除外。
        //int listviewOffset = mScreenHeight - mSelectCircleItemH - mCurrentKeyboardH - mEditTextBodyHeight;
        int listviewOffset = screenHeight - selectItemH - currentKeyboardH - editTextBodyHeight;
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            listviewOffset = listviewOffset + selectCommentItemOffset;
        }
       // listviewOffset = listviewOffset + selectCommentItemOffset;
        return listviewOffset;
    }

    private void measureCircleItemHighAndCommentItemOffset(CommentConfig commentConfig) {
        if (commentConfig == null)
            return;

        int firstPosition = listView2.getFirstVisiblePosition();
        //只能返回当前可见区域（列表可滚动）的子项
        View selectCircleItem = listView2.getChildAt(commentConfig.circlePosition - firstPosition);
        Log.e(TAG,"selectCircleItem:"+selectCircleItem);
        if (selectCircleItem != null) {
            selectItemH = selectCircleItem.getHeight();
        }else{
            Log.e(TAG,"selectCircleItem: is null");
            return;
        }


        LinearLayout commentLv = (LinearLayout) selectCircleItem.findViewById(R.id.item_pinglun_layout);
        if (commentConfig.commentType == CommentConfig.Type.REPLY) {
            //回复评论的情况
            if (commentLv != null) {
                //找到要回复的评论view,计算出该view距离所属动态底部的距离
                View selectCommentItem = commentLv.getChildAt(commentConfig.commentPosition);
                if (selectCommentItem != null) {
                    //选择的commentItem距选择的CircleItem底部的距离
                    selectCommentItemOffset = 0;
                    View parentView = selectCommentItem;
                    do {
                        int subItemBottom = parentView.getBottom();
                        parentView = (View) parentView.getParent();
                        if (parentView != null) {
                            selectCommentItemOffset += (parentView.getHeight() - subItemBottom);
                        }
                    } while (parentView != null && parentView != selectCircleItem);
                }
            }
        } /*else {
            if (commentLv != null) {
                selectCommentItemOffset = commentLv.getHeight();
            }
        }*/
    }

    @Override
    public void itemClick(CommentConfig commentConfig) {
        this.commentConfig = commentConfig;

        selectedPostion = commentConfig.circlePosition;
        updateEditTextBodyVisible(View.VISIBLE, commentConfig);
    }
}
