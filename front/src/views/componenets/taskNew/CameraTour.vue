<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 09:43:08
 * @LastEditors: Alex
-->
<template>
  <div class="cameraList_box">
    <div
      class="left_box"
      style="
        background: linear-gradient(
          180deg,
          #0a1d64 0%,
          rgba(21, 30, 73, 1) 100%
        );
      "
    >
      <div class="heard">{{ keyName }}漫游编辑</div>
      <div class="content">
        <div v-show="false">{{ zhanwei }}</div>
        <div class="item_box">
          <div
            class="importantPoint"
            v-for="(item, i) in oaList"
            :key="i"
            @click="handleClick(item, i)"
            :class="item.index === styleId ? 'activs_style' : ''"
          >
            <div class="btn_icon">
              <div class="icon_btn" title="删除" @click.stop="deleteKey(i)">
                <el-icon><Delete /></el-icon>
              </div>
              <div class="icon_btn" title="刷新" @click.stop="resetKey(i)">
                <el-icon><Refresh /></el-icon>
              </div>
            </div>
            <div class="index_data" v-if="item.image">漫游镜头{{ i + 1 }}</div>
            <div class="index_data" v-else>自动漫游{{ i + 1 }}</div>
            <div class="model"></div>
            <img class="camera-img" :src="item.image" v-if="item.image" />
          </div>
          <div class="camera">
            <div class="img_add" @click="handleAdd"></div>
          </div>
        </div>
      </div>
      <div class="setting_time">
        <span style="margin-right: 6px">单帧漫游时长设定：</span>
        <el-input-number
          v-model="tourTimes"
          :min="1"
          :max="1000"
          @change="handleChange"
          size="large"
          style="width: 120px"
        />
        <span style="margin-left: 4px">S</span>
      </div>
      <div class="save_box">
        <div @click="goback" class="sub_btn">返回</div>
        <div class="sub_btn sure_btn" @click="handleSure">保存</div>
        <div
          class="sub_btn sure_btn"
          @click="madalShowToSetTour(keyName, screenInfo.id, screenInfo.taskId)"
          style="width: 110px"
          v-if="screenInfo.type !== '2'&&screenInfo.type !== '3'"
        >
          一键生成漫游
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  reactive,
  nextTick,
  getCurrentInstance,
  defineEmits,
} from "vue";
import { closeFloors } from "@/components/SmartMap/js/utils";
import { setAutoCameraTour } from "@/components/SmartMap/js/addPlot";
import { clearDraw } from "./util";
import useTaskStore from "@/store/modules/taskStore";
import useScreenStore from "@/store/modules/screenStore";
import { updateScreenNew } from "@/api/task/new";
import { saveRoamData, queryTaskInfo } from "@/api/task/index";
import{getLineListData } from '@/api/task/task'
import usePlanTaskStore from "@/store/modules/planTask";
import { ElMessage, ElMessageBox } from "element-plus";
import { dataType } from "element-plus/es/components/table-v2/src/common.mjs";
import emitter from "@/utils/emitter";
const { proxy } = getCurrentInstance();
const emit = defineEmits(["closeTour"]);
const taskStore = useTaskStore();
let screenInfo = computed(() => useScreenStore().screenInfo);
let roamPage = computed(() => usePlanTaskStore().roamPage);
let keyName = computed(() => taskStore.keyFramesName);
// 详情信息数据
//   let taskInfo = computed(()=>cameraStore.taskInfo)
// 漫游总时长设定
let tourTimes = ref(3);
//导览的名称
let styleId = ref("");
let name = ref("");
//存放各个帧的信息数组
let oaList = ref([]);
//选中的帧索引
let checkCuri = ref();
//鼠标所在的索引悬浮
//上一帧播放到当前帧的时间
let time = ref(0);
//总计时长
let allTime = ref(0);
//是否为新建
let isNew = ref(true);
//是否播放导览
let playCameraTour = ref(false);
//setTimeout计时器
let playtourtimer = ref(null);
onMounted(() => {
  console.log("漫游编辑");
  refreshData();
});
// 修改时长
const handleChange = (val) => {
  console.log(val);
};
//刷新
function refreshData() {
  oaList.value = window._cameraTour.curOa.keyFrames;
  console.log(oaList.value, window._cameraTour.curOa);
  styleId.value = "";
  if (oaList.value.length > 1) {
    isNew.value = false;
    allTime.value = oaList.value[oaList.value.length - 1].time;
    // time.value = 3;
    tourTimes.value = oaList.value[1].time;
    name.value = window._cameraTour.curOa.name;
  } else {
    //   name.value = screenInfo.value.name
  }
}
let zhanwei = ref(true);
const handleAdd = () => {
  if (tourTimes.value === 0 && oaList.value.length > 0) {
    return ElMessage({
      message: "相邻两帧时间不能为0秒",
      type: "warning",
    });
  }
  checkCuri.value = null;
  allTime.value += tourTimes.value;
  window._cameraTour.curOa.time = allTime.value;
  // time.value = 3;
  time.value = tourTimes.value;
  window._cameraTour.setKeyFrames(1).then(() => {
    zhanwei.value = !zhanwei.value;
    oaList.value[oaList.value.length - 1].image = getCurCameraImage();
  });
};
//获取当前图片
function getCurCameraImage() {
  var video = document.getElementById("player").querySelector("video");
  var canvas = document.createElement("canvas");
  var scale = video.videoWidth / video.videoHeight;
  canvas.width = 300;
  canvas.height = parseInt(300 / scale);
  canvas.getContext("2d").drawImage(video, 0, 0, canvas.width, canvas.height);
  const base64 = canvas.toDataURL("image/png", 0.7);
  return base64;
}
//确定
async function handleSure() {
  if (oaList.value.length < 1) {
    return ElMessage({
      message: "导览不能为空",
      type: "warning",
    });
  }
  var curOa = window._cameraTour.curOa;
  curOa.name = name.value;
  curOa.time = allTime.value;
  curOa.image = oaList.value[0].image; // 导览保存第一帧
  let copycurOa = cloneDeep(curOa);
  if (isNew.value) {
    saveGuide(copycurOa);
    window._cameraTour.finish();
  } else {
    editGuide(copycurOa);
  }
  await stopTour();
  // cameraStore.setisShowListShow(true)
}
const madalShowToSetTour = async (name, id, taskId) => {
  let params = { sceneId: id, type: "lines", lineType: "0" };
  let lines = await getLineListData(params);
  if (lines.data?.length) {
    let list = [];
    for (const item of lines.data) {
      list.push(item.data.coordinates);
    }
    let type = name === "第一视角" ? 1 : 3;
    let keyFramesObj = await setAutoCameraTour(list, type);
    let obj;
    if (name === "第一视角") {
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
        closeFloors();
        clearDraw().then(() => {
          taskStore.SET_ISSHOWFRAMES(false);
          usePlanTaskStore().set_roamPage(false);
          emit("closeTour");
        });
        if (screenInfo.value.taskId) {
          queryTaskInfo({ id: screenInfo.value.taskId }).then((res) => {
            taskStore.SET_TASKINFO(res.data);
          });
        }
      }
    });
  } else {
    ElMessage.warning("当前场景没有路线，无法自动生成漫游");
  }
};
//深拷贝对象
function cloneDeep(source, hash = new WeakMap()) {
  if (typeof source !== "object" || source === null) {
    return source;
  }
  if (hash.has(source)) {
    return hash.get(source);
  }
  const target = Array.isArray(source) ? [] : {};
  Reflect.ownKeys(source).forEach((key) => {
    const val = source[key];
    if (typeof val === "object" && val != null) {
      target[key] = cloneDeep(val, hash);
    } else {
      target[key] = val;
    }
  });
  return target;
}
// 新增时保存导览到store
async function saveGuide(options) {
  let obj;
  if (keyName.value === "第一视角") {
    obj = {
      id: screenInfo.value.id,
      time: options.time,
      keyFrames: [options],
    };
  } else {
    obj = {
      id: screenInfo.value.id,
      thirdTime: options.time,
      thirdKeyFrames: [options],
    };
  }
  var curOa = window._cameraTour.curOa;
  if (keyName.value === "第一视角") {
    if (obj.keyFrames?.length && obj.keyFrames[0].keyFrames?.length) {
      obj.keyFrames.map((item, index) => {
        // item.time = item.keyFrames.length * 3 - 3;
        // item.keyFrames.map((child, i) => {
        //   child.time = child.index * 3;
        // });
        curOa.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        item.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        item.keyFrames.map((child, i) => {
          child.time = child.index * tourTimes.value;
        });
      });
      obj.time = obj.keyFrames[0].time;
    }
  } else {
    if (obj.thirdKeyFrames?.length && obj.thirdKeyFrames[0].keyFrames?.length) {
      obj.thirdKeyFrames.map((item, index) => {
        curOa.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        item.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        // item.time = item.keyFrames.length * 3 - 3;
        item.keyFrames.map((child, i) => {
          // child.time = child.index * 3;
          child.time = child.index * tourTimes.value;
        });
      });
      obj.thirdTime = obj.thirdKeyFrames[0].time;
    }
  }
  // console.log(obj,'121323')
  updateScreenNew(obj).then((res) => {
    if (res.code === 0) {
      proxy.$modal.msgSuccess("已保存");

      closeFloors();
      clearDraw().then(() => {
        taskStore.SET_ISSHOWFRAMES(false);
        usePlanTaskStore().set_roamPage(false);
        emit("closeTour");
      });
      if (screenInfo.value.taskId) {
        queryTaskInfo({ id: screenInfo.value.taskId }).then((res) => {
          taskStore.SET_TASKINFO(res.data);
        });
      }
    }
  });
}
// 编辑后保存导览到store
async function editGuide(options) {
  let obj;
  if (keyName.value === "第一视角") {
    obj = {
      id: screenInfo.value.id,
      time: options.time,
      keyFrames: [options],
    };
  } else {
    obj = {
      id: screenInfo.value.id,
      thirdTime: options.time,
      thirdKeyFrames: [options],
    };
  }
  var curOa = window._cameraTour.curOa;
  if (keyName.value === "第一视角") {
    if (obj.keyFrames?.length && obj.keyFrames[0].keyFrames?.length) {
      obj.keyFrames.map((item, index) => {
        // item.time = item.keyFrames.length * 3 - 3;
        curOa.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        item.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        item.keyFrames.map((child, i) => {
          // child.time = child.index * 3;
          child.time = child.index * tourTimes.value;
        });
      });
      obj.time = obj.keyFrames[0].time;
    }
  } else {
    if (obj.thirdKeyFrames?.length && obj.thirdKeyFrames[0].keyFrames?.length) {
      obj.thirdKeyFrames.map((item, index) => {
        // item.time = item.keyFrames.length * 3 - 3;
        curOa.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        item.time = item.keyFrames.length * tourTimes.value - tourTimes.value;
        item.keyFrames.map((child, i) => {
          // child.time = child.index * 3;
          child.time = child.index * tourTimes.value;
        });
      });
      obj.thirdTime = obj.thirdKeyFrames[0].time;
    }
  }

  // console.log(obj,'121323')
  updateScreenNew(obj).then((res) => {
    if (res.code === 0) {
      proxy.$modal.msgSuccess("已保存");

      closeFloors();
      clearDraw().then(() => {
        emit("closeTour");
        taskStore.SET_ISSHOWFRAMES(false);
        usePlanTaskStore().set_roamPage(false);
      });
      if (screenInfo.value.taskId) {
        queryTaskInfo({ id: screenInfo.value.taskId }).then((res) => {
          taskStore.SET_TASKINFO(res.data);
        });
      }
    }
  });
}
//点击到某一帧的事件
function handleClick(item, i) {
  styleId.value = item.index;
  if (checkCuri.value === i) {
    i > 0 ? (time.value = 3) : (time.value = 0);
    checkCuri.value = null;
    return;
  }
  stopTour();

  checkCuri.value = i;
  if (i == 0) {
    time.value = 0;
  } else {
    time.value = item.time - oaList.value[i - 1].time;
  }
  if (item.rotation) {
    window.__g.camera.set(
      item.location[0],
      item.location[1],
      item.location[2],
      item.rotation[0],
      item.rotation[1],
      0.5
    );
  } else {
    window.__g.camera.set(
      item.location[0],
      item.location[1],
      item.location[2],
      0.5
    );
  }
}
//停止
async function stopTour() {
  if (playCameraTour.value) {
    playCameraTour.value = false;
    window._cameraTour.stop_biz();
    if (playtourtimer.value) clearTimeout(playtourtimer.value);
    playtourtimer.value = null;
  }
}
function deleteKey(i) {
  var curTime =
    i > 0
      ? oaList.value[i].time - oaList.value[i - 1].time
      : oaList.value[i].time;
  oaList.value.splice(i, 1);
  window._cameraTour.setKeyFrames(4, i, curTime).then(() => {
    allTime.value = oaList.value[oaList.value.length - 1].time;
    oaList.value.length > 0 ? (time.value = 3) : (time.value = 0);
  });
}
function resetKey(i) {
  if (time.value === 0 && i > 0) {
    return ElMessage({
      message: "相邻两帧时间不能为0秒",
      type: "warning",
    });
  }
  window._cameraTour.setKeyFrames(2, i, time.value).then(() => {
    allTime.value = oaList.value[oaList.value.length - 1].time;
    oaList.value[i].image = getCurCameraImage();
  });
}
const goback = () => {
  oaList.value = [];
  window._cameraTour.stop_biz();
  closeFloors();
  clearDraw().then(() => {
    emit("closeTour");
    taskStore.SET_ISSHOWFRAMES(false);
    usePlanTaskStore().set_roamPage(false);
  });
};
const refreshList = async () => {
  if (roamPage.value) {
    // emitter.emit("refreshListData")
    usePlanTaskStore().set_roamPage(false);
  }
};
function insetKey(i) {
  if (time.value === 0) {
    return ElMessage({
      message: "相邻两帧时间不能为0秒",
      type: "warning",
    });
  }
  window._cameraTour.setKeyFrames(3, i, time.value).then(() => {
    allTime.value = oaList.value[oaList.value.length - 1].time;
    oaList.value[i + 1].image = getCurCameraImage();
  });
}
</script>

