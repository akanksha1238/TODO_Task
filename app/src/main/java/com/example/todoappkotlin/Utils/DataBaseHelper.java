package com.example.todoappkotlin.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todoappkotlin.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";
    private static final String COL_4 = "DUE_DATE";
    private static final String COL_5 = "PRIORITY";
    private static final String COL_6 = "CATEGORY";
    private static final String COL_7 = "DESCRIPTION";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase(); // Initialize db here
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+" TASK TEXT, STATUS INTEGER,"+" DUE_DATE TEXT,"+" PRIORITY TEXT,"+" CATEGORY TEXT,"+" DESCRIPTION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertTask(ToDoModel model) {
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTask());
        values.put(COL_3, 0);
        values.put(COL_4, model.getDueDate());
        values.put(COL_5, model.getPriority());
        values.put(COL_6, model.getCategory());
        values.put(COL_7, model.getDescription());
        db.insert(TABLE_NAME, null, values);
    }

    public void updateTask(int id, String task, String dueDate, String priority, String category, String description) {
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        values.put(COL_4, dueDate);
        values.put(COL_5, priority);
        values.put(COL_6, category);
        values.put(COL_7, description);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status) {
        ContentValues values = new ContentValues();
        values.put(COL_3, status);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<ToDoModel> getAllTasks() {
        Cursor cursor = null;
        List<ToDoModel> modelList = new ArrayList<>();
        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndex(COL_1)));
                        task.setTask(cursor.getString(cursor.getColumnIndex(COL_2)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(COL_3)));
                        task.setDueDate(cursor.getString(cursor.getColumnIndex(COL_4)));
                        task.setPriority(cursor.getString(cursor.getColumnIndex(COL_5)));
                        task.setCategory(cursor.getString(cursor.getColumnIndex(COL_6)));
                        task.setDescription(cursor.getString(cursor.getColumnIndex(COL_7)));
                        modelList.add(task);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return modelList;
    }
}
