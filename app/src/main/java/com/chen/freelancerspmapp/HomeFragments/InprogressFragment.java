package com.chen.freelancerspmapp.HomeFragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
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
    private View taskDetailView;

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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        View view = inflater.inflate(R.layout.fragment_inprogress, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.ongoing_recyclerview);
        taskDetailView = inflater.inflate(R.layout.detail_dialog, container, false);

        AlertDialog taskDetailDialog;
        AlertDialog.Builder detailDialogBuilder = new AlertDialog.Builder(getContext());
        detailDialogBuilder.setCancelable(true);
        detailDialogBuilder.setView(taskDetailView);
        detailDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        taskDetailDialog = detailDialogBuilder.create();

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
                        List<Task> temp = doingTaskList.getValue();
                        Task task = temp.get(position);
                        EditText title = taskDetailView.findViewById(R.id.task_name);
                        title.setText(task.getName());
                        TextView proj = taskDetailView.findViewById(R.id.belongs_to);
                        proj.setText(sharedViewModel.getCurrentProj().getValue().getName());
                        TextView planning = taskDetailView.findViewById(R.id.planning);
                        planning.setText(task.getPlanningDateToString());
                        TextInputEditText detail = taskDetailView.findViewById(R.id.description);
                        detail.setText(task.getDetail());

                        TextView real = taskDetailView.findViewById(R.id.real);
                        real.setText(simpleDateFormat.format(new Date(task.getActualStartDate())));
                        taskDetailDialog.show();
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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<BusyTime> busyDoing = taskViewModel.getBusyInDoing();
        busyDoing.forEach(busyTime -> {
            String startDate = busyTime.getDurationStartString().substring(0,5);
            String endDate = busyTime.getDurationDueString().substring(0,5);
            String toastText = "You are busy between " + startDate + " and " + endDate;
            Toast.makeText(requireActivity().getApplication(),toastText,Toast.LENGTH_SHORT).show();
        });

    }
}