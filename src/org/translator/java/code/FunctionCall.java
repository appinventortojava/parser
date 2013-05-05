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

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 *
 * @author Joshua
 */
public class FunctionCall extends Value
{
    private Value target = null;
    private String identifier;
    private final ArrayList<Value> parameters = new ArrayList<Value>();

    public FunctionCall( String identifier )
    {
        this.identifier = new String( identifier );
    }

    public FunctionCall( Value target, String identifier )
    {
        this.target = target;
        this.identifier = new String( identifier );
    }

    public FunctionCall( String identifier, Value... params )
    {
        this.identifier = identifier;

        for( Value v : params )
            parameters.add( v );
    }

    public FunctionCall( Value target, String identifier, Value... params )
    {
        this.target = target;
        this.identifier = new String( identifier );

        for( Value v : params )
            parameters.add( v );
    }

    public FunctionCall( Value target, String identifier, Collection<Value> params )
    {
        this.target = target;
        this.identifier = new String( identifier );

        for( Value v : params )
            parameters.add( v );
    }

    public FunctionCall( String identifier, Collection<Value> params )
    {
        this.identifier = identifier;

        for( Value v : params )
            parameters.add( v );
    }

    public String toString()
    {
        String call = identifier.concat( parameterString() );
        
        if( target == null ){
            return call;
        }
        else {
            return String.format( "%s.%s", target, call );
        }
    }

    protected SortedMap<String, String> getDependencies()
    {
        TreeMap<String, String> dependencies = new TreeMap<String, String>();

        if( target != null )
            dependencies.putAll( target.getDependencies() );
        for( Value v : parameters )
            dependencies.putAll( v.getDependencies() );

        return dependencies;
    }

    private String parameterString()
    {
        StringBuilder builder = new StringBuilder( "(" );

        for( int i = 0; i < parameters.size(); i++ )
        {
            if( i > 0 )
                builder.append( ", " );
            else
                builder.append( " " );

            String param = parameters.get(i).toString();
            if (identifier.equals("Text") && !param.contains("\"")) {
	            builder.append( "(" );
	            builder.append( param );
	            builder.append( ").toString()" );
            } else if (identifier.equals("Height") || identifier.equals("Width")) {
	            builder.append( "Integer.valueOf(" );
	            builder.append( param );
	            builder.append( ")" );
            }
        	else {
	            builder.append( param );
            }
        }

        if( parameters.size() > 0 )
            builder.append( " " );

        builder.append( ")" );
        return builder.toString();
    }
}
