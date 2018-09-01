package com.ghsoft.criminalintent.database;

/**
 * Created by mac on 2018/8/29.
 */

public class CrimeDbSchema {
    // crime记录表
    // 定义描述表元素的String常量
    public static final class CrimeTable{
        // 表名
        public static final String NAME = "crimes";
        // 字段名/列名
        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
            public static final String SUSPECT = "suspect";
            public static final String PHONE_NUMBER = "phone_number";
        }
    }
}
