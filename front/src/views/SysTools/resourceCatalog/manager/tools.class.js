class Tools {
    constructor() { }

    /**
     * 开始等高线分析，适用于地形
     * @param {object} options 参数对象信息，支持以下属性（如果未设置某个属性，则使用默认值）
     * isShowContourPlane 是否显示等高面，{boolean}，默认值：true
     * contourPlaneOpacity 等高面颜色不透明度，仅当isShowContourPlane设置为true时生效，{number}，取值范围：[0~1]，默认值：1
     * isShowContourLine 是否显示等高线，{boolean}，默认值：true
     * contourLineColor 等高线颜色值，仅当isShowContourLine设置为true时生效，{Color}，默认值：[1,1,1,1]
     * contourLineSpacing 等高线的间距，仅当isShowContourLine设置为true时生效，{number}，单位：米，默认值：20米
     * contourLineRangeMax 等高线最大可视高度，{number}，单位：米，默认值：1000000米
     * contourLineRangeMin 等高线最小可视高度，{number}，单位：米，默认值：-1000000米
     * @returns 可选的回调函数
     */
    startContourLineAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startContourLineAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止等高线分析
     * @returns 可选的回调函数
     */
    stopContourLineAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopContourLineAnalysis(() => {
                resolve()
            })
        })
    }
    /**
     * 开始填挖方分析，适用于地形
     * @param {object} options 参数对象信息，支持以下属性（如果未设置某个属性，则使用默认值）
     * height 高度，单位：米，取值范围：[任意负数~任意正数]，默认值：0
     * gridSize 网格的大小，取值范围：[0~100]，默认值：5
     * lineThickness 线段的宽度，单位：米，取值范围：[0~100]，默认值：2
     * pointSize 点的大小，取值范围：[0~100]，默认值：5
     * gridLineThickness 网格线的宽度，单位：米，取值范围：[0~100]，默认值：5
     * gridLineThickness 网格线的宽度，单位：米，取值范围：[0~100]，默认值：5
     * fillLineColor 填方线的颜色，默认值：绿色 Color.Green
     * cutPointColor 挖方点的颜色，默认值：蓝色 Color.Blue
     * fillPointColor 填方点的颜色，默认值：蓝色 Color.Blue
     * gridColor 网格线的颜色，默认值：黄色 Color.Yellow
     * @returns 可选的回调函数
     */
    startCutFillAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startCutFillAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止填挖方分析
     * @returns 可选的回调函数
     */
    stopCutFillAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopCutFillAnalysis(() => {
                resolve()
            })
        })
    }
    /**
     * 开始水淹分析
     * @param {object} options 参数信息，支持以下属性
     * min (array) 水淹分析坐标范围: [minX, minY]，最小X/Y坐标值
     * max (array) 水淹分析坐标范围: [maxX, maxY]，最大X/Y坐标值
     * seed (array) 出水点: [X, Y]，X/Y取值需在对应的min和max范围内
     * elevation (number) 水位高度，单位：米
     * color (Color) 水颜色，支持四种格式
     * precision (number) 水淹模拟精度 (取值范围：[0~1] 精度越高效率会降低)，默认值：0.5
     * @returns 可选的回调函数
     */
    startFloodFill = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startFloodFill(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止水淹分析
     * @returns 可选的回调函数
     */
    stopFloodFill = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopFloodFill(() => {
                resolve()
            })
        })
    }
    /**
     * 停止多边形剖切（2021.03.23之后的版本已弃用，请用stopPolygonClip代替）
     * @returns 可选的回调函数
     */
    stopClip = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopClip(() => {
                resolve()
            })
        })
    }
    /**
     * 开始开敞度分析
     * @param {object} options 参数信息，支持以下属性（如果未设置某个属性，则使用默认值）
     * radius 展示半径，单位：米，取值范围：[0.01~任意正数]，默认值：500
     * opacity 透明度，取值范围：[0~1]，默认值：0.5
     * visibleColor 可见区域的颜色，默认值：红色 Color.Red
     * invisibleColor 不可见区域的颜色，默认值：绿色 Color.Green
     * @returns 可选的回调函数
     */
    startViewDomeAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startViewDomeAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止开敞度分析
     * @returns 可选的回调函数
     */
    stopViewDomeAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopViewDomeAnalysis(() => {
                resolve()
            })
        })
    }
    /**
     * 开始通视分析
     * @param {object} options 参数信息，支持以下属性（如果未设置某个属性，则使用默认值）
     * height 视点高度（距离场景交互所拾取点的高度），取值范围：[任意负数~任意正数]，默认值：0
     * visibleColor 可见区域的颜色，默认值：红色 Color.Red
     * invisibleColor 不可见区域的颜色，默认值：绿色 Color.Green
     * @returns 可选的回调函数
     */
    startVisiblityAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startVisiblityAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止通视分析
     * @returns 可选的回调函数
     */
    stopVisiblityAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopVisiblityAnalysis(() => {
                resolve()
            })
        })
    }
    /**
     * 开始坡度坡向分析，适用于地形
     * @param {object} options 参数对象信息，支持以下属性（如果未设置某个属性，则使用默认值）
     * showArrow 是否显示箭头，{boolean}，默认值：true
     * colorMode 模式，{number}，取值范围：【1坡度 2坡向】，默认值：1
     * arrowColor 箭头颜色，默认值：白色 [1,1,1,1]
     * @returns 可选的回调函数
     */
    startTerrainSlopeAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startTerrainSlopeAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止坡度坡向分析
     * @returns 可选的回调函数
     */
    stopTerrainSlopeAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopTerrainSlopeAnalysis(() => {
                resolve()
            })
        })
    }
    /**
     * 开始日照分析
     * @param {object} options 参数对象信息，支持以下属性（如果未设置某个属性，则使用默认值）
     * year 年，取值范围：[1970~至今年份]，默认值：当前日期
     * month 月，取值范围：[1~12]，默认值：当前日期
     * day 日，取值范围：[1~31]，默认值：当前日期
     * startTime 开始时间，取值类型：时间字符串，默认值：08:00
     * endTime 结束时间，取值类型：时间字符串，默认值：16:00
     * spacing 间距，取值范围：[任意负数~任意正数]，默认值：-1米
     * groundElevation 底面高度，取值范围：[任意负数~任意正数]，默认值：0米
     * height 高度，取值范围：[0~任意正数]，默认值：5000米
     * sphereRadius 日照效果球半径，取值范围：[0~任意正数]，默认值：1米
     * @returns 可选的回调函数
     */
    startSunshineAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startSunshineAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止日照分析
     * @returns 可选的回调函数
     */
    stopSunshineAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopSunshineAnalysis(() => {
                resolve()
            })
        })
    }
    /**
     * 开始面剖切
     * @param {array} location 生成位置坐标：[X,Y,Z]，取值示例，数组元素类型：(number)，取值范围：[任意数值]
     * @param {array} rotation 旋转角度 [Pitch,Yaw,Roll]，数组元素类型：(number)，取值范围：[任意数值]
     * @param {boolean} isShowPlane 剖切时是否显示辅助面，默认值：false
     * @param {boolean} isEditable 剖切时是否可交互编辑，默认值：false
     * @returns 可选的回调函数
     */
    startPlaneClip = function (location, rotation, isShowPlane, isEditable) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startPlaneClip(location, rotation, isShowPlane, isEditable, () => {
                resolve()
            })
        })
    }
    /**
     * 停止面剖切
     * @returns 可选的回调函数
     */
    stopPlaneClip = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopPlaneClip(() => {
                resolve()
            })
        })
    }
    /**
     * 开始体剖切
     * @param {array} bbox bounding box
     * @param {number} value 0：正向剖切，1：反向剖切
     * @param {boolean} isShowBBox 剖切时是否显示剖切范围的包围盒，默认值：false
     * @param {boolean} isEditable 剖切时是否可交互编辑，默认值：false
     * @param {array} rotation bbox的欧拉角，[Pitch,Yaw,Roll]，设置包围盒的旋转
     * @returns 可选的回调函数
     */
    startVolumeClip = function (bbox, value, isShowBBox, isEditable, rotation) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startVolumeClip(bbox, value, isShowBBox, isEditable, rotation, () => {
                resolve()
            })
        })
    }
    /**
     * 更新体剖切
     * @param {array} bbox bounding box
     * @param {array} rotation bbox的欧拉角，[Pitch,Yaw,Roll]，设置包围盒的旋转，注意：只支持更新Yaw
     * @param {number} value 0：正向剖切，1：反向剖切
     * @param {boolean} isShowBBox 剖切时是否显示剖切范围的包围盒，默认值：false
     * @param {boolean} isEditable 剖切时是否可交互编辑，默认值：false
     * @returns 可选的回调函数
     */
    updateVolumeClip = function (bbox, rotation, value, isShowBBox, isEditable) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.updateVolumeClip(bbox, rotation, value, isShowBBox, isEditable, () => {
                resolve()
            })
        })
    }
    /**
     * 停止体剖切
     * @returns 可选的回调函数
     */
    stopVolumeClip = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopVolumeClip(() => {
                resolve()
            })
        })
    }
    /**
     * TileLayer多边形剖切
     * @param {array} coordinates 多边形坐标数组（数组的格式与Polygon、Polygon3D、HighlightArea一样）
     * @param {boolean} isReverseCut 多边形剖切是否反转，默认值：false
     * @returns 可选的回调函数
     */
    startPolygonClip = function (coordinates, isReverseCut) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startPolygonClip(coordinates, isReverseCut, () => {
                resolve()
            })
        })
    }
    /**
     * 停止多边形剖切
     * @returns 可选的回调函数
     */
    stopPolygonClip = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopPolygonClip(() => {
                resolve()
            })
        })
    }
    /**
     * 开始测量，用户可以在三维场景中点击鼠标进行测量
     * @returns 可选的回调函数
     */
    startMeasurement = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startMeasurement(() => {
                resolve()
            })
        })
    }
    /**
     * 设置测量模式及相关参数
     * @param {MeasurementMode} type 测量模式
     * @param {object} options 选项，目前支持的选项有：
     * pointSize: 默认值 8.0
     * textColor: 默认值 Color.Yellow
     * pointColor: 默认值 [0,0,1,0.3]
     * lineColor: 默认值 Color.Red
     * areaColor: 默认值 [0,1,0,0.3]
     * showCoordinateText: 是否显示坐标测量的文本，默认值 false
     * @returns 可选的回调函数
     */
    setMeasurement = function (type, options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.setMeasurement(type, options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止测量
     * @returns 可选的回调函数
     */
    stopMeasurement = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopMeasurement(() => {
                resolve()
            })
        })
    }
    /**
     * 开始天际线分析
     * @param {object} options 参数信息，支持以下属性（如果未设置某个属性，则使用默认值）：
     * showOutline 是否显示场景轮廓线，默认值：true
     * outlineThickness 场景轮廓线像素宽度 (仅显示场景轮廓线有效)，默认值：1
     * outlineColor 场景轮廓线颜色 (仅显示场景轮廓线有效)，默认值：绿色 Color.Green
     * useSceneColor 是否使用自定义场景颜色，默认值：false
     * sceneColor 设置场景颜色 (仅使用自定义场景颜色有效)，默认值：黑色 Color.Black
     * showSkyline 是否显示天际线窗口，默认值：true
     * windowSize 天际线窗口大小 0:X 1:Y
     * skylineColor 天际线颜色(仅显示天际线窗口有效)，默认值：绿色 Color.Green
     * backgroundColor 天际线窗口背景颜色：默认值：[0,0,0,0.75]
     * height 视点高度（距离场景交互所拾取点的高度），默认值：0
     * tileLayers 数组类型，若此数组为空默认绘制所有物体形成的天际线;不为空则多个TileLayer形成的一条天际线，多条天际线绘制到该图上
     * @returns 可选的回调函数
     */
    startSkylineAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startSkylineAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 结束天际线分析
     * @returns 可选的回调函数
     */
    stopSkylineAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopSkylineAnalysis(() => {
                resolve()
            })
        })
    }
    /**
     * 开始视域分析
     * @param {object} options 参数信息，支持以下属性（如果未设置某个属性，则使用默认值）：
     * startPoint 可选参数，视域分析起点坐标位置，若不传入则从鼠标点击拾取获取
     * endPoint 可选参数，视域分析终点坐标位置，若不传入则从鼠标点击拾取获取
     * fov_h 水平视角，取值范围：[1°~150°]，默认值：60
     * fov_v 垂直视角，取值范围：[1°~150°]，默认值：30
     * height 视点高度（距离场景交互所拾取点的高度），默认值：0
     * visibleColor 可见区域的颜色，默认值：绿色 Color.Green
     * invisibleColor 不可见区域的颜色，默认值：红色 Color.Red
     * @returns 可选的回调函数
     */
    startViewshedAnalysis = function (options) {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.startViewshedAnalysis(options, () => {
                resolve()
            })
        })
    }
    /**
     * 停止视域分析
     * @returns 可选的回调函数
     */
    stopViewshedAnalysis = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tools.stopViewshedAnalysis(() => {
                resolve()
            })
        })
    }
}

export default Tools