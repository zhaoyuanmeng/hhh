<!-- /*
 * @FileDescription: 天气仿真 -> 雾
 * @Author: DTS: zhangXue 
 * @Date: 2022-11-22 16:03:52 
 * @Last Modified by:   DTS: zhangXue 
 * @Last Modified time: 2022-11-22 16:03:52 
 */ -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            雾
            <!-- <span @click.stop="handleClose">
                    <i class="el-icon-close"></i>
                </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="weather-state">
            <span>开启雾</span>
            <el-switch v-model="fogSetting" active-color="#13ce66" inactive-color="#979797" @change="changeOpen">
            </el-switch>
        </div>
        <el-form size="mini" class="func-warp" label-position="top">
            <el-form-item v-for="(item, index) in fogModel" :label="item.name" :key="index + item.name">
                <el-row :gutter="20">
                    <el-col :span="16">
                        <el-slider :min="item.min" :max="item.max" :step="item.step" v-model="item.value"
                            @change="((val) => { changeValue(val, item.type) })" :show-input="false"
                            :show-input-controls="false"></el-slider>
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
    name: 'Fog',
    components: {
        CircleClose
    },
    emit: [],
    data() {
        return {
            fogSetting: window.sealAPI._weather.param['雾'].open,
            fogModel: window.sealAPI._weather.param['雾'].data
        }
    },
    created() { },
    mounted() { },
    methods: {
        async changeOpen(value) {
            if (value) {
                window.sealAPI._weather.param['雾'].open = true;
                await window.sealAPI._weather.setFogParam(
                    this.fogModel[0].value,
                    this.fogModel[1].value,
                    this.fogModel[2].value
                );
            } else {
                window.sealAPI._weather.param['雾'].open = false;
                await window.sealAPI._weather.setFogParam(0, 0, 0);
            }
        },
        changeValue(val, type) {
            if (type == 'fogDensity') {
                window.sealAPI._weather.param['雾'].data[0].value = val;
            } else if (type == 'fogGroundDensity') {
                window.sealAPI._weather.param['雾'].data[1].value = val;
            } else if (type == 'fogHeight') {
                window.sealAPI._weather.param['雾'].data[2].value = val;
            }
            if (this.fogSetting) {
                this.changeOpen(true);
            }
        },
        handleClose() {
            // if (this.$parent.hasOwnProperty('funcCMPT')) this.$parent.closeCMPT();
            this.$emit('close')
        }
    },
    async onBeforeUnmount() {
        await this.handleClose();
    }
}
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