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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author jswank
 * 
 * @edited by Aaron & Ramya & msilvestri
 */
public class AppInventorProject {
    private final HashMap<String, AppInventorScreen> screens = new HashMap<String, AppInventorScreen>();
	EclipseFileGen eclipseGen;
	private int mode = 0;
	private String filepath;

    public AppInventorProject( File inputFile, int mode ) throws IOException
	{
		this.mode = mode;
		if (mode == 1) {
			filepath = inputFile.getParent();
			eclipseGen = new EclipseFileGen(filepath);
			load(inputFile);

			eclipseGen.build();
			//create the rest of the eclipse files       	
		}
		else{
			filepath = inputFile.getParent();
			load( inputFile );
		}
	}

	public static void main(String [] args) {
		File inputFile = new File(args[0]); 
		int mode = 0;
		if (args[1].toLowerCase().compareTo("eclipse") == 0)
			mode = 1;
		// else only export source files 
		try {
			AppInventorProject project = new AppInventorProject(inputFile, mode);
			String projectName = project.screens.entrySet().iterator().next().getValue().getPackageName();
			String[] name = projectName.split("\\.");
			System.out.println(name[name.length-1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void load( File inputFile ) throws IOException
	{
		InputStream inputStream = null;

		// if file is a directory load all yail files inside of it
		if (inputFile.isDirectory()){
			File allFiles[] = inputFile.listFiles();
			for (File file : allFiles){
				inputStream = new FileInputStream(file);
				load(inputStream, file.getName(), file.getAbsolutePath());
				inputStream.close();
			}
		}

		// else just load the single file
		else { 
			inputStream=new FileInputStream(inputFile);
			load(inputStream,inputFile.getName(), inputFile.getAbsolutePath());
			inputStream.close();
		}
		
		generateSource();
	}

	private void load( InputStream inputStream, String myname, String absPath ) throws IOException
	{
		if (myname.endsWith(".yail") || myname.endsWith(".java")){
			loadSourceFile( myname, inputStream, absPath );
			return;
		}
	}

   
	private void loadSourceFile( String path, InputStream inputStream, String absPath ) throws IOException
	{
		//screens hash map will always have only one key/value pair, because he uses the same
		//key for each file, which will always result in the previous value being overwritten.
		//practically, this means the last file passed to this method will be the only one
		//with a corresponding AppInventorScreens object in the HashMap
		//projectName = getFolder( path );
		//String screenName = getScreenName( path );
		AppInventorScreen screen = new AppInventorScreen( path.substring(0,path.length()-5 ), inputStream, absPath);
		if (path.endsWith(".yail") || path.endsWith(".java")){
			screen.loadYailFile();
			screens.put( path.substring(0,path.length()-5 ), screen );
			if (mode == 1)
			{
				screen.genManifest(eclipseGen.screenNames, eclipseGen.permissions);
				eclipseGen.setPackageName(screen.getPackageName());
			}

		}
	}

	private void generateSource()
	{
		String filedir = filepath;
		if (mode == 1){
			filedir += "/src";


			File dir = new File(filedir);
			dir.mkdir();
			filedir += "/org";

			dir = new File(filedir);
			dir.mkdir();

			String packageName = screens.values().iterator().next().getPackageName();
			String tmp[] = packageName.split("\\.");
			//Bad use of memory thank you garbage collection!
			for (String s : tmp){
				filedir += "/" + s;
				dir = new File(filedir);
				dir.mkdir();
			}
		}
		for( AppInventorScreen screen : screens.values() ){
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(filedir+"/"+screen.getName()+".java"));
				writer.append(screen.generateJavaFile().toString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
