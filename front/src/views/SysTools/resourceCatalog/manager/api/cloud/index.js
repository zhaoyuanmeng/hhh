import apiRequest from "../request.js"
function cleanArray(actual) {
    const newArray = [];
    for (let i = 0; i < actual.length; i++) {
        if (actual[i]) {
            newArray.push(actual[i]);
        }
    }
    return newArray;
}

function toQueryString(obj) {
    if (!obj) return "";
    return cleanArray(
        Object.keys(obj).map(key => {
            if (obj[key] === undefined) return "";
            return encode(key) + "=" + encode(obj[key]);
        })
    ).join("&");
}

//云渲染资源
export const getpreview = params => apiRequest.post('service/media/getconfig' , params);


export const gettree = params => apiRequest.post('service/media/tree' , params);

//渲染组装详情
export const getone = params => apiRequest.post('service/media/one' , params);

//渲染组装更新
export const getupdate = params => apiRequest.post('service/media/update', params);

