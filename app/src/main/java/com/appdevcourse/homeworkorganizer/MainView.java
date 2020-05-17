package com.appdevcourse.homeworkorganizer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainView extends AppCompatActivity implements AddNewAssignment.DataBus1, ToDoListView.DataBus2 {
    private ArrayList<Assignment> assignments;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);

        assignments = new ArrayList<>();

        BottomNavigationView navBar = findViewById(R.id.navigation);

        BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.toDo:
                                loadBundle(assignments);
                                openFragment(new ToDoListView());
                                return true;
                            case R.id.brainBreak:
                                openFragment(new BrainBreak());
                                return true;
                            case R.id.add:
                                openFragment(new AddNewAssignment());
                                return true;
                        }
                        return false;
                    }
                };
        navBar.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        openFragment(new ToDoListView());
    }

    public void loadBundle(ArrayList<Assignment> list) {
        bundle = new Bundle();
        for(int i = 0; i < list.size(); i++) {
            bundle.putSerializable("assignment"+i, assignments.get(i));
        }
    }
    public void openFragment(Fragment f){
        f.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f);
        transaction.commit();
    }

    @Override
    public void transferData(Assignment assignment) {
        assignments.add(assignment);
    }

    @Override
    public void transferdata2(ArrayList<Assignment> assignmentList, ArrayList<Assignment> completed) {
        assignments.clear();
        for(int i = 0; i < assignmentList.size(); i++) {
            assignments.add(assignmentList.get(i));
        }

        for(int i = 0; i < completed.size(); i++) {
            assignments.add(completed.get(i));
        }
    }
}
