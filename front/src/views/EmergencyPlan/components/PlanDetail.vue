<template>
  <div class="emergency-plan-details">
    <div class="header">
      预案列表
      <div class="close_modal" @click="closeModal"></div>
    </div>
    <div class="content">
      <div class="tabsBox">
        <div class="top_name">{{ info.title }}</div>
        <div class="level_name">
          <div class="left">预案等级:</div>
          <div class="right">
            {{
              info.type === "gs"
                ? "高速"
                : info.type === "gt"
                ? "高铁"
                : info.type === "xc"
                ? "现场"
                : "住地"
            }}
          </div>
        </div>
        <div class="creat_btn" @click="creatData">创建</div>
      </div>
      <div class="listBox">
        <div
          class="item_list"
          v-for="(list, idx) in listData"
          :key="idx"
          @click.stop="selectData(list)"
          :class="{ activeSelect: list.id === selectDom }"
        >
          <div class="left_num">{{ list.name }}</div>
          <div class="right_text">
            <div class="left">
              类型:<span class="spanColor">{{ list.type }}</span>
            </div>
            <div class="right">
              <div class="edit_btn" @click.stop="editFun(list)">
                <el-icon> <EditPen /> </el-icon>编辑
              </div>
              <div class="del_btn" @click.stop="delFun(list)">
                <el-icon> <Delete /> </el-icon>删除
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 编辑修改应急预案 -->
  <el-dialog
    v-model="openBol"
    width="486px"
    align-center
    :destory-on-close="true"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    :show-close="false"
    append-to-body
    class="yjya_Dialog"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ dialogType }}应急预案</div>
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
            label="预案名称"
            prop="name"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input
              v-model="dialogForm.name"
              placeholder="请输入预案名称..."
              style="width: 250px"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item
            label="预案类型"
            prop="type"
            :rules="[{ required: true, message: '必填', trigger: 'change' }]"
          >
            <el-select
              v-model="dialogForm.type"
              placeholder="请选择预案类型"
              style="width: 250px"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
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

  <!-- 预案详情页面 -->
  <InfoDetails v-if="showInfo" @closeCard="closeCardFun" />
</template>

<script setup>
// 方法类
import { closeFloors } from "@/components/SmartMap/js/utils";
import {clearDraw} from '../../componenets/taskNew/util'
import {drawBasicMarker,drawScreenData,drawLinesName} from '../utils'
// 组件
import InfoDetails from "./infoDetails.vue";
//UI
import { ElMessage, ElMessageBox } from "element-plus";
import { ref, computed, onMounted, getCurrentInstance } from "vue";
// api
import { getEmcyListdata, addEmcy, delEmcy,getEmcyInfo } from "@/api/task/emergency";
import { getDrawDataForScreen } from "@/api/task/task";
// store
import useEmergencyStore from "@/store/modules/emergencyPlan";
import useTaskStore from "@/store/modules/taskStore";
import useSettingStore from "@/store/modules/settingStore";
import useFloorStore from "@/store/modules/floorStore";
const EmergencyStore = useEmergencyStore();
const { proxy } = getCurrentInstance();
let info = computed(() => EmergencyStore.detailsInfo);
let showInfo = computed(()=>EmergencyStore.isShowInfo)
const listData = ref([]);
const openBol = ref(false);
const dialogType = ref("创建");
const options = ref([
  { label: "应急避险点", value: "应急避险点" },
  { label: "应急医院", value: "应急医院" },
]);
const dialogForm = ref({
  name: "", // 预案名称
  type: "", // 预案类型
  basicDataId: info.value.id,
});
const selectDom = ref("");
// 关闭
const closeModal = async () => {
  let g = window.__g;
  await g.marker.deleteByGroupId("yjya");
  //初始化、不传参等同于1;1：清除所有接口添加的对象 2：重置用户设置 4：复位相机到初始位置
  await g.reset(1|4);
  EmergencyStore.set_indexBol(true);
  EmergencyStore.set_infoBol(false);
  EmergencyStore.setDetailsPage(false)
  EmergencyStore.set_editYA(false);
  useSettingStore().setShowTool(false); // 关闭工具栏
  closeFloors();
  clearDraw()
  EmergencyStore.set_YAInfo({});
};
// 取消
const cancel = () => {
  resetForm("customForm");
  openBol.value = false;
};
// 确定
const submit = () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
      addEmcy(dialogForm.value).then((res) => {
        if (res.code === 0) {
          if (dialogType.value === "创建") {
            ElMessage.success("新增成功");
          } else {
            ElMessage.success("编辑成功");
          }
          cancel();
          getEmcyListdata({ basicDataId: info.value.id }).then((res) => {
            listData.value = res.data;
          });
        }
      });
    }
  });
};
// 编辑
const editFun = (item) => {
  let obj = {
    name: item.name,
    type: item.type, // 预案类型
    basicDataId: info.value.id,
    data: item.data,
    drawDataList: item.drawDataList,
    id: item.id,
  };
  dialogForm.value = obj;
  dialogType.value = "编辑";
  openBol.value = true;
};
// 删除
const delFun = (item) => {
  ElMessageBox.confirm("您确定要删除该应急预案？", "删除", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "Warning",
  }).then(() => {
    delEmcy({ id: item.id }).then((res) => {
      if (res.code === 0) {
        proxy.$modal.msgSuccess("删除成功");
        cancel();
        getEmcyListdata({ basicDataId: info.value.id }).then((res) => {
          listData.value = res.data;
        });
      }
    });
  });
};
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
};
// 创建
const creatData = () => {
  dialogForm.value = {
    name: "", // 预案名称
    type: "", // 预案类型
    basicDataId: info.value.id,
  };
  dialogType.value = "创建";
  openBol.value = true;
  resetForm("customForm");
};
// 选择应急预案
const selectData = async (data) => {
  selectDom.value = data.id; // 高亮样式
  console.log(data);
  EmergencyStore.setDetailsPage(true)
  useSettingStore().setShowTool(true); // 打开工具栏
  useFloorStore().set_isShowFloor(true) // 楼层接口查询可操作
  // 绘制标绘数据
  getDrawDataForScreen({ sceneId: data.id }).then((res) => {
    console.log(res.data, "9090");
      drawScreenData(res.data);
      useFloorStore().set_floorMarkers([]);
      closeFloors(); // 清除楼层炸开数据
      setTimeout(()=>{
         // 绘制基础点位信息
        drawBasicMarker(info.value,true)
      },100)
  });
  // 设置预案信息
  EmergencyStore.set_YAInfo(data);
  // 设置预案能否编辑操作
  EmergencyStore.set_editYA(true);
  // 查询详情编辑信息
  getEmcyInfo({id:data.id}).then(res=>{
    if(res.data.data){
      res.data.data.type= res.data.type
      EmergencyStore.set_editInfo(res.data.data)
    }else{
      let obj = {
        zrld:'',
        phone:'',
        zrdw:'',
        bxd:'',
        lxr:'',
        lxfs:'',
        line:'',
        type:res.data.type
      }
      EmergencyStore.set_editInfo(obj)
    }
    if(res.data.drawDataList?.length){
      // drawLinesName(res.data.drawDataList)
    }
  })
};
const closeCardFun = () => {
  selectDom.value = "";
  EmergencyStore.setDetailsPage(false)
  useSettingStore().setShowTool(false); // 关闭工具栏
  EmergencyStore.set_editYA(false);
  closeFloors();
  clearDraw()
};
onMounted(() => {
  console.log(info.value);
  getEmcyListdata({ basicDataId: info.value.id }).then((res) => {
    listData.value = res.data;
  });
});
</script>

