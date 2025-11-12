<template>
  <div class="content-wraper" v-if="showTaskDetail">
    <div class="content-header">
      <div @click="goBack" class="back_modal"></div>
      任务详情
    </div>
    <div class="content-box">
      <div class="task-info-box">
        <p class="task-name">{{ taskItem.taskName }}</p>
        <p>
          任务等级：<b
            :class="`task-level ${levelColorMap[taskItem.taskLevel]}`"
            >{{ taskItem.taskLevel }}</b
          >
        </p>
        <p>开始时间：{{ taskItem.taskStartTime }}</p>
        <p>结束时间：{{ taskItem.taskEndTime }}</p>
        <div class="task-roam" @click="handleTaskPlay">
          <img src="@/assets/workcockpit/roam_icon.png" alt="" />
          <span>任务漫游</span>
        </div>
      </div>
      <div class="btn-group-box">
        <div
          class="btn"
          v-for="item in btnGroup"
          :key="item.key"
          :class="{ active: curBtn === item.key }"
          @click="changeContent(item)"
        >
          {{ item.label }}
        </div>
      </div>
      <div class="info-box">
        <!-- <component :is="btnGroup.find(item => item.key === curBtn).component" /> -->
        <component :is="curComponent" />
      </div>
    </div>
  </div>

  <!-- 资源列表 -->
  <ResourceList :curBtn="curBtn" />

  <!-- marker信息 -->
  <InfoDialog v-if="showMarkerInfo" />

  <!-- 任务分工节点弹窗 -->
  <TaskAssignDialog v-if="taskPlanNode" />

  <!-- 涉及多个场景时，场景选择列表弹窗 -->
  <SceneDialog v-if="showSceneDialog" />

  <!-- 任务页面点击警力弹框 -->
  <!-- <taskPoliceBasicDialog /> -->
  <PoliceBasicDialog v-if="showPolicDialog" />

  <!-- 预览PDF -->
  <PdfView v-if="showPdf" :pdfObj="pdfData" @close="delClose" />

  <!-- 解析文档 -->
  <!-- <ElectronicArchives v-if="showDA" @close="showDA=false"/> -->

  <!-- 电子文档 -->
  <!-- <ElectronCard v-if="showDA" @close="showDA=false"/> -->
</template>

