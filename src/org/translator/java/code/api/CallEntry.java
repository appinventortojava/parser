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

package org.translator.java.code.api;

import java.util.LinkedList;
import org.translator.java.code.Value;
import org.translator.java.code.api.util.APIUtil;
import org.w3c.dom.Node;

/**
 *
 * @author Joshua
 */
public class CallEntry extends ActionEntry
{
    private String genus;

    public CallEntry( Node entry )
    {
        super( entry );
        this.genus = APIUtil.getField( entry.getAttributes(), "genus" );
    }

    public Value buildCode( APIMapping mapping, Value target, LinkedList<Value> params )
    {
        LinkedList<Value> p = (LinkedList<Value>)params.clone();

        if( target != null && !mapping.isStatic( genus, target, p ))
            p.addFirst( target );

        return mapping.generateCode( genus, p );
    }
}