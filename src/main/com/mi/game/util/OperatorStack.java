package com.mi.game.util;

import java.util.ArrayList;
import java.util.List;

public class OperatorStack {
	 private List<String> list;  
	    private int size;  
	    public String top;  
	  
	    public OperatorStack() {  
	        list = new ArrayList<String>();  
	        size = 0;  
	        top = null;  
	    }  
	  
	    public int size() {  
	        return size;  
	    }  
	  
	    public void push(String s) {  
	        list.add(s);  
	        top = s;  
	        size++;  
	    }  
	  
	    public String pop() {  
	        String s = list.get(size - 1);  
	        list.remove(size - 1);  
	        size--;  
	        top = size == 0 ? null : list.get(size - 1);  
	        return s;  
	    }  
}
