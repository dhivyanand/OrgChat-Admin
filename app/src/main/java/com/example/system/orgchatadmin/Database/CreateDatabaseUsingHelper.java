package com.example.system.orgchatadmin.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;


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
        sqLiteDatabase.execSQL(DatabaseFunctions.createDateTable());

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE DEPARTMENT");
        sqLiteDatabase.execSQL("DROP TABLE SUBDEPARTMENT");
        sqLiteDatabase.execSQL("DROP TABLE USER");
        sqLiteDatabase.execSQL("DROP TABLE MESSAGE");
        sqLiteDatabase.execSQL("DROP TABLE CIRCULAR");
        sqLiteDatabase.execSQL("DROP TABLE ATTACHMENT");
        sqLiteDatabase.execSQL("DROP TABLE DATE");

        onCreate(sqLiteDatabase);

    }
}
