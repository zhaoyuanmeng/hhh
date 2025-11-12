<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-20 18:58:23
 * @LastEditTime: 2024-06-21 13:23:37
 * @LastEditors: Alex
-->
<template>
  <div class="app-container home">
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
          <div class="d_name">警力部署</div>
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
            <el-form-item
              label="位置描述"
              prop="weizhi"
              :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
              style="width: 100%"
            >
              <el-input
                v-model="dialogForm.weizhi"
                placeholder="请输入..."
              ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="部署数量"
              prop="num"
              :rules="[
                { required: true, message: '必填', trigger: 'blur' },
                { validator: validatePositiveInteger, trigger: 'blur' },
              ]"
              style="width: 100%"
            >
              <el-input v-model="dialogForm.num"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="分组名称" style="width: 100%">
              <el-select
                v-model="selectGroup"
                placeholder="请选择已有分组"
                style="width: 100%"
                v-if="options.length > 0"
              >
                <el-option
                  v-for="(item, index) in options"
                  :key="index"
                  :label="item"
                  :value="item"
                />
              </el-select>
              <el-input
                v-model="dialogForm.group"
                placeholder="新建分组"
                :style="{ marginTop: options.length > 0 ? '16px' : '0' }"
              ></el-input>
            </el-form-item>
          </el-col>
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
import {
  reactive,
  ref,
  computed,
  getCurrentInstance,
  watch,
  nextTick,
  onMounted,
} from "vue";
import useScreenStore from "@/store/modules/screenStore";
import useTaskStore from "@/store/modules/taskStore";
import { queryTaskInfo } from "@/api/task/index";
import useFloorStore from "@/store/modules/floorStore";
import {
  saveScreenDraw,
  searchNodePlanToScreen,
  getDrawDataForScreen,
  getMarkersForFloorAllData,
  searchGroupList,
} from "@/api/task/task";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter";
const { proxy } = getCurrentInstance();
const openBol = ref(true);
let drawData = computed(() => useScreenStore().screenDrawData);
let policeLine = computed(() => useScreenStore().screenLineData);
let screenInfo = computed(() => useScreenStore().screenInfo);
const openFloor = computed(() => useFloorStore().openFloor); // 楼层是否炸开
const floornum = computed(() => useFloorStore().floornum); // 当前楼层点击楼层数
let explodebuildname = computed(() => useFloorStore().explodebuildname); //当前点击的楼栋名称
const dialogForm = reactive({
  weizhi: "",
  num: 1,
  fangxian: "",
  group: "", // 分组
});
const selectGroup = ref("");
const options = ref(["组1", "组2", "组三"]);
const validatePositiveInteger = (rule, value, callback) => {
  if (!value || parseInt(value, 10) <= 0 || !/^\d+$/.test(value)) {
    callback(new Error("请输入正整数"));
  } else {
    callback();
  }
};
onMounted(() => {
  // 请求分组数据
  searchGroupList({ sceneId: screenInfo.value.id }).then((res) => {
    options.value = res.data;
  });
});
const cancel = () => {
  resetForm("customForm");
  useScreenStore().set_screenModal(false);
  // 删除缓存的数据和场景图标
  deleteDrawMap(drawData.value);
};
const submit = async () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
      if (!dialogForm.group) {
        if (selectGroup.value) {
          dialogForm.group = selectGroup.value;
        }
      }
      dialogForm.num = Number(dialogForm.num);
      // 组件做标绘数据的保存 数据组装
      if (openFloor.value && floornum.value) {
        drawData.value.info.buildName = explodebuildname.value;
        drawData.value.info.floorNum = floornum.value;
      }
      let query = {
        taskId: screenInfo.value.taskId,
        sceneId: screenInfo.value.id,
        planNode: "警力部署",
        type: drawData.value.marker.groupId,
        policeType:
          drawData.value.marker.userData === "快反力量" ||
          drawData.value.marker.userData === "机动力量"
            ? "应急处突警力"
            : "执勤警力",
        data: 
        drawData.value.marker.groupId === "uav"
            ? {
          id: drawData.value.id,
          info: { ...dialogForm, ...drawData.value.info },
          marker: drawData.value.marker,
          customData: drawData.value.customData,
          uavData: drawData.value.uavData,
        }
            : {
            id: drawData.value.id,
            info: { ...dialogForm, ...drawData.value.info },
            marker: drawData.value.marker,
            customData: drawData.value.customData,
          },
      };
      saveScreenDraw(query).then((res) => {
        if (res.code === 0) {
          proxy.$modal.msgSuccess("添加成功");
          emitter.emit("refreshPolice");
          emitter.emit("refreshResource", query.data.id);
          if (openFloor.value && floornum.value) {
            getMarkersForFloorAllData({
              buildName: explodebuildname.value,
            }).then((res) => {
              if (res.data?.length) {
                useFloorStore().set_floorMarkers(res.data);
              } else {
                useFloorStore().set_floorMarkers([]);
              }
            });
          }
          // 重新设置缓存值警力、警车、基础数据
          //         sessionStorage.remove("policeData"); // 清除缓存的警力
          // sessionStorage.remove("carData"); // 清除缓存的警车
          // sessionStorage.remove("basicData"); // 清除缓存的基础数据
          let data1 =
            drawData.value.marker.groupId === "police"
              ? sessionStorage.get("policeData")
              : drawData.value.marker.groupId === "car"
              ? sessionStorage.get("carData")
              : drawData.value.marker.groupId === "uav"
              ? sessionStorage.get("uavData")
              : sessionStorage.get("basicData");
          let newData = data1.filter((item) => item.id !== drawData.value.id);
          newData.push(query.data);
          if (drawData.value.marker.groupId === "police") {
            sessionStorage.set("policeData", newData);
          }
          if (drawData.value.marker.groupId === "car") {
            sessionStorage.set("carData", newData);
          }
          if (drawData.value.marker.groupId === "basic") {
            sessionStorage.set("basicData", newData);
          }
          if (drawData.value.marker.groupId === "uav") {
            sessionStorage.set("uavData", newData);
          }
          if (screenInfo.value.taskId) {
            queryTaskInfo({ id: screenInfo.value.taskId }).then((res) => {
              if (res.code === 0) {
                useTaskStore().SET_TASKINFO(res.data);
              }
            });
          }
          useTaskStore().SET_TASKINFO(res.data);
          nextTick(() => {
            resetForm("customForm");
            useScreenStore().set_screenModal(false);
          });
          searchNodePlanToScreen({
            sceneId: screenInfo.value.id,
            planNode: "警力部署",
          }).then((res) => {
            // 场景详情面板
            if (res.code === 0) {
              res.data.map((item) => {
                item.data = item.data
                  ? item.data
                  : { zrld: "", phone: "", time: "" };
              });
              useScreenStore().set_sceneCardInfo(res.data);
            }
          });
        }
      });
      if (!isEmptyObject(policeLine.value)) {
        let query = {
          taskId: screenInfo.value.taskId,
          sceneId: screenInfo.value.id,
          planNode: "警力部署",
          type: "lines",
          policeType: "执勤警力",
          data: policeLine.value,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            if (screenInfo.value.taskId) {
              queryTaskInfo({ id: screenInfo.value.taskId }).then((res) => {
                if (res.code === 0) {
                  useScreenStore().set_screenLineData({});
                }
              });
            }
          }
        });
      }
    }
  });
  //
};
// 函数用于检查对象是否为空
const isEmptyObject = (obj) => {
  return Object.keys(obj).length === 0 && obj.constructor === Object;
};
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
};
// 清除场景数据以及缓存数据
const deleteDrawMap = async (obj) => {
  let g = window.__g;
  await g.marker.delete(obj.id);
  await g.polyline.delete(obj.id);
  await g.customObject.delete(obj.id);
  await g.marker3d.delete(obj.id);
  if (obj.info && obj.info.lineData) {
    g.polyline.delete(obj.info.lineData.id);
  }
  if (obj.customData) {
    g.customObject.delete(obj.customData.id);
  }
  if (obj.uavData) {
    g.radiationPoint.delete(obj.uavData.id);
  }
  // 从缓存中清除对应的标绘数据
  let QXZS_pointLine = sessionStorage.get("QXZS_pointLine") || []; // 清除缓存的线
  let QXZS_polygon = sessionStorage.get("QXZS_polygon") || []; // 清除缓存的面
  let QXZS_polygon3d = sessionStorage.get("QXZS_polygon3d") || []; // 清除缓存的多边形
  let policeData = sessionStorage.get("policeData") || []; // 清除缓存的警力
  let carData = sessionStorage.get("carData") || []; // 清除缓存的警车
  let basicData = sessionStorage.get("basicData") || []; // 清除缓存的基础数据
  let uavData = sessionStorage.get("uavData") || []; // 清除缓存的无人机数据
  if (QXZS_pointLine.length > 0) {
    let newData = QXZS_pointLine.filter((item) => obj.id !== item.id);
    sessionStorage.set("QXZS_pointLine", newData);
  }
  if (QXZS_polygon.length > 0) {
    let newData = QXZS_polygon.filter((item) => obj.id !== item.id);
    sessionStorage.set("QXZS_polygon", newData);
  }
  if (QXZS_polygon3d.length > 0) {
    let newData = QXZS_polygon3d.filter((item) => obj.id !== item.id);
    sessionStorage.set("QXZS_polygon3d", newData);
  }
  if (policeData.length > 0) {
    let newData = policeData.filter((item) => obj.id !== item.id);
    sessionStorage.set("policeData", newData);
  }
  if (carData.length > 0) {
    let newData = carData.filter((item) => obj.id !== item.id);
    sessionStorage.set("carData", newData);
  }
  if (basicData.length > 0) {
    let newData = basicData.filter((item) => obj.id !== item.id);
    sessionStorage.set("basicData", newData);
  }
  if (uavData.length > 0) {
    let newData = uavData.filter((item) => obj.id !== item.id);
    sessionStorage.set("uavData", newData);
  }
};
</script>

<style scoped lang="scss">
:deep(.my_Dialog) {
  background: url("../../assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
  .heard_name {
    background: url("../../assets/panel/right_panel.png") no-repeat;
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
