class Misc {
    constructor() { }

    /**
     * 调用整个渲染场景内指定的蓝图函数 注意：调用前请先确认被调用的蓝图函数已存在，并和设计蓝图函数的开发人员沟通确认相关参数取值后再调用
     * @param {object} data 
     * actorTag (string) 发布蓝图函数时在模型包含的Actor上添加的tag，调用前需和设计蓝图函数的开发人员确认
     * objectName (string) 模型包含的Actor对象的ID，可以根据__g.tileLayer.getObjectIDs(tileLayerIds)方法获取
     * functionName (string) 待调用的蓝图函数名称，调用前需和设计蓝图函数的开发人员确认此函数已存在
     * paramType (BPFuncParamType) 传入单个参数，待传入参数类型 参考BPFuncParamType，如果需传递对象类型参数可以把Json对象定义为StringArray类型，在蓝图函数内部自己实现反序列化解析
     * paramValue (any) 传入单个参数，根据参数类型设置对应参数值
     * parameters (array) 传入多个参数，数组类型，注意：传入多参数的顺序与类型务必与蓝图函数的参数顺序及其参数类型一致以保证执行结果符合预期
     * @returns 可选的回调函数
     */
    callBPFunction = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.misc.callBPFunction(data, () => {
                resolve()
            })
        })
    }

    /**
     * 退出汇报演示模式
     * @returns 可选的回调函数
     */
    exitReportMode = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.misc.exitReportMode(() => {
                resolve()
            })
        })
    }

    /**
     * 播放视频（显示播放窗口）
     * @param {string} id 字符串类型的ID
     * @param {number} x 视频窗口的位置X
     * @param {number} y 视频窗口的位置Y
     * @param {number} width 视频窗口的宽度
     * @param {number} height 视频窗口的高度
     * @param {string} url 视频文件路径，支持本地绝对路径和流媒体网络路径，注意：不支持工程本地的相对路径
     * @returns 可选的回调函数
     */
    playVideo = function (id, x, y, width, height, url) {
        return new Promise((resolve, reject) => {
            window.origAPI.misc.playVideo(id, x, y, width, height, url, () => {
                resolve()
            })
        })
    }

    /**
     * 停止播放视频（播放窗口会消失）
     * @param {string} id 字符串类型的ID
     * @returns 可选的回调函数
     */
    stopPlayVideo = function (id) {
        return new Promise((resolve, reject) => {
            window.origAPI.misc.stopPlayVideo(id, () => {
                resolve()
            })
        })
    }

}

export default Misc