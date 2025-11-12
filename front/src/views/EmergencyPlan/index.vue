<template>
  <div class="emergency-plan" v-if="indexBol">
    <!-- <div class="header">应急预案库
      <div class="close_modal" @click="closeModal"></div>
    </div> -->
    <div class="content">
      <div class="tabsBox">
        <div class="tab_item" v-for="(item,index) in tabBtnList" :key="index" :class="{'active':item.key===activeName}" @click="getBasicData(item.key)">
          {{ item.label }}
        </div>
      </div>
      <div class="line"></div>
      <div class="listBox" style="    height: calc(100% - 70px);overflow: auto;">
        <div class="item_list" v-for="(list,idx) in list" :key="idx">
          <div class="left_num">{{ idx+1 }}</div>
          <div class="right_text">
            <div class="left">{{ list.title }}</div>
            <div class="right" @click="ckeckDetails(list)">查看详情</div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 预案详情 -->
  <!-- <EmergencyInfo v-if="infoBol"/> -->
</template>

<script setup>
import { ref, computed,onMounted} from 'vue'
// api
import {quertListForTypeId} from "@/api/basic/index";
// utils
import { closeFloors } from "@/components/SmartMap/js/utils";
import {clearDraw} from "../componenets/taskNew/util";
import {drawBasicMarker} from './utils'
// components
import EmergencyInfo from './components/PlanDetail.vue'
// store
import useEmergencyStore from '@/store/modules/emergencyPlan'
import useTaskStore from "@/store/modules/taskStore";
import useSettingStore from "@/store/modules/settingStore";
const EmergencyStore = useEmergencyStore()
const taskStore = useTaskStore()
const settingStore = useSettingStore()
let infoBol = computed(()=>EmergencyStore.infoBol)
let indexBol = computed(() =>EmergencyStore.indexBol)
// tab按钮
const tabBtnList=ref([
  {key:'gs',label:'高速'},
  {key:'gt',label:'高铁'},
  {key:'xc',label:'现场'},
  {key:'zd',label:'住地'},
])
// 按钮数据
const list = ref([])
let activeName = ref('gs')
// 退出应急预案
const closeModal = () => {
  clearDraw();// 清除标绘
  closeFloors()// 清除楼层抽出
  taskStore.SET_clearModal(true);
  EmergencyStore.set_showEmergencyPlan(false)
  settingStore.setShowTool(false);
}
// 查看详情
const ckeckDetails = (item) => {
  console.log(item)
    // taskStore.SET_clearModal(false);
    EmergencyStore.set_detailsInfo(item)
    EmergencyStore.set_indexBol(false)
    EmergencyStore.set_infoBol(true)
    // 绘制基础点位信息
    drawBasicMarker(item)
}
// 查询基础数据
const getBasicData = (type) => {
  activeName.value = type
  quertListForTypeId(type).then((res) => {
    console.log(res)
    let data = []
    res.data.forEach(item=>{
        let geos 
        if(type==='gs'||type==='gt'){
          geos = forNameGetCoord(item.data.title)
        }else{
          geos = [Number(item.data.jingdu),Number(item.data.weidu)]
        }
        let obj = {
          id:item.id,
          title:item.data.title,
          geo:geos,
          type:type
        }
        data.push(obj)
      })
    list.value = data
  });
}
// 根据名称返回坐标
const forNameGetCoord = (name) => {
  let data
  if(name==='大广高速'){
    data = [520606.43625, 4319543.04]
  }
  if(name==='京雄高速'){
    data =[495739.01656250004, 4327233.6]
  }
  if(name==='津雄高速'){
    data =[497961.71375, 4323955.5200000005]
  }
  if(name==='保津高铁'){
    data = [499823.141953125, 4325940.48]
  }
  if(name==='京广高铁'){
    data = [482113.455, 4328359.04]
  }
  if(name==='京雄高铁'){
    data = [514297.46625, 4326498.24]
  }
  return data
}
// 绘制
onMounted(()=>{
  getBasicData('gs');
  EmergencyStore.set_indexBol(true)
  EmergencyStore.set_infoBol(false)
})


</script>

