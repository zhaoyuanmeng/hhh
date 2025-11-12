<!--
 * @FileDescription: 资源库->高亮区
 * @Author: yuanhaijun
 * @Date: 2023.04.04
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.04
 -->
<template>
    <div>
        <template v-if="!editHelper.flag">
            <div v-if="isShowAddPoint" class="cloud-func draw-list">
                <div class="func-title">
                    高亮区
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
                            <div class="list" v-for="(item, index) in highLightAreaList" :key="index">
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
                        <div @click="drawParam('shape')"> <i class="el-icon-plus"></i> 新增高亮区 </div>
                    </div>
                </div>
            </div>

            <div v-if="isShowPonitModel" class="cloud-func draw-form">
                <div class="func-title">
                    新增高亮区
                </div>
                <el-scrollbar style="height: 100%">
                    <div class="func-warp">
                        <div class="warp-list">
                            <p>名称</p>
                            <el-input v-model="option.name" placeholder="请输入名称" maxlength="200"></el-input>
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
                    <!-- <div class="warp-list">
							<p>透明度</p>
                    							<div class="input-box">
                    								<el-slider :min="0" :max="1" :step="0.1" v-model="option.opacity" @change="handleChange"
                    									:show-input="false" :show-input-controls="false"></el-slider>
                    								<el-input-number v-model="option.opacity" controls-position="right" :min="0" :max="1"
                    									:step="0.1" @change="handleChange"></el-input-number>
                    							</div>
                    						</div> -->
                        <div class="warp-list">
                            <p>高度范围</p>
                            <div class="input-box">
                                <el-slider v-model="option.heightRange" @change="handleChange" :min="1" :max="1000"
                                    :step="1"></el-slider>
                                <el-input-number v-model="option.heightRange" controls-position="right" :min="1" :max="1000"
                                    :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>颜色强度</p>
                            <div class="input-box">
                                <el-slider v-model="option.intensity" @change="handleChange" :min="1" :max="1000"
                                    :step="1"></el-slider>
                                <el-input-number v-model="option.intensity" controls-position="right" :min="1" :max="1000"
                                    :step="1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>深度检测</p>
                            <div class="el-radio-box">
                                <el-radio-group v-model="option.depthTest" @change="handleChange">
                                    <el-radio :value="true" :label="true">是</el-radio>
                                    <el-radio :value="false" :label="false">否</el-radio>
                                </el-radio-group>
                            </div>
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
import { get, post, put, deletes } from "@/utils/fetch"
import { CircleClose } from '@element-plus/icons-vue'
import ActionButtonTmpl from '../common/ActionButton.tmpl'
const defaultOption = {
    id: "",
    name: "", // 高亮区名称
    color: "#FF0000",  // 颜色 value: 1, min: .1, max: 100, opacity:1
    heightRange: 30,// 高亮染色区域高度范围 [min,max]，数组元素取值范围：[任意浮点数]，取值说明：Z坐标的区间，只有Z值这这个区间的模型才会被染色
    intensity: 10, //  (number) 高亮颜色的强度，取值范围：[0~1000]
    depthTest: false, // 是否做深度检测，默认为true（DepthTest=true会被遮挡，false的话不会被遮挡）
    coordinates: [],
    utype: "highlightArea",
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
            highLightAreaList: [], // 高亮区列表
            editHelper: {
                flag: false,
                type: "lines",
            },
            option: {},
            editIndex: -1,
            icon: "cloud-gaoliang",
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
        // 获取数据库高亮区
        async getHighLightArea() {
            let data = sessionStorage.getItem("QXZS_highLightArea")
            return data ? JSON.parse(data) : []
        },
        async initData(opr) {
            this.highLightAreaList = await this.getHighLightArea();
            this.option = JSON.parse(JSON.stringify(defaultOption));

            // 初始效果
            if (opr === 'init') {
                await window.origAPI.highlightArea.clear();
                this.highLightAreaList.forEach((item, i) => {
                    let node = { ...item, heightRange: [0, item.heightRange] }
                    this.checkedList(node, i)
                });
            }
        },
        // 画高亮区
        drawParam(lineType) {
            this.editHelper.type = lineType;
            this.isShowAddPoint = false;
            this.editHelper.flag = true;
        },
        async handleCommand(command) {
            let node = this.highLightAreaList[command.index];
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
                    await window.origAPI.highlightArea.delete(node.id);
                    this.deleteHighLightArea(node, command.index);
                    break;
                default:
                    window.origAPI.highlightArea.focus(node.id);
            }
        },
        // 删除数据库高亮区
        deleteHighLightArea(data, index) {
            // let index = this.highLightAreaList.findIndex(i => i.name === obj.name);
            this.highLightAreaList.splice(index, 1);
            sessionStorage.setItem("QXZS_highLightArea", JSON.stringify(this.highLightAreaList))
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
        _colorBuild(option) {
            let r = parseInt(option.color.slice(1, 3), 16) / 255;
            let g = parseInt(option.color.slice(3, 5), 16) / 255;
            let b = parseInt(option.color.slice(5, 7), 16) / 255;
            return [r, g, b, option.opacity];
        },
        // 获取编辑助手返回数据
        getEditDate(coordinates) {
            this.editHelper.flag = false;
            this.isShowPonitModel = true;
            this.isShowAddPoint = false;

            this.option.coordinates = coordinates;

            if (!this.option.id) this.option.id = "hla_" + new Date().getTime();
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
                let res = await window.origAPI.highlightArea.get(item.id);
                if (res.data && res.data.length > 0) {
                    await window.origAPI.highlightArea.show(item.id);
                    window.origAPI.highlightArea.focus(item.id);
                } else {
                    await window.origAPI.highlightArea.add(item);
                    window.origAPI.highlightArea.focus(item.id);
                }
            } else window.origAPI.highlightArea.hide(item.id);
        },
        handleChange: debounce(function (e) {
            this.updateModule();
        }),
        async updateModule() {
            let option = {
                ...this.option,
                heightRange: [0, this.option.heightRange]
                // color: this._colorBuild(this.option),
            };

            await window.origAPI.highlightArea.update(option);
            window.origAPI.highlightArea.focus(option.id);
            this.isShowPonitModel = true;
        },
        async addModule() {
            let option = {
                ...this.option,
                heightRange: [0, this.option.heightRange]
                // color: this._colorBuild(this.option),
            };

            await window.origAPI.highlightArea.delete([option.id]);
            await window.origAPI.highlightArea.add(option);
            window.origAPI.highlightArea.focus(option.id);
            this.isShowPonitModel = true;
        },
        handleSure() {
            if (!this.option.name) return this.$message.warning("名称不能为空！");
            let targetIndex = this.highLightAreaList.findIndex(
                (item) => item.name == this.option.name
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");

            let option = JSON.parse(JSON.stringify(this.option));
            option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性

            if (this.editIndex < 0) this.highLightAreaList.push(option);
            else {
                // this.$set(this.highLightAreaList, this.editIndex, option);
                this.highLightAreaList[this.editIndex] = option
                this.editIndex = -1;
            }

            this.option = JSON.parse(JSON.stringify(defaultOption));

            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editFlag) this.editHighLightArea(option);
            else this.saveHighLightArea(option);
        },
        // 保存高亮区到后端数据库
        async saveHighLightArea(obj) {
            let data = sessionStorage.getItem("QXZS_highLightArea")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_highLightArea", JSON.stringify(data))

            this.initData('init');
        },
        // 编辑高亮区到后端数据库
        async editHighLightArea(obj) {
            let index = this.highLightAreaList.findIndex(i => i.name === obj.name);
            this.highLightAreaList.splice(index, 1, obj);
            sessionStorage.setItem("QXZS_highLightArea", JSON.stringify(this.highLightAreaList))

            this.editFlag = false;
        },
        async handleCancel() {
            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editIndex < 0) await window.origAPI.highlightArea.delete(this.option.id);
            else this.editIndex = -1;
            this.option = JSON.parse(JSON.stringify(defaultOption));
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    beforeUnmount() {
        if (this.option.id) window.origAPI.highlightArea.delete(this.option.id);
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";
@import "../css/draw.scss";
</style>