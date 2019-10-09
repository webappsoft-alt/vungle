var exec = require('cordova/exec');


module.exports.initSDK=function(arg0,success,error){
	exec(success,error,'vungle',[arg0]);
}
