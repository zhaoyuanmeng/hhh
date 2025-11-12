class RadiationPoint {
    constructor() { }

    /**
     * 添加一个或多个RadiationPoint对象
     * @param {object | array} data 对象或者数组，对于每一个对象支持以下属性：
     * id (string) 字符串类型的ID
     * groupId (string) 可选，Group分组id
     * userData (string) 可选，用户自定义数据
     * coordinates (array) 多边形坐标数组（二维数组）
     * radius (number) 辐射圈的半径，取值范围：[0~500000]，单位：米
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.radiationPoint.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏RadiationPoint
     * @param {string | array} ids RadiationPoint对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.radiationPoint.hide(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 显示RadiationPoint
     * @param {string | array} ids RadiationPoint对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.radiationPoint.show(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除RadiationPoint
     * @param {string | array} ids RadiationPoint对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.radiationPoint.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 清除场景中所有RadiationPoint
     * @returns 可选的回调函数
     */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.radiationPoint.clear(() => {
                resolve()
            })
        })
    }
}

export default RadiationPoint