import Tag from './tag.class'
import { sessionStorage } from '@/utils/storage'
class TagBIZ extends Tag {
    constructor() {
        super()

        this.tags =  sessionStorage.get('QXZS_3dMarker')||[]
    }
}

/**
 * 删除一个或多个Tag对象
 */
TagBIZ.prototype.delete_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        if (Object.prototype.toString.call(data) === '[object Object]') {
            let index = that.tags.findIndex(item => item.id === data.id)
            if (index !== -1) that.tags.splice(index, 1)
        }
        await that.delete(ids)
        resolve()
    })
}

/**
 * 自动定位到合适的观察距离
 */
TagBIZ.prototype.focus_biz = function (data, distance, flyTime, rotation) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.focus(ids, distance, flyTime, rotation)
        resolve()
    })
}

/**
 * 隐藏Tag
 */
TagBIZ.prototype.hide_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.hide(ids)
        resolve()
    })
}

/**
 * 显示Tag
 */
TagBIZ.prototype.show_biz = function (data) {
    let that = this
    return new Promise(async (resolve, reject) => {
        let ids = window.sealAPI.getIds(data)
        if (!ids) resolve()

        await that.show(ids)
        resolve()
    })
}

/**
 * 创建标签
 */
function createTag(id, coordinate, imagePath, imageSize, url, text, range, showLine, option) {
    let o = new TagData(id, coordinate, imagePath, imageSize, url, text, range, showLine)
    o.textColor = Color.Black
    o.showLine = showLine
    o.textBackgroundColor = Color.White
    if (option) Object.assign(o, option)
    return o
}

/**
 * 添加标签
 */
TagBIZ.prototype.addTag = function (opt) {
    console.log(opt,'7897797')
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!opt) resolve()

        let option = Object.assign({
            id: `tag${new Date().getTime()}`,
            coordinate: [],
            groupId:'',
            imagePath: '',
            imageSize: '',
            url: '',
            text: '',
            range: [0, 18000],
            showLine: false,
            delete: false
        }, opt)

        if (option.delete) {
            await that.delete(option.id)

            if (option.ratio) {
                option.imageSize[0] = option.imageSize[0] * option.ratio / 100.0
                option.imageSize[1] = option.imageSize[1] * option.ratio / 100.0
            }
            let tag = null
            if (option.type) {
                tag = createTag(option.id, option.coordinate, option.labelImage,
                    option.imageSize, option.url, '', option.range, option.showLine)
                tag.type = option.type
                tag.ratio = option.ratio
                tag.font = option.font
                tag.fontSize = option.fontSize
                tag.fontColor = option.fontColor
                tag.pLeft = option.pLeft
                tag.pTop = option.pTop
                tag.tLeft = option.tLeft
                tag.tTop = option.tTop
                tag.groupId = option.groupId
                tag.originImage = option.imagePath
                tag.originText = option.text
                tag.text = option.text
                tag.textColor  = option.fontColor
            } else {
                tag = createTag(option.id, option.coordinate, option.imagePath,
                    option.imageSize, option.url, option.text, option.range, option.showLine)
            }


            let index = that.tags.findIndex(item => item.id === option.id)
            if (index !== -1) that.tags.splice(index, 1)
            that.tags.push(tag)

            await that.add(tag)
        } else {
            if (option.ratio) {
                option.imageSize[0] = option.imageSize[0] * option.ratio / 100.0
                option.imageSize[1] = option.imageSize[1] * option.ratio / 100.0
            }
            let tag = null
            if (option.type) {
                tag = createTag(option.id, option.coordinate, option.labelImage,
                    option.imageSize, option.url, '', option.range, option.showLine)
                tag.type = option.type
                tag.ratio = option.ratio
                tag.font = option.font
                tag.groupId = option.groupId
                tag.fontSize = option.fontSize
                tag.fontColor = option.fontColor
                tag.pLeft = option.pLeft
                tag.pTop = option.pTop
                tag.tLeft = option.tLeft
                tag.tTop = option.tTop
                tag.originImage = option.imagePath
                tag.originText = option.text
                tag.text = option.text
                tag.textColor  = option.fontColor
            } else {
                tag = createTag(option.id, option.coordinate, option.imagePath,
                    option.imageSize, option.url, option.text, option.range, option.showLine)
            }
            that.tags.push(tag)
            console.log(tag)
            await that.add(tag)
        }

        resolve()
    })
}

/**
 * 获取标签数据
 */
TagBIZ.prototype.getTags = function () {
    // let arrs = sessionStorage.get('QXZS_3dMarker')
    return this.tags
    // return arrs
}

/**
 * 序列化
 * @param {} json
 */
TagBIZ.prototype.serialize = function (json) {
    json.push(...this.tags)
}

