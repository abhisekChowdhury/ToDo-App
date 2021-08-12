package com.birdicomputers.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TaskDBHelper extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public TaskDBHelper(@Nullable Context context) {
        super(context, TaskContract.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTasksTableCmd = "CREATE TABLE tasks" +
                "( " + TaskContract.TaskEntry._ID + " INTEGER Primary key AUTOINCREMENT NOT NULL, " +
                TaskContract.TaskEntry.TASK_TEXT + " TEXT, " +
                TaskContract.TaskEntry.DATE + " TEXT, " +
                TaskContract.TaskEntry.TIME + " TEXT, " +
                TaskContract.TaskEntry.IMAGE_1 + " TEXT, " +
                TaskContract.TaskEntry.NOTIFICATION_ID + " TEXT);";
        String createNotesTableCmd = "CREATE TABLE notes" +
                "( " + TaskContract.TaskEntry._ID + " INTEGER Primary key AUTOINCREMENT NOT NULL, " +
                TaskContract.TaskEntry.TASK_TEXT + " TEXT, " +
                TaskContract.TaskEntry.DATE + " TEXT, " +
                TaskContract.TaskEntry.TIME + " TEXT);";
        db.execSQL(createTasksTableCmd);
        db.execSQL(createNotesTableCmd);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE ADD_NAME ADD COLUMN NEW_COLOUM INTEGER DEFAULT 0");
        }
        onCreate(db);
    }

    public boolean addTask(String task, String date, String time, String attached_image_1, String notification_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("attached_image_1", attached_image_1);
        contentValues.put("notification_id", notification_id);
        db.insert("tasks", null, contentValues);
        return true;
    }

    public boolean addNote(String task, String date, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", task);
        contentValues.put("date", date);
        contentValues.put("time", time);
        db.insert("notes", null, contentValues);
        return true;
    }

    public List<String[]> browseReminders(){
        List<String[]> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor =  db.rawQuery( "select * from tasks", null );
            if (cursor != null){
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    String[] eachTask = new String[6];
                    eachTask[0] = cursor.getString(0);
                    eachTask[1] = cursor.getString(1);
                    eachTask[2] = cursor.getString(2);
                    eachTask[3] = cursor.getString(3);
                    eachTask[4] = cursor.getString(4);
                    eachTask[5] = cursor.getString(5);
                    tasks.add(eachTask);
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            Log.e("Browse Reminders ", ex.getMessage());
        }
        return tasks;
    }

    public String[] searchReminders(String id){
        String[] searchResult = new String[6];
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor =  db.rawQuery( "select * from "+ TaskContract.TaskEntry.TABLE_NAME +"  WHERE "+TaskContract.TaskEntry._ID+" =  \"" + id + "\"", null );
            if (cursor != null){
                cursor.moveToFirst();
                searchResult[0] = cursor.getString(0);
                searchResult[1] = cursor.getString(1);
                searchResult[2] = cursor.getString(2);
                searchResult[3] = cursor.getString(3);
                searchResult[4] = cursor.getString(4);
                searchResult[5] = cursor.getString(5);
                Log.d("database",searchResult[1]+searchResult[2]);
            }
        } catch (Exception ex) {
            Log.e("Browse Reminders ", ex.getMessage());
        }
        return  searchResult;
    }

    public void UpdateTasks(String id, String task, String date, String time, String attached_image_1, String notification_id){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.TASK_TEXT,task);
            values.put(TaskContract.TaskEntry.DATE,date);
            values.put(TaskContract.TaskEntry.TIME,time);
            values.put(TaskContract.TaskEntry.IMAGE_1,attached_image_1);
            values.put(TaskContract.TaskEntry.NOTIFICATION_ID,notification_id);
            db.update(TaskContract.TaskEntry.TABLE_NAME, values,TaskContract.TaskEntry._ID + " = ? ",new String[]{ String.valueOf(id) });
        }
        catch (Exception e){
            Log.d("Update Tasks: ",e.getMessage());
        }
    }

    public void DeleteFromReminders(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String where="_id=?";
            db.delete("tasks", where, new String[]{id});
        }
        catch(Exception e){
            Log.d("DeleteFromReminders", e.getMessage());
        }
    }

    public void DeleteFromNotes(String id){
        SQLiteDatabase dbs = this.getReadableDatabase();
        try{
            String where="_id=?";
            dbs.delete("notes", where, new String[]{id});
        }
        catch(Exception e){
            Log.d("DeleteFromNotes", e.getMessage());
        }

    }

    public List<String[]> browseNotes(){
        List<String[]> tasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            Cursor cursor =  db.rawQuery( "select * from notes", null );
            if (cursor != null){
                cursor.moveToFirst();
                while(!cursor.isAfterLast()){
                    String[] eachTask = new String[4];
                    eachTask[0] = cursor.getString(0);
                    eachTask[1] = cursor.getString(1);
                    tasks.add(eachTask);
                    cursor.moveToNext();
                }
            }
        } catch (Exception ex) {
            Log.e("Browse notes ", ex.getMessage());
        }
        return tasks;
    }
}
