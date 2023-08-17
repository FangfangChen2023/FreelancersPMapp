package com.chen.freelancerspmapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.helper.TabViewPageAdapter;
import com.chen.freelancerspmapp.viewmodel.MyViewModelFactory;
import com.chen.freelancerspmapp.viewmodel.ProjectViewModel;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    View emptyPage;
    View projNotEmpty;
    TabViewPageAdapter tabViewPageAdapter;
    Button btnChangeProj;
    Button btnNewProject;
    Button newProjBtnInEmptyPage;
    Toolbar toolBar;
    private MutableLiveData<List<Project>> projectsMenu;
    private Project currentProj;
    private SharedViewModel sharedViewModel;
    private ProjectViewModel projectViewModel;
    private MyViewModelFactory myViewModelFactory;
//    private SharedPreferences sharedPreferences;
    private String selectedStartDate;
    private Long startDateLong;
    private String selectedDueDate;
    private Long dueDateLong;
    private String DatePattern = "MM-dd-yyyy";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DatePattern);
    public HomeFragment() {
//        this.sharedPreferences = sharedPreferences;
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        projectsMenu = new MutableLiveData<>(new ArrayList<>());
        myViewModelFactory = new MyViewModelFactory(requireActivity().getApplication());
        sharedViewModel = new ViewModelProvider(this, myViewModelFactory).get(SharedViewModel.class);

        projectViewModel = new ViewModelProvider(this, myViewModelFactory).get(ProjectViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        emptyPage = view.findViewById(R.id.empty_page);
        projNotEmpty = view.findViewById(R.id.projNotEmptyLayout);

        btnNewProject = view.findViewById(R.id.new_project);
        tabLayout = view.findViewById(R.id.kanban_tab);
        viewPager2 = view.findViewById(R.id.view_pager2);

        viewPager2.setSaveEnabled(false);

        btnChangeProj = view.findViewById(R.id.button_change_project);
        toolBar = view.findViewById(R.id.home_toolbar);


//-----------------------Building and setting AlertDialog------start----------------------------------------------------//
        AlertDialog alertDialog;
        AlertDialog.Builder newProjectDialog = new AlertDialog.Builder(getContext());
        newProjectDialog.setTitle("New Project");
        newProjectDialog.setCancelable(true);
        View dialogView = inflater.inflate(R.layout.dialog_new_project, container, false);
        newProjectDialog.setView(dialogView);
        alertDialog = newProjectDialog.create();

        Button btnDatePicker = dialogView.findViewById(R.id.date_duration_picker);
        TextView projStartDate = dialogView.findViewById(R.id.add_start_date);
        TextView projDueDate = dialogView.findViewById(R.id.add_due_date);
        btnDatePicker.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Project Start and Due Date");
             MaterialDatePicker<Pair<Long, Long>> dateRangePicker = builder.build();
            dateRangePicker.addOnPositiveButtonClickListener(selection -> {
                startDateLong = selection.first;
                dueDateLong = selection.second;

                selectedStartDate = simpleDateFormat.format(new Date(startDateLong));
                selectedDueDate = simpleDateFormat.format(new Date(dueDateLong));
            });
            dateRangePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_RANGE_PICKER");
            dateRangePicker.addOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    projStartDate.setText(selectedStartDate);
                    projDueDate.setText(selectedDueDate);
                }
            });

        });



        newProjectDialog.setPositiveButton("Add", (dialog, which) -> {
            TextInputLayout editName = dialogView.findViewById(R.id.add_name);
            editName.setHint("Project Name");
            String projName = editName.getEditText().getText().toString();

            TextInputLayout editDetails = dialogView.findViewById(R.id.add_details);
            editDetails.setHint("Project Details");
            String projDetails = editDetails.getEditText().getText().toString();

            String projStartDate1 = projStartDate.getText().toString();
            String projDueDate1 = projDueDate.getText().toString();

            if (!projName.trim().isEmpty() && !projStartDate1.trim().isEmpty() && !projDueDate1.trim().isEmpty()) {
                Project newProj = new Project(projName, projDetails, startDateLong, dueDateLong, 0);
                projectViewModel.insertProject(newProj);

                projectsMenu.setValue(projectViewModel.getCurrentProjects().getValue());

                currentProj = projectsMenu.getValue().get(projectsMenu.getValue().size() - 1);
                sharedViewModel.setCurrentProj(currentProj);
                Log.d("getting current ID from home fragment()()()()()()()()()()()()()()", sharedViewModel.getCurrentProj().getValue().getProjectID().toString());

                toolBar.setTitle(currentProj.getName());

                toolBar.setSubtitle(simpleDateFormat.format(new Date(currentProj.getPlanningStartDate())) + " - " + simpleDateFormat.format(new Date(currentProj.getPlanningDueDate())));

                if (emptyPage.getVisibility() == View.VISIBLE) {
                    emptyPage.setVisibility(View.INVISIBLE);
                    projNotEmpty.setVisibility(View.VISIBLE);
                    btnNewProject.setVisibility(View.VISIBLE);
                }
                // clear all texts
                editName.getEditText().setText("");
                editDetails.getEditText().setText("");
                projStartDate.setText("");
                projDueDate.setText("");

                alertDialog.dismiss();
            } else {
                if (projName.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Using a name is good to remember the project~", Toast.LENGTH_LONG);
                }
                if (projStartDate1.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Staring date is important for planning~", Toast.LENGTH_LONG);
                }
                if (projDueDate1.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Don't forget the deadline~", Toast.LENGTH_LONG);
                }
            }

        });
        newProjectDialog.setNegativeButton("Cancel", (dialog, which) -> alertDialog.dismiss());
