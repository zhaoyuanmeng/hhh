<template>
    <div class="cloud-query">
        <div class="query-title">
            构件查询
            <!-- <span @click="handleClose">
                <i class="el-icon-close"></i>
            </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="operate-btns">
            <div :class="{ active: isShowInfo }" @click="handleAttribute">
                <img src="@/assets/images/cloud/query/icon-attr.png" alt="属性" />
                <span>属性</span>
            </div>
            <div :class="{ active: isHide }" @click="handleHide">
                <img src="@/assets/images/cloud/query/icon-hide.png" alt="隐藏" />
                <span>隐藏</span>
            </div>
            <div :class="{ active: isDelOpera }" @click="handleDel">
                <img src="@/assets/images/cloud/query/icon-del.png" alt="删除" />
                <span>删除</span>
            </div>
        </div>
        <div class="construc-info" v-show="tilelayerid && objectid && !isHide && isShowInfo">
            <h3>
                构件ID
                <!-- <i v-show="isDelOpera" class="el-icon-delete" @click="handleDelBim"></i>
                 -->
                <el-icon @click="handleDelBim" v-show="isDelOpera">
                    <DeleteFilled />
                </el-icon>
            </h3>
            <p>
                图层: <br /><span>{{ tilelayerid }}</span>
            </p>
            <p>
                图元: <br /><span>{{ objectid }}</span>
            </p>
        </div>
        <!-- <div class="query-warp" v-if="bimModule && bimModule.attr.length > 0">
            <h3>
                {{ bimModule.levelname ? `${bimModule.levelname}${"-"}` : ""
                }}{{ bimModule.category }}
            </h3>
            <ul>
                <el-scrollbar style="height: 100%" ref="scrollbarEle">
                    <li v-for="(item, index) in bimModule.attr" :key="index">
                        <h5>{{ item.Name }}</h5>
                        <p>{{ item.Value || "---" }}</p>
                    </li>
                </el-scrollbar>
            </ul>
        </div> -->
        <div class="query-warp query-warp-2" v-if="isHide && hideIDs.length > 0">
            <h3>隐藏构件列表</h3>
            <ul>
                <el-scrollbar style="height: 100%" ref="scrollbarEle">
                    <li v-for="( item, index ) in  hideIDs " :key="index">
                        <!-- <i class="el-icon-view" :class="{ active2: !item.hide }" @click="handleHideSingle(index, item)"></i> -->
                        <el-icon :class="{ active2: !item.hide }" @click="handleHideSingle(index, item)">
                            <View />
                        </el-icon>
                        <p class="id-con">
                            <el-tooltip :content="item.tilelayerid" effect="customized" class="">
                                {{ item.tilelayerid }}
                            </el-tooltip>
                        </p>
                        <p class="id-con">{{ item.objectid }}</p>
                    </li>
                </el-scrollbar>
            </ul>
        </div>
        <div class="query-warp" v-show="!isShowInfo">
            <p class="alert-info">请选择查询类型状态！</p>
        </div>
        <div class="query-warp" v-show="!tilelayerid && !objectid && isShowInfo">
            <p class="alert-info">{{ alertInfo1 }}</p>
        </div>
    </div>
