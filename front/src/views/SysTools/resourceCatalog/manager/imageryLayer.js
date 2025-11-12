import ImageryLayer from './imageryLayer.class'
import { loadXMLDoc, loadXMLDocAsync, xmlStr2XmlObj, xmlObj2json, xmlStr2json } from '@/utils/xml'

class ImageryLayerBIZ extends ImageryLayer {
    constructor() {
        super()

    }
}


/**
 * 添加arcgis 的wmts服务
 * @param {string} id   图层id 
 * @param {string}  url 图层地址
 * @param {number} renderMode 渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
 * @returns 
 */
ImageryLayerBIZ.prototype.addArcGISWMTS = function (id, url, renderMode) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!id) reject()

        let xmlurl = url + '/WMTS/1.0.0/WMTSCapabilities.xml'
        let xml = await loadXMLDocAsync(xmlurl)

        let layerName = xml.getElementsByTagName('Contents')[0].getElementsByTagName('Layer')[0].getElementsByTagName('ows:Identifier')[0].innerHTML
        let content = xml.getElementsByTagName('Contents')[0]

        let tileMatrixSets = content.getElementsByTagName('TileMatrixSet')
        let tileMatrixSet = tileMatrixSets[tileMatrixSets.length - 1]
        let tileMatrixName = tileMatrixSet.getElementsByTagName('ows:Identifier')[0].innerHTML
        let EPSG = tileMatrixSet.getElementsByTagName('ows:SupportedCRS')[0].innerHTML
        let epsgArr = EPSG.replace(/:/g, '').split('EPSG')
        EPSG = epsgArr[epsgArr.length - 1]
        let ogcEPSG = 'EPSG:' + EPSG

        await that.init({
            xmlUrl: xmlurl,
            layerName: layerName,
            tileMatrixName: tileMatrixName,
            ogcEPSG: ogcEPSG,
            cachePath: ':memory:',
            mapMode: true, // 大地图true，小地图false，默认：小地图false
            renderMode: renderMode,
        })

        await that.add({
            id: id ? id : layerName,
            url: url + '/tile/{z}/{y}/{x}'
        })

        resolve()
    })
}

/**
 * 添加geoserver的wmts服务
 * @param {string} id   图层id 
 * @param {string}  url 图层地址
 * @param {string}  workspace 工作空间
 * @param {string}  layerName 图层名称
 * @param {string}  epsg 坐标系EGPS id
 * @param {number} renderMode 渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
 * @returns 
 */
ImageryLayerBIZ.prototype.addGeoserverWMTS = function (id, url, workspace, layerName, epsg, renderMode) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!id) reject()

        let xmlurl = url + '/gwc/service/wmts?REQUEST=GetCapabilities'
        let xml = await loadXMLDocAsync(xmlurl)

        let content = xml.getElementsByTagName('Contents')[0]
        let layers = content.getElementsByTagName('Layer')
        let tileMatrixName = null
        let _layerName = workspace + ':' + layerName
        let layersArray = Array.from(layers)

        layersArray.map(item => {
            let Identifier = item.getElementsByTagName('ows:Identifier')[0].innerHTML
            if (Identifier == _layerName) {
                let TileMatrixSetLinks = item.getElementsByTagName('TileMatrixSetLink')
                let linkArray = Array.from(TileMatrixSetLinks)
                linkArray.forEach(TileMatrixSetLink => {
                    let TileMatrixSet = TileMatrixSetLink.querySelector('TileMatrixSet').innerHTML
                    if (TileMatrixSet != 'EPSG:900913' && TileMatrixSet != 'EPSG:4326') {
                        tileMatrixName = TileMatrixSet
                    }
                })
            }
        })
        let ogcEPSG = 'EPSG:' + epsg

        await that.init({
            xmlUrl: xmlurl,
            layerName: _layerName,
            tileMatrixName: tileMatrixName,
            ogcEPSG: ogcEPSG,
            cachePath: ':memory:',
            mapMode: true,//大地图true，小地图false，默认：小地图false
            renderMode: renderMode,//
        })
        await that.add({
            id: id ? id : _layerName,
            url: url + `/gwc/service/wmts?layer=${_layerName}&style=&tilematrixset=${tileMatrixName}&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image/png&TileMatrix=${tileMatrixName}:{z}&TileCol={x}&TileRow={y}`
        })

        resolve()
    })
}

