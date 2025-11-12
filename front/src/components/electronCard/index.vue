<template>
  <div class="content-wraper">
    <div class="content-header">
      电子档案
      <div class="close_modal" @click="closeModal"></div>
    </div>
    <div class="content-box">
      <div class="plan_box" v-if="wordTextData.length!==0||wordTitle.length!==0">
      <div class="word_title" v-for="(t,i) in wordTitle" :key="i" v-html="t.content"></div>
      <el-collapse v-model="activeName" accordion @change="activeNameChild=''">
        <el-collapse-item
          :title="item.title"
          :name="item.id"
          v-for="(item, index) in wordTextData"
          :key="index"
        >
          <!-- <div
            class="collapse_box"
            v-html="item.content"
            style="padding: 16px"
          ></div> -->
          <div class="collapse_box" >
              <div v-if="item.children?.length" style="padding: 10px 0 0 6px">
                <div v-if="item.content" v-html="item.content" style="padding: 6px 16px 16px 10px"></div>
                <el-collapse v-model="activeNameChild" accordion>
                <el-collapse-item
                  :title="child.title"
                  :name="child.id"
                  v-for="(child, idx) in item.children"
                  :key="idx"
                >
                  <div class="collapse_box" style="padding: 16px" v-html="child.content"></div>
                </el-collapse-item>
              </el-collapse>
              </div>
              <div v-else v-html="item.content" style="padding:16px"></div>
            </div>
        </el-collapse-item>
      </el-collapse>
    </div>
      <div class="noData" v-else>暂无内容</div>
      <div class="import_claer_btn">
        <div class="left_icon" @click="uploadWord">
          <el-icon><Upload /></el-icon><span class="text_span">导入</span>
        </div>
        <div class="right_icon" @click="clearWord">
          <el-icon><Delete /></el-icon><span class="text_span">清空</span>
        </div>
      </div>
    </div>
  </div>
  <el-dialog
    v-model="uploadBol"
    width="400px"
    align-center
    :destory-on-close="true"
    :close-on-click-modal="false"
    class="my_Dialog_text"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">导入</div>
      </div>
    </template>
    <el-upload
      ref="uploadRef"
      name="file"
      :limit="1"
      accept=".docx"
      :headers="upload.headers"
      :before-upload="beforeAvatarUpload"
      :auto-upload="true"
      :action="upload.url"
      :on-error="handleError"
      :data="{ businessId: upload.id }"
      :show-file-list="false"
      :on-success="handleFileSuccess"
      drag
    >
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div
          class="el-upload__tip"
          style="
            display: flex;
            align-items: center;
            justify-content: space-between;
            color: #fff;
          "
        >
          <span
            >仅支持上传<span style="color: #f56c6c">docx</span>格式文件。</span
          >
        </div>
      </template>
    </el-upload>
  </el-dialog>
</template>

<script setup>
import { ElMessage, ElMessageBox, ElLoading } from "element-plus";
import {
  computed,
  watch,
  ref,
  onMounted,
  getCurrentInstance,
  reactive,
  defineEmits,
} from "vue";
import { getWordContent, clearWordContent } from "@/api/task/task";
const { proxy } = getCurrentInstance();
const emit = defineEmits(["close"]);
const props = defineProps({
  id: {
    type: String,
    default: "",
  },
});
let businessId = computed(() => props.id);
const uploadBol = ref(false);
const wordTextData = ref([]);
const wordTitle = ref([]);
const activeName = ref('')
const activeNameChild = ref('')
const loadingInstance = ref(null); // 用于存储 loading 实例
/*** 导入参数 */
const upload = reactive({
  id: businessId.value,
  // 设置上传的请求头部
  headers: { "x-auth-token": localStorage.getItem("token") },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/task/uploadArchives",
});
onMounted(() => {
  getCardList();
});
// 关闭弹框
const closeModal = () => {
  emit("close", 0);
};
// 导入
const uploadWord = () => {
  uploadBol.value = true;
};
// 清空
const clearWord = () => {
  ElMessageBox.confirm("确认要清除数据？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let result = await clearWordContent({ businessId: businessId.value });
      if (result.code === 0) {
        getCardList();
        ElMessage({
          type: "success",
          message: "已清空",
        });
      }
    })
    .catch(() => {});
};
const beforeAvatarUpload = (file) => {
  const extension = file.name.split(".").pop().toLowerCase();
  if (extension !== "docx") {
    ElMessage.error("仅支持上传docx文件!");
    return false;
  }
  loadingInstance.value = ElLoading.service({
    text: "上传中",
    background: "rgba(0, 0, 0, 0.7)", // 设置背景遮罩颜色
  });
};

