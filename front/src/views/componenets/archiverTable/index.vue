<template>
  <div class="app-container home">
    <el-dialog
      v-model="openBol"
      width="100%"
      @close="cancel"
      align-center
      :destory-on-close="false"
      :close-on-click-modal="false"
      class="my_Dialog"
    >
      <template #header>
        <div class="heard_name">
          <div class="d_name">列表查询</div>
        </div>
      </template>
      <div class="archiverTable">
        <div class="tableContainer">
          <div class="tBody">
            <div class="searchFormContainer">
              <el-form :inline="true" :model="DataDialog.formInline">
                <el-form-item label="档案名称">
                  <el-select
                    v-model="DataDialog.formInline.archiver"
                    placeholder=""
                    @change="archiverChanged"
                    style="width: 200px;"
                  >
                    <el-option
                      v-for="item in DataDialog.archivers"
                      :label="item.name"
                      :value="item.id"
                      :key="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="档案内容">
                  <el-select v-model="DataDialog.formInline.region" placeholder="" style="width: 200px;">
                    <el-option
                      v-for="item in DataDialog.archiverItems"
                      :label="item.archives_name"
                      :value="item.id"
                      :key="item.id"
                    ></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">查询</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="mytable">
              <!-- 动态生成table -->
              <!-- key 是必须的，强制组件重置，否则表头会有缓存的bug -->
              <archiverTemplate
                :templateData="DataDialog.templateData"
                ref="tables"
                :key="DataDialog.archiverKey"
                v-if="showTabs"
              />

              <div
                class="pagination"
                style="padding: 20px 0px"
                v-if="DataDialog.tableData.length > 0"
              >
                <el-pagination
                  @current-change="handleCurrentChange"
                  :current-page.sync="DataDialog.pagination.page"
                  :page-size="DataDialog.pagination.size"
                  layout="prev, pager, next, jumper"
                  :total="DataDialog.pagination.total"
                  style="text-align: center"
                >
                </el-pagination>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script setup>
