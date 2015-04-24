$(document).ready(function(){
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
		var cmd = $("#queryInput").val(); 
		var dataResource = JSON.stringify({command: cmd});
		$.ajax({
  			type: "POST",
  			url: "modify",
  			data: dataResource,
  			contentType: "application/json",
  			success: function(data){
  				$("#content").empty().append(data);
  			}, 
  			dataType: "HTML"
		});
	});

});