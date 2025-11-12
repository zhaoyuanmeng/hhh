<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 11:20:59
 * @LastEditors: Alex
-->
<template>
  <div class="TaskPlay_box">
    <div class="goBack" @click="out"></div>
    <div class="icon_up" @click="hideShow(true)" title="收起"></div>
    <div class="icon_down" @click="hideShow(false)" title="展开"></div>
    <div class="hide_text" v-show="hideList">漫游列表</div>
    <el-scrollbar style="background: rgba(21, 30, 73, 0.8)" v-show="!hideList">
      <div class="content_box">
        <div
          class="item"
          v-for="item in tourList"
          :key="item.id"
          :class="{ active: item.active }"
        >
          <div class="top">
            <div class="name">
              {{
                item.type === "1"
                  ? "路线"
                  : item.type === "2"
                  ? "现场"
                  : "住地"
              }}：<span class="rightData">{{ item.sceneName }}</span>
            </div>
          </div>
          <div class="scroll">
            <el-progress
              :percentage="50"
              :show-text="false"
              color="#00CEFF"
            ></el-progress>
          </div>
          <div class="time">
            <div class="time_text">{{ item.beginTime }}</div>
            <div class="time_text">{{ item.endTime }}</div>
          </div>
          <div class="bottom_btn">
            <div class="name">
              负责人：<span class="rightData">{{ item.head }}</span>
            </div>
            <div>
              <div
                class="stop_btn"
                v-if="
                  item.keyFrames.length > 0 &&
                  item.active &&
                  !item.again &&
                  item.haveData &&
                  item.keyFrames &&
                  item.keyFrames[0].keyFrames.length > 0
                "
                @click="stopPlay(item)"
              >
                暂停
              </div>
              <div
                class="stop_btn"
                v-if="
                  (item.again || item.ending) && item.haveData && item.active
                "
                @click="plause(item)"
              >
                播放
              </div>
              <div
                class="change_btn"
                v-if="!item.active"
                @click="changeVideo(item)"
              >
                切换
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-scrollbar>
  </div>
  <!-- 漫游预览视角切换按钮 -->

  <div class="tourView_box">
    <div
      class="btn_view"
      @click="changeMY(1)"
      :class="{ active: selectMy === 1 }"
    >
      第一视角
    </div>
    <div
      class="btn_view"
      @click="changeMY(3)"
      :class="{ active: selectMy === 3 }"
    >
      第三视角
    </div>
    <!-- <el-icon @click="outView"><Close /></el-icon> -->
  </div>

  <!-- marker信息 -->
  <InfoDialog v-if="showMarkerInfo" />
</template>

