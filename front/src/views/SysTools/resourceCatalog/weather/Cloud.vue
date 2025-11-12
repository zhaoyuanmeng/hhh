<!--
 * @FileDescription: 天气仿真->云
 * @Author: yuanhaijun
 * @Date: 2022.12.15
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.12.15
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            云
            <!-- <span @click.stop="handleClose">
                        <i class="el-icon-close"></i>
                    </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="weather-state">
            <span>开启云</span>
            <el-switch v-model="status" active-color="#13ce66" inactive-color="#979797" @change="changeOpen">
            </el-switch>
        </div>
        <el-form size="mini" class="func-warp" label-position="top">
            <el-form-item v-for="(item, index) in cloudModel" :label="item.name" :key="index + item.name">
                <el-row :gutter="20">
                    <el-col :span="16">
                        <el-slider :min="item.min" :max="item.max" :step="item.step" v-model="item.value" @change="
                            (val) => {
                                handleChange(item.type, index, val);
                            }
                        " :show-input="false" :show-input-controls="false"></el-slider>
                    </el-col>
                    <el-col :span="8">
                        <el-input-number v-model="item.value" :min="item.min" :max="item.max" :step="item.step"
                            controls-position="right"></el-input-number>
                    </el-col>
                </el-row>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import { onBeforeUnmount } from 'vue'
import { CircleClose } from '@element-plus/icons-vue'
export default {
    name: "Cloud",
    components: {
        CircleClose
    },
    data() {
        return {
            status: null,
            cloudModel: [],
        };
    },
    created() {
        let param = window.sealAPI._weather.getPARAM("云");
        this.status = param.open;
        this.cloudModel = param.data;

        this.changeOpen(this.status);
    },
    mounted() { },
    methods: {
        async changeOpen(value) {
            window.sealAPI._weather.param["云"].open = value;

            if (value) {
                this.cloudModel.forEach((item, index) => {
                    this.handleChange(item.type, index, item.value, "init");
                });
            } else await window.sealAPI._weather.setCloudDensity(0);
        },
        handleChange(type, index, val, opr) {
            if (!opr) {
                window.sealAPI._weather.param["云"].data[index].value = val;
                if (!this.status) return;
            }

            switch (type) {
                case "cloudDensity":
                    window.sealAPI._weather.setCloudDensity(val);
                    break;
                case "cloudHeight":
                    window.sealAPI._weather.setCloudHeight(val);
                    break;
                case "cloudThickness":
                    window.sealAPI._weather.setCloudThickness(val);
                    break;
            }
        },
        handleClose() {
            //   if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
            this.$emit('close')
        },
    },
    async onBeforeUnmount() {
        await this.handleClose();
    },
};
</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.weather-state {
    height: 56px;
    display: flex;
    font-size: 14px;
    padding: 20px 24px;
    align-items: center;
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);

    span {
        margin-right: 20px;
    }
}

/deep/.func-warp {
    .el-form-item {
        flex-direction: column;
        align-items: flex-start !important;
    }
}
</style>