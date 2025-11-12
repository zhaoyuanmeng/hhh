<template>
    <div>

    </div>
</template>
<script lang="ts" setup>
import { useToolsStore } from '@/stores/tools'
import { onBeforeUnmount, onMounted } from 'vue';
const ToolsStore = useToolsStore()


onMounted(() => {
    ToolsStore.SetAnimationShow(true)
})

onBeforeUnmount(() => {
    ToolsStore.SetAnimationShow(false)
})
</script>
<style></style>