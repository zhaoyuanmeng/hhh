/**
 * 加载xml文件
 * @param {Object} dname
 */
function loadXMLDoc(dname) {
    let xhttp
    if (window.XMLHttpRequest) {
        xhttp = new XMLHttpRequest();
    } else {
        xhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xhttp.open("GET", dname, false);
    xhttp.send();
    return xhttp.responseXML;
}
/**
 * 加载xml文件-异步
 * @param {Object} dname
 */
function loadXMLDocAsync(dname) {
    return new Promise((resolve, reject) => {
        let xhttp
        if (window.XMLHttpRequest) {
            xhttp = new XMLHttpRequest();
        } else {
            xhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xhttp.open("GET", dname, true);
        xhttp.onreadystatechange = function () {  //绑定响应状态事件监听函数
            if (xhttp.readyState == 4) {  //监听readyState状态
                if (xhttp.status == 200) {  //监听HTTP状态码
                    resolve(xhttp.responseXML);
                }
            }
        }
        xhttp.send();
    });
}
/**
  * xml字符串转换xml对象数据
  * @param {Object} xmlStr
  */
function xmlStr2XmlObj(xmlStr) {
    var xmlObj = {};
    if (document.all) {
        var xmlDom = new ActiveXObject("Microsoft.XMLDOM");
        xmlDom.loadXML(xmlStr);
        xmlObj = xmlDom;
    } else {
        xmlObj = new DOMParser().parseFromString(xmlStr, "text/xml");
    }
    return xmlObj;
}

/**
 * xml直接转换json数据
 * @param {Object} xml
 */
function xmlObj2json(xml) {
    try {
        var obj = {};
        if (xml.children.length > 0) {
            for (var i = 0; i < xml.children.length; i++) {
                var item = xml.children.item(i);
                var nodeName = item.nodeName;
                if (typeof (obj[nodeName]) == "undefined") {
                    obj[nodeName] = xmlObj2json(item);
                } else {
                    if (typeof (obj[nodeName].push) == "undefined") {
                        var old = obj[nodeName];
                        obj[nodeName] = [];
                        obj[nodeName].push(old);
                    }
                    obj[nodeName].push(xmlObj2json(item));
                }
            }
        } else {
            obj = xml.textContent;
        }
        return obj;
    } catch (e) {
        console.log(e.message);
    }
}

/**
 * xml字符串转换json数据
 * @param {Object} xml
 */
function xmlStr2json(xml) {
    var xmlObj = xmlStr2XmlObj(xml);
    var jsonObj = {};
    if (xmlObj.childNodes.length > 0) {
        jsonObj = xmlObj2json(xmlObj);
    }
    return jsonObj;
}
export {
    loadXMLDoc,
    loadXMLDocAsync,
    xmlStr2XmlObj,
    xmlObj2json,
    xmlStr2json
}
