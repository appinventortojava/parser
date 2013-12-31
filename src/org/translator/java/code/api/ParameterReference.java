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

import java.util.ArrayList;
import java.util.LinkedList;
import org.translator.java.code.Value;
import org.translator.java.code.api.util.APIUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Joshua
 */
public class ParameterReference
{
    private int index = -1, start = -1, end = -1;
    private String value = null;
    private ArrayList<ActionEntry> entries = new ArrayList<ActionEntry>();
    private boolean isInterval = false;

    public ParameterReference( Node param )
    {
        if( param.getNodeName().equals( "Parameter" ))
            loadParameter( param );
        else if( param.getNodeName().equals( "Parameters" ))
            loadParameters( param );
    }

    protected ParameterReference( int nParameters )
    {
        isInterval = true;
        if( nParameters > 0 )
        {
            start = 0;
            end = nParameters - 1;
        }
    }

    protected ParameterReference()
    {
        isInterval = true;
    }

    public LinkedList<Value> getParameters( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        LinkedList<Value> parameters = new LinkedList<Value>();
        int size = params.size();
        int lowerBound = (start >= 0)?start:0;
        int upperBound = ((end >= 0 && end < size)?end:size - 1);
        
        if( isInterval )
            for( int i = lowerBound; i <= upperBound; i++ )
                parameters.add( params.get( i ));
        else if( index != -1 )
            parameters.add( params.get( index ));
        else if( value != null )
            parameters.add( new Value( value ));

        for( ActionEntry entry : entries )
            parameters.add( entry.generateCode( mapping, target, params ));

        return parameters;
    }

    private void loadParameter( Node param )
    {
        NamedNodeMap fields = param.getAttributes();
        String index = APIUtil.getField( fields, "index" );
        String v = APIUtil.getField( fields, "value" );

        if( !index.isEmpty() )
            this.index = Integer.valueOf( index );
        else if( !v.isEmpty() )
            this.value = APIUtil.getField( fields, "value" );
        else
            loadActions( param, false );
    }

    private void loadParameters( Node param )
    {
        NamedNodeMap fields = param.getAttributes();
        String min = APIUtil.getField( fields, "start" );
        if( !min.isEmpty() )
            start = Integer.valueOf( min );

        String max = APIUtil.getField( fields, "end" );
        if( !max.isEmpty() )
            end = Integer.valueOf( max );

        isInterval = true;
        loadActions( param, true );
    }

    private void loadActions( Node param, boolean multiple )
    {
        NodeList children = param.getChildNodes();

        for( int i = 0; i < children.getLength(); i++ )
        {
            ActionEntry entry = ActionEntryFactory.create( children.item( i ));
            if( entry != null )
            {
                entries.add( entry );
                if( !multiple )
                    break;
            }
        }
    }
}
