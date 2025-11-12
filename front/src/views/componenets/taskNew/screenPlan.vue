<template>
  <div class="taskPlan_box">
    <div class="heard">
      {{ screenTitle }}详情
      <div v-if="router.currentRoute.value.name === 'Task'" class="close_modal" @click="closeModal"></div>
      <div v-if="router.currentRoute.value.name === 'WorkCockpit'" @click="goBack" class="back_modal"></div>
    </div>
    <div class="plan_box">
      <div class="screen_box" style="position: relative;">
        <div class="title_name">{{ screenInfo.sceneName }}</div>
        <div class="task-roam" @click="handleTaskPlay" v-if="router.currentRoute.value.name === 'WorkCockpit'" >
          <img src="@/assets/workcockpit/roam_icon.png" alt="">
          <span>任务漫游</span>
        </div>
        <div @click="getCood(screenInfo.id, screenInfo.taskId)" class="taskViewCoodr" title="场景视角"></div>
        <div class="task_box">
          <div class="item_box">
            <div class="left">任务等级：</div>
            <div class="right yjjq" style="display: flex">
              <div class="point"></div>
              <div class="text">一级加强</div>
            </div>
          </div>
          <div class="item_box">
            <div class="left">开始时间：</div>
            <div class="right">{{ screenInfo.beginTime }}</div>
          </div>
          <div class="item_box">
            <div class="left">结束时间：</div>
            <div class="right">{{ screenInfo.endTime }}</div>
          </div>
        </div>
      </div>
      <div class="add_Node">
      <div class="add_Node_item" @click="addNodeFun">添加节点</div>
    </div>
      <el-collapse v-model="activeName" accordion @change="changeActive">
        <el-collapse-item :title="item.name" :name="item.node" v-for="(item, index) in activeList" :key="index">
          <div class="collapse_box" v-if="item.name !== '警卫部署及安全措施'">
            <div class="edit" @click="openDialog(item)">编辑</div>
            <div class="content" v-if="item.name !== '组织领导'">
              <div>{{ textData }}</div>
              <div class="basicInfo_box" v-if="activeName === '基本情况'" style="padding: 0px;">
                <div class="title_name">路线统计</div>
                <div class="basicData">路线距离：{{ Number(basicLines.totalLength).toFixed(2) }}公里</div>
                <div class="title_name">路线详情</div>
                <div class="basicData" v-for="(item,index) in basicLines.detail" style="cursor: pointer;" @click="jumpLine(item)">
                  {{ item.name }}（{{Number(item.length).toFixed(2)}}公里）
                </div>
              </div>
            </div>
            <div v-else class="org_modal">
              <div v-if="orgInfo.data">
                <div class="item_box">
                  <div class="title">总指挥</div>
                  <div class="name">姓名：{{ orgInfo.data.zzh.name }}</div>
                  <div class="bottom_data">
                    <div class="left">职位：{{ orgInfo.data.zzh.job }}</div>
                    <div class="right">电话：{{ orgInfo.data.zzh.phone }}</div>
                  </div>
                </div>
                <div class="item_box">
                  <div class="title">副总指挥</div>
                  <div class="name">姓名：{{ orgInfo.data.fzzh.name }}</div>
                  <div class="bottom_data">
                    <div class="left">职位：{{ orgInfo.data.fzzh.job }}</div>
                    <div class="right">电话：{{ orgInfo.data.fzzh.phone }}</div>
                  </div>
                </div>
                <div class="item_box" v-for="(item, index) in orgInfo.data.member">
                  <div class="title">成员{{ index + 1 }}</div>
                  <div class="name">姓名：{{ item.name }}</div>
                  <div class="bottom_data">
                    <div class="left">职位：{{ item.job }}</div>
                    <div class="right">电话：{{ item.phone }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else>
            <ScreenPolice />
          </div>
        </el-collapse-item>
      </el-collapse>

          <!-- 自定义节点 -->
    <el-collapse v-model="activeName" accordion @change="changeActive">
      <el-collapse-item
        :title="`${item.node}`"
        :name="item.node"
        v-for="(item, index) in addActiveList"
        :key="index"
      >
      <template #title>
        <div style="display:flex;align-items:center;justify-content: space-between;width: 95%;">{{item.node}}
          <el-icon @click.stop="delNode(item.name)"><Delete /></el-icon>
        </div>
        </template>
        <div class="collapse_box">
            <div class="edit" @click="openDialog(item)">编辑</div>
            <div class="content" >
              <div>{{ textData }}</div>
            </div>

          </div>
      </el-collapse-item>
    </el-collapse>


    </div>
     <!-- 视角复位 -->
     <div class="viewReset" @click="viewResetFun" v-if="router.currentRoute.value.name === 'Task'">
    <div class="icon_name"></div>
  </div>
  </div>
  <el-dialog v-model="openBol" width="400px" @close="cancel" align-center :destory-on-close="false"
    :close-on-click-modal="false" append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ titleName }}</div>
      </div>
    </template>
    <div class="orgLeard" v-if="titleName === '组织领导'">
      <el-form ref="customForm" :model="orgForm" :label-suffix="'：'" :label-width="100" class="customForm" size="small">
        <el-form-item label="总指挥" prop="zzh.name" :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          style="width: 100%">
          <el-input v-model="orgForm.zzh.name" placeholder="请输入..."></el-input>
        </el-form-item>

        <el-form-item label="联系电话" style="width: 100%" prop="zzh.phone"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]">
          <el-input v-model="orgForm.zzh.phone" placeholder="请输入..."></el-input>
        </el-form-item>
        <el-form-item label="职位" style="width: 100%" prop="zzh.job"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]">
          <el-input v-model="orgForm.zzh.job" placeholder="请输入..."></el-input>
        </el-form-item>
        <div class="org_box">
          <el-form-item label="副总指挥" prop="fzzh.name" :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 100%">
            <el-input v-model="orgForm.fzzh.name" placeholder="请输入..."></el-input>
          </el-form-item>

          <el-form-item label="联系电话" style="width: 100%" prop="fzzh.phone"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]">
            <el-input v-model="orgForm.fzzh.phone" placeholder="请输入..."></el-input>
          </el-form-item>
          <el-form-item label="职位" style="width: 100%" prop="fzzh.job"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]">
            <el-input v-model="orgForm.fzzh.job" placeholder="请输入..."></el-input>
          </el-form-item>
        </div>
        <div class="org_box" v-for="(item, index) in orgForm.member" :key="index">
          <el-form-item label="成员" :prop="'member.' + index + '.name'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]" style="width: 100%">
            <el-input v-model="item.name" placeholder="请输入..." style="width: 90%"></el-input>
            <el-icon style="margin-left: 6px; cursor: pointer; color: #fff" @click="clearPeople(index)">
              <Delete />
            </el-icon>
          </el-form-item>

          <el-form-item label="联系电话" style="width: 100%" :prop="'member.' + index + '.phone'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]">
            <el-input v-model="item.phone" placeholder="请输入..."></el-input>
          </el-form-item>
          <el-form-item label="职位" style="width: 100%" :prop="'member.' + index + '.job'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]">
            <el-input v-model="item.job" placeholder="请输入..."></el-input>
          </el-form-item>
        </div>
        <div class="addPeople" @click="addPerson">添加成员</div>
      </el-form>
    </div>
    <div class="text_box" v-else>
      <el-input v-model="htmlData" :rows="10" type="textarea" placeholder="请输入..." />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="submit">确定</div>
      </div>
    </template>
  </el-dialog>

  <!-- 新增节点 -->
  <el-dialog
    v-model="nodeBol"
    width="400px"
    @close="nodeName = ''"
    align-center
    :destory-on-close="false"
    :close-on-click-modal="false"
    append-to-body
    class="my_Dialog_text"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">添加节点</div>
      </div>
    </template>
    <div class="text_box">
      <el-input
        v-model="nodeName"
        placeholder="请输入节点名称..."
      />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="submitNodeName">确定</div>
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
import useFloorStore from '@/store/modules/floorStore'
import { closeFloors } from "@/components/SmartMap/js/utils";
import { clearDraw, drawContrlData } from "./util";
import useTaskStore from "@/store/modules/taskStore";
import useScreenStore from "@/store/modules/screenStore";
import useSettingStore from "@/store/modules/settingStore";
import useCameraStore from "@/store/modules/cameraSet";
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import {searchScreenForId} from "@/api/task/new"
import {addTourResouce } from "@/components/SmartMap/js/utils";
import {drawResourceDataAll} from '@/components/SmartMap/js/resource'
import { searchNodePlanToScreen, saveScreenPlan, updateScreen,searchBasicLinesStatic,searchPlanNodeList,delPlanNodeList } from "@/api/task/task";
import {getDrawDataForScreen,getReourceDataForScene} from "@/api/task/task"
import { ElMessage, ElMessageBox } from "element-plus";
import ScreenPolice from "./screenAndPolice.vue";
import { flattenTreeData,loadPicture} from "@/utils";
import { queryTaskInfo } from "@/api/task/new";
let taskInfo = computed(() => useTaskStore().taskInfo); // 任务信息信息
let eventMapData = computed(()=>useWorkCockpitStore().eventMapData)
const { proxy } = getCurrentInstance();
const router = useRouter();
const activeName = ref("");
let playtimer = ref(null);
// let checked1 = ref(false)
// let checked2 = ref(false)
// let checked3 = ref(false)
// let checked4 = ref(true)
// let checked5 = ref(false)
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
  { name: "基本情况", node: "基本情况" },
  { name: "组织领导", node: "组织领导" },
  { name: "任务分工", node: "任务分工" },
  { name: "警卫部署及安全措施", node: "警卫部署及安全措施" },
  { name: "工作要求", node: "工作要求" },
]);
const addActiveList = ref([])
const nodeBol = ref(false)
const nodeName = ref('')
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
const selectLay = (event, type) => {
  console.log(event, type)
  drawContrlData(event, type)
  useWorkCockpitStore().set_checkBox(type,event)
}
const orgInfo = ref({});
const basicLines = ref({})
// 添加成员
const addPerson = () => {
  let obj = { name: "", phone: "", job: "" };
  orgForm.member.push(obj);
};
// 删除成员
const clearPeople = (index) => {
  orgForm.member.splice(index, 1);
};
onMounted(() => {
  console.log(screenInfo.value,'初始化页面');
  setTimeout(()=>{
    getNodeList()
  },500)
  // selectLay(false,'police')
  // selectLay(false,'car')
  // selectLay(false,'line')
  // selectLay(false,'business')
  // selectLay(false,'community')
  // selectLay(false,'basic')
});

