<template>
  <div class="taskPlan_box">
    <div class="heard">
      招商详情
      <div class="close_modal" @click="closeModal"></div>
    </div>
    <div class="plan_box">
      <div class="screen_box" style="position: relative;">
        <div class="title_name">{{ objInfo.title }}</div>
        <div class="task_box">
          <div class="item_box">
            <div class="left">公司名称：</div>
            <div class="right ">
             {{ objInfo.name }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">所属行业：</div>
            <div class="right">{{ objInfo.industry }}</div>
          </div>
          
          <div class="item_box">
            <div class="left">位&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;置：</div>
            <div class="right">{{ objInfo.positions.join(';') }}</div>
          </div>
          <div class="item_box">
            <div class="left">负责人：</div>
            <div class="right">
              {{ objInfo.head }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">联系电话：</div>
            <div class="right">
              {{ objInfo.phone }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">签约时间：</div>
            <div class="right">
              {{ objInfo.signingTime }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">是否入住：</div>
            <div class="right">
              {{ objInfo.settledFlag===1?'是':'否' }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">是否排查：</div>
            <div class="right">
              {{ objInfo.checkedFlag===1?'是':'否' }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import {
  computed,
  getCurrentInstance,
  onBeforeUnmount,
  onUnmounted,
  onMounted,
  ref,
  watch
} from "vue";
import useFloorStore from '@/store/modules/floorStore'
import { closeFloors } from "@/components/SmartMap/js/utils";
import { clearDraw, drawContrlData } from "./util";
import useTaskStore from "@/store/modules/taskStore";
import useScreenStore from "@/store/modules/screenStore";
import useSettingStore from "@/store/modules/settingStore";
import useCameraStore from "@/store/modules/cameraSet";
import { searchNodePlanToScreen, saveScreenPlan, updateScreen } from "@/api/task/task";
import { ElMessage, ElMessageBox } from "element-plus";
import { getFloorsNoBoomDataApi } from "@/api/basic/index";
let taskInfo = computed(() => useTaskStore().taskInfo); // 任务信息信息
const { proxy } = getCurrentInstance();
const screenInfo = computed(() => useTaskStore().screenModalInfo);
const roomCode = computed(()=>useTaskStore().roomCode)
const houseBol = ref(false)
let objInfo = ref({
    "id": "",
    "name": "",
    "industry": "",
    "productType": "",
    "head": "",
    "phone": "",
    "settledFlag": "",
    "signingTime": "",
    "positions": [],
    "title": ""
})
watch(
  () => roomCode.value,
  (val) => {
   getFloorsNoBoomDataApi({roomCode:val}).then(res=>{
    objInfo.value = res.data
  })
  },
  { deep: true }
);
onMounted(() => {
  getFloorsNoBoomDataApi({roomCode:roomCode.value}).then(res=>{
    objInfo.value = res.data
  })
});
// 关闭面板
const closeModal = () => {
  useTaskStore().set_attractBol(false);
  useTaskStore().set_roomCode('');
  // useTaskStore().set_communityBol(false);
  // const taskLevel = useTaskStore().taskInfo.taskLevel
  // // 显示详情面板
  // taskLevel === '一级加强' ? useTaskStore().SET_SCREENPLAN(true) : useTaskStore().set_sceneBol(true)
  // useWorkCockpitStore().setShowSceenRightCard(true)
  // console.log('显示')
  // useEmergencyStore().setDetailsPage(true) // 预案详情页
  // emitter.emit("barChange", true)
};
onBeforeUnmount(() => {
});
onUnmounted(() => {
});
</script>
<style lang="scss" scoped>
.taskPlan_box {
  // position: absolute;
  // transform: scale(0.8);
  // z-index: 3;
  // right: -1.6%;
  // bottom: -50px;
  // top: -32px;
  // width: 400px;
  // background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
  // display: flex;
  // flex-direction: column;
  position: absolute;
    transform: scale(0.8);
    z-index: 300;
    right: 18%;
    top: 45px;
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
  }

}

.plan_box {
  flex: 1;
  overflow: auto;
  padding: 16px 20px;

  .screen_box {
    padding: 20px 28px;
    background: rgba(0, 12, 78, 0.5);

    .taskViewCoodr {
      position: absolute;
      top: 14px;
      right: 20px;
      width: 80px;
      height: 32px;
      background: #274EEF;
      text-align: center;
      line-height: 32px;
      text-align: center;
      font-family: Source Han Sans;
      font-size: 14px;
      font-weight: 500;
      color: #fff;
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
      min-height: 30px;

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
        word-break: break-all;
        // overflow: hidden;
        // text-overflow: ellipsis;
        /* 超出部分省略号 */
        // word-break: break-all;
        /* break-all(允许在单词内换行。) */
        // display: -webkit-box;
        /* 对象作为伸缩盒子模型显示 */
        // -webkit-box-orient: vertical;
        /* 设置或检索伸缩盒对象的子元素的排列方式 */
        // -webkit-line-clamp: 2;
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
    }
  }
}
</style>

