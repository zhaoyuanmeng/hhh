<template>
  <div class="content-box" :class="{ 'quit-width': showTaskDetail, 'full-width': !showTaskDetail }">
    <div class="bread-crumb">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item @click="changeContent('overview')">总览</el-breadcrumb-item>
        <el-breadcrumb-item @click="toTaskView" class="cursor">{{ taskItem.taskName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="operate-icon" :class="{ 'full-icon': showTaskDetail, 'quit-icon': !showTaskDetail }"
      @click="handleFullScreen">
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import useBasicStore from "@/store/modules/basicData";
import { clearDraw } from '@/views/componenets/taskNew/util.js'
import {closeFloors} from '@/components/SmartMap/js/utils'
import {addCommunityMerchant} from '@/components/SmartMap/js/addMarkers'
import { drawGsGtData } from "@/components/SmartMap/js/utils";
// store
import useFloorStore from '@/store/modules/floorStore'
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const WorkCockpitStore = useWorkCockpitStore()
const taskItem = computed(() => WorkCockpitStore.taskItem)
const showTaskDetail = ref(true)

// 全屏/推出全屏
const handleFullScreen = () => {
  showTaskDetail.value = !showTaskDetail.value
  WorkCockpitStore.setShowTaskDetail(showTaskDetail.value)
  !showTaskDetail.value && WorkCockpitStore.setTaskPlanNode('')
}

// 面包屑切换页面
const changeContent = async(page) => {
  closeFloors()
  let g = window.__g
   g.reset(4)
    g.polyline.clear() // 清除线
     g.polygon.clear();// 清除面
     g.tag.clear();// 清除标签
     g.marker.clear();// 清除marker
     g.marker3d.clear();
     g.radiationPoint.clear(); // 清除所有辐射点位样式
    g.polygon3d.clear();// 清除警戒线
    g.customObject.clear();
    g.markerLayer.clear();// 清除markerLayer 
    g.infoTree.show('8C6F67F84F71F11130C1BEACD0BAF612')
    g.infoTree.show('A3B755BF43736C15455300A0FB44761F')
    g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
    drawGsGtData(1)
  WorkCockpitStore.set_tabsOne('rcap')
  WorkCockpitStore.set_tabsTwo('')
  WorkCockpitStore.setCurPage(page)
  WorkCockpitStore.setShowTaskDetail(true)
  WorkCockpitStore.setTaskPlanNode('')
  WorkCockpitStore.setShowSceneDialog(false)
  useFloorStore().setFloornumberShow(false)
  useBasicStore().set_roadMap(false)
  addCommunityMerchant()
}

// 点击任务名称切换到任务保存的视角
const toTaskView=()=>{
  useBasicStore().set_roadMap(false)
  if(taskItem.value.viewData){
    let g = window.__g
    g.camera.set(taskItem.value.viewData.camera, 2)
  }
}

</script>

<style lang="scss" scoped>
.content-box {
  display: flex;
  justify-content: space-between;
  position: absolute;
  top: 72px;
  left: 160px;
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
    border: 1px solid #5B759B;
    box-sizing: border-box;
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
    box-sizing: border-box;
    border: 1px solid #5B759B;
    cursor: pointer;
  }
}

.full-width {
  width: calc(100% - 160px - 12px);
}

.quit-width {
  width: calc(100% - 160px - 342px);
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #00CEFF;
  cursor: pointer;
}

:deep(.el-breadcrumb__inner) {
  color: #fff;
  cursor: pointer;
}

:deep(.el-breadcrumb__inner:hover) {
  color: #00CEFF;
}
</style>