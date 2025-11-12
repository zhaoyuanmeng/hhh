import Weather from './weather.class'

class WeatherBIZ extends Weather {
    constructor() {
        super()

        this.hour = null
        this.isDark = null
        this.param = {
            雨: {
                open: false,
                data: [
                    // { name: '强度', value: 0.3, min: 0, max: 1, step: 0.01, type: 'strength' },
                    // { name: '速度', value: 0.4, min: 0, max: 10, step: 0.01, type: 'speed' },
                    // { name: '雨滴大小', value: 0.5, min: 0, max: 5, step: 0.01, type: 'size' },
                    // 6.0
                    { name: '强度', value: 0.3, min: 0, max: 1, step: 0.01, type: 'strength' },
                    { name: '速度', value: 0.4, min: 0, max: 10, step: 0.01, type: 'speed' },
                    { name: '雨滴大小', value: 0.5, min: 0, max: 5, step: 0.01, type: 'raindropSize' },
                ],
            },
            雪: {
                open: false,
                data: [
                    // { name: '强度', value: 0.3, min: 0, max: 1, step: 0.01, type: 'strength' },
                    // { name: '速度', value: 0.4, min: 0, max: 1, step: 0.01, type: 'speed' },
                    // { name: '雪花大小', value: 0.5, min: 0, max: 1, step: 0.01, type: 'size' },
                    // 6.0
                    { name: '强度', value: 0.3, min: 0, max: 1, step: 0.01, type: 'strength' },
                    { name: '速度', value: 0.4, min: 0, max: 10, step: 0.01, type: 'speed' },
                    { name: '雪花大小', value: 0.5, min: 0, max: 25, step: 0.01, type: 'snowflakeSize' },
                ],
            },
            雾: {
                open: false,
                data: [
                    // { name: '整体雾浓度', value: 0.5, min: 0, max: 1, step: 0.01, type: 'fogDensity' },
                    // { name: '地面雾浓度', value: 0.05, min: 0, max: 1, step: 0.01, type: 'fogGroundDensity' },
                    // { name: '地面雾高度', value: 1, min: 0, max: 10, step: 0.1, type: 'fogHeight' },
                    // 6.0
                    { name: '整体雾浓度', value: 0.07, min: 0, max: 2, step: 0.01, type: 'fogDensity' },
                    { name: '雾的颜色', value: 'rgba(255, 255, 255, 0.5)', min: 0, max: 1, step: 0.01, type: 'fogColor' },
                    { name: '高度衰减', value: 0.44, min: 0, max: 2, step: 0.01, type: 'fogHeightFalloff' },
                    { name: '起雾距离', value: 190, min: 0, max: 10000, step: 0.1, type: 'fogStartDistance' },
                    { name: '透明度', value: 0.3, min: 0, max: 1, step: 0.1, type: 'fogOpacity' },
                ],
            },
            云: {
                open: false,
                data: [
                    // { name: "云层密度", value: 0.5, min: 0, max: 1, step: 0.01, type: 'cloudDensity' },
                    // { name: "云层高度", value: 0.5, min: 0, max: 20, step: 0.01, type: 'cloudHeight' },
                    // { name: "云层厚度", value: 0.5, min: 0, max: 20, step: 0.01, type: 'cloudThickness' },
                    //6.0
                    { name: "云层密度", value: 0.5, min: 0, max: 1, step: 0.01, type: 'cloudDensity' },
                    { name: "云层高度", value: 0.5, min: 0, max: 20, step: 0.01, type: 'cloudHeight' },
                    { name: "云层厚度", value: 0.5, min: 0, max: 0.5, step: 0.01, type: 'cloudThickness' },
                ],
            },
        }
        this.initPARAM()
    }
}

/**
 * 获取时间
 * @param {*} attr 属性名
 * @param {*} val 属性值
 * @returns 
 */
function getTime(attr, val) {
    let _DT = new Date()
    switch (attr) {
        case 'hour': // 0~23
            _DT.setHours(val)
            break
    }
    return {
        year: _DT.getFullYear(),
        month: _DT.getMonth(),
        day: _DT.getDate(),
        hour: _DT.getHours(),
        minute: _DT.getMinutes()
    }
}

/**
 * 设置早、中、晚、夜晚
 * @param {*} hour 小时 [1~24]
 * @returns 可选的回调函数
 */
WeatherBIZ.prototype.setDateByHour = function (hour) {
    let that = this
    return new Promise(async (resolve, reject) => {
        // let _DT = getTime('hour', hour - 1)
        let _DT = that.getDateTime()
        await that.setDateTime(_DT.year, _DT.month, _DT.day, hour || _DT.hour, _DT.minute, false)
        that.hour = hour
        resolve()
    })
}

/**
 * 获取小时
 */
WeatherBIZ.prototype.getHour = function () {
    return this.hour
}

/**
 * 设置黑暗模式
 * @param {boolean} bDark 布尔值
 * @returns 可选的回调函数
 */
WeatherBIZ.prototype.setDarkMode_biz = function (bDark) {
    let that = this
    return new Promise(async (resolve, reject) => {
        await that.setDarkMode(bDark)
        that.isDark = bDark
        sessionStorage.setItem('bDark', bDark)
        resolve()
    })
}
/**
 * 获取是否为黑暗模式
 */
WeatherBIZ.prototype.getDarkMode = function () {
    let bDark = sessionStorage.getItem('bDark')
    this.isDark = bDark
    return this.isDark
}
/**
 * 初始化天气相关的参数
 */
WeatherBIZ.prototype.initPARAM = function () {
    let that = this
    return new Promise(async (resolve, reject) => {
        // 日期时间
        let _DT = await that.getDateTime()
        that.hour = _DT.hour
        // 天气参数
        let res = await that.getParams()
        that.isDark = res.darkMode === 1 // 是否黑暗模式，0关闭 1打开
        sessionStorage.setItem('bDark', that.isDark)
        that.param['云'].data[0].value = res.cloudDensity
        that.param['云'].data[1].value = res.cloudHeight
        that.param['云'].data[2].value = res.cloudThickness

        resolve()
    })
}
/**
 * 获取天气相关的数据
 */
WeatherBIZ.prototype.getPARAM = function (attr) {
    return this.param[attr]
}

export default WeatherBIZ