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
        基础工作
        <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>

      <div class="func-warp">
        <div class="basicInfo">
          <div class="infoName">基本信息</div>
          <div class="input_name" style="display: flex;align-items: center;justify-content: space-between;">
            <div>名称</div>
            <!-- <div v-if="pointData.featureType">
              <span style="margin-right: 10px;">重点标记</span>
              <el-switch v-model="pointData.status" active-value="1"
              :inactive-value="null"/>
            </div> -->
          </div>
          <div class="input_box">
            <el-input v-model="pointData.mc" placeholder="请输入名称" clearable />
          </div>
          <div class="input_name">位置（公里）</div>
          <div class="input_box">
            <span style="font-size: 14px;margin-right: 8px;">距离</span><el-input v-model="refLineName"  placeholder="请输入路线名称" clearable  style="width: 150px;"/>
            <span style="font-size: 14px;margin: 0 8px;">大约</span><el-input v-model="position"  placeholder="请输入大概距离" clearable   style="width: 130px;"/>
          </div>
          <div class="input_name">备注</div>
          <div class="input_box">
            <el-input v-model="pointData.bz" type="textarea" :rows="5" placeholder="请输入备注" clearable />
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
      </div>

      <div class="warp-footer">
        <div @click="deleteMarker(markerData)">删除</div>
        <div @click="handleClose">取消</div>
        <div class="primary" @click="handleSubmit">确定</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { deleteDrawData, saveScreenDraw, getById } from "@/api/task/task";
import useWorkCockpitStore from '@/store/modules/workCockpit'
import useScreenStore from "@/store/modules/screenStore";

import useFloorStore from "@/store/modules/floorStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter"
// const screenInfo = computed(() => useTaskStore().screenModalInfo);
const WorkCockpitStore = useWorkCockpitStore()
const taskId = computed(()=>WorkCockpitStore.basicTaskId)
const sceneId = computed(()=>WorkCockpitStore.basicSceneId)
const y_aEdit = computed(()=>useEmergencyStore().editYA) // 预案是否调用接口
const editScreen = computed(() => useScreenStore().editScreen)
const openFloor = computed(() => useFloorStore().openFloor) // 楼层是否炸开
const floornum = computed(() => useFloorStore().floornum) // 当前楼层点击楼层数
//当前点击的楼栋名称
let explodebuildname = computed(() => useFloorStore().explodebuildname);
import { computed, nextTick, reactive, ref, watch } from "vue";
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
const position = ref('')
const refLineName = ref('')
const pointData = reactive({
  mc: "",
  bz: "",
  zf: "微软雅黑",
  fontsize: 14,
  color1: "#000000",
  valueNmber: 100,
  featureType: null,
  status:null
});
const markerData = ref({});
const handleClose = () => {
  ToolsCimStore.SET_MARKEREVENT({});
  emit("closeRightBox", false);
};
// 获取地图打点的数据
const getMarker = async (item) => {
  if (
    item.GroupID === "basic" &&
    item.eventtype === "LeftMouseButtonClick" &&
    (item.Type === "marker"||item.Type === 'MarkerLayer')
  ) {
    showData(item);
  }
};
const isNumeric =(str)=> {
      return /^-?\d+(\.\d+)?$/.test(str);
    }
