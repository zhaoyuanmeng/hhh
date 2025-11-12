import Polygon from './polygon.class.js'
import util from './js/util.js'

class PolygonBIZ extends Polygon {
    constructor() {
        super()

        this.Oa = []
        this.groups = []
    }
}

/**
 * 插入数据 
 * @param {} oa 
 * @param {*} un  默认插入开头 
 */
PolygonBIZ.prototype.addData = function (oa, un = true) {
    if (un) this.Oa.unshift(oa)
    else this.Oa.push(oa)

}

PolygonBIZ.prototype.read = function (url, z, color, frameColor, frameThickness, depthTest, checked = false) {
    return new Promise((resolve, reject) => {
        util.read(url, z).then(function (data) {
            let features = data.features
            let groupName = data.name

            let polygonOa = []
            for (let i = 0; i < features.length; i++) {
                let feature = features[i]
                let coordinates = feature.geometry.coordinates[0][0]
                let o = new PolygonData(groupName + i, color, coordinates, frameColor, frameThickness)
                o.depthTest = depthTest
                o.groupName = groupName
                polygonOa.push(o)
            }
            resolve({ name: groupName, oa: polygonOa, pType: 1, checked: checked })
        })
    })
}

/**
 * 反序列化
 * @param {*} json 
 * @param {*} show 
 * @returns 
 */
PolygonBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return
    let that = this
    return new Promise(async (resolve, reject) => {
        for (let i = 0; i < json.length; i++) {
            if (json[i].pType === 0) {
                let oa = new PolygonData(json[i].id, json[i].color, json[i].coordinates, json[i].frameColor, json[i].frameThickness)
                oa.depthTest = json[i].depthTest
                oa['pType'] = 0
                oa['name'] = json[i].name
                oa['checked'] = json[i].checked
                that.Oa.push(oa)
            }
            else {
                that.groups.push({
                    pType: 1,
                    url: json[i].url,
                    Z: json[i].Z,
                    color: json[i].color,
                    frameColor: json[i].frameColor,
                    frameThickness: json[i].frameThickness,
                    depthTest: json[i].depthTest
                })
                that.read(json[i].url, json[i].Z, json[i].color, json[i].frameColor, json[i].frameThickness, json[i].depthTest, json[i].checked).then(async (o) => {
                    await that.add(o.oa)
                    if (!o.checked) that.hide(o.oa)
                    that.Oa.push(o)
                })
            }
        }

        await that.add(that.Oa)
        if (show) {
            let hideOa = that.Oa.filter(item => !item.checked)
            if (hideOa.length > 0) await that.hide(hideOa)
        } else await that.hide(that.Oa)

        resolve()
    })
}

/**
 * 序列化
 * @param {*} json 
 */
PolygonBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa.filter(item => item.pType !== 1))
    json.push(...this.groups)
}
/**
 * 显示Polygon
 */
PolygonBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}
/**
 * 隐藏Polygon
 */
PolygonBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}
/**
 * 根据Id删除
 * @param {*} data  string | object | array(两种形式)   例子 '12' 、['23','23']、{id:'123'}、[{id:'123'},{id:'123'}]
 */
PolygonBIZ.prototype.delete_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.delete(ids)
        resolve()
    })
}
/**
 * 自动定位到合适的观察距离
 */
PolygonBIZ.prototype.focus_biz = function (data, distance, flyTime) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.focus(ids, distance, flyTime)
        resolve()
    })
}

export default PolygonBIZ