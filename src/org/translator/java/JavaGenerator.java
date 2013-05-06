/*
   appinventor-java-translation

   Originally authored by Joshua Swank at the University of Alabama
   Work supported in part by NSF award #0702764 and a Google 2011 CS4HS award

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package org.translator.java;

import org.translator.java.code.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Aaron Halbleib
 */
/*
 * generates java from data structures populated by AppInventorScreen
 */
class JavaGenerator {
	private String screen;
	//components
	private HashMap<String, AppComponent> components;
	//events (click, touched, etc..)
	private ArrayList<CodeBlock> events;
	//Java types like int, String, Lists, and Random
	private ArrayList<JVar> variables;
	//user defined functions
	private ArrayList<Method> methods;
	private ArrayList<String> permissions;
	private int permNo;
	protected JavaGenerator(String screen,
			HashMap<String, AppComponent> components,
			ArrayList<CodeBlock> events, ArrayList<JVar> variables,
			ArrayList<Method> methods) {
		permissions=new ArrayList<String>();
		permNo=0;
		this.methods = methods;
		this.screen = screen;
		this.components = components;
		this.events = events;
		this.variables = variables;
	}

	protected CodeSegment generateCode() {
		CodeSegment codeSegment = null;
		codeSegment = generateFormBlock();
		return codeSegment;
	}

	protected Statement declaration(String type, String name) {
		return new DeclarationStatement(
				TranslatorConstants.COMPONENT_PREFIX.concat(type), name,
				CodeVisibility.PRIVATE);
	}

	protected CodeSegment instantiation(AppComponent appComponent) {

		CodeSegment codeSegment = new CodeSegment();

		Value[] params = { new Value(appComponent.getParentComponent()) };
		codeSegment.add(new AssignmentStatement(appComponent.getComponentName(),
				new ConstructorCall(TranslatorConstants.COMPONENT_PREFIX
						.concat(appComponent.getType()), params)));

		HashMap<String, String> funcs = appComponent.getProperties();
		for (String key : funcs.keySet()) {
			String functionName = String.format("%s.%s", appComponent.getComponentName(),
					key);
			String tempString=funcs.get(key);
			Value [] value=new Value[1];
			if (tempString.startsWith("#")){
				value[0]= new Value(tempString.replaceFirst("[#]", "0")) ;
			}
			else if(functionName.endsWith("FontSize")){
				value[0] =  new Value("(float)"+tempString) ;
			}
			else {
				value[0] =  new Value(tempString) ;
			}
			codeSegment.add(new ValueStatement(new FunctionCall(functionName, value)));
		}
		codeSegment.add(new Value("")); // To add a new line

		return codeSegment;
	}
	
	
	private void parseList(ArrayList list, FunctionSegment defineFunction, 
							String listName, Value[] params, ClassSegment classSegment){
		for(int i = 0; i < list.size(); i++){
			if(list.get(i) instanceof ArrayList){
				classSegment.add(declaration("ArrayList",listName+ i));
				defineFunction.add(new AssignmentStatement(listName+ i,
						new ConstructorCall(
								TranslatorConstants.COMPONENT_PREFIX.concat("ArrayList"), params)));
				parseList((ArrayList)list.get(i), defineFunction, listName+i, params, classSegment);
				defineFunction.add(new ValueStatement(new Value(listName + ".add(" + listName+i + ")")));
				
			}else{
				defineFunction.add(new ValueStatement(new Value(listName + ".add(" + list.get(i).toString() + ")")));
			}
		}
		
	}
	
