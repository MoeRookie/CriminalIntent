package com.ghsoft.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mac on 2018/8/2.
 */

public class CrimeFragment extends Fragment {
    private Crime mCrime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrime = new Crime();
    }

    /**
     *
     * @param inflater
     * @param container 当前视图的父视图,通常需要父视图来正确配置组件?
     * @param savedInstanceState 用来存储以及恢复数据,可供该方法从保存状态下重建视图
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        // attachToRoot:告知inflater是否将生成的视图添加给父视图
        // false - 我将以activity代码的方式添加视图
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        return view;
    }
}
