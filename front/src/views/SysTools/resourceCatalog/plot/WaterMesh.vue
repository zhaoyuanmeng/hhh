<!--
 * @FileDescription: 标绘->水纹
 * @Author: yuanhaijun
 * @Date: 2023.04.03
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.03
 -->
<template>
    <div>
        <template v-if="!editHelper.flag">
            <div v-if="isShowAddPoint" class="cloud-func draw-list">
                <div class="func-title">
                    水纹
                    <!-- <span @click.stop="handleClose">
                                <i class="el-icon-close"></i>
                            </span> -->
                    <el-icon class="" @click.stop="handleClose">
                        <CircleClose />
                    </el-icon>
                </div>
                <div class="func-warp">
                    <div class="warp-list">
                        <el-scrollbar>
                            <div class="list" v-for="(item, index) in rippleList" :key="index">
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
                                                <el-dropdown-item
                                                    :command="{ type: '定位', index: index }">定位</el-dropdown-item>
                                                <el-dropdown-item
                                                    :command="{ type: '编辑', item: item, index: index }">编辑</el-dropdown-item>
                                                <el-dropdown-item
                                                    :command="{ type: '删除', index: index }">删除</el-dropdown-item>
                                            </el-dropdown-menu>
                                        </template>
                                    </el-dropdown>
                                </div>
                            </div>
                        </el-scrollbar>
                    </div>
                    <div class="button-wrap">
                        <div @click="drawParam()"> <i class="el-icon-plus"></i> 新增水纹 </div>
                    </div>
                </div>
            </div>

            <div v-if="isShowPonitModel" class="cloud-func draw-form">
                <div class="func-title">新增水纹</div>
                <el-scrollbar style="height: 100%">
                    <div class="func-warp">
                        <div class="warp-list">
                            <p>名称</p>
                            <el-input v-model="option.name" placeholder="请输入名称" maxlength="200"></el-input>
                        </div>
                        <div class="warp-list">
                            <p>贴图间隔 (m)</p>
                            <div class="input-box">
                                <el-slider v-model="option.waterUVRepeat" @change="handleChange" :min="1" :max="1000"
                                    :step="10"></el-slider>
                                <el-input-number v-model="option.waterUVRepeat" controls-position="right" :min="1"
                                    :max="1000" :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>波纹强度 (m)</p>
                            <div class="input-box">
                                <el-slider v-model="option.waveScale" @change="handleChange" :min="0.1" :max="1"
                                    :step="0.05"></el-slider>
                                <el-input-number v-model="option.waveScale" controls-position="right" :min="0.1" :max="1"
                                    :step="0.01" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>水流方向 ( °)</p>
                            <div class="input-box">
                                <el-slider v-model="option.waterDirection" @change="handleChange" :min="0" :max="360"
                                    :step="10"></el-slider>
                                <el-input-number v-model="option.waterDirection" controls-position="right" :min="0"
                                    :max="360" :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>水流速度 (m/s)</p>
                            <div class="input-box">
                                <el-slider v-model="option.waterSpeed" @change="handleChange" :min="1" :max="30"
                                    :step="1"></el-slider>
                                <el-input-number v-model="option.waterSpeed" controls-position="right" :min="1" :max="30"
                                    :step="0.1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <!-- <div class="warp-list user-attr-box">
                            <p>自定义属性</p>
                            <ul>
                                <li v-for="(item, index) in option.cusAttr" :key="index">
                                    <el-input v-model="item.key" placeholder="key" @change="attrChange(item, index)" />
                                    <el-input v-model="item.value" placeholder="value" />
                                    <i class="icon-btn-del el-icon-remove-outline" @click="delUserAttr(item, index)"></i>
                                </li>
                            </ul>
                            <i class="icon-btn-add el-icon-circle-plus-outline" @click="addUserAttr"></i>
                        </div> -->
                    </div>
                </el-scrollbar>
                <div class="button-wrap bg">
                    <div @click="handleCancel"> 取消 </div>
                    <div @click="handleSure" class="bg"> 确定 </div>
                </div>
            </div>
        </template>

        <!-- 操作按钮 -->
        <ActionButtonTmpl v-if="editHelper.flag" :editHelper="editHelper" />
    </div>
</template>

