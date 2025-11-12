<template>
  <div class="backHome" v-if="backBol" @click="goBackHome">返回</div>
  <div
    class="func-tpl"
    v-if="
      router.currentRoute.value.name === 'Task' ||
      (router.currentRoute.value.name === 'WorkCockpit' &&
        curPage === 'sceenDetail')
    "
    ref="funcTPL"
    style="transform: scale(0.8)"
  >
    <template v-for="(item, i) in menuList1">
      <div
        v-if="item.show"
        :key="`1_${i}`"
        :class="[
          'pry',
          {
            // 区分多选的工具
            active: multipleChoiceArr.map((i) => i.name).includes(item.label)
              ? multipleChoiceArr.find((i) => i.name == item.label)?.isCheck
              : isCheck(1, item),
          },
        ]"
      >
        <div @click="handleMenu(item)">
          <img :src="item.src" />
          <span style="margin-top: 6px; font-size: 14px">
            {{ item.label }}
          </span>
        </div>

        <div
          v-if="(item.children || []).length > 0"
          :class="['sec', { wrap: (item.children || []).length > 10 }]"
        >
          <template v-for="(jtem, j) in item.children" :key="j">
            <span
              v-if="jtem.show"
              :key="`1_${i}_${j}`"
              @click="handleMenu(jtem)"
              :class="{ active: isCheck(2, jtem) }"
            >
              <img :src="jtem.iconImg" alt="" v-show="!isCheck(2, jtem)" />
              <img :src="jtem.activeIconImg" alt="" v-show="isCheck(2, jtem)" />
              <span style="margin-top: 16px; font-size: 14px">
                {{ jtem.label }}
              </span>
            </span>
          </template>
        </div>
      </div>
    </template>
  </div>

  <!-- 组件 -->

  <drwLine
    v-if="curMenu && curMenu.id === 501 && !switching"
    @close="closeCMPT"
  />
  <drawLineArrows
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 508 && !switching"
  />
  <drawSurface
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 502 && !switching"
  />
  <pullSurface
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 503 && !switching"
  />
  <tag3D
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 505 && !switching"
  />
  <Viewshed
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 301 && !switching"
  />
  <perspective
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 305 && !switching"
  />
  <measurement1
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 306 && !switching"
    :cmptData="curMenu"
  />
  <measurement2
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 307 && !switching"
    :cmptData="curMenu"
  />
  <measurement3
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 308 && !switching"
    :cmptData="curMenu"
  />
  <measurement
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 4 && !switching"
    :cmptData="curMenu"
  />
  <!-- <policeModel @close="closeCMPT" v-if="curMenu && curMenu.id === 1 && !switching" /> -->
  <policeCustomObj
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 1 && !switching"
  />
  <carMode
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 2 && !switching"
  />
  <checkModel
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 66 && !switching"
  />
  <pullSurface
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 6 && !switching"
  />
  <basicWork
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 8 && !switching"
  />
  <BoxSelect
    @close="closeCMPT"
    v-if="curMenu && curMenu.id === 9 && !switching"
  />

  <Police v-if="showPolice" @closeRightBox="closeRightBoxFun"></Police>
  <PoliceCar v-if="showCar" @closeRightBox="closeRightBoxFun" />
  <basicEdit v-if="showBasic" @closeRightBox="closeRightBoxFun" />
  <uavEdit v-if="showUAV" @closeRightBox="closeRightBoxFun" />
  <ajEdit v-if="showAJ" @closeRightBox="closeRightBoxFun" />
</template>

