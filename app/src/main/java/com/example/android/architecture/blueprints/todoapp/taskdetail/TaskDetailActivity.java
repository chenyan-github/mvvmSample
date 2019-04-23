/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp.taskdetail;

import static com.example.android.architecture.blueprints.todoapp.addedittask.AddEditTaskActivity.ADD_EDIT_RESULT_OK;
import static com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailFragment.REQUEST_EDIT_TASK;

import android.content.Intent;
import android.os.Bundle;

import com.example.android.architecture.blueprints.todoapp.Event;
import com.example.android.architecture.blueprints.todoapp.Injection;
import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditTaskActivity;
import com.example.android.architecture.blueprints.todoapp.addedittask.AddEditTaskFragment;
import com.example.android.architecture.blueprints.todoapp.databinding.TaskdetailActBinding;
import com.example.android.architecture.blueprints.todoapp.util.ActivityUtils;
import com.example.android.mvvm.base.BaseActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;

/**
 * Displays task details screen.
 */
public class TaskDetailActivity extends BaseActivity<TaskdetailActBinding, TaskDetailViewModel> implements TaskDetailNavigator {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    public static final int DELETE_RESULT_OK = RESULT_FIRST_USER + 2;

    public static final int EDIT_RESULT_OK = RESULT_FIRST_USER + 3;

    @Override
    protected int getLayoutResId() {
        return R.layout.taskdetail_act;
    }

    @Override
    protected void initView() {
        mViewModel.setmTasksRepository(Injection.provideTasksRepository(getApplicationContext()));

        setupToolbar();

        TaskDetailFragment taskDetailFragment = findOrCreateViewFragment();

        ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                taskDetailFragment, R.id.contentFrame);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        subscribeToNavigationChanges(mViewModel);
    }

    @NonNull
    private TaskDetailFragment findOrCreateViewFragment() {
        // Get the requested task id
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);

        if (taskDetailFragment == null) {
            taskDetailFragment = TaskDetailFragment.newInstance(taskId);
        }
        return taskDetailFragment;
    }

    private void setupToolbar() {
        setSupportActionBar(mViewDatabinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
    }

    private void subscribeToNavigationChanges(TaskDetailViewModel viewModel) {
        // The activity observes the navigation commands in the ViewModel
        viewModel.getEditTaskCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskEvent) {
                if (taskEvent.getContentIfNotHandled() != null) {
                    TaskDetailActivity.this.onStartEditTask();
                }
            }
        });
        viewModel.getDeleteTaskCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> taskEvent) {
                if (taskEvent.getContentIfNotHandled() != null) {
                    TaskDetailActivity.this.onTaskDeleted();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EDIT_TASK) {
            // If the task was edited successfully, go back to the list.
            if (resultCode == ADD_EDIT_RESULT_OK) {
                // If the result comes from the add/edit screen, it's an edit.
                setResult(EDIT_RESULT_OK);
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTaskDeleted() {
        setResult(DELETE_RESULT_OK);
        // If the task was deleted successfully, go back to the list.
        finish();
    }

    @Override
    public void onStartEditTask() {
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);
        Intent intent = new Intent(this, AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskFragment.ARGUMENT_EDIT_TASK_ID, taskId);
        startActivityForResult(intent, REQUEST_EDIT_TASK);
    }

}
