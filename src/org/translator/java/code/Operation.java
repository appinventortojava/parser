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
import java.util.Collection;
import java.util.SortedMap;

/**
 *
 * @author Joshua
 */
public class Operation extends Value
{
    private OperationType type;
    private final ArrayList<Value> params = new ArrayList<Value>();

    public Operation( String type, String style, Value... params )
    {
        this.type = OperationType.fromString( type, style );
        
        for( Value v : params )
            this.params.add( v );
    }

    public Operation( String type, String style, Collection<Value> params )
    {
        this.type = OperationType.fromString( type, style );
        this.params.addAll( params );
    }

    public Operation( String type, Value... params )
    {
        this.type = OperationType.fromString( type );

        for( Value v : params )
            this.params.add( v );
    }

    public Operation( String type, Collection<Value> params )
    {
        this.type = OperationType.fromString( type );
        this.params.addAll( params );
    }

    protected SortedMap<String, String> getDependencies()
    {
        return buildDependencies( params.toArray( new CodeSegment[0] ));
    }

    public String toString()
    {
        return type.format( params );
    }

    public enum OperationType
    {
        ASSIGN( "=", OperationStyle.INFIX, 0, true ),
        ASSIGN_SUM( "+=", OperationStyle.INFIX, 0, true ),
        ASSIGN_DIFFERENCE( "-=", OperationStyle.INFIX, 0, true ),
        ASSIGN_PRODUCT( "*=", OperationStyle.INFIX, 0, true ),
        ASSIGN_QUOTIENT( "/=", OperationStyle.INFIX, 0, true ),
        ASSIGN_MOD( "%=", OperationStyle.INFIX, 0, true ),
        ASSIGN_AND( "&=", OperationStyle.INFIX, 0, true ),
        ASSIGN_OR( "|=", OperationStyle.INFIX, 0, true ),
        ASSIGN_XOR( "^=", OperationStyle.INFIX, 0, true ),
        ASSIGN_SHIFT_LEFT( "<<=", OperationStyle.INFIX, 0, true ),
        ASSIGN_SHIFT_RIGHT( ">>=", OperationStyle.INFIX, 0, true ),
        ASSIGN_SHIFT_RIGHT_UNSIGNED( ">>>=", OperationStyle.INFIX, 0, true ),
        TERNARY( "?:", OperationStyle.TERNARY, 1 ),
        LOGICAL_OR( "||", OperationStyle.INFIX, 2 ),
        LOGICAL_AND( "&&", OperationStyle.INFIX, 3 ),
        BITWISE_OR( "|", OperationStyle.INFIX, 4 ),
        BITWISE_XOR( "^", OperationStyle.INFIX, 5 ),
        BITWISE_AND( "&", OperationStyle.INFIX, 6 ),
        EQUAL_TO( "==", OperationStyle.INFIX, 7 ),
        NOT_EQUAL_TO( "!=", OperationStyle.INFIX, 7 ),
        LESS_THAN( "<", OperationStyle.INFIX, 8 ),
        GREATER_THAN( ">", OperationStyle.INFIX, 8 ),
        LESS_THAN_EQUAL_TO( "<=", OperationStyle.INFIX, 8 ),
        GREATER_THAN_EQUAL_TO( ">=", OperationStyle.INFIX, 8 ),
        INSTANCE_OF( "instanceof", OperationStyle.INFIX, 8 ),
        SHIFT_LEFT( "<<", OperationStyle.INFIX, 9 ),
        SHIFT_RIGHT( ">>", OperationStyle.INFIX, 9 ),
        SHIFT_RIGHT_UNSIGNED( ">>>", OperationStyle.INFIX, 9 ),
        SUM( "+", OperationStyle.INFIX, 10 ),
        DIFFERENCE( "-", OperationStyle.INFIX, 10 ),
        PRODUCT( "*", OperationStyle.INFIX, 11 ),
        QUOTIENT( "/", OperationStyle.INFIX, 11 ),
        MOD( "%", OperationStyle.INFIX, 11 ),
        INCREMENT_UNARY( "++", OperationStyle.UNARY, 12 ),
        DECREMENT_UNARY( "--", OperationStyle.UNARY, 12 ),
        POSITIVE( "+", OperationStyle.UNARY, 12 ),
        NEGATIVE( "-", OperationStyle.UNARY, 12 ),
        BITWISE_NOT( "~", OperationStyle.UNARY, 12 ),
        LOGICAL_NOT( "!", OperationStyle.UNARY, 12 ),
        INCREMENT_POSTFIX( "++", OperationStyle.POSTFIX, 13 ),
        DECREMENT_POSTFIX( "--", OperationStyle.POSTFIX, 13 ),
        PARENTHESES( "()", OperationStyle.SURROUND, 14 );

