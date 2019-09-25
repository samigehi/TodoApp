package com.kumar.sumeet.ziro.database;

import android.provider.BaseColumns;

public class Todo {
    public static final String DB_NAME = "zero_test.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {

        public static final String DB_TABLE = "tasks";
        public static final String TODO = "todo_text";
        public static final String DATE = "todo__date";
        public static final String STATUS = "complete_status";
    }
}