//-----------------------Building and setting AlertDialog--------------end--------------------------------------------//


        //--------------------start empty page button setting-------------------------------//
        newProjBtnInEmptyPage = emptyPage.findViewById(R.id.new_proj_emptyPage);

        newProjBtnInEmptyPage.setOnClickListener(v -> {
            if (dialogView.getParent() != null)
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);
            newProjectDialog.show();
        });
        // -------------------------end empty page button setting-------------------------//


        btnNewProject.setOnClickListener(v -> {
            if (dialogView.getParent() != null)
                ((ViewGroup) dialogView.getParent()).removeView(dialogView);
            newProjectDialog.show();
        });

// -------- change current project icon button
        btnChangeProj.setOnClickListener(v -> {
            PopupMenu projectsPopupMenu = new PopupMenu(getContext(), btnChangeProj);
            projectsPopupMenu.getMenuInflater().inflate(R.menu.home_projects_list, projectsPopupMenu.getMenu());
            projectsPopupMenu.getMenu().clear();

            projectsMenu.setValue(projectViewModel.getCurrentProjects().getValue());

            projectsMenu.getValue().forEach((project) -> {
                if (currentProj.getProjectID() != project.getProjectID()) {
                    projectsPopupMenu.getMenu().add(Menu.NONE, Math.toIntExact(project.getProjectID()), Menu.NONE, project.getName());
                }
            });
            projectsPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    projectsMenu.getValue().forEach((project -> {
                        if (project.getProjectID() == item.getItemId()) {
                            currentProj = project;

                            toolBar.setTitle(currentProj.getName());
                            toolBar.setSubtitle(simpleDateFormat.format(new Date(currentProj.getPlanningStartDate())) + " - " + simpleDateFormat.format(new Date(currentProj.getPlanningDueDate())));
                            projectsPopupMenu.getMenu().removeItem(item.getItemId());

                            sharedViewModel.setCurrentProj(currentProj);
                        }
                    }));

                    return true;
                }
            });
            projectsPopupMenu.show();
        });

        tabViewPageAdapter = new TabViewPageAdapter(this, sharedViewModel);
        viewPager2.setAdapter(tabViewPageAdapter);

        sharedViewModel.getCurrentProj().observe(getViewLifecycleOwner(), new Observer<Project>() {
            @Override
            public void onChanged(Project project) {
                tabViewPageAdapter.reloadFragments();
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        projectsMenu.setValue(projectViewModel.getCurrentProjects().getValue());


        // -------------------first time loading views checking
        if (projectsMenu.getValue() == null || projectsMenu.getValue().size() == 0) {
            emptyPage.setVisibility(View.VISIBLE);

        } else {
            if (currentProj == null) {
                currentProj = projectsMenu.getValue().get(projectsMenu.getValue().size() - 1);
            }
            toolBar.setTitle(currentProj.getName());
            toolBar.setSubtitle(simpleDateFormat.format(new Date(currentProj.getPlanningStartDate())) + " - " + simpleDateFormat.format(new Date(currentProj.getPlanningDueDate())));

            projNotEmpty.setVisibility(View.VISIBLE);
            btnNewProject.setVisibility(View.VISIBLE);
        }

        return view;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        viewPager2.setAdapter(null);
//    }
}