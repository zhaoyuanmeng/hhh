<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 11:20:59
 * @LastEditors: Alex
-->
<template>
  <div class="Draw_content">
    <div class="action_btn">
      <div class="manyou_btn" @click="edit">编辑漫游</div>
      <div class="manyou_btn" style="margin-left: 16px" @click="view">
        预览漫游
      </div>
      <div class="caozuo_btn" @click="save">保存</div>
      <div class="caozuo_btn" @click="out">退出</div>
    </div>
  </div>
  <!-- 导览列表 -->
  <cameraTourTmpl v-if="isShowList" />
</template>

<script setup>
import {
  onBeforeUnmount,
  onMounted,
  ref,
  computed,
  onBeforeMount,
  getCurrentInstance,
} from "vue";
import useTaskStore from "@/store/modules/taskStore";
import { addScreenNew } from "@/api/task/new";
import { ElMessage, ElMessageBox } from "element-plus";
import useCameraStore from "@/store/modules/cameraSet";
import useSettingStore from "@/store/modules/settingStore";
import cameraTourTmpl from "./CameraTour.vue";
import CameraTourBIZ from "../setCameraTour/cameraTour";
import { sessionStorage } from "@/utils/storage";
import {
  initTools,
} from "@/components/SmartMap/js/addPlot";
import {
  queryTaskInfo,
} from "@/api/task/index";
import useFloorStore from "@/store/modules/floorStore";
import { openFloors, closeFloors } from "@/components/SmartMap/js/utils";
const emit = defineEmits(["out"]);
const { proxy } = getCurrentInstance();
const taskStore = useTaskStore();
let isShowList = computed(() => taskStore.isShowFrames);
let tourList = computed(() => taskStore.keyFrames);
let addFormData = computed(() => taskStore.addFormData);

window._cameraTour = new CameraTourBIZ();
const SettingStore = useSettingStore();
const cameraStore = useCameraStore();
const floorStore = useFloorStore();
let playtimer = ref(null);
let isNew = ref(false);
let selectId = ref(null);
onMounted(() => {
  cameraStore.setisShowListShow(true);
  cameraStore.setSaveBtn(false);
  cameraStore.setSceneFrame({});
  let obj = {
    id: "",
    name: "",
  };
  cameraStore.setScreenInfo(obj);
  initTools();
  isNew.value = false;
  selectId.value = null;
});
// 退出
const out = () => {
  taskStore.SET_ISOPENDRAW(false);
  SettingStore.set_isClickTools(true)
  closeFloors()// 清除楼层炸开数据
  clearDraw();
  emit("out");
};
// 保存
const save = async() => {
  console.log(floorStore.floornum,floorStore.openFloor)
      let floorData = []
      if(floorStore.openFloor){
        floorData = [{name:'会展中心',num:floorStore.floornum}]
      }else{
        floorData =[]
      }
  let g = window.__g
  let locations = await g.camera.get();
  let drawDatas = {
    police: sessionStorage.get("policeData") || [],
    car: sessionStorage.get("carData") || [],
    lines: sessionStorage.get("QXZS_pointLine") || [],
    location:locations.camera,
    basic:sessionStorage.get("basicData") || [],
    cordon:sessionStorage.get("QXZS_polygon3d") || [],
    uav:sessionStorage.get("uavData") || []
  };
  let obj = {
    ...addFormData.value,
    keyFrames: tourList.value,
    drawData: drawDatas,
    floors:floorData
  };
  console.log(obj);
  addScreenNew(obj).then((res) => {
    console.log(res);
    if (res.code === 0) {
      proxy.$modal.msgSuccess("保存成功！");
      queryTaskInfo({ id: addFormData.value.taskId }).then((res) => {
        cameraStore.setTaskInfo(res.data);
        taskStore.SET_TASKINFO(res.data);
        out();
      });
    }
  });
};
// 漫游预览
const view = () => {
  SettingStore.set_isClickTools(true)
  if (tourList.value.length === 0) {
    return ElMessage({
      message: "当前没有漫游数据，请先编辑添加",
      type: "warning",
    });
  }
  tourList.value[0].play = true;
  window._cameraTour.curOa = tourList.value[0];
  window._cameraTour.play_biz();
  playtimer.value = setTimeout(() => {
    tourList.value[0].play = false;
    playtimer.value = null;
  }, Number(tourList.value[0].time) * 1000);
};
// 漫游编辑
const edit = () => {
  SettingStore.set_isClickTools(true)
  if (tourList.value.length > 0) {
    window._cameraTour.curOa = tourList.value[0];
  } else {
    window._cameraTour.start();
  }
  taskStore.SET_ISSHOWFRAMES(true);
};
// 清除画的数据
const clearDraw = async () => {
  let g = window.__g;
  await g.polyline.clear(); // 清除线
  await g.polygon.clear(); // 清除面
  await g.tag.clear(); // 清除标签
  await g.marker.deleteByGroupId("police");
  await g.marker.deleteByGroupId("car");
  await g.marker.deleteByGroupId("basic");
  await g.marker.deleteByGroupId("uav");
  await g.radiationPoint.clear(); // 清除所有辐射点位样式
  sessionStorage.remove("QXZS_pointLine");
  sessionStorage.remove("carData");
  sessionStorage.remove("policeData");
  sessionStorage.remove("basicData");
  sessionStorage.remove("uavData");
  taskStore.SET_KEYFRAMES([]);
};
//删除计时器
onBeforeUnmount(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
});
onBeforeMount(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
});

</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
