package rltut;

import java.util.HashMap;

public class DataStructure {
	protected HashMap<String, Object> data;
	public void setData(String key, Object value){ data.put(key, value); }
	public void setAllData(HashMap<String, Object> datas){ data.putAll(datas); } 
	public void unsetData(String key) { data.remove(key); }
	public Object getData(String key){ return data.get(key); }
	public HashMap<String, Object> getAllData(){ return data; }
	public boolean getBooleanData(String key) { return data.get(key) == null ? false : (boolean) data.get(key); }
	public int getIntegerData(String key) { return data.get(key) == null ? 0 : (int) data.get(key); }
	
	public DataStructure(){
		data = new HashMap<String, Object>();
	}
}
