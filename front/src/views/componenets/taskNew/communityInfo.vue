<template>
  <div class="taskPlan_box">
    <div class="heard">
      社区详情
      <div class="close_modal" @click="closeModal"></div>
    </div>
    <div class="plan_box">
      <div class="screen_box" style="position: relative;">
        <div class="title_name" @click="houseBol = true">{{ InfoData.buildingName }}</div>
        <div class="task_box">
          <div class="item_box">
            <div class="left">所属社区：</div>
            <div class="right ">
             {{ InfoData.communityName }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">所属小区：</div>
            <div class="right">{{ InfoData.estateName }}</div>
          </div>
          <div class="item_box">
            <div class="left">地&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;址：</div>
            <div class="right">{{ InfoData.buildingAddress }}</div>
          </div>
          <div class="item_box">
            <div class="left">居住人数：</div>
            <div class="right">
              {{ InfoData.householdRsNumber }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">户&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数：</div>
            <div class="right" style="display: flex;">
              <div> {{ InfoData.householdNumber }}</div>
              <div class="text1" v-if="InfoData.focusNumber>0" style="background: #E0655C;height: 24px;">{{ InfoData.focusNumber }}名重点人员</div>
              <div class="text2" v-else>无重点人员</div>
            </div>
          </div>
        </div>
      </div>
      <div class="search_box">
        <div class="left_text">楼层单元号</div>
        <el-select v-model="unitVal" size="large" style="width:250px;margin-left: 10px;" @change="changeSelect"><el-option v-for="item in InfoData.unitNumber"
            :key="item" :label="item" :value="item" />
        </el-select>
      </div>
      <div class="floorNumBox">
        <div class="floor_item" v-for="(item,index) in  Object.values(InfoData.houseData[unitVal]).reverse()" >
          <div style="display: flex;align-items: center;">
            <div class="child_item" v-for="(child,idx) in item" @click="choseFloors(child)" :class="{'active_item':selectVal===child.houseNumber}">
              <div class="item_list" :style="{'color':child.focusNumber>0?'#E0655C':'#fff'}">{{ child.houseNumber }}</div>
            <div class="important_people" v-if="child.focusNumber>0"></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="houseInfo" v-if="houseBol">
    <div class="heard">
      住户详情
      <div class="close_modal" @click="closeModalChild"></div>
    </div>
    <div class="plan_box">
      <div class="screen_box" style="position: relative;">
        <div class="title_name">{{ detailsInfo.title }}</div>
        <div class="task_box">
          <div class="item_box">
            <div class="left">户&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;主：</div>
            <div class="right">
              {{ detailsInfo.ownerUserName }}
            </div>
          </div>
          <div class="item_box">
            <div class="left">身份证号：</div>
            <div class="right">{{ detailsInfo.ownerUserCardCode }}</div>
          </div>
          <div class="item_box">
            <div class="left">联系方式：</div>
            <div class="right">{{ detailsInfo.ownerUserTel }}</div>
          </div>
          <div class="item_box">
            <div class="left">房屋类型：</div>
            <div class="right">{{ detailsInfo.roomType }}</div>
          </div>
          <!-- <div class="item_box">
            <div class="left">住宅面积：</div>
            <div class="right">
              {{  detailsInfo.roomArea}}㎡
            </div>
          </div> -->
          <div class="item_box">
            <div class="left">常住人数：</div>
            <div class="right">
              {{ detailsInfo.perNumber }}
            </div>
          </div>
          <!-- <div class="item_box">
            <div class="left">是否排查：</div>
            <div class="right">
              {{ detailsInfo.checkedFlag===1?'是':'否' }}
            </div>
          </div> -->
        </div>
      </div>
      <div class="floorNumBoxInfo">
        <el-collapse v-model="activeName" accordion >
          <el-collapse-item  :name="item.id" v-for="(item, index) in detailsInfo.residenceInfoList" :key="index">
            <template #title>
              <div style="display: flex;align-items: center;">
                {{ item.name }} 
                <div class="important_people" v-if="item.focusFlag"></div>
                <span style="margin-left: 30px;">{{ item.tel!=='null'?item.tel:'' }}</span>
              </div>
          
        </template>
            <div class="collapse_box">
              <div class="item">
                <div class="left">性别</div>
                <div class="right">{{ item.sex }}</div>
              </div>
              <div class="item">
                <div class="left">身份证</div>
                <div class="right">{{ item.identityCard }}</div>
              </div>
              <div class="item">
                <div class="left">民族</div>
                <div class="right">{{ item.nationName }}</div>
              </div>
              <div class="item">
                <div class="left">户籍详情</div>
                <div class="right">{{ item.registerAddress }}</div>
              </div>
              <div class="item">
                <div class="left">学历</div>
                <div class="right">{{ item.educationName }}</div>
              </div>
              <div class="item">
                <div class="left">服务场所</div>
                <div class="right">{{ item.workUnit!=='null'?item.workUnit:'' }}</div>
              </div>
              <div class="item">
                <div class="left">就业状态</div>
                <div class="right">{{ item.workStatusName }}</div>
              </div>
              <!-- <div class="item">
                <div class="left">就业详情</div>
                <div class="right">{{ item.workUnit!=='null'?item.workUnit:'' }}</div>
              </div>
              <div class="item">
                <div class="left">就业状态</div>
                <div class="right">{{ item.workUnit!=='null'?item.workUnit:'' }}</div>
              </div> -->
              <div class="item" v-if="item.focusFlag">
                <div class="left">重点关注</div>
                <div class="right" style="color: #E0655C;">{{ item.focusFlag }}</div>
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
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
  nextTick
} from "vue";
import useTaskStore from "@/store/modules/taskStore";
import { ElMessage } from "element-plus";
import { getCommunityUserInfo } from "@/api/basic/index";
let InfoData = computed(() => useTaskStore().communityInfo); // 居民楼信息信息
const { proxy } = getCurrentInstance();
const houseBol = ref(false)
const activeName = ref('')
const selectVal = ref(0)
const unitVal = ref(1)
let detailsInfo = ref({})
onMounted(() => {
  console.log(InfoData.value.houseData[unitVal])
});
// 关闭面板
const closeModal = () => {
  useTaskStore().set_communityBol(false);
  // const taskLevel = useTaskStore().taskInfo.taskLevel
  // // 显示详情面板
  // taskLevel === '一级加强' ? useTaskStore().SET_SCREENPLAN(true) : useTaskStore().set_sceneBol(true)
  // useWorkCockpitStore().setShowSceenRightCard(true)
  // console.log('显示')
  // useEmergencyStore().setDetailsPage(true) // 预案详情页
  // emitter.emit("barChange", true)
};
const changeSelect = () => {
  selectVal.value = 0
    houseBol.value = false
}
const choseFloors = (item) => {
  if(selectVal.value!==item.houseNumber){
    selectVal.value = item.houseNumber
  let params = {
    houseId: item.id
  }
  getCommunityUserInfo(params).then(res=>{
    if(res.data){
      detailsInfo.value = res.data
    nextTick(()=>{
      houseBol.value = true
    })
    }else{
      ElMessage.warning('暂无信息')
    }

  })
  }else{
    selectVal.value = 0
    houseBol.value = false
  }

}
const closeModalChild = () => {
  selectVal.value = 0
  houseBol.value = false
}
onBeforeUnmount(() => {
});
onUnmounted(() => {
});
</script>
<style lang="scss" scoped>
.taskPlan_box {
  position: absolute;
  transform: scale(0.8);
  z-index: 3;
  right: 360px;
  bottom: 20%;
  top: 0px;
  z-index: 100;
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

  :deep(.search_box) {
    display: flex;
    align-items: center;

    .left_text::before {
      content: '*';
      color: red;
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

  .floorNumBox {
    width: 100%;
    height: calc(100% - 280px);
    overflow-y: auto;
    padding-top: 16px;
    // column-count: 3; /* 将 div 元素分为三列 */
    .floor_item{
    break-inside: avoid; /* 避免在元素内部断行 */
    margin-bottom: 10px; /* 每个元素下方的间隔 */
    // background-color: #f0f0f0; /* 背景颜色，便于观察 */
    }
    .child_item{
    padding: 10px; /* 内边距 */
    box-sizing: border-box; /* 确保内边距和边框包含在宽度内 */
    display: flex;
    align-items: center;
    justify-content: center;
    background: url('@/assets/basic/communityNum.png') no-repeat;
    background-size: 100% 100%;
    cursor: pointer;
    flex: 1;
    margin-right: 4px;
    }
    .active_item{
    background: url('@/assets/basic/community_active.png') no-repeat;
    background-size: 100% 100%;
    }
    .important_people{
      margin-left: 4px;
      background: url('@/assets/basic/importantP.png') no-repeat;
      background-size: 100% 100%;
      width:14.57px;
      height:18.86px;
    }
  }

  .floorNumBoxInfo {
    width: 100%;
    height: calc(100% - 250px);
    overflow-y: auto;
    margin-top: 16px;

    :deep(.el-collapse) {
      border: none;
      background: rgba(0, 12, 78, 0.5);
    }

    :deep(.el-collapse-item) {
      margin-bottom: 4px;
    }

    :deep(.el-collapse-item__header) {
      background: rgba(28, 53, 155, 0.5) !important;
      border: 0px;
      transition: none;
      height: 40px;
      border-radius: 0;
      font-size: 14px;
      color: #fff;
      line-height: 32px;
      position: relative;
      padding-left: 20px !important;
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
      padding: 10px 20px;
    }
    .collapse_box{
      .item{
        display: flex;
        align-items: center;
        .left{
          width: 80px;
          text-align: left;
        }
        .right{
          flex: 1;
        }
      }
    }
    .important_people{
      margin-left: 6px;
      background: url('@/assets/basic/importantP.png') no-repeat;
      background-size: 100% 100%;
      width:14.57px;
      height:18.86px;
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
        .text1{
          background: #E0655C;
          height: 24px;
          line-height: 24px;
          text-align: center;
          margin-left: 10px;
          color: #000000;
          min-width: 85px;
        }
        .text2{
          background: #47D756;
          width: 85px;
          height: 24px;
          line-height: 24px;
          text-align: center;
          margin-left: 10px;
          color: #000000;
        }

      }
    }
  }
}

.houseInfo {
  position: absolute;
  transform: scale(0.8);
  z-index: 100;
  right: calc(330px + 360px);
  top: 6px;
  width: 400px;
  background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
  display: flex;
  flex-direction: column;
  height: 660px;

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
</style>

