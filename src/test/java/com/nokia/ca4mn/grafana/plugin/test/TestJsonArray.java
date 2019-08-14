package com.nokia.ca4mn.grafana.plugin.test;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONObject;
public class TestJsonArray {
 public static void main(String[] args) throws ParseException {
//  String jsonStr = "[{\"a\":123, \"b\":\"hello\", \"x\":[{\"inner\":\"Inner JSONObject\"}]}]";
//   
//  System.out.println(jsonStr);
//  JSONArray jsonArray = new JSONArray(jsonStr);
//  JSONObject jsonObj = jsonArray.getJSONObject(0);
//  System.out.println(jsonObj);
//   
//  int a = jsonObj.getInt("a");
//  String b = jsonObj.getString("b");
//  JSONArray jsonArrayX = jsonObj.getJSONArray("x");
//   
//  System.out.println(a);
//  System.out.println(b);
//  System.out.println(jsonArrayX);
//  System.out.println(jsonArrayX.getJSONObject(0).getString("inner"));
 
  System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
 
  try{
	  //String str = "{'list':['1,2','3,4','5,6']}";
	  String str = "{'datapoints':[[1,2],[3,4],[5,6]]}";
	  
	  JSONObject jsonObject = new JSONObject(str); 
	  JSONArray jsonArray= jsonObject.getJSONArray("datapoints"); //获取list的值
	  System.out.println(jsonArray);
	  }catch(Exception e){e.printStackTrace();}
 
 }
 
 
 
 
}