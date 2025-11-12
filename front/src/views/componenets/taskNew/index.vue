<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 11:20:59
 * @LastEditors: Alex
-->
<template>
  <div class="taskStart_content">
    <transition name="slide" v-if="isShowList">
      <div class="left_box">
        <div class="heard">
          <div class="go_back" @click="gobackTo"></div>
          {{ taskInfo.taskName }}
          <div class="close_modal" @click="gobackTo" v-if="false"></div>
        </div>
        <div class="taskInfo" style="position: relative">
          <div @click="getCood(taskInfo.id)" class="taskViewCoodr">
            任务视角
          </div>
          <div class="top_info">{{ taskInfo.taskName }}</div>
          <div class="task_box">
            <div class="item_box">
              <div class="left">任务等级：</div>
              <div class="right" style="display: flex" :class="taskInfo.taskLevel === '一级加强'
                  ? 'yjjq'
                  : taskInfo.taskLevel === '一级任务'
                    ? 'yjrw'
                    : taskInfo.taskLevel === '二级任务'
                      ? 'ejrw'
                      : taskInfo.taskLevel === '三级任务'
                        ? 'sjrw'
                        : 'bwrw'
                ">
                <div class="point"></div>
                <div class="text">{{ taskInfo.taskLevel }}</div>
              </div>
            </div>
            <div class="item_box">
              <div class="left">开始时间：</div>
              <div class="right">{{ taskInfo.taskStartTime }}</div>
            </div>
            <div class="item_box">
              <div class="left">结束时间：</div>
              <div class="right">{{ taskInfo.taskEndTime }}</div>
            </div>
            <div class="task_newBox">
              <div class="task_gh" @click="taskPlan">任务规划</div>
              <div class="task_yzt" @click="taskPrc">任务一张图</div>
              <div class="task_my" @click="playTask">
                <el-icon style="margin-right: 4px">
                  <VideoPlay />
                </el-icon>任务漫游
              </div>
            </div>
          </div>
        </div>
        <div class="addTask" @click="add">
          <div class="left_text">日程安排</div>
          <div class="right_creat_btn">创建</div>
        </div>
        <div class="content">
          <div class="item_box">
            <div class="importantPoint" v-for="(item, index) in taskInfo.sceneList" :key="index">
              <div class="top_time">
                <div class="left"></div>
                <div class="right">
                  {{ item.beginTime.slice(-8) }}
                  <div class="line"></div>
                  {{ item.endTime.slice(-8) }}
                </div>
              </div>
              <div class="data_box">
                <div class="left"></div>
                <div class="right" @click.stop="clickScreen(item)" :class="item.select ? 'active' : ''">
                  <div class="type_box">
                    {{
                      item.type === "1"
                      ? "路线"
                      : item.type === "2"
                        ? "现场"
                        : "住地"
                    }}
                  </div>
                  <div class="task_name">{{ item.sceneName }}</div>
                  <div class="task_name1">{{ item.sceneDesc }}</div>
                  <div class="bottom_btn">
                    <div class="edit_btn" @click.stop="editScreenPlan(item)">
                      编辑
                    </div>
                    <div class="action_btn" @click.stop="tourView(item)" style="margin-left: 8px">
                      漫游预览
                    </div>
                    <div class="action_btn" @click.stop="tourEdit(item)" style="margin: 0 8px">
                      漫游编辑
                    </div>
                    <div class="action_btn" @click.stop="deleteGuide(item)">
                      删除<el-icon>
                        <Delete />
                      </el-icon>
                    </div>
                    <div class="action_btn" @click.stop="uploadLines(item)" v-if="item.type === '1'"
                      style="margin-left: 8px">
                      导入<el-icon>
                        <Upload />
                      </el-icon>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>

  <addTabs v-if="addData" @out="getOut" />

  <!-- 编辑弹框 -->
  <el-dialog v-model="centerDialogVisible" title="日程编辑" width="400" center :close-on-click-modal="false"
    :close-on-press-escape="false" :show-close="false" class="edit_dialog">
    <div class="tabs">
      <div class="tabs_item" :class="{ active: selectItem === '1' }" @click="editChange('1')">
        路线
      </div>
      <div class="tabs_item" :class="{ active: selectItem === '2' }" @click="editChange('2')">
        现场
      </div>
      <div class="tabs_item" :class="{ active: selectItem === '3' }" @click="editChange('3')">
        住地
      </div>
    </div>
    <el-form ref="drawForm" :inline="true" :model="editForm" :label-suffix="'：'" :label-width="100" size="large"
      class="customForm">
      <el-row>
        <el-col :span="24" v-if="selectItem === '1'">
          <el-form-item label="类型" style="width: 100%">
            <el-radio-group v-model="radio" @change="getGSGT">
              <el-radio :value="0">道路</el-radio>
              <el-radio :value="2">高铁</el-radio>
              <el-radio :value="1">高速</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <div v-if="radio === 0">
            <el-form-item label="路线起点" prop="startPointId" :rules="[{ required: true, message: '必填', trigger: 'change' }]"
              style="width: 100%">
              <el-cascader :options="options" ref="cascaderRef" :show-all-levels="false" placeholder="请选择起点"
                style="width: 100%;" v-model="startPointList" clearable @change="getStartEnd" />
            </el-form-item>
            <el-form-item label="路线终点" prop="endPointId" :rules="[{ required: true, message: '必填', trigger: 'change' }]"
              style="width: 100%">
              <el-cascader :options="options" ref="cascaderRef1" :show-all-levels="false" placeholder="请选择终点"
                style="width: 100%;" v-model="endPointList" clearable @change="getStartEndOther" />
            </el-form-item>
            <el-form-item label="旧名称" style="width: 100%" v-if="!roadSelect&&oldType==='1'">
              <el-input v-model="oldSceneName" placeholder="请输入..." readonly></el-input>
            </el-form-item>
          </div>
          <el-form-item label="名称" prop="sceneName" :rules="[{ required: true, message: '必填', trigger: 'change' }]"
            style="width: 100%" v-else>
            <!-- <el-input
              v-model="editForm.sceneName"
              placeholder="请输入..."
              v-if="radio === 0"
            ></el-input> -->
            <el-select v-model="editForm.sceneName" value-key="id" @change="getSelectData" placeholder="请选择">
              <el-option v-for="item in selectData" :key="item.id" :label="item.name" :value="item" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="常备方案" style="width: 100%">
            <el-select v-model="editForm.schemeId" placeholder="请选择" clearable :disabled="selectBol"
              @change="changeSelect">
              <el-option v-for="(item, index) in planData" :key="index" :label="item.sceneName" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="场景复用" style="width: 100%">
            <el-cascader :disabled="treeBol" :show-all-levels="false"
              :props="{ value: 'id', label: 'name', children: 'children' }" :options="optionsList" clearable
              v-model="selectedValue" placeholder="请选择" style="width: 100%" @change="changeTree" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="开始时间" prop="beginTime" :rules="[{ required: true, message: '必填', trigger: 'change' }]"
            style="width: 100%">
            <el-date-picker v-model="editForm.beginTime" type="datetime" placeholder="开始时间"
              value-format="YYYY-MM-DD HH:mm:ss" :editable="false" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="结束时间" prop="endTime" :rules="[{ required: true, message: '必填', trigger: 'change' }]"
            style="width: 100%">
            <el-date-picker v-model="editForm.endTime" type="datetime" placeholder="结束时间"
              value-format="YYYY-MM-DD HH:mm:ss" :editable="false" style="width: 100%" />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="日程备注" style="width: 100%" prop="sceneDesc"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]">
            <el-input v-model="editForm.sceneDesc" placeholder="请输入..." type="textarea" :rows="4">
            </el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <div class="btn_box cancle_btn" @click="cancelDraw">取消</div>
        <div class="btn_box" @click="sureDraw">确定</div>
      </div>
    </template>
  </el-dialog>

  <!-- 绘制 -->
  <Draw v-if="drawBol" @out="outDraw" />

  <!-- 任务规划 -->
  <TaskPlan v-if="taskPlanModal" />

  <!-- 一级加强场景规划 -->

  <!-- <ScreenPlan v-if="screenPlanModal" /> -->

  <!-- 其他任务场景详情 -->
  <ScreenOtherPlan v-if="sceneBol" @closeModal="loadScenePlace" />

  <!-- 导览列表 -->
  <cameraTourTmpl v-if="showTour" @closeTour="loadScenePlace" />

  <!-- 画路线 -->
  <drwLine v-if="drawline" @close="closeCMPT" />

  <!-- 资源列表 -->
  <ResourceList v-if="resourceBol" @close="closeResource" />

  <!-- 矢量路网数据 -->
  <VectorRoadMap :styles="style" v-if="resourceBol" />
  <!-- 选择漫游视角弹框 -->
  <el-dialog v-model="tourBol" title="请选择漫游编辑视角" width="400" center :close-on-click-modal="false"
    :close-on-press-escape="false" class="edit_dialog" @close="tourItem = {}">
    <template #footer>
      <div class="dialog-footer">
        <div class="btn_box cancle_btn" @click="selectTour('第一视角')">
          第一视角
        </div>
        <div class="btn_box cancle_btn" @click="selectTour('第三视角')">
          第三视角
        </div>
      </div>
    </template>
  </el-dialog>
  <!-- 漫游预览视角切换按钮 -->

  <div class="tourView_box" v-if="showMYbtn">
    <div class="btn_view" @click.stop="changeMY(1)" :class="{ active: selectMy === 1 }">
      第一视角
      <div class="pleusBtn" v-if="selectMy === 1 && showPlayBtn" @click.stop="playPauseResume(btnText)">
        {{ btnText }}
      </div>
    </div>
    <div class="btn_view" @click.stop="changeMY(3)" :class="{ active: selectMy === 3 }">
      第三视角
      <div class="pleusBtn" v-if="selectMy === 3 && showPlayBtn" @click.stop="playPauseResume(btnText)">
        {{ btnText }}
      </div>
    </div>
    <el-icon @click="outView">
      <Close />
    </el-icon>
  </div>

  <!-- 预览PDF -->
  <PdfView v-if="showPdf" :pdfObj="pdfData" @close="delClose" />

  <!-- 路线导入 -->
  <!-- <el-upload
    ref="uploadRef"
    :auto-upload="false"
    :show-file-list="false"
    :accept="'.txt'"
    :on-change="handleFileChange"
    style="display: none"
  >
  </el-upload> -->
  <el-upload ref="uploadRef" :auto-upload="false" style="display: none"></el-upload>
  <input type="file" ref="realInput" @change="handleRealChange" style="display: none" accept=".txt" />
