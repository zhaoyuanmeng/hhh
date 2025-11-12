
import AirCityClass from './manager/aircity.class'
import PlayerClass from './manager/player.class'
import SettingsPanelClass from './manager/settingsPanel.class'
import TagBIZ from './manager/tag'
import CameraBIZ from './manager/camera'
import TileLayerBIZ from './manager/tileLayer'
import PolylineBIZ from './manager/polyline'
import PolygonBIZ from './manager/polygon'
import Polygon3DBIZ from './manager/polygon3d'
import ODLineBIZ from './manager/odline'
import RadiationPointBIZ from './manager/radiationPoint'
import EditHelperBIZ from './manager/editHelper'
import ToolsBIZ from './manager/tools'
import ViewshedBIZ from './manager/viewshed'
import MiscBIZ from './manager/misc'
import WeatherBIZ from './manager/weather'
import BeamBIZ from './manager/beam'
import HighlightAreaBIZ from './manager/highlightArea'
import SettingsBIZ from './manager/settings'
// import ClipBIZ from './manager/clip'
import CameraTourBIZ from './manager/cameraTour'
import InfoTreeBIZ from './manager/infoTree'
import MarkerBIZ from './manager/marker'
// var __g = window.__g
let globalObject = undefined
function SealAirCityAPI() {
    globalObject = this
}

SealAirCityAPI.prototype.initClass = function () {
    this._aircity = new AirCityClass()
    this._player = new PlayerClass()
    this._settingsPanel = new SettingsPanelClass()
    this._tag = new TagBIZ()
    this._tileLayer = new TileLayerBIZ()
    this._camera = new CameraBIZ()
    this._polyline = new PolylineBIZ()
    this._polygon = new PolygonBIZ()
    this._polygon3d = new Polygon3DBIZ()
    this._odline = new ODLineBIZ()
    this._radiationPoint = new RadiationPointBIZ()
    this._editHelper = new EditHelperBIZ()
    this._tools = new ToolsBIZ()
    this._viewshed = new ViewshedBIZ()
    this._misc = new MiscBIZ()
    this._weather = new WeatherBIZ()
    this._beam = new BeamBIZ()
    this._highlightArea = new HighlightAreaBIZ()
    this._settings = new SettingsBIZ()
    // this._clip = new ClipBIZ(this)
    this._cameraTour = new CameraTourBIZ()
    this._infoTree = new InfoTreeBIZ()
    this._marker = new MarkerBIZ()
}

SealAirCityAPI.prototype.onReady = function () {
    window.origAPI = window.__g
    window.origAPI.misc.setMainUIVisibility(false); // 面板
    window.__g.settings.setCampassVisible(false);
    window.origAPI.tag.clear()
    globalObject.initClass()
}

/**
 * 获取id的值
 * @param {*} data '12' 、['23','23']、{id:'123'}、[{id:'123'},{id:'123'}]
 */
SealAirCityAPI.prototype.getIds = function (data) {
    let ids = null
    switch (Object.prototype.toString.call(data)) {
        case '[object String]':
        case '[object Number]':
            ids = data
            break
        case '[object Array]':
            ids = data.map(item => {
                if (Object.prototype.toString.call(item) === '[object Object]') {
                    if (!item.id) return item.id
                    return null
                }
                return item
            }).filter(item => !!item)
            break
        case '[object Object]':
            ids = data.id
            break
    }
    return ids
}

SealAirCityAPI.prototype.clear = function () {
    let that = this
    return Promise.all([
        // 清楚业务数据
        // that._polyline.hide_biz(that),
        // that._odline.clear(),
        // that._polygon.clear(),
        // that._polygon3d.clear(),
        // // that._radiationPoint.clear(),
        // // that._heatmap.clear(),
        // // that._highlightArea.clear(),
        // that._marker.clear(),
        // that._tag.clear(),
        that._viewshed.clear_biz(that),
        that._tools.clear_biz(),
        that._editHelper.cancel(),
        that._analyse.clear_biz(that),
    ])
}



export default SealAirCityAPI
