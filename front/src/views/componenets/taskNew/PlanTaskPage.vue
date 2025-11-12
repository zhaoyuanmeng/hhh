<template>
  <!-- 资源列表 -->
  <ResourceList/>

    <!-- 一级加强场景规划 -->
    <!-- <ScreenPlan v-if="taskItem.taskLevel === '一级加强'"/> -->
  <div  v-if="showSceenRightCard">
     <!-- 其他任务场景详情 -->
     <ScreenOtherPlan />
  </div>
  <!-- marker信息 -->
  <InfoDialog v-if="showMarkerInfo" />

</template>

<script setup>
import { sessionStorage } from "@/utils/storage";
import { computed,onMounted, watch, ref } from 'vue'
import ResourceList from "@/views/componenets/taskNew/ResourceList.vue"
import ScreenPlan from "@/views/componenets/taskNew/screenPlan.vue"
import ScreenOtherPlan from "@/views/componenets/taskNew/planScene.vue"
import InfoDialog from '@/views/WorkCockpit/taskDetail/components/InfoDialog.vue'
import { drawScreenData,clearDraw } from '@/views/componenets/taskNew/util.js'
import { getTourDataForScreen} from "@/api/task/task"
import { getSceneDataForIdAndToDtaw } from '@/api/workCockpit/index.js'
import {drawMarkers,drawLinesName} from '@/views/WorkCockpit/utils'
import {closeFloors} from '@/components/SmartMap/js/utils'
import {addCommunityMerchant} from '@/components/SmartMap/js/addMarkers'

// store
import useScreenStore from "@/store/modules/screenStore"
import useFloorStore from "@/store/modules/floorStore"
import useTaskStore from "@/store/modules/taskStore"
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import usePlanTaskStore from "@/store/modules/planTask";
const WorkCockpitStore = useWorkCockpitStore()
const PlanTaskStore = usePlanTaskStore()
const taskItem = computed(() => WorkCockpitStore.taskItem)
const eventMapData=computed(()=>PlanTaskStore.planTaskInfo)
const showMarkerInfo = computed(() => WorkCockpitStore.showMarkerInfo)
const showSceenRightCard = computed(() => WorkCockpitStore.showSceenRightCard)


// 获取场景下数据
const getSceneDetail=async()=>{
  let g = window.__g
  getTourDataForScreen({id:eventMapData.value.id}).then(res=>{
    useTaskStore().SET_SCREENMODALINFO(res.data)
    useScreenStore().set_screenInfo(res.data)
    useTaskStore().SET_SCREENPLANTITLE(
      res.data.type === "1"||res.data.type === "4" ||res.data.type === "5"  ? "路线" : res.data.type === "2" ? "现场" : "住地"
    )
    if(res.data.type === "1"||res.data.type === "4" ||res.data.type === "5" ){
      WorkCockpitStore.set_threePageType('roadLine')
      WorkCockpitStore.set_checkBox('line',true)
      WorkCockpitStore.set_checkBox('cordon',false)
    }else{
      WorkCockpitStore.set_threePageType('')
      WorkCockpitStore.set_checkBox('cordon',true)
    }
    if(res.data.viewData){
      let g = window.__g
      g.camera.set(res.data.viewData.camera, 1)
    }
    let projectType = res.data.type
    // drawScreenData(res.data.drawDataList)
    getSceneDataForIdAndToDtaw({sceneIds:eventMapData.value.id}).then(res=>{
      if(eventMapData.value.type!=='1'&&eventMapData.value.type!=='4'&&eventMapData.value.type!=='5'){
        if(res.data.basicData?.length){
        drawMarkers(eventMapData.value.id, res.data.basicData[0].name, res.data.basicData)
    }
    // if(res.data.refPointData&&res.data.refPointData?.length){
    //     for(let point of res.data.refPointData){
    //       let arrs = [{id:point.id,name:point.name,type:'现场基本情况',geojson:{coordinates:[Number(point.x),Number(point.y),Number(point.z)]}}]
    //       drawMarkers(eventMapData.value.id, point.name, arrs)
    //     }
    //   }
    }
      if (res.data.drawLineData?.length) {
        if(projectType==='1'||projectType==='4'||projectType==='5'){
           // 绘制路线名称
            // drawLinesName(res.data.drawLineData)
        }
        sessionStorage.set("lineName", res.data.drawLineData);
    }
  })
  })
  addCommunityMerchant()
}


onMounted(()=>{
  // useTaskStore().SET_TASKINFO(eventMapData.value)
  useScreenStore().set_editScreen(true)
  useFloorStore().set_isShowFloor(true)
  setTimeout(()=>{
    getSceneDetail()
  },1000)
})
</script>

<style lang="scss" scoped></style>