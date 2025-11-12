<template>
  <el-scrollbar>
    <div class="content">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="总体部署" name="zt"></el-tab-pane>
        <el-tab-pane label="部署详情" name="details"></el-tab-pane>
      </el-tabs>
      <!-- 总体部署 -->
      <div class="info-box" style="height: 80%">
        <div class="info-total" v-if="activeTab === 'zt'">
          <div class="total-card">
            <p class="num safe-num">{{ dangerTotal }}</p>
            <p>安全排查总数</p>
          </div>
          <div class="total-card">
            <p class="num police-num">{{ policeTotal }}</p>
            <p>共投入警力</p>
          </div>
        </div>
        <div class="info-type" :class="{ taskActive: activeName === 'dl' }"
          v-if="activeTab === 'zt' || activeTab === 'dl'" @click="taskSelectFun('dl')">
          <p class="title">涉及道路：{{ roadData?.num }}条</p>
          <div>
            <p>
              安全隐患数量：<b class="value">{{ roadData.dangerNum }}个</b>
            </p>
            <p>
              道路执勤<b class="value">{{ roadData?.policeData?.onduty }}人</b>、 应急力量<b class="value">{{
                roadData?.policeData?.emergency }}人</b>
            </p>
            <!-- <p>此次任务共涉及公路路线：<b class="value">{{ roadData?.num }}条</b></p> -->
            <p>责任领导：{{ roadData?.basicData?.zrld }}</p>
            <p>责任单位：{{ roadData?.basicData?.zrdw }}</p>
          </div>
        </div>
        <div class="info-type" :class="{ taskActive: activeName === 'gs' }"
          v-if="activeTab === 'zt' || activeTab === 'gs'" @click="taskSelectFun('gs')">
          <p class="title">涉及高速：{{ highwayData?.num }}条</p>
          <div>
            <p>
              安全隐患数量：<b class="value">{{ highwayData.dangerNum }}个</b>
            </p>
            <p>
              道路执勤<b class="value">{{ highwayData?.policeData?.onduty }}人</b>、 应急力量<b class="value">{{
                highwayData?.policeData?.emergency }}人</b>
            </p>
            <!-- <p>此次任务共涉及公路路线：<b class="value">{{ roadData?.num }}条</b></p> -->
            <p>责任领导：{{ highwayData?.basicData?.zrld }}</p>
            <p>责任单位：{{ highwayData?.basicData?.zrdw }}</p>
          </div>
        </div>
        <div class="info-type" :class="{ taskActive: activeName === 'gt' }"
          v-if="activeTab === 'zt' || activeTab === 'gt'" @click="taskSelectFun('gt')">
          <p class="title">涉及铁路：{{ railwayData?.num }}条</p>
          <div>
            <p>
              安全隐患数量：<b class="value">{{ railwayData.dangerNum }}个</b>
            </p>
            <p>
              道路执勤<b class="value">{{ railwayData?.policeData?.onduty }}人</b>、 应急力量<b class="value">{{
                railwayData?.policeData?.emergency }}人</b>
            </p>
            <!-- <p>此次任务共涉及公路路线：<b class="value">{{ roadData?.num }}条</b></p> -->
            <p>责任领导：{{ railwayData?.basicData?.zrld }}</p>
            <p>责任单位：{{ railwayData?.basicData?.zrdw }}</p>
          </div>
        </div>
        <div class="info-type" :class="{ taskActive: activeName === 'zd' }"
          v-if="activeTab === 'zt' || activeTab === 'zd'" @click="taskSelectFun('zd')">
          <p class="title">涉及住地：{{ residenceData?.num }}处</p>
          <div>
            <p>
              安全隐患数量：<b class="value">{{ residenceData.dangerNum }}个</b>
            </p>
            <p>
              住地执勤<b class="value">{{ residenceData?.policeData?.onduty }}人</b>、 应急力量<b class="value">{{
                residenceData?.policeData?.emergency }}人</b>
            </p>
            <!-- <p>此次任务共涉及住地：<b class="value">{{ residenceData?.num }}处</b></p> -->
            <p>责任领导：{{ residenceData?.basicData?.zrld }}</p>
            <p>责任单位：{{ residenceData?.basicData?.zrdw }}</p>
          </div>
        </div>
        <div class="info-type" :class="{ taskActive: activeName === 'xc' }"
          v-if="activeTab === 'zt' || activeTab === 'xc'" @click="taskSelectFun('xc')">
          <p class="title">涉及现场：{{ siteData?.num }}处</p>
          <div>
            <p>
              安全隐患数量：<b class="value">{{ siteData.dangerNum }}个</b>
            </p>
            <p>
              现场执勤<b class="value">{{ siteData?.policeData?.onduty }}人</b>、 应急力量<b class="value">{{
                siteData?.policeData?.emergency }}人</b>
            </p>
            <!-- <p>此次任务共涉及现场：<b class="value">{{ siteData?.num }}处</b></p> -->
            <p>责任领导：{{ siteData?.basicData?.zrld }}</p>
            <p>责任单位：{{ siteData?.basicData?.zrdw }}</p>
          </div>
        </div>
      </div>
      <!-- 部署详情 -->
      <div v-if="activeTab === 'details'">
        <div class="select_box" style="width: 40%; margin-bottom: 10px">
          <el-select v-model="detailsType" size="large" @change="changeDetaisType">
            <el-option label="全部" value="all" />
            <el-option label="道路" value="dl" />
            <el-option label="高速" value="gs" />
            <el-option label="高铁" value="gt" />
            <el-option label="现场" value="xc" />
            <el-option label="住地" value="zd" />
          </el-select>
        </div>
        <div class="info-item" v-for="(item, index) in newTaskSenceData" :key="index"
          @click="handleClickData1(item, index)" :class="{ active: curActive === item.id }">
          <div v-if="item.type === 1">
            <p class="title-name">{{ item.name }}</p>
            <p>{{ item.roadName }}</p>
            <p>
              安全隐患数量：<b class="label">{{ item.dangerNum }}个</b>
            </p>
            <p>
              道路执勤<b class="label">{{ item.policeData?.onduty }}人、</b>
              应急力量<b class="label"> {{ item.policeData?.emergency }}人</b>
            </p>
            <p>
              上岗时间：<b class="label">{{
                item.planNodeData[0]?.data?.time
              }}</b>（任务到达前1小时）
            </p>
            <p style="display: flex;justify-content: space-between;">
            <p>用时：{{ item.usedTime }}分钟</p><b class="label" @click.stop="goToDetails(item)">详情</b></p>
          </div>
          <div v-else-if="item.type === 2">
            <p class="title-name">现场：{{ item.name }}</p>
            <p>负责人：{{ item.planNodeData[0]?.data?.zrld }}</p>
            <p>电话：{{ item.planNodeData[0]?.data?.phone }}</p>
            <p>
              安全隐患数量：<b class="label">{{ item.dangerNum }}个</b>
            </p>
            <p>
              警力部署：共部署警力<b class="label">{{ item.policeData?.total }}人，</b>
              执勤警力<b class="label">{{ item.policeData?.onduty }}人</b>、
              应急力量<b class="label">{{ item.policeData?.emergency }}人</b>
            </p>
            <p style="display: flex;justify-content: space-between;">
            <p>
              上岗时间：<b class="label">{{
                item.planNodeData[0]?.data?.time
              }}</b>（任务到达前1小时）
            </p>
            <b class="label" @click.stop="goToDetails(item)">详情</b></p>
          </div>
          <div v-else>
            <p class="title-name">住地：{{ item.name }}</p>
            <p>负责人：{{ item.planNodeData[0]?.data?.zrld }}</p>
            <p>电话：{{ item.planNodeData[0]?.data?.phone }}</p>
            <p>
              安全隐患数量：<b class="label">{{ item.dangerNum }}个</b>
            </p>
            <p>
              警力部署：共部署警力<b class="label">{{ item.policeData?.total }}人，</b>
              执勤警力<b class="label">{{ item.policeData?.onduty }}人</b>、
              应急力量<b class="label">{{ item.policeData?.emergency }}人</b>
            </p>
            <p style="display: flex;justify-content: space-between;">
            <p>
              上岗时间：<b class="label">{{
                item.planNodeData[0]?.data?.time
              }}</b>（任务到达前1小时）
            </p>
            <b class="label" @click.stop="goToDetails(item)">详情</b></p>
          </div>
        </div>
      </div>
    </div>
  </el-scrollbar>
