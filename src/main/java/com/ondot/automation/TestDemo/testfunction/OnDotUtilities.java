package com.ondot.automation.TestDemo.testfunction;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OnDotUtilities {

	public String getValueFromJsonData(JSONObject jsonObj, String key){
		
		String value = null;
		try {
			value = jsonObj.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;
		
	}
	
	public String getValueFromJsonData(JSONObject jsonObj, String jsonArrayName, int arrayindex, String key){
		
		String value = null;
		try {
			value = jsonObj.getJSONArray(jsonArrayName).getJSONObject(arrayindex).getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
		
	}
	
	public String getValueFromJsonData(JSONObject jsonObj, String jsonObjName, String key){
		
		String value = null;
		 try {
			value = jsonObj.getJSONObject(jsonObjName).getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
		
	}
}
