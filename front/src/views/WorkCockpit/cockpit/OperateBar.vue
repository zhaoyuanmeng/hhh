<template>
  <div class="content-box" :class="{ 'quit-width': !isFullScreen, 'full-width': isFullScreen }">
    <!-- <div class="bread-crumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>总览</el-breadcrumb-item>
      </el-breadcrumb>
    </div> -->
    <div class="operate-icon" :class="{ 'full-icon': !isFullScreen, 'quit-icon': isFullScreen }"
      @click="handleFullScreen">
    </div>
  </div>

</template>

<script setup>
import { ref ,onMounted} from 'vue'
import useWorkCockpitStore from '@/store/modules/workCockpit.js'

const WorkCockpitStore = useWorkCockpitStore()
const isFullScreen = ref(false)

// 全屏/推出全屏
const handleFullScreen = () => {
  isFullScreen.value = !isFullScreen.value
  WorkCockpitStore.setFullScreen(isFullScreen.value)
}

onMounted(()=>{

})

</script>

<style lang="scss" scoped>
.content-box {
  width: calc(100% - 640px - 50px);
  display: flex;
  justify-content:flex-end;
  position: absolute;
  top: 72px;
  z-index: 2;

  .bread-crumb {
    height: 42px;
    border-radius: 4px;
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 0px 16px;
    gap: 8px;
    font-size: 14px;
    background: rgba(14, 31, 96, 0.5);
    box-sizing: border-box;
    border: 1px solid #5B759B;
  }

  .full-icon {
    background: url('@/assets/workcockpit/full.png') no-repeat center;
  }

  .quit-icon {
    background: url('@/assets/workcockpit/quit.png') no-repeat center;
  }

  .operate-icon {
    width: 42px;
    height: 42px;
    border-radius: 4px;
    background-color: rgba(14, 31, 96, 0.5);
    border: 1px solid #5B759B;
    box-sizing: border-box;
    cursor: pointer;
  }
}

.full-width {
  width: calc(100% - 40px);
  left: 20px;
}

.quit-width {
  width: calc(100% - 640px - 50px);
  left: 350px;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #00CEFF;
}

:deep(.el-breadcrumb__inner) {
  color: #fff;
  cursor: pointer;
}

:deep(.el-breadcrumb__inner:hover) {
  color: #00CEFF;
}
</style>