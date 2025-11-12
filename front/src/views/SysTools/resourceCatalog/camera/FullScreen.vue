<template>
    <div>
    </div>
</template>
<script lang="ts" setup>
import { onBeforeUnmount, onMounted } from 'vue';

onMounted(() => {
    const app = document.getElementById('app')
    launchFullScreen(app)
})
onBeforeUnmount(() => {

})

const launchFullScreen = function (element: any) {
    if (element.requestFullscreen) {
        element.requestFullscreen()
    } else if (element.mozRequestFullScreen) {
        element.mozRequestFullScreen()
    } else if (element.webkitRequestFullscreen) {
        element.webkitRequestFullscreen()
    } else if (element.msRequestFullscreen) {
        element.msRequestFullscreen()
    }
}
</script>
<style></style>