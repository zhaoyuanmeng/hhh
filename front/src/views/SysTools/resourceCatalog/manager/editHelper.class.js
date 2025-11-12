class EditHelper {
    constructor() { }

    /**
     * 进入绘制模式
     * @returns 可选的回调函数
     */
    start = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.editHelper.start(() => {
                resolve()
            })
        })
    }
    /**
     * 设置绘制参数
     * @param {number} lineType 线类型，0：直线，1：曲线，默认值是0
     * @param {number} buildType 绘制类型，0：画多点线段， 1：画多边形， 默认值是0
     * @param {	Color} color 颜色
     * @returns 可选的回调函数
     */
    setParam = function (lineType, buildType, color) {
        return new Promise((resolve, reject) => {
            window.origAPI.editHelper.setParam(lineType, buildType, color, () => {
                resolve()
            })
        })
    }
    /**
     * 取消绘制模式
     * @returns 可选的回调函数
     */
    cancel = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.editHelper.cancel(() => {
                resolve()
            })
        })
    }
    /**
     * 绘制结束。调用此方法会结束当前的绘制，并在回调函数中返回绘制类型和坐标点，然后根据这些坐标点再创建相关的几何图形
     * @param {boolean} withOffset 是否计算工程中心偏移，默认值是true
     * @returns 可选的回调函数
     */
    finish = function (withOffset) {
        return new Promise((resolve, reject) => {
            window.origAPI.editHelper.finish(withOffset, (resp) => {
                resolve(resp)
            })
        })
    }
}

export default EditHelper