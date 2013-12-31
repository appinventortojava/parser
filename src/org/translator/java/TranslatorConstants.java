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

package org.translator.java;

import org.translator.java.code.api.APIMapping;

/**
 *
 * @author Joshua
 */
public abstract class TranslatorConstants
{
    public static final String API_MAPPING_PATH = "/resources/APIMapping.xml";
    public static final String COMPONENT_PREFIX = "com.google.devtools.simple.runtime.components.android.";
    public static final String FORM = "com.google.devtools.simple.runtime.components.android.Form";
    public static final String[] EVENT_HANDLING_INTERFACES = { "com.google.devtools.simple.runtime.components.HandlesEventDispatching" };
    public static final String EVENT_DISPATCHER = "com.google.devtools.simple.runtime.events.EventDispatcher";
    public static final String PACKAGE_PREFIX = "org.";
    public static final String UTILITY_NAME = "AppInventorUtility";

    public static final String MANIFEST_XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";
    public static final String MANIFEST_ANDROID_VERSIONCODE = "1";
    public static final String MANIFEST_ANDROID_VERSIONNAME = "1.0";
    public static final String MANIFEST_ANDROID_ICON = "@drawable/icon";
    public static final String MANIFEST_ACTION_MAIN = "android.intent.action.MAIN";
    public static final String MANIFEST_CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER";


    public static final APIMapping API = new APIMapping();

    public static String getUtilityClass()
    {
        return String.format( "%sutil.%s", PACKAGE_PREFIX, UTILITY_NAME );
    }
}
