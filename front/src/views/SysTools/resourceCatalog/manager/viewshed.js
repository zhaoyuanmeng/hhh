
import Viewshed from './viewshed.class'

class ViewshedBIZ extends Viewshed {
    constructor() {
        super()

        this.option = {
            coordinate: [0, 0, 0],
            fov: 70,
            radius: 1000,
            direction: 120,
            curOa: {
                fov_h: 60,
                fov_v: 30,
                height: 0,
                visibleColor: [0, 1, 0, 1],
                invisibleColor: [1, 0, 0, 1],
                interactiveLock:false
            }
        }
        this.isEditing = false
        this.Oa = []
    }
}

/**
 * 添加 
 * @param {*} data ViewshedData
 */
ViewshedBIZ.prototype.add_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!data) resolve()

        await that.add()
        resolve()
    })
}

/**
 * 开始
 */
ViewshedBIZ.prototype.start = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        that.option.curOa.height = 0
        if (that.isEditing) await that.cancel()

        await window.sealAPI._tools.startViewshedAnalysis(that.option.curOa)
        that.isEditing = true
        resolve()
    })
}

ViewshedBIZ.prototype.reset = function () {
    return new Promise(async (resolve, reject) => {
        await window.sealAPI._tools.stopViewshedAnalysis()
        await window.sealAPI._tools.startViewshedAnalysis(this.option.curOa)
        resolve()
    })
}

/**
 * 更新绘制基础模型
 */
ViewshedBIZ.prototype.update = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        await window.sealAPI._tools.startViewshedAnalysis(that.option.curOa)
        resolve()
    })
}

/**
 * 取消
 */
ViewshedBIZ.prototype.cancel = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!that.isEditing) resolve()

        await window.sealAPI._tools.stopViewshedAnalysis()
        that.isEditing = false
        resolve()
    })
}

/**
 * 结束
 * @returns 
 */
ViewshedBIZ.prototype.finish = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        let curId = 'editviewshed' + (new Date().getTime())
        let o = new ViewshedData(curId)
        o.coordinate = that.option.coordinate
        o.fov = that.option.fov
        o.radius = that.option.radius
        o.direction = that.option.direction
        o.utype = 'viewshed'
        Object.assign(that.option.curOa, o)

        await that.add(o)
        resolve({ success: 1 })
    })
}

ViewshedBIZ.prototype.clear_biz = function (apiOBJ) {
    let that = this
    return new Promise(async (resolve, reject) => {
        await apiOBJ._tools.stopViewshedAnalysis()
        that.isEditing = false
        resolve()
    })
}

ViewshedBIZ.prototype.getSetting = function () {
    return this.option
}

/**
 * 插入数据 
 * @param {} oa 
 * @param {*} un  默认插入开头 
 */
ViewshedBIZ.prototype.addData = function (oa, un = true) {
    if (un) this.Oa.unshift(oa)
    else this.Oa.push(oa)
}

/**
 * 序列化
 * @param {*} json 
 */
ViewshedBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa)
}

/**
 * 反序列化
 * @param {*} json 
 * @param {*} show 
 * @returns 
 */
ViewshedBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return

    let that = this
    return new Promise(async (resolve, reject) => {
        json.forEach(item => {
            let oa = new ViewshedData(item.id, item.coordinate, item.fov, item.radius, item.direction)
            oa['pType'] = 0
            oa['name'] = item.name
            that.Oa.push(oa)
        })

        await that.add_biz(that.Oa)
        if (show) {
            let hideOa = that.Oa.filter(item => !item.checked)
            if (hideOa.length === 0) resolve()

            await that.hide(hideOa)
        } else await that.hide(that.Oa)

        resolve()
    })
}

export default ViewshedBIZ