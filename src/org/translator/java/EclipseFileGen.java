package org.translator.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.conn.Wire;

public class EclipseFileGen {
	
	public static String CAMERA_PERM_TXT = "camera";
	public static String TEXT_PERM_TXT = "text";
	public static String INTERNET_PERM_TXT = "internet";
	public static String VIBARTE_PERM_TXT = "vib";
	public static String LISTPICKER_TXT = "ListPicker";
	
	protected static String RESOURCE_PATH = "/myresources/";
	//protected static String RESOURCE_PATH = "/usr/share/buildserver/lib/myresources/";
	
	private static String CAMERAPERMISSION = "<uses-permission android:name=\"android.permission.CAMERA\"/>\n";
    private static String SENDTXTPERMISSION = "<uses-permission android:name=\"android.permission.SEND_SMS\"/>\n";
    private static String RECEIVETXTPERMISSION = "<uses-permission android:name=\"android.permission.RECEIVE_SMS\"/>\n";
    private static String INTERNETPERMISSION = "<uses-permission android:name=\"android.permission.INTERNET\"/>\n";
    private static String VIBRATEPERMISSION = "<uses-permission android:name=\"android.permission.VIBRATE\"/>\n";
    private static String LISTPICKER = "<activity android:name=\"com.google.devtools.simple.runtime.components.android.ListPickerCheckboxActivity\"/>\n";
    
    protected ArrayList<String> screenNames = new ArrayList<String>();
    protected HashMap<String, Boolean> permissions = new HashMap<String, Boolean>();
    protected String filePath;
    protected String packageName;
	
    
    public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public EclipseFileGen(String filePath){
    	this.filePath = filePath;
    }
	
	public void build(){
		buildManifestFile();
		writeClassPath();
		writeProjectFile();
		writeProjectProperites();
		addLibsRes();
	}
    
    
	public void buildManifestFile(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/AndroidManifest.xml"));
			writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.append("<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\n");
			writer.append("package=\"org."+ packageName + "\" \n");
			writer.append("android:versionCode=\"1\"\n android:versionName=\"1.0\" > \n");
			writer.append("<uses-sdk \n android:minSdkVersion=\"7\"\n android:targetSdkVersion=\"7\" /> \n");
			
			for (String per : permissions.keySet()){
				if (per.compareTo(CAMERA_PERM_TXT) == 0){
					writer.append(CAMERAPERMISSION);
				}
				else if (per.compareTo(INTERNET_PERM_TXT) == 0){
					writer.append(INTERNETPERMISSION);
				} else if (per.compareTo(TEXT_PERM_TXT) == 0){
					writer.append(SENDTXTPERMISSION);
					writer.append(RECEIVETXTPERMISSION);
				} else if (per.compareTo(LISTPICKER_TXT) == 0){
					writer.append(LISTPICKER);
				} else {
					writer.append(VIBRATEPERMISSION);
				}
			}
			String[] tmp = packageName.split("\\.");
			writer.append("<application android:icon=\"@drawable/ic_launcher\" android:label=\"");
			writer.append(tmp[tmp.length-1] + "\">\n");
			
			// make first screen main
			String screen1 = screenNames.get(0);
			writer.append("<activity android:name=\""+ "." +screen1 + "\">\n <intent-filter>\n" +
				"<action android:name=\"android.intent.action.MAIN\"/>\n" +
				"<category android:name=\"android.intent.category.LAUNCHER\"/>\n" +
			    "</intent-filter></activity>\n");
			
			screenNames.remove(0);
			
			for (String screen: screenNames){
				writer.append("<activity android:name=\""+ screen + "\"></activity>\n");
			}
			writer.append("</application>" +
					"</manifest>");
			
			writer.flush();
			writer.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.print("Failed in manifest writing");
			e.printStackTrace();
		}
		
	}

	public void writeClassPath() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/.classpath"));
			writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.append("<classpath>\n");
			writer.append("	<classpathentry kind=\"src\" path=\"src\"/>\n");
			writer.append("	<classpathentry kind=\"src\" path=\"gen\"/>\n");
			writer.append("	<classpathentry kind=\"con\" path=\"com.android.ide.eclipse.adt.ANDROID_FRAMEWORK\"/>\n");
			writer.append("	<classpathentry kind=\"con\" path=\"com.android.ide.eclipse.adt.LIBRARIES\"/>\n");
			writer.append("	<classpathentry kind=\"lib\" path=\"libs/AiBridge_v1.0.1.jar\"/>\n");
			writer.append("	<classpathentry kind=\"output\" path=\"bin/classes\"/>\n");
			writer.append("</classpath>\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.print("Failed in classpath writing");
			e.printStackTrace();
		}
		
	}
	
	public void writeProjectFile(){
		try {
			String[] tmp = packageName.split("\\.");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + "/.project"));
			writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			writer.append("<projectDescription>\n");
			//insert the correct name
			writer.append("	<name>"+ tmp[tmp.length-1] + "</name>\n");
			writer.append("	<comment></comment>\n");
			writer.append("	<projects>\n");
			writer.append("	</projects>\n");
			writer.append("	<buildSpec>\n");
			writer.append("		<buildCommand>\n");
			writer.append("			<name>com.android.ide.eclipse.adt.ResourceManagerBuilder</name>\n");
			writer.append("			<arguments>\n");
			writer.append("			</arguments>\n");
			writer.append("		</buildCommand>\n");
			writer.append("		<buildCommand>\n");
			writer.append("			<name>com.android.ide.eclipse.adt.PreCompilerBuilder</name>\n");
			writer.append("			<arguments>\n");
			writer.append("			</arguments>\n");
			writer.append("		</buildCommand>\n");
			writer.append("		<buildCommand>\n");
			writer.append("			<name>org.eclipse.jdt.core.javabuilder</name>\n");
			writer.append("			<arguments>\n");
			writer.append("			</arguments>\n");
			writer.append("		</buildCommand>\n");
			writer.append("		<buildCommand>\n");
			writer.append("			<name>com.android.ide.eclipse.adt.ApkBuilder</name>\n");
			writer.append("			<arguments>\n");
			writer.append("			</arguments>\n");
			writer.append("		</buildCommand>\n");
			writer.append("	</buildSpec>\n");
			writer.append("	<natures>\n");
			writer.append("		<nature>com.android.ide.eclipse.adt.AndroidNature</nature>\n");
			writer.append("		<nature>org.eclipse.jdt.core.javanature</nature>\n");
			writer.append("	</natures>\n");
			writer.append("</projectDescription>\n");
			
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.print("Failed in .project writing");
			e.printStackTrace();
		}
	}

	public void writeProjectProperites(){

		String dest = filePath + "/project.properties";

		String source = RESOURCE_PATH + "project.properties";
		
		copyFile(source, dest);		 
		
	}
	
	public void addLibsRes(){
		String source = RESOURCE_PATH + "AiBridge_v1.0.1.jar";
		
		File dir = new File(filePath + "/libs");
		dir.mkdir();
		
		String dest = filePath + "/libs/AiBridge_v1.0.1.jar";
		
		copyFile(source, dest);
		
		dir = new File(filePath + "/res");
		dir.mkdir();
		
		dir = new File(filePath + "/res/drawable");
		dir.mkdir();
		
		source = RESOURCE_PATH + "ic_launcher.png";
		dest = filePath + "/res/drawable/ic_launcher.png";
		
		copyFile(source, dest);
		
		
	}
	
	public void copyFile(String source, String dest){
		File sourceFile = new File(source);
		
		File targetFile = new File(dest);
		try {
			FileUtils.copyFile(sourceFile, targetFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.print("File copy failed");
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
