<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-11 09:41:28
 * @LastEditTime: 2024-06-22 11:25:00
 * @LastEditors: Alex
-->
<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-21 11:05:29
 * @LastEditors: Alex
-->
<template>
  <div class="taskStart_content" v-if="clearModal">
    <div v-if="taskInfoBol">
      <div class="left_title_shrink" v-if="!showPanle" @click="toggleLength">
        <div class="importantInfoText">任务管理</div>
      </div>
    </div>
    <transition name="slide">
      <div class="left_box" v-if="showPanle">
        <div :class="showPanle ? 'shrink' : ''" @click="toggleLength">
          <div :class="showPanle ? 'to_left' : ''"></div>
        </div>
        <div class="heard">任务管理</div>
        <div class="content">
          <div class="taskcount">
            <div class="top_box">
              <div class="left">
                <div class="data_num">
                  {{ taskStatic.taskTotalNum }}<span class="span_1">个</span>
                </div>
                <div class="task_num_name">任务预案</div>
              </div>
              <div class="left">
                <div class="data_num">
                  {{ planTaskVal }}<span class="span_1">个</span>
                </div>
                <div class="task_num_name">常备方案</div>
              </div>

              <!-- <div class="right">
                <div class="task_one">
                  一级加强<span>{{
                    getNum(taskStatic.taskNumDetail, "一级加强")
                  }}</span>
                </div>
                <div class="other_num">
                  <div class="other_data">
                    一级任务<span class="span1">{{
                      getNum(taskStatic.taskNumDetail, "一级任务")
                    }}</span>
                  </div>
                  <div class="other_data">
                    二级任务<span class="span2">{{
                      getNum(taskStatic.taskNumDetail, "二级任务")
                    }}</span>
                  </div>
                </div>
                <div class="other_num">
                  <div class="other_data">
                    三级任务<span class="span3">{{
                      getNum(taskStatic.taskNumDetail, "三级任务")
                    }}</span>
                  </div>
                  <div class="other_data">
                    保卫任务<span class="span4">{{
                      getNum(taskStatic.taskNumDetail, "保卫任务")
                    }}</span>
                  </div>
                </div>
              </div> -->
            </div>
            <div class="tabs_content">
              <div class="tabs_btn" :class="{ active_btn: tanbsName === 'task' }" @click="changTabs('task')">
                任务预案
              </div>
              <div class="tabs_btn" :class="{ active_btn: tanbsName === 'plan' }" @click="changTabs('plan')">
                常备方案
              </div>
              <div class="tabs_btn" :class="{ active_btn: tanbsName === 'emergency' }" @click="changTabs('emergency')">
                应急预案
              </div>
            </div>
          </div>
          <div class="task_plan_box">
            <div style="width: 100%; height: 100%" v-if="tanbsName === 'task'">
              <div class="search_box">
                <div class="left_input">
                  <el-input v-model="fileter" placeholder="搜索" clearable size="default" />
                </div>
                <div class="right_creat" @click="add">创建</div>
              </div>
              <div class="item_box">
                <div class="importantPoint" v-for="(item, index) in filteredList" :key="index" @click="goToInfo(item.id)">
                  <div class="top_title">
                    <div class="left_index">{{ index + 1 }}</div>
                    <div class="right_name">{{ item.taskName }}</div>
                    <div class="del" @click.stop="play(item)" title="播放" style="margin-right: 8px">
                      <el-icon>
                        <VideoPlay />
                      </el-icon>
                    </div>
                    <div class="del" @click.stop="updateTask(item)" title="修改" style="margin-right: 8px">
                      <el-icon>
                        <Edit />
                      </el-icon>
                    </div>
                    <div class="del" @click.stop="deleteTaskFun(item)" title="删除" style="margin-right: 8px">
                      <el-icon>
                        <Delete />
                      </el-icon>
                    </div>
                    <div class="del" @click.stop="downloadLine(item)" title="导出路线">
                      <el-icon><Download /></el-icon>
                    </div>
                  </div>
                  <div class="center_text">
                    <div class="items_label">
                      任务等级：
                      <div class="right_icon" :class="item.taskLevel === '一级加强'
                          ? 'yjjq'
                          : item.taskLevel === '一级任务'
                            ? 'yjrw'
                            : item.taskLevel === '二级任务'
                              ? 'ejrw'
                              : item.taskLevel === '三级任务'
                                ? 'sjrw'
                                : 'bwrw'
                        ">
                        <div class="point"></div>
                        <div class="text">{{ item.taskLevel }}</div>
                      </div>
                    </div>
                    <div class="items_label">
                      开始时间：
                      <div class="right_icon">{{ item.taskStartTime }}</div>
                    </div>
                    <div class="items_label">
                      结束时间：
                      <div class="right_icon">{{ item.taskEndTime }}</div>
                    </div>
                    <!-- <div class="items_label">
                  责任人：
                  <div class="right_icon">{{ item.head }}</div>
                </div> -->
                  </div>
                </div>
              </div>
            </div>
            <!-- 常备方案 -->
            <PlanTask v-if="tanbsName === 'plan'" />
             <!-- 应急预案库 -->
            <EmergencyPlan v-if="tanbsName === 'emergency'" />
          </div>
        </div>
      </div>
    </transition>
    <!-- 添加任务弹框 -->
    <addTaskDialog :open="showDialog" @hideDialog="changeCotrlFun" :itemInfo="updataData" :type="dialogType" />

    <!-- 添加任务场景 -->
    <!-- <addTaskScreen v-if="showTask" /> -->

    <!-- 迭代新模块 -->
    <taskScreen v-if="showTask" />
  </div>

  <!-- 任务整体推演 -->
  <TaskPlay v-if="showPlayTour" @out="cleaOut" />

  <!-- 任务场景一张图 -->
  <OnePicture v-if="showPicture" />

 

  <!-- 常备方案页面 -->
  <PlanTaskPage v-if="curPage" />

  <!-- 常备漫游播放页面 -->
  <PlanRoamPage v-if="roamPage" />

  <!-- marker信息 -->
  <InfoDialog v-if="showMarkerInfo" />

    <!-- 预案详情 -->
  <EmergencyInfo v-if="infoBol"/>