<script setup>
import { ElMessage } from "element-plus";
import emitter from "@/utils/emitter";
import { shallowRef, markRaw } from "vue";
import { computed, watch, ref, onMounted, nextTick } from "vue";
// import BasicView from './components/BasicView.vue'
import TaskProfile from "./components/TaskProfile.vue";
import DailySchedule from "./components/DailySchedule.vue";
import TaskDeploy from "./components/TaskDeploy.vue";
// import OrgLeader from './components/OrgLeader.vue'
import ResourceList from "./components/ResourceList.vue";
import InfoDialog from "./components/InfoDialog.vue";
import TaskAssignDialog from "./components/TaskAssignDialog.vue";
import SceneDialog from "./components/SceneDialog.vue";
// import ElectronicArchives from '@/components/electronicArchives'
// import PdfUpload from '@/components/pdfUpload'
import PdfView from "@/components/pdfView";
// import ElectronCard from '@/components/electronCard'
// import taskPoliceBasicDialog from './components/taskPoliceBasicDialog.vue';
import PoliceBasicDialog from "./components/policeBasicDialog.vue";
// 接口
import { drawGsGtData, addTourResouce } from "@/components/SmartMap/js/utils";
import { drawResourceDataAll } from "@/components/SmartMap/js/resource";
import {
  getDrawDataOfTask,
  getRelatedFeatureDataOfTask,
  getTaskList,
} from "@/api/workCockpit/index.js";
import { searchScreenForId, queryTaskInfo } from "@/api/task/new";
import {
  getDrawDataForTask,
  getDrawDataForScreen,
  getReourceDataForScene,
} from "@/api/task/task";
import { loadPicture, flattenTreeData } from "@/utils";
import { addCommunityMerchant } from "@/components/SmartMap/js/addMarkers";
import { closeFloors } from "@/components/SmartMap/js/utils";
// store
import useScreenStore from "@/store/modules/screenStore";
import useFloorStore from "@/store/modules/floorStore";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import useTaskStore from "@/store/modules/taskStore";
import useSettingStore from "@/store/modules/settingStore";
import {
  drawTaskScenePointAndLine,
  cancelMarkersHightLight,
  cancelLinesHightLight,
  hideAllLayerReset,
} from "../utils";
const WorkCockpitStore = useWorkCockpitStore();
const showTaskDetail = computed(() => WorkCockpitStore.showTaskDetail);
const showMarkerInfo = computed(() => WorkCockpitStore.showMarkerInfo);
const taskItem = computed(() => WorkCockpitStore.taskItem);
const taskPlanNode = computed(() => WorkCockpitStore.taskPlanNode);
const levelColorMap = computed(() => WorkCockpitStore.levelColorMap);
const canceMarkerlData = computed(() => WorkCockpitStore.canceMarkerlData);
const cancelLineIds = computed(() => WorkCockpitStore.cancelLineIds);
const showSceneDialog = computed(() => WorkCockpitStore.showSceneDialog);
const showPolicDialog = computed(() => WorkCockpitStore.showPoliceBasic);
const taskSceneInfo = computed(() => WorkCockpitStore.taskSceneData);
let selectTab = computed(() => WorkCockpitStore.tabsOne);
let selectTabTwo = computed(() => WorkCockpitStore.tabsTwo);
const curComponent = computed(() => {
  return btnGroup.value.find((item) => item.key === curBtn.value)?.component;
});
const curBtn = ref("rcap");
const showDA = ref(false);
const showPdf = ref(false);
const pdfData = ref({
  src: "",
  name: "",
  id: "",
});
const btnGroup = shallowRef([
  { key: "rcap", label: "日程安排", component: markRaw(DailySchedule) },
  { key: "rwbs", label: "任务部署", component: markRaw(TaskDeploy) },
  // { key: 'jbgk', label: '基本概况', component: BasicView },
  // { key: 'zzld', label: '组织领导', component: OrgLeader },
  { key: "rwda", label: "任务档案", component: markRaw(TaskProfile) },
]);
// 按钮切换
const changeContent = (item,type=false) => {
  if (item.key === "rcap") {
    emitter.emit("clearTimeInit");
  }
  curBtn.value = item.key;
  WorkCockpitStore.setSceneIds([]);
  if(!type){
     WorkCockpitStore.setShowSceneDialog(false);
  }
  WorkCockpitStore.set_tabsOne(item.key);
  // let g = window.__g;
  // // 取消高亮marker
  // if (canceMarkerlData.value) {
  //   cancelMarkersHightLight(canceMarkerlData.value);
  //   if (canceMarkerlData.value?.basicDataId) {
  //     g.marker.setText(
  //       canceMarkerlData.value.basicDataId,
  //       canceMarkerlData.value.name
  //     );
  //   }
  // }
  // // 取消高亮线路
  // if (cancelLineIds.value?.length) {
  //   cancelLinesHightLight(cancelLineIds.value);
  //   g.marker.setText(cancelLineIds.value[0], canceMarkerlData.value.name);
  // }
  // 绘制
  // g.marker.clear()
  // g.marker3d.clear();
  // g.polyline.clear()
  // g.polygon.clear();// 清除面
  // g.polygon3d.clear();// 清除警戒线
  // g.markerLayer.clear();// 清除markerLayer
  // g.customObject.clear(); // 清除自定义对象
  // drawTaskScenePointAndLine(taskSceneInfo.value)
  if (taskItem.value.viewData) {
    let g = window.__g;
    g.camera.set(taskItem.value.viewData.camera, 2);
  }
  addCommunityMerchant();
};
// 刷新数据
const refreshData = () => {
  queryTaskInfo({ id: taskItem.value.id }).then((res) => {
    if (res.code === 0) {
      WorkCockpitStore.setTaskItem(res.data);
    }
  });
};
const delClose = (e) => {
  showPdf.value = false;
  if (e === 0) {
    refreshData();
  }
};
// 绘制漫游编辑以及漫游预览的资源要素
const drawToursForReasuce = async (sceneId) => {
  getReourceDataForScene({ sceneId }).then((res) => {
    WorkCockpitStore.set_basicIds(sceneId, 1);
    let treeData = res.data || [];
    let treeLayer = flattenTreeData(treeData[0].children);
    console.log(treeLayer);
    // drawMarkers(fileterNode)
    let arr = treeLayer.filter((item) => item.dataLevelFlag);
    let fileterNode = arr.filter((item) => !item.floorNum);
    // addTourResouce(fileterNode);
    drawResourceDataAll(fileterNode, true);
    //   let g = window.__g
    // let markerarr = []
    // for (const item of arr) {
    //   let o = {
    //     id: item.id,
    //     groupId: 'resource',
    //     coordinate: [item.coordinates[0],item.coordinates[1],0], //坐标位置
    //     coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    //     anchors: [-34.5, 85],
    //     range: [0, 150000], //可视范围
    //     // imageSize: [50, 56], //图片的尺寸,//图片的尺寸
    //     imagePath: loadPicture(`./images/resource/${item.type}.png`),
    //     fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    //     text: item.name, //显示的文字
    //     useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    //     textRange: [1, 15], //文本可视范围[近裁距离, 远裁距离]
    //     textOffset: [0, 0], // 文本偏移
    //     textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
    //     fontSize: 12, //字体大小
    //     fontOutlineSize: 1, //字体轮廓线大小
    //     fontColor: Color.White, //字体颜色
    //     fontOutlineColor: Color.Black, //字体轮廓线颜色
    //     autoHeight: true, // 自动判断下方是否有物体
    //     displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
    //     priority: 0, //避让优先级
    //     occlusionCull: true, //是否参与遮挡剔除
    //     clusterByImage: true
    //   };
    //   markerarr.push(o)
    // }
    // if (markerarr.length > 0) {
    //    g.marker.add(markerarr)
    //   // g.marker.focusAll()
    // }
  });
};
// 任务漫游
const handleTaskPlay = () => {
  searchScreenForId({ taskId: taskItem.value.id }).then((res) => {
    if (res.code === 0) {
      if (res.data.length > 0) {
        useTaskStore().SET_TASKVIDEOLIST(res.data);
        getDrawDataForScreen({ sceneId: res.data[0].id }).then((res1) => {
          useTaskStore().set_taskDrawData(res1.data);
          drawToursForReasuce(res.data[0].id);
          WorkCockpitStore.setCurTourId("");
          WorkCockpitStore.setCurPage("taskPlay");
          WorkCockpitStore.set_showResouce(true);
        });
      } else {
        proxy.$modal.msgWarning("暂无推演数据");
      }
    }
  });
  // getDrawDataForTask({ taskId: taskItem.value.id }).then(res => {
  //   useTaskStore().set_taskDrawData(res.data)
  //   searchScreenForId({ taskId: taskItem.value.id }).then((res) => {
  //     if (res.code === 0) {
  //       if (res.data.length > 0) {
  //         useTaskStore().SET_TASKVIDEOLIST(res.data)
  //         WorkCockpitStore.setCurPage('taskPlay')
  //       } else {
  //         ElMessage({ message: '暂无推演数据', type: 'warning' })
  //       }
  //     }
  //   })
  // })
};

