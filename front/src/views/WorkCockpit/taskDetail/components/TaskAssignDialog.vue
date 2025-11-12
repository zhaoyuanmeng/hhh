<template>
    <div class="content-box" :class="{ 'quit-width': !isFullScreen, 'full-width': isFullScreen }">
      <div class="content-header">
        <span class="title">{{ taskPlanNode }}</span>
        <img class="close" src="@/assets/workcockpit/close.png" alt="" @click="handleClose">
      </div>
      <el-scrollbar height="250px">
        <div class="tab-box">
          <!-- 应急处突以Tab页显示 -->
          <el-tabs v-if="false" v-model="activeTab" class="demo-tabs" @tab-change="handleClick">
            <el-tab-pane label="责任人员" name="zrry">
              <div class="tab-content">
                <div class="column">
                  <p class="title">责任领导：</p>
                  <div class="info">
                    <p class="mb8">姓名：XXX</p>
                    <p class="mb8">职位：XXXXXX</p>
                    <p>电话：15022223333</p>
                  </div>
                </div>
                <div class="column">
                  <p class="title">责任单位：</p>
                  <div class="info">
                    <p class="mb8">XXX单位</p>
                    <p class="mb8">XXX单位</p>
                    <p>XXX单位</p>
                  </div>
                </div>
                <div class="column">
                  <p class="title">任务职责：</p>
                  <div class="info">
                    <p class="duty-info">
                      本论坛旨在搭建一个高层次、宽领域、多角度的交流平台，汇聚国内外[行业/领域]的专家学者、企业领袖、政府官员及业界精英，共同探讨[行业/领域]的热点问题、发展趋势及未来前景。</p>
                  </div>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="警力部署" name="jlbs">
              <div class="tab-content">
                <div class="deploy-item" v-for="item in deployList">
                  <p class="title">
                    <span>{{ item.title }}：1,200人</span>
                  </p>
                  <div class="list-box">
                    <div class="list-item" v-for="el in item.list">
                      <span>{{ el.label }}：</span>
                      <span class="value">{{ el.value }}人</span>
                    </div>
                  </div>
                </div>
              </div>
              <p class="sg-time">
                <span class="label">警力上岗时间：</span>
                <span>2024年10月1日</span>
              </p>
            </el-tab-pane>
            <el-tab-pane label="紧急避险点" name="jjbxd">
              <div class="tab-content">
                <div class="column">
                  <p class="info mb8">
                    <span class="label">应急避险点：</span>
                    <span>XXXX</span>
                  </p>
                  <p class="info mb8">
                    <span class="label">联系人：</span>
                    <span>XXXX</span>
                  </p>
                  <p class="info">
                    <span class="label">电话：</span>
                    <span>XXXX</span>
                  </p>
                </div>
                <div class="column">
                  <p class="info path-box">
                    <span class="label">应急路线：</span>
                    <span class="path">xx会展中心——xx街——xx街——xxxx路——xx街 ——xx街——xx路——xx街——xx街——应急医院</span>
                  </p>
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane label="应急医院" name="yjyy">
              <div class="tab-content">
                <div class="column">
                  <p class="info mb8">
                    <span class="label">应急医院：</span>
                    <span>XXXX</span>
                  </p>
                  <p class="info mb8">
                    <span class="label">联系人：</span>
                    <span>XXXX</span>
                  </p>
                  <p class="info">
                    <span class="label">电话：</span>
                    <span>XXXX</span>
                  </p>
                </div>
                <div class="column">
                  <p class="info path-box">
                    <span class="label">应急路线：</span>
                    <span class="path">xx会展中心——xx街——xx街——xxxx路——xx街 ——xx街——xx路——xx街——xx街——应急医院</span>
                  </p>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
          <!-- 非应急处突 -->
          <div >
            <div class="info-content" v-if="infoData">
              <div class="info-left">
                <div class="column" style="margin-bottom: 34px;">
                  <p class="title">责任领导：</p>
                  <div class="info">
                    <p class="mb8">姓名：{{ infoData.zrld }}</p>
                    <p>电话：{{ infoData.phone }}</p>
                  </div>
                </div>
                <div class="column">
                  <p class="title">责任单位：</p>
                  <div class="info">{{ infoData.zrdw }} </div>
                </div>
              </div>
              <div class="info-right">
                <!-- 现场警卫 -->
                <div v-if="taskPlanNode === '现场警卫' || taskPlanNode === '住地警卫'">
                  <div class="info-total">
                    <p class="title">任务共涉{{ taskPlanNode === '现场警卫' ?'现场':'住地'}}：{{ total }}处</p>
                  </div>
                  <div class="info-box">
                    <div class="info-item" v-for="(item, index) in sceneDataList" :key="index">
                      <div class="row-info">
                        <p>
                          <span class="label">{{ taskPlanNode === '现场警卫' ?'现场':'住地'}}{{ index + 1 }}：</span>
                          <span>{{ item.sceneName }}</span>
                        </p>
                        <p>
                          <span class="label">负责人：</span>
                          <span>{{ item.scenePlanList[0]?.data?.zrld }}</span>
                        </p>
                        <p>
                          <span class="label">电话：</span>
                          <span>{{ item.scenePlanList[0]?.data?.phone }}</span>
                        </p>
                      </div>
                      <p>
                        <span class="label">警力部署：</span>
                        <span>总部署警力{{ item.scenePlanList[0]?.policeTypeStatistics?.total  || 0}}人，
                          现场执勤警力{{ item.scenePlanList[0]?.policeTypeStatistics?.onduty || 0}}人，
                          应急处突警力{{ item.scenePlanList[0]?.policeTypeStatistics?.emergency || 0 }}人</span>
                      </p>
                      <p>
                        <span class="label">上岗时间：</span>
                        <span>{{ item.scenePlanList[0]?.data?.time }}</span>
                      </p>
                    </div>
                  </div>
                </div>
                <!-- 任务职责 -->
                <div class="info-rwzz" v-if="!showRwzr">
                  <p class="title">任务职责：</p>
                  <div class="info">
                    <p class="duty-info">{{ infoData.rwzz }}</p>
                  </div>
                </div>
                <!-- 路线 -->
                <div class="info-lx" v-if="false">
                  <p class="title">路线：</p>
                  <div class="info">
                    <p class="path">1.xx会展中心——xx街——xx街——xxxx路——xx街</p>
                    <p class="path">2.xxxxxx会展中心——xxxxx街——xxxxx街——xxxx路——xxxxx街</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-scrollbar>

    </div>
