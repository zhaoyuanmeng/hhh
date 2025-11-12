<!-- 
 * @FileDescription: 空间测量 measurement
 * @Author: DTS: zhangXue 
 * @Date: 2022-11-22 10:43:07 
 * @Last Modified by: DTS: zhangXue
 * @Last Modified time: 2022-11-22 10:43:48
 *
-->
<template>
    <div></div>
</template>

<script setup>
import { onMounted, defineProps, onBeforeUnmount } from 'vue'
const props = defineProps({
    cmptData: {
        type: Object,
        default: () => {
            return {
                mode: '' // 测量工具类型
            }
        }
    }
})
const emit = defineEmits(["close"])
onMounted(() => {
    startMeasurement(props.cmptData);
})
const startMeasurement = async function (cmptData) {
    await window.sealAPI._tools.setMeasurement_biz(cmptData.mode);
}
const cancelMeasurement = async function () {
    await window.sealAPI._tools.stopMeasurement_biz();
}
// const handleClose = function () {
//     if (this.$parent.hasOwnProperty("funcCMPT")) this.$parent.closeCMPT();
// }
onBeforeUnmount(async () => {
    await cancelMeasurement();
    emit("close")
})
</script>

<style lang="scss" scoped></style>