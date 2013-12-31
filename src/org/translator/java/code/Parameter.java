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

import java.util.SortedMap;

/**
 *
 * @author Joshua
 */
public class Parameter extends Value
{
    private String type, identifier;
    private int index;

    public Parameter( String type, String identifier )
    {
        this.type = type;
        this.identifier = identifier;

        setValue( String.format( "%s %s", type, identifier ));
    }

    public Parameter( String type, String identifier, int index )
    {
        this.type = type;
        this.identifier = identifier;
        this.index = index;

        setValue( String.format( "%s %s", type, identifier ));
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex( int index )
    {
        this.index = index;
    }

    protected SortedMap<String, String> getDependencies()
    {
        SortedMap<String, String> dependencies = buildDependencies();

        addDependency( dependencies, type );

        return dependencies;
    }

    protected String getType()
    {
        return type;
    }

    protected String getIdentifier()
    {
        return identifier;
    }
}
