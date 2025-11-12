import Tools from './tools.class'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
const SysToolsCimStore = useSysToolsCimStore()

class ClipBIZ extends Tools {
    constructor() {
        super()

        this.setting = {
            0: [
                {
                    type: 'slider',
                    name: '高度',
                    key: 'height',
                    value: 0,
                    min: -100,
                    max: 500,
                    step: 1,
                }
            ],
            1: [
                {
                    type: 'slider',
                    name: '左负右正',
                    key: 'length',
                    value: 0,
                    min: -10000,
                    max: 10000,
                    step: 1,
                }
            ],
            2: [
                {
                    type: 'slider',
                    name: '长',
                    key: 'x',
                    value: 20,
                    min: 0,
                    max: 10000,
                    step: 1,
                },
                {
                    type: 'slider',
                    name: '宽',
                    key: 'y',
                    value: 20,
                    min: 0,
                    max: 10000,
                    step: 1,
                },
                {
                    type: 'slider',
                    name: '高',
                    key: 'z',
                    value: 20,
                    min: 0,
                    max: 10000,
                    step: 1,
                },
                {
                    type: 'radio',
                    name: '剖切方向',
                    value: 0,
                    key: 'direction',
                    options: [
                        { label: '正向', value: 0 },
                        { label: '反向', value: 1 },
                    ],
                },
            ]
        }
        this.option = {
            setting: [],
            curOa: {},
            curType: -1
        }

        this.isEditing = false
        this.Oa = []

        this.initPARAM()
    }
}


// 设置TileLayer图层参与剖切 && 禁止TileLayer图层参与剖切
let infotree = []
const setClip = async (ID, OPR) => {
    if (!window.Player || infotree.length === 0) return

    let _R = undefined
    let ids = infotree.map(i => i.iD)
    switch (OPR) {
        case 'ENA':
            if (!ID) return

            _R = { H: ids.filter(i => i !== ID), S: ID }
            break
        case 'DIS':
            _R = { H: [], S: ids }
            break
    }
    if (!_R) return

    if (_R.H.length > 0) await window.sealAPI._tileLayer.hide_biz(_R.H)
    if (_R.S.length > 0) await window.sealAPI._tileLayer.show_biz(_R.S)
}

ClipBIZ.prototype.getSetting = function () {
    return this.option
}

ClipBIZ.prototype.resetSetting = function () {
    this.option.setting = JSON.parse(JSON.stringify(this.setting[this.option.curType]))
    return this.option
}

/**
 * 重置参数：横剖切、纵剖切、体剖切
 * @param {*} type 
 * @param {*} data 
 */
ClipBIZ.prototype.resetPARAM = function (type, data) {
    this.setting[type] = data
}

/**
 * 开始剖切
 * @param {*} type  0横 1纵 2体
 * @returns 
 */
ClipBIZ.prototype.start = function (type) {
    if (type === undefined) return

    let that = this
    return new Promise(async (resolve, reject) => { // eslint-disable-next-line no-async-promise-executor
        that.option.curType = type
        that.isEditing = true
        that.option.setting = JSON.parse(JSON.stringify(that.setting[that.option.curType]))

        await that.clear(type)
        resolve()
    })
}


/**
 * 剖切回调函数（横、纵、体）
 * @param {*} data 
 * @returns 
 */
