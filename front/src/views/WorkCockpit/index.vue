<template>
  <!-- 驾驶舱 -->
  <div v-if="!isFullScreen && curPage === 'overview'&&loadShow">
    <BasicData v-if="!isFullScreen && curPage === 'overview'&&loadShow"/>
    <LeaderShip />
    <TaskView />
    <PlanView/>
    <ProblemList/>
    <VideoPlay v-if="showPlayVideo"/>
  </div>
  <!-- 任务总览查看页面 -->
  <div v-if="curPage === 'taskDetail'">
    <TaskDetail />
  </div>

  <!-- 场景页面 -->
  <div v-if="curPage === 'sceenDetail'">
    <SceenDetail />
  </div>

  <!-- 任务漫游 -->
  <div v-if="curPage === 'taskPlay'">
    <TaskPlay  @out="cleaOut" />
  </div>

  <!-- 面包屑/全屏操作，共用一个交互麻烦 -->
  <OperateBar v-if="curPage === 'overview'" />
  <OperateBarTask v-if="curPage === 'taskDetail'" />
  <OperateBarSceen v-if="curPage === 'sceenDetail'" />


</template>

<script setup>
import { computed, onMounted ,nextTick,onBeforeUnmount} from 'vue'
// 驾驶舱页面组件
import LeaderShip from './cockpit/LeaderShip.vue'
import BasicData from './cockpit/BasicData.vue'
import TaskView from './cockpit/TaskView.vue'
import OperateBar from './cockpit/OperateBar.vue'
import PlanView from './cockpit/PlanView.vue' // 常备方案总览页面
import VideoPlay from './cockpit/videoPlay.vue'
import ProblemList from './cockpit/ProblemList.vue'
// 任务总览查看页面组件
import TaskDetail from './taskDetail/TaskDetail.vue'
import OperateBarTask from './taskDetail/components/OperateBarTask.vue'
// 场景
import SceenDetail from './sceenDetail/SceenDetail.vue'
import OperateBarSceen from './sceenDetail/components/OperateBarSceen.vue'
import TaskPlay from "@/views/componenets/taskNew/taskPlay.vue"

// store
import useSettingStore from "@/store/modules/settingStore";
import useScreenStore from "@/store/modules/screenStore"
import useFloorStore from "@/store/modules/floorStore"
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const WorkCockpitStore = useWorkCockpitStore()
const isFullScreen = computed(() => WorkCockpitStore.isFullScreen)
const showPlayVideo = computed(()=>WorkCockpitStore.showPlayVideo)
const curPage = computed(() => WorkCockpitStore.curPage)
const curTourId = computed(()=>WorkCockpitStore.curTourId)
const loadShow = computed(()=>WorkCockpitStore.loadStatus)

// 漫游页面退回
const cleaOut = () => {
  WorkCockpitStore.setCancelData({});
  if(curTourId.value){
    useSettingStore().setShowTool(true);
    WorkCockpitStore.setCurPage('sceenDetail')
    WorkCockpitStore.set_showResouce(false)
    WorkCockpitStore.set_checkBox('init',false)
  }else{
  WorkCockpitStore.set_tabsOne("rcap");
  WorkCockpitStore.set_tabsTwo("");
  WorkCockpitStore.setCurPage('taskDetail')
  WorkCockpitStore.setCurTourId('')
  }
  
}
onMounted(()=>{
  WorkCockpitStore.setCurPage('overview')
  useScreenStore().set_editScreen(false)
  useFloorStore().set_isShowFloor(false)
  WorkCockpitStore.setTaskPlanNode('')
  nextTick(()=>{
    let g = window.__g
    g.reset(4)
  })
})

onBeforeUnmount(() => {
})

</script>

<style lang="scss" scoped>

</style>