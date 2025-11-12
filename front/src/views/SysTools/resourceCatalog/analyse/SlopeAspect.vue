<!--
 * @FileDescription: 辅助分析->坡度坡向分析
 * @Author: yuanhaijun
 * @Date: 2022.10.26
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.10.26
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            坡度坡向分析
            <!-- <span @click.stop="handleClose">
                <i class="el-icon-close"></i>
            </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <el-form size="mini" :model="fModel" ref="fRef" class="func-warp">
            <el-form-item label="显示样式" prop="colorMode">
                <el-radio-group v-model="fModel.colorMode" @change="submitForm">
                    <el-radio :label="1">坡度 </el-radio>
                    <el-radio :label="2">坡向</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="坡向箭头" prop="showArrow">
                <el-checkbox v-model="fModel.showArrow" @change="submitForm"></el-checkbox>
            </el-form-item>
            <el-form-item label="箭头颜色" prop="arrowColor">
                <el-color-picker v-model="fModel.arrowColor" :show-alpha="true" color-format="rgb"
                    @change="submitForm"></el-color-picker>
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
import { rgbaToRgb } from "@/utils/common";
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: "SlopeAspect",
    components: {
        CircleClose
    },
    data() {
        return {
            fModel: {
                showArrow: true,
                colorMode: 1,
                arrowColor: "rgb(255 255 255)",
            },
            isCreated: false, // 已创建true
        };
    },
    mounted() { },
    methods: {
        submitForm() {
            this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;

                await window.sealAPI._tools.startTerrainSlopeAnalysis_biz(
                    this.fModel.showArrow,
                    this.fModel.colorMode,
                    rgbaToRgb(this.fModel.arrowColor)
                );
                this.isCreated = true;
            });
        },
        async cancelForm() {
            if (!this.isCreated) return false;

            await window.sealAPI._tools.stopTerrainSlopeAnalysis();
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
}

/deep/.el-radio-group .el-radio {
    color: #fff;
}
</style>