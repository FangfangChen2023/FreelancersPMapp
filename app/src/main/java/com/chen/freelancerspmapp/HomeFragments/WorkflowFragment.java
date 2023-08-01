package com.chen.freelancerspmapp.HomeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.helper.TimeLineAdapter;
import com.chen.freelancerspmapp.Entity.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkflowFragment extends Fragment {

    List<Task> allTaskList = new ArrayList<>();
    private Task task;

    public WorkflowFragment() {
        // Required empty public constructor
    }


    public static WorkflowFragment newInstance() {
        WorkflowFragment fragment = new WorkflowFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.timeline_recycler, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.timeline_recyclerView);
        // TODO: Test data. Delete later.
//        task = new Task("T7", "Task 1", "It is the task 1.", new Date(), new Date(), 0);
//        allTaskList.add(task);
//        task = new Task("T8","Task 2", "It is the task 2.", new Date(), new Date(), 0);
//        allTaskList.add(task);
//        task = new Task("T9","Task 3", "It is the task 3.", new Date(), new Date(), 0);
//        allTaskList.add(task);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new TimeLineAdapter(allTaskList));

        return recyclerView;
    }
}