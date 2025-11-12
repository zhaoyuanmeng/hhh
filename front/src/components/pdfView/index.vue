<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-11-08 15:13:08
 * @LastEditTime: 2024-11-15 11:24:14
 * @LastEditors: Alex
-->
<template>
  <div>
    <el-dialog v-model="showPdf" :close-on-click-modal="false" :close-on-press-escape="false" :append-to-body="true"
    :width="currentWidth + 'px'" class="pdf_dialog" align-center @close="cancel" draggable>
      <!-- 右侧拖拽条 -->
    <div 
      class="drag-handle"
      @mousedown.stop="startResize"
    ></div>
      <template #header>
        <div class="heard_name_view">
          <div class="d_name">{{ title }}</div>
          <div class="d_name" title="删除" @click="delPdf"><el-icon>
              <Delete />
            </el-icon></div>
        </div>
      </template>
      <div class="dialog_body_custom">
        <IFrame v-model:src="url" />
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, defineProps, onMounted, defineEmits } from 'vue'
import { ElMessage, ElMessageBox } from "element-plus";
import IFrame from '@/components/iFrame/index.vue'
import {deleteTaskFile} from '@/api/index'
const emit = defineEmits(["close"]);
const props = defineProps({
  pdfObj: {
    type: Object,
    default: {
      src:'',
      name:'',
      id:''
    }
  }
})

const showPdf = ref(true)
const url = computed(() => props.pdfObj.src)
const title = computed(() => props.pdfObj.name)
const bussinessId = computed(() => props.pdfObj.id)
const currentWidth = ref(1200) // 初始宽度
let isResizing = false
let startX = 0
let startWidth = 0

const startResize = (e) => {
  isResizing = true
  startX = e.clientX
  startWidth = currentWidth.value
  document.addEventListener('mousemove', handleResize)
  document.addEventListener('mouseup', stopResize)
}

const handleResize = (e) => {
  if (!isResizing) return
  const delta = e.clientX - startX
  currentWidth.value = Math.max(400, startWidth + delta) // 最小宽度400px
}

const stopResize = () => {
  isResizing = false
  document.removeEventListener('mousemove', handleResize)
}
onMounted(() => {
})
const cancel = () => {
  emit('close', 1)
}
// 删除pdf
const delPdf = () => {
  ElMessageBox.confirm(`确定要删除吗？`, "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      deleteTaskFile({businessId:bussinessId.value}).then((res) => {
        if (res.code === 0) {
          ElMessage.success("删除成功");
          emit('close', 0)
        }
      });
    })
    .catch(() => {
      console.log("已取消");
    });
}
</script>
<style>
.pdf_dialog {
  padding: 0;
}

.heard_name_view {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 0 100px 0 30px;
}

.d_name .el-icon {
  color: #00CEFF;
  margin-right: 8px;
  font-size: 18px;
  cursor: pointer;
}

.pdf_dialog .el-dialog__body {
  height: 800px;
}

.pdf_dialog .el-dialog__header {
  padding: 0px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dialog_body_custom {
  width: 100%;
  height: 100%;
}
.drag-handle {
  position: absolute;
  right: -5px;
  top: 0;
  bottom: 0;
  width: 10px;
  cursor: col-resize;
  background: transparent;
  z-index: 2000;
}

/* 拖拽时显示反馈 */
.drag-handle:hover::after,
.drag-handle:active::after {
  content: '';
  position: absolute;
  right: 4px;
  top: 0;
  bottom: 0;
  width: 2px;
  /* background: #409EFF; */
}
.el-dialog__header {
  cursor: move;
  user-select: none;
}

.el-dialog__body {
  pointer-events: none; /* 防止内容干扰拖拽 */
}

.el-dialog__body * {
  pointer-events: auto; /* 恢复子元素事件 */
}
</style>
