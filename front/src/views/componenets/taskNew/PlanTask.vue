<template>
  <div class="plan_task_box">
    <div class="header">
      <div class="creat_modal" @click="creatPlan">创建</div>
    </div>
    <div class="content">
      <div class="tabsBox">
        <div
          class="tab_item"
          v-for="(item, index) in tabBtnList"
          :key="index"
          :class="{ active: item.type === activeName }"
          @click="getBasicData(item.type)"
        >
          {{ item.label }}
        </div>
      </div>
      <div class="listBox" v-if="list?.length">
        <div
          class="item_list"
          v-for="(item, idx) in list"
          :key="idx"
          @click.stop="clickScreen(item)"
        >
          <div class="type_box">
            {{
              item.type === "1" || item.type === "4" || item.type === "5"
                ? "路线"
                : item.type === "2"
                ? "现场"
                : "住地"
            }}
          </div>
          <div class="task_name">{{ item.sceneName }}</div>
          <div class="task_name1">{{ item.basicDataName }}</div>
          <div class="bottom_btn">
            <div class="edit_btn" @click.stop="editPlan(item)">编辑</div>
            <div
              class="action_btn"
              @click.stop="manyouView(item)"
              style="margin-left: 16px"
            >
              漫游预览
            </div>
            <div
              class="action_btn"
              @click.stop="manyouEdit(item)"
              style="margin: 0 16px"
            >
              漫游编辑
            </div>
            <el-dropdown placement="bottom" @command="handleCommand">
              <el-icon class="right_icon">
                <More />
              </el-icon>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item :command="{ type: 'copy', items: item }"
                    >复制</el-dropdown-item
                  >
                  <el-dropdown-item :command="{ type: 'del', items: item }"
                    >删除</el-dropdown-item
                  >
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
      <div v-else style="text-align: center; padding: 100px 0">暂无数据</div>
    </div>
    <AddPlanTask
      :open="showDialog"
      @hideDialog="changeCotrlFun"
      :itemInfo="updataData"
      :type="dialogType"
    />
  </div>
  <!-- 编辑弹框 -->
  <el-dialog
    v-model="centerDialogVisible"
    title="常备方案编辑"
    width="400"
    center
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
    class="edit_dialog"
    append-to-body
  >
    <el-form
      ref="drawForm"
      :inline="true"
      :model="editForm"
      :label-suffix="'：'"
      :label-width="100"
      size="large"
      class="customForm"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item
            label="名称"
            prop="sceneName"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 100%"
          >
            <el-input
              v-model="editForm.sceneName"
              placeholder="请输入..."
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <div class="btn_box cancle_btn" @click="centerDialogVisible = false">
          取消
        </div>
        <div class="btn_box" @click="sureDraw">确定</div>
      </div>
    </template>
  </el-dialog>

  <!-- 选择漫游视角弹框 -->
  <el-dialog
    v-model="tourBol"
    title="请选择漫游编辑视角"
    width="400"
    center
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    class="edit_dialog"
    @close="tourItem = {}"
    append-to-body
  >
    <template #footer>
      <div class="dialog-footer">
        <div class="btn_box cancle_btn" @click="selectTour('第一视角')">
          第一视角
        </div>
        <div class="btn_box cancle_btn" @click="selectTour('第三视角')">
          第三视角
        </div>
      </div>
    </template>
  </el-dialog>
  <!-- 漫游预览视角切换按钮 -->

  <div class="tourView_box" v-if="showMYbtn">
    <div
      class="btn_view"
      @click.stop="changeMY(1)"
      :class="{ active: selectMy === 1 }"
    >
      第一视角
      <div
        class="pleusBtn"
        v-if="selectMy === 1 && showPlayBtn"
        @click.stop="playPauseResume(btnText)"
      >
        {{ btnText }}
      </div>
    </div>
    <div
      class="btn_view"
      @click.stop="changeMY(3)"
      :class="{ active: selectMy === 3 }"
    >
      第三视角
      <div
        class="pleusBtn"
        v-if="selectMy === 3 && showPlayBtn"
        @click.stop="playPauseResume(btnText)"
      >
        {{ btnText }}
      </div>
    </div>
    <el-icon @click="outView">
      <Close />
    </el-icon>
  </div>

  <!-- 导览列表 -->
  <!-- <cameraTourTmpl v-if="showTour" /> -->
