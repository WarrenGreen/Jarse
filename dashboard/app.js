var express = require('express');
var mysql = require('mysql');
var http = require('http');

var connection = mysql.createConnection({
	host: 'localhost',
	user: 'root', 
	password: 'password'
});

var app = express(); 
var server = http.createServer(app);

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
    res.sendfile(__dirname + '/index.html');
});

// button to create a table from within the dashboard
app.post('/create', function(req, res){

}); 

// for inserting values into a given table from the dashboard.
app.post('/insert', function(req, res){

}); 

// for dleeting values into a given table from the dashboard. 
app.post('/delete', function(req, res){ 
	
});