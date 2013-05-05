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
import org.translator.java.code.api.util.APIUtil;
import java.util.LinkedList;
import org.translator.java.code.Value;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Joshua
 */
public class APIEntry
{
    private String genus, type = "java.lang.Object", targetType = "java.lang.Object";
    private final ArrayList<String> parameterTypes = new ArrayList<String>();
    private int minParams = -1, maxParams = -1;
    private boolean isStatic = false;
    private ActionEntry action;

    public APIEntry( APIMapping mapping, Node entry )
    {
        NamedNodeMap fields = entry.getAttributes();

        this.genus = APIUtil.getField( fields, "genus" );

        String nParams = APIUtil.getField( fields, "nParams" );
        if( !nParams.isEmpty() )
            this.minParams = this.maxParams = Integer.valueOf( nParams );
        else
        {
            String max = APIUtil.getField( fields, "maxParams" );
            String min = APIUtil.getField( fields, "minParams" );

            if( !max.isEmpty() )
                this.maxParams = Integer.valueOf( max );
            if( !min.isEmpty() )
                this.minParams = Integer.valueOf( min );
        }

        String stat = APIUtil.getField( fields, "static" );
        if( !stat.isEmpty() )
            this.isStatic = Boolean.valueOf( stat );

        String type = mapping.getFullType( APIUtil.getField( fields, "type" ));
        if( !type.isEmpty() )
            this.type = type;

        String targetType = mapping.getFullType( APIUtil.getField( fields, "target" ));
        if( !targetType.isEmpty() )
            this.targetType = targetType;

        String simpleFunction = APIUtil.getField( fields, "simpleFunction" );

        if( !simpleFunction.isEmpty() )
            action = new FunctionEntry( simpleFunction, isStatic );
        else
            loadAction( entry );

        loadParameterTypes( mapping, entry );
    }

    public String getGenus()
    {
        return new String( genus );
    }

    public String toString()
    {
        return getGenus();
    }

    public boolean isStatic()
    {
        return isStatic;
    }

    @SuppressWarnings("unchecked")
	public Value generateCode( APIMapping mapping, LinkedList<Value> p )
    {
        LinkedList<Value> params = (LinkedList<Value>)p.clone();
        Value target = isStatic?null:params.removeFirst();
        target = setDefaults( mapping, target, params );

        return generateCode( mapping, target, params );
    }

    public String getParameterType( int index )
    {
        int size = parameterTypes.size();

        if( index < size )
            return new String( parameterTypes.get( index ));
        else if( size >= 1 )
            return new String( parameterTypes.get( size - 1 ));
        else
            return "java.lang.Object";
    }

    public String getType()
    {
        return new String( type );
    }

    public String getTargetType()
    {
        return new String( targetType );
    }

    protected Value generateCode( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        return action.generateCode( mapping, target, params );
    }

    @SuppressWarnings("unchecked")
	protected boolean matches( LinkedList<Value> params )
    {
        LinkedList<Value> p = (LinkedList<Value>)params.clone();
        Value target = null;

        if( !isStatic )
            target = p.removeFirst();

        return matches( target, p );
    }

    protected boolean matches( Value target, LinkedList<Value> params )
    {
        int size = params.size();

        if( minParams >= 0 )
            if( size < minParams )
                return false;

        if( maxParams >= 0 )
            if( size > maxParams )
                return false;

        return true;
    }

    private void loadAction( Node entry )
    {
        NodeList children = entry.getChildNodes();

        for( int i = 0; i < children.getLength(); i++ )
        {
            ActionEntry e = ActionEntryFactory.create( children.item( i ));

            if( e != null )
            {
                action = e;
                return;
            }
        }
    }

    private void loadParameterTypes( APIMapping mapping, Node entry )
    {
        NodeList children = entry.getChildNodes();

        for( int i = 0; i < children.getLength(); i++ )
        {
            Node n = children.item( i );
            String name = n.getNodeName();
            if( name.equals( "Parameter" ))
            {
                String type = mapping.getFullType( APIUtil.getField( n.getAttributes(), "type" ));
                parameterTypes.add( (type.isEmpty())?"java.lang.Object":type );
            } else if( name.equals( "Parameters" )) {
                String type = mapping.getFullType( APIUtil.getField( n.getAttributes(), "type" ));
                String start = APIUtil.getField( n.getAttributes(), "start" );
                String end = APIUtil.getField( n.getAttributes(), "end" );

                int min = (start.isEmpty())?-1:Integer.valueOf( start );
                int max = (end.isEmpty())?-1:Integer.valueOf( end );

                loadParameters( min, max, type );

                if( max < 0 )
                    break;
            }
        }

        if( parameterTypes.size() == 0 )
            parameterTypes.add( "java.lang.Object" );
    }

    private void loadParameters( int start, int end, String type )
    {
        if( start >= 0 )
        {
            while( start < parameterTypes.size() )
                parameterTypes.add( "java.lang.Object" );

            if( start != parameterTypes.size() )
                throw new APIMappingException( "Invalid parameter start position" );
        }

        if( end >= 0 )
            for( int i = parameterTypes.size(); i < end; i++ )
                parameterTypes.add( type );
        else
            parameterTypes.add( type );
    }

    private Value setDefaults( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        if( target != null )
            if( target.isNull() )
                target = mapping.getDefaultValue( getTargetType() );

        for( int i = 0; i < params.size(); i++ )
            if( params.get( i ).isNull() )
            {
                String type = getParameterType( i );
                params.set( i, mapping.getDefaultValue( type ));
            }

        return target;
    }
}