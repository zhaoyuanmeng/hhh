import axios from "axios";

function util() {

}

/**
 * 合并数组
 * @param {*} a 
 * @param {*} b 
 */
function union(a, b) {
    for (var i in b) {
        a.push(b[i]);
    }
}

/**
 * 解析geojson,并增加z值
 * @param {*} url 
 * @param {*} z 
 * @returns 
 */
function read(url, z = 0) {
    axios.defaults.withCredentials = true;
    return new Promise((resolve, reject) => {
        axios.get(url).then(function (response) {
            let features = response.data.features;
            for (var i = 0; i < features.length; i++) {
                var feature = features[i];
                var coordinates = feature.geometry.coordinates[0][0];

                for (var j = 0; j < coordinates.length; j++) {
                    coordinates[j].push(z);
                }
            }
            resolve(response.data);
        }).catch(() => {
            resolve(false)
        });
    });
}

/**
 * 读取文件。
 * @param {} url 
 * @returns 
 */
function readFile(url) {
    axios.defaults.withCredentials = true;
    return new Promise((resolve, reject) => {
        axios.get(url).then(function (response) {
            resolve(response.data);
        }).catch((error) => {
            resolve(false)
        });
    });
}

util.read = read;
util.readFile = readFile;
util.union = union;

export default util;