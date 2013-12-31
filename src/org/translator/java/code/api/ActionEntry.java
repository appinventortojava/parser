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
import org.translator.java.code.Value;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Joshua
 */
public abstract class ActionEntry
{
    private TargetReference target = null;
    private final ParameterReferenceList parameters = new ParameterReferenceList();

    protected ActionEntry( Node entry )
    {
        NodeList children = entry.getChildNodes();

        TargetReference possibleTarget = new TargetReference( entry );
        if( possibleTarget != null )
            target = possibleTarget;

        for( int i = 0; i < children.getLength(); i++ )
        {
            Node child = children.item( i );

            if( child.getNodeName().equals( "Parameter" ) || child.getNodeName().equals( "Parameters" ))
                parameters.add( child );
        }
    }

    protected ActionEntry()
    {
        parameters.add( new ParameterReference() );
    }

    protected ActionEntry( int nParameters )
    {
        if( nParameters != 0 )
            parameters.add( new ParameterReference( nParameters ) );
    }

    protected ActionEntry( TargetReference target, ParameterReferenceList parameters )
    {
        this.target = target;
        this.parameters.addAll( parameters );
    }

    public final Value generateCode( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        Value t = getTarget( mapping, target, params );

        LinkedList<Value> p = getParameters( mapping, target, params );

        return buildCode( mapping, t, p );
    }

    private LinkedList<Value> getParameters( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        return parameters.getParameters( mapping, target, params );
    }

    private Value getTarget( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        if( this.target == null )
            return target;
        else
            return this.target.getTarget( mapping, target, params );
    }

    protected abstract Value buildCode( APIMapping mapping, Value target, LinkedList<Value> params );
}
