package com.example.calculator;

import org.scijava.parsington.ExpressionParser;
import org.scijava.parsington.eval.DefaultTreeEvaluator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalcBuffer {
    private StringBuilder buffer;
    private DefaultTreeEvaluator evaluator;
    private Pattern pt_operators, pt_floatNums;

    public CalcBuffer() {
        buffer = new StringBuilder();
        evaluator = new DefaultTreeEvaluator();

        pt_operators = Pattern.compile("[+\\-*\\/xX^]");
        pt_floatNums = Pattern.compile("\\d+\\.\\d+");
    }
    public void append(char c) {
        buffer.append(c);
    }
    public void append(String str) {
        buffer.append(str);
    }
    public String toString() {
        return buffer.toString();
    }
    public void clear() {
        buffer.setLength(0);
    }
    public String getResult() {
        String expression = parse(buffer.toString());
        Object result=null;
        try {
            result = evaluator.evaluate(expression);
        } catch (Exception e) {
//            result = e.getMessage() + ":" + expression;
            result = "";
        } finally {
            if(result == null) {
                // No result when operator expr of the form 1234/ or num-op
//                result = buffer.toString();
                result = "";
            }
        }
        return result.toString();
    }
    public void removeLast() {
        if(buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length()-1);
        }
    }
    public void changeSign() {
        if(buffer.length() <= 0) return;

        StringBuilder lastNum = new StringBuilder();
        int i = buffer.length()-1;
        // exract last-input-number
        if(buffer.toString().endsWith(")")) {
            while(buffer.charAt(i) != '(') i--;
            --i;
            lastNum.append(buffer.subSequence(i+1, buffer.length()-1));
        } else {
            for (i = buffer.length() - 1; i >= 0; i--) {
                if (isOperator(buffer.charAt(i))) {
                    break;
                } else if (buffer.charAt(i) == '(' || buffer.charAt(i) == ')') {
                    continue;
                }
                lastNum.insert(0, buffer.charAt(i));
            }
        }
        if(lastNum.length() > 0) {  // if there is a last number
            // apply the transformation if of the form (-x) => x
            if(i>0 && buffer.charAt(i) == '-' && buffer.charAt(i-1) == '(') {
                i-=2;   // move i to before (-x)
//                lastNum.insert(0, '+');
            } else { // else apply the transformation x => (-x)
                lastNum.insert(0,"(-");
                lastNum.append(")");
            }
            buffer.delete(i+1, buffer.length());
            buffer.append(lastNum);
        }
    }
    private boolean isOperator(char ch) {
        switch(ch) {
            case '+':
            case '-':
            case '/':
            case 'x':
            case 'X':
            case '*':
            case '%':
                return true;
        }
        return false;
    }
    public String parse(String expr) {
        if(expr == null || expr.isEmpty()) {
            return "";
        }
        // replace x with multiply(*)
        expr=expr.replaceAll("[xX]", "*");
        // resolve x[+-/*], expression ends with operator
        String exprEnd = expr.substring(expr.length()-1);
        if(Pattern.matches("[+\\-\\/*xX]", exprEnd)) {
            if(Pattern.matches("[\\/*xX]", exprEnd)) {
                expr = expr + "1"; // multiplicative identity
            } else {
                expr = expr + "0"; // additive identity
            }
        }
        // resolve mismatched parenthesis ()
        if(countGroups(expr) > 0) {
            expr = expr + ")";
        } else if(countGroups(expr) < 0) {
            expr = "(" + expr;
        }

        String[] nums = pt_operators.split(expr);
        for (int i = 0; i < nums.length; i++) {
            // convert non-floating points to floating points
            if(Pattern.matches(pt_floatNums.pattern(), nums[i]) == false) {
                if(Pattern.matches("\\(\\)?", nums[i]))
                { // avoid transformations (-x) => (.0 and -x.0), (.0)
                    continue;
                }
                else if(Pattern.matches("\\d+\\)", nums[i])) {
                    nums[i] = nums[i].substring(0, nums[i].length()-1) + ".0)";
                }
                else {
                    nums[i] = nums[i] + ".0";
                }
            }
        }
        // join expression back
        StringBuilder new_expr = new StringBuilder();
        int i = 0;
        Matcher matcher = pt_operators.matcher(expr);
        while(matcher.find()) {
            if(i < nums.length) {
                new_expr.append(nums[i++]);
            }
            new_expr.append(matcher.group(0));
        }
        while(i < nums.length) {
            new_expr.append(nums[i++]);
        }

        return new_expr.toString();
    }

    private int countGroups(String expr) {
        int leftCount = 0, rightCount = 0;
        Matcher leftParen = Pattern.compile("\\(").matcher(expr);
        Matcher rightParen = Pattern.compile("\\)").matcher(expr);

        while(leftParen.find()) leftCount++;
        while(rightParen.find()) rightCount++;

        return leftCount - rightCount;
    }
}
