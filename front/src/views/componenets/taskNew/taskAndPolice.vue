<template>
  <div class="task_police_box">
    <el-collapse v-model="activeName" accordion @change="changeActive">
      <el-collapse-item :title="`${item}`" :name="item" v-for="(item, index) in activeList" :key="index">
        <div class="taskPolice">
          <div class="edit_box">
            <div class="edit_btn" @click="edit(item)">编辑</div>
          </div>
          <div class="data_content">
            <el-row >
              <el-col :span="24" v-if="activeName==='现场警卫组'||activeName==='住地警卫组'" style="font-size:16px;padding:0 20px;" class="color_custom">总数{{collapseData.total}}个</el-col>
              <el-col :span="24" v-if="activeName==='交通保障组'" style="font-size:16px;padding:0 20px;" class="color_custom">线路总数{{collapseData.total}}条</el-col>
              <el-col :span="option.zwls > 1 ? 24 : 12" v-for="(option, index) in dialogFormData" :key="index">
                <div class="show_data">
                  <div class="left_name">{{ option.zdmc }}：</div>
                <div class="right_data">{{ dialogForm[option.cczd] }}</div>
              </div>
              </el-col>
              <el-col :span="24" v-if="(activeName==='现场警卫组'||activeName==='住地警卫组')">
                <div v-for="(item,index) in policeList" :key="index" class="task_polic_box">
                  <div class="item_police">
                    <div class="left color_custom">现场{{ index+1 }}:</div>
                    <div class="right color_custom">{{ item.sceneName }}</div></div>
                  <div v-for="(child,idx) in item.scenePlanList" :key="idx">
                    <div class="item_police"><div class="left">负责人:</div><div class="right color_custom">{{ child.data.zrld }}</div></div>
                    <div class="item_police"><div class="left">电话:</div><div class="right color_custom">{{ child.data.phone }}</div></div>
                    <div class="color_custom">警力部署</div>
                    <div class="item_police"><div class="left">总部署警力:</div><div class="right color_custom">{{ child.policeTypeStatistics.total }}</div></div>
                    <div class="item_police"><div class="left">执勤警力:</div><div class="right color_custom">{{ child.policeTypeStatistics.onduty }}</div></div>
                    <div class="item_police"><div class="left">应急处突警力:</div><div class="right color_custom">{{ child.policeTypeStatistics.emergency }}</div></div>
                    <div class="item_police"><div class="left">警力上岗时间:</div><div class="right">{{ child.data.time }}</div></div>
                    <div class="color_custom">具体岗哨</div>
                    <div v-for="(last,a) in child.placeStatistics" :key="a">
                      <div class="item_police">
                        <div class="left">{{ last.weizhi }}部署：</div>
                      <div class="right" style="display: flex;flex-wrap: wrap;">
                        <div v-for="(lastChild,b) in last.data" :key="b">
                          {{ lastChild.leixing }}<span class="color_custom">{{ lastChild.num }}</span>人
                        </div>
                      </div>
                    </div>
                    </div>
                  </div>
                </div>
              </el-col>

              <el-col :span="24" v-if="activeName==='交通保障组'">
                <div v-for="(item,index) in policeList" :key="index" class="task_polic_box">
                  <div class="item_police">
                    <div class="left ">{{ index+1 }}、路线:</div>
                    <div class="right ">{{ item.sceneName }}</div>
                  </div>
                  <div class="item_police">
                    <div class="left ">路线详情:</div>
                    <div class="right ">{{ item.sceneRoadDesc }}</div>
                  </div>
                  <div class="item_police">
                    <div class="left ">距离:</div>
                    <div class="right ">{{ item.sceneRoadLength }}</div>
                  </div>
                  <div class="item_police">
                    <div class="left ">用时约:</div>
                    <div class="right ">{{ item.sceneRoadTime }}</div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </el-collapse-item>
    </el-collapse>
  </div>
  <!-- 警力部署编辑弹框 -->
  <el-dialog v-model="openModal" width="686px" @close="cancel" align-center :destory-on-close="false"
    :close-on-click-modal="false" append-to-body class="my_Dialog_text">
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ activeName }}</div>
      </div>
    </template>
    <el-form ref="customForm" :inline="true" :model="dialogForm" :label-suffix="'：'" :label-width="100"
      class="form_custom_class" style="width:95%">
      <el-row>
        <FormCustom v-for="(items, index) in dialogFormData" :key="index" :obj="dialogForm" :option="items"
          @changeCotrl="functionEvents" @changeDialogCtrl="functionEvents1" />
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="text-align: center;">
        <el-button type="primary" @click="submitForm" style="background:#274eef">确 定</el-button>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import JsonData from './taskJson'