<script setup>
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  reactive,
  onBeforeMount,
  getCurrentInstance,
  watch,
  nextTick,
} from "vue";
import useTaskStore from "@/store/modules/taskStore";
import { flattenTreeData, loadPicture } from "@/utils";
import useSettingStore from "@/store/modules/settingStore";
import { ElMessage, ElMessageBox } from "element-plus";
import CameraTourBIZ from "../setCameraTour/cameraTour";
import { sessionStorage } from "@/utils/storage";
import { clearDraw, moveDrawAction, drawScreenData } from "./util";
import {
  openFloors,
  closeFloors,
  addTourResouce,
  drawGsGtData,
} from "@/components/SmartMap/js/utils";
import {
  drawResourceDataAll,
  ctrlMoveToSetMarkerMode,
} from "@/components/SmartMap/js/resource";
import { getDrawDataForScreen, getReourceDataForScene } from "@/api/task/task";
import InfoDialog from "@/views/WorkCockpit/taskDetail/components/InfoDialog.vue";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import useScreenStore from "@/store/modules/screenStore";
const emit = defineEmits(["out"]);
const { proxy } = getCurrentInstance();
const taskStore = useTaskStore();
const playList = computed(() => taskStore.taskVideoList);
const isEndPlay = computed(() => taskStore.endPlay);
const drawData = computed(() => taskStore.taskDrawData);
const showMarkerInfo = computed(() => useWorkCockpitStore().showMarkerInfo);
const mapEvent = computed(() => useSysToolsCimStore().eventSealAPI);
const curTourId = computed(() => useWorkCockpitStore().curTourId);
const selectMy = ref(1);
const hideList = ref(false);
const selectItem = ref(null); // 当前选中的数据
let tourList = ref([]); // 每个场景的导览数据
let playtimer = ref(null);
window._cameraTour = new CameraTourBIZ();
// watch(
//   () => isEndPlay.value,
//   (nV, oV) => {
//     if (nV) {
//       tourList.value.forEach((item) => {
//         if (selectItem.value.id === item.id) {
//           item.ending = true;
//           item.again = true
//         }
//       });
//     }
//   },
//   { deep: true, immediate: true }
// );
watch(
  () => mapEvent.value,
  (nV, oV) => {
    if (nV) {
      if (nV.eventtype === "CameraTourStop") {
        // 暂停漫游
        tourList.value.forEach((item) => {
          if (selectItem.value && selectItem.value !== null) {
            if (selectItem.value.id === item.id) {
              item.again = true;
              item.ending = false;
            }
          }
        });
      }
      if (nV.eventtype === "CameraTourFinished") {
        // 漫游播放完成
        tourList.value.forEach((item) => {
          if (selectItem.value && selectItem.value !== null) {
            if (selectItem.value.id === item.id) {
              item.again = true;
              item.ending = true;
            }
          }
        });
      }
    }
  },
  { deep: true, immediate: true }
);
onMounted(() => {
  hideList.value = false;
  closeFloors(); // 关闭楼层炸开
  if (playList.value) {
    tourList.value = playList.value;
    tourList.value.map((item) => {
      item.active = false;
      if (item.keyFrames.length > 0) {
        item.keyFrames[0].play = false;
      }
      item.again = false;
      item.ending = false;
      item.haveData = true;
    });
    if (curTourId.value) {
      let index = tourList.value.findIndex(
        (item) => item.id === curTourId.value
      );
      tourList.value[index].active = true;
      if (
        tourList.value[index].keyFrames.length > 0 &&
        tourList.value[index].keyFrames[0].keyFrames.length > 0
      ) {
        window.__g.camera.set(
          tourList.value[index].keyFrames[0].keyFrames[0].location[0],
          tourList.value[index].keyFrames[0].keyFrames[0].location[1],
          tourList.value[index].keyFrames[0].keyFrames[0].location[2],
          tourList.value[index].keyFrames[0].keyFrames[0].rotation
            ? tourList.value[index].keyFrames[0].keyFrames[0].rotation[0]
            : 0,
          tourList.value[index].keyFrames[0].keyFrames[0].rotation
            ? tourList.value[index].keyFrames[0].keyFrames[0].rotation[1]
            : 0,
          1
        );
        tourList.value[index].keyFrames[0].play = true;
        window._cameraTour.curOa = tourList.value[index].keyFrames[0];
        window._cameraTour.play_biz();
        taskStore.SET_ENDPLAY(false);
        playtimer.value = setTimeout(() => {
          tourList.value[index].keyFrames[0].play = false;
          playtimer.value = null;
        }, Number(tourList.value[index].keyFrames[0].time) * 1000);
        selectItem.value = tourList.value[index];
      } else {
        tourList.value[index].haveData = false;
        proxy.$modal.msgWarning("第一视角下当前场景暂无导览数据！");
      }
    } else {
      tourList.value[0].active = true;

      if (
        tourList.value[0].keyFrames.length > 0 &&
        tourList.value[0].keyFrames[0].keyFrames.length > 0
      ) {
        window.__g.camera.set(
          tourList.value[0].keyFrames[0].keyFrames[0].location[0],
          tourList.value[0].keyFrames[0].keyFrames[0].location[1],
          tourList.value[0].keyFrames[0].keyFrames[0].location[2],
          tourList.value[0].keyFrames[0].keyFrames[0].rotation
            ? tourList.value[0].keyFrames[0].keyFrames[0].rotation[0]
            : 0,
          tourList.value[0].keyFrames[0].keyFrames[0].rotation
            ? tourList.value[0].keyFrames[0].keyFrames[0].rotation[1]
            : 0,
          1
        );
        tourList.value[0].keyFrames[0].play = true;
        window._cameraTour.curOa = tourList.value[0].keyFrames[0];
        window._cameraTour.play_biz();
        tourList.value[0].haveData = true;
        taskStore.SET_ENDPLAY(false);
        playtimer.value = setTimeout(() => {
          tourList.value[0].keyFrames[0].play = false;
          playtimer.value = null;
        }, Number(tourList.value[0].keyFrames[0].time) * 1000);
        selectItem.value = tourList.value[0];
      } else {
        tourList.value[0].haveData = false;
        proxy.$modal.msgWarning("第一视角下当前场景暂无导览数据！");
      }
    }
  }
});
const hideShow = (bol) => {
  hideList.value = bol;
};
// 退出
const out = () => {
  drawGsGtData(0);
  useSettingStore().set_isClickTools(true);
  // window.__g.cameraTour.stop();
  window._cameraTour.stop_biz();
  closeFloors(); // 关闭楼层炸开
  clearDraw().then(() => {
    emit("out");
  });
  taskStore.SET_ENDPLAY(false);
  taskStore.SET_clearModal(true);
  useWorkCockpitStore().setShowMarkerInfo(false);
};
// 切换视角重新播放
const changeMY = async (type) => {
  await window.__g.cameraTour.stop();
  console.log(selectItem.value)
  clearDraw();
  closeFloors(); // 关闭楼层炸开
  selectMy.value = type;
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
  if (type === 1) {
    if (playList.value) {
      tourList.value = playList.value;
      let arrs = [];
      tourList.value.map((item) => {
        item.active = false;
        if (item.keyFrames.length > 0) {
          item.keyFrames[0].play = false;
          arrs.push(
            item.keyFrames[0].time === 0
              ? 8 * 1000
              : item.keyFrames[0].time * 1000
          );
        } else {
          arrs.push(8 * 1000);
        }
        item.again = false;
        item.ending = false;
        item.haveData = true;
      });
      if (curTourId.value) {
        let index = tourList.value.findIndex(
          (item) => item.id === curTourId.value
        );
        tourList.value[index].active = true;
        if (
          tourList.value[index].keyFrames.length > 0 &&
          tourList.value[index].keyFrames[0].keyFrames.length > 0
        ) {
          window.__g.camera.set(
            tourList.value[index].keyFrames[0].keyFrames[0].location[0],
            tourList.value[index].keyFrames[0].keyFrames[0].location[1],
            tourList.value[index].keyFrames[0].keyFrames[0].location[2],
            tourList.value[index].keyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].keyFrames[0].keyFrames[0].rotation[0]
              : 0,
            tourList.value[index].keyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].keyFrames[0].keyFrames[0].rotation[1]
              : 0,
            1
          );
          tourList.value[index].keyFrames[0].play = true;
          window._cameraTour.curOa = tourList.value[index].keyFrames[0];
          window._cameraTour.play_biz();
          taskStore.SET_ENDPLAY(false);
          playtimer.value = setTimeout(() => {
            tourList.value[index].keyFrames[0].play = false;
            playtimer.value = null;
          }, Number(tourList.value[index].keyFrames[0].time) * 1000);
          changeViewToLoadData(tourList.value[index].id);
          selectItem.value = tourList.value[index];
        } else {
          tourList.value[index].haveData = false;
          proxy.$modal.msgWarning("第一视角下当前场景暂无导览数据！");
        }
      } else {
        let index = tourList.value.findIndex(
          (item) => item.id === selectItem.value.id
        );
        tourList.value[index].active = true;
        if (
          tourList.value[index].keyFrames.length > 0 &&
          tourList.value[index].keyFrames[0].keyFrames.length > 0
        ) {
          window.__g.camera.set(
            tourList.value[index].keyFrames[0].keyFrames[0].location[0],
            tourList.value[index].keyFrames[0].keyFrames[0].location[1],
            tourList.value[index].keyFrames[0].keyFrames[0].location[2],
            tourList.value[index].keyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].keyFrames[0].keyFrames[0].rotation[0]
              : 0,
            tourList.value[index].keyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].keyFrames[0].keyFrames[0].rotation[1]
              : 0,
            1
          );
          tourList.value[index].keyFrames[0].play = true;
          window._cameraTour.curOa = tourList.value[index].keyFrames[0];
          window._cameraTour.play_biz();
          taskStore.SET_ENDPLAY(false);
          playtimer.value = setTimeout(() => {
            tourList.value[index].keyFrames[0].play = false;
            playtimer.value = null;
          }, Number(tourList.value[index].keyFrames[0].time) * 1000);
          changeViewToLoadData(tourList.value[index].id);
          selectItem.value = tourList.value[index];
        } else {
          tourList.value[index].haveData = false;
          proxy.$modal.msgWarning("第一视角下当前场景暂无导览数据！");
        }
        // tourList.value[0].active = true;
        // if (
        //   tourList.value[0].keyFrames.length > 0 &&
        //   tourList.value[0].keyFrames[0].keyFrames.length > 0
        // ) {
        //   window.__g.camera.set(
        //     tourList.value[0].keyFrames[0].keyFrames[0].location[0],
        //     tourList.value[0].keyFrames[0].keyFrames[0].location[1],
        //     tourList.value[0].keyFrames[0].keyFrames[0].location[2],
        //     tourList.value[0].keyFrames[0].keyFrames[0].rotation
        //       ? tourList.value[0].keyFrames[0].keyFrames[0].rotation[0]
        //       : 0,
        //     tourList.value[0].keyFrames[0].keyFrames[0].rotation
        //       ? tourList.value[0].keyFrames[0].keyFrames[0].rotation[1]
        //       : 0,
        //     1
        //   );
        //   tourList.value[0].keyFrames[0].play = true;
        //   window._cameraTour.curOa = tourList.value[0].keyFrames[0];
        //   window._cameraTour.play_biz();
        //   taskStore.SET_ENDPLAY(false);
        //   playtimer.value = setTimeout(() => {
        //     tourList.value[0].keyFrames[0].play = false;
        //     playtimer.value = null;
        //   }, Number(tourList.value[0].keyFrames[0].time) * 1000);
        //   changeViewToLoadData(tourList.value[0].id);
        //   selectItem.value = tourList.value[0];
        // } else {
        //   tourList.value[0].haveData = false;
        //   proxy.$modal.msgWarning("第一视角下当前场景暂无导览数据！");
        // }
      }
    }
  } else {
    if (playList.value) {
      tourList.value = playList.value;
      tourList.value.map((item) => {
        item.active = false;
        if (item.thirdKeyFrames.length > 0) {
          item.thirdKeyFrames[0].play = false;
        } 
        item.again = false;
        item.ending = false;
        item.haveData = true;
      });
      if (curTourId.value) {
        let index = tourList.value.findIndex(
          (item) => item.id === curTourId.value
        );
        tourList.value[index].active = true;
        if (
          tourList.value[index].thirdKeyFrames.length > 0 &&
          tourList.value[index].thirdKeyFrames[0].keyFrames.length > 0
        ) {
          changeViewToLoadData(tourList.value[index].id);
          window.__g.camera.set(
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].location[0],
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].location[1],
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].location[2],
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation[0]
              : 0,
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation[1]
              : 0,
            1
          );
          tourList.value[index].thirdKeyFrames[0].play = true;
          window._cameraTour.curOa = tourList.value[index].thirdKeyFrames[0];
          window._cameraTour.play_biz();
          playtimer.value = setTimeout(() => {
            tourList.value[index].thirdKeyFrames[0].play = false;
            playtimer.value = null;
          }, Number(tourList.value[index].thirdKeyFrames[0].time) * 1000);
          selectItem.value = tourList.value[0];
        } else {
          tourList.value[index].haveData = false;
          proxy.$modal.msgWarning("第三视角下当前场景暂无导览数据！");
        }
      } else {
        let index = tourList.value.findIndex(
          (item) => item.id === selectItem.value.id
        );
        tourList.value[index].active = true;
        if (
          tourList.value[index].thirdKeyFrames.length > 0 &&
          tourList.value[index].thirdKeyFrames[0].keyFrames.length > 0
        ) {
          changeViewToLoadData(tourList.value[index].id);
          window.__g.camera.set(
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].location[0],
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].location[1],
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].location[2],
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation[0]
              : 0,
            tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation
              ? tourList.value[index].thirdKeyFrames[0].keyFrames[0].rotation[1]
              : 0,
            1
          );
          tourList.value[index].thirdKeyFrames[0].play = true;
          window._cameraTour.curOa = tourList.value[index].thirdKeyFrames[0];
          window._cameraTour.play_biz();
          playtimer.value = setTimeout(() => {
            tourList.value[index].thirdKeyFrames[0].play = false;
            playtimer.value = null;
          }, Number(tourList.value[index].thirdKeyFrames[0].time) * 1000);
          selectItem.value = tourList.value[0];
        } else {
          tourList.value[index].haveData = false;
          proxy.$modal.msgWarning("第三视角下当前场景暂无导览数据！");
        }
        // tourList.value[0].active = true;
        // if (
        //   tourList.value[0].thirdKeyFrames.length > 0 &&
        //   tourList.value[0].thirdKeyFrames[0].keyFrames.length > 0
        // ) {
        //   changeViewToLoadData(tourList.value[0].id);
        //   window.__g.camera.set(
        //     tourList.value[0].thirdKeyFrames[0].keyFrames[0].location[0],
        //     tourList.value[0].thirdKeyFrames[0].keyFrames[0].location[1],
        //     tourList.value[0].thirdKeyFrames[0].keyFrames[0].location[2],
        //     tourList.value[0].thirdKeyFrames[0].keyFrames[0].rotation
        //       ? tourList.value[0].thirdKeyFrames[0].keyFrames[0].rotation[0]
        //       : 0,
        //     tourList.value[0].thirdKeyFrames[0].keyFrames[0].rotation
        //       ? tourList.value[0].thirdKeyFrames[0].keyFrames[0].rotation[1]
        //       : 0,
        //     1
        //   );
        //   tourList.value[0].thirdKeyFrames[0].play = true;
        //   window._cameraTour.curOa = tourList.value[0].thirdKeyFrames[0];
        //   window._cameraTour.play_biz();
        //   playtimer.value = setTimeout(() => {
        //     tourList.value[0].thirdKeyFrames[0].play = false;
        //     playtimer.value = null;
        //   }, Number(tourList.value[0].thirdKeyFrames[0].time) * 1000);
        //   selectItem.value = tourList.value[0];
        // } else {
        //   tourList.value[0].haveData = false;
        //   proxy.$modal.msgWarning("第三视角下当前场景暂无导览数据！");
        // }
      }
    }
  }
};
// 切换
const changeVideo = async (item) => {
  await window._cameraTour.stop_biz();
  tourList.value.forEach((list) => {
    if (list.id === item.id) {
      list.active = true;
      item.again = false;
      item.ending = false;
      item.haveData = true;
    } else {
      list.active = false;
    }
  });
  closeFloors(); // 清除楼层炸开
  clearDraw(); // 清除之前场景内数据
  selectItem.value = item;
  // window.__g.cameraTour.stop()
  // 判断是否之前定时器开启啦如果有 就清除定时器
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
  taskStore.SET_ENDPLAY(false);
  // 判断当前视角
  if (selectMy.value === 1) {
    if (
      item.keyFrames.length === 0 ||
      item.keyFrames[0].keyFrames.length === 0
    ) {
      selectItem.value = null;
      item.haveData = false;
      proxy.$modal.msgWarning("第一视角下当前场景暂无导览数据！");
      window._cameraTour.stop_biz();
    } else {
      changeViewToLoadData(item.id);
      window.__g.camera.set(
        item.keyFrames[0].keyFrames[0].location[0],
        item.keyFrames[0].keyFrames[0].location[1],
        item.keyFrames[0].keyFrames[0].location[2],
        item.keyFrames[0].keyFrames[0].rotation
          ? item.keyFrames[0].keyFrames[0].rotation[0]
          : 0,
        item.keyFrames[0].keyFrames[0].rotation
          ? item.keyFrames[0].keyFrames[0].rotation[1]
          : 0,
        1
      );
      window._cameraTour.curOa = item.keyFrames[0];
      window._cameraTour.play_biz();
      playtimer.value = setTimeout(() => {
        item.keyFrames[0].play = false;
        playtimer.value = null;
      }, Number(item.keyFrames[0].time) * 1000);
    }
  } else {
    if (
      item.thirdKeyFrames.length === 0 ||
      item.thirdKeyFrames[0].keyFrames.length === 0
    ) {
      item.haveData = false;
      selectItem.value = null;
      proxy.$modal.msgWarning("第三视角下当前场景暂无导览数据！");
      // window.__g.cameraTour.stop();
      window._cameraTour.stop_biz();
    } else {
      changeViewToLoadData(item.id);
      window.__g.camera.set(
        item.thirdKeyFrames[0].keyFrames[0].location[0],
        item.thirdKeyFrames[0].keyFrames[0].location[1],
        item.thirdKeyFrames[0].keyFrames[0].location[2],
        item.thirdKeyFrames[0].keyFrames[0].rotation
          ? item.thirdKeyFrames[0].keyFrames[0].rotation[0]
          : 0,
        item.thirdKeyFrames[0].keyFrames[0].rotation
          ? item.thirdKeyFrames[0].keyFrames[0].rotation[1]
          : 0,
        1
      );
      window._cameraTour.curOa = item.thirdKeyFrames[0];
      window._cameraTour.play_biz();
      playtimer.value = setTimeout(() => {
        item.thirdKeyFrames[0].play = false;
        playtimer.value = null;
      }, Number(item.thirdKeyFrames[0].time) * 1000);
    }
  }
};
// 播放
const plause = (item) => {
  console.log(item, isEndPlay.value);
  if (isEndPlay.value) {
    playAgain(item);
  } else {
    window.__g.cameraTour.resume();
    item.again = false;
    taskStore.SET_ENDPLAY(false);
  }
  // ctrlMoveToSetMarkerMode(true)
};
// 暂停
const stopPlay = async (item) => {
  tourList.value.forEach((list) => {
    if (list.id === item.id) {
      list.again = true;
    } else {
      list.again = false;
    }
  });
  window.__g.cameraTour.pause();
  if (playtimer.value) clearTimeout(playtimer.value);
  playtimer.value = null;
};
// 二次播放
const playAgain = async (item) => {
  await window._cameraTour.stop_biz();
  tourList.value.forEach((list) => {
    if (list.id === item.id) {
      list.active = true;
      item.again = false;
      item.ending = false;
    } else {
      list.active = false;
    }
  });
  selectItem.value = item;
  // 判断是否之前定时器开启啦如果有 就清除定时器
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
  // 判断当前视角
  if (selectMy.value === 1) {
    window.__g.camera.set(
      item.keyFrames[0].keyFrames[0].location[0],
      item.keyFrames[0].keyFrames[0].location[1],
      item.keyFrames[0].keyFrames[0].location[2],
      item.keyFrames[0].keyFrames[0].rotation
        ? item.keyFrames[0].keyFrames[0].rotation[0]
        : 0,
      item.keyFrames[0].keyFrames[0].rotation
        ? item.keyFrames[0].keyFrames[0].rotation[1]
        : 0,
      1
    );
    window._cameraTour.curOa = item.keyFrames[0];
    window._cameraTour.play_biz();
    playtimer.value = setTimeout(() => {
      item.keyFrames[0].play = false;
      playtimer.value = null;
    }, Number(item.keyFrames[0].time) * 1000);
    taskStore.SET_ENDPLAY(false);
  } else {
    window.__g.camera.set(
      item.thirdKeyFrames[0].keyFrames[0].location[0],
      item.thirdKeyFrames[0].keyFrames[0].location[1],
      item.thirdKeyFrames[0].keyFrames[0].location[2],
      item.thirdKeyFrames[0].keyFrames[0].rotation
        ? item.thirdKeyFrames[0].keyFrames[0].rotation[0]
        : 0,
      item.thirdKeyFrames[0].keyFrames[0].rotation
        ? item.thirdKeyFrames[0].keyFrames[0].rotation[1]
        : 0,
      1
    );
    window._cameraTour.curOa = item.thirdKeyFrames[0];
    window._cameraTour.play_biz();
    playtimer.value = setTimeout(() => {
      item.thirdKeyFrames[0].play = false;
      playtimer.value = null;
    }, Number(item.thirdKeyFrames[0].time) * 1000);
    taskStore.SET_ENDPLAY(false);
  }
};
// 切换漫游重新加载当前场景数据
const changeViewToLoadData = async (id) => {
  getDrawDataForScreen({ sceneId: id }).then((res) => {
    // drawScreenData(res.data);
    drawToursForReasuce(id);
  });
};
// 绘制漫游编辑以及漫游预览的资源要素
const drawToursForReasuce = async (sceneId) => {
  getReourceDataForScene({ sceneId }).then((res) => {
    useWorkCockpitStore().set_basicIds(sceneId, 1);
    let treeData = res.data || [];
    let treeLayer = flattenTreeData(treeData[0].children);
    let arr = treeLayer.filter((item) => item.dataLevelFlag);
    let fileterNode = arr.filter((item) => !item.floorNum);
    // addTourResouce(fileterNode);
    drawResourceDataAll(fileterNode, true).then(() => {
      // ctrlMoveToSetMarkerMode(true)
    });
  });
};
//删除计时器
onBeforeUnmount(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
  out();
  // window.__g.cameraTour.stop();
  // window._cameraTour.stop_biz();
});
onUnmounted(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
  out();
  // window._cameraTour.stop_biz();
});
onBeforeMount(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
  // window._cameraTour.stop_biz();
});
</script>

