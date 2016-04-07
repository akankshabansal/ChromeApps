$(document).ready(function() {
	$(document.body).annotator();
	var content = jQuery(document.body).annotator();
	content.annotator('addPlugin', 'Offline');
	var annotator = content.data('annotator');
});
