package com.mi.game.util;

import java.util.ArrayList;
import java.util.List;

public class NBLCalculator {
	private OperatorStack ms1 = new OperatorStack();//生成逆波兰表达式的栈  
    private OperatorStack ms2 = new OperatorStack();//运算栈  
      
    /** 
     * 将字符串转换为中序表达式 
     */  
    public List<String> conversion(String s) {  
        List<String> ls = new ArrayList<String>();//存储中序表达式  
        int i = 0;  
        String str;  
        char c;  
        do {  
            if ((c = s.charAt(i)) < 48 || (c = s.charAt(i)) > 57) {  
                ls.add("" + c);  
                i++;  
            } else {  
                str = "";  
                while (i < s.length() && (c = s.charAt(i)) >= 48  
                        && (c = s.charAt(i)) <= 57) {  
                    str += c;  
                    i++;  
                }  
                ls.add(str);  
            }  
  
        } while (i < s.length());  
        return ls;  
    }  
    /** 
     * 将中序表达式转换为逆波兰表达式 
     */  
    public List<String> parse(List<String> ls) {  
        List<String> lss = new ArrayList<String>();  
        for (String ss : ls) {  
            if (ss.matches("\\d+")) {  
                lss.add(ss);  
            } else if (ss.equals("(")) {  
                ms1.push(ss);  
            } else if (ss.equals(")")) {  
  
                while (!ms1.top.equals("(")) {  
                    lss.add(ms1.pop());  
                }  
                ms1.pop();  
            } else {  
                while (ms1.size() != 0 && getValue(ms1.top) >= getValue(ss)) {  
                    lss.add(ms1.pop());  
                }  
                ms1.push(ss);  
            }  
        }  
        while (ms1.size() != 0) {  
            lss.add(ms1.pop());  
        }  
        return lss;  
    }  
    /** 
     * 对逆波兰表达式进行求值 
     */  
    public int calculate(List<String> ls) {  
        for (String s : ls) {  
            if (s.matches("\\d+")) {  
                ms2.push(s);  
            } else {  
                int b = Integer.parseInt(ms2.pop());  
                int a = Integer.parseInt(ms2.pop());  
                if (s.equals("+")) {  
                    a = a + b;  
                } else if (s.equals("-")) {  
                    a = a - b;  
                } else if (s.equals("*")) {  
                    a = a * b;  
                } else if (s.equals("\\")) {  
                    a = a / b;  
                }  
                ms2.push("" + a);  
            }  
        }  
        return Integer.parseInt(ms2.pop());  
    }  
    /** 
     * 获取运算符优先级 
     * +,-为1 *,/为2 ()为0 
     */  
    public int getValue(String s) {  
        if (s.equals("+")) {  
            return 1;  
        } else if (s.equals("-")) {  
            return 1;  
        } else if (s.equals("*")) {  
            return 2;  
        } else if (s.equals("\\")) {  
            return 2;  
        }  
        return 0;  
    }  
    
    public int calculateValue(String stringValue) {
    	return calculate(parse(conversion(stringValue)));
    }
  
}
