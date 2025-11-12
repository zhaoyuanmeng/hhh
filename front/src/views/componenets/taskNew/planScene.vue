<template>
  <div class="taskPlan_box">
    <div class="heard">
      {{ screenTitle }}详情
      <div  class="close_modal" @click="closeModal"></div>
      <div v-if="isRouter" @click="goBack" class="back_modal"></div>
    </div>
    <div class="plan_box">
      <div class="screen_box" style="position: relative;height: 30px;">
        <div @click="getCood(screenInfo.id)" class="taskViewCoodr" title="场景视角"></div>
        <div class="title_name">{{ screenInfo.sceneName }}</div>
      </div>
      <div class="item_box" @click="showDA=true" style="padding-left: 24px;">
        <div class="left" style="color: #00CEFF;cursor: pointer;width: 100%;">电子档案</div>
        </div>
        <ScreenOtherPolice/>
    </div>

     <!-- 视角复位 -->
     <div class="viewReset" @click="viewResetFun">
    <div class="icon_name"></div>
  </div>

  </div>
    <!-- 电子文档 -->
  <ElectronCard v-if="showDA" @close="showDA=false" :id="screenInfo.id"/>
  <el-dialog
    v-model="openBol"
    width="400px"
    @close="cancel"
    align-center
    :destory-on-close="false"
    :close-on-click-modal="false"
    append-to-body
    class="my_Dialog_text"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ titleName }}</div>
      </div>
    </template>
    <div class="orgLeard" v-if="titleName === '组织领导'">
      <el-form
        ref="customForm"
        :model="orgForm"
        :label-suffix="'：'"
        :label-width="100"
        class="customForm"
        size="small"
      >
        <el-form-item
          label="总指挥"
          prop="zzh.name"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          style="width: 100%"
        >
          <el-input
            v-model="orgForm.zzh.name"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>

        <el-form-item
          label="联系电话"
          style="width: 100%"
          prop="zzh.phone"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="orgForm.zzh.phone"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
        <el-form-item
          label="职位"
          style="width: 100%"
          prop="zzh.job"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="orgForm.zzh.job"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
        <div class="org_box">
          <el-form-item
            label="副总指挥"
            prop="fzzh.name"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 100%"
          >
            <el-input
              v-model="orgForm.fzzh.name"
              placeholder="请输入..."
            ></el-input>
          </el-form-item>

          <el-form-item
            label="联系电话"
            style="width: 100%"
            prop="fzzh.phone"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input
              v-model="orgForm.fzzh.phone"
              placeholder="请输入..."
            ></el-input>
          </el-form-item>
          <el-form-item
            label="职位"
            style="width: 100%"
            prop="fzzh.job"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input
              v-model="orgForm.fzzh.job"
              placeholder="请输入..."
            ></el-input>
          </el-form-item>
        </div>
        <div
          class="org_box"
          v-for="(item, index) in orgForm.member"
          :key="index"
        >
          <el-form-item
            label="成员"
            :prop="'member.' + index + '.name'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 100%"
          >
            <el-input
              v-model="item.name"
              placeholder="请输入..."
              style="width: 90%"
            ></el-input>
            <el-icon
              style="margin-left: 6px; cursor: pointer; color: #fff"
              @click="clearPeople(index)"
            >
              <Delete />
            </el-icon>
          </el-form-item>

          <el-form-item
            label="联系电话"
            style="width: 100%"
            :prop="'member.' + index + '.phone'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input v-model="item.phone" placeholder="请输入..."></el-input>
          </el-form-item>
          <el-form-item
            label="职位"
            style="width: 100%"
            :prop="'member.' + index + '.job'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input v-model="item.job" placeholder="请输入..."></el-input>
          </el-form-item>
        </div>
        <div class="addPeople" @click="addPerson">添加成员</div>
      </el-form>
    </div>
    <div class="text_box" v-else>
      <el-input
        v-model="htmlData"
        :rows="10"
        type="textarea"
        placeholder="请输入..."
      />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="submit">确定</div>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import { useRouter } from "vue-router";
