<!--
 * @FileDescription: 资源库->ODline
 * @Author: yuanhaijun
 * @Date: 2023.04.04
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.04
 -->
<template>
    <div>
        <template v-if="!editHelper.flag">
            <div v-if="isShowPonitModel == false" class="cloud-func draw-list">
                <div class="func-title">
                    ODLine
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
                            <div class="list" v-for="(item, index) in odLineineList" :key="index">
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
                        <div @click="drawParam('lines')"> <i class="el-icon-plus"></i> 新增ODLine </div>
                    </div>
                </div>
            </div>

            <div v-if="isShowPonitModel == true" class="cloud-func draw-form">
                <div class="func-title">新增ODLine</div>
                <el-scrollbar style="height: 100%">
                    <div class="func-warp">
                        <div class="warp-list">
                            <p>名称</p>
                            <el-input v-model="option.name" placeholder="请输入名称" maxlength="200"></el-input>
                        </div>
                        <div class="warp-list">
                            <p>起点名称</p>
                            <el-input v-model="option.startName" placeholder="请输入起点名称" maxlength="200"
                                @change="handleChange"></el-input>
                        </div>
                        <div class="warp-list">
                            <p>终点名称</p>
                            <el-input v-model="option.endName" placeholder="请输入终点名称" maxlength="200"
                                @change="handleChange"></el-input>
                        </div>
                        <div class="warp-list color-picker-box">
                            <p>颜色</p>
                            <div>
                                <el-color-picker size="small" v-model="option.color" @change="handleChange" :min="1"
                                    :max="10" :step="1"></el-color-picker>
                                <el-input v-model="option.color" placeholder="输入，例如:#FF0000"
                                    @change="handleChange"></el-input>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>透明度</p>
                            <div class="input-box">
                                <el-slider :min="0" :max="1" :step="0.1" v-model="option.opacity" @change="handleChange"
                                    :show-input="false" :show-input-controls="false"></el-slider>
                                <el-input-number v-model="option.opacity" controls-position="right" :min="0" :max="1"
                                    :step="0.1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>流速</p>
                            <div class="input-box">
                                <el-slider v-model="option.flowRate" @change="handleChange" :min="0" :max="1"
                                    :step="0.1"></el-slider>
                                <el-input-number v-model="option.flowRate" controls-position="right" :min="0" :max="1"
                                    :step="0.1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>亮度</p>
                            <div class="input-box">
                                <el-slider v-model="option.intensity" @change="handleChange" :min="0" :max="1"
                                    :step="0.1"></el-slider>
                                <el-input-number v-model="option.intensity" controls-position="right" :min="0" :max="1"
                                    :step="0.1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>弯曲度</p>
                            <div class="input-box">
                                <el-slider v-model="option.bendDegree" @change="handleChange" :min="0" :max="1"
                                    :step="0.1"></el-slider>
                                <el-input-number v-model="option.bendDegree" controls-position="right" :min="0" :max="1"
                                    :step="0.1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>重复度</p>
                            <div class="input-box">
                                <el-slider v-model="option.tiling" @change="handleChange" :min="0" :max="100"
                                    :step="1"></el-slider>
                                <el-input-number v-model="option.tiling" controls-position="right" :min="0" :max="100"
                                    :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>线宽（米）</p>
                            <div class="input-box">
                                <el-slider v-model="option.lineThickness" @change="handleChange" :min="0" :max="100"
                                    :step="1"></el-slider>
                                <el-input-number v-model="option.lineThickness" controls-position="right" :min="0"
                                    :max="100" :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>运动点缩放</p>
                            <div class="input-box">
                                <el-slider v-model="option.flowPointSizeScale" @change="handleChange" :min="0" :max="100"
                                    :step="1"></el-slider>
                                <el-input-number v-model="option.flowPointSizeScale" controls-position="right" :min="0"
                                    :max="100" :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>两端点缩放</p>
                            <div class="input-box">
                                <el-slider v-model="option.labelSizeScale" @change="handleChange" :min="0" :max="1000"
                                    :step="1"></el-slider>
                                <el-input-number v-model="option.labelSizeScale" controls-position="right" :min="0"
                                    :max="1000" :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list el-radio-box">
                            <p>材质样式</p>
                            <el-radio-group v-model="option.lineStyle" @change="handleChange">
                                <el-radio :label="0">纯色</el-radio>
                                <el-radio :label="1">箭头</el-radio>
                                <el-radio :label="2">流动点</el-radio>
                            </el-radio-group>
                        </div>
                        <div class="warp-list el-radio-box">
                            <p>模型样式</p>
                            <el-radio-group v-model="option.lineShape" @change="handleChange">
                                <el-radio :label="0">平面</el-radio>
                                <el-radio :label="1">柱体</el-radio>
                            </el-radio-group>
                        </div>
                        <div class="warp-list el-radio-box">
                            <p>发光点样式</p>
                            <el-radio-group v-model="option.flowShape" @change="handleChange">
                                <el-radio :label="0">无</el-radio>
                                <el-radio :label="1">球体</el-radio>
                            </el-radio-group>
                        </div>
                        <div class="warp-list el-radio-box">
                            <p>StartPoint样式</p>
                            <el-radio-group v-model="option.startPointShape" @change="handleChange">
                                <el-radio :label="0">无</el-radio>
                                <el-radio :label="1">球体</el-radio>
                            </el-radio-group>
                        </div>
                        <div class="warp-list el-radio-box">
                            <p>EndPoint样式</p>
                            <el-radio-group v-model="option.endPointShape" @change="handleChange">
                                <el-radio :label="0">无</el-radio>
                                <el-radio :label="1">球体</el-radio>
                            </el-radio-group>
                        </div>
                        <div class="warp-list el-radio-box">
                            <p>StartLabel样式</p>
                            <el-radio-group v-model="option.startLabelShape" @change="handleChange">
                                <el-radio :label="0">无</el-radio>
                                <el-radio :label="1">圆</el-radio>
                            </el-radio-group>
                        </div>
                        <div class="warp-list el-radio-box">
                            <p>EndLabel样式</p>
                            <el-radio-group v-model="option.endLabelShape" @change="handleChange">
                                <el-radio :label="0">无</el-radio>
                                <el-radio :label="1">圆</el-radio>
                            </el-radio-group>
                        </div>
                        <div class="warp-list user-attr-box">
                            <p>自定义属性</p>
                            <ul>
                                <li v-for="(item, index) in option.cusAttr" :key="index">
                                    <el-input v-model="item.key" placeholder="key" @change="attrChange(item, index)" />
                                    <el-input v-model="item.value" placeholder="value" />
                                    <i class="icon-btn-del el-icon-remove-outline" @click="delUserAttr(item, index)"></i>
                                </li>
                            </ul>
                            <i class="icon-btn-add el-icon-circle-plus-outline" @click="addUserAttr"></i>
                        </div>
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
import { CircleClose } from '@element-plus/icons-vue'
import ActionButtonTmpl from '../common/ActionButton.tmpl'
const defaultOption = {
    id: "",
    name: "",
    utype: "odlineP",
    coordinates: [], // 坐标 [起点, 终点]
    startName: "", // sid = id + "_sp", coordinates[0]
    endName: "", // eid = id + "_ep", coordinates[1]
    color: "#FF0000",
    opacity: 0.8,
    // opacity: 1,
    flowRate: 0.5, // 流速 [0 ~ 1.0]
    intensity: 0.5, // 亮度 [0.1~1000]
    bendDegree: 0.5, // 弯曲度 [0~1.0]
    tiling: 5, // 重复度 [0 ~ 100]
    lineThickness: 5, // 线宽 [0 ~ 100]
    flowPointSizeScale: 20, // 运动点缩放 [0 ~ 100]
    labelSizeScale: 100, // 两端点缩放 [0 ~ 1000]
    lineStyle: 0, // 材质样式:  0:纯色 1:箭头，2:流动点
    lineShape: 1, // 模型样式:  0:平面 1:柱体
    flowShape: 1, // 发光点样式: 0:无 1:球体
    startPointShape: 0, // StartPoint样式: 1:球体 0:无
    endPointShape: 0, // EndPoint样式: 1:球体 0:无
    startLabelShape: 0, // StartLabel样式: 1:圆 0:无
    endLabelShape: 0, // EndLabel样式: 1:圆 0:无
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
            odLineineList: [],
            editHelper: {
                flag: false,
                type: "lines"
            },
            option: {},
            editIndex: -1,
            icon: "cloud-odline",
            isShowPonitModel: false,
            editFlag: false,
            disabled: false
        };
    },
    created() {
        this.initData('init');
    },
    methods: {
        // 获取数据库ODLine
        async getODLine() {
            let data = sessionStorage.getItem("QXZS_ODLine")
            return data ? JSON.parse(data) : []
        },
        async initData(opr) {
            this.odLineineList = await this.getODLine();
            this.option = JSON.parse(JSON.stringify(defaultOption));

            // 初始效果
            if (opr === 'init') {
                await window.origAPI.odline.clear();
                this.odLineineList.forEach((item, i) => {
                    this.checkedList(item, i)
                });
            }
        },
        drawParam(lineType) {
            this.editHelper.type = lineType;
            this.isShowPonitModel = null;
            this.editHelper.flag = true;
        },
        async handleCommand(command) {
            let node = this.odLineineList[command.index];
            switch (command.type) {
                case "编辑":
                    this.isShowPonitModel = true;
                    this.editIndex = command.index;
                    this.editFlag = true;
                    // let option = JSON.parse(JSON.stringify(command.item))
                    // this.option = option;
                    this.option = JSON.parse(JSON.stringify(command.item));
                    this.$nextTick(() => {
                        this.addModule(true);
                    });
                    break;
                case "删除":
                    await window.origAPI.odline.delete(node.id);
                    this.deleteODLine(node, command.index);
                    break;
                default:
                    window.origAPI.odline.focus(node.id);
            }
        },
        // 删除数据库ODLine
        deleteODLine(data, index) {
            // let index = this.odLineineList.findIndex(i => i.name === obj.name);
            this.odLineineList.splice(index, 1);
            sessionStorage.setItem("QXZS_ODLine", JSON.stringify(this.odLineineList))
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
                    index
                });
            }
        },
        // 删除自定义属性
        delUserAttr(item, index) {
            this.option.cusAttr.splice(index, 1);
        },
        // _colorBuild(color, opacity) {
        //   let r = parseInt(color.slice(1, 3), 16) / 255;
        //   let g = parseInt(color.slice(3, 5), 16) / 255;
        //   let b = parseInt(color.slice(5, 7), 16) / 255;
        //   return [r, g, b, opacity];
        // },
        // 获取编辑助手返回数据
        getEditDate(coordinates) {
            this.editHelper.flag = false;
            this.isShowPonitModel = true;

            this.option.coordinates = [coordinates[0], coordinates[1]];

            if (!this.option.id) this.option.id = "odl_" + new Date().getTime();
            this.$nextTick(() => {
                this.addModule(false);
            });
        },
        // 关闭编辑助手
        closeEditHelper() {
            this.editHelper.flag = false;
            this.isShowPonitModel = false;
        },
        // 复选框事件!!slot
        async checkedList(item, index) {
            if (item.isChecked) {
                let res = await window.origAPI.odline.get(item.id);
                if (res.data && res.data.length > 0) {
                    await window.origAPI.odline.show(item.id);
                    window.origAPI.odline.focus(item.id);
                } else {
                    await window.origAPI.odline.add(item);
                    window.origAPI.odline.focus(item.id);
                }
            } else window.origAPI.odline.hide(item.id);
        },
        handleChange: debounce(function (e) {
            this.updateModule();
        }),
        async updateModule() {
            let option = {
                ...this.option,
                // color: this._colorBuild(this.option.color, this.option.opacity)
            };

            await window.origAPI.odline.update(option);
            window.origAPI.odline.focus(option.id);
        },
        async addModule(bool) {
            let option = {
                ...this.option,
                // color: this._colorBuild(this.option.color, this.option.opacity)
            };

            await window.origAPI.odline.delete([option.id]);
            await window.origAPI.odline.add(option);
            window.origAPI.odline.focus(option.id);
        },
        handleSure() {
            if (!this.option.name) return this.$message.warning("名称不能为空！");
            let targetIndex = this.odLineineList.findIndex(
                (item) => item.name == this.option.name
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");

            let option = JSON.parse(JSON.stringify(this.option));
            option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性

            if (this.editIndex < 0) this.odLineineList.push(option);
            else {
                // this.$set(this.odLineineList, this.editIndex, option);
                this.odLineineList[this.editIndex] = option
                this.editIndex = -1;
            }

            this.option = JSON.parse(JSON.stringify(defaultOption));
            this.isShowPonitModel = false;

            if (this.editFlag) this.editODLine(option);
            else this.saveODLine(option);
        },
        // 保存ODLine到后端数据库
        async saveODLine(obj) {
            let data = sessionStorage.getItem("QXZS_ODLine")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_ODLine", JSON.stringify(data))

            this.initData('init');
        },
        // 编辑ODLine到后端数据库
        async editODLine(obj) {
            let index = this.odLineineList.findIndex(i => i.name === obj.name);
            this.odLineineList.splice(index, 1, obj);
            sessionStorage.setItem("QXZS_ODLine", JSON.stringify(this.odLineineList))

            this.editFlag = false;
        },
        async handleCancel() {
            this.isShowPonitModel = false;

            if (this.editIndex < 0) await window.origAPI.odline.delete(this.option.id);
            else this.editIndex = -1;

            this.option = JSON.parse(JSON.stringify(defaultOption));
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    beforeUnmount() {
        if (this.option.id) window.origAPI.odline.delete(this.option.id);
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";
@import "../css/draw.scss";
</style>