/**
 * 添加supermap 的wmts服务
 * @param {string} id   图层id 
 * @param {string}  url 服务地址
 * @param {string}  layerName 图层名称
 * @param {number} renderMode 渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
 * @returns 
 */
ImageryLayerBIZ.prototype.addSuperMapWMTS = function (id, url, layerName, renderMode) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!id) reject()

        let xmlurl = url
        let xml = await loadXMLDocAsync(xmlurl)

        let content = xml.getElementsByTagName('Contents')[0]
        let layers = content.getElementsByTagName('Layer')
        let tileMatrixName = null
        let _layerName = null
        let ogcEPSG = null

        let layersArray = Array.from(layers)
        layersArray.forEach(item => {
            let identifier = item.getElementsByTagName('ows:Identifier')[0]
            if (identifier.innerHTML.indexOf(layerName) > -1) {
                _layerName = identifier.innerHTML
            }
        })

        let TileMatrixSets = content.querySelectorAll('TileMatrixSet')
        let TileMatrixSetsArray = Array.from(TileMatrixSets)

        TileMatrixSetsArray.forEach(item => {
            let identifiers = item.getElementsByTagName('ows:Identifier')
            if (identifiers.length > 0) {
                if (identifiers[0].innerHTML.indexOf(layerName) > -1 && identifiers[0].innerHTML.indexOf('Custom') > -1) {
                    tileMatrixName = identifiers[0].innerHTML

                    let crs = item.getElementsByTagName('ows:SupportedCRS')[0].innerHTML
                    let crsSplit = crs.split(':')
                    ogcEPSG = 'EPSG:' + crsSplit[crsSplit.length - 1]
                }
            }
        })
        await that.init({
            xmlUrl: xmlurl,
            layerName: _layerName,
            tileMatrixName: tileMatrixName,
            ogcEPSG: ogcEPSG,
            cachePath: ':memory:',
            mapMode: true, // 大地图true，小地图false，默认：小地图false
            renderMode: renderMode,
        })
        await that.add({
            id: id ? id : _layerName,
            url: xmlurl + `/${_layerName}/default/${tileMatrixName}/{z}/{y}/{x}.png`
        })

        resolve()
    })
}

/**
 * 添加arcgis的wms服务
 * @param {string} id   图层id 
 * @param {string}  url 图层地址
 * @param {number} renderMode 渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
 * @returns 
 */
ImageryLayerBIZ.prototype.addArcGISWMS = function (id, url, renderMode) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!id) reject()

        url = url.replace('rest/', '')
        let xmlurl = url + '/WMSServer?request=GetCapabilities&service=WMS'
        let xml = await loadXMLDocAsync(xmlurl)

        let layerName = xml.getElementsByTagName('Layer')[0].getElementsByTagName('Name')[0].innerHTML
        let tileMatrixName = ''
        let CRSArray = xml.getElementsByTagName('CRS')
        let CRS = CRSArray[CRSArray.length - 1]

        let ogcEPSG = CRS.innerHTML

        await that.init({
            xmlUrl: xmlurl,
            layerName: layerName,
            tileMatrixName: tileMatrixName,
            ogcEPSG: ogcEPSG,
            cachePath: ':memory:',
            mapMode: true, // 大地图true，小地图false，默认：小地图false
            renderMode: renderMode,
        })

        let _url = xmlurl.split('?')[0]
        await that.add({
            id: id ? id : layerName,
            url: `${_url}?version=1.3.0&request=GetMap&crs=${ogcEPSG}&bbox={bbox-epsg-${ogcEPSG.replace('EPSG:', '')}}&width=256&height=256&format=image/png&TRANSPARENT=TRUE&styles=&layers=${layerName}`
        })

        resolve()
    })
}

/**
 * 添加geoserver的wms服务
 * @param {string} id   图层id 
 * @param {string}  serverurl geoserver地址
 * @param {string}  workspace 工作空间
 * @param {string}  layerName 图层名称
 * @param {string}  epsg 坐标系EGPS id
 * @param {number} renderMode 渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
 * @returns 
 */