// 添加节点
const addNodeFun = () => {
  nodeBol.value = true;
}
// 删除节点
const delNode = (item) =>{
  ElMessageBox.confirm("您确定要删除该节点?", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      delPlanNodeList({ id: item }).then((res) => {
        if (res.code === 0) {
          proxy.$modal.msgSuccess("删除成功！");
          getNodeList()
          activeName.value = ''
        }
      });
    })
    .catch(() => {
      console.log("已取消");
    });
}
const getNodeList = () => {
  let datas = ["基本情况","组织领导","任务分工","工作要求","隐患排查",
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
  "重点人"]
  let params = {
    sceneId: screenInfo.value.id,
    excludePlanNodes:datas.join(',')
  }
  searchPlanNodeList(params).then(res=>{
    if(res.data?.length){
      let datas = res.data.map(item=>{
        return {name:item.id,node:item.planNode}
      })
      addActiveList.value = datas
    }else{
      addActiveList.value = []
    }

  })
}
// 添加节点名
const submitNodeName = () => {
  if (!nodeName.value) return proxy.$modal.msgWarning("节点名称不能为空");
  let data = {
    sceneId: screenInfo.value.id,
    planNode: nodeName.value,
  };
  saveScreenPlan(data).then((res) => {
    if (res.code === 0) {
      proxy.$modal.msgSuccess("添加成功");
      getNodeList()
      nodeBol.value = false;
    }
  });
}

