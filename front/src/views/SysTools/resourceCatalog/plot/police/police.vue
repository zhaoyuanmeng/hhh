<!--
 * @FileDescription: 标绘->三维标注（标签）
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
  <div>
    <div v-show="!isShowAdd" class="cloud-func tag">
      <div class="func-title">
        警力
        <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>
      <div class="func-warp">
        <el-scrollbar>
          <draggable
            :list="labelList"
            item-key="src"
            @start="drag = true"
            @end="onDragEnd"
          >
            <template #item="{ element }">
              <div class="material-list">
                <ul
                  @click.stop="clickPoint(element)"
                  :style="{
                    border: element.select ? '2px solid #274eef' : 'none',
                  }"
                >
                  <img :src="element.src" alt="" srcset="" />
                  <div class="name">{{ element.name }}</div>
                </ul>
              </div>
            </template>
          </draggable>
          <div class="jysb">警用设备</div>
          <draggable
            :list="labelList1"
            item-key="src"
            @start="drag = true"
            @end="onDragEnd1"
          >
            <template #item="{ element }">
              <div class="material-list" style="height: 100px;">
                <ul
                  @click.stop="clickPointUat(element)"
                  :style="{
                    border: element.select ? '2px solid #274eef' : 'none',
                  }"
                >
                  <img :src="element.src" alt="" srcset=""  style="height: 48px;"/>
                  <div class="name">{{ element.name }}</div>
                </ul>
              </div>
            </template>
          </draggable>
        </el-scrollbar>
      </div>
    </div>
    <!-- 新增标签 -->
    <add-tmpl ref="add" v-if="isShowAdd" />
    <MarkerDrawLine
      v-if="addLine"
      @close="addLine = false"
      @getLineInfo="getLineInfoFun"
    />
    <!-- 新增标绘 -->
    <ScreenModal v-if="screenModal" />
    <el-dialog
      v-model="centerDialogVisible"
      title="请选择游动哨的路线"
      width="300px"
      center
      :show-close="false"
      :destory-on-close="true"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
      <div class="list" v-for="(item, index) in lineList" :key="index">
        <!-- <el-checkbox
          v-model="item.isChecked"
          :checked="item.isChecked"
          @change="checkedList(item, index)"
        >
        </el-checkbox> -->
        <div class="list-left">
          <svg-icon icon-class="cloud-huaxian" class-name="icon"></svg-icon>
          <span>{{ item.name }}</span>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="centerDialogVisible = false"
            >确定</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, computed, watch, defineEmits } from "vue";
