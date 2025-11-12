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
        无人机反制
        <el-icon class="" @click.stop="handleClose1" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>

      <div class="func-warp">
        <div class="basicInfo">
          <div class="infoName">基本信息</div>
          <div class="input_name" style="padding-top: 0">名称</div>
          <div class="input_box">
            <el-input v-model="pointData.mc" placeholder="请输入名称" clearable />
          </div>
          <div class="input_name">备注</div>
          <div class="input_box">
            <el-input v-model="pointData.bz" placeholder="请输入备注" clearable :rows="3" type="textarea" />
          </div>
        </div>

        <div class="setting">
          <div class="infoName" style="margin-top: 20px">设置参数</div>
          <div class="setting_box">
            <div class="left_text">颜色</div>
            <div class="font">
              <el-input v-model="pointData.color" readonly> </el-input>
            </div>
            <el-color-picker v-model="pointData.color" @change="handleChangeFont1"
              style="margin-left: 10px"></el-color-picker>
          </div>
          <div class="setting_box">
            <div class="left_text">半径范围</div>
            <el-input-number v-model="pointData.bjfw" :min="10" :max="500" :step="10" style="width: 225px"
              @change="setData" />m
          </div>
        </div>
      </div>

      <div class="warp-footer">
        <div @click="deleteMarker(markerData, customObjectData)">删除</div>
        <div @click="handleClose">取消</div>
        <div class="primary" @click="handleSubmit">确定</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { loadPicture } from "@/utils";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { CircleClose } from "@element-plus/icons-vue";
import { deleteDrawData, saveScreenDraw, getById } from "@/api/task/task";
import { sessionStorage } from "@/utils/storage";
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
import emitter from "@/utils/emitter";
import { ElMessage } from "element-plus";
const { proxy } = getCurrentInstance();
const ToolsCimStore = useSysToolsCimStore();
const emit = defineEmits(["closeRightBox"]);
let markerEvent = computed(() => ToolsCimStore.markerEvent);
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

const pointData = reactive({
  mc: "",
  bz: "",
  color: "#130FEB",
  bjfw: 100,
});
const markerData = ref({}); // 无人机marker点
const customObjectData = ref({})
const uavData = ref({}); // 无人机半径
const handleClose = () => {
  ToolsCimStore.SET_MARKEREVENT({});
  emit("closeRightBox", false);
};
const handleClose1 = () => {
  ToolsCimStore.SET_MARKEREVENT({});
  emit("closeRightBox", false);
};
// 获取地图打点的数据
const getMarker = async (item) => {
  if (
    item.GroupID === "uav" &&
    item.eventtype === "LeftMouseButtonClick" &&
    (item.Type === "marker" || item.Type === "CustomObj" || item.Type === 'marker3d')
  ) {
    showData(item);
  }
};
// 回显数据
const showData = async (item) => {
  let g = window.__g;
  let params;
  if (item.Type === "CustomObj" || item.Type === 'marker3d') {
    params = { id: item.Id };
    let objData = await g.customObject.get(item.Id);
    let items = objData.data[0];
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
    };
    customObjectData.value = obj;
  } else {
    params = { id: item.Id };
  }
  getById(params).then((res) => {
    console.log(res)
    let info = res.data.data.info;
    markerData.value = res.data.data.marker;
    if (res.data.data.uavData) {
      uavData.value = res.data.data.uavData
    } else {
      uavData.value = {
        id: res.data.data.marker.id,
        coordinate: res.data.data.marker.coordinate, //辐射圈坐标位置
        coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
        radius: 100, //辐射半径
        rippleNumber: 5, //波纹数量
        color: "#130FEB", //颜色
        intensity: 0.5, //亮度
        autoHeight: false, //自动判断下方是否有物体
        userData: "无人机",
        groupId: "uav",
      };
    }
    pointData.mc = res.data.data.marker.text;
    if (info && Object.keys(info).length !== 0) {
      pointData.mc = info.mc || markerData.value.text;
      pointData.bz = info.bz;
      pointData.color = info.color ? info.color : pointData.color;
      pointData.bjfw = info.bjfw ? info.bjfw : pointData.bjfw;
    }
  })
};
// 删除地位信息
const deleteMarker = async (data, obj) => {
  let g = window.__g;
  await g.marker.delete(data.id);
  await g.marker3d.delete(data.id)
  await g.radiationPoint.delete(data.id);
  if (obj && Object.keys(obj).length !== 0) {
    g.customObject.delete(obj.id)
  }
  deleteDrawData(data.id).then(res => {
    if (res.code === 0) {
      emitter.emit("refreshResource", data.id);
      ElMessage.success('删除成功!')
    }
  })
  emit("closeRightBox", false);
  // 从缓存中找警车数据
  let carData = sessionStorage.get("uavData") || [];
  if (carData.length > 0) {
    let newData = carData.filter((item) => (data.id || obj.id) !== item.id);
    sessionStorage.set("uavData", newData);
  }
};
const handleSubmit = async () => {
  let g = window.__g;
  if (pointData.mc) {
    markerData.value.text = pointData.mc;
    // g.marker.setText(markerData.value.id,pointData.mc);
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
  let allObj = {
    id: markerData.value.id,
    info: pointData,
    marker: markerData.value,
    customData: customObjectData.value,
    uavData: uavData.value,
  };
  let query = {
    id: markerData.value.id,
    type: 'uav',
    data: allObj
  }
  saveScreenDraw(query).then(res => {
    if (res.code === 0) {
      emitter.emit("refreshResource", query.data.id);
      ElMessage.success('编辑成功')
    }
  })
  let data1 = sessionStorage.get("uavData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.id);
    newData.push(allObj);
    sessionStorage.set("uavData", newData);
  } else {
    sessionStorage.set("uavData", [allObj]);
  }

  nextTick(() => {
    ToolsCimStore.SET_MARKEREVENT({});
    emit("closeRightBox", false);
  });
};

// 改变颜色
const handleChangeFont1 = (val) => {
  upDateMarker(uavData.value, "color", val);
};
// 修改点位
const upDateMarker = async (obj, type, val) => {
  let g = window.__g;
  let points = obj;
  if (type === "mi") {
    points.radius = val;
  }
  if (type === "color") {
    points.color = val;
  }
  g.radiationPoint.update(points);
};

// 设置半径范围
const setData = async (val) => {
  upDateMarker(uavData.value, "mi", val);
};
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

:deep(.add) {
  width: 400px;
  position: absolute;
  top: -20px;
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

      .el-textarea__inner {
        background: rgba(0, 12, 78, 0.5);
        border: 1px solid #5b6799;
      }

      .is-focus {
        border: 0px;
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
        margin-bottom: 10px;

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

        .left_text {
          width: 80px;
        }

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
}</style>
