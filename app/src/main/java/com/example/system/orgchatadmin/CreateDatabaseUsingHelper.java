package com.example.system.orgchatadmin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CreateDatabaseUsingHelper extends SQLiteOpenHelper {

    public CreateDatabaseUsingHelper(Context context) {
        super(context, "org_chat_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(DatabaseFunctions.createDepartmentTable());
        sqLiteDatabase.execSQL(DatabaseFunctions.createSubDepartmentTable());
        sqLiteDatabase.execSQL(DatabaseFunctions.createUserTable());
        sqLiteDatabase.execSQL(DatabaseFunctions.createMessageTable());
        sqLiteDatabase.execSQL(DatabaseFunctions.createCircularTable());
        sqLiteDatabase.execSQL(DatabaseFunctions.createAttachmentTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
