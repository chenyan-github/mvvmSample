package com.example.android.mvvm.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

/**
 * @author chenyan@huobi.com
 * @date 2019/4/23 下午2:35
 * @desp
 */
public abstract class BaseFragment<VB extends ViewDataBinding, VM extends ViewModel> extends Fragment {

    protected VB mViewDatabinding;

    protected VM mViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getLayoutResId() > 0) {
            mViewDatabinding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false);
        }
        return mViewDatabinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewModel();
        initView();
        initData(savedInstanceState);

    }

    protected void initViewModel() {
        Class<VM> vmClass = null;
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass)
                    .getActualTypeArguments();
            if (actualTypeArguments != null && actualTypeArguments.length > 1) {
                vmClass = (Class<VM>) actualTypeArguments[1];
            }
        }
        if(vmClass != null) {
            mViewModel = ViewModelProviders.of(this).get(vmClass);
        }
    }

    protected <T extends ViewModel> T obtainViewModel(FragmentActivity activity, Class<T> vmClass) {
        return ViewModelProviders.of(activity).get(vmClass);
    }

    protected <T extends ViewModel> T obtainViewModel(Fragment fragment, Class<T> vmClass) {
        return ViewModelProviders.of(fragment).get(vmClass);
    }

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);
}
