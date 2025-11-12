class Cesium3DTileset {
    constructor() {}
    /**
     * 添加一个或多个Cesium3DTileset对象
     * @param {object | array} data 数据结构，支持对象或数组，对于每一个对象支持以下属性：
     * id (string) 字符串类型的ID
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * tileURL (string) URL
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.cesium3dtileset.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个Cesium3DTileset对象
     * @param {string | array} ids 要删除的Cesium3DTileset对象的ID或者ID数组（可以删除一个或者多个）
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.cesium3dtileset.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 自动定位到合适的观察距离
     * @param {string | array} ids Cesium3DTileset对象的ID或者ID数组
     * @param {number} distance 可选参数，观察点距离目标点（被拍摄物体）的距离，取值范围：[0.01~任意正数]，如果设置为0或者不设置，系统自动计算
     * @param {number} flyTime 可选参数，相机飞行的时间，取值范围：[0~任意正数]，单位：秒，默认值2秒
     * @param {array} rotation 可选参数，相机旋转的欧拉角
     */
    focus = function (ids, distance, flyTime, rotation) {
        return new Promise((resolve, reject) => {
            window.origAPI.cesium3dtileset.focus(ids, distance, flyTime, rotation, () => {
                resolve()
            })
        })
    }
}

export default Cesium3DTileset