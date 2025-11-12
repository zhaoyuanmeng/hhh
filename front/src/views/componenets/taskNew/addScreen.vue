<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 11:20:59
 * @LastEditors: Alex
-->
<template>
  <div class="addScreen_content" v-if="showDraw" :style="{height: radio === 0?'750px':'650px'}">
    <div class="tabs">
      <div
        class="tabs_item"
        :class="{ active: selectItem === 1 }"
        @click="change(1)"
      >
        路线
      </div>
      <div
        class="tabs_item"
        :class="{ active: selectItem === 2 }"
        @click="change(2)"
      >
        现场
      </div>
      <div
        class="tabs_item"
        :class="{ active: selectItem === 3 }"
        @click="change(3)"
      >
        住地
      </div>
    </div>
    <div class="from_content">
      <el-form
        ref="drawForm"
        :inline="true"
        :model="dialogForm"
        :label-suffix="'：'"
        :label-width="100"
        size="large"
        class="customForm"
      >
        <el-row>
          <el-col :span="24" v-if="selectItem === 1">
            <el-form-item label="类型" style="width: 100%">
              <el-radio-group v-model="radio" @change="getGSGT">
                <el-radio :value="0">道路</el-radio>
                <el-radio :value="2">高铁</el-radio>
                <el-radio :value="1">高速</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="24">
            <div  v-if="radio === 0&&selectItem === 1">
            <el-form-item
              label="路线起点"
              prop="startPointId"
              :rules="[{ required: true, message: '必填', trigger: 'change' }]"
              style="width: 100%"
             
            >
              <el-cascader :options="options" ref="cascaderRef" :show-all-levels="false" placeholder="请选择起点" style="width: 100%;" v-model="startPointList" clearable @change="getStartEnd"/>
            </el-form-item>
            <el-form-item
              label="路线终点"
              prop="endPointId"
              :rules="[{ required: true, message: '必填', trigger: 'change' }]"
              style="width: 100%"
            >
                <el-cascader :options="options" ref="cascaderRef1" :show-all-levels="false" placeholder="请选择终点" style="width: 100%;" v-model="endPointList" clearable @change="getStartEndOther" />
            </el-form-item>
          </div>
            <el-form-item
              label="名称"
              prop="sceneName"
              :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
              style="width: 100%"
              v-else
            >
              <el-select
                v-model="dialogForm.sceneName"
                placeholder="请选择"
                value-key="id"
                @change="getSelectData"
              >
                <el-option
                  v-for="item in selectData"
                  :key="item.id"
                  :label="item.name"
                  :value="item"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="常备方案" style="width: 100%">
              <el-select v-model="dialogForm.schemeId" placeholder="请选择" clearable :disabled="selectBol" @change="changeSelect">
                <el-option
                  v-for="(item, index) in planData"
                  :key="index"
                  :label="item.sceneName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="场景复用" style="width: 100%">
              <el-cascader :disabled="treeBol" :show-all-levels="false" :props="{ value: 'id', label: 'name', children: 'children' }" :options="optionsList" clearable v-model="selectedValue" placeholder="请选择" style="width: 100%;"  @change="changeTree"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="开始时间"
              prop="beginTime"
              :rules="[{ required: true, message: '必填', trigger: 'change' }]"
              style="width: 100%"
            >
              <el-date-picker
                v-model="dialogForm.beginTime"
                type="datetime"
                placeholder="开始时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                :editable="false"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="结束时间"
              prop="endTime"
              :rules="[{ required: true, message: '必填', trigger: 'change' }]"
              style="width: 100%"
            >
              <el-date-picker
                v-model="dialogForm.endTime"
                type="datetime"
                placeholder="结束时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                :editable="false"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item
              label="日程备注"
              prop="sceneDesc"
              :rules="[{ required: true, message: '必填', trigger: 'blur' }]"
              style="width: 100%"
            >
              <el-input
                v-model="dialogForm.sceneDesc"
                placeholder="请输入日程详细安排"
                type="textarea"
                :rows="4"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col
            :span="24"
            style="display: flex; align-items: center; justify-content: center"
          >
            <el-form-item style="margin-right: 0px !important">
              <div class="cancle_btn" @click="close">取消</div>
              <div class="subMit_btn" @click="submit">确定</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </div>

  <!-- 绘制 -->
  <Draw v-if="drawBol" @out="outDraw" />

  <drwLine v-if="drawline" @close="closeCMPT" />
