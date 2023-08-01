package com.chen.freelancerspmapp.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.HomeFragments.CompletedFragment;
import com.chen.freelancerspmapp.HomeFragments.InprogressFragment;
import com.chen.freelancerspmapp.HomeFragments.TodoFragment;
import com.chen.freelancerspmapp.HomeFragments.WorkflowFragment;
import com.chen.freelancerspmapp.viewmodel.SharedViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabViewPageAdapter extends FragmentStateAdapter {
    //    private final Fragment parentFragment;
    private SharedViewModel sharedViewModel;
    private Fragment parentFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private TodoFragment todoFragment;
    private InprogressFragment inprogressFragment;
    private CompletedFragment completedFragment;
    private WorkflowFragment workflowFragment;

    public TabViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public TabViewPageAdapter(@NonNull Fragment parentFragment, SharedViewModel sharedViewModel) {
        super(parentFragment);
        this.sharedViewModel = sharedViewModel;
        this.parentFragment = parentFragment;
    }

    public TabViewPageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        todoFragment = TodoFragment.newInstance(sharedViewModel);
        inprogressFragment = InprogressFragment.newInstance(sharedViewModel);
        completedFragment = CompletedFragment.newInstance(sharedViewModel);
        workflowFragment = WorkflowFragment.newInstance();

        Log.d("createFragment is called", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        switch (position) {
            case 0:
                return todoFragment;
            case 1:
                return inprogressFragment;
            case 2:
                return completedFragment;
            case 3:
                return workflowFragment;
            default:
                return todoFragment;
        }
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
//        Log.d("Item position>>>>>>>>>>>", position+"");
//        if(fragments!=null){
//            Log.d("Fragments is not null", ">>>>>>>>>>>>>>>>>"+fragments.get(position));
//        }
        if (!fragments.isEmpty() && fragments.get(position)!= null) return fragments.get(position).hashCode();
        return super.getItemId(position);
    }

    @Override
    public boolean containsItem(long itemId) {
        Fragment temp = null;

        if(fragments.size()>0){
            while(fragments.remove(null));
             temp = fragments.stream().filter(fragment -> fragment.hashCode() == itemId)
                    .findAny()
                    .orElse(null);
        }

        return temp != null;
    }

    public void reloadFragments() {
//        FragmentManager fm = parentFragment.getActivity().getSupportFragmentManager();
//        FragmentManager childFragmentManager = parentFragment.getChildFragmentManager();
//        List<Fragment> fragmentList = childFragmentManager.getFragments();
//        if (fragmentList != null && fragmentList.size() > 0) {
//            fragmentList.forEach(fragment -> {
//                childFragmentManager.beginTransaction().detach(fragment).commitNow();
//                Log.d("+++++++++++++++++++++++++", fragment + "is detached!!!");
//                childFragmentManager.beginTransaction().attach(fragment).commitNow();
//
//            });
//        }
        Log.d("reloadFragments is called", "========================");
        fragments.clear();
        fragments.add(0, todoFragment);
        fragments.add(1, inprogressFragment);
        fragments.add(2, completedFragment);
        fragments.add(3, workflowFragment);
        notifyDataSetChanged();
    }
}