        private String value;
        private OperationStyle style;
        private int precedence;
        private boolean rightToLeft = false;

        private OperationType( String value, OperationStyle style, int precedence )
        {
            this.value = value.toLowerCase();
            this.style = style;
            this.precedence = precedence;
        }

        private OperationType( String value, OperationStyle style, int precedence, boolean rightToLeft )
        {
            this.value = value.toLowerCase();
            this.style = style;
            this.precedence = precedence;
            this.setRightToLeft(rightToLeft);
        }

        public static OperationType fromString( String operator, String style )
        {
            OperationStyle s = OperationStyle.fromString( style );

            for( OperationType t : OperationType.values() )
                if( t.value.equals( operator.toLowerCase() ) && t.style.equals( s ))
                    return t;

            return null;
        }

        public static OperationType fromString( String operator )
        {
            for( OperationType t : OperationType.values() )
                if( t.value.equals( operator.toLowerCase() ))
                    return t;

            return null;
        }

        public int getPrecedence()
        {
            return precedence;
        }

        public String format( Value... params )
        {
            return style.format( value, params );
        }

        public String format( Collection<Value> params )
        {
            return format( params.toArray( new Value[0] ));
        }

        public boolean isRightToLeft() {
			return rightToLeft;
		}

		public void setRightToLeft(boolean rightToLeft) {
			this.rightToLeft = rightToLeft;
		}

		private enum OperationStyle
        {
            INFIX( 2, "infix" ), UNARY( 1, "unary" ), POSTFIX( 1, "postfix" ), TERNARY( 3, "ternary" ), SURROUND( 1, "surround" );

            private int nParams;
            private String name;

            private OperationStyle( int nParams, String name )
            {
                this.nParams = nParams;
                this.name = name.toLowerCase();
            }

            String format( String operator, Value... params )
            {
                String s = null;

                if( params.length >= nParams )
                    switch( this )
                    {
                        case UNARY:
                            s = String.format( "%s%s", operator, params[0] );
                        break;

                        case INFIX:
                            s = String.format( "%s %s %s", params[0], operator, params[1] );
                        break;

                        case POSTFIX:
                            s = String.format( "%s%s", params[0], operator );
                        break;

                        case TERNARY:
                            s = String.format( "%s%c%s%c%s", params[0], operator.charAt( 0 ), params[1], operator.charAt( 1 ), params[2] );
                        break;

                        case SURROUND:
                            s = String.format( "%c%s%c", operator.charAt( 0 ), params[0], operator.charAt( 1 ));
                        break;
                    }

                    //TODO: Make it only insert parentheses as needed
                return String.format( "(%s)", s );
            }

            @SuppressWarnings("unused")
			String format( String operation, Collection<Value> params )
            {
                return format( operation, params.toArray( new Value[0] ));
            }

            static OperationStyle fromString( String style )
            {
                for( OperationStyle s : OperationStyle.values() )
                    if( s.name.equals( style.toLowerCase() ))
                        return s;

                return null;
            }
        }
    }
}
