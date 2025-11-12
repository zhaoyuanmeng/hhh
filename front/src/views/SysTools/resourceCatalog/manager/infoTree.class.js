class InfoTree {
    constructor() { }

    /**
     * 获取图层树信息
     * @returns 可选的回调函数
     */
    get = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.infoTree.get((resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 显示图层
     * @param {string | array} ids 要显示的图层ID（支持单个ID或ID数组）
     * @returns 可选的回调函数
     */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.infoTree.show(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏图层
     * @param {string | array} ids 要隐藏的图层ID（支持单个ID或ID数组）
     * @returns 可选的回调函数
     */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.infoTree.hide(ids, () => {
                resolve()
            })
        })
    }

}

export default InfoTree