<!--
 * @FileDescription: 三维标注->新增标签
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: Alex
 * @LastEditTime: 2024-06-20 16:08:05
 -->
<template>
  <div>
    <div class="cloud-func add">
      <div class="func-title">
        警力
        <el-icon class="" @click.stop="handleClose1" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>

      <div class="func-warp">
        <div class="call_btn" @click="callPhone(pointData.lxfs)">一键呼叫</div>
        <div class="basicInfo">
          <div class="infoName">基本信息</div>
          <div class="input_name" style="padding-top: 0">警力名称</div>
          <div class="input_box">
            <el-input v-model="pointData.jlmc" placeholder="请输入警力名称" clearable />
          </div>
          <div class="input_name" style="padding-top: 0">警力位置</div>
          <div class="input_box">
            <el-input v-model="pointData.weizhi" placeholder="请输入警力位置" clearable />
          </div>
          <div class="input_name" style="padding-top: 0">部署数量</div>
          <div class="input_box">
            <el-input v-model="pointData.num" placeholder="请输入部署数量" clearable />
          </div>
          <div class="input_name" style="padding-top: 0">分组名称</div>
          <div class="input_box">
            <el-select v-model="pointData.group" placeholder="请选择已有分组" style="width: 100%" clearable>
              <el-option v-for="(item, index) in groupList" :key="index" :label="item" :value="item" />
            </el-select>
          </div>
          <div class="input_box" style="margin-top: 8px">
            <el-input v-model="addGroupName" placeholder="新建分组名称" clearable />
          </div>

          <div class="input_name" style="padding-top: 0">部署防线</div>
          <div class="input_box">
            <el-radio-group v-model="pointData.fangxian">
              <el-radio value="一道防线">一道防线</el-radio>
              <el-radio value="二道防线">二道防线</el-radio>
              <el-radio value="三道防线">三道防线</el-radio>
            </el-radio-group>
          </div>
          <div class="input_name">警力编号</div>
          <div class="input_box">
            <el-input v-model="pointData.jlbh" placeholder="请输入警力编号" clearable />
          </div>
          <div class="input_name">联系方式</div>
          <div class="input_box">
            <el-input v-model="pointData.lxfs" placeholder="请输入警力联系方式" clearable />
          </div>
        </div>
        <div class="fuzhufenx" v-if="markerData.userData === '固定哨' ||
          markerData.userData === '制高点哨'
          ">
          <div class="infoName" style="
              margin-top: 20px;
              display: flex;
              align-items: center;
              justify-content: space-between;
            ">
            辅助分析<el-switch v-model="pointData.sfkqsy" style="margin-left: 10px" @change="changeSwitch" />
          </div>
          <div class="input_name" v-if="pointData.sfkqsy">视域分析方向</div>
          <div class="input_box" v-if="pointData.sfkqsy">
            <el-slider v-model="pointData.fxVal" :min="0" :max="359" @change="handleSliderChange"
              :disabled="!pointData.sfkqsy" :show-tooltip="false" />
          </div>
          <div class="input_name" v-if="pointData.sfkqsy">视域可视距离</div>
          <div class="input_box" v-if="pointData.sfkqsy">
            <el-slider v-model="pointData.fxjl" :min="80" :max="1000" @change="handleJuliChange"
              :disabled="!pointData.sfkqsy" :show-tooltip="false" />
            <el-input v-model="pointData.fxjl" readonly style="width: 100px; margin-left: 10px" />
          </div>
          <div class="input_name" v-if="pointData.sfkqsy">实时周边</div>
          <div class="input_box" v-if="pointData.sfkqsy">
            <el-input-number v-model="pointData.sszb" :min="10" :max="500" :step="20" :disabled="!pointData.sfkqzb"
              style="width: 240px" @change="setData" />m
            <el-switch v-model="pointData.sfkqzb" style="margin-left: 10px" @change="changeSwitch1" />
          </div>
        </div>
        <div class="fuzhufenx">
          <div class="input_name" v-if="markerData.userData === '游动哨'">
            移动路线 <el-switch v-model="pointData.sfkqLine" style="margin-left: 10px" @change="openMoveLine" />
          </div>
        </div>
        <div class="fuzhufenx">
          <div class="input_name" v-if="markerData.userData === '游动哨'">
            移动速度
          </div>
          <div class="input_box" v-if="markerData.userData === '游动哨'">
            <el-input-number v-model="pointData.ydsj" :min="1" :max="30000" :step="1" :disabled="!pointData.sfkqyd"
              style="width: 240px" @change="changeTime" />s/m
            <el-switch v-model="pointData.sfkqyd" style="margin-left: 10px" @change="changeSwitch2" />
          </div>
        </div>
        <div class="setting" v-if="false">
          <div class="infoName" style="margin-top: 20px">设置参数</div>
          <div class="setting_box">
            字符
            <el-select v-model="pointData.zf" placeholder="请选择" style="width: 100px; margin-left: 10px">
              <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
            <div class="font" style="margin-left: 10px">
              <svg-icon icon-class="cloud-font-size" class-name="icon"></svg-icon>
              <el-input v-model="pointData.fontsize" @change="handleChangeFont" style="width: 100px; margin-left: 10px">
              </el-input>
            </div>
            <el-color-picker v-model="pointData.color1" @change="handleChangeFont1"
              style="margin-left: 10px"></el-color-picker>
          </div>

          <div class="setting_box" style="margin-top: 10px">
            <div class="label">模型比例</div>
            <div class="proportion" style="margin-left: 10px">
              <el-slider :min="10" :max="100" :step="10" v-model="pointData.valueNmber" :show-input="false"
                :show-input-controls="false" style="width: 100px"></el-slider>
              <el-input-number v-model="pointData.valueNmber" controls-position="right" :max="100" :min="10"
                :step="10"></el-input-number>
            </div>
          </div>
        </div>
        <div class="basicInfo" style="margin-top: 10px" v-show="pointData.sfkqsy">
          <div class="infoName">视域分析参数设定</div>
          <el-scrollbar>
            <div class="warp-border">
              <p>俯仰视角（°）</p>
              <ul>
                <el-slider :min="-90" :max="90" v-model="pointData.fysj" @change="handleChangeVAL1" :show-input="false"
                  :show-input-controls="false" style="width: 100px"></el-slider>
                <el-input-number v-model="pointData.fysj" @change="handleChangeVAL1" controls-position="right" :min="-90"
                  :max="90" style="width: 100px; margin-left: 10px"></el-input-number>
              </ul>
            </div>
            <div v-for="(item, index) in setting" :key="index">
              <!-- slider -->
              <div v-if="item.type === 'slider'" class="warp-border">
                <p>{{ item.name }}</p>
                <ul>
                  <el-slider :min="item.min" :max="item.max" :step="item.step" v-model="item.value"
                    @change="handleChangeVAL" :show-input="false" :show-input-controls="false" style="width: 100px"
                    :disabled="item.key === 'height'"></el-slider>
                  <el-input-number v-model="item.value" @change="handleChangeVAL" controls-position="right"
                    :min="item.min" :max="item.max" :disabled="item.key === 'height'" :step="item.step"
                    style="width: 100px; margin-left: 10px"></el-input-number>
                </ul>
              </div>
              <!-- color -->
              <div v-else-if="item.type === 'color'">
                <div class="warp-color">
                  <span>{{ item.name }}</span>
                  <div class="color-setting">
                    <ul>
                      <svg-icon icon-class="cloud-color" class-name="icon"></svg-icon>
                      <el-color-picker v-model="item.value" @change="handleChangeVAL" size="small"></el-color-picker>
                    </ul>
                    <el-input v-model="item.value" placeholder="输入，例如:#000080" @change="handleChangeVAL"
                      style="margin-right: 30px"></el-input>
                  </div>
                </div>
                <div class="warp-border">
                  <p>透明度</p>
                  <ul>
                    <el-slider :min="0" :max="1" :step="0.1" v-model="item.opacity" @change="handleChangeVAL"
                      :show-input="false" :show-input-controls="false" style="width: 100px"></el-slider>
                    <el-input-number v-model="item.opacity" @change="handleChangeVAL" controls-position="right" :min="0"
                      :max="1" :step="0.1" style="width: 100px; margin-left: 10px"></el-input-number>
                  </ul>
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>
      </div>

      <div class="warp-footer">
        <div @click="deleteMarker(markerData, customObjectData)">删除</div>
        <div @click="handleClose">取消</div>
        <div class="primary" @click="handleSubmit">确定</div>
      </div>
    </div>
    <div class="callModal" v-if="callBol" @click="callBol = false"></div>
  </div>
