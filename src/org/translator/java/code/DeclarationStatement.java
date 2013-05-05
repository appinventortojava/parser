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
import java.util.SortedMap;

/**
 *
 * @author Joshua
 */
public class DeclarationStatement extends Statement
{
    private String type, identifier;
    private CodeVisibility visibility = CodeVisibility.DEFAULT;
    private Value value = null;

    public DeclarationStatement( String type, String identifier )
    {
        this.type = type;
        this.identifier = identifier;
    }

    public DeclarationStatement( String type, String identifier, Value value )
    {
        this.type = type;
        this.identifier = identifier;
        this.value = value;
    }

    public DeclarationStatement( String type, String identifier, CodeVisibility visibility )
    {
        this.type = type;
        this.identifier = identifier;
        this.visibility = visibility;
    }

    public DeclarationStatement( String type, String identifier, CodeVisibility visibility, Value value )
    {
        this.type = type;
        this.identifier = identifier;
        this.visibility = visibility;
        this.value = value;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append( String.format( "%s%s %s", visibility.toString(), CodeUtil.lastIdentifier( type ), identifier ));

        if( value != null )
            builder.append( String.format( " = %s", value.toString() ));

        builder.append( ";" );
        return builder.toString();
    }

    protected SortedMap<String, String> getDependencies()
    {
        SortedMap<String, String> dependencies = buildDependencies( value );
        
        // really hacky but the float shit is not legit! 
        if (type.endsWith("float")) 
        	return dependencies;
        addDependency( dependencies, type );

        return dependencies;
    }
}
