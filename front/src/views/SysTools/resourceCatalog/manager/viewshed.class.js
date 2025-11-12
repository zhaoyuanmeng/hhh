class Viewshed {
    constructor() { }

    /**
     * 添加
     * @param {*} data ViewshedData
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.viewshed.add(data, () => {
                resolve()
            })
        })
    }
}

export default Viewshed