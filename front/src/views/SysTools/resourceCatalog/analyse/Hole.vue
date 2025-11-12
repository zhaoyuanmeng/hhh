<!--
 * @FileDescription: 辅助分析->挖洞
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
    <div></div>
    <!-- 操作按钮 -->
    <ActionButtonTmpl v-if="editHelper.flag" :editHelper="editHelper" />
</template>

<script lang="ts" setup>
import { ElMessage } from 'element-plus';
import { onBeforeUnmount, onMounted, defineEmits, ref } from 'vue'
import ActionButtonTmpl from '../common/ActionButton.tmpl.vue'
const emit = defineEmits(['close'])

onMounted(async () => {
    startDraw();
    ElMessage({
        message: "请单击绘制多边形，双击结束绘制",
        type: 'success'
    })
})

const editHelper = ref({
    flag: false,
    type: "shape"
})
const fModel = ref({
    type: "",
    coords: [] as Array<Array<number>>,
    buffer: 0,
    tableName: "", // 基础数据
})



// 获取编辑助手返回数据
const getEditDate = function (coordinates: Array<Array<number>>) {
    editHelper.value.flag = false
    fModel.value.coords = coordinates

}
// 关闭编辑助手
const closeEditHelper = function () {
    // this.$set(this.editHelper, 'flag', false);
    editHelper.value.flag = false
}

// 开始绘制
const startDraw = async function () {
    await window.sealAPI._editHelper.drawParam(10);
}
// 绘制结束
const cancelDraw = async function () {
    window.sealAPI._editHelper.cancelDraw();
    window.sealAPI._tools.stopPolygonClip_biz();
}
const handleClose = async function () {
    emit('close')
}
onBeforeUnmount(async () => {
    await cancelDraw();
    await handleClose();
})
</script>

<style lang="scss" scoped></style>