</template>

<script setup>
import { loadPicture } from "@/utils";
import {
  deleteDrawData,
  saveScreenDraw,
  getById,
  searchGroupList,
} from "@/api/task/task";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import useScreenStore from "@/store/modules/screenStore";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import useEmergencyStore from "@/store/modules/emergencyPlan"; // 预案store
import useFloorStore from "@/store/modules/floorStore";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter";
import {
  computed,
  nextTick,
  reactive,
  ref,
  watch,
  onBeforeUnmount,
  onUnmounted,
  getCurrentInstance,
} from "vue";
import { ElMessage } from "element-plus";
const { proxy } = getCurrentInstance();
const ToolsCimStore = useSysToolsCimStore();
let screenInfo = computed(() => useScreenStore().screenInfo);
const curPage = computed(() => useWorkCockpitStore().curPage);
const editScreen = computed(() => useScreenStore().editScreen);
const y_aEdit = computed(() => useEmergencyStore().editYA); // 预案是否调用接口
const openFloor = computed(() => useFloorStore().openFloor); // 楼层是否炸开
const floornum = computed(() => useFloorStore().floornum); // 当前楼层点击楼层数
const emit = defineEmits(["closeRightBox"]);
let markerEvent = computed(() => ToolsCimStore.markerEvent);
let timer = ref(null); // 游动哨定时器
const callBol = ref(false);
const syfxArr = ref([]);
const moveBol = ref(false);
watch(
  () => markerEvent,
  (nV, oV) => {
    if (nV) {
      nextTick(() => {
        getMarker(nV.value);
      });
    }
  },
  { deep: true, immediate: true }
);
const setting = ref([
  {
    type: "slider",
    name: "横向视角（°）",
    key: "fov_h",
    value: 120,
    min: 1,
    max: 180,
    step: 1,
  },
  {
    type: "slider",
    name: "垂直视角（°）",
    value: 60,
    key: "fov_v",
    min: 1,
    max: 180,
    step: 1,
  },
  {
    type: "slider",
    name: "视角高度（米）",
    value: 1.7,
    key: "height",
    min: 0,
    max: 500,
    step: 1,
  },
  {
    type: "color",
    name: "可视颜色",
    value: "#00FF00",
    opacity: 1.0,
    key: "visibleColor",
  },
  {
    type: "color",
    name: "不可视颜色",
    value: "#000080",
    opacity: 1.0,
    key: "invisibleColor",
  },
]);
const options = ref([
  {
    value: "微软雅黑",
    label: "微软雅黑",
  },
  {
    value: "fangsong",
    label: "仿宋",
  },
]);
const addGroupName = ref("");
const pointData = reactive({
  jlmc: "", // 警车名称
  weizhi: "", // 位置
  num: "", // 部署数量
  group: "", // 分组名称
  fangxian: "", // 部署防线
  jlbh: "", // 编号
  lxfs: "", // 联系方式
  syfx: "0",
  fxVal: 0, // 分析方向值
  fxjl: 300, // 分析距离
  fysj: 0, // 俯仰视角
  hxsj: 120, //横向视角
  czsj: 60, //垂直视角（°）
  sfkqsy: false, // 是否开启视域
  sfkqLine:false,// 是否开启游动哨路线
  sszb: 100, // 实时周边
  sfkqzb: false, // 是否开启周边
  ydsj: 3, // 移动时间
  endPoint: [], // 保存的终点视角
  sfkqyd: false, // 是否开启移动
  zf: "微软雅黑",
  fontsize: 14,
  color1: "#000000",
  valueNmber: 100,
});
const openLine = ref(false)
const markerData = ref({});
const customObjectData = ref({})
const groupList = ref([]);
const infoObj = ref({});
const callPhone = (phone) => {
  callBol.value = true;
};
const handleClose = async () => {
  await window.sealAPI._viewshed.cancel();
  let g = window.__g;
  //
  // if(!pointData.sfkqLine){
    
  // }
  ToolsCimStore.SET_MARKEREVENT({});
  // window.__g.radiationPoint.clear();
  // window.__g.radiationPoint.delete(markerData.value.id);
  if (markerData.value.userData === "游动哨") {
    g.customObject.pause(customObjectData.value.id);
    g.customObject.setLocation(customObjectData.value.id, markerData.value.coordinate);
    if(openLine.value){
      // 开启了游动哨
      if(!pointData.sfkqLine){
        g.polyline.add(infoObj.value.lineData);
      }
    }else{
      g.polyline.delete(infoObj.value.lineData.id);
    }
  } else {
    // g.marker.update(markerData.value);
  }
  g.radiationPoint.delete(markerData.value.id);
  emit("closeRightBox", false);
};
const handleClose1 = async () => {
  await window.sealAPI._viewshed.cancel();
  let g = window.__g;
  if (markerData.value.userData === "游动哨") {
    g.customObject.pause(customObjectData.value.id);
    g.customObject.setLocation(customObjectData.value.id, markerData.value.coordinate);
    if(openLine.value){
      // 开启了游动哨
      if(!pointData.sfkqLine){
        g.polyline.add(infoObj.value.lineData);
      }
    }else{
      g.polyline.delete(infoObj.value.lineData.id);
    }
  } else {
    if (markerData.value.id) {
      // g.marker.update(markerData.value);
    }
  }
  g.radiationPoint.delete(markerData.value.id);
  ToolsCimStore.SET_MARKEREVENT({});
  // window.__g.radiationPoint.clear();
  emit("closeRightBox", false);
};
// 获取地图打点的数据
const getMarker = async (item) => {
  if (
    item.GroupID === "police" &&
    item.eventtype === "LeftMouseButtonClick" &&
    (item.Type === "marker" || item.Type === "CustomObj" || item.Type === 'marker3d')
  ) {
    showData(item);
  }
  if (item.eventtype === "StartMoveFinished") {
    moveMarker(item);
  }
};

