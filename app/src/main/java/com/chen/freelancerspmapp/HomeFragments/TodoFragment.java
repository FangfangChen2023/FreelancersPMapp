package com.chen.freelancerspmapp.HomeFragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.helper.TodoRecyclerAdapter;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.OnMoreActionButtonClickListener;
import com.chen.freelancerspmapp.viewmodel.ProjectViewModel;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;
import com.chen.freelancerspmapp.viewmodel.TaskViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TodoFragment extends Fragment {

    private MutableLiveData<List<Task>> todoTaskList = new MutableLiveData<>(new ArrayList<>());
    private TaskViewModel taskViewModel;
    private Long currentProjID;

    private DatePickerDialog datePickerDialogForStart;
    private DatePickerDialog datePickerDialogForDue;
    private SharedViewModel sharedViewModel;
    private TodoRecyclerAdapter todoRecyclerAdapter;
    private String selectedStartDate;
    private Long startDateLong;
    private String selectedDueDate;
    private Long dueDateLong;
    private String DatePattern = "MM-dd-yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern);
    private MyViewModelFactory myViewModelFactory;
    private View dialogView;

    public TodoFragment(SharedViewModel sharedViewModel) {
        // the SharedViewModel is passed from the TabViewPageAdapter to make sure it's the same instance
        this.sharedViewModel = sharedViewModel;
    }

    public static TodoFragment newInstance(SharedViewModel sharedViewModel) {
        TodoFragment fragment = new TodoFragment(sharedViewModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myViewModelFactory = new MyViewModelFactory(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.todo_recyclerview);
        dialogView = inflater.inflate(R.layout.dialog_new_project, container, false);


        if (sharedViewModel.getCurrentProj().getValue() != null) {
            currentProjID = sharedViewModel.getCurrentProj().getValue().getProjectID();
        }


        myViewModelFactory.setProjectID(currentProjID);
        taskViewModel = new ViewModelProvider(this, myViewModelFactory).get(TaskViewModel.class);
        taskViewModel.initializeLists(currentProjID);

        todoTaskList.setValue(taskViewModel.getToDoTasks().getValue());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        todoRecyclerAdapter = new TodoRecyclerAdapter(todoTaskList.getValue());

        // task operations button setting
        todoRecyclerAdapter.setOnMoreActionBtnClickListener((position, moreActionsPopupMenu) -> {
            moreActionsPopupMenu.setOnMenuItemClickListener(actionItem -> {
                Date date = new Date();
                switch (actionItem.getItemId()) {
                    case R.id.state_changeto_doing: {
                        // change the task state to doing
                        List<Task> temp = todoTaskList.getValue();
                        Task task = temp.remove(position);
                        task.setStatus(1);
                        task.setActualStartDate(date.getTime());
                        taskViewModel.updateTask(task);
                        todoTaskList.setValue(temp);
                        return true;
                    }

//                    case R.id.state_changeto_done: {
//                        // change the task state to done
//                        List<Task> temp = todoTaskList.getValue();
//                        Task task = temp.remove(position);
//                        task.setStatus(2);
//                        task.setActualDueDate(date.getTime());
//                        taskViewModel.updateTask(task);
//                        todoTaskList.setValue(temp);
//                        return true;
//                    }

                    case R.id.see_details: {
                        // TODO: the task details
                        return true;
                    }

                    case R.id.delete_item: {
                        // delete the tasks
                        List<Task> temp = todoTaskList.getValue();
                        Task task = temp.remove(position);
                        taskViewModel.deleteTask(task);
                        todoTaskList.setValue(temp);
                        return true;
                    }
                    default:
                        return false;
                }

            });
            moreActionsPopupMenu.show();
        });

        recyclerView.setAdapter(todoRecyclerAdapter);

        // when insert task, delete task or change the task state, refresh the RecyclerView
        todoTaskList.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                todoRecyclerAdapter.refreshTodoTaskList(tasks);
                todoRecyclerAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //-----------------------Building and setting AlertDialog------start----------------------------------------------------//
        AlertDialog alertDialog;
        AlertDialog.Builder newTaskDialog = new AlertDialog.Builder(getContext());
        newTaskDialog.setTitle("New Task");
        newTaskDialog.setCancelable(true);
        newTaskDialog.setView(dialogView);
        alertDialog = newTaskDialog.create();

        Button btnDatePicker = dialogView.findViewById(R.id.date_duration_picker);
        TextView taskStartDate = dialogView.findViewById(R.id.add_start_date);
        TextView taskDueDate = dialogView.findViewById(R.id.add_due_date);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                // Customize the date picker if needed (optional)
                builder.setTitleText("Task Start and Due Date");
                // Create the date picker
                final MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
                // Set a listener to get the selected date
                datePicker.addOnPositiveButtonClickListener(selection -> {
                    // 'selection' is the selected timestamp in milliseconds
                    startDateLong = selection.first;
                    dueDateLong = selection.second;
                    selectedStartDate = simpleDateFormat.format(new Date(startDateLong));
                    selectedDueDate = simpleDateFormat.format(new Date(dueDateLong));
                });
                datePicker.show(requireActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");

                datePicker.addOnDismissListener(dialog -> {
                    taskStartDate.setText(selectedStartDate);
                    taskDueDate.setText(selectedDueDate);
                });
            }
        });

        newTaskDialog.setPositiveButton("Add", (dialog, which) -> {
            TextInputLayout editName = dialogView.findViewById(R.id.add_name);
            editName.setHint("Task Name");
            String taskName = editName.getEditText().getText().toString();

            TextInputLayout editDetails = dialogView.findViewById(R.id.add_details);
            editDetails.setHint("Task Detail");
            String taskDetails = editDetails.getEditText().getText().toString();

            String taskStartDate1 = taskStartDate.getText().toString();
            String taskDueDate1 = taskDueDate.getText().toString();

            if (!taskName.trim().isEmpty() && !taskStartDate1.trim().isEmpty() && !taskDueDate1.trim().isEmpty()) {

                if (taskViewModel == null || currentProjID == null) {
                    currentProjID = sharedViewModel.getCurrentProj().getValue().getProjectID();

                    myViewModelFactory.setProjectID(currentProjID);
                    taskViewModel = new ViewModelProvider(this, myViewModelFactory).get(TaskViewModel.class);
                }
//                Log.d("current ID :%%%%%%%%%%%%%%%%", currentProjID.toString());
                Task newTask = new Task(currentProjID, taskName, taskDetails, startDateLong, dueDateLong, 0);
                taskViewModel.insertTask(newTask);

                // clear all texts
                editName.getEditText().setText("");
                editDetails.getEditText().setText("");
                taskStartDate.setText("");
                taskDueDate.setText("");

                todoTaskList.setValue(taskViewModel.getToDoTasks().getValue());

                alertDialog.dismiss();
            } else {
                if (taskName.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Using a task name is good to remember the task~", Toast.LENGTH_LONG);
                }
                if (taskStartDate1.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Staring date is important for planning~", Toast.LENGTH_LONG);
                }
                if (taskDueDate1.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Don't forget the deadline~", Toast.LENGTH_LONG);
                }
            }

        });
        newTaskDialog.setNegativeButton("Cancel", (dialog, which) -> alertDialog.dismiss());
//-----------------------Building and setting AlertDialog--------------end--------------------------------------------//

        View addTaskBtn = view.findViewById(R.id.add_list_item);
        addTaskBtn.setOnClickListener(v -> {

            if (dialogView.getParent() != null)
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);
            newTaskDialog.show();
        });
    }
}