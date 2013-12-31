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
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author Joshua
 */
public class ClassSegment extends CodeSegment
{
    private String identifier, parent = null;
    private final ArrayList<String> interfaces = new ArrayList<String>();
    private CodeVisibility visibility = CodeVisibility.DEFAULT;
    //private boolean isEnum = false; //TODO: Add support for this

    private final TreeMap<String, String> dependencies = new TreeMap<String, String>();

    public ClassSegment( String identifier )
    {
        this.identifier = identifier;
    }

    public ClassSegment( String identifier, CodeVisibility visibility )
    {
        this.identifier = identifier;
        this.visibility = visibility;
    }

    public ClassSegment( String identifier, CodeVisibility visibility, String parent )
    {
        this.identifier = identifier;
        this.visibility = visibility;
        setParent( parent );
    }
    
    public ClassSegment( String identifier, CodeVisibility visibility, Collection<String> interfaces )
    {
        this.identifier = identifier;
        this.visibility = visibility;
        setInterfaces( interfaces );
    }

    public ClassSegment( String identifier, CodeVisibility visibility, String[] interfaces )
    {
        this.identifier = identifier;
        this.visibility = visibility;
        setInterfaces( interfaces );
    }

    public ClassSegment( String identifier, CodeVisibility visibility, String parent, String[] interfaces )
    {
        this.identifier = identifier;
        this.visibility = visibility;
        setParent( parent );
        setInterfaces( interfaces );
    }

    public ClassSegment( String identifier, CodeVisibility visibility, String parent, Collection<String> interfaces )
    {
        this.identifier = identifier;
        this.visibility = visibility;
        setParent( parent );
        setInterfaces( interfaces );
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append( String.format( "%sclass %s", visibility, identifier ));

        if( parent != null )
            builder.append( String.format( " extends %s", CodeUtil.lastIdentifier( parent )));

        if( !interfaces.isEmpty() )
        {
            builder.append( " implements " );
            for( int i = 0; i < interfaces.size(); i++ )
            {
                if( i != 0 )
                    builder.append( ", " );
                builder.append( CodeUtil.lastIdentifier( interfaces.get( i )));
            }
        }

        builder.append( "\n{" );

        for( int i = 0; i < blocks.size(); i++ )
        {
            builder.append( "\n" );

            builder.append( CodeUtil.indent( blocks.get( i ).toString().replaceAll( String.format( "\\b%s([.]{0,1})", identifier ), "this$1" )));
        }

        builder.append( "\n}" );

        return builder.toString();
    }

    protected SortedMap<String, String> getDependencies()
    {
        @SuppressWarnings("unchecked")
		SortedMap<String, String> dependenciesCopy = (SortedMap<String, String>)dependencies.clone();

        for( CodeSegment block : blocks )
            dependenciesCopy.putAll( block.getDependencies() );

        return dependenciesCopy;
    }

    private void setParent( String parent )
    {
        this.parent = parent;
        dependencies.put( CodeUtil.removeGeneric( parent ), CodeUtil.removeGeneric( CodeUtil.lastIdentifier( parent )));
    }

    private void setInterfaces( String[] newInterfaces )
    {
        for( String s : newInterfaces )
        {
            interfaces.add( s );
            dependencies.put( CodeUtil.removeGeneric( s ), CodeUtil.removeGeneric( CodeUtil.lastIdentifier( s )));
        }
    }

    private void setInterfaces( Collection<String> newInterfaces )
    {
        setInterfaces( (String[])newInterfaces.toArray() );
    }
}