<style lang="scss" scoped>
.emergency-plan {
  // width: 400px;
  background: linear-gradient(180deg, #0A1D64 0%, rgba(21, 30, 73, 0.6984) 100%);
  // position: absolute;
  // top: 0px;
  // left: -20px;
  // bottom: 0px;
  // z-index: 10;
  // transform: scale(0.8);
  margin-top: 10px;
  height: calc(100% - 3%);
  .header {
    width: 100%;
    height: 48px;
    line-height: 48px;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    background: url("@/assets/workcockpit/header_bg.png") no-repeat;
    position: relative;
    .close_modal {
      position: absolute;
      right: 16px;
      top: 50%;
      transform: translateY(-50%);
      width: 20px;
      height: 20px;
      background: url("../componenets/taskNew//img/close_new.png") no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
    }
  }

  .content {
    font-size: 14px;
    // height: calc(100% - 48px);
    height: 100%;
    padding: 20px;
    box-sizing: border-box;
    .tabsBox{
      display: flex;
      align-items: center;
      background: rgba(0, 0, 0, 0.1);
      .tab_item{
        flex: 1;
        height: 40px;
        color: #FFFFFF;
        font-size: 14px;
        font-weight: 500;
        display: flex;
        align-items: center;
        justify-content: center;
        background: url('../componenets/taskNew/img/yjya1.png') no-repeat;
        background-size: 100% 100%;
        margin-right: 6px;
        cursor: pointer;
      }
      :last-child{
        margin-right: 0px;
      }
      .active{
        background: url('../componenets/taskNew/img/yjya_active.png') no-repeat;
        background-size: 100% 100%;
      }
    }
    .line{
      width: 100%;
      height: 1px;
      opacity: 1;
      background: rgba(38, 68, 173, 0.5);
      margin: 12px 0;
    }
    .item_list{
      padding: 0 16px;
      display: flex;
      align-items: center;
      // background: url('../componenets/taskNew/img/yjya2.png') no-repeat;
      background: url('../componenets/taskNew/img/active_yjya.png') no-repeat;
      background-size: 100% 100%;
      // background: url('../componenets/taskNew/img/yjya1.png') no-repeat;
      background-size: 100% 100%;
      box-sizing: border-box;
      height: 64px;
      margin-bottom: 8px;
      .left_num{
        width: 20px;
        height: 20px;
        background: linear-gradient(219deg, #00AEFF 15%, #2474FF 86%);
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 8px;
      }
      .right_text{
        flex: 1;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .left{
          font-size: 18px;
          font-weight: bold;
          line-height: normal;
          color: #fff;
        }
        .right{
          font-size: 14px;
          font-weight: 500;
          color: #00CEFF;
          cursor: pointer;
        }
      }
    }
  }
}

:deep(button:focus) {
  outline: none;
}

:deep(.el-button--primary) {
  font-size: 14px;
  --el-button-bg-color: #2E5AFF;
  --el-button-border-color: #2E5AFF;
  --el-button-hover-bg-color: #3f67f8;
  --el-button-hover-border-color: #3f67f8;
}

:deep(.el-select__wrapper) {
  height: 36px;
  background-color: #0C184B;
  border: 1px solid #2644AD;
  box-shadow: none;
}

:deep(.el-select__wrapper.is-hovering:not(.is-focused)) {
  box-shadow: 0 0 0 1px #3050c5;
}

:deep(.el-select__placeholder) {
  color: rgba(255, 255, 255, 0.2);
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #00CEFF;
}

:deep(.el-breadcrumb__inner) {
  color: rgba($color: #fff, $alpha: .45);
  cursor: pointer;
}

:deep(.el-breadcrumb__inner:hover) {
  color: #00CEFF;
}

:deep(.el-dialog) {
  width: 717px;
  height: 626px;
  background: url('@/assets/emergencyplan/bg_dialog.png') no-repeat center;
  padding: 24px 32px;
  box-sizing: border-box;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #fff;
  font-size: 18px;
  margin: 20px 32px 0 0;
}

:deep(.el-dialog__header.show-close) {
  padding-right: 0;
}

:deep(.el-dialog__body) {
  padding: 34px 80px 0;
  box-sizing: border-box;
}

:deep(.el-dialog__footer) {
  padding-top: 0;
}

:deep(.el-form-item__label) {
  color: #fff;
  font-size: 14px;
}

:deep(.el-input__wrapper) {
  height: 36px;
}

:deep(.el-textarea),
:deep(.el-input) {
  --el-input-bg-color: #0C184B;
  --el-input-border-color: #2644AD;
  --el-input-text-color: #fff;
  --el-input-placeholder-color: rgba(255, 255, 255, 0.2);
  --el-input-hover-border-color: #3c59c5;
  --el-input-focus-border-color: #3c59c5;
}

:deep(.el-radio) {
  --el-radio-text-color: #fff;
  --el-radio-input-bg-color: transparent;
  --el-radio-input-border: 1px solid #5B6799;
}

:deep(.el-radio.el-radio--small .el-radio__inner) {
  width: 14px;
  height: 14px;
}
</style>