// 保存任务视角
const getCood = async (ids, taskId) => {
  let g = window.__g
  let cood = await g.camera.get()
  let obj = {
    viewData: cood,
    id: ids
  }
  updateScreen(obj).then(res => {
    if (res.code === 0) {
      ElMessage.success('场景视角已保存')
      queryTaskInfo({ id: taskId }).then((res) => {
        useTaskStore().SET_TASKINFO(res.data);
      });
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
      if(e==='基本情况'){
        searchBasicLinesStatic({ sceneId: screenInfo.value.id }).then(res=>{
          basicLines.value = res.data
        })
      }
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
  useTaskStore().SET_SCREENPLAN(false);
  useCameraStore().setisShowListShow(true);
  useScreenStore().set_resourceBol(false);
  useSettingStore().setShowTool(false);
  useFloorStore().set_isShowFloor(false)
  closeFloors();
  clearDraw()
};
// 普通填写编辑
const openDialog = (item) => {
  openBol.value = true;
  titleName.value = item.node;
  htmlData.value = textData.value;
  if (item.node === "组织领导") {
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
const goBack = () => {
  useWorkCockpitStore().setCurPage('taskDetail')
  useFloorStore().setFloornumberShow(false)
  closeFloors()
  useSettingStore().set_isClickTools(true);// 隐藏工具栏选中状态
  useWorkCockpitStore().setShowTaskDetail(true)
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
// 场景漫游
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
    // addTourResouce(fileterNode);
    drawResourceDataAll(fileterNode,true)
  })
}
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
// 路线跳转
const jumpLine = async (item) => {
  let g = window.__g
  let res = await g.polyline.get(item.id)
  if(res.result===0){
    g.polyline.focus(item.id, 50,0.5)
  }else{
    ElMessage.warning("地图上暂无该路线，请先勾选绘制");
  }
}
</script>
<style lang="scss" scoped>
.taskPlan_box {
  position: absolute;
  transform: scale(0.8);
  z-index: 3;
  right: -1.6%;
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
    .add_Node{
    display:flex;
    align-items:center;
    justify-content:end;
    margin-bottom:10px;
    margin-right:20px;

    .add_Node_item{
      height:30px;
      width:80px;
      text-align:center;
      line-height:30px;
      background: rgba(35, 101, 255, 0.898);
      cursor:pointer;
      font-size:14px;
    }
  }
    :deep(.el-collapse) {
      border: none;
    }

    :deep(.el-collapse-item) {
      margin-bottom: 7px;
    }

    :deep(.el-collapse-item__header) {
      background: linear-gradient(180deg,
          #3a60f9 0%,
          rgba(16, 51, 194, 0.13) 98%);
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
      height: 120px;
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
        height: 30px;

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
  .basicInfo_box{
  padding: 0px 30px;
  .title_name{
    color: #00CEFF;
    font-weight: bold;
    font-size: 14px;
    margin-top: 10px;
  }
  .basicData{
    color: #fff;
    font-size: 14px;
    margin-top: 6px;
  }
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
