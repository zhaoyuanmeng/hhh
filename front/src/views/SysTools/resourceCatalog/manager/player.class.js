class Player {
    constructor() { }

    /**
     * 重新调整布局
     * @returns 可选的回调函数
     */
    resize = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.player.resize(() => {
                resolve()
            })
        })
    }
    /**
     * 立即重启实例
     * @returns 可选的回调函数
     */
    focus = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.player.container.firstChild.focus(() => {
                resolve()
            })
        })
    }
}

export default Player