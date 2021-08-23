package com.test.myapplication;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = { Todo.class }, version = 1, exportSchema = false)
@TypeConverters( {DateLongConverter.class })
public abstract class TodoDatabase extends RoomDatabase{

    private static volatile TodoDatabase INSTANCE;

    public abstract TodoDAO TodoDAO();

    public static synchronized TodoDatabase getAppDatabase(Context context) {

        if (INSTANCE == null) {
        INSTANCE = Room.databaseBuilder
                (
                        context.getApplicationContext(),
                        TodoDatabase.class,
                        "TodoDatabase@V1"
                )
                .fallbackToDestructiveMigration()
                .addCallback(roomCallback)
                .build();
    }
        return INSTANCE;
}

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.i("AppDatabase Callback", "Created");
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.i("AppDatabase Callback", "Opened");
        }
    };

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
