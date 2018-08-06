package com.ghsoft.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by mac on 2018/8/5.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab; // 以s为前缀的此变量说明该变量为静态变量
    private List<Crime> mCrimes;
    public static CrimeLab get(Context ctx){
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(ctx);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context ctx){
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }
    }
    public List<Crime> getCrimes(){
        return mCrimes;
    }

    /**
     * 获取Crime对象
     * @param id 指定ID
     * @return Crime对象
     */
    public Crime getCrime(UUID id){
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}
