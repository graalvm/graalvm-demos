// Generated using webpack-cli https://github.com/webpack/webpack-cli

const path = require('path');
const { EnvironmentPlugin } = require('webpack');

const isProduction = process.env.NODE_ENV == 'production';

const config = {
    entry: './main.mjs',
    output: {
        path: process.env.BUILD_DIR
            ? path.resolve(process.env.BUILD_DIR)
            : path.resolve(__dirname, '../../../target/classes/bundle'),
        filename: 'bundle.mjs',
        module: true,
        library: {
            type: 'module',
        },
        globalObject: 'globalThis'
    },
    experiments: {
        outputModule: true // Generate ES module sources
    },
    optimization: {
        usedExports: true, // Include only used exports in the bundle
        minimize: false,   // Disable minification
    },
    resolve: {
        aliasFields: [],   // Disable browser alias to use the server version of the qrcode package
        fallback: {        // Redirect Node.js core modules to polyfills
            "stream": require.resolve("stream-browserify"),
            "zlib": require.resolve("browserify-zlib"),
            "fs": false    // Exclude the fs module altogether
        },
    },

    plugins: [
        // Add your plugins here
        // Learn more about plugins from https://webpack.js.org/configuration/plugins/
        // @ts-ignore
        new EnvironmentPlugin({
            NODE_DEBUG: false, // Set process.env.NODE_DEBUG to false
        }),
    ],
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/i,
                loader: 'babel-loader',
            },
            {
                test: /\.(eot|svg|ttf|woff|woff2|png|jpg|gif)$/i,
                type: 'asset',
            },

            // Add your rules for custom modules here
            // Learn more about loaders from https://webpack.js.org/loaders/
        ],
    },
    mode: isProduction ? 'production' : 'development',
};

module.exports = () => config;