import {
  ref,
  computed,
  getCurrentInstance,
  reactive,
  onBeforeUnmount,
  onUnmounted,
  onMounted,
} from "vue";
import {  closeFloors } from "@/components/SmartMap/js/utils";
import useFloorStore from '@/store/modules/floorStore'
import { clearDraw,drawContrlData } from "./util";
import useTaskStore from "@/store/modules/taskStore";
import useScreenStore from "@/store/modules/screenStore";
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import useSettingStore from "@/store/modules/settingStore";
import useCameraStore from "@/store/modules/cameraSet";
import { searchNodePlanToScreen, saveScreenPlan,updateScreen } from "@/api/task/task";
import {addTourResouce } from "@/components/SmartMap/js/utils";
import {drawResourceDataAll} from '@/components/SmartMap/js/resource'
import {getDrawDataForScreen,getReourceDataForScene} from "@/api/task/task"
import usePlanTaskStore from "@/store/modules/planTask";
import ElectronCard from '@/components/electronCard'
import { ElMessage, ElMessageBox } from "element-plus";
import {searchScreenForId} from "@/api/task/new"
import ScreenOtherPolice from "./screenOtherPolice.vue";
import { flattenTreeData,loadPicture} from "@/utils";
import {queryTaskInfo} from "@/api/task/new";
let taskInfo = computed(() => useTaskStore().taskInfo); // 任务信息信息
let eventMapData = computed(()=>useWorkCockpitStore().eventMapData)
const { proxy } = getCurrentInstance();
const router = useRouter();
const activeName = ref("");
let playtimer = ref(null);
const isRouter = computed(()=>usePlanTaskStore().routerGo)
// let checked1 = ref(false)
// let checked2= ref(false)
// let checked3 = ref(false)
// let checked4 = ref(true)
// let checked5= ref(false)
// let checked6 = ref(false)
let checked1 = computed(()=>useWorkCockpitStore().policeCheckBox)
let checked2= computed(()=>useWorkCockpitStore().carCheckBox)
let checked3 = computed(()=>useWorkCockpitStore().linesCheckBox)
let checked4 = computed(()=>useWorkCockpitStore().policeLineCheckBox)
let checked5= computed(()=>useWorkCockpitStore().bussinessCheckBox)
let checked6 = computed(()=>useWorkCockpitStore().communityCheckBox)
const screenTitle = computed(() => useTaskStore().screenPlanTitle);
const screenInfo = computed(() => useTaskStore().screenModalInfo);
const activeList = ref([
  "基本情况",
  "警力部署",
  "应急力量",
  "应急避险点",
  "应急医院",
]);
const showDA = ref(false)
const openBol = ref(false);
const htmlData = ref("");
const titleName = ref("");
const textData = ref("");
const selectVal = ref("");
const updateNodeId = ref(null);
const orgForm = reactive({
  zzh: { name: "", phone: "", job: "" },
  fzzh: { name: "", phone: "", job: "" },
  member: [],
});
const selectLay = (event,type)=> {
  console.log(event,type)
  useWorkCockpitStore().set_checkBox(type,event)
  drawContrlData(event,type)
}
const orgInfo = ref({});
// 添加成员
const addPerson = () => {
  let obj = { name: "", phone: "", job: "" };
  orgForm.member.push(obj);
};
// 删除成员
const clearPeople = (index) => {
  orgForm.member.splice(index, 1);
};
const goBack = () => {
  if(isRouter.value){
    router.back();
  }else{
    useSettingStore().setShowTaskPanle(true);
    useSettingStore().setshowTaskFun(true);
  }
  usePlanTaskStore().set_checkInfo({});
  usePlanTaskStore().set_checkDetails(false);
  usePlanTaskStore().set_routerGo(false)
  useSettingStore().setShowTool(false);
  clearDraw(); // 清除场景数据
  closeFloors(); // 清除楼层炸开数据
  useCameraStore().setShowTaskInfo(false);
  usePlanTaskStore().setCurPage(false)
  useScreenStore().set_editScreen(false);
  useTaskStore().set_sceneBol(false);
  useScreenStore().set_resourceBol(false);
  useFloorStore().set_isShowFloor(false)
}
onMounted(() => {
  // selectLay(false,'police')
  // selectLay(false,'car')
  // selectLay(false,'line')
  // selectLay(false,'business')
  // selectLay(false,'community')
  // selectLay(false,'basic')
});
// 保存任务视角
const getCood = async (ids) => {
  let g = window.__g
  let cood = await g.camera.get()
  let obj = {
    viewData: cood,
    id: ids
  }
  updateScreen(obj).then(res => {
    if (res.code === 0) {
      ElMessage.success('场景视角已保存')
    }
  })
}
const viewResetFun = () => {
  if(screenInfo.value.viewData){
    let g = window.__g
    g.camera.set(screenInfo.value.viewData.camera, 1.5)
  }
}
// 选择面板
const changeActive = (e) => {
  if (e) {
    if (e !== "警卫部署及安全措施") {
      let params = { sceneId: screenInfo.value.id, planNode: e };
      selectVal.value = e;
      searchNodePlanToScreen(params).then((res) => {
        if (e === "组织领导") {
          if (res.data && res.data.length > 0) {
            updateNodeId.value = res.data[0].id;
            orgInfo.value = res.data[0];
          } else {
            updateNodeId.value = null;
            orgInfo.value = {};
          }
        } else {
          if (res.data && res.data.length > 0) {
            textData.value = res.data[0].data.msg;
            updateNodeId.value = res.data[0].id;
          } else {
            textData.value = "";
            updateNodeId.value = null;
          }
        }
      });
    }
  }
};
// 关闭面板
const closeModal = () => {
  if(isRouter.value){
    router.back();
  }else{
    useSettingStore().setShowTaskPanle(true);
    useSettingStore().setshowTaskFun(true);
  }
  usePlanTaskStore().set_checkInfo({});
  usePlanTaskStore().set_checkDetails(false);
  usePlanTaskStore().set_routerGo(false)
  useSettingStore().setShowTool(false);
  clearDraw(); // 清除场景数据
  closeFloors(); // 清除楼层炸开数据
  useCameraStore().setShowTaskInfo(false);
  usePlanTaskStore().setCurPage(false)
 
  useScreenStore().set_editScreen(false);
  useTaskStore().set_sceneBol(false);
  useScreenStore().set_resourceBol(false);
  useFloorStore().set_isShowFloor(false)
  
};
// 普通填写编辑
const openDialog = (item) => {
  openBol.value = true;
  titleName.value = item.name;
  htmlData.value = textData.value;
  if (item.name === "组织领导") {
    console.log(orgInfo.value);
    // orgForm
    if (orgInfo.value.id) {
      orgForm.zzh = orgInfo.value.data.zzh;
      orgForm.fzzh = orgInfo.value.data.fzzh;
      orgForm.member = orgInfo.value.data.member;
    }
  }
};
const cancel = () => {
  openBol.value = false;
  htmlData.value = "";
};
const submit = () => {
  if (titleName.value === "组织领导") {
    proxy.$refs["customForm"].validate((valid) => {
      if (valid) {
        console.log(orgForm);
        let data = {
          sceneId: screenInfo.value.id,
          planNode: titleName.value,
          data: orgForm,
          id: updateNodeId.value,
        };
        saveScreenPlan(data).then((res) => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess("编辑成功");
            changeActive(titleName.value);
            cancel();
          }
        });
      }
    });
  } else {
    if (!htmlData.value) return proxy.$modal.msgWarning("不能为空");
    let data = {
      sceneId: screenInfo.value.id,
      planNode: titleName.value,
      data: { msg: htmlData.value },
      id: updateNodeId.value,
    };
    saveScreenPlan(data).then((res) => {
      if (res.code === 0) {
        proxy.$modal.msgSuccess("编辑成功");
        changeActive(titleName.value);
        cancel();
      }
    });
  }
};