<style lang="scss" scoped>
:deep(.el-input-number__decrease) {
  color: #fff;
  background: #274eef;
}
:deep(.el-input-number__increase) {
  color: #fff;
  background: #274eef;
}
:deep(.el-input__wrapper) {
  background: none;
  color: #fff;
  .el-input__inner {
    color: #fff;
  }
}
.cameraList_box {
  .left_box {
    position: absolute;
    transform: scale(0.8);
    z-index: 3;
    left: -2%;
    bottom: -50px;
    top: -20px;
    width: 400px;
    background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
    display: flex;
    flex-direction: column;
    transition: width 0.1s; /* 添加过渡效果 */
    .heard {
      height: 40px;
      background: url("../../../assets/panel/panel_bg.png") no-repeat;
      background-size: 100% 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: PingFangSC;
      font-weight: 400;
      font-size: 14px;
      color: #ffffff;
      text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
      font-style: normal;
    }
    .content {
      // flex: 1;
      // padding: 16px;
      // padding-bottom: 80px;
      // overflow: auto;
      padding: 16px;
      padding-bottom: 0px;
      overflow: auto;
      height: calc(100% - 200px);
      .item_box {
        overflow: auto;
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        .importantPoint {
          cursor: pointer;
          flex: 1;
          margin: 0 12px 12px 0; // 间隙为5px
          background: rgba(0, 0, 0, 0.3);
          border-radius: 8px;
          height: 88px;
          font-family: PingFang SC;
          position: relative;
          width: calc(
            (100% - 10px) / 2
          ); // 这里的10px = (分布个数2-1)*间隙5px, 可以根据实际的分布个数和间隙区调整
          min-width: calc(
            (100% - 24px) / 2
          ); // 加入这两个后每个item的宽度就生效了
          max-width: calc(
            (100% - 24px) / 2
          ); // 加入这两个后每个item的宽度就生效了
          &:nth-child(2n) {
            margin-right: 0;
          }

          .camera-img {
            width: 100%;
            height: 100%;
          }
          .model {
            visibility: hidden;
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: radial-gradient(rgba(0, 0, 0, 0) 0%, #000000 100%);
          }
          .btn_icon {
            visibility: hidden;
            position: absolute;
            z-index: 2;
            right: 8px;
            top: 8px;
            .icon_btn {
              cursor: pointer;
              margin-bottom: 8px;
            }
          }
          .index_data {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            font-weight: 600;
            font-size: 16px;
            color: #ffffff;
            z-index: 1;
          }
        }

        .importantPoint:hover {
          border: 2px solid #274eef;
          .model {
            visibility: visible;
          }
          .btn_icon {
            visibility: visible;
          }
        }
        .activs_style {
          .model {
            visibility: visible;
            border: 1px solid #00ffff;
          }
        }
        .camera {
          flex: 1;
          margin: 0 12px 12px 0; // 间隙为5px
          width: calc(
            (100% - 10px) / 2
          ); // 这里的10px = (分布个数2-1)*间隙5px, 可以根据实际的分布个数和间隙区调整
          min-width: calc(
            (100% - 24px) / 2
          ); // 加入这两个后每个item的宽度就生效了
          max-width: calc(
            (100% - 24px) / 2
          ); // 加入这两个后每个item的宽度就生效了
          &:nth-child(2n) {
            margin-right: 0;
          }
          height: 88px;
          font-family: PingFang SC;
          background: rgba(0, 0, 0, 0.5);
          border-radius: 8px;
          border: 1px solid #5b6799;
          display: flex;
          align-items: center;
          justify-content: center;
          .img_add {
            width: 48px;
            height: 48px;
            background: url("../../../assets/panel/camrea.png") no-repeat;
            background-size: contain;
            cursor: pointer;
          }
        }
        .addTask {
          margin: 0 16px;
          height: 56px;
          background: rgba(0, 0, 0, 0.5);
          border-radius: 12px 12px 12px 12px;
          border: 1px solid #5b6799;
          display: flex;
          align-items: center;
          justify-content: center;
          cursor: pointer;
        }
      }
    }
    .setting_time {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 40px;
      margin: 20px 0;
      .el-input-number__decrease {
        background: #274eef !important;
      }
    }
    .save_box {
      display: flex;
      justify-content: center;
      align-items: center;
      position: absolute;
      bottom: 20px;
      left: 0;
      right: 0;
      .sub_btn {
        width: 88px;
        height: 36px;
        line-height: 36px;
        text-align: center;
        background: rgba(31, 76, 152, 0.87);
        border-radius: 4px 4px 4px 4px;
        cursor: pointer;
      }
      .sure_btn {
        margin-left: 16px;
        background: #274eef;
      }
    }
  }
}
</style>
