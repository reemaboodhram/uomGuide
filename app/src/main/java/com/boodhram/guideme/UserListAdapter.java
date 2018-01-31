package com.boodhram.guideme;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boodhram.guideme.Utils.AccountDTO;

import java.util.List;

/**
 * Created by Jessnah on 1/31/2018.
 */

public class UserListAdapter  extends ArrayAdapter<AccountDTO> {

    Context context;
    int layoutResourceId;
    List<AccountDTO> list;

    public UserListAdapter(Context context, int layoutResourceId, List<AccountDTO> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.list = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.imgIcon = row.findViewById(R.id.imgIcon);
            holder.txtTitle = row.findViewById(R.id.txtTitle);

            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }

        AccountDTO accountDTO = list.get(position);
        holder.txtTitle.setText(accountDTO.getUsername());
        if(accountDTO.getOnlineStatus()){
            holder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.presence_online));
        }else{
            holder.imgIcon.setImageDrawable(ContextCompat.getDrawable(context,android.R.drawable.presence_invisible));
        }

        return row;
    }

    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}