// 场景漫游播放
const playTours = (item) => {
  if (!item.keyFrames || item.keyFrames.length === 0) {
    return ElMessage({
      message: "当前场景没有漫游数据，请先编辑添加",
      type: "warning",
    });
  }
  window._cameraTour.curOa = item.keyFrames[0];
  window._cameraTour.play_biz();
  playtimer.value = setTimeout(() => {
    playtimer.value = null;
  }, Number(item.time) * 1000);
};
// 播放当前任务下场景漫游
const handleTaskPlay = () => {
console.log(taskInfo.value,eventMapData.value)
  searchScreenForId({ taskId: taskInfo.value.id }).then((res) => {
      if (res.code === 0) {
        if (res.data.length > 0) {
          useTaskStore().SET_TASKVIDEOLIST(res.data);
          getDrawDataForScreen({ sceneId: eventMapData.value.GroupID }).then((res1) => {
            useTaskStore().set_taskDrawData(res1.data);
            drawToursForReasuce(eventMapData.value.GroupID)
            useWorkCockpitStore().setCurPage('taskPlay')
            useWorkCockpitStore().set_showResouce(true)
            useWorkCockpitStore().setCurTourId(eventMapData.value.GroupID)
          })
        } else {
          proxy.$modal.msgWarning("暂无推演数据");
        }
      }
    });
}

