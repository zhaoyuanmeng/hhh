class SettingsPanel {
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
     * serverURL : WMTS风格路径，二维数组，元素说明：index[0]服务id，index[1]服务地址；取值示例：[['111', 'http://192.168.1.29:81'], ['222', 'http://192.168.1.29:82'], ['333', 'http://192.168.1.29:83']]
     * coordOrder: 坐标顺序，0: XY; 1: YX，默认值：0
     * maxLevel : WMTS服务最大显示层级，取值范围：[0~22]，默认值：10
     * @returns 可选的回调函数
     */
    setMapMode = function (mode, options) {
        return new Promise((resolve, reject) => {
            window.origAPI.settingsPanel.setMapMode(mode, options, () => {
                resolve()
            })
        })
    }
    /**
     * 获取参数 后期面板
     * @returns 可选的回调函数
     */
    getPostProcessMode = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.settingsPanel.getPostProcessMode((resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 设置参数 后期面板
     * @param {object} postProcessOptions 后期面板配置参数对象，支持以下属性
     * contrast {number} 对比度，取值范围：[0~100]，默认值：10
     * saturation {number} 饱和度，取值范围：[0~100]，默认值：10
     * bloomIntensity {number} 泛光，取值范围：[0~10.0]，默认值：0
     * ambientIntensity {number} 环境光遮罩强度，取值范围：[0~100]，默认值：60
     * darkCorner {number} 暗角，取值范围：[0~1]，单位：百分比，默认值：0
     * lutMode {number} LUT调色模式，取值范围：[0~30]，默认值：0（关闭调色模式），1-30对应不同LUT调色效果
     * lutIntensity {number} LUT调色强度，类型为百分比，取值范围：[0~1.0]，默认值：0，即小数对应的百分比
     * lensFlareIntensity {number} 光晕强度 ，取值范围：[0~1.0]，默认值：0
     * screenPercentage {number} 屏幕百分比，取值范围：[50~200]，默认值：125
     * terrainGlobalAlpha {number} 地形不透明度，取值范围：[0~1.0]，默认值：1.0
     * terrainGlobalLitStatus {boolean} 地形是否参与光照，默认值：true
     * osgbGlobalLitStatus {boolean} 倾斜摄影是否参与光照，默认值：false
     * osgbGlobalAlpha {number} 倾斜摄影不透明度，取值范围：[0~1.0],默认值：1.0
     * antiAliasing {boolean} 是否开启反走样，默认值：true
     * tonemapper {boolean} 是否开启色彩优化，默认值：true
     * postProcessEffects {number} 特效(滤镜效果)，取值范围：0【默认无效果】 1【景深效果】 2【线框效果】，3【圆珠笔】，4【白框】，5【蓝图】， 默认值：0
     * wireThickness {number} 可选参数，仅在线框效果下生效，线框大小，取值范围：[1.0~3.0]，默认值：1.0
     * dofMode {number} 可选参数，仅在景深效果下生效，对焦距离，取值：0【近距离0.5km】 1【中远距离2km】 2【中远距离5km】 3【远距离10km】，默认值：0
     * receiveDecalMode {number} 对象贴合模式，取值：0【无】 1【所有对象】 2【仅地形】，默认值：1
     * @returns 可选的回调函数
     */
    setPostProcessMode = function (postProcessOptions) {
        return new Promise((resolve, reject) => {
            window.origAPI.settingsPanel.setPostProcessMode(postProcessOptions, () => {
                resolve()
            })
        })
    }
}

export default SettingsPanel