// 获取任务下标绘数据
const getDrawData = async () => {
  let g = window.__g;
  g.infoTree.hide("8C6F67F84F71F11130C1BEACD0BAF612"); //隐藏高速 高铁
  g.infoTree.hide("A3B755BF43736C15455300A0FB44761F"); //隐藏高速 高铁
  g.infoTree.hide("8B6725594EA91CCA8431338C12C4399F");
  drawGsGtData(0);
  hideAllLayerReset();
};

// 获取指定场景类型的标绘数据 1:道路  2:现场  3:住地
const getDrawDataByType = () => {
  getRelatedFeatureDataOfTask({ taskId: taskItem.value.id, sceneType: 1 }).then(
    (res) => {
      WorkCockpitStore.setRoadDrawList(res.data);
    }
  );
  getRelatedFeatureDataOfTask({ taskId: taskItem.value.id, sceneType: 2 }).then(
    (res) => {
      WorkCockpitStore.setSiteDrawList(res.data);
    }
  );
  getRelatedFeatureDataOfTask({ taskId: taskItem.value.id, sceneType: 3 }).then(
    (res) => {
      WorkCockpitStore.setResidenceDrawList(res.data);
    }
  );
};
// 返回总览
const goBack = () => {
  WorkCockpitStore.setCurPage('overview')
  useFloorStore().setFloornumberShow(false)
  closeFloors()
  useSettingStore().set_isClickTools(true);// 隐藏工具栏选中状态
    WorkCockpitStore.set_tabsOne('rcap')
    WorkCockpitStore.set_tabsTwo('')
    let g = window.__g
    g.reset(1 | 2 | 4);
    g.infoTree.show('8C6F67F84F71F11130C1BEACD0BAF612')
    g.infoTree.show('A3B755BF43736C15455300A0FB44761F')
    g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
    drawGsGtData(1)
    addCommunityMerchant()
}
onMounted(() => {
  // getDrawDataOfTask({ taskId: taskItem.value.id }).then(res => {
  //   console.log(res)
  //   WorkCockpitStore.setTaskSceneData(res.data)
  //   let lines =[]
  //   res.data.forEach((item,index) => {
  //     if(item.type==='1'){
  //       if(item.drawInfoList?.length){
  //         item.drawInfoList.forEach((child,i)=>{
  //           lines.push(child)
  //         })
  //       }
  //     }
  //   });
  //   console.log(lines,'任务所有路线')
  // })
  if (curBtn.value === selectTab.value) {
    getDrawData();
    // getDrawDataByType()
  } else {
    curBtn.value = selectTab.value;
    let items = btnGroup.value.find((item) => item.key === selectTab.value);
    changeContent(items,true);
  }
  if (taskItem.value.viewData) {
    let g = window.__g;
    g.camera.set(taskItem.value.viewData.camera, 2);
  }
  useScreenStore().set_editScreen(false);
  useFloorStore().set_isShowFloor(false);
});
</script>

