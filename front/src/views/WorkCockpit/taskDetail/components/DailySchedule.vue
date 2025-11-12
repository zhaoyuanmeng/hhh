<template>
  <div class="content"  style="padding-top: 20px;">
    <el-scrollbar height="70vh">
          <el-timeline>
            <el-timeline-item
              v-for="item in dataInfo?.timeline"
              :timestamp="item.time"
              placement="top"
            >
              <template #dot>
                <img :src="Circle" alt="icon" />
              </template>
              <div class="line-item" v-for="el in item.data" :key="el.id">
                <span class="time">{{ el.time }}</span>
                <span cladd="addr" style="cursor: pointer;" @click="taskProcess(el)" :class="{'selectActive':activeVal===el.id}">{{ el.desc }}</span>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-scrollbar>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted, onBeforeUnmount } from "vue";
import { loadPicture } from "@/utils"
import Circle from "@/assets/workcockpit/circle.png";
import { hideMarker } from "@/components/SmartMap/js/map";
import {
  getDailyScheduleOfTask,
  getSceneDataForIdAndToDtaw,
} from "@/api/workCockpit/index.js";
import {
  drawTaskScenePointAndLine,
  drawLinesAndeMarker,
  drawTaskMarkers,
  drawTaskLines,
  drawLinesName
} from "../../utils";
// store
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import emitter from "@/utils/emitter";
import {addCommunityMerchant} from '@/components/SmartMap/js/addMarkers'
const WorkCockpitStore = useWorkCockpitStore();
const taskItem = computed(() => WorkCockpitStore.taskItem);
const taskSceneInfo = computed(() => WorkCockpitStore.taskSceneData);
const roadDrawList = computed(() => WorkCockpitStore.roadDrawList);
const siteDrawList = computed(() => WorkCockpitStore.siteDrawList);
const residenceDrawList = computed(() => WorkCockpitStore.residenceDrawList);
let selectTab = computed(()=>WorkCockpitStore.tabsTwo)
const activeTab = ref("rcap");
const activeVal = ref('')
let lineTimer = ref(null);
let showFlag = ref(true)
const dataInfo = ref({});
const getTimeLineData = () => {
  getDailyScheduleOfTask({ taskId: taskItem.value.id }).then((res) => {
    dataInfo.value = res.data || {};
  });
};

// 任务安排点击事件
const taskProcess = async (item) => {
  if(lineTimer.value){
    clearInterval(lineTimer.value)
    lineTimer.value = null
    showFlag.value = true
  }
  // 每次点击前都恢复原来样子
  cancleHeightLight(taskSceneInfo.value).then(()=>{
    if(activeVal.value===item.id){
    activeVal.value = ''
    return
  }
  activeVal.value = item.id
    if(item.type==='1'){
    roadClickNew(item)
    }else{
      // handleClickDataNew(item)
      crolMarkerShowHide(item.id)
    }
  })
 
}
const handleClickDataNew = async (item) => {
  let g = window.__g;
  // 绘制点击的
  const list =
  item.type === "2" ? siteDrawList.value : residenceDrawList.value;
  const dataItem = list.find(
    (el) => el.sceneInfo.id === item.id
  );
  dataItem && drawTaskMarkersNew([dataItem]);
}
const drawTaskMarkersNew = (arr) => {
  if (arr && arr.length > 0) {
    for (const item of arr) {
      if (item.basicDataList?.length) {
        drawMarkersChange(item.basicDataList)
      }
    }
  }
} 
// 取消高亮
const cancleHeightLight = async(arr) => {
  let g = window.__g
  if (arr && arr.length > 0) {
    for (const item of arr) {
      if(item.type==='2'||item.type==='3'){
         // 还原驻地和现场
      // if (item.pointInfoList?.length) {
      //   let markerarr = item.pointInfoList.map(item=>item.id)
      //   g.marker.show(markerarr)
      // }
      let markerarr = arr.map(item=>item.id)
      g.marker.show(markerarr)
      }else{
        // 还原线
      if (item.drawInfoList?.length) {
        let ids = item.drawInfoList.map(item=>item.id)
        g.polyline.show(ids)
        g.marker.showByGroupId(item.id)
      }
      }

    }
  }
  hideMarker({eventtype:'CameraStopMove'})
}
const drawMarkersChange = (arr) => {
  let g = window.__g
  crolMarkerShowHide(arr[0].id)
}

// 点击路线
const roadClickNew = async (item, index) => {
  getDataToDrawNew(item);
}

