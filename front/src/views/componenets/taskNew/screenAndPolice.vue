<template>
  <div class="task_police_box">
    <el-collapse v-model="activeName" accordion @change="changeActive">
      <el-collapse-item
        :title="`${item}`"
        :name="item"
        v-for="(item, index) in activeList"
        :key="index"
      >
        <div class="taskPolice">
          <div class="edit_box">
            <div class="edit_btn" @click="edit(item)">编辑</div>
            <div
              @click="getCood(item)"
              class="taskViewCoodr1"
              v-if="
                activeName === '隐患排查' ||
                activeName === '应急力量' ||
                activeName === '应急避险点' ||
                activeName === '应急医院' ||
                activeName === '警力部署'
              "
            ></div>
          </div>
          <div class="data_content">
            <el-row v-if="activeName === '隐患排查'">
              <el-col
                :span="24"
                v-for="(option, index) in policArr"
                :key="index"
              >
                <div
                  class="all_data"
                  style="margin-bottom: 20px; padding: 16px"
                >
                  {{ option.data.msg }}
                </div>
              </el-col>
            </el-row>
            <el-row v-if="activeName === '应急力量'">
              <el-col
                :span="24"
                v-for="(option, index) in policArr"
                :key="index"
              >
                <div
                  class="all_data"
                  style="margin-bottom: 20px; padding: 16px"
                >
                  <div class="item">
                    <div class="left">责任领导：{{ option.data.zrld }}</div>
                    <div class="right"></div>
                  </div>
                  <div class="item">
                    <div class="left">责任单位：{{ option.data.zrdw }}</div>
                    <div class="right">电话：{{ option.data.phone }}</div>
                  </div>
                  <div class="item">
                    <div class="left">
                      应急力量：{{ option.data.emergency }}
                    </div>
                    <div class="right"></div>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row
              v-if="activeName === '应急避险点' || activeName === '应急医院'"
            >
              <el-col
                :span="24"
                v-for="(option, index) in policArr"
                :key="index"
              >
                <div
                  class="all_data"
                  style="margin-bottom: 20px; padding: 16px"
                >
                  <div class="item">
                    <div class="left">责任领导：{{ option.data.zrld }}</div>
                    <div class="right"></div>
                  </div>
                  <div class="item">
                    <div class="left">责任单位：{{ option.data.zrdw }}</div>
                    <div class="right">电话：{{ option.data.phone }}</div>
                  </div>
                  <div class="item">
                    <div class="left">
                      {{
                        activeName === "应急避险点" ? "应急避险点" : "应急医院"
                      }}：<span style="color: aqua">{{ option.data.bxd }}</span>
                    </div>
                    <div class="right"></div>
                  </div>
                  <div class="item">
                    <div class="left">联系人：{{ option.data.lxr }}</div>
                    <div class="right">联系方式：{{ option.data.lxfs }}</div>
                  </div>
                  <div class="item">
                    <div class="left" style=" flex: none">
                      {{
                        activeName === "应急避险点" ? "避险点" : "医院"
                      }}路线：
                    </div>
                    <div class="right" v-html="option.data.line"></div>
                  </div>
                  <!-- <div v-if="option.data.pointNames">
                    <div class="item" v-for="(p,point) in option.data.pointNames">
                      <div class="left">{{activeName}}{{point+1}}：<span style="margin-left: 4px;">{{ p }}</span></div>
                    </div>
                  </div>
                  <div v-if="option.data.lineNames">
                    <div class="item" v-for="(p,point) in option.data.lineNames">
                      <div class="left">{{activeName}}路线{{point+1}}：<span style="margin-left: 4px;">{{ p }}</span></div>
                    </div>
                  </div> -->
                </div>
              </el-col>
            </el-row>
            <el-row v-if="activeName === '警力部署'" class="police_setting">
              <el-col
                :span="24"
                v-for="(option, index) in policArr"
                :key="index"
              >
                <div
                  class="all_data"
                  style="margin-bottom: 20px; padding: 16px"
                >
                  <div class="item">
                    <div class="left">责任领导：{{ option.data.zrld }}</div>
                    <div class="right"></div>
                  </div>
                  <div class="item">
                    <div class="left">责任单位：{{ option.data.zrdw }}</div>
                    <div class="right">电话：{{ option.data.phone }}</div>
                  </div>
                  <div class="item">
                    <div class="left">备注信息：{{ option.data.bzxx }}</div>
                  </div>
                  <div class="item" style="align-items: baseline">
                    <div class="left_1" style="width: 95px">警力部署：</div>
                    <div class="right_1">
                      共部署警力<span style="color: aqua">{{
                        option.policeTypeStatistics.total
                      }}</span
                      >人、 现场执勤警力<span style="color: aqua">{{
                        option.policeTypeStatistics.onduty
                      }}</span
                      >人、 应急处突警力<span style="color: aqua">{{
                        option.policeTypeStatistics.emergency
                      }}</span
                      >人
                    </div>
                  </div>
                </div>
                <div
                  class="child_box"
                  v-for="(child, idx) in option.extDataList"
                  :key="idx"
                  style="
                    margin-bottom: 20px;
                    background: rgba(0, 0, 0, 0.4);
                    padding: 16px;
                  "
                >
                  <div class="fxmc">
                    <div style="color: aqua; font-size: 16px">
                      {{ child.policeData.fangxian }}
                    </div>
                    <div
                      class="edit_btn"
                      @click="editPolice(child, index, idx)"
                    >
                      编辑
                    </div>
                  </div>
                  <div class="fxmc_tj" style="margin-bottom:8px">
                    共部署警力<span style="color: aqua">{{
                      child.policeData.policeTypeOfLine.total
                    }}</span
                    >人、现场执勤警力<span style="color: aqua">{{
                      child.policeData.policeTypeOfLine.onduty
                    }}</span
                    >人、应急处突警力<span style="color: aqua">{{
                      child.policeData.policeTypeOfLine.emergency
                    }}</span
                    >人
                  </div>
                  <div class="phone_p">
                    <div class="policeSet">责任人：{{ child.data.zrld }}</div>
                    <div class="policeSet">电话：{{ child.data.phone }}</div>
                  </div>
                  <div class="unit">责任单位：{{ child.data.zrdw }}</div>
                  <div class="unit">上岗时间：{{ child.data.sgsj }}</div>
                  <div class="police_box">
                    <div class="title_police">警力部署</div>
                    <div
                      class="group_list"
                      v-for="(group, index) in child.policeData.groupData"
                    >
                      <div
                        class="group_name"
                        style="color: aqua; font-size: 16px"
                      >
                        {{ group.group }}
                      </div>
                      <div class="right_1" v-if="group.group"  style="margin-bottom:8px">
                        共部署警力<span style="color: aqua">{{
                         group.policeNum
                        }}</span
                        >人、<span
                          v-for="(types, index) in group.policeTypeOfGroup"
                          :key="index"
                        >
                          {{ `${types.post}`
                          }}<span style="color: aqua">{{ types.num }}人</span
                          ><span
                            v-if="index !== group.policeTypeOfGroup.length - 1"
                            >、</span
                          >
                        </span>
                      </div>
                      <div
                        class="police_item"
                        v-for="(p, i) in group.jinglibushu"
                        :key="i"
                        @click="focusMarkerShowHide(p)"
                        :class="{ activeColor: clickId === p.id }"
                      >
                        <div class="location">{{ p.weizhi }}</div>
                        <div class="policeName">{{ p.leixing }}</div>
                        <div class="policeNum">{{ p.num }}人</div>
                      </div>
                    </div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
  <!-- 警力部署编辑弹框 -->
  <el-dialog
    v-model="openModal"
    width="686px"
    @close="cancel"
    align-center
    :destory-on-close="false"
    :close-on-click-modal="false"
    append-to-body
    class="my_Dialog_text"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ activeName }}</div>
      </div>
    </template>
    <el-form
      ref="customForm"
      :inline="true"
      :model="dialogForm"
      :label-suffix="'：'"
      :label-width="100"
      class="form_custom_class"
      style="width: 95%"
    >
      <el-row class="edit_fx_name" v-if="fx_name">
        {{ fx_name }}
      </el-row>
      <el-row v-if="activeName === '应急避险点' || activeName === '应急医院'">
        <div class="dialog_police">
          <div class="left"></div>
          <div class="right" @click="y_a_import">应急预案库导入</div>
        </div>
      </el-row>
      <el-row>
        <FormCustom
          v-for="(items, index) in dialogFormData"
          :key="index"
          :obj="dialogForm"
          :option="items"
          @changeCotrl="functionEvents"
          @changeDialogCtrl="functionEvents1"
        />
      </el-row>
      <el-row v-if="police_data && police_data.length > 0">
        <div class="dialog_police">
          <div class="left">警力部署</div>
          <div class="right" v-if="false">载入地图数据</div>
        </div>
        <div
          class="dialog_police_item"
          v-for="(dialog, d) in police_data"
          :key="d"
        >
          <div class="left">地点：{{ dialog.weizhi }}</div>
          <div class="center">类型：{{ dialog.leixing }}</div>
          <div class="right">数量：{{ dialog.num }}</div>
        </div>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button
          type="primary"
          @click="submitForm"
          style="background: #274eef"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
  <!-- 应急预案库弹框列表 -->
  <el-dialog
    v-model="openYAModal"
    width="686px"
    @close="cancelYaModal"
    align-center
    :destory-on-close="false"
    :close-on-click-modal="false"
    append-to-body
    class="my_Dialog_text"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">应急预案库列表</div>
      </div>
    </template>
    <div class="table_list">
      <el-radio-group v-model="selectedRow" style="width: 100%">
        <el-table
          :data="tableData"
          style="width: 100%"
          height="400px"
          highlight-current-row
        >
          <el-table-column type="index" width="50"> </el-table-column>
          <el-table-column prop="name" label="名称"> </el-table-column>
          <el-table-column prop="type" label="类型" width="180">
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="{ row }">
              <el-radio :value="row.id" @click="selectRow(row)">选择</el-radio>
            </template>
          </el-table-column>
        </el-table>
      </el-radio-group>
    </div>
    <template #footer>
      <div class="dialog-footer" style="text-align: center">
        <el-button
          type="primary"
          @click="getRowData"
          style="background: #274eef"
          >确 定</el-button
        >
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import JsonData from "./screenJson";
import FormCustom from "./formCustom.vue";
import {
  ref,
  computed,
  getCurrentInstance,
  reactive,
  onBeforeUnmount,
  onUnmounted,
} from "vue";
import useTaskStore from "@/store/modules/taskStore";
import {
  searchNodePlanToScreen,
  saveScreenPlan,
  saveScreenPlanFX,
  getDrawDataForScreen,
} from "@/api/task/task";
import { getSceneDataForIdAndToDtaw } from "@/api/workCockpit/index.js";
import { drawLinesName } from "@/views/WorkCockpit/utils";
import { ElMessage, ElMessageBox } from "element-plus";
import {
  getEmcyListdata,
  importYAData,
  getEmcyInfo,
} from "@/api/task/emergency";
import { dialogRules } from "@/utils/index";
import useFloorStore from "@/store/modules/floorStore";
import { drawScreenData } from "./util";
import { closeFloors } from "@/components/SmartMap/js/utils";
import { flattenTreeData, loadPicture } from "@/utils";
import emitter from "@/utils/emitter";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import { drawContrlData } from "./util";
let policeBox = computed(() => useWorkCockpitStore().policeCheckBox);
emitter.on("refreshPolice", () => {
  console.log("刷新页面");
  activeName.value === "警力部署" && changeActive("警力部署");
});
onBeforeUnmount(() => {
  emitter.off("refreshPolice");
  clearTimerFun();
});
onUnmounted(() => {
  emitter.off("refreshPolice");
  clearTimerFun();
});
const { proxy } = getCurrentInstance();
let taskInfo = computed(() => useTaskStore().taskInfo); // 任务信息信息
const screenInfo = computed(() => useTaskStore().screenModalInfo);
const activeName = ref("");
const selectVal = ref("");
const collapseData = ref({});
//预案模块star
const openModal = ref(false);
const openYAModal = ref(false);
const tableData = ref([]);
const selectedRow = ref(null);
const selectedRowData = ref({});
const drawDataList = ref([]);
//预案模块end
const dialogForm = ref({});
const dialogFormData = ref([]);
const updateNodeId = ref(null);
const edit_type = ref(false);
const policArr = ref([]); // 警力部署数据
const fx_name = ref(""); // 防线名称
const police_data = ref([]); // 编辑单个防线的警力部署
const editPoliceId = ref(""); // 编辑单个警力部署的id
const activeList = ref([
  "隐患排查",
  "警力部署",
  "人员政审",
  "互动人员管控",
  "社会面摸排",
  "安全检查",
  "“低慢小”飞行器",
  "地下管网排查",
  "应急力量",
  "应急避险点",
  "应急医院",
  "重点人",
]);
// 具体岗哨警力部署警力信息地图联动
const markerTimer = ref(null);
const showFlag = ref(true);
const clickId = ref("");
const focusMarkerShowHide = async (data) => {
  console.log(data)
  if (!policeBox.value) {
    useWorkCockpitStore().set_checkBox("police", true);
    drawContrlData(true, "police");
  }
  if (clickId.value === data.id) {
    let g = window.__g;
    clickId.value = "";
    if (markerTimer.value) {
      clearInterval(markerTimer.value);
      markerTimer.value = null;
      showFlag.value = true;
    }
    await g.marker.show(data.id);
    return;
  }
  clearTimerFun().then(async () => {
    let g = window.__g;
    let info = await g.marker.get(data.id);
    if (info.result !== 0) {
      let str 
      if(data.buildname&&data.floornum){
        str = `${data.buildname}${data.floornum}层数据`;
      }else{
        str = `数据没勾选`;
      }
      ElMessage.warning(str);
      return;
    }
    await g.marker.focus(data.id, 20, 0.1);
    clickId.value = data.id;
    markerTimer.value = setInterval(() => {
      if (showFlag.value) {
        showMrkers(data.id);
      } else {
        hideMarkers(data.id);
      }
    }, 1000);
  });
};
const showMrkers = async (id) => {
  let g = window.__g;
  await g.marker.show(id);
  showFlag.value = false;
};
const hideMarkers = async (id) => {
  let g = window.__g;
  await g.marker.hide(id);
  showFlag.value = true;
};
const clearTimerFun = async () => {
  let g = window.__g;
  if (clickId.value) {
    await g.marker.show(clickId.value);
    clickId.value = "";
  }
  if (markerTimer.value) {
    clearInterval(markerTimer.value);
    markerTimer.value = null;
    showFlag.value = true;
  }
};
function isEmptyObject(obj) {
  return Object.keys(obj).length === 0;
}
const changeActive = (e) => {
  clearTimerFun();
  if (e) {
    let params = { sceneId: screenInfo.value.id, planNode: e };
    selectVal.value = e;
    searchNodePlanToScreen(params).then((res) => {
      edit_type.value = false;
      if (res.data && res.data.length > 0) {
        collapseData.value = res.data[0].data;
        updateNodeId.value = res.data[0].id;
        if (res.data[0].data && !isEmptyObject(res.data[0].data)) {
          dialogForm.value = res.data[0].data;
          if (res.data[0].data.viewData) {
            let g = window.__g;
            g.camera.set(res.data[0].data.viewData, 1.5);
          }
        }
        dialogFormData.value = JsonData[e];
        policArr.value = res.data;
      } else {
        collapseData.value = {};
        updateNodeId.value = null;
        dialogForm.value = {};
        dialogFormData.value = [];
        policArr.value = [];
      }
    });
  }
};
// 应急预案库导入
const y_a_import = () => {
  openYAModal.value = true;
  console.log(screenInfo.value, activeName.value);
  getEmcyListdata({
    basicDataId: screenInfo.value.basicDataId,
    type: activeName.value,
  }).then((res) => {
    console.log(res);
    tableData.value = res.data;
  });
};

