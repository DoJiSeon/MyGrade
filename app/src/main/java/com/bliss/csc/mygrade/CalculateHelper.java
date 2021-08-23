package com.bliss.csc.mygrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CalculateHelper {
    public static double num1;
    public static double num2;
    public static double resultNumber;

    // push - 삽입 , pop - 삭제 , peek - 읽기

    private ArrayList splitTokens(String equation) {
        String[] constant = equation.split(" "); //공백을 기준

        ArrayList constantList = new ArrayList();
        double number = 0;

        boolean flag = false;
        for (String data : constant) {
            if (data.equals(" ")) {
                continue;
            }
            if (checkNumber(data)) { // checkNumber 메소드에서 숫자일 경우 true 반환, true일 경우 실행
                number = number * 10 + Double.parseDouble(data);
                flag = true;
            } else { // data가 숫자가 아닐경우
                if (flag) { // 숫자가 들어있다면 피연산자를 넣어줌
                    constantList.add(number); // constantList에 넣어준다.
                    number = 0; // number 초기화
                }
                flag = false;
                constantList.add(data);
            }
        }

        if (flag) { // flag가 true이면
            constantList.add(number); //number를 추가한다.
        }

        return constantList;
    }

    //후위 표기식으로 변형
    private ArrayList infixToPostfix(ArrayList constant) {
        ArrayList result = new ArrayList();
        HashMap level = new HashMap();
        Stack stack = new Stack();



//각 기호의 우선순위 레벨. 곱하기, 나누기 > 더하기, 빼기 > 기타
        level.put("*", 3); // (키값, value) , 우선순위 설정
        level.put("/", 3); // (키값, value) , 우선순위 설정
        level.put("+", 2); // (키값, value) , 우선순위 설정
        level.put("-", 2); // (키값, value) , 우선순위 설정
        level.put("(", 1); // (키값, value) , 우선순위 설정

        for (Object object : constant) {
            if (object.equals("(")) { // 배열의 값이 ( 일경우
                stack.push(object); // object 삽입
            } else if (object.equals(")")) { // 배열의 값이 ) 일경우
                while (!stack.peek().equals("(")) { // 가장 마지막에 들어온 스택의 값이 ( 가 아닐경우
                    Object val = stack.pop(); // 마지막거 빼기
                    if (!val.equals("(")) { // 뺀 값이 (가 아닐경우
                        result.add(val); // result arraylist에 추가
                    }
                }
                stack.pop(); // 스택에서 ( 빼기
            } else if (level.containsKey(object)) { // level에 *, /, +, -, ( 중 하나가 있다면
                if (stack.isEmpty()) { // 스택이 비어있다면
                    stack.push(object); // 오브젝트를 스택에 넣기
                } else { // 스택이 비어있지 않다면
                    if (Double.parseDouble(level.get(stack.peek()).toString()) >= Double.parseDouble(level.get(object).toString())) { // 우선순위 비교
                        result.add(stack.pop()); // result arraylist에 스택에서 빼온 값 넣기
                        stack.push(object); // 스택에 object 넣기
                    } else {
                        stack.push(object); // 스택에 object 넣기
                    }
                }
            } else {
                result.add(object); // result에 object 넣기
            }
        }

        while (!stack.isEmpty()) { // 스택이 비어있지 않다면
            result.add(stack.pop()); // result에 스택에서 빼온 값 넣기
        }

        return result;
    }

    //후위 표기식을 계산
    private Double postFixEval(ArrayList expr) {
        Stack numberStack = new Stack();
        for (Object o : expr) {
            if (o instanceof Double) { // o가 Double의 자식이라면 즉, 피연산자라면
                numberStack.push(o);
            } else if (o.equals("+")) { // o가 +이면
                num1 = (Double) numberStack.pop(); // numberstack에서 값 빼오기
                num2 = (Double) numberStack.pop();// numberstack에서 값 빼오기
                numberStack.push(num2 + num1); // 2개를 더해서 넣기
            } else if (o.equals("-")) { // o가 -이면
                num1 = (Double) numberStack.pop();// numberstack에서 값 빼오기
                num2 = (Double) numberStack.pop();// numberstack에서 값 빼오기
                numberStack.push(num2 - num1);// 2개를 빼서서 넣기
            } else if (o.equals("*")) { // o가 *이면
                num1 = (Double) numberStack.pop();// numberstack에서 값 빼오기
                num2 = (Double) numberStack.pop();// numberstack에서 값 빼오기
                numberStack.push(num2 * num1);// 2개를 곱해서 넣기
            } else if (o.equals("/")) {// o가 /이면
                num1 = (Double) numberStack.pop();// numberstack에서 값 빼오기
                num2 = (Double) numberStack.pop();// numberstack에서 값 빼오기
                numberStack.push(num2 / num1);// 2개를 나눠서 넣기
            }
        }

        resultNumber = (Double) numberStack.pop(); // 결과값은 결국 하나. 마지막으로 남은 결과 값 빼와서 넣어줌.

        return resultNumber;
    }

    public Double process(String equation) {
        ArrayList postfix = infixToPostfix(splitTokens(equation)); // 계산 값을 배열과 리스트로 나눠서 넣음.
        Double result = postFixEval(postfix); // 배열을 계산해 result 에 넣어줌.
        return result; //result 값을 반환
    }

    public boolean checkNumber(String str) {
        char check;

        if (str.equals("")) // 공백, 더이상 글자가 없다면 리턴(종료)
            return false;

        for (int i = 0; i < str.length(); i++) {
            check = str.charAt(i); // str의 첫번째 숫자부터 끝 자리까지
            if (check < 48 || check > 58) { // 아스키 코드표를 보면 이해 가능, 숫자가 아닐경우
                if (check != '.') // check가 . 도 아닐경우 false 반환
                    return false;
            }
        }
        return true; // 숫자 혹은 . 일경우 true
    }
}