</template>

<script setup>
import { ref, onMounted, computed, onUnmounted, onBeforeUnmount,nextTick } from "vue";
import { closeFloors } from "@/components/SmartMap/js/utils";
import {
  getPolicePresenceOfTask,
  getSceneDataForIdAndToDtaw,
} from "@/api/workCockpit/index.js";
import {
  drawTaskScenePointAndLine,
  cancelMarkersHightLight,
  drawLinesAndeMarker,
  drawTaskMarkers,
  drawTaskLines,
  drawLinesName,
} from "../../utils";
// store
import useSettingStore from "@/store/modules/settingStore";
import useTaskStore from "@/store/modules/taskStore";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import { addCommunityMerchant } from "@/components/SmartMap/js/addMarkers";
const WorkCockpitStore = useWorkCockpitStore();
const taskItem = computed(() => WorkCockpitStore.taskItem);
const taskSceneInfo = computed(() => WorkCockpitStore.taskSceneData);
const roadDrawList = computed(() => WorkCockpitStore.roadDrawList);
const siteDrawList = computed(() => WorkCockpitStore.siteDrawList);
const residenceDrawList = computed(() => WorkCockpitStore.residenceDrawList);
const sceneList = computed(() => WorkCockpitStore.sceneList);
let selectTab = computed(() => WorkCockpitStore.tabsTwo);
const activeTab = ref("zt");
const activeName = ref("");
const detailsType = ref("all");
const policeTotal = ref(0);
const dangerTotal = ref(0);
const residenceData = ref({});
const residenceDetails = ref([]);
const roadData = ref({});
const roadDetails = ref([]);
const siteData = ref({});
const siteDetails = ref([]);
const highwayData = ref({});
const highwayDetails = ref([]);
const railwayData = ref({});
const railwayDetails = ref([]);
const newTaskSenceData = ref([]);
const allTaskSenceData = ref([]);
const lineIds = ref([]);
const xcIds = ref([]);
const zdIds = ref([]);
let timers = ref(null);
let showFlag = ref(true); // 道路
let showFlagH = ref(true) // 高速
let showFlagR = ref(true) // 高铁
let rodaIds = ref([]) // 道路所有ids
let hRoadIds = ref([]) // 高速所有ids
let rRoadIds = ref([]) // 高铁所有ids
// 进入详情三级页面
const goToDetails = async (item) => {
  // closeFloors()
  WorkCockpitStore.setEventMapData({GroupID: item.id,Id: item.basicDataId,UserData:item.name});
  useTaskStore().SET_SCREENMODALINFO({ id: item.id });
  WorkCockpitStore.getSceneData(item.name).then(() => {
    if(sceneList.value.length<=1){
      WorkCockpitStore.openHouse(true);
      }else{
        WorkCockpitStore.openHouse(false);
      }
    WorkCockpitStore.setCurPage("");
    useSettingStore().setShowTool(true);
    nextTick(() => {
      WorkCockpitStore.setCurPage("sceenDetail")
    });
  });
}
const getPolicyData = () => {
  getPolicePresenceOfTask({ taskId: taskItem.value.id }).then((res) => {
    policeTotal.value = res.data.policeTotal;
    dangerTotal.value = res.data.dangerTotal;
    residenceData.value = res.data.residenceData;
    residenceDetails.value = res.data.residenceDetails;
    roadData.value = res.data.roadData;
    roadDetails.value = res.data.roadDetails;
    siteData.value = res.data.siteData;
    siteDetails.value = res.data.siteDetails;
    highwayData.value = res.data.highwayData;
    highwayDetails.value = res.data.highwayDetails;
    railwayData.value = res.data.railwayData;
    railwayDetails.value = res.data.railwayDetails;
    newTaskSenceData.value = res.data.SceneDetail;
    allTaskSenceData.value = res.data.SceneDetail;
    getAllRoadIdList('dl', res.data.roadDetails)
    getAllRoadIdList('gs', res.data.highwayDetails)
    getAllRoadIdList('gt', res.data.railwayDetails)
  });
};
// 拿到当前任务下道路、高速、高铁的所有id集合
const getAllRoadIdList = (type, arr) => {
  if (type === 'dl') {
    if (arr?.length) {
      const combinedIds = arr.map(obj => obj.drawDataIds).flat();
      rodaIds.value = combinedIds
    } else {
      rodaIds.value = []
    }
  }
  if (type === 'gs') {
    if (arr?.length) {
      const combinedIds = arr.map(obj => obj.drawDataIds).flat();
      hRoadIds.value = combinedIds
    } else {
      hRoadIds.value = []
    }
  }
  if (type === 'gt') {
    if (arr?.length) {
      const combinedIds = arr.map(obj => obj.drawDataIds).flat();
      rRoadIds.value = combinedIds
    } else {
      rRoadIds.value = []
    }
  }

}
const changeDetaisType = (e) => {
  console.log(e);
  if (e === "all") {
    newTaskSenceData.value = allTaskSenceData.value;
  }
  if (e === "dl") {
    newTaskSenceData.value = roadDetails.value;
  }
  if (e === "xc") {
    newTaskSenceData.value = siteDetails.value;
  }
  if (e === "zd") {
    newTaskSenceData.value = residenceDetails.value;
  }
  if (e === "gs") {
    newTaskSenceData.value = highwayDetails.value;
  }
  if (e === "gt") {
    newTaskSenceData.value = railwayDetails.value;
  }
};

