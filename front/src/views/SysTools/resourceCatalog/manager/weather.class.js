class Weather {
    constructor() { }

    /**
     * 获取日期时间
     * @returns 可选的回调函数
     */
    getDateTime = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.getDateTime((resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 设置日期时间
     * @param {number} year 年
     * @param {number} month 月
     * @param {number} day 日
     * @param {number} hour 时
     * @param {number} minute 分
     * @param {boolean} daynightLoop 是否日夜循环 如果为true 则三分钟模拟循环一天
     * @returns 可选的回调函数
     */
    setDateTime = function (year, month, day, hour, minute, daynightLoop) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setDateTime(year, month, day, hour, minute, daynightLoop, () => {
                resolve()
            })
        })
    }
    /**
     * 获取天气相关的参数
     * @returns 可选的回调函数
     */
    getParams = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.getParams((resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 设置环境光强度
     * @param {number} ambientLightIntensity 环境光强度，取值范围:[0~10]
     * @returns 可选的回调函数
     */
    setAmbientLightIntensity = function (ambientLightIntensity) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setAmbientLightIntensity(ambientLightIntensity, () => {
                resolve()
            })
        })
    }
    /**
     * 设置色温值
     * @param {number} temperature 色温值，取值范围:[1700~12000]
     * @returns 可选的回调函数
     */
    setTemperature = function (temperature) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setTemperature(temperature, () => {
                resolve()
            })
        })
    }
    /**
     * 设置阴影质量，值越大表示阴影越精细，但也越消耗显卡性能
     * @param {number} shadowQuality 阴影质量，取值范围：[1~5]
     * @returns 可选的回调函数
     */
    setShadowQuality = function (shadowQuality) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setShadowQuality(shadowQuality, () => {
                resolve()
            })
        })
    }
    /**
     * 设置太阳尺寸
     * @param {number} sunSize 太阳尺寸，取值范围:[0~100]
     * @returns 可选的回调函数
     */
    setSunSize = function (sunSize) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setSunSize(sunSize, () => {
                resolve()
            })
        })
    }
    /**
     * 设置太阳光照射强度
     * @param {number} sunIntensity 太阳光照射强度，取值范围:[0~10]
     * @returns 可选的回调函数
     */
    setSunIntensity = function (sunIntensity) {
        return new Promise((resolve, reject) => {
            resolve()
            window.origAPI.weather.setSunIntensity(sunIntensity, () => {
            })
        })
    }
    /**
     * 设置月亮尺寸
     * @param {number} moonSize 月亮尺寸，取值范围:[0~100]
     * @returns 可选的回调函数
     */
    setMoonSize = function (moonSize) {
        return new Promise((resolve, reject) => {
            resolve()
            window.origAPI.weather.setMoonSize(moonSize, () => {
            })
        })
    }
    /**
     * 设置月亮光照强度
     * @param {number} moonIntensity 月亮光照强度，取值范围:[0~100]
     * @returns 选的回调函数
     */
    setMoonIntensity = function (moonIntensity) {
        return new Promise((resolve, reject) => {
            resolve()
            window.origAPI.weather.setMoonIntensity(moonIntensity, () => {
            })
        })
    }
    /**
     * 设置阴影可视距离，即相机镜头距离物体阴影的距离
     * @param {number} shadowDistance 阴影可视距离，取值范围:[0~任意正数]，单位：米
     * @returns 可选的回调函数
     */
    setShadowDistance = function (shadowDistance) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setShadowDistance(shadowDistance, () => {
                resolve()
            })
        })
    }
    /**
     * 设置云层的密度
     * @param {number} cloudDensity 云层密度，取值范围：[0~1.0]
     * @returns 可选的回调函数
     */
    setCloudDensity = function (cloudDensity) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setCloudDensity(cloudDensity, () => {
                resolve()
            })
        })
    }
    /**
     * 设置云层的高度
     * @param {number} cloudHeight 云层高度，取值范围：[0~20]km，单位：公里
     * @returns 可选的回调函数
     */
    setCloudHeight = function (cloudHeight) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setCloudHeight(cloudHeight, () => {
                resolve()
            })
        })
    }
    /**
     * 设置云层的厚度
     * @param {number} cloudThickness 云层厚度，取值范围：[0~20]
     * @returns 可选的回调函数
     */
    setCloudThickness = function (cloudThickness) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setCloudThickness(cloudThickness, () => {
                resolve()
            })
        })
    }
    /**
     * 设置雨效。注意：开启雨效前需先设置云层的厚度和密度
     * @param {number} strength 强度（必须大于0，才会有下雨效果），取值范围:[0~1.0]
     * @param {number} speed 速度，取值范围:[0~10]
     * @param {number} raindropSize 雨滴大小，取值范围:[0~5]
     * @returns 可选的回调函数
     */
    setRainParam = function (strength, speed, raindropSize) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setRainParam(strength, speed, raindropSize, () => {
                resolve()
            })
        })
    }
    /**
     * 设置雪效。注意：开启雪效前需先设置云层的厚度和密度
     * @param {number} strength 强度（必须大于0，才会有下雪效果），取值范围:[0~1.0]
     * @param {number} speed 速度，取值范围:[0~10]
     * @param {number} snowflakeSize 雪花大小，取值范围:[0~25]
     * @returns 可选的回调函数
     */
    setSnowParam = function (strength, speed, snowflakeSize) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setSnowParam(strength, speed, snowflakeSize, () => {
                resolve()
            })
        })
    }
    /**
     * 设置雾效参数
     * @param {number} fogDensity 整体雾浓度，取值范围:[0~1.0]
     * @param {number} fogGroundDensity 地面雾浓度，取值范围:[0~1.0]
     * @param {number} fogGroundHeight 地面雾高度，取值范围:[0~10]（单位KM）
     * @param {number} fogRange 雾化范围，取值范围:[0~1.0]
     * @returns 可选的回调函数
     */
    setFogParam = function (fogDensity, fogGroundDensity, fogGroundHeight, fogRange) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setFogParam(fogDensity, fogGroundDensity, fogGroundHeight, fogRange, () => {
                resolve()
            })
        })
    }
    /**
     * 关闭雨雪效果
     * @returns 可选的回调函数
     */
    disableRainSnow = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.disableRainSnow(() => {
                resolve()
            })
        })
    }
    /**
     * 设置是否进入黑暗模式
     * @param {boolean} bDark 布尔值
     * @returns 可选的回调函数
     */
    setDarkMode = function (bDark) {
        return new Promise((resolve, reject) => {
            window.origAPI.weather.setDarkMode(bDark, () => {
                resolve()
            })
        })
    }

}

export default Weather