OF.i18n = {};

OF.i18n.MESSAGE_KEY_DATA_PROPERTY_NAME = "i18n";

OF.i18n.initializeAll = function(el) {
	if (! el) {
		el = document;
	}
	$(el).find("[data-" + OF.i18n.MESSAGE_KEY_DATA_PROPERTY_NAME + "]").each(function(index, subEl) {
		OF.i18n.initializeEl(subEl);
	});
};

OF.i18n.initializeEl = function(el) {
	var messageKey = $(el).data(OF.i18n.MESSAGE_KEY_DATA_PROPERTY_NAME);
	if (messageKey) {
		var message = jQuery.i18n.prop(messageKey);
		$(el).html(message);
	}
};