/** 文件上传成功处理 */
const handleFileSuccess = (response, file, fileList) => {
  if (response.code === 0) {
    ElMessage.success("导入成功。");
    uploadBol.value = false;
    loadingInstance.value.close(); // 关闭 loading
    getCardList();
  } else {
    loadingInstance.value.close(); // 关闭 loading
    ElMessage.warning("导入失败。");
  }
  proxy.$refs["uploadRef"].handleRemove(file);
};
const handleError = (err, file, fileList) => {
  loadingInstance.value.close(); // 关闭 loading
};
// 查询导入的文档内容
const getCardList = () => {
  getWordContent({ businessId: businessId.value }).then((res) => {
    if (res.data && res.data.length > 0) {
      wordTextData.value = res.data.filter((item) => item.title !== "标题"&&item.title !== "");
      wordTitle.value = res.data.filter((item) => item.title === "标题"&&item.title !== "");
      console.log(wordTextData.value,wordTitle.value)
    } else {
      wordTextData.value = [];
      wordTitle.value = [];
    }
  });
};
</script>

<style lang="scss" scoped>
.content-wraper {
  width: 400px;
  height: calc(110vh);
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 1) 100%
  ) !important;
  position: absolute;
  top: 72px;
  right: 10px;
  bottom: 20px;
  z-index: 30;
  transform: scale(0.8);
  transform-origin: right top;

  .content-header {
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
      width: 30px;
      height: 30px;
      background: url("@/assets/panel/close.png") no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
    }
  }
  .content-box {
    height: calc(100% - 48px);
    padding: 0px;
    box-sizing: border-box;
    font-family: Source Han Sans;
    font-size: 14px;
    position: relative;
    .noData {
      color: #fff;
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      font-size: 14px;
      font-weight: bold;
    }
    .import_claer_btn {
      display: flex;
      align-items: center;
      justify-content: center;
      position: absolute;
      bottom: 15px;
      right: 20px;
      .left_icon {
        cursor: pointer;
        display: flex;
        align-items: center;
        color: #00ceff;
        margin-right: 20px;
        .el-icon {
          font-size: 20px;
        }
      }
      .right_icon {
        cursor: pointer;
        display: flex;
        align-items: center;
        color: #00ceff;
        .el-icon {
          font-size: 20px;
        }
      }
      .text_span {
        margin-left: 4px;
      }
    }
  .plan_box {
    overflow: auto;
    padding: 14px 4px;
    height: calc(100% - 50px);
    width: 100%;
    padding-bottom: 0px;
    .word_title{
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 0 10px 20px 10px;
      font-weight: bold;
      font-size: 16px;
      // font-family: 宋体;
    }
    :deep(.el-collapse) {
      border: none;
    }

    :deep(.el-collapse-item) {
      margin-bottom: 7px;
    }

    :deep(.el-collapse-item__header) {
      background: rgba(28, 53, 155, 0.5) !important;
      border: 0px;
      transition: none;
      height: 36px;
      border-radius: 0;
      font-size: 14px;
      color: #fff;
      padding-left: 10px;
      line-height: 36px;
      position: relative;
      font-size: 16px;
      // font-family: 宋体;
    }

    :deep(.el-collapse-item__arrow) {
      position: absolute;
      right: 16px;
      font-size: 16px;
    }

    :deep(button:focus, button:focus-visible) {
      outline: none;
    }

    :deep(.el-collapse-item__wrap) {
      background: none;
      border: none;
    }

    :deep(.el-collapse-item__content) {
      color: #fff;
      min-height: 80px;
      padding-bottom: 0px;
    }

    .collapse_box {
      // padding: 6px 20px 0 30px;
      position: relative;
    }
  }
  }
}
</style>
