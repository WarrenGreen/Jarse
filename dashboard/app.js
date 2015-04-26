var express = require('express');
var bodyParser = require('body-parser');
var mysql = require('mysql');
var http = require('http');

var connection = mysql.createConnection({
	host: 'localhost',
	user: 'root', 
	password: ''
});

var app = express(); 
var server = http.createServer(app);

app.use(express.static(__dirname + '/'));
app.use(bodyParser.json());


//this is just sample stuff, can be changed around if needed.

connection.query('CREATE DATABASE IF NOT EXISTS test', function (err) {
    if (err) throw err;
    connection.query('USE test', function (err) {
        if (err) throw err;
        connection.query('CREATE TABLE IF NOT EXISTS users('
            + 'id INT NOT NULL AUTO_INCREMENT,'
            + 'PRIMARY KEY(id),'
            + 'name VARCHAR(30)'
            +  ')', function (err) {
                if (err) throw err;
            });
    });
});

// just to send our main html file at the base

app.get('/', function(req, res) {
    res.sendFile(__dirname + '/index.html');
});

// button to view all tables


// PSEUDOCODE: var keyNames = Object.getKeys(rows[0])
//          var tableString =            "<table>"              
//for (var i = 0; i < rows.length; i++) { 

app.post('/view', function(req, res){
    var command = req.body.command;
    var headerString = "<h4> List of tables in the Database </h4>";
    var tableString = "<table>"
    connection.query(command, function(err, rows){
        if(err) throw err;
        console.log(rows);
        var keys = Object.keys(rows[0]);
        console.log(keys);
        for (var i = 0; i < rows.length; i++){
            tableString += "<tr>"
            for (var j = 0; j < keys.length; j++) { 
                tableString += "<td>" + rows[i][keys[j]] + "</td>"
            }
            tableString += "</tr>"
        }
        tableString += "</table>"
        res.send(headerString+tableString);
    });
    
}); 


app.post('/modify', function(req, res){
    var command = req.body.command;
    var tableString = "<table>"
    connection.query(command, function(err, rows){
        console.log(rows);
        if(err) throw err;
        var keys = Object.keys(rows[0]);
        for (var i = 0; i < rows.length; i++){
            tableString += "<tr>"
            for (var j = 0; j < keys.length; j++) { 
                tableString += "<td>" + rows[i][keys[j]] + "</td>"
            }
            tableString += "</tr>"
        }
        tableString += "</table>"
        res.send(tableString);
    });
}); 

var showAll = function(tableName) { 
    connection.query("SELECT * FROM " + tableName, function(err, rows){
        if (err) return "<h2 class = 'error'> Not a valid SQL query! </h2>";
        var keys = Object.keys(rows[0]);
        var tableString = "<table>";
        for (var i = 0; i < rows.length; i++) { 
            tableString += "<tr>";
            for (var j = 0; j < keys.length; j++) { 
                tableString += "<td>" + rows[i][keys[j]] + "</td>";
            }
            tableString += "</tr>";
        }
        tableString += "</table>";
        return tableString;
    });
}

app.listen(3000);
console.log("Express server listening on port 3000");