</template>
<script>
import { get, post, deletes } from "@/utils/fetch.js"
import { mapState } from 'pinia'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { View, DeleteFilled, CircleClose } from '@element-plus/icons-vue'
export default {
    data() {
        return {
            alertInfo: "请选择查询类型状态！",
            alertInfo1: "点击左侧模型！",
            isShowInfo: false,
            isHide: false,
            isDelOpera: false,
            hideIDs: [], // {tilelayerid,objectid,hide}
            tilelayerid: "",
            objectid: "",
            bimModule: null,
        };
    },
    components: {
        View,
        DeleteFilled,
        CircleClose
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
            handler(val) {
                if (
                    val.eventtype == "LeftMouseButtonClick" &&
                    val.Type == "TileLayer"
                ) {
                    // if (this.$parent.isShowBimConstruct) {
                    if (!val.Id || !val.ObjectID) return;

                    this.tilelayerid = val.Id;
                    this.objectid = val.ObjectID;

                    if (this.isShowInfo) {

                        this.$nextTick(async () => {
                            await window.sealAPI._tileLayer.stopHighlightActors();
                            await window.sealAPI._tileLayer.highlightActor(val.Id, val.ObjectID);
                        });
                    } else if (this.isHide) {
                        this.$nextTick(() => {
                            if (this.tilelayerid && this.objectid) {
                                let targetIndex = this.hideIDs.findIndex(
                                    (item) =>
                                        item.tilelayerid == this.tilelayerid &&
                                        item.objectid == this.objectid
                                );
                                if (targetIndex < 0)
                                    this.hideIDs.push({
                                        tilelayerid: this.tilelayerid,
                                        objectid: this.objectid,
                                        hide: true,
                                    });
                                else
                                    this.hideIDs.splice(targetIndex, 1, {
                                        ...this.hideIDs[targetIndex],
                                        hide: true,
                                    });

                                window.sealAPI._tileLayer.hideActor(this.tilelayerid, this.objectid);
                            }
                        });
                    }
                    else {
                        this.$nextTick(() => {
                            // this.getBimProps();
                        });
                    }
                    // }
                }
            },
        },
    },
    created() {
        // this.getBimDelete();
    },
    mounted() {
        /*
          let json = [
            { key: "nowname", name: "建筑名称", index: 0 },
            { key: "bldg_id", name: "房屋建筑代码", index: 1 },
            { key: "o_bldg_id", name: "旧建筑原代码", index: 2 },
            { key: "project_id", name: "项目代码", index: 3 },
            { key: "st_bldaddr", name: "标准地址", index: 5 },
            { key: "bldaddr", name: "详细地址", index: 6 },
            { key: "bp_id", name: "建设工程编规号划许可证", index: 7 },
            { key: "cp_id", name: "施工许可证编号", index: 8 },
            { key: "compac_id", name: "竣工验收合格证编号", index: 9 },
            { key: "caparc_id", name: "宗地代码", index: 10 },
            { key: "rprope_uid", name: "不动产单元代码", index: 11 },
            { key: "bldstru", name: "建筑结构", index: 12 },
            { key: "deadline", name: "使用年限", index: 13 },
            { key: "comp_date", name: "建造年代", index: 14 },
            { key: "bldcond", name: "建造状态", index: 15 },
            { key: "up_bldrg_f", name: "建筑层数（地上）", index: 16 },
            { key: "down_bldg_", name: "建筑层数（地下）", index: 17 },
            { key: "bldg_heig", name: "建筑高度", index: 18 },
            { key: "up_seats", name: "地面停车位", index: 19 },
            { key: "down_seats", name: "地下停车位", index: 20 },
            { key: "bldg_ld_ar", name: "基底面积", index: 21 },
            { key: "floor_area", name: "总建筑面积", index: 22 },
            { key: "bldg_usage", name: "主要用途", index: 23 },
            { key: "protected_", name: "保护性建筑类型", index: 24 },
            { key: "sp_anno", name: "特别说明", index: 25 },
            { key: "jingdu", name: "经度", index: 26 },
            { key: "weidu", name: "纬度", index: 27 },
            { key: "yey", name: "幼儿园", index: 28 },
            { key: "yy", name: "医院", index: 29 },
            { key: "lvsejz", name: "绿色建筑", index: 30 },
            { key: "wenhuajz", name: "文化建筑", index: 31 },
            { key: "xws", name: "学位数", index: 32 },
            { key: "name", name: "行政区划", index: 33 },
            { key: "lsjz", name: "历史建筑", index: 34 },
            { key: "gcgmd", name: "高层高密度", index: 35 },
          ];
        */

        this.alertInfo = "点击左侧模型！";
        window.bim = this;
    },
    methods: {
        async getBimProps() {
            await window.sealAPI._tileLayer.stopHighlightActors();
            await window.sealAPI._tileLayer.highlightActor(this.tilelayerid, this.objectid);

            get(`${cim_main}service/bim/props`, {
                objectid: this.objectid,
                tilelayerid: this.tilelayerid,
            }).then((data) => {
                let { message } = data;
                if (!message) {
                    this.alertInfo = "无信息！";
                    this.bimModule = null;
                    return;
                }
                message = { ...message, attr: JSON.parse(message.attr) };
                this.bimModule = message;
            });
        },
        getBimDelete() {
            let params = {
                page: 1,
                rows: 999999,
            };
            get(`${cim_main}freedo/cim/bim`, params).then((res) => {
                let data = [
                    // {id, objectIds}
                ];
                (res.message || []).forEach((item) => {
                    let { tilelayerid, objectid } = item;
                    if (tilelayerid && objectid)
                        data.push({ id: tilelayerid, objectIds: objectid });
                });
                window.sealAPI._tileLayer.hideActors(data);
                // window.sealAPI._tileLayer.showActors(data);
            });
        },
        postBimDelete(data) {
            // let data = {
            //   bimId: "",
            //   tilelayerid: "",
            //   objectid: "",
            //   status: 1,
            // };
            let token = this.$store.state.login.account.token;
            post(`${cim_main}freedo/cim/bim?token=${token}`, {
                ...data,
                bimId: "",
            }).then((res) => {
                if (res.message == 1) this.$message.success("删除成功");
                else {
                    this.$message.error("删除失败");
                    console.error(res);
                }
            });
        },
        // 未引用
        delBimDelete() {
            let params = {
                uuid: "",
            };
            deletes(`${cim_main}freedo/cim/bim`, params).then((res) => {
                console.log("getBimDelete", res);
            });
        },
        handleAttribute() {
            if (this.isShowInfo) {
                this.isShowInfo = false;
                this.bimModule = null;
                this.alertInfo = "点击左侧模型！";
                window.sealAPI._tileLayer.stopHighlightActors();
                return;
            }

            this.isShowInfo = true;
            this.isHide = false;
            this.isDelOpera = false;
            // if (this.tilelayerid && this.objectid) this.getBimProps();
        },
        handleHide() {
            if (this.isHide) {
                this.isHide = false;
                return;
            }

            this.isHide = true;
            this.isShowInfo = false;
            this.isDelOpera = false;
            this.bimModule = null;

            this.hideIDs.forEach((item) => {
                if (!item.hide)
                    window.sealAPI._tileLayer.highlightActor(item.tilelayerid, item.objectid);
            });
        },
        handleHideSingle(index, row) {
            if (row.hide) {
                window.sealAPI._tileLayer.showActor(row.tilelayerid, row.objectid);
                window.sealAPI._tileLayer.highlightActor(row.tilelayerid, row.objectid);
                this.hideIDs.splice(index, 1, { ...row, hide: false });
            } else {
                window.sealAPI._tileLayer.hideActor(row.tilelayerid, row.objectid);
                this.hideIDs.splice(index, 1, { ...row, hide: true });
            }
        },
        handleDel() {
            if (this.isDelOpera) {
                this.isDelOpera = false;
                return;
            }

            this.isDelOpera = true;
            this.isShowInfo = false;
            this.bimModule = null;
            this.isHide = false;
        },
        handleDelBim() {
            let delBimInfo = {
                tilelayerid: this.tilelayerid,
                objectid: this.objectid,
                status: 1,
            };

            this.tilelayerid = "";
            this.objectid = "";
            this.bimModule = null;
            this.isShowInfo = false;

            this.isHide = true;
            this.handleHide();

            window.sealAPI._tileLayer.hideActor(delBimInfo.tilelayerid, delBimInfo.objectid);
            let targetIndex = this.hideIDs.findIndex(
                (item) =>
                    item.tilelayerid == delBimInfo.tilelayerid &&
                    item.objectid == delBimInfo.objectid
            );
            if (targetIndex >= 0) this.hideIDs.splice(targetIndex, 1);
            this.alertInfo = "点击左侧模型！";
            // deving: 存入 attr 两个接口，一个存储，一次供场景加载时查询
            // this.postBimDelete(delBimInfo);
        },
        handleClose() {
            // this.$parent.closeNav();
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    beforeUnmount() {
        window.sealAPI._tileLayer.stopHighlightActors();
        this.hideIDs.forEach((item) => {
            if (item.hide) window.sealAPI._tileLayer.showActor(item.tilelayerid, item.objectid);
        });
    },
};
</script>
<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

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
        // border-top-left-radius: 15px;
        // border-top-right-radius: 15px;

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
    justify-content: space-between;

    div {
        cursor: pointer;
        display: inline-flex;
        flex-direction: column;
        align-items: center;
        justify-content: space-between;
        color: #fff;
        font-size: 12px;
        width: 46px;
        height: 46px;
        padding: 6px;
        background: #4d515a;
        box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
            0px 0px 0px 0px rgba(0, 0, 0, 0.5);
        border-radius: 8px;

        img {
            height: 26px;
        }
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
}

.construc-info {
    color: #fff;
    // padding-bottom: 20px;
    padding: 30px 0 30px 0;

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
</style>