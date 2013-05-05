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

import org.w3c.dom.Node;

/**
 *
 * @author Joshua
 */
public abstract class ActionEntryFactory
{
    public static ActionEntry create( Node entry )
    {
        if( entry.getNodeName().equals( "Function" ))
            return new FunctionEntry( entry );
        else if( entry.getNodeName().equals( "Call" ))
            return new CallEntry( entry );
        else if( entry.getNodeName().equals( "Operation" ))
            return new OperationEntry( entry );
        else if( entry.getNodeName().equals( "Value" ))
            return new ValueEntry( entry );
        else if( entry.getNodeName().equals( "UtilityFunction" ))
            return new UtilityFunctionEntry( entry );
        
        return null;
    }
}
