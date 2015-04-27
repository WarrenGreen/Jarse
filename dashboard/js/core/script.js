// function to fill the modal with table names
// repeated this function already 
var showTables = function(){
	var query = JSON.stringify({command: 'SHOW TABLES from test'});
	var returnVal = {};
	$.ajax({
		type: "POST",
		url: "view",
		data: query,
		contentType: "application/json",
		success: function(data){
			populateModal(data);
		}, 
		dataType: "HTML"
	});
};


var populateModal = function(data){ 
	var tds = $(data).find("td");
	$(tds).each(function(i){
		$(".inputSelects").append("<label> <input type = 'checkbox'/> " + $(this).text() + " </label>");
	});
};

var getCheckedInputs = function(){ 
	var inputs = [];
	var labels = $(".inputSelects > label"); 
	var checkboxes = $("input:checked");
	$(checkboxes).each(function(i){
		inputs.push($.trim($(this).parent().text()));
	});
	console.log(inputs);
	return inputs;
};

$(document).ready(function(){
	showTables();
});

$("#home").on('click', function(){
	$("#content").empty();
	$("#content").append("<h2> Jarse Analytics Dashboard </h2>");
	$("#contentText").text("Welcome to the Jarse Analytics Dashboard. This dashboard is provided as a web interface to view SQL commands in real time, and to see what they do. Click around to explore!");
});	

$("#view-db").on('click', function(){
	var dataResource = JSON.stringify({command: 'SHOW TABLES FROM test'});
	$.ajax({
		type: "POST",
		url: "view",
		data: dataResource,
		contentType: "application/json",
		success: function(data){
			$("#content").empty().append(data);
		}, 
		dataType: "HTML"
	});
});

$("#query-submit").on('click', function(){
	var queryString = $("#queryInput").val(); 
	var query = JSON.stringify({command: queryString});
	$.ajax({
		type: "POST",
		url: "modify",
		data: query,
		contentType: "application/json",
		success: function(data){  				  				
			console.log(data);
			$("#content").empty().append(data);
			$("#queryModal").modal("hide");
		}, 
		dataType: "HTML"
	});
});

$("#table-submit").on('click', function(){
	var checked = getCheckedInputs();
	var query = JSON.stringify({command: checked});
	$.ajax({
		type: "POST",
		url: "tables",
		data: query,
		contentType: "application/json",
		success: function(data){  				  				
			console.log(data);
			$("#content").empty().append(data);
			$("#selectModal").modal("hide");
		}, 
	dataType: "HTML"
	});
});