// 回显数据
const showData = async (item) => {
  let g = window.__g
  // 获取分组列表
  searchGroupList({ sceneId: screenInfo.value.id }).then((res) => {
    groupList.value = res.data;
  });
  let params
  if (item.Type === "CustomObj" || item.Type === 'marker3d') {
    params = { id: item.Id }
    let objData = await g.customObject.get(item.Id)
    let items = objData.data[0]
    let obj = {
      id: items.id, //自定义对象唯一id
      pakFilePath: items.pakFilePath, //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
      assetPath: items.assetPath, //资源目录，自定义对象在pak文件资源包里的相对路径
      location: items.location, //位置坐标
      coordinateType: 0, // 坐标系类型
      rotation: items.rotation, // 世界坐标系旋转
      range: [0, 10000], //可见范围
      groupId: items.groupId,
      userData: items.userData,
      localRotation: items.localRotation, //模型自身旋转
      scale: items.scale, //模型缩放
      isEffectRotation: true, //是否开启旋转效果
      smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
      supportAttach: true, //不支持贴画贴合
      visible: true, //模型加载后默认是否显示
    }
    customObjectData.value = obj
  } else {
    params = { id: item.Id }
  }
  getById(params).then((res) => {
    let showObj = res.data.data.marker;
    let info = res.data.data.info;
    markerData.value = showObj;
    infoObj.value = info;
    pointData.jlmc = markerData.value.text;
    if (info && Object.keys(info).length !== 0) {
      openLine.value = info.sfkqLine || false
      pointData.weizhi = info.weizhi;
      pointData.num = info.num;
      pointData.fangxian = info.fangxian;
      pointData.group = info.group;
      pointData.jlmc = info.jlmc || markerData.value.text;
      pointData.fxVal = info.fxVal || 0;
      pointData.fysj = info.fysj || 0;
      pointData.hxsj = info.fov_h || 120;
      pointData.czsj = info.fov_w || 60;
      setting.value[0].value = info.fov_h || 120;
      setting.value[1].value = info.fov_w || 60;
      pointData.endPoint = info.endPoint || [];
      pointData.jlbh = info.jlbh;
      pointData.lxfs = info.lxfs;
      pointData.syfx = info.syfx || "0";
      pointData.sfkqsy = false;
      pointData.sszb = info.sszb || 100;
      pointData.sfkqLine = info.sfkqLine || false;
      pointData.fxjl = info.fxjl || 300;
      pointData.sfkqzb = false;
      pointData.ydsj = info.ydsj || 3;
      pointData.sfkqyd = false;
      pointData.zf = info.zf;
      pointData.fontsize = info.fontsize;
      pointData.color1 = info.color1;
      pointData.valueNmber = info.valueNmber;
      pointData.buildName = info.buildName || null;
      pointData.floorNum = info.floorNum || null;
    }
  });
};

