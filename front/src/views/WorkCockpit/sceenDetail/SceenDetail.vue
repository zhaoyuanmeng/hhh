<template>
  <!-- 资源列表 -->
  <ResourceList :isCocpit="true" v-if="testBol" />

  <div v-if="showSceenRightCard">
    <!-- 一级加强场景规划 -->
    <!-- <ScreenPlan v-if="taskItem.taskLevel === '一级加强'"/> -->

    <!-- 其他任务场景详情 -->
    <ScreenOtherPlan />
    <div class="viewReset" @click="viewResetFun">
      <div class="icon_name"></div>
    </div>
  </div>
  <!-- 底部工具条 -->
  <!-- <FunctionTmpl /> -->

  <!-- marker信息 -->
  <InfoDialog v-if="showMarkerInfo" />

    <!-- 涉及多个场景时，场景选择列表弹窗 -->
    <!-- <SceneDialog v-if="showSceneDialog" /> -->
    <!-- 矢量路网数据 -->
    <VectorRoadMap :styles="style"/>
</template>

<script setup>
import { keyBuilding } from "@/components/SmartMap/componenets/buildingExplode/markerEvent.js";
import VectorRoadMap from "@/components/vectorRoadMap"
import { sessionStorage } from "@/utils/storage";
import { computed, onMounted, watch, ref, nextTick } from "vue";
import ResourceList from "@/views/componenets/taskNew/ResourceList.vue";
import ScreenPlan from "@/views/componenets/taskNew/screenPlan.vue";
import ScreenOtherPlan from "@/views/componenets/taskNew/screenOtherPlan.vue";
import FunctionTmpl from "@/views/SysTools/resourceCatalog/FunctionTmpl.vue";
import InfoDialog from "@/views/WorkCockpit/taskDetail/components/InfoDialog.vue";
import { getRelatedFeatureDataOfTask } from "@/api/workCockpit/index.js";
import { drawScreenData } from "@/views/componenets/taskNew/util.js";
import { getTourDataForScreen } from "@/api/task/task";
import { getSceneDataForIdAndToDtaw } from "@/api/workCockpit/index.js";
import { drawMarkers, drawLinesName } from "../utils.js";
import { closeFloors } from "@/components/SmartMap/js/utils";
import SceneDialog from "@/views/WorkCockpit/taskDetail/components/SceneDialog.vue";
import {
  addCommunityMerchant,
  addScenePlacePoint,
  drawRoadLineMarker
} from "@/components/SmartMap/js/addMarkers";
import { drawTaskScenePointAndLine } from "@/components/SmartMap/js/resource";
import { hideMarker } from "@/components/SmartMap/js/map";

