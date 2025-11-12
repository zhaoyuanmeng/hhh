class Polygon {
    constructor() { }

    /**
     * 添加一个或多个Polygon对象
     * @param {object | array} data 对象或者数组，对于每一个对象支持以下属性：
     * id (string) 字符串类型的ID
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * coordinates (array) 多边形坐标数组（二维数组）
     * color (Color) 多边形高亮颜色，支持四种格式
     * frameColor (Color) 边框颜色，支持四种格式
     * frameThickness (number) 边框厚度，单位：米。（当frameThickness设置为0的时候，不创建轮廓）
     * depthTest (boolean) 是否做深度检测，默认为true，true会被遮挡，false不会被遮挡
     * intensity (number) 亮度，取值范围：[0~1000]，注：目前仅单色模式下生效[style:PolygonStyle.SingleColor]
     * style (PolygonStyle) 多边形样式，单色/圆点/体积/渐变/波纹/贴地等
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon.add(data, () => {
                resolve()
            })
        })
    }
    /** 
    * 显示Polygon
    * @param {string | array} ids Polygon对象的ID或者ID数组
    * @returns 可选的回调函数
    */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon.show(ids, () => {
                resolve()
            })
        })
    }
    /** 
    * 隐藏Polygon
    * @param {string | array} ids Polygon对象的ID或者ID数组
    * @returns 可选的回调函数
    */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon.hide(ids, () => {
                resolve()
            })
        })
    }
    /** 
    * 删除一个或多个Polygon对象
    * @param {string | array} ids Polygon对象的ID或者ID数组
    * @returns 可选的回调函数
    */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon.delete(ids, () => {
                resolve()
            })
        })
    }
    /** 
    * 删除场景中所有的Polygon
    * @returns 可选的回调函数
    */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon.clear(() => {
                resolve()
            })
        })
    }
    /**
     * 自动定位到合适的观察距离
     * @param {string | array} ids Polyline对象的ID或者ID数组
     * @param {number} distance 可选参数，观察点距离目标点（被拍摄物体）的距离
     * @param {number} flyTime 可选参数，相机飞行的时间
     * @param {array} fn 可选的回调函数
     * @returns 
     */
    focus = function (ids, distance, flyTime) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon.focus(ids, distance, flyTime, () => {
                resolve()
            })
        })
    }
}

export default Polygon