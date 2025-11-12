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
      width="686px"
      @close="cancel"
      align-center
      :destory-on-close="true"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">{{ props.type==='add'?"创建任务":"修改任务" }}</div>
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
              label="任务名称"
              prop="taskName"
              :rules="[
                { required: true, message: '必填', trigger: 'blur' },
              ]"
              style="width: 100%"
            >
              <el-input v-model="dialogForm.taskName" placeholder="请输入..."></el-input>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item label="预案描述" style="width: 100%"  prop="taskDesc"   :rules="[
                { required: true, message: '必填', trigger: 'blur' },
              ]">
              <el-input
                v-model="dialogForm.taskDesc"
                placeholder="请输入..."
                type="textarea"
                :rows="5"
              />
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item
              label="预定时间"
              prop="times"
              :rules="[
                { required: true, message: '必填', trigger: 'change' },
              ]"
              style="width: 100%"
            >
              <el-date-picker
                v-model="dialogForm.times"
                type="datetimerange"
                range-separator="-"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                :editable="false"
              />
            </el-form-item>
          </el-col>
          <!-- <el-col :span="24">
            <el-form-item
              label="责任人"
              style="width: 100%"
              prop="head"
              :rules="[
                { required: true, message: '必填', trigger: 'blur' },
              ]"
            >
              <el-input v-model="dialogForm.head" placeholder="请输入...">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="联系电话"
              style="width: 100%"
              prop="phone"
              :rules="[
                { required: true, message: '必填', trigger: 'blur' },
              ]"
            >
              <el-input v-model="dialogForm.phone" placeholder="请输入...">
              </el-input>
            </el-form-item>
          </el-col> -->
          <el-col :span="24">
            <el-form-item
              label="任务等级"
              prop="taskLevel"
              :rules="[
                { required: true, message: '必填', trigger: 'change' },
              ]"
              style="width: 100%"
            >
              <!-- <el-input v-model="dialogForm.taskLevel" placeholder="请输入..."> -->
                <el-select v-model="dialogForm.taskLevel" clearable placeholder="请选择任务等级" size="large">
                  <el-option label="一级加强" value="一级加强" />
                  <el-option label="一级任务" value="一级任务" />
                  <el-option label="二级任务" value="二级任务" />
                  <el-option label="三级任务" value="三级任务" />
                  <el-option label="保卫任务" value="保卫任务" />
              </el-select>
              <!-- </el-input> -->
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
import { reactive, ref,computed,getCurrentInstance,watch,defineProps } from "vue";
import { Search } from "@element-plus/icons-vue";
import {addTask,updateTask} from '@/api/task/index'
const { proxy } = getCurrentInstance();
const emit = defineEmits(["hideDialog"]);
const openBol = ref(false)
const dialogForm = reactive({
  taskName: "",
  taskDesc:'',
  taskStartTime:'',
  taskEndTime:'',
  head:'',
  phone:'',
  taskLevel:'',
  createId:'',
  times:[]
});
const props = defineProps({
  open: {
    type: Boolean,
    defalut: false,
  },
  itemInfo:{
    type: Object,
    defalut: function () {
      return {}
    }
  },
  type:{
    type:String,
    defalut:'add'
  }
});
watch(() => props.itemInfo, (val) => {
  if(val.id){
    dialogForm.taskName = val.taskName
    dialogForm.taskDesc= val.taskDesc
    dialogForm.taskStartTime= val.taskStartTime
    dialogForm.taskEndTime= val.taskEndTime
    dialogForm.head= val.head
    dialogForm.phone= val.phone
    dialogForm.taskLevel= val.taskLevel
    dialogForm.id= val.id
    dialogForm.times= [val.taskStartTime,val.taskEndTime]
  }else{
   
    dialogForm.taskName = ''
    dialogForm.taskDesc= ''
    dialogForm.taskStartTime= ''
    dialogForm.taskEndTime= ''
    dialogForm.head= ''
    dialogForm.phone= ''
    dialogForm.taskLevel= ''
    dialogForm.id= null
    dialogForm.times= []
    if (proxy.$refs['customForm']) {
    proxy.$refs['customForm'].resetFields();
  }
  }
}, { deep: true,immediate: true });
watch(() => props.open, val => {
  if (val) {
    openBol.value = val
  }else{
    openBol.value = val
    
  }
}, { immediate: true, deep: true });


const cancel = () => {
  resetForm('customForm')
  emit("hideDialog");
};
const submit = async()=>{
  proxy.$refs["customForm"].validate(valid => {
    if (valid) {
       dialogForm.taskStartTime = dialogForm.times[0]
      dialogForm.taskEndTime = dialogForm.times[1]
      if(dialogForm.id){
        // 编辑
        updateTask(dialogForm).then(res=>{
          if(res.code===0){
          proxy.$modal.msgSuccess("修改成功！");
          resetForm('customForm')
          emit("hideDialog",'edit');
        }
        })
      }else{
        // 新增
        addTask(dialogForm).then(res=>{
        if(res.code===0){
          resetForm('customForm')
          proxy.$modal.msgSuccess("新增成功！");
          emit("hideDialog",res.data);
        }
      })
      }
    }
  });
  // 
}
// 表单重置
const resetForm =(refName)=> {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
}
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
  .el-dialog__headerbtn:active {
  background-color: transparent !important;
  outline: none !important;
  box-shadow: none !important;
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
        // height: 36px;
        // line-height: 36px;
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
:deep(.el-select__wrapper){
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;
  .el-select__selected-item{
    color: #ffffff;
    opacity: 0.8;
  }
  .is-transparent{
    color: #a8abb2;
    // opacity: 0.8;
  }
}
:deep(.el-date-editor){
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;
  .el-range-input,.el-range-separator{
    font-size: 14px;
        color: #ffffff;
        opacity: 0.8;
  }

}
:deep(.el-textarea__inner){
  color: #fff;
  opacity: 0.8;
}
</style>
