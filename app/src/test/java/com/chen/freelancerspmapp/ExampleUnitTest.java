package com.chen.freelancerspmapp;

import org.junit.Test;

import static org.junit.Assert.*;

import android.app.Application;
import android.app.Instrumentation;
import android.content.ContentProvider;
import android.content.Context;

import com.chen.freelancerspmapp.Entity.Project;
import com.chen.freelancerspmapp.Repository.ProjectRepositoryImpl;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

//    @Test
//    public void dbTest(){
//        Project project = new Project();
//        project.setName("project 1");
//        project.setStatus(0);
//        ProjectRepositoryImpl projectRepository = new ProjectRepositoryImpl(ContentProvider);
//    }
}