ClipBIZ.prototype.clipCallback = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {// eslint-disable-next-line no-async-promise-executor

        if (!that.isEditing) return resolve()

        switch (that.option.curType) { // 0横 1纵 2体
            case 0:
                await that.stopPlaneClip()
                // await setClip(data.Id, 'ENA')

                that.option.curOa.location = data.MouseClickPoint
                that.option.setting[0].value = data.MouseClickPoint[2]
                that.option.curOa.rotation = [0, 0, 90]

                await that.startPlaneClip(that.option.curOa.location, [0, 0, -90], true, true)
                break
            case 1:
                await that.stopPlaneClip()
                // await setClip(data.Id, 'ENA')

                that.option.curOa.location = data.MouseClickPoint
                that.option.setting[0].value = 0
                that.option.curOa.rocationy = data.MouseClickPoint[1]
                that.option.curOa.rotation = [0, 0, 0]

                await that.startPlaneClip(that.option.curOa.location, [0, 0, 0], true, true)
                break
            case 2:
                await that.stopVolumeClip()
                // await setClip(data.Id, 'ENA')

                that.option.curOa.location = data.MouseClickPoint
                for (let i in that.option.setting) {
                    that.option.curOa[that.option.setting[i].key] = that.option.setting[i].value
                }
                that.option.curOa.volumePoint = [
                    that.option.curOa.location[0] - that.option.curOa.x / 2,
                    that.option.curOa.location[1] - that.option.curOa.y / 2,
                    that.option.curOa.location[2] - that.option.curOa.z / 2,
                    that.option.curOa.location[0] + that.option.curOa.x / 2,
                    that.option.curOa.location[1] + that.option.curOa.y / 2,
                    that.option.curOa.location[2] + that.option.curOa.z / 2]

                await that.startVolumeClip(that.option.curOa.volumePoint, that.option.curOa.direction, true, true
                    , [0, 0, 0])
                // await that.updateVolumeClip(that.option.curOa.volumePoint, that.option.curOa.direction, false, false, [0, 0, 0])

                break
        }

        // console.log(window.Player, 'dwadwadwadwad');
        // console.log(window.Player.func, 'dwadwadwadwad');
        // console.log(window.Player.func.isShowClip, 'isisisisisis');
        // if (window.Player && window.Player.func) {
        //     window.Player.func.value.isShowClip.value = true
        // }\
        SysToolsCimStore.SET_ISSHOWCLIP(true)
        resolve()
    })
}


/**
 * 更新绘制基础模型
 */
ClipBIZ.prototype.update = function () {
    let that = this
    return new Promise(async (resolve, reject) => {// eslint-disable-next-line no-async-promise-executor

        switch (that.option.curType) {
            case 0:
                await that.stopPlaneClip()

                that.option.curOa.location[2] = that.option.curOa.height
                await that.startPlaneClip(that.option.curOa.location, [0, 0, -90], true, true)
                break
            case 1:
                await that.stopPlaneClip()

                that.option.curOa.location[1] = that.option.curOa.length
                await that.startPlaneClip(that.option.curOa.location, [0, 0, 0], true, true)
                break
            case 2:
                await that.stopVolumeClip()

                that.option.curOa.volumePoint = [
                    that.option.curOa.location[0] - that.option.curOa.x / 2,
                    that.option.curOa.location[1] - that.option.curOa.y / 2,
                    that.option.curOa.location[2] - that.option.curOa.z / 2,
                    that.option.curOa.location[0] + that.option.curOa.x / 2,
                    that.option.curOa.location[1] + that.option.curOa.y / 2,
                    that.option.curOa.location[2] + that.option.curOa.z / 2]
                await that.startVolumeClip(that.option.curOa.volumePoint, that.option.curOa.direction, false, false, [0, 0, 0])
                break
        }

        resolve()
    })
}

/**
 * 清除剖切效果，恢复场景
 * @param {*} type 0横 1纵 2体
 * @returns 
 */
ClipBIZ.prototype.clear = function (type = null) {
    let that = this
    return new Promise(async (resolve, reject) => {// eslint-disable-next-line no-async-promise-executor

        if (type === null) that.isEditing = false
        type = type === null ? that.option.curType : type
        switch (type) {
            case 0:
            case 1:
                await that.stopPlaneClip()
                break
            case 2:
                await that.stopVolumeClip()
                break
        }
        // await setClip(null, 'DIS')
        resolve()
    })
}

/**
 * 初始化剖切参数
 */
ClipBIZ.prototype.initPARAM = function () {
    let that = this
    return new Promise((resolve, reject) => {// eslint-disable-next-line no-async-promise-executor

        // TODO 2305 获取图层树（定向剖切）
        window.origAPI.infoTree.get((resp) => {
            infotree = resp.infotree
            resolve()
        })
    })
}
export default ClipBIZ