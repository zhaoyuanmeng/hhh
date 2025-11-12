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
            展示控制
            <el-icon class="" @click.stop="handleClose" style="cursor:pointer">
                <CircleClose />
            </el-icon>
        </div>

        <div class="func-warp draw-list">
            <div class="warp-list">
                <el-scrollbar style="height: 100%">
                    <div class="title">
                        <div>名称</div>
                        <div>透明度</div>
                    </div>
                    <div class="list" v-for="(item, index) in cameraViews" :key="index" style="cursor: pointer;">
                        <div class="list-left">
                            <el-icon class="icon " v-if="activeId.indexOf(item.name) == -1"
                                @click="controlLayerTree(item.name, index)">
                                <View />
                            </el-icon>
                            <el-icon class="icon ischeck" v-if="activeId.indexOf(item.name) !== -1"
                                @click="controlLayerTree(item.name, index)">
                                <Remove />
                            </el-icon>
                            <el-dropdown trigger="click" v-if="item.name !== '默认'" :hide-on-click="false"
                                :teleported="false" class="el-dropdownMenu">
                                <el-icon class="icon" style="margin-left: 10px;color: rgba(255,255,255,0.6);">
                                    <ArrowDownBold />
                                </el-icon>
                                <template #dropdown>
                                    <el-dropdown-menu>
                                        <el-dropdown-item v-for="(layer, key) in layerOpenStatus[item.name]" :key="layer">
                                            <el-checkbox v-model="layerOpenStatus[item.name][key]" :label="key" size="large"
                                                :disabled="activeId.indexOf(item.name) == -1"
                                                @change="changeLayerShowStatus($event, key)" />
                                        </el-dropdown-item>
                                    </el-dropdown-menu>
                                </template>
                            </el-dropdown>
                            <span :class="{ 'ischeck': activeId.indexOf(item.name) !== -1 }">{{ item.name }}</span>
                            <el-slider v-model="item.transparency" size="small" :max="1" :step="0.01"
                                @change="transparencyChange($event, item.name)" v-if="item.name !== '默认'"
                                :disabled="activeId.indexOf(item.name) == -1" :teleported="false"
                                :popper-append-to-body='false' />
                        </div>
                    </div>
                </el-scrollbar>
            </div>
        </div>

    </div>
</template>

