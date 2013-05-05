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
public class AssignmentStatement extends Statement
{
    private String identifier;
    private Value value;

    public AssignmentStatement( String identifier, Value value )
    {
        this.identifier = identifier;
        this.value = value;
    }

    public String toString()
    {
    	if (value.toString().matches("\\d+")) {
    		//note: the second %s had an f following it before
	        return String.format( "%s = %sf;", identifier, value.toString() );
    		
    	} else {
	        return String.format( "%s = %s;", identifier, value.toString() );
    	}
    }

    protected SortedMap<String, String> getDependencies()
    {
        return value.getDependencies();
    }
}
