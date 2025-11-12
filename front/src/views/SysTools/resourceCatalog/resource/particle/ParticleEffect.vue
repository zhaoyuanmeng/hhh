<!--
 * @FileDescription: 资源库->粒子效果
 * @Author: yuanhaijun
 * @Date: 2023.04.11
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.11
 -->
<template>
    <div>
        <div v-if="isShowAddPoint" class="cloud-func draw-list">
            <div class="func-title">
                粒子效果
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
                        <div class="list" v-for="(item, index) in particleList" :key="index">
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
                    <div @click="drawParam()"> <i class="el-icon-plus"></i> 新增粒子 </div>
                </div>
            </div>
        </div>

        <div v-if="isShowPonitModel" class="cloud-func draw-form">
            <div class="func-title"> {{ !!editFlag ? '编辑' : '新增' }}粒子 </div>
            <el-scrollbar style="height: 100%">
                <div class="func-warp">
                    <div class="warp-list">
                        <p>粒子类型</p>
                        <el-cascader v-model="option.particle" :options="resourceParticle"
                            :props="{ expandTrigger: 'hover' }" @change="handleChangeParticle"
                            popper-class="pc_cascader"></el-cascader>
                    </div>
                    <div class="warp-list">
                        <p>名称</p>
                        <el-input v-model="option.name" placeholder="请输入名称" maxlength="200"></el-input>
                    </div>
                    <div class="warp-list">
                        <p>可见距离</p>
                        <div class="wl-row">
                            <el-input-number v-for="(item, i) in option.range" :key="i" v-model="option.range[i]"
                                @change="handleChange" controls-position="right"></el-input-number>
                        </div>
                    </div>
                    <div class="warp-list">
                        <p class="points">
                            位置
                            <span :class="['img', { ck: !!editHelper.flag }]" @click="handleCoords">
                                <img src="@/assets/images/cloud/shuxing.png" />
                            </span>
                        </p>
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
                        <p>缩放比</p>
                        <el-input-number v-model="option.pointScale" @change="handleChange" :min="0.5" :max="20" :step="0.1"
                            label="描述文字"></el-input-number>
                    </div>

                    <!-- 粒子-拓展参数（暂时不支持编辑） -->
                    <div v-if="!!particleParams" class="warp-list">
                        <p>拓展参数</p>
                        <div v-for="(item, i) in particleParams" :key="i" class="wl-attr">
                            <span> {{ item.functionName }} </span>
                            <el-input v-model="item.functionParams[0].defaultValue" disabled></el-input>
                        </div>
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
import { mapState } from "pinia";
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { debounce } from "@/utils/funs.js";
import { get, post, put, deletes } from "@/utils/fetch";
import { CircleClose } from '@element-plus/icons-vue'
import { deepTree } from "@/utils/util.js";
import particles from "./particles.js";
const defaultOption = {
    id: "", // 3D标注的唯一标识符
    name: "", // UD：名称
    pointName: "", // 3D标注展示的特效名称，取值详见Explorer资源面板->动态标记下的显示名称，取值示例：Point_B_特效编号
    particle: [], // UD：粒子类型
    utype: "marker3d",
    pointVisible: true, // 3D标注是否显示，默认值：true
    pointScale: 8, // 3D标注整体缩放比例，取值范围：[0.01,任意正数]
    coordinate: [], // 3D标注的位置坐标
    isChecked: true,

    range: [1, 10000], // 3D标注的可视距离范围：[min,max]，单位：米
};
export default {
    components: {
        CircleClose
    },
    data() {
        return {
            resourceParticle: [], // 资源-粒子
            particleList: [], // 粒子列表
            option: {},
            particleParams: undefined, // 粒子-拓展参数
            editHelper: {
                flag: false,
                // type: "point",
            },
            editIndex: -1,
            icon: "cloud-tube-burst",
            isShowAddPoint: true,
            isShowPonitModel: false,

            editFlag: false,
            disabled: false,
        };
    },
    computed: {
        ...mapState(useSysToolsCimStore, ["eventSealAPI"]),
    },
    watch: {
        eventSealAPI: {
            indeterminate: true,
            deep: true,
            handler(val) {
                switch (val.eventtype) {
                    case "Measurement":
                        if (val.Type === "Coordinate") {
                            // this.$set(this.editHelper, "flag", false);
                            // this.$set(this.option, "coordinate", val.Coordinate);
                            this.editHelper.flag = false
                            this.option.coordinate = val.Coordinate
                            if (!this.option.id) {
                                // this.$set(this.option, "id", "pe_" + new Date().getTime());
                                this.option.id = "pe_" + new Date().getTime()
                                this.addModule();
                            } else {
                                this.handleChange();
                            }
                            setTimeout(() => {
                                window.sealAPI._tools.stopMeasurement_biz();
                            }, 200);
                        }
                        return;
                }
            },
        },
    },
    created() {
        this.initData('init');
    },
    mounted() {
        this.initParticle();
    },
    methods: {
        // 初始化 资源-粒子
        initParticle() {
            let data = [...particles];
            data.forEach(i => {
                i.label = i.name;
                i.value = i.name;
                (i.children || []).forEach(j => {
                    j.label = j.name;
                    j.value = j.name;
                });
            });
            this.resourceParticle = data;
        },
        // 粒子类型
        handleChangeParticle() {
            let val = this.option.particle;
            val = val.slice(-1)[0];
            // this.$set(this.option, "pointName", val);
            this.option.pointName = val
            this.handleChange();

            this.initExpandAttr(val);
        },
        // 粒子拓展属性
        initExpandAttr(name) {
            let dList = deepTree(particles, [], "children");
            let node = dList.find(i => i.name === name);
            this.particleParams = node ? node.params : undefined;
        },
        // 获取坐标点
        handleCoords() {
            // this.$set(this.editHelper, "flag", true);
            this.editHelper.flag = true
            this.$message.warning("请在场景中点选坐标！");
            window.sealAPI._tools.setMeasurement_biz(1);
        },
        // 获取数据库粒子
        async getParticle() {
            let data = sessionStorage.getItem("QXZS_particleEffect")
            return data ? JSON.parse(data) : []
        },
        async initData(opr) {
            this.particleList = await this.getParticle();
            this.option = JSON.parse(JSON.stringify(defaultOption));

            // 初始效果
            if (opr === 'init') {
                await window.origAPI.marker3d.clear();
                this.particleList.forEach((item, i) => {
                    this.checkedList(item, i)
                });
            }
        },
        // 画粒子
        drawParam() {
            this.isShowAddPoint = false;
            this.isShowPonitModel = true;
        },
        async handleCommand(command) {
            let node = this.particleList[command.index];
            switch (command.type) {
                case "编辑":
                    this.isShowAddPoint = false;
                    this.isShowPonitModel = true;
                    this.editIndex = command.index;
                    this.editFlag = true;
                    this.option = JSON.parse(JSON.stringify(command.item));
                    this.initExpandAttr(this.option.pointName);
                    this.$nextTick(() => {
                        this.addModule();
                    });
                    break;
                case "删除":
                    await window.origAPI.marker3d.delete(node.id);
                    this.deleteBooster(node, command.index);
                    break;
                default:
                    window.origAPI.marker3d.focus(node.id);
            }
        },
        // 删除数据库粒子
        deleteBooster(obj, index) {
            // let index = this.particleList.findIndex(i => i.name === obj.name);
            this.particleList.splice(index, 1);
            sessionStorage.setItem("QXZS_particleEffect", JSON.stringify(this.particleList))
        },
        // 复选框事件
        async checkedList(item, index) {
            if (item.isChecked) {
                let res = await window.origAPI.marker3d.get(item.id);
                if (res.data && res.data.length > 0) {
                    await window.origAPI.marker3d.show(item.id);
                    window.origAPI.marker3d.focus(item.id);
                } else {
                    await window.origAPI.marker3d.add(item);
                    window.origAPI.marker3d.focus(item.id);
                }
            } else window.origAPI.marker3d.hide(item.id);
        },
        handleChange: debounce(function (e) {
            this.updateModule();
        }),
        async updateModule() {
            let option = { ...this.option };

            await window.origAPI.marker3d.update(option);
            window.origAPI.marker3d.focus(option.id);
        },
        async addModule() {
            let option = { ...this.option };

            await window.origAPI.marker3d.delete([option.id]);
            await window.origAPI.marker3d.add(option);
            window.origAPI.marker3d.focus(option.id);
        },
        handleSure() {
            if (!this.option.pointName) return this.$message.warning("请选择粒子类型！");
            if (!this.option.name) return this.$message.warning("名称不能为空！");
            if (!this.option.coordinate || !this.option.coordinate[0]) return this.$message.warning("请设置位置坐标！");
            let targetIndex = this.particleList.findIndex(
                (item) => item.name == this.option.name
            );
            if (targetIndex >= 0 && targetIndex !== this.editIndex)
                return this.$message.warning("名称不能重复！");

            let option = JSON.parse(JSON.stringify(this.option));
            if (this.editIndex < 0) this.particleList.push(option);
            else {
                // this.$set(this.particleList, this.editIndex, option);
                this.particleList[this.editIndex] = option
                this.editIndex = -1;
            }

            this.option = JSON.parse(JSON.stringify(defaultOption));

            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editFlag) this.editBooster(option);
            else this.saveBooster(option);
        },
        // 保存粒子到后端数据库
        async saveBooster(obj) {
            let data = sessionStorage.getItem("QXZS_particleEffect")
            if (data) {
                data = JSON.parse(data)
                data.push(obj)
            } else {
                data = [obj]
            }
            sessionStorage.setItem("QXZS_particleEffect", JSON.stringify(data))

            this.initData('init');
        },
        // 编辑粒子到后端数据库
        async editBooster(obj) {
            let index = this.particleList.findIndex(i => i.name === obj.name);
            this.particleList.splice(index, 1, obj);
            sessionStorage.setItem("QXZS_particleEffect", JSON.stringify(this.particleList))

            this.editFlag = false;
        },
        async handleCancel() {
            this.isShowPonitModel = false;
            this.isShowAddPoint = true;

            if (this.editIndex < 0) await window.origAPI.marker3d.delete(this.option.id);
            else {
                // 还原参数&&效果
                this.option = JSON.parse(JSON.stringify(this.particleList[this.editIndex]));
                await this.updateModule();
                this.editIndex = -1;
            }
            this.option = JSON.parse(JSON.stringify(defaultOption));
            if (this.editFlag) this.editFlag = false;
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    beforeUnmount() {
        if (this.option.id) window.origAPI.marker3d.delete(this.option.id);
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";
@import "../../css/draw.scss";

.points {
    position: relative;

    .img {
        position: absolute;
        right: 0;
        top: 50%;
        transform: translateY(-50%);
        cursor: pointer;
        padding: 6px;
        border-radius: 6px;

        &.ck {
            &::after {
                content: "";
                display: block;
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-image: radial-gradient(rgba(49, 101, 235, 0.4),
                        rgba(49, 101, 235, 0));
            }
        }

        img {
            width: 16px;
        }
    }
}

.wl-row {
    display: flex;

    &>*:not(:last-child) {
        margin-right: 14px;
    }
}

.wl-attr {
    display: flex;
    align-items: center;
    opacity: 0.8;

    &:not(:last-child) {
        margin-bottom: 8px;
    }

    span {
        display: inline-block;
        min-width: 68px;
        margin-right: 14px;
        font-size: 14px;
    }
}

.coors-box .coor-item {
    &:not(:last-child) {
        margin-bottom: 8px;
    }

    .coor-name {
        margin-right: 14px;
    }
}


/deep/ {
    .el-cascader {
        width: 100%;
    }

    .el-input-number {
        line-height: 34px;

        .el-input-number__decrease,
        .el-input-number__increase {
            border-color: #646b6f;
            background: transparent;
            color: #ffffff;
        }
    }
}
</style>