<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-19 10:06:18
 * @LastEditTime: 2024-06-20 09:00:13
 * @LastEditors: Alex
-->
<template>
  <div>
    <!-- 功能模板入口 -->
    <FunctionTmpl ref="view"/>
  </div>
</template>
<script setup>
import { onMounted, ref } from "vue";
import FunctionTmpl from "./resourceCatalog/FunctionTmpl.vue";
import SealAirCityAPI from "./resourceCatalog/SealAirCityAPI.js";
const view = ref('view')
onMounted(() => {
  window.sealAPI = new SealAirCityAPI();
  window.sealAPI.onReady();
});
</script>
<style lang="scss" scoped>
.pry {
  position: relative;
  font-size: 14px;
  // 选中
  &.active {
    & > div:not(.sec) {
      background: linear-gradient(
        180deg,
        rgba(80, 109, 155, 0.53) 0%,
        rgba(44, 60, 80, 0.9) 100%
      );
      box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5),
        0px 11px 17px -11px rgba(7, 8, 15, 0.95);
      filter: blur(0px);

      span {
        color: #48ebff;
      }
    }

    .sec {
      display: flex !important;
    }
  }

  & > div:not(.sec) {
    position: relative;
    min-width: 78px;
    padding: 10px 0;
    border-radius: 12px;
    cursor: pointer;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  img {
    width: 32px;
    height: 32px;
    margin-bottom: 10px;
  }
}
</style>
