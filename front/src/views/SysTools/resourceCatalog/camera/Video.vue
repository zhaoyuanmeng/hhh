<template>
    <div></div>
</template>
<script lang="ts" setup>
import { onBeforeUnmount, onMounted } from 'vue';
import { getCameraByCode, getCameraIndexCode } from '@/api/dataApi'

onMounted(() => {
    __g.camera.set(
        494922.15875, 4326296.8475, 17.464635, -41.973946, 107.347252, 4
    )
    // setVideo()
})

const setVideo = async () => {
    getCameraIndexCode({
        cameraIndexCode: '13310206041320302862',
        protocol: 'rtsp'
    }).then(async (res) => {
        console.log(res, 'getCameraByCodegetCameraByCodegetCameraByCode');
        await __g.videoProjection.delete('vp1')
        let o = {
            id: 'vp1',
            videoURL: res.data.url, //视频地址
            location: [494926.460781, 4326296.77375, 7.59302],
            rotation: [-19.485655, 112.732587, 0],
            fov: 75, //垂直夹角
            aspectRatio: 1.65, //纵横比
            distance: 40, //距离
            depthCulling: false, //是否背面剔除 即背面不显示投影
            frustumVisible: true, //是否显示投影线框
            frustumColor: [1, 1, 1, 1] //投影线框颜色
        }
        const result = await __g.videoProjection.add(o)
        await __g.videoProjection.focus(o.id, 5)
    })

}

onBeforeUnmount(async () => {
    await __g.videoProjection.delete('vp1')
})
</script>
<style></style>