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
        警车
        <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>
      <div class="func-warp">
        <el-scrollbar>
          <draggable
            :list="labelList"
            item-key="src"
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
                  <div class="name" style="padding-top: 12px">
                    {{ element.name }}
                  </div>
                </ul>
              </div>
            </template>
          </draggable>
        </el-scrollbar>
      </div>
    </div>

    <el-dialog
      v-model="openBol"
      width="386px"
      align-center
      :destory-on-close="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      class="my_Dialog"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">选择防线</div>
        </div>
      </template>
      <el-form
        ref="customForm"
        :inline="true"
        :model="dialogForm"
        :label-suffix="'：'"
        :label-width="110"
        class="customForm"
        size="large"
      >
        <el-row>
          <el-col :span="24">
            <el-radio-group v-model="dialogForm.fangxian">
              <el-radio value="一道防线">一道防线</el-radio>
              <el-radio value="二道防线">二道防线</el-radio>
              <el-radio value="三道防线">三道防线</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <div @click="cancel" class="sub_btn">取消</div>
          <div class="sub_btn sure_btn" @click="submit">确定</div>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, computed, watch, defineEmits } from "vue";
import draggable from "vuedraggable";
import useBasicStore from "@/store/modules/basicData";
import useScreenStore from "@/store/modules/screenStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";// 预案store
import useTaskStore from "@/store/modules/taskStore";
import { loadPicture } from "@/utils";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import { queryTaskInfo } from "@/api/task/index";
import { saveScreenDraw } from "@/api/task/task";
import { ElMessage, ElMessageBox } from "element-plus";
import emitter from "@/utils/emitter"
const { proxy } = getCurrentInstance();
const emit = defineEmits(["close"]);
let eventMap = computed(() => useBasicStore().eventSealAPI);
let screenInfo = computed(() => useScreenStore().screenInfo);
const editScreen = computed(() => useScreenStore().editScreen);
const y_aEdit = computed(()=>useEmergencyStore().editYA) // 预案是否调用接口
const y_a_Info = computed(()=>useEmergencyStore().YAInfo) // 预案信息
const isShowAdd = ref(false);
const drag = ref(false);
const addLine = ref(false);
const lineList = ref([]);
const items = ref({});
const dialogForm = ref({
  fangxian:''
})
const allObj = ref({})
const openBol = ref(false)
const labelList = ref([
  {
    src: loadPicture("./images/cloud/car/icon-1.png"),
    name: "巡逻车",
    objSrc:'/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/特种车辆/警车_1'
  },
  {
    src: loadPicture("./images/cloud/car/icon-2.png"),
    name: "护卫车",
    objSrc:'/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/特种车辆/警车_1'
  },
  {
    src: loadPicture("./images/cloud/car/icon-3.png"),
    name: "侦察车",
    objSrc:'/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/特种车辆/警车_1'
  },
  {
    src: loadPicture("./images/cloud/police/cb.png"),
    name: "船舶",
    objSrc:'/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/船/船_3'
  }
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
      item.select = true;
      useBasicStore().setAddPoint(true);
      items.value = item;
      proxy.$modal.msg("请点击地图选择点位");
    } else {
      item.select = false;
    }
  });
};
const extractImagePath =(url)=> { const parts = url.split('/images/'); return parts.length > 1 ? '/images/' + parts[1] : null; }
// 绘制场景标绘数据
const cancel = () => {
  let g = window.__g
  openBol.value = false
  dialogForm.value.fangxian = ''
  g.marker.delete(allObj.value.id)
  g.marker3d.delete(allObj.value.id)
  g.customObject.delete(allObj.value.id)
};
const addPoint = (arr, obj) => {
  let g = window.__g;
  dialogForm.value.fangxian = ''
  var markerObj = {
      id: `car${new Date().getTime()}`,
      groupId: "car",
      userData: obj.name,
      coordinate: arr, //坐标位置
      coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      text: obj.name, //显示的文字
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
      id:markerObj.id,
      pakFilePath: "@path:DTS_Library_6.1_240731.pak", //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
      assetPath:obj.objSrc, //资源目录，自定义对象在pak文件资源包里的相对路径
      location: arr, //位置坐标
      coordinateType: 0, // 坐标系类型
      rotation: [0, 0, 0], // 世界坐标系旋转
      range: [0, 10000], //可见范围
      groupId: "car",
      userData: markerObj.id,
      localRotation: [0, 90, 0], //模型自身旋转
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
        offset: [0, 0, 2], //偏移量
      }
    ]);
    // let allObj = {
    //   id: markerObj.id,
    //   info: {},
    //   customData:customObj,
    //   marker: markerObj,
    // };
    allObj.value = {
      id: markerObj.id,
      info: {},
      customData:customObj,
      marker: markerObj,
    };
    openBol.value = true
  // if (editScreen.value) {
  //   let query = {
  //     taskId: screenInfo.value.taskId,
  //     sceneId: screenInfo.value.id,
  //     planNode: "2",
  //     type: "car",
  //     policeType: "2",
  //     data: allObj,
  //   };
  //   saveScreenDraw(query).then((res) => {
  //     if (res.code === 0) {
  //        // 更新资源
  //        emitter.emit("refreshResource",query.data.id)
  //       if(screenInfo.value.taskId){
  //         queryTaskInfo({ id: screenInfo.value.taskId}).then((res) => {
  //         if (res.code === 0) {
  //           useTaskStore().SET_TASKINFO(res.data);
  //         }
  //       });
  //       }
  //     }
  //   });
  // }
  // if(y_aEdit.value){
  //     let query = {
  //       sceneId: y_a_Info.value.id,
  //       type: "car",
  //       data: allObj,
  //     };
  //     saveScreenDraw(query).then((res) => {
  //       if (res.code === 0) {
  //          // 更新资源
  //          emitter.emit("refreshResource",query.data.id)
  //           ElMessage.success('新增成功')
  //       }
  //     });
  //   }
  // let data1 = sessionStorage.get("carData");
  // if (data1 && data1.length > 0) {
  //   let newData = data1.filter((item) => item.id !== allObj.id);
  //   newData.push(allObj);
  //   sessionStorage.set("carData", newData);
  // } else {
  //   sessionStorage.set("carData", [allObj]);
  // }
  clearAddPoint();
};
// 提交
const submit = () => {
  allObj.value.info.fangxian = dialogForm.value.fangxian
   if (editScreen.value) {
    let query = {
      taskId: screenInfo.value.taskId,
      sceneId: screenInfo.value.id,
      planNode: "2",
      type: "car",
      policeType: "2",
      data: allObj.value,
    };
    saveScreenDraw(query).then((res) => {
      if (res.code === 0) {
        ElMessage.success('新增成功')
        openBol.value = false
         // 更新资源
         emitter.emit("refreshResource",query.data.id)
        if(screenInfo.value.taskId){
          queryTaskInfo({ id: screenInfo.value.taskId}).then((res) => {
          if (res.code === 0) {
            useTaskStore().SET_TASKINFO(res.data);
          }
        });
        }
      }
    });
  }
  if(y_aEdit.value){
      let query = {
        sceneId: y_a_Info.value.id,
        type: "car",
        data: allObj.value,
      };
      saveScreenDraw(query).then((res) => {
        if (res.code === 0) {
           // 更新资源
           openBol.value = false
           emitter.emit("refreshResource",query.data.id)
            ElMessage.success('新增成功')
        }
      });
    }
  let data1 = sessionStorage.get("carData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.value.id);
    newData.push(allObj.value);
    sessionStorage.set("carData", newData);
  } else {
    sessionStorage.set("carData", [allObj.value]);
  }
}
const clearAddPoint = () => {
  items.value = {};
  useBasicStore().setAddPoint(false);
  labelList.value.forEach((item) => {
    item.select = false;
  });
};
const handleClose = async () => {
  clearAddPoint();
  emit("close");
};
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
  max-height: 300px !important;
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
    height: 100px;
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
        width: 60px;
        height: 32px;
      }

      .name {
        // padding: 10px 0;
      }
    }
  }
}


