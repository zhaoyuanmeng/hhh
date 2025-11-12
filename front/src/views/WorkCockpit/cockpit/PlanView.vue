<template>
  <div class="content-box-plan">
    <div class="content-header">常备方案统计</div>
    <div class="content">
      <div class="header-box">
        <div class="header-left">
          <span class="label">常备方案总数</span>
          <span class="total">{{ countData.total }}</span>
          <span class="unit">个</span>
        </div>
      </div>
      <!-- 查看详情 -->
       <div class="checkDetails" @click="checkDetailsFun">查看详情</div>
      <!-- 按钮 -->
      <div class="btn-box">
        <div
          class="btn"
          v-for="(item, index) in planList"
          :key="index"
          @click="handleSearch(item)"
        >
          <div class="icon_name"><p class="boldFont">{{ item.num }}</p></div>
          <div class="text_name">{{ item.taskName }}</div>
        </div>
      </div>
      <!-- 常备方案任务列表 -->
      <!-- <div class="task-list-box" >
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
            prop="sceneName"
            label="名称"
            align="left"
            show-overflow-tooltip
          />
          <el-table-column prop="" label="操作" align="right">
            <template #default="scope">
              <span class="view" @click="checkPlanTask(scope.row)">查看</span>
            </template>
          </el-table-column>
        </el-table>
      </div> -->
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed,nextTick } from "vue";
import { useRouter } from "vue-router";
import { getPlanNumber, getPlanListApi, getPlanStaticData } from "@/api/plan";
// store
import usePlanTaskStore from "@/store/modules/planTask";
const PlanTaskStore = usePlanTaskStore();
const router = useRouter();
const planList = ref([
  { num: "2", taskName: "现场", type: "xc", style: "2" },
  { num: "3", taskName: "住地", type: "zd", style: "3" },
  { num: "4", taskName: "高速", type: "gs", style: "4" },
  { num: "5", taskName: "高铁", type: "gt", style: "5" },
  { num: "1", taskName: "城市道路", type: "city", style: "1" },
]);
const tableData = ref([
  { name: "xxxx现场" },
  { name: "xxxxxx现场" },
  { name: "xxxxxxxxx现场" },
]);
const countData = ref({
  total: 6,
});
const activeBtn = ref("xc");
// 查询
const handleSearch = (item) => {
  activeBtn.value = item.type;
  getPlanListApi({ type: item.style }).then((res) => {
    tableData.value = res.data;
  });
};

// 任务数量统计
const getTaskCount = () => {
  getPlanNumber().then((res) => {
    countData.value.total = res.data;
  });
  getPlanStaticData().then((res) => {
    planList.value[4].num = res.data.a1;
    planList.value[0].num = res.data.a2;
    planList.value[1].num = res.data.a3;
    planList.value[2].num = res.data.a4;
    planList.value[3].num = res.data.a5;
  });
};

// 查看
const checkPlanTask = async (row) => {
  console.log(row);
  PlanTaskStore.set_routerGo(true);
  let data = {
    id: row.id,
    taskId:  row.taskId,
    sceneName: row.sceneName,
    type: row.type,
    basicDataId: row.basicDataId,
    basicDataName: row.basicDataName,
    time: row.time,
    keyFrames:row.keyFrames,
    thirdKeyFrames: row.thirdKeyFrames,
    thirdTime:row.thirdTime,
    sceneRoadLength: row.sceneRoadLength,
    sceneRoadTime: row.sceneRoadTime,
    pointInfoList: row.pointInfoList,
    drawInfoList: row.drawInfoList,
    viewData: row.viewData,
    schemeFlag: row.schemeFlag,
    level: row.level,
    schemeId: row.schemeId,
  };
  PlanTaskStore.set_checkInfo(data);
  // router.push({ name: "Task", query: data });
  router.push({ name: "Task"});
  
  // let data = {
  //   id:'id',
  //   taskId:'taskId',
  //   sceneName: 'sceneName',
  //   type:'type',
  //   basicDataId:'basicDataId',
  //   basicDataName: 'basicDataName',
  //   time:'time',
  //   keyFrames:'keyFrames',
  //   thirdKeyFrames:'thirdKeyFrames',
  //   thirdTime:'thirdTime',
  //   sceneRoadLength: 'sceneRoadLength',
  //   sceneRoadTime: 'sceneRoadTime',
  //   pointInfoList: 'pointInfoList',
  //   drawInfoList: 'drawInfoList',
  //   viewData: 'viewData',
  //   schemeFlag:'schemeFlag',
  //   level: 'level',
  //   schemeId: 'schemeId',
  // }
  // router.push({ name: "Task", state: data });
};
// 查看详情
const checkDetailsFun = async() => {
  PlanTaskStore.set_routerGo(true);
  PlanTaskStore.set_checkDetails(true)
  nextTick(()=>{
    router.push({ name: "Task"});
  })
  
}
onMounted(() => {
  getTaskCount();
  handleSearch({ id: "2", taskName: "现场", type: "xc",style:'2' });
});
</script>

<style lang="scss" scoped>
.content-box-plan {
  // width: 400px;
  // height: calc(120vh - 62px );
  // background: linear-gradient(180deg, #0A1D64 0%, rgba(21, 30, 73, 0.6984) 100%);
  // margin-bottom: 10px;
  // position: absolute;
  // top: 72px;
  // right: 10px;
  // bottom: 20px;
  // z-index: 23;
  // transform: scale(0.8);
  // transform-origin: right top;
  width: 400px;
  height: 25%;
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  margin-bottom: 10px;
  position: absolute;
  top: 45%;
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
      justify-content: space-between;

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
      height: calc(100% - 120px);
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
</style>
