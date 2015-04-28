# Jarse Dashboard 

Jarse is our final project for CS411, Database Systems at the University of Illinois at Urbana-Champaign. 

## Background 

The Jarse web dashboard allows you to view and interact with SQL databases in real time from your browser. This is the web component to the Jarse SDK.
It serves as a quick way that you can prototype SQL queries and view their output, without the need to interact through a shell. 

## Set-Up Instructions 

First, make sure that you have node and npm installed. To do this, do: 

	$ node -v 
	v0.10.33

	$ npm -v
	v2.1.6

Then, run ```npm install``` to install all of the dependencies. 

If you want to run the development server, then simply run ```nodemon app.js```. Otherwise run ```node app.js```, and navigate to localhost:3000 in your browser. 

For any changes you make, run ```gulp``` to ensure that you properly re-compile everything back to normal.
