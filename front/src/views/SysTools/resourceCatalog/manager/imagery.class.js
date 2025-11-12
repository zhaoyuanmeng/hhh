class Imagery {
    constructor() { }

    /**
     * 删除一个或多个ImageryLayer图层对象
     * @param {string | array} ids 要删除的ImageryLayer对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.imagery.delete(ids, () => {
                resolve()
            })
        })
    }

}

export default Imagery