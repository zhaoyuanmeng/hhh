class TileLayer {
    constructor() { }

    /**
     * 设置TileLayer的样式
     * @param {string | array} ids TileLayer的ID或ID数组
     * @param {number} style 样式， 0：默认；1：X光；2：纯色；3：水晶体；4：暗黑；5：科幻；6：扩散
     * @param {Color} color 颜色，默认值:Color.White，支持四种格式
     * @param {number} saturation 饱和度，仅在默认样式0下生效，取值范围：[0~2]，默认值：1.0
     * @param {number} brightness 亮度 ，仅在默认样式0下生效，取值范围：[0.1~10]，默认值：1.0
     * @param {number} contrast 对比度，仅在默认样式0下生效，取值范围：[0.2~5]，默认值：1.0
     * @param {number} contrastBase 对比度基准，仅在默认样式0下生效，取值范围：[0.036~0.9]，默认值：0.18
     * @returns 可选的回调函数
     */
    setStyle = function (ids, style, color, saturation, brightness, contrast, contrastBase) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.setStyle(ids, style, color, saturation, brightness, contrast, contrastBase, () => {
                resolve()
            })
        })
    }
    /**
     * 启用X光
     * @param {number | array} ids TileLayer的ID或者ID数组
     * @param {Color} color 颜色，支持四种格式
     * @returns 可选的回调函数
     */
    enableXRay = function (ids, color) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.enableXRay(ids, color, () => {
                resolve()
            })
        })
    }
    /**
     * 禁用X光
     * @param {number | array} ids TileLayer的ID或者ID数组
     * @returns 可选的回调函数
     */
    disableXRay = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.disableXRay(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 高亮一个Actor
     * @param {string} id TileLayer的ID
     * @param {string} objectId Actor的ID
     * @returns 可选的回调函数
     */
    highlightActor = function (id, objectId) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.highlightActor(id, objectId, () => {
                resolve()
            })
        })
    }
    /**
     * 取消高亮一个Actor
     * @param {string} id TileLayer的ID
     * @param {string} objectId Actor的ID
     * @returns 可选的回调函数
     */
    stopHighlightActor = function (id, objectId) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.stopHighlightActor(id, objectId, () => {
                resolve()
            })
        })
    }
    /**
     * 高亮多个Actor
     * @param {object} data 数据对象，支持以下属性：
     * id (string) TileLayer的ID
     * objectIds (array) TileLayer里的Actor的ObjectID（单个或者数组）
     * @returns 可选的回调函数
     */
    highlightActors = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.highlightActors(data, () => {
                resolve()
            })
        })
    }
    /**
     * 设置TileLayer是否参与碰撞检测
     * @param {string | array} ids TileLayer的ID或ID数组
     * @param {boolean} enabled 是否开启碰撞检测的总开关，如果此参数设置为false，则下面三个参数均会失效
     * @param {boolean} mouseInteract 是否开启鼠标交互，默认值：true 开启
     * @param {boolean} mouseFunction 是否开启鼠标相关的功能交互，包含鼠标拾取、分析工具、测量工具等，默认值：true 开启
     * @param {boolean} characterCollision 是否开启角色碰撞，默认值：true 开启角色碰撞
     * @returns 可选的回调函数
     */
    setCollision = function (ids, enabled, mouseInteract, mouseFunction, characterCollision) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.setCollision(ids, enabled, mouseInteract, mouseFunction, characterCollision, () => {
                resolve()
            })
        })
    }
    /**
     * 停止高亮Actor
     * @param {object | array} data 数据结构，支持对象或数组，对于每一个对象支持以下属性：
     * id (string) TileLayer的ID
     * objectIds (array) TileLayer里的Actor的ObjectID（单个或者数组）
     * @returns 可选的回调函数
     */
    stopHighlightActors = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.stopHighlightActors(data, (res) => {
                resolve(res)
            })
        })
    }
    /**
     * 显示Actor
     * @param {string} id TileLayer的ID
     * @param {string} objectId Actor的ID
     * @returns 可选的回调函数
     */
    showActor = function (id, objectId) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.showActor(id, objectId, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏Actor
     * @param {string} id TileLayer的ID
     * @param {string} objectId Actor的ID
     * @returns 可选的回调函数
     */
    hideActor = function (id, objectId) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.hideActor(id, objectId, () => {
                resolve()
            })
        })
    }
    /**
     * 显示一个或多个Actor
     * @param {object | array} data 数据结构，支持对象或数组，对于每一个对象支持以下属性：
     * id (string) TileLayer的ID
     * objectIds (array) TileLayer里的Actor的ObjectID（单个或者数组）
     * @returns 可选的回调函数
     */
    showActors = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.showActors(data, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏一个或多个Actor
     * @param {object | array} data 数据结构，支持对象或数组，对于每一个对象支持以下属性：
     * id (string) TileLayer的ID
     * objectIds (array) TileLayer里的Actor的ObjectID（单个或者数组）
     * @returns 可选的回调函数
     */
    hideActors = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.hideActors(data, () => {
                resolve()
            })
        })
    }
    /**
     * 设置平移
     * @param {string} id TileLayer的ID
     * @param {array} newVal 新的位置坐标：[X,Y,Z]，取值示例，数组元素类型：(number)，取值范围：[任意数值]
     * @returns 可选的回调函数
     */
    setLocation = function (id, newVal) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.setLocation(id, newVal, () => {
                resolve()
            })
        })
    }
    /**
     * 设置旋转
     * @param {string} id TileLayer的ID
     * @param {array} newVal 新的旋转坐标：[Pitch,Yaw,Roll]，数组元素类型：(number)，取值范围：[任意数值]
     * @returns 可选的回调函数
     */
    setRotation = function (id, newVal) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.setRotation(id, newVal, () => {
                resolve()
            })
        })
    }
    /**
     * 设置缩放
     * @param {string} id TileLayer的ID
     * @param {array} newVal 新的缩放：[X,Y,Z]，数组元素类型：(number)，取值范围：[任意正整数]
     * @returns 
     */
    setScale = function (id, newVal) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.setScale(id, newVal, () => {
                resolve()
            })
        })
    }
    /**
     * 获取指定TileLayer包含的所有Actor对象的ID
     * @param {string} ids TileLayer的ID或者ID数组
     * @returns 
     */
    getObjectIDs = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.getObjectIDs(ids, (res) => {
                resolve(res.data || [])
            })
        })
    }
    /**
     * 显示TileLayer图层
     * @param {string | array} ids TileLayer对象的ID或者ID数组
     * @returns 
     */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.show(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏TileLayer图层
     * @param {string | array} ids TileLayer对象的ID或者ID数组
     * @returns 
     */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.hide(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 自动定位到合适的观察距离
     * @param {string | array} ids TileLayer对象的ID或者ID数组
     * @param {number} distance 可选参数，观察点距离目标点（被拍摄物体）的距离，取值范围：[0.01~任意正数]，如果设置为0或者不设置，系统自动计算
     * @param {number} flyTime 可选参数，相机飞行的时间，取值范围：[0~任意正数]，单位：秒，默认值2秒
     * @param {array} rotation 可选参数，相机旋转的欧拉角：[Pitch,Yaw,Roll]，数组元素类型：(number)，取值范围：Pitch[-90~90] Yaw[-180~180] Roll[0]
     * @returns 
     */
    focus = function (ids, distance, flyTime, rotation) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.focus(ids, distance, flyTime, rotation, () => {
                resolve()
            })
        })
    }
    /**
     * 通过OID查询Actor的矩阵和bound等信息
     * @param {object | array} data 对象或数组类型，如果是数组类型，对于每个数组元素，有以下属性：
            id (string) TileLayer对象的ID
            objectIds (array) TileLayer里的Actor的ObjectID字符串（单个或者数组）
     * @returns 
     */
    getActorInfo = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.getActorInfo(data, (res) => {
                resolve(res)
            })
        })
    }
    /**
* 用于批量多次修改对象的属性
* 在开始修改之前调用updateBegin，然后可以多次调用setXXX方法，最后调用updateEnd提交修改更新数据
  注意：
  updateBegin不是异步调用，不需要await，也没有回调函数参数
* @returns 可选的回调函数
*/
    updateBegin = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.updateBegin(() => {
                resolve()
            })
        })
    }
    /**
     * 用于批量多次修改对象的属性，与updateBegin配套使用
       注意：
       updateEnd是异步调用，可以用回调函数也可以await
     * @returns 可选的回调函数
     */
    updateEnd = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tileLayer.updateEnd(() => {
                resolve()
            })
        })
    }
}

export default TileLayer