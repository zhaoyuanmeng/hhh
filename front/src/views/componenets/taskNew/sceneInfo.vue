<template>
  <div class="taskPlan_box">
    <div class="heard">
      {{ screenTitle }}详情
      <div class="close_modal" @click="closeModal"></div>
    </div>
    <div class="plan_box">
      <div v-if="sceneCardList.length>0">
        <div class="xc_info" v-for="(item,index) in sceneCardList" :key="index">
        <div class="item" style="color: #00ceff">
          <div class="left">{{ screenTitle }}名称：</div>
          <div class="right">{{ screenInfo.sceneName }}</div>
        </div>
        <div class="item">
          <div class="left">责任领导：</div>
          <div class="right">
            <el-input
              v-model="item.data.zrld"
              style="width: 100%"
              placeholder="请输入..."
            />
          </div>
        </div>
        <div class="item">
          <div class="left">联系电话：</div>
          <div class="right">
            <el-input
              v-model="item.data.phone"
              style="width: 100%"
              placeholder="请输入..."
            />
          </div>
        </div>
        <div class="police_box">
          <div class="title_name">警力部署</div>
          <div class="item">
            <div class="left">总警力部署:</div>
            <div class="right">{{ item.policeTypeStatistics.total }}</div>
          </div>
          <div class="item">
            <div class="left">执勤警力:</div>
            <div class="right">{{ item.policeTypeStatistics.onduty }}</div>
          </div>
          <div class="item">
            <div class="left">应急处突警力:</div>
            <div class="right">{{ item.policeTypeStatistics.emergency }}</div>
          </div>
          <div class="item">
            <div class="left">警力上岗时间:</div>
            <div class="right">
              <el-input
                v-model="item.data.time"
                style="width: 100%"
                placeholder="请输入..."
              />
            </div>
          </div>
        </div>
        <div class="police_box">
          <div class="title_name">具体岗哨</div>
          <div class="item details_box" v-for="(child,idx) in item.placeStatistics" :key="idx">
            <div class="left">{{ child.weizhi }}部署：</div>
            <div class="right" style="display: flex;flex-wrap: wrap;">
              <div v-for="(last,la) in child.data" :key="la">
                <span>{{  last.leixing}}</span>
                <span style="color: #00ceff;">{{ last.num }}人</span>
              </div>
            </div>
          </div>
          
        </div>
      </div>
      </div>
      <div v-else>
        <div class="xc_info">
        <div class="item" style="color: #00ceff">
          <div class="left">{{ screenTitle }}名称：</div>
          <div class="right">{{ screenInfo.sceneName }}</div>
        </div>
        <div class="item">
          <div class="left">责任领导：</div>
          <div class="right">
            <el-input
              v-model="dataForm.zrld"
              style="width: 100%"
              placeholder="请输入..."
            />
          </div>
        </div>
        <div class="item">
          <div class="left">联系电话：</div>
          <div class="right">
            <el-input
              v-model="dataForm.phone"
              style="width: 100%"
              placeholder="请输入..."
            />
          </div>
        </div>
        </div>
      </div>
    </div>
    <div class="btn_action">
      <div class="btn left" @click="closeModal">取消</div>
      <div class="btn" @click="submit">保存</div>
    </div>
  </div>
</template>
<script setup>
import {
  ref,
  computed,
  getCurrentInstance,
  reactive,
  onBeforeUnmount,
  onUnmounted,
  watch,
} from "vue";
import useTaskStore from "@/store/modules/taskStore";
import useScreenStore from "@/store/modules/screenStore";
import useSettingStore from "@/store/modules/settingStore";
import { searchNodePlanToScreen, saveScreenPlan } from "@/api/task/task";
import { ElMessage, ElMessageBox } from "element-plus";
let taskInfo = computed(() => useTaskStore().taskInfo); // 任务信息信息
const { proxy } = getCurrentInstance();
const screenTitle = computed(() => useTaskStore().screenPlanTitle);
const screenInfo = computed(() => useTaskStore().screenModalInfo);
const sceneBol = computed(() => useTaskStore().sceneBol);
const sceneCardList = computed(() =>useScreenStore().sceneCardInfo)
const dataForm = ref({
  zrld:'',
  phone:'',
  time:''
})
const submit = () => {
  if(sceneCardList.value.length>0){
    let data = {
      sceneId: screenInfo.value.id,
      planNode: '警力部署',
      data: sceneCardList.value[0].data,
      id: sceneCardList.value[0].id,
    };
    saveScreenPlan(data).then((res) => {
      if (res.code === 0) {
        closeModal()
        proxy.$modal.msgSuccess("保存成功");
      }
    });
  }else{
    let data = {
      sceneId: screenInfo.value.id,
      planNode: '警力部署',
      data: dataForm.value,
      id: '',
    };
    saveScreenPlan(data).then((res) => {
      if (res.code === 0) {
        closeModal()
        proxy.$modal.msgSuccess("保存成功");
      }
    });
  }
 
};
const closeModal = () => {
  useTaskStore().set_sceneBol(false);
};

onBeforeUnmount(() => {});
onUnmounted(() => {});
</script>
<style lang="scss" scoped>
.taskPlan_box {
  position: absolute;
  transform: scale(0.8);
  z-index: 3;
  right: -2%;
  bottom: -50px;
  top: -20px;
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

  .plan_box {
    flex: 1;
    overflow: auto;
    margin: 20px 20px 0 20px;
    background: rgba(0, 12, 78, 0.5);
    :deep(.xc_info) {
      padding: 20px;
      .item {
        display: flex;
        align-items: center;
        height: 40px;
        .left {
          width: 110px;
        }
        .right {
          flex: 1;
        }
      }
      .details_box{
        .left{width: auto;}
      }
      .el-input {
        border: 1px solid #5b6799;
        border-radius: 2px;

        .el-input__wrapper {
          background: rgba(0, 12, 78, 0.5);
          box-shadow: none;
        }

        .el-input__inner {
          height: 30px;
          line-height: 30px;
          font-size: 14px;
          color: #ffffff;
          opacity: 0.8;
        }
      }
      .title_name {
        margin-top: 10px;
        height: 40px;
        line-height: 40px;
        color: #00ceff;
      }
    }
  }
  .btn_action {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    margin: 20px;
    .btn {
      width: 67px;
      height: 32px;
      border-radius: 4px;
      line-height: 32px;
      text-align: center;
      color: #fff;
      cursor: pointer;
      background: #2e5aff;
    }
    .left {
      background: rgba(31, 76, 152, 0.8677);
      margin-right: 20px;
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
</style>
