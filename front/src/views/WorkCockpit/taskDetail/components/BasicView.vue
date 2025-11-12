<template>
  <div class="content">
    <div class="tab-box">
      <el-tabs v-model="activeTab" class="tab" @tab-change="handleClick">
        <el-scrollbar height="65vh">
          <el-tab-pane v-for="item in tabs" :key="item" :label="item" :name="item">
            <p class="content-info">{{ msgInfo }}</p>
          </el-tab-pane>
        </el-scrollbar>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import {  searchNodePlan } from '@/api/task/task'
// store
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const WorkCockpitStore = useWorkCockpitStore()
const taskItem = computed(() => WorkCockpitStore.taskItem)

// 选中tab
const activeTab = ref('基本情况')
const tabs = ref(['基本情况', '指导思想', '工作要求'])
const handleClick = (tab) => {
  getTaskBasicData(tab)
}

const msgInfo = ref('')

// 组织领导
const leaderData = ref({})


// 获取任务基本情况数据 
const getTaskBasicData = (planNode) => {
  searchNodePlan({ taskId: taskItem.value.id, planNode }).then(res => {
    msgInfo.value = res.data?.data?.msg
    leaderData.value = res.data?.data
  })
}

onMounted(() => {
  getTaskBasicData('基本情况')
})
</script>

<style lang="scss" scoped>
.content {
  font-family: Source Han Sans;

  .tab-box {
    margin-top: 8px;

    .content-info {
      margin-bottom: 16px;
      font-size: 14px;
      line-height: normal;
      text-indent: 28px;
      padding-right: 8px;
    }
  }

  .color-enhance {
    color: #FF4040;
  }

  .color-level-one {
    color: #FF6633;
  }

  .color-level-two {
    color: #FFE433;
  }

  .color-level-three {
    color: #33FF77;
  }

  .color-level-secure {
    color: #00A6FF;
  }
}
</style>