<style lang="scss" scoped>
.content-wraper {
  width: 400px;
  height: calc(110vh);
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  position: absolute;
  top: 72px;
  right: 10px;
  bottom: 20px;
  z-index: 30;
  transform: scale(0.8);
  transform-origin: right top;

  .content-header {
    width: 100%;
    height: 48px;
    line-height: 48px;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    background: url("@/assets/workcockpit/header_bg.png") no-repeat;
    position: relative;
  }

  .content-box {
    height: calc(100% - 48px);
    padding: 20px;
    box-sizing: border-box;
    font-family: Source Han Sans;
    font-size: 14px;

    .task-info-box {
      padding: 12px;
      background: rgba(0, 0, 0, 0.1);
      box-sizing: border-box;
      border: 1px solid #1a2a65;
      line-height: 26px;
      margin-bottom: 12px;
      position: relative;

      .task-name {
        font-size: 18px;
        font-weight: bold;
        margin-bottom: 8px;
      }

      .task-level {
        font-weight: bold;
      }

      .task-roam {
        position: absolute;
        top: 12px;
        right: 16px;
        display: flex;
        align-items: center;
        gap: 3px;
        color: #00ceff;
        cursor: pointer;
      }
    }

    .btn-group-box {
      display: flex;
      align-items: flex-end;
      margin-bottom: 12px;

      .btn {
        width: 91px;
        height: 28px;
        line-height: 30px;
        text-align: center;
        background: url("@/assets/workcockpit/btn_group_bg.png") no-repeat;
        box-sizing: border-box;
        color: rgba($color: #fff, $alpha: 0.35);
        cursor: pointer;

        &:not(:first-child) {
          margin-left: -6px;
        }

        &:first-child {
          z-index: 60;
        }

        &:nth-child(2) {
          z-index: 59;
        }

        &:nth-child(3) {
          z-index: 58;
        }

        &:last-child {
          z-index: 57;
        }
      }

      .active {
        width: 101px;
        height: 40px;
        line-height: 42px;
        background: url("@/assets/workcockpit/btn_group_press.png") no-repeat;
        font-size: 16px;
        font-weight: bold;
        color: #fff;
        z-index: 80;
      }
    }

    .info-box {
      height: 73%;
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
}

:deep(.el-tabs__item) {
  color: #fff;
  padding: 0 16px;
}

:deep(.el-tabs__item.is-active) {
  color: #00ceff;
  font-weight: bold;
}

:deep(.el-tabs__active-bar) {
  background-color: #00ceff;
}

:deep(.el-tabs__nav-wrap:after) {
  background-color: #2644ad;
  height: 1px;
}

:deep(.el-tabs__header) {
  margin-bottom: 20px;
}

:deep(.el-tabs--top .el-tabs__item.is-top:nth-child(2)) {
  padding-left: 16px;
}
.back_modal {
  position: absolute;
  left: 16px;
  top: 50%;
  transform: translateY(-50%);
  width: 20px;
  height: 20px;
  background: url("@/views/componenets/taskNew/img/back.png") no-repeat;
  background-size: 100% 100%;
  cursor: pointer;
}
</style>
