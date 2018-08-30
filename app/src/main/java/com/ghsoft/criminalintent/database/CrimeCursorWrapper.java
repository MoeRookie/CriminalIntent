package com.ghsoft.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

/**
 * Created by mac on 2018/8/30.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
}
