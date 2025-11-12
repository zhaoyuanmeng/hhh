<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-20 18:58:23
 * @LastEditTime: 2024-06-21 13:23:37
 * @LastEditors: Alex
-->
<template>
  <div class="app-container home">
    <el-dialog
      v-if="openBol"
      v-model="openBol"
      width="600px"
      @close="cancel"
      align-center
      append-to-body
      :destory-on-close="true"
      :close-on-click-modal="false"
      class="my_Dialog_plan"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">
            {{ props.type === "add" ? "创建常备方案" : "修改常备方案" }}
          </div>
        </div>
      </template>
      <div class="tabs">
        <div
          class="tabs_item"
          :class="{ active: selectItem === '1' }"
          @click="change('1')"
        >
          路线
        </div>
        <div
          class="tabs_item"
          :class="{ active: selectItem === '2' }"
          @click="change('2')"
        >
          现场
        </div>
        <div
          class="tabs_item"
          :class="{ active: selectItem === '3' }"
          @click="change('3')"
        >
          住地
        </div>
      </div>
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
          <el-col :span="24" v-if="selectItem === '1'">
            <el-form-item label="类型" style="width: 100%">
              <el-radio-group v-model="dialogForm.type" @change="getGSGT">
                <el-radio value="1">道路</el-radio>
                <el-radio value="5">高铁</el-radio>
                <el-radio value="4">高速</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24" v-if="dialogForm.type !== '1'">
            <el-form-item
              label="基础配置"
              style="width: 100%"
              prop="basicDataId"
              :rules="[{ required: true, message: '必填', trigger: 'change' }]"
            >
              <el-select v-model="dialogForm.basicDataId" placeholder="请选择">
                <el-option
                  v-for="item in selectData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="名称"
              prop="sceneName"
              :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
              style="width: 100%"
            >
              <el-input
                v-model="dialogForm.sceneName"
                placeholder="请输入..."
              ></el-input>
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
  </div>
</template>

<script setup>
import {
  reactive,
  ref,
  computed,
  getCurrentInstance,
  watch,
  defineProps,
} from "vue";
import { Search } from "@element-plus/icons-vue";
import { addPlanApi, updatePlanApi } from "@/api/plan";
import { getScreenPosition } from "@/api/task/task";
const { proxy } = getCurrentInstance();
const emit = defineEmits(["hideDialog"]);
const openBol = ref(false);
const selectItem = ref("1");
const dialogForm = reactive({
  taskId: "",
  id:'',
  sceneName: "",
  type: "1",
  basicDataId: "",
});
const selectData = ref([]);
const showDatas = (e)=>{
  let params;
  switch (e) {
    case "2":
      params = 3;
      break;
    case "3":
      params = 4;
      break;
    case "4":
      params = 1;
      break;
    case "5":
      params = 2;
      break;
  }
  getScreenPosition({ type: params }).then((res) => {
    if (res.code === 0) {
      selectData.value = res.data;
    }
  });
}
const props = defineProps({
  open: {
    type: Boolean,
    defalut: false,
  },
  itemInfo: {
    type: Object,
    defalut: function () {
      return {};
    },
  },
  type: {
    type: String,
    defalut: "add",
  },
});
watch(
  () => props.itemInfo,
  (val) => {
    if (val.id) {
      dialogForm.sceneName = val.sceneName;
      dialogForm.type = val.type;
      showDatas(val.type)
      dialogForm.basicDataId = val.basicDataId
      dialogForm.id = val.id
      dialogForm.taskId = ''
    } else {
      selectItem.value = '1'
      dialogForm.sceneName = '';
      dialogForm.type ='1';
      dialogForm.basicDataId = ''
      dialogForm.id = ''
      dialogForm.taskId = ''
      if (proxy.$refs["customForm"]) {
        proxy.$refs["customForm"].resetFields();
      }
    }
  },
  { deep: true, immediate: true }
);
watch(
  () => props.open,
  (val) => {
    if (val) {
      openBol.value = val;
    } else {
      openBol.value = val;
    }
  },
  { immediate: true, deep: true }
);

const cancel = () => {
  resetForm("customForm");
  emit("hideDialog");
};
const getGSGT = (e) => {
  if (e === "1") {
    return;
  }
  let params;
  switch (e) {
    case "2":
      params = 3;
      break;
    case "3":
      params = 4;
      break;
    case "4":
      params = 1;
      break;
    case "5":
      params = 2;
      break;
  }
  getScreenPosition({ type: params }).then((res) => {
    if (res.code === 0) {
      selectData.value = res.data;
    }
  });
};
const change = (e) => {
  selectItem.value = e;
  dialogForm.type = e;
  if (e !== "1") {
    getGSGT(e);
  }
};
const submit = async () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
      console.log(dialogForm);
      if (dialogForm.id) {
        // 编辑
        updatePlanApi(dialogForm).then((res) => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess("修改成功！");
            resetForm("customForm");
            emit("hideDialog",dialogForm.type);
          }
        });
      } else {
        // 新增
        addPlanApi(dialogForm).then((res) => {
          if (res.code === 0) {
            resetForm("customForm");
            proxy.$modal.msgSuccess("新增成功！");
            emit("hideDialog",dialogForm.type);
          }
        });
      }
    }
  });
  //
};
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
};
</script>

<style lang="scss">
.my_Dialog_plan {
  background: url("@/assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
  .heard_name {
    background: url("@/assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;
    // display: flex;
    // align-items: center;
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
  .el-dialog__headerbtn {
    position: absolute;
    top: 10px;
    right: 20px;
    .el-dialog__close {
      color: #fff;
      font-size: 20px;
    }
  }
  .el-dialog__headerbtn :focus,
  .el-dialog__headerbtn:hover {
    outline: none;
    box-shadow: none;
  }
  .el-button:focus {
    outline: none;
  }
  .el-dialog__body {
    padding-right: 30px;
  }
  .dialog-footer {
    display: flex;
    justify-content: end;
    padding-right: 20px;
    color: #fff;
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
    }
  }
  .customForm {
    padding: 20px 24px;
    padding-left: 10px;
    padding-top: 30px;
    .el-form-item__label {
      font-weight: 400;
      font-size: 14px;
      color: #ffffff;
      opacity: 0.8;
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
          // height: 36px;
          // line-height: 36px;
          font-size: 14px;
          color: #ffffff;
          opacity: 0.8;
        }
      }
      .el-textarea__inner {
        background: rgba(0, 12, 78, 0.5);
        box-shadow: none;
        border: 1px solid #5b6799;
      }
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
      // opacity: 0.8;
    }
  }
  .el-date-editor {
    background: rgba(0, 12, 78, 0.5);
    box-shadow: none;
    border: 1px solid #5b6799;
    .el-range-input,
    .el-range-separator {
      font-size: 14px;
      color: #ffffff;
      opacity: 0.8;
    }
  }
  .el-textarea__inner {
    color: #fff;
    opacity: 0.8;
  }
  .tabs {
    display: flex;
    width: 100%;
    height: 40px;
    align-items: center;
    width: 50%;
    padding-left: 30px;
    .tabs_item {
      flex: 1;
      text-align: center;
      font-weight: 350;
      font-size: 14px;
      color: #fff;
      border-radius: 0px;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
    }

    .active {
      color: #00ceff;
      border-bottom: 3px solid #00ceff;
    }
  }
  .el-radio__label {
    color: #fff;
    opacity: 0.8;
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
}
</style>
