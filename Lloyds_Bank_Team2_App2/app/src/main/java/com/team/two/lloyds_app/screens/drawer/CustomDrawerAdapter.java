package com.team.two.lloyds_app.screens.drawer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.team.two.lloyds_app.R;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel on 18/03/2015.
 */
public class CustomDrawerAdapter extends RecyclerView.Adapter<CustomDrawerAdapter.MyViewHolder> {
    private final LayoutInflater inflater;
    private List<DrawerItem> data = Collections.emptyList();
    protected final Context context;

    public CustomDrawerAdapter(Context context, List<DrawerItem> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.listText.setText(data.get(position).getItemName());
        holder.listIcon.setImageResource(data.get(position).getImgResID());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView listText;
        ImageView listIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            listText = (TextView) itemView.findViewById(R.id.list_text);
            listIcon = (ImageView) itemView.findViewById(R.id.list_icon);

        }
    }
}

