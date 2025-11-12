class Polygon3D {
    constructor() { }

    /**
     * 添加一个或多个3DPolygon对象
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * coordinates (array) 多边形坐标数组（二维数组）
     * color (Color) 多边形高亮颜色，支持四种格式
     * height (number) 3D多边形的高度，取值范围：[任意正数]
     * intensity (number) 亮度，取值范围：[0~1000]
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon3d.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 显示3DPolygon
     * @param {string | array} ids 3DPolygon对象的ID或者ID数组 
     * @returns 
     */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon3d.show(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏3DPolygon
     * @param {string | array} ids 3DPolygon对象的ID或者ID数组
     * @returns 
     */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon3d.hide(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个3DPolygon对象
     * @param {string | array} ids 要删除的3DPolygon对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon3d.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除场景中所有的3DPolygon
     * @returns 可选的回调函数
     */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.polygon3d.clear(() => {
                resolve()
            })
        })
    }
}

export default Polygon3D