<!--
 * @FileDescription: 辅助分析->日照分析
 * @Author: yuanhaijun
 * @Date: 2022.10.24
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.10.25
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            日照分析
            <!-- <span @click.stop="handleClose">
                <i class="el-icon-close"></i>
            </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <el-form size="mini" :model="fModel" ref="fRef" class="func-warp">
            <el-form-item label="分析日期" prop="date">
                <el-date-picker v-model="fModel.date" type="date" @change="submitForm">
                </el-date-picker>
            </el-form-item>
            <el-form-item label="分析时间" prop="time">
                <el-time-picker is-range v-model="fModel.time" range-separator="-" start-placeholder="开始时间"
                    end-placeholder="结束时间" @change="submitForm">
                </el-time-picker>
            </el-form-item>
            <el-form-item label="高度" prop="height">
                <el-input-number v-model="fModel.height" controls-position="right" :precision="0" :min="0"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="底面高度" prop="groundElevation">
                <el-input-number v-model="fModel.groundElevation" controls-position="right" :precision="0"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="采样点尺寸" prop="sphereRadius">
                <el-input-number v-model="fModel.sphereRadius" controls-position="right" :precision="2"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="采样间距" prop="spacing">
                <el-input-number v-model="fModel.spacing" controls-position="right" :precision="0"
                    @change="submitForm"></el-input-number>
                m
            </el-form-item>
            <el-form-item label="日照颜色" class="color">
                <img :src="require('@/assets/images/cloud/sunshine.png')" />
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
import { formatDate } from "@/utils/common"
import { useSysToolsCimStore } from "@/stores/sysToolsCim"
import { mapState } from "pinia";
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: "Sunshine",
    components: {
        CircleClose
    },
    data() {
        return {
            fModel: {
                date: new Date(),
                time: [],
                spacing: 0,
                groundElevation: 0,
                height: 50,
                sphereRadius: 1,
            },
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
                if (val.eventtype === "Sunshine") {
                    this.setResults(val);
                }
            },
        },
    },
    mounted() {
        this.setTimeSlot();
    },
    methods: {
        // set data
        setTimeSlot() {
            let time0 = new Date();
            time0.setHours(8);
            time0.setMinutes(0);
            time0.setSeconds(0);
            let time1 = new Date();
            time1.setHours(18);
            time1.setMinutes(0);
            time1.setSeconds(0);
            // this.$set(this.fModel, "time", [time0, time1]);
            this.fModel.time = [time0, time1]
        },
        // 地面高度、采样间距
        setResults(data) {
            // this.$set(this.fModel, "spacing", data.spacing);
            // this.$set(this.fModel, "groundElevation", data.buildPoints[0][2]);
            this.fModel.spacing = data.spacing
            this.fModel.groundElevation = data.buildPoints[0][2]
        },
        submitForm() {
            this.$refs.fRef.validate(async (valid) => {
                if (!valid) return false;
                let date = this.fModel.date;
                let time = this.fModel.time;

                await window.sealAPI._tools.startSunshineAnalysis_biz(
                    date.getFullYear(),
                    date.getMonth() + 1,
                    date.getDate(),
                    formatDate(time[0], "hh:mm"),
                    formatDate(time[1], "hh:mm"),
                    this.fModel.spacing,
                    this.fModel.groundElevation,
                    this.fModel.height,
                    this.fModel.sphereRadius
                );
                this.isCreated = true;
            });
        },
        async cancelForm() {
            if (!this.isCreated) return false;

            await window.sealAPI._tools.stopSunshineAnalysis();
            this.isCreated = false;
            // this.resetForm();
        },
        async resetForm(opr) {
            this.$refs.fRef.resetFields();
            this.setTimeSlot();

            if (opr === "RTZ") {
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

<style lang="scss" >
/deep/.el-popper .popper__arrow::after {
    border-bottom-color: #ffffff !important;
}
</style>
<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.cloud-func {
    width: 340px;

    .color img {
        width: 100%;
    }

    /deep/.func-warp .el-form-item {
        .el-form-item__label {
            width: 92px !important;
        }

        &:not(.btn) {
            .el-form-item__content {
                width: calc(100% - 92px) !important;
            }
        }
    }
}
</style>