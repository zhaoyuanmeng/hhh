class Settings {
    constructor() { }

    /**
     * 设置地图样式
     * @param {MapMode} mode 地图样式，0：指南针；1：小地图；2：大地图
     * @param {object} options 地图模式相关的参数，目前支持的选项有下面这些（如果某个参数没有设置，会使用默认值）：
     * serviceType: 服务类型，0：MVT矢量切片； 1：WMTS(ArcGis) （默认值是0）
     * coordType: 坐标系类型，0：经纬度；1：本地（默认值是0）
     * mapPoint: 同名点，取值范围：[x,y]，（默认值是[0, 0]）
     * longitude: 经度，取值范围：[0~180]（默认值是0.0）
     * latitude: 纬度，取值范围：[0~90]（默认值是0.0）
     * cache: 缓存路径，字符串地址，（默认值是 ":memory:"）
     * style: 风格路径，字符串地址，（默认值是 "mapbox://styles/mapbox/streets-v10"）
     * groundHeight: 地面高度，取值范围：[0~任意数值]（默认值是0.0）
     * renderMode: 渲染模式，0：正常；1：透明；2：标注；3：贴地（默认值是0）
     * serverURL : WMTS服务路径，二维数组，元素说明：index[0]服务id，index[1]服务地址；取值示例：[['111', 'http://192.168.1.29:81'], ['222', 'http://192.168.1.29:82'], ['333', 'http://192.168.1.29:83']]
     * coordOrder: 坐标顺序，0: XY; 1: YX（默认值为0）
     * maxLevel : WMTS服务最大显示层级，取值范围：[0~22]，默认值：10
     * @returns 可选的回调函数
     */
    setMapMode = function (mode, options) {
        return new Promise((resolve, reject) => {
            window.origAPI.settings.setMapMode(mode, options, () => {
                resolve()
            })
        })
    }
    /**
     * 获取地图样式
     * @returns 可选的回调函数
     */
    getMapMode = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.settings.getMapMode((resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 设置主界面UI元素的可见性
     * @param {boolean} visible 主界面UI元素是否可见
     * @returns 可选的回调函数
     */
    setMainUIVisibility = function (visible) {
        return new Promise((resolve, reject) => {
            window.origAPI.settings.setMainUIVisibility(visible, () => {
                resolve()
            })
        })
    }
    /**
     * 设置交互模式
     * @param {number} mode 五种交互模式，取值范围：[0,1,2,3,4]，默认值：0；【0：漫游，1：人物，2：无人机，3：中心漫游（物体观察），4：地图】
     * @returns 可选的回调函数
     */
    setInteractiveMode = function (mode) {
        return new Promise((resolve, reject) => {
            window.origAPI.settings.setInteractiveMode(mode, () => {
                resolve()
            })
        })
    }
    /**
     * 设置WMTS图层的可见性
     * @param {object} data 控制设置地图模式时添加的WMTS网络图层服务，参数类型：二维数组，数组元素index[0]：WMTS服务id 字符串id ；数组元素index[1]：服务是否可见 boolean ；取值示例：[['111', true], ['222', false]]，打开图层服务[111] 关闭图层服务[222]
     * @returns 可选的回调函数
     */
    setWMTSLayerVisible = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.settings.setWMTSLayerVisible(data, () => {
                resolve()
            })
        })
    }
    /**
     * 设置交互开关，目前支持启用和禁用鼠标交互，禁用后可以通过API设置交互
     * @param {boolean} bEnable 是否开启鼠标交互
     * @returns 可选的回调函数
     */
    setEnableInteract = function (bEnable) {
        return new Promise((resolve, reject) => {
            window.origAPI.settings.setEnableInteract(bEnable, () => {
                resolve()
            })
        })
    }
    /**
     * 设置指北针位置
     * @param {number} left 指北针距离视频流左侧原点像素距离，类似CSS内绝对定位left，注意: 如果设置为负值则恢复到原始位置
     * @param {number} top 指北针距离视频流上方原点像素距离，类似CSS内绝对定位top，注意: 如果设置为负值则恢复到原始位置
     * @returns 可选的回调函数
     */
    setCampassPosition = function (left, top) {
        return new Promise((resolve, reject) => {
            window.origAPI.settings.setCampassPosition(left, top, () => {
                resolve()
            })
        })
    }
}

export default Settings