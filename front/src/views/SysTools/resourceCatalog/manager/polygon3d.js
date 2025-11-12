import Polygon3D from './polygon3d.class.js'
import util from './js/util.js'

class Polygon3DBIZ extends Polygon3D {
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
Polygon3DBIZ.prototype.addData = function (oa, un = true) {
    if (un) this.Oa.unshift(oa)
    else this.Oa.push(oa)
}

/**
 * 解析json
 * @param {*} url 
 * @param {*} z 
 * @param {*} type 
 * @param {*} color 
 * @param {*} height 
 * @param {*} intensity 
 * @returns 
 */
Polygon3DBIZ.prototype.read = function (url, z, type, color, height, intensity, depthTest, checked = false) {
    return new Promise((resolve, reject) => {
        util.read(url, z).then(function (data) {
            let features = data.features
            let groupName = data.name

            let polygon3dOa = []
            for (let i = 0; i < features.length; i++) {
                let feature = features[i]
                let coordinates = feature.geometry.coordinates[0][0]
                let o = new Polygon3DData(groupName + i, type, coordinates, color, height, intensity)
                o.depthTest = depthTest
                o.groupName = groupName
                polygon3dOa.push(o)
            }
            resolve({ name: groupName, oa: polygon3dOa, pType: 1, checked: checked })
        })
    })
}

/**
 * 反序列化
 * @param {*} json 
 * @param {*} show 
 * @returns 
 */
Polygon3DBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return
    let that = this
    return new Promise(async (resolve, reject) => {
        for (let i = 0; i < json.length; i++) {
            if (json[i].pType === 0) {
                let oa = new Polygon3DData(json[i].id, json[i].type, json[i].coordinates, json[i].color, json[i].height, json[i].intensity)
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
                    color: json[i].type,
                    height: json[i].height,
                    intensity: json[i].intensity,
                    type: json[i].type
                })
                that.read(json[i].url, json[i].Z, json[i].type, json[i].color, json[i].height, json[i].intensity, json[i].depthTest, json[i].checked).then(async (o) => {
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
Polygon3DBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa.filter(item => item.pType !== 1))
    json.push(...this.groups)
}

Polygon3DBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

Polygon3DBIZ.prototype.hide_biz = function (data) {
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
Polygon3DBIZ.prototype.delete_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.delete(ids)
        resolve()
    })
}

export default Polygon3DBIZ