const selectRow = (row) => {
  console.log(row);
  selectedRowData.value = row.data;
  getEmcyInfo({ id: row.id }).then((res) => {
    console.log(res);
    if (res.data.drawDataList?.length) {
      let arr = [];
      for (const item of res.data.drawDataList) {
        let obj = {
          taskId: screenInfo.value.taskId,
          sceneId: screenInfo.value.id,
          planNode: activeName.value,
          type: item.type,
          data: item.data,
        };
        arr.push(obj);
      }
      drawDataList.value = arr;
    } else {
      ElMessage.warning("当前方案下没有标绘数据");
      drawDataList.value = [];
    }
  });
};
const getRowData = () => {
  console.log(selectedRowData.value);
  dialogForm.value = selectedRowData.value;
  cancelYaModal();
};
const cancelYaModal = () => {
  openYAModal.value = false;
  selectedRow.value = null;
  tableData.value = [];
  selectedRowData.value = {};
};
// endding
const edit = (item) => {
  console.log(item, collapseData.value);
  openModal.value = true;
  edit_type.value = false;
  fx_name.value = "";
  // 封装数据
  getData(JsonData[item], collapseData.value);
};

// 视角保存
const getCood = async (item) => {
  console.log(item);
  let g = window.__g;
  let cood = await g.camera.get();
  console.log(cood);
  let coodr = { viewData: cood.camera };
  let obj = dialogForm.value;
  if (obj.viewData) {
    delete obj.viewData;
  }
  let data = {
    sceneId: screenInfo.value.id,
    planNode: item,
    data: { ...obj, ...coodr },
    id: updateNodeId.value || "",
  };
  saveScreenPlan(data).then((res) => {
    if (res.code === 0) {
      ElMessage.success(`${item}视角保存成功`);
      changeActive(item);
    }
  });
};
const editPolice = (item, index, idx) => {
  console.log(item, collapseData.value);
  edit_type.value = true;
  openModal.value = true;
  // 封装数据
  getData(
    JsonData[item.planNode === "警力部署" ? "警力部署child" : item.planNode],
    item.data
  );
  fx_name.value = item.policeData.fangxian;
  police_data.value = item.policeData.jinglibushu;
  editPoliceId.value = item.id;
  // if (item.data) {
  //   dialogForm.value = item.data;
  // }
};
const submitForm = () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
      if (drawDataList.value?.length) {
        importYAData(drawDataList.value).then((res) => {
          if (res.code === 0) {
            // 更新资源
            emitter.emit("refreshResource", "update");
            // drawDataList.value = [];
            // // 重新绘制标绘数据
            // getDrawDataForScreen({ sceneId: screenInfo.value.id }).then(
            //   (res) => {
            //     drawScreenData(res.data);
            //     useFloorStore().set_floorMarkers([]);
            //     closeFloors(); // 清除楼层炸开数据
            //     getSceneDataForIdAndToDtaw({sceneIds:screenInfo.value.id}).then(res=>{
            //     if(screenInfo.value.type!=='1'){
            //       if(res.data.basicData?.length){
            //         drawMarkerScreen(screenInfo.value.id, screenInfo.value.sceneName, res.data.basicData)
            //       }
            //     }
            //     // if (res.data.drawLineData?.length) {
            //     //   // 绘制路线名称
            //     //   drawLinesName(res.data.drawLineData)
            //     // }
            //   })
            //   }
            // );
          }
        });
      }
      if (fx_name.value) {
        dialogForm.value.fangxian = fx_name.value;
      }
      if (edit_type.value) {
        let data = {
          sceneId: screenInfo.value.id,
          planNode: activeName.value,
          data: dialogForm.value,
          id: editPoliceId.value,
        };
        saveScreenPlanFX(data).then((res) => {
          if (res.code === 0) {
            openModal.value = false;
            changeActive(activeName.value);
          }
        });
      } else {
        let data = {
          sceneId: screenInfo.value.id,
          planNode: activeName.value,
          data: dialogForm.value,
          id: updateNodeId.value,
        };
        saveScreenPlan(data).then((res) => {
          console.log(res);
          if (res.code === 0) {
            openModal.value = false;
            changeActive(activeName.value);
          }
        });
      }
    }
  });
};
const cancel = () => {
  resetForm("customForm");
  dialogForm.value = {};
  dialogFormData.value = [];
  police_data.value = [];
  edit_type.value = false;
  changeActive(activeName.value);
};
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
};
const functionEvents = () => {};
const functionEvents1 = () => {};
const getData = (arr, formObj) => {
  if (arr === undefined || arr === null || arr.length === 0) {
    return;
  }
  let param = [];
  arr.map((item) => {
    let obj = item;
    if (item.required) {
      if (item.zdlx === 0 || item.zdlx === 3 || item.zdlx === 4) {
        obj.dialogRules = dialogRules("blur");
      }
      if (item.zdlx === 1 || item.zdlx === 2) {
        obj.dialogRules = dialogRules("change");
      }
    }
    if (formObj) {
      dialogForm.value = formObj;
    } else {
      if (item.yxdx) {
        dialogForm.value[item.cczd] = [];
      } else {
        dialogForm.value[item.cczd] = "";
      }
    }

    param.push(obj);
  });
  dialogFormData.value = param;
};

