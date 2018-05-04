var express = require('express');
var bodyParser = require("body-parser");
var app = express();
var APP_PORT = 8088;

Polyglot.evalFile("ruby", "validator.rb");
var Validator = Polyglot.import('Validator');

Polyglot.evalFile("R", "functionGraph.r");
var plotFunction = Polyglot.import('plotFunction');

app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

app.post('/graph', function (req, res) {
    let expr = req.body.expr;
    let x1 = req.body.x1;
    let x2 = req.body.x2;
    if (!x1 || !x2) {
      x1 = -10;
      x2 = 10;
    }
    let errorMsg = Validator.validate(expr);
    if (errorMsg.length() == 0) {
        res.send(plotFunction(expr, x1, x2));
    } else {
        res.send(errorMsg);
    }
});

app.use(express.static(__dirname + "/public"));
app.listen(APP_PORT, function() {
    console.log("Server listening on port " + APP_PORT);
});
