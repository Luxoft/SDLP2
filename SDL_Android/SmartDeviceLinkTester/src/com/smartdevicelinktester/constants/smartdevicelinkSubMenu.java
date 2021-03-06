package com.smartdevicelinktester.constants;

public class smartdevicelinkSubMenu {
	private String menuName;
	private int id;
	
	public void setSubMenuId(int parentID){
		this.id = parentID;
	}
	public int getSubMenuId(){
		return this.id;
	}
	public void setName(String name){
		this.menuName = name;
	}
	public String getName(){
		return this.menuName;
	}
	
	public String toString(){
		return "(" + getSubMenuId() + ") " + getName();
	}
	
}
