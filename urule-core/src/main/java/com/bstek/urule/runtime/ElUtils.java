package com.bstek.urule.runtime;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Stack;

import com.bstek.urule.Utils;

/**
 * @author Jacky.gao
 * @since 2017年10月27日
 */
public class ElUtils {
    private static int[] operatPriority = new int[] { 0, 3, 2, 1, -1, 1, 0, 2 };// 运用运算符ASCII码-40做索引的运算符优先级
    
    public static void main(String[] args) {
    	try{
    		String expr="'gaojie:'+1.3243248E7+1024*1024";
    		System.out.println(eval(expr));    		
    	}catch(Exception ex){
    		System.out.println("="+ex.getMessage()+"=");
    	}
	}

    public static Object eval(String expression) {
    	expression = transform(expression);
    	Object result = calculate(expression);
        return result;
    }

    /**
     * 将表达式中负数的符号更改
     * @param expression 例如-2+-1*(-3E-2)-(-1) 被转为 ~2+~1*(~3E~2)-(~1)
     * @return 返回转换结果
     */
    private static String transform(String expression) {
        char[] arr = expression.toCharArray();
        for (int i = 0; i < arr.length; i++) {
        	char cc=arr[i];
            if (cc == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                        arr[i] = '~';
                    }
                }
            }
        }
        if(arr[0]=='~'||arr[1]=='('){
            arr[0]='-';
            return "0"+new String(arr);
        }else{
            return new String(arr);
        }
    }

    /**
     * 按照给定的表达式计算
     * @param expression 要计算的表达式例如:5+12*(3+5)/7
     * @return 返回计算结果
     */
    private static Object calculate(String expression) {
    	Stack<String> postfixStack = new Stack<String>();
        Stack<Object> resultStack = new Stack<Object>();
        prepare(expression,postfixStack);
        Collections.reverse(postfixStack);// 将后缀式栈反转
        while (!postfixStack.isEmpty()) {
            String currentValue = postfixStack.pop();
            if (currentValue.equals("") || !isOperator(currentValue.charAt(0))) {// 如果不是运算符则存入操作数栈中
            	if(currentValue.startsWith("\"")){
            		currentValue=currentValue.substring(1,currentValue.length()-1);
            	}
                currentValue = currentValue.replace("~", "-");
                resultStack.push(currentValue);
            } else {// 如果是运算符则从操作数栈中取两个值和该数值一起参与运算
                String secondValue = resultStack.pop().toString();
                String firstValue = resultStack.pop().toString();

                // 将负数标记符改为负号
                firstValue = firstValue.replace("~", "-");
                secondValue = secondValue.replace("~", "-");

                Object tempResult = calculate(firstValue, secondValue, currentValue.charAt(0));
                resultStack.push(tempResult);
            }
        }
        return resultStack.pop();
    }

    private static void prepare(String expression,Stack<String> postfixStack) {
    	Stack<Character> opStack = new Stack<Character>();
        opStack.push(',');// 运算符放入栈底元素逗号，此符号优先级最低
        char[] arr = expression.toCharArray();
        int currentIndex = 0;// 当前字符的位置
        int count = 0;// 上次算术运算符到本次算术运算符的字符的长度便于或者之间的数值
        char currentOp, peekOp;// 当前操作符和栈顶操作符
        String prevData=null;
        for (int i = 0; i < arr.length; i++) {
            currentOp = arr[i];
            if(isInvertedComma(currentOp)){
            	currentIndex=i+1;
            	i++;
            	count=1;
            	while(i<arr.length){
            		currentOp = arr[i];
            		if(isInvertedComma(currentOp)){
                		prevData=new String(arr, currentIndex, count-1);
                		prevData="\""+prevData+"\"";
                		break;
            		}
            		i++;
            		count++;
            	}
            	continue;
            }else if(isOperator(currentOp)) {
            	//当前字符是运算符
            	if(prevData!=null){
            		postfixStack.push(prevData);
            		prevData=null;
            	}else if (count > 0) {
            		String data=new String(arr, currentIndex, count).trim();
                    postfixStack.push(data);
                    //取两个运算符之间的内容
                }
                peekOp = opStack.peek();
                if (currentOp == ')') {
                	//遇到反括号则将运算符栈中的元素移除到后缀式栈中直到遇到左括号
                    while (opStack.peek() != '(') {
                        postfixStack.push(String.valueOf(opStack.pop()));
                    }
                    opStack.pop();
                } else {
                    while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                        postfixStack.push(String.valueOf(opStack.pop()));
                        peekOp = opStack.peek();
                    }
                    opStack.push(currentOp);
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if(prevData!=null){
        	postfixStack.push(prevData);	
        }else if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {// 最后一个字符不是括号或者其他运算符的则加入后缀式栈中
            postfixStack.push(new String(arr, currentIndex, count));
        }

        while (opStack.peek() != ',') {
            postfixStack.push(String.valueOf(opStack.pop()));// 将操作符栈中的剩余的元素添加到后缀式栈中
        }
    }
    
    private static boolean isInvertedComma(char c){
    	return c=='"';
    }
    
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '(' || c == ')';
    }
    public static boolean compare(char cur, char peek) {// 如果是peek优先级高于cur，返回true，默认都是peek优先级要低
    	if(cur=='%')cur='*';
    	if(peek=='%')peek='*';
        boolean result = false;
        if (operatPriority[(peek) - 40] >= operatPriority[(cur) - 40]) {
            result = true;
        }
        return result;
    }

    private static Object calculate(String firstValue, String secondValue, char currentOp) {
        Object result = null;
        BigDecimal first,second;
        try{
        	switch (currentOp) {
        	case '+':
        		first=Utils.toBigDecimal(firstValue);
        		second=Utils.toBigDecimal(secondValue);
        		result=first.add(second);
        		break;
        	case '-':
        		first=Utils.toBigDecimal(firstValue);
        		second=Utils.toBigDecimal(secondValue);
        		result=first.subtract(second);
        		break;
        	case '*':
        		first=Utils.toBigDecimal(firstValue);
        		second=Utils.toBigDecimal(secondValue);
        		result=first.multiply(second);
        		break;
        	case '/':
        		first=Utils.toBigDecimal(firstValue);
        		second=Utils.toBigDecimal(secondValue);
        		result=first.divide(second);
        		break;
        	case '%':
        		first=Utils.toBigDecimal(firstValue);
        		second=Utils.toBigDecimal(secondValue);
        		result=first.doubleValue() % second.doubleValue();
        		break;
        	}
        }catch(Exception ex){
        	String msg=ex.getMessage() == null ? "" : ex.getMessage();
        	String a=(firstValue == null ? "null" : firstValue.toString());
        	String b=(secondValue == null ? "null" : secondValue.toString());
			if(currentOp=='+'){
				result = a+b;				
			}else if(currentOp=='/' && msg.indexOf("by zero")!=-1){
				result = "Infinity";
			}else{
				throw ex;							
			}
        }
        return result;
    }
}