</template>

<script setup>
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  reactive,
  onBeforeMount,
  getCurrentInstance,
  watch,
  nextTick,
} from "vue";
import VectorRoadMap from "@/components/vectorRoadMap"
import { Edit } from "@element-plus/icons-vue";
import addTabs from "./addScreen.vue";
import cameraTourTmpl from "./CameraTour.vue";
import drwLine from "../../SysTools/resourceCatalog/plot/DrawLineNew.vue";
import CameraTourBIZ from "../setCameraTour/cameraTour";
import { ElMessage, ElMessageBox } from "element-plus";
import useTaskStore from "@/store/modules/taskStore";
import Draw from "./editDraw.vue";
import TaskPlan from "./taskPlan.vue";
import ScreenPlan from "./screenPlan.vue";
import ScreenOtherPlan from "./screenOtherPlan.vue";
import ResourceList from "./ResourceList.vue";
import useCameraStore from "@/store/modules/cameraSet";
import useSettingStore from "@/store/modules/settingStore";
import useScreenStore from "@/store/modules/screenStore";
import useFloorStore from "@/store/modules/floorStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import PdfUpload from "@/components/pdfUpload";
import PdfView from "@/components/pdfView";
import { Plus } from "@element-plus/icons-vue";
import emitter from "@/utils/emitter";
import {
  queryTaskInfo,
  deleteScreenNew,
  searchScreenForId,
  updateScreenNew,
} from "@/api/task/new";
import { quertListForTypeId } from "@/api/basic/index";
import { getPlanListApi } from "@/api/plan";
import { drawLinesName } from "@/views/WorkCockpit/utils";
import {
  getDrawDataForScreen,
  getScreenPosition,
  updateScreen,
  searchNodePlanToScreen,
  getDrawDataForTask,
  getMarkersForFloor,
  getReourceDataForScene,
  getTourDataForScreen,
  getSceneTreeData,
  getLineListData,
  sceneLineImport,
  importLineSave,
} from "@/api/task/task";
import {
  openFloors,
  closeFloors,
  addTourResouce,
} from "@/components/SmartMap/js/utils";
import {
  drawResourceDataAll,
  drawTaskScenePointAndLine,
} from "@/components/SmartMap/js/resource";
import { addScenePlacePoint, drawRoadLineMarker } from "@/components/SmartMap/js/addMarkers";
import { setAutoCameraTour } from "@/components/SmartMap/js/addPlot";
import { getSceneDataForIdAndToDtaw } from "@/api/workCockpit/index.js";
import { getDrawDataOfTask } from "@/api/workCockpit/index.js";
import { updateTask } from "@/api/task/index";
import { moveDrawAction, clearDraw, drawScreenData } from "./util";
import { cloneDeep } from "lodash-es";
import { sessionStorage } from "@/utils/storage";
import { flattenTreeData, loadPicture } from "@/utils";
import { useRouter } from "vue-router";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
const WorkCockpitStore = useWorkCockpitStore();
const router = useRouter();
const { proxy } = getCurrentInstance();
const cameraStore = useCameraStore();
const SettingStore = useSettingStore();
const style = ref({
  bottom: '100px',
  right: '340px'
})
window._cameraTour = new CameraTourBIZ();
let playtimer = ref(null);
const addData = ref(false);
const centerDialogVisible = ref(false);
const drawline = ref(false);
const editForm = ref({});
const editId = ref("");
const planTaskId = ref("");
const copySceneId = ref("");
const selectData = ref([]);
const drawBol = ref(false);
const btnText = ref("暂停");
const buildList = ref([]); // 楼层楼宇数据
const taskPlanModal = computed(() => useTaskStore().taskPlan);
const TaskManageList = computed(() => WorkCockpitStore.taskManagement)
const screenPlanModal = computed(() => useTaskStore().screenPlan);
const sceneBol = computed(() => useTaskStore().sceneBol);
const resourceBol = computed(() => useScreenStore().resourceBol);
const showTour = computed(() => useTaskStore().isShowFrames);
let tourList = computed(() => useTaskStore().keyFrames);
let taskId = computed(() => useTaskStore().taskInfo.id);
// 详情信息数据
let taskInfo = computed(() => useTaskStore().taskInfo);
// 编辑的标绘数据
let drawEdits = computed(() => useTaskStore().drawEdit);
let isShowList = computed(() => cameraStore.isShowList);
let layerData = computed(() => SettingStore.layerList); // 所有可见图层
let openFloor = computed(() => useFloorStore().openFloor);
let floorMarkers = computed(() => useFloorStore().floorMarkers);
// 漫游编辑视角
const mapEvent = computed(() => useSysToolsCimStore().eventSealAPI);
const tourBol = ref(false);
const tourItem = ref({});
const importData = ref({});
const selectMy = ref(1);
const showPlayBtn = ref(true);
const showMYbtn = ref(false);
const showPdf = ref(false);
const pdfData = ref({
  src: "http://localhost:5173/imgPath/2025-03-26/20250326192427215373fc4be84564acbb2351b14b4368.pdf",
  name: "测试pdf名称",
  id: "666",
});
// 编辑操作
const selectItem = ref("");
const selectData1 = ref([]);
const planData = ref([]);
const optionsList = ref([]);
const selectedValue = ref([]); // 绑定的值
const selectBol = ref(false);
const treeBol = ref(false);
const planType = ref("1");
const radio = ref(0);
const options = ref([
  {
    value: 'xc',
    label: '现场',
    children: [],
  },
  {
    value: 'zd',
    label: '住地',
    children: [],
  }
])
const roadSelect = ref(false)
const startName = ref('')
const endName = ref('')
const startPointList = ref([])
const endPointList = ref([])
const cascaderRef = ref(null)
const cascaderRef1 = ref(null)
const startId = ref('')
const endId = ref('')
const oldType = ref('')// 原始类型
const oldSceneName = ref('')// 旧名称
// 编辑操作end
onMounted(() => {
  cameraStore.setisShowListShow(true);
  useTaskStore().SET_TASKPLAN(false);
  useTaskStore().SET_SCREENPLAN(false);
  // 清除场景数据
  clearDraw().then(() => {
    getDrawDataOfTask({ taskId: taskInfo.value.id }).then((res) => {
      WorkCockpitStore.setTaskSceneData(res.data);
      WorkCockpitStore.setRoadDrawList(res.data);
      drawTaskScenePointAndLine(res.data, true);
      addScenePlacePoint(res.data);
    });
  });
  buildList.value = [];
  useScreenStore().set_resourceBol(false);
  useTaskStore().set_sceneBol(false);
  emitter.on("ToTaskDetails", (data) => {
    useScreenStore().set_resourceBol(false);
    nextTick(() => {
      let foundItem = taskInfo.value.sceneList.find(
        (item) => item.id === data.GroupID
      );
      // clickScreen(foundItem);
      onlyOneClick(foundItem)
    });
  });
});

