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

import org.translator.java.code.util.CodeUtil;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Joshua
 */
public class FunctionSegment extends CodeSegment
{
    private String identifier, returnType = "void";
    private CodeVisibility visibility = CodeVisibility.DEFAULT;
    private final ArrayList<Parameter> parameters = new ArrayList<Parameter>();

    private final TreeMap<String, String> dependencies = new TreeMap<String, String>();

    public FunctionSegment( String identifier )
    {
        this.identifier = identifier;
    }

    public FunctionSegment( String identifier, Parameter... params )
    {
        this.identifier = identifier;
        setParameters( params );
    }

    public FunctionSegment( String identifier, String returnType )
    {
        this.identifier = identifier;
        setReturnType( returnType );
    }

    public FunctionSegment( String identifier, CodeVisibility visibility, String returnType )
    {
        this.identifier = identifier;
        setReturnType( returnType );
        this.visibility = visibility;
    }

    public FunctionSegment( String identifier, CodeVisibility visibility, String returnType, Parameter... params )
    {
        this.identifier = identifier;
        setReturnType( returnType );
        this.visibility = visibility;
        setParameters( params );
    }

    public SortedMap<String, String> getDependencies()
    {
        return buildDependencies( blocks.toArray( new CodeSegment[0] ));
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append( String.format("%s%s %s%s\n{", visibility.toString(), CodeUtil.lastIdentifier( returnType ), identifier, parameterString() ));

        for( int i = 0; i < blocks.size(); i++ )
        {
            builder.append( "\n" );
            builder.append( CodeUtil.indent( blocks.get( i ).toString() ));
        }

        builder.append( "\n}" );
        return builder.toString();
    }

    private String parameterString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append( "(" );

        for( int i = 0; i < parameters.size(); i++ )
        {
            if( i != 0 )
                builder.append( ", " );
            else
                builder.append( " " );

            builder.append( String.format( "%s %s", CodeUtil.lastIdentifier( parameters.get( i ).getType() ), parameters.get( i ).getIdentifier() ));
        }

        if( parameters.size() > 0 )
            builder.append( " " );
        builder.append( ")" );

        return builder.toString();
    }

    private void setReturnType( String s )
    {
        returnType = s;
        dependencies.put( CodeUtil.removeGeneric( s ), CodeUtil.removeGeneric( CodeUtil.lastIdentifier( s )));
    }

    private void setParameters( Parameter[] params )
    {
        for( Parameter p : params )
        {
            dependencies.put( CodeUtil.removeGeneric( p.getType() ), CodeUtil.removeGeneric( CodeUtil.lastIdentifier( p.getType() )));
            parameters.add( p );
        }
    }
}
