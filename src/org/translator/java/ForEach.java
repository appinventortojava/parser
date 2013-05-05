package org.translator.java;

public class ForEach {
	private String type;
	private String varName;
	private String listName;
	private CodeBlock codeBlock;
	public ForEach(String type, String listName, String varName,CodeBlock codeBlock){
		this.type=type;
		this.listName=listName;
		this.varName=varName;
		this.codeBlock=codeBlock;
	}
	public String getListName(){
		return listName;
	}
	public String getVarName(){
		return this.varName;
	}
	public String getType(){
		return type;
	}
	public CodeBlock getEvent(){
		return codeBlock;
	}
}
