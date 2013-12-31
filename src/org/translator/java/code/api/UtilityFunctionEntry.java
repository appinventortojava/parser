package org.translator.java.code.api;

import org.translator.java.TranslatorConstants;
import org.translator.java.code.api.util.APIUtil;
import org.w3c.dom.Node;

/**
 *
 * @author Joshua
 */
public class UtilityFunctionEntry extends FunctionEntry
{
    public UtilityFunctionEntry( Node entry )
    {
        super( String.format( "%s.%s", TranslatorConstants.getUtilityClass(), APIUtil.getField( entry.getAttributes(), "name" )), true,
                Integer.valueOf( APIUtil.getField( entry.getAttributes(), "nParams" )));
    }
}
