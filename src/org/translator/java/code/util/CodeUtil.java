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

package org.translator.java.code.util;

/**
 *
 * @author Joshua
 */
public abstract class CodeUtil
{
    public static String indent( String s )
    {
        return new StringBuilder( s.replaceAll( "\n", "\n\t" )).insert( 0, "\t" ).toString();
    }

    public static String lastIdentifier( String s )
    {
        if( s.contains( "." ))
            return s.substring( s.lastIndexOf( "." ) + 1 );
        else
            return s;
    }

    public static String classNameWithoutGeneric( String s )
    {
        return removeGeneric( lastIdentifier( s ));
    }

    public static String removeGeneric( String s )
    {
        if( s.contains( "<" ))
            return s.substring( 0, s.lastIndexOf( "<" ) );
        else
            return s;
    }

    public static String removeLastIdentifier( String s )
    {
        if( s.contains( "." ))
            return s.substring( 0, s.lastIndexOf( "." ));
        else
            return new String();
    }
}
