package com.example.johnpconsidine.classshare.Adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.johnpconsidine.classshare.R;

import java.util.List;

/**
 * Created by johnpconsidine on 12/8/15.
 */
public class ClassGroupAdapter extends ArrayAdapter<String> {

        protected Context mContext;
        protected List<String> mGroups;

        public ClassGroupAdapter(Context context, List<String> groups) {
            super(context, R.layout.groupitem, groups);
            mContext = context;
            mGroups = groups;

        }

        public View getView (int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.groupitem, null);
                holder = new ViewHolder();
                holder.iconImageView = (ImageView) convertView.findViewById(R.id.groupIcon);
                holder.groupLabel = (TextView) convertView.findViewById(R.id.groupItem);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }
            String group = mGroups.get(position);

            holder.iconImageView.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);

            holder.groupLabel.setText(group);
            holder.groupLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            return convertView;
        }

        private static class ViewHolder {
            ImageView iconImageView;
            TextView groupLabel;
        }


}