const getDataToDrawNew = (item) => {
  getSceneDataForIdAndToDtaw({ sceneIds: item.id }).then((res) => {
      crolLineShowHide(res.data.drawLineData,item.id)
  });
}
const crolLineShowHide = async (arr,groupId) => {
  lineTimer.value = setInterval(() => {
      if(showFlag.value){
        showLines(arr,groupId)
      }else{
        hideLines(arr,groupId)
      }
    }, 500);
}
const crolMarkerShowHide = async (id) => {
  lineTimer.value = setInterval(() => {
      if(showFlag.value){
        showMrkers(id)
      }else{
        hideMarkers(id)
      }
    }, 500);
}
const showLines = async(arr,id) => {
  let g = window.__g
  let ids = arr.map(item=>item.id)
 await g.polyline.show(ids)
 await g.marker.showByGroupId(id)
 showFlag.value = false
}
const hideLines = async(arr,id) => {
  let g = window.__g
  let ids = arr.map(item=>item.id)
  await g.polyline.hide(ids)
  await g.marker.hideByGroupId(id)
  showFlag.value = true
}
const showMrkers = async(id) => {
  let g = window.__g
 await g.marker.show(id)
 showFlag.value = false
}
const hideMarkers = async(id) => {
  let g = window.__g
  await g.marker.hide(id)
  showFlag.value = true
}

onMounted(() => {
  if(lineTimer.value){
    clearInterval(lineTimer.value);
    lineTimer.value = null;
  }
  getTimeLineData();
});
onUnmounted(()=>{
  if(lineTimer.value){
    clearInterval(lineTimer.value);
    lineTimer.value = null;
  }
  emitter.off("clearTimeInit");
})
emitter.on("clearTimeInit", () => {
  if(lineTimer.value){
    clearInterval(lineTimer.value);
    lineTimer.value = null;
  }
   activeVal.value = ''
  
});
onBeforeUnmount(()=>{
  if(lineTimer.value){
    clearInterval(lineTimer.value);
    lineTimer.value = null;
  }
  emitter.off("clearTimeInit");
})
</script>

<style lang="scss" scoped>
.content {
  font-size: 14px;
  display: flex;
  flex-direction: column;
  gap: 12px;

  .time-mark {
    color: #00ceff;
    font-weight: bold;
    margin: 12px 0 8px;
  }

  .line-item {
    color: #fff;
    font-size: 14px;
    line-height: normal;
    margin-bottom: 8px;

    .time {
      color: #00ceff;
      margin-right: 18px;
    }
  }

  .info-total {
    padding: 12px;
    background: rgba(0, 0, 0, 0.1);
    line-height: 26px;

    .total {
      color: #00ceff;
      font-weight: bold;
    }
  }

  .info-path {
    background: rgba(0, 0, 0, 0.1);
    padding: 12px;
    box-sizing: border-box;
    border: 1px solid rgba(38, 68, 173, 0.3);
    line-height: 24px;
    margin-top: 12px;
    cursor: pointer;

    &:hover {
      background: rgba(0, 123, 255, 0.302);
      box-shadow: inset 0px 0px 8px 0px #007bff;
    }

    .title {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 4px;
    }
  }

  .info-item {
    background: rgba(0, 0, 0, 0.1);
    padding: 16px 20px;
    box-sizing: border-box;
    border: 1px solid #182659;
    line-height: 24px;
    margin-top: 12px;
    cursor: pointer;

    &:hover {
      background: rgba(0, 123, 255, 0.302);
      box-shadow: inset 0px 0px 8px 0px #007bff;
    }

    .date-time {
      display: flex;
      align-items: center;
      gap: 12px;
      padding-left: 16px;

      .date {
        color: #00ceff;
        font-size: 14px;
        font-weight: bold;
      }
    }

    .addr {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 12px;
    }
   
  }

  .active {
    background: rgba(0, 123, 255, 0.302);
    box-shadow: inset 0px 0px 8px 0px #007bff;
  }

  .color-enhance {
    color: #ff4040;
  }

  .color-level-one {
    color: #ff6633;
  }

  .color-level-two {
    color: #ffe433;
  }

  .color-level-three {
    color: #33ff77;
  }

  .color-level-secure {
    color: #00a6ff;
  }
}

:deep(.el-timeline-item__timestamp) {
  font-size: 18px;
  font-weight: bold;
  color: #00ceff;
}

:deep(.el-timeline-item__tail) {
  border-left: 4px solid #0d1a41;
}

:deep(.el-timeline-item__dot) {
  width: 14px;
  height: 14px;
}
.selectActive{
      color: #00ceff;
  }
</style>