const onlyOneClick = async (item) => {
  WorkCockpitStore.set_showResouce(false);
  WorkCockpitStore.set_checkBox("init", false);
  if (item.viewData) {
    let g = window.__g;
    g.camera.set(item.viewData.camera, 2);
  }
  WorkCockpitStore.set_threePageType("");
  WorkCockpitStore.set_checkBox("cordon", true);
  statusSetting();
  useFloorStore().set_isShowFloor(true);
  useTaskStore().SET_KEYFRAMES(item.keyFrames); // 设置漫游数据
  //  显示资源列表
  useScreenStore().set_resourceBol(true);
  getData(item.id);
  // 设置场景规划面板类型
  useTaskStore().SET_SCREENPLANTITLE(
    item.type === "1" ? "路线" : item.type === "2" ? "现场" : "住地"
  );
  // 设之场景面板弹框所需信息
  useTaskStore().SET_SCREENMODALINFO(item);
  // 设置工具栏显示
  SettingStore.setShowTool(true);
  // 设置场景信息
  useScreenStore().set_screenInfo(item);
  // 设置场景能否编辑操作
  useScreenStore().set_editScreen(true);
  // 设置任务规划面板隐藏
  useTaskStore().SET_TASKPLAN(false);
  cameraStore.setisShowListShow(false);

  setTimeout(() => {
    drawTaskScenePointAndLine(WorkCockpitStore.taskSceneData, true).then(() => {
      if (TaskManageList.value?.length) {
        let list = []
        for (const option of TaskManageList.value) {
          if (option.name === item.sceneName) {
            list.push(option.id)
          }
        }
        if (list.length > 1) {
          let ids = []
          for (const idArr of list) {
            if (idArr != item.id) {
              ids.push(idArr)
            }
          }
          window.__g.marker.hide(ids)
        }
      }
    });
    addScenePlacePoint(WorkCockpitStore.taskSceneData);
  }, 100);
}
// 导入路线
const uploadLines = (item) => {
  console.log(item);
  ElMessageBox.confirm(`是否导入路线，如果此前有路线会覆盖掉`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      importData.value.id = item.id;
      importData.value.taskId = item.taskId;
      // proxy.$refs.uploadRef.$el.querySelector("input").click(); // 触发文件选择
      proxy.$refs.realInput.click(); // 触发原生input
    })
    .catch(() => {
      importData.value = {};
    });
};
const handleRealChange = (e) => {
  const file = e.target.files[0];
  proxy.$refs.uploadRef.handleStart(file); // 手动注入文件到upload组件
  handleFileChange(file); // 触发原on-change逻辑
};
// 文件选择后处理
const handleFileChange = (file) => {
  console.log(file)
  if (!file) return;
  if (!file.name.endsWith(".txt")) {
    ElMessage.error("仅支持TXT格式文件");
    return false;
  }
  const formData = new FormData();
  formData.append("file", file);
  // 调用上传接口
  sceneLineImport(formData).then((res) => {
    let name = res.data.fileName;
    let coor = JSON.parse(res.data.coordinates);
    let jwd = coor.map(subArr => {
      return [subArr[0], subArr[1], subArr[2] + 1]
    })
    importLines(name, jwd);
    // 强制重置input的value值（关键代码）
    const input = proxy.$refs.realInput
    input.value = null;
  });
};
const importLines = (name, jwd) => {
  let length = Math.floor(calculate3DLength(jwd));
  let tiling = Math.round(Number(length) / 20);
  let id = "drline_" + new Date().getTime();
  // 找到路线中间数据
  const length1 = jwd.length;
  const middleIndex = Math.floor((length1 - 1) / 2);
  let zhongjianNum = jwd[middleIndex]; // 返回中间的元素
  console.log(zhongjianNum);
  let obj = {
    userData: [
      zhongjianNum[0],
      zhongjianNum[1],
      500,
      -55.421288, -116.548866, 0,
    ],
    color: "#0000ff",
    shape: 0,
    customStyle: 0,
    thickness: 10,
    tiling: tiling,
    utype: "polyline",
    coordinates: jwd,
    depthTest: true,
    isChecked: true,
    cusAttr: [],
    brightness: 0.3,
    name: name,
    flowRate: 0.3,
    style: 0,
    id: id,
    opacity: 1,
    info: {},
  };
  console.log(obj);
  let query = {
    taskId: importData.value.taskId,
    sceneId: importData.value.id,
    planNode: "3",
    type: "lines",
    policeType: "3",
    data: obj,
  };
  importLineSave(query).then((res) => {
    ElMessage.success("导入成功！");
    refreshData();
  });
};
// 三维空间距离计算（包含Z轴）
const calculate3DLength = (points) => {
  let total = 0;
  for (let i = 0; i < points.length - 1; i++) {
    const [x1, y1, z1] = points[i];
    const [x2, y2, z2] = points[i + 1];
    total += Math.hypot(x2 - x1, y2 - y1, z2 - z1);
  }
  return total;
};
const loadScenePlace = () => {
  if (taskInfo.value.viewData) {
    let g = window.__g;
    g.camera.set(taskInfo.value.viewData.camera, 2);
  }
  getDrawDataOfTask({ taskId: taskInfo.value.id }).then((res) => {
    WorkCockpitStore.setTaskSceneData(res.data);
    WorkCockpitStore.setRoadDrawList(res.data);
    drawTaskScenePointAndLine(res.data, true);
    addScenePlacePoint(res.data);
  });
};
watch(
  () => mapEvent.value,
  (nV, oV) => {
    if (nV) {
      if (nV.eventtype === "CameraTourFinished") {
        // 漫游播放完成
        showPlayBtn.value = false;
      }
    }
  },
  { deep: true, immediate: false }
);
// pdf上传预览
// 刷新数据
const refreshData = () => {
  queryTaskInfo({ id: taskId.value }).then((res) => {
    if (res.code === 0) {
      useTaskStore().SET_TASKINFO(res.data);
    }
  });
};
const delClose = (e) => {
  showPdf.value = false;
  if (e === 0) {
    refreshData();
  }
};
// pdf 查看
const pdfCheck = (url, name, id) => {
  pdfData.value.src = "/imgPath" + url;
  pdfData.value.name = name;
  pdfData.value.id = id;
  nextTick(() => {
    showPdf.value = true;
  });
};