</template>

<script setup>
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  reactive,
  onBeforeMount,
  getCurrentInstance,
  watch,
  nextTick,
} from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import drwLine from "../../SysTools/resourceCatalog/plot/DrawLineNew.vue"; // 画线
import { quertListForTypeId} from "@/api/basic/index";
import { sessionStorage } from "@/utils/storage";
import { getScreenPosition, addScreen,getSceneTreeData } from "@/api/task/task";
import { queryTaskInfo } from "@/api/task/index";
import { getPlanListApi } from "@/api/plan";
import useTaskStore from "@/store/modules/taskStore";
import useScreenStore from "@/store/modules/screenStore";
import Draw from "./draw.vue";
import { Plus } from "@element-plus/icons-vue";
import { clearDraw } from "./util";
const { proxy } = getCurrentInstance();
const emit = defineEmits(["out"]);
const taskStore = useTaskStore();
let drawBol = computed(() => taskStore.isOpenDraw);
let task = computed(() => taskStore.taskInfo.id);
const options = ref([
  {
    value: 'xc',
    label: '现场',
    children: [],
  },
  {
    value: 'zd',
    label: '住地',
    children: [],
  }
])
const startName = ref('')
const endName = ref('')
const selectItem = ref(1);
const radio = ref(0);
const planType = ref("1");
const cascaderRef = ref(null)
const cascaderRef1 = ref(null)
const drawline = ref(false);
const showDraw = ref(true);
const selectData = ref([]);
const planData = ref([]);
const optionsList = ref([])
const selectedValue = ref([]) // 绑定的值
const startPointList = ref([])
const endPointList = ref([])
const selectBol = ref(false)
const treeBol = ref(false)
const dialogForm = reactive({
  taskId: task.value,// 任务id
  schemeId: "", // 常备方案id
  sceneName: "", // 场景名称
  basicDataId: "",// 挂载的现场和住地id
  type: 1,// 类型
  beginTime: "",// 开始时间
  endTime: "", // 结束时间
  sceneDesc: "", //场景描述
  sourceSceneId:'',// 场景复用id
  startPointId:null,// 路线起点
  endPointId:null,// 路线终点
  startType:null,//路线起点类型
  endType:null// 路线终点类型
});
onMounted(() => {
  // 获取现场住地列表
  quertListForTypeId('xc').then(res=>{
    console.log(res)
    let datas = res.data.map(item=>{
      return {value:item.id,label:item.data.title}
    })
    options.value[0].children = datas
  })
  quertListForTypeId('zd').then(res=>{
    let datas = res.data.map(item=>{
      return {value:item.id,label:item.data.title}
    })
    options.value[1].children = datas
  })
  // 初始画获取道路数据
  getPlanListApi({ type: "1" }).then((res) => {
    planData.value = res.data;
  });
   // 初始画获取道路场景数据
   getSceneTreeData({ type: "1" }).then((res) => {
   console.log(res)
   optionsList.value = res.data
  });
});
onBeforeUnmount(() => {});
onBeforeMount(() => {});
const getStartEnd = (e) => {
  if(e){
    const nodes = cascaderRef.value.getCheckedNodes()
    dialogForm.startPointId = e[1]
    dialogForm.startType = e[0]
    startName.value = nodes[0].label
  }else{
    dialogForm.startPointId = ''
    dialogForm.startType = ''
    startName.value = ''
  }
}
const getStartEndOther = (e) => {
  if(e){
    const nodes = cascaderRef1.value.getCheckedNodes()
    dialogForm.endPointId = e[1]
    dialogForm.endType = e[0]
    endName.value = nodes[0].label
  }else{
    dialogForm.endPointId = ''
    dialogForm.endType = ''
    endName.value = ''
  }
}
const getGSGT = (e) => {
  console.log(e);
  switch (e) {
    case 0:
      planType.value = "1";
      selectBol.value = false
      treeBol.value = false
      break;
    case 1:
      planType.value = "4";
      selectBol.value = false
      treeBol.value = false
      break;
    case 2:
      planType.value = "5";
      selectBol.value = false
      treeBol.value = false
      break;
    case 3:
      planType.value = "2";
      break;
    case 4:
      planType.value = "3";
      break;
  }
  if (e !== 0) {
    getScreenPosition({ type: e }).then((res) => {
      if (res.code === 0) {
        selectData.value = res.data;
      }
    });
    planData.value = []
    optionsList.value = []
    dialogForm.sceneName = ''
    dialogForm.schemeId = ''
  }else{
    getPlanListApi({type:'1'}).then((res) => {
    planData.value = res.data;
  });
  getSceneTreeData({type:'1'}).then((res) => {
   optionsList.value = res.data
  });
  dialogForm.sceneName = ''
  dialogForm.schemeId = ''
  }
};
const getSelectData = (item) => {
  console.log(item);
  dialogForm.basicDataId = item.id;
  dialogForm.sceneName = item.name;
  let params = {
    type: planType.value,
    basicDataId: item.id,
  };
  getPlanListApi(params).then((res) => {
    planData.value = res.data;
  });
  let params1 = {
    type: selectItem.value,
    name: item.name,
  };
  getSceneTreeData(params1).then((res) => {
   console.log(res)
   optionsList.value = res.data
  });
};
const change = (type) => {
  selectItem.value = type;
  if (type === 1) {
    radio.value = 0;
    getPlanListApi({type:'1'}).then((res) => {
    planData.value = res.data;
  });
  getSceneTreeData({type:'1'}).then((res) => {
   optionsList.value = res.data
  });
  } else {
    getGSGT(type + 1);
    radio.value = 6;
    planData.value = []
  optionsList.value = []
  }
  treeBol.value = false
  selectBol.value = false
  dialogForm.sceneName = ''
  dialogForm.schemeId = ''
  resetForm("drawForm");
};
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
  }
};
// 退出
const outDraw = () => {
  resetForm("drawForm");
  showDraw.value = true;
  emit("out");
};
const close = () => {
  resetForm("drawForm");
  emit("out");
};
const changeSelect = (e) => {
  if(e){
    treeBol.value = true
  }else{
    treeBol.value = false
  }
  
}
const changeTree = (e) => {
  console.log(e)
  if(e){
    selectBol.value = true
  }else{
    selectBol.value = false
  }
  
}
const submit = () => {
  proxy.$refs["drawForm"].validate((valid) => {
    if (valid) {
      dialogForm.type = selectItem.value;
      console.log(dialogForm.type,radio.value)
      if(dialogForm.type===1&&radio.value===0){
        dialogForm.sceneName = `${startName.value}至${endName.value}`
      }
      if(selectedValue.value?.length){
        dialogForm.sourceSceneId =selectedValue.value[selectedValue.value.length-1] 
      }else{
        dialogForm.sourceSceneId = ''
      }
      addScreen(dialogForm).then((res) => {
        if (res.code === 0) {
          ElMessage.success("创建成功");
          queryTaskInfo({ id: dialogForm.taskId }).then((res) => {
            taskStore.SET_TASKINFO(res.data);
            clearDraw();
            emit("out");
          });
        }
      });
    }
  });
};
const closeCMPT = (obj) => {
  if (obj) {
    dialogForm.lineName = obj.name;
    dialogForm.roadData = obj;
  }
  drawline.value = false;
};
// 绘制路线
const addLine = () => {
  drawline.value = true;
};
</script>

<style lang="scss" scoped>
@import "./index.scss";

:deep(.el-radio__label) {
  color: #fff;
  opacity: 0.8;
}

:deep(.el-select__wrapper) {
  background: rgba(0, 12, 78, 0.5);
  box-shadow: none;
  border: 1px solid #5b6799;
  border-radius: 2px;
  color: #fff;
}

:deep(.el-select__selected-item) {
    color: #fff;
}
:deep(.is-transparent){
  color: #fff;
  opacity: .4;
}
</style>
