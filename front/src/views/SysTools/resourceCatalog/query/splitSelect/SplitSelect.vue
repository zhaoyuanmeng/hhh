<template>
    <div></div>
</template>
<script lang="ts" setup>
import { onBeforeUnmount, onMounted } from 'vue'
import { useDialogStore } from '@/stores/dialog'
const dialogStore = useDialogStore()
onMounted(() => {
    dialogStore.setPlayer2Flag(true)
    // 开启多视口
    //视口布局类型，取值范围：[1~7]
    let viewportMode = 1
    //可选参数，激活后视口边框线的颜色
    let lineColor = '#DEA309'
    //可选参数，激活后视口边框线的宽度，单位：像素px
    let lineSize = 2
    window.player1Obj.misc.enterMultiViewportMode(viewportMode, lineColor, lineSize)
    //多视口模式下设置相机是否同步
    window.player1Obj.misc.setMultiviewportInteractSync(true)
})
onBeforeUnmount(async () => {
    dialogStore.setPlayer2Flag(false)
    __g.settings.setMainUIVisibility(false)
    __g.settings.setCampassVisible(false);

    // 退出多视口
    window.player1Obj.misc.exitMultiViewportMode()
    // 隐藏所有图层
    let hideArr: any = []
    // window.YXDataList.forEach((item: any) => {
    //     hideArr.push(item.value)
    // })
    window.player1Obj.infoTree.hide(hideArr)
})
</script>
<style></style>