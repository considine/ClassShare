package com.example.johnpconsidine.classshare.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.johnpconsidine.classshare.R;

import java.util.List;

/**
 * Created by johnpconsidine on 12/6/15.
 */
public class MessageAdapter extends ArrayAdapter<String>{
    protected Context mContext;
    protected List<String> mMessages;
    protected List<String> mInitials;
    protected List<Boolean> mSender;

    public MessageAdapter(Context context, List<String> messages, List<String> initials, List<Boolean> sender) {
        super(context, R.layout.message_item, messages);
        mContext = context;
        mMessages = messages;
        mInitials = initials;
        mSender = sender;

    }

    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.initials = (TextView) convertView.findViewById(R.id.initials);
            holder.message = (TextView) convertView.findViewById(R.id.TextMessage);
            if (mSender.get(position)) {
                Log.v("this", "true");
                holder.initials.setVisibility(View.INVISIBLE);
                holder.message.setGravity(Gravity.END);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.message.setBackgroundResource(R.drawable.blue);
            }

            else if (mInitials.get(position).equals(mContext.getString(R.string.admin_sender))) {
                holder.initials.setVisibility(View.INVISIBLE);
                holder.message.setGravity(Gravity.CENTER_HORIZONTAL);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                holder.message.setBackgroundResource(R.drawable.gray);
            }
            else {

                holder.initials.setVisibility(View.VISIBLE);
                holder.message.setGravity(Gravity.START);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.message.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                holder.message.setBackgroundResource(R.drawable.green);
            }
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.initials.setText(mInitials.get(position));

        holder.message.setText(mMessages.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView initials;
        TextView message;

    }



}