package com.appdevcourse.homeworkorganizer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

class CompletedCustomAdapter extends RecyclerView.Adapter<CompletedCustomAdapter.CustomViewHolder> {
    private static ArrayList<Assignment> mAssignments;
    private static AdapterOnClickHandler mAdapterOnClickHandler;
    public Check check;
    public Delete delete;

    public CompletedCustomAdapter(ArrayList<Assignment> mAssignments, AdapterOnClickHandler ach, Check check, Delete delete) {
        this.mAssignments = mAssignments;
        this.check = check;
        this.delete = delete;
        mAdapterOnClickHandler = ach;
    }

    // Define a custom view holder for each item in the list
    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView assignmentTv;
        public TextView dateTv;
        public CheckBox checkTv;
        public ImageButton deleteTv;

        public CustomViewHolder(View v) {
            super(v);
            assignmentTv = v.findViewById(R.id.assignmentTextCell);
            dateTv = v.findViewById(R.id.dateTextCell);
            checkTv = v.findViewById(R.id.checkBox);
            deleteTv = v.findViewById(R.id.delete);
            v.setOnClickListener(this);

            checkTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check.onClick(v, getAdapterPosition());
                }
            });

            deleteTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete.onDelete(v, getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mAdapterOnClickHandler.onClick(adapterPosition);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Creates the new view for each cell. Instead of a simple TextView,
        // this can be a CardView or a ViewGroup
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_single_cell, parent, false);

        CustomViewHolder cvh = new CustomViewHolder(v);
        return cvh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        holder.assignmentTv.setText(mAssignments.get(position).getAssignment());
        holder.dateTv.setText(mAssignments.get(position).getDueDate());

        if(mAssignments.get(position).isCompleted()) {
            holder.checkTv.setChecked(true);
        } else {
            holder.checkTv.setChecked(false);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mAssignments.size();
    }

    public interface AdapterOnClickHandler {
        void onClick(int position);
    }

    public interface Check {
        void onClick(View view, int position);
    }

    public interface Delete {
        void onDelete(View view, int position);
    }
}

