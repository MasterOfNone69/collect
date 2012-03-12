/**
 * Generated by Gas3 v2.3.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package org.openforis.collect.model {

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.model.UITabDefinition")]
    public class UITabDefinition extends UITabDefinitionBase {
		
		public function getTab(name:String):UITab {
			for each(var tab:UITab in tabs) {
				if(tab.name == name) {
					return tab;
				}
			}
			return null;
		}
    }
}