<script setup>
import { useRouter } from "vue-router";
import { showHideLayer } from "@/components/SmartMap/js/utils.js";
import drwLine from "../resourceCatalog/plot/DrawLine.vue"; // 画线
import drawLineArrows from "../resourceCatalog/plot/DrawLineArrows.vue"; // 三维箭头
import drawSurface from "../resourceCatalog/plot/DrawSurface.vue"; // 画面
import pullSurface from "../resourceCatalog/plot/PullSurface.vue"; // 三维多边形
import tag3D from "../resourceCatalog/plot/tag/Tag3D.vue"; // 三维标注
import Viewshed from "../resourceCatalog/analyse/Viewshed.vue"; // 可视域
import perspective from "../resourceCatalog/analyse/Perspective.vue"; // 通视分析
import measurement from "../resourceCatalog/measurement/Measurement.vue"; // 地图框选
import measurement1 from "../resourceCatalog/measurement/MeasurementTool.vue"; // 直线
import measurement2 from "../resourceCatalog/measurement/MeasurementTool.vue"; // 地表面积
import measurement3 from "../resourceCatalog/measurement/MeasurementTool.vue"; // 坐标
import policeModel from "../resourceCatalog/plot/police/police.vue"; // 警力
import policeCustomObj from "../resourceCatalog/plot/police/policeObj.vue"; // 警力自定义对象
import carMode from "../resourceCatalog/plot/policeCar/car.vue"; // 警车
import checkModel from "../resourceCatalog/plot/check/index.vue";
import basicWork from "../resourceCatalog/plot/basic/index.vue"; // 基础工作
import Police from "../resourceCatalog/PoliceAndCar/police.vue"; // 警力编辑
import PoliceCar from "../resourceCatalog/PoliceAndCar/car.vue"; // 警车编辑
import uavEdit from "../resourceCatalog/PoliceAndCar/uav.vue"; // 无人机编辑
import basicEdit from "../resourceCatalog/plot/basic/edit.vue"; // 基础工作编辑
import BoxSelect from "../resourceCatalog/BoxSelect/index.vue"; // 基础工作编辑
import ajEdit from "../resourceCatalog/plot/check/edit.vue"; // 安检编辑
import { DTSTree } from "@/utils/funModule";
import {
  computed,
  getCurrentInstance,
  nextTick,
  onBeforeUnmount,
  onMounted,
  ref,
  watch,
} from "vue";
import { handleTree } from "@/utils";
import { sessionStorage } from "@/utils/storage";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { useToolsStore } from "@/store/modules/tools";
import { getCameraByCode } from "@/api/dataApi";
import useScreenStore from "@/store/modules/screenStore";
import useSettingStore from "@/store/modules/settingStore";
import { customCarToMove } from "@/components/SmartMap/js/addMarkers.js";
import emitter from "@/utils/emitter";
// 驾驶舱store
import useTaskStore from "@/store/modules/taskStore";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import useEmergencyStore from "@/store/modules/emergencyPlan"; //预案store
const curPage = computed(() => useWorkCockpitStore().curPage);

const settingStore = useSettingStore();
const router = useRouter();
let layerData = computed(() => settingStore.layerList);
let backBol = computed(() => settingStore.dxModel);
const SysToolsCimStore = useSysToolsCimStore();
const editObj = ref({});
const showPolice = ref(false);
const showCar = ref(false);
const showBasic = ref(false);
const showUAV = ref(false);
const showAJ = ref(false);
const curMenu = ref(null);
const curChildMenu = ref(null);
const switching = ref(false); // 切换中（菜单）
const menuList1 = ref(DTSTree);
const fullscreen = ref(false);
// 存储工具条需要多选的工具
const multipleChoiceArr = ref([
  {
    name: "全屏展示",
    isCheck: false,
  },
  {
    name: "图层",
    isCheck: false,
  },
]);

const func = ref();
const ToolsStore = useToolsStore();
onMounted(async function () {
  window.Player = getCurrentInstance();
});

const isCheck = computed(() => (level, data) => {
  if (!curMenu.value) return;
  return changeMenu(level, data);
});
const contrlShow = computed(() => settingStore.isClickTools);

const menuSlect = computed(() => settingStore.selectMenu);
const isCheckTuCeng = computed(() => {
  return ToolsStore.$state.LayerTreeShow;
});
const changeMenu = (level, data) => {
  switch (level) {
    case 1:
      return data.id === curMenu.value.id || data.id === curMenu.value.pid;
    case 2:
      return data.id === curMenu.value.id;
  }
};

