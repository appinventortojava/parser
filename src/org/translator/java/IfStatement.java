package org.translator.java;


public class IfStatement {
	private String left;
	private String right;
	private String operator;
	private CodeBlock event;
	private CodeBlock elsenocon;
	private IfStatement elseif;
	public IfStatement(String left,String right,String operator, CodeBlock then){
		this.left=left;
		this.right=right;
		this.operator=operator;
		event=then;
		elseif=null;
		elsenocon=null;
	}
	public void setElseNoCon(CodeBlock e){
	elsenocon=e;
	}
	public CodeBlock getElseNoCon(){
		return elsenocon;
	}
	public String getOperator(){
		return operator;
	}
	public IfStatement getElseIf(){
		return elseif;
	}
	public void setElseIf (IfStatement elseif){
		this.elseif=elseif;
	}
	public String getRight(){
		return right;
	}
	public String getLeft(){
		return left;
	}
	public CodeBlock getEvent(){
		return event;
	}
}
