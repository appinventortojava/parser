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
import java.util.SortedMap;
import java.util.TreeMap;
import org.translator.java.code.util.CodeUtil;

/**
 *
 * @author Joshua
 */
public class CodeSegment
{
    protected final ArrayList<CodeSegment> blocks = new ArrayList<CodeSegment>();

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for( int i = 0; i < blocks.size(); i++ )
        {
            if( i != 0 )
                builder.append( "\n" );
            builder.append( blocks.get( i ).toString() );
        }

        return builder.toString();
    }
    
    public void add( CodeSegment block )
    {
        if( block != null )
            blocks.add( block );
    }

    public boolean isEmpty()
    {
        return blocks.isEmpty();
    }

    protected SortedMap<String, String> getDependencies()
    {
        return buildDependencies( blocks.toArray( new CodeSegment[0] ));
    }

    protected void addDependency( SortedMap<String, String> dependencies, String className )
    {
        dependencies.put( CodeUtil.removeGeneric( className ), CodeUtil.classNameWithoutGeneric( className ));
    }

    protected SortedMap<String, String> buildDependencies( CodeSegment... segments )
    {
        TreeMap<String, String> dependencies = new TreeMap<String, String>();

        for( CodeSegment segment : segments )
            if( segment != null )
                dependencies.putAll( segment.getDependencies() );

        return dependencies;
    }
}
