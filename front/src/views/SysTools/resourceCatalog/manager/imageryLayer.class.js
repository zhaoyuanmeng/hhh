class ImageryLayer {
    constructor() { }

    /**
     * 添加图层服务前需要先初始化相关参数
     * @param {object} option 初始化对象，包含图层初始化环境配置和图层范围信息
     * xmlUrl (string) 必选，xml协议的url路径
     * layerName (string) 可选，图层名称
     * tileMatrixName (string) 可选，如果服务类型是WMTS：tileMatrixName是切片方案，如果服务类型是WMS：tileMatrixName是坐标类型
     * ogcEPSG (string) 必选，坐标系类型
     * cachePath (string) 必选，缓存路径
     * mapMode (boolean) 必选，大地图true，小地图false，默认：小地图false
     * renderMode (number) 必选，渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
     * @returns 可选的回调函数
     */
    init = function (option) {
        return new Promise((resolve, reject) => {
            window.origAPI.imageryLayer.init(option, () => {
                resolve()
            })
        })
    }
    /**
     * 添加一个或多个网络地图服务，如WMTS/WMS服务等网络图层服务
     * @param {object} data 图层服务的对象，属性如下：
     * id (string) 必选，字符串类型的ID
     * url (string) 必选，WMTS/WMS服务的URL地址，例如WMTS GetTile操作的基本URL（用于KVP编码的请求）或tile-URL模板（用于RESTful请求）。 tile-URL模板应包含以下变量:{style}，{TileMatrixSet}，{TileMatrix}，{TileRow}，{TileCol}。如果实际值是硬编码的，或者服务器不需要，则前两个是可选的。 {s}关键字可用于指定子域
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.imageryLayer.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个ImageryLayer图层对象
     * @param {string | array} ids 要删除的ImageryLayer对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.imageryLayer.delete(ids, () => {
                resolve()
            })
        })
    }
}

export default ImageryLayer