</template>
  
  <script setup>
  import { ref, computed, onMounted, watch } from 'vue'
  import useWorkCockpitStore from '@/store/modules/workCockpit.js'
  import { getTaskPlanNode } from '@/api/workCockpit/index.js'
  const WorkCockpitStore = useWorkCockpitStore()
  const isFullScreen = computed(() => WorkCockpitStore.isFullScreen)
  const taskPlanNode = computed(() => WorkCockpitStore.taskPlanNode)
  const taskItem = computed(() => WorkCockpitStore.taskItem)
  const showRwzr = computed(() => ['现场警卫', '住地警卫', '交通保障'].includes(WorkCockpitStore.taskPlanNode))
  
  watch(() => taskPlanNode.value, () => {
    getNodeData()
  }, { deep: true })
  
  
  // 选中tab
  const activeTab = ref('zrry')
  const handleClick = (tab) => {
    console.log(111, tab)
  }
  
  const deployList = ref([
    {
      id: 1,
      title: '出勤总人数',
      list: [
        { label: '固定哨', value: 6 },
        { label: '交通哨', value: 6 },
        { label: '社控哨', value: 6 },
        { label: '随卫哨', value: 6 },
        { label: '游动哨', value: 6 },
        { label: '昼夜哨', value: 6 },
        { label: '至高点哨', value: 6 },
      ]
    }
  ])
  
  const handleClose = () => {
    WorkCockpitStore.setTaskPlanNode('')
  }
  
  const total = ref(0)
  const infoData = ref()
  const sceneDataList = ref([])
  const getNodeData = () => {
    getTaskPlanNode({ taskId: taskItem.value.id, planNode: `${taskPlanNode.value}组` }).then(res => {
      total.value = res.data?.total
      infoData.value = res.data?.data ?? null
      sceneDataList.value = res.data?.sceneDataList
    })
  }
  
  onMounted(() => {
    taskPlanNode.value && getNodeData()
  })
  
  </script>
  
  <style lang="scss" scoped>
  .content-box {
    width: 960px;
    height: 360px;
    background: linear-gradient(180deg, rgba(10, 29, 100, 0.8) 0%, rgba(21, 30, 73, 0.6984) 100%);
    position: absolute;
    bottom: 42px;
    right: 340px;
    z-index: 50;
    transform: scale(0.8);
    transform-origin: right bottom;
  
    .content-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      height: 32px;
      padding: 0 16px 0 20px;
      box-sizing: border-box;
      background: url("@/assets/workcockpit/title_bg.png") no-repeat;
  
      .title {
        font-family: YouSheBiaoTiHei;
        font-size: 16px;
      }
  
      .close {
        cursor: pointer;
      }
    }
  
    .tab-box {
      padding: 12px 20px;
      box-sizing: border-box;
  
      .info-content {
        padding: 12px 20px;
        display: flex;
        gap: 134px;
      }
  
      .title {
        font-size: 14px;
        font-weight: bold;
        line-height: normal;
        color: #00CEFF;
        margin-bottom: 16px;
      }
  
      .label {
        font-weight: bold;
        color: #00CEFF;
      }
  
      .info {
        font-size: 14px;
        line-height: normal;
  
        .duty-info {
          width: 320px;
        }
      }
  
      .tab-content {
        padding: 12px 20px;
        display: flex;
        gap: 60px;
  
        .path-box {
          display: flex;
          margin-left: 66px;
  
          .path {
            display: inline-block;
            width: 350px;
            line-height: normal;
          }
        }
  
        .mb8 {
          margin-bottom: 6px;
        }
  
        .deploy-item {
          margin-bottom: 12px;
  
          .title {
            font-size: 14px;
            font-weight: bold;
            line-height: normal;
            color: #00CEFF;
            margin-bottom: 4px;
          }
  
          .list-box {
            width: 487px;
            background: rgba(0, 0, 0, 0.1);
            padding: 20px;
            box-sizing: border-box;
            border: 1px solid #182659;
            display: flex;
            flex-wrap: wrap;
            gap: 8px 34px;
  
            .list-item {
              font-size: 14px;
              line-height: normal;
  
              .value {
                color: #00CEFF;
              }
            }
  
          }
        }
      }
  
      .sg-time {
        padding-left: 20px;
        font-size: 14px;
  
        .label {
          font-weight: bold;
          color: #00CEFF;
        }
      }
  
      .info-item {
        font-size: 14px;
        line-height: normal;
        margin-bottom: 16px;
  
        .row-info {
          display: flex;
          gap: 32px;
          margin-bottom: 8px;
        }
      }
    }
  }
  
  // .full-width {
  //   width: calc(100% - 0px);
  //   left: 0px;
  // }
  
  // .quit-width {
  //   width: calc(100% - 800px - 40px);
  //   left: 420px;
  // }
  
  :deep(.el-tabs__item) {
    color: #fff;
    padding: 0 12px;
  }
  
  :deep(.el-tabs__item.is-active) {
    color: #00CEFF;
    font-weight: bold;
  }
  
  :deep(.el-tabs__active-bar) {
    background-color: #00CEFF;
  }
  
  :deep(.el-tabs__nav-wrap:after) {
    background-color: rgba($color: #ECFAFF, $alpha: .18);
    height: 1px;
  }
  
  :deep(.el-tabs__header) {
    margin-bottom: 8px;
  }
  </style>
  