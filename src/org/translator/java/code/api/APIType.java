package org.translator.java.code.api;

import org.translator.java.code.Value;
import org.translator.java.code.api.util.APIUtil;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Joshua
 */
public class APIType
{
    private String name, defaultValue = "null", literalGenusPattern = null, literalFormatString;

    public APIType( Node type )
    {
        NamedNodeMap fields = type.getAttributes();

        this.name = APIUtil.getField( fields, "name" );

        String defaultValue = APIUtil.getField( fields, "default" );
        if( !defaultValue.isEmpty() )
            this.defaultValue = defaultValue;

        String literalGenusPattern = APIUtil.getField( fields, "literalGenusPattern" );
        if( !literalGenusPattern.isEmpty() )
            this.literalGenusPattern = literalGenusPattern;

        this.literalFormatString = APIUtil.getField( fields, "literalFormatString" );
    }

    public String getDefaultValue()
    {
        return new String( defaultValue );
    }

    public String getName()
    {
        return new String( name );
    }

    public boolean isLiteral()
    {
        return ( literalGenusPattern != null );
    }

    public String getLiteralGenusPattern()
    {
        return new String( literalGenusPattern );
    }

    public Value getValue( String label )
    {
        return new Value( String.format( literalFormatString, label ));
    }
}