import draggable from "vuedraggable";
import useTaskStore from "@/store/modules/taskStore";
import useFloorStore from "@/store/modules/floorStore";
import useBasicStore from "@/store/modules/basicData";
import useScreenStore from "@/store/modules/screenStore";
import useEmergencyStore from "@/store/modules/emergencyPlan"; // 预案store
import ScreenModal from "@/components/addScreenModal";
import { loadPicture } from "@/utils";
import AddTmpl from "./addModel.vue";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import { saveScreenDraw, getMarkersForFloorAllData } from "@/api/task/task";
import { ElMessage, ElMessageBox } from "element-plus";
import MarkerDrawLine from "../MarkerDrawLine.vue";
const { proxy } = getCurrentInstance();
const emit = defineEmits(["close"]);
let eventMap = computed(() => useBasicStore().eventSealAPI);
const screenModal = computed(() => useScreenStore().screenModal);
const editScreen = computed(() => useScreenStore().editScreen);
const y_a_Info = computed(() => useEmergencyStore().YAInfo); // 预案信息
const y_aEdit = computed(() => useEmergencyStore().editYA); // 预案是否调用接口
const openFloor = computed(() => useFloorStore().openFloor); // 楼层是否炸开
const floornum = computed(() => useFloorStore().floornum); // 当前楼层点击楼层数
const taskLevel = computed(() => useTaskStore().taskInfo.taskLevel);
//当前点击的楼栋名称
let explodebuildname = computed(() => useFloorStore().explodebuildname);
const isShowAdd = ref(false);
const drag = ref(false);
const items = ref({});
const addLine = ref(false);
const centerDialogVisible = ref(false);
const lineList = ref([]);
const labelList1 = ref([
  {
    src: loadPicture("./images/cloud/police/wrj.png"),
    name: "无人机反制",
  },
]);
const labelList = ref([
  {
    src: loadPicture("./images/cloud/police/icon-0.png"),
    name: "交通哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-1.png"),
    name: "固定哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-2.png"),
    name: "随卫哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-3.png"),
    name: "社控哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-4.png"),
    name: "昼夜哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-5.png"),
    name: "制高点哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-6.png"),
    name: "游动哨",
  },
  {
    src: loadPicture("./images/cloud/police/icon-7.png"),
    name:
      useTaskStore().taskInfo.taskLevel === "一级加强"
        ? "快反力量"
        : "机动力量",
  },
]);
watch(
  () => eventMap.value,
  (nV, oV) => {
    if (nV) {
      if (nV.eventtype === "LeftMouseButtonClick") {
        addPoint(nV.MouseClickPoint, items.value);
      }
    }
  },
  { deep: true, immediate: false }
);
const clickPoint = (el) => {
  labelList.value.forEach((item) => {
    if (el.name === item.name) {
      if (el.name === "游动哨") {
        let lineData = sessionStorage.get("QXZS_pointLine");
        let policeData = sessionStorage.get("policeData");
        let lines = [];
        if (lineData && lineData.length > 0) {
          if (policeData && policeData.length > 0) {
            lines = removeCommonById(lineData, policeData);
          } else {
            lines = lineData;
          }
        } else {
          lines = [];
        }
        ElMessageBox.confirm("请配置游动哨路线，您可以直接新增路线！", "提示", {
          confirmButtonText: "新增路线",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        })
          .then(() => {
            addLine.value = true;
          })
          .catch(() => {});
      } else {
        item.select = true;
        useBasicStore().setAddPoint(true);
        items.value = item;
        proxy.$modal.msg("请点击地图选择点位");
      }
    } else {
      item.select = false;
    }
  });
};
const clickPointUat = (el) => {
  labelList1.value.forEach((item) => {
    if (el.name === item.name) {
      item.select = true;
      useBasicStore().setAddPoint(true);
      items.value = item;
      proxy.$modal.msg("请点击地图选择点位");
    } else {
      item.select = false;
    }
  });
};
const extractImagePath = (url) => {
  const parts = url.split("/images/");
  return parts.length > 1 ? "/images/" + parts[1] : null;
};
// 绘制场景标绘数据
const keepImagesAndAfter = (url) => {
  return extractImagePath(url);
};
const getLineInfoFun = async (obj) => {
  console.log(obj);
  let g = window.__g;
  var customObj = {
    id: `custom${new Date().getTime()}`, //自定义对象唯一id
    pakFilePath: "@path:DTS_Library_6.1_240731.pak", //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
    assetPath: "/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_B_1", //资源目录，自定义对象在pak文件资源包里的相对路径
    location: obj.coordinates[0], //位置坐标
    coordinateType: 0, // 坐标系类型
    rotation: [0, 0, 0], // 世界坐标系旋转
    range: [0, 10000], //可见范围
    groupId: "coGroup", //分组id
    localRotation: [0, 0, 0], //模型自身旋转
    scale: [1, 1, 1], //模型缩放
    isEffectRotation: true, //是否开启旋转效果
    smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
    supportAttach: false, //不支持贴画贴合
    visible: false, //模型加载后默认是否显示
  };
  var tagObject = {
    id: `police${new Date().getTime()}`,
    userData: "游动哨",
    groupId: "police",
    coordinate: obj.coordinates[0], //坐标位置
    anchors: [-13, 70], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
    imageSize: [26, 70], //图片的尺寸
    range: [0, 500], //可视范围
    imagePath:
      HostConfig.ImagePath +
      keepImagesAndAfter(loadPicture("./images/cloud/police/icon-6.png")),
    fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    text: "游动哨", //显示的文字
    useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    textRange: [1, 100], //文本可视范围[近裁距离, 远裁距离]
    textOffset: [-34, -50], // 文本偏移
    textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
    fontSize: 8, //字体大小
    fontOutlineSize: 1, //字体轮廓线大小
    fontColor: Color.White, //字体颜色
    showLine: false, //标注点下方是否显示垂直牵引线
    lineSize: [1, 5], //垂直牵引线宽度和高度[width, height]
    lineColor: Color.SpringGreen, //垂直牵引线颜色
    lineOffset: [15, 0], //垂直牵引线偏移
    autoHeight: false, // 自动判断下方是否有物体
    displayMode: 0, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
    priority: 0, //避让优先级
    occlusionCull: true, //是否参与遮挡剔除
  };
  g.marker.add(tagObject);
  g.customObject.add(customObj);
  let allObj = {
    id: tagObject.id,
    info: { lineData: obj, custom: customObj },
    marker: tagObject,
  };
  if (editScreen.value) {
    useScreenStore().set_screenDrawData(allObj);
    useScreenStore().set_screenModal(true);
  }
  if (y_aEdit.value) {
    if (openFloor.value && floornum.value) {
      allObj.info.buildName = explodebuildname.value;
      allObj.info.floorNum = floornum.value;
    }
    let query = {
      sceneId: y_a_Info.value.id,
      type: allObj.marker.groupId,
      data: allObj,
    };
    saveScreenDraw(query).then((res) => {
      if (res.code === 0) {
        ElMessage.success("新增成功");
        if (openFloor.value && floornum.value) {
          getMarkersForFloorAllData({ buildName: explodebuildname.value }).then(
            (res) => {
              if (res.data?.length) {
                useFloorStore().set_floorMarkers(res.data);
              }
            }
          );
        }
      }
    });
  }
  let data1 = sessionStorage.get("policeData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.id);
    newData.push(allObj);
    sessionStorage.set("policeData", newData);
  } else {
    sessionStorage.set("policeData", [allObj]);
  }
};
function removeCommonById(array1, array2) {
  // 创建一个Set来存储array2中所有对象的id
  const ids = new Set(array2.map((item) => item.id));
  // 使用filter方法过滤掉array1中id在ids Set中的对象
  return array1.filter((item) => !ids.has(item.id));
}
// 无人机反制结束绘制
const onDragEnd1 = async (event) => {};
const onDragEnd = async (event) => {};