// 绘制漫游编辑以及漫游预览的资源要素
const drawToursForReasuce = async (sceneId) => {
  getReourceDataForScene({ sceneId }).then(res => {
    useWorkCockpitStore().set_basicIds(sceneId,1)
   let  treeData = res.data || []
    let treeLayer = flattenTreeData(treeData[0].children)
    let arr = treeLayer.filter(item=>item.dataLevelFlag)
    let fileterNode = arr.filter((item) => !item.floorNum);
    // addTourResouce(fileterNode)
    drawResourceDataAll(fileterNode,true)
    
  })
}
// 场景一张图
const onePicture = () => {
  useTaskStore().SET_clearModal(false);
  useScreenStore().set_showPicture(true);
  useScreenStore().set_pictureId(screenInfo.value.id);
  useScreenStore().set_pictureName(screenInfo.value.sceneName);
  useScreenStore().set_pictureType("scene");
  useSettingStore().setShowTool(false);
};
onBeforeUnmount(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
});
onUnmounted(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
});
</script>
<style lang="scss" scoped>
.taskPlan_box {
  position: absolute;
  transform: scale(0.8);
  z-index: 3;
  right: -2%;
  bottom: -50px;
  top: -32px;
  width: 400px;
  background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
  display: flex;
  flex-direction: column;

  .heard {
    height: 40px;
    background: url("../../../assets/panel/panel_bg.png") no-repeat;
    background-size: 100% 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: PingFangSC;
    font-weight: 400;
    font-size: 14px;
    color: #ffffff;
    text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
    font-style: normal;
    position: relative;

    .close_modal {
      position: absolute;
      right: 16px;
      top: 50%;
      transform: translateY(-50%);
      width: 20px;
      height: 20px;
      background: url("./img/close_new.png") no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
    }
    .back_modal{
      position: absolute;
      left: 16px;
      top: 50%;
      transform: translateY(-50%);
      width: 20px;
      height: 20px;
      background: url("./img/back.png") no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
    }
  }

  .plan_box {
    flex: 1;
    overflow: auto;
    padding: 10px 4px;

    :deep(.el-collapse) {
      border: none;
    }

    :deep(.el-collapse-item) {
      margin-bottom: 7px;
    }

    :deep(.el-collapse-item__header) {
      background: linear-gradient(
        180deg,
        #3a60f9 0%,
        rgba(16, 51, 194, 0.13) 98%
      );
      border: 0px;
      transition: none;
      height: 32px;
      border-radius: 0;
      font-size: 14px;
      color: #fff;
      padding-left: 40px;
      line-height: 32px;
      position: relative;
    }

    :deep(.el-collapse-item__arrow) {
      position: absolute;
      left: 16px;
      font-size: 16px;
    }

    :deep(button:focus, button:focus-visible) {
      outline: none;
    }

    :deep(.el-collapse-item__wrap) {
      background: none;
      border: none;
    }

    :deep(.el-collapse-item__content) {
      color: #fff;
      min-height: 80px;
    }

    .collapse_box {
      padding: 6px 20px 0 30px;
      position: relative;

      .edit {
        // position: absolute;
        width: 84px;
        height: 32px;
        background: #274eef;
        line-height: 32px;
        text-align: center;
        right: 20px;
        cursor: pointer;
        float: right;
      }

      .org_modal {
        .item_box {
          .title {
            font-size: 16px;
            margin-top: 8px;
          }

          .bottom_data {
            display: flex;

            .left {
              flex: 1;
            }

            .right {
              flex: 1;
            }
          }
        }
      }
    }

    .screen_box {
      height: 100px;
      padding: 0px 24px 20px 24px;

      .taskViewCoodr {
        position: absolute;
        top: 10px;
        right: 20px;
        width: 20px;
        height: 20px;
        // background: #274EEF;
        background: url('@/assets/basic/视角锁定@1x.png') no-repeat;
        background-size: 100% 100%;
        // text-align: center;
        // line-height: 32px;
        // text-align: center;
        // font-family: Source Han Sans;
        // font-size: 14px;
        // font-weight: 500;
        // color: #fff;
        cursor: pointer;
      }
      .task-roam {
        position: absolute;
        top: 10px;
        right: 60px;
        display: flex;
        align-items: center;
        gap: 3px;
        color: #00CEFF;
        cursor: pointer;
      }
      .title_name {
        font-family: Source Han Sans;
        font-size: 18px;
        font-weight: bold;
        line-height: normal;
        letter-spacing: 0em;
        color: #ffffff;
      }

      .screen_prc {
        display: flex;
        align-items: center;
        margin-top: 24px;
        .btn_action {
          display: flex;
          align-items: center;
          justify-content: center;
          width: 84px;
          height: 32px;
          background: #274eef;
          color: #fff;
          cursor: pointer;
          font-size: 12px;
        }
      }
    }
    .task_box {
      padding-top: 10px;
      .item_box {
        display: flex;
        align-items: center;
        height: auto;

        .left {
          font-family: PingFangSC;
          font-weight: 400;
          font-size: 14px;
          color: rgba(255, 255, 255, 0.7);
          width: 70px;
        }

        .right {
          // color: #fff;
          font-family: PingFangSC;
          font-weight: 400;
          font-size: 14px;
          flex: 1;
          display: flex;
          align-items: center;
          overflow: hidden;
          text-overflow: ellipsis;
          /* 超出部分省略号 */
          word-break: break-all;
          /* break-all(允许在单词内换行。) */
          display: -webkit-box;
          /* 对象作为伸缩盒子模型显示 */
          -webkit-box-orient: vertical;
          /* 设置或检索伸缩盒对象的子元素的排列方式 */
          -webkit-line-clamp: 2;
          /* 显示的行数 */
          flex-wrap: wrap;

          .point {
            width: 8px;
            height: 8px;
            // background: #2b99ff;
            border-radius: 0px 0px 0px 0px;
            border-radius: 50%;
          }

          .text {
            // color: #2b99ff;
            margin-left: 4px;
          }
        }
        .yjjq {
          color: #ff4040;
          .point {
            background: #ff4040;
          }
        }
        .yjrw {
          color: #ff6633;
          .point {
            background: #ff6633;
          }
        }
        .ejrw {
          color: #ffe433;
          .point {
            background: #ffe433;
          }
        }
        .sjrw {
          color: #33ff77;
          .point {
            background: #33ff77;
          }
        }
        .bwrw {
          color: #00a6ff;
          .point {
            background: #00a6ff;
          }
        }
      }
    }
  }
}

