import Tools from './tools.class'

class ToolsBIZ extends Tools {
    constructor() {
        super()

        this.isClip = false
        this.Oa = []
        // 当前是否进入了测量状态
        this.isMeasuring = false
        // 测量默认参数
        this.toolsSetting = {
            mode: {
                1: MeasurementMode.Coordinate,
                2: MeasurementMode.Linear,
                3: MeasurementMode.Horizontal,
                4: MeasurementMode.Vertical,
                5: MeasurementMode.MultiPoint,
                6: MeasurementMode.Volume,
                7: MeasurementMode.TerrainArea, // 地表面积
            },
            options: {
                'lineSize': 3.0,
                'pointSize': 8.0,
                'textColor': Color.Yellow,
                'pointColor': [0, 0, 1, 0.3],
                'lineColor': Color.Red,
                'areaColor': [0, 1, 0, 0.3],
                'showCoordinateText': false
            }
        }
    }
}

/**
 * 置测量参数：模式、颜色
 */
ToolsBIZ.prototype.setMeasurement_biz = function (_measurementMode, _options) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            'lineSize': 3.0,
            'pointSize': 8.0,
            'textColor': Color.Yellow,
            'pointColor': [0, 0, 1, 0.3],
            'lineColor': Color.Red,
            'areaColor': [0, 1, 0, 0.3],
            'showCoordinateText': false
        }
        let measurementMode = that.toolsSetting.mode[_measurementMode] || that.toolsSetting.mode[1]
        Object.assign(options, _options)

        if (!that.isMeasuring) await that.startMeasurement_biz()
        await that.setMeasurement(measurementMode, options)

        resolve()
    })
}

/**
 * 进入测量状态，用户可以在三维场景中点击鼠标进行测量
 */
ToolsBIZ.prototype.startMeasurement_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.startMeasurement()
        that.isMeasuring = true
        resolve()
    })
}

/**
 * 取消测量
 */
ToolsBIZ.prototype.stopMeasurement_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.stopMeasurement()
        that.isMeasuring = false
        resolve()
    })
}

/**
 * TileLayer多边形剖切（挖洞）
 */
ToolsBIZ.prototype.startPolygonClip_biz = function (_coordinates) {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.startPolygonClip(_coordinates, false)
        that.isClip = true
        resolve()
    })
}

/**
 * TileLayer停止多边形剖切
 */
ToolsBIZ.prototype.stopPolygonClip_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!that.isClip) resolve()

        await that.stopPolygonClip()
        that.isClip = false
        resolve()
    })
}

/**
 * 面剖切
 */
ToolsBIZ.prototype.startPlaneClip_biz = function (_coordinates) {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.startPlaneClip(_coordinates, [0, 0, 0])
        that.isClip = true
        resolve()
    })
}

/**
 * 停止面剖切
 */
ToolsBIZ.prototype.stopPlaneClip_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!that.isClip) resolve()

        await that.stopPlaneClip()
        that.isClip = false
        resolve()
    })
}

/**
 * 体剖切
 */
ToolsBIZ.prototype.startVolumeClip_biz = function (_coordinates) {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.startVolumeClip(_coordinates)
        that.isClip = true
        resolve()
    })
}

/**
 * 停止体剖切
 */
ToolsBIZ.prototype.stopVolumeClip_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!that.isClip) resolve()

        await that.stopVolumeClip()
        that.isClip = false
        resolve()
    })
}

/**
 * 开始开敞度分析
 */
ToolsBIZ.prototype.startViewDomeAnalysis_biz = function (radius, opacity, visibleColor, invisibleColor) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            radius: radius || 500,
            opacity: opacity || 0.5,
            visibleColor: visibleColor || Color.Red,
            invisibleColor: invisibleColor || Color.Green,
        }
        await that.startViewDomeAnalysis(options)
        resolve()
    })
}

/**
 * 开始通视分析
 */