const addPoint = (arr, obj) => {
  let g = window.__g;
  if (obj.name === "无人机反制") {
    let o = {
      id: `police${new Date().getTime()}`,
      coordinate: arr, //辐射圈坐标位置
      coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
      radius: 100, //辐射半径
      rippleNumber: 5, //波纹数量
      color: "#130FEB", //颜色
      intensity: 0.5, //亮度
      autoHeight: false, //自动判断下方是否有物体
      userData: "无人机",
      groupId: "uav",
    };
    g.radiationPoint.add(o);
    var tagObject = {
      id: o.id,
      userData: "无人机反制",
      groupId: "uav",
      coordinate: arr, //坐标位置
      anchors: [-15, 34], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
      imageSize: [30, 34], //图片的尺寸
      range: [0, 18000], //可视范围
      imagePath:
        HostConfig.ImagePath +
        keepImagesAndAfter(loadPicture("./images/cloud/police/wrj.png")),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: "无人机", //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 10000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [-34, -30], // 文本偏移
      textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
      fontSize: 8, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      showLine: false, //标注点下方是否显示垂直牵引线
      lineSize: [1, 5], //垂直牵引线宽度和高度[width, height]
      lineColor: Color.SpringGreen, //垂直牵引线颜色
      lineOffset: [15, 0], //垂直牵引线偏移
      autoHeight: false, // 自动判断下方是否有物体
      displayMode: 2, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
    };
    g.marker.add(tagObject);
    let allObj = {
      id: o.id,
      info: {},
      uav: o,
      marker: tagObject,
    };
    if (editScreen.value) {
      useScreenStore().set_screenDrawData(allObj);
      useScreenStore().set_screenModal(true);
    }
    if (y_aEdit.value) {
      if (openFloor.value && floornum.value) {
        allObj.info.buildName = explodebuildname.value;
        allObj.info.floorNum = floornum.value;
      }
      let query = {
        sceneId: y_a_Info.value.id,
        type: allObj.marker.groupId,
        data: allObj,
      };
      saveScreenDraw(query).then((res) => {
        if (res.code === 0) {
          ElMessage.success("新增成功");
          if (openFloor.value && floornum.value) {
            getMarkersForFloorAllData({
              buildName: explodebuildname.value,
            }).then((res) => {
              if (res.data?.length) {
                useFloorStore().set_floorMarkers(res.data);
              }
            });
          }
        }
      });
    }
    let data1 = sessionStorage.get("uavData");
    if (data1 && data1.length > 0) {
      let newData = data1.filter((item) => item.id !== allObj.id);
      newData.push(allObj);
      sessionStorage.set("uavData", newData);
    } else {
      sessionStorage.set("uavData", [allObj]);
    }
  } else {
    var tagObject = {
      id: `police${new Date().getTime()}`,
      userData: obj.name,
      groupId: "police",
      coordinate: arr, //坐标位置
      anchors: [-13, 70], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
      imageSize: [26, 70], //图片的尺寸
      range: [0, 500], //可视范围
      // viewHeightRange:[0, 10000], 
      imagePath: HostConfig.ImagePath + keepImagesAndAfter(obj.src),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: obj.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 100], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [-34, -50], // 文本偏移
      textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
      fontSize: 8, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      showLine: false, //标注点下方是否显示垂直牵引线
      lineSize: [1, 5], //垂直牵引线宽度和高度[width, height]
      lineColor: Color.SpringGreen, //垂直牵引线颜色
      lineOffset: [15, 0], //垂直牵引线偏移
      autoHeight: false, // 自动判断下方是否有物体
      displayMode: 0, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
    };
    g.marker.add(tagObject);

    let allObj = {
      id: tagObject.id,
      info: {},
      marker: tagObject,
    };
    if (editScreen.value) {
      useScreenStore().set_screenDrawData(allObj);
      useScreenStore().set_screenModal(true);
    }
    if (y_aEdit.value) {
      if (openFloor.value && floornum.value) {
        allObj.info.buildName = explodebuildname.value;
        allObj.info.floorNum = floornum.value;
      }
      let query = {
        sceneId: y_a_Info.value.id,
        type: allObj.marker.groupId,
        data: allObj,
      };
      saveScreenDraw(query).then((res) => {
        if (res.code === 0) {
          ElMessage.success("新增成功");
          if (openFloor.value && floornum.value) {
            getMarkersForFloorAllData({
              buildName: explodebuildname.value,
            }).then((res) => {
              if (res.data?.length) {
                useFloorStore().set_floorMarkers(res.data);
              }
            });
          }
        }
      });
    }
    let data1 = sessionStorage.get("policeData");
    if (data1 && data1.length > 0) {
      let newData = data1.filter((item) => item.id !== allObj.id);
      newData.push(allObj);
      sessionStorage.set("policeData", newData);
    } else {
      sessionStorage.set("policeData", [allObj]);
    }
  }

  clearAddPoint();
};
const clearAddPoint = () => {
  items.value = {};
  useBasicStore().setAddPoint(false);
  labelList.value.forEach((item) => {
    item.select = false;
  });
  labelList1.value.forEach((item) => {
    item.select = false;
  });
};
const handleClose = async () => {
  clearAddPoint();
  emit("close");
};

