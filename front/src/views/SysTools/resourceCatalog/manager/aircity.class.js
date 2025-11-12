class AirCity {
    constructor() { }

    /**
     * 设置三维事件（例如相机飞行开始、结束、Actor的点击等）的回调函数
     */
    setEventCallback = function (onEvent) {
        return new Promise((resolve, reject) => {
            window.origAPI.setEventCallback((resp) => {
                resolve(onEvent && onEvent(resp))
            })
        })
    }
    /**
     * 重置场景（重置到刚打开工程的状态）
     */
    reset = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.reset(() => {
                resolve()
            })
        })
    }
    /**
     * 关闭WebSocket连接
     */
    destroy = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.destroy(() => {
                resolve()
            })
        })
    }
}

export default AirCity