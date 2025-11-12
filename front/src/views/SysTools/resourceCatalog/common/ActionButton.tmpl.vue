<!--
 * @FileDescription: 【绘制-操作按钮】需要通过API获取坐标
 * @Author: yuanhaijun
 * @Date: 2023.03.30
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.07
 -->
<template>
    <div class="cloud-action-button">
        <div @click="handleSure">
            <el-icon><Select /></el-icon>
            <p>确定绘制</p>
        </div>
        <div @click="handleCancel('CX')">
            <el-icon>
                <CloseBold />
            </el-icon>
            <p>取消绘制</p>
        </div>
        <!-- <div @click="goBack()" v-if="showDel">
            <el-icon><RefreshLeft /></el-icon>
            <p>撤销绘制</p>
        </div> -->
    </div>
</template>

<script>
import { Select, CloseBold,RefreshLeft } from '@element-plus/icons-vue'
export default {
    props: {
        editHelper: {
            type: Object,
            default: () => {
                return {
                    flag: false,
                    type: "point", // point lines curve shape
                }
            },
        },
        showDel:{
            type:Boolean,
            default:false
        }
    },
    components: {
        Select,
        CloseBold,
    },
    data() {
        return {
            coords: [], // 坐标点
        };
    },
    watch: {
        "$store.getters.eventSealAPI": {
            indeterminate: true,
            deep: true,
            handler(val) {
                switch (val.eventtype) {
                    case "Measurement":
                        if (val.Type === "Coordinate") {
                            this.coords = val.Coordinate;
                        }
                        return;
                }
            }
        }
    },
    mounted() {
        let _TP = this.editHelper.type;
        switch (_TP) {
            case "point":
                this.$message.warning("请在场景中点选坐标！");
                window.sealAPI._tools.setMeasurement_biz(1);
                break;
            default:
                this.setParam(_TP);
                break;
        }
    },
    methods: {
        goBack(){
            console.log('撤销')
        },
        // 【EditHelper】用户手动绘制 设置绘制参数
        setParam(type) {
            if (!(["lines", "shape", "curve", "body"].includes(type))) return;
            let lineType = type == "curve" ? 1 : 0; //0：直线，1：曲线
            let buildType = type == "lines" || type == "curve" ? 0 : 1; //0：画多点线段， 1：画多边形
            let color = Color.Red; //绘制颜色
            window.origAPI.editHelper.setParam(lineType, buildType, color);
            window.origAPI.editHelper.start();
        },
        async handleSure() {
            let _TP = this.editHelper.type;
            switch (_TP) {
                case "point":
                    if (this.coords.length === 0) {
                        this.$message.warning("未获取到坐标，请在场景中拾取！");
                        return;
                    }
                    break;
                default:
                    {
                        let res = await window.origAPI.editHelper.finish(true);
                        if (!res || res.coordinates.length < 2) {
                            this.$message.warning("请先选择绘制区域！")
                            this.setParam(_TP);
                            return;
                        }
                        this.coords = res.coordinates
                    }
                    break;
            }
            this.$parent.getEditDate(this.coords);
            console.log(this.coords)
            // this.$emit('getEditDateFun',this.coords)
        },
        handleCancel(opr) {
            let _TP = this.editHelper.type;
            switch (_TP) {
                case "point":
                    window.sealAPI._tools.stopMeasurement_biz();
                    break;
                default:
                    window.origAPI.editHelper.cancel();
                    break;
            }
            //this.$emit('closeEditHelperFun')
            if (opr === "CX")  this.$parent.closeEditHelper();
        },
    },
    beforeUnmount() {
        this.handleCancel();
    },
};
</script>

<style lang="scss" scoped>
.cloud-action-button {
    position: fixed;
    top: 38%;
    // right: 33px;
    right: 0px;
    // transform: translateY(-50%);
    transform: scale(0.8);
    z-index: 50;
    // width: 88px;
    height: 214px;
    background: rgba(46, 52, 65, 0.8);
    box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5), 0px 2px 5px 0px rgba(38, 38, 44, 0.5);
    border-radius: 9px;
    padding: 0 16px;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;

    div {
        padding: 16px 0;
        box-sizing: border-box;
        flex: 1;
        color: #ffffff;

        i {
            cursor: pointer;
            width: 56px;
            height: 56px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 40px;
        }

        i:hover {
            background: rgba(216, 216, 216, 0.2);
            border-radius: 8px;
        }

        p {
            font-size: 14px;
            font-weight: 400;
            color: #ffffff;
        }
    }

    div:nth-child(1) {
        border-bottom: 1px solid #686c7c;
    }
}
</style>