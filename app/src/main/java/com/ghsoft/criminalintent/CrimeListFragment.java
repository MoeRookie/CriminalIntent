package com.ghsoft.criminalintent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mac on 2018/8/5.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private boolean mSubTitleVisible;
    private Callbacks mCallbacks;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private TextView mBtnAdd;
    private RelativeLayout mRlcrimeEmpty;

    /**
     * 声明其托管方activity的回调接口
     */
    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通知fragmentManager需调用创建选项菜单的方法
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mRlcrimeEmpty = view.findViewById(R.id.rl_crime_empty);
        mBtnAdd = view.findViewById(R.id.btn_add);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 新建crime保存到陋习列表中,并在陋习详情中设置新建crime的属性
                Crime crime = new Crime();
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.addCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
            }
        });
        // 1.隐藏列表界面,显示空空如也的界面
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mSubTitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI();
        return view;
    }
    private void setLayoutVisibility(boolean isVisibility){
        mCrimeRecyclerView.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        mRlcrimeEmpty.setVisibility(!isVisibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        boolean isCrimeEmpty = crimes.size() != 0;
        setLayoutVisibility(isCrimeEmpty);
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater); // 约定:任何超类定义的菜单项在子类方法中同样获得应用
        // 解析菜单定义,将菜单项填充到menu对象中
        inflater.inflate(R.menu.fragment_crime_list,menu);
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        subtitleItem.setTitle(mSubTitleVisible ? R.string.hide_subtitle : R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                // 新建crime保存到陋习列表中,并在陋习详情中设置新建crime的属性
                Crime crime = new Crime();
                CrimeLab crimeLab = CrimeLab.get(getActivity());
                crimeLab.addCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true; // 全部任务已完成
            case R.id.menu_item_show_subtitle:
                // 子标题显示与否的状态值取反
                mSubTitleVisible = !mSubTitleVisible;
                // 重建选项菜单
                getActivity().invalidateOptionsMenu();
                // 更新显示工具栏中的子标题
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 更新工具栏中的子标题
     */
    private void updateSubtitle(){
        // 1.获取到crimeLab对象
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        // 2.获取到当前crime的个数
        int crimeCount = crimeLab.getCrimes().size();
        // 3.设置工具栏子标题显示当前crime的个数
        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plurals, crimeCount,crimeCount);
        if (!mSubTitleVisible) {
            subtitle = null;
        }
        // 3.2.显示当前crime的个数子标题
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.list_item_crime_title_text_view);
            mDateTextView = itemView.findViewById(R.id.list_item_crime_date_text_view);
            mSolvedCheckBox = itemView.findViewById(R.id.list_item_crime_solved_check_box);
        }

        @Override
        public void onClick(View v) {
            mCallbacks.onCrimeSelected(mCrime);
        }

        /**
         * 绑定当前作为列表项显示的view到crime对象数据
         * @param crime crime对象数据
         */
        public void bindCrime(Crime crime){
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;
        public CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent,false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return mCrimes != null ? mCrimes.size() : 0;
        }


        public void setCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubTitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}