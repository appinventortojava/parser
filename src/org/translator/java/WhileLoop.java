package org.translator.java;

public class WhileLoop {
	String test;
	CodeBlock body;
	public WhileLoop(String test, CodeBlock cb){
		this.test=test;
		this.body=cb;
	}
	public CodeBlock getBody(){
		return body;
	}
	public String getTest(){
		return test;
	}
}
