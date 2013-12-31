package org.translator.java;

import java.util.HashMap;

/**
 * @author Ramya Gadiyaram
 *
 */
public class AppComponent {
	
	private HashMap<String,String> componentProperties;
	private String componentName;
	private String componentType;
	private String parentComponent;
	/** componentProperties, eventHandlers will be set in TokenGenerator.java
	 * when corresponding tokens are parsed 
	 * @param componentName
	 */
	public AppComponent (String parent) {
		this.parentComponent=parent;
		componentProperties=new HashMap<String,String>();
	}
	public String getComponentName() {
		return componentName;
	}
	public String getType(){
		return componentType;
	}
	public void setType(String type){
		this.componentType=type;
	}
	public String getParentComponent(){
		return parentComponent;
	}
	public void setParentComponent(String parent){
		this.parentComponent=parent;
	}
	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public HashMap<String,String> getProperties() {
		return this.componentProperties;
	}
	
	public void setProperties(String propertyName, String propertyValue) {
		this.componentProperties.put(propertyName, propertyValue);		
	}

}
