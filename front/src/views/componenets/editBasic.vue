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
      v-model="openBol"
      width="580px"
      @close="cancel"
      align-center
      :destory-on-close="true"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">编辑数据</div>
        </div>
      </template>
      <el-form
        ref="customForm"
        :inline="true"
        :model="dialogForm"
        :label-suffix="'：'"
        :label-width="110"
        class="customForm"
      >
        <el-row>
          <el-col :span="24" v-for="(item, index) in formItem" :key="index">
            <el-form-item :label="item.name" style="width: 100%">
              <el-input v-model="dialogForm[item.label]" placeholder="请输入..."> </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24" >
            <el-form-item label="图片" style="width: 100%">
              <img :src="'/imgs/'+dialogForm.imgName+'.png'" alt="" class="img_icon"/>
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
import { reactive, ref, computed, getCurrentInstance, watch,defineProps } from "vue";
const { proxy } = getCurrentInstance();
const emit = defineEmits(["hideDialog"]);
const openBol = ref(true);
const dialogForm = ref({
  imgName:''
});
const labels = ref({});
const formItem = ref([]);
const props = defineProps({
  pointInfo: {
    type: Object,
    defalut: () => {
      return {};
    },
  },
});
watch(
  () => props.pointInfo,
  (val) => {
    if (val) {
      getMapPointInfo(val);
    }
  },
  { deep: true }
);

const getMapPointInfo = (val) => {
  // 数据的重新组装
  dialogForm.value.imgName = val.imgName
  // 转换成数组对象
  let arrs = [];
  for (let key in val.data) {
    if (val.data.hasOwnProperty(key)) {
      arrs.push({ label: key, val: val.data[key] });
    }
  }
  formItem.value = arrs;
  arrs.map((item)=>{
    item.name= val.info[item.label]
    dialogForm.value[item.label] = item.val
  })
  
};
const cancel = () => {
  resetForm("customForm");
  emit("hideDialog");
};
const submit = async () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
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

<style scoped lang="scss">
:deep(.my_Dialog) {
  background: url("../../assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
  .heard_name {
    background: url("../../assets/panel/right_panel.png") no-repeat;
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
}
:deep(.el-dialog__headerbtn) {
  position: absolute;
  top: 10px;
  right: 20px;
  .el-dialog__close {
    color: #fff;
    font-size: 20px;
  }
}
:deep(.el-dialog__headerbtn :focus, .el-dialog__headerbtn:hover) {
  outline: none;
  box-shadow: none;
}
/* 解决按钮黑边框bug */
:deep(.el-button:focus) {
  outline: none;
}
:deep(.el-dialog__body) {
  padding-right: 30px;
}
:deep(.dialog-footer) {
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
  }
}
:deep(.customForm) {
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
        height: 36px;
        line-height: 36px;
        font-size: 14px;
        color: #ffffff;
        opacity: 0.8;
      }
    }
  }
}
:deep(.el-textarea__inner) {
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;
}
:deep(.el-select__wrapper) {
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
:deep(.el-date-editor) {
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
:deep(.el-textarea__inner) {
  color: #fff;
  opacity: 0.8;
}
</style>
