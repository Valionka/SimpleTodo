package com.codepath.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * A helper database class which facilitates db transactions
 *
 * Created by vmiha on 9/9/16.
 */

public class TodoItemDatabase extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "todosDatabase";
    private static final int DATABASE_VERSION = 7;

    // Table Name
    private static final String TABLE_TODOS = "todos";

    // DB Table Columns
    private static final String KEY_TODO_ID = "id";
    private static final String KEY_TODO_NAME = "name";
    private static final String KEY_TODO_PRIORITY = "priority";
    private static final String KEY_TODO_DUE_DATE = "duedate";

    // logging
    private static final String TAG = "DB Error";

    // create a singleton db instance
    private static TodoItemDatabase sInstance;

    public static synchronized TodoItemDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TodoItemDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    // singleton constructor
    private TodoItemDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODOS_TABLE = "CREATE TABLE " + TABLE_TODOS +
                "(" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY," + // Define a primary key
                KEY_TODO_NAME + " TEXT NOT NULL DEFAULT ''," +
                KEY_TODO_PRIORITY + " TEXT CHECK( "+ KEY_TODO_PRIORITY + " IN ('LOW', 'MEDIUM', 'HIGH') ) NOT NULL DEFAULT 'LOW'," +
                KEY_TODO_DUE_DATE + " TEXT" +
                ")";

        db.execSQL(CREATE_TODOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
            onCreate(db);
        }
    }

    // Insert a todo into the database
    public void addTODO(Todo todo) {
        // Create and/or open the database for writing
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(KEY_TODO_NAME, todo.name);
            values.put(KEY_TODO_PRIORITY, todo.priority.toString());
            values.put(KEY_TODO_DUE_DATE, todo.date);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_TODOS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add a todo to database");
        } finally {
            db.endTransaction();
        }
    }

    // delete todos
    public void deleteAllTodos() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_TODOS, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all todos");
        } finally {
            db.endTransaction();
        }
    }

    public List<Todo> getAllTodos() {
        List<Todo> todos = new ArrayList<>();

        String TODOS_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_TODOS);

        SQLiteDatabase rdb = getReadableDatabase();
        Cursor cursor = rdb.rawQuery(TODOS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    Todo newTodo = new Todo();
                    newTodo.name = cursor.getString(cursor.getColumnIndex(KEY_TODO_NAME));
                    newTodo.priority = Todo.Priority.valueOf(cursor.getString(cursor.getColumnIndex(KEY_TODO_PRIORITY)));
                    todos.add(newTodo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get todos from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return todos;
    }
}