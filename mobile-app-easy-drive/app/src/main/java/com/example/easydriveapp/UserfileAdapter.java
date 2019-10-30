package com.example.easydriveapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserfileAdapter extends RecyclerView.Adapter<UserfileAdapter.ViewHolder> {

    private ArrayList<Userfile> userfiles;
    TextView tvFileName;
    ImageView ivFiletype;

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
        ImageView ivFiletype;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvFileName = itemView.findViewById(R.id.tvFileName);
            ivFiletype = itemView.findViewById(R.id.ivFiletype);

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
        if (userfiles.get(i).getExtension() != null) {
            viewHolder.tvFileName.setText(userfiles.get(i).getName() +"."+ userfiles.get(i).getExtension());
        }
        else{
            viewHolder.tvFileName.setText(userfiles.get(i).getName());
        }
        if ( userfiles.get(i).getFileType() == Userfile.FileType.IMAGE){
            viewHolder.ivFiletype.setImageResource(R.drawable.png);
        }
        if ( userfiles.get(i).getFileType() == Userfile.FileType.PDF){
            viewHolder.ivFiletype.setImageResource(R.drawable.pdf);
        }
        if ( userfiles.get(i).getFileType() == Userfile.FileType.TEXT){
            viewHolder.ivFiletype.setImageResource(R.drawable.text);
        }
    }

    @Override
    public int getItemCount() {
        return userfiles.size();
    }
}
