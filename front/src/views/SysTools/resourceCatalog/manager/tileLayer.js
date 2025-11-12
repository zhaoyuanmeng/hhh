import TileLayer from './tileLayer.class'
import axios from 'axios'

class TileLayerBIZ extends TileLayer {
    constructor() {
        super()

        // 【属性查询】属性信息
        this.attrInfo = []
    }
}

/**
 * 显示TileLayer图层
 */
TileLayerBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

/**
 * 隐藏TileLayer图层
 */
TileLayerBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}

/**
 * 自动定位到合适的观察距离
 */
TileLayerBIZ.prototype.focus_biz = function (data, distance, flyTime, rotation) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.focus(ids, distance, flyTime, rotation)
        resolve()
    })
}

/**
 * 【属性查询】获取属性信息
 * @param {string} propertyName 构件名称
 * @param {string} objectID ObjectID
 * @param {string} Id 构件ID
 */
TileLayerBIZ.prototype.getInfoByObjectID = function (propertyName, objectID, Id) {
    let that = this
    if (propertyName === '给排水') {
        propertyName = 'geipaishui'
    }
    let name = ['幕墙', 'geipaishui', '建筑', '结构', '空调风', '空调水', '喷淋', '强电', '弱电', '消火栓', '照明']
    if (window.Player && window.Player.$refs.func) window.Player.$refs.func.isShowAttr = false
    if (name.indexOf(propertyName) !== -1) {
        axios({
            method: 'GET',
            url: infoQueryUrl + '/propertySet/queryPropertySetByUniqueId?directory=' + propertyName + '&uniqueId=' + objectID,
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Access-Control-Allow-Origin': '*' // cors错误是因为请求头没加Access-Control-Allow-XXX信息
            },
        }).then(async (res) => {
            // 将查询结果展示出来
            if (res.data.rows) {
                that.attrInfo = res.data.rows[0].porpertys
                if (window.Player && window.Player.$refs.func) window.Player.$refs.func.isShowAttr = true
                await that.stopHighlightActor()
                await that.highlightActor(Id, objectID)
            }
        }).catch(error => {
            console.log(error)
        })
    }
}

/**
 * TileLayer的样式
 */
TileLayerBIZ.prototype.setStyle_biz = function (data, style = 0, color = [1, 0, 1, 0.0381]) {
	let that = this
	return new Promise(async (resolve, reject) => {
			let ids = window.sealAPI.getIds(data)
			if (!ids) resolve()

			await that.setStyle(ids, Number(style), color)
			resolve()
	})
}


export default TileLayerBIZ