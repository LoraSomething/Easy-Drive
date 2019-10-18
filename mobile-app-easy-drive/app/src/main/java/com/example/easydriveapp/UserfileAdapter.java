package com.example.easydriveapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserfileAdapter extends RecyclerView.Adapter<UserfileAdapter.ViewHolder> {

    private ArrayList<Userfile> userfiles;
    TextView tvFileName;

    ItemClicked activity;

    public interface ItemClicked
    {
        void onItemClicked(int index);
    }

    public UserfileAdapter(Context context,ArrayList<Userfile> list) {
        this.userfiles = list;
        activity = (ItemClicked) context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvFileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFileName = itemView.findViewById(R.id.tvFileName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onItemClicked(userfiles.indexOf((Userfile)view.getTag()));
                }
            });

        }
    }

    @NonNull
    @Override
    public UserfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_files,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserfileAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(userfiles.get(i));
        viewHolder.tvFileName.setText(userfiles.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return userfiles.size();
    }
}
