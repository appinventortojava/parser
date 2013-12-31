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

package org.translator.java.code;

/**
 *
 * @author Joshua
 */
public class SourceFile
{
    private String pkg;
    private ClassSegment mainClass;

    public SourceFile()
    {
        pkg = "";
        mainClass = null;
    }

    public SourceFile( String pkg )
    {
        this.pkg = pkg;
        mainClass = null;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        if( !pkg.equals( "" ))
            builder.append( String.format("package %s;\n\n", pkg ));

        builder.append( importString() );

        if( mainClass != null )
            builder.append( mainClass.toString() );

        return builder.toString();
    }

    public void setMainClass( ClassSegment mainClass )
    {
        this.mainClass = mainClass;
    }

    private static String getPackage( String s )
    {
        if( s.contains( "." ))
            return s.substring( 0, s.lastIndexOf( "." ));
        else
            return "";
    }

    private String importString()
    {
        StringBuilder builder = new StringBuilder();
    
        if( mainClass != null )
            for( String dep : mainClass.getDependencies().keySet() )
            {
                String depPackage = getPackage( dep );
                if( !depPackage.isEmpty() && !depPackage.equals( pkg ) && !depPackage.equals( "java.lang" )){
                	if (!dep.endsWith("int") && !dep.endsWith("String") && !dep.endsWith("YailList") && !dep.endsWith("ArrayList") &&!dep.endsWith("Random")){
                		builder.append( String.format( "import %s;\n", dep ));
                	}
                	else {
                		if (dep.endsWith("String")){
                			builder.append("import java.lang.String;\n");
                		}
                		else if (dep.endsWith("YailList")){
                			builder.append("import com.google.devtools.simple.runtime.components.util.YailList;\n");
                			 builder.append("import com.google.devtools.simple.runtime.components.android.util.CsvUtil;\n");
                			builder.append("import java.util.ArrayList;\n");
                		}
                		else if (dep.endsWith("Random")){
                			builder.append("import java.util.Random;\n");
                		}
                	}
                }
            }
       

        builder.append("import com.google.devtools.simple.runtime.components.Component;\n");
        if( builder.length() > 0 )
            builder.append( "\n" );

        return builder.toString();
    }
}
