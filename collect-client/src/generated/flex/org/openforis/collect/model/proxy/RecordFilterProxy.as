/**
 * Generated by Gas3 v2.3.0 (Granite Data Services).
 *
 * NOTE: this file is only generated if it does not exist. You may safely put
 * your custom code here.
 */

package org.openforis.collect.model.proxy {

	import mx.collections.IList;
	import mx.utils.StringUtil;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.model.proxy.RecordFilterProxy")]
    public class RecordFilterProxy extends RecordFilterProxyBase {
    
    	public function isEmpty():Boolean {
    		return ! (
    			containsNonEmptyValue(keyValues) 
    			|| (ownerIds != null && ownerIds.length > 0)
    			|| modifiedSince != null
    			|| ! isNaN(recordId)
    			|| step != null
    			|| stepGreaterOrEqual != null
    			);
    	}
    	
    	private function containsNonEmptyValue(values:IList):Boolean {
    		if (values == null || values.length == 0) {
    			return false;
    		}
    		var result:Boolean = false;
    		for each (var value:String in values) {
    			if (value != null && StringUtil.trim(value).length > 0) {
    				return true;
    			}
    		}
    		return false;
    	}
			
    }
}