ToolsBIZ.prototype.startVisiblityAnalysis_biz = function (height, visibleColor, invisibleColor) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            height: height || 0,
            visibleColor: visibleColor || Color.Red,
            invisibleColor: invisibleColor || Color.Green,
        }
        await that.startVisiblityAnalysis(options)
        resolve()
    })
}

/**
 * 开始水淹分析
 */
ToolsBIZ.prototype.startFloodFill_biz = function (min, max, seed, elevation, color, precision) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            min: min,
            max: max,
            seed: seed,
            elevation: elevation || 0,
            color: color || Color.Blue,
            precision: precision || 0.5,
        }
        await that.startFloodFill(options)
        resolve()
    })
}

/**
 * 开始日照分析
 */
ToolsBIZ.prototype.startSunshineAnalysis_biz = function (year, month, day, startTime, endTime, spacing, groundElevation, height, sphereRadius) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            year: year,
            month: month,
            day: day,
            startTime: startTime || '08: 00',
            endTime: endTime || '16:00',
            spacing: spacing || -1,
            groundElevation: groundElevation || 0,
            height: height || 5000,
            sphereRadius: sphereRadius || 1,
        }
        await that.startSunshineAnalysis(options)
        resolve()
    })
}

/**
 * 开始填挖方分析，适用于地形
 */
ToolsBIZ.prototype.startCutFillAnalysis_biz = function (height, gridSize, lineThickness, pointSize, gridLineThickness, cutLineColor, fillLineColor, cutPointColor, fillPointColor, gridColor) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            height: height || 0,
            gridSize: gridSize || 5,
            lineThickness: lineThickness || 2,
            pointSize: pointSize || 5,
            gridLineThickness: gridLineThickness || 5,
            cutLineColor: cutLineColor || Color.Red,
            fillLineColor: fillLineColor || Color.Green,
            cutPointColor: cutPointColor || Color.Blue,
            fillPointColor: fillPointColor || Color.Blue,
            gridColor: gridColor || Color.Yellow,
        }
        await that.startCutFillAnalysis(options)
        resolve()
    })
}

/**
 * 开始坡度坡向分析，适用于地形
 */
ToolsBIZ.prototype.startTerrainSlopeAnalysis_biz = function (showArrow, colorMode, arrowColor) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            showArrow: showArrow,
            colorMode: colorMode || 1,
            arrowColor: arrowColor || Color.White,
        }
        await that.startTerrainSlopeAnalysis(options)
        resolve()
    })
}


/**
 * 开始等高线分析，适用于地形
 */
ToolsBIZ.prototype.startContourLineAnalysis_biz = function (isShowContourPlane, contourPlaneOpacity, isShowContourLine, contourLineColor, contourLineSpacing, contourLineRangeMax, contourLineRangeMin) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let options = {
            isShowContourPlane: isShowContourPlane,
            contourPlaneOpacity: contourPlaneOpacity || 1,
            isShowContourLine: isShowContourLine,
            contourLineColor: contourLineColor || '#fff',
            contourLineSpacing: contourLineSpacing || 20,
            contourLineRangeMax: contourLineRangeMax || 1000000,
            contourLineRangeMin: contourLineRangeMin || -1000000
        }
        await that.startContourLineAnalysis(options)
        resolve()
    })
}

ToolsBIZ.prototype.clear_biz = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.stopPolygonClip()
        await that.stopPlaneClip()
        await that.stopVolumeClip()

        that.isClip = false
        resolve()
    })

}

ToolsBIZ.prototype.getIsClip = function () {
    return this.isClip
}

ToolsBIZ.prototype.addData = function (oa, un = true) {
    if (un) this.Oa.unshift(oa)
    else this.Oa.push(oa)
}

ToolsBIZ.prototype.serialize = function (json) {
    json.push(...this.Oa)
}

ToolsBIZ.prototype.deserialize = function (json) {
    if (!json) return

    let that = this
    return new Promise((resolve, reject) => {
        that.Oa = json
    })
}

export default ToolsBIZ