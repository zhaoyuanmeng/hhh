class Camera {
    constructor() { }

    /**
     * 获取当前的相机位置 
     * @returns 可选的回调函数（camera属性值有6个元素，依次为 [X, Y, Z, Pitch, Yaw, Roll]）
     */
    get = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.get((resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 设置相机位置
     * @param {number} x 坐标X
     * @param {number} y 坐标Y
     * @param {number} z 坐标Z
     * @param {number} pitch 上下旋转角度（俯仰），单位是度。 此参数可选，如果没有设置或者设置为0，系统会自动设置默认值
     * @param {number} yaw 左右旋转角度（航向），单位是度。 此参数可选，如果没有设置或者设置为0，系统会自动设置默认值
     * @param {number} flyTime 可选参数，相机飞行的时间，取值范围：[0~任意正数]，单位：秒，默认值2秒
     * @returns 可选的回调函数
     */
    set = function (x, y, z, pitch, yaw, flyTime) {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.set(x, y, z, pitch, yaw, flyTime, () => {
                resolve()
            })
        })
    }
    /**
     * 通过观察点设置相机位置
     * @param {number} x 观察点坐标X
     * @param {number} y 观察点坐标Y
     * @param {number} z 观察点坐标Z
     * @param {number} distance 可选参数，观察点距离目标点（被拍摄物体）的距离，取值范围：[0.01~任意正数]，如果设置为0或者不设置，系统自动计算
     * @param {number} pitch 上下旋转角度（俯仰），单位是度。可选参数，如果没有设置或者设置为0，系统会自动设置默认值
     * @param {number} yaw 左右旋转角度（航向），单位是度。可选参数，如果没有设置或者设置为0，系统会自动设置默认值
     * @param {number} flyTime 可选参数，相机飞行的时间，取值范围：[0~任意正数]，单位：秒，默认值2秒
     * @returns 可选的回调函数
     */
    lookAt = function (x, y, z, distance, pitch, yaw, flyTime) {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.lookAt(x, y, z, distance, pitch, yaw, flyTime, () => {
                resolve()
            })
        })
    }
    /**
     * 开始播放动画导航
     * @param {number} id 动画导航的编号
     * @returns 可选的回调函数
     */
    playAnimation = function (id) {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.playAnimation(id, () => {
                resolve()
            })
        })
    }
    /**
     * 停止播放动画导航
     * @returns 选的回调函数
     */
    stopAnimation = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.stopAnimation(() => {
                resolve()
            })
        })
    }
    /**
     * 前进
     * @returns 可选的回调函数
     */
    moveForward = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.moveForward(() => {
                resolve()
            })
        })
    }
    /**
     * 后退
     * @returns 可选的回调函数
     */
    moveBackward = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.moveBackward(() => {
                resolve()
            })
        })
    }
    /**
     * 右移
     * @returns 可选的回调函数
     */
    moveRight = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.moveRight(() => {
                resolve()
            })
        })
    }
    /**
     * 下降
     * @returns 可选的回调函数
     */
    moveDown = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.moveDown(() => {
                resolve()
            })
        })
    }
    /**
     * 左移
     * @returns 可选的回调函数
     */
    moveLeft = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.moveLeft(() => {
                resolve()
            })
        })
    }
    /**
     * 上升
     * @returns 可选的回调函数
     */
    moveUp = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.moveUp(() => {
                resolve()
            })
        })
    }
    /**
     * 抬头
     * @returns 可选的回调函数
     */
    turnUp = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.turnUp(() => {
                resolve()
            })
        })
    }
    /**
     * 低头
     * @returns 可选的回调函数
     */
    turnDown = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.turnDown(() => {
                resolve()
            })
        })
    }
    /**
     * 左转
     * @returns 可选的回调函数
     */
    turnLeft = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.turnLeft(() => {
                resolve()
            })
        })
    }
    /**
     * 右转
     * @returns 可选的回调函数
     */
    turnRight = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.turnRight(() => {
                resolve()
            })
        })
    }
    /**
     * 停止
     * @returns 选的回调函数
     */
    stop = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.camera.stop(() => {
                resolve()
            })
        })
    }
}

export default Camera