package org.translator.java;

public class ForRange {
	private CodeBlock codeblock;
	private String var;
	private String start;
	private String end;
	private String increment;
	public ForRange(String var, String start, String end, String increment, CodeBlock codeblock){
		this.var=var;
		this.start=start;
		this.end=end;
		this.increment=increment;
		this.codeblock=codeblock;
	}
	public CodeBlock getCodeBlock(){
		return codeblock;
	}
	public String getVar(){
		return var;
	}
	public String getStart(){
		return start;
	}
	public String getEnd(){
		return end;
	}
	public String getInc(){
		return increment;
	}
}