import FormCustom from './formCustom.vue'
import { ref, computed, getCurrentInstance, reactive } from 'vue'
import useTaskStore from "@/store/modules/taskStore";
import { searchNodePlan, saveTaskPlan } from '@/api/task/task'
import { dialogRules } from '@/utils/index'
const { proxy } = getCurrentInstance()
let taskInfo = computed(() => useTaskStore().taskInfo);// 任务信息信息
const activeName = ref('')
const selectVal = ref('')
const collapseData = ref({})
const policeList = ref([])
const openModal = ref(false)
const dialogForm = ref({})
const dialogFormData = ref([])
const updateNodeId = ref(null)
const activeList = ref(['隐患排查组', '情报研判组', '现场警卫组', '住地警卫组', '交通保障组', '沿线管控组', '任务前站组', '随身警卫组', '应急处突组', '安全检查组', '人员政审组', '通讯保障组'])
const changeActive = (e) => {
  console.log(e)
  if (e) {
    let params = { taskId: taskInfo.value.id, planNode: e }
    selectVal.value = e
    searchNodePlan(params).then(res => {
      if (res.data.id) {
        collapseData.value = res.data
        updateNodeId.value = res.data.id
        dialogForm.value = res.data.data
        dialogFormData.value = JsonData[e]
        policeList.value = res.data.sceneDataList 
      } else {
        collapseData.value = res.data
        dialogForm.value = res.data.data? res.data.data:{zrld:'',zrdw:''}
        updateNodeId.value = null
        dialogFormData.value = JsonData[e]
        policeList.value = res.data.sceneDataList 
      }
    })
  }

}
const edit = (item) => {
  console.log(item, JsonData[item])
  openModal.value = true
  // 封装数据
  getData(JsonData[item])
}
const submitForm = () => {
  proxy.$refs['customForm'].validate(valid => {
    if (valid) {
      let data = { taskId: taskInfo.value.id, planNode: activeName.value, data: dialogForm.value, id: updateNodeId.value }
      saveTaskPlan(data).then(res => {
        console.log(res)
        if (res.code === 0) {
          openModal.value = false
          changeActive(activeName.value)
        }
      })
    }
  });
}
const cancel = () => {

}
const functionEvents = () => {

}
const functionEvents1 = () => {

}
const getData = (arr) => {
  if (arr === undefined || arr === null || arr.length === 0) {
    return
  }
  let param = []
  arr.map(item => {
    let obj = item
    if (item.required) {
      if (item.zdlx === 0 || item.zdlx === 3 || item.zdlx === 4) {
        obj.dialogRules = dialogRules('blur')
      }
      if (item.zdlx === 1 || item.zdlx === 2) {
        obj.dialogRules = dialogRules('change')
      }
    }
    if (updateNodeId.value) {

    } else {
      if (item.yxdx) {
        dialogForm.value[item.cczd] = []
      } else {
        dialogForm.value[item.cczd] = ''
      }
    }

    param.push(obj)
  })
  console.log(param, dialogForm.value)
  dialogFormData.value = param
}
</script>
<style lang="scss" scope>
.task_police_box {
  margin-top: 7px;

  .el-collapse-item__header {
    background: rgba(28, 53, 155, 0.5) !important;
    padding-left: 60px !important;
  }

  .el-collapse-item__arrow {
    position: absolute;
    left: 40px !important;
  }

  .edit_box {
    display: flex;
    align-items: center;
    justify-content: end;

    .edit_btn {
      cursor: pointer;
      width: 84px;
      height: 32px;
      background: #274EEF;
      line-height: 32px;
      text-align: center;
      right: 20px;
    }
  }

  .form_custom_class {
    .el-form-item__label {
      color: #fff !important;
    }
  }
  .data_content{
    .show_data{
      display:flex;
      padding: 0 20px;
     height: 30px;
      align-items: center;
      .left_name{
        min-width:60px;
      }
    }
  }
}
.task_polic_box {
  padding: 10px 20px 0 20px;
  .item_police{
    display: flex;
    .left{
      width:30%;
    }
    .right{
      flex: 1;
    }
  }
}
</style>
<style lang="scss">
.my_Dialog_text{
  .el-dialog__body{
    overflow:visible !important;
  }
}
.color_custom{
  color:#00CEFF;
}
</style>