</template>

<script setup>
import { initTools } from "@/components/SmartMap/js/addPlot";
import { drawGsGtData, addTourResouce } from "@/components/SmartMap/js/utils";
import { drawResourceDataAll } from '@/components/SmartMap/js/resource'
import { queryTaskList, queryTaskInfo, deleteTask,downLoadTaskLines } from "@/api/task/index";
import {
  getTaskStatic,
  getReourceDataForScene,
  getDrawDataForScreen,
} from "@/api/task/task";
import { getPlanNumber } from "@/api/plan";
import { searchScreenForId } from "@/api/task/new";
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  reactive,
  watch,
  getCurrentInstance,
} from "vue";
import { ElMessageBox,ElMessage } from "element-plus";
import { flattenTreeData, loadPicture } from "@/utils";
import useSettingStore from "@/store/modules/settingStore";
import useCameraStore from "@/store/modules/cameraSet";
import useTaskStore from "@/store/modules/taskStore";
import usePlanTaskStore from "@/store/modules/planTask";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import addIcon from "@/assets/panel/Plus_add.png";
import addTaskDialog from "./componenets/addTask.vue";
// import addTaskScreen from './componenets/setCameraTour/index.vue'
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import taskScreen from "./componenets/taskNew/index.vue";
import TaskPlay from "./componenets/taskNew/taskPlay.vue";
import useScreenStore from "@/store/modules/screenStore";
import OnePicture from "./componenets/taskNew/onePicture.vue";
import PlanTask from "./componenets/taskNew/PlanTask.vue";
import PlanRoamPage from './componenets/taskNew/roamPage.vue'
import InfoDialog from '@/views/WorkCockpit/taskDetail/components/InfoDialog.vue'
// 应急预案库
import EmergencyPlan from "@/views/EmergencyPlan/index.vue";
import EmergencyInfo from '@/views/EmergencyPlan/components/PlanDetail.vue'
// 常备方案页面
import PlanTaskPage from "./componenets/taskNew/PlanTaskPage.vue";
import { useRoute } from "vue-router";
const route = useRoute();
const { proxy } = getCurrentInstance();
const WorkCockpitStore = useWorkCockpitStore()
const SettingStore = useSettingStore();
const cameraStore = useCameraStore();
const taskStore = useTaskStore();
const planTaskStore = usePlanTaskStore();
const EmergencyStore = useEmergencyStore()
let showTask = computed(() => cameraStore.taskInfoBool);
let showPanle = computed(() => SettingStore.showTaskPanle);
const clearModal = computed(() => taskStore.clearModal);
let curPage = computed(() => planTaskStore.planPage);
let roamPage = computed(() => planTaskStore.roamPage)
let showPicture = computed(() => useScreenStore().showPicture);
let showPlayTour = computed(() => useScreenStore().showPlay);
let planCheckInfo = computed(() => planTaskStore.checkInfo)
let planDetails = computed(() => planTaskStore.checkDetails)
// 详情信息数据
let taskInfo = computed(() => taskStore.taskInfo);
const showMarkerInfo = computed(() => useWorkCockpitStore().showMarkerInfo)
let showEmergencyPlan = computed(() => useEmergencyStore().showEmergencyPlan);
let infoBol = computed(()=>EmergencyStore.infoBol)
const tanbsName = ref("task");
const changTabs = (tab) => {
  tanbsName.value = tab;
};
// 输出：123
// if (route.query.id && route.query.sceneName && route.query.type) {
//   let item = route.query;

