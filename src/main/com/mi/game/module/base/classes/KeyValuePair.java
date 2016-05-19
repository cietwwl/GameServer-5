package com.mi.game.module.base.classes;

/**
 * 泛型数据类,主要用于存放key,value类型的对象
 * @author Administrator
 * @param <TKey>
 * @param <TValue>
 */
public class KeyValuePair <TKey,TValue> {
	private TKey key;
	private TValue value;
	
	public KeyValuePair(TKey key , TValue value){
		this.key = key;
		this.value = value;
	}
	
	public KeyValuePair(){}
	
	public TKey getKey() {
		return key;
	}
	public void setKey(TKey key) {
		this.key = key;
	}
	public TValue getValue() {
		return value;
	}
	public void setValue(TValue value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO 自动生成的方法存根
		return super.equals(obj);
	}
	
	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		return "key:"+ key +" value:" + value;
	}
}
