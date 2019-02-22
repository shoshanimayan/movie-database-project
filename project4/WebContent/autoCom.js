/**
 * 
 */
console.log("starting");
jQuery.ajax({
	"method": "GET",
	// generate the request url from the query.
	// escape the query string to avoid errors caused by special characters 
	"url": "auto?query=" + escape("wonder bar"),
	"success": function(data) {
		// pass the data, query, and doneCallback function into the success handler
		console.log(data);
	},
	"error": function(errorData) {
		console.log("lookup ajax error")
		console.log(errorData)
	}

	
})
