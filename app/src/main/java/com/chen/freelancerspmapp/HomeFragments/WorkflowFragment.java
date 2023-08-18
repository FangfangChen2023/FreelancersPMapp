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

import com.chen.freelancerspmapp.Entity.BusyTime;
import com.chen.freelancerspmapp.Entity.FreeTimeCard;
import com.chen.freelancerspmapp.Entity.WorkflowCard;
import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.helper.TimeLineAdapter;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;
import com.chen.freelancerspmapp.viewmodel.TaskViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WorkflowFragment extends Fragment {

    private MutableLiveData<List<Task>> allTaskList = new MutableLiveData<>(new ArrayList<>());
    private List<WorkflowCard> workflowList = new ArrayList();
    private SharedViewModel sharedViewModel;
    private TaskViewModel taskViewModel;

    private Long currentProjID;
    private TimeLineAdapter timeLineAdapter;

    public WorkflowFragment(SharedViewModel sharedViewModel) {
        this.sharedViewModel = sharedViewModel;
    }


    public static WorkflowFragment newInstance(SharedViewModel sharedViewModel) {
        WorkflowFragment fragment = new WorkflowFragment(sharedViewModel);
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


        MyViewModelFactory myViewModelFactory = new MyViewModelFactory(requireActivity().getApplication());

        if (sharedViewModel.getCurrentProj().getValue() != null) {
            currentProjID = sharedViewModel.getCurrentProj().getValue().getProjectID();
        }

        myViewModelFactory.setProjectID(currentProjID);
        taskViewModel = new ViewModelProvider(this, myViewModelFactory).get(TaskViewModel.class);
        taskViewModel.initializeLists(currentProjID);

        allTaskList.setValue(taskViewModel.getAllTasks().getValue());
        addFreeTimeCard(allTaskList.getValue());

        taskViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                allTaskList.setValue(taskViewModel.getAllTasks().getValue());
            }
        });

        List<BusyTime> busydoing = taskViewModel.getBusyInDoing();
        List<BusyTime> busydone = taskViewModel.getBusyInDone();
        List<BusyTime> busytodo = taskViewModel.getBusyInTodo();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        timeLineAdapter = new TimeLineAdapter(workflowList, busytodo, busydoing, busydone);
        recyclerView.setAdapter(timeLineAdapter);

        allTaskList.observe(getViewLifecycleOwner(), tasks -> {
            addFreeTimeCard(tasks);
            timeLineAdapter.refreshTaskList(workflowList, taskViewModel.getBusyInTodo(), taskViewModel.getBusyInDoing(), taskViewModel.getBusyInDone());
            timeLineAdapter.notifyDataSetChanged();
        });

        return recyclerView;
    }

    // The taskList is already sorted
    public void addFreeTimeCard(List<Task> taskList) {
        long lastDue;
        long thisStart;
        int freeDays;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        workflowList = new ArrayList<>();

        for (int i = 1; i <= taskList.size(); i++) {
            WorkflowCard workflowCard = new WorkflowCard();
            workflowCard.setTask(taskList.get(i - 1));
            workflowList.add(workflowCard);
            if (i == taskList.size()) {
                break;
            }
//  check free time
            lastDue = taskList.get(i - 1).getPlanningDueDate();
            thisStart = taskList.get(i).getPlanningStartDate();
            if (taskList.get(i - 1).getActualDueDate() != null) {
                lastDue = taskList.get(i - 1).getActualDueDate();
            }
            if (taskList.get(i).getActualStartDate() != null) {
                thisStart = taskList.get(i).getActualStartDate();
            }
            freeDays = (int) TimeUnit.MILLISECONDS.toDays(thisStart - lastDue);
            if (freeDays > 1) {
                String freeTimeText = " ";
                FreeTimeCard freeTimeCard = new FreeTimeCard();
                freeTimeCard.setFreeDays(freeDays);

                long lastFree = TimeUnit.MILLISECONDS.toDays(lastDue)+1;
                long thisFree = TimeUnit.MILLISECONDS.toDays(thisStart)-1;

                freeTimeCard.setFreeStart(TimeUnit.DAYS.toMillis(lastFree));
                freeTimeCard.setFreeEnd(TimeUnit.DAYS.toMillis(thisFree));

                String lastFreeText = simpleDateFormat.format(new Date(TimeUnit.DAYS.toMillis(lastFree))).substring(0, 5);
                String thisFreeText = simpleDateFormat.format(new Date(TimeUnit.DAYS.toMillis(thisFree))).substring(0, 5);

                if(freeDays == 2){
                    freeTimeText = "You don't have any work on " + lastFreeText;
                }else {
                    freeTimeText = "You don't have any work between " + lastFreeText + " and " + thisFreeText;
                }

                freeTimeCard.setFreeTimeText(freeTimeText);
                WorkflowCard card = new WorkflowCard();
                card.setFreeTimeCard(freeTimeCard);
                workflowList.add(card);
            }

        }
    }

}