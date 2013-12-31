package org.translator.java;

import java.util.ArrayList;

public class JVar {
	private String type;
	private String name;
	private String val;
	private String arrayType;
	private ArrayList members;
	private ArrayList<String> membersString;
	private final String yailList = "YailList"; 
	
	public JVar(String name){
		this.name=name;
		val=null;
		type=null;
		members=null;
		membersString = new ArrayList<String>();
	}
	
	private ArrayList removeType(ArrayList list){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) instanceof ArrayList){
				removeType((ArrayList)list.get(i));
			}else if(list.get(list.size() - 1) instanceof String && ((list.get(list.size()-1).equals("YailList")))){
				list.remove(list.size() -1);
			}
		}
		return list;
	}
	
	public ArrayList getMembers(){
		removeType(members);
		return members;
	}
	
	public String getArrayType(){
		if(members.size() > 0)
		{
			String firstType = changeMembersType(members.get(0),"",0, "nocompares");
			removeType(members);
			arrayType = changeMembersType(members, "", 0, firstType);
			if(arrayType.equals("")){
				return "ArrayList";
			}else{
				return arrayType;	
			}
		}
		else
		{
			return "ArrayList";
		}
	}
	
	public String checkString(Object obj){
		String type = "";
		
		if(obj instanceof String){
			try{
				Integer.parseInt((String)obj);
				type = "Integer";
			}catch(java.lang.NumberFormatException ex){
				try{	
					Double.parseDouble((String)obj);
					type = "Double";
				}catch(java.lang.NumberFormatException ex2){
					type = "String";
				}
			}
		}
		return type;
	}
	
	public String changeMembersType(Object obj, String prevArrayType, int nesting, String firstType){
		String type = "";
		if(obj instanceof ArrayList){
			nesting++;
			firstType = changeMembersType(((ArrayList) obj).get(0), prevArrayType, nesting, "nocompares");
			for(Object objInList : (ArrayList) obj){
				type = changeMembersType(objInList, prevArrayType+"ArrayList<", nesting, "nocompares");
			}
		}else{
			type = checkString(obj);
			type = prevArrayType + type;
			for(int i = 0; i < nesting; i++){
				type = type + ">";
			}
			
		}
		return type;
	}	
	
	
	public void setMembers(ArrayList members){
		
		this.members=members;
	}
	public void setType(String type){
		this.type=type;
	}
	public void setVal(String val){
		this.val=val;
	}
	public String getName(){
		return name;
	}
	public String getType(){
		return type;
	}
	public String getVal(){
		return val;
	}
}
