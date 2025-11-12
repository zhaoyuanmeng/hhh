<!--
 * @FileDescription: 辅助分析->填挖方
 * @Author: yuanhaijun
 * @Date: 2022.10.26
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.10.26
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            填挖方
            <!-- <span @click.stop="handleClose">
                <i class="el-icon-close"></i>
            </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <el-form size="mini" :model="fModel" ref="fRef" class="func-warp">
            <el-form-item label="高度" prop="height">
                <el-input-number v-model="fModel.height" controls-position="right" :precision="2"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="网格尺寸" prop="gridSize">
                <el-input-number v-model="fModel.gridSize" controls-position="right" :precision="2" :min="0" :max="100"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="网格" prop="gridLineThickness">
                <el-input-number v-model="fModel.gridLineThickness" controls-position="right" :precision="0" :min="0"
                    :max="100" @change="submitForm"></el-input-number>
                像素
            </el-form-item>
            <el-form-item label="线宽" prop="lineThickness">
                <el-input-number v-model="fModel.lineThickness" controls-position="right" :precision="0" :min="0" :max="100"
                    @change="submitForm"></el-input-number>
                像素
            </el-form-item>
            <el-form-item label="端点" prop="pointSize">
                <el-input-number v-model="fModel.pointSize" controls-position="right" :precision="0" :min="0" :max="100"
                    @change="submitForm"></el-input-number>
                像素
            </el-form-item>
            <el-form-item label="网格颜色" prop="gridColor">
                <el-color-picker v-model="fModel.gridColor" :show-alpha="true" color-format="rgb"
                    @change="submitForm"></el-color-picker>
            </el-form-item>
            <el-form-item label="挖方颜色" prop="cutLineColor">
                <el-color-picker v-model="fModel.cutLineColor" :show-alpha="true" color-format="rgb"
                    @change="submitForm"></el-color-picker>
            </el-form-item>
            <el-form-item label="填方颜色" prop="fillLineColor">
                <el-color-picker v-model="fModel.fillLineColor" :show-alpha="true" color-format="rgb"
                    @change="submitForm"></el-color-picker>
            </el-form-item>
            <el-form-item label="挖方端点" prop="cutPointColor">
                <el-color-picker v-model="fModel.cutPointColor" :show-alpha="true" color-format="rgb"
                    @change="submitForm"></el-color-picker>
            </el-form-item>
            <el-form-item label="填方端点" prop="fillPointColor">
                <el-color-picker v-model="fModel.fillPointColor" :show-alpha="true" color-format="rgb"
                    @change="submitForm"></el-color-picker>
            </el-form-item>

            <div v-if="!!analysisResults.eventtype" class="result">
                <span>分析结果</span>
                <ul>
                    <li>
                        挖方面积
                        <span>{{ analysisResults.cutVolume }}m²</span>
                        <i class="el-icon-document-copy" @click="handleCopy(analysisResults.cutVolume)"></i>
                    </li>
                    <li>
                        填方面积
                        <span>{{ analysisResults.fillArea }}m²</span>
                        <i class="el-icon-document-copy" @click="handleCopy(analysisResults.fillArea)"></i>
                    </li>
                    <li>
                        挖方体积
                        <span>{{ analysisResults.cutArea }}m³</span>
                        <i class="el-icon-document-copy" @click="handleCopy(analysisResults.cutArea)"></i>
                    </li>
                    <li>
                        填方体积
                        <span>{{ analysisResults.fillVolume }}m³</span>
                        <i class="el-icon-document-copy" @click="handleCopy(analysisResults.fillVolume)"></i>
                    </li>
                    <li>
                        体积差值
                        <span>{{ analysisResults.volumeDifference }}m³</span>
                        <i class="el-icon-document-copy" @click="handleCopy(analysisResults.volumeDifference)"></i>
                    </li>
                </ul>
            </div>

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
import { mapState } from "pinia";
import { useSysToolsCimStore } from '@/stores/sysToolsCim'
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: "CutFill",
    components: {
        CircleClose
    },
    data() {
        return {
            fModel: {
                height: 0,
                gridSize: 5,
                gridLineThickness: 3,
                lineThickness: 1,
                pointSize: 5,
                gridColor: "rgb(255,255,0)",
                cutLineColor: "rgb(255,0,0)",
                fillLineColor: "rgb(0,128,0)",
                cutPointColor: "rgb(0,0,255)",
                fillPointColor: "rgb(0,0,255)",
            },
            analysisResults: {},
            isCreated: false, // 已创建true
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
                if (val.eventtype === "cutFillAnalysis") {
                    this.setResults(val);
                }
            },
        },
    },
    methods: {
        setResults(data) {
            this.analysisResults = data;
        },
        submitForm() {
            this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;

                window.sealAPI._tools.startCutFillAnalysis_biz(
                    this.fModel.height,
                    this.fModel.gridSize,
                    this.fModel.lineThickness,
                    this.fModel.pointSize,
                    this.fModel.gridLineThickness,
                    rgbaToRgb(this.fModel.cutLineColor),
                    rgbaToRgb(this.fModel.fillLineColor),
                    rgbaToRgb(this.fModel.cutPointColor),
                    rgbaToRgb(this.fModel.fillPointColor),
                    rgbaToRgb(this.fModel.gridColor)
                );
                this.isCreated = true;
            });
        },
        async cancelForm() {
            if (!this.isCreated) return false;

            await window.sealAPI._tools.stopCutFillAnalysis();
            this.isCreated = false;
            this.analysisResults = {};
            // this.resetForm();
        },
        async resetForm(opr) {
            this.$refs.fRef.resetFields();
            if (opr === "RTZ") {
                this.submitForm();
            }
        },
        handleCopy(data) {
            var input = document.createElement("input");
            input.value = data;
            document.body.appendChild(input);
            input.select();
            document.execCommand("Copy");
            document.body.removeChild(input);
            this.$message({
                message: "已复制到剪切板",
                type: "success",
            });
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    async beforeUnmount() {
        this.cancelForm();
        __g.tools.stopCutFillAnalysis();
        // await this.resetForm();
        await this.handleClose();
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.result {
    margin-bottom: 16px;

    &>span {
        color: #ffffff80;
    }

    &>ul {
        font-size: 12px;
        border-radius: 4px;
        background: #494d52;
        padding: 6px 0;
        margin-top: 6px;

        li {
            height: 28px;
            line-height: 28px;
            padding: 0 14px;

            span {
                color: #fff;
                margin: 0 4px;
            }

            i {
                line-height: inherit;
                padding: 0 4px;
                float: right;
                cursor: pointer;
            }
        }
    }
}
</style>