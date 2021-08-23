package com.bliss.csc.mygrade;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Calculator extends AppCompatActivity {
    CalculateHelper calculateHelper;

    boolean isDot; // . 이있나 없나
    boolean isBracket; // 괄호가 있나 없나
    boolean isPreview; // preview 실행 유무

    TextView textView;
    TextView textView2;

    int size;

    String result;

    Button num0;
    Button num1;
    Button num2;
    Button num3;
    Button num4;
    Button num5;
    Button num6;
    Button num7;
    Button num8;
    Button num9;

    Button add; //더하기
    Button sub; //빼기
    Button mul; //곱하기
    Button div; //나누기
    Button clear; //전체 삭제
    Button bracket; // 괄호
    Button percent; // 퍼센트
    Button back; // 삭제
    Button dot; //점

    Button equal; // 결과

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        calculateHelper = new CalculateHelper();

        size = 0;
        int number = 25;
        int t = String.valueOf(Math.sqrt(number)).length();  // Math.sqrt = 제곱근 구하는 함수 , 25의 제곱근 = 5
        Log.d("test", "" + t + " ? " + String.valueOf(Math.sqrt(number)));

        isPreview = false;
        isBracket = false;
        isDot = false;

        int[][] test = new int[5][4];
        setButton();
        setTextView();
    }

    private void setButton() {
        num0 = findViewById(R.id.num0);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);
        num7 = findViewById(R.id.num7);
        num8 = findViewById(R.id.num8);
        num9 = findViewById(R.id.num9);

        add = findViewById(R.id.add);
        sub = findViewById(R.id.sub);
        mul = findViewById(R.id.mul);
        div = findViewById(R.id.div);
        clear = findViewById(R.id.clear);
        bracket = findViewById(R.id.bracket);
        percent = findViewById(R.id.percent);
        back = findViewById(R.id.back);
        dot = findViewById(R.id.dot);

        equal = findViewById(R.id.equal);

        num0.setOnClickListener(numClickListener);
        num1.setOnClickListener(numClickListener);
        num2.setOnClickListener(numClickListener);
        num3.setOnClickListener(numClickListener);
        num4.setOnClickListener(numClickListener);
        num5.setOnClickListener(numClickListener);
        num6.setOnClickListener(numClickListener);
        num7.setOnClickListener(numClickListener);
        num8.setOnClickListener(numClickListener);
        num9.setOnClickListener(numClickListener);

        add.setOnClickListener(markClickListener);
        sub.setOnClickListener(markClickListener);
        mul.setOnClickListener(markClickListener);
        div.setOnClickListener(markClickListener);
        clear.setOnClickListener(markClickListener);
        bracket.setOnClickListener(markClickListener);
        percent.setOnClickListener(markClickListener);
        back.setOnClickListener(markClickListener);
        dot.setOnClickListener(markClickListener);

        equal.setOnClickListener(markClickListener);
    }

    //숫자 버튼이 눌렸을 경우
    private Button.OnClickListener numClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.num0:
                    textView.append("0");
                    break;
                case R.id.num1:
                    textView.append("1");
                    break;
                case R.id.num2:
                    textView.append("2");
                    break;
                case R.id.num3:
                    textView.append("3");
                    break;
                case R.id.num4:
                    textView.append("4");
                    break;
                case R.id.num5:
                    textView.append("5");
                    break;
                case R.id.num6:
                    textView.append("6");
                    break;
                case R.id.num7:
                    textView.append("7");
                    break;
                case R.id.num8:
                    textView.append("8");
                    break;
                case R.id.num9:
                    textView.append("9");
                    break;
            }

            preview();
        }
    };

    //기호 버튼이 눌렸을 경우
    private Button.OnClickListener markClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add:
                    textView.append(" + ");
                    isPreview = true;
                    break;
                case R.id.sub:
                    textView.append(" - ");
                    isPreview = true;
                    break;
                case R.id.mul:
                    textView.append(" * ");
                    isPreview = true;
                    break;
                case R.id.div:
                    textView.append(" / ");
                    isPreview = true;
                    break;
                case R.id.percent:
                    textView.append(" % ");
                    isPreview = true;
                    break;
                case R.id.clear:
                    textView.setText("");
                    textView2.setText("");

                    calculateHelper = new CalculateHelper();

                    isPreview = false;

                    break;
                case R.id.bracket:
                    if (!isBracket) { // isBracket가 false 일경우 ( 넣기
                        textView.append("( ");
                        isBracket = true;
                    } else { // isBracket가 true 일경우 ) 넣기
                        textView.append(" )");
                        isBracket = false; // 다시 false 로 바꿈
                    }

                    isPreview = true;

                    break;
                case R.id.back:
                    size = textView.getText().length(); // 계산 값의 길이를 받아옴

                    if (size != 0)
                        textView.setText(textView.getText().toString().substring(0, size - 1)); // 원래 길이만큼 가져옴

                    if (size > 1) {
                        if (calculateHelper.checkNumber(textView.getText().toString().substring(size - 2))) // 이상한 문자가 없다면 한자리를 뺌.
                            preview();
                        else {
                            isPreview = false;
                            textView2.setText("");
                        }
                    }

                    break;
                case R.id.dot:
                    textView.append(".");
                    isDot = true;
                    break;
                case R.id.equal:
                    result = textView.getText().toString();
                    double r = calculateHelper.process(result);

                    if (!isDot) // . 이 없다면
                        textView.setText(String.valueOf((int) r)); // 정수형으로 반환해서 글자 넣어줌
                    else
                        textView.setText(String.valueOf(r)); //  . 이 있다면 실수형으로 반환해야 하기에 그대로 반환

                    textView2.setText("");
                    isDot = false;
                    isPreview = false;
                    break;
            }
        }
    };

    private void preview() {
        if (isPreview) { // isPreview가 true 일 경우
            result = textView.getText().toString(); //입력한 값 받아옴
            double r = calculateHelper.process(result); // process에서 result 결과 값 받아옴

            if (!isDot) // . 이 없을 경우
                textView2.setText(String.valueOf((int) r)); // Double 형으로 출력하면 xx.0으로 출력 되므로 (int)를 써서 강제 형변환을 한 상태로 값 입력함.
            else
                textView2.setText(String.valueOf(r)); // .이 있다면 실수 이므로 그대로 출력.
        }
    }
    private void setTextView() {
        textView = findViewById(R.id.first_textView); // 계산할 값
        textView2 = findViewById(R.id.second_textView); // 결과 값
    }
}
