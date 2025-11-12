<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-19 14:48:00
 * @LastEditTime: 2024-06-19 19:17:09
 * @LastEditors: Alex
-->
<template>
  <svg :class="svgClass" v-bind="$attrs" :style="{ color: color }">
    <use :xlink:href="iconName" rel="external nofollow"   />
  </svg>
</template>
<script setup>
import { defineProps, computed } from "vue"
const props = defineProps({
  iconClass: {
    type: String,
    required: true
  },
  color: {
    type: String,
    default: ""
  },
  className: {
      type: String,
      default: "icon",
    },
})
const iconName = computed(() => `#icon-${props.iconClass}`)
const svgClass = computed(() => {
  if (props.name) {
     return `svg-icon ${iconName.value}`
  }
  return "svg-icon"
})
</script>
<style scoped>
.svg-icon {
  width: 1em;
  height: 1em;
  fill: currentColor;
  vertical-align: middle;
}
</style>