//   planTaskStore.set_planTaskInfo(item);
//   planTaskStore.setCurPage(true);
//   SettingStore.setShowTool(true);
//   WorkCockpitStore.set_showResouce(false);
//   WorkCockpitStore.set_checkBox("init", false);
//   // 设之场景面板弹框所需信息
//   taskStore.SET_SCREENMODALINFO(item);
//   // 设置场景信息
//   useScreenStore().set_screenInfo(item);
//   // 设置场景能否编辑操作
//   useScreenStore().set_editScreen(true);
//   setTimeout(()=>{
//     SettingStore.setShowTaskPanle(false);
//     SettingStore.setshowTaskFun(false);
//   },500)
// }

watch(() => planCheckInfo.value, val => {
  if (val) {
    if (val.id && val.sceneName && val.type) {
      let item = val;

      planTaskStore.set_planTaskInfo(item);
      planTaskStore.setCurPage(true);
      SettingStore.setShowTool(true);
      WorkCockpitStore.set_showResouce(false);
      WorkCockpitStore.set_checkBox("init", false);
      // 设之场景面板弹框所需信息
      taskStore.SET_SCREENMODALINFO(item);
      // 设置场景信息
      useScreenStore().set_screenInfo(item);
      // 设置场景能否编辑操作
      useScreenStore().set_editScreen(true);
      setTimeout(() => {
        SettingStore.setShowTaskPanle(false);
        SettingStore.setshowTaskFun(false);
      }, 500)
    }

  }
}, { immediate: true, deep: true });
watch(() => planDetails.value, val => {
  if (val) {
    changTabs('plan')
  }
}, { deep: true, immediate: true })
let filteredList = computed(() => {
  if (taskList.value.length > 0) {
    return taskList.value.filter((item) => {
      if (fileter.value) {
        return item.taskName
          .toLowerCase()
          .includes(fileter.value.toLowerCase());
      } else {
        return item;
      }
    });
  } else {
    return [];
  }
});
const updataData = ref({});
const dialogType = ref("add");
const fileter = ref("");
const showPlay = ref(false);
const taskStatic = ref({});
const planTaskVal = ref(0);
watch(
  () => showPanle,
  (val) => {
    if (val) {
      if (val.value) {
        queryTaskList({}).then((res) => {
          if (res.code === 0) {
            taskList.value = res.data;
          } else {
            taskList.value = [];
          }
        });
        getTaskStatic().then((res) => {
          taskStatic.value = res.data;
        });
        getPlanNumber().then((res) => {
          planTaskVal.value = res.data;
        });
      }
    }
  },
  { immediate: true, deep: true }
);
let taskInfoBol = computed(() => SettingStore.showTask);
const taskList = ref([]);
const showDialog = ref(false);