ImageryLayerBIZ.prototype.addGeoserverWMS = function (id, serverurl, workspace, layerName, epsg, renderMode) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!id) reject()

        let xmlurl = serverurl + '/' + workspace + '/wms?service=wms&version=1.3.0&request=GetCapabilities'
        let ogcEPSG = 'EPSG:' + epsg
        await that.init({
            xmlUrl: xmlurl,
            layerName: layerName,
            tileMatrixName: '',
            ogcEPSG: ogcEPSG,
            cachePath: ':memory:',
            mapMode: true, // 大地图true，小地图false，默认：小地图false
            renderMode: renderMode,
        })
        await that.add({
            id: id ? id : layerName,
            url: serverurl + `/${workspace}/wms?service=wms&version=1.3.0&request=GetMap&layers=${layerName}&bbox={bbox-epsg-${epsg}}&width=256&height=256&srs=EPSG:${epsg}&styles=&format=image%2Fpng&TRANSPARENT=true`
        })

        resolve()
    })
}

/**
 * 添加supermap的wms服务
 * @param {string} id   图层id 
 * @param {string}  url 服务地址
 * @param {string}  layerName 图层名称
 * @param {number} renderMode 渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
 * @returns 
 */
ImageryLayerBIZ.prototype.addSuperMapWMS = function (id, url, layerName, renderMode) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!id) reject()

        let xmlurl = url + '/' + layerName
        let xml = await loadXMLDocAsync(xmlurl)

        let layers = xml.getElementsByTagName('Layer')

        let epsg = null
        if (url.indexOf('wms130') > -1) epsg = xml.querySelector('CRS')
        else epsg = xml.querySelector('SRS')

        let _layerName = ''
        let ogcEPSG = epsg.innerHTML

        let layersArray = Array.from(layers)

        for (let i = 0; i < layersArray.length; i++) {
            const item = layersArray[i]
            let nameele = item.querySelector('Name')
            if (nameele && nameele.innerHTML) {
                _layerName = nameele.innerHTML
                break
            }
        }

        await that.init({
            xmlUrl: xmlurl,
            layerName: _layerName,
            tileMatrixName: '',
            ogcEPSG: ogcEPSG,
            cachePath: ':memory:',
            mapMode: true, // 大地图true，小地图false，默认：小地图false
            renderMode: renderMode,
        })

        let addurl = xmlurl
        addurl = addurl.replace('wms130', 'rest/maps')
        addurl = addurl.replace('wms110', 'rest/maps')
        addurl = addurl.replace('wms111', 'rest/maps')
        let version = '1.3.0'
        if (url.indexOf('wms111') > -1) {
            version = '1.1.1'
        } else if (url.indexOf('wms100') > -1) {
            version = '1.0.0'
        } else if (url.indexOf('wms130') > -1) {
            version = '1.3.0'
        }
        await that.add({
            id: id ? id : _layerName,
            url: addurl + `/image.png?redirect=false&transparent=true&cacheEnabled=true&overlapDisplayed=false&viewBounds={bbox-epsg-${ogcEPSG.replace('EPSG:', '')}}&width=256&height=256&version=${version}`
        })

        resolve()
    })
}

/**
 * 添加arcgis的mapserver服务
 * @param {string} id 图层id 
 * @param {string} url 图层地址
 * @param {number} renderMode 渲染模式，取值范围：0：正常（默认值）；1：透明；2：标注；3：贴地
 * @returns 
 */
ImageryLayerBIZ.prototype.addArcGISMapServer = function (id, url, renderMode) {
    let that = this
    return new Promise(async (resolve, reject) => {
        if (!id) reject()

        let res = await request.get(url + '?f=json')
        console.log('liujingjie  res', res)

        let epsg = 4547
        let xmlurl = `${url}/export?bbox={bbox-epsg-${epsg}}&bboxSR=${epsg}&layers=&layerDefs=&size=256%2C256&imageSR=${epsg}&format=png&transparent=true&f=image`
        await that.init({
            xmlUrl: xmlurl,
            layerName: '',
            tileMatrixName: '',
            ogcEPSG: '',
            cachePath: ':memory:',
            mapMode: true, // 大地图true，小地图false，默认：小地图false
            renderMode: renderMode,
        })

        await that.add({
            id: id ? id : 'mapserver' + Math.random(),
            url: xmlurl,
        })

        resolve()
    })
}

export default ImageryLayerBIZ