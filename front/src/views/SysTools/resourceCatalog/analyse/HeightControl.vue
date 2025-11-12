<!--
 * @FileDescription: 辅助分析->控高分析
 * @Author: yuanhaijun
 * @Date: 2022.11.18
 * @LastEditors: tiejinning
 * @LastEditTime: 2023.04.07
 -->
<template>
    <div>
        <div v-show="!editHelper.flag" class="cloud-func">
            <div class="func-title">
                限高分析
                <!-- <span @click.stop="handleClose">
                    <i class="el-icon-close"></i>
                </span> -->
                <el-icon class="" @click.stop="handleClose">
                    <CircleClose />
                </el-icon>
            </div>
            <el-form size="mini" :model="fModel" ref="fRef" class="func-warp" @submit.prevent>
                <el-form-item label="限高">
                    <el-input-number v-model="fModel.heightRange[0]" controls-position="right" :step="1" :min="0"
                        :max="1000000" @change="changeValue"></el-input-number>
                    m
                </el-form-item>
                <el-form-item class="btn">
                    <el-button @click="reset">重置</el-button>

                    <el-button v-if="!isCreated" type="primary" @click="submitForm">
                        创建
                    </el-button>
                    <el-button v-else type="primary" @click="restart">重新创建</el-button>
                </el-form-item>
            </el-form>
        </div>

        <!-- 操作按钮 -->
        <ActionButtonTmpl v-if="editHelper.flag" :editHelper="editHelper" />
    </div>
</template>

<script>
import ActionButtonTmpl from '../common/ActionButton.tmpl'
import { CircleClose } from '@element-plus/icons-vue'
export default {
    components: {
        ActionButtonTmpl,
        CircleClose
    },
    name: "HeightControl",
    data() {
        return {
            editHelper: {
                flag: false,
                type: "shape",
            },
            fModel: {
                id: "",
                coordinates: [],
                heightRange: [30.0, 500.0], //高亮染色区域可以限定一个高度范围，也就是Z坐标的区间，只有Z值这这个区间的模型才会被染色
                color: [1, 0, 0, 0.8],
                intensity: 5.0, //高亮颜色的强度
                depthTest: true, //深度检测
            },
            isCreated: false, // 已创建true
            coors: undefined, // 绘制信息
            // cacheParams: undefined, // 缓存参数
        };
    },
    beforeUnmount() {
        window.sealAPI._highlightArea.clear();
        window.origAPI.editHelper.cancel();
    },
    methods: {
        // 开始分析
        async startAnalysis(type = "add") {
            if (type == "add") {
                this.fModel.id = "lh_" + new Date().getTime();
                await window.sealAPI._highlightArea.add(this.fModel);
                window.sealAPI._highlightArea.focus(this.fModel.id);
                // this.cacheParams = { ...this.fModel }
            } else if (type == "edit") {
                window.sealAPI._highlightArea.update(this.fModel);
            }

        },
        // 更新
        changeValue() {
            if (!this.fModel.id) return
            this.$nextTick(() => {
                this.startAnalysis("edit");
            })
        },
        submitForm() {
            this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;

                this.isCreated = true;
                // this.$set(this.editHelper, 'flag', true);
                this.editHelper.flag = true
                await window.origAPI.floodFill.clear();
            });
        },
        // 获取编辑助手返回数据
        getEditDate(coordinates) {
            // this.$set(this.editHelper, 'flag', false);
            // this.$set(this.fModel, 'coordinates', coordinates)
            this.editHelper.flag = false
            this.fModel.coordinates = coordinates
            this.$nextTick(() => {
                this.startAnalysis();
            });
        },
        // 关闭编辑助手
        closeEditHelper() {
            // this.$set(this.editHelper, 'flag', false);
            this.editHelper.flag = false
        },
        // 重新开始
        reset() {
            if (!this.fModel.id) return
            // this.$set(this.fModel, "heightRange", [30.0, 500.0])
            this.fModel.heightRange = [30.0, 500.0]
            this.$nextTick(() => {
                this.startAnalysis("edit");
            })
        },
        restart() {
            window.sealAPI._highlightArea.clear();
            // this.$set(this.fModel, "id", "")
            this.fModel.id = ""
            // this.$set(this.fModel, "heightRange", [30.0, 500.0])
            this.$nextTick(() => { })
            this.submitForm()
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";
</style>