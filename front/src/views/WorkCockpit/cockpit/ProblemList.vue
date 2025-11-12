<template>
  <div class="content-box-problem">
    <div class="content-header">问题清单统计</div>
    <div class="content">
      <div class="header-box">
        <div class="header-left">
          <span class="label">总数</span>
          <span class="total">{{ countData.total }}</span>
          <span class="unit">条</span>
        </div>
        <div class="header-left" style="margin-left: 30px;">
          <span class="label">类型</span>
          <span class="total">{{ tableData.length }}</span>
          <span class="unit">种</span>
        </div>
      </div>
      <!-- 查看详情 -->
       <div class="checkDetails" @click="checkInfo('all','all')">查看详情</div>
      <!-- 常备方案任务列表 -->
      <div class="task-list-box" >
        <el-table
          :data="tableData"
          empty-text="暂无数据"
          :show-header="false"
          height="100%"
        >
          <el-table-column
            type="index"
            label="序号"
            align="center"
            width="60"
          />
          <el-table-column
            prop="content"
            label="名称"
            align="left"
            show-overflow-tooltip
          >
          <template #default="scope">
              <div v-html="scope.row.content"></div>
            </template>
          </el-table-column>
          <el-table-column prop="" label="操作" align="right">
            <template #default="scope">
              <span class="view" @click="checkPlanTask(scope.row)">查看</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>


  <el-dialog
    v-model="checkBol"
    width="800px"
    align-center
    :append-to-body="true"
    :destory-on-close="true"
    :close-on-click-modal="false"
    class="my_Dialog_library"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">
          问题清单库
        </div>
      </div>
    </template>
    <div class="tablebox">
      <el-scrollbar>
        <div style="margin: 0 10px 0 6px">
          <el-tree
            accordion
            ref="treeRef"
            :data="treeData"
            node-key="id"
            :expand-on-click-node="true"
            highlight-current
          >
            <template #default="{ node, data }">
            <div class="custom-tree-node">
            <div class="left" v-html="data.content"></div>
        </div>
            </template>
          </el-tree>
        </div>
      </el-scrollbar>
    </div>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, computed,nextTick } from "vue";
import { useRouter } from "vue-router";
import { getProblemNum, getProblemList, checkProblemInfo } from "@/api/basic/index";
const tableData = ref([]);
const treeData = ref([])
const countData = ref({
  total: 0,
  type:0
});
const checkBol = ref(false)

const checkInfo = (type,idVal) => {
  if(type==='all'){
    getProblemList().then(res=>{
      treeData.value = res.data
      checkBol.value = true
    })
  }else{
    getProblemList({id:idVal}).then(res=>{
      checkBol.value = true
      treeData.value = res.data
    })
  }
}

// 任务数量统计
const getTaskCount = () => {
  getProblemNum().then((res) => {
    countData.value.total = res.data
  });
  getProblemList().then((res) => {
    tableData.value = res.data
  });
};

// 查看详情
const checkPlanTask = async(row) => {
  checkInfo('one',row.id)
}
onMounted(() => {
  getTaskCount();
});
</script>

