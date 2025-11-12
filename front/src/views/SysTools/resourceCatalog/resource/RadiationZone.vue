<!--
 * @FileDescription: 资源库->辐射区
 * @Author: yuanhaijun
 * @Date: 2023.04.06
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.06
 -->
<template>
    <div>
        <div v-if="isShowAddPoint" class="cloud-func draw-list">
            <div class="func-title">
                辐射区
                <!-- <span @click.stop="handleClose">
                                                            					<i class="el-icon-close"></i>
                                                            				</span> -->
                <el-icon class="" @click.stop="handleClose" style="cursor:pointer">
                    <CircleClose />
                </el-icon>
            </div>
            <div class="func-warp">
                <div class="warp-list">
                    <el-scrollbar>
                        <div class="list" v-for="(item, index) in radiationList" :key="index">
                            <el-checkbox v-model="item.isChecked" :checked="item.isChecked"
                                @change="checkedList(item, index)">
                            </el-checkbox>
                            <div>
                                <div class="list-left">
                                    <svg-icon :icon-class="icon" class-name="icon"></svg-icon>
                                    <span>{{ item.name }}</span>
                                </div>
                                <el-dropdown placement="left" :disabled="disabled" @command="handleCommand">
                                    <span class="el-dropdown-link">
                                        <svg-icon icon-class="cloud-more" class-name="icon">
                                        </svg-icon>
                                    </span>
                                    <template v-slot:dropdown>
                                        <el-dropdown-menu class="cloud-dropdown">
                                            <el-dropdown-item :command="{ type: '定位', index: index }">定位</el-dropdown-item>
                                            <el-dropdown-item :command="{ type: '删除', index: index }">删除</el-dropdown-item>
                                        </el-dropdown-menu>
                                    </template>
                                </el-dropdown>
                            </div>
                        </div>
                    </el-scrollbar>
                </div>
                <div class="button-wrap">
                    <div @click="drawParam()"> <i class="el-icon-plus"></i> 新增辐射区 </div>
                </div>
            </div>
        </div>
        <div v-if="isShowPonitModel" class="cloud-func draw-form">
            <div class="func-title">新增辐射区</div>
            <div class="func-warp">
                <div class="warp-list">
                    <p>名称</p>
                    <el-input v-model="option.name" placeholder="请输入名称" maxlength="200"></el-input>
                </div>
                <div class="warp-list">
                    <p>位置坐标</p>
                    <div class="coors-box">
                        <div class="coor-item">
                            <span class="coor-name">X</span>
                            <el-input-number v-model="option.coordinate[0]" @change="handleChange"
                                controls-position="right"></el-input-number>
                        </div>
                        <div class="coor-item">
                            <span class="coor-name">Y</span>
                            <el-input-number v-model="option.coordinate[1]" @change="handleChange"
                                controls-position="right"></el-input-number>
                        </div>
                        <div class="coor-item">
                            <span class="coor-name">Z</span>
                            <el-input-number v-model="option.coordinate[2]" @change="handleChange"
                                controls-position="right"></el-input-number>
                        </div>
                    </div>
                </div>
                <div class="warp-list">
                    <p>亮度</p>
                    <div class="input-box">
                        <el-slider v-model="option.intensity" @change="handleChange" :min="0" :max="1"
                            :step="0.1"></el-slider>
                        <el-input-number v-model="option.intensity" controls-position="right" :min="0" :max="1" :step="0.1"
                            @change="handleChange"></el-input-number>
                    </div>
                </div>
                <div class="warp-list">
                    <p>半径（米）</p>
                    <el-input-number v-model="option.radius" @change="handleChange" :min="1" :step="1"
                        label="描述文字"></el-input-number>
                </div>
            </div>
            <div class="button-wrap bg">
                <div @click="handleCancel"> 取消 </div>
                <div @click="handleSure" class="bg"> 确定 </div>
            </div>
        </div>

    </div>
</template>