<style lang="scss" scoped>
.emergency-plan-details {
  // width: 400px;
  // background: linear-gradient(
  //   180deg,
  //   #0a1d64 0%,
  //   rgba(21, 30, 73, 0.6984) 100%
  // );
  // position: absolute;
  // top: 0px;
  // left: -20px;
  // bottom: 0px;
  // z-index: 10;
  // transform: scale(0.8);
  position: absolute;
    z-index: 5;
    transform: scale(0.8);
    left: -2%;
    bottom: -50px;
    top: -20px;
    width: 400px;
    background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
    display: flex
;
    flex-direction: column;
    transition: width 0.1s;

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
      background: url("../../componenets/taskNew//img/close_new.png") no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
    }
  }

  .content {
    font-size: 14px;
    height: calc(100% - 48px);
    padding: 20px;
    box-sizing: border-box;
    background: linear-gradient(
      180deg,
      #0a1d64 0%,
      rgba(21, 30, 73, 0.6984) 100%
    );
    .tabsBox {
      background: rgba(0, 0, 0, 0.1);
      border-bottom: 1px solid rgba(38, 68, 173, 0.7);
      position: relative;
      height: 70px;
      .top_name {
        font-size: 18px;
        font-weight: bold;
        padding-top: 8px;
      }
      .level_name {
        display: flex;
        align-items: center;
        padding-top: 16px;
        .left {
          font-size: 14px;
          color: rgba(255, 255, 255, 0.45);
        }
        .right {
          font-size: 14px;
          font-weight: bold;
          color: #00ceff;
          margin-left: 16px;
        }
      }
      .creat_btn {
        position: absolute;
        top: 50%;
        right: 20px;
        width: 80px;
        height: 32px;
        transform: translateY(-50%);
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
    }

    .item_list {
      cursor: pointer;
      margin-top: 10px;
      padding: 0 16px;
      background: url("../../componenets/taskNew/img/active_yjya.png") no-repeat;
      background-size: 100% 100%;
      background-size: 100% 100%;
      box-sizing: border-box;
      height: 86px;
      .left_num {
        font-size: 18px;
        font-weight: bold;
        padding-top: 12px;
      }
      .right_text {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding-top: 12px;
        .left {
          font-size: 14px;
          color: rgba(255, 255, 255, 0.45);
          .spanColor {
            font-weight: bold;
            color: #00ceff;
            margin-left: 16px;
          }
        }
        .right {
          display: flex;
          align-items: center;
          .edit_btn {
            width: 58px;
            height: 28px;
            border-radius: 2px;
            box-sizing: border-box;
            border: 1px solid #6a87ff;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 12px;
            cursor: pointer;
            color: #6a87ff;
            font-size: 14px;
            .el-icon {
              margin-right: 2px;
            }
          }
          .del_btn {
            width: 58px;
            height: 28px;
            font-size: 14px;
            border-radius: 2px;
            box-sizing: border-box;
            border: 1px solid #e05959;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #e05959;
            cursor: pointer;
            .el-icon {
              margin-right: 2px;
            }
          }
        }
      }
    }
    .activeSelect {
      background: url("../../componenets/taskNew/img/listSelect.png") no-repeat;
      background-size: 100% 100%;
    }
  }
}
</style>

<style lang="scss">
.yjya_Dialog {
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
      color: #fff;
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
}
</style>