const changeCotrlFun = (e) => {
  // updataData.value = {}
  if (e === "edit") {
    showDialog.value = false;
    queryTaskList({}).then((res) => {
      if (res.code === 0) {
        taskList.value = res.data;
      } else {
        taskList.value = [];
      }
    });
  } else {
    if (e) {
      goToInfo(e);
    } else {
      showDialog.value = false;
    }
  }
};
const getNum = (arr, text) => {
  let num;
  if (arr && arr.length > 0) {
    arr.forEach((item) => {
      if (item.tasklevel === text) {
        num = item.num;
      }
    });
    return num;
  }
};
// 播放整个任务
const play = (item) => {
  searchScreenForId({ taskId: item.id }).then((res) => {
    if (res.code === 0) {
      if (res.data.length > 0) {
        taskStore.SET_TASKVIDEOLIST(res.data);
        getDrawDataForScreen({ sceneId: res.data[0].id }).then((res1) => {
          useScreenStore().set_showPlay(true);
          SettingStore.setShowTaskPanle(false);
          SettingStore.set_isClickTools(true);
          taskStore.set_taskDrawData(res1.data);
          drawToursForReasuce(res.data[0].id);
        });
      } else {
        proxy.$modal.msgWarning("暂无推演数据");
      }
    }
  });
};
// 到处任务路线
const downloadLine = async(item) => {
    downLoadTaskLines({taskId:item.id,sceneId:""}).then((res)=>{
      // 生成Blob对象  
    const blob = new Blob([res], { type: 'application/zip' });  
    const url = window.URL.createObjectURL(blob);  

    // 创建下载链接  
    const link = document.createElement('a');  
    link.href = url;  
    link.download = `${item.taskName}-路线.zip`;  
    document.body.appendChild(link);  
    link.click();  
    // 清理资源  
    document.body.removeChild(link);  
    window.URL.revokeObjectURL(url);  
    ElMessage.success('下载完成，请查看浏览器下载列表')
    })
}
// 绘制漫游编辑以及漫游预览的资源要素
const drawToursForReasuce = async (sceneId) => {
  getReourceDataForScene({ sceneId }).then((res) => {
    WorkCockpitStore.set_basicIds(sceneId, 1)
    let treeData = res.data || [];
    let treeLayer = flattenTreeData(treeData[0].children);
    // drawMarkers(fileterNode)
    let arr = treeLayer.filter((item) => item.coordinates);
    let fileterNode = arr.filter((item) => !item.floorNum);
    drawResourceDataAll(fileterNode, true);
    //   let g = window.__g
    // let markerarr = []
    // for (const item of arr) {
    //   let o = {
    //     id: item.id,
    //     groupId: 'resource',
    //     coordinate: [item.coordinates[0],item.coordinates[1],0], //坐标位置
    //     coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    //     anchors: [-34.5, 85],
    //     range: [0, 150000], //可视范围
    //     // imageSize: [50, 56], //图片的尺寸,//图片的尺寸
    //     imagePath: loadPicture(`./images/resource/${item.type}.png`),
    //     fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    //     text: item.name, //显示的文字
    //     useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    //     textRange: [1, 15], //文本可视范围[近裁距离, 远裁距离]
    //     textOffset: [0, 0], // 文本偏移
    //     textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
    //     fontSize: 12, //字体大小
    //     fontOutlineSize: 1, //字体轮廓线大小
    //     fontColor: Color.White, //字体颜色
    //     fontOutlineColor: Color.Black, //字体轮廓线颜色
    //     autoHeight: true, // 自动判断下方是否有物体
    //     displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
    //     priority: 0, //避让优先级
    //     occlusionCull: true, //是否参与遮挡剔除
    //     clusterByImage: true
    //   };
    //   markerarr.push(o)
    // }
    // if (markerarr.length > 0) {
    //    g.marker.add(markerarr)
    //   // g.marker.focusAll()
    // }
  });
};
// 退回
const cleaOut = () => {
  // showPlay.value = false;
  useScreenStore().set_showPlay(false);
  if (showPanle.value) {
    SettingStore.setShowTaskPanle(true);
  }
  if (taskInfo.value && taskInfo.value.viewData) {
    let g = window.__g;
    g.camera.set(taskInfo.value.viewData.camera, 0.5);
  }
  // let g = window.__g;
  // g.reset(4);
};
// 修改任务信息
const updateTask = (item) => {
  dialogType.value = "edit";
  updataData.value = item;
  showDialog.value = true;
};
// 删除任务
const deleteTaskFun = (item) => {
  ElMessageBox.confirm("您确定要删除该任务?", "提示", {
    confirmButtonText: "确认",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      deleteTask({ id: item.id }).then((res) => {
        if (res.code === 0) {
          proxy.$modal.msgSuccess("删除成功！");
          queryTaskList({}).then((res) => {
            if (res.code === 0) {
              taskList.value = res.data;
            } else {
              taskList.value = [];
            }
          });
        }
      });
    })
    .catch(() => {
      console.log("已取消");
    });
};
// 点击进入详情页面
const goToInfo = (e) => {
  queryTaskInfo({ id: e }).then((res) => {
    if (res.code === 0) {
      // initTools()
      console.log(res.data)
      if (res.data.viewData) {
        let g = window.__g;
        g.camera.set(res.data.viewData.camera, 0.03);
      }
      SettingStore.setShowTool(false);
      showDialog.value = false;
      cameraStore.setTaskInfo(res.data);
      console.log('aaaaaaaaaaaaa',res.data)
      taskStore.SET_TASKINFO(res.data);
      SettingStore.setShowTaskPanle(false);
      cameraStore.setShowTaskInfo(true);
      SettingStore.setshowTaskFun(false);
      SettingStore.set_isClickTools(true);
    }
  });
};
const add = () => {
  updataData.value = {};
  dialogType.value = "add";
  showDialog.value = true;
};
const toggleLength = () => {
  SettingStore.setShowTaskPanle();
};
onMounted(() => {
  SettingStore.setshowTaskFun(true);
  SettingStore.setShowTaskPanle(true);
  cameraStore.setShowTaskInfo(false);
  // setTimeout(()=>{
  // window.__g.infoTree.hide('8C6F67F84F71F11130C1BEACD0BAF612')
  // window.__g.infoTree.hide('A3B755BF43736C15455300A0FB44761F')
  // },500)
  // window.__g.polygon3d.clear();// 清除面
  // queryTaskList({}).then(res=>{
  //   if(res.code===0){
  //     taskList.value = res.data
  //   }else{
  //     taskList.value = []
  //   }
  // })
});
onBeforeUnmount(() => {
  window.__g.infoTree.show("8C6F67F84F71F11130C1BEACD0BAF612");
  window.__g.infoTree.show("A3B755BF43736C15455300A0FB44761F");
  window.__g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
  drawGsGtData(1);
});
onUnmounted(() => {
  window.__g.infoTree.show("8C6F67F84F71F11130C1BEACD0BAF612");
  window.__g.infoTree.show("A3B755BF43736C15455300A0FB44761F");
  window.__g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
  drawGsGtData(1);
});
</script>

