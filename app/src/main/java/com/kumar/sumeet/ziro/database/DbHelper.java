package com.kumar.sumeet.ziro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kumar.sumeet.ziro.model.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, Todo.DB_NAME, null, Todo.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Todo.TaskEntry.DB_TABLE + " ( " +
                Todo.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Todo.TaskEntry.TODO + " TEXT, " +
                Todo.TaskEntry.DATE + " TEXT, " +
                Todo.TaskEntry.STATUS + " INTEGER);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Todo.TaskEntry.DB_TABLE);
        onCreate(db);
    }

    public void addTodo(@NotNull Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Todo.TaskEntry.TODO, task.getTask());
        values.put(Todo.TaskEntry.DATE, task.getDate());
        values.put(Todo.TaskEntry.STATUS, task.isComplete() ? 1 : 0);
        db.insertWithOnConflict(Todo.TaskEntry.DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateTodo(@NotNull Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Todo.TaskEntry.TODO, task.getTask());
        values.put(Todo.TaskEntry.DATE, task.getDate());
        values.put(Todo.TaskEntry.STATUS, task.isComplete() ? 1 : 0);
        db.update(Todo.TaskEntry.DB_TABLE, values, Todo.TaskEntry._ID + " = ?", new String[]{""+task.getId()});
        //db.insertWithOnConflict(Todo.TaskEntry.DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void completeTodo(@NotNull Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Todo.TaskEntry.TODO, task.getTask());
        values.put(Todo.TaskEntry.DATE, task.getDate());
        values.put(Todo.TaskEntry.STATUS, 1);
        db.update(Todo.TaskEntry.DB_TABLE, values, Todo.TaskEntry._ID + " = ?", new String[]{""+task.getId()});
        db.close();
    }

    public void deleteTask(int id) {
        String task = String.valueOf(id);
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Todo.TaskEntry.DB_TABLE, Todo.TaskEntry._ID + " = ?", new String[]{task});
        db.close();
    }
    public void deleteTask(String text) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Todo.TaskEntry.DB_TABLE, Todo.TaskEntry.TODO + " = ?", new String[]{text});
        db.close();
    }

    public ArrayList<Task> getList() {
        ArrayList<Task> todoList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(Todo.TaskEntry.DB_TABLE, null,null, null, null, null, null);
        while (cursor.moveToNext()) {
            todoList.add(new Task(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3) == 1));
        }

        cursor.close();
        db.close();
        return todoList;
    }
}
