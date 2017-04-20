package dhu.cst.zjm.encryptmvp.mvp.view.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dhu.cst.zjm.encryptmvp.R;
import dhu.cst.zjm.encryptmvp.mvp.model.File;

/**
 * Created by zjm on 3/2/2017.
 */

public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.MyViewHolder> {
    private List<File> list = new ArrayList<>();
    private LayoutInflater mInflater;
    private OnItemClickListener listener;

    public FileListAdapter(Context mContext, List<File> list) {
        mInflater = LayoutInflater.from(mContext);
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.rv_menu_file_list_item, parent,
                false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        File file = list.get(position);
        holder.tv_lv_menu_file_name.setText(file.getName());
        holder.tv_lv_menu_file_size.setText(file.getSize());
        holder.tv_lv_menu_file_upload_time.setText(file.getUploadTime().toString());
        holder.rl_lv_menu_file_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rl_lv_menu_file_item;
        TextView tv_lv_menu_file_name;
        TextView tv_lv_menu_file_size;
        TextView tv_lv_menu_file_upload_time;

        public MyViewHolder(View view) {
            super(view);
            rl_lv_menu_file_item = (RelativeLayout) view.findViewById(R.id.rl_lv_menu_file_item);
            tv_lv_menu_file_name = (TextView) view.findViewById(R.id.tv_lv_menu_file_name);
            tv_lv_menu_file_size = (TextView) view.findViewById(R.id.tv_lv_menu_file_size);
            tv_lv_menu_file_upload_time = (TextView) view.findViewById(R.id.tv_lv_menu_file_upload_time);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}