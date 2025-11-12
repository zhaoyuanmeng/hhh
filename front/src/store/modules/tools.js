/*
 * @Author: your name
 * @Date: 2022-03-30 10:19:03
 * @LastEditTime: 2022-04-21 18:24:50
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \DTSWEEKLY_ZHGK\src\stores\tools.ts
 */
import { defineStore } from "pinia";
import { useDialogStore } from "./dialog";

export const useToolsStore = defineStore({
    id: "Tools",
    state: () => ({
        //   图层树展示
        LayerTreeShow: false,
        infotree: null,
        // 导览列表展示
        AnimationShow: false,
        // 天空盒展示
        SkyBoxShow: false,
        // 气象展示
        WeatherShow: false,
        // 建筑拆解展示
        BuildShow: false,
        // 拆楼信息保存
        Buildinfo: null,
        // 水淹分析弹窗
        FloodShow: false,
        // 分层分户按钮展示
        FenFlag: false,
        // 分层分户actor是否可以点击
        CanClickActor: false,
        // 构建查询分析展示
        attrShowFlag: false,
        //点击图层的id 用于判断是否属于构建分许的图层
        selectLayerId: null,
        //点击object的id 用于判断是否属于构建分许的object
        selectObjectId: null,
        //点击object构件的信息 用于弹窗展示
        attrBaseInfoList: null,
    }),

    actions: {
        async SetAttrBaseInfoList(pyload) {
            this.$patch({
                attrBaseInfoList: pyload,
            });
        },
        async SetSelectObjectId(pyload) {
            this.$patch({
                selectObjectId: pyload,
            });
        },
        async SetSelectLayerId(pyload) {
            this.$patch({
                selectLayerId: pyload,
            });
        },
        async SetAttrShowFlag(pyload) {
            this.$patch({
                attrShowFlag: pyload,
            });
        },
        async SetLayerTreeShow(pyload) {
            this.$patch({
                LayerTreeShow: pyload,
            });
        },
        async Setinfotree(pyload) {
            this.$patch({
                infotree: pyload,
            });
        },
        async SetAnimationShow(pyload) {
            this.$patch({
                AnimationShow: pyload,
            });
        },
        async SetSkyBoxShow(pyload) {
            this.$patch({
                SkyBoxShow: pyload,
            });
        },
        async SetWeatherShow(pyload) {
            this.$patch({
                WeatherShow: pyload,
            });
        },
        async SetBuildShow(pyload) {
            this.$patch({
                BuildShow: pyload,
            });
        },
        async SetBuildinfo(pyload) {
            this.$patch({
                Buildinfo: pyload,
            });
        },
        async SetFloodShow(pyload) {
            this.$patch({
                FloodShow: pyload,
            });
        },
        async SetFenFlag(pyload) {
            this.$patch({
                FenFlag: pyload,
            });
        },
        async SetCanClickActor(pyload) {
            this.$patch({
                CanClickActor: pyload,
            });
        },
    },
});
