class HeatMap {
    constructor() { }

    /**
     * 添加HeatMap
     * @param {string} id 字符串类型的ID
     * @param {string} groupId 可选，Group分组Id
     * @param {string} userData 可选，用户自定义数据
     * @param {array} bbox 热力点坐标的范围：[minX,minY,minZ,maxX,maxY,maxZ]，数组元素类型：[任意浮点数]
     * @param {array} range 热力值的范围：[min,max]，数组元素类型：[任意浮点数]
     * @param {object | array} data 点数据，（注意：点的heatValue取值要在range所设定的范围内），对于每个点，支持以下属性：
     * id (string) 字符串类型的ID
     * coordinate (array) 热力点坐标
     * radius (number) 热力点影像半径范围，取值范围：[任意数值]
     * heatValue (number) 热力值，取值范围：[range参数设定范围内的任意数值]
     * @returns 可选的回调函数
     */
    add = function (id, groupId, userData, bbox, range, data) {
        return new Promise((resolve, reject) => {
            window.origAPI.heatmap.add(id, groupId, userData, bbox, range, data, () => {
                resolve()
            })
        })
    }
    /**
     * 显示HeatMap
     * @param {string | array} ids HeatMap对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.heatmap.show(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏HeatMap
     * @param {string | array} ids HeatMap对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.heatmap.hide(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个HeatMap对象
     * @param {string | array} ids 要删除的HeatMap对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.heatmap.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除场景中所有的HeatMap
     * @returns 可选的回调函数
     */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.heatmap.clear(() => {
                resolve()
            })
        })
    }
}

export default HeatMap