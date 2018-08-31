package com.ghsoft.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ghsoft.criminalintent.database.CrimeBaseHelper;
import com.ghsoft.criminalintent.database.CrimeCursorWrapper;
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
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
        return values;
    }
    public List<Crime> getCrimes(){
        ArrayList<Crime> crimes = new ArrayList<>();
        // 查询获取封装了crimeCursor的对象
        CrimeCursorWrapper cursor = queryCrimes(null, null);
        // 遍历cursor获取每一个节点对应的crime并将其添加到list中
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            // 别忘了!轻则导致应用报错,重则导致应用崩溃!
            cursor.close();
        }
        return crimes;
    }

    /**
     * 获取Crime对象
     * @param id 指定ID
     * @return Crime对象
     */
    public Crime getCrime(UUID id){
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        }finally {
            cursor.close();
        }
    }

    /**
     * 添加crime记录到数据库
     * @param crime crime记录
     */
    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME,null,values);
    }

    /**
     * 更新crime记录到数据库
     * @param crime crime记录
     */
    public void updateCrime(Crime crime){
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME,values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }

    public void deleteCrime(UUID crimeId){
        mDatabase.delete(
                CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{crimeId.toString()}
        );
    }

    /**
     * 从数据库中查询crime记录
     * @param whereClause 条件语句
     * @param whereArgs 条件填充参数数组
     * @return 查询结果集
     */
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }
}