// 绘制现场和住地的marker
const drawMarkerScreen = async (sceneId, sceneName, arr) => {
  let g = window.__g;
  let markerarr = [];
  for (const item of arr) {
    let o = {
      id: item.id,
      groupId: sceneId,
      userData: sceneName,
      coordinate: [item.geojson.coordinates[0], item.geojson.coordinates[1], 1], //坐标位置
      coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 55],
      range: [0, 1500000], //可视范围
      imageSize: [40, 55], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(`./images/cockpit/${item.type}.png`),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 185000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      // textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
      textBackgroundColor:
        item.type === "现场基本情况" ? Color.Yellow : Color.Crimson, //文本背景颜色
      fontSize: 12, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: true, // 自动判断下方是否有物体
      displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true,
    };
    markerarr.push(o);
  }
  if (markerarr.length > 0) {
    g.marker.add(markerarr);
  }
};
</script>
<style lang="scss" scope>
.activeColor {
  color: aqua;
}
.taskViewCoodr1 {
  width: 20px;
  height: 20px;
  background: url("@/assets/basic/视角锁定@1x.png") no-repeat;
  background-size: 100% 100%;
  cursor: pointer;
  margin-left: 16px;
}
.task_police_box {
  margin-top: 7px;

  .el-collapse-item__header {
    background: rgba(28, 53, 155, 0.5) !important;
    padding-left: 60px !important;
  }

  .el-collapse-item__arrow {
    position: absolute;
    left: 40px !important;
  }

  .edit_box {
    display: flex;
    align-items: center;
    justify-content: end;
    margin-right: 20px;
    .edit_btn {
      cursor: pointer;
      width: 84px;
      height: 32px;
      background: #274eef;
      line-height: 32px;
      text-align: center;
      right: 20px;
    }
  }

  .form_custom_class {
    .el-form-item__label {
      color: #fff !important;
    }
  }
  .data_content {
    .show_data {
      display: flex;
      padding: 0 20px;
      height: 30px;
      align-items: center;
      .left_name {
        min-width: 60px;
      }
    }
  }
  .police_setting {
    padding: 0 16px;
    .fxmc {
      font-weight: bold;
      font-size: 16px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .edit_btn {
        width: 60px;
        height: 25px;
        line-height: 25px;
        text-align: center;
        color: #fff;
        background: #386ef2;
        cursor: pointer;
        font-size: 12px;
      }
    }
    .phone_p {
      display: flex;
      align-items: center;
      height: 24px;
      .policeSet {
        flex: 1;
        font-size: 14px;
      }
    }
    .unit {
      line-height: 24px;
    }
    .police_box {
      .title_police {
        font-size: 14px;
        line-height: 24px;
      }
      .police_item {
        display: flex;
        align-items: center;
        padding-left: 30px;
        height: 30px;
        .location {
          width: 50%;
        }
        .policeName {
          width: 30%;
        }
        .policeNum {
          flex: 1;
        }
      }
      .group_list {
        background: rgba(0, 0, 0, 0.3);
        margin-bottom: 10px;
      }
    }
  }
  .all_data {
    .item {
      display: flex;
      align-items: center;
      .left {
        flex: 1;
      }
      .right {
        flex: 1;
      }
    }
  }
}
.dialog_police {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  color: #fff;
  margin-bottom: 12px;
  .left {
    padding-left: 30px;
  }
  .right {
    width: 100px;
    height: 28px;
    opacity: 1;
    background: #386ef2;
    text-align: center;
    line-height: 28px;
    cursor: pointer;
    font-size: 12px;
  }
}
.dialog_police_item {
  width: 100%;
  display: flex;
  align-items: center;
  height: 36px;
  margin-bottom: 12px;
  padding-left: 100px;
  color: #fff;
  .left {
    width: 55%;
  }
  .center {
    width: 30%;
  }
  .right {
    flex: 1;
  }
}
</style>
<style lang="scss">
.my_Dialog_text {
  .el-dialog__body {
    overflow: visible !important;
  }
}
.edit_fx_name {
  font-size: 16.91px;
  margin: 10px 0;
  color: #ffffff;
  padding-left: 24px;
  padding-bottom: 10px;
}
</style>