<style lang="scss" scoped>
.content-box-problem {
  width: 400px;
  height: 40%;
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  margin-bottom: 10px;
  position: absolute;
  top: 66.5%;
  right: 10px;
  bottom: 20px;
  z-index: 23;
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
  }

  .content {
    padding: 20px;
    box-sizing: border-box;
    height: calc(100% - 48px);
    position: relative;
    .checkDetails{
      position: absolute;
      font-family: PingFang SC;
      font-size: 12px;
      color: #A1D9FD;
      cursor: pointer;
      right: 20px;
      top: 40px;
    }
    .header-box {
      display: flex;
      align-items: flex-end;
      // justify-content: space-between;

      .header-left {
        display: flex;
        align-items: flex-end;

        .label {
          font-family: YouSheBiaoTiHei;
          font-size: 18px;
        }

        .total {
          font-family: DIN;
          font-size: 40px;
          line-height: 30px;
          color: #00ceff;
          margin: 0 4px 0 8px;
        }

        .unit {
          font-size: 14px;
        }
      }

      .header-right {
        font-family: Source Han Sans CN;
        font-size: 12px;
        color: rgba($color: #fff, $alpha: 0.4);
        cursor: pointer;

        &:hover {
          color: #00ceff;
        }
      }
    }

    .btn-box {
      display: flex;
      align-items: center;
      gap: 8px;
      padding: 8px 0;
      box-sizing: border-box;
      // border-bottom: 1px solid rgba(38, 68, 173, 0.5);
      margin-bottom: 8px;
      margin-top: 16px;
      background: rgba(0, 0, 0, 0.1);
      .btn {
        flex: 1;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        text-align: center;
        font-family: Source Han Sans CN;
        font-size: 14px;
        margin-right: 2px;
        cursor: pointer;
        .icon_name{
          width: 100%;
          height: 62px;
          display: flex;
          align-items: center;
          justify-content: center;
          background: url("@/assets/basic/组116357@1x.png") no-repeat;
          background-size: 100% 100%;
        }
        .text_name{
          font-size: 16px;
          font-weight: 350;
          color: #fff;
          margin-top: 16px;
        }
        .boldFont {
          font-size: 16px;
          font-weight: bold;
          background: linear-gradient(180deg, #FFFFFF 34%, #68ACFF 75%);
          -webkit-background-clip: text;
          background-clip: text;
          color: transparent;
          // font-family: D-DIN;
          // font-size: 20px;
          // font-weight: normal;
          // line-height: 20px;
          // letter-spacing: 0px;
          // color: #00ceff;
          // margin-right: 2px !important;
        }
      }
      :last-child {
        margin-right: 0px;
      }
      // .btn-press {
      //   background: url("@/assets/basic/border@1.png") no-repeat;
      //   background-size: 100% 100%;
      // }
    }

    .task-count-box {
      width: 100%;
      display: flex;
      gap: 4px;
      margin-bottom: 8px;

      .rect {
        display: flex;
        align-items: center;
        justify-content: center;
        background: rgba(0, 0, 0, 0.1);
        box-sizing: border-box;
        border: 1px solid #182659;
      }

      .level-box {
        display: flex;
        flex-wrap: wrap;
        gap: 4px;
        font-size: 14px;

        .level-item {
          width: 116px;
          height: 34px;

          .label {
            margin-right: 17px;
          }

          .count {
            font-family: DIN;
          }
        }
      }
    }

    .task-list-box {
      height: calc(100% - 40px);
      margin-top: 16px;
      .view {
        color: #00ceff;
        cursor: pointer;
      }

      .bold {
        font-weight: bold;
      }
    }

    .color-enhance {
      color: #ff4040;
    }

    .color-level-one {
      color: #ff6633;
    }

    .color-level-two {
      color: #ffe433;
    }

    .color-level-three {
      color: #33ff77;
    }

    .color-level-secure {
      color: #00a6ff;
    }
  }
}

:deep(.el-table) {
  --el-table-header-bg-color: #112a6b;
  --el-table-row-hover-bg-color: transparent;
  --el-table-current-row-bg-color: transparent;
  --el-table-bg-color: transparent;
  --el-table-tr-bg-color: transparent;
  --el-table-text-color: #fff;
  font-size: 14px;
}

:deep(.el-table td.el-table__cell) {
  border-bottom: 0;
}

:deep(.el-table__inner-wrapper:before) {
  display: none;
}

:deep(.el-table--small .el-table__cell) {
  padding: 9px 0;
}

:deep(.el-table tr:nth-child(odd)) {
  background: rgba(0, 0, 0, 0.1);
}

:deep(.el-table tr:hover) {
  background: url("@/assets/workcockpit/tr_hover.png") !important;
}
.tablebox{
  width: 100%;
  height: 500px;
  overflow: auto;
  .custom-tree-node {
    display: flex;
    align-items: center;
    width: 95%;
    min-height: 48px;
    .node-name {
      display: inline-block;
      max-width: 95px;
      overflow: hidden;
      text-overflow: ellipsis;
      text-wrap: nowrap;
      font-size: 12px;
    }
    .left{
      width: 100%;
      // display: flex;
      flex-wrap: wrap;
      word-break: break-all;
      white-space: pre-wrap;
      padding: 4px 0;
      line-height: 20px;
    }
  }

:deep(.el-tree) {
  background: transparent;
  color: #fff;
  --el-tree-node-hover-bg-color: rgba(21, 30, 73, 0.6984);
  --el-tree-node-content-height: 32px;
}

:deep(
    .el-tree--highlight-current
      .el-tree-node.is-current
      > .el-tree-node__content
  ) {
  background-color: rgba(21, 30, 73, 0.6);
}


:deep(.el-tree-node__content){
  height: auto;
  background: rgba(31, 76, 152, 0.6);
}
:deep(.el-tree-node__children){
  .el-tree-node__content{
    background: rgba(31, 76, 152, 0.16);
  }
  .el-tree-node__children{
    .el-tree-node__content{
    background: rgba(0,0,0, .2);
  }
  }
}
}
</style>
<style>
  .my_Dialog_library{
    background: url("@/assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;
  }
  .my_Dialog_library .heard_name{
    background: url("@/assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;
  }
  .my_Dialog_library .heard_name .d_name{
    font-family: YouSheBiaoTiHei;
      font-weight: 400;
      font-size: 17px;
      color: #ffffff;
      height: 22px;
      line-height: 20px;
      padding-left: 20px;
  }
 .my_Dialog_library .el-dialog__headerbtn:active{
  background-color: transparent !important;
    outline: none !important;
    box-shadow: none !important;
 }
 .my_Dialog_library .el-dialog__headerbtn{
  position: absolute;
    top: 10px;
    right: 20px;
 }
 .my_Dialog_library .el-dialog__headerbtn .el-dialog__close{
  color: #fff;
  font-size: 20px;
 }
 .my_Dialog_library .el-dialog__headerbtn :focus, .el-dialog__headerbtn:hover{
  outline: none;
  box-shadow: none;
 }
 .my_Dialog_library .el-button:focus{
  outline: none;
 }
 .my_Dialog_library .el-dialog__body{
  padding-right: 30px;
 }
</style>