<script>
import { get, post, put, deletes } from "@/utils/fetch";
import { CircleClose, View, Remove, ArrowDownBold } from '@element-plus/icons-vue';
import { useAirCityStore } from "@/stores/aircity";
import { mapState } from 'pinia'
export default {
    data() {
        return {
            cameraViews: TreeTypeList,
            layerOpenStatus: layerTreeIsShowInfo,
            nameVP: "",
            activeId: JSON.parse(sessionStorage.getItem('activeLayerId')) ? JSON.parse(sessionStorage.getItem('activeLayerId')) : [],
            activeType: '',
            value1: false,
            isShowTreeIdArr: [],
            XLightTileLayerIdArr: [],
            isOpenTransparency: false,
            checked1: false
        };
    },
    created() {
        // this.initData()
    },
    mounted() {
        this.formArray()
    },
    watch: {
    },
    components: {
        View,
        CircleClose,
        Remove,
        ArrowDownBold
    },
    computed: {
        ...mapState(useAirCityStore, ['layerTreeObject', 'resetTreeInfo', 'TreeInfo'])
    },
    methods: {
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
        async controlLayerTree(name, id) {
            if (name !== '默认') {
                if (this.activeId.indexOf('默认') !== -1) {
                    this.activeId.splice(this.activeId.indexOf('默认'), 1)
                }
                const index = this.activeId.indexOf(name)
                if (index !== -1) {
                    this.activeId.splice(index, 1)//菜单多选取消选中
                    const hideTreeIdArr = []
                    for (const key in layerTreeIsShowInfo[name]) {
                        if (Object.hasOwnProperty.call(layerTreeIsShowInfo[name], key)) {
                            const element = layerTreeIsShowInfo[name][key]
                            layerTreeIsShowInfo[name][key] = false
                            hideTreeIdArr.push(this.layerTreeObject[key])
                            this.isShowTreeIdArr.splice(this.isShowTreeIdArr.indexOf(this.layerTreeObject[key]))
                        }
                    }
                    await __g.infoTree.hide(hideTreeIdArr)
                }
                else {
                    this.activeId.push(name)//菜单多选选中
                    for (const key in layerTreeIsShowInfo[name]) {
                        if (Object.hasOwnProperty.call(layerTreeIsShowInfo[name], key)) {
                            const element = layerTreeIsShowInfo[name][key];
                            this.isShowTreeIdArr.push(this.layerTreeObject[key])
                            layerTreeIsShowInfo[name][key] = true
                        }
                    }
                    if (this.activeId.length == 1) { //仅有一个选中时，需要先隐藏全部
                        await __g.infoTree.hide(this.layerTreeObject["世界"])
                        await __g.infoTree.show(this.isShowTreeIdArr)
                    } else {        //不仅一个选中时，直接显示添加的
                        await __g.infoTree.show(this.isShowTreeIdArr)
                    }
                }
                if (this.cameraViews[id].camera.length !== 0 && index == -1) {
                    __g.camera.set(this.cameraViews[id].camera)
                }
            } else {
                this.activeId = ['默认']
                const camera = await __g.camera.get()
                console.log(camera.camera);
                await __g.reset()
                await __g.reset(6)
                // __g.settingsPanel.setCameraMode(0.1, 90, 5, 60000)
                const res = await __g.infoTree.get()
                const trueArr = []
                const falseArr = []
                res.infotree.forEach((item) => {
                    if (item.visiblity && item.type == 'EPT_Scene') trueArr.push(item.iD)
                    else if (!item.visiblity && item.type == 'EPT_Scene') falseArr.push(item.iD)
                })
                __g.infoTree.show(trueArr)
                __g.infoTree.hide(falseArr)
                __g.camera.set(camera.camera)
                await __g.settings.setMainUIVisibility(false)
                await __g.settings.setCampassVisible(false);
            }
            sessionStorage.setItem('activeLayerId', JSON.stringify(this.activeId))
        },
        formArray() {
            const _this = this
            this.cameraViews.forEach((item) => {
                item.transparency = 1,
                    item.isOpenTransparency = false
            })
        },
        transparencyChange(nV, name) {
            const LayerType = this.cameraViews.find(item => item.name == name)
            let typeIdArr = []
            this.XLightTileLayerIdArr = []
            for (const key in layerTreeIsShowInfo[LayerType.name]) {
                if (Object.hasOwnProperty.call(layerTreeIsShowInfo[LayerType.name], key)) {
                    const element = this.TreeInfo.find((item) => {
                        return item.name == key
                    })
                    if (element.type == 'EPT_Folder') {
                        this.getLayerTreeToTileLayer(element)
                        typeIdArr.push(...this.XLightTileLayerIdArr)
                    } else {
                        typeIdArr.push(element.iD)
                        // typeIdArr.push(this.layerTreeObject[element.name])
                    }
                }
            }
            if (nV < 1) {
                __g.tileLayer.enableXRay(typeIdArr, [0 / 255, 89 / 255, 255 / 255, nV]);
            } else {
                __g.tileLayer.disableXRay(typeIdArr);
            }
        },
        getLayerTreeToTileLayer(infoTree) { //
            console.log(infoTree.type, infoTree.name);
            this.TreeInfo.forEach((item) => {
                if (item.parentIndex == infoTree.index) {
                    if (item.type !== 'EPT_Folder') {
                        this.XLightTileLayerIdArr.push(item.iD)
                    } else {
                        this.getLayerTreeToTileLayer(item)
                    }
                }
            })
        },
        changeLayerShowStatus(nV, name) {
            if (nV) {
                __g.infoTree.show(this.layerTreeObject[name])
            } else {
                __g.infoTree.hide(this.layerTreeObject[name])
            }
        }
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";


.el-dropdown-menu {
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    border-radius: 15px;
}

.el-dropdown-menu__item {
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    border-radius: 15px;

    &:hover {
        background: rgba(46, 52, 65, 0.8);
        box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
            0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
        border-radius: 15px;
    }
}

.el-dropdown__popper.el-popper[role=tooltip] {
    background: rgba(46, 52, 65, 0.8);
    border: #5d5f61 1px solid;
}

/deep/ .el-dropdown__popper.el-popper[role=tooltip] {
    background: rgba(46, 52, 65, 0.8);
    // box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
    //     0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    // border-radius: 15px;
    // padding: 0%;
}

/deep/ .el-popper.is-light {
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    border-radius: 15px;
}

.el-checkbox {
    color: #fff
}

.el-dropdown__popper {
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    border-radius: 15px;
}

.el-slider {
    width: 120px;
    float: right;
    margin-left: 10px;
}

.slider-demo-block {
    display: flex;
    align-items: center;
}

.slider-demo-block .el-slider {
    margin-top: 0;
    margin-left: 12px;
}

.slider-demo-block .demonstration {
    font-size: 12px;
    color: var(--el-text-color-secondary);
    line-height: 44px;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-bottom: 0;
}

.slider-demo-block .demonstration+.el-slider {
    flex: 0 0 70%;
}


.ischeck {
    color: #029eff;
}

.draw-list {

    .warp-list {
        overflow: hidden;

        .title {
            width: 285px;
            height: 40px;
            display: flex;
            justify-content: space-around;
        }

        .el-scrollbar__wrap {
            width: 400px;
            max-height: 70vh !important;
            margin-bottom: 0 !important;
            overflow-x: hidden;

            .list {
                width: 400px;
                line-height: 50px;
                display: flex;
                align-items: center;
                justify-content: space-between;
                border-bottom: 1px solid rgba(255, 255, 255, 0.1);
                padding: 0;

                .list-left {
                    width: 292px;
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
                        display: block;
                        width: 80px;
                        height: 50px;
                        margin-left: 12px;
                        text-align: center;
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
        // border: 1px solid #DCDFE6;
        border: 1px solid #2CE1FF;
        cursor: pointer;
        margin: 0 14px;

        i.el-icon-plus {
            margin-right: 4px;
        }
    }
}
</style>
’