<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-20 18:58:23
 * @LastEditTime: 2024-06-21 13:23:37
 * @LastEditors: Alex
-->
<template>
  <div class="file-preview">
    <el-dialog
      v-model="openBol"
      width="400px"
      align-center
      :destory-on-close="true"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      class="my_Dialog"
      @close="cancel"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">电子档案</div>
        </div>
      </template>
      <div class="preview-container">
        <!-- 结构化显示 -->
        <div class="structure-preview">
          <!-- 前言部分 -->
          <div class="preface" v-if="prefaceContent">
            <div v-html="prefaceContent" class="wordName"></div>
          </div>
          <el-collapse
            v-model="activeName"
            accordion
            v-if="sections.length > 0"
          >
            <el-collapse-item
              :name="section.title"
              v-for="(section, index) in sections"
              :key="index"
            >
              <template #title>
                <div style="display: flex; align-items: center">
                  {{ section.title }}
                </div>
              </template>
              <div class="collapse_box" v-html="section.content"></div>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-upload
            action="#"
            :auto-upload="false"
            :on-change="handleFile"
            :show-file-list="false"
            accept=".docx"
          >
            <el-button type="primary">解析Word文档</el-button>
          </el-upload>
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
  nextTick,
  onMounted,
  defineEmits,
} from "vue";
import * as mammoth from "mammoth";
import DOMPurify from "dompurify";
import { sessionStorage } from "@/utils/storage";
import emitter from "@/utils/emitter";
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from "element-plus";
const emit = defineEmits(["close"]);
const { proxy } = getCurrentInstance();
const openBol = ref(true);
const showWord = ref(true);
const loadingInstance = ref(null); // 用于存储 loading 实例
/*** 导入参数 */
const upload = reactive({
  id: "",
  // 设置上传的请求头部
  headers: { "x-auth-token": localStorage.getItem("token") },
  // 上传的地址
  url:
    import.meta.env.VITE_APP_BASE_API +
    "/specialServiceArchives/uploadArchivesIcon",
});
const prefaceContent = ref("");
const activeName = ref("");
const sections = ref([]);
onMounted(() => {});

// 处理文件上传
const handleFile = async (file) => {
  try {
    const arrayBuffer = await readFile(file.raw);
    const { value: html } = await mammoth.convertToHtml({ arrayBuffer },);
    parseStructure(html);
  } catch (err) {
    ElMessage.error(`解析失败: ${err}`);
  }
};

// 文件读取
const readFile = (file) => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = (e) => resolve(e.target.result);
    reader.onerror = reject;
    reader.readAsArrayBuffer(file);
  });
};
const parseStructure = (html) => {
  const parser = new DOMParser();
  const doc = parser.parseFromString(html, "text/html");
  const nodes = doc.body.childNodes;
  let currentSection = null;
  let preface = [];
  // 遍历所有节点
  Array.from(nodes).forEach((node) => {
    if (isTitleNode(node)) {
      // 遇到新标题
      if (currentSection) {
        sections.value.push(currentSection);
      }
      currentSection = {
        title: node.textContent.trim(),
        content: "",
      };
    } else {
      // 内容处理
      const htmlContent = node.outerHTML || node.textContent;
      if (currentSection) {
        currentSection.content += htmlContent;
      } else {
        preface.push(htmlContent);
      }
    }
  });

  // 处理最后部分
  if (currentSection) {
    sections.value.push(currentSection);
  }

  prefaceContent.value = preface.join("");
};
// 判断标题节点（根据实际样式调整）
const isTitleNode = (node) => {
  return (
    node.nodeName === "H1" || /^[一二三四五六七八九十]、/.test(node.textContent)
  );
};
const cancel = () => {
  // 删除缓存的数据和场景图标
  emit("close");
};
</script>

<style scoped lang="scss">
:deep(.my_Dialog) {
  background: url("../../assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;

  // padding: 20px 32px 37px 32px;
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
}

.noData {
  color: #ffffff;
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
  // padding-right: 30px;
}

:deep(.dialog-footer) {
  display: flex;
  justify-content: end;
  padding-right: 20px;

  .del {
    .el-icon {
      color: #00ceff;
      margin-right: 8px;
      font-size: 18px;
      cursor: pointer;
    }
  }
}
:deep(.wordName p:first-child) {
  text-align: center;
  font-size: 18px;
}
.preface {
  padding-bottom: 20px;
  color: #fff;
}

:deep(table) {
  border-collapse: collapse;
  margin: 10px 0;
}

:deep(td) {
  border: 1px solid #ddd;
  padding: 8px;
}
:deep(.el-collapse) {
  border: none;
  background: rgba(0, 12, 78, 0.5);
}

:deep(.el-collapse-item) {
  margin-bottom: 4px;
}

:deep(.el-collapse-item__header) {
  background: rgba(28, 53, 155, 0.5) !important;
  border: 0px;
  transition: none;
  height: 40px;
  border-radius: 0;
  font-size: 14px;
  color: #fff;
  line-height: 32px;
  position: relative;
  padding-left: 20px !important;
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
  padding: 10px 20px;
}

.collapse_box,
.wordName {
  font-family: "Microsoft Yahei", sans-serif;
  line-height: 1.8;
}

/* 序号样式增强 */
.collapse_box p[data-original-number]::before {
  content: attr(data-original-number);
  color: #1890ff;
  font-weight: 500;
  margin-right: 0.5em;
}

/* 多级缩进模拟 */
.collapse_box p {
  margin: 8px 0;
  padding-left: 0.5em;
}

.collapse_box p[style*="margin-left"] {
  position: relative;
  padding-left: 1em;
}

/* 强制显示列表编号 */
:deep(.wordName ol) {
  list-style-type: decimal !important;
  padding-left: 2em;
}

:deep(.wordName li) {
  list-style-position: outside !important;
}

/* 处理手动编号样式 */
:deep(.wordName p) {
  margin: 0.5em 0;
}
</style>
