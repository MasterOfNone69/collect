/**
 * Generated by Gas3 v2.3.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package org.openforis.collect.model {

    import org.granite.util.Enum;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.model.UserInGroup$UserGroupRole")]
    public class UserInGroup$UserGroupRole extends Enum {

        public static const OWNER:UserInGroup$UserGroupRole = new UserInGroup$UserGroupRole("OWNER", _);
        public static const ADMINISTRATOR:UserInGroup$UserGroupRole = new UserInGroup$UserGroupRole("ADMINISTRATOR", _);
        public static const DATA_ANALYZER:UserInGroup$UserGroupRole = new UserInGroup$UserGroupRole("DATA_ANALYZER", _);
        public static const OPERATOR:UserInGroup$UserGroupRole = new UserInGroup$UserGroupRole("OPERATOR", _);
        public static const VIEWER:UserInGroup$UserGroupRole = new UserInGroup$UserGroupRole("VIEWER", _);

        function UserInGroup$UserGroupRole(value:String = null, restrictor:* = null) {
            super((value || OWNER.name), restrictor);
        }

        protected override function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [OWNER, ADMINISTRATOR, DATA_ANALYZER, OPERATOR, VIEWER];
        }

        public static function valueOf(name:String):UserInGroup$UserGroupRole {
            return UserInGroup$UserGroupRole(OWNER.constantOf(name));
        }
    }
}