const taskSelectFun = (name) => {
  if (timers.value) {
    clearInterval(timers.value);
    timers.value = null;
    showFlag.value = true;
    showFlagH.value = true
    showFlagR.value = true
  }
  cancelShowHide().then(() => {
    if (activeName.value === name) {
      activeName.value = "";
      // 取消所有效果
      return;
    }
    activeName.value = name;
    addStyleActive(name);
  });

  console.log(taskSceneInfo.value);
};
const cancelShowHide = async () => {
  showLines('all');
};
const showLines = async (type) => {
  let g = window.__g;
  if (type === 'all') {
    await g.polyline.show(rodaIds.value);
    await g.polyline.show(hRoadIds.value);
    await g.polyline.show(rRoadIds.value);
    showFlag.value = false;
    showFlagH.value = false
    showFlagR.value = false
  }
  if (type === 'dl') {
    await g.polyline.show(rodaIds.value);
    showFlag.value = false;
  }

  if (type === 'gs') {
    await g.polyline.show(hRoadIds.value);
    showFlagH.value = false
  }

  if (type === 'gt') {
    await g.polyline.show(rRoadIds.value);
    showFlagR.value = false
  }
  // await g.polyline.showAll();
  // showFlag.value = false;
};
const hideLines = async (type) => {
  let g = window.__g;
  if (type === 'dl') {
    await g.polyline.hide(rodaIds.value);
    showFlag.value = true;
  }

  if (type === 'gs') {
    await g.polyline.hide(hRoadIds.value);
    showFlagH.value = true
  }

  if (type === 'gt') {
    await g.polyline.hide(rRoadIds.value);
    showFlagR.value = true
  }
  // await g.polyline.hideAll();
  // showFlag.value = true;
};

