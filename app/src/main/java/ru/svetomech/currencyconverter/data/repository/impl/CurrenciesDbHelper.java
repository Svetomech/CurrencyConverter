package ru.svetomech.currencyconverter.data.repository.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CurrenciesDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Currencies.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CurrenciesContract.CurrenciesEntry.TABLE_NAME + " (" +
                    CurrenciesContract.CurrenciesEntry._ID + " INTEGER PRIMARY KEY," +
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NUM_CODE + " INTEGER," +
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_CHAR_CODE + " TEXT," +
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NOMINAL + " INTEGER," +
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NAME + " TEXT," +
                    CurrenciesContract.CurrenciesEntry.COLUMN_NAME_VALUE + " REAL," +
                    "UNIQUE(" + CurrenciesContract.CurrenciesEntry.COLUMN_NAME_NUM_CODE + ") ON CONFLICT REPLACE)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CurrenciesContract.CurrenciesEntry.TABLE_NAME;

    public CurrenciesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