const changeTime = async (val) => {
  pointData.ydsj = val;
  changeSwitch2(true);
};
// 删除地位信息
const deleteMarker = async (data, obj) => {
  let g = window.__g;
  await g.marker.delete(data.id);
  await g.marker3d.delete(data.id);
  if (obj.id) {
    g.customObject.delete(obj.id)
  }
  if (
    infoObj.value.lineData &&
    Object.keys(infoObj.value.lineData).length !== 0
  ) {
    await g.polyline.delete(infoObj.value.lineData.id);
    // 从缓存中找线数据
    let carData = sessionStorage.get("QXZS_pointLine") || [];
    if (carData.length > 0) {
      let newData = carData.filter(
        (item) => infoObj.value.lineData.id !== item.id
      );
      sessionStorage.set("QXZS_pointLine", newData);
    }
  }
  window.sealAPI._viewshed.cancel();
  if (editScreen.value || y_aEdit.value || curPage.value === "taskDetail") {
    deleteDrawData(data.id).then((res) => {
      if (res.code === 0) {
        ElMessage.success("删除成功!");
        emitter.emit("refreshPolice");
        emitter.emit("refreshResource", data.id);
      }
    });
  }
  emit("closeRightBox", false);
  // 从缓存中找警车数据
  let carData = sessionStorage.get("policeData") || [];
  if (carData.length > 0) {
    let newData = carData.filter((item) => (data.id || obj.id) !== item.id);
    sessionStorage.set("policeData", newData);
  }
};
const handleSubmit = async () => {
  let g = window.__g;
  if (pointData.sfkqyd) {
    pointData.sfkqyd = false;
    g.customObject.setLocation(customObjectData.value.id, markerData.value.coordinate);
  }
  let fxObj = {
    fov_h: setting.value[0].value,
    fov_w: setting.value[1].value,
    height: setting.value[2].value,
    visibleColor: setting.value[3].value,
    invisibleColor: setting.value[4].value,
  };
  if (pointData.jlmc) {
    markerData.value.text = pointData.jlmc;
    console.log(markerData.value)
    // g.marker.setText(markerData.value.id,pointData.jlmc);
    markerData.value = {
      id: markerData.value.id,
      userData: markerData.value.userData,
      groupId: markerData.value.groupId,
      coordinate: markerData.value.coordinate, //坐标位置
      coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      text: markerData.value.text, //显示的文字
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
    }
    await g.marker3d.update(markerData.value)
    g.marker3d.setAttachCustomObject([
      {
        marker3dId: markerData.value.id, //标注id
        objectId: customObjectData.value.id, //自定义对象id
        offset: [0, 0, 2], //偏移量
      }
    ]);
  }
  let allObj = {};
  let moveObj = {
    lineData: infoObj.value.lineData ? infoObj.value.lineData : {},
  };
  if (pointData.sfkqsy) {
    allObj = {
      id: markerData.value.id,
      info: { ...pointData, ...fxObj, ...moveObj },
      marker: markerData.value,
      customData: customObjectData.value
    };
  } else {
    allObj = {
      id: markerData.value.id,
      info: { ...pointData, ...moveObj },
      marker: markerData.value,
      customData: customObjectData.value
    };
  }
  if (addGroupName.value && addGroupName.value !== "") {
    allObj.info.group = addGroupName.value;
  }
  if (editScreen.value || y_aEdit.value) {
    let query = {
      id: markerData.value.id,
      type: "police",
      data: allObj,
    };
    saveScreenDraw(query).then((res) => {
      if (res.code === 0) {
        ElMessage.success("编辑成功");
        emitter.emit("refreshPolice");
        emitter.emit("refreshResource", query.id);
      }
    });
  }
  g.radiationPoint.delete(markerData.value.id);
  let data1 = sessionStorage.get("policeData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.id);
    newData.push(allObj);
    sessionStorage.set("policeData", newData);
  } else {
    sessionStorage.set("policeData", [allObj]);
  }

  nextTick(() => {
    // handleClose();
    ToolsCimStore.SET_MARKEREVENT({});
    window.sealAPI._viewshed.cancel();
    emit("closeRightBox", false);
  });
};
const handleSliderChange = async (val) => {
  console.log(val, syfxArr.value[val], "89080");
  window.sealAPI._viewshed.cancel();
  pointData.endPoint = syfxArr.value[val];
  let g = window.__g;
  let options = {
    startPoint: [
      markerData.value.coordinate[0],
      markerData.value.coordinate[1],
      markerData.value.coordinate[2],
    ], //起点坐标
    endPoint: syfxArr.value[val], //终点坐标
    fov_h: setting.value[0].value, //横向视角，取值范围：[1°~150°]，默认值：60
    fov_v: setting.value[1].value, //纵向视角，取值范围：[1°~150°]，默认值：30
    height: setting.value[2].value, //视点高度（距离场景交互所拾取点的高度），默认值：0
    visibleColor: setting.value[3].value, //可见区域的颜色，默认值：红色 Color.Red
    invisibleColor: setting.value[4].value, //不可见区域的颜色，默认值：绿色 Color.Green
    interactiveLock: true, //是否开启交互锁定
  };
  g.tools.startViewshedAnalysis(options);
};
const handleJuliChange = async (val) => {
  console.log(val, pointData.fxjl);
  window.sealAPI._viewshed.cancel();
  // pointData.fxVal = 0
  changeSwitch(true);
};
// 视域开启和关闭
const changeSwitch = (val) => {
  let g = window.__g;
  if (val) {
    console.log(markerData.value.coordinate, pointData.fxjl);
    getCircular(markerData.value.coordinate, pointData.fxjl);
    syfxArr.value = getCircular(markerData.value.coordinate, pointData.fxjl);
    pointData.endPoint = syfxArr.value[Number(pointData.fxVal)];
    // let end = [
    //     markerData.value.coordinate[0] + pointData.fxjl,
    //     markerData.value.coordinate[1],
    //     markerData.value.coordinate[2],
    //   ];
    let end = syfxArr.value[Number(pointData.fxVal)];
    let options = {
      startPoint: [
        markerData.value.coordinate[0],
        markerData.value.coordinate[1],
        markerData.value.coordinate[2],
      ], //起点坐标
      endPoint: end, //终点坐标
      fov_h: setting.value[0].value, //横向视角，取值范围：[1°~150°]，默认值：60
      fov_v: setting.value[1].value, //纵向视角，取值范围：[1°~150°]，默认值：30
      height: setting.value[2].value, //视点高度（距离场景交互所拾取点的高度），默认值：0
      visibleColor: setting.value[3].value, //可见区域的颜色，默认值：红色 Color.Red
      invisibleColor: setting.value[4].value, //不可见区域的颜色，默认值：绿色 Color.Green
      interactiveLock: true, //是否开启交互锁定
    };
    g.tools.startViewshedAnalysis(options);
    nextTick(() => {
      handleChangeVAL1(pointData.fysj);
    });
  } else {
    window.sealAPI._viewshed.cancel();
    // pointData.fxVal = 0
    // pointData.fxjl = 100
    // pointData.endPoint = []
  }
};
/**
 * 获取圆形的区域点位
 * @param center
 * @param radius
 * @param r
 * @param pointNum
 * @returns
 */
