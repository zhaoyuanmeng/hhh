class Polyline {
    constructor() { }
    /* 
        添加一个或多个Polyline对象
        param {object | array} data 对象或者数组，对于每一个对象支持以下属性：
        id (string) 字符串类型的ID
        groupId (string) 可选，Group分组
        userData (string) 可选，用户自定义数据
        color (Color) 颜色值，支持四种格式
        coordinates (array) 坐标点数组
        coordinateType (number) 坐标系类型，取值范围：0为Projection类型，1为WGS84类型，默认值：0
        style (PolylineStyle) 折线样式，箭头/光流/贴地/实线/虚线等，取值范围：[0~7]
        thickness (number) 线宽，单位：米，默认值20
        intensity (number) 亮度，取值范围：[0~1000]，默认值：0.5
        flowRate (number) 流速，取值范围：[0~1.0]，默认值：0.5
        shape (number) 样式，0：直线， 1：曲线
        returns 可选的回调函数
    */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.polyline.add(data, () => {
                resolve()
            })
        })
    }
    /* 
        显示Polyline
        param {string | array} ids Polyline对象的ID或者ID数组
        returns 可选的回调函数
    */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polyline.show(ids, () => {
                resolve()
            })
        })
    }
    /* 
        隐藏Polyline
        param {string | array} ids Polyline对象的ID或者ID数组
        returns 可选的回调函数
    */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polyline.hide(ids, () => {
                resolve()
            })
        })
    }
    /* 
        删除一个或多个Polyline对象
        param {string | array} ids Polyline对象的ID或者ID数组
        returns 可选的回调函数 
    */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polyline.delete(ids, () => {
                resolve()
            })
        })
    }
    /* 
        删除场景中所有的Polyline
        @returns 可选的回调函数
    */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.polyline.clear(() => {
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
    focus = function (ids, distance, flyTime, rotation) {
        return new Promise((resolve, reject) => {
            window.origAPI.polyline.focus(ids, distance, flyTime, rotation, () => {
                resolve()
            })
        })
    }
    /**
     * 设置新的颜色值
     * @param {string} id 
     * @param Color 新颜色值，支持四种格式
     * @returns
     */
    setColor = function (id, newVal) {
        return new Promise((resolve, reject) => {
            window.origAPI.polyline.setColor(id, newVal, () => {
                resolve
            })
        })
    }
}

export default Polyline