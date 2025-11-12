<template>
  <div class="childbox">
    <div class="line1">
      <div class="heard_name">
        <div class="d_name">数据更新</div>
      </div>
      <div class="box1">
      </div>
    </div>
    <div class="tablebox">
      <el-table
        :data="tableData"
        highlight-current-row
        style="width: 100%; height: 80%; background: #021b4f"
        :header-cell-style="{
          fontFamily: 'Arial',
          color: '#fff',
          fontWeight: '500',
          fontSize: '14px',
        }"
      >
        <el-table-column type="index" label="序号" width="80" align="center" />
        <el-table-column prop="type" label="数据类型" align="center" />
        <el-table-column prop="updateTime" label="更新时间" align="center" />
        <el-table-column prop="updateResult" label="结果" align="center" />
        <el-table-column prop="updateName" label="操作用户" align="center" />
        <el-table-column fixed="right" label="操作" align="center">
          <template #default="scope">
            <el-button
              @click="edit(scope.row)"
              style="color: #00ceff"
              link
              type="primary"
            >
              上传
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>

  <el-dialog
    v-model="uploadBol"
    width="600px"
    @close="cancel"
    align-center
    :destory-on-close="true"
    :close-on-click-modal="false"
    class="my_Dialog"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">
          上传
        </div>
      </div>
    </template>
    <el-upload ref="uploadRef" name="file" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
          :before-upload="beforeAvatarUpload" :auto-upload="true" :action="upload.url"
          :on-error="handleError" 
          :data="{id: upload.id }" :show-file-list="false" :on-success="handleFileSuccess" drag>
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <template #tip>
          <div class="el-upload__tip" style="display: flex;align-items: center;justify-content: space-between;color: #fff;">
            <span>仅允许导入<span style="color:#f56c6c">xls、xlsx</span>格式文件。</span>
            <text style="cursor: pointer;color: #409eff;" @click="uploadTmpl(upload.type)">
              下载模板
            </text>
          </div>
        </template>
        </el-upload>
  </el-dialog>
</template>
<script setup>
import {
  ref,
  onMounted,
  onBeforeUnmount,
  reactive,
  computed,
  watch,
  getCurrentInstance,
  nextTick,
} from "vue";
import axios from 'axios';
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from 'element-plus';
import {getDataImportList} from "@/api/basic/index";
import {download} from '@/utils/request'
const { proxy } = getCurrentInstance();
const uploadBol = ref(false)
const loadingInstance = ref(null) // 用于存储 loading 实例
/*** 导入参数 */
const upload = reactive({
  id:'',
  type:'',
  // 设置上传的请求头部
  headers: {'x-auth-token': localStorage.getItem('token')},
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/building-info/importCommercialData"
})
let tableData = ref([]);

const cancel = () => {
 
};
const beforeAvatarUpload = (file) => {
  console.log(upload.type)
  if(upload.type==='基础工作数据'||upload.type==='商业企业数据'||upload.type==='社区数据'){
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
}
const downloadTemplate = async (data,name) => {
  try {
    const response = await fetch(data);
    const blob = await response.blob();
    
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = name;
    link.click();
    URL.revokeObjectURL(url);
  } catch (error) {
    console.error('下载失败:', error);
  }
}

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  console.log(response,file,upload.id)
  if (response.code === 0) {
    if(upload.id==='3'){
      ElMessage.success(response.data)
    }else{
      ElMessage.success('导入成功。')
    }
    uploadBol.value = false;
    loadingInstance.value.close(); // 关闭 loading
    getUserListData();
  } else {
    loadingInstance.value.close(); // 关闭 loading
    ElMessage.warning('导入失败。')
  }
  proxy.$refs["uploadRef"].handleRemove(file);
};
const handleError = (err, file, fileList) => {
  loadingInstance.value.close(); // 关闭 loading
}
onMounted(() => {
  getUserListData();
});
const getUserListData = () => {
  getDataImportList().then((res) => {
    console.log(res)
    if (res.code === 0) {
      tableData.value = res.data;
    }
  });
};
const uploadTmpl = (type) => {
  let url
  let name
  if(type==='基础工作数据'){
    // download('/template/基础数据模版.xlsx',{},'基础数据模版.xlsx')
    url = '/template/基础数据模版.xlsx'; // public 文件夹中的文件路径
    name = '基础数据模版.xlsx'
  }
  if(type==='商业企业数据'){
    url =  '/template/商服数据模版.xls'; // public 文件夹中的文件路径
    name = '商服数据模版.xls'
  }
  if(type==='社区数据'){
    url =  '/template/社区数据模版.xlsx'; // public 文件夹中的文件路径
    name = '社区数据模版.xlsx'
  }
    downloadTemplate(url,name)
}
//编辑用户信息
function edit(row) {
  console.log(row);
  upload.type = row.type
  upload.id = row.id
  if(row.type==='基础工作数据'){
    upload.url = import.meta.env.VITE_APP_BASE_API + "/point-info/importPoint"
  }
  if(row.type==='商业企业数据'){
    upload.url = import.meta.env.VITE_APP_BASE_API + "/building-info/importCommercialData"
  }
  if(row.type==='社区数据'){
    upload.url = import.meta.env.VITE_APP_BASE_API + "/building-info/importResidentialData"
  }
  uploadBol.value = true;
}