// 保存任务视角
const getCood = async (taskId) => {
  let g = window.__g;
  let cood = await g.camera.get();
  console.log(cood);
  let obj = {
    viewData: cood,
    id: taskId,
  };
  updateTask(obj).then((res) => {
    if (res.code === 0) {
      ElMessage.success("任务视角已保存");
      queryTaskInfo({ id: taskId }).then((res) => {
        useTaskStore().SET_TASKINFO(res.data);
      });
    }
  });
};
// 任务规划
const taskPlan = () => {
  statusSetting();
  useTaskStore().SET_TASKPLAN(true);
};
// 任务一张图
const taskPrc = () => {
  getDrawDataOfTask({ taskId: taskInfo.value.id }).then((res) => {
    WorkCockpitStore.setTaskSceneData(res.data);
    WorkCockpitStore.setRoadDrawList(res.data);
    statusSetting();
    WorkCockpitStore.set_showResouce(true);
    useTaskStore().SET_clearModal(false);
    useScreenStore().set_showPicture(true);
    useScreenStore().set_pictureId(taskInfo.value.id);
    useScreenStore().set_pictureName(taskInfo.value.taskName);
    useScreenStore().set_pictureType("task");
    useSettingStore().setShowTool(false);
    console.log(taskInfo.value);
    if (taskInfo.value.viewData) {
      let g = window.__g;
      g.camera.set(taskInfo.value.viewData.camera, 2);
    }
    // WorkCockpitStore.setCurPage('taskDetail');
    WorkCockpitStore.setTaskItem(taskInfo.value);
  });
};
// 应急预案
const toEmergencyPlan = () => {
  statusSetting();
  useTaskStore().SET_clearModal(false);
  useEmergencyStore().set_showEmergencyPlan(true);
};

// 任务漫游
const playTask = () => {
  searchScreenForId({ taskId: taskInfo.value.id }).then((res) => {
    if (res.code === 0) {
      if (res.data.length > 0) {
        useTaskStore().SET_TASKVIDEOLIST(res.data);
        getDrawDataForScreen({ sceneId: res.data[0].id }).then((res1) => {
          useTaskStore().set_taskDrawData(res1.data);
          drawToursForReasuce(res.data[0].id);
          WorkCockpitStore.setCurTourId("");
          useTaskStore().SET_clearModal(false);
          SettingStore.set_isClickTools(true);
          useScreenStore().set_showPlay(true);
        });
      } else {
        proxy.$modal.msgWarning("暂无推演数据");
      }
    }
  });
};

// 返回
const gobackTo = () => {
  let g = window.__g
  g.cameraTour.stop();// 停止漫游
  g.reset(4);
  clearDraw(); // 清除场景数据
  closeFloors(); // 清除楼层炸开数据
  cameraStore.setShowTaskInfo(false);
  SettingStore.setShowTaskPanle(true);
  SettingStore.setshowTaskFun(true);
  // SettingStore.setShowTool(true);
  useTaskStore().SET_TASKPLAN(false);
  useTaskStore().SET_SCREENPLAN(false);
  useScreenStore().set_editScreen(false);
  useTaskStore().set_sceneBol(false);
  // useTaskStore().set_communityBol(false);
  useScreenStore().set_resourceBol(false);
  useFloorStore().set_isShowFloor(false);
};

const getOut = (e) => {
  addData.value = false;
  getDrawDataOfTask({ taskId: taskInfo.value.id }).then((res) => {
      WorkCockpitStore.setTaskSceneData(res.data);
      WorkCockpitStore.setRoadDrawList(res.data);
      drawTaskScenePointAndLine(res.data, true);
      addScenePlacePoint(res.data);
    });
};