const getCircular = (center, radius, pointNum = 360) => {
  const num = pointNum;
  //待计算圆上的点 注意：半径越大需要计算的点数量越多 本示例使用360个点
  const pointArr = [];
  for (let i = 0; i < num; i++) {
    //角度转弧度
    const radians = (i * (360 / num) * Math.PI) / 180;
    //计算圆上的点X坐标
    const x1 = center[0] + radius * Math.cos(radians);
    //计算圆上的点Y坐标
    const y1 = center[1] + radius * Math.sin(radians);
    //赋值圆上所有点数组
    pointArr.push([x1, y1, center[2]]);
  }
  console.log("pointArr::: ", pointArr);
  return pointArr;
};
// 游动哨开启和关闭
const changeSwitch2 = async (val) => {
  console.log(markerData.value, infoObj.value);
  if(!pointData.sfkqLine){
    pointData.sfkqyd = false
    return ElMessage.warning('请先开启移动路线')
  }
  let g = window.__g;
  if (val) {
    moveBol.value = true;
    // 平移动画路径
    const paths = infoObj.value.lineData.coordinates;
    //构造移动路径点数组
    const pathPointArr = [];

    paths.forEach((ii, index) => {
      //构造数组元素 每1秒移动一次
      let elementPoint = { time: index * pointData.ydsj, coordinate: ii };
      pathPointArr.push(elementPoint);
    });
    //车辆按GPS轨迹移动
    g.customObject.startMove(customObjectData.value.id, 0, pathPointArr);
  } else {
    g.customObject.pause(customObjectData.value.id)
    moveBol.value = false;
    g.customObject.setLocation(customObjectData.value.id, markerData.value.coordinate);
    // ToolsCimStore.SET_MARKEREVENT({});
    // g.customObject.startMove(infoObj.value.custom.id, 0, [{time:0,coordinate:markerData.value.coordinate}]);
    //   await g.marker.setAttachCustomObject([{
    //     markerId: markerData.value.id, //标注id
    //     objectId: infoObj.value.custom.id, //自定义对象id
    //     offset: [0, 0.5, 0] //偏移量
    // }]);
    // 平移动画路径
    // const paths =  [infoObj.value.lineData.coordinates[0]]
    // //构造移动路径点数组
    // const pathPointArr = [];
    // paths.forEach((ii, index) => {
    //     //构造数组元素 每1秒移动一次
    //     let elementPoint = { 'time': (index) * 1, 'coordinate': ii };
    //     pathPointArr.push(elementPoint);
    // })
    // //车辆按GPS轨迹移动
    // g.customObject.startMove(infoObj.value.custom.id, 0, pathPointArr);
  }
};
// 游动哨路线的开启和关闭
const openMoveLine = async (val) => {
  let g = window.__g;
  if(val){
    g.polyline.add(infoObj.value.lineData);
  }else{
    g.customObject.pause(customObjectData.value.id)
    g.customObject.setLocation(customObjectData.value.id, markerData.value.coordinate);
    g.polyline.delete(infoObj.value.lineData.id);
    pointData.sfkqyd = false
  }
}
// 游动哨来回游动
const moveMarker = async (item) => {
  console.log(item, "来回移动");
  let g = window.__g;
  if (moveBol.value) {
    // 平移动画路径
    const paths = infoObj.value.lineData.coordinates.reverse();
    //构造移动路径点数组
    const pathPointArr = [];
    paths.forEach((ii, index) => {
      //构造数组元素 每1秒移动一次
      let elementPoint = { time: index * pointData.ydsj, coordinate: ii };
      pathPointArr.push(elementPoint);
    });
    //车辆按GPS轨迹移动
    g.customObject.startMove(customObjectData.value.id, 0, pathPointArr);
  } else {
    g.customObject.setLocation(customObjectData.value.id, markerData.value.coordinate);
  }
};
const handleChangeFont = () => {
  upDateMarker(markerData.value, "size", pointData.fontsize);
};
const handleChangeFont1 = () => {
  upDateMarker(markerData.value, "color", pointData.color1);
};
// 修改点位
const upDateMarker = async (obj, type, val) => {
  let g = window.__g;
  let points = obj;
  if (type === "size") {
    points.fontSize = val;
  }
  if (type === "color") {
    points.fontColor = val;
  }
  g.marker.update(points);
};
// 实时周边开启
const changeSwitch1 = async (val) => {
  let g = window.__g;
  // g.radiationPoint.clear();
  if (val) {
    if (!pointData.sfkqyd) {
      let o = {
        id: markerData.value.id,
        coordinate: [
          markerData.value.coordinate[0],
          markerData.value.coordinate[1],
          markerData.value.coordinate[2],
        ], //辐射圈坐标位置
        coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
        radius: pointData.sszb, //辐射半径
        rippleNumber: 1, //波纹数量
        color: [1, 0, 0, 0.8], //颜色
        intensity: 0.8, //亮度
        autoHeight: true, //自动判断下方是否有物体
      };
      await g.radiationPoint.add(o);
    }
    // g.radiationPoint.focus(o.id, Number(pointData.sszb)+500, );
  } else {
    g.radiationPoint.delete(markerData.value.id);
  }
};
const setData = async (val) => {
  let g = window.__g;
  let o = {
    id: markerData.value.id,
    coordinate: [
      markerData.value.coordinate[0],
      markerData.value.coordinate[1],
      markerData.value.coordinate[2],
    ], //辐射圈坐标位置
    coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
    radius: val, //辐射半径
    rippleNumber: 1, //波纹数量
    color: [1, 0, 0, 0.8], //颜色
    intensity: 0.8, //亮度
    autoHeight: true, //自动判断下方是否有物体
  };
  await g.radiationPoint.update(o);
};
const handleChangeVAL = () => {
  var curOa = window.sealAPI._viewshed.getSetting().curOa;
  console.log(markerData.value.coordinate[2], "8908098080");
  if (curOa) {
    for (var i = 0; i < setting.value.length; i++) {
      if (setting.value[i].type === "color") {
        var r = parseInt(setting.value[i].value.slice(1, 3), 16) / 255;
        var g = parseInt(setting.value[i].value.slice(3, 5), 16) / 255;
        var b = parseInt(setting.value[i].value.slice(5, 7), 16) / 255;
        curOa[setting.value[i].key] = [r, g, b, setting.value[i].opacity];
      } else {
        curOa[setting.value[i].key] =
          setting.value[i].key === "height"
            ? setting.value[i].value + markerData.value.coordinate[2]
            : setting.value[i].value;
      }
    }
    window.sealAPI._viewshed.update();
  }
};
const handleChangeVAL1 = (val) => {
  console.log(val, syfxArr.value[pointData.fxVal]);
  let end = [
    syfxArr.value[pointData.fxVal][0],
    syfxArr.value[pointData.fxVal][1],
    syfxArr.value[pointData.fxVal][2] + val,
  ];
  console.log(end);
  window.sealAPI._viewshed.cancel();
  let g = window.__g;
  let options = {
    startPoint: [
      markerData.value.coordinate[0],
      markerData.value.coordinate[1],
      markerData.value.coordinate[2],
    ], //起点坐标
    endPoint: end, //终点坐标
    fov_h: setting.value[0].value, //横向视角，取值范围：[1°~150°]，默认值：60
    fov_v: setting.value[1].value, //纵向视角，取值范围：[1°~150°]，默认值：30
    height: setting.value[2].value, //视点高度（距离场景交互所拾取点的高度），默认值：0
    visibleColor: setting.value[3].value, //可见区域的颜色，默认值：红色 Color.Red
    invisibleColor: setting.value[4].value, //不可见区域的颜色，默认值：绿色 Color.Green
    interactiveLock: true, //是否开启交互锁定
  };
  g.tools.startViewshedAnalysis(options);
};
onBeforeUnmount(async () => {
  await window.sealAPI._viewshed.cancel();
  if (timer.value) {
    clearInterval(timer.value);
  }
});
onUnmounted(async () => {
  await window.sealAPI._viewshed.cancel();
  if (timer.value) {
    clearInterval(timer.value);
  }
});
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