import {
  reactive,
  onMounted,
  defineEmits,
  onBeforeUnmount,
  onUnmounted,
  nextTick,
  ref
} from "vue";
import emitter from "@/utils/emitter";
import archiverTemplate from "./components/archiverTemplate.vue";
import { ElMessage } from "element-plus";
import { getInfoByArchivesId, getPointInfoByArchivesId } from "@/api/index";
const emit = defineEmits(["hideDialog"]);
const openBol = ref(true);
const showTabs = ref(false)
const tables = ref(null)
const DataDialog = reactive({
  archivers: [],
  archiverItems: [],
  archiverName: "",
  archiverItemName: "",
  templateData: {},
  formInline: {
    archiver: "",
    region: "",
  },
  tableData: [],
  pagination: {
    page: 1,
    size: 10,
    total: 0,
  },
  archiverKey: new Date().getTime(),
});
onMounted(() => {
  emitter.on("eventTableinfoDialog", (res) => {
    DataDialog.archivers = res.archiver;
    DataDialog.formInline.archiver = res.id;
    nextTick(() => {
      getInfoByArchivesIdFun();
    });
  });
});
onBeforeUnmount(() => {
  emitter.off("eventTableinfoDialog");
});
onUnmounted(() => {
  emitter.off("eventTableinfoDialog");
});
const cancel = () => {
  emit("hideDialog");
};
const archiverChanged = (val) => {
  getInfoByArchivesIdFun();
};
const onSubmit = () => {
  showTabs.value = false
  if (
    DataDialog.formInline.archiver == "" ||
    DataDialog.formInline.region == ""
  ) {
    ElMessage.error("档案名称或内容不能为空");
    return;
  }

  getPointInfoByArchivesIdFun();
  for (let archiver of DataDialog.archivers) {
    if (archiver.id == DataDialog.formInline.archiver) {
      DataDialog.archiverName = archiver.name;
      break;
    }
  }
  for (let archiverItem of DataDialog.archiverItems) {
    if (archiverItem.id == DataDialog.formInline.region) {
      DataDialog.archiverItemName = archiverItem.archives_name;
      break;
    }
  }
  DataDialog.archiverKey = new Date().getTime();

  DataDialog.templateData = {
    tableData: DataDialog.tableData,
    archiverName: DataDialog.archiverName,
    archiverItemName: DataDialog.archiverItemName,
  };
  nextTick(()=>{
    showTabs.value = true
  })
};
const handleCurrentChange = (val) => {
  DataDialog.pagination.page = val;
  getPointInfoByArchivesIdFun();
};
const getInfoByArchivesIdFun =  () => {
  let param = {
    archivesId: DataDialog.formInline.archiver,
  };
  getInfoByArchivesId(param).then((res) => {
    if (res.code === 0) {
      DataDialog.archiverItems = res.data || [];
      DataDialog.formInline.region = DataDialog.archiverItems[0].id;
      onSubmit();
    }
  });
};
const getPointInfoByArchivesIdFun = async() => {
  let param = {
    archivesId: DataDialog.formInline.region,
    page: DataDialog.pagination.page - 1,
    size: DataDialog.pagination.size,
  };
  let res = await getPointInfoByArchivesId(param)
  if (res.code === 0) {
      let { page = 0, size = 10, total = 0, content } = res.data.field_data;
      DataDialog.pagination.page = page + 1;
      DataDialog.pagination.size = size;
      DataDialog.pagination.total = total;
      DataDialog.tableData = content.map((val) => {
        return val.data;
      });
      DataDialog.templateData = {
        tableData: DataDialog.tableData,
        archiverName: DataDialog.archiverName,
        archiverItemName: DataDialog.archiverItemName,
      };
      setTimeout(()=>{
        showTabs.value = true
      },1500)
    }
};
</script>
<style lang="scss" scoped>
:deep(.my_Dialog) {
  background: url("@/assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
  .heard_name {
    background: url("@/assets/panel/right_panel.png") no-repeat;
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
:deep(.el-dialog__headerbtn) {
  position: absolute;
  top: 10px;
  right: 20px;
  .el-dialog__close {
    color: greenyellow;
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
:deep(.archiverTable) {
  width: 100%;
  height:800px;
  .tableContainer {
    width: 100%;
    box-sizing: border-box;
    height: calc(100% - 80px);
    padding: 10px 10px;
    .tBody {
      width: 100%;
      height: 100%;
      padding: 0px 10px;
      box-shadow: inset 0px 0px 20px 20px rgba(11, 102, 181, 0.2),
        inset 0px 0px 8px 5px rgba(11, 102, 181, 0.5);
      border: 1px solid #3baafd;
      .searchFormContainer {
        height: 60px;
        display: flex;
        align-items: center;
        padding: 0px 10px;
        margin-top: 20px;
      }
      .mytable {
        // position: relative;
        height: calc(100% - 60px);
        overflow: auto;
        // z-index: 1;
        .pagination {
          position: absolute;
          width: 100%;
          bottom: 20px;
        }
      }
    }
    .el-input__inner {
      // background: #07497f;
      // border: 1px solid #3baafd;
      color: #07497f;
    }
    .el-form-item__label {
      color: #3aa8fc !important;
    }
    .el-form-item {
      margin-bottom: 0px;
    }
    .el-table {
      color: #fff;
    }
    .el-table thead {
      color: #fff;
      text-align: center;
    }
    .el-table__row:hover > td {
      background-color: #0266c2 !important;
      color: #fff !important;
    }
    .el-table::before {
      display: none;
    }
    .el-table--border {
      border: 1px solid #267abc;
    }
    .el-table--group::after,
    .el-table--border::after {
      width: 0px;
    }
    .el-table--border th.el-table__cell {
      border-color: #267abc;
    }
    .el-table th.el-table__cell {
      background-color: transparent !important;
    }
    .el-table,
    .el-table__expanded-cell {
      background-color: transparent !important;
    }
    .el-table tr {
      background-color: transparent !important;
    }
    .el-table th.el-table__cell.is-leaf,
    .el-table td.el-table__cell {
      border-color: #267abc;
    }
    .el-table th.el-table__cell.is-leaf,
    .el-table td.el-table__cell {
      border-bottom-color: #267abc;
    }
    .el-pagination .btn-prev,
    .el-pagination .btn-next {
      background-color: transparent !important;
      color: #fff;
    }
    .el-pagination button:disabled {
      color: #1890ff !important;
    }
    .el-pager li {
      background: transparent !important;
      color: #fff;
    }
    .el-pager li.active {
      color: #1890ff !important;
    }
    .el-pagination__jump {
      color: #fff;
    }
    .el-table__header thead th div,
    .el-table__body tbody td div {
      display: flex;
      justify-content: center;
      align-items: center;
    }
  }
  .el-image-viewer__mask {
  // z-index: 9999 !important;
}

.el-image-viewer__wrapper {
  z-index: 10000 !important;
}

/* 压制表格固定列层级 */
.el-table__fixed,
.el-table__fixed-right {
  z-index: 1 !important; /* 原值2 */
}

/* 禁用表格自身层级提升 */
.el-table {
  position: static !important; /* 消除堆叠上下文 */
}

.el-table::before {
  display: none !important; /* 移除默认边框阴影 */
}
}
</style>