// 场景数据编辑
const sureDraw = async () => {
  proxy.$refs["drawForm"].validate((valid) => {
    if (valid) {
      if (selectedValue.value?.length) {
        editForm.value.sourceSceneId =
          selectedValue.value[selectedValue.value.length - 1];
      } else {
        editForm.value.sourceSceneId = "";
      }
      if (editForm.value.type === '1' && radio.value === 0 && roadSelect.value) {
        editForm.value.sceneName = `${startName.value}至${endName.value}`
      }
      if(!roadSelect.value&&oldType.value==='1'){
        editForm.value.sceneName = `${startName.value}至${endName.value}`
      }
      if (
        (editForm.value.basicDataId !== editId.value ||
        editForm.value.schemeId !== planTaskId.value ||
        editForm.value.sourceSceneId !== copySceneId.value
        ||editForm.value.startPointId !== startId.value ||
        editForm.value.endPointId !== endId.value)&& roadSelect.value
      ) {
        ElMessageBox.confirm(
          "修改场景名会把原先场景的数据删除,确定要修改?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "Warning",
          }
        ).then(() => {
          let obj = {
          taskId: editForm.value.taskId,// 任务id
          schemeId: editForm.value.schemeId, // 常备方案id
          sceneName: editForm.value.sceneName, // 场景名称
          basicDataId:editForm.value.basicDataId,// 挂载的现场和住地id
          type: editForm.value.type,// 类型
          beginTime: editForm.value.beginTime,// 开始时间
          endTime: editForm.value.endTime, // 结束时间
          sceneDesc: editForm.value.sceneDesc, //场景描述
          sourceSceneId:editForm.value.sourceSceneId,// 场景复用id
          startPointId:editForm.value.startPointId,// 路线起点
          endPointId:editForm.value.endPointId,// 路线终点
          startType:editForm.value.startType,//路线起点类型
          endType:editForm.value.endType,// 路线终点类型
          id:editForm.value.id
        }
          updateScreen(obj).then((res) => {
            if (res.code === 0) {
              ElMessage.success("编辑成功！");
              queryTaskInfo({ id: taskId.value }).then((res) => {
                useTaskStore().SET_TASKINFO(res.data);
                cancelDraw();
                clearDraw().then(() => {
                  loadScenePlace();
                });
              });
            }
          });
        });
      } else {
        let obj = {
          taskId: editForm.value.taskId,// 任务id
          schemeId: editForm.value.schemeId, // 常备方案id
          sceneName: editForm.value.sceneName, // 场景名称
          basicDataId:editForm.value.basicDataId,// 挂载的现场和住地id
          type: editForm.value.type,// 类型
          beginTime: editForm.value.beginTime,// 开始时间
          endTime: editForm.value.endTime, // 结束时间
          sceneDesc: editForm.value.sceneDesc, //场景描述
          sourceSceneId:editForm.value.sourceSceneId,// 场景复用id
          startPointId:editForm.value.startPointId,// 路线起点
          endPointId:editForm.value.endPointId,// 路线终点
          startType:editForm.value.startType,//路线起点类型
          endType:editForm.value.endType,// 路线终点类型
          id:editForm.value.id
        }
        updateScreen(obj).then((res) => {
          if (res.code === 0) {
            ElMessage.success("编辑成功！");
            queryTaskInfo({ id: taskId.value }).then((res) => {
              useTaskStore().SET_TASKINFO(res.data);
              cancelDraw();
              clearDraw();
            });
          }
        });
      }
      // } else {
      //   updateScreen(editForm.value).then((res) => {
      //     if (res.code === 0) {
      //       ElMessage.success("编辑成功！");
      //       queryTaskInfo({ id: taskId.value }).then((res) => {
      //         useTaskStore().SET_TASKINFO(res.data);
      //         cancelDraw();
      //         clearDraw();
      //       });
      //     }
      //   });
      // }
    }
  });
};
//  修改路线
const closeCMPT = (obj) => {
  if (obj) {
    editForm.value.roadData = obj;
  }
  drawline.value = false;
  centerDialogVisible.value = true;
};
// 绘制路线
const addLine = () => {
  centerDialogVisible.value = false;
  drawline.value = true;
};
const cancelDraw = () => {
  resetForm("drawForm");
  centerDialogVisible.value = false;
};
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
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
      coordinateType: item.geojson.coordinates[2] ? 0 : 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 68],
      range: [0, 1500000], //可视范围
      imageSize: [40, 68], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(
        `./imgs/${item.name === "白洋淀站" || item.name === "雄安站"
          ? "gt"
          : item.type === "3" || item.type === "住地基本情况"
            ? "hotel"
            : "jz"
        }.png`
      ),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 1500], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [2, 0], // 文本偏移
      // textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
      // textBackgroundColor: item.type==='现场基本情况'?Color.Yellow:Color.Crimson, //文本背景颜色
      textBackgroundColor: "#2a4cac", //文本背景颜色
      fontSize: 12, //字体大小
      // fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      // fontOutlineColor: Color.Black, //字体轮廓线颜色
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
//场景选中
const clickScreen = async (item) => {
  console.log(item, '路线数据')
  window._cameraTour.stop_biz();
  showMYbtn.value = false;
  WorkCockpitStore.set_showResouce(false);
  WorkCockpitStore.set_checkBox("init", false);
  if (item.viewData) {
    let g = window.__g;
    g.camera.set(item.viewData.camera, 2);
  }
  if (item.type === "1") {
    WorkCockpitStore.set_threePageType("roadLine");
    WorkCockpitStore.set_checkBox("line", true);
    WorkCockpitStore.set_checkBox("cordon", false);
  } else {
    WorkCockpitStore.set_threePageType("");
    WorkCockpitStore.set_checkBox("cordon", true);
  }
  statusSetting();
  useFloorStore().set_isShowFloor(true);
  useTaskStore().SET_KEYFRAMES(item.keyFrames); // 设置漫游数据
  //  显示资源列表
  useScreenStore().set_resourceBol(true);
  // 绘制标绘数据
  getDrawDataForScreen({ sceneId: item.id }).then((res) => {
    useFloorStore().set_floorMarkers([]);
    closeFloors(); // 清除楼层炸开数据
  });
  getData(item.id);
  // 设置场景规划面板类型
  useTaskStore().SET_SCREENPLANTITLE(
    item.type === "1" ? "路线" : item.type === "2" ? "现场" : "住地"
  );
  // 设之场景面板弹框所需信息
  useTaskStore().SET_SCREENMODALINFO(item);
  // 设置工具栏显示
  SettingStore.setShowTool(true);
  // 设置场景信息
  useScreenStore().set_screenInfo(item);
  // 设置场景能否编辑操作
  useScreenStore().set_editScreen(true);
  // 设置任务规划面板隐藏
  useTaskStore().SET_TASKPLAN(false);
  cameraStore.setisShowListShow(false);

  setTimeout(() => {
    console.log(WorkCockpitStore.taskSceneData, TaskManageList.value)
    // 当前任务下的现场和住地

    drawTaskScenePointAndLine(WorkCockpitStore.taskSceneData, true).then(() => {
      if (TaskManageList.value?.length) {
        let list = []
        for (const option of TaskManageList.value) {
          if (option.name === item.sceneName) {
            list.push(option.id)
          }
        }
        if (list.length > 1) {
          let ids = []
          for (const idArr of list) {
            if (idArr != item.id) {
              ids.push(idArr)
            }
          }
          window.__g.marker.hide(ids)
        }
      }
    });
    if (item.type === "1" && item.startData && item.startPointId) {
      // let datas = WorkCockpitStore.taskSceneData.filter(item=>item.type === "2" || item.type === "3")
      // console.log(datas)
      let newDatas = [{
        id: item.startPointId,
        sceneName: item.startData.title,
        coor: [Number(item.startData.jingdu), Number(item.startData.weidu)],
        type: item.startType === 'xc' ? '2' : '3'
      }, {
        id: item.endPointId,
        sceneName: item.endData.title,
        coor: [Number(item.endData.jingdu), Number(item.endData.weidu)],
        type: item.endType === 'xc' ? '2' : '3'
      }]
      // 方法1：使用filter过滤
      const filteredArray = newDatas.filter(item2 =>
        !WorkCockpitStore.taskSceneData.some(item1 => item1.sceneName === item2.sceneName)
      )
      if (filteredArray?.length) {
        // 添加该条路线的起点和终点
        drawRoadLineMarker(filteredArray, item.viewData)
      }
      let allDatas = [...filteredArray, WorkCockpitStore.taskSceneData]
      addScenePlacePoint(allDatas);
    } else {
      addScenePlacePoint(WorkCockpitStore.taskSceneData);
    }
  }, 500);
};

