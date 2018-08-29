package com.ghsoft.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ghsoft.criminalintent.database.CrimeBaseHelper;
import com.ghsoft.criminalintent.database.CrimeDbSchema;
import com.ghsoft.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by mac on 2018/8/5.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab; // 以s为前缀的此变量说明该变量为静态变量
    private Context mContext;
    private SQLiteDatabase mDatabase;
    public static CrimeLab get(Context ctx){
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(ctx);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context ctx){
        mContext = ctx.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }
    private ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }
    public List<Crime> getCrimes(){
        return new ArrayList<>();
    }

    /**
     * 获取Crime对象
     * @param id 指定ID
     * @return Crime对象
     */
    public Crime getCrime(UUID id){
        return null;
    }
    public void addCrime(Crime crime){
    }
    public void deleteCrime(UUID crimeId){
    }
}
