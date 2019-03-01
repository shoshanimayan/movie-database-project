/**
 * js for autocomplete suggestion list for total text search on main page. 
 */
var dict={};

function handleLookup(query, doneCallback) {	
	console.log("autocomplete query initiated");

	var check = query in dict;
	if(check==true){
		console.log("search is cached");
		console.log(dict[query]);

		doneCallback( { suggestions: dict[query] } );

	}
	else{
		console.log("starting ajax");
	jQuery.ajax({
		"method": "GET",
		"url": "AutoJS?query=" + escape(query),
		"success": function(data) {
			handleLookupAjaxSuccess(data, query, doneCallback) ;
			
		},
		"error": function(errorData) {
			console.log("lookup ajax error")
			console.log(errorData)
		}		
	})
	}
}

function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup successful")
	if (JSON.parse(data) !== undefined && JSON.parse(data).length > 0) {
		var jsonData= new Array;
		for (i = 0; i < 10; i++) {
			jsonData[i] =JSON.parse(data)[i]
		} 
		dict[query]= jsonData
		console.log(jsonData)
		doneCallback( { suggestions: jsonData } );
}
	
}


function handleSelectSuggestion(suggestion) {
	// TODO: jump to the specific result page based on the selected suggestion
	
	//console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"]["ID"])
	document.location.href="/project1/MovieServlet?fulltextSearch="+suggestion["value"];
}

$('#autocomplete').autocomplete({
	// documentation of the lookup function can be found under the "Custom lookup function" section
    lookup: function (query, doneCallback) {
    	if($('#autocomplete').val().length>=3){
    		handleLookup(query, doneCallback)}
    },
    // when select from suggestion list
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    // set delay time
    deferRequestBy: 300,
    
});

function handleNormalSearch(query) {
	//console.log("doing normal search with query: " + query);
	document.location.href="/project1/MovieServlet?fulltextSearch="+query;
}

//bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
	// keyCode 13 is the enter key
	if (event.keyCode == 13) {
		// pass the value of the input box to the handler function
		handleNormalSearch($('#autocomplete').val())
	}
})