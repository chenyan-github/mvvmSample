/*
 * Copyright 2016, The Android Open Source Project
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

package com.example.android.mvvm.todoapp.statistics;

import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.android.architecture.blueprints.todoapp.Injection;
import com.example.android.architecture.blueprints.todoapp.databinding.StatisticsActBinding;
import com.example.android.mvvm.base.BaseActivity;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.NavUtils;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBar;
import android.view.MenuItem;

import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.mvvm.todoapp.util.ActivityUtils;

/**
 * Show statistics for tasks.
 */
public class StatisticsActivity extends BaseActivity<StatisticsActBinding, StatisticsViewModel> {

    @Override
    protected int getLayoutResId() {
        return R.layout.statistics_act;
    }

    @Override
    protected void initView() {
        mViewModel.setmTasksRepository(Injection.provideTasksRepository(getApplicationContext()));

        setupToolbar();

        setupNavigationDrawer();

        findOrCreateViewFragment();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mViewDatabinding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private StatisticsFragment findOrCreateViewFragment() {
        StatisticsFragment statisticsFragment = (StatisticsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.contentFrame);
        if (statisticsFragment == null) {
            statisticsFragment = StatisticsFragment.newInstance();
            ActivityUtils.replaceFragmentInActivity(getSupportFragmentManager(),
                    statisticsFragment, R.id.contentFrame);
        }
        return statisticsFragment;
    }

    private void setupNavigationDrawer() {
        mViewDatabinding.drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        if (mViewDatabinding.navView != null) {
            setupDrawerContent(mViewDatabinding.navView);
        }
    }

    private void setupToolbar() {
        setSupportActionBar(mViewDatabinding.toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.statistics_title);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.list_navigation_menu_item:
                                NavUtils.navigateUpFromSameTask(StatisticsActivity.this);
                                break;
                            case R.id.statistics_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mViewDatabinding.drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
