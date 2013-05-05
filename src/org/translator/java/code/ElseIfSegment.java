package org.translator.java.code;


import org.translator.java.code.util.CodeUtil;

/**
 *
 * @author Joshua
 */
public class ElseIfSegment extends CodeSegment
{
    private Value condition;
    private CodeSegment thendo=null;
    private CodeSegment elseThen=null;
    public ElseIfSegment( Value condition )
    {
        this.condition = condition;
    }

    public void setThendo(CodeSegment thendo){
    	this.thendo=thendo;
    }
    public void setElse( CodeSegment elseThen )
    {
        this.elseThen = elseThen;
    }
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append( String.format( "else if( %s )\n{", condition.toString() ));

        for( int i = 0; i < blocks.size(); i++ )
        {
            builder.append( "\n" );

            if( blocks.get( i ) instanceof FunctionSegment && i != 0 )
                builder.append( "\n" );

            builder.append( CodeUtil.indent( blocks.get( i ).toString() ));
        }
        if (thendo!=null){
        	builder.append(CodeUtil.indent(thendo.toString()));
        }
        builder.append( "\n}" );
        if( elseThen != null )
        {
            builder.append( " else {\n" );
            builder.append( CodeUtil.indent( elseThen.toString() ));
            builder.append( "\n}" );
        }

        return builder.toString();
    }
}