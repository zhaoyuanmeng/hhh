<template>
    <div class="cloud-query" v-if="!isShowSetting">
        <div class="query-title">
            模型替换
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="operate-btns">
            <div class="item">
                <!-- <div :class="{ active: isShowInfo == '选取替换源' }" @click="handleAttribute('选取替换源')">
                    <img src="@/assets/images/cloud/query/icon-attr.png" alt="属性" />
                    <span>选取替换源</span>
                </div> -->
                <p>模型源</p>
                <el-select v-model="sourceModel" @change="handleChangeSourceModel" placeholder="请选择模型" class="select">
                    <el-option :value="key" :label="key" v-for="(model, key) in sourceModelList" :key="model"
                        placement="bottom"></el-option>
                </el-select>
            </div>
            <div class="item">
                <div :class="{ active: isShowInfo == '选取目标源' }" @click="handleAttribute('选取目标源')">
                    <img src="@/assets/images/cloud/query/icon-attr.png" alt="属性" />
                    <span>选取目标源</span>
                </div>
            </div>
        </div>
        <div style="width: 100%;text-align: center;">
            <el-button type="primary" v-show="sourceModel && targetTilelayerid && targetObjectid && isShowInfo"
                @click="handleChangeModel">更换</el-button>
        </div>
        <div class="construc-info">
            <h3>
                构件ID
            </h3>
            <p>
                图层: <br /><span>{{ isShowInfo == '选取替换源' ? tilelayerid : targetTilelayerid }}</span>
            </p>
            <p>
                图元: <br /><span>{{ isShowInfo == '选取替换源' ? objectid : targetObjectid }}</span>
                <el-tooltip class="box-item" effect="dark" content="回显" placement="top" v-for="item in customObjectList"
                    :key="item.targetTilelayerid"><el-icon style=" cursor: pointer; font-size: 25px;"
                        @click="handleResetTocusTomObj_or_Actor(item)">
                        <Refresh />
                    </el-icon>
                </el-tooltip>
            </p>
        </div>
    </div>
    <div class="cloud-setting" v-if="isShowSetting">
        <div class="title">属性设置</div>
        <div class="itemSettingType">Location</div>
        <div class="settingLocation">
            <div class="item">
                <span>X</span>
                <el-input-number v-model="settingX" @change="handleChange" />
            </div>
            <div class="item">
                <span>Y</span>
                <el-input-number v-model="settingY" @change="handleChange" />
            </div>
            <div class="item">
                <span>Z</span>
                <el-input-number v-model="settingZ" @change="handleChange" />
            </div>
        </div>
    </div>
