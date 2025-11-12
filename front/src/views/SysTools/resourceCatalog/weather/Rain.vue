<!-- /*
 * @FileDescription: 天气仿真 -> 雨
 * @Author: DTS: zhangXue 
 * @Date: 2022-11-22 16:03:52 
 * @Last Modified by:   DTS: zhangXue 
 * @Last Modified time: 2022-11-22 16:03:52 
 */ -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            雨
            <!-- <span @click.stop="handleClose">
                    <i class="el-icon-close"></i>
                </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="weather-state">
            <span>开启下雨</span>
            <el-switch v-model="rainSetting" active-color="#13ce66" inactive-color="#979797" @change="changeOpen">
            </el-switch>
        </div>
        <el-form size="mini" label-position="top" class="func-warp">
            <el-form-item v-for="(item, index) in rainModel" :label="item.name" :key="index + item.name">
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

const rainSetting = ref(window.sealAPI._weather.param['雨'].open)
const rainModel = reactive(window.sealAPI._weather.param['雨'].data)

const changeOpen = async function (value: any) {
    if (value) {
        window.sealAPI._weather.param['雨'].open = true;
        const res = await window.sealAPI._weather.setRainParam(
            rainModel[0].value,
            rainModel[1].value,
            rainModel[2].value
        )
    } else {
        window.sealAPI._weather.param['雨'].open = false;
        await window.sealAPI._weather.disableRainSnow();
    }
}
const changeValue = function (val: any, type: any) {
    if (type == 'strength') {
        window.sealAPI._weather.param['雨'].data[0].value = val;
    } else if (type == 'speed') {
        window.sealAPI._weather.param['雨'].data[1].value = val;
    } else if (type == 'size') {
        window.sealAPI._weather.param['雨'].data[2].value = val;
    }
    if (rainSetting.value) {
        changeOpen(true);
    }
}
const handleClose = function () {
    emit('close')
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