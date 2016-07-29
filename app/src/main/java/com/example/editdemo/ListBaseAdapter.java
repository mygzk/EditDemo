package com.example.editdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.editdemo.bean.CommentEntity;
import com.example.editdemo.bean.TestEntity;

import java.util.List;

/**
 * Created by guozhk on 16-7-26.
 */
public class ListBaseAdapter extends BaseAdapter {
    private Context mContext;
    private List<TestEntity> listdata;
    private LayoutInflater mInflater;
    private IItemClick temClick;

    public ListBaseAdapter(Context mContext, List<TestEntity> listdata,IItemClick temClick) {
        this.mContext = mContext;
        this.listdata = listdata;
        mInflater = LayoutInflater.from(mContext);
        this.temClick=temClick;
    }

    @Override
    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int i) {
        return listdata.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        HolderView holderView = null;
        if (view == null) {
            holderView = new HolderView();
            view = mInflater.inflate(R.layout.list_item, null);
            holderView.content = (TextView) view.findViewById(R.id.item_name);
            holderView.title = (TextView) view.findViewById(R.id.item_title);
            holderView.pinglun = (TextView) view.findViewById(R.id.item_comment);
            holderView.pinlunContainer = (LinearLayout) view.findViewById(R.id.item_pinglun_layout);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }

        holderView.content.setText(listdata.get(i).getName());
        holderView.title.setText(listdata.get(i).getTitle());
        holderView.pinglun.setTag(i);
        holderView.pinglun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentConfig commentConfig = new CommentConfig();
                commentConfig.itemPosition = i;
                commentConfig.commentItemPosition = 0;
                commentConfig.commentType = CommentConfig.Type.PUBLIC;
                temClick.itemClick(commentConfig);
            }
        });

        if (listdata.get(i).getCommentList() != null && listdata.get(i).getCommentList().size() > 0) {
            initPinglunData(listdata.get(i).getCommentList(), holderView.pinlunContainer,i);
        }
        return view;
    }

    private void initPinglunData(List<CommentEntity> commentList, LinearLayout pinlunContainer, final int pos) {
        if (commentList.isEmpty()) {
            return;
        }
        pinlunContainer.removeAllViews();
        for (int i = 0; i < commentList.size(); i++) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 80);
            textView.setLayoutParams(layoutParams);
            textView.setPadding(10,0,0,0);
            textView.setBackgroundColor(Color.parseColor("#356695"));
            textView.setTextColor(Color.parseColor("#cc000000"));
            textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
            CommentEntity entity=commentList.get(i);
            if(entity.getType()==CommentEntity.COMMENT_TYPE_OTHER){
                textView.setText(commentList.get(i).getName()+":"+commentList.get(i).getContent());
            }else{
                textView.setText("回复:@"+commentList.get(i).getName()+":"+commentList.get(i).getContent());
            }
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommentConfig commentConfig = new CommentConfig();
                    commentConfig.itemPosition = pos;
                    commentConfig.commentItemPosition = finalI;
                    commentConfig.commentType = CommentConfig.Type.REPLY;
                    temClick.itemClick(commentConfig);
                }
            });
            pinlunContainer.addView(textView);
        }
    }

    private static class HolderView {
        TextView title;
        TextView content;
        TextView pinglun;
        LinearLayout pinlunContainer;
    }


    public interface IItemClick {
        void itemClick(CommentConfig commentConfig);
    }
}
