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

import org.translator.java.code.util.CodeUtil;

/**
 *
 * @author Joshua
 */
public class IfSegment extends CodeSegment
{
    private Value condition;
    private CodeSegment elseThen = null;
    private ArrayList<ElseIfSegment> elseifs=new ArrayList<ElseIfSegment>();
    private CodeSegment thendo=null;
    public IfSegment( Value condition )
    {
        this.condition = condition;
    }

    public IfSegment( Value condition, CodeSegment elseThen )
    {
        this.condition = condition;
        this.elseThen = elseThen;
    }

    public void setElse( CodeSegment elseThen )
    {
        this.elseThen = elseThen;
    }
    public void setThendo(CodeSegment thendo){
    	this.thendo=thendo;
    }
    public void addElseIf(ElseIfSegment eis){
    	elseifs.add(eis);
    }
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        builder.append( String.format( "if( %s )\n{", condition.toString() ));

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
        for (ElseIfSegment eis : elseifs){
        	 builder.append(eis.toString());
        	 
        }
        
        if( elseThen != null )
        {
            builder.append( " else {\n" );
            builder.append( CodeUtil.indent( elseThen.toString() ));
            builder.append( "\n}" );
        }

        return builder.toString();
    }
}
