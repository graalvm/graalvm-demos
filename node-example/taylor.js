const http = require('http');
const plt = require('matplotnode');
const fs = require('fs');

const file = './plot.png';

//-----------------------Creating-server------------------------------

const server = http.createServer((req, res) => {
    plt.close('all');
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

    plt.title(`Evaluations took ${time} nanoseconds.`);
    plt.plot(data.x, data.y, 'color=b', 'label=sin(x)');
    plt.legend();

    plt.save(file);

    let imageStream = fs.createReadStream(file);
    res.writeHead(200, {'Content-Type': 'image/png'});
    imageStream.pipe(res);
}

//-----------------------Time-measurement-----------------------------

function getNanoSecTime() {
    let hrTime = process.hrtime();
    return hrTime[0] * 1000000000 + hrTime[1];
}