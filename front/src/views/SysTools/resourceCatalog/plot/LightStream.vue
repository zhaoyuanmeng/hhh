<!--
 * @FileDescription: 资源库->光流
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
                    人员撤离路线
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
                            <div class="list" v-for="(item, index) in streamList" :key="index">
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
                        <div @click="drawParam('lines')"> <i class="el-icon-plus"></i> 新增撤离路线 </div>
                    </div>
                </div>
            </div>

            <div v-if="isShowPonitModel" class="cloud-func draw-form">
                <div class="func-title"> 新增撤离路线 </div>
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
                								<el-input-number v-model="option.opacity" controls-position="right" :min="0" :max="1" :step="0.1"
                									@change="handleChange"></el-input-number>
                							</div>
                						</div> -->
                        <div class="warp-list">
                            <p>持续时间</p>
                            <div class="input-box">
                                <el-slider v-model="option.duration" @change="handleChange" :min="1" :max="10"
                                    :step="0.1"></el-slider>
                                <el-input-number v-model="option.duration" controls-position="right" :min="1" :max="10"
                                    :step="0.1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>间隔</p>
                            <div class="input-box">
                                <el-slider v-model="option.interval" @change="handleChange" :min="1" :max="10"
                                    :step="0.1"></el-slider>
                                <el-input-number v-model="option.interval" controls-position="right" :min="1" :max="10"
                                    :step="0.1" @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>宽度</p>
                            <div class="input-box">
                                <el-slider v-model="option.thickness" @change="handleChange" :min="0.1" :max="100"
                                    :step="0.1"></el-slider>
                                <el-input-number v-model="option.thickness" controls-position="right" :max="100" :step="0.1"
                                    @change="handleChange"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list">
                            <p>速率</p>
                            <div class="input-box">
                                <el-slider v-model="option.velocity" @change="handleChange" :min="0" :max="1"
                                    :step="0.1"></el-slider>
                                <el-input-number v-model="option.velocity" @change="handleChange" :min="0" :max="1"
                                    :step="0.1" controls-position="right"></el-input-number>
                            </div>
                        </div>
                        <div class="warp-list user-attr-box">
                            <p>自定义属性</p>
                            <ul>
                                <li v-for="(item, index) in option.cusAttr" :key="index">
                                    <el-input v-model="item.key" placeholder="key" @change="attrChange(item, index)" />
                                    <el-input v-model="item.value" placeholder="value" />
                                    <!-- <i class="icon-btn-del el-icon-remove-outline" @click="delUserAttr(item, index)"></i> -->
                                    <el-icon>
                                        <CircleClose @click="delUserAttr(item, index)" />
                                    </el-icon>
                                </li>
                            </ul>
                            <!-- <i class="icon-btn-add el-icon-circle-plus-outline" @click="addUserAttr"></i> -->
                            <el-icon>
                                <CirclePlus @click="addUserAttr" />
                            </el-icon>
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
import { get, post, put, deletes, } from "@/utils/fetch";
import { CircleClose, CirclePlus } from '@element-plus/icons-vue'
import ActionButtonTmpl from '../common/ActionButton.tmpl'
const defaultOption = {
    id: "",
    name: "", // 光流名称
    duration: 5, // 持续时间 value: 5, min: 1, max: 10, step:0.1  slider
    thickness: 1, // 宽度 value: 1, min: .1, max: 100, step:1
    velocity: 2, // 速率 value: 1, min: 0, max: 10, step:.1
    interval: 1, // 间隔 value: 1, min: 0, max: 10, step:.1
    color: "#FF0000",  // 颜色 value: 1, min: .1, max: 100, opacity:1
    coordinates: [],
    utype: "beam",
    cusAttr: [], // 自定义属性: [{key: "", value:"", index: 0}]
    isChecked: true,
};
export default {
    components: {
        ActionButtonTmpl,
        CircleClose,
        CirclePlus,
    },
    data() {
        return {
            streamList: [], // 光流数据
            editHelper: {
                flag: false,
                type: "lines",
            },
            option: {},
            editIndex: -1,
            icon: "cloud-model-line",
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
        // 获取数据库光流
        async getBeam() {
            let data = sessionStorage.getItem("QXZS_beam")
            return data ? JSON.parse(data) : []
        },
        async initData(opr) {
            this.streamList = await this.getBeam();
            this.option = JSON.parse(JSON.stringify(defaultOption));

            // 初始效果
            if (opr === 'init') {
                await window.origAPI.beam.clear();
                this.streamList.forEach((item, i) => {
                    this.checkedList(item, i)
                });
            }
        },
        // 画光流
        drawParam(lineType) {
            this.editHelper.type = lineType;
            this.isShowAddPoint = false;
            this.editHelper.flag = true;
        },
        async handleCommand(command) {
            let node = this.streamList[command.index];
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
                    await window.origAPI.beam.delete(node.id);
                    this.deleteBeam(node, command.index);
                    break;
                default:
                    window.origAPI.beam.focus(node.id);
            }
        },
        // 删除数据库水纹
        deleteBeam(data, index) {
            // let index = this.streamList.findIndex(i => i.name === obj.name);
            this.streamList.splice(index, 1);
            sessionStorage.setItem("QXZS_beam", JSON.stringify(this.streamList))
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

            if (!this.option.id) this.option.id = "beam_" + new Date().getTime();
            this.$nextTick(() => {
                this.addModule();
                // this.customObjectMove(coordinates)
                // this.personnelEvacuation(coordinates)
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
                let res = await window.origAPI.beam.get(item.id);
                if (res.data && res.data.length > 0) {
                    await window.origAPI.beam.show(item.id);
                    window.origAPI.beam.focus(item.id);
                } else {
                    await window.origAPI.beam.add(item);
                    window.origAPI.beam.focus(item.id);
                }
            } else window.origAPI.beam.hide(item.id);
        },
        handleChange: debounce(function (e) {
            this.updateModule();
        }),
        async updateModule() {
            let option = {
                ...this.option,
                // color: this._colorBuild(this.option),
            };

            await window.origAPI.beam.update(option);
            window.origAPI.beam.focus(option.id);
            this.isShowPonitModel = true;
        },
        async addModule() {
            let option = {
                ...this.option,
                // color: this._colorBuild(this.option),
            };

            await window.origAPI.beam.delete([option.id]);
            await window.origAPI.beam.add(option);
            window.origAPI.beam.focus(option.id);
            this.isShowPonitModel = true;
        },
        handleSure() {
            if (!this.option.name) return this.$message.warning("名称不能为空！");
            let targetIndex = this.streamList.findIndex(
                (item) => item.name == this.option.name
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");

            let option = JSON.parse(JSON.stringify(this.option));
            option.cusAttr = option.cusAttr.filter((item) => item.key !== ""); // 过滤无效属性

            if (this.editIndex < 0) this.streamList.push(option);
            else {
                // this.$set(this.streamList, this.editIndex, option);
                this.streamList[this.editIndex] = option
                this.editIndex = -1;
            }

            this.option = JSON.parse(JSON.stringify(defaultOption));

            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editFlag) this.editBeam(option);
            else this.saveBeam(option);
        },
        // 保存光流到后端数据库
        async saveBeam(obj) {
            let data = sessionStorage.getItem("QXZS_beam")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_beam", JSON.stringify(data))

            this.initData('init');
        },
        // 编辑光流到后端数据库
        async editBeam(obj) {
            let index = this.streamList.findIndex(i => i.name === obj.name);
            this.streamList.splice(index, 1, obj);
            sessionStorage.setItem("QXZS_beam", JSON.stringify(this.streamList))

            this.editFlag = false;
        },
        async handleCancel() {
            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editIndex < 0) await window.origAPI.beam.delete(this.option.id);
            else this.editIndex = -1;
            this.option = JSON.parse(JSON.stringify(defaultOption));
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
        async customObjectMove(positionArr) {
            //添加前清空所有customObject 防止id重复
            __g.customObject.clear();
            //投影坐标
            let co_location = positionArr[0];
            let o = {
                id: 'o1',//自定义对象唯一id
                pakFilePath: '@path:DTS_Library.pak',//资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
                assetPath: '/Game/Common/Asset_Bank/Mesh/Car/BP_Car_JiuHuChe',//资源目录，自定义对象在pak文件资源包里的相对路径
                location: co_location,//位置坐标
                coordinateType: 0,// 坐标系类型 
                rotation: [0, 0, 0],// 世界坐标系旋转
                localRotation: [0, 0, 0],//模型自身旋转
                scale: [5, 5, 5],//模型缩放
                smoothMotion: 1   //1: 平滑移动，0: 跳跃移动
            };
            await __g.customObject.add(o);
            //构造移动路径点数组
            let pathPointArr = [];
            for (let i = 0; i < positionArr.length; i++) {
                //构造数组元素 每1秒移动一次
                let elementPoint = { 'time': (i) * 2, 'coordinate': positionArr[i] };
                pathPointArr.push(elementPoint);
            }
            //设置相机自动跟随
            //__g.customObject.focus('o1', -1);
            //设置自定义相机跟随
            // __g.customObject.focus('o1', 5, 0, [-30, 4, 0], ActionMode.Follow);
            //车辆按GPS轨迹移动
            __g.customObject.startMove('o1', 0, pathPointArr);
        },
        async personnelEvacuation(moveCoordinateArr) {
            //添加前清空所有customObject 防止id重复
            __g.customObject.clear();
            //步行人物模型路径
            var walk_man_file_path = "/JC_CustomAssets/BP_Walk";

            //人物运动路径
            var walk_man_path_arr = moveCoordinateArr;

            //控制人物移动数组
            let walkManPointArr = [];
            let walkTime = 0;
            for (let i = 0; i < walk_man_path_arr.length; i++) {
                //构造路径参数数组 每1000毫秒移动一次
                walkTime = walkTime + 12;
                let elementPoint = { 'time': (i) * 10, 'coordinate': walk_man_path_arr[i] };
                walkManPointArr.push(elementPoint);
            }
            //人物运动的开始坐标
            var man_location = walk_man_path_arr[0];

            //添加人数 人数最大为5 5以下为路径坐标点数减1
            // walk_man_path_arr.forEach(async (element, index) => {
            // if (index <= 5 && index < walk_man_path_arr.length - 1) {
            let walkmanCustomObj = {
                id: 'walkman_walk',//自定义对象唯一id
                pakFilePath: '@path:walkman.pak',//pak文件路径
                assetPath: walk_man_file_path,//运动人物模型路径
                location: man_location,//位置坐标
                coordinateType: 0,// 坐标系类型 
                rotation: [0, 0, 0],//旋转
                scale: [1, 1, 1],//缩放
                smoothMotion: 1,   //1: 平滑插值，0: 跳跃
            };
            await __g.customObject.add(walkmanCustomObj);

            __g.customObject.startMove('walkman_walk', 0, walkManPointArr);
            // }
            // })






            //设置视角跟随相机
            //__g.customObject.focus('walkman_walk', -1);
            //人物按轨迹移动

            //设置跟随相机
            //__g.customObject.focus('walkman_walk',-1);

            //运动结束后切换为静止人物
            // let timeOut = walkTime * 1000 + 500;
            // let location = [493139.25, 2492023, 2.3132898807525635];
            // window.setTimeout("switchStaticMan(" + 1 + ")", timeOut);
        },
        switchStaticMan(mode) {
            let static_man_location = [];
            if (mode == "1") {
                static_man_location = [493139.25, 2492023, 2.3132898807525635];
            }
            if (mode == "2") {
                static_man_location = [493228.21875, 2492001.5, 1.7359277009963989];
            }
            //运动结束后切换为静止人物
            __g.customObject.clear();
            //静止人物模型材质路径 UE工程内Actor文件路径
            var static_man_file_path = "/JC_CustomAssets/BP_Idle";
            //人物运动的终点坐标 static_man_location
            let walkmanCustomObjStatic = {
                id: 'walkman_static',//自定义对象唯一id
                pakFilePath: HostConfig.Path + '/media/pak/walkman.pak',//pak文件路径
                assetPath: static_man_file_path,//静止人物模型路径
                location: static_man_location,//位置坐标
                coordinateType: 0,// 坐标系类型 
                rotation: [0, 0, 0],//旋转
                scale: [1, 1, 1],//缩放
                smoothMotion: 1,   //1: 平滑插值，0: 跳跃
            };
            __g.customObject.add(walkmanCustomObjStatic);
        }
    },
    beforeUnmount() {
        if (this.option.id) window.origAPI.beam.delete(this.option.id);
        __g.customObject.clear()
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";
@import "../css/draw.scss";
</style>