/**
 * 菜单点击
 */
const handleMenu = async (data) => {
  console.log(data);
  // 关闭任务详情面板
  // const taskLevel=useTaskStore().taskInfo.taskLevel
  // taskLevel==='一级加强'? useTaskStore().SET_SCREENPLAN(false) : useTaskStore().set_sceneBol(false)
  useTaskStore().SET_SCREENPLAN(false);
  useTaskStore().set_sceneBol(false);
  useWorkCockpitStore().setShowSceenRightCard(false); // 驾驶舱三级页面详情
  useEmergencyStore().setDetailsPage(false); // 预案详情页
  emitter.emit("barChange", false);
  useScreenStore().set_colorWidth({})
  settingStore.set_isClickTools(false);
  settingStore.set_selectMenu(data);
  showPolice.value = false;
  showCar.value = false;
  showBasic.value = false;
  showUAV.value = false;
  showAJ.value = false;
  sessionStorage.remove("groupId"); // 每次点击都清掉缓存
  // 多选工具单独处理
  if (multipleChoiceArr.value.map((item) => item.name.includes(data.label))) {
    const item = multipleChoiceArr.value.find(
      (item) => item.name == data.label
    );
    item ? (item.isCheck = !item.isCheck) : "";
  }
  // 工具选择
  switch (data.label) {
    case "图层":
      {
        if (!isCheckTuCeng.value) {
          g.infoTree.hide(ids);
        } else {
          g.infoTree.show(ids);
        }
      }
      break;
    case "全屏展示":
      {
        fullscreen.value = !fullscreen.value;
        if (fullscreen.value) {
          const app = document.body;
          launchFullScreen(app);
        } else {
          document.exitFullscreen();
        }
      }
      break;

    default:
      {
        switching.value = true;
        SysToolsCimStore.SET_ISSHOWCLIP(false);
        if (
          !!curMenu.value &&
          (curMenu.value.id === data.id || curMenu.value.pid === data.id)
        ) {
          // 二级菜单取消选中（显示选框）
          if (Object.prototype.hasOwnProperty.call(data, "pid")) {
            curMenu.value = menuList1.value.find(
              (item) => item.id === data.pid
            );
            curChildMenu.value = null;
          } else {
            if (data.id === 0) {
              showHideLayer(false);
            }
            if (data.id === 7) {
              let g = window.__g;
              g.customObject.clear();
            }
            curMenu.value = null;
            curChildMenu.value = null;
            // 显示详情面板
            // taskLevel==='一级加强'? useTaskStore().SET_SCREENPLAN(true) : useTaskStore().set_sceneBol(true)
            useTaskStore().set_sceneBol(true);
            useWorkCockpitStore().setShowSceenRightCard(true);
            console.log("显示");
            useEmergencyStore().setDetailsPage(true); // 预案详情页
            emitter.emit("barChange", true);
          }
        } else {
          if (data.id === 505) {
            sessionStorage.set("groupId", "3dMarkers"); // 三维标注
          }
          if (data.id === 508) {
            sessionStorage.set("groupId", "line"); // 三维标注
            useWorkCockpitStore().set_checkBox("line", true);
          }
          if (data.id === 8) {
            sessionStorage.set("groupId", "basic"); // 三维标注
          }
          if (data.id === 1) {
            sessionStorage.set("groupId", "police"); // 警用标注
          }
          if (data.id === 2) {
            sessionStorage.set("groupId", "car"); // 三维标注
          }
          if (data.id === 66) {
            sessionStorage.set("groupId", "fbaj"); // 防爆安检
          }
          if (data.id === 6) {
            sessionStorage.set("groupId", "cordon"); // 警戒线
            useWorkCockpitStore().set_checkBox("line", true);
          }
          if (data.id === 9) {
            sessionStorage.set("groupId", "mapDrawSelect"); // 地图框选
          }
          if (data.id === 0) {
            showHideLayer(true);
          }
          if (data.id === 7) {
            customCarToMove();
          }
          curMenu.value = data;
          curChildMenu.value = data;
          setTimeout(() => {
            switching.value = false;
          }, 300);
        }
      }
      break;
  }
};

