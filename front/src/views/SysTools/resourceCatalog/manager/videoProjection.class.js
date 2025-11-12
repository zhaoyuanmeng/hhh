class VideoProjection {
    constructor () {}
    /**
     * 
     * @param {object | array} data 数据结构，支持对象或数组，对于每一个对象支持以下属性：
     * id (string) 字符串类型的ID
     * groupId (string) 可选，Group分组
     * userData (string) 可选，用户自定义数据
     * videoURL (string) 视频地址
     * location (array) 位置坐标
     * rotation (array) 旋转
     * fov (number) 垂直夹角
     * aspectRatio (number) 纵横比
     * distance (number) 距离
     * depthCulling (boolean) 是否背面剔除
     * frustumVisible (boolean) 是否显示投影线框
     * frustumColor (Color) 投影线框颜色
     * texturePath (string) 自定义投影蒙版图片路径
     */
    add = function(data) {
        return new Promise((resolve, reject) => {
            window.origAPI.videoProjection.add(data, () => {
                resolve()
            })
        })
    }
    delete = function(ids) {
        return new Promise((resolve, reject) => {
            window.origAPI.videoProjection.delete(ids, () => {
                resolve()
            })
        })
    }
}
export default VideoProjection