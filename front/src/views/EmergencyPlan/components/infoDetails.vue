<template>
  <div class="taskPlan_box">
    <div class="heard">
      预案详情
      <div class="close_modal" @click="closeModal"></div>
    </div>
    <div class="plan_box">
      <div class="topbox">
        <div class="left">{{ info.title }}应急措施</div>
        <div class="edit" @click="editData">编辑</div>
      </div>
      <div class="info_box">
        <div class="item">
          <div class="left">负责人：</div>
          <div class="right">{{ editInfo.zrld }}</div>
        </div>
        <div class="item">
          <div class="left">联系方式：</div>
          <div class="right">{{ editInfo.phone }}</div>
        </div>
        <div class="item">
          <div class="left">责任单位：</div>
          <div class="right">{{ editInfo.zrdw }}</div>
        </div>
        <div class="item">
          <div class="left">{{editInfo.type==='应急避险点'?'应急避险点':'应急医院'}}：</div>
          <div class="right">
            <span class="spanC">{{ editInfo.bxd }}</span>
          </div>
        </div>
        <div class="item">
          <div class="left">联系人：</div>
          <div class="right">{{ editInfo.lxr }}</div>
        </div>
        <div class="item">
          <div class="left">联系方式：</div>
          <div class="right">{{ editInfo.lxfs }}</div>
        </div>
        <div class="item">
          <div class="left">{{editInfo.type==='应急避险点'?'避险点':'医院'}}路线：</div>
          <div class="right">{{ editInfo.line }}</div>
        </div>
      </div>
    </div>
  </div>
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
        <div class="d_name">{{editInfo.type==='应急避险点'?'应急避险点':'应急医院'}}</div>
      </div>
    </template>
    <div class="orgLeard">
      <el-form
        ref="customForm"
        :model="dialogData"
        :label-suffix="'：'"
        :label-width="100"
        class="customForm"
        size="small"
      >
        <el-form-item
          label="负责人"
          prop="zrld"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          style="width: 100%"
        >
          <el-input
            v-model="dialogData.zrld"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>

        <el-form-item
          label="联系方式"
          style="width: 100%"
          prop="phone"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="dialogData.phone"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
        <el-form-item
          label="责任单位"
          style="width: 100%"
          prop="zrdw"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="dialogData.zrdw"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="editInfo.type==='应急避险点'?'应急避险点':'应急医院'"
          prop="bxd"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          style="width: 100%"
        >
          <el-input v-model="dialogData.bxd" placeholder="请输入..."></el-input>
        </el-form-item>

        <el-form-item
          label="联系人"
          style="width: 100%"
          prop="lxr"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input v-model="dialogData.lxr" placeholder="请输入..."></el-input>
        </el-form-item>
        <el-form-item
          label="联系方式"
          style="width: 100%"
          prop="lxfs"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="dialogData.lxfs"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
        <el-form-item
          :label="editInfo.type==='应急避险点'?'应急路线':'医院路线'"
          style="width: 100%"
          prop="line"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="dialogData.line"
            :rows="3"
            type="textarea"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="submit">确定</div>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import {
  ref,
  computed,
  getCurrentInstance,
  reactive,
  onMounted,
  defineEmits,
  watch,
} from "vue";
import { getEmcyInfo, addEmcy } from "@/api/task/emergency";
import useTaskStore from "@/store/modules/taskStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import { ElMessage, ElMessageBox } from "element-plus";
import { searchNodePlan, saveTaskPlan } from "@/api/task/task";
const emit = defineEmits(["closeCard"]);
let y_a_Info = computed(() => useEmergencyStore().YAInfo); // 预案详细信息
let info = computed(() => useEmergencyStore().detailsInfo);
let editInfo = computed(() => useEmergencyStore().editInfo);
const { proxy } = getCurrentInstance();
const openBol = ref(false);
const dialogData = ref({
  zrld: "",
  phone: "",
  zrdw: "",
  bxd: "",
  lxr: "",
  lxfs: "",
  line: "",
});
// 关闭面板
const closeModal = () => {
  emit("closeCard");
};

const cancel = () => {
  openBol.value = false;
};
const submit = () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
      let obj = {
        id: y_a_Info.value.id,
        data: dialogData.value,
      };
      addEmcy(obj).then((res) => {
        if (res.code === 0) {
          ElMessage.success("编辑成功");
          openBol.value = false;
          getEmcyInfo({ id: y_a_Info.value.id }).then((res) => {
            dialogData.value = res.data.data;
            useEmergencyStore().set_editInfo(res.data.data);
          });
        }
      });
    }
  });
};
// 编辑数据
const editData = () => {
  openBol.value = true;
  dialogData.value.zrld = editInfo.value.zrld;
  dialogData.value.phone = editInfo.value.phone;
  dialogData.value.zrdw = editInfo.value.zrdw;
  dialogData.value.bxd = editInfo.value.bxd;
  dialogData.value.lxr = editInfo.value.lxr;
  dialogData.value.lxfs = editInfo.value.lxfs;
  dialogData.value.line = editInfo.value.line;
};
onMounted(() => {});
</script>
<style lang="scss" scoped>
.taskPlan_box {
  position: absolute;
  transform: scale(0.8);
  z-index: 3;
  right: -2%;
  bottom: 0px;
  top: 0px;
  width: 400px;
  background: linear-gradient(180deg, #0a1d64 0%, rgba(21, 30, 73, 0.7) 100%);
  display: flex;
  flex-direction: column;

  .heard {
    height: 48px;
    background: url("../../../assets/panel/panel_bg.png") no-repeat;
    background-size: 100% 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: PingFangSC;
    font-weight: 400;
    font-size: 24px;
    color: #ffffff;
    text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
    font-style: normal;
    position: relative;
    font-family: YouSheBiaoTiHei;

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

  .plan_box {
    flex: 1;
    overflow: auto;
    padding: 20px 20px;
    .topbox {
      display: flex;
      align-items: center;
      justify-content: space-between;
      .left {
        font-size: 18px;
        font-weight: bold;
        color: #fff;
      }
      .edit {
        width: 80px;
        height: 36px;
        background: #274eef;
        color: #fff;
        font-size: 14px;
        cursor: pointer;
        text-align: center;
        line-height: 36px;
      }
    }
    .info_box {
      background: rgba(0, 12, 78, 0.5);
      padding: 20px;
      .item {
        display: flex;
        align-items: center;
        font-size: 14px;
        color: #fff;
        min-height: 30px;
        .left {
          width: 100px;
        }
        .right {
          flex: 1;
          .spanC {
            color: #00ceff;
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
