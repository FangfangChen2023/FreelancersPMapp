package com.chen.freelancerspmapp.HomeFragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.freelancerspmapp.Entity.BusyTime;
import com.chen.freelancerspmapp.R;
import com.chen.freelancerspmapp.helper.TodoRecyclerAdapter;
import com.chen.freelancerspmapp.Entity.Task;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;
import com.chen.freelancerspmapp.viewmodel.TaskViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TodoFragment extends Fragment {

    private MutableLiveData<List<Task>> todoTaskList = new MutableLiveData<>(new ArrayList<>());
    private TaskViewModel taskViewModel;
    private Long currentProjID;

    private SharedViewModel sharedViewModel;
    private TodoRecyclerAdapter todoRecyclerAdapter;
    private String selectedStartDate;
    private Long startDateLong;
    private String selectedDueDate;
    private Long dueDateLong;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private MyViewModelFactory myViewModelFactory;
    private View dialogView;
    private View taskDetailView;
    private DialogInterface.OnClickListener busyAction = null;
    private DialogInterface.OnClickListener notBusyAction = null;

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
                        List<Task> temp = todoTaskList.getValue();
                        Task task = temp.get(position);
                        EditText title = taskDetailView.findViewById(R.id.task_name);
                        title.setText(task.getName());
                        TextView proj = taskDetailView.findViewById(R.id.belongs_to);
                        proj.setText(sharedViewModel.getCurrentProj().getValue().getName());
                        TextView planning = taskDetailView.findViewById(R.id.planning);
                        planning.setText(task.getPlanningDateToString());
                        TextInputEditText detail = taskDetailView.findViewById(R.id.description);
                        detail.setText(task.getDetail());

                        TextView realText = taskDetailView.findViewById(R.id.actual_text);
                        realText.setVisibility(View.GONE);
                        TextView real = taskDetailView.findViewById(R.id.real);
                        real.setVisibility(View.GONE);

                        taskDetailDialog.show();
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
                builder.setTheme(R.style.ThemeOverlay_App_MaterialCalendar);
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
            AtomicBoolean isBusy = new AtomicBoolean(true);
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
                Task newTask = new Task(currentProjID, taskName, taskDetails, startDateLong, dueDateLong, 0);
                List<BusyTime> busyTodo = taskViewModel.getBusyInTodo();
                List<BusyTime> busyDoing = taskViewModel.getBusyInDoing();

                for (int i = 0; i < busyTodo.size(); i++) {
                    isBusy.set(busyTodo.get(i).isOverlappedBy(startDateLong, dueDateLong));
                }
                for (int i = 0; i < busyDoing.size(); i++) {
                    isBusy.set(busyDoing.get(i).isOverlappedBy(startDateLong, dueDateLong));
                }
                if (isBusy.get()) {
                    // TODO dialog
                    //-----------------------AlertDialog for busytime--------------------------------------------------------//
                    AlertDialog.Builder busyDialogBuilder = new AlertDialog.Builder(getContext());
                    busyDialogBuilder.setMessage("You've already had work during this time. Are you sure adding more?")
                            .setTitle("Busy Time Alert!");
                    busyDialogBuilder.setPositiveButton("Yes", (busydialog, which12) -> {
                        Log.d("Yes button-----------------==================", "");
                        // Busy but continue
                        taskViewModel.insertTask(newTask);
                        todoTaskList.setValue(taskViewModel.getToDoTasks().getValue());
                        busydialog.dismiss();
                        // clear all texts
                        editName.getEditText().setText("");
                        editDetails.getEditText().setText("");
                        taskStartDate.setText("");
                        taskDueDate.setText("");
                    });
                    busyDialogBuilder.setNegativeButton("Change date", (busydialog, which1) -> {
                        Log.d("No button-----------------==================", "");
                        busydialog.dismiss();
                        if (dialogView.getParent() != null)
                            ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                        alertDialog.show();
                    });
                    AlertDialog busyDialog = busyDialogBuilder.create();
                    alertDialog.dismiss();
                    busyDialog.show();
                } else {
                    taskViewModel.insertTask(newTask);
                    // clear all texts
                    editName.getEditText().setText("");
                    editDetails.getEditText().setText("");
                    taskStartDate.setText("");
                    taskDueDate.setText("");

                    todoTaskList.setValue(taskViewModel.getToDoTasks().getValue());

                    alertDialog.dismiss();
                }
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

    @Override
    public void onResume() {
        super.onResume();
        List<BusyTime> busyTodo = taskViewModel.getBusyInTodo();
        busyTodo.forEach(busyTime -> {
            String startDate = busyTime.getDurationStartString().substring(0, 5);
            String endDate = busyTime.getDurationDueString().substring(0, 5);
            String toastText = "You will be busy between " + startDate + " and " + endDate;
            Toast.makeText(requireActivity().getApplication(), toastText, Toast.LENGTH_SHORT).show();
        });

    }
}