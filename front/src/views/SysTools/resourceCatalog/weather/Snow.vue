<!-- /*
 * @FileDescription: 天气仿真 -> 雪
 * @Author: DTS: zhangXue 
 * @Date: 2022-11-22 16:03:52 
 * @Last Modified by:   DTS: zhangXue 
 * @Last Modified time: 2022-11-22 16:03:52 
 */ -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            雪
            <!-- <span @click.stop="handleClose">
                    <i class="el-icon-close"></i>
                </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="weather-state">
            <span>开启下雪</span>
            <el-switch v-model="snowSetting" active-color="#13ce66" inactive-color="#979797" @change="changeOpen">
            </el-switch>
        </div>
        <el-form size="mini" class="func-warp" label-position="top">
            <el-form-item v-for="(item, index) in snowModel" :label="item.name" :key="index + item.name">
                <el-row :gutter="20">
                    <el-col :span="16">
                        <el-slider :min="item.min" :max="item.max" :step="item.step" v-model="item.value"
                            @change="((val: any) => { changeValue(val, item.type) })" :show-input="false"
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

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { CircleClose } from '@element-plus/icons-vue'

const emit = defineEmits(['close'])

const snowSetting = ref(window.sealAPI._weather.param['雪'].open)
const snowModel = reactive(window.sealAPI._weather.param['雪'].data)
const changeOpen = async function (value: any) {
    if (value) {
        window.sealAPI._weather.param['雪'].open = true;
        await window.sealAPI._weather.setSnowParam(
            snowModel[0].value,
            snowModel[1].value,
            snowModel[2].value
        );
    } else {
        window.sealAPI._weather.param['雪'].open = false;
        await window.sealAPI._weather.disableRainSnow();
    }
}
const changeValue = function (val: any, type: any) {
    if (type == 'strength') {
        window.sealAPI._weather.param['雪'].data[0].value = val;
    } else if (type == 'speed') {
        window.sealAPI._weather.param['雪'].data[1].value = val;
    } else if (type == 'size') {
        window.sealAPI._weather.param['雪'].data[2].value = val;
    }
    if (snowSetting.value) {
        changeOpen(true);
    }
}
const handleClose = function () {
    emit("close")
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