package com.appdevcourse.homeworkorganizer;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class AddNewAssignment extends Fragment {
    private View rootView;
    private EditText assignment;
    private EditText dueDate;
    private Button add;
    private Button clear;
    private Assignment added;
    DataBus1 mDataBus1;

    public AddNewAssignment() {
        // Required empty public constructor
    }

    public interface DataBus1 {
        public void transferData(Assignment assignment);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mDataBus1 = (DataBus1) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_new_assignment, container, false);

        assignment = rootView.findViewById(R.id.assignmentTextInput);
        dueDate = rootView.findViewById(R.id.dateTextInput);
        dueDate = rootView.findViewById(R.id.dateTextInput);
        add = rootView.findViewById(R.id.addButton);
        clear = rootView.findViewById(R.id.clearButton);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                added = new Assignment(assignment.getText().toString(), dueDate.getText().toString());
                mDataBus1.transferData(added);
                assignment.setText("");
                dueDate.setText("");
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignment.setText("");
                dueDate.setText("");
            }
        });
        return rootView;
    }
}
