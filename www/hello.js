/*global cordova, module*/

module.exports = {
    greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "greet", [name]);
    },
	check: function (name, successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "Hello", "check", [name]);
	},
	start: function (name, successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "Hello", "start", (typeof name === 'string') ? [name] : name);
	}
};
