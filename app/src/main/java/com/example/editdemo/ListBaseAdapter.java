package com.example.editdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by guozhk on 16-7-26.
 */
public class ListBaseAdapter extends BaseAdapter {
    private Context mContext;
    private List<TestEntity> listdata;
    private LayoutInflater mInflater;
    private View.OnClickListener mClickListener;

    public ListBaseAdapter(Context mContext, List<TestEntity> listdata , View.OnClickListener clickListener) {
        this.mContext = mContext;
        this.listdata = listdata;
        this.mClickListener = clickListener;
        mInflater = LayoutInflater.from(mContext);
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
            holderView.name = (TextView) view.findViewById(R.id.item_name);
            holderView.pinlunContainer = (LinearLayout) view.findViewById(R.id.item_pinglun_layout);
            view.setTag(holderView);
        } else {
            holderView = (HolderView) view.getTag();
        }

        holderView.name.setText(listdata.get(i).getName());
        holderView.name.setTag(i);
       /* holderView.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommentConfig commentConfig = new CommentConfig();
                commentConfig.circlePosition = i;
                commentConfig.commentType = CommentConfig.Type.REPLY;


                ((MainActivity)mContext).updateEditTextBodyVisible(View.VISIBLE,commentConfig);
            }
        });*/
        holderView.name.setOnClickListener(mClickListener);

        if (listdata.get(i).getPinglunList() != null && listdata.get(i).getPinglunList().size() > 0) {
            initPinglunData(listdata.get(i).getPinglunList(), holderView.pinlunContainer);
        }
        return view;
    }

    private void initPinglunData(List<String> pinglunList, LinearLayout pinlunContainer) {
        if (pinglunList.isEmpty()) {
            return;
        }
        for (int i = 0; i < pinglunList.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setText(pinglunList.get(i));
            pinlunContainer.addView(textView);
        }
    }

    private static class HolderView {
        TextView name;
        LinearLayout pinlunContainer;
    }
}
