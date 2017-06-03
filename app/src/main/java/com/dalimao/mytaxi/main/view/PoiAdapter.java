package com.dalimao.mytaxi.main.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.dalimao.mytaxi.R;

import java.util.List;

/**
 * Created by liuguangli on 17/3/23.
 */

public class PoiAdapter extends ArrayAdapter {
    private LayoutInflater inflater ;
    private List<String> data;
    private OnItemtClickListener mOnItemtClickListener;

    public PoiAdapter(Context context, List data) {
        super(context, R.layout.poi_list_item);
        this.data = data;
        inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void setOnItemtClickListener(OnItemtClickListener listener) {
        mOnItemtClickListener = listener;
    }

    public void setData(List<String> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();

    }



    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.poi_list_item, null);
            holder = new Holder();
            holder.textView = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
            /*convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemtClickListener != null) {
                        int pos = ((Holder)v.getTag()).id;
                        mOnItemtClickListener.onItemClick(pos);
                    }
                }
            });*/
        } else {
            Object tag = convertView.getTag();
            if (tag == null) {
                holder = new Holder();
                holder.textView = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) tag;
            }
        }
        holder.id = position;
        holder.textView.setText(data.get(position));

        return convertView;
    }


    class Holder {
        int id;
        TextView textView;
    }

    public static interface OnItemtClickListener {
        void onItemClick(int id);
    }
}
