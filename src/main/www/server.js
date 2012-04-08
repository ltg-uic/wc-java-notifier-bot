/**
    Simple stand-alone HTTP server with built-in reverse proxy for XMPP-BOSH.    
    This uses node.js with the http-proxy and node-static modules.

*/

var BOSH_URL = "http://phenomena.evl.uic.edu:5280/"
var MONGOOSE_URL = "http://phenomena.evl.uic.edu:27080/"
var SRV_ADDRESS = '127.0.0.1';
// var SRV_ADDRESS = '192.168.1.2';
var SRV_PORT = 1337;

var http = require('http');
var httpProxy = require('http-proxy');
var httpStatic = require('node-static');
var url = require('url');
var util = require('util');
var fs = require('fs');

var proxy = new httpProxy.RoutingProxy();
var file = new(httpStatic.Server)('.', {cache: false});

global.proxyMap = [
    {
        name: 'BOSH',
        match: function(req) { return url.parse(req.url).pathname.match(/^\/http-bind/) },
        proxy: function(req, res) {
            console.log("PROXY "+req.url+" ==> "+BOSH_URL)
            boshUrl = url.parse(BOSH_URL)
            proxy.proxyRequest(req, res, {
                host: boshUrl.hostname,
                port: boshUrl.port
            })
        }
    },

    {
        name: "Mongoose",
        match: function(req) { return url.parse(req.url).pathname.match(/^\/mongoose/); },
        proxy: function(req, res) {
            req.url = req.url.replace(/\/mongoose/,'');
            var mongooseUrl = url.parse(MONGOOSE_URL);
            console.log("PROXY "+req.url+" ==> "+MONGOOSE_URL);
            req.headers['host'] = mongooseUrl.hostname;
            proxy.proxyRequest(req, res, {
                host: mongooseUrl.hostname,
                port: mongooseUrl.port
            });
        }
    },
      

    {
        name: "STATIC",
        match: function(req) { return true },
        proxy: function(req, res) {
            req.addListener('end', function(){ 
                console.log("STATIC "+req.url)
                file.serve(req, res)      
            })
        }
    }
]


http.createServer(function (req, res) {
    for (i in global.proxyMap) {
        map = global.proxyMap[i]
        
        if (map.match(req)) {
            map.proxy(req, res)
            break
        }
    }
}).listen(SRV_PORT, SRV_ADDRESS);

console.log("Server listening on http://"+ SRV_ADDRESS + ":" +SRV_PORT+"...");