const getData = (id) => {
  let params = { sceneId: id, planNode: "警力部署" };
  searchNodePlanToScreen(params).then((res) => {
    // 场景详情面板
    if (res.code === 0) {
      useTaskStore().set_sceneBol(true);
      if (res.data && res.data.length > 0) {
        res.data.map((item) => {
          item.data = item.data ? item.data : { zrld: "", phone: "", time: "" };
        });
      }
      useScreenStore().set_sceneCardInfo(res.data);
    }
  });
};
// 场景编辑
const editScreenPlan = async (item) => {
  console.log(item);
  oldType.value = item.type
  oldSceneName.value = item.sceneName
  if (item.type === "1" && !item.basicDataId) {
    radio.value = 0;
    // 获取现场住地列表
    quertListForTypeId('xc').then(res => {
      let datas = res.data.map(item => {
        return { value: item.id, label: item.data.title }
      })
      options.value[0].children = datas
    })
    quertListForTypeId('zd').then(res => {
      let datas = res.data.map(item => {
        return { value: item.id, label: item.data.title }
      })
      options.value[1].children = datas
    })
    if (item.startData && item.startPointId && item.startType) {

      startPointList.value = [item.startType, item.startPointId]
      endPointList.value = [item.endType, item.endPointId]
      roadSelect.value = true
      startId.value = item.startPointId
      endId.value = item.endPointId
      startName.value = item.startData.title
      endName.value = item.endData.title
    } else {
      startPointList.value = []
      endPointList.value = []
      roadSelect.value = false
    }
  }

  editId.value = item.basicDataId;
  planTaskId.value = item.schemeId;
  copySceneId.value = item.sourceSceneId;
  selectItem.value = item.type;
  if (item.type === "1" && !item.basicDataId) {
    getPlanListApi({ type: "1" }).then((res) => {
      planData.value = res.data;
    });
    // 初始画获取道路场景数据
    getSceneTreeData({ type: "1" }).then((res) => {
      optionsList.value = res.data;
    });
  }
  if (item.type === "1" && item.basicDataId) {
    if (item.sceneName.includes("高速")) {
      radio.value = 1;
      let res1 = await getScreenPosition({ type: "1" });
      let data1 = res1.data.map((item) => {
        return { ...item, type: "1" };
      });
      selectData.value = data1;
      getPlanListApi({ type: "4", basicDataId: item.basicDataId }).then(
        (res) => {
          planData.value = res.data;
        }
      );
      getSceneTreeData({ type: "1", name: item.sceneName }).then((res) => {
        optionsList.value = res.data;
      });
    }
    if (item.sceneName.includes("高铁")) {
      radio.value = 2;
      let res1 = await getScreenPosition({ type: "2" });
      let data1 = res1.data.map((item) => {
        return { ...item, type: "1" };
      });
      selectData.value = data1;
      getPlanListApi({ type: "5", basicDataId: item.basicDataId }).then(
        (res) => {
          planData.value = res.data;
        }
      );
      getSceneTreeData({ type: "1", name: item.sceneName }).then((res) => {
        optionsList.value = res.data;
      });
    }
  }
  if (item.type === "2") {
    radio.value = 3
    let res1 = await getScreenPosition({ type: "3" });
    let data1 = res1.data.map((item) => {
      return { ...item, type: "2" };
    });
    selectData.value = data1;
    getPlanListApi({ type: "2", basicDataId: item.basicDataId }).then((res) => {
      planData.value = res.data;
    });
    getSceneTreeData({ type: "2", name: item.sceneName }).then((res) => {
      optionsList.value = res.data;
    });
  }
  if (item.type === "3") {
    radio.value = 4
    let res1 = await getScreenPosition({ type: "4" });
    let data1 = res1.data.map((item) => {
      return { ...item, type: "3" };
    });
    selectData.value = data1;
    getPlanListApi({ type: "3", basicDataId: item.basicDataId }).then((res) => {
      planData.value = res.data;
    });
    getSceneTreeData({ type: "3", name: item.sceneName }).then((res) => {
      optionsList.value = res.data;
    });
  }
  editForm.value = cloneDeep(item);
  centerDialogVisible.value = true;
};
const getStartEnd = (e) => {
  if (e) {
    const nodes = cascaderRef.value.getCheckedNodes()
    editForm.value.startPointId = e[1]
    editForm.value.startType = e[0]
    startName.value = nodes[0].label
  } else {
    editForm.value.startPointId = ''
    editForm.value.startType = ''
    startName.value = ''
  }
}
const getStartEndOther = (e) => {
  if (e) {
    const nodes = cascaderRef1.value.getCheckedNodes()
    editForm.value.endPointId = e[1]
    editForm.value.endType = e[0]
    endName.value = nodes[0].label
  } else {
    editForm.value.endPointId = ''
    editForm.value.endType = ''
    endName.value = ''
  }
}
const editChange = (type) => {
  editForm.value.type = type;
  if (type === "1") {
    radio.value = 0;

    getPlanListApi({ type: "1" }).then((res) => {
      planData.value = res.data;
    });
    getSceneTreeData({ type: "1" }).then((res) => {
      optionsList.value = res.data;
    });
  } else {
    getGSGT(Number(type) + 1);
    radio.value = 6;
    planData.value = [];
    optionsList.value = [];
  }
  treeBol.value = false;
  selectBol.value = false;
  editForm.value.sceneName = "";
  editForm.value.schemeId = "";
  editForm.value.basicDataId = "";
  selectItem.value = type;
  // if (proxy.$refs['drawForm']) {
  //   proxy.$refs['drawForm'].clearValidate();
  // }
};
const changeSelect = (e) => {
  if (e) {
    treeBol.value = true;
  } else {
    treeBol.value = false;
  }
};
const changeTree = (e) => {
  console.log(e);
  if (e) {
    selectBol.value = true;
  } else {
    selectBol.value = false;
  }
};
const getGSGT = (e) => {
  switch (e) {
    case 0:
      planType.value = "1";
      selectBol.value = false;
      treeBol.value = false;
      break;
    case 1:
      planType.value = "4";
      selectBol.value = false;
      treeBol.value = false;
      break;
    case 2:
      planType.value = "5";
      selectBol.value = false;
      treeBol.value = false;
      break;
    case 3:
      planType.value = "2";
      break;
    case 4:
      planType.value = "3";
      break;
  }
  if (e !== 0) {
    getScreenPosition({ type: e }).then((res) => {
      if (res.code === 0) {
        selectData.value = res.data;
      }
    });
    planData.value = [];
    optionsList.value = [];
    editForm.value.sceneName = "";
    editForm.value.schemeId = "";
    editForm.value.basicDataId = "";
  } else {
    getPlanListApi({ type: "1" }).then((res) => {
      planData.value = res.data;
    });
    getSceneTreeData({ type: "1" }).then((res) => {
      optionsList.value = res.data;
    });
    editForm.value.sceneName = "";
    editForm.value.schemeId = "";
    editForm.value.basicDataId = "";
  }
};
const getSelectData = (item) => {
  editForm.value.basicDataId = item.id;
  editForm.value.sceneName = item.name;
  // editForm.value.type = item.type;

  let params = {
    type: planType.value,
    basicDataId: item.id,
  };
  getPlanListApi(params).then((res) => {
    planData.value = res.data;
  });
  let params1 = {
    type: selectItem.value,
    name: item.name,
  };
  getSceneTreeData(params1).then((res) => {
    console.log(res);
    optionsList.value = res.data;
  });
};
// 漫游预览
const tourView = async (item) => {
  console.log(item);
  tourItem.value = item;
  useScreenStore().set_editScreen(false);
  useFloorStore().set_isShowFloor(true);
  useTaskStore().SET_TASKPLAN(false);
  useScreenStore().set_screenInfo(item);
  if (item.keyFrames || item.thirdKeyFrames) {
    if (item.keyFrames) {
      useTaskStore().SET_KEYFRAMES(item.keyFrames); // 设置第一视角漫游数据
    }
    if (item.thirdKeyFrames) {
      useTaskStore().SET_THIRDKEYFRAMES(item.thirdKeyFrames); // 设置第三视角漫游数据
    }
    // 绘制标绘数据
    showMarkersDrawData(item.taskId, item.id);
    drawToursForReasuce(item.id, true);
    // 第一视角存在默认播放第一视角
    if (item.keyFrames) {
      window._cameraTour.curOa = item.keyFrames[0];
      selectMy.value = 1;
      showPlayBtn.value = true;
      showMYbtn.value = true;
      btnText.value = "暂停";
    } else {
      // 否则默认选中第三视角
      window._cameraTour.curOa = item.thirdKeyFrames[0];
      selectMy.value = 3;
      showPlayBtn.value = true;
      showMYbtn.value = true;
      btnText.value = "暂停";
    }
    await window._cameraTour.play_biz();
    window.__g.cameraTour.setMouseClickToPause(
      window._cameraTour.curOa.id,
      false
    );
    playtimer.value = setTimeout(() => {
      playtimer.value = null;
    }, Number(window._cameraTour.curOa.time) * 1000);
  } else {
    // 路线
    if (item.type === "1") {
      madalShowToSetTourCustom(1, item.id, item.taskId);
    } else {
      ElMessage({
        message: "当前没有漫游数据，请先编辑添加",
        type: "warning",
      });
    }
  }
  // if (!item.keyFrames && !item.thirdKeyFrames) {
  //   return ElMessage({
  //     message: "当前没有漫游数据，请先编辑添加",
  //     type: "warning",
  //   });
  // }
};
// 漫游预览的暂停播放
const playPauseResume = (text) => {
  if (text === "暂停") {
    window.__g.cameraTour.pause();
    if (playtimer.value) clearTimeout(playtimer.value);
    playtimer.value = null;
    btnText.value = "播放";
  } else {
    window.__g.cameraTour.resume();
    btnText.value = "暂停";
  }
};
// 漫游预览切换视角
const changeMY = async (num) => {
  btnText.value = "暂停";
  if (num === 1) {
    if (tourItem.value.keyFrames) {
      showPlayBtn.value = true;
      window._cameraTour.curOa = tourItem.value.keyFrames[0];
      await window._cameraTour.play_biz();
      window.__g.cameraTour.setMouseClickToPause(
        window._cameraTour.curOa.id,
        false
      );
      playtimer.value = setTimeout(() => {
        playtimer.value = null;
      }, Number(window._cameraTour.curOa.time) * 1000);
      selectMy.value = num;
    } else {
      if (tourItem.value.type === "1") {
        changeNumToPlay(1, tourItem.value.id, tourItem.value.taskId);
      } else {
        ElMessage({
          message: "当前视角没有漫游数据",
          type: "warning",
        });
      }
    }
  } else {
    if (tourItem.value.thirdKeyFrames) {
      window._cameraTour.curOa = tourItem.value.thirdKeyFrames[0];
      showPlayBtn.value = true;
      await window._cameraTour.play_biz();
      window.__g.cameraTour.setMouseClickToPause(
        window._cameraTour.curOa.id,
        false
      );
      playtimer.value = setTimeout(() => {
        playtimer.value = null;
      }, Number(window._cameraTour.curOa.time) * 1000);
      selectMy.value = num;
    } else {
      if (tourItem.value.type === "1") {
        changeNumToPlay(3, tourItem.value.id, tourItem.value.taskId);
      } else {
        ElMessage({
          message: "当前视角没有漫游数据",
          type: "warning",
        });
      }
      // madalShowToSetTourCustom(3, tourItem.value.id, tourItem.value.taskId)
      // ElMessage({
      //   message: "当前视角没有漫游数据",
      //   type: "warning",
      // });
      // changeNumToPlay(3,tourItem.value.id,tourItem.value.taskId)
      // let msg = "第三视角";
      // let params = { sceneId: tourItem.value.id, type: "lines", lineType: "0" };
      // let lines = await getLineListData(params);
      //   let list = [];
      //   for (const item of lines.data) {
      //     list.push(item.data.coordinates);
      //   }
      //   let keyFramesObj = await setAutoCameraTour(list, 3);
      //   let obj = {
      //       id: tourItem.value.id,
      //       thirdTime: keyFramesObj.time,
      //       thirdKeyFrames: [keyFramesObj],
      //     };
      //   updateScreenNew(obj).then((res) => {
      //     if (res.code === 0) {
      //       proxy.$modal.msgSuccess(`已为您自动生成${msg}漫游`);
      //       queryTaskInfo({ id: tourItem.value.taskId }).then((res) => {
      //         cameraStore.setTaskInfo(res.data);
      //         useTaskStore().SET_TASKINFO(res.data);
      //         let news = taskInfo.value.sceneList.filter(item=>item.id===tourItem.value.id)
      //         tourItem.value = news[0]
      //         changeMY(3)
      //       });
      //     }
      //   });
    }
  }
};

