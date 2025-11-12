/*
 * @Author: your name
 * @Date: 2021-11-18 22:54:40
 * @LastEditTime: 2022-04-25 13:20:56
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \fdaircityinit\src\stores\aircity.ts
 */

// @ts-check
import { defineStore } from "pinia";

export const useAirCityStore = defineStore({
    id: "aircity",
    state: () => ({
        AirCityPlayer: null,
        AirCityApi: null,
        TreeInfo: [],
        resetTreeInfo: [],
        layerTreeObject: {},
        layerTreeIdKeyObject: {},
        IsOnReady: false,
        iscontextmenu: false,
        // 当前选中的管廊
        polylineData: {
            Id: ""
        },
        currentPoint: {}, // 鼠标点击事件
        isStartPlaneClip: false,
        //导览列表
        AnimationList: null,
        AnimationImages: null,
        //图层信息 图层的bbox.....
        tileLayerInfo: [],
        weatherParams: {}, //存储环境参数
        checkBoxPoiIds:[]
    }),

    actions: {
         SetCheckBoxPoiIds(pyload) {
            this.$patch({
                checkBoxPoiIds: pyload,
            });
        },
        async SetWeatherParams(pyload) {
            this.$patch({
                weatherParams: pyload,
            });
        },
        async SetTileLayerInfo(pyload) {
            this.$patch({
                tileLayerInfo: pyload,
            });
        },
        async SetAnimationImages(pyload) {
            this.$patch({
                AnimationImages: pyload,
            });
        },
        async SetAnimationList(pyload) {
            this.$patch({
                AnimationList: pyload,
            });
        },
        async SetLayerTreeIdKeyObject(pyload) {
            this.$patch({
                layerTreeIdKeyObject: pyload,
            });
        },
        async SetLayerTreeObject(pyload) {
            this.$patch({
                layerTreeObject: pyload,
            });
        },
        async SetAirCityPlayer(pyload) {
            this.$patch({
                AirCityPlayer: pyload,
            });
        },
        async SetAirCityApi(pyload) {
            this.$patch({
                AirCityApi: pyload,
            });
        },
        async SetResetTreeInfo(pyload) {
            this.$patch({
                TreeInfo: pyload,
            });
        },
        async SetTreeInfo(pyload) {
            this.$patch({
                resetTreeInfo: pyload,
            });
        },
        async SetIsOnReady(pyload) {
            this.$patch({
                IsOnReady: pyload,
            });
        },
        async Setiscontextmenu(pyload) {
            this.$patch({
                iscontextmenu: pyload,
            });
        },
        focusPolyline(payload) {
            if (this.polylineData.Id) {
                __g.polyline.setBrightness(this.polylineData.Id, 1);
            }
            this.$patch({
                polylineData: payload
            })
            return Promise.all([__g.polyline.focus(payload.Id, 1500, 1), __g.polyline.setBrightness(payload.Id, 5)]);
        },
        setMouseEventPoint(payload) {
            this.$patch({
                currentPoint: payload
            })
            if (this.isStartPlaneClip) {
                this.startPlaneClip();
            }
        },
        startPlaneClip() {
            return __g.tools.startPlaneClip([this.currentPoint.MouseClickPoint[0], this.currentPoint.MouseClickPoint[1], this.currentPoint.MouseClickPoint[2] + 15], [180, 0, 90], true, true);
        },
        switchPlaneClip(payload) {
            this.$patch({
                isStartPlaneClip: payload
            })
        }
    },
});

// if (import.meta.hot) {
//   import.meta.hot.accept(acceptHMRUpdate(useAirCityStore, import.meta.hot));
// }