//  添加效果
const addStyleActive = async (type) => {
  if (type === "dl") {
    timers.value = setInterval(() => {
      if (showFlag.value) {
        showLines('dl');
      } else {
        hideLines('dl');
      }
    }, 500);
  }
  if (type === 'gs') {
    timers.value = setInterval(() => {
      if (showFlagH.value) {
        showLines('gs');
      } else {
        hideLines('gs');
      }
    }, 500);
  }
  if (type === 'gt') {
    timers.value = setInterval(() => {
      if (showFlagR.value) {
        showLines('gt');
      } else {
        hideLines('gt');
      }
    }, 500);
  }
};
// 把所有id存起来
const getTaskAllIDs = (arr) => {
  console.log(arr);
  console.log(siteDrawList.value);
  xcIds.value = [].concat(
    ...arr
      .filter((item) => item.type === "2")
      .map((item) => item.pointInfoList.map((listItem) => listItem.id))
  );
  zdIds.value = [].concat(
    ...arr
      .filter((item) => item.type === "3")
      .map((item) => item.pointInfoList.map((listItem) => listItem.id))
  );
};
const handleTabChange = (tab) => {
  if (timers.value) {
    clearInterval(timers.value);
    timers.value = null;
    showFlag.value = true;
    showFlagH.value = true
    showFlagR.value = true
  }
  activeName.value = "";
  // 清除
  let g = window.__g;
  // g.polyline.clear();
  // g.marker.clear();
  // g.marker3d.clear();
  // g.markerLayer.clear();// 清除markerLayer 
  // g.customObject.clear(); // 清除自定义对象
  curIndex.value = -1;
  curActive.value = "";
  WorkCockpitStore.setSceneIds([]);
  WorkCockpitStore.setTabsName("");
  WorkCockpitStore.set_tabsTwo(tab);
  if (tab === "zt") {
    // drawTaskScenePointAndLine(taskSceneInfo.value);
    WorkCockpitStore.set_twoPageSource("zt");
  } else if (tab === "dl") {
    drawTaskLines(roadDrawList.value);
  } else if (tab === "xc") {
    drawTaskMarkers(siteDrawList.value);
  } else if (tab === "details") {
    detailsType.value = "all";
    // drawTaskScenePointAndLine(taskSceneInfo.value);
    WorkCockpitStore.set_twoPageSource("details");
  } else {
    drawTaskMarkers(residenceDrawList.value);
  }
  if (taskItem.value.viewData) {
    g.camera.set(taskItem.value.viewData.camera, 2);
  }
  addCommunityMerchant();
};