// store
import useScreenStore from "@/store/modules/screenStore";
import useFloorStore from "@/store/modules/floorStore";
import useTaskStore from "@/store/modules/taskStore";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
const WorkCockpitStore = useWorkCockpitStore();
const taskItem = computed(() => WorkCockpitStore.taskItem);
const eventMapData = computed(() => WorkCockpitStore.eventMapData);
const showMarkerInfo = computed(() => WorkCockpitStore.showMarkerInfo);
const showSceenRightCard = computed(() => WorkCockpitStore.showSceenRightCard);
const screenInfo = computed(() => useScreenStore().screenInfo);
const sceneList = computed(() => WorkCockpitStore.sceneList);
const openHouseBol = computed(()=>WorkCockpitStore.openHouseBol)
const showSceneDialog = computed(() => WorkCockpitStore.showSceneDialog);
const testBol = ref(false);
const style = ref({
  bottom:'100px',
  right:'340px'
})
// 获取场景下数据
const getSceneDetail = () => {
  // closeFloors();
  getTourDataForScreen({ id: eventMapData.value.GroupID }).then((res) => {
    testBol.value = true;
    useTaskStore().SET_SCREENMODALINFO(res.data);
    useScreenStore().set_screenInfo(res.data);
    useTaskStore().SET_SCREENPLANTITLE(
      res.data.type === "1" ? "路线" : res.data.type === "2" ? "现场" : "住地"
    );
    if (res.data.type === "1") {
      WorkCockpitStore.set_threePageType("roadLine");
      WorkCockpitStore.set_checkBox("line", true);
      WorkCockpitStore.set_checkBox("cordon", false);
    } else {
      WorkCockpitStore.set_threePageType("");
      WorkCockpitStore.set_checkBox("cordon", true);
    }
    if (res.data.viewData) {
      let g = window.__g;
      g.camera.set(res.data.viewData.camera, 2);
    }
    setTimeout(() => {
      drawTaskScenePointAndLine(WorkCockpitStore.taskSceneData, true);
      if(res.data.type === "1"&&res.data.startData&&res.data.startPointId){
      let newDatas = [{id: res.data.startPointId,
        sceneName: res.data.startData.title,
            coor: [Number(res.data.startData.jingdu),Number(res.data.startData.weidu)],
            type: res.data.startType==='xc'?'2':'3'},{ id: res.data.endPointId,
            sceneName: res.data.endData.title,
            coor: [Number(res.data.endData.jingdu),Number(res.data.endData.weidu)],
            type: res.data.endType==='xc'?'2':'3'}]
        // 方法1：使用filter过滤
        const filteredArray = newDatas.filter(item2 => 
          !WorkCockpitStore.taskSceneData.some(item1 => item1.sceneName === item2.sceneName)
        ) 
        if(filteredArray?.length){
          // 添加该条路线的起点和终点
          drawRoadLineMarker(filteredArray,res.data.viewData)
        }     
        let allDatas = [...filteredArray,WorkCockpitStore.taskSceneData]
        addScenePlacePoint(allDatas);
    }else{
      addScenePlacePoint(WorkCockpitStore.taskSceneData);
    }

      // addScenePlacePoint(WorkCockpitStore.taskSceneData);
      let names = eventMapData.value.UserData;
      if(sceneList.value.length>1||openHouseBol.value){
        if (
        names === "会展中心" ||
        names === "国酒会议中心" ||
        names === "国际酒店" ||
        names === "公务接待楼" ||
        names === "办公业态3#" ||
        names === "办公业态4#" ||
        names === "办公业态5#" ||
        names === "综合商业6#" ||
        names === "雄安站" ||
        names === "计算中心" ||
        names === "中国星网" ||
        names === "中国中化大厦" ||
        names === "中国电信" ||
        names === "规划展示中心" ||
        names === "雄安会展中心酒店"
      ) {
        keyBuilding(eventMapData.value.UserData);
      }
      let ids = sceneList.value.map(item=>item.sceneId)
      window.__g.marker.hide(ids);
      useFloorStore().set_floorsId(ids[0]);
      }
      // hideMarker({ eventtype: "CameraStopMove" });
      //     getSceneDataForIdAndToDtaw({sceneIds:eventMapData.value.GroupID}).then(res=>{
      //   if(res.data.basicData?.length){
      //     if(projectType!=='1'){
      //       drawMarkers(eventMapData.value.GroupID, eventMapData.value.UserData, res.data.basicData)
      //     }
      //   }
      //     if (res.data.drawLineData?.length) {
      //       if(projectType==='1'){
      //          // 绘制路线名称
      //           // drawLinesName(res.data.drawLineData)
      //       }
      //       sessionStorage.set("lineName", res.data.drawLineData);
      //   }
      // })
    }, 500);
  });
};

// 视角复位
const viewResetFun = () => {
  if (screenInfo.value.viewData) {
    let g = window.__g;
    g.camera.set(screenInfo.value.viewData.camera, 1.5);
  }
};

onMounted(() => {
  useTaskStore().SET_TASKINFO(taskItem.value);
  useScreenStore().set_editScreen(true);
  useFloorStore().set_isShowFloor(true);
  WorkCockpitStore.set_showPolicDialog("all", false);
  // setTimeout(()=>{
  getSceneDetail();
  // },500)
});
</script>

<style lang="scss" scoped>
.viewReset {
  position: absolute;
  bottom: 50px;
  right: 340px;
  z-index: 20;
  width: 42px;
  height: 42px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  // padding: 5px;
  cursor: pointer;
  border: 1px solid #5b759b;
  background: linear-gradient(
    180deg,
    rgba(10, 29, 100, 0.7) 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  // background: url("@/assets/basic/viewEye.png") no-repeat;
  // background-size: 100% 100%;
  .icon_name {
    width: 24px;
    height: 24px;
    background: url("@/assets/basic/容器@1x.png") no-repeat;
    background-size: 100% 100%;
  }
}
.viewReset:hover {
  background: rgba(38, 68, 173, 0.5);
  box-sizing: border-box;
  border: 0.61px solid rgba(38, 68, 173, 0.3);
}
</style>
