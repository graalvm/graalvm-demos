const plotly = require('plotly')('username', 'api_key');
const http = require('http');

//-----------------------Creating-server------------------------------

const server = http.createServer((req, res) => {
    outputSinGraph(res);
});

const PORT = process.env.PORT || 5000;

server.listen(PORT, () => console.log(`Server running on localhost:${PORT}`));

//-----------------------Evaluating-Taylor-series---------------------

const factorial = x => (x < 1) ? 1 : x * factorial(x - 1);

function outputSinGraph(res) {
    let data = {
        x : [],
        y : []
    };

    let t1 = getNanoSecTime();
    data = tabulateSin(-Math.PI * 4, Math.PI * 4, 0.1);
    let time = getNanoSecTime() - t1;
    console.log(`\nEvaluations took ${time} nanoseconds.\n`);

    function taylorSin(x) {
        let sum = 0, n = 0, elem = 1, eps = 0.0000001;

        while(Math.abs(elem) > eps) {
            elem = Math.pow(-1, n) * (Math.pow(x, 2 * n + 1) / factorial(2 * n + 1));
            sum += elem;
            n++;
        }
        return sum;
    }

    function tabulateSin(left, right, step) {
        let x = left;
        let d = {
            x : [],
            y : []
        };
        while(x <= right) {
            let y = taylorSin(x);
            console.log(`sin(${Math.round(x * 100) / 100}) = ${Math.round(y * 100000) / 100000} --- actual ${Math.round(Math.sin(x) * 100000) / 100000}`);
            d.x.push(x);
            d.y.push(y);
            x += step;
        }
        return d;
    }

    //-----------------------Plotting-graph-------------------------------

    var layout = {
        title: {
        text: `Evaluations took ${time} nanoseconds.`,
        font: {
            family: 'Courier New, monospace',
            size: 24
        },
        xref: 'paper',
        x: 0.05,
        }
    }

    var trace1 = {
        x: data.x,
        y: data.y,
        type: "scatter"
    };

    var figure = { 
        'data': [trace1],
        'layout' : layout
    };

    var imgOpts = {
        format: 'png',
        width: 1000,
        height: 500
    };
    
    plotly.getImage(figure, imgOpts, function (error, imageStream) {
        if (error) return console.log (error);
        res.writeHead(200, {'Content-Type': 'image/png'});
        imageStream.pipe(res);
    });
}

//-----------------------Time-measurement-----------------------------

function getNanoSecTime() {
    var hrTime = process.hrtime();
    return hrTime[0] * 1000000000 + hrTime[1];
}