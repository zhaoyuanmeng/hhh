import CameraTour from './cameraTour.class'

class CameraTourBIZ extends CameraTour {
    constructor() {
        super()

        // this.cameraTourOa = []
        this.curOa = {
            id: '',
            name: '',
            time: 0,
            keyFrames: []
        }
    }
}


/**
* @param {*} type 1 新增 2 更新 3 插入 4 删除
* @param {*} i 
 * @returns 
 */
CameraTourBIZ.prototype.setKeyFrames = function (type, i, newTime) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let res = await window.sealAPI._camera.get()

        let _location = res.camera.slice(0, 3)
        let _rotation = res.camera.slice(3, 6)
        switch (type) {
            case 1: // 新增
                {
                    let fk1 = new CameraTourKeyFrame(that.curOa.keyFrames.length, that.curOa.time, _location, _rotation)
                    that.curOa.keyFrames.push(fk1)
                    break
                }
            case 2: // 更新
                {
                    that.curOa.keyFrames[i].location = _location
                    that.curOa.keyFrames[i].rotation = _rotation

                    let disTime = i > 0 ? newTime - (that.curOa.keyFrames[i].time - that.curOa.keyFrames[i - 1].time) : newTime
                    for (let j = i; j < that.curOa.keyFrames.length; j++) {
                        that.curOa.keyFrames[j].time += disTime
                    }
                    break
                }
            case 3: // 插入
                {
                    let fk3 = new CameraTourKeyFrame(i + 1, that.curOa.keyFrames[i].time + newTime, _location, _rotation)
                    that.curOa.keyFrames.splice(i + 1, 0, fk3)
                    if (that.curOa.keyFrames.length - i > 2) {
                        for (let j = i + 2; j < that.curOa.keyFrames.length; j++) {
                            that.curOa.keyFrames[j].index += 1
                            that.curOa.keyFrames[j].time += newTime
                        }
                    }
                    break
                }
            case 4: // 删除
                if (that.curOa.keyFrames.length > i) {
                    for (let j = i; j < that.curOa.keyFrames.length; j++) {
                        that.curOa.keyFrames[j].index -= 1
                        that.curOa.keyFrames[j].time -= newTime
                    }
                }
                break
        }
        resolve()
    })
}

CameraTourBIZ.prototype.start = function () {
    let that = this
    return new Promise((resolve, reject) => {
        that.curOa.keyFrames = []
        that.curOa.time = 0
        resolve()
    })
}

CameraTourBIZ.prototype.update = function () {
    let that = this
    return new Promise((resolve, reject) => {
        let disTime = 0
        if (that.curOa.keyFrames.length > 0 && that.curOa.keyFrames[0].time > 0) {
            disTime = that.curOa.keyFrames[0].time
        }
        for (let i in that.curOa.keyFrames) {
            that.curOa.keyFrames[i].index = Number(i)
            that.curOa.keyFrames[i].time -= disTime
        }
        resolve()
    })
}

CameraTourBIZ.prototype.cancel = function () {
    let that = this
    return new Promise((resolve, reject) => {
        that.curOa.keyFrames = []
        that.curOa.time = 0
        resolve()
    })
}

CameraTourBIZ.prototype.finish = function () {
    let that = this
    return new Promise((resolve, reject) => {
        // that.cameraTourOa.push(JSON.parse(JSON.stringify(that.curOa)))
        that.curOa.keyFrames = []
        that.curOa.time = 0
        that.curOa.name = ''
        resolve()
    })
}

CameraTourBIZ.prototype.play_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        that.curOa.id = 'camera' + Date.now()
        let newKeyFrames = []
        for (let i = 0; i < that.curOa.keyFrames.length; i++) {
            let cur = {
                index: that.curOa.keyFrames[i].index,
                time: that.curOa.keyFrames[i].time,
                location: that.curOa.keyFrames[i].location,
                rotation: that.curOa.keyFrames[i].rotation,
            }
            newKeyFrames.push(cur)
        }
        await that.add(new CameraTourData(that.curOa.id, that.curOa.name, newKeyFrames))
        await that.play(that.curOa.id)
        resolve()
    })
}

CameraTourBIZ.prototype.stop_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.stop()
        await that.delete(that.curOa.id)
        resolve()
    })
}

// /**
//  * 反序列化
//  * @param {*} json 
//  */
// CameraTourBIZ.prototype.deserialize = function (json) {
//     this.cameraTourOa = json
// }

// /**
//  * 序列化
//  * @param {*} json 
//  */
// CameraTourBIZ.prototype.serialize = function (json) {
//     json = this.cameraTourOa
// }

export default CameraTourBIZ