// 回到首页初始化

const goBackHome = () => {
  showHideLayer(false);
  settingStore.setDXModel(false);
  if (!switching.value) handleMenu(curMenu.value);
};
const closeRightBoxFun = (val) => {
  showPolice.value = false;
  showCar.value = false;
  showBasic.value = false;
  showUAV.value = false;
  showAJ.value = false;
  showPolice.value = val;
  showCar.value = val;
  showBasic.value = val;
  showUAV.value = val;
  showAJ.value = val;
  SysToolsCimStore.SET_MARKEREVENT({});
  sysToolsCimStore.SET_MAPCLICKTYPE(false);
};

// 关闭子组件
const closeCMPT = function () {
  // 显示详情面板
  const taskLevel = useTaskStore().taskInfo.taskLevel;
  // taskLevel==='一级加强'? useTaskStore().SET_SCREENPLAN(true) : useTaskStore().set_sceneBol(true)
  useTaskStore().set_sceneBol(true);
  useWorkCockpitStore().setShowSceenRightCard(true);
  useEmergencyStore().setDetailsPage(true); // 预案详情页
  emitter.emit("barChange", false);
  if (!switching.value) handleMenu(curMenu.value);
};
//全屏展示
const launchFullScreen = function (element) {
  if (element.requestFullscreen) {
    element.requestFullscreen();
  } else if (element.mozRequestFullScreen) {
    element.mozRequestFullScreen();
  } else if (element.webkitRequestFullscreen) {
    element.webkitRequestFullscreen();
  } else if (element.msRequestFullscreen) {
    element.msRequestFullscreen();
  }
};

/**
 * 视频弹窗所用
 */
const popTitle = ref("");
const popShowFlag = ref(false);
const msgPositionStyle = ref({
  width: "490px",
  height: "350px",
});
const popupCloseHandle = function () {
  popShowFlag.value = false;
};