const changeNumToPlay = async (type, id, taskId) => {
  let msg = type === 3 ? "第三视角" : "第一视角";
  let params = { sceneId: id, type: "lines", lineType: "0" };
  let lines = await getLineListData(params);
  if (lines.data?.length) {
    let list = [];
    for (const item of lines.data) {
      list.push(item.data.coordinates);
    }
    let keyFramesObj = await setAutoCameraTour(list, type);
    let obj;
    if (type === 1) {
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
        proxy.$modal.msgSuccess(`已为您自动生成${msg}漫游`);
        queryTaskInfo({ id: taskId }).then((res) => {
          cameraStore.setTaskInfo(res.data);
          useTaskStore().SET_TASKINFO(res.data);
          let news = taskInfo.value.sceneList.filter((item) => item.id === id);
          tourItem.value = news[0];
          changeMY(type);
        });
      }
    });
  } else {
    ElMessage.warning("当前场景没有路线，无法自动生成漫游");
  }
};
// 退出预览
const outView = () => {
  window._cameraTour.stop_biz();
  showMYbtn.value = false;
  closeFloors();
  hideFloorMarkers();
  useFloorStore().set_isShowFloor(false);
  clearDraw().then(() => {
    loadScenePlace();
  });
  // if (taskInfo.value && taskInfo.value.viewData) {
  //   let g = window.__g;
  //   g.camera.set(taskInfo.value.viewData.camera, 0.5);
  // }
};
//隐藏所有炸开数据
const hideFloorMarkers = () => {
  if (floorMarkers.value && floorMarkers.value.length > 0) {
    let g = window.__g;
    for (const item of floorMarkers.value) {
      g.marker.delete(item.id);
      g.marker3d.delete(item.id);
    }
  }
};
// 漫游编辑
const tourEdit = (item) => {
  if (showMYbtn.value) return ElMessage.warning("请先退出漫游预览");
  tourItem.value = item;
  useScreenStore().set_screenInfo(item);
  tourBol.value = true;
  queryTaskInfo({ id: item.taskId }).then((res) => {
    cameraStore.setTaskInfo(res.data);
    useTaskStore().SET_TASKINFO(res.data);
  });
  showMarkersDrawData(item.taskId, item.id);
  useFloorStore().set_isShowFloor(true);
  // getDrawDataForScreen({ sceneId: item.id }).then((res) => {
  //   drawScreenData(res.data);
  //   moveDrawAction(res.data);
  //   let g = window.__g;
  //   let layer = [];
  //   layerData.value.forEach((gis) => {
  //     if (item.sceneName.includes(gis.name)) {
  //       layer.push(gis);
  //     }
  //   });
  //   if (layer.length > 0) {
  //     g.infoTree.focus(layer[0].iD);
  //   } else {
  //   }
  // });
  drawToursForReasuce(item.id);
  // useScreenStore().set_resourceBol(true);
};

// 漫游编辑视角选择
const selectTour = (type) => {
  console.log(tourItem.value);
  let id = tourItem.value.id;
  let taskId = tourItem.value.taskId;
  if (type === "第一视角") {
    if (tourItem.value.keyFrames && tourItem.value.keyFrames.length > 0) {
      useTaskStore().SET_KEYFRAMES(tourItem.value.keyFrames); // 设置漫游第一视角数据
      window._cameraTour.curOa = tourItem.value.keyFrames[0];
      window._cameraTour.curOa.name = "第一视角";
      tourSetting(type);
    } else {
      // if (tourItem.value.type === "1") {
      //   madalShowToSetTour(1, id, taskId);
      // } else {
      useTaskStore().SET_KEYFRAMES([]); // 设置漫游数据
      window._cameraTour.start();
      tourSetting(type);
      // }
    }
  } else {
    if (
      tourItem.value.thirdKeyFrames &&
      tourItem.value.thirdKeyFrames.length > 0
    ) {
      useTaskStore().SET_THIRDKEYFRAMES(tourItem.value.thirdKeyFrames); // 设置漫游第三视角数据
      window._cameraTour.curOa = tourItem.value.thirdKeyFrames[0];
      window._cameraTour.curOa.name = "第三视角";
      tourSetting(type);
    } else {
      // if (tourItem.value.type === "1") {
      //   madalShowToSetTour(3, id, taskId);
      // } else {
      useTaskStore().SET_THIRDKEYFRAMES([]); // 设置漫游数据
      window._cameraTour.start();
      tourSetting(type);
      // }
    }
  }
  tourBol.value = false;
};
// 自动弹框生成漫游数据

