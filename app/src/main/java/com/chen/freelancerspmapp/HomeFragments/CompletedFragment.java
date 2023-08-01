package com.chen.freelancerspmapp.HomeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.helper.CompletedRecyclerAdapter;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.helper.DoingRecyclerAdapter;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.ProjectViewModel;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;
import com.chen.freelancerspmapp.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CompletedFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private MutableLiveData<List<Task>> completedTaskList = new MutableLiveData<>(new ArrayList<>());
    private TaskViewModel taskViewModel;

    private Long currentProjID;
    private CompletedRecyclerAdapter completedRecyclerAdapter;

    public CompletedFragment(SharedViewModel sharedViewModel) {
        this.sharedViewModel = sharedViewModel;
    }


    // TODO: Rename and change types and number of parameters
    public static CompletedFragment newInstance(SharedViewModel sharedViewModel) {
        CompletedFragment fragment = new CompletedFragment(sharedViewModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.completed_recyclerview);

        MyViewModelFactory myViewModelFactory = new MyViewModelFactory(requireActivity().getApplication());

        if(sharedViewModel.getCurrentProj().getValue()!=null){
            currentProjID = sharedViewModel.getCurrentProj().getValue().getProjectID();
        }

        myViewModelFactory.setProjectID(currentProjID);
        taskViewModel = new ViewModelProvider(this, myViewModelFactory).get(TaskViewModel.class);
        taskViewModel.initializeLists(currentProjID);

        completedTaskList.setValue(taskViewModel.getDoneTasks().getValue());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        completedRecyclerAdapter = new CompletedRecyclerAdapter(completedTaskList.getValue());

        // task operations button setting
        completedRecyclerAdapter.setOnMoreActionBtnClickListener((position, moreActionsPopupMenu) -> {
            moreActionsPopupMenu.setOnMenuItemClickListener(actionItem -> {
                switch (actionItem.getItemId()) {

                    case R.id.see_details: {
                        // TODO: the task details
                        return true;
                    }

                    case R.id.delete_item: {
                        // delete the tasks
                        List<Task> temp =completedTaskList.getValue();
                        Task task = temp.remove(position);
                        taskViewModel.deleteTask(task);
                        completedTaskList.setValue(temp);
                        return true;
                    }
                    default:
                        return false;
                }
            });
            moreActionsPopupMenu.show();
        });

        completedTaskList.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                completedRecyclerAdapter.refreshDoingTaskList(tasks);
                completedRecyclerAdapter.notifyDataSetChanged();
            }
        });

        recyclerView.setAdapter(completedRecyclerAdapter);

        return view;
    }
}