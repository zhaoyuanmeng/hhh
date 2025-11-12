<template>
    <div></div>
</template>
<script lang="ts" setup>
import { onBeforeUnmount, onMounted } from 'vue';

onMounted(() => {
    __g.settings.setCampassVisible(true)
})

onBeforeUnmount(() => {
    __g.settings.setCampassVisible(false)

})

</script>
<style></style>