// ( item.GroupID === "resource" ||
// 回显数据
const showData = async (item) => {
  // getById({ id: item.Id }).then(res => {
  //   console.log(res)
  // })
  // if (openFloor.value && floornum.value) {
    //sceneId:sceneId.value
    getById({ id: item.Id,sceneId:sceneId.value }).then(res => {
      markerData.value = res.data.data.marker
      if(res.data.position){
        position.value = Number(res.data.position).toFixed(2) || ''
      }
      if(res.data.refLineName){
        refLineName.value = res.data.refLineName || ''
      }
      if (res.data.data.info && Object.keys(res.data.data.info).length !== 0) {
        pointData.mc = res.data.data.info.mc || res.data.data.info.featureType;
        pointData.bz = res.data.data.info.bz;
        pointData.zf = res.data.data.info.zf;
        pointData.fontsize = res.data.data.info.fontsize;
        pointData.color1 = res.data.data.info.color1;
        pointData.valueNmber = res.data.data.info.valueNmber;
        pointData.featureType = res.data.data.info.featureType || null;
        pointData.buildName = res.data.data.info.buildName || null;
        pointData.floorNum = res.data.data.info.floorNum || null;
        pointData.status = res.data.data.info.status || null;
        // if(openFloor.value && floornum.value&&explodebuildname.value){
        //   pointData.buildName = explodebuildname.value
        //   pointData.floorNum = floornum.value
        // }
      }
    })
  // } else {
  //   // 从缓存中找警车数据
  //   let policeData = sessionStorage.get("basicData") || [];
  //   if (policeData.length > 0) {
  //     let info = null;
  //     let markerInfo = {};
  //     policeData.forEach((car) => {
  //       if (car.id === item.Id) {
  //         info = car.info;
  //         markerInfo = car.marker;
  //       }
  //     });
  //     markerData.value = markerInfo;
  //     if (info && Object.keys(info).length !== 0) {
  //       pointData.mc = info.mc||info.featureType;
  //       pointData.bz = info.bz;
  //       pointData.zf = info.zf;
  //       pointData.fontsize = info.fontsize;
  //       pointData.color1 = info.color1;
  //       pointData.valueNmber = info.valueNmber;
  //       pointData.featureType = info.featureType || null;
  //       pointData.buildName = info.buildName || null;
  //       pointData.floorNum = info.floorNum || null;
  //       pointData.status = info.status || null;
  //     }else{
  //       pointData.mc = markerInfo.text;
  //     }
  //   }
  // }
};
// 删除地位信息
const deleteMarker = async (data) => {
  let g = window.__g;
  await g.marker.delete(data.id);
  await g.markerLayer.delete(data.id)
  // useScreenStore().SET_watchBol(false);
  if (editScreen.value||y_aEdit.value) {
    deleteDrawData(data.id).then(res => {
      if (res.code === 0) {
        ElMessage.success('删除成功!')
        emitter.emit("refreshResource",data.id)
        // if (pointData.featureType) {
        //   useScreenStore().SET_watchBol(true);
        //   useWorkCockpitStore().set_showResouce(true)
        // } else {
        //   useScreenStore().SET_watchBol(false);
        // }
      }
    })
  }
  emit("closeRightBox", false);
  // 从缓存中找警车数据
  let carData = sessionStorage.get("basicData") || [];
  if (carData.length > 0) {
    let newData = carData.filter((item) => data.id !== item.id);
    sessionStorage.set("basicData", newData);
  }
};
// 确定
const handleSubmit = async () => {
  if (pointData.mc) {
    let g = window.__g;
    markerData.value.text = pointData.mc;
    g.marker.update(markerData.value);
        let obj = {
            id: markerData.value.id,
            groupId: markerData.value.groupId,
            userData:markerData.value.userData,
            coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
            range: [0, 1, 10000, 20000],
            viewHeightRange: [0, 10000],
            autoHeight: false,
            radius: 0,
            fixedSize: false,
            anchors: [-15, 34], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
            useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
            textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
            textOffset: [0, 0], // 文本偏移
            textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
            fontSize: 8, //字体大小
            fontOutlineSize: 1, //字体轮廓线大小
            fontColor: Color.White, //字体颜色
            displayMode: 0, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
            clusterByImage: false, // 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
            priority: 0, //避让优先级
            occlusionCull: true, //是否参与遮挡剔除
            markers: [
              {
                id: markerData.value.id,
                groupId: markerData.value.groupId,
                coordinate: markerData.value.coordinate, //坐标位置
                text: pointData.mc, //显示的文字
                imagePath: markerData.value.imagePath,
                imageSize: [30,34],
                hoverImagePath: markerData.value.imagePath, // 鼠标悬停时显示的图片路径
                hoverImageSize: [30,34], //鼠标悬停时显示的图片尺寸
              },
            ],
          };
      g.markerLayer.update(obj)    
  }
  if(pointData.status){
    let g = window.__g;
    console.log(markerData.value)
    markerData.value.textBackgroundColor = [1,0,0,0.75];
    g.marker.update(markerData.value);
  }
  let allObj = {
    id: markerData.value.id,
    info: pointData,
    marker: markerData.value,
  };
  // useScreenStore().SET_watchBol(false);
  if (editScreen.value||y_aEdit.value) {
    let query = {
      id: markerData.value.id,
      type: 'basic',
      data: allObj,
      position:position.value,
      refLineName:refLineName.value,
    }
    saveScreenDraw(query).then(res => {
      if (res.code === 0) {
        ElMessage.success('编辑成功')
        emitter.emit("refreshResource",query.data.id)
        // if (allObj.info.featureType) {
        //   useScreenStore().SET_watchBol(true);
        //   useWorkCockpitStore().set_showResouce(true)
        // } else {
        //   useScreenStore().SET_watchBol(false);
        // }
      }
    })
  }
  let data1 = sessionStorage.get("basicData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.id);
    newData.push(allObj);
    sessionStorage.set("basicData", newData);
  } else {
    sessionStorage.set("basicData", [allObj]);
  }

  nextTick(() => {
    handleClose();
  });
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
</script>

<style lang="scss" scoped>
@import "@/styles/func3.scss";

:deep(.add) {
  width: 400px;
  position: absolute;
  top: -20px;
  bottom: -20px;
  background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);

  .func-warp {
    height: calc(100% - 150px);
    display: flex;
    flex-direction: column;
    overflow: auto;
    padding: 4px 20px;

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
      .el-textarea__inner{
        background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
          border: 1px solid #5b6799;
        border-radius: 2px;
        color:#fff;
      }
      .el-input__count{
        background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
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
</style>
