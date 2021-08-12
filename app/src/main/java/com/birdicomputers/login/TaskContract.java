package com.birdicomputers.login;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "TasksDB";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String TABLE_NAME_NOTES = "notes";
        public static final String TASK_TEXT = "title";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String IMAGE_1 = "attached_image_1";
        public static final String NOTIFICATION_ID = "notification_id";
    }
}
