<!--
 * @FileDescription: 辅助分析->通视分析
 * @Author: yuanhaijun
 * @Date: 2022.10.24
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-19 19:51:50
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            通视分析
            <!-- <span @click.stop="handleClose">
                <i class="el-icon-close"></i>
            </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <el-form  :model="fModel" ref="fRef" class="func-warp">
            <el-form-item label="观察高度" prop="height">
                <el-input-number v-model="fModel.height" controls-position="right" :precision="2"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="可见颜色" prop="visibleColor">
                <el-color-picker v-model="fModel.visibleColor" @change="submitForm"></el-color-picker>
            </el-form-item>
            <el-form-item label="遮挡颜色" prop="invisibleColor">
                <el-color-picker v-model="fModel.invisibleColor" @change="submitForm"></el-color-picker>
            </el-form-item>
            <el-form-item class="btn">
                <el-button @click="resetForm('RTZ')">重置</el-button>

                <el-button v-if="!isCreated" type="primary" @click="submitForm">
                    创建
                </el-button>
                <el-button v-else type="primary" @click="cancelForm">取消</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { mapState } from "pinia";
import { useSysToolsCimStore } from '@/store/modules/sysToolsCim'
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: "Perspective",
    components: {
        CircleClose
    },
    data() {
        return {
            fModel: {
                height: 0,
                visibleColor: "#00FF00",
                invisibleColor: "#FF0000",
            },
            isCreated: false, // 已创建true
            viewpointHeight: undefined, // 视点高度（距离场景交互所拾取点的高度），取值范围：[任意负数~任意正数]，默认值：0
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
                if (val.eventtype === "VisibilityAnalysis") {
                    this.setResults(val);
                }
            },
        },
    },
    methods: {
        // 观察高度
        setResults(data) {
            if (Object.prototype.hasOwnProperty.call(data.hasOwnProperty, "isVisibility")) return;

            // this.$set(this.fModel, "height", data.viewpoint[2]);
            this.fModel.height = data.viewpoint[2]
            if (this.viewpointHeight === undefined) {
                this.viewpointHeight = this.fModel.height;
            }
        },
        submitForm() {
            this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;

                let height = this.fModel.height;
                if (this.viewpointHeight !== undefined) {
                    height = height - this.viewpointHeight;
                }
                await window.sealAPI._tools.startVisiblityAnalysis_biz(
                    height,
                    this.fModel.visibleColor,
                    this.fModel.invisibleColor
                );
                this.isCreated = true;
            });
        },
        async cancelForm() {
            if (!this.isCreated) return false;

            this.viewpointHeight = undefined;
            await window.sealAPI._tools.stopVisiblityAnalysis();
            this.isCreated = false;
            // this.resetForm();
        },
        async resetForm(opr) {
            this.$refs.fRef.resetFields();

            if (opr === "RTZ") {
                // this.$set(this.fModel, "height", this.viewpointHeight);
                this.fModel.height = this.viewpointHeight
                this.submitForm();
            }
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    async beforeUnmount() {
        this.cancelForm();
        // await this.resetForm();
        await this.handleClose();
    },
};
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";
</style>
