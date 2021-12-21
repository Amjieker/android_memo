package com.example.lin.Tool;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lin.R;

import java.util.LinkedList;

/**
 * Listview 的adapter 用于 聊天渲染记录用
 */

public class ListAdapter extends BaseAdapter {
    private Context context;
    private String name;
    private LinkedList<String> list;

    public ListAdapter(Context context, LinkedList<String> list,String name) {
        this.context = context;
        this.list = list;
        this.name = name;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item, parent, false);
//            str = list.get(position);
//            textView = (TextView) convertView.findViewById(R.id.text_list);
//        } else {
//            textView = (TextView) convertView.getTag();
        }
        String str = list.get(position);
        textView = (TextView) convertView.findViewById(R.id.text_list);

        if(str.length()>name.length() && str.startsWith(name)){
            textView.setGravity(Gravity.RIGHT);
            ((ImageView) convertView.findViewById(R.id.img_item_right)).setImageResource(R.mipmap.cheems);
            ((ImageView) convertView.findViewById(R.id.img_item_right)).setVisibility(View.VISIBLE);
            ((ImageView) convertView.findViewById(R.id.img_item_left)).setVisibility(View.GONE);

            ((TextView) convertView.findViewById(R.id.left_text)).setVisibility(View.GONE);
            ((TextView) convertView.findViewById(R.id.right_text)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.right_text)).setText(str.substring(0, Math.max(0, str.indexOf(":"))));
        }else{
            textView.setGravity(Gravity.LEFT);
            ((ImageView) convertView.findViewById(R.id.img_item_left)).setImageResource(R.mipmap.dog);
            ((ImageView) convertView.findViewById(R.id.img_item_left)).setVisibility(View.VISIBLE);
            ((ImageView) convertView.findViewById(R.id.img_item_right)).setVisibility(View.GONE);

            ((TextView) convertView.findViewById(R.id.left_text)).setVisibility(View.VISIBLE);
            ((TextView) convertView.findViewById(R.id.right_text)).setVisibility(View.GONE);
//            System.out.println("第几个 " + str);
            ((TextView) convertView.findViewById(R.id.left_text)).setText(str.substring(0, Math.max(0, str.indexOf(":"))));

        }
        textView.setText(str.substring(str.indexOf(":")+1));
        return convertView;
    }
}