<script>
import { debounce } from "@/utils/funs.js";
import { get, post, put, deletes } from "@/utils/fetch";
import { CircleClose } from '@element-plus/icons-vue'
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { mapState } from 'pinia'
const defaultOption = {
    id: "",
    name: "",
    utype: "radiationPoint",
    radius: 5,
    intensity: 1
};
export default {
    data() {
        return {
            radiationList: [], // 辐射区列表
            option: {},
            editHelper: {
                flag: false,
                type: "point",
            },
            editIndex: -1,
            icon: "cloud-fushe",
            isShowAddPoint: true,
            isShowPonitModel: false,

            editFlag: false,
            disabled: false,
        };
    },
    computed: {
        ...mapState(useSysToolsCimStore, ['eventSealAPI'])
    },
    components: {
        CircleClose
    },
    watch: {
        eventSealAPI: {
            indeterminate: true,
            deep: true,
            handler(val) {
                switch (val.eventtype) {
                    case "Measurement":
                        if (val.Type === "Coordinate") {
                            this.option.coordinate = val.Coordinate;
                            if (!this.option.id) this.option.id = "tb_" + new Date().getTime();
                            this.$nextTick(() => {
                                this.addModule();
                            });
                            setTimeout(() => {
                                window.sealAPI._tools.stopMeasurement_biz();
                            }, 200);
                        }
                        return;
                }
            }
        }
    },
    created() {
        this.initData('init');
    },
    methods: {
        // 获取数据库辐射圈
        async getBooster() {
            let data = sessionStorage.getItem("QXZS_radiationPoint")
            return data ? JSON.parse(data) : []
        },
        async initData(opr) {
            this.radiationList = await this.getBooster();
            this.option = JSON.parse(JSON.stringify(defaultOption));

            // 初始效果
            if (opr === 'init') {
                await window.origAPI.radiationPoint.clear();
                this.radiationList.forEach((item, i) => {
                    this.checkedList(item, i)
                });
            }
        },
        drawParam() {
            this.isShowAddPoint = false;

            // 坐标点
            this.$message.warning("请在场景中点选坐标！");
            window.sealAPI._tools.setMeasurement_biz(1);
        },
        async handleCommand(command) {
            let node = this.radiationList[command.index];
            switch (command.type) {
                case "编辑":
                    this.isShowAddPoint = false;
                    this.editIndex = command.index;
                    this.editFlag = true;
                    this.option = JSON.parse(JSON.stringify(command.item));
                    this.$nextTick(() => {
                        this.addMeteor();
                    });
                    break;
                case "删除":
                    await window.origAPI.radiationPoint.delete(node.id);
                    this.deleteBooster(node, command.index);
                    break;
                default:
                    window.origAPI.radiationPoint.focus(node.id);
            }
        },
        // 删除数据库辐射圈
        deleteBooster(data, index) {
            // let index = this.radiationList.findIndex(i => i.name === obj.name);
            this.radiationList.splice(index, 1);
            sessionStorage.setItem("QXZS_radiationPoint", JSON.stringify(this.radiationList))
        },
        // 复选框事件
        async checkedList(item, index) {
            if (item.isChecked) {
                let res = await window.origAPI.radiationPoint.get(item.id);
                if (res.data && res.data.length > 0) {
                    await window.origAPI.radiationPoint.show(item.id);
                    window.origAPI.radiationPoint.focus(item.id);
                } else {
                    await window.origAPI.radiationPoint.add(item);
                    window.origAPI.radiationPoint.focus(item.id);
                }
            } else window.origAPI.radiationPoint.hide(item.id);
        },

        handleChange: debounce(function (e) {
            this.updateModule();
        }),
        async updateModule() {
            let option = {
                ...this.option,
            };

            await window.origAPI.radiationPoint.update(option);
            window.origAPI.radiationPoint.focus(option.id);
            this.isShowPonitModel = true;
        },
        async addModule() {
            let option = {
                ...this.option,
            };

            await window.origAPI.radiationPoint.delete([option.id]);
            await window.origAPI.radiationPoint.add(option);
            window.origAPI.radiationPoint.focus(option.id);
            this.isShowPonitModel = true;
        },
        handleSure() {
            if (!this.option.name) return this.$message.warning("名称不能为空！");
            let targetIndex = this.radiationList.findIndex(
                (item) => item.name == this.option.name
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");

            let option = JSON.parse(JSON.stringify(this.option));
            if (this.editIndex < 0) this.radiationList.push(option);
            else {
                // this.$set(this.radiationList, this.editIndex, option);
                this.radiationList[this.editIndex] = option
                this.editIndex = -1;
            }

            this.option = JSON.parse(JSON.stringify(defaultOption));

            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editFlag) this.editBooster(option);
            else this.saveBooster(option);
        },
        // 保存辐射圈到后端数据库
        async saveBooster(obj) {
            let data = sessionStorage.getItem("QXZS_radiationPoint")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_radiationPoint", JSON.stringify(data))

            this.initData('init');
        },
        // 编辑辐射圈到后端数据库
        async editBooster(obj) {
            let index = this.radiationList.findIndex(i => i.name === obj.name);
            this.radiationList.splice(index, 1, obj);
            sessionStorage.setItem("QXZS_radiationPoint", JSON.stringify(this.radiationList))

            this.editFlag = false;
        },
        async handleCancel() {
            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editIndex < 0) await window.origAPI.radiationPoint.delete(this.option.id);
            else this.editIndex = -1;
            this.option = JSON.parse(JSON.stringify(defaultOption));
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    beforeUnmount() {
        if (this.option.id) window.origAPI.radiationPoint.delete(this.option.id);
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";
@import "../css/draw.scss";

.coors-box .coor-item {
    &:not(:last-child) {
        margin-bottom: 8px;
    }

    .coor-name {
        margin-right: 14px;
    }
}

/deep/.el-input-number {
    line-height: 34px;

    .el-input-number__decrease,
    .el-input-number__increase {
        border-color: #646b6f;
        background: transparent;
        color: #ffffff;
    }
}
</style>