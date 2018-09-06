package com.ghsoft.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by mac on 2018/8/5.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_twopane;
    }
}
