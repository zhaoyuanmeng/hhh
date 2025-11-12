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
          <draggable :list="labelList" item-key="src">
            <template #item="{ element }">
              <div class="material-list">
                <ul @click.stop="clickPoint(element)" :style="{
                  border: element.select ? '2px solid #274eef' : 'none',
                }">
                  <img :src="element.src" alt="" srcset="" />
                  <div class="name">{{ element.name }}</div>
                </ul>
              </div>
            </template>
          </draggable>
          <div class="jysb">警用设备</div>
          <draggable :list="labelList1" item-key="src">
            <template #item="{ element }">
              <div class="material-list" style="height: 100px">
                <ul @click.stop="clickPointUat(element)" :style="{
                  border: element.select ? '2px solid #274eef' : 'none',
                }">
                  <img :src="element.src" alt="" srcset="" style="height: 65px" />
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
    <MarkerDrawLine v-if="addLine" @close="addLine = false" @getLineInfo="getLineInfoFun" />
    <!-- 新增标绘 -->
    <ScreenModal v-if="screenModal" />
    <el-dialog v-model="centerDialogVisible" title="请选择游动哨的路线" width="300px" center :show-close="false"
      :destory-on-close="true" :close-on-click-modal="false" class="my_Dialog">
      <div class="list" v-for="(item, index) in lineList" :key="index">
        <div class="list-left">
          <svg-icon icon-class="cloud-huaxian" class-name="icon"></svg-icon>
          <span>{{ item.name }}</span>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="centerDialogVisible = false">确定</el-button>
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
    pak: '@path:DTS_Library_6.1_240731.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/其他/无人机_1'
  },
]);
const labelList = ref([
  {
    src: loadPicture("./images/cloud/police/icon-0.png"),
    name: "交通哨",
    pak: '@path:人物打包.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警'
  },
  {
    src: loadPicture("./images/cloud/police/icon-1.png"),
    name: "固定哨",
    pak: '@path:DTS_Library_6.1_240731.pak',
    path: '/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1'
  },
  {
    src: loadPicture("./images/cloud/police/icon-2.png"),
    name: "随卫哨",
    pak: '@path:人物打包.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察'
  },
  {
    src: loadPicture("./images/cloud/police/icon-3.png"),
    name: "社控哨",
    pak: '@path:人物打包.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察'
  },
  {
    src: loadPicture("./images/cloud/police/icon-4.png"),
    name: "昼夜哨",
    pak: '@path:人物打包.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察'
  },
  {
    src: loadPicture("./images/cloud/police/icon-5.png"),
    name: "制高点哨",
    pak: '@path:人物打包.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察'
  },
  {
    src: loadPicture("./images/cloud/police/icon-6.png"),
    name: "游动哨",
    pak: '@path:人物打包.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察'
  },
  {
    src: loadPicture("./images/cloud/police/icon-7.png"),
    pak: '@path:人物打包.pak',
    path: '/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵',
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
          .catch(() => { });
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
  g.polyline.delete(obj.id);
  var markerObj = {
    id: `police${new Date().getTime()}`,
    userData: "游动哨",
    groupId: "police",
    coordinate: obj.coordinates[0], //坐标位置
    coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    text: '游动哨', //显示的文字
    textSize: 120,//3D标注显示文字大小
      textColor: '#000080',//3D标注显示文字颜色
      textOutlineSize: 1,//3D标注显示文字轮廓大小
      textOutlineColor: Color.Black,// 3D标注显示文字轮廓颜色
      textFixed: false,// 3D标注显示文字是否固定文本朝向
      fixedSize: true,// 默认尺寸 非近大远小
      textVisible: true,//3D标注显示文字是否显示文本
      textLocation: [0, 0, 0.1],// 文字位置
      textRotation: [0, 90, 0],// 文字旋转
      textScale: [1, 1, 1],// 文字缩放
      pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A',//3D标注展示的特效名称
      pointVisible: true,//3D标注是否显示
      pointScale: 1,//3D标注整体缩放比例
      range: [1, 2000], //3D标注的可视距离范围：[min,max]，单位：米
      autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
      collision: true //默认开启碰撞
  };
  g.marker3d.add(markerObj);
  let customObj = {
    id: markerObj.id,
    pakFilePath:'@path:人物打包.pak', //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
    assetPath:'/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察', //资源目录，自定义对象在pak文件资源包里的相对路径
    location: obj.coordinates[0], //位置坐标
    coordinateType: 0, // 坐标系类型
    rotation: [0, 0, 0], // 世界坐标系旋转
    range: [0, 10000], //可见范围
    groupId: "police",
    userData: markerObj.id,
    localRotation: [0, 90, 0], //模型自身旋转
    scale: [1.3, 1.3, 1.3], //模型缩放
    isEffectRotation: true, //是否开启旋转效果
    smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
    supportAttach: true, //不支持贴画贴合
    visible: true, //模型加载后默认是否显示
  };
  g.customObject.add(customObj);
  g.marker3d.setAttachCustomObject([
    {
      marker3dId: markerObj.id, //标注id
      objectId: customObj.id, //自定义对象id
      offset: [0, 0, 2], //偏移量
    }
  ]);
  let allObj = {
    id: markerObj.id,
    info: { lineData: obj },
    customData: customObj,
    marker: markerObj,
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
const addPoint = (arr, obj) => {
  let g = window.__g;
  if (obj.name === "无人机反制") {
    var markerObj = {
      id: `police${new Date().getTime()}`,
      groupId: "uav",
      userData: '无人机反制',
      coordinate: arr, //坐标位置
      coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      text: '无人机', //显示的文字
      textSize: 120,//3D标注显示文字大小
      textColor: '#000080',//3D标注显示文字颜色
      textOutlineSize: 1,//3D标注显示文字轮廓大小
      textOutlineColor: Color.Black,// 3D标注显示文字轮廓颜色
      textFixed: false,// 3D标注显示文字是否固定文本朝向
      fixedSize: true,// 默认尺寸 非近大远小
      textVisible: true,//3D标注显示文字是否显示文本
      textLocation: [0, 0, 0.1],// 文字位置
      textRotation: [0, 90, 0],// 文字旋转
      textScale: [1, 1, 1],// 文字缩放
      pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A',//3D标注展示的特效名称
      pointVisible: true,//3D标注是否显示
      pointScale: 1,//3D标注整体缩放比例
      range: [1, 2000], //3D标注的可视距离范围：[min,max]，单位：米
      autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
      collision: true //默认开启碰撞
    };
    g.marker3d.add(markerObj);
    let uvaObj = {
      id: markerObj.id,
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
    g.radiationPoint.add(uvaObj);
    let customObj = {
      id: markerObj.id, //自定义对象唯一id
      pakFilePath: obj.pak, //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
      assetPath: obj.path, //资源目录，自定义对象在pak文件资源包里的相对路径
      location: arr, //位置坐标
      coordinateType: 0, // 坐标系类型
      rotation: [0, 0, 0], // 世界坐标系旋转
      range: [0, 10000], //可见范围
      groupId: "uav",
      userData: markerObj.id,
      localRotation: [0, 0, 0], //模型自身旋转
      scale: [1, 1, 1], //模型缩放
      isEffectRotation: true, //是否开启旋转效果
      smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
      supportAttach: true, //不支持贴画贴合
      visible: true, //模型加载后默认是否显示
    };
    g.customObject.add(customObj);
    g.marker3d.setAttachCustomObject([
      {
        marker3dId: markerObj.id, //标注id
        objectId: customObj.id, //自定义对象id
        offset: [0, 0, 1], //偏移量
      }
    ]);
    let allObj = {
      id: markerObj.id,
      info: {},
      customData: customObj,
      marker: markerObj,
      uavData: uvaObj,
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
    var markerObj = {
      id: `police${new Date().getTime()}`,
      groupId: "police",
      userData: obj.name,
      coordinate: arr, //坐标位置
      coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      text: obj.name, //显示的文字
      textSize: 120,//3D标注显示文字大小
      textColor: '#000080',//3D标注显示文字颜色
      // textColor: '#ff0000',//3D标注显示文字颜色
      textOutlineSize: 1,//3D标注显示文字轮廓大小
      textOutlineColor: Color.Black,// 3D标注显示文字轮廓颜色
      textFixed: false,// 3D标注显示文字是否固定文本朝向
      fixedSize: true,// 默认尺寸 非近大远小
      textVisible: true,//3D标注显示文字是否显示文本
      textLocation: [0, 0, 0.1],// 文字位置
      textRotation: [0, 90, 0],// 文字旋转
      textScale: [1, 1, 1],// 文字缩放
      pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A',//3D标注展示的特效名称
      pointVisible: true,//3D标注是否显示
      pointScale: 1,//3D标注整体缩放比例
      range: [1, 2000], //3D标注的可视距离范围：[min,max]，单位：米
      autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
      collision: true //默认开启碰撞
    };
    g.marker3d.add(markerObj)
    let customObj = {
      id: markerObj.id,
      pakFilePath: obj.pak, //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
      assetPath: obj.path, //资源目录，自定义对象在pak文件资源包里的相对路径
      location: arr, //位置坐标
      coordinateType: 0, // 坐标系类型
      rotation: [0, 0, 0], // 世界坐标系旋转
      range: [0, 10000], //可见范围
      groupId: "police",
      userData: markerObj.id,
      localRotation: [0, 90, 0], //模型自身旋转
      scale: [1.3, 1.3, 1.3], //模型缩放
      isEffectRotation: true, //是否开启旋转效果
      smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
      supportAttach: true, //不支持贴画贴合
      visible: true, //模型加载后默认是否显示
    };
    g.customObject.add(customObj);
    if (obj.name === '固定哨') {
      g.customObject.callBPFunction([
        {
          id: customObj.id,
          functionName: '动画类型',
          parameters: [
            { "paramType": 5, "paramValue": "站立" },
          ]
        },
      ]);
    }
    g.marker3d.setAttachCustomObject([
      {
        marker3dId: markerObj.id, //标注id
        objectId: customObj.id, //自定义对象id
        offset: [0, 0, 2], //偏移量
      }
    ]);
    let allObj = {
      id: markerObj.id,
      info: {},
      customData: customObj,
      marker: markerObj,
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
    height: 108px;
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
        width: 65px;
        height: 71px;
      }

      .name {
        // padding: 10px 0;
        padding-top: 8px;
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
