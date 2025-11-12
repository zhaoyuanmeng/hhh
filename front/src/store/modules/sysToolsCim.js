/*
 * @Author: your name
 * @Date: 2022-03-30 10:19:03
 * @LastEditTime: 2022-04-21 18:24:50
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \DTSWEEKLY_ZHGK\src\stores\tools.ts
 */
import { defineStore } from "pinia";

export const useSysToolsCimStore = defineStore({
    id: "sysToolsCim",
    state() {
        return {
            // 服务映射
            serviceMapping: sessionStorage.getItem('QXZSZX_serviceMapping')
                ? JSON.parse(sessionStorage.getItem('QXZSZX_serviceMapping') )
                : {},
            // CIM数据（模型）
            allModel: sessionStorage.getItem('QXZSZX_allModel')
                ? JSON.parse(sessionStorage.getItem('QXZSZX_allModel') )
                : null,
            eventData: {},
            // 封装API的JS回调数据
            eventSealAPI: {},
            // 当前点击警力、警车的图标信息
            markerEvent:{},
            mapClickType:false,
            // 路由高亮小点显示
            monitoringcenterDot: '',
            // 门禁考勤信息
            CMAccessControl: {},
            // 按钮权限
            buttonPerArray: [],
            // 东站选中名字
            eastStationNaval: '可视化分析',
            // 点击的楼宇高亮信息 用来关闭高亮
            hightbuilding: {},
            // os图则多边形的信息
            os: [],
            // 头部点击样式相关
            headerIndex: 0,
            // 权限校验key && token
            key: '',
            //环境传感器弹窗
            popEvPos: [],
            //监控弹窗
            popVideoPos: [],
            // 登录信息
            login: sessionStorage.getItem('QXZSZX_LOGIN')
                ? JSON.parse(sessionStorage.getItem('QXZSZX_LOGIN') )
                : {
                    uploadtoken: '',
                },
            // 场景服务ID
            sceneServiceId: '',
            splitScreenSwitch: false,
            treeData: sessionStorage.getItem('QXZSZX_TREEDATA')
                ? JSON.parse(sessionStorage.getItem('QXZSZX_TREEDATA') )
                : {}, //图层树信息
            sceneInfo: sessionStorage.getItem('QXZSZX_SCENEINFO')
                ? JSON.parse(sessionStorage.getItem('QXZSZX_SCENEINFO') )
                : {}, //场景、服务相关信息
            isShowClip: false, //控制体剖切面板显隐
            isShowVisibleRangePanel: false, //控制显示可视域调节面板展示
            stbmValue: '',
            evPopShowFlag: false,
            imgBoxPopShowFlag: false,
            evPopInFo: {
                stbm: '',
                pointid: '',
                subType: '',
            },
            //指北针是否显示
            compassStatus: false,
            //统计分析弹窗是否显示
            isShowStatisticAnalysis: false,
            //统计分析图层列表
            tileLayerList: [],
            //统计分析构件·列表
            objectSum: 0,
            //统计分析polygon3d名称
            polygon3dId: '',
            //退线分析事件回调
            regressionAnalysis: {
                "eventtype": "Measurement",
                "Type": "VerticalLine",
                "HorizontalLength": "31.14",
                "VerticalHeight": "4.10",
                "StartCoordinate": [492936.5352734375, 2492018.516191406, 14.045277099609375],
                "VerticalCoordinate": [492936.5352734375, 2492018.516191406, 9.949510498046875],
                "EndCoordinate": [492949.4150390625, 2492046.8702246095, 9.949510498046875]
            },
            iid:"1753547947629",
        }
    },

    actions: {
        SET_MAPCLICKTYPE(BOL){
            this.$patch({
                mapClickType: BOL,
            })   
        },
        SET_IID(pyload) {
            this.$patch({
                iid: pyload,
            })
        },
        SET_REGRESSIONANALYSIS(pyload) {
            this.$patch({
                regressionAnalysis: pyload,
            })
        },
        SET_MARKEREVENT(value){
            this.$patch({
                markerEvent: value,
            })
        },
        SET_POLYGON3DID(pyload) {
            this.$patch({
                polygon3dId: pyload,
            })
        },
        SET_OBJECTNAMELIST(pyload) {
            this.$patch({
                objectSum: pyload,
            })
        },
        SET_TILELAYERLIST(pyload) {
            this.$patch({
                tileLayerList: pyload,
            })
        },
        SET_ISSHOWSTATISTICANALYSIS(pyload) {
            this.$patch({
                isShowStatisticAnalysis: pyload,
            })
        },
        SET_COMPASSSTATUS(pyload) {
            this.$patch({
                compassStatus: pyload,
            })
        },
        SET_POPVIDEOPOS(pyload) {
            this.$patch({
                popVideoPos: pyload
            })
        },
        SET_POPEVPOS(pyload) {
            this.$patch({
                popEvPos: pyload
            })
        },
        SET_EVPOPINFO(pyload) {
            this.$patch({
                evPopInFo: pyload
            })
        },
        SET_EVPOPSHOWFLAG(pyload) {
            this.$patch({
                evPopShowFlag: pyload
            })
        },
        SET_IMGBOXPOPSHOWFLAG(pyload) {
            this.$patch({
                imgBoxPopShowFlag: pyload
            })
        },
        SET_STBMVALUE(pyload) {
            this.$patch({
                stbmValue: pyload
            })
        },
        SET_ISSHOWCLIP(pyload) {
            this.$patch({
                isShowClip: pyload
            })
        },
        SET_KEY(pyload) {
            this.$patch({
                key: pyload
            })
        },
        SET_MAPPING(obj) {
            const keys = Object.keys(obj)
            keys.forEach((key) => {
                this.serviceMapping[key] = obj[key]
            })
            sessionStorage.setItem(
                'QXZSZX_serviceMapping',
                JSON.stringify(this.serviceMapping)
            )
        },
        SET_ALLMODEL(obj) {
            this.allModel = obj
            sessionStorage.setItem('QXZSZX_allModel', JSON.stringify(this.allModel))
        },
        SET_EVENTDATA(obj) {
            this.eventData = obj
        },
        async SET_EVENTSEALAPI(pyload) {
            // this.$patch({
            //     eventSealAPI: obj
            // })
            this.$patch({
                eventSealAPI: pyload,
            })
        },
        SET_TREEDATA(obj) {
            this.treeData = obj
            sessionStorage.setItem('QXZSZX_TREEDATA', JSON.stringify(this.treeData))
        },
        SET_SCENEINFO(obj) {
            this.sceneInfo = obj
            sessionStorage.setItem(
                'QXZSZX_SCENEINFO',
                JSON.stringify(this.sceneInfo)
            )
        },
        SET_LOGIN(obj) {
            this.login = obj
            sessionStorage.setItem('QXZSZX_LOGIN', JSON.stringify(this.login))
        },
        /* 是否分屏 */
        splitScreenSwitch(bool) {
            this.splitScreenSwitch = bool
        },
        clickRouter(data) {
            this.monitoringcenterDot = data
        },
        changeCMAccessControl(data) {
            this.CMAccessControl = data
        },
        /* 设置按钮权限 */
        setButtonPer(data) {
            this.buttonPerArray = data
        },
        /* 高亮的楼宇楼层id信息 */
        hightbuilding(obj) {
            this.hightbuilding = obj
        },
        /* os */
        oscase(Array) {
            this.os = Array
        },

        setEastStationNaval(state, data) {
            localStorage.setItem('eastStationNaval', data)
            this.eastStationNaval = localStorage.getItem('eastStationNaval')
        },
        /* 头部点击样式相关 */
        setNavHeaderIndex(state, data) {
            sessionStorage.setItem('headerIndex', data)
            this.headerIndex = Number(sessionStorage.getItem('headerIndex'))
        },
    },
    getters: {
        // eventData: (state) => state.eventData,
        // eventSealAPI: (state) => (state as any).eventSealAPI,
        // monitoringcenterDot: (state) => state.monitoringcenterDot,
        // CMAccessControl: (state) => state.CMAccessControl,
        // eastStationNaval: (state) => state.eastStationNaval,
        // login: (state) => state.login,
        // treeData: (state) => state.treeData,
        // sceneInfo: (state) => state.sceneInfo,
    }
});
