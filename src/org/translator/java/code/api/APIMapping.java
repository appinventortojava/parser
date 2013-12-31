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

import java.io.InputStream;

import java.util.ArrayList;
import org.translator.java.TranslatorConstants;
import java.util.HashMap;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilderFactory;
import org.translator.java.code.Value;
import org.translator.java.code.util.CodeUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Joshua
 */
public class APIMapping extends HashMap<String, ArrayList<APIEntry>>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final HashMap<String, APIType> defaultValues = new HashMap<String, APIType>();
    private final HashMap<String, String> knownTypes = new HashMap<String, String>();
    private final HashMap<String, APIType> knownLiterals = new HashMap<String, APIType>();
    private String literalGenusPattern = "";

    public APIMapping()
    {
        load( TranslatorConstants.API_MAPPING_PATH );
    }

    public Value generateCode( String genus, LinkedList<Value> params )
    {
        APIEntry entry = findMatchingEntry( genus, params );

        if( entry != null )
            return entry.generateCode( this, params );
        else
            return new Value();
    }

    public Value generateCode( String genus, Value target, LinkedList<Value> params )
    {
        APIEntry entry = findMatchingEntry( genus, target, params );

        if( entry != null )
            return entry.generateCode( this, target, params );
        else
            return new Value();
    }

    public String getLiteralGenusPattern()
    {
        return new String( literalGenusPattern );
    }

    public Value getDefaultValue( String type )
    {
        if( defaultValues.containsKey( type ))
            return new Value( defaultValues.get( type ).getDefaultValue() );
        else
            return new Value( "null" );
    }

    public String getFullType( String simple )
    {
        if( knownTypes.containsKey( simple ))
            return knownTypes.get( simple );
        else
            return simple;
    }

    public APIType getMatchingLiteral( String genus )
    {
        for( String pattern : knownLiterals.keySet() )
            if( genus.matches( pattern ))
                return knownLiterals.get( pattern );

        throw new APIMappingException( String.format( "No literal pattern matches genus \"%s\"", genus ));
    }

    public boolean isStatic( String genus, Value target, LinkedList<Value> params )
    {
        return findMatchingEntry( genus, target, params ).isStatic();
    }

    private void load( String fileName )
    {
        try
        {
						String path = this.getClass().getResource(fileName).toString();
						InputStream is = this.getClass().getResourceAsStream(fileName);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse( is, path );

            NodeList nodes = doc.getDocumentElement().getChildNodes();

            for( int i = 0; i < nodes.getLength(); i++ )
                addEntry( nodes.item( i ));
        } catch( Exception e ) {
            System.err.println( "Error reading API mapping: ".concat( e.toString() ));
            e.printStackTrace();
        }
    }

    private void addEntry( String genus, APIEntry entry )
    {
        if( containsKey( genus ))
            get( genus ).add( entry );
        else
        {
            ArrayList<APIEntry> list = new ArrayList<APIEntry>();
            list.add( entry );
            put( genus, list );
        }
    }

    private void addEntry( Node n )
    {
        if( n.getNodeName().equals( "Entry" ))
        {
            APIEntry entry = new APIEntry( this, n );
            addEntry( entry.toString(), entry );
        } else if( n.getNodeName().equals( "Type" )) {
            APIType t = new APIType( n );
            defaultValues.put( t.getName(), t );
            knownTypes.put( CodeUtil.lastIdentifier( t.getName() ), t.getName() );

            if( t.isLiteral() )
            {
                knownLiterals.put( t.getLiteralGenusPattern(), t );
                addToLiteralGenusPattern( t.getLiteralGenusPattern() );
            }
        }
    }

    private void addToLiteralGenusPattern( String pattern )
    {
        if( literalGenusPattern.isEmpty() )
            literalGenusPattern = literalGenusPattern.concat( pattern );
        else
            literalGenusPattern = String.format( "%s|%s", literalGenusPattern, pattern );
    }

    private APIEntry findMatchingEntry( String genus, Value target, LinkedList<Value> params )
    {
        ArrayList<APIEntry> entries = get( genus );

        if( entries != null )
            for( APIEntry entry : entries )
                if( entry.matches( target, params ))
                    return entry;

       return null;
    }

    private APIEntry findMatchingEntry( String genus, LinkedList<Value> params )
    {
        ArrayList<APIEntry> entries = get( genus );

        if( entries != null )
            for( APIEntry entry : entries )
                if( entry.matches( params ))
                    return entry;

       return null;
    }
}
