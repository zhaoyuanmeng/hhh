<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-20 18:58:23
 * @LastEditTime: 2024-06-21 13:23:37
 * @LastEditors: Alex
-->
<template>
  <div class="file-preview">
    <!-- :data="{id: upload.id }" -->
    <el-upload class="del" ref="uploadRef" name="file" :limit="1" accept=".pdf" :headers="upload.headers"
      :before-upload="beforeAvatarUpload" :auto-upload="true" :data="{businessId: upload.id,businessType:upload.type }" :action="upload.url" :on-error="handleError"
      :show-file-list="false" :on-success="handleFileSuccess">
      <p style="color: #00CEFF;cursor: pointer;font-size: 14px;">上传电子档案</p>
    </el-upload>
  </div>
</template>

<script setup>
import {
  reactive,
  ref,
  computed,
  getCurrentInstance,
  watch,
  nextTick,
  onMounted,
  defineEmits,
  defineProps
} from "vue";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter";
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from 'element-plus';
const emit = defineEmits(["success"]);
const UPLOAD_BASE_URL = '/imgPath'
const { proxy } = getCurrentInstance();
const loadingInstance = ref(null) // 用于存储 loading 实例
/*** 导入参数 */
const upload = reactive({
  id: '',
  type:'DangAn',
  // 设置上传的请求头部
  headers: { 'x-auth-token': localStorage.getItem('token') },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + '/file/uploadFile'
})
const props = defineProps({
    id: {
    type: String,
    defalut: () => {
      return '';
    },
  },
});
watch(
  () => props.id,
  (val) => {
    if (val) {
      upload.id = val
    }
  },
  { deep: true, immediate: true }
);
// 响应式数据
const htmlContent = ref('')
const toc = ref([])
const activeId = ref('')
const contentRef = ref(null)
const currentFile = ref('')
// 4. 安全HTML显示
const safeHtml = computed(() => htmlContent.value)
onMounted(() => {
  // 请求分组数据
});
const handleError = (err, file, fileList) => {
  loadingInstance.value.close(); // 关闭 loading
}
// 选取文件
const beforeAvatarUpload = (file) => {
  // 校验文件类型
  const isValidType = ['.pdf'].some(ext =>
    file.name.toLowerCase().endsWith(ext)
  );

  if (!isValidType) {
    ElMessage.error('仅支持 PDF 文档格式！');
    return false; // 阻止上传
  }
  loadingInstance.value = ElLoading.service({
    text: '上传中',
    background: 'rgba(0, 0, 0, 0.7)', // 设置背景遮罩颜色
  });
}

/** 文件上传成功处理 */
const handleFileSuccess = async (response, file, fileList) => {
  if (response.code === 0) {
    ElMessage.success('导入成功。')
    loadingInstance.value.close(); // 关闭 loading
    emit('success')

  } else {
    loadingInstance.value.close(); // 关闭 loading
    ElMessage.warning('导入失败。')
  }
  proxy.$refs["uploadRef"].handleRemove(file);
};
</script>

<style scoped lang="scss"></style>
