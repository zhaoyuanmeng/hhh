<template>
   <!-- 导览列表 -->
   <cameraTourTmpl v-if="showTour" />

</template>

<script setup>
import { computed,onMounted, watch, ref } from 'vue'
import cameraTourTmpl from "./CameraTour.vue";
// store
import useTaskStore from "@/store/modules/taskStore"
const showTour = computed(() => useTaskStore().isShowFrames);

onMounted(()=>{
})
</script>

<style lang="scss" scoped></style>