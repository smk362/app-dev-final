package com.appdevcourse.homeworkorganizer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ToDoListView extends Fragment implements ToDoCustomAdapter.AdapterOnClickHandler, CompletedCustomAdapter.AdapterOnClickHandler {
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mAdapter2;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    private ArrayList<Assignment> assignments;
    private ArrayList<Assignment> completed;
    private RecyclerView assignmentsList;
    private RecyclerView completedList;
    public DataBus2 mDataBus2;

    public ToDoListView() {
        // Required empty public constructor
    }

    public interface DataBus2 {
        public void transferdata2(ArrayList<Assignment> assignmentList, ArrayList<Assignment> completed);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mDataBus2 = (DataBus2) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_to_do_list_view, container, false);

        assignments = new ArrayList<>();
        completed = new ArrayList<>();
        assignmentsList = rootView.findViewById(R.id.assignments);
        completedList = rootView.findViewById(R.id.completed);
        Bundle b = getArguments();
        if(b != null) {
            for(String key : b.keySet()) {
                Assignment a = (Assignment) b.getSerializable(key);
                if(a.isCompleted()) {
                    completed.add(a);
                } else {
                    assignments.add(a);
                }
            }
        }

        loadAssignmentsRecycler(assignmentsList);
        loadCompletedRecycler(completedList);

        return rootView;
    }

    public void loadAssignmentsRecycler(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ToDoCustomAdapter(assignments, this, new ToDoCustomAdapter.Check() {
            @Override
            public void onClick(View v, int position) {
                Assignment checked = assignments.get(position);
                checked.setCompleted(true);
                assignments.remove(checked);
                completed.add(checked);
                mAdapter.notifyDataSetChanged();
                mDataBus2.transferdata2(assignments, completed);
            }
        }, new ToDoCustomAdapter.Delete() {
            @Override
            public void onDelete(View view, int position) {
                Assignment deleted = assignments.get(position);
                assignments.remove(deleted);
                mAdapter.notifyDataSetChanged();
                mDataBus2.transferdata2(assignments, completed);
            }
        });

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    public void loadCompletedRecycler(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        layoutManager2 = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager2);

        mAdapter2 = new CompletedCustomAdapter(completed, this, new CompletedCustomAdapter.Check() {
            @Override
            public void onClick(View v, int position) {
                Assignment checked = completed.get(position);
                Log.d("test7", completed.get(position).isCompleted() + "");
                checked.setCompleted(false);
                assignments.add(checked);
                completed.remove(checked);
                mAdapter2.notifyDataSetChanged();
                mDataBus2.transferdata2(assignments, completed);
            }
        }, new CompletedCustomAdapter.Delete() {
            @Override
            public void onDelete(View view, int position) {
                Assignment deleted = completed.get(position);
                completed.remove(deleted);
                mAdapter.notifyDataSetChanged();
                mDataBus2.transferdata2(assignments, completed);
            }
        });

        recyclerView.setAdapter(mAdapter2);
        mAdapter2.notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
    }
}
