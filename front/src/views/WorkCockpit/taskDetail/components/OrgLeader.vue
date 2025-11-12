<template>
  <div class="content">
    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="组织架构" name="zzjg">
        <div class="info-box p8">
          <div class="org-item">
            <p class="label">总指挥</p>
            <p class="value">
              <span>{{ leaderData?.zzh?.job }}</span>
              <span> {{ leaderData?.zzh?.name }}</span>
            </p>
          </div>
          <div class="org-item">
            <p class="label">副总指挥</p>
            <p class="value">
              <span>{{ leaderData?.fzzh?.job }}</span>
              <span> {{ leaderData?.fzzh?.name }}</span>
            </p>
          </div>
          <!-- 成员 -->
          <div class="org-item">
            <p class="label">成员：</p>
            <p class="value" v-for="(item, index) in leaderData?.member" :key="index">
              <span>{{ item.job }}</span>
              <span>{{ item.name }}</span>
            </p>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="任务分工" name="rwfg">
        <div class="info-box">
          <div class="task-assign">
            <div class="assign-item" :class="{ 'active': taskPlanNode === item }" v-for="(item, index) in assignList"
              :key="index" @click="handleChangeNode(item)">
              {{ item }}
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>


  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import { searchNodePlanToScreen, searchNodePlan } from '@/api/task/task'
// store
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const WorkCockpitStore = useWorkCockpitStore()
const taskItem = computed(() => WorkCockpitStore.taskItem)
const taskPlanNode = computed(() => WorkCockpitStore.taskPlanNode)

const activeTab = ref('zzjg')
const assignList = ref(['隐患排查', '情报研判', '现场警卫', '住地警卫', '交通保障', '沿线管控', '任务前站', '随身警卫', '应急处突',
  '安全检查', '人员政审', '通讯保障'])

const handleTabChange=(tab)=>{
  WorkCockpitStore.setTaskPlanNode('')
}

const handleChangeNode = (node) => {
  WorkCockpitStore.setTaskPlanNode(node)
}

const leaderData = ref({})
// 获取组织领导
const getTaskOrgLeader = () => {
  searchNodePlan({ taskId: taskItem.value.id, planNode: '组织领导' }).then(res => {
    leaderData.value = res.data?.data
  })
}

onMounted(() => {
  getTaskOrgLeader()
})
</script>

<style lang="scss" scoped>
.content {
  font-family: Source Han Sans;
  font-size: 14px;

  .info-box {
    margin-bottom: 48px;
    line-height: 24px;

    .org-item {
      margin-bottom: 16px;
    }

    .title {
      font-family: YouSheBiaoTiHei;
      font-size: 24px;
      margin-bottom: 18px;
    }

    .label {
      color: #00CEFF;
    }

    .value {
      width: 100%;
      display: flex;
      justify-content: space-between;
      padding-left: 48px;
      box-sizing: border-box;
    }

    .task-assign {
      display: flex;
      flex-wrap: wrap;
      gap: 7px;

      .assign-item {
        width: 115px;
        height: 40px;
        line-height: 40px;
        text-align: center;
        font-size: 14px;
        background: url('@/assets/workcockpit/fg_normal.png');
        cursor: pointer;

        &:hover {
          background: url('@/assets/workcockpit/fg_hover.png');
        }
      }

      .active {
        background: url('@/assets/workcockpit/fg_press.png');
      }
    }
  }

  .p8{
    padding: 0 8px;
  }
}
</style>