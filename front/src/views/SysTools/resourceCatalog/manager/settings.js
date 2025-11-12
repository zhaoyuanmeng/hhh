import Settings from './settings.class'

class SettingsBIZ extends Settings {
    constructor() {
        super()

    }
}

/**
 * 设置地图样式
 * @param {string} type 类型
 * @param {object} data 地图模式相关的参数
 */
SettingsBIZ.prototype.setMapMode_biz = function (type, data) {
    let _type = 0, options = {}

    switch (type) {
        case '指南针':
            _type = 0
            break
        case '小地图':
            _type = 1
            break
        case '大地图':
            _type = 2
            if (data) {
                options = data
            } else {
                options = {
                    serviceType: 1, // 服务类型 0：MVT矢量切片 1：WMTS(ArcGis) （默认值是0）
                    coordType: 0, // 坐标系类型，0：经纬度；1：本地。（默认值是0）
                    mapPoint: [493350, 2491700], // 同名点。（默认值是[0, 0]）
                    longitude: 113.935341,
                    latitude: 22.522806,
                    // cache: ':memory:',
                    // style: 'http://map.geoq.cn/arcgis/rest/services/ChinaOnlineStreetPurplishBlue/MapServer', // 风格路径
                    groundHeight: 0.0,
                    renderMode: 0, // 绘制模式，0：正常；1：透明；2：标注（默认值是0）
                    serverURL: [['1', 'http://server.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer']],
                    coordOrder: 0, // 0: XY 1: YX（默认值为0）
                }
            }
            break
    }
    this.setMapMode(_type, options)
}
export default SettingsBIZ