</template>

<script setup>
import { ElMessageBox, ElMessage } from "element-plus";
import {
  ref,
  computed,
  onMounted,
  getCurrentInstance,
  onUnmounted,
  onBeforeUnmount,
  watch,
} from "vue";
// api
import {
  getPlanListApi,
  delPlanApi,
  updatePlanApi,
  getPlanInfoForId,
  copyPlanApi,
} from "@/api/plan";
import {
  getDrawDataForScreen,
  getReourceDataForScene,
  getLineListData,
} from "@/api/task/task";
import { updateScreenNew } from "@/api/task/new";
// utils
import { sessionStorage } from "@/utils/storage";
import { flattenTreeData, loadPicture } from "@/utils";
import { clearDraw, drawMarkerScreen } from "./util";
import { drawScreenData } from "../../EmergencyPlan/utils";
import { closeFloors, addTourResouce } from "@/components/SmartMap/js/utils";
import { drawResourceDataAll } from "@/components/SmartMap/js/resource";
import { drawLinesName } from "@/views/WorkCockpit/utils";
import { setAutoCameraTour } from "@/components/SmartMap/js/addPlot";
// components
import CameraTourBIZ from "../setCameraTour/cameraTour";
import AddPlanTask from "./addPlanTask.vue";
import cameraTourTmpl from "./CameraTour.vue";
// store
import usePlanTaskStore from "@/store/modules/planTask";
import useTaskStore from "@/store/modules/taskStore";
import useSettingStore from "@/store/modules/settingStore";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import useFloorStore from "@/store/modules/floorStore";
import useScreenStore from "@/store/modules/screenStore";
import useCameraStore from "@/store/modules/cameraSet";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
const { proxy } = getCurrentInstance();
const WorkCockpitStore = useWorkCockpitStore();
const FloorStore = useFloorStore();
const ScreenStore = useScreenStore();
const TaskStore = useTaskStore();
const SettingStore = useSettingStore();
const PlanTaskStore = usePlanTaskStore();
const cameraStore = useCameraStore();
window._cameraTour = new CameraTourBIZ();
const showTour = computed(() => useTaskStore().isShowFrames);
let floorMarkers = computed(() => useFloorStore().floorMarkers);
let playtimer = ref(null);
const showDialog = ref(false);
// 漫游编辑视角
const mapEvent = computed(() => useSysToolsCimStore().eventSealAPI);
const tourBol = ref(false);
const tourItem = ref({});
const selectMy = ref(1);
const showPlayBtn = ref(true);
const showMYbtn = ref(false);
const btnText = ref("暂停");
// 漫游编辑end
const centerDialogVisible = ref(false);
const updataData = ref({});
const editForm = ref({});
const dialogType = ref("add");
// tab按钮
const tabBtnList = ref([
  { key: "gs", label: "高速", type: "4" },
  { key: "gt", label: "高铁", type: "5" },
  { key: "xc", label: "现场", type: "2" },
  { key: "zd", label: "住地", type: "3" },
  { key: "city", label: "城市道路", type: "1" },
]);
// 创建
const creatPlan = () => {
  updataData.value = {};
  dialogType.value = "add";
  showDialog.value = true;
};
// 编辑
const editPlan = (item) => {
  // dialogType.value = "edit";
  centerDialogVisible.value = true;
  // updataData.value = item;
  // showDialog.value = true;
  editForm.value = item;
};
const changeNumToPlay = async (type,id) => {
  let msg = type===3?"第三视角":"第一视角";
      let params = { sceneId: id, type: "lines", lineType: "0" };
      let lines = await getLineListData(params);
      if (lines.data?.length) {
        let list = [];
        for (const item of lines.data) {
          list.push(item.data.coordinates);
        }
        let keyFramesObj = await setAutoCameraTour(list, type);
        let obj;
        if (type === 1) {
          obj = {
            id: id,
            time: keyFramesObj.time,
            keyFrames: [keyFramesObj],
          };
        } else {
          obj = {
            id: id,
            thirdTime: keyFramesObj.time,
            thirdKeyFrames: [keyFramesObj],
          };
        }
        updateScreenNew(obj).then((res) => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess(`已为您自动生成${msg}漫游`);
            getPlanInfoForId({ id: id }).then(async (res) => {
              let item = res.data;
              tourItem.value = item;
              changeMY(type)
            })
          }
        });
      }else{
        ElMessage.warning("当前场景没有路线，无法自动生成漫游");
      }

}
// 漫游预览切换视角
const changeMY = async (num) => {
  // await window.__g.cameraTour.stop()
  btnText.value = "暂停";
  if (num === 1) {
    if (tourItem.value.keyFrames) {
      showPlayBtn.value = true;
      window._cameraTour.curOa = tourItem.value.keyFrames[0];
      await window._cameraTour.play_biz();
      window.__g.cameraTour.setMouseClickToPause(
        window._cameraTour.curOa.id,
        false
      );
      playtimer.value = setTimeout(() => {
        playtimer.value = null;
      }, Number(window._cameraTour.curOa.time) * 1000);
      selectMy.value = num;
    } else {
      if(tourItem.value.type==='2'||tourItem.value.type==='3'){
        ElMessage({
        message: "当前视角没有漫游数据",
        type: "warning",
      })
      }else{
        changeNumToPlay(1,tourItem.value.id)
      }
    }
  } else {
    if (tourItem.value.thirdKeyFrames) {
      window._cameraTour.curOa = tourItem.value.thirdKeyFrames[0];
      showPlayBtn.value = true;
      await window._cameraTour.play_biz();
      window.__g.cameraTour.setMouseClickToPause(
        window._cameraTour.curOa.id,
        false
      );
      playtimer.value = setTimeout(() => {
        playtimer.value = null;
      }, Number(window._cameraTour.curOa.time) * 1000);
      selectMy.value = num;
    } else {
      if(tourItem.value.type==='2'||tourItem.value.type==='3'){
        ElMessage({
        message: "当前视角没有漫游数据",
        type: "warning",
      })
      }else{
        changeNumToPlay(3,tourItem.value.id)
      }
    }
  }
};
const sureDraw = () => {
  proxy.$refs["drawForm"].validate((valid) => {
    if (valid) {
      updatePlanApi(editForm.value).then((res) => {
        if (res.code === 0) {
          ElMessage.success("修改成功！");
          getBasicData(activeName.value);
          centerDialogVisible.value = false;
        }
      });
    }
  });
};
// 删除
const delPlan = (item) => {
  ElMessageBox.confirm("您确定要删除该常备任务?", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      delPlanApi({ id: item.id }).then((res) => {
        if (res.code === 0) {
          ElMessage.success("删除成功！");
          getBasicData(item.type);
        }
      });
    })
    .catch(() => {
      console.log("已取消");
    });
};
const handleCommand = (data) => {
  console.log(data);
  if (data.type === "copy") {
    copyData(data.items);
  } else {
    delPlan(data.items);
  }
};
// 复制
const copyData = (item) => {
  console.log(item);
  copyPlanApi({ id: item.id }).then((res) => {
    if (res.code === 0) {
      ElMessage.success("复制成功！");
      getBasicData(item.type);
    }
  });
};
// 常备任务选中
const clickScreen = async (item) => {
  console.log(item,'zyd----------');
  PlanTaskStore.set_routerGo(false);
  SettingStore.setShowTaskPanle(false);
  SettingStore.setshowTaskFun(false);
  PlanTaskStore.set_planTaskInfo(item);
  PlanTaskStore.setCurPage(true);
  SettingStore.setShowTool(true);
  WorkCockpitStore.set_showResouce(false);
  WorkCockpitStore.set_checkBox("init", false);
  // 设之场景面板弹框所需信息
  TaskStore.SET_SCREENMODALINFO(item);
  // 设置场景信息
  ScreenStore.set_screenInfo(item);
  // 设置场景能否编辑操作
  ScreenStore.set_editScreen(true);

  // 设置null 因为接口是taskID为空
  TaskStore.SET_TASKINFO({});
};
// 漫游预览
const manyouView = async (info) => {
  getPlanInfoForId({ id: info.id }).then(async (res) => {
    let item = res.data;
    tourItem.value = item;
    useScreenStore().set_editScreen(false);
    useFloorStore().set_isShowFloor(true);
    useTaskStore().SET_TASKPLAN(false);
    useScreenStore().set_screenInfo(item);
    if(item.keyFrames || item.thirdKeyFrames){
      if (item.keyFrames) {
      useTaskStore().SET_KEYFRAMES(item.keyFrames); // 设置第一视角漫游数据
    }
    if (item.thirdKeyFrames) {
      useTaskStore().SET_THIRDKEYFRAMES(item.thirdKeyFrames); // 设置第三视角漫游数据
    }
    // 绘制标绘数据
    showMarkersDrawData(item.id);
    drawToursForReasuce(item.id);
    // 第一视角存在默认播放第一视角
    if (item.keyFrames) {
      window._cameraTour.curOa = item.keyFrames[0];
      selectMy.value = 1;
      showMYbtn.value = true;
      showPlayBtn.value = true;
      btnText.value = "暂停";
    } else {
      // 否则默认选中第三视角
      window._cameraTour.curOa = item.thirdKeyFrames[0];
      selectMy.value = 3;
      showMYbtn.value = true;
      showPlayBtn.value = true;
      btnText.value = "暂停";
    }
    await window._cameraTour.play_biz();
    window.__g.cameraTour.setMouseClickToPause(
      window._cameraTour.curOa.id,
      false
    );
    playtimer.value = setTimeout(() => {
      playtimer.value = null;
    }, Number(window._cameraTour.curOa.time) * 1000);
    }else{
          // 路线
    if(item.type==='2'||item.type==='3'){
      ElMessage({
      message: "当前没有漫游数据，请先编辑添加",
      type: "warning",
    });
    }else{
      madalShowToSetTourCustom(1, item.id)
    }
    }
    // if (!item.keyFrames && !item.thirdKeyFrames) {
    //   return ElMessage({
    //     message: "当前没有漫游数据，请先编辑添加",
    //     type: "warning",
    //   });
    // }

  });
};
const madalShowToSetTourCustom = async(type,id) => {
  let msg = type === 1 ? "第一视角" : "第三视角";
      let params = { sceneId: id, type: "lines", lineType: "0" };
      let lines = await getLineListData(params);
      if (lines.data?.length) {
        let list = [];
        for (const item of lines.data) {
          list.push(item.data.coordinates);
        }
        let keyFramesObj = await setAutoCameraTour(list, type);
        let obj;
        if (type === 1) {
          obj = {
            id: id,
            time: keyFramesObj.time,
            keyFrames: [keyFramesObj],
          };
        } else {
          obj = {
            id: id,
            thirdTime: keyFramesObj.time,
            thirdKeyFrames: [keyFramesObj],
          };
        }
        updateScreenNew(obj).then((res) => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess(`已为您自动生成${msg}漫游`);
            manyouView({id:id})
            // queryTaskInfo({ id: taskId }).then((res) => {
            //   cameraStore.setTaskInfo(res.data);
            //   useTaskStore().SET_TASKINFO(res.data);
            //   let news = taskInfo.value.sceneList.filter(item=>item.id===id)
            //   tourView(news[0])
            // });
          }
        });
      } else {
        ElMessage.warning("当前场景没有路线，无法自动生成漫游");
      }
}
//隐藏所有炸开数据
const hideFloorMarkers = () => {
  if (floorMarkers.value && floorMarkers.value.length > 0) {
    let g = window.__g;
    for (const item of floorMarkers.value) {
      g.marker.delete(item.id);
      g.marker3d.delete(item.id);
    }
  }
};
// 漫游预览的暂停播放
const playPauseResume = (text) => {
  if (text === "暂停") {
    window.__g.cameraTour.pause();
    if (playtimer.value) clearTimeout(playtimer.value);
    playtimer.value = null;
    btnText.value = "播放";
  } else {
    window.__g.cameraTour.resume();
    btnText.value = "暂停";
  }
};
// 退出预览
const outView = () => {
  window._cameraTour.stop_biz();
  showMYbtn.value = false;
  closeFloors();
  hideFloorMarkers();
  useFloorStore().set_isShowFloor(false);
  clearDraw();
};
// 漫游编辑
const manyouEdit = async (info) => {
  if (showMYbtn.value) return ElMessage.warning("请先退出漫游预览");
  getPlanInfoForId({ id: info.id }).then((res) => {
    let item = res.data;
    tourItem.value = item;
    useScreenStore().set_screenInfo(item);
    tourBol.value = true;
    showMarkersDrawData(item.id);
    useFloorStore().set_isShowFloor(true);
    drawToursForReasuce(item.id);
  });
};
// 漫游编辑视角选择
const selectTour = (type) => {
  let id = tourItem.value.id;
  if (type === "第一视角") {
    if (tourItem.value.keyFrames && tourItem.value.keyFrames.length > 0) {
      useTaskStore().SET_KEYFRAMES(tourItem.value.keyFrames); // 设置漫游第一视角数据
      window._cameraTour.curOa = tourItem.value.keyFrames[0];
      window._cameraTour.curOa.name = "第一视角";
      tourSetting(type);
    } else {
      // if (tourItem.value.type !== "2" && tourItem.value.type !== "3") {
      //   madalShowToSetTour(1, id);
      // } else {
        useTaskStore().SET_KEYFRAMES([]); // 设置漫游数据
        window._cameraTour.start();
        tourSetting(type);
      // }
    }
  } else {
    if (
      tourItem.value.thirdKeyFrames &&
      tourItem.value.thirdKeyFrames.length > 0
    ) {
      useTaskStore().SET_THIRDKEYFRAMES(tourItem.value.thirdKeyFrames); // 设置漫游第三视角数据
      window._cameraTour.curOa = tourItem.value.thirdKeyFrames[0];
      window._cameraTour.curOa.name = "第三视角";
      tourSetting(type);
    } else {
      // if (tourItem.value.type !== "2" && tourItem.value.type !== "3") {
      //   madalShowToSetTour(3, id);
      // } else {
        useTaskStore().SET_KEYFRAMES([]); // 设置漫游数据
        window._cameraTour.start();
        tourSetting(type);
      // }
    }
  }
  tourBol.value = false;
};
// 漫游参数设置
const tourSetting = (type) => {
  useTaskStore().set_keyFramesName(type);
  PlanTaskStore.set_roamPage(true);
  useTaskStore().SET_ISSHOWFRAMES(true);
  useScreenStore().set_editScreen(false);
  useTaskStore().SET_TASKPLAN(false);
  useTaskStore().SET_SCREENPLAN(false); // 加强任务场景规划弹窗
  useTaskStore().set_sceneBol(false); // 普通任务场景弹框
};
// 自动弹框生成漫游数据
const madalShowToSetTour = async (type, id) => {
  let msg = type === 1 ? "第一视角" : "第三视角";
  ElMessageBox.confirm(`是否要自动生成${msg}漫游数据`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let params = { sceneId: id, type: "lines", lineType: "0" };
      let lines = await getLineListData(params);
      if (lines.data?.length) {
        let list = [];
        for (const item of lines.data) {
          list.push(item.data.coordinates);
        }
        let keyFramesObj = await setAutoCameraTour(list, type);
        let obj;
        if (type === 1) {
          obj = {
            id: id,
            time: keyFramesObj.time,
            keyFrames: [keyFramesObj],
          };
        } else {
          obj = {
            id: id,
            thirdTime: keyFramesObj.time,
            thirdKeyFrames: [keyFramesObj],
          };
        }
        updateScreenNew(obj).then((res) => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess("已自动生成漫游");
            let g = window.__g
            g.reset()
          }
        });
      } else {
        ElMessage.warning("当前场景没有路线，无法自动生成漫游");
      }
    })
    .catch(() => {
      useTaskStore().SET_KEYFRAMES([]); // 设置漫游数据
      window._cameraTour.start();
      tourSetting(type);
    });
};
// 编辑标绘数据以及楼层炸开数据
const showMarkersDrawData = (id) => {
  // 绘制标绘数据
  // getDrawDataForScreen({ sceneId: id }).then((res) => {
  // drawScreenData(res.data);
  useFloorStore().set_floorMarkers([]);
  closeFloors(); // 清除楼层炸开数据
  // });
};
// 绘制漫游编辑以及漫游预览的资源要素
const drawToursForReasuce = async (sceneId) => {
  getReourceDataForScene({ sceneId }).then(async (res) => {
    WorkCockpitStore.set_basicIds(sceneId, 1);
    if (res.data && res.data?.length) {
      let treeData = res.data || [];
      let treeLayer = flattenTreeData(treeData[0].children);
      let arr = treeLayer.filter((item) => item.coordinates);
      let fileterNode = arr.filter((item) => !item.floorNum);
      drawResourceDataAll(fileterNode, true);
    }
  });
};
const changeCotrlFun = (type) => {
  if (type) {
    activeName.value = type;
    getBasicData(type);
  }
  showDialog.value = false;
};
// 按钮数据
const list = ref([]);
let activeName = ref("4");
// 查询基础数据
const getBasicData = (e) => {
  console.log('aaaaaaaaaaaaaaa')
  activeName.value = e;
  getPlanListApi({ type: e }).then((res) => {
    list.value = res.data;
  });
};
watch(
  () => mapEvent.value,
  (nV, oV) => {
    if (nV) {
      if (nV.eventtype === "CameraTourFinished") {
        // 漫游播放完成
        showPlayBtn.value = false;
      }
    }
  },
  { deep: true, immediate: false }
);
// 绘制
onMounted(() => {
  getBasicData("4");
});
onBeforeUnmount(() => {
  window._cameraTour.stop_biz();
});
onUnmounted(() => {
  window._cameraTour.stop_biz();
});
</script>

