/**
 * Generated by Gas3 v2.3.0 (Granite Data Services).
 *
 * WARNING: DO NOT CHANGE THIS FILE. IT MAY BE OVERWRITTEN EACH TIME YOU USE
 * THE GENERATOR.
 */

package org.openforis.collect.metamodel.ui {

    import org.granite.util.Enum;

    [Bindable]
    [RemoteClass(alias="org.openforis.collect.metamodel.ui.UIOptions$Orientation")]
    public class UIOptions$Orientation extends Enum {

        public static const HORIZONTAL:UIOptions$Orientation = new UIOptions$Orientation("HORIZONTAL", _);
        public static const VERTICAL:UIOptions$Orientation = new UIOptions$Orientation("VERTICAL", _);

        function UIOptions$Orientation(value:String = null, restrictor:* = null) {
            super((value || HORIZONTAL.name), restrictor);
        }

        protected override function getConstants():Array {
            return constants;
        }

        public static function get constants():Array {
            return [HORIZONTAL, VERTICAL];
        }

        public static function valueOf(name:String):UIOptions$Orientation {
            return UIOptions$Orientation(HORIZONTAL.constantOf(name));
        }
    }
}