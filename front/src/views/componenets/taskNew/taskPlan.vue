<template>
  <div class="taskPlan_box">
    <div class="heard">
      任务规划
      <div class="close_modal" @click="closeModal"></div>
    </div>
    <div class="plan_box">
      <div v-if="wordTextData.length !== 0 || wordTitle.length !== 0">
        <div
          class="word_title"
          v-for="(t, i) in wordTitle"
          :key="i"
          v-html="t.content"
        ></div>
        <el-collapse v-model="activeName" accordion @change="activeNameChild=''">
          <el-collapse-item
            :title="item.title"
            :name="item.id"
            v-for="(item, index) in wordTextData"
            :key="index"
          >
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
      <div v-else class="noData">暂无内容</div>
      <!-- <el-collapse v-model="activeName" accordion @change="changeActive">
        <el-collapse-item
          :title="item.name"
          :name="item.node"
          v-for="(item, index) in activeList"
          :key="index"
        >
          <div class="collapse_box" v-if="item.name !== '任务分工及警力部署'">
            <div class="edit" @click="openDialog(item)">编辑</div>
            <div class="content" v-if="item.name !== '组织领导'">
              {{ textData }}
            </div>
            <div v-else class="org_modal">
              <div v-if="orgInfo.data">
                <div class="item_box">
                  <div class="title">总指挥</div>
                  <div class="name">姓名：{{ orgInfo.data.zzh.name }}</div>
                  <div class="bottom_data">
                    <div class="left">职位：{{ orgInfo.data.zzh.job }}</div>
                    <div class="right">电话：{{ orgInfo.data.zzh.phone }}</div>
                  </div>
                </div>
                <div class="item_box">
                  <div class="title">副总指挥</div>
                  <div class="name">姓名：{{ orgInfo.data.fzzh.name }}</div>
                  <div class="bottom_data">
                    <div class="left">职位：{{ orgInfo.data.fzzh.job }}</div>
                    <div class="right">电话：{{ orgInfo.data.fzzh.phone }}</div>
                  </div>
                </div>
                <div
                  class="item_box"
                  v-for="(item, index) in orgInfo.data.member"
                >
                  <div class="title">成员{{ index + 1 }}</div>
                  <div class="name">姓名：{{ item.name }}</div>
                  <div class="bottom_data">
                    <div class="left">职位：{{ item.job }}</div>
                    <div class="right">电话：{{ item.phone }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else>
            <TaskPolice />
          </div>
        </el-collapse-item>
      </el-collapse> -->

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
  <!-- <el-dialog
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
        <div class="d_name">{{ titleName }}</div>
      </div>
    </template>
    <div class="orgLeard" v-if="titleName === '组织领导'">
      <el-form
        ref="customForm"
        :model="orgForm"
        :label-suffix="'：'"
        :label-width="100"
        class="customForm"
        size="small"
      >
        <el-form-item
          label="总指挥"
          prop="zzh.name"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          style="width: 100%"
        >
          <el-input
            v-model="orgForm.zzh.name"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>

        <el-form-item
          label="联系电话"
          style="width: 100%"
          prop="zzh.phone"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="orgForm.zzh.phone"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
        <el-form-item
          label="职位"
          style="width: 100%"
          prop="zzh.job"
          :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
        >
          <el-input
            v-model="orgForm.zzh.job"
            placeholder="请输入..."
          ></el-input>
        </el-form-item>
        <div class="org_box">
          <el-form-item
            label="副总指挥"
            prop="fzzh.name"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 100%"
          >
            <el-input
              v-model="orgForm.fzzh.name"
              placeholder="请输入..."
            ></el-input>
          </el-form-item>

          <el-form-item
            label="联系电话"
            style="width: 100%"
            prop="fzzh.phone"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input
              v-model="orgForm.fzzh.phone"
              placeholder="请输入..."
            ></el-input>
          </el-form-item>
          <el-form-item
            label="职位"
            style="width: 100%"
            prop="fzzh.job"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input
              v-model="orgForm.fzzh.job"
              placeholder="请输入..."
            ></el-input>
          </el-form-item>
        </div>
        <div
          class="org_box"
          v-for="(item, index) in orgForm.member"
          :key="index"
        >
          <el-form-item
            label="成员"
            :prop="'member.' + index + '.name'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 100%"
          >
            <el-input
              v-model="item.name"
              placeholder="请输入..."
              style="width: 90%"
            ></el-input>
            <el-icon
              style="margin-left: 6px; cursor: pointer; color: #fff"
              @click="clearPeople(index)"
            >
              <Delete />
            </el-icon>
          </el-form-item>

          <el-form-item
            label="联系电话"
            style="width: 100%"
            :prop="'member.' + index + '.phone'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input v-model="item.phone" placeholder="请输入..."></el-input>
          </el-form-item>
          <el-form-item
            label="职位"
            style="width: 100%"
            :prop="'member.' + index + '.job'"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
          >
            <el-input v-model="item.job" placeholder="请输入..."></el-input>
          </el-form-item>
        </div>
        <div class="addPeople" @click="addPerson">添加成员</div>
      </el-form>
    </div>
    <div class="text_box" v-else>
      <el-input
        v-model="htmlData"
        :rows="10"
        type="textarea"
        placeholder="请输入..."
      />
    </div>
    <template #footer>
      <div class="dialog-footer">
        <div class="sub_btn sure_btn" @click="submit">确定</div>
      </div>
    </template>
  </el-dialog> -->

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
import { ref, computed, getCurrentInstance, reactive, onMounted } from "vue";
import useTaskStore from "@/store/modules/taskStore";
import {
  searchNodePlan,
  saveTaskPlan,
  getWordContent,
  clearWordContent,
} from "@/api/task/task";
import TaskPolice from "./taskAndPolice.vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from "element-plus";
let taskInfo = computed(() => useTaskStore().taskInfo); // 任务信息信息
const { proxy } = getCurrentInstance();
const activeName = ref("");
const activeNameChild = ref("")
const uploadBol = ref(false);
const loadingInstance = ref(null); // 用于存储 loading 实例
/*** 导入参数 */
const upload = reactive({
  id: taskInfo.value.id,
  type: "",
  // 设置上传的请求头部
  headers: { "x-auth-token": localStorage.getItem("token") },
  // 上传的地址
  url: import.meta.env.VITE_APP_BASE_API + "/task/uploadArchives",
});
const activeList = ref([
  { name: "指导思想", node: "指导思想" },
  { name: "基本情况", node: "基本情况" },
  { name: "组织领导", node: "组织领导" },
  { name: "任务分工及警力部署", node: "任务分工及警力部署" },
  { name: "工作要求", node: "工作要求" },
]);
const wordTextData = ref([]);
const wordTitle = ref([]);
const openBol = ref(false);
const htmlData = ref("");
const titleName = ref("");
const textData = ref("");
const selectVal = ref("");
const updateNodeId = ref(null);
const orgForm = reactive({
  zzh: { name: "", phone: "", job: "" },
  fzzh: { name: "", phone: "", job: "" },
  member: [],
});
const orgInfo = ref({});
// 添加成员
const addPerson = () => {
  let obj = { name: "", phone: "", job: "" };
  orgForm.member.push(obj);
};
// 删除成员
const clearPeople = (index) => {
  orgForm.member.splice(index, 1);
};
// 上传word
const uploadWord = () => {
  uploadBol.value = true;
};
// 清除word
const clearWord = () => {
  ElMessageBox.confirm("确认要清除数据？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let result = await clearWordContent({ businessId: taskInfo.value.id });
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
  console.log(response, file, upload.id);
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
// 选择面板
const changeActive = (e) => {
  if (e) {
    if (e !== "任务分工及警力部署") {
      let params = { taskId: taskInfo.value.id, planNode: e };
      selectVal.value = e;
      searchNodePlan(params).then((res) => {
        if (e === "组织领导") {
          console.log(res);
          if (res.data.id) {
            updateNodeId.value = res.data.id;
            orgInfo.value = res.data;
          } else {
            updateNodeId.value = null;
            orgInfo.value = {};
          }
        } else {
          if (res.data.data) {
            textData.value = res.data.data.msg;
            updateNodeId.value = res.data.id;
          } else {
            textData.value = "";
            updateNodeId.value = null;
          }
        }
      });
    }
  }
};
// 关闭面板
const closeModal = () => {
  useTaskStore().SET_TASKPLAN(false);
};
// 普通填写编辑
const openDialog = (item) => {
  openBol.value = true;
  titleName.value = item.name;
  htmlData.value = textData.value;
  if (item.name === "组织领导") {
    console.log(orgInfo.value);
    // orgForm
    if (orgInfo.value.id) {
      orgForm.zzh = orgInfo.value.data.zzh;
      orgForm.fzzh = orgInfo.value.data.fzzh;
      orgForm.member = orgInfo.value.data.member;
    }
  }
};
const cancel = () => {
  openBol.value = false;
  htmlData.value = "";
};
const submit = () => {
  if (titleName.value === "组织领导") {
    proxy.$refs["customForm"].validate((valid) => {
      if (valid) {
        console.log(orgForm);
        let data = {
          taskId: taskInfo.value.id,
          planNode: titleName.value,
          data: orgForm,
          id: updateNodeId.value,
        };
        saveTaskPlan(data).then((res) => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess("编辑成功");
            changeActive(titleName.value);
            cancel();
          }
        });
      }
    });
  } else {
    if (!htmlData.value) return proxy.$modal.msgWarning("不能为空");
    let data = {
      taskId: taskInfo.value.id,
      planNode: titleName.value,
      data: { msg: htmlData.value },
      id: updateNodeId.value,
    };
    saveTaskPlan(data).then((res) => {
      if (res.code === 0) {
        proxy.$modal.msgSuccess("编辑成功");
        changeActive(titleName.value);
        cancel();
      }
    });
  }
};
function modifyStylesWithDOM(htmlStr, styles) {
  const parser = new DOMParser();
  const doc = parser.parseFromString(htmlStr, "text/html");

  const walkDOM = (node) => {
    if (node.nodeType === Node.ELEMENT_NODE) {
      // 修改当前节点样式
      if (node.style) {
        if (styles.lineHeight) {
          node.style.lineHeight = styles.lineHeight;
        }
        if (styles.fontSize) {
          node.style.fontSize = styles.fontSize;
        }
      }

      // 递归处理子节点
      Array.from(node.children).forEach((child) => walkDOM(child));
    }
  };

  walkDOM(doc.body);
  return doc.body.innerHTML;
}
// 查询导入的文档内容
const getCardList = () => {
  getWordContent({ businessId: taskInfo.value.id }).then((res) => {
    console.log(res);
    if (res.data && res.data.length > 0) {
      wordTextData.value = res.data.filter((item) => item.title !== "标题"&&item.title !== "");
      wordTitle.value = res.data.filter((item) => item.title === "标题"&&item.title !== "");
    } else {
      wordTextData.value = [];
      wordTitle.value = [];
    }
  });
};
onMounted(() => {
  getCardList();
});
</script>
<style lang="scss" scoped>
.taskPlan_box {
  position: absolute;
  transform: scale(0.8);
  z-index: 3;
  right: -2%;
  bottom: -50px;
  top: -20px;
  width: 400px;
  background: linear-gradient(180deg, #0a1d64 0%, rgb(21, 30, 73) 100%);
  display: flex;
  flex-direction: column;

  .heard {
    height: 40px;
    background: url("../../../assets/panel/panel_bg.png") no-repeat;
    background-size: 100% 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: PingFangSC;
    font-weight: 400;
    font-size: 14px;
    color: #ffffff;
    text-shadow: 0px 0px 12px rgba(0, 106, 255, 0.8);
    font-style: normal;
    position: relative;

    .close_modal {
      position: absolute;
      right: 16px;
      top: 50%;
      transform: translateY(-50%);
      width: 20px;
      height: 20px;
      background: url("./img/close_new.png") no-repeat;
      background-size: 100% 100%;
      cursor: pointer;
    }
  }

  .plan_box {
    flex: 1;
    overflow: auto;
    padding: 14px 4px;
    .noData {
      color: #fff;
      position: absolute;
      left: 50%;
      top: 50%;
      transform: translate(-50%, -50%);
      font-size: 14px;
      font-weight: bold;
    }
    .word_title {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 0 10px 20px 10px;
      font-weight: bold;
      font-size: 14pt;
    }
    :deep(.el-collapse) {
      border: none;
    }

    :deep(.el-collapse-item) {
      margin-bottom: 7px;
    }

    :deep(.el-collapse-item__header) {
      // background: linear-gradient(
      //   180deg,
      //   #3a60f9 0%,
      //   rgba(16, 51, 194, 0.13) 98%
      // );
      background: rgba(28, 53, 155, 0.5) !important;
      border: 0px;
      transition: none;
      height: 36px;
      border-radius: 0;
      font-size: 16px;
      color: #fff;
      padding-left: 10px;
      line-height: 36px;
      position: relative;
      // font-size: 14pt;
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

      .edit {
        // position: absolute;
        width: 84px;
        height: 32px;
        background: #274eef;
        line-height: 32px;
        text-align: center;
        right: 20px;
        cursor: pointer;
        float: right;
      }

      .org_modal {
        .item_box {
          .title {
            font-size: 16px;
            margin-top: 8px;
          }

          .bottom_data {
            display: flex;

            .left {
              flex: 1;
            }

            .right {
              flex: 1;
            }
          }
        }
      }
    }
  }
}
.import_claer_btn {
  display: flex;
  align-items: center;
  justify-content: center;
  position: absolute;
  bottom: 10px;
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
