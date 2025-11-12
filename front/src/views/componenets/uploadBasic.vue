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
      width="600px"
      @close="cancel"
      align-center
      :destory-on-close="true"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
    <template #header>
        <div class="heard_name">
          <div class="d_name">上传</div>
        </div>
      </template>
      <el-upload ref="uploadRef" name="file" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
          :before-upload="beforeAvatarUpload" :auto-upload="true" :action="upload.url"
          :on-error="handleError" 
          :show-file-list="false" :on-success="handleFileSuccess" drag>
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <template #tip>
          <div class="el-upload__tip" style="display: flex;align-items: center;justify-content: space-between;color: #fff;">
            <span>仅允许导入<span style="color:#f56c6c">xls、xlsx</span>格式文件。</span>
            <text style="cursor: pointer;color: #409eff;" v-if="false">
              下载模板
            </text>
          </div>
        </template>
        </el-upload>
    </el-dialog>
  </div>
</template>
<script setup>
import { reactive, ref, computed, getCurrentInstance, watch, onMounted } from "vue";
const { proxy } = getCurrentInstance();
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from 'element-plus';
const emit = defineEmits(["hideDialog"]);
const uploadBol = ref(false)
const openBol = ref(true)
const loadingInstance = ref(null) // 用于存储 loading 实例
/*** 导入参数 */
const upload = reactive({
  // 设置上传的请求头部
  headers: {'x-auth-token': localStorage.getItem('token')},
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/point-info/uploadPointFile"
})
const cancel = () => {
  emit('hideDialog',false)
}
const beforeAvatarUpload = (file) => {
  const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
  if (!isExcel) {
    ElMessage.error('上传文件只能是 xlsx 或 xls 格式!');
    return false
  }
  loadingInstance.value = ElLoading.service({
        text: '上传中',
        background: 'rgba(0, 0, 0, 0.7)', // 设置背景遮罩颜色
    });
}

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  if (response.code === 0) {
    ElMessage.success('导入成功。')
    uploadBol.value = false;
    loadingInstance.value.close(); // 关闭 loading
    emit('hideDialog',true)
  } else {
    loadingInstance.value.close(); // 关闭 loading
    ElMessage.warning(response.message)
  }
  proxy.$refs["uploadRef"].handleRemove(file);
};
const handleError = (err, file, fileList) => {
  loadingInstance.value.close(); // 关闭 loading
}
onMounted(()=>{

})
</script>

<style scoped lang="scss">
:deep(.el-upload__tip) {
  margin-top: 10px;
  color: #e6a23c;
}

:deep(.my_Dialog) {
  background: url("../../assets/panel/弹窗bg.png") no-repeat;
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
.map_box{
  display: flex;
  align-items: center;
  .mapMarker{
    width: 80px;
    height: 26px;
    background: #274EEF;
    border-radius: 2px;
    line-height: 26px;
    text-align: center;
    font-family: PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: #FFFFFF;
    cursor: pointer;
  }
  .customBtn{
    background: #274EEF;
    opacity: 0.5;
    height: 26px;
    border: 0px;
    width: 80px;
    margin-left: 10px;
  }
  .point{
    font-family: PingFang SC;
    font-weight: 400;
    font-size: 14px;
    color: #FFFFFF;
    margin-left: 22px;
    opacity: 0.8;
  }
}
.el-input-number{
  width: 120px;
  .el-input {
    height: 30px !important;
    border-radius: 0px !important;
    border: none !important;
  }
  .el-input__wrapper{
    background: none !important;
  }
  .el-input-number__decrease,.el-input-number__increase{
    background: rgba(0, 0, 0, 0);
    border-radius: 2px 2px 2px 2px;
    border: 1px solid #FFFFFF;
    opacity: 0.8;
    width: 30px;
    .el-icon{
      color: #fff;
    }
  }
}
.upload_item{
  .el-form-item__content{
    padding-left: 80px;
  }
}
.upload_item{
  .upload_btn{
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    width: 100px;
    height: 32px;
    background: #274EEF;
    border-radius: 2px;
    .icon_btn{
      background: url('../../assets/basic/upload_icon.png') no-repeat;
      background-size: 100% 100%;
      width: 16px;
      height: 16px;
    }
    .upload_text{
      font-family: PingFang SC;
      font-weight: 400;
      font-size: 14px;
      color: #FFFFFF;
      margin-left: 8px;
      opacity: 0.8;
    }
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
