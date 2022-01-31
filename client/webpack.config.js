let path = require('path');
let webpack = require('webpack');
module.exports = {
    devtool: 'source-map',
    mode: 'development',
    entry: './src/main/js/index.js',
    output: {
        path: path.join(__dirname, 'src/main/resources/generated'),
        filename: 'app.js',
        clean: true
    },
    resolve: {extensions: ['.js', '.jsx']},
    plugins: [
        new webpack.LoaderOptionsPlugin({
            debug: true}),
        new webpack.DefinePlugin({
            "process.env": {
                NODE_ENV: JSON.stringify("production")
            }
        })
    ],
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                loader: 'babel-loader',
                exclude: /(node_modules|bower_components)/,
                options: { presets: ['@babel/env','@babel/preset-react'] },
            },
            {
                test: /\.css$/i,
                use: ['style-loader', 'css-loader'],
            }
        ]
    },
    devServer: {
        static: {
            directory: path.resolve(__dirname, "/src/main/javascript"),
            publicPath: "/app-public-path/",
            staticOptions: {},
            watch: true,
            serveIndex: true
        },

    }
}