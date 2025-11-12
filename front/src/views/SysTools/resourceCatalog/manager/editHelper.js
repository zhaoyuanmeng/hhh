import { ElMessage } from 'element-plus'
import EditHelper from './editHelper.class'
// import { Message } from 'element-ui'
let __that = null

class EditHelperBIZ extends EditHelper {
    constructor() {
        super()

        __that = this
        this.isEditing = false
        this.drawType = 0;
        // TODO 挖洞功能待优化
        this.editDefaultParam = {
            curDrawType: 0,
            curOa: null
        }
    }
}

/**
 * 绘制
 * @param {*} type 1 线 2 曲线 3 面 4 体 5 光流 6 ODline 7 热力图 8 高亮区 9 辐射区 10地形开挖
 */
EditHelperBIZ.prototype.drawParam = function (type) {
    let that = this

    return new Promise(async (resolve, reject) => {
        if (type) that.drawType = type
        else type = that.drawType

        console.log('editedit', type);
        if (type == 10) {
            console.log('editedit');
            that.editDefaultParam.curDrawType = 3
            await window.sealAPI._tools.stopPolygonClip_biz()
            await that.setDrawParam(0, 1, 1)
        }
        resolve()
    })
}
/**
 * 设置绘制参数
 * @param {*} lineType number	线类型，0：直线，1：曲线，默认值是0
 * @param {*} buildType number	绘制类型，0：画多点线段， 1：画多边形， 默认值是0
 * @param {*} drawType number	绘制显示类型，0：线 1：平面， 默认值是0
 * @param {*} color any	颜色
 * @param {*} drawThickness number	默认值10，当DrawType为线时设置无效
 */
EditHelperBIZ.prototype.setDrawParam = function (lineType, buildType, drawType, _color) {
    let that = this
    console.log('editedit');
    return new Promise(async (resolve, reject) => {
        await that.setParam(lineType, buildType, _color || Color.Red)

        if (that.isEditing) resolve()
        else {
            await that.startDraw()
            addDblclick()
            resolve()
        }
    })
}

/**
 * 开始绘制
 */
EditHelperBIZ.prototype.startDraw = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.start()
        that.isEditing = true
        resolve()
    })
}
/**
 * 取消绘制
 */
EditHelperBIZ.prototype.cancelDraw = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!that.isEditing) resolve()

        await that.cancel()
        that.isEditing = false
        removeDblclick()
        resolve()
    })
}

/**
 * 绘制结束。调用此方法会结束当前的绘制，并在回调函数中返回绘制类型和坐标点，然后根据这些坐标点再创建相关的几何图形
 * @param {*} withOffset boolean 是否计算工程中心偏移，默认值是true
 */
EditHelperBIZ.prototype.finishDraw = function (withOffset = true) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let buildType = that.editDefaultParam.curDrawType

        let res = await that.finish(withOffset)
        that.isEditing = false

        // 去除最后由于双击绘制的重复点
        deplicate(res.coordinates)

        if (res.coordinates.length > 1) {
            removeDblclick()

            if (buildType) {
                for (let i = 0; i < res.coordinates.length; i++) {
                    res.coordinates[i][2] = -100
                }
                that.editDefaultParam.curOa = { coordinates: res.coordinates }
                await window.sealAPI._tools.startPolygonClip_biz(res.coordinates)
                resolve({ success: 1 })
            }
        } else {
            await that.drawParam()
            resolve({ success: 0 })
        }
    })
}

// 去除最后由于双击绘制的重复点
function deplicate(array) {
    if (array.length > 1 && array[array.length - 1][0] == array[array.length - 2][0]
        && array[array.length - 1][1] == array[array.length - 2][1]
        && array[array.length - 1][2] == array[array.length - 2][2]) {
        array.pop()
    }
}
function listenFinishDraw(event) {
    return new Promise(async (resolve, reject) => {
        let res = await __that.finishDraw(true)
        if (res.success === 0) ElMessage({
            message: '请先选择绘制区域',
            type: 'warning'
        })
    })
}
function addDblclick() {
    document.getElementById('player')
        .addEventListener('dblclick', listenFinishDraw, false)
}
function removeDblclick() {
    document.getElementById('player')
        .removeEventListener('dblclick', listenFinishDraw, false)
}


export default EditHelperBIZ