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

package org.translator.java.code.api;

import java.util.LinkedList;
import org.translator.java.code.FunctionCall;
import org.translator.java.code.StaticFunctionCall;
import org.translator.java.code.Value;
import org.translator.java.code.api.util.APIUtil;
import org.translator.java.code.util.CodeUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Joshua
 */
public class FunctionEntry extends ActionEntry
{
    private String name;
    private boolean isStatic;

    public FunctionEntry( Node entry )
    {
        super( entry );

        NamedNodeMap fields = entry.getAttributes();

        name = APIUtil.getField( fields, "name" );
        isStatic = Boolean.valueOf( APIUtil.getField( fields, "static" ));
    }

    public FunctionEntry( String name, boolean isStatic )
    {
        this.name = new String( name );
        this.isStatic = isStatic;
    }

    public FunctionEntry( String name, boolean isStatic, int nParameters )
    {
        super( nParameters );
        this.name = new String( name );
        this.isStatic = isStatic;
    }

    public Value buildCode( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        if( !isStatic )
            return new FunctionCall( target, name, params );
        else
        {
            String className = null;
            String functionName = name;

            className = CodeUtil.removeLastIdentifier( name );
            functionName = CodeUtil.lastIdentifier( name );

            if( !className.isEmpty() )
                return new StaticFunctionCall( className, functionName, params );
            else
                return new FunctionCall( functionName, params );
        }
    }
}