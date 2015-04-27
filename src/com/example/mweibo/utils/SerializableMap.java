package com.example.mweibo.utils;

import java.io.Serializable;
import java.util.Map;

//将数据保存为一个对象
public class SerializableMap implements Serializable{
	private Map<String,Object> map;
	 
    public Map<String, Object> getMap() {
        return map;
    }
 
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
	
}
