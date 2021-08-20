package com.bliss.csc.mygrade;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Data extends Activity {

    private DBManager mDBManager; // private SQLiteDatabase mDB;
    private SQLiteDatabase mDB;

    Button btn_insert,btn_delete,btn_update,btn_select;
    EditText grade, semester, average, id;
    ListView listView, listView2, listView3, listView4, listView_id;
    Cursor cursor;
    ImageButton btn_help;
    ArrayAdapter adapter, adapter2, adapter3, adapter4, adapter_id;
    RadioGroup examgroup;

    SharedPreferences pref; //프리퍼런스
    SharedPreferences.Editor editor; //에디터

    int countInt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        grade = (EditText)findViewById(R.id.grade);
        semester = (EditText)findViewById(R.id.semester);
        average = (EditText)findViewById(R.id.average);
        id = (EditText)findViewById(R.id.id_edt);

        btn_insert = (Button)findViewById(R.id.btn_insert);
        btn_delete = (Button)findViewById(R.id.btn_delete);
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_select = (Button)findViewById(R.id.btn_select);
        btn_help = (ImageButton) findViewById(R.id.btn_help);

        listView = (ListView)findViewById(R.id.listview);
        listView2 = (ListView)findViewById(R.id.listview2);
        listView3 = (ListView)findViewById(R.id.listview3);
        listView4 = (ListView)findViewById(R.id.listview4);
        listView_id = (ListView)findViewById(R.id.listview_id);


        examgroup = (RadioGroup)findViewById(R.id.examRgroup);

        mDBManager = new DBManager(this);
        mDBManager.getReadableDatabase();
        mDBManager.getWritableDatabase();
        mDBManager.close();

        pref = getSharedPreferences("pref",Activity.MODE_PRIVATE);
        editor = pref.edit();

        countInt = pref.getInt("myCount",1);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    try {
                        try{
                            Integer Grade = Integer.valueOf(grade.getText().toString());
                            Integer Semester = Integer.valueOf(semester.getText().toString());
                            Integer Average = Integer.valueOf(average.getText().toString());

                            String exam;
                            int id = examgroup.getCheckedRadioButtonId();
                            RadioButton rb = (RadioButton)findViewById(id);
                            exam = rb.getText().toString();

                            mDBManager.insert(mDBManager, countInt , Grade, Semester, exam, Average);

                            grade.setText("");
                            semester.setText("");
                            average.setText("");

                            countInt = countInt +1;
                            editor.putInt("myCount", countInt);
                            editor.apply();
                        }catch (NullPointerException e){
                            Toast.makeText(getApplicationContext(),"모든 칸과 버튼을 채워주세요",Toast.LENGTH_SHORT).show();
                        }
                    }catch (SQLiteConstraintException e){
                        Toast.makeText(getApplicationContext(),"시험 종류를 선택해주세요",Toast.LENGTH_SHORT).show();
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"모든 칸과 버튼을 채워주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBManager.delete(mDBManager);
                Toast.makeText(getApplicationContext(),"모든 데이터가 삭제되었습니다.",Toast.LENGTH_SHORT).show();

                countInt = 1;
                editor.putInt("myCount", countInt);
                editor.apply();
            }

        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Integer ID = Integer.valueOf(id.getText().toString());
                    Integer Grade = Integer.valueOf(grade.getText().toString());
                    Integer Semester = Integer.valueOf(semester.getText().toString());
                    Integer Average = Integer.valueOf(average.getText().toString());
                    String exam;
                    int id = examgroup.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton)findViewById(id);
                    exam = rb.getText().toString();
                    mDBManager.update(mDBManager, ID, Grade, Semester, exam , Average);


                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(), "수정하고 싶다면 모든 값을 채워주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mDBManager.select(mDBManager);
                listUpdate();
            }
        });

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelping_window();
            }
        });

    }

    private void showHelping_window() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Data.this)
                .setTitle("나의 성적 입력 가이드")
                .setMessage("1. 정보 입력 시 번호란을 제외한 나머지 칸(버튼)을 모두 입력/클릭합니다." +
                        " \n" +"2. 삭제 버튼 클릭 혹은 앱 삭제 시 모든 데이터가 삭제 됩니다.\n" +
                        "3. 정보 수정 시 수정할 리스트의 번호와 수정할 데이터를 입력하되 바뀌지 않은 값은 칸을 비우지 않고 그대로 채워 넣어준 후 업데이트 버튼을 누릅니다. ")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();

    }

    public Cursor avg_average(){
        //String query = "SELECT AVG("+ TABLE_COLUMN_AVERAGE +") FROM "+ TABLE_NAME;
        mDB = mDBManager.getReadableDatabase();
        Cursor cursor = mDB.rawQuery("SELECT AVG( average ) FROM MyGrade;", null);
        startManagingCursor(cursor);
        cursor.close();
        return cursor;

    }

    public void listUpdate(){
        mDB = mDBManager.getReadableDatabase();
        cursor = mDB.rawQuery("SELECT * FROM MyGrade",null);
        startManagingCursor(cursor);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        adapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        adapter_id = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        while (cursor.moveToNext()){
            adapter_id.add(cursor.getInt(0));
            adapter.add(cursor.getInt(1));
            adapter2.add(cursor.getInt(2));
            adapter3.add(cursor.getString(3));
            adapter4.add(cursor.getInt(4));
        }
        listView_id.setAdapter(adapter_id);
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);
        listView4.setAdapter(adapter4);
        cursor.close();
    }


}