<style lang="scss" scoped>
.taskStart_content {
  .left_title_shrink {
    cursor: pointer;
    position: absolute;
    width: 30px;
    height: 120px;
    top: 50%;
    left: 10px;
    transform: translate(0, -50%);
    z-index: 5;
    background: rgba(4, 35, 84, 1);
    border-radius: 8px;
    color: #ffffff;
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .shrink {
    position: absolute;
    right: -20px;
    top: 50%;
    z-index: 10;
    width: 10px;
    height: 20px;
    border: 1px solid cyan;
    border-left: none;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    cursor: pointer;
    transform: translate(0, -50%);

    .to_left {
      width: 0;
      height: 0;
      border-top: 7px solid transparent;
      border-bottom: 7px solid transparent;
      border-right: 6px solid cyan;
      animation: flow 0.8s infinite alternate;
    }
  }

  .shrink_hide {
    position: absolute;
    left: 40px;
    top: 50%;
    z-index: 10;
    width: 10px;
    height: 20px;
    border: 1px solid cyan;
    border-right: none;
    display: flex;
    align-items: center;
    justify-content: flex-start;
    cursor: pointer;
    transform: translate(0, -50%);

    .to_left_hide {
      width: 0;
      height: 0;
      border-top: 7px solid transparent;
      border-bottom: 7px solid transparent;
      border-left: 6px solid cyan;
      animation: flowHide 0.8s infinite alternate;
    }
  }

  @keyframes flow {
    from {
      margin-right: 1px;
    }

    to {
      margin-right: 16px;
    }
  }

  @keyframes flowHide {
    from {
      margin-left: 1px;
    }

    to {
      margin-left: 16px;
    }
  }

  /* 使用 @keyframes 定义过渡效果 */
  @keyframes slide {
    0% {
      width: 0px;
    }

    /* 打开时宽度从0开始 */
    100% {
      width: 400px;
    }

    /* 打开时宽度变为400 */
  }

  @keyframes slideReverse {
    0% {
      width: 400px;
    }

    /* 关闭时宽度从400开始 */
    100% {
      width: 0px;
    }

    /* 关闭时宽度变为0 */
  }

  .slide-enter-active,
  .slide-leave-active {
    animation: slide 0.1s forwards;
    /* 应用定义的动画 */
  }

  .slide-leave-active {
    animation-direction: reverse;
    /* 设置动画反向播放 */
  }

  .left_box {
    position: absolute;
    z-index: 3;
    transform: scale(0.8);
    left: -2%;
    bottom: -50px;
    top: -20px;
    width: 400px;
    background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
    display: flex;
    flex-direction: column;
    transition: width 0.1s;

    /* 添加过渡效果 */
    .heard {
      height: 48px;
      background: url("../assets/panel/panel_bg.png") no-repeat;
      background-size: 100% 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-family: YouSheBiaoTiHei;
      font-weight: 400;
      font-size: 24px;
      color: #ffffff;
      text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
      font-style: normal;
    }

    .content {
      flex: 1;
      padding: 16px;
      overflow: hidden;
      padding-top: 12px;

      .item_box {
        // height: calc(100% - 160px);
        // overflow: auto;
        height: calc(100% - 50px);
        overflow: auto;

        .importantPoint {
          cursor: pointer;
          width: 100%;
          background: rgba(0, 0, 0, 0.3);
          border-radius: 12px 12px 12px 12px;
          margin-bottom: 12px;
          height: 192px;
          font-family: PingFang SC;
          font-weight: 600;
          font-size: 14px;
          color: #ffffff;
          display: flex;
          flex-direction: column;

          .top_title {
            display: flex;
            margin: 0px 16px 8px 16px;
            padding-top: 16px;
            align-items: center;

            .left_index {
              width: 24px;
              height: 24px;
              background: linear-gradient(162deg, #274eef 0%, #0529bf 100%);
              border-radius: 50%;
              text-align: center;
              line-height: 24px;
              margin-right: 4px;
            }

            .right_name {
              flex: 1;
              height: 32px;
              background: linear-gradient(90deg,
                  #004adb 0%,
                  rgba(0, 40, 117, 0) 100%);
              border-radius: 4px 8px 8px 4px;
              padding-left: 8px;
              text-align: left;
              line-height: 32px;
            }
          }

          .center_text {
            flex: 1;
            display: flex;
            flex-direction: column;
            padding-left: 52px;
            padding-bottom: 6px;

            .items_label {
              flex: 1;
              display: flex;
              align-items: center;
              font-family: PingFangSC;
              font-weight: 400;
              font-size: 14px;
              color: rgba(255, 255, 255, 0.7);
              text-align: left;

              .right_icon {
                display: flex;
                align-items: center;
                // color: #fff;

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

      .addTask {
        background: rgba(0, 0, 0, 0.24);
        border-radius: 4px 4px 4px 4px;
        border: 1px solid rgba(80, 170, 255, 0.23);
        margin: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        height: 56px;
      }

      .taskcount {
        .top_box {
          display: flex;
          align-items: center;
          height: 100px;

          .left {
            // width: 40%;
            flex: 1;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            .data_num {
              font-family: DIN;
              font-size: 40px;
              font-weight: 500;
              line-height: 32px;
              letter-spacing: 0px;
              font-variation-settings: "opsz" auto;
              color: #00ceff;
            }

            .span_1 {
              font-size: 14px;
              margin-left: 4px;
              color: #fff;
            }

            .task_num_name {
              margin-top: 20px;
              font-family: YouSheBiaoTiHei;
              font-size: 18px;
              font-weight: normal;
              line-height: 18px;
              letter-spacing: 0px;
              color: #fff;
            }
          }

          .right {
            flex: 1;

            .task_one {
              display: flex;
              align-items: center;
              justify-content: center;
              font-family: Source Han Sans;
              font-size: 16px;
              font-weight: bold;
              line-height: 14px;
              letter-spacing: 0em;
              color: #fff;

              span {
                font-family: DIN;
                font-size: 20px;
                font-weight: bold;
                line-height: normal;
                letter-spacing: 0px;
                color: #ff4040;
                margin-left: 6px;
              }
            }

            .other_num {
              display: flex;
              margin-top: 16px;

              .other_data {
                flex: 1;
                font-family: Source Han Sans;
                font-size: 14px;
                font-weight: normal;
                line-height: 14px;
                letter-spacing: 0em;
                color: #ffffff;

                span {
                  font-family: DIN;
                  font-size: 18px;
                  font-weight: 500;
                  line-height: normal;
                  letter-spacing: 0px;
                  margin-left: 16px;
                }

                .span1 {
                  color: #ff6633;
                }

                .span2 {
                  color: #ffe433;
                }

                .span3 {
                  color: #33ff77;
                }

                .span4 {
                  color: #00a6ff;
                }
              }
            }
          }
        }

        .tabs_content {
          display: flex;
          align-items: center;
          justify-content: center;

          .tabs_btn {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 40px;
            cursor: pointer;
            box-sizing: border-box;
            border: 1px solid;
            background: #033683;
            border: 1px solid rgba(38, 68, 173, 0.3);
          }

          .active_btn {
            background: #2e5aff;
          }
        }
      }
    }
  }
}

.task_plan_box {
  height: calc(100% - 140px);

  // overflow: auto;
  .search_box {
    display: flex;
    align-items: center;
    margin: 16px 0;

    .left_input {
      flex: 1;
    }

    .right_creat {
      width: 84px;
      height: 32px;
      background: #274eef;
      color: #fff;
      text-align: center;
      line-height: 32px;
      margin-left: 10px;
      font-size: 12px;
      font-weight: 500;
      letter-spacing: 0em;
      cursor: pointer;
    }
  }
}
</style>
