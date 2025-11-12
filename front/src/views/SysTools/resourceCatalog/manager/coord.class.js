class Coord {
    constructor() {}
    /**
     * 地理坐标转投影坐标
     * @param {array} coordinates 坐标值
     */
    gcs2pcs = function(coordinates) {
        return new Promise((resolve, reject) => {
            window.origAPI.coord.gcs2pcs(coordinates, (resp) => {
                resolve(resp)
            })
        })
    }
    /**
     * 投影坐标转地理坐标
     * @param {array} coordinates 坐标值
     */
    pcs2gcs =  function (coordinates) {
        return new Promise((resolve, reject) => {
            window.origAPI.coord.pcs2gcs(coordinates, (resp) => {
                resolve(resp)
            })
        })
    }
}

export default Coord