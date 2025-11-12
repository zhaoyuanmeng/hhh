<template>
  <div class="content-box">
    <div class="content-header">任务统计</div>
    <div class="content">
      <div class="header-box">
        <div
          class="header-left"
          style="margin-right: 30px"
          :class="{ active_name: tabs === 0 }"
          @click="changeTba(0)"
        >
          <span class="label">任务总数</span>
          <span class="total">{{ taskYearData }}</span>
          <span class="unit">个</span>
          <el-date-picker
            class="custom_year"
            v-model="year"
            type="year"
            format="YYYY"
            value-format="YYYY"
            :editable="false"
            placeholder="全部"
            style="width: 70px"
            @change="changeYear"
          />
        </div>
        <div
          class="header-left"
          :class="{ active_name: tabs === 1 }"
          @click="changeTba(1)"
        >
          <span class="label">任务预演</span>
          <span class="total">{{ countData.total }}</span>
          <span class="unit">个</span>
        </div>
      </div>
      <!-- 按钮 -->
      <!-- <div class="btn-box">
        <div class="btn" :class="{ 'btn-press': activeBtn === 'all' }" @click="handleSearch('all')">
          全部任务<span class="all-color">{{ countData.total }}</span>个
        </div>
        <div class="btn" :class="{ 'btn-press': activeBtn === 'executing' }" @click="handleSearch('executing')">
          执行中任务<span class="run-color">{{ countData.executing }}</span>个
        </div>
        <div class="btn" :class="{ 'btn-press': activeBtn === 'executed' }" @click="handleSearch('executed')">已执行任务<span
            class="done-color">{{ countData.executed }}</span>个
        </div>
      </div> -->
      <!-- 任务数统计 -->
      <div class="task-count-box" v-if="tabs === 0">
        <div class="level-box">
          <div
            class="rect level-item"
            v-for="item in taskCountInfo"
            :key="item.label"
          >
            <span class="label">{{ item.label }}</span>
            <span :class="`count ${item.textColor}`">{{ item.count }}</span>
          </div>
        </div>
      </div>
      <!-- echarts图表 -->
      <div class="task-list-box" v-if="tabs === 0">
        <el-carousel style="height:100%">
    <el-carousel-item v-for="(item,index) in showImgList" :key="index" class="banna_box" style="height: 100%;">
      <BarChart :options="chartData" style="height: calc(100% - 80px)" v-if="index===0"/>
      <BarChart :options="chartData1" style="height: calc(100% - 80px)" v-else :title="'历年等级数量'"/>
    </el-carousel-item>
  </el-carousel>
        <!-- <BarChart :options="chartData"/> -->
      </div>
      <!-- 任务列表 -->
      <div
        class="task-list-box"
        v-if="tabs === 1"
        style="margin-top: 8px; height: calc(100% - 56px)"
      >
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
            prop="taskName"
            label="名称"
            align="left"
            width="120"
            show-overflow-tooltip
          />
          <el-table-column label="等级" align="center">
            <template #default="scope">
              <div :class="`${levelColorMap[scope.row.taskLevel]}`">
                <span :class="{ bold: scope.row.taskLevel === '一级加强' }">{{
                  scope.row.taskLevel
                }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="" label="操作" align="center">
            <template #default="scope">
              <span class="view" @click="viewDetail(scope.row)">查看</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { drawGsGtData,layerInit } from "@/components/SmartMap/js/utils";
import $modal from '@/utils/modal'
import {
  getTaskList,
  statisticalAllLevel,
  getExecutedList,
  getExecutingList,
  statisticalQuantity,
  statisticalExecutingLevel,
  statisticalExecutedLevel,
  getRelatedFeatureDataOfTask,
  getDrawDataOfTask
} from "@/api/workCockpit/index.js";
import { getIndexTaskData,getTaskDataForYear } from "@/api/basic/index";
import { closeFloors } from "@/components/SmartMap/js/utils";
// store
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import BarChart from "@/components/chart";
const WorkCockpitStore = useWorkCockpitStore();
const taskItem = computed(() => WorkCockpitStore.taskItem);
const year = ref("");
const chartData = ref([])
const chartData1 = ref([])
const showImgList = ref([{},{}])
const tableData = ref([{ id: 1, taskName: "测试", taskLevel: "一级任务" }]);
const tabs = ref(0);
const taskYearData = ref(0);
const countData = ref({
  total: 0,
  executed: 0,
  executing: 0,
});

// 任务等级统计
const taskCountInfo = ref([
  { label: "一级加强", textColor: "color-enhance", count: 0 },
  { label: "一级任务", textColor: "color-level-one", count: 0 },
  { label: "二级任务", textColor: "color-level-two", count: 0 },
  { label: "三级任务", textColor: "color-level-three", count: 0 },
  { label: "保卫任务", textColor: "color-level-secure", count: 0 },
  { label: "非等级任务", textColor: "color-level-three", count: 0 },
]);

// 颜色枚举
const levelColorMap = {
  一级加强: "color-enhance",
  一级任务: "color-level-one",
  二级任务: "color-level-two",
  三级任务: "color-level-three",
  保卫任务: "color-level-secure",
  非等级任务: "color-level-three",
};

const activeBtn = ref("all");
const changeTba = (e) => {
  tabs.value = e;
};
// 查询
const handleSearch = (type) => {
  activeBtn.value = type;
  if (type === "all") {
    getAllTaskList();
    // getAllCount();
  } else if (type === "executing") {
    getExecutingTaskList();
    // getExecutingCount();
  } else {
    getExecutedTaskList();
    // getExecutedCount();
  }
};

// 任务数量统计
const getTaskCount = () => {
  statisticalQuantity().then((res) => {
    countData.value = res.data;
  });
  getIndexTaskData({}).then(res=>{
    let datas = [{total:res.data.oneplus,year:'一级加强'},
    {total:res.data.one,year:'一级任务'},
    {total:res.data.two,year:'二级任务'},
    {total:res.data.three,year:'三级任务'},
    {total:res.data.security,year:'保卫任务'},
    {total:res.data.other,year:'非等级任务'}
    ]
    chartData1.value = datas
  })
};

// 数据处理
const processData = (res) => {
  if (!res.data?.length) return;
  taskCountInfo.value.map((item) => (item.count = 0));
  res.data.forEach((item) => {
    const task = taskCountInfo.value.find((el) => el.label === item.task_level);
    if (task) {
      task.count = item.count;
    }
  });
};

// 统计全部任务的等级分类
const getAllCount = () => {
  statisticalAllLevel().then((res) => {
    processData(res);
  });
};

// 统计执行中任务的等级分类
const getExecutingCount = () => {
  statisticalExecutingLevel().then((res) => {
    processData(res);
  });
};

// 统计已执行任务的等级分类
const getExecutedCount = () => {
  statisticalExecutedLevel().then((res) => {
    processData(res);
  });
};

// 查询全部任务
const getAllTaskList = () => {
  getTaskList({}).then((res) => {
    tableData.value = res.data ?? [];
  });
};

// 查询已执行任务
const getExecutedTaskList = () => {
  getExecutedList().then((res) => {
    tableData.value = res.data ?? [];
  });
};

// 查询执行中任务
const getExecutingTaskList = () => {
  getExecutingList().then((res) => {
    tableData.value = res.data ?? [];
  });
};

const getIndexTaskDataFun = () => {
  let paras = { year: year.value };
  getIndexTaskData(paras).then((res) => {
    if (res.data && res.data.total) {
      taskYearData.value = res.data.total;
      taskCountInfo.value[0].count = res.data.oneplus;
      taskCountInfo.value[1].count = res.data.one;
      taskCountInfo.value[2].count = res.data.two;
      taskCountInfo.value[3].count = res.data.three;
      taskCountInfo.value[4].count = res.data.security;
      taskCountInfo.value[5].count = res.data.other;
    } else {
      taskYearData.value = 0;
      taskCountInfo.value[0].count = 0;
      taskCountInfo.value[1].count = 0;
      taskCountInfo.value[2].count = 0;
      taskCountInfo.value[3].count = 0;
      taskCountInfo.value[4].count = 0;
      taskCountInfo.value[5].count = 0;
    }
  });
  getTaskDataForYear({year:''}).then(res=>{
    console.log(res,'1111')
    chartData.value = res.data || []
  })
};

const changeYear = (e) => {
  if (e) {
    getIndexTaskData({ year: e }).then((res) => {
      if (res.data && res.data.total) {
        taskYearData.value = res.data.total;
        taskCountInfo.value[0].count = res.data.oneplus;
        taskCountInfo.value[1].count = res.data.one;
        taskCountInfo.value[2].count = res.data.two;
        taskCountInfo.value[3].count = res.data.three;
        taskCountInfo.value[4].count = res.data.security;
        taskCountInfo.value[5].count = res.data.other;
      } else {
        taskYearData.value = 0;
        taskCountInfo.value[0].count = 0;
        taskCountInfo.value[1].count = 0;
        taskCountInfo.value[2].count = 0;
        taskCountInfo.value[3].count = 0;
        taskCountInfo.value[4].count = 0;
        taskCountInfo.value[5].count = 0;
      }
    });
  } else {
    getIndexTaskData({ year: "" }).then((res) => {
      if (res.data && res.data.total) {
        taskYearData.value = res.data.total;
        taskCountInfo.value[0].count = res.data.oneplus;
        taskCountInfo.value[1].count = res.data.one;
        taskCountInfo.value[2].count = res.data.two;
        taskCountInfo.value[3].count = res.data.three;
        taskCountInfo.value[4].count = res.data.security;
        taskCountInfo.value[5].count = res.data.other;
      } else {
        taskYearData.value = 0;
        taskCountInfo.value[0].count = 0;
        taskCountInfo.value[1].count = 0;
        taskCountInfo.value[2].count = 0;
        taskCountInfo.value[3].count = 0;
        taskCountInfo.value[4].count = 0;
        taskCountInfo.value[5].count = 0;
      }
    });
  }
};

 
// 查看
const viewDetail = async (row) => {
  let g = window.__g;
  WorkCockpitStore.setCancelData({});
  getDrawDataByType(row.id)
  layerInit().then(async()=>{
    getDrawDataOfTask({ taskId: row.id }).then(res => {
    WorkCockpitStore.setTaskSceneData(res.data)
    // let lines =[]
    // res.data.forEach((item,index) => {
    //   if(item.type==='1'){
    //     if(item.drawInfoList?.length){
    //       item.drawInfoList.forEach((child,i)=>{
    //         lines.push(child)
    //       })
    //     }
    //   }
    // });
    // console.log(lines,'任务所有常规路线')
    drawGsGtData(0);
    WorkCockpitStore.setTaskItem(row);
    WorkCockpitStore.set_tabsOne("rcap");
    WorkCockpitStore.set_tabsTwo("");
    closeFloors();
    WorkCockpitStore.setCurPage("taskDetail");
  })
  })
};
// 获取指定场景类型的标绘数据 1:道路  2:现场  3:住地
const getDrawDataByType=(id)=>{
  getRelatedFeatureDataOfTask({taskId:id,sceneType:1}).then(res=>{
    WorkCockpitStore.setRoadDrawList(res.data) 
  })
  getRelatedFeatureDataOfTask({taskId:id,sceneType:2}).then(res=>{
    WorkCockpitStore.setSiteDrawList(res.data) 
  })
  getRelatedFeatureDataOfTask({taskId:id,sceneType:3}).then(res=>{
    WorkCockpitStore.setResidenceDrawList(res.data) 
  })
}
onMounted(() => {
  getTaskCount();
  handleSearch("all");
  getIndexTaskDataFun();
});
</script>

<style lang="scss" scoped>
.content-box {
  width: 400px;
  // height: calc(120vh - 62px );
  height: calc(120vh - 75%);
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  // margin-bottom: 10px;
  position: absolute;
  top: 72px;
  right: 10px;
  // bottom: 20px;
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
    padding: 10px 20px;
    box-sizing: border-box;
    height: calc(100% - 48px);
    // overflow: auto;

    .header-box {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 48px;
      border-bottom: 1px solid rgba(38, 68, 173, 0.5);
      .header-left {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
        cursor: pointer;
        .label {
          font-family: YouSheBiaoTiHei;
          font-size: 18px;
        }

        .total {
          font-family: DIN;
          font-size: 40px;
          line-height: 30px;
          color: #00ceff;
          margin: 0 2px 0 2px;
        }

        .unit {
          font-size: 14px;
        }
      }
      .active_name {
        border-bottom: 2px solid #a1d9fd;
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
      border-bottom: 1px solid rgba(38, 68, 173, 0.5);
      margin-bottom: 8px;

      .btn {
        flex: 1;
        height: 40px;
        line-height: 40px;
        text-align: center;
        font-family: Source Han Sans CN;
        font-size: 14px;
        background: url("@/assets/workcockpit/btn_normal.png");
        cursor: pointer;

        &:hover {
          // background: url("@/assets/workcockpit/btn_hover.png");
        }

        .run-color,
        .done-color,
        .all-color {
          margin: 0 4px;
          font-family: DIN;
          font-size: 20px;
        }

        .all-color {
          color: #ffcf33;
        }
        .run-color {
          color: #00ceff;
        }

        .done-color {
          color: #33ff77;
        }
      }

      .btn-press {
        background: url("@/assets/workcockpit/btn_press.png");
      }
    }

    .task-count-box {
      width: 100%;
      display: flex;
      gap: 4px;
      margin: 8px;

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
      height: calc(100% - 135px);
      .view {
        color: #00ceff;
        cursor: pointer;
      }

      .bold {
        font-weight: bold;
      }
      :deep(.el-carousel__indicators--horizontal){
        position: absolute;
        bottom: -5%;
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
:deep(.custom_year) {
  .el-input__wrapper {
    background: rgba(0, 12, 78, 0.5);
    box-shadow: none;
    border: 1px solid  #a1d9fd;
  }
  .el-input__prefix {
    display: none;
  }
  .el-input__inner {
    color: #fff;
    text-align: center;
    
  }
}
</style>
