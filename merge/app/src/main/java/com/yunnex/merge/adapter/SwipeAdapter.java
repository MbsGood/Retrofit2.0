package com.yunnex.merge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunnex.merge.R;

import java.util.ArrayList;

/**
 * author ChenCHaoXue
 * Created by supercard on 2016/6/23 18:21
 */
public class SwipeAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    ArrayList<String> mList=new ArrayList<>();
    public SwipeAdapter(Context context, ArrayList<String> list) {
        this.mContext=context;
        this.mList=list;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() <= 0) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.listview_item,null);
            viewHolder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }

        viewHolder.tv_price.setText(mList.get(position));

        return convertView;
    }
    private static class ViewHolder {
        TextView tv_price;
    }
    public void setList(ArrayList<String> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        mList = list;
        notifyDataSetChanged();
    }

    public void addList(ArrayList<String> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        mList.addAll(list);
        notifyDataSetChanged();
    }

}
