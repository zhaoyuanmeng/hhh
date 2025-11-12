class Beam {
    constructor() { }

    /**
     * 添加一个或多个Beam对象
     * @param {object | array} data 数据结构，支持对象或数组，对于每一个对象支持以下属性：
     * id (string) 字符串类型的ID
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * duration (number) 光流粒子的生命周期，取值范围：[0.1~3.0]，单位：秒
     * thickness (number) 光流线的宽度，取值范围： [0.01~10.0]，单位：因Beam是粒子加上自发光不能精确单位,故目前按比例显示,需要精确单位的推荐使用Polyline
     * interval (number) 光流粒子发射间隔，取值范围：[1.0~10.0]，单位：秒
     * velocity (number) 光流粒子的速度，取值范围：[0.1~5.0]
     * color (Color) 光流的颜色，支持四种格式
     * coordinates (array) 光流的polyline的坐标数组
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.beam.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 显示Beam
     * @param {string | array} ids Beam对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.beam.show(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏Beam
     * @param {string | array} ids Beam对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.beam.hide(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个Beam对象
     * @param {string | array} ids 要删除的Beam对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.beam.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 删除场景中所有的Beam
     * @returns 可选的回调函数
     */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.beam.clear(() => {
                resolve()
            })
        })
    }
}

export default Beam