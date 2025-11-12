import HeatMap from './heatmap.class'
import util from './js/util.js'

class HeatMapBIZ extends HeatMap {
    constructor() {
        super()

        this.Oa = []
    }
}

/**
 * 显示HeatMap
 */
HeatMapBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

/**
 * 隐藏HeatMap
 */
HeatMapBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}

/**
 * 删除场景中所有的HeatMap
 */
HeatMapBIZ.prototype.delete_biz = function (data) {
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
HeatMapBIZ.prototype.addData = function (data, un = true) {
    if (un) this.Oa.unshift(data)
    else this.Oa.push(data)
}

/**
 * 序列化
 * @param {*} json 
 */
HeatMapBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa)
}

/**
 * 解析json
 */
HeatMapBIZ.prototype.read = function (url, id, checked = true, name = '') {
    var that = this
    return new Promise((resolve, reject) => {
        util.readFile(url).then(async (data) => {
            if (!(data && data.pointdata instanceof Array
                && data.pointdata.length > 0
                && data.pointdata[0].coord.length === 3
                && data.pointdata[0].radius
                && data.pointdata[0].heatValue)) {
                resolve({ success: 0 })
            }
            var minx = 100000000, miny = 100000000, minz = 100000000, maxx = -100000000, maxy = -100000000, maxz = -100000000
            let pointData = []
            let range = [0, 100]
            let minHeatValue = 100000, maxHeatValue = -100000;
            (data.pointdata || []).forEach(item => {
                if (item.coord[0] < minx) minx = item.coord[0]
                if (item.coord[1] < miny) miny = item.coord[1]
                if (item.coord[0] > maxx) maxx = item.coord[0]
                if (item.coord[1] > maxy) maxy = item.coord[1]
                if (item.coord[2] < minz) minz = item.coord[2]
                if (item.coord[2] > maxz) maxz = item.coord[2]
                if (item.heatValue < minHeatValue) minHeatValue = item.heatValue
                if (item.heatValue > maxHeatValue) maxHeatValue = item.heatValue

                let o = new HeatMapPointData(id + i, item.coord, item.radius, item.heatValue)
                pointData.push(o)
            })

            range[0] = minHeatValue
            range[1] = maxHeatValue
            var bbox = [minx, miny, minz - 50, maxx, maxy, maxz + 50]
            that.addData({
                url: url,
                id: id,
                checked: checked,
                name: name
            })
            await that.add(id, bbox, range, pointData)

            if (checked) resolve({ success: 1 })
            else {
                await that.hide(id)
                resolve({ success: 1 })
            }
        }).catch(() => {
            resolve({ success: 0 })
        })
    })
}

/**
 * 反序列化
 * @param {*} json 
 * @returns 
 */
HeatMapBIZ.prototype.deserialize = function (json) {
    if (!json) return

    let that = this
    return new Promise((resolve, reject) => {
        json.forEach(async (item) => {
            await that.read(item.url, item.id, item.checked, item.name)
        })
    })
}

export default HeatMapBIZ