onBeforeUnmount(() => {});
</script>
<style lang="scss" scoped>
.childbox {
  width: calc(100% - 40px);
  height: calc(100% - 20px);
  /* 减去上下 padding 之和 */
  background: #021b4f;
  padding: 20px 20px 0 20px;
}

.line1 {
  // display: flex;
  width: 100%;

  // justify-content: space-between;
  /* 新增这行代码 */
  .heard_name {
    background: url("../../assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;
    display: flex;
    align-items: center;

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

  .box1 {
    display: flex;
    align-items: center;
    justify-content: start;
    width: auto;
    height: 20px;
  }
}

.tablebox {
  width: 100%;
  height: calc(100% - 96px);
  overflow: auto;

  :deep(.el-table) {
    --el-table-border-color: #071336;
  }

  :deep(.el-table__header) {
    height: 48px;
  }

  :deep(.el-table .el-table__header tr th) {
    background: rgba(31, 76, 152, 0.8);
    color: #fff;
  }

  :deep(.el-table .el-table__row .el-table__cell) {
    background: rgba(31, 76, 152, 0.16);
    // height: 40px;
  }

  :deep(.el-table tr) {
    background: #021b4f;
    height: 48px;
  }

  :deep(.el-table .cell) {
    color: #fff;
  }

  :deep(.el-table__row:hover) {
    background-color: rgba(0, 206, 255, 0.2) !important;
    /* 使用您想要的颜色替换 #your-color-code */
  }

  :deep(.el-pagination) {
    display: flex;
    align-items: center;
    justify-content: center;

    .el-pagination__total {
      color: rgba(255, 255, 255, 0.7);
    }

    .el-pager {
      .is-active {
        background: #274eef;
      }
    }

    .btn-prev,
    .btn-next {
      background: none;

      .el-icon {
        font-size: 16px;
      }
    }
  }
}

:deep(.el-input) {
  padding: 0;
  width: 250px;
  height: 40px;
}

:deep(.el-button .el-icon) {
  margin-right: 6px;
}

:deep(.el-checkbox) {
  height: 28px !important;
}

:deep(.el-checkbox-group) {
  margin: 20px;
}

:deep(.el-radio__label) {
  color: #fff;
}
.role_Box {
  .item {
    display: flex;
    align-items: center;
    justify-content: start;
    height: 38px;
    .label_class {
      font-weight: 500;
      font-size: 14px;
      color: #ffffff;
      width: 80px;
    }
    .right_data {
      flex: 1;
      display: flex;
      align-items: center;
      font-size: 14px;
      color: #ffffff;
      font-weight: 500;
      .item_box {
        width: 100px;
        height: 36px;
        background: rgba(30, 55, 147, 0.5);
        border-radius: 2px 2px 2px 2px;
        border: 1px solid #1e3793;
        text-align: center;
        line-height: 36px;
        margin-right: 16px;
        font-size: 14px;
        color: #ffffff;
        font-weight: 500;
      }
    }
  }
}
:deep(.el-scrollbar__view){
  width: 100%;
  height: 100%;
}
</style>
