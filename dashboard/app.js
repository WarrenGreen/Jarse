var express = require('express');
var bodyParser = require('body-parser');
var mysql = require('mysql');
var http = require('http');
var q = require('q');

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
    var tableString = "<table class = 'table'>"
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
    console.log(command);
    connection.query(command, function(err, response){
        console.log(response);
        if(err) res.send("<h2 class = 'error'> Not a valid SQL query! </h2");
        res.send("<h3> Query successful! Number of affected rows " + response.affectedRows + "</h3>");
    });
});

var showAll = function(tableName, deferred, res) { 
    var tableString = "<table>";
    connection.query("SELECT * FROM " + tableName, function(err, rows){
        if (err){
           res.send("<h2 class = 'error'> Not a valid SQL query! </h2>");
           return;  
        } 
        deferred.resolve(rows);
    }); 

    return deferred.promise;
} 


app.post('/tables', function(req, res){
    var tables = req.body.command;
    var resultHtml = "";
    var pending = tables.length; 

    (function increment(x){
        if (x === pending) { 
            res.send(resultHtml);
            return;
        }
        console.log(x);
        console.log(pending);

        var defer = q.defer();
        var table = tables[x];
        var tableString = "<table class = 'table'>";
        
        showAll(table, defer, res).then(function(rows){
            var keys = Object.keys(rows[0]);
            tableString += "<tr>"; 
            for (var i = 0; i < keys.length; i++) { 
                tableString += "<th>" + keys[i] + "</th>";
            }
            tableString += "</tr>";
            for (var i = 0; i < rows.length; i++) { 
                tableString += "<tr>";
                for (var j = 0; j < keys.length; j++) {
                    tableString += "<td>" + rows[i][keys[j]] + "</td>";
                }
            }
            tableString += "</table>";
            resultHtml+= "<h4>" + table + "</h4>" + tableString;
            console.log(resultHtml);
            increment(x+1);
        });

    })(0)   
});

app.listen(3000);
console.log("Express server listening on port 3000");