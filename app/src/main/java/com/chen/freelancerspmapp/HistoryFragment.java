package com.chen.freelancerspmapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.freelancerspmapp.helper.HistoryRecyclerAdapter;
import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.ProjectViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    private MutableLiveData<List<Project>> historyProjList = new MutableLiveData<>(new ArrayList<>());
    private MyViewModelFactory myViewModelFactory;
    private ProjectViewModel projectViewModel;
    private View emptyHistory;


    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.history_recyclerview);
        emptyHistory = view.findViewById(R.id.empty_history_page);
        View historyPage = view.findViewById(R.id.history_page);

        myViewModelFactory = new MyViewModelFactory(requireActivity().getApplication());
        projectViewModel = new ViewModelProvider(this, myViewModelFactory).get(ProjectViewModel.class);
        historyProjList.setValue(projectViewModel.getHistoryProjects());

//        historyProjList.observe(getViewLifecycleOwner(), new Observer<List<Project>>() {
//            @Override
//            public void onChanged(List<Project> projects) {
//
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new HistoryRecyclerAdapter(historyProjList.getValue()));

        if(historyProjList.getValue().size() > 0){
            historyPage.setVisibility(View.VISIBLE);
        }else{
            emptyHistory.setVisibility(View.VISIBLE);
        }

        return view;
    }
}