:deep(.my_Dialog) {
  background: url("@/assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
  .heard_name {
    background: url("@/assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;
    .d_name {
      font-family: YouSheBiaoTiHei;
      font-weight: 400;
      font-size: 17px;
      color: #ffffff;
      height: 22px;
      line-height: 20px;
      padding-left: 20px;
    }
  }
  .el-dialog__headerbtn:active {
    background-color: transparent !important;
    outline: none !important;
    box-shadow: none !important;
  }
}
:deep(.el-dialog__headerbtn) {
  position: absolute;
  top: 10px;
  right: 20px;
  .el-dialog__close {
    color: #fff;
    font-size: 20px;
  }
}
:deep(.el-dialog__headerbtn :focus, .el-dialog__headerbtn:hover) {
  outline: none;
  box-shadow: none;
}
/* 解决按钮黑边框bug */
:deep(.el-button:focus) {
  outline: none;
}
:deep(.el-dialog__body) {
  // padding-right: 30px;
}
:deep(.dialog-footer) {
  display: flex;
  justify-content: end;
  padding-right: 20px;
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
:deep(.customForm) {
  .el-form-item__label {
    font-weight: 400;
    font-size: 14px;
    color: #ffffff;
    opacity: 0.8;
  }
  .el-form-item__content {
    .el-input {
      border: 1px solid #5b6799;
      border-radius: 2px;
      .el-input__wrapper {
        background: rgba(0, 12, 78, 0.5);
        box-shadow: none;
      }
      .el-input__inner {
        font-size: 14px;
        color: #ffffff;
        opacity: 0.8;
      }
    }
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
  }
  .el-radio__label {
    color: #fff;
  }
}
</style>