	//generates java source
	private CodeSegment generateFormBlock() {
		ClassSegment classSegment;
		classSegment = new ClassSegment(screen, CodeVisibility.PUBLIC,
				TranslatorConstants.FORM,
				TranslatorConstants.EVENT_HANDLING_INTERFACES);
		permissions=getPermissions(components);
		if (!permissions.isEmpty()){
			if (permNo>1){
				classSegment.add(new Value("//"+permNo+" types of components in your app require special permissions. Add the following lines of code to your Android_Manifest.xml file:"));
			}
			else {
				classSegment.add(new Value("//One type of component in your app requires special permissions. Add the following line(s) of code to your Android_Manifest.xml file:"));
			}
			classSegment.add(new Value(""));
		}
		for (String s : permissions){
			classSegment.add(new Value("//"+s));
		}
		classSegment.add(new Value(""));
		// variable decs
		for (JVar jVar : variables) {
			classSegment.add(declaration(jVar.getType(), jVar.getName()));
			if (jVar.getType().contentEquals("YailList")){
				classSegment.add(declaration(jVar.getArrayType(),jVar.getName()+"IL"));
			}	
		}
		classSegment.add(new Value());
		// Start declarations
		for (AppComponent appComponent : components.values()) {
			classSegment.add(declaration(appComponent.getType(), appComponent.getComponentName()));
		}
		
		classSegment.add(new Value());

		// End declarations

		// Start $define
		FunctionSegment defineFunction = new FunctionSegment("$define");

		defineFunction.add(new Value());

		for (JVar jVar : variables) {
			if (jVar.getType() == "YailList") {
				Value[] params = { new Value("") };
				defineFunction.add(new AssignmentStatement(jVar.getName(),
						new ConstructorCall(
								TranslatorConstants.COMPONENT_PREFIX.concat(jVar
										.getType()), params)));
				defineFunction.add(new AssignmentStatement(jVar.getName()+"IL",
						new ConstructorCall(
								TranslatorConstants.COMPONENT_PREFIX.concat(jVar.getArrayType()), params)));
				if(jVar.getMembers() != null) {
					ArrayList tempVarMembers = jVar.getMembers();
					for(int i = 0; i < tempVarMembers.size(); i++){
						if(tempVarMembers.get(i) instanceof ArrayList){
							classSegment.add(declaration("ArrayList",jVar.getName()+"IL" + i));
							defineFunction.add(new AssignmentStatement(jVar.getName()+"IL" + i,
									new ConstructorCall(
											TranslatorConstants.COMPONENT_PREFIX.concat(jVar.getArrayType()), params)));
							parseList((ArrayList) tempVarMembers.get(i), defineFunction, jVar.getName() + "IL" + i, params, classSegment);
							defineFunction.add(new ValueStatement(new Value(jVar
									.getName() + "IL.add(" + jVar.getName()+"IL"+i + ")")));
							
						}else{
							defineFunction.add(new ValueStatement(new Value(jVar
									.getName() + "IL.add(" + tempVarMembers.get(i).toString() + ")")));
						}
					}
				}
			
				defineFunction.add(new ValueStatement(new Value(jVar.getName()+".setItems("+jVar.getName()+"IL)")));
			}
			else if(jVar.getType().contentEquals("Random")){
				defineFunction.add(new ValueStatement(new Value(jVar.getName()+"= new Random()")));
			}
			else {
				defineFunction.add(new AssignmentStatement(jVar.getName(),
						new Value(jVar.getVal())));
			}
		}
		// make sure components with "this" as parent are instantiated first
		for (AppComponent appComponent : components.values()) {
			if (appComponent.getParentComponent().contentEquals(this.screen))
				defineFunction.add(instantiation(appComponent));
		}
		for (AppComponent appComponent : components.values()) {
			if (!appComponent.getParentComponent().contentEquals(this.screen))
				defineFunction.add(instantiation(appComponent));
		}
		for (CodeBlock event : events) {
			//code for the initialize event is just added to the define function
			//instead of giving it an event handler
		
			if (event.getMethod().contentEquals("Initialize")) {
				CodeSegment codeSegment = getActions(event);
				defineFunction.add(codeSegment);
				break;
			}
		}
		defineFunction.add(registerEvents());
		classSegment.add(defineFunction);
		// End $define

		// Start dispatchEvent
		FunctionSegment dispatchFunction = new FunctionSegment("dispatchEvent",
				CodeVisibility.PUBLIC, "boolean", new Parameter("Component",
						"component", 0), new Parameter("java.lang.String",
						"componentName", 1), new Parameter("java.lang.String",
						"eventName", 2), new Parameter("Object[]", "params", 3));
		for (CodeBlock event : events) {
			if (!event.getMethod().contentEquals("Initialize")) {
				IfSegment ifSegment = new IfSegment(new Value(dispatchCondition(event)));
				StringBuffer argstring = new StringBuffer();
				int i = 0;
				//this if/else is here to properly cast arguments to event handlers, since the dispatch event 
				//function params argument is an array of objects
				//ArrayList<String> event.getArgTypes() will be empty unless the particular component and event
				//are explicitly handled in the getEventParamTypes method in AppInventorScreen, and arguments to the 
				//event function will not be cast
				if (event.getArgTypes().size()==0)
					for (String arg : event.getArgs()) {
						if (i == 0) {
							argstring.append("params[0]");
						} else {
							argstring.append(", " + "params[" + i + "]");
						}
						i++;
					}
				else {
					ArrayList<String> typearray=event.getArgTypes();
					for (String arg : event.getArgs()) {
						if (i == 0) {
							argstring.append(typearray.get(i)+" params[0]");
						} else {
							argstring.append(", " +typearray.get(i)+ " params[" + i + "]");
						}
						i++;
					}
				}
				ifSegment.add(new Value());
				ValueStatement vs=new ValueStatement(new Value(event.getComponent()
						+ event.getMethod() + "(" + argstring.toString() + ");"+"\n\t"+"return true"));
				ifSegment.setThendo(vs);
				dispatchFunction.add(ifSegment);
			}	
		}
		dispatchFunction.add(new ValueStatement(new Value("return false")));
		classSegment.add(dispatchFunction);
		// End dispatchEvent
		//generate user functions
		for (Method m : methods) {
			FunctionSegment tempSegment;
			if (m.getArgs().size()>0){
				Parameter[] params = new Parameter[m.getArgs().size()];
				int i=0;
				for (String arg : m.getArgs()) {
					params[i] = new Parameter("Object", arg);
					i++;
				}
				tempSegment = new FunctionSegment(m.getName(),
						CodeVisibility.PUBLIC, m.getReturnType(),params);
			}
			else {
			tempSegment = new FunctionSegment(m.getName(),
					CodeVisibility.PUBLIC, m.getReturnType());
			}
			
			tempSegment.add(getActions(m.getCodeBlock()));
			if (m.getCodeBlock().getReturnVal()!=null){
				tempSegment.add(new ValueStatement(new Value("return "+m.getCodeBlock().getReturnVal())));
			}
			classSegment.add(tempSegment);
		}
		//generate functions called in dispatchEvent function
		for (CodeBlock event : events) {
					if (!event.getMethod().contentEquals("Initialize")) {
				CodeSegment cs = getActions(event);
				Parameter[] params = new Parameter[event.getArgs().size()];
				int i = 0;
				if (event.getArgTypes().size()==0){
					for (String arg : event.getArgs()) {
						params[i] = new Parameter("Object", arg);
						i++;
					}
				}
				else {
					ArrayList<String> temparray=event.getArgTypes();
					for (String arg : event.getArgs()) {
						params[i] = new Parameter(temparray.get(i).substring(1, temparray.get(i).length()-1), arg);
						i++;
					}
				}
				FunctionSegment tempSegment = new FunctionSegment(
						event.getComponent() + event.getMethod(),
						CodeVisibility.PUBLIC, "void", params);
				tempSegment.add(cs);
				classSegment.add(tempSegment);
			}
		}
		return classSegment;
	}
	//returns code for any and all structures which deal with arbitrary sequences of statements,
	//i.e for loops, event handlers, user functions, and if/else blocks
	private CodeSegment getActions(CodeBlock eventCodeBlock) {
		CodeSegment codeSegment = new CodeSegment();
		ArrayList<ComponentCall> componentCalls = eventCodeBlock.getActions();
		codeSegment.add(new Value(""));
		for (ComponentCall componentCall : componentCalls) {
			if (!componentCall.getMarker() && componentCall.getCalledComponentName().length() > 0) {
				String functionName = componentCall.getCalledComponentName()+"."+componentCall.getMethod();
				
				Value[] values = new Value[componentCall.getArguments().size()];
				for (int j = 0; j < values.length; j++) {
					String tempstring=componentCall.getArguments().get(j);
					if (componentCall.getMethod().contentEquals("Text")) {
						boolean b=false;
						for (JVar jVar :variables){
							if (jVar.getType().contentEquals("YailList") && jVar.getName().contentEquals(tempstring)){
								values[j]=new Value(tempstring + ".toString()");
								b=true;
								break;
							}
						}
						if (!b){
							values[j] = new Value(tempstring);
						}
					}
					else if(componentCall.getMethod().contentEquals("FontSize")){
						values[j] = new Value("(float)"+tempstring);
					}
					else if (tempstring.startsWith("#")){
						values[j] = new Value(tempstring.replaceFirst("[#]", "0"));
					}
					else {
						values[j] = new Value(tempstring);
					}
				}
				if (componentCall.getVar() == null) {
					codeSegment.add(new ValueStatement(new FunctionCall(functionName,
							values)));
				} else {
					codeSegment.add(new AssignmentStatement(componentCall.getVar(),
							new FunctionCall(functionName, values)));
				}
			//User defined procedures go here
			} else if (!componentCall.getMarker()) {
				StringBuffer procedureCall = new StringBuffer(componentCall.getMethod() + "(");
				//Append all arguments
				if(componentCall.getArguments() != null && componentCall.getArguments().size() > 0){
					//add the first one to avoid comma problem.
					procedureCall.append(componentCall.getArguments().get(0));
					for(int i = 1; i < componentCall.getArguments().size(); i++)
						procedureCall.append(", " + componentCall.getArguments().get(i));
					
				}
				procedureCall.append(")");
				codeSegment.add(new ValueStatement(new Value(procedureCall.toString())));
				
			}
			//generate if/else code
			else if (componentCall.getCalledComponentName().contentEquals("if")) {
				org.translator.java.IfStatement ifStatement = eventCodeBlock.popFromIfStatements();
				String ret=null;
				if (ifStatement.getEvent().getComponent().contentEquals("choose")){
					ret="return "+ifStatement.getEvent().popFromArgs();
				}
				CodeSegment then = getActions(ifStatement.getEvent());
				IfSegment tempIfSegment;
				if (ifStatement.getLeft() != null && ifStatement.getRight() != null) {
					tempIfSegment = new IfSegment(new Value(ifStatement.getLeft()
							.concat(ifStatement.getOperator()).concat(ifStatement.getRight())));
					tempIfSegment.setThendo(then);
				} else {
					tempIfSegment = new IfSegment(new Value(ifStatement.getOperator()));
					if (ret!=null){
						then.add(new ValueStatement(new Value(ret)));
					}
					tempIfSegment.setThendo(then);
				}
				ElseIfSegment elseIfSegment;
				//if this condition is true, there is an else if
				if (ifStatement.getElseIf() != null) {
					while (ifStatement.getElseIf() != null) {
						ifStatement = ifStatement.getElseIf();
						if (ifStatement.getLeft() != null && ifStatement.getRight() != null) {
							elseIfSegment = new ElseIfSegment(new Value(ifStatement.getLeft()
									.concat(ifStatement.getOperator())
									.concat(ifStatement.getRight())));
							elseIfSegment.setThendo(getActions(ifStatement.getEvent()));
						} else {
							elseIfSegment = new ElseIfSegment(new Value(ifStatement.getOperator()));
							elseIfSegment.setThendo(getActions(ifStatement.getEvent()));
						}
						//if this condition is true, the current else if has the else and we are done
						if (ifStatement.getElseNoCon() != null) {
							CodeSegment et = getActions(ifStatement.getElseNoCon());
							elseIfSegment.setElse(et);
						}
						tempIfSegment.addElseIf(elseIfSegment);
					}
				}
				//this condition only applies if there are no else ifs
				//handles the case where there is an if directly followed by an else, rather than if, else if,else if.... else
				else if (ifStatement.getElseNoCon() != null) {
					String ret1=null;
					if (ifStatement.getElseNoCon().getComponent().contentEquals("choose")){
						ret1="return "+ifStatement.getElseNoCon().popFromArgs();
					}
					CodeSegment elseSegment = getActions(ifStatement.getElseNoCon());
					if (ret1!=null){
					elseSegment.add(new ValueStatement(new Value(ret1)));
					}
					tempIfSegment.setElse(elseSegment);
				}

				codeSegment.add(tempIfSegment);
			}
			//generate code for for loop
			else if (componentCall.getCalledComponentName().contentEquals("foreach")) {
				ForEach fe = eventCodeBlock.popFromForLoops();
				CodeSegment codeBlock = getActions(fe.getEvent());
				ForEachSegment tempSegment = new ForEachSegment(new Value(
						fe.getVarName()), new Value(fe.getListName()),
						new Value(fe.getType()));
				tempSegment.add(codeBlock);
				codeSegment.add(tempSegment);

			}
			else if (componentCall.getCalledComponentName().contentEquals("while")) {
				WhileLoop wl = eventCodeBlock.popFromWhiles();
				CodeSegment codeBlock = getActions(wl.getBody());
				WhileSegment tempSegment = new WhileSegment(new Value(wl.getTest()));
				tempSegment.add(codeBlock);
				codeSegment.add(tempSegment);

			}
			else if (componentCall.getCalledComponentName().contentEquals("forrange")) {
				ForRange fr = eventCodeBlock.popFromForRange();
				CodeSegment codeBlock = getActions(fr.getCodeBlock());
				ForRangeSegment tempSegment = 
						new ForRangeSegment(new Value("int "+fr.getVar()+"="+fr.getStart()+"; "
								+fr.getVar()+"<"+fr.getEnd()+"; "+fr.getVar()+"="+fr.getVar()+"+"+fr.getInc()));
				tempSegment.add(codeBlock);
				codeSegment.add(tempSegment);

			}
			//generate code for assignment statement
			else if (componentCall.getCalledComponentName().contentEquals("assignment")) {
				JVar jVar = eventCodeBlock.popFromAssignments();
				codeSegment.add(new AssignmentStatement(jVar.getName(), new Value(jVar
						.getVal())));
			}
		}
		return codeSegment;
	}