:deep(.add) {
  width: 400px;
  position: absolute;
  top: -30px;
  bottom: -80px;
  background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);

  .func-warp {
    height: calc(100% - 150px);
    display: flex;
    flex-direction: column;
    overflow: auto;
    padding: 4px 20px;
    position: relative;

    .call_btn {
      position: absolute;
      top: 20px;
      right: 20px;
      background: #029eff;
      width: 100px;
      text-align: center;
      line-height: 36px;
      border-radius: 4px;
      color: #fff;
      cursor: pointer;
    }

    .input_box {
      display: flex;
      align-items: center;

      .el-input {
        border: 1px solid #5b6799;
        border-radius: 2px;

        .el-input__wrapper {
          background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
        }

        .el-input__inner {
          height: 36px;
          line-height: 36px;
          font-size: 14px;
          color: #ffffff;
          opacity: 0.8;
        }
      }

      .el-select {

        // border: 1px solid #5b6799;
        // border-radius: 2px;
        .el-select__wrapper {
          height: 40px;
          line-height: 40px;
          background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
          border: 1px solid #5b6799;

          .el-select__selected-item {
            color: #ffffff;
            opacity: 1;
          }

          .is-transparent {
            color: #a8abb2;
          }
        }
      }

      .el-radio__label {
        color: #fff;
      }
    }

    .infoName {
      height: 40px;
      line-height: 40px;
      font-weight: 400;
      font-size: 17px;
      color: #ffffff;
    }

    .input_name {
      height: 30px;
      line-height: 30px;
      font-size: 14px;
      color: #fff;
      padding-top: 10px;
    }

    .fuzhufenx {
      .el-select__wrapper {
        background: rgba(0, 12, 78, 0.5);
        box-shadow: none;
        border: 1px solid #5b6799;

        .el-select__selected-item {
          color: #ffffff;
          opacity: 0.8;
        }

        .is-transparent {
          color: #a8abb2;
        }
      }

      .el-input-number__decrease,
      .el-input-number__increase {
        background: rgba(0, 0, 0, 0);
        border-radius: 2px 2px 2px 2px;
        border: 1px solid #ffffff;
        opacity: 0.8;
        width: 30px;

        .el-icon {
          color: #fff;
        }
      }
    }

    .setting {
      .setting_box {
        display: flex;
        align-items: center;
        height: 30px;

        .proportion {
          display: flex;
          justify-content: space-between;

          .el-slider {
            width: calc(60% - 14px);
          }

          .el-input-number {
            width: 40%;
          }
        }
      }
    }

    .warp-border {
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      padding: 10px 0;
      display: flex;
      align-items: center;

      p {
        width: 120px;
      }

      ul {
        display: flex;
      }
    }

    .warp-color {
      padding: 10px 0;
      box-sizing: border-box;
      font-size: 16px;
      color: #ffffff;
      display: flex;
      align-items: center;
      justify-content: space-between;

      .color-setting {
        display: flex;
        align-items: center;

        ul {
          background: #494d52;
          border: 1px solid #646b6f;
          overflow: hidden;
          border-radius: 4px;

          .icon {
            width: 20px;
            height: 22px;
          }
        }

        .el-input {
          width: 95px;
          margin-left: 10px;
        }
      }
    }
  }

  .warp-footer {
    display: flex;
    justify-content: center;
    padding: 24px;
    border-top: 1px solid #ffffff33;

    div {
      width: 80px;
      line-height: 38px;
      cursor: pointer;
      border-radius: 5px;
      border: 1px solid #029eff;
      text-align: center;
      margin-left: 20px;

      &.primary {
        background: #029eff;
        margin-left: 20px;
      }
    }
  }
}

.callModal {
  background: url("../../../../assets/panel/callModal.png") no-repeat;
  background-size: 100% 100%;
  width: 236px;
  height: 342.07px;
  position: absolute;
  // scale: 0.8;
  top: 100px;
  // right: 18%;
  right: 340px;
  cursor: pointer;
}
</style>
