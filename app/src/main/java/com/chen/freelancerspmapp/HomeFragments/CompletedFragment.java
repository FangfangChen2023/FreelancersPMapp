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

import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.helper.CompletedRecyclerAdapter;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.helper.DoingRecyclerAdapter;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.ProjectViewModel;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;
import com.chen.freelancerspmapp.viewmodel.TaskViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CompletedFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private MutableLiveData<List<Task>> completedTaskList = new MutableLiveData<>(new ArrayList<>());
    private TaskViewModel taskViewModel;
    private View taskDetailView;
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
        taskDetailView = inflater.inflate(R.layout.detail_dialog, container, false);
        //---------------------------Dialog for Task Detail--------------------------------------//
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
        //---------------------------Dialog for Task Detail--------------------------------------//

        MyViewModelFactory myViewModelFactory = new MyViewModelFactory(requireActivity().getApplication());

        if (sharedViewModel.getCurrentProj().getValue() != null) {
            currentProjID = sharedViewModel.getCurrentProj().getValue().getProjectID();
        }

        myViewModelFactory.setProjectID(currentProjID);
        taskViewModel = new ViewModelProvider(this, myViewModelFactory).get(TaskViewModel.class);
        taskViewModel.initializeLists(currentProjID);

        completedTaskList.setValue(taskViewModel.getDoneTasks().getValue());

        taskViewModel.getDoneTasks().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                completedTaskList.setValue(taskViewModel.getDoneTasks().getValue());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        completedRecyclerAdapter = new CompletedRecyclerAdapter(completedTaskList.getValue());

        // task operations button setting
        completedRecyclerAdapter.setOnMoreActionBtnClickListener((position, moreActionsPopupMenu) -> {
            moreActionsPopupMenu.setOnMenuItemClickListener(actionItem -> {
                switch (actionItem.getItemId()) {

                    case R.id.see_details: {
                        List<Task> temp = completedTaskList.getValue();
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
                        real.setText(task.getActualDateToString());
                        taskDetailDialog.show();
                        return true;
                    }

                    case R.id.delete_item: {
                        // delete the tasks
                        List<Task> temp = completedTaskList.getValue();
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

        recyclerView.setAdapter(completedRecyclerAdapter);
        completedTaskList.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                completedRecyclerAdapter.refreshDoneTaskList(tasks);
                completedRecyclerAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}