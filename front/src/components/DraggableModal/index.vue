<template>
  <div class="content-box">
    <div class="content-header">
      <span >城市路网</span>
      <!-- <el-icon class="close" @click="handleClose">
        <Close />
      </el-icon> -->
    </div>
    <div class="content">
      <slot></slot>
    </div>
  </div>

</template>

<script setup>
import { Close } from '@element-plus/icons-vue'
import { ref, onMounted ,computed,defineProps,watch,defineEmits} from 'vue'
const emit = defineEmits(['closeMap'])
onMounted(() => {
})
const handleClose = () => {
  emit('closeMap')
}
</script>

<style lang="scss" scoped>
.content-box {
  width: 300px;
  height: 450px;
  background: linear-gradient(180deg, #0A1D64 0%, rgba(21, 30, 73, 0.6984) 100%);
  position: absolute;
  left: 60%;
  top: 53%;
  z-index: 51;
  // transform: translate(-50%,-50%);
  transform: scale(0.8);

  .content-header {
    width: 100%;
    height: 48px;
    line-height: 48px;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    background: url("@/assets/workcockpit/header_bg.png") no-repeat;
    position: relative;

    .close {
      position: absolute;
      right: 12px;
      top: 8px;
      cursor: pointer;
    }
  }

  .content {
    height: calc(520px - 48px);
    padding: 20px;
    box-sizing: border-box;
    font-size: 14px;
    overflow: auto;
    position: relative;
  }
}
</style>