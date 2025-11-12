<!--
 * @FileDescription: 暗黑模式
 * @Author: yuanhaijun
 * @Date: 2023.03.16
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.03.16
 -->
<template>
    <div class="cloud-func">
        <div class="func-title">
            暗黑模式
            <!-- <span @click.stop="handleClose">
                        <i class="el-icon-close"></i>
                    </span> -->
            <el-icon class="" @click.stop="handleClose">
                <CircleClose />
            </el-icon>
        </div>
        <div class="weather-state">
            <span>开启暗黑模式</span>
            <el-switch v-model="isDark" active-color="#13ce66" inactive-color="#979797" @change="handleChange">
            </el-switch>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { onBeforeUnmount, ref } from 'vue';
import { CircleClose } from '@element-plus/icons-vue'

const emit = defineEmits(['close'])

const isDark = ref(window.sealAPI._weather.isDark)

const handleChange = function () {
    window.sealAPI._weather.setDarkMode_biz(isDark.value);
}
const handleClose = function () {
    emit('close')
}

onBeforeUnmount(async () => {
    await handleClose();
})


</script>

<style lang="scss" scoped>
@import "~@/assets/css/func3.scss";

.weather-state {
    display: flex;
    align-items: center;
    min-height: 80px;
    padding: 20px 24px;
    font-size: 14px;

    span {
        margin-right: 20px;
    }
}
</style>