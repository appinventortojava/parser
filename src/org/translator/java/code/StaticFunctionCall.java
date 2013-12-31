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

import org.translator.java.code.util.CodeUtil;
import java.util.Collection;

import java.util.SortedMap;

/**
 *
 * @author Joshua
 */
public class StaticFunctionCall extends FunctionCall
{
    private String className;

    public StaticFunctionCall( String className, String functionName )
    {
        super( String.format( "%s.%s", CodeUtil.classNameWithoutGeneric( className ), functionName ));
        this.className = new String( className );
    }

    public StaticFunctionCall( String className, String functionName, Value... parameters )
    {
        super( String.format( "%s.%s", CodeUtil.classNameWithoutGeneric( className ), functionName ), parameters );
        this.className = new String( className );
    }

    public StaticFunctionCall( String className, String functionName, Collection<Value> parameters )
    {
        super( String.format( "%s.%s", CodeUtil.classNameWithoutGeneric( className ), functionName ), parameters );
        this.className = new String( className );
    }

    protected SortedMap<String, String> getDependencies()
    {
        SortedMap<String, String> dependencies = super.getDependencies();

        addDependency( dependencies, className );

        return dependencies;
    }
}