/**
 * 绘制图文
 */
function drawImage(imageUrl, text, font, fontSize, fontColor, tLeft, tTop, index) {
    return new Promise((resolve, reject) => {
        let image = new Image
        image.setAttribute('crossOrigin', 'Anonymous')
        let imageWidth = 0
        let imageHeight = 0
        image.onload = function () {
            // 先绘制底图
            let canvas = document.createElement('canvas')
            let context = canvas.getContext('2d')
            context.imageSmoothingEnabled = true
            imageWidth = image.width
            imageHeight = image.height
            canvas.width = imageWidth
            canvas.height = imageHeight

            canvas.style.background = 'rgba(255,255,255,0)'
            canvas.style.backgroundColor = 'transparent'
            canvas.style.width = imageWidth + 'px'
            canvas.style.height = imageHeight + 'px'
            canvas.style.margin = 0
            context.clearRect(0, 0, imageWidth, imageHeight)
            context.drawImage(image, 0, 0, imageWidth, imageHeight)

            // 根据窗体分辩率绘制文字
            let fontCanvas = document.createElement('canvas')
            let fontContext = fontCanvas.getContext('2d')
            let devicePixelRatio = window.devicePixelRatio || 1
            let backingStoreRatio =
                fontContext.webkitBackingStorePixelRatio ||
                fontContext.mozBackingStorePixelRatio ||
                fontContext.msBackingStorePixelRatio ||
                fontContext.oBackingStorePixelRatio ||
                fontContext.backingStorePixelRatio ||
                1
            let ratio = devicePixelRatio / backingStoreRatio
            fontContext.imageSmoothingEnabled = true
            fontCanvas.width = imageWidth * ratio
            fontCanvas.height = imageHeight * ratio
            fontCanvas.style.backgroundColor = 'transparent'
            fontCanvas.style.width = imageWidth + 'px'
            fontCanvas.style.height = imageHeight + 'px'
            fontCanvas.style.margin = 0
            fontContext.fillStyle = fontColor // 填充颜色
            fontContext.textBaseline = 'top'
            fontContext.font = 'normal ' + fontSize + 'px ' + font
            fontContext.font = 'normal ' + fontSize * ratio + 'px ' + font
            fontContext.fillText(
                text,
                tLeft * ratio,
                (tTop + 5) * ratio
            )
            fontContext.font = 'normal ' + fontSize + 'px ' + font
            fontContext.stroke()
            let dataUrl = fontCanvas.toDataURL('image/png', 1)

            let fontImage = new Image()
            fontImage.setAttribute('crossOrigin', 'Anonymous')
            fontImage.src = dataUrl
            // 将文字与底图合并，得到最终的图片
            fontImage.onload = function () {
                context.drawImage(fontImage, 0, 0, imageWidth, imageHeight)
                let img = canvas.toDataURL('image/png', 1)
                resolve({ image: img, index: index })
            }
        }
        image.src = imageUrl
    })
}

/**
 * 反序列化 （创建+添加）
 * @param {} json
 * @param {} show 默认显示
 */
TagBIZ.prototype.deserialize = function (json, show = true) {
    if (!json) return

    let that = this
    return new Promise(async (resolve, reject) => {
        let arr = []

        json.forEach(async (item) => {
            if (item.type) {
                let res = await drawImage(item.imagePath, item.text, item.font, item.fontSize, item.fontColor, item.tLeft, item.tTop, i)

                json[res.index].imageSize[0] = json[res.index].imageSize[0] * json[res.index].ratio / 100.0
                json[res.index].imageSize[1] = json[res.index].imageSize[1] * json[res.index].ratio / 100.0

                let oa = createTag(json[res.index].id, json[res.index].coordinate, res.image, json[res.index].imageSize, json[res.index].url, '', json[res.index].range, json[res.index].showLine, json[res.index].option)
                oa.type = json[res.index].type
                oa.ratio = json[res.index].ratio
                oa.font = json[res.index].font
                oa.fontSize = json[res.index].fontSize
                oa.fontColor = json[res.index].fontColor
                oa.pLeft = json[res.index].pLeft
                oa.pTop = json[res.index].pTop
                oa.tLeft = json[res.index].tLeft
                oa.tTop = json[res.index].tTop
                oa.originImage = json[res.index].imagePath
                oa.originText = json[res.index].text

                that.tags.push(oa)
                that.add(oa)
            }
            else {
                let oa = createTag(item.id, item.coordinate, item.imagePath, item.imageSize, item.url, item.text, item.range, item.showLine, item.option)

                that.tags.push(oa)
                arr.push(oa)
            }
        })

        await that.add(arr)
        if (!show) await that.hide(arr)

        resolve()
    })
}


export default TagBIZ
