<!--
 * @FileDescription: 资源库->光源
 * @Author: yuanhaijun
 * @Date: 2023.04.07
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.07
 -->
<template>
    <div>
        <div v-if="isShowAddPoint" class="cloud-func draw-list">
            <div class="func-title">
                灯光
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
                        <div class="list" v-for="(item, index) in lampList" :key="index">
                            <el-checkbox v-model="item.isChecked" :checked="item.isChecked"
                                @change="checkedList(item, index)">
                            </el-checkbox>
                            <div>
                                <div class="list-left">
                                    <svg-icon :icon-class="icon" class-name="icon"></svg-icon>
                                    <span>{{ item.name }}</span>
                                </div>
                                <el-dropdown placement="left" @command="handleCommand">
                                    <span class="el-dropdown-link">
                                        <svg-icon icon-class="cloud-more" class-name="icon">
                                        </svg-icon>
                                    </span>
                                    <template v-slot:dropdown>
                                        <el-dropdown-menu class="cloud-dropdown" :disabled="disabled">
                                            <el-dropdown-item :command="{ type: '定位', index: index }">定位</el-dropdown-item>
                                            <el-dropdown-item
                                                :command="{ type: '编辑', item: item, index: index }">编辑</el-dropdown-item>
                                            <el-dropdown-item :command="{ type: '删除', index: index }">删除</el-dropdown-item>
                                        </el-dropdown-menu>
                                    </template>
                                </el-dropdown>
                            </div>
                        </div>
                    </el-scrollbar>
                </div>
                <div class="button-wrap">
                    <div @click="drawParam(1)"> <i class="el-icon-plus"></i> 点光源 </div>
                    <div @click="drawParam(2)"> <i class="el-icon-plus"></i> 聚光源 </div>
                </div>
            </div>
        </div>

        <div v-if="isShowPonitModel" class="cloud-func draw-form">
            <div class="func-title">
                新增{{ option.type == 1 ? "点光源" : "聚光源" }}
            </div>
            <el-scrollbar style="height: 100%">
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
                                <el-input-number v-model="option.coordinates[0][0]" @change="handleChange"
                                    controls-position="right"></el-input-number>
                            </div>
                            <div class="coor-item">
                                <span class="coor-name">Y</span>
                                <el-input-number v-model="option.coordinates[0][1]" @change="handleChange"
                                    controls-position="right"></el-input-number>
                            </div>
                            <div class="coor-item">
                                <span class="coor-name">Z</span>
                                <el-input-number v-model="option.coordinates[0][2]" @change="handleChange"
                                    controls-position="right"></el-input-number>
                            </div>
                        </div>
                    </div>
                    <div class="warp-list" v-show="option.type == 2">
                        <p>旋转角度</p>
                        <div class="angle-box">
                            <el-input-number v-model="option.rotation[0]" controls-position="right"
                                @change="handleChange"></el-input-number>
                            <el-input-number v-model="option.rotation[1]" controls-position="right"
                                @change="handleChange"></el-input-number>
                            <el-input-number v-model="option.rotation[2]" controls-position="right"
                                @change="handleChange"></el-input-number>
                        </div>
                    </div>
                    <div class="warp-list" v-show="option.type == 2">
                        <p>投射扇形夹角</p>
                        <div class="input-box">
                            <el-slider v-model="option.outerConAngle" @change="handleChange" :min="0" :max="180"
                                :step="1"></el-slider>
                            <el-input-number v-model="option.outerConAngle" controls-position="right" :min="0" :max="180"
                                :step="1" @change="handleChange"></el-input-number>
                        </div>
                    </div>
                    <div class="warp-list color-picker-box">
                        <p>颜色</p>
                        <div>
                            <el-color-picker size="small" show-alpha v-model="option.color"
                                @change="handleChange"></el-color-picker>
                            <el-input v-model="option.color" placeholder="输入，例如:#FF0000" @change="handleChange"></el-input>
                        </div>
                    </div>
                    <div class="warp-list">
                        <p>亮度</p>
                        <div class="input-box">
                            <el-slider v-model="option.intensity" @change="handleChange" :min="0" :max="1000000"
                                :step="100"></el-slider>
                            <el-input-number v-model="option.intensity" controls-position="right" :min="0" :max="1000000"
                                :step="100" @change="handleChange"></el-input-number>
                        </div>
                    </div>
                    <div class="warp-list">
                        <p>可视距离（米）</p>
                        <div class="input-box">
                            <el-slider v-model="option.distance" @change="handleChange" :min="0" :max="10000"
                                :step="1"></el-slider>
                            <el-input-number v-model="option.distance" controls-position="right" :min="0" :max="10000"
                                :step="1" @change="handleChange"></el-input-number>
                        </div>
                    </div>

                    <div class="warp-list">
                        <p>衰减半径（米）</p>
                        <div class="input-box">
                            <el-slider v-model="option.attenuationRadius" @change="handleChange" :min="0" :max="2000"
                                :step="1"></el-slider>
                            <el-input-number v-model="option.attenuationRadius" controls-position="right" :min="0"
                                :max="2000" :step="1" @change="handleChange"></el-input-number>
                        </div>
                    </div>
                    <div class="warp-list">
                        <p>开启阴影</p>
                        <el-radio-group v-model="option.castShadows">
                            <el-radio :label="true">是</el-radio>
                            <el-radio :label="false">否</el-radio>
                        </el-radio-group>
                    </div>
                </div>
            </el-scrollbar>
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
    coordinateType: 0, // (number) 坐标系类型，取值范围：0为Projection类型，1为WGS84类型，默认值：0
    coordinates: [[]],
    // 光源位置坐标，二维数组，光源类型是点光源和聚光源时参数为单个坐标点： [[X,Y,Z]]，
    // 光源类型是矩形光源时参数为包含起始点的坐标数组：[[X1,Y1,Z1], [X2,Y2,Z2]]，

    rotation: [0, 0, 0], // 光源旋转角度：[Pitch,Yaw,Roll]，数组元素类型：(number)，取值范围：[任意数值]，默认值：[0,0,0]
    type: 1, // 光源类型: 1点光源 2聚光源 3矩形光源
    color: "#FF0000",
    opacity: 1, // 透明度
    intensity: 50000, // 亮度，单位：流明，取值范围：[0~任意数值]
    distance: 500, // 光源的可视距离，单位：米，取值范围：[1~任意正数]，默认值：5000米
    attenuationRadius: 100, // 光源衰减半径，单位：米，取值范围：[0~任意数值]
    castShadows: true, // 光源是否开启阴影效果，注意：当添加多个光源时非常耗性能，默认关闭false
    outerConAngle: 45, // (number) 可选参数，聚光源投射的扇形夹角，单位：度，取值范围：[0~180]，默认值：45度，仅光源类型为聚光源时(type=2)生效
    // thickness (number) 可选参数，矩形光源的宽度，单位：米，取值范围：[0.1~20000]，默认值：3米，仅光源类型为矩形光源时(type=3)生效

    utype: "light",
    isChecked: true,
};

