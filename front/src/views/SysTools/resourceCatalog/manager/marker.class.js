class Marker {
    constructor() {}
    /**
     * 添加一个或多个标注点
     * @param {object | array} 标注点的数据，可以是Object类型或者Array类型，对于每一个标注点，支持以下属性：
     * id (string) 标注点的唯一标识符
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * coordinateType (number) 坐标系类型，取值0(Projection), 1(WGS84)
     * coordinate (array) 标注点的位置坐标: [x, y, z]
     * anchors (array) 锚点: [x, y]，默认值:[-16, 32]
     * range (array) 可视范围: [近裁距离, 远裁距离]，默认值: [10, 10000]
     * textRange (array) 文本可视范围
     * ……
     * return
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.marker.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个标注对象
     * @param {string | array} ids 要删除的标注对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 
     */
    delete = function(ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.marker.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除场景中所有的标注
     * @returns
     */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.marker.clear(() => {
                resolve()
            })
        })
    }
    /**
     * 
     * @param {string | array} ids 标注对象的ID或者ID数组
     * @param {number} distance 可选参数，观察点距离目标点（被拍摄物体）的距离，取值范围：[0.01~任意正数]，如果设置为0或者不设置，系统自动计算
     * @param {number} flyTime 可选参数，相机飞行的时间，取值范围：[0~任意正数]，单位：秒，默认值2秒
     * @param {array} rotation 欧拉角 可选参数
     */
    focus = function (ids, distance, flyTime, rotation) {
        return new Promise((resolve, reject) => {
            window.origAPI.marker.focus(ids, distance, flyTime, rotation, () => {
                resolve()
            })
        })
    }
}

export default Marker