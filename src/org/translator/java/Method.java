package org.translator.java;

import java.util.ArrayList;

public class Method {
	private String name;
	private String returntype;
	private CodeBlock codeBlock;
	private ArrayList<String> args;
	public Method(String name, CodeBlock codeBlock,ArrayList<String> args){
		this.args=args;
		this.name=name;
		this.codeBlock=codeBlock;
		this.returntype="void";
	}
	public Method(String name, CodeBlock codeBlock,ArrayList<String> args, String returntype){
		this.args=args;
		this.name=name;
		this.codeBlock=codeBlock;
		this.returntype=returntype;
	}
	public String getReturnType(){
		return returntype;
	}
	public ArrayList<String> getArgs(){
		return args;
	}
	public void addArg(String arg){
		args.add(arg);
	}
	public CodeBlock getCodeBlock(){
		return codeBlock;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
}
