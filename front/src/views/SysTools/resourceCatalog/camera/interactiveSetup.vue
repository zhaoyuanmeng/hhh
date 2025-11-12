<!--
 * @FileDescription: 相机->视点
 * @Author: yuanhaijun
 * @Date: 2023.04.03
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.03
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            交互设置
            <el-icon class="" @click.stop="handleClose" style="cursor:pointer">
                <CircleClose />
            </el-icon>
        </div>
        <div v-show="!isShowAddPanel" class="func-warp draw-list">
            <div class="warp-list">
                <el-scrollbar style="height: 100%">
                    <div class="list" v-for="(item, index) in interactiveSetupList" :key="index"
                        @click="interactiveSetup(item)" style="cursor: pointer;">
                        <div class="list-left">
                            <el-icon :class="['icon', { 'ischeck': activeId == item.name }]">
                                <Setting />
                            </el-icon>
                            <span :class="{ 'ischeck': activeId == item.name }">{{ item.name }}</span>
                        </div>
                    </div>
                </el-scrollbar>
            </div>
        </div>
        <div class="isLookUnderground">
            <div class="item">
                <span>开启观测地下</span>
                <el-switch v-model="value1" @change="changeIsLookUnderground" />
            </div>
            <div class="item">
                <span>指北针</span>
                <el-switch v-model="compassStatus" @change="changeIsOpenCompass" />
            </div>
        </div>
    </div>
</template>

<script>
import { get, post, put, deletes } from "@/utils/fetch";
import { CircleClose, Setting } from '@element-plus/icons-vue';
import { mapState } from 'pinia'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
export default {
    data() {
        return {
            interactiveSetupList: [
                {
                    name: '视角移动-慢',
                    id: 0,
                    data: [0.2, 0.2, false, false]
                },
                {
                    name: '视角移动-中',
                    id: 1,
                    data: [0.5, 0.5, false, false]
                },
                {
                    name: '视角移动-快',
                    id: 2,
                    data: [1, 1, false, false]
                }
            ],
            activeId: '',
            value1: false,
            // CompassStatus: false
        };
    },
    created() {
        // this.initData()
    },
    async mounted() {
        // this.initRoad()
        await __g.shapeFileLayer.clear()
        // this.initGeoJson()
    },
    beforeUnmount() {
        __g.imageryLayer.delete(["xa_wmts1"])
        __g.shapeFileLayer.clear()
        __g.geoJSONLayer.delete('layer2')
    },
    computed: {
        ...mapState(useSysToolsCimStore, ['compassStatus'])
    },
    components: {
        Setting,
        CircleClose,
    },
    methods: {
        handleCancel() {
            this.isShowAddPanel = false;
        },
        handleClose() {
            this.$emit('close')
        },
        interactiveSetup(item) {
            // alear('ccc')
            this.activeId = item.name
            __g.settingsPanel.setControlMode(...this.interactiveSetupList[item.id].data)
        },
        changeIsLookUnderground(val) {
            let nearClipPlane = 10;
            let fovH = 100;
            let minCameraHeight = -5;
            let maxCameraHeight = 100000;
            __g.settingsPanel.setCameraMode(nearClipPlane, fovH, minCameraHeight, maxCameraHeight);
            val ? __g.settingsPanel.setCameraMode(0.1, 90, -100, 60000) : __g.settingsPanel.setCameraMode(0.1, 90, 5, 60000)
        },
        async changeIsOpenCompass(val) {
            __g.settings.setCampassVisible(val);
            useSysToolsCimStore().SET_COMPASSSTATUS(val)
            if (val) {
                const res = await __g.imageryLayer.show(["xa_wmts1"])
                console.log(res, ' __g.imageryLayer.show(["xa_wmts1"]) __g.imageryLayer.show(["xa_wmts1"])');
            } else {
                __g.imageryLayer.hide(["xa_wmts1"])
            }
        },
        async initRoad() {
            __g.shapeFileLayer.clear()
            let polyline = {
                id: "sp2",
                file: '@path:/shp/国道.shp', //shp文件路径
                polylineDefaultThickness: 30,//线宽
                defaultColor: [1, 0, 0, 1],
                offset: [0, 0, 0],//坐标位置偏移量
                rotation: [0, 0, 0],//旋转
            };
            const res = await __g.shapeFileLayer.add(polyline)
            console.log(res, 'pooooooooooooooooooo');
            __g.shapeFileLayer.focus(polyline.id);
        },
        async initGeoJson() {
            //添加前先删除保证id唯一
            __g.geoJSONLayer.delete('layer2');

            //简单渲染器
            let simpleRenderer = {
                //渲染器类型
                type: RendererType.SimpleRenderer,
                //默认符号化配置
                defaultSymbol: {
                    //符号化类型枚举：0 simple-marker圆形点填充  1 simple-line线填充  2 simple-fill面填充 3 polygon3d填充
                    type: 1,
                    //填充颜色
                    color: [0, 1, 0, 1]
                }
            };

            //用简单渲染器添加GeoJSONLayer
            const res = await __g.geoJSONLayer.load({
                id: 'layer2',
                visible: true,//加载后是否显示
                url: '@path:/geoJson/国道.geojson',
                renderer: simpleRenderer
            });

        }
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.isLookUnderground {
    display: flex;
    width: 340px;
    // margin-left: 100px;
    margin-bottom: 20px;
    align-items: center;
    justify-content: space-around;
    text-align: center;

    span {
        display: block;
        margin-right: 10px;
        height: 15px;
    }

    .item {
        width: 160px;
        display: flex;
        justify-content: center;
        align-items: center;
    }
}

.ischeck {
    color: #029eff;
}

.draw-list {
    .warp-list {
        overflow: hidden;

        .el-scrollbar__wrap {
            max-height: 70vh !important;
            margin-bottom: 0 !important;
            overflow-x: hidden;

            .list {
                line-height: 50px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                padding: 0;

                .list-left {
                    font-size: 16px;
                    color: #ffffff;
                    display: flex;
                    align-items: center;

                    .icon {
                        width: 24px;
                        height: 24px;
                        font-size: 24px;
                    }

                    span {
                        margin-left: 12px;
                    }
                }

                .el-dropdown span {
                    display: flex;
                    align-items: center;
                }

                .icon {
                    cursor: pointer;
                    width: 24px;
                    height: 24px;
                }

                .icon-del {
                    display: flex;
                    align-items: center;
                    position: relative;

                    ul {
                        display: none;
                        width: 68px;
                        height: 40px;
                        line-height: 40px;
                        background: #5d5f61;
                        position: absolute;
                        top: -8px;
                        right: 20px;
                        border-radius: 4px;
                        text-align: center;
                        font-size: 16px;
                        color: white;

                        span {
                            cursor: pointer;
                        }
                    }
                }

                .icon-del:hover {
                    ul {
                        display: block;
                    }
                }
            }
        }
    }

}

.draw-form {
    .warp-list {
        p {
            margin-bottom: 8px;
        }
    }

    .button-wrap {
        margin: 20px 0 10px 0;
    }
}

/deep/.button-wrap {
    display: flex;
    justify-content: center;

    &.bg>div {

        &:nth-child(1) {
            color: #ffffff;
            border-color: #979797;
        }

        &:nth-child(2) {
            color: #029eff;
            border-color: #029eff;
        }
    }

    &>div {
        display: flex;
        align-items: center;
        justify-content: center;
        width: 116px;
        height: 38px;
        border-radius: 5px;
        border: 1px solid #2CE1FF;
        cursor: pointer;
        margin: 0 14px;

        i.el-icon-plus {
            margin-right: 4px;
        }
    }
}
</style>
