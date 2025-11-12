<template>
  <div class="childbox">
    <div class="line1">
      <div class="heard_name">
        <div class="d_name">任务数据</div>
      </div>
      <div class="box1">
        <el-button @click="Adduser" type="primary" class="add_btn"
          ><el-icon>
            <Plus />
          </el-icon>
          新增</el-button
        >
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
        <el-table-column prop="year" label="年份" align="center" />
        <el-table-column prop="onePlus" label="一级加强" align="center" />
        <el-table-column prop="one" label="一级任务" align="center" />
        <el-table-column prop="two" label="二级任务" align="center" />
        <el-table-column prop="three" label="三级任务" align="center" />
        <el-table-column prop="security" label="保安任务" align="center" />
        <el-table-column prop="other" label="非等级任务" align="center" />
        <el-table-column fixed="right" label="操作" align="center">
          <template #default="scope">
            <el-button
              @click="edit(scope.row)"
              style="color: #00ceff"
              link
              type="primary"
            >
              编辑
            </el-button>
            <el-button
              @click="deleterow(scope.row)"
              style="color: #00ceff"
              link
              type="primary"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 20px"
        background
        :current-page="query.page.pageNo"
        :page-size="query.page.pageSize"
        :total="total"
        @current-change="handlePageChange"
        layout="total,prev, pager, next"
      ></el-pagination>
    </div>
  </div>

  <el-dialog
    v-model="addUser"
    width="800px"
    @close="cancel"
    align-center
    :destory-on-close="true"
    :close-on-click-modal="false"
    class="my_Dialog"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">
          {{ titleType === "add" ? "新增任务数据" : "编辑任务数据" }}
        </div>
      </div>
    </template>
    <el-form
      ref="customForm"
      :inline="true"
      :model="form"
      :label-suffix="'：'"
      :label-width="120"
      class="customForm"
      size="large"
    >
      <el-row>
        <el-col :span="24">
          <el-form-item
            label="选择年份"
            prop="year"
            :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
            style="width: 300px"
          >
            <el-date-picker
              v-model="form.year"
              type="year"
              format="YYYY"
              value-format="YYYY"
              :editable="false"
              :disabled="titleType!== 'add'"
              placeholder="选择年份"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            label="一级加强"
            style="width: 100%"
            prop="onePlus"
            :rules="[
              {
                required: true,
                trigger: 'blur',
                validator: validatePositiveInteger,
              },
            ]"
          >
            <el-input
              v-model.number="form.onePlus"
              placeholder="请输入数值"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            label="一级任务"
            style="width: 100%"
            prop="one"
            :rules="[
              {
                required: true,
                trigger: 'blur',
                validator: validatePositiveInteger,
              },
            ]"
          >
            <el-input
            v-model.number="form.one"
              placeholder="请输入数值"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            label="二级任务"
            style="width: 100%"
            prop="two"
            :rules="[
              {
                required: true,
                trigger: 'blur',
                validator: validatePositiveInteger,
              },
            ]"
          >
            <el-input
            v-model.number="form.two"
              placeholder="请输入数值"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            label="三级任务"
            style="width: 100%"
            prop="three"
            :rules="[
              {
                required: true,
                trigger: 'blur',
                validator: validatePositiveInteger,
              },
            ]"
          >
            <el-input
            v-model.number="form.three"
              placeholder="请输入数值"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            label="安保任务"
            style="width: 100%"
            prop="security"
            :rules="[
              {
                required: true,
                trigger: 'blur',
                validator: validatePositiveInteger,
              },
            ]"
          >
            <el-input
            v-model.number="form.security"
              placeholder="请输入数值"
            ></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item
            label="非等级任务"
            style="width: 100%"
            prop="other"
            :rules="[
              {
                required: true,
                trigger: 'blur',
                validator: validatePositiveInteger,
              },
            ]"
          >
            <el-input
            v-model.number="form.other"
              placeholder="请输入数值"
            ></el-input>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <div @click="cancel" class="sub_btn">取消</div>
        <div class="sub_btn sure_btn" @click="submit">保存</div>
      </div>
    </template>
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
import { ElMessage, ElMessageBox } from "element-plus";
import { addTaskStaticData,getTaskDataListPage,delTaskStaticData,updateTaskStaticData } from "@/api/basic/index";
const { proxy } = getCurrentInstance();
// 自定义校验规则
const validatePositiveInteger = (rule, value, callback) => {
  if (value === "" || value === undefined || value === null) {
    callback(new Error("请输入数字"));
  } else if (!Number.isInteger(value) || value < 0) {
    callback(new Error("只能输入0和正整数"));
  } else {
    callback();
  }
};
const query = reactive({
  page: {
    pageNo: 1,
    pageSize: 15,
  },
  entity: {
    year: "",
  },
});
let total = ref(0);
let tableData = ref([]);
let addUser = ref(false);
let customForm = ref(null);
let titleType = ref("");
// 新增参数
let form = reactive({
  id: null,
  year: "",
  onePlus: 0,
  one: 0,
  two:0,
  three:0,
  security:0,
  other:0
});

const cancel = () => {
  resetForm("customForm");
};
const submit = async () => {
  proxy.$refs["customForm"].validate((valid) => {
    if (valid) {
      if (titleType.value === "edit") {
        // 编辑
        updateTaskStaticData(form).then((res) => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess("修改成功！");
            resetForm("customForm");
            getUserListData();
          }
        });
      } else {
        // 新增
        addTaskStaticData(form).then((res) => {
          if (res.code === 0) {
            resetForm("customForm");
            proxy.$modal.msgSuccess("新增成功！");
            getUserListData();
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
    addUser.value = false;
  }
};
//新增用户
function Adduser() {
  form.id = null;
  titleType.value = "add";
  addUser.value = true;
}
function handlePageChange(newPage) {
  query.page.pageNo = newPage;
  nextTick(() => {
    getUserListData();
  });
}
onMounted(() => {
  getUserListData();
});
const getUserListData = () => {
  getTaskDataListPage(query).then((res) => {
    if (res.code === 0) {
      tableData.value = res.data.records;
      total.value = res.data.totalCount;
    }
  });
};
//编辑用户信息
function edit(row) {
  console.log(row);
  form.year = row.year;
  form.onePlus = row.onePlus;
  form.one = row.one;
  form.two = row.two;
  form.three = row.three;
  form.security = row.security;
  form.other = row.other;
  form.id = row.id;
  titleType.value = "edit";
  addUser.value = true;
}
//删除某一行
function deleterow(row) {
  console.log(row);
  ElMessageBox.confirm("确认要删除该条数据？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let result = await delTaskStaticData({ id: row.id });
      if (result.code === 0) {
        getUserListData();
        ElMessage({
          type: "success",
          message: "已删除完成",
        });
      }
    })
    .catch(() => {});
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
    height: 68px;

    .add_btn {
      width: 88px;
      height: 36px;
      background: #274eef;
      border-radius: 2px 2px 2px 2px;
      border: 0px;
      font-size: 14px;
    }

    .serach {
      height: 36px;
      margin-left: 20px;
    }

    :deep(.el-input-group__append) {
      background: #274eef;
      color: #fff;
      box-shadow: none;
      cursor: pointer;
      padding: 0 8px;
    }

    :deep(.el-input__wrapper) {
      background: linear-gradient(
        360deg,
        rgba(0, 20, 70, 0.8) 0%,
        rgba(0, 39, 105, 0.7088) 100%
      );
      box-shadow: none;
      border: 1px solid #0424aa;
    }

    :deep(.el-input__inner) {
      color: #fff;
    }
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
