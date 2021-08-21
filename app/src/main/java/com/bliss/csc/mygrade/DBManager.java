package com.bliss.csc.mygrade;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {

    private static final String TABLE_COLUMN_AVERAGE = "average"; // 평균 필드의 이름 선언
    private static final String TABLE_NAME = "MyGrade"; // 테이블 이름 선언
    public static final String DB_NAME = "dbtest.db"; // db 이름 선언
    public static final int DB_VERSION = 1; //데이터 베이스의 버전
    private SQLiteDatabase mDB; // mDB 선언
    // *표 = 테이블, 행 = 레코드, 열 = 필드 라고 함.*
    public static final String COL_1 = "_id"; // 첫 번째 필드(열, 세로)의 이름
    public static final String COL_2 = "grade";// 두 번째 필드(열, 세로)의 이름
    public static final String COL_3 = "semester";// 세 번째 필드(열, 세로)의 이름
    public static final String COL_4 = "exam";// 네 번째 필드(열, 세로)의 이름
    public static final String COL_5 = "average";// 다섯 번째 필드(열, 세로)의 이름

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블을 만든다. 테이블의 필드 갯수 설정.
        db.execSQL("CREATE TABLE MyGrade( _id INTEGER ," + "grade INTEGER, semester INTEGER, exam TEXT, average INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MyGrade"); // 테이블의 버전 업그레이드 혹은 테이블 수정 시 기존에 있던 테이블이 있다면 삭제
        onCreate(db); // 재생성
    }

    @Override public void onOpen(SQLiteDatabase db) { super.onOpen(db); mDB = db; }

    // 값 입력 메서드
    public void insert(DBManager mDBManager, Integer Id ,Integer grade, Integer semester, String exam, Integer average)
    {
        mDBManager.getWritableDatabase(); // mDBManager의 데이터베이스를 읽고 쓰는 형식으로 열어줌.
        mDB.execSQL("INSERT INTO MyGrade VALUES (" + Id + ", "+ grade +", " +semester+", '"+ exam +"', "+ average +" );"); // 받아온 파라미터 값 넣어줌.
        mDBManager.close(); // 데이터 베이스 사용이 끝났으므로 닫아줌
        String insert = Id+ "," + grade + " ,"+ semester+" ," + exam + ", " + average; // insert변수에 값들을 넣어줌
        Log.i("Insert DB : ", insert); // 값들을 로그로 확인
    }

    // 값 삭제 메서드
    public void delete(DBManager mDBManager)
    {
        mDBManager.getWritableDatabase(); // mDBManager의 데이터베이스를 읽고 쓰는 형식으로 열어줌.
        mDB.execSQL("DELETE FROM MyGrade"); // MyGrade에 있는 모든 테이블을 삭제함.
        mDBManager.close(); // 데이터 베이스 사용이 끝났으므로 닫아줌
        Log.i("Delete DB", "모든 테이블이 삭제 되었습니다.");
    }

    // 값 업데이트 메서드
    public void update(DBManager mDBManager, Integer id, Integer grade, Integer semester, String exam, Integer average)
    {
        mDBManager.getWritableDatabase(); // mDBManager의 DB를 읽고 쓸 수 있는 형식으로 받아옴.
        ContentValues contentValues = new ContentValues(); // contentValues 초기화
        contentValues.put(COL_1, id); // 첫번째 필드의 값 바꿔줌
        contentValues.put(COL_2, grade); // 두번째 필드의 값 바꿔줌
        contentValues.put(COL_3, semester); // 세번째 필드의 값 바꿔줌
        contentValues.put(COL_4, exam); // 네번째 필드의 값 바꿔줌
        contentValues.put(COL_5, average); // 다섯번째 필드의 값 바꿔줌
        mDB.update(TABLE_NAME,contentValues,"_id = ?", new String[]{String.valueOf(id)}); // 받아온 id 값이 있는 레코드의 값들을 바꿈
        mDBManager.close(); // 다 사용했으므로 닫아줌.
        Log.i(null,"Update DB : "+id);
    }

    // 평균 값 계산 메서드 (아직 개발중...)
    public void all_average(DBManager mDBManager) {
        String query = "SELECT AVG("+ TABLE_COLUMN_AVERAGE +") FROM "+ TABLE_NAME;
        mDB.rawQuery(query, null);
    }

}
