import Camera from './camera.class'

class CameraBIZ extends Camera {
    constructor() {
        super()

        this.isKeyDown = false
    }
}

/**
 * 设置相机（定位到视角）
 * @param {object} data 视角
 * @param {number} flyTime 时间
 */
CameraBIZ.prototype.set_biz = function (data, flyTime = 2) {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.set(data.x, data.y, data.z, data.pitch, data.yaw, flyTime)
        resolve()
    })
}

/**
 * 键盘事件
 */
CameraBIZ.prototype.keyEvent = function (e) {
    let that = this
    if (!that.isKeyDown && e.type === 'keydown') {
        that.isKeyDown = true
        switch (e.key) {
            case 'w':
                if (e.type === 'keydown') that.moveForward()
                break
            case 's':
                if (e.type === 'keydown') that.moveBackward()
                break
            case 'a':
                if (e.type === 'keydown') that.moveRight()
                break
            case 'd':
            case 'e':
                if (e.type === 'keydown') that.moveDown()
                break
            case 'q':
                if (e.type === 'keydown') that.moveUp()
                break
            case 'ArrowUp':
                if (e.type === 'keydown') that.turnUp()
                break
            case 'ArrowDown':
                if (e.type === 'keydown') that.turnDown()
                break
            case 'ArrowLeft':
                if (e.type === 'keydown') that.turnLeft()
                break
            case 'ArrowRight':
                if (e.type === 'keydown') that.turnRight()
                break
            default:
                break
        }
    } else if (e.type === 'keyup') {
        that.isKeyDown = false
        that.stop()
    }
}

/**
 * 绑定键盘事件
 * @param {boolean} show 是否绑定
 * @returns 
 */
CameraBIZ.prototype.addKeyEvent = function (show) {
    if (show) {
        document.addEventListener('keydown', this.keyEvent, false)
        document.addEventListener('keyup', this.keyEvent, false)
    } else {
        document.removeEventListener('keydown', this.keyEvent, false)
        document.removeEventListener('keyup', this.keyEvent, false)
    }
}


export default CameraBIZ