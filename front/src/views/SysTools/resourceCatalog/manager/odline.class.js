class ODline {
    constructor() { }
    /**
     * 添加一个或多个ODLine对象
     * @param {object | array} data 对象或者数组，对于每一个对象支持以下属性:
     * id (string) 字符串类型的ID
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * coordinates (array) 坐标点数组，只有2个元素，第1个元素是起点坐标，第2个元素是终点坐标
     * flowRate (number) 流速，取值范围：[0~1.0]，默认值：0.5
     * intensity (number) 亮度，取值范围：[0.1~1000]，默认值：0.5
     * bendDegree (number) 弯曲度，取值范围：[0~1.0]，默认值：0.5
     * tiling (number) 材质贴图平铺比例
     * lineThickness (number) 线宽
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.odline.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 显示ODLine
     * @param {string | array} ids ODLine对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    show = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.odline.show(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏ODLine
     * @param {string | array} ids ODLine对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    hide = function (data) {
        window.origAPI.odline.hide(ids, () => {
            resolve()
        })
    }
    /**
     * 删除ODLine
     * @param {string | array} ids ODLine对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    delete = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.odline.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除场景中所有ODLine
     * @returns 可选的回调函数
     */
    clear = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.odline.clear(() => {
                resolve()
            })
        })
    }
}

export default ODline