package rltut;

import java.util.HashMap;

public class Thing {
	protected HashMap<String, Object> data;
	public Object getData(String key) { return key == null ? 0 : data.get(key) == null ? 0 : data.get(key); }
	public boolean getBooleanData(String key) { return key == null ? false : data.get(key) == null ? false : (boolean) data.get(key); }
	public int getIntegerData(String key) { return key == null ? 0 : data.get(key) == null ? 0 : (int) data.get(key); }
	public void setData(String key, Object value) { if(key == null || value == null) return; data.put(key, value); }
	public void unsetData(String key) { if(key == null || data.get(key) == null) return; data.remove(key); }
}
