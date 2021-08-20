package com.bliss.csc.mygrade;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {

    private static final String TABLE_COLUMN_AVERAGE = "average";
    private static final String TABLE_NAME = "MyGrade";
    public static final String DB_NAME = "dbtest.db";
    public static final int DB_VERSION = 1;
    private SQLiteDatabase mDB;
    private int count_ID = 1;

    public static final String COL_1 = "_id";
    public static final String COL_2 = "grade";
    public static final String COL_3 = "semester";
    public static final String COL_4 = "exam";
    public static final String COL_5 = "average";

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE MyGrade( _id INTEGER ," + "grade INTEGER, semester INTEGER, exam TEXT, average INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MyGrade");
        onCreate(db);
    }

    @Override public void onOpen(SQLiteDatabase db) { super.onOpen(db); mDB = db; }

    public void insert(DBManager mDBManager, Integer Id ,Integer grade, Integer semester, String exam, Integer average)
    {
        mDBManager.getWritableDatabase();
        mDB.execSQL("INSERT INTO MyGrade VALUES (" + Id + ", "+ grade +", " +semester+", '"+ exam +"', "+ average +" );");
        mDBManager.close();
        String insert = Id+ "," + grade + " ,"+ semester+" ," + exam + ", " + average;
        Log.i("Insert DB : ", insert);
    }

    public void delete(DBManager mDBManager)
    {
        mDBManager.getWritableDatabase();
        mDB.execSQL("DELETE FROM MyGrade");
        mDBManager.close();
        Log.i("Delete DB", "모든 테이블이 삭제 되었습니다.");
    }

    public void update(DBManager mDBManager, Integer id, Integer grade, Integer semester, String exam, Integer average)
    {
        mDBManager.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, id);
        contentValues.put(COL_2, grade);
        contentValues.put(COL_3, semester);
        contentValues.put(COL_4, exam);
        contentValues.put(COL_5, average);
        mDB.update(TABLE_NAME,contentValues,"_id = ?", new String[]{String.valueOf(id)});

        // mDB.execSQL("UPDATE Android SET price = "+ (count_ID+10) +" WHERE price = "+ (count_ID-1) +";");
        // mDB.execSQL("INSERT OR REPLACE INTO MyGrade(_id, grade, semester, exam, average) VALUES (" + id+ ", "+ grade +", " +semester+", '"+ exam +"', "+ average +" );");
        mDBManager.close();
        Log.v(null,"Update DB : "+id);
    }

    public void all_average(DBManager mDBManager) {
        String query = "SELECT AVG("+ TABLE_COLUMN_AVERAGE +") FROM "+ TABLE_NAME;
        mDB.rawQuery(query, null);
    }

}