<script>
import { debounce } from "@/utils/funs.js";
import { get, post, put, deletes } from "@/utils/fetch";
import ActionButtonTmpl from '../common/ActionButton.tmpl'
import { CircleClose } from '@element-plus/icons-vue'
const defaultOption = {
    id: "",
    name: "",
    utype: "waterMesh",
    coordinates: [], //坐标点数组
    indices: [2, 0, 3, 0, 2, 1],// 顶点坐标的索引构成的数组
    //   height: 10,  //水面高度 (m)高度接口无效
    waterColor: [0.2, 0.5, 0.7, 1], //水纹颜色
    waterUVRepeat: 500, //贴图间隔 (m)
    waterSpeed: 0.5, //水流速度 (m/s)
    waveScale: 0.8, //波纹强度 (m)
    waterDirection: 30, //水流方向 ( °)
    cusAttr: [], // 自定义属性: [{key: "", value:"", index: 0}]
    isChecked: true,
};
export default {
    components: {
        ActionButtonTmpl,
        CircleClose
    },
    data() {
        return {
            rippleList: [], // 水纹列表
            editHelper: {
                flag: false,
                type: "shape"
            },
            option: {},
            editIndex: -1,
            icon: "cloud-watermesh",
            isShowAddPoint: true,
            isShowPonitModel: false,
            editFlag: false,
            disabled: false
        };
    },
    created() {
        this.initData('init');
    },
    methods: {
        // 获取数据库水纹
        async getWaterMesh() {
            let data = sessionStorage.getItem("QXZS_waterMesh")
            return data ? JSON.parse(data) : []
        },
        async initData(opr) {
            this.rippleList = await this.getWaterMesh();
            this.option = JSON.parse(JSON.stringify(defaultOption));

            // 初始效果
            if (opr === 'init') {
                await window.origAPI.waterMesh.clear();
                this.rippleList.forEach((item, i) => {
                    this.checkedList(item, i)
                });
            }
        },
        // 画水纹
        drawParam() {
            this.isShowAddPoint = false;
            this.editHelper.flag = true;
        },
        async handleCommand(command) {
            let node = this.rippleList[command.index];
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
                    await window.origAPI.waterMesh.delete(node.id);
                    this.deleteWaterMesh(node, command.index);
                    break;
                default:
                    window.origAPI.waterMesh.focus(node.id);
            }
        },
        // 删除数据库水纹
        deleteWaterMesh(data, index) {
            // let index = this.rippleList.findIndex(i => i.name === obj.name);
            this.rippleList.splice(index, 1);
            sessionStorage.setItem("waterMesh", JSON.stringify(this.rippleList));
        },
        // 增加自定义属性
        addUserAttr() {
            this.option.cusAttr.push({
                key: "",
                value: "",
                index: this.option.cusAttr.length,
            });
        },
        // 屏蔽重复属性名
        attrChange(attr, index) {
            let target = this.option.cusAttr.find(
                (item, i) => attr.key != "" && index != i && item.key == attr.key
            );
            if (target) {
                this.$message.warning("不能存在重复的属性！");
                this.option.cusAttr.splice(index, 1, {
                    key: "",
                    value: "",
                    index,
                });
            }
        },
        // 删除自定义属性
        delUserAttr(item, index) {
            this.option.cusAttr.splice(index, 1);
        },
        // 获取编辑助手返回数据
        getEditDate(coordinates) {
            this.editHelper.flag = false;
            this.isShowPonitModel = true;
            this.isShowAddPoint = false;
            coordinates.forEach(element => {
                element[2] += 0.1
            });
            this.option.coordinates = coordinates;

            if (!this.option.id) this.option.id = "body_" + new Date().getTime();
            this.$nextTick(() => {
                this.addModule();
            });
        },
        // 关闭编辑助手
        closeEditHelper() {
            this.editHelper.flag = false;

            this.isShowPonitModel = false;
            this.isShowAddPoint = true;
        },
        // 复选框事件
        async checkedList(item, index) {
            if (item.isChecked) {
                let res = await window.origAPI.waterMesh.get(item.id);
                if (res.data && res.data.length > 0) {
                    await window.origAPI.waterMesh.show(item.id);
                    window.origAPI.waterMesh.focus(item.id);
                } else {
                    await window.origAPI.waterMesh.add(item);
                    window.origAPI.waterMesh.focus(item.id);
                }
            } else window.origAPI.waterMesh.hide(item.id);
        },
        handleChange: debounce(function (e) {
            this.updateModule();
        }),
        async updateModule() {
            let option = {
                ...this.option,
            }

            await window.origAPI.waterMesh.update(option);
            window.origAPI.waterMesh.focus(option.id);
            this.isShowPonitModel = true;
        },
        async addModule() {
            let option = {
                ...this.option
            };

            await window.origAPI.waterMesh.delete([option.id]);
            await window.origAPI.waterMesh.add(option);
            window.origAPI.waterMesh.focus(option.id);
            this.isShowPonitModel = true;
        },
        handleSure() {
            if (!this.option.name) return this.$message.warning("名称不能为空！");
            let targetIndex = this.rippleList.findIndex(
                (item) => item.name == this.option.name
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");

            let option = JSON.parse(JSON.stringify(this.option));
            option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性

            if (this.editIndex < 0) this.rippleList.push(option);
            else {
                // this.$set(this.rippleList, this.editIndex, option);
                this.rippleList[this.editIndex] = option
                this.editIndex = -1;
            }

            this.option = JSON.parse(JSON.stringify(defaultOption));

            this.isShowPonitModel = false;
            this.isShowAddPoint = true;


            if (this.editFlag) this.editWaterMesh(option);
            else this.saveWaterMesh(option);
        },
        // 保存水纹到后端数据库
        async saveWaterMesh(obj) {
            rippleList
            let data = sessionStorage.getItem("QXZS_waterMesh")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_waterMesh", JSON.stringify(data))

            this.initData('init');
        },
        // 编辑水纹到后端数据库
        async editWaterMesh(obj) {
            let index = this.rippleList.findIndex(i => i.name === obj.name);
            this.rippleList.splice(index, 1, obj);
            sessionStorage.setItem("QXZS_waterMesh", JSON.stringify(this.rippleList))

            this.editFlag = false;
        },
        async handleCancel() {
            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editIndex < 0) await window.origAPI.waterMesh.delete(this.option.id);
            else this.editIndex = -1;
            this.option = JSON.parse(JSON.stringify(defaultOption));
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty('funcCMPT')) this.$parent.closeCMPT();
            this.$emit('close')
        }
    },
    beforeUnmount() {
        if (this.option.id) window.origAPI.waterMesh.delete(this.option.id);
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";
@import "../css/draw.scss";
</style>