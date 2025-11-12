import ODline from './odline.class.js'

class ODlineBIZ extends ODline {
    constructor() {
        super()

        this.Oa = []
    }
}

/**
 * 插入数据 
 * @param {} oa 
 * @param {*} un  默认插入开头 
 */
ODlineBIZ.prototype.addData = function (oa, un = true) {
    if (un) this.Oa.unshift(oa)
    else this.Oa.push(oa)
}

ODlineBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

ODlineBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}

ODlineBIZ.prototype.delete_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.delete(ids)
        resolve
    })
}

ODlineBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return
    let that = this
    return new Promise(async (resolve, reject) => {
        let oaList = []
        for (let i = 0; i < json.length; i++) {
            if (json[i].pType === 2) {
                let oas = [], obj = {}
                for (let j = 0; j < json[i].oa.length; j++) {
                    let oa = new ODLineData(json[i].oa[j].id)
                    Object.assign(oa, json[i].oa[j])
                    oa['checked'] = json[i].checked
                    oas.push(oa)
                    oaList.push(oa)
                }
                obj['name'] = json[i].name
                obj['checked'] = json[i].checked
                obj['pType'] = 2
                obj['oa'] = oas
                that.Oa.push(obj)
                continue
            }
            let o = new ODLineData(json[i].id)
            Object.assign(o, json[i])
            // o.color = json[i].color
            // o.coordinates = json[i].coordinates
            // o.flowRate = json[i].flowRate
            // o.brightness = json[i].brightness
            // o.bendDegree = json[i].bendDegree
            // o.tiling = json[i].tiling

            // o.lineThickness = json[i].lineThickness
            // o.flowPointSizeScale = json[i].flowPointSizeScale
            // o.labelSizeScale = json[i].labelSizeScale

            // o.lineShape = json[i].lineShape  //ODLine模型样式 0:平面 1:柱体，默认值1
            // o.lineStyle = json[i].lineStyle  //ODLine材质样式 0:纯色 1:箭头，2:流动点，默认值0
            // o.flowShape = json[i].flowShape  //ODLine发光点样式 0:无 1:球体，默认值0

            // o.startPointShape = json[i].startPointShape
            // o.endPointShape = json[i].endPointShape
            // o.startLabelShape = json[i].startLabelShape
            // o.endLabelShape = json[i].endLabelShape
            // o['name'] = json[i].name
            // o['pType'] = 0
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

ODlineBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa)
}

export default ODlineBIZ