const cancelHighLight = () => {
  let g = window.__g;
  // 如果有高亮的marker,取消高亮
  if (cancelMarkerItem.value?.basicDataId) {
    cancelMarkersHightLight(cancelMarkerItem.value);
    g.marker.setText(
      cancelMarkerItem.value.basicDataId,
      cancelMarkerItem.value.name
    );
  }
};

const cancelMarkerItem = ref({}); // 要取消高亮的marker
const cancelLineIds = ref([]); // 要取消高亮的lines
const curIndex = ref(-1);
const curActive = ref("");
// 点击数据项
const handleClickData = (item, index) => {
  console.log(111, item);
  curIndex.value = index;
  let g = window.__g;
  cancelHighLight();
  if (item.drawDataIds && item.drawDataIds.length > 0) {
    // 场景有保存视角切到保存的视角
    if (item.viewData) {
      g.camera.set(item.viewData.camera, 2);
    } else {
      g.marker.focus(item.drawDataIds[0], 300, 0.01);
    }
    // 更新资源数
    WorkCockpitStore.setSceneIds([item.id]);
    WorkCockpitStore.setTabsName("");
    // 清除
    g.polyline.clear();
    g.marker.clear();
    // 绘制点击的道路和marker
    let params = { sceneIds: item.id };
    getSceneDataForIdAndToDtaw(params).then((res) => {
      console.log(res);
      if (res.data.drawLineData?.length) {
        drawLinesAndeMarker(res.data.drawLineData, item.name);
        // 绘制路线名称
        // drawLinesName(res.data.drawLineData);
      }
    });
    addCommunityMerchant();
  }
  if (item.basicDataId) {
    cancelMarkerItem.value = JSON.parse(JSON.stringify(item));
    WorkCockpitStore.setCancelData(cancelMarkerItem.value);
    // 更新资源数
    WorkCockpitStore.setSceneIds([item.id]);
    WorkCockpitStore.setTabsName("");
    // 场景有保存视角切到保存的视角
    if (item.viewData) {
      g.camera.set(item.viewData.camera, 2);
    }
    // 绘制点击的
    const list =
      activeTab.value === "xc" ? siteDrawList.value : residenceDrawList.value;
    const dataItem = list.find((el) => el.sceneInfo.id === item.id);
    dataItem && drawTaskMarkers([dataItem]);
    const text = `${item.name}\n安全隐患：${item.dangerNum}\n警力部署：${item.policeData?.onduty}\n应急力量：${item.policeData?.emergency}`;
    g.marker.setText(item.basicDataId, text);
  }
};

