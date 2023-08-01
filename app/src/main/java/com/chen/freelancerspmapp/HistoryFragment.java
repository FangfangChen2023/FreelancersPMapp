package com.chen.freelancerspmapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.freelancerspmapp.helper.HistoryRecyclerAdapter;
import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.Entity.Task;

import java.util.ArrayList;
import java.util.Date;

public class HistoryFragment extends Fragment {

    ArrayList<Project> historyProjList = new ArrayList<>();
    Project historyProj;
    private SharedPreferences sharedPreferences;

    public HistoryFragment(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

//    public static HistoryFragment newInstance() {
//        HistoryFragment fragment = new HistoryFragment();
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.history_recyclerview);

        // TODO: Test Data. Delete later.
        ArrayList<Task> taskList = new ArrayList<>();
        Task task;
//        task = new Task("Task 7", "It is the task 7.", new Date(), new Date(), 2);
//        taskList.add(task);
//        task = new Task("Task 8", "It is the task 8.", new Date(), new Date(), 2);
//        taskList.add(task);

//        historyProj = new Project("1", "Project 1", new Date(), new Date(), taskList);
//        historyProjList.add(historyProj);
//        historyProj = new Project("2","Project 2", new Date(), new Date(), taskList);
//        historyProjList.add(historyProj);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new HistoryRecyclerAdapter(historyProjList));

        return view;
    }
}