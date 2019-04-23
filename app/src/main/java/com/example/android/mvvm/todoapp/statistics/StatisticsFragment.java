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

import com.example.android.architecture.blueprints.todoapp.R;
import com.example.android.architecture.blueprints.todoapp.databinding.StatisticsFragBinding;
import com.example.android.mvvm.base.BaseFragment;

/**
 * Main UI for the statistics screen.
 */
public class StatisticsFragment extends BaseFragment<StatisticsFragBinding, StatisticsViewModel> {

    @Override
    protected int getLayoutResId() {
        return R.layout.statistics_frag;
    }

    @Override
    protected void initView() {
        mViewModel = obtainViewModel(getActivity(), StatisticsViewModel.class);

        mViewDatabinding.setStats(mViewModel);
        mViewDatabinding.setLifecycleOwner(getActivity());
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    public boolean isActive() {
        return isAdded();
    }
}
