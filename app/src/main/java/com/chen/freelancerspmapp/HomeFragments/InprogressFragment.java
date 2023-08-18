package com.chen.freelancerspmapp.HomeFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chen.freelancerspmapp.Entity.BusyTime;
import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.helper.DoingRecyclerAdapter;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.ProjectViewModel;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;
import com.chen.freelancerspmapp.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InprogressFragment extends Fragment {

    private MutableLiveData<List<Task>> doingTaskList = new MutableLiveData<>(new ArrayList<>());
    private TaskViewModel taskViewModel;
    private Long currentProjID;
    private SharedViewModel sharedViewModel;
    private DoingRecyclerAdapter doingRecyclerAdapter;


    public InprogressFragment(SharedViewModel sharedViewModel) {
        this.sharedViewModel = sharedViewModel;
    }

    public static InprogressFragment newInstance(SharedViewModel sharedViewModel) {
        InprogressFragment fragment = new InprogressFragment(sharedViewModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inprogress, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.ongoing_recyclerview);

        MyViewModelFactory myViewModelFactory = new MyViewModelFactory(requireActivity().getApplication());

        if(sharedViewModel.getCurrentProj().getValue()!=null){
            currentProjID = sharedViewModel.getCurrentProj().getValue().getProjectID();
        }

        myViewModelFactory.setProjectID(currentProjID);
        taskViewModel = new ViewModelProvider(this, myViewModelFactory).get(TaskViewModel.class);
        taskViewModel.initializeLists(currentProjID);

        doingTaskList.setValue(taskViewModel.getInProgressTasks().getValue());

        taskViewModel.getInProgressTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                doingTaskList.setValue(taskViewModel.getInProgressTasks().getValue());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        doingRecyclerAdapter = new DoingRecyclerAdapter(doingTaskList.getValue());

        // task operations button setting
        doingRecyclerAdapter.setOnMoreActionBtnClickListener((position, moreActionsPopupMenu) -> {
            moreActionsPopupMenu.setOnMenuItemClickListener(actionItem -> {
                switch (actionItem.getItemId()) {

                    case R.id.state_changeto_done: {
                        // change the task state to done
                        List<Task> temp = doingTaskList.getValue();
                        Task task = temp.remove(position);
                        task.setStatus(2);
                        task.setActualDueDate(new Date().getTime());
                        taskViewModel.updateTask(task);
                        doingTaskList.setValue(temp);
                        return true;
                    }

                    case R.id.see_details: {
                        // TODO: the task details
                        return true;
                    }

                    case R.id.delete_item: {
                        // delete the tasks
                        List<Task> temp = doingTaskList.getValue();
                        Task task = temp.remove(position);
                        taskViewModel.deleteTask(task);
                        doingTaskList.setValue(temp);
                        return true;
                    }
                    default:
                        return false;
                }

            });
            moreActionsPopupMenu.show();
        });

        recyclerView.setAdapter(doingRecyclerAdapter);

        doingTaskList.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                doingRecyclerAdapter.refreshDoingTaskList(tasks);
                doingRecyclerAdapter.notifyDataSetChanged();
            }
        });

       List<BusyTime> busyDoing = taskViewModel.getBusyInDoing();
       busyDoing.forEach(busyTime -> {
           String startDate = busyTime.getDurationStartString().substring(0,6);
           String endDate = busyTime.getDurationDueString().substring(0,6);
           String toastText = "You are busy between " + startDate + " and " + endDate;
           Toast.makeText(requireActivity().getApplication(),toastText,Toast.LENGTH_SHORT).show();
       });

        return view;
    }
}