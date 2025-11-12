class CustomObject {
    constructor() { }

    /**
     * 高亮
     * @param {string | array} ids CustomObject对象的ID或者数组
     * @returns 可选的回调函数
     */
    highlight = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.customObject.highlight(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 取消高亮
     * @param {string | array} ids CustomObject对象的ID或者数组
     * - 如果没有指定该参数，则会把场景中所有高亮的CustomObject对消高亮显示
     * - 如果指定了该参数，则只取消高亮指定的CustomObject对象
     * @returns 可选的回调函数
     */
    unhighlight = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.customObject.unhighlight(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 把TileLayer图层中包含的一个或多个(Actor)模型复制为一个CustomObject对象
     * @param {	object | array} data 数据结构，支持对象或数组，对于每一个对象支持以下属性：
     * id (string) CustomObject对象的ID
     * tileLayerId (string) TileLayer图层的ID
     * objectId (string | array) TileLayer图层中包含的待复制的模型(Actor)的ObjectId，同时也支持数组类型参数即把多个actor复制为一个customObject
     * location (array) 位置坐标：[X,Y,Z]，取值示例，数组元素类型：(number)，取值范围：[任意数值]
     * coordinateType (number) 坐标系类型，取值：0为Projection类型，1为WGS84类型，默认值：0
     * rotation (array) 旋转：[Pitch,Yaw,Roll]，数组元素类型：(number)，取值范围：[任意数值]
     * scale (array) 缩放：[X,Y,Z]，数组元素类型：(number)，取值范围：[任意正整数]
     * smoothMotion (number) 1: 平滑插值，0: 跳跃
     * @returns 可选的回调函数
     */
    addByTileLayer = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.customObject.addByTileLayer(data, () => {
                resolve()
            })
        })
    }
    /**
     * 根据ID获取CustomObject的详细信息
     * @param {string | array} ids 要获取的CustomObject对象ID或者ID数组（可以获取一个或者多个）
     * @returns 可选的回调函数
     */
    get = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.customObject.get(ids, (resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 修改一个或多个CustomObject对象
     * @param {object | array} data 数据结构，请参考add方法
     * @returns 可选的回调函数
     */
    update = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.customObject.update(data, () => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个CustomObject对象
     * @param {string | array} ids 要删除的CustomObject对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.customObject.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除场景中所有的CustomObject
     * @returns 可选的回调函数
     */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.customObject.clear(() => {
                resolve()
            })
        })
    }
}

export default CustomObject