const stbmValue = computed(() => SysToolsCimStore.stbmValue);
const popVideoPos = computed(() => SysToolsCimStore.popVideoPos);
const mapEvent = computed(() => SysToolsCimStore.eventSealAPI);
const flagModal = ref(false);
watch(
  () => stbmValue.value,
  (nV, oV) => {
    if (nV) {
      popShowFlag.value = false;
      //   popTitle = "GL-RD-N3-NY-205"
      popTitle.value = nV.split("-").slice(0, 5).join("-");
      msgPositionStyle.value.left = popVideoPos.value[0] - 200 + "px";
      msgPositionStyle.value.top = popVideoPos.value[1] - 290 + "px";
      nextTick(async () => {
        popShowFlag.value = true;
        await initPopVideo();
      });
    }
  }
);
// 监听当前路由的name变化
watch(
  () => router.currentRoute.value.name,
  (newRouterName) => {
    if (sessionStorage.get("groupId")) {
      if (Object.keys(menuSlect.value).length !== 0) {
        handleMenu(menuSlect.value);
      }
    }
  },
  { immediate: true }
);
watch(
  () => contrlShow.value,
  (nV, oV) => {
    if (nV) {
      console.log(nV, menuSlect.value);
      if (nV) {
        if (sessionStorage.get("groupId")) {
          if (Object.keys(menuSlect.value).length !== 0) {
            handleMenu(menuSlect.value);
          }
        }
      }
    }
  },
  { deep: true, immediate: false }
);
watch(
  () => mapEvent.value,
  (nV, oV) => {
    // 任务和驾驶舱三级页面显示
    const isTask =
      router.currentRoute.value.name === "Task" ||
      useWorkCockpitStore().curPage === "sceenDetail";
    if (nV) {
      if (
        (nV.GroupID === "police" ||
          nV.GroupID === "car" ||
          nV.GroupID === "basic" ||
          nV.GroupID === "uav" ||
          nV.GroupID === "fbaj") &&
        nV.eventtype === "LeftMouseButtonClick" &&
        (nV.Type === "marker" ||
          nV.Type === "MarkerLayer" ||
          nV.Type === "CustomObj"||
          nV.Type==='marker3d')
      ) {
        // if (nV.GroupID === "resource" && isTask ) {
        //   showBasic.value = true;
        //   SysToolsCimStore.SET_MARKEREVENT(nV);
        // }
        if (
          (nV.GroupID === "police" ||
            nV.GroupID === "car" ||
            nV.GroupID === "uav") &&
          isTask
        ) {
          if (nV.Type === "CustomObj"||nV.Type === "marker3d") {
            if (nV.GroupID === "police") {
              showPolice.value = true;
              SysToolsCimStore.SET_MARKEREVENT(nV);
            }
            if (nV.GroupID === "car") {
              showCar.value = true;
              SysToolsCimStore.SET_MARKEREVENT(nV);
            }
            if (nV.GroupID === "uav") {
              showUAV.value = true;
              SysToolsCimStore.SET_MARKEREVENT(nV);
            }
          }
        }
        // if (nV.GroupID === "police" && isTask ) {
        //   showPolice.value = true;
        //   SysToolsCimStore.SET_MARKEREVENT(nV);
        // }
        // if (nV.GroupID === "car" && isTask ) {
        //   showCar.value = true;
        //   SysToolsCimStore.SET_MARKEREVENT(nV);
        // }
        if (nV.GroupID === "basic" && isTask) {
          showBasic.value = true;
          SysToolsCimStore.SET_MARKEREVENT(nV);
        }
        // if (nV.GroupID === 'uav' && isTask) {
        //   showUAV.value = true
        //   SysToolsCimStore.SET_MARKEREVENT(nV);
        // }
        if (nV.GroupID === "fbaj" && isTask) {
          showAJ.value = true;
          SysToolsCimStore.SET_MARKEREVENT(nV);
        }
        editObj.value = nV;
        console.log(editObj.value);
      }
    }
  },
  { deep: true, immediate: false }
);
const initPopVideo = async function () {
  const res = await getCameraByCode({
    stbm: stbmValue.value,
    protocol: "wss",
  }).then(async (res) => {
    if (res.data && res.data.url) {
      await testHKWS(res.data.url);
    }
  });
};
let player = null;
const testHKWS = async (url) => {
  // let player
  player = null;
  player = new window.JSPlugin({
    szId: "popVideoId",
    szBasePath: "/js/h5player",
    oStyle: {
      border: "#ff0000",
      borderSelect: "#FFCC00",
      background: "#000",
    },
  });
  let mode = 1;
  let playURL = url;
  await player
    .JS_Play(
      playURL,
      {
        playURL,
        mode,
      },
      0
    )
    .then(() => {
      (err) => {
        console.log("播放失败");
      };
    });
  initPlugin();
};
// 事件初始化
const initPlugin = () => {
  player
    .JS_SetWindowControlCallback({
      windowEventSelect(iWindIndex) {
        // 插件选中窗口回调
        this.iWind = iWindIndex;
      },
      pluginErrorHandler(iWindIndex, iErrorCode, oError) {
        // 插件错误回调
        // console.error(
        //     `window-${iWindIndex}, errorCode: ${iErrorCode}`,
        //     oError
        // )
      },
      windowEventOver(iWindIndex) {
        // 鼠标移过回调
        // console.log(iWindIndex)
      },
      windowEventOut(iWindIndex) {
        // 鼠标移出回调
        // console.log(iWindIndex)
      },
      windowFullCcreenChange(bFull) {
        // 全屏切换回调
        // console.log(bFull)
      },
      firstFrameDisplay(iWndIndex, iWidth, iHeight) {
        // 首帧显示回调
        // console.log(iWndIndex, iWidth, iHeight)
      },
      performanceLack(iWndIndex) {
        // 性能不足回调
        // console.log(iWndIndex)
      },
    })
    .then(() => {
      player
        .JS_SetOptions({
          bSupportSound: true, // 是否支持音频，默认支持
          bSupportDoubleClickFull: false, // 是否双击窗口全屏，默认支持
          bOnlySupportMSE: false, // 只支持 MSE
          bOnlySupportJSDecoder: true, // 只支持 JSDecoder
        })
        .then(() => {});
    });
};

