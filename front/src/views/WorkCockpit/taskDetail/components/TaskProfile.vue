<template>
  <div class="content">
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
          <!-- <div class="collapse_box"  v-html="item.content" style="padding: 16px;"></div> -->
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
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted } from 'vue'
import {  searchNodePlan,getWordContent } from '@/api/task/task'
// store
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const WorkCockpitStore = useWorkCockpitStore()
const taskItem = computed(() => WorkCockpitStore.taskItem)
const activeName = ref('')
const activeNameChild = ref('')
const wordTextData = ref([])
const wordTitle = ref([])

// 获取任务基本情况数据 
const getTaskBasicData = () => {
  getWordContent({businessId: taskItem.value.id}).then(res => {
    if(res.data&&res.data.length>0){
      wordTextData.value = res.data.filter(item => item.title !== '标题'&&item.title !== '')
      wordTitle.value = res.data.filter(item => item.title === '标题'&&item.title !== '')
    }else{
      wordTextData.value = []
      wordTitle.value = []
    }
  })
}
onMounted(() => {
  getTaskBasicData()
})
</script>

<style lang="scss" scoped>
.content {
  width: 100%;
  height: 100%;
  overflow: auto;
  font-family: Source Han Sans;
  .plan_box{
    flex: 1;
    overflow: auto;
    padding: 14px 4px;
    :deep(.el-collapse) {
      border: none;
    }
    :deep(.el-collapse-item) {
      margin-bottom: 7px;
    }
    :deep(.el-collapse-item__header) {
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
    .word_title {
      display: flex;
      justify-content: center;
      align-items: center;
      padding: 0 10px 20px 10px;
      font-weight: bold;
      font-size: 14pt;
    }
    .collapse_box {
      // padding: 6px 20px 0 30px;
    }
  }
}
</style>