:deep(.customForm) {
  .el-form-item__label {
    color: #fff;
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
        //   height: 36px;
        //   line-height: 36px;
        font-size: 14px;
        color: #ffffff;
        opacity: 0.8;
      }
    }
  }
}
.task_police_box {
  margin-top: 7px;

  .el-collapse-item__header {
    background: rgba(28, 53, 155, 0.5) !important;
    padding-left: 40px !important;
  }

  .el-collapse-item__arrow {
    position: absolute;
    left: 20px !important;
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

.viewReset {
  position: absolute;
  bottom: 0px;
  right: 420px;
  z-index: 20;
  width: 42px;
  height: 42px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  // padding: 5px;
  cursor: pointer;
  border: 1px solid #5B759B;
  background: linear-gradient(180deg, rgba(10, 29, 100, 0.7) 0%, rgba(21, 30, 73, 0.6984) 100%);
  // background: url("@/assets/basic/viewEye.png") no-repeat;
  // background-size: 100% 100%;
  .icon_name{
    width: 24px;
    height: 24px;
    background: url('@/assets/basic/容器@1x.png') no-repeat;
    background-size: 100% 100%;
  }
}
.viewReset:hover{
    background: rgba(38, 68, 173, 0.5);
    box-sizing: border-box;
    border: 0.61px solid rgba(38, 68, 173, 0.3);
  }
</style>

<style lang="scss">
.my_Dialog_text {
  background: url("../../../assets/panel/弹窗bg.png") no-repeat;
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

  .el-dialog__body {
    max-height: 600px;
    overflow: auto;
  }

  .dialog-footer {
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
      color: #fff;
    }
  }

  .text_box {
    .el-textarea__inner {
      background: rgba(0, 12, 78, 0.5);
      box-shadow: none;
      border: 1px solid #5b6799;
      color: #fff;
    }
  }

  .orgLeard {
    .org_box {
      margin-top: 16px;
    }

    .addPeople {
      width: 88px;
      height: 36px;
      text-align: center;
      line-height: 36px;
      background: #274eef;
      border-radius: 4px;
      color: #ffffff;
      cursor: pointer;
      margin: 0 auto;
    }
  }
}
.el-checkbox__label {
  color: #fff
}
</style>
