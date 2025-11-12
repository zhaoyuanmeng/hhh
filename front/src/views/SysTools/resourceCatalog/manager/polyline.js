import Polyline from './polyline.class.js'
import util from './js/util.js'

class PolylineBIZ extends Polyline {
    constructor() {
        super()

        this.Oa = []
        this.groups = []
    }
}

/**
 * 响应事件点击事件
 * @param {*} data 
 * @returns 
 */
PolylineBIZ.prototype.onEvent = function (data) {
    return false
}

/**
 * 插入数据 
 * @param {} oa 
 * @param {*} un  默认插入开头 
 */
PolylineBIZ.prototype.addData = function (oa, un = true) {
    if (un) this.Oa.unshift(oa)
    else this.Oa.push(oa)
}

/**
 * 解析geojson
 * @param {*} url 
 * @param {*} z 
 */
PolylineBIZ.prototype.read = function (url, z, color, style, thickness, brightness, flowRate, depthTest, checked = false) {
    return new Promise((resolve, reject) => {
        util.read(url, z).then(function (data) {
            let features = data.features
            let groupName = data.name

            let polylineOa = []
            for (let i = 0; i < features.length; i++) {
                let feature = features[i]
                let coordinates = feature.geometry.coordinates[0][0]
                let o = new PolylineData(groupName + i, color, coordinates, style, thickness, brightness, flowRate)
                o.depthTest = depthTest
                o.groupName = groupName
                polylineOa.push(o)
            }
            resolve({ name: groupName, oa: polylineOa, pType: 1, checked: checked })
        })
    })
}

/**
 * 反序列化
 * @param {*} json 
 * @param {*} show 
 * @returns 
 */
PolylineBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return
    let that = this
    return new Promise(async (resolve, reject) => {
        for (let i = 0; i < json.length; i++) {
            if (json[i].pType === 0) {
                let oa = new PolylineData(json[i].id, json[i].color, json[i].coordinates, json[i].style, json[i].thickness, json[i].brightness, json[i].flowRate)
                oa.depthTest = json[i].depthTest
                oa["name"] = json[i].name
                oa['checked'] = json[i].checked
                oa["pType"] = 0
                that.Oa.push(oa)
            } else if (json[i].pType === 1) {
                that.groups.push({
                    pType: 1,
                    url: json[i].url,
                    Z: json[i].Z,
                    color: json[i].color,
                    style: json[i].style,
                    thickness: json[i].thickness,
                    brightness: json[i].brightness,
                    flowRate: json[i].flowRate,
                    depthTest: json[i].depthTest
                })
                that.read(json[i].url, json[i].Z, json[i].color, json[i].style, json[i].thickness, json[i].brightness, json[i].flowRate, json[i].depthTest, json[i].checked).then(async (o) => {
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
PolylineBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa.filter(item => item.pType !== 1))
    json.push(...this.groups)
}

PolylineBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

PolylineBIZ.prototype.hide_biz = function (data) {
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
 * @param {*} data string | object | array(两种形式)   例子 '12' 、['23','23']、{id:'123'}、[{id:'123'},{id:'123'}]
 * @param {*} apiOBJ SealAirCityAPI
 */
PolylineBIZ.prototype.delete_biz = function (data, apiOBJ) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let __apiOBJ = apiOBJ || window.sealAPI
        let ids = __apiOBJ.getIds(data)
        if (!ids) resolve()

        await that.delete(ids)
        resolve()
    })
}
/**
 * 自动定位到合适的观察距离
 */
PolylineBIZ.prototype.focus_biz = function (data, distance, flyTime, rotation) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.focus(ids, distance, flyTime, rotation)
        resolve()
    })
}

export default PolylineBIZ