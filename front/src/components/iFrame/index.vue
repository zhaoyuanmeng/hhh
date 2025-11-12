<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-11-08 15:13:08
 * @LastEditTime: 2024-11-15 11:24:14
 * @LastEditors: Alex
-->
<template>
  <div v-loading="loading" style="width: 100%; height: 100%">
    <iframe
      :src="url"
      frameborder="0"
      style="width: 100%; height: 100%"
      scrolling="auto"
    />
  </div>
</template>

<script setup>
import { ref, computed, defineProps, onMounted } from 'vue'
const props = defineProps({
  src: {
    type: String,
    required: true
  }
})

const height = ref(document.documentElement.clientHeight - 94.5 + 'px;')
const loading = ref(true)
const url = computed(() => props.src)

onMounted(() => {
  setTimeout(() => {
    loading.value = false
  }, 300)
  window.onresize = function temp() {
    height.value = document.documentElement.clientHeight - 94.5 + 'px;'
  }
})
</script>
