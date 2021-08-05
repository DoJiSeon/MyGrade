package com.bliss.csc.mygrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CalculateHelper {
    public static double num1;
    public static double num2;
    public static double resultNumber;

    private ArrayList splitTokens(String equation) {
        String[] constant = equation.split(" "); //공백을 기준

        ArrayList constantList = new ArrayList();
        double number = 0;

        boolean flag = false;
        for (String data : constant) {
            if (data.equals(" ")) {
                continue;
            }
            if (checkNumber(data)) {
                number = number * 10 + Double.parseDouble(data);
                flag = true;
            } else {
                if (flag) {
                    constantList.add(number);
                    number = 0;
                }
                flag = false;
                constantList.add(data);
            }
        }

        if (flag) {
            constantList.add(number);
        }

        return constantList;
    }

    //후위 표기식으로 변형
    private ArrayList infixToPostfix(ArrayList constant) {
        ArrayList result = new ArrayList();
        HashMap level = new HashMap();
        Stack stack = new Stack();



//각 기호의 우선순위 레벨. 곱하기, 나누기 > 더하기, 빼기 > 기타
        level.put("*", 3);
        level.put("/", 3);
        level.put("+", 2);
        level.put("-", 2);
        level.put("(", 1);

        for (Object object : constant) {
            if (object.equals("(")) {
                stack.push(object);
            } else if (object.equals(")")) {
                while (!stack.peek().equals("(")) {
                    Object val = stack.pop();
                    if (!val.equals("(")) {
                        result.add(val);
                    }
                }
                stack.pop();
            } else if (level.containsKey(object)) {
                if (stack.isEmpty()) {
                    stack.push(object);
                } else {
                    if (Double.parseDouble(level.get(stack.peek()).toString()) >= Double.parseDouble(level.get(object).toString())) {
                        result.add(stack.pop());
                        stack.push(object);
                    } else {
                        stack.push(object);
                    }
                }
            } else {
                result.add(object);
            }
        }

        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    //후위 표기식을 계산
    private Double postFixEval(ArrayList expr) {
        Stack numberStack = new Stack();
        for (Object o : expr) {
            if (o instanceof Double) {
                numberStack.push(o);
            } else if (o.equals("+")) {
                num1 = (Double) numberStack.pop();
                num2 = (Double) numberStack.pop();
                numberStack.push(num2 + num1);
            } else if (o.equals("-")) {
                num1 = (Double) numberStack.pop();
                num2 = (Double) numberStack.pop();
                numberStack.push(num2 - num1);
            } else if (o.equals("*")) {
                num1 = (Double) numberStack.pop();
                num2 = (Double) numberStack.pop();
                numberStack.push(num2 * num1);
            } else if (o.equals("/")) {
                num1 = (Double) numberStack.pop();
                num2 = (Double) numberStack.pop();
                numberStack.push(num2 / num1);
            }
        }

        resultNumber = (Double) numberStack.pop();

        return resultNumber;
    }

    public Double process(String equation) {
        ArrayList postfix = infixToPostfix(splitTokens(equation));
        Double result = postFixEval(postfix);
        return result;
    }

    public boolean checkNumber(String str) {
        char check;

        if (str.equals(""))
            return false;

        for (int i = 0; i < str.length(); i++) {
            check = str.charAt(i);
            if (check < 48 || check > 58) {
                if (check != '.')
                    return false;
            }
        }
        return true;
    }
}
