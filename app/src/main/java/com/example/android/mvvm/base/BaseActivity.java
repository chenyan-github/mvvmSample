package com.example.android.mvvm.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

/**
 * @author chenyan@huobi.com
 * @date 2019/4/23 下午2:35
 * @desp
 */
public abstract class BaseActivity<VB extends ViewDataBinding, VM extends ViewModel> extends AppCompatActivity {

    protected VB mViewDatabinding;

    protected VM mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLayoutResId() > 0) {
            //修复android 7.0以上部分厂商机型沉浸式状态栏不透明问题
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                    Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                    field.setAccessible(true);
                    int statusbg = field.getInt(getWindow().getDecorView());
                    if (Color.TRANSPARENT != statusbg) {//三星机型透明状态栏默认有个遮罩色
                        //LogTool.d(field.getName() + " color: " + statusbg);
                        field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
                    }
                } catch (Exception e) {
                }
            }

            mViewDatabinding = DataBindingUtil.setContentView(this, getLayoutResId());
        } else {
            throw new IllegalArgumentException("You must return a right layout resource Id");
        }

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

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);
}
