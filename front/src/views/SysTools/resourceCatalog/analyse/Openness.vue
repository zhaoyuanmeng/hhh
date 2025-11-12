<!--
 * @FileDescription: 辅助分析->开敞度分析
 * @Author: yuanhaijun
 * @Date: 2022.10.24
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.10.24
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            开敞度分析
            <!-- <span @click.stop="handleClose">
                <i class="el-icon-close"></i>
            </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <el-form size="mini" :model="fModel" ref="fRef" class="func-warp">
            <el-form-item label="球体直线" prop="radius">
                <el-input-number v-model="fModel.radius" controls-position="right" :min="0.01"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="不透明度" prop="opacity">
                <el-input-number v-model="fModel.opacity" controls-position="right" :precision="0" :step="1" :min="0"
                    :max="100" @change="submitForm">
                </el-input-number>
                %
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
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: "Openness",
    components: {
        CircleClose
    },
    data() {
        return {
            fModel: {
                radius: 150, // 500
                opacity: 30,
                visibleColor: "#00FF00",
                invisibleColor: "#FF0000",
            },
            isCreated: false, // 已创建true
        };
    },
    mounted() { },
    methods: {
        submitForm() {
            this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;

                await window.sealAPI._tools.startViewDomeAnalysis_biz(
                    this.fModel.radius,
                    this.fModel.opacity / 100,
                    this.fModel.visibleColor,
                    this.fModel.invisibleColor
                );
                this.isCreated = true;
            });
        },
        async cancelForm() {
            if (!this.isCreated) return false;

            await window.sealAPI._tools.stopViewDomeAnalysis();
            this.isCreated = false;
            // this.resetForm();
        },
        async resetForm(opr) {
            this.$refs.fRef.resetFields();

            if (opr === "RTZ") {
                this.submitForm();
            }
        },
        handleClose() {
            //   if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
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
@import "~@/assets/css/func3.scss";
</style>