<style lang="scss" scoped>
.TaskPlay_box {
  position: absolute;
  top: 45px;
  left: 0;
  right: 0;
  height: 160px;

  z-index: 88;
  transform: scale(0.8);
  :deep(.el-scrollbar__bar) {
    &.is-vertical {
      width: var(--scrollbar-width) !important;
      background-color: #f5f5f5 !important; /* 轨道背景色 */
      display: none !important;
    }
    .el-scrollbar__bar.is-horizontal {
      opacity: 1 !important; /* 覆盖淡出动画 */
      display: block !important; /* 确保显示 */
      background-color: #f5f5f5; /* 轨道背景色 */
      border-radius: 3px; /* 轨道圆角 */
    }
  }

  :deep(.el-scrollbar__thumb) {
    background-color: #00ceff !important;
    border-radius: 3px !important; /* 滑块圆角 */
    opacity: 1 !important;
    transition: none !important; /* 禁用动画 */
    &:hover {
      background-color: #00ceff !important; /* 悬停颜色 */
      opacity: 1 !important;
    }
  }
  .hide_text {
    width: 100%;
    text-align: center;
    line-height: 30px;
    background: rgba(21, 30, 73, 0.8);
  }

  .goBack {
    width: 26px;
    height: 26px;
    background: #274eef;
    background-image: url("../taskNew/img/close@2x.png");
    background-size: 100% 100%;
    position: absolute;
    right: -30px;
    top: 0px;
    z-index: 100;
    cursor: pointer;
  }

  .icon_up {
    width: 26px;
    height: 26px;
    background: #274eef;
    background-image: url("../taskNew/img/up@2x.png");
    background-size: 100% 100%;
    position: absolute;
    right: -30px;
    top: 40px;
    z-index: 100;
    cursor: pointer;
  }

  .icon_down {
    width: 26px;
    height: 26px;
    background: #274eef;
    background-image: url("../taskNew/img/down@2x.png");
    background-size: 100% 100%;
    position: absolute;
    right: -30px;
    top: 80px;
    z-index: 100;
    cursor: pointer;
  }

  .head_title {
    width: 100%;
    height: 43px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: 700;
    font-size: 16px;
    color: #ffffff;
    text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
    background-image: url("../taskNew/img/task_bg.png");
    background-size: 100% 100%;
    position: relative;
  }

  .content_box {
    // padding-left: 10px;
    display: flex;
    // flex-wrap: nowrap;
    // overflow-x: auto;  //设置容器水平方向出现滚动条。
    // width: calc(100% - 10px);
    width: 100%;

    .item {
      flex: 0 0 auto;
      /* 防止flex项被压缩 */
      width: 300px;
      height: 140px;
      border: 1px solid rgba(151, 185, 255, 0.37);
      margin: 10px 0 10px 10px;
      background: rgba(0, 0, 0, 0.28);

      // padding: 20px;
      .top {
        padding: 20px 20px 0 20px;

        .name {
          font-weight: 400;
          font-size: 14px;
          color: #97b9ff;
          line-height: 24px;
        }

        .rightData {
          color: #fff;
        }
      }

      .scroll {
        margin: 10px 0;
        padding: 0 20px;
      }

      .time {
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-weight: 350;
        font-size: 10px;
        color: #97b9ff;
        padding: 0 20px;
      }

      .bottom_btn {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-top: 20px;
        padding: 0 20px;

        .name {
          font-weight: 400;
          font-size: 14px;
          color: #97b9ff;
          line-height: 24px;
        }

        .rightData {
          color: #fff;
        }

        .stop_btn {
          width: 60px;
          height: 26px;
          background: #204485;
          border-radius: 2px 2px 2px 2px;
          font-size: 12px;
          color: #ffffff;
          line-height: 26px;
          text-align: center;
          cursor: pointer;
        }

        .change_btn {
          width: 60px;
          height: 26px;
          background: #274eef;
          border-radius: 2px 2px 2px 2px;
          font-size: 12px;
          color: #ffffff;
          line-height: 26px;
          text-align: center;
          margin-left: 16px;
          cursor: pointer;
        }
      }
    }

    .active {
      background: rgba(0, 64, 175, 0.19);
      border: 1px solid #97b9ff;
    }
  }
}

.tourView_box {
  position: absolute;
  z-index: 3;
  left: 30px;
  bottom: 30px;
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