// 任务部署 部署详情单条数据点击
const handleClickData1 = (item, index) => {
  console.log(item)
  curActive.value = item.id;
  let g = window.__g;
  cancelHighLight();
  if (item.drawDataIds && item.drawDataIds.length > 0) {
    WorkCockpitStore.setCancelData({});
    // 场景有保存视角切到保存的视角
    if (item.viewData) {
      g.camera.set(item.viewData.camera, 2);
    } else {
      g.marker.focus(item.drawDataIds[0], 300, 0.01);
    }
    // 更新资源数
    WorkCockpitStore.setSceneIds([item.id]);
    WorkCockpitStore.setTabsName("");
    // 清除
    g.polyline.clear();
    g.marker.clear();
    g.marker3d.clear();
    g.markerLayer.clear();// 清除markerLayer 
    g.customObject.clear(); // 清除自定义对象
    // 绘制点击的道路和marker
    // let params = { sceneIds: item.id };
    // getSceneDataForIdAndToDtaw(params).then((res) => {
    //   if (res.data.drawLineData?.length) {
    //     drawLinesAndeMarker(res.data.drawLineData, item.name);
    //   }
    // });
    addCommunityMerchant();
  }
  if (item.basicDataId) {
    // // 清除
    g.polyline.clear();
    g.marker.clear();
    g.marker3d.clear();
    g.markerLayer.clear();// 清除markerLayer 
    g.customObject.clear(); // 清除自定义对象
    cancelMarkerItem.value = JSON.parse(JSON.stringify(item));
    WorkCockpitStore.setCancelData(cancelMarkerItem.value);
    // 更新资源数
    WorkCockpitStore.setSceneIds([item.id]);
    WorkCockpitStore.setTabsName("");
    // 场景有保存视角切到保存的视角
    if (item.viewData) {
      g.camera.set(item.viewData.camera, 2);
    }
    // // 绘制点击的
    // const list = item.type === 2 ? siteDrawList.value : residenceDrawList.value;
    // const dataItem = list.find((el) => el.sceneInfo.id === item.id);
    // dataItem && drawTaskMarkers([dataItem]);
    const text = `${item.name}\n安全隐患：${item.dangerNum}\n警力部署：${item.policeData?.onduty}\n应急力量：${item.policeData?.emergency}`;
    g.marker.setText(item.id, text);
    addCommunityMerchant();
  }
};

onMounted(() => {
  getPolicyData();
  getTaskAllIDs(taskSceneInfo.value);
  if (selectTab.value === "details") {
    activeTab.value = "details";
  }
  if (timers.value) {
    clearInterval(timers.value);
    timers.value = null;
  }
});
onUnmounted(() => {
  if (timers.value) {
    clearInterval(timers.value);
    timers.value = null;
  }
});
onBeforeUnmount(() => {
  if (timers.value) {
    clearInterval(timers.value);
    timers.value = null;
  }
});
</script>

<style lang="scss" scoped>
.content {
  height: 100%;
  font-size: 14px;

  .info-total {
    width: 100%;
    display: flex;
    gap: 12px;
    margin-bottom: 16px;

    .total-card {
      flex: 1;
      height: 80px;
      background: rgba(0, 0, 0, 0.1);
      box-sizing: border-box;
      border: 1px solid #1a2a65;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      .num {
        font-family: DIN;
        font-size: 36px;
        margin-bottom: 8px;
      }

      .safe-num {
        color: #ff4040;
      }

      .police-num {
        color: #00ceff;
      }
    }
  }

  .info-type {
    line-height: 24px;
    margin-bottom: 28px;
    // padding-left: 12px;
    padding: 10px 12px;
    cursor: pointer;

    &:last-child {
      margin-bottom: 8px;
    }

    .value {
      color: #00ceff;
    }
  }

  .taskActive {
    background: rgba(0, 123, 255, 0.302);
    box-shadow: inset 0px 0px 8px 0px #007bff;
  }

  .title {
    color: #00ceff;
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 8px;
  }

  .info-item {
    padding: 12px;
    box-sizing: border-box;
    background: rgba(0, 0, 0, 0.1);
    border: 1px solid rgba(38, 68, 173, 0.3);
    line-height: 24px;
    margin-bottom: 8px;
    cursor: pointer;

    &:hover {
      background: rgba(0, 123, 255, 0.302);
      box-shadow: inset 0px 0px 8px 0px #007bff;
    }

    .title-name {
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 8px;
    }

    .label {
      color: #00ceff;
    }
  }

  .active {
    background: rgba(0, 123, 255, 0.302);
    box-shadow: inset 0px 0px 8px 0px #007bff;
  }

  :deep(.el-tabs__nav) {
    width: 100%;

    .el-tabs__item {
      flex: 1;
    }
  }
}

:deep(.el-scrollbar) {
  height: 112%;
}

:deep(.el-select__wrapper) {
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;

  .el-select__selected-item {
    color: #ffffff;
    opacity: 0.8;
  }

  .is-transparent {
    color: #a8abb2;
    // opacity: 0.8;
  }
}
</style>
