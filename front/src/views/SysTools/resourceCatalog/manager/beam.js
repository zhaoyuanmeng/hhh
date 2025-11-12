import Beam from './beam.class'

class BeamBIZ extends Beam {
    constructor() {
        super()

    }
}

/**
 * 显示Beam
 */
BeamBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}
/**
 * 隐藏Beam
 */
BeamBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}
/**
 * 删除一个或多个Beam对象
 */
BeamBIZ.prototype.delete_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.delete(ids)
        resolve()
    })
}


export default BeamBIZ