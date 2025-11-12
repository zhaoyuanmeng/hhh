<!--
 * @FileDescription: 辅助分析->等高线分析
 * @Author: yuanhaijun
 * @Date: 2022.10.27
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.10.27
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            等高线分析
            <!-- <span @click.stop="handleClose">
                <i class="el-icon-close"></i>
            </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <el-form size="mini" :model="fModel" ref="fRef" class="func-warp">
            <el-form-item label="显示样式">
                <el-checkbox v-model="fModel.isShowContourLine" @change="submitForm">
                    等高线
                </el-checkbox>
                <el-checkbox v-model="fModel.isShowContourPlane" @change="submitForm">
                    等高面
                </el-checkbox>
            </el-form-item>
            <!-- <el-form-item label="填充颜色" prop="fillColor">
                <el-color-picker
                  v-model="fModel.fillColor"
                  @change="submitForm"
                ></el-color-picker>
              </el-form-item> -->
            <el-form-item v-if="fModel.isShowContourPlane" label="不透明度" prop="contourPlaneOpacity">
                <el-input-number v-model="fModel.contourPlaneOpacity" controls-position="right" :precision="0" :min="0"
                    :max="100" @change="submitForm"></el-input-number>
                %
            </el-form-item>
            <el-form-item label="最小高度" prop="contourLineRangeMin">
                <el-input-number v-model="fModel.contourLineRangeMin" controls-position="right" :precision="0"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="最大高度" prop="contourLineRangeMax">
                <el-input-number v-model="fModel.contourLineRangeMax" controls-position="right" :precision="0"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item v-if="fModel.isShowContourLine" label="线颜色" prop="contourLineColor">
                <el-color-picker v-model="fModel.contourLineColor" @change="submitForm"></el-color-picker>
            </el-form-item>
            <el-form-item v-if="fModel.isShowContourLine" label="线间距" prop="contourLineSpacing">
                <el-input-number v-model="fModel.contourLineSpacing" controls-position="right" :precision="0" :min="5"
                    :max="5000" @change="submitForm"></el-input-number>
                m
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
    name: "Contour",
    components: {
        CircleClose
    },
    data() {
        return {
            fModel: {
                isShowContourPlane: true,
                contourPlaneOpacity: 100,
                isShowContourLine: true,
                contourLineColor: "#ffffff",
                contourLineSpacing: 20,
                contourLineRangeMax: 1000000,
                contourLineRangeMin: -1000000,
            },
            isCreated: false, // 已创建true
        };
    },
    mounted() { },
    methods: {
        submitForm() {
            this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;

                await window.sealAPI._tools.startContourLineAnalysis_biz(
                    this.fModel.isShowContourPlane,
                    this.fModel.contourPlaneOpacity / 100,
                    this.fModel.isShowContourLine,
                    this.fModel.contourLineColor,
                    this.fModel.contourLineSpacing,
                    this.fModel.contourLineRangeMax,
                    this.fModel.contourLineRangeMin
                );
                this.isCreated = true;
            });
        },
        async cancelForm() {
            if (!this.isCreated) return false;

            await window.sealAPI._tools.stopContourLineAnalysis();
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

/deep/.el-checkbox {
    line-height: normal;
    padding: 3px;

    &:not(:last-child) {
        margin-right: 6px;
    }
}
</style>