<!--
 * @FileDescription: 辅助分析->水淹分析
 * @Author: yuanhaijun
 * @Date: 2022.10.24
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.10.25
 -->
<template>
    <div>
        <div v-show="!editHelper.flag" class="cloud-func">
            <div class="func-title">
                水淹分析
                <!-- <span @click.stop="handleClose">
                    <i class="el-icon-close"></i>
                </span> -->
                <el-icon class="" @click.stop="handleClose">
                    <CircleClose />
                </el-icon>
            </div>
            <el-form size="mini" :model="fModel" ref="fRef" class="func-warp">
                <el-form-item label="水位海拔" prop="elevation">
                    <el-input-number v-model="fModel.elevation" controls-position="right" :precision="2" :min="0"
                        :max="1000000" @change="floodAnalysis"></el-input-number>
                    m
                </el-form-item>
                <el-form-item label="精度" prop="precision">
                    <el-input-number v-model="fModel.precision" controls-position="right" :precision="2" :step="0.01"
                        :min="0" :max="1" @change="floodAnalysis">
                    </el-input-number>
                </el-form-item>
                <el-form-item label="长度" prop="length">
                    <el-input-number v-model="fModel.length" controls-position="right" :step="1" :min="0" :max="1000000"
                        @change="floodAnalysis"></el-input-number>
                    m
                </el-form-item>
                <el-form-item label="宽度" prop="width">
                    <el-input-number v-model="fModel.width" controls-position="right" :step="1" :min="0" :max="1000000"
                        @change="floodAnalysis"></el-input-number>
                    m
                </el-form-item>
                <el-form-item label="颜色" prop="color">
                    <el-color-picker v-model="fModel.color" @change="floodAnalysis"></el-color-picker>
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

        <!-- 操作按钮 -->
        <ActionButtonTmpl v-if="editHelper.flag" :editHelper="editHelper" />
    </div>
</template>

<script>
import ActionButtonTmpl from "../common/ActionButton.tmpl.vue"
import { CircleClose } from '@element-plus/icons-vue'
export default {
    components: {
        ActionButtonTmpl,
        CircleClose
    },
    name: "Flooding",
    data() {
        return {
            editHelper: {
                flag: false,
                type: "shape",
            },
            fModel: {
                length: 0,
                width: 0,
                elevation: 0,
                color: "#006199",
                precision: 0.5,
            },
            isCreated: false, // 已创建true
            coors: undefined, // 绘制信息
            cacheParams: undefined, // 缓存参数
        };
    },
    mounted() { },
    methods: {
        // 水淹分析
        floodAnalysis() {
            let _w = this.fModel.width / 2;
            let _l = this.fModel.length / 2;
            this.startAnalysis(
                [this.coors.centre[0] - _w, this.coors.centre[1] - _l],
                [this.coors.centre[0] + _w, this.coors.centre[1] + _l]
            );
        },
        // 开始分析
        async startAnalysis(min, max) {
            let seed = [this.coors.arr[0][0], this.coors.arr[0][1]]; // DEF：第一个点

            await window.origAPI.floodFill.clear();
            let params = {
                id: `ff_${new Date().getTime()}`,
                min: min, // 水淹分析范围min
                max: max, // 水淹分析范围max
                seed: seed, // 出水点 注意：出水点一定要在水淹分析范围[min~max]内，否则接口会报错
                elevation: this.fModel.elevation, // 水位高度
                color: this.fModel.color, // 水颜色
                precision: this.fModel.precision // 水淹模拟精度
            }
            await window.origAPI.floodFill.add(params);
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
            this.editHelper.flag = false
            let arr = coordinates || [];
            let _X = arr.map((a) => a[0]).sort((a, b) => a - b); // 升序
            let _Y = arr.map((a) => a[1]).sort((a, b) => a - b);
            let _C = {
                arr: arr,
                W: Math.abs(_X[_X.length - 1] - _X[0]),
                H: Math.abs(_Y[_Y.length - 1] - _Y[0]),
            };
            _C.centre = [_X[0] + _C.W / 2, _Y[0] + _C.H / 2]; // 中心点
            this.coors = _C;
            this.$nextTick(() => {
                // this.$set(this.fModel, "width", _C.W);
                // this.$set(this.fModel, "length", _C.H);
                // this.$set(this.fModel, "elevation", Number(_C.arr[0][2]) + 0.01); // DEF：第一个点的高度
                this.fModel.width = _C.W
                this.fModel.length = _C.H
                this.fModel.elevation = Number(_C.arr[0][2]) + 0.01
                this.cacheParams = { ...this.fModel }

                this.startAnalysis(
                    [_X[0], _Y[0]],
                    [_X[_X.length - 1], _Y[_Y.length - 1]]
                );
            });
        },
        // 关闭编辑助手
        closeEditHelper() {
            // this.$set(this.editHelper, 'flag', false);
            this.editHelper.flag = false
        },
        async cancelForm() {
            if (!this.isCreated) return false;

            // 删除场景中所有的FloodFill
            await window.origAPI.floodFill.clear();
            this.isCreated = false;
            // this.resetForm();
        },
        async resetForm(opr) {
            console.log(this.$refs.fRef, ' this.$refs.fRef this.$refs.fRef');
            this.$refs.fRef.resetFields();

            if (opr === "RTZ") {
                this.fModel = { ...this.cacheParams }
                this.floodAnalysis();
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