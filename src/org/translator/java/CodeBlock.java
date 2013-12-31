package org.translator.java;

import java.util.ArrayList;

public class CodeBlock {
	private String component;
	private String method;
	private org.translator.parser.Token token;
	private ArrayList<ComponentCall> actions;
	private ArrayList <org.translator.java.IfStatement> ifStatements;
	private ArrayList<ForEach> forLoops;
	private ArrayList<ForRange> frLoops;
	private ArrayList<WhileLoop> wLoops;
	private ArrayList<String>  args;
	private ArrayList<String>  argtypes;
	private ArrayList<JVar> assignments;
	private String returnval;
	//utility booleans
	private boolean b1;
	private boolean b2;
	
	public CodeBlock(String component){
		returnval=null;
		token=null;
		b1=false;
		b2=false;
		assignments=new ArrayList<JVar>();
		args=new ArrayList<String>();
		argtypes=new ArrayList<String>();
		this.component=component;
		actions=new ArrayList<ComponentCall>();
		forLoops=new ArrayList<ForEach>();
		frLoops=new ArrayList<ForRange>();
		wLoops=new ArrayList<WhileLoop>();
		ifStatements=new ArrayList<org.translator.java.IfStatement>();
	}
	public void setReturnVal(String returnval){
		this.returnval=returnval;
	}
	public String getReturnVal(){
		return returnval;
	}
	public void setComponent(String component){
		this.component=component;
	}
	public boolean getB1(){
		return b1;
	}
	public void setToken(org.translator.parser.Token t){
		token=t;
	}
	public org.translator.parser.Token getToken(){
		return token;
	}
	public void setB1(boolean b){
		b1=b;
	}
	public boolean getB2(){
		return b2;
	}

	public void setDefE(boolean b){
		b2=b;
	}
	public String getComponent(){
		return component;
	}
	public void setMethod(String method){
		this.method=method;
	}
	public String getMethod(){
		return method;
	}
	public void addAssignment(JVar var){
		assignments.add(var);
	}
	public ArrayList<JVar> getAssignments(){
		return assignments;
	}
	public JVar popFromAssignments(){
		return assignments.remove(0);
	}
	public void addArg(String arg){
		args.add(arg);
	}
	public ArrayList<String> getArgs(){
		return args;
	}
	public void addArgType(String argtype){
		argtypes.add(argtype);
	}
	public ArrayList<String> getArgTypes(){
		return argtypes;
	}
	public void addCall(ComponentCall c){
		actions.add(c);
	}
	public void addIf(org.translator.java.IfStatement ifStatement){
		ifStatements.add(ifStatement);
	}
	public ArrayList<org.translator.java.IfStatement> getIfStatements(){
		return ifStatements;
	}
	public org.translator.java.IfStatement popFromIfStatements(){
		return ifStatements.remove(0);
	}
	public String popFromArgs(){
		return args.remove(args.size()-1);
	}
	public void addForEach(ForEach fe){
		forLoops.add(fe);
	}
	public ArrayList<ForEach> getForLoops(){
		return forLoops;
	}
	public ForEach popFromForLoops(){
		return forLoops.remove(0);
	}
	public void addForRange(ForRange fr){
		frLoops.add(fr);
	}
	public ArrayList<ForRange> getForRangeLoops(){
		return frLoops;
	}
	public ForRange popFromForRange(){
		return frLoops.remove(0);
	}
	public void addWhile(WhileLoop wl){
		wLoops.add(wl);
	}
	public ArrayList<WhileLoop> getWhileLoops(){
		return wLoops;
	}
	public WhileLoop popFromWhiles(){
		return wLoops.remove(0);
	}
	public ArrayList<ComponentCall> getActions(){
		return actions;
	}
}