/**
 * 环境传感器
 */
const sysToolsCimStore = useSysToolsCimStore();
const evPopTitle = ref("");
const evPopShowFlag = computed(() => sysToolsCimStore.evPopShowFlag);
const evInfoPopup = ref();

watch(
  () => evPopShowFlag.value,
  (nV, oV) => {
    if (nV) {
      nextTick(function () {
        evInfoPopup.value.initInfoData(
          sysToolsCimStore.evPopInFo.stbm,
          sysToolsCimStore.evPopInFo.pointid,
          sysToolsCimStore.evPopInFo.subType
        );
      });
    }
  }
);

const evPopupClose = function () {
  sysToolsCimStore.SET_EVPOPSHOWFLAG(false);
};

onBeforeUnmount(() => {
  popShowFlag.value = false;
  sysToolsCimStore.SET_EVPOPSHOWFLAG(false);
});
</script>

<style lang="scss" scoped>
.backHome {
  position: fixed;
  top: 100px;
  right: 100px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  width: 70px;
  border: 2px solid red;
  cursor: pointer;
  border-radius: 6px;
  z-index: 10;
}

.func-tpl {
  position: fixed;
  z-index: 999;
  bottom: 4px;
  left: calc(33%);
  transform: translateX(-50%);
  margin-right: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url("../../../assets/panel/工具栏bg.png");
  background-size: contain;
  // box-shadow: 0px 2px 8px 0px rgba(0, 31, 44, 0.3);
  // background-color: rgba(0, 13, 31, 0.6);
  background: linear-gradient(#001446 0%, rgba(0, 39, 105, 0.6) 100%);
  border-radius: 0px 0px 0px 0px;
  border: 1px solid #090831;

  // border: 2px solid rgba(44, 225, 255, 0.4);
  // level_1
  .pry {
    position: relative;
    padding: 0 5px;
    height: 100%;

    // 选中
    &.active {
      & > div:not(.sec) {
        border-bottom: 2px solid #2ce1ff;

        span {
          color: #48ebff;
          transform: translateY(2px);
        }
      }

      .sec {
        display: flex !important;
      }
    }

    & > div:not(.sec) {
      height: 70%;
      position: relative;
      min-width: 78px;
      padding: 10px 0;
      // border-radius: 12px;
      cursor: pointer;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
    }

    img {
      width: 24px;
      height: 24px;
    }
  }

  // level_2
  .sec {
    height: 54px;
    position: absolute;
    bottom: calc(100% + 16px);
    left: 50%;
    transform: translateX(-50%);

    align-items: center;
    justify-content: center;
    display: none;
    background: linear-gradient(#001446 0%, rgba(0, 39, 105, 0.6) 100%);
    border-radius: 0px 0px 0px 0px;
    border: 1px solid #090831;
    padding: 10px 10px;

    span {
      color: #fff;
      width: 85px;
      height: 100%;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      white-space: nowrap;
      // margin: 0 5px;
      font-size: 14px;
      // padding: 5px 15px;
      cursor: pointer;

      img {
        width: 24px;
        height: 24px;
      }

      &.active {
        color: #2ce1ff;

        span {
          color: #2ce1ff;
        }
      }
    }
  }
}

.iframe {
  position: absolute;
  width: 0px;
  height: 0px;
  overflow: hidden;
}

.video_div {
  width: 100% !important;
  height: 100% !important;
  border: 1px solid rgba(255, 255, 255, 0.5);
}

.pop_video_div {
  width: 100%;
  height: 250px;
}
</style>
