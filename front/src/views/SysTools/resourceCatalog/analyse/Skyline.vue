<!--
 * @FileDescription: 辅助分析->天际线分析
 * @Author: yuanhaijun
 * @Date: 2022.11.25
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.06
 -->
<template>
    <div class="cloud-func" v-show="showPanle">
        <div class="func-title">
            天际线
            <el-icon class="" @click="handleClose">
                <CircleClose />
            </el-icon>
        </div>

        <el-form size="mini" :model="fModel" ref="fRef" label-position="top" class="func-warp">
            <el-scrollbar>
                <el-form-item label="显示轮廓线" prop="showOutline">
                    <el-radio-group v-model="fModel.showOutline" @change="startAnalysis">
                        <el-radio v-for="(op, i) in radioOPT" :key="i" :label="op.value">
                            {{ op.label }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="轮廓宽度" prop="outlineThickness" class="flex-row">
                    <el-slider :min="0.1" :max="10" :step="0.1" v-model="fModel.outlineThickness" @change="startAnalysis"
                        :show-input="false" :show-input-controls="false"></el-slider>
                    <el-input-number :min="0.1" :max="10" :step="0.1" v-model="fModel.outlineThickness"
                        @change="startAnalysis" controls-position="right"></el-input-number>
                </el-form-item>
                <el-form-item label="轮廓颜色" prop="outlineColor" class="color-setting">
                    <ul>
                        <svg-icon icon-class="cloud-color" class-name="icon"></svg-icon>
                        <el-color-picker v-model="fModel.outlineColor" @change="startAnalysis"></el-color-picker>
                    </ul>
                    <el-input v-model="fModel.outlineColor" placeholder="输入，例如:#FF0000" @change="startAnalysis"></el-input>
                </el-form-item>
                <el-form-item label="使用场景颜色" prop="useSceneColor">
                    <el-radio-group v-model="fModel.useSceneColor" @change="startAnalysis">
                        <el-radio v-for="(op, i) in radioOPT" :key="i" :label="op.value">
                            {{ op.label }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="场景颜色" prop="sceneColor" class="color-setting">
                    <ul>
                        <svg-icon icon-class="cloud-color" class-name="icon"></svg-icon>
                        <el-color-picker v-model="fModel.sceneColor" @change="startAnalysis"></el-color-picker>
                    </ul>
                    <el-input v-model="fModel.sceneColor" placeholder="输入，例如:#FF0000" @change="startAnalysis"></el-input>
                </el-form-item>
                <el-form-item label="显示天际线窗口" prop="showSkyline">
                    <el-radio-group v-model="fModel.showSkyline" @change="startAnalysis">
                        <el-radio v-for="(op, i) in radioOPT" :key="i" :label="op.value">
                            {{ op.label }}
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="天际线窗口" prop="windowSize" class="flex2-row">
                    <template v-for="(val, j) in fModel.windowSize" :key="j">
                        <el-input-number :min="50" :max="2000" :step="1" v-model="fModel.windowSize[j]"
                            @change="startAnalysis" controls-position="right"></el-input-number>
                    </template>
                </el-form-item>
                <el-form-item label="天际颜色" prop="skylineColor" class="color-setting">
                    <ul>
                        <svg-icon icon-class="cloud-color" class-name="icon"></svg-icon>
                        <el-color-picker v-model="fModel.skylineColor" @change="startAnalysis"></el-color-picker>
                    </ul>
                    <el-input v-model="fModel.skylineColor" placeholder="输入，例如:#FF0000" @change="startAnalysis"></el-input>
                </el-form-item>
                <el-form-item label="窗口颜色" prop="backgroundColor" class="color-setting">
                    <ul>
                        <svg-icon icon-class="cloud-color" class-name="icon"></svg-icon>
                        <el-color-picker v-model="fModel.backgroundColor" @change="startAnalysis"></el-color-picker>
                    </ul>
                    <el-input v-model="fModel.backgroundColor" placeholder="输入，例如:#FF0000"
                        @change="startAnalysis"></el-input>
                </el-form-item>
                <el-form-item label="视点高度" prop="height" class="flex-row">
                    <el-slider :min="0" :max="10000" :step="1" v-model="fModel.height" @change="startAnalysis"
                        :show-input="false" :show-input-controls="false"></el-slider>
                    <el-input-number :min="0" :max="10000" :step="1" v-model="fModel.height" @change="startAnalysis"
                        controls-position="right"></el-input-number>
                </el-form-item>
            </el-scrollbar>

            <el-form-item class="btn">
                <el-button type="primary" @click="handleReset">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { mapState } from "pinia";
import { useSysToolsCimStore } from "@/stores/sysToolsCim"
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: "SkylineAnalysis",
    data() {
        return {
            showPanle: false,
            radioOPT: [
                { label: "是", value: true },
                { label: "否", value: false },
            ],
            fModelBAK: undefined,
            fModel: {
                showOutline: true,
                outlineThickness: 1,
                outlineColor: "#00FF00",
                useSceneColor: false,
                sceneColor: "#000000",
                showSkyline: true,
                windowSize: [400, 200],
                skylineColor: "#00FF00",
                backgroundColor: "#000000",
                height: 0
            },
        };
    },
    components: {
        CircleClose
    },
    computed: {
        ...mapState(useSysToolsCimStore, ['eventSealAPI'])
    },
    watch: {
        eventSealAPI: {
            indeterminate: true,
            deep: true,
            handler(val) {
                if (val.eventtype == "Skyline") {
                    this.showPanle = true;
                    let that = this;
                    setTimeout(() => {
                        let _h = val.position[2] + 0.1;
                        if (that.fModelBAK.height !== _h) {
                            // that.$set(that.fModelBAK, "height", _h);
                            // 视点高度
                            // that.$set(that.fModel, "height", _h);
                            that.fModelBAK.height = _h
                            that.fModel.height = _h
                        }
                        that.startAnalysis();
                    }, 200)
                }
            }
        }
    },
    mounted() {
        this.fModelBAK = JSON.parse(JSON.stringify(this.fModel));

        this.$message.success("进入天际线分析！");
        this.startAnalysis();
    },
    methods: {
        handleReset() {
            this.$refs.fRef.resetFields();
            this.fModel = JSON.parse(JSON.stringify(this.fModelBAK));

            this.$nextTick(() => {
                this.startAnalysis();
            });
        },
        // 开始分析
        async startAnalysis() {
            await window.sealAPI._tools.startSkylineAnalysis(this.fModel);
        },
        // 分析结束
        async cancelAnalyse() {
            await window.sealAPI._tools.stopSkylineAnalysis();
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    async beforeUnmount() {
        await this.cancelAnalyse();
        await this.handleClose();
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.func-warp {

    /deep/.el-form-item {
        flex-direction: column;
        align-items: flex-start !important;
    }

    /deep/.el-scrollbar {
        max-height: 72vh;
        overflow-y: auto;
        margin-bottom: 14px;
    }

    /deep/.btn {
        .el-button {
            width: 100% !important;
        }
    }

    /deep/.color-setting .el-form-item__content {
        display: flex;
        align-items: center;

        ul {
            display: flex;
            align-items: center;
            margin-right: 16px;
        }

        .el-input {
            width: 100px;
        }
    }

    /deep/.flex-row .el-form-item__content {
        display: flex;
        align-items: center;

        .el-slider {
            width: calc(100% - 100px);
            margin-right: 14px;
            margin-left: 8px;
        }

        .el-input-number {
            width: 80px;
        }
    }

    /deep/.flex2-row .el-form-item__content {
        display: flex;
        align-items: center;

        .el-input-number {
            width: calc(50% - 7px);

            &:not(:last-child) {
                margin-right: 16px;
            }
        }
    }
}
</style>