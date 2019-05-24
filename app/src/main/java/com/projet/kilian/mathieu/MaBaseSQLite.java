package com.projet.kilian.mathieu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MaBaseSQLite extends SQLiteOpenHelper {

    private static final String TABLE_LIVRES = "table_images";
    private static final String COL_ID = "ID";
    private static final String COL_PATH = "PATH";
    private static final String COL_NAME = "NAME";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_LIVRES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_PATH + " TEXT NOT NULL, "
            + COL_NAME + " TEXT NOT NULL);";

    public MaBaseSQLite(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_LIVRES + ";");
        onCreate(db);
    }
}