// 功能新增
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

.list {
  display: flex;
  align-items: center;
  margin-bottom: 16px;

  .list-left {
    margin-left: 16px;
  }
}

.cloud-func {
  // height: 450px !important;
}

.tag .func-warp {
  width: 370px;
  padding: 10px 12px;

  :deep(.el-scrollbar__wrap) {
    max-height: 720px;
    margin-bottom: 0 !important;
    overflow-x: hidden;

    .label-warp-list {
      margin-bottom: 12px;
      height: 164px;
      position: relative;
      background: #4b5059;
      box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5);
      border-radius: 16px;
      border: 2px solid #4b5059;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      &.active {
        border-color: #02b2ff;
      }

      .warp-list-top {
        width: 100%;
        height: 100%;
        padding: 18px;
        padding-bottom: 0;
        box-sizing: border-box;
        text-align: center;
        overflow: hidden;

        li {
          font-size: 18px;
          font-weight: 400;
          color: #ffffff;
          text-align: left;
        }

        img {
          width: auto;
          height: auto;
        }
      }

      .el-dropdown {
        position: absolute;
        top: 18px;
        right: 18px;
      }
    }
  }

  .warp-footer {
    display: flex;
    justify-content: center;
    margin-top: 10px;

    div {
      width: 100%;
      line-height: 38px;
      cursor: pointer;
      border-radius: 5px;
      border: 1px solid #029eff;
      text-align: center;
    }
  }

  :deep(.el-scrollbar__view) {
    height: 500px;
  }

  .material-list {
    cursor: pointer;
    width: 100px;
    height: 130px;
    margin-bottom: 5px;
    margin-right: 8px;
    background: #363840;
    border-radius: 9px;
    border: 1px solid #63646a;
    display: inline-block;
    text-align: center;
    padding: 7px;
    box-sizing: border-box;
    overflow: hidden;

    ul {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;

      img {
        // width: auto;
        // height: auto;
        // max-width: 100%;
        // max-height: 100%;
        width: 26px;
        height: 70px;
      }

      .name {
        // padding: 10px 0;
        padding-top: 12px;
      }
    }
  }

  .jysb {
    line-height: 50px;
    font-size: 22px;
    color: #ffffff;
  }
}
</style>