</template>
<script>
import { get, post, deletes } from "@/utils/fetch.js"
import { mapState } from 'pinia'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { CircleClose, Refresh } from '@element-plus/icons-vue'
export default {
    data() {
        return {
            alertInfo: "点击左侧模型！",
            isShowInfo: false,
            isHide: false,
            isDelOpera: false,
            hideIDs: [], // {tilelayerid,objectid,hide}
            tilelayerid: "",
            objectid: "",
            bimModule: null,
            targetTilelayerid: '',
            targetObjectid: "",
            customObjectList: [],
            settingX: 0,
            settingY: 0,
            settingZ: 0,
            settingObjectId: '',
            isShowSetting: false,
            sourceModel: '',
            sourceModelList: sourceModelList,
            sourceModelRotation: [],
            sourceModelScale: [],
            sourceModelStartHeight: 0,
            nowIsShow: ''
        };
    },
    components: {
        CircleClose,
        Refresh,
    },
    computed: {
        ...mapState(useSysToolsCimStore, ['eventSealAPI']),
        showAlertInfo() {
            if (this.isHide) return !this.hideIDs.length > 0;
            else if (this.isShowInfo) return !this.bimModule;
            else if (!this.tilelayerid || !this.objectid) return true;
            else return false;
        },
    },
    watch: {
        eventSealAPI: {
            indeterminate: true,
            deep: true,
            async handler(val) {
                if (val.eventtype == "LeftMouseButtonClick" && val.Type == "TileLayer") {
                    if (!val.Id || !val.ObjectID) return;
                    if (this.isShowInfo == '选取替换源') {
                        if (this.tilelayerid && this.objectid) await __g.tileLayer.stopHighlightActor(this.tilelayerid, this.objectid);
                        await window.sealAPI._tileLayer.highlightActor(val.Id, val.ObjectID);
                        this.tilelayerid = val.Id;
                        this.objectid = val.ObjectID;
                    } else if (this.isShowInfo == '选取目标源') {
                        if (this.targetTilelayerid && this.targetObjectid) await __g.tileLayer.stopHighlightActor(this.targetTilelayerid, this.targetObjectid);
                        await window.sealAPI._tileLayer.highlightActor(val.Id, val.ObjectID);
                        this.targetTilelayerid = val.Id;
                        this.targetObjectid = val.ObjectID;

                    }
                }
            },
        },
    },
    created() {
    },
    mounted() {
        window.sealAPI._tileLayer.stopHighlightActors();
        this.alertInfo = "点击左侧模型！";
        window.bim = this;
    },
    methods: {
        handleAttribute(actions) {
            if (this.isShowInfo == actions) {
                this.isShowInfo = '';
                this.bimModule = null;
                this.tilelayerid =
                    window.sealAPI._tileLayer.stopHighlightActors();
                return;
            }

            this.isShowInfo = actions;
        },
        async handleChangeModel() {
            __g.customObject.delete(this.customObjectList[0]?.id || '')
            this.customObjectList = []
            const res1 = await __g.tileLayer.getActorInfo({ //获取目标的位置
                id: this.targetTilelayerid,
                objectIds: [this.targetObjectid]
            })
            const location = res1.data[0].location
            location[2] = this.sourceModelStartHeight
            await __g.infoTree.hide(this.targetTilelayerid)
            const customObjectInfo = {
                id: 'customObjectActor' + this.targetTilelayerid + this.targetObjectid + this.sourceModelList[this.sourceModel].objectId[0],
                location: location,
                tileLayerId: this.sourceModelList[this.sourceModel].tileLayerId,
                //注意5.3新增特性：数组参数 也支持复制单个构件
                objectId: this.sourceModelList[this.sourceModel].objectId,
                coordinateType: 0,// 坐标系类型 
                rotation: this.sourceModelRotation,//模型旋转
                scale: this.sourceModelScale,//模型缩放
                smoothMotion: 0     //1: 平滑移动，0: 跳跃移动
            }
            await __g.customObject.addByTileLayer(customObjectInfo)
            this.customObjectList.push({
                targetActorid: this.targetObjectid,
                targetTilelayerid: this.targetTilelayerid,
                customObjectid: customObjectInfo.id,
                nowIsShow: 'customObj',
                location: location,
                scale: customObjectInfo.scale,
                id: customObjectInfo.id
            })

            this.tilelayeri = ""
            this.objectid = ""
            this.sourceModelRotation = ''
            this.sourceModelScale = ''
            this.isShowInfo = false
            window.sealAPI._tileLayer.stopHighlightActors();
        },
        handleClose() {
            // this.$parent.closeNav();
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
        handleResetTocusTomObj_or_Actor(item) {
            if (item.nowIsShow == 'customObj') {
                __g.infoTree.show(targetModel.tileLayerId)
                __g.customObject.hide(item.customObjectid)
                item.nowIsShow = 'actor'
            } else {
                __g.infoTree.hide(targetModel.tileLayerId)
                __g.customObject.show(item.customObjectid)
                item.nowIsShow = 'customObj'
            }
        },
        handleSetting(item) {
            __g.tileLayer.stopHighlightActors()
            this.settingX = item.location[0]
            this.settingY = item.location[1]
            this.settingZ = item.location[2]
            this.settingObjectId = item.id
        },
        handleChange() {
            __g.customObject.setLocation(this.settingObjectId, [this.settingX, this.settingY, this.settingZ]);
        },
        async handleChangeSourceModel() {
            const res = await __g.tileLayer.getActorInfo({ id: this.sourceModelList[this.sourceModel].tileLayerId, objectIds: sourceModelList[this.sourceModel].objectId })
            this.sourceModelRotation = res.data[0].rotation
            this.sourceModelScale = res.data[0].scale
            this.sourceModelStartHeight = res.data[0].location[2]
        }
    },
    beforeUnmount() {
        window.sealAPI._tileLayer.stopHighlightActors();
        this.hideIDs.forEach((item) => {
            if (item.hide) window.sealAPI._tileLayer.showActor(item.tilelayerid, item.objectid);
        })
        __g.customObject.clear(null)
        this.customObjectList.forEach((item) => {
            __g.tileLayer.showActor(item.targetTilelayerid, item.targetActorid)
        })
    },
};
</script>
<style lang="scss" scoped>
.box-item {
    width: 100%;
    text-align: center;
    margin-top: 20px;
}

.cloud-query {
    position: fixed;
    top: 50%;
    right: 33px;
    transform: translateY(-50%);
    z-index: 5;
    width: 300px;
    // background: rgba(46, 52, 65, 0.8);
    // box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
    //     0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    // border-radius: 15px;
    background: rgba(0, 13, 31, 0.4);
    box-shadow: 0px 2px 8px 0px rgba(0, 31, 44, 0.3);
    border: 1px solid rgba(44, 225, 255, 0.4);
    border-top: 4px solid #2CE1FF;
    pointer-events: auto;
    color: #fff;

    // height: 80%;
    // padding-bottom: 20px;
    .query-title {
        height: 56px;
        line-height: 56px;
        font-size: 22px;
        color: #ffffff;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 25px;
        box-sizing: border-box;
        background: rgba(46, 52, 65, 0.8);
        border-top-left-radius: 15px;
        border-top-right-radius: 15px;

        span {
            cursor: pointer;
            display: inline-block;
            width: 22px;
            height: 22px;
            background: rgba(0, 0, 0, 0.5);
            box-shadow: 0px 0px 5px 0px rgba(0, 0, 0, 0.5);
            border: 1px solid #ffffff;
            border-radius: 50%;
            display: flex;
            justify-content: center;
            align-items: center;

            i {
                font-size: 8px;
            }
        }
    }

    .query-warp {
        padding: 10px;
        padding-bottom: 20px;
        box-sizing: border-box;
        font-size: 16px;
        font-weight: 400;
        color: #ffffff;

        .alert-info {
            text-align: center;
            padding: 40px 0 80px 0;
        }

        h3 {
            font-size: 20px;
            font-weight: 400;
            color: #ffffff;
            margin: 0;
            padding-bottom: 10px;
        }

        ul {
            height: 460px;

            /deep/ .el-scrollbar__wrap {
                overflow-x: hidden;
            }

            li {
                display: flex;
                align-items: stretch;
                border-bottom: solid 1px #666;

                h5,
                p {
                    box-sizing: border-box;
                    display: inline-flex;
                    align-items: center;
                    padding: 8px 5px;
                    background: #3f4246;
                    color: #848691;
                    font-size: 14px;
                    font-weight: 400;
                    word-break: break-all;
                    width: 120px;
                    margin: 0;
                }

                p {
                    background: #494d52;
                    color: #c0c2cc;
                    flex-grow: 1;
                }
            }
        }
    }

    .query-warp-2 {
        ul {
            /deep/ li {
                align-items: center !important;

                p.id-con {
                    box-sizing: border-box;
                    padding: 2px 5px !important;
                    width: 130px;
                    font-size: 10px !important;
                    overflow: hidden;
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 2;
                }

                i {
                    cursor: pointer;
                    color: #ccc;
                    padding: 0 2px;
                }

                .active2 {
                    color: #0396ff;
                }
            }
        }

        /deep/.el-input {
            --el-input-bg-color: transparent;
            // background-color: transparent;
        }

        /deep/ .el-select__popper .el-popper[role=tooltip] {
            background-color: transparent !important;
        }
    }
}

/*定义滚动条高宽及背景 高宽分别对应横竖滚动条的尺寸*/
::-webkit-scrollbar {
    width: 5px;
    background-color: rgba(14, 73, 121, 0.2) !important;
}

/*定义了滚动条滑块的样式*/
::-webkit-scrollbar-thumb {
    border-radius: 0;
    border-width: 1.5px;
    background-clip: padding-box;
    background: #ededed;
}

.operate-btns {
    padding: 10px 16px;
    display: flex;
    align-items: center;
    justify-content: space-around;

    /deep/.el-input {
        --el-input-bg-color: transparent;
        // background-color: transparent;
    }

    /deep/ .el-select__popper .el-popper[role=tooltip] {
        background-color: transparent !important;
    }

    div {
        cursor: pointer;
        display: inline-flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        color: #fff;
        font-size: 12px;
        width: 76px;
        height: 76px;
        padding: 6px;
        // background: #4d515a;
        // box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        //     0px 0px 0px 0px rgba(0, 0, 0, 0.5);
        // border-radius: 8px;

        img {
            height: 46px;
        }

        p {
            margin-top: 10px;
        }
    }

    .item {
        padding: 0;
    }

    .active {
        background: #535d84;
        border-style: hidden;
        position: relative;
    }

    .active:after {
        content: "";
        position: absolute;
        top: -2px;
        bottom: -2px;
        left: -2px;
        right: -2px;
        background: linear-gradient(to right, #0396ff, #21f7e8);
        border-radius: 8px;
        z-index: -1;
    }

    .select {
        width: 130px;
    }
}

.construc-info {
    color: #fff;
    padding-bottom: 20px;

    h3 {
        color: #fff;
        margin: 5px 16px;
        display: flex;
        align-items: center;

        i {
            color: rgb(218, 92, 72);
            margin-left: 10px;
            font-size: 16px;
            cursor: pointer;
        }
    }

    p {
        padding: 5px 16px;
        color: #ccc;

        span {
            font-size: 12px;
            color: #fff;
        }
    }
}

.el-popper.is-customized {
    /* Set padding to ensure the height is 32px */
    padding: 6px 12px;
    background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
    color: #fff;
}

.el-popper.is-customized .el-popper__arrow::before {
    background: linear-gradient(45deg, #b2e68d, #bce689);
    right: 0;
    color: #fff;
}

.cloud-setting {
    position: fixed;
    top: 50%;
    right: 33px;
    transform: translateY(-50%);
    z-index: 5;
    width: 300px;
    height: 400px;
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    border-radius: 15px;
    pointer-events: auto;
    color: #fff;

    .title {
        height: 56px;
        line-height: 56px;
        font-size: 22px;
        color: #ffffff;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 25px;
        box-sizing: border-box;
        background: rgba(46, 52, 65, 0.8);
        border-top-left-radius: 15px;
        border-top-right-radius: 15px;
    }

    .settingLocation {
        padding: 10px;
        padding-bottom: 20px;
        box-sizing: border-box;
        font-size: 16px;
        font-weight: 400;
        color: #ffffff;
        display: flex;
        flex-direction: column;

        .item {
            display: flex;
            justify-content: space-around;
            align-items: center;

            /deep/ .el-input-number .el-input__inner {
                background: rgba(46, 52, 65, 0.8);
                box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
                    0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
                color: #fff;

            }

            /deep/ .el-input-number__decrease,
            .el-input-number__increase {
                background: rgba(46, 52, 65, 0.8);
                box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15);
                color: #fff
            }

            /deep/ .el-input-number__increase {
                background: rgba(46, 52, 65, 0.8);
                box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15);
                color: #fff
            }
        }
    }
}

/deep/ .el-input__inner {
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    color: #fff;

}
</style>