class Tag {
    constructor() { }

    /**
     * 添加一个或多个Tag对象
     * @param {object | array} data 标签数据，可以是Object类型或者Array类型
     * id (string) 字符串类型的ID
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * coordinate (array) 坐标值：标签添加的位置坐标
     * imagePath (string) 图片路径，支持本地路径和网络路径
     * imageSize (array) 图片尺寸[width,height]，取值范围：[0~任意正数]
     * url (string) 鼠标点击标签后弹出的网页的URL，也可以是本地视频文件，鼠标点击标签后会弹出视频播放窗口
     * popupBackgroundColor (Color) 弹窗背景色，支持四种格式
     * popupPos (array) 弹窗位置: [x, y]，数组元素类型：(number)，数组元素取值范围：[任意正整数]
     * popupSize (array) 弹窗尺寸：[width, height]，数组元素类型：(number)，数组元素取值范围：[任意正整数]
     * range (array) 标签的可见范围 [Min,Max]，Min和Max指摄像机相对于标签的最小和最大距离，在此范围内标签才可见，类型均为浮点数，元素取值范围：[1.0~100000.0]，单位：米
     * text (string) 标签显示的文字
     * textRange (number) 文字的可见范围，取值范围：[1.0~100000.0]，单位：米
     * textSize (number) 文字大小，取值范围：[0~任意正整数]
     * textColor (Color) 文字颜色，支持四种格式
     * textBorderColor (Color) 文字边框颜色，支持四种格式
     * textBackgroundColor (Color) 文本背景颜色，默认值白色，支持四种格式
     * showLine (boolean) 标签下方是否显示垂直牵引线，默认值：true
     * hoverImagePath (string) 鼠标悬停时显示的图片路径
     * autoHidePopupWindow (boolean) 是否自动关闭弹出窗口，默认值：true，失去焦点后会自动关闭
     * autoHeight (boolean)自动判断下方是否有物体，设置正确高度，默认值：false
     * coordinateType (number) 坐标系类型，取值范围：0(Projection), 1(WGS84)，如果不设置此参数，默认为0
     * @returns 可选的回调函数
     */
    add = function (data) {
        return new Promise((resolve, reject) => {
            window.origAPI.tag.add(data, () => {
                resolve()
            })
        })
    }
    /**
     * 删除场景中所有的Tag
     * @returns 可选的回调函数
     */
    clear = function () {
        return new Promise((resolve, reject) => {
            window.origAPI.tag.clear(() => {
                resolve()
            })
        })
    }
    /**
     * 删除一个或多个Tag对象
     * @param {string | array} ids 要删除的Tag对象的ID或者ID数组（可以删除一个或者多个）
     * @returns 可选的回调函数
     */
    delete = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.tag.delete(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 自动定位到合适的观察距离
     * @param {string | array} ids Tag对象的ID或者ID数组
     * @param {number} distance 可选参数，观察点距离目标点（被拍摄物体）的距离，取值范围：[0.01~任意正数]，如果设置为0或者不设置，系统自动计算
     * @param {number} flyTime 可选参数，相机飞行的时间，取值范围：[0~任意正数]，单位：秒，默认值2秒
     * @param {array} rotation 可选参数，相机旋转的欧拉角：[Pitch,Yaw,Roll]，数组元素类型：(number)，取值范围：Pitch[-90~90] Yaw[-180~180] Roll[0]
     * @returns 可选的回调函数
     */
    focus = function (ids, distance, flyTime, rotation) {
        return new Promise((resolve, reject) => {
            window.origAPI.tag.focus(ids, distance, flyTime, rotation, () => {
                resolve()
            })
        })
    }
    /**
     * 隐藏Tag
     * @param {string | array} ids Tag对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    hide = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.tag.hide(ids, () => {
                resolve()
            })
        })
    }
    /**
     * 显示Tag
     * @param {string | array} ids Tag对象的ID或者ID数组
     * @returns 可选的回调函数
     */
    show = function (ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.tag.show(ids, () => {
                resolve()
            })
        })
    }
}

export default Tag