<style lang="scss" scoped>
.plan_task_box {
  height: 100%;

  .header {
    display: flex;
    justify-content: end;
    align-items: center;

    .creat_modal {
      width: 120px;
      height: 36px;
      background: #2e5aff;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
      margin-top: 20px;
    }
  }

  .content {
    font-size: 14px;
    height: calc(100% - 48px);
    padding-top: 12px;
    box-sizing: border-box;

    .tabsBox {
      display: flex;
      align-items: center;
      background: rgba(0, 0, 0, 0.1);

      .tab_item {
        flex: 1;
        height: 40px;
        color: #ffffff;
        font-size: 14px;
        font-weight: 500;
        display: flex;
        align-items: center;
        justify-content: center;
        // background: url('../componenets/taskNew/img/yjya1.png') no-repeat;
        background: #033683;
        border: 1px solid rgba(38, 68, 173, 0.3);
        background-size: 100% 100%;
        margin-right: 6px;
        cursor: pointer;
      }

      :last-child {
        margin-right: 0px;
      }

      .active {
        background: url("@/assets/basic/矩形 6186@1x.png") no-repeat;
        background-size: 100% 100%;
      }
    }

    .listBox {
      padding: 20px 0;
      height: calc(100% - 80px);
      overflow: auto;

      .item_list {
        min-height: 130px;
        border-radius: 4px;
        margin-bottom: 20px;
        opacity: 1;
        background: linear-gradient(
          180deg,
          rgba(0, 0, 2, 0.24) 0%,
          rgba(0, 7, 33, 0.38) 99%
        );
        border: 0px solid #274eef;
        position: relative;
        cursor: pointer;

        .type_box {
          position: absolute;
          right: 0;
          top: 0;
          width: 90px;
          height: 34px;
          background: #f2994a;
          color: #ffffff;
          font-size: 16px;
          font-weight: bold;
          line-height: 34px;
          text-align: center;
        }

        .task_name {
          line-height: 23.24px;
          padding: 34px 0 0 22px;
          font-family: Source Han Sans;
          font-size: 16px;
          font-weight: 500;
        }

        .task_name1 {
          line-height: 20px;
          font-size: 16px;
          font-weight: 500;
          padding: 10px 0 10px 22px;
        }

        .bottom_btn {
          display: flex;
          align-items: center;
          margin-top: 7px;
          margin-left: 12px;
          padding-bottom: 10px;

          .edit_btn {
            width: 71px;
            height: 26px;
            line-height: 26px;
            color: #fff;
            cursor: pointer;
            text-align: center;
            background: #274eef;
            font-size: 12px;
          }

          .action_btn {
            width: 58px;
            height: 24px;
            border-radius: 2px;
            opacity: 1;
            box-sizing: border-box;
            border: 0.6px solid rgba(151, 185, 255, 0.6);
            font-size: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;

            .el-icon {
              margin-left: 6px;
            }
          }

          .right_icon {
            font-size: 40px;
            color: #fff;
            margin-left: 16px;
          }
        }
      }
    }
  }
}

:deep(button:focus) {
  outline: none;
}

:deep(.el-button--primary) {
  font-size: 14px;
  --el-button-bg-color: #2e5aff;
  --el-button-border-color: #2e5aff;
  --el-button-hover-bg-color: #3f67f8;
  --el-button-hover-border-color: #3f67f8;
}

:deep(.el-select__wrapper) {
  height: 36px;
  background-color: #0c184b;
  border: 1px solid #2644ad;
  box-shadow: none;
}

:deep(.el-select__wrapper.is-hovering:not(.is-focused)) {
  box-shadow: 0 0 0 1px #3050c5;
}

:deep(.el-select__placeholder) {
  color: rgba(255, 255, 255, 0.2);
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #00ceff;
}

:deep(.el-breadcrumb__inner) {
  color: rgba($color: #fff, $alpha: 0.45);
  cursor: pointer;
}

:deep(.el-breadcrumb__inner:hover) {
  color: #00ceff;
}

:deep(.el-dialog) {
  width: 717px;
  height: 626px;
  background: url("@/assets/emergencyplan/bg_dialog.png") no-repeat center;
  padding: 24px 32px;
  box-sizing: border-box;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #fff;
  font-size: 18px;
  margin: 20px 32px 0 0;
}

:deep(.el-dialog__header.show-close) {
  padding-right: 0;
}

:deep(.el-dialog__body) {
  padding: 34px 80px 0;
  box-sizing: border-box;
}

:deep(.el-dialog__footer) {
  padding-top: 0;
}

:deep(.el-form-item__label) {
  color: #fff;
  font-size: 14px;
}

:deep(.el-input__wrapper) {
  height: 36px;
}

:deep(.el-textarea),
:deep(.el-input) {
  --el-input-bg-color: #0c184b;
  --el-input-border-color: #2644ad;
  --el-input-text-color: #fff;
  --el-input-placeholder-color: rgba(255, 255, 255, 0.2);
  --el-input-hover-border-color: #3c59c5;
  --el-input-focus-border-color: #3c59c5;
}

:deep(.el-radio) {
  --el-radio-text-color: #fff;
  --el-radio-input-bg-color: transparent;
  --el-radio-input-border: 1px solid #5b6799;
}

:deep(.el-radio.el-radio--small .el-radio__inner) {
  width: 14px;
  height: 14px;
}

.tourView_box {
  position: absolute;
  z-index: 3;
  left: 420px;
  bottom: 60px;
  width: 234px;
  // background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
  display: flex;
  align-items: center;

  .btn_view {
    background: rgba(31, 76, 152, 0.8677);
    width: 80px;
    height: 30px;
    text-align: center;
    color: #fff;
    line-height: 30px;
    margin-right: 10px;
    cursor: pointer;
    border-radius: 4px;
    font-size: 14px;
    position: relative;
    .pleusBtn {
      background: #274eef;
      width: 40px;
      height: 20px;
      text-align: center;
      color: #fff;
      line-height: 20px;
      position: absolute;
      top: -25px;
      left: 20px;
      font-size: 12px;
    }
  }

  .active {
    background: #274eef;
  }

  .el-icon {
    font-size: 24px;
    cursor: pointer;
  }
}
</style>
