package com.teiki.cafea1e.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.teiki.cafea1e.R;

/**
 * Created by antoinegaltier on 12/05/2014.
 */
public class NavigationAdapter extends BaseAdapter {

    Context context;
    String[] menuTitle;
    LayoutInflater inflater;

    public NavigationAdapter(Context context) {
        this.context = context;

        menuTitle = context.getResources().getStringArray(R.array.menu_array);
    }

    @Override
    public int getCount() {
        return menuTitle.length;
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position <= getCount())
            return menuTitle[position];
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.navigationitem, parent,false);

        TextView textView = (TextView) itemView.findViewById(R.id.navigationtexttitle);
        LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.navigationlayout);


        if (position%2 == 0)
            layout.setBackgroundResource(R.drawable.drawer_background_color_fonce);
        else
            layout.setBackgroundResource(R.drawable.drawer_background_color_clair);

        textView.setText(menuTitle[position]);

        return itemView;
    }
}