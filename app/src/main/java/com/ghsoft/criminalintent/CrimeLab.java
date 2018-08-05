package com.ghsoft.criminalintent;

import android.content.Context;

/**
 * Created by mac on 2018/8/5.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab; // 以s为前缀的此变量说明该变量为静态变量
    public static CrimeLab get(Context ctx){
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(ctx);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context ctx){

    }
}