export default {
    data() {
        return {
            lampList: [], // 灯光列表
            option: {},
            editIndex: -1,
            icon: "cloud-lamp",
            isShowAddPoint: true,
            isShowPonitModel: false,

            editFlag: false,
            disabled: false,
            defaultTime: undefined
        };
    },
    components: {
        CircleClose,
    },
    computed: {
        ...mapState(useSysToolsCimStore, ['eventSealAPI'])
    },
    watch: {
        eventSealAPI: {
            indeterminate: true,
            deep: true,
            handler(val) {
                switch (val.eventtype) {
                    case "Measurement":
                        if (val.Type === "Coordinate") {
                            this.option.coordinates = [val.Coordinate];
                            if (!this.option.id) this.option.id = "ll_" + new Date().getTime();
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
        // this.initDateTime();
        this.initData('init');
    },
    methods: {
        async initDateTime() {
            // 获取日期时间
            let _DT = await window.sealAPI._weather.getDateTime();
            this.defaultTime = { ..._DT }

            // 设置系统时间为晚上
            let DT = new Date(`${_DT.year}-${_DT.month}-${_DT.day} 21:${_DT.minute}`);
            this.setDateTimeW(DT);
        },
        // API：设置日期时间
        setDateTimeW(DT = new Date(), dnLoop = false) {
            let PARM = {
                year: DT.getFullYear(),
                month: DT.getMonth() + 1,
                day: DT.getDate(),
                hour: DT.getHours(),
                minute: DT.getMinutes(),
                daynightLoop: dnLoop, // 是否日夜循环：0\false\关闭， 1\true\开启
            };
            window.sealAPI._weather.setDateTime(PARM.year, PARM.month, PARM.day, PARM.hour, PARM.minute, PARM.daynightLoop);
        },
        // 获取数据库灯光
        async getLamp() {
            let data = sessionStorage.getItem("QXZS_light")
            return data ? JSON.parse(data) : []
        },
        async initData(opr) {
            this.lampList = await this.getLamp();
            this.option = JSON.parse(JSON.stringify(defaultOption));

            // 初始效果
            if (opr === 'init') {
                await window.origAPI.light.clear();
                this.lampList.forEach((item, i) => {
                    this.checkedList(item, i)
                });
            }
        },
        // 画灯光
        drawParam(type) {
            this.option.type = type;
            this.isShowAddPoint = false;

            // 坐标点
            this.$message.warning("请在场景中点选坐标！");
            window.sealAPI._tools.setMeasurement_biz(1);
        },
        async handleCommand(command) {
            let node = this.lampList[command.index];
            switch (command.type) {
                case "编辑":
                    this.editIndex = command.index;
                    this.option = JSON.parse(JSON.stringify(command.item));
                    this.editFlag = true;
                    this.isShowAddPoint = false;
                    this.$nextTick(() => {
                        this.addModule();
                    });
                    break;
                case "删除":
                    await window.origAPI.light.delete(node.id);
                    this.deleteLamp(node, command.index);
                    break;
                default:
                    window.origAPI.light.focus(node.id);
            }
        },
        // 删除数据库灯光
        deleteLamp(data, index) {
            // let index = this.lampList.findIndex(i => i.name === obj.name);
            this.lampList.splice(index, 1);
            sessionStorage.setItem("QXZS_light", JSON.stringify(this.lampList))
        },
        _colorBuild(option) {
            let r = parseInt(option.color.slice(1, 3), 16) / 255;
            let g = parseInt(option.color.slice(3, 5), 16) / 255;
            let b = parseInt(option.color.slice(5, 7), 16) / 255;
            return [r, g, b, option.opacity];
        },
        // 复选框事件
        async checkedList(item, index) {
            if (item.isChecked) {
                let res = await window.origAPI.light.get(item.id);
                if (res.data && res.data.length > 0) {
                    await window.origAPI.light.show(item.id);
                    window.origAPI.light.focus(item.id);
                } else {
                    await window.origAPI.light.add(item);
                    window.origAPI.light.focus(item.id);
                }
            } else window.origAPI.light.hide(item.id);
        },
        handleChange: debounce(function (e) {
            this.updateModule();
        }),
        async updateModule() {
            let option = {
                ...this.option,
                // color: this._colorBuild(this.option)
            };

            await window.origAPI.light.update(option);
            window.origAPI.light.focus(option.id);
            this.isShowPonitModel = true;
        },
        async addModule() {
            let option = {
                ...this.option,
                // color: this._colorBuild(this.option)
            };

            await window.origAPI.light.delete([option.id]);
            await window.origAPI.light.add(option);
            window.origAPI.light.focus(option.id);
            this.isShowPonitModel = true;
        },
        handleSure() {
            if (!this.option.name) return this.$message.warning("名称不能为空！");
            let targetIndex = this.lampList.findIndex(
                (item) => item.name == this.option.name
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");

            let option = JSON.parse(JSON.stringify(this.option));
            if (this.editIndex < 0) this.lampList.unshift(option);
            else {
                // this.$set(this.lampList, this.editIndex, option);
                this.lampList[this.editIndex] = option
                this.editIndex = -1;
            }

            this.option = JSON.parse(JSON.stringify(defaultOption));

            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editFlag) this.editLamp(option);
            else this.saveLamp(option);
        },
        // 保存灯光到后端数据库
        async saveLamp(obj) {
            let data = sessionStorage.getItem("QXZS_light")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_light", JSON.stringify(data))

            this.initData('init');
        },
        // 编辑灯光到后端数据库
        async editLamp(obj) {
            let index = this.lampList.findIndex(i => i.name === obj.name);
            this.lampList.splice(index, 1, obj);
            sessionStorage.setItem("QXZS_light", JSON.stringify(this.lampList))

            this.editFlag = false;
        },
        async handleCancel() {
            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editIndex < 0) await window.origAPI.light.delete(this.option.id);
            else this.editIndex = -1;
            this.option = JSON.parse(JSON.stringify(defaultOption));
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    beforeUnmount() {
        // 还原日期时间
        // let DT = new Date(`${this.defaultTime.year}-${this.defaultTime.month}-${this.defaultTime.day} ${this.defaultTime.hour}:${this.defaultTime.minute}`);
        // this.setDateTimeW(DT);

        if (this.option.id) window.origAPI.light.delete(this.option.id);
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

.angle-box>*:not(:last-child) {
    margin-bottom: 8px;
}
</style>