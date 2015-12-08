package com.example.johnpconsidine.classshare.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnpconsidine.classshare.R;

import java.util.List;

/**
 * Created by johnpconsidine on 12/6/15.
 */
public class NavDrawerAdapter extends BaseAdapter {
    protected Context mContext;
    protected List<String> mMenuItems;

    public NavDrawerAdapter(Context context, List<String> items) {
        mContext = context;
        mMenuItems = items;
        Log.v("Hi", "its happening");


    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.navdrawer_item, null);
            holder = new ViewHolder();
            holder.navText = (TextView) convertView.findViewById(R.id.navdrawerText);
            holder.navIcon = (ImageView) convertView.findViewById(R.id.navdrawerIcon);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.navText.setText(mMenuItems.get(position));
        Log.v("jhi", mMenuItems.get(position));
        if (position == 0) {
            holder.navIcon.setImageResource(R.drawable.ic_plus_one_black_24dp);
        }
        else if (position == getCount()-1) {
            holder.navIcon.setImageResource(R.drawable.ic_power_settings_new_black_24dp);
        }
        else {
            holder.navIcon.setImageResource(R.drawable.ic_launch_black_24dp);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView navText;
        ImageView navIcon;
    }

}