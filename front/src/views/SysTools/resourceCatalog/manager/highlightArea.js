import HighlightArea from './highlightArea.class'
import util from './js/util.js'

class HighlightAreaBIZ extends HighlightArea {
    constructor() {
        super()

        this.Oa = []
        this.groups = []
    }
}

/**
 * 显示HighlightArea
 */
HighlightAreaBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

/**
 * 隐藏HighlightArea
 */
HighlightAreaBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}

/**
 * 删除一个或多个HighlightArea对象
 */
HighlightAreaBIZ.prototype.delete_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.delete(ids)
        resolve()
    })
}

/**
 * 插入数据 
 * @param {*} data 
 * @param {boolean} un 默认插入开头 
 */
HighlightAreaBIZ.prototype.addData = function (data, un = true) {
    if (un) this.Oa.unshift(data)
    else this.Oa.push(data)
}

/**
 * 序列化
 * @param {*} json 
 */
HighlightAreaBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa.filter(item => item.pType !== 1))
    json.push(...this.groups)
}

/**
 * 解析geojson
 * @param {*} url 
 * @param {*} z 
 */
HighlightAreaBIZ.prototype.read = function (url, z, color, heightRange, intensity, checked = false) {
    return new Promise((resolve, reject) => {
        util.read(url, z).then(function (data) {
            let groupName = data.name

            let highlightAreaOa = [];
            (data.features || []).forEach(feature => {
                let coordinates = feature.geometry.coordinates[0][0]
                let o = new HighlightAreaData(groupName + i, coordinates, color, heightRange, intensity)
                o.groupName = groupName
                highlightAreaOa.push(o)
            })

            resolve({ name: groupName, oa: highlightAreaOa, pType: 1, checked: checked })
        })
    })
}

/**
 * 反序列化
 * @param {*} json 
 * @param {*} show 
 * @returns 
 */
HighlightAreaBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return

    let that = this
    return new Promise(async (resolve, reject) => {
        json.forEach(async (item) => {
            if (item.pType === 0) {
                let oa = new HighlightAreaData(item.id, item.coordinates, item.color, item.heightRange, item.intensity)
                oa['pType'] = 0
                oa['name'] = item.name
                oa['checked'] = item.checked

                that.Oa.push(oa)
            } else {
                that.groups.push({
                    pType: 1,
                    url: item.url,
                    Z: item.Z,
                    color: item.color,
                    heightRange: item.heightRange,
                    intensity: item.intensity,
                })
                that.read(item.url, item.Z, item.color, item.heightRange, item.intensity, item.checked).then(async (o) => {
                    await that.add(o.oa)
                    if (!o.checked) that.hide(o.oa)
                    that.Oa.push(o)
                })
            }
        })

        await that.add(that.Oa)

        if (show) {
            let hideOa = that.Oa.filter(item => !item.checked)
            if (hideOa.length > 0) await that.hide(hideOa)
        } else await that.hide(that.Oa)

        resolve()
    })
}


export default HighlightAreaBIZ