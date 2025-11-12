import RadiationPoint from './radiationPoint.class.js'

class RadiationPointBIZ extends RadiationPoint {
    constructor() {
        super()

        this.Oa = []
    }
}

/**
 * 添加数据
 * @param {*} id 
 * @param {*} coordinate 
 * @param {*} radius 
 * @param {*} rippleNumber 
 * @param {*} color 
 * @param {*} brightness 
 * @returns 
 */
RadiationPointBIZ.prototype.read = function (id, coordinate, radius, rippleNumber, color, brightness) {
    let o = new RadiationPointData(id, coordinate, radius, rippleNumber, color, brightness)
    return o
}

/**
 * 插入数据 
 * @param {} oa 
 * @param {*} un  默认插入开头 
 */
RadiationPointBIZ.prototype.addData = function (oa, un = true) {
    if (un) {
        this.Oa.unshift(oa)
    } else {
        this.Oa.push(oa)
    }
}

/**
 * 
 * @param {*} json 
 * @param {*} show 
 * @returns 
 */
RadiationPointBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return
    let that = this
    return new Promise(async (resolve, reject) => {
        let oaList = []
        for (let i = 0; i < json.length; i++) {
            if (json[i].pType === 2) {
                let oas = [], obj = {}
                for (let j = 0; j < json[i].oa.length; j++) {
                    let o = await that.read(json[i].oa[j].id, json[i].oa[j].coordinate, json[i].oa[j].radius, json[i].oa[j].rippleNumber, json[i].oa[j].color, json[i].oa[j].brightness)

                    o['checked'] = json[i].checked
                    oas.push(o)
                    oaList.push(o)
                }
                obj['name'] = json[i].name
                obj['checked'] = json[i].checked
                obj['pType'] = 2
                obj['oa'] = oas
                that.Oa.push(obj)
                continue
            }
            let o = await that.read(json[i].id, json[i].coordinate, json[i].radius, json[i].rippleNumber, json[i].color, json[i].brightness)
            o['name'] = json[i].name
            o["pType"] = 0
            that.Oa.push(o)
            oaList.push(o)
        }

        await that.add(oaList)
        if (show) {
            let hideOa = oaList.filter(item => !item.checked)
            if (hideOa.length > 0) await that.hide(hideOa)
        } else await that.hide(oaList)
        resolve()
    })
}

/**
 * 
 * @param {*} json 
 */
RadiationPointBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa)
    // for (let i = 0 i < this.Oa.length i++) {
    //     json.push(
    //         {
    //             id: this.Oa[i].id,
    //             coordinate: this.Oa[i].coordinate,
    //             radius: this.Oa[i].radius,
    //             rippleNumber: this.Oa[i].rippleNumber,
    //             color: this.Oa[i].color,
    //             brightness: this.Oa[i].brightness
    //         }
    //     )
    // }
}

/**
 * 
 * @param {*} data 
 * @returns 
 */
RadiationPointBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}

/**
 * 
 * @param {*} data 
 * @returns 
 */
RadiationPointBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

/**
 * 
 * @param {*} data 
 * @returns 
 */
RadiationPointBIZ.prototype.delete_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.delete(ids)
        resolve()
    })
}

export default RadiationPointBIZ