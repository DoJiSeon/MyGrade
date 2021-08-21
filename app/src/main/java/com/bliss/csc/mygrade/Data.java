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
    ImageButton btn_help; // 경고창
    ArrayAdapter adapter, adapter2, adapter3, adapter4, adapter_id; //리스트에 넣어줄 어댑터
    RadioGroup examgroup; //라디오 그룹

    SharedPreferences pref; //프리퍼런스
    SharedPreferences.Editor editor; //에디터

    int countInt; //데이터베이스에 넘길 번호 값

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        grade = (EditText)findViewById(R.id.grade); //학년 받아올 에디트 텍스트
        semester = (EditText)findViewById(R.id.semester);//학기 받아올 에디트 텍스트
        average = (EditText)findViewById(R.id.average);//평균 받아올 에디트 텍스트
        id = (EditText)findViewById(R.id.id_edt);//번호 받아올 에디트 텍스트

        btn_insert = (Button)findViewById(R.id.btn_insert); // 값 입력 버튼
        btn_delete = (Button)findViewById(R.id.btn_delete); // 값 삭제 버튼
        btn_update = (Button)findViewById(R.id.btn_update); // 값 업데이트 버튼
        btn_select = (Button)findViewById(R.id.btn_select); // 값 조회 버튼
        btn_help = (ImageButton) findViewById(R.id.btn_help); // 사용 안내 버튼

        //리스트 뷰들
        listView = (ListView)findViewById(R.id.listview);
        listView2 = (ListView)findViewById(R.id.listview2);
        listView3 = (ListView)findViewById(R.id.listview3);
        listView4 = (ListView)findViewById(R.id.listview4);
        listView_id = (ListView)findViewById(R.id.listview_id);


        examgroup = (RadioGroup)findViewById(R.id.examRgroup); //라디오 그룹

        mDBManager = new DBManager(this); //DB매니저 인스턴스화
        mDBManager.getReadableDatabase(); // 데이터 베이스를 읽어올수만 있음.
        mDBManager.getWritableDatabase(); // 데이터 베이스를 읽고, 쓸 수 있음.
        mDBManager.close(); // 데이터 베이스 닫기

        pref = getSharedPreferences("pref",Activity.MODE_PRIVATE); // 값 영구적으로 저장하는 데에 쓰임
        editor = pref.edit(); // 선언

        countInt = pref.getInt("myCount",1); // myCount라는 키에서 값을 받아오고, 값이 없다면 1을 넣어줌.

        // 입력 버튼을 눌렀을 때
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    try {
                        try{
                            Integer Grade = Integer.valueOf(grade.getText().toString()); // 학년 에디트 텍스트에 쓰여진 값을 받아서 변수에 넣음.
                            Integer Semester = Integer.valueOf(semester.getText().toString());// 학기 에디트 텍스트에 쓰여진 값을 받아서 변수에 넣음.
                            Integer Average = Integer.valueOf(average.getText().toString());// 평균 에디트 텍스트에 쓰여진 값을 받아서 변수에 넣음.

                            String exam;
                            int id = examgroup.getCheckedRadioButtonId(); // examgroup의 체크된 라디오 버튼의 아이디를 받아옴.
                            RadioButton rb = (RadioButton)findViewById(id); // 받아온 아이디를 rb 라디오 버튼에 넣어줌.
                            exam = rb.getText().toString(); // rb 라디오 버튼의 텍스트를 받아옴. ex)중간고사, 기말고사

                            //mDBManager의 insert 메소드로 파라미터 값들을 보내줌.
                            mDBManager.insert(mDBManager, countInt , Grade, Semester, exam, Average);

                            // 학년 값을 넣어줬으므로 이제 필요없는 값 지워줌.
                            grade.setText("");
                            semester.setText("");
                            average.setText("");

                            countInt = countInt +1; // 값 저장 후 1을 추가, 처음 값을 넣었다면 다음 번호로는 1이 추가되어 2를 값으로 보냄.
                            editor.putInt("myCount", countInt); // 추가한 값 저장.
                            editor.apply(); // apply를 써주지 않는다면 영구적으로 저장되지 않음. 꼭 쓰기
                        }catch (NullPointerException e){ // 버튼안이 비어있을 때 나오는 예외에 대처하는 코드
                            Toast.makeText(getApplicationContext(),"모든 칸과 버튼을 채워주세요",Toast.LENGTH_SHORT).show();
                        }
                    }catch (SQLiteConstraintException e){ // sqlite 예외에 대처하는 코드
                        Toast.makeText(getApplicationContext(),"시험 종류를 선택해주세요",Toast.LENGTH_SHORT).show();
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"모든 칸과 버튼을 채워주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });


        //삭제 버튼을 눌렀을 때
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBManager.delete(mDBManager); // mDBManager에 현재 매니저를 보내줌.
                Toast.makeText(getApplicationContext(),"모든 데이터가 삭제되었습니다.",Toast.LENGTH_SHORT).show(); // 알림창을 띄움.

                countInt = 1; // 번호를 1로 초기화 해준다.
                editor.putInt("myCount", countInt); // 초기화된 값을 저장해준다.
                editor.apply();
            }

        });

        // 업데이트 버튼을 눌렀을 때
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Integer ID = Integer.valueOf(id.getText().toString()); // 번호 에디트 텍스트에 쓰여진 값을 받아서 변수에 넣음.
                    Integer Grade = Integer.valueOf(grade.getText().toString()); // 학년 에디트 텍스트에 쓰여진 값을 받아서 변수에 넣음.
                    Integer Semester = Integer.valueOf(semester.getText().toString()); // 학기 에디트 텍스트에 쓰여진 값을 받아서 변수에 넣음.
                    Integer Average = Integer.valueOf(average.getText().toString()); // 평균 에디트 텍스트에 쓰여진 값을 받아서 변수에 넣음.
                    String exam; //
                    int id = examgroup.getCheckedRadioButtonId(); // examgroup의 체크된 라디오 버튼의 아이디를 받아옴.
                    RadioButton rb = (RadioButton)findViewById(id); // 받아온 아이디를 rb 라디오 버튼에 넣어줌.
                    exam = rb.getText().toString();  // rb 라디오 버튼의 텍스트를 받아옴. ex)중간고사, 기말고사
                    mDBManager.update(mDBManager, ID, Grade, Semester, exam , Average); // 받아 온 값들을 update 메소드로 넘겨줌


                }catch (NumberFormatException e){ // 모든 버튼이 채워지지 않았을 때
                    Toast.makeText(getApplicationContext(), "수정하고 싶다면 모든 값을 채워주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 조회 버튼을 눌렀을 때
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //리스트 업데이트 데이터 실행
                listUpdate();
            }
        });

        // 경고창 이미지 버튼 클릭 시
        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelping_window();
            }
        });

    }

    private void showHelping_window() {
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Data.this)
                .setTitle("나의 성적 입력 가이드")// 경고창 제목
                //경고창 내용 부분
                .setMessage("1. 정보 입력 시 번호란을 제외한 나머지 칸(버튼)을 모두 입력/클릭합니다." +
                        " \n" +"2. 삭제 버튼 클릭 혹은 앱 삭제 시 모든 데이터가 삭제 됩니다.\n" +
                        "3. 정보 수정 시 수정할 리스트의 번호와 수정할 데이터를 입력하되 바뀌지 않은 값은 칸을 비우지 않고 그대로 채워 넣어준 후 업데이트 버튼을 누릅니다. ")
                //확인 버튼
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog msgDlg = msgBuilder.create();
        msgDlg.show();

    }

    // 현재 테스트 중
    public Cursor avg_average(){
        //String query = "SELECT AVG("+ TABLE_COLUMN_AVERAGE +") FROM "+ TABLE_NAME;
        mDB = mDBManager.getReadableDatabase();
        Cursor cursor = mDB.rawQuery("SELECT AVG( average ) FROM MyGrade;", null);
        startManagingCursor(cursor);
        cursor.close();
        return cursor;

    }

    // 데이터 베이스 값들을 가져옴
    public void listUpdate(){
        mDB = mDBManager.getReadableDatabase();
        cursor = mDB.rawQuery("SELECT * FROM MyGrade",null); // MyGrade 테이블에서 모든 값들을 받아옴.
        startManagingCursor(cursor);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1); // 학년 어댑터
        adapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1);// 학기 어댑터
        adapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1);// 시험 어댑터
        adapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1);// 평균 어댑터
        adapter_id = new ArrayAdapter(this, android.R.layout.simple_list_item_1);// 번호 어댑터

        while (cursor.moveToNext()){ // 커서가 현재 레코드의 끝까지 가면 다음 레코드로 이동
            adapter_id.add(cursor.getInt(0)); // 첫번째 필드의 값
            adapter.add(cursor.getInt(1)); // 두번째 필드의 값
            adapter2.add(cursor.getInt(2)); //세번째 필드의 값
            adapter3.add(cursor.getString(3)); // 네번째 필드의 값
            adapter4.add(cursor.getInt(4)); //다섯번째 필드의 값
        }
        listView_id.setAdapter(adapter_id); // 번호 값을 넣을 리스트 뷰에 번호 어댑터 값을 넘겨 줌.
        listView.setAdapter(adapter); // 학년 값을 넣을 리스트 뷰에 학년 어댑터 값을 넘겨 줌.
        listView2.setAdapter(adapter2); // 학기 값을 넣을 리스트 뷰에 학기 어댑터 값을 넘겨 줌.
        listView3.setAdapter(adapter3); // 시험 값을 넣을 리스트 뷰에 시험 어댑터 값을 넘겨 줌.
        listView4.setAdapter(adapter4); // 평균 값을 넣을 리스트 뷰에 평균 어댑터 값을 넘겨 줌.
        cursor.close(); // 커서를 다 이용했으므로 커서를 닫아줌.
    }


}