	private CodeSegment registerEvents() {

		CodeSegment segment = new CodeSegment();

		for (CodeBlock eventCodeBlock : events) {
			//Initialize event goes in the define function so dont register it here
			if (!eventCodeBlock.getMethod().contentEquals("Initialize")) {
				segment.add(new ValueStatement(new StaticFunctionCall(
						TranslatorConstants.EVENT_DISPATCHER,
						"registerEventForDelegation", new Value("this"),
						new Value("\"" + eventCodeBlock.getComponent() + "\""), new Value(
								"\"" + eventCodeBlock.getMethod() + "\""))));
			}
		}
		return segment;
	}

	private String dispatchCondition(CodeBlock eventCodeBlock) {
		return "component.equals(" + eventCodeBlock.getComponent()
				+ ") && eventName.equals(\"" + eventCodeBlock.getMethod() + "\")";
	}
	//determines which permissions may need to be added to the android_manifest.xml files and generates comments to notify the user
	//this is incomplete
	private ArrayList<String> getPermissions(HashMap<String,AppComponent> hm){
		ArrayList<String> ta=new ArrayList<String>();
		boolean vib=false;
		boolean internet=false;
		boolean text=false;
		boolean camera=false;
		for (String s : hm.keySet()){
			AppComponent ac=hm.get(s);
			if (ac.getType().contentEquals("Sound") && !vib){
				ta.add("Sound: if you call the Vibrate method of the Sound component, you need the vibrate permision. ");
				ta.add("<uses-permission android:name=\"android.permission.VIBRATE\"/>");
				permNo++;
				vib=true;
			}
			else if(ac.getType().contentEquals("Texting") && !text){
				ta.add("Texting: Depending on your use of the Texting component, you may need some or all of the following permissions. ");
				ta.add("<uses-permission android:name=\"android.permission.SEND_SMS\"/>");
				ta.add("<uses-permission android:name=\"android.permission.RECEIVE_SMS\"/>");
				ta.add("<uses-permission android:name=\"android.permission.BROADCAST_SMS\"/>");
				permNo++;
				text=true;
			}
			else if (ac.getType().contentEquals("Web") && !internet){
				ta.add("Web: You need the internent permission to access the internet using the web component. ");
				ta.add("<uses-permission android:name=\"android.permission.INTERNET\"/>");
				permNo++;
				internet=true;
			}
			else if (ac.getType().contentEquals("Camera") && !camera) {
				ta.add("Camera: You need the camera permission to use the camera component. ");
				ta.add("<uses-permission android:name=\"android.permission.CAMERA\"/>");
				permNo++;
				camera=true;
			}
		}
		return ta;
	}

}
