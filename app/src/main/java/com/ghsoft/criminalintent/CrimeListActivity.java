package com.ghsoft.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by mac on 2018/8/5.
 */

public class CrimeListActivity extends SingleFragmentActivity
implements CrimeListFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    /**
     * 当点击某个crime以及新建crime菜单项时回调
     * @param crime 当前crime
     */
    @Override
    public void onCrimeSelected(Crime crime) {
        // 1.根据当前activity管理的用户界面中是否存在可装载crime细节fragment的容器
        if (findViewById(R.id.detail_fragment_container) == null) {
            // 3.不存在即单版面界面,需启动crimePagerActivity所管理的用户界面以完成对当前crime的创建及细节的查看
            Intent intent = CrimePagerActivity.newIntent(
                    CrimeListActivity.this, crime.getId());
            startActivity(intent);
        }else{
            // 2.存在即管理双版面界面,需将crimeFragment添加到该容器中
            CrimeFragment newDetail = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }
}
