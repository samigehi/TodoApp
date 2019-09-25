package com.kumar.sumeet.ziro.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kumar.sumeet.ziro.R;
import com.kumar.sumeet.ziro.model.Task;

import java.util.List;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.ViewHolder> {

    private List<Task> mData;
    private LayoutInflater mInflater;

    public TodoRecyclerAdapter(Context context, List<Task> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task t = mData.get(position);
        holder.task.setText(t.getTask());
        holder.date.setText(t.getDate());
        if(t.isComplete()){
            holder.status.setText("Completed");
            holder.status.setTextColor(Color.GREEN);
        }
        else {
            holder.status.setText("Not Complete");
            holder.status.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView task, date, status;

        ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.tv_task);
            date = itemView.findViewById(R.id.tv_date);
            status = itemView.findViewById(R.id.tv_status);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setHolderId(getAdapterPosition());
                    return false;
                }
            });

        }

        @Override
        public void onClick(View view) {
            setHolderId(getAdapterPosition());
            view.showContextMenu();
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select");
            menu.add(0, UPDATE, 0, "Update");//groupId, itemId, order, title
            menu.add(0, DELETE, 0, "Delete");
            menu.add(0, COMPLETE, 0, "Complete?");
        }

    }

    public static final int UPDATE = 11;
    public static final int DELETE = 12;
    public static final int COMPLETE = 13;

    private int holderId;
    public int getHolderId() {
        return holderId;
    }

    public void setHolderId(int holderId) {
        this.holderId = holderId;
    }

    Task getTask(int id) {
        return mData.get(id);
    }

}