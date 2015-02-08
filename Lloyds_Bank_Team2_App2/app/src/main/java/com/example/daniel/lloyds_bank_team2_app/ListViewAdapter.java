package com.example.daniel.lloyds_bank_team2_app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Daniel on 08/02/2015.
 */
public class ListViewAdapter extends BaseAdapter {
    public ArrayList<HashMap<String,String>> list;
    Activity activity;

    public ListViewAdapter(Activity activity, ArrayList<HashMap<String,String>> list){
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    private class ViewHolder {
        TextView txtDate;
        TextView txtDescription;
        TextView txtType;
        TextView txtIn;
        TextView txtOut;
        TextView txtBalance;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.statement_view_row,null);
            holder = new ViewHolder();

            holder.txtDate = (TextView) convertView.findViewById(R.id.date_text);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.description_text);
            holder.txtType = (TextView) convertView.findViewById(R.id.type_text);
            holder.txtIn = (TextView) convertView.findViewById(R.id.in_text);
            holder.txtOut = (TextView) convertView.findViewById(R.id.out_text);
            holder.txtBalance = (TextView) convertView.findViewById(R.id.balance_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, String> map = list.get(position);

        holder.txtDate.setText(map.get("Date"));
        holder.txtDescription.setText(map.get("Description"));
        holder.txtType.setText(map.get("Type"));
        holder.txtIn.setText(map.get("Income"));
        holder.txtOut.setText(map.get("Outcome"));
        holder.txtBalance.setText(map.get("Balance"));

        return convertView;
    }
}
