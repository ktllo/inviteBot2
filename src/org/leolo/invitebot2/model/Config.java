package org.leolo.invitebot2.model;

public class Config {
	private String key;
	private String value;
	private ConfigSnapshot snapshot;
	
	public Config(String key, String value){
		this.key = key;
		this.value = value;
		snapshot = createSnapshot();
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public ConfigSnapshot createSnapshot(){
		return new ConfigSnapshot(key,value);
	}

	public ConfigSnapshot getSnapshot() {
		return snapshot;
	}
	
	public class ConfigSnapshot{
		private String key;
		private String value;
		
		ConfigSnapshot(String key, String value){
			this.key = key;
			this.value = value;
		}
		
		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}
	}
}