const madalShowToSetTour = async (type, id, taskId) => {
  let msg = type === 1 ? "第一视角" : "第三视角";
  ElMessageBox.confirm(`是否要自动生成${msg}漫游数据`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let params = { sceneId: id, type: "lines", lineType: "0" };
      let lines = await getLineListData(params);
      if (lines.data?.length) {
        let list = [];
        for (const item of lines.data) {
          list.push(item.data.coordinates);
        }
        let keyFramesObj = await setAutoCameraTour(list, type);
        // let obj = {
        //   id: id,
        //   time: keyFramesObj.time,
        //   keyFrames: [keyFramesObj],
        // };
        let obj;
        if (type === 1) {
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
            queryTaskInfo({ id: taskId }).then((res) => {
              cameraStore.setTaskInfo(res.data);
              useTaskStore().SET_TASKINFO(res.data);
            });
          }
        });
      } else {
        ElMessage.warning("当前场景没有路线，无法自动生成漫游");
      }
    })
    .catch(() => {
      useTaskStore().SET_KEYFRAMES([]); // 设置漫游数据
      window._cameraTour.start();
      tourSetting(type);
    });
};
const madalShowToSetTourCustom = async (type, id, taskId) => {
  let msg = type === 1 ? "第一视角" : "第三视角";
  let params = { sceneId: id, type: "lines", lineType: "0" };
  let lines = await getLineListData(params);
  if (lines.data?.length) {
    let list = [];
    for (const item of lines.data) {
      list.push(item.data.coordinates);
    }
    let keyFramesObj = await setAutoCameraTour(list, type);
    let obj;
    if (type === 1) {
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
        proxy.$modal.msgSuccess(`已为您自动生成${msg}漫游`);
        queryTaskInfo({ id: taskId }).then((res) => {
          cameraStore.setTaskInfo(res.data);
          useTaskStore().SET_TASKINFO(res.data);
          let news = taskInfo.value.sceneList.filter((item) => item.id === id);
          tourView(news[0]);
        });
      }
    });
  } else {
    ElMessage.warning("当前场景没有路线，无法自动生成漫游");
  }
};
// 漫游参数设置
const tourSetting = (type) => {
  useTaskStore().set_keyFramesName(type);
  useTaskStore().SET_ISSHOWFRAMES(true);
  useScreenStore().set_editScreen(false);
  useTaskStore().SET_TASKPLAN(false);
  useTaskStore().SET_SCREENPLAN(false); // 加强任务场景规划弹窗
  useTaskStore().set_sceneBol(false); // 普通任务场景弹框
};
// 关闭资源列表
const closeResource = () => {
  statusSetting();
  cameraStore.setisShowListShow(true);
};
// 状态控制
const statusSetting = () => {
  clearDraw(); // 清除标绘数据
  closeFloors(); // 清除楼层炸开数据
  useScreenStore().set_editScreen(false); // 设置编辑状态
  useTaskStore().SET_TASKPLAN(false); // 设置任务规划弹窗
  useTaskStore().SET_SCREENPLAN(false); // 加强任务场景规划弹窗
  useTaskStore().set_sceneBol(false); // 普通任务场景弹框
  // useTaskStore().set_communityBol(false); // 社区详情弹框
  useEmergencyStore().set_showEmergencyPlan(false); // 取消应急预案弹框
  // 清除选中样式
  taskInfo.value.sceneList.map((item) => {
    item.select = false;
  });
  // 设置工具栏隐藏
  SettingStore.setShowTool(false);
  // 隐藏资源列表
  useScreenStore().set_resourceBol(false);
  useFloorStore().set_isShowFloor(false);
};
// 添加场景
const add = async () => {
  statusSetting();
  addData.value = true;
};
// 删除场景
async function deleteGuide(index) {
  ElMessageBox.confirm("您确定要删除该场景并删除场景下的数据吗?", "删除", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "Warning",
  }).then(() => {
    deleteScreenNew({ id: index.id }).then((res) => {
      if (res.code === 0) {
        proxy.$modal.msgSuccess("删除成功");
        statusSetting();
        drawBol.value = false;
        clearDraw().then(() => {
          loadScenePlace();
        }); // 清除场景数据
        queryTaskInfo({ id: index.taskId }).then((res) => {
          cameraStore.setTaskInfo(res.data);
          useTaskStore().SET_TASKINFO(res.data);
        });
      }
    });
  });
}
// 清除所有动效
const clearMove = async () => {
  let g = window.__g;
  g.radiationPoint.clear(); // 清除所有辐射点位样式
  g.tools.stopViewshedAnalysis(); // 停止视域分析
  //  g.cameraTour.stop();// 停止漫游
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
};
// 编辑标绘数据以及楼层炸开数据
const showMarkersDrawData = (taskId, id) => {
  // 绘制标绘数据
  useFloorStore().set_floorMarkers([]);
  closeFloors(); // 清除楼层炸开数据
};
// 绘制漫游编辑以及漫游预览的资源要素
const drawToursForReasuce = async (sceneId, status = false) => {
  getReourceDataForScene({ sceneId }).then(async (res) => {
    WorkCockpitStore.set_basicIds(sceneId, 1);
    let treeData = res.data || [];
    let treeLayer = flattenTreeData(treeData[0].children);
    let arr = treeLayer.filter((item) => item.dataLevelFlag);
    let fileterNode = arr.filter((item) => !item.floorNum);
    drawResourceDataAll(fileterNode, true);
  });
};
onBeforeUnmount(() => {
  emitter.off("ToTaskDetails");
  clearMove();
});
onUnmounted(() => {
  emitter.off("ToTaskDetails");
  clearMove();
});
</script>

<style lang="scss" scoped>
@import "./index.scss";

.tourView_box {
  position: absolute;
  z-index: 3;
  left: 350px;
  bottom: 60px;
  width: 234px;
  // background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
  display: flex;
  align-items: center;

  .btn_view {
    background: rgba(31, 76, 152, 0.8677);
    width: 80px;
    height: 30px;
    text-align: center;
    color: #fff;
    line-height: 30px;
    margin-right: 10px;
    cursor: pointer;
    border-radius: 4px;
    font-size: 14px;
    position: relative;

    .pleusBtn {
      background: #274eef;
      width: 40px;
      height: 20px;
      text-align: center;
      color: #fff;
      line-height: 20px;
      position: absolute;
      top: -25px;
      left: 20px;
      font-size: 12px;
    }
  }

  .active {
    background: #274eef;
  }

  .el-icon {
    font-size: 24px;
    cursor: pointer;
  }
}
</style>
<style lang="scss">
.taskViewCoodr {
  position: absolute;
  top: 14px;
  right: 20px;
  width: 80px;
  height: 32px;
  background: #274eef;
  text-align: center;
  line-height: 32px;
  text-align: center;
  font-family: Source Han Sans;
  font-size: 14px;
  font-weight: 500;
  color: #fff;
  cursor: pointer;
}

.edit_dialog {
  background: url("../../../assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
}

.edit_dialog .el-dialog__title {
  color: #fff;
  font-weight: bold;
}

.edit_dialog {
  .el-radio__label {
    color: #fff;
  }

  .dialog-footer {
    display: flex;
    align-items: center;
    justify-content: center;

    .btn_box {
      width: 88px;
      height: 36px;
      background: #274eef;
      border-radius: 4px;
      font-size: 14px;
      color: #ffffff;
      line-height: 36px;
      text-align: center;
      cursor: pointer;
    }

    .cancle_btn {
      margin-right: 20px;
      background: rgba(31, 76, 152, 0.8677);
    }
  }

  .el-select__wrapper {
    background: rgba(0, 12, 78, 0.5);
    box-shadow: none;
    border: 1px solid #5b6799;
    border-radius: 2px;
    color: #fff;
  }

  .el-select__selected-item {
    span {
      color: #fff;
    }
  }

  .tabs {
    display: flex;
    width: 100%;
    height: 40px;
    align-items: center;
    border: 1px solid gray;
    margin-bottom: 10px;

    .tabs_item {
      flex: 1;
      text-align: center;
      font-weight: 350;
      font-size: 14px;
      color: #97b9ff;
      border-radius: 0px;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
    }

    .active {
      color: #fff;
      background: linear-gradient(180deg,
          rgba(0, 74, 218, 0) 0%,
          rgba(0, 74, 218, 0.75) 100%);
      border-radius: 0px 0px 0px 0px;
      border: 1px solid;
      border-image: linear-gradient(180deg,
          rgba(0, 0, 0, 0),
          rgba(151.00000619888306, 185.00000417232513, 255, 1)) 1 1;
      border-left: 0px;
      border-bottom: 0px;
    }
  }
}
</style>
