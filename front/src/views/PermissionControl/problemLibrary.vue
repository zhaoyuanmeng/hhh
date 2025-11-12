<template>
  <div class="childbox">
    <div class="line1">
      <div class="heard_name">
        <div class="d_name">问题清单库</div>
      </div>
      <div class="box1">
        <div class="left_name">问题清单总数<span class="span_class">{{ dataNum }}</span></div>
        <el-button @click="Adduser" type="primary" class="add_btn"><el-icon>
            <Plus />
          </el-icon>新增类型</el-button>
      </div>
    </div>
    <div class="tablebox">
      <!-- :default-expanded-keys="expandList"
            @node-expand="getNodeExpand"
            @node-collapse ="getNodeCollapse" 
      -->
      <el-scrollbar>
        <div style="margin: 0 10px 0 6px">
          <el-tree
            accordion
            ref="treeRef"
            :data="tableData"
            node-key="id"
            :expand-on-click-node="true"
            highlight-current
          >
            <template #default="{ node, data }">
            <div class="custom-tree-node">
            <div class="left" v-html="data.content"></div>
            <div class="center" v-if="data.level===3||data.total===0"></div>
            <div class="center" v-else>问题数量{{ data.total }}</div>
          <div class="right">
            <a @click.stop="appendData(data)" v-if="data.level!==3" class="addClass">新增</a>
            <a @click.stop="editData(data)" class="addClass">编辑</a>
            <a @click.stop="removeData(data)" class="addClass"> 删除 </a>
          </div>
        </div>
            </template>
          </el-tree>
        </div>
      </el-scrollbar>
    </div>
  </div>

  <el-dialog
    v-model="addBol"
    width="600px"
    @close="cancel"
    align-center
    :destory-on-close="true"
    :close-on-click-modal="false"
    class="my_Dialog"
  >
    <template #header>
      <div class="heard_name">
        <div class="d_name">
          {{type==='add'?'新增类型':'编辑类型'}}
        </div>
      </div>
    </template>
    <el-form ref="customForm" :inline="true" :model="fromData" :label-suffix="'：'" :label-width="100" class="customForm"
      size="large">
      <el-row>
        <el-col :span="24">
          <el-form-item label="内容" prop="content" :rules="[
            { required: true, message: '必填', trigger: 'blur' },
          ]" style="width: 100%">
            <!-- <el-input v-model="fromData.content" placeholder="请输入..."  :rows="3"
            type="textarea" maxlength="500" style="width: 100%;"></el-input> -->
            <div style="flex: 1;">
              <WangEditor :initialValue="fromData.content" :editorHeight="'100px'" @handleChange="handleChangeEdit" v-if="addBol"/>
            </div>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <div @click="cancel" class="sub_btn">取消</div>
        <div class="sub_btn sure_btn" @click="submit">保存</div>
      </div>
    </template>
  </el-dialog>
</template>
<script setup>
import {
  ref,
  onMounted,
  onBeforeUnmount,
  reactive,
  computed,
  watch,
  getCurrentInstance,
  nextTick,
} from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { ElLoading } from 'element-plus';
import {getProblemList,getProblemNum,addProblem,editProblem,delProblem} from "@/api/basic/index";
import WangEditor from '@/components/WangEditor';
const { proxy } = getCurrentInstance();
const addBol = ref(false)
let tableData = ref([]);
const dataNum = ref(0)
const fromData= ref({
  id:null,
  content:'',
  parentId:'',
})
let type = ref('add')
let treeRef = ref(null)
let expandList = ref([]) // 用于记录展开的节点
const getNodeExpand = (data,node,noInstance) => {
  if(! expandList.value.includes(node.key)){
    expandList.value.push(node.key)
  }
}
const getNodeCollapse = (data,node,noInstance) => {
  const index = expandList.value.indexOf(node.key)
  if(index!==-1){
    expandList.value.splice(index,1)
  }
}
const SetExpandkey = async (Keys) => {
  expandList.value = Keys
  await nextTick()
  treeRef.value.updateKeyChildren()
}
const Adduser = () => {
  addBol.value = true
  fromData.value.parentId = ''
  fromData.value.content = ''
  type.value = 'add'
}
const appendData = (data) => {
  fromData.value.parentId = data.id
  fromData.value.id = null
  addBol.value = true
  type.value = 'add'
}
const editData = (data) => {
  console.log(data)
  addBol.value = true
  type.value = 'edit'
  fromData.value.parentId = null
  fromData.value.id = data.id
  fromData.value.content = data.content
}
const removeData = (data) => {
 
  ElMessageBox.confirm(
    '您确定要删除此节点及其节点下的数据?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(async() => {
      let result = await delProblem({ id: data.id });
      if (result.code === 0) {
        getUserListData();
        ElMessage.success('删除成功')
      }
    })
    .catch(() => {
      console.log('已取消')
    })
 
}
const cancel = () => {
  resetForm('customForm')
};
const submit = async () => {
  proxy.$refs["customForm"].validate(valid => {
    if (valid) {
      if (type.value === 'edit') {
        // 编辑
        editProblem(fromData.value).then(res => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess("修改成功！");
            resetForm('customForm')
            getUserListData()
          }
        })
      } else {
        // 新增
        addProblem(fromData.value).then(res => {
          if (res.code === 0) {
            resetForm('customForm')
            proxy.$modal.msgSuccess("新增成功！");
            getUserListData()
          }
        })
      }
    }
  });
  // 
}
const handleChangeEdit = (value) => {
  fromData.value.content = value

}
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
    addBol.value = false
  }
}
onMounted(() => {
  getUserListData();
});
const getUserListData = () => {
  getProblemList().then((res) => {
    if (res.code === 0) {
      tableData.value = res.data;
      // nextTick(()=>{
      //   let keys = expandList.value
      //   if(keys?.length){
      //     SetExpandkey(keys)
      //     treeRef.value?.setExpandedkey(keys)
      //   }
       
      // })
    }
  });
  getProblemNum().then(res=>{
    dataNum.value = res.data
  })
};
onBeforeUnmount(() => {});
</script>
<style lang="scss" scoped>
.childbox {
  width: calc(100% - 40px);
  height: calc(100% - 20px);
  /* 减去上下 padding 之和 */
  background: #021b4f;
  padding: 20px 20px 0 20px;
}

.line1 {
  // display: flex;
  width: 100%;

  // justify-content: space-between;
  /* 新增这行代码 */
  .heard_name {
    background: url("../../assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;
    display: flex;
    align-items: center;

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

  .box1 {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: auto;
    height: 68px;
    .left_name{
      display: flex;
      align-items: center;
      font-family: PingFang SC;
      font-size: 18px;
      color: #FFFFFF;
      .span_class{
        font-size: 36px;
        margin-left: 16px;
      }
    }
    .add_btn {
      width: 90px;
      height: 36px;
      background: #274EEF;
      border-radius: 2px 2px 2px 2px;
      border: 0px;
      font-size: 14px;
    }
  }
}

.tablebox {
  width: 100%;
  height: calc(100% - 120px);
  overflow: auto;
  .custom-tree-node {
    display: flex;
    align-items: center;
    width: 100%;
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
      width: 80%;
      // display: flex;
      flex-wrap: wrap;
      word-break: break-all;
      white-space: pre-wrap;
      padding: 4px 0;
      line-height: 20px;
    }
    .center{
      width: 10%;
      text-align: center;
    }
    .right{
      // width: 30%;
      display: flex;
      align-items: center;
      justify-content: center;
      .addClass{
        color: #00ceff;
        margin-right: 8px;
      }
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

:deep(.el-input) {
  padding: 0;
  width: 250px;
  height: 40px;
}

:deep(.el-button .el-icon) {
  margin-right: 6px;
}

:deep(.el-checkbox) {
  height: 28px !important;
}

:deep(.el-checkbox-group) {
  margin: 20px;
}

:deep(.el-radio__label) {
  color: #fff;
}
.role_Box {
  .item {
    display: flex;
    align-items: center;
    justify-content: start;
    height: 38px;
    .label_class {
      font-weight: 500;
      font-size: 14px;
      color: #ffffff;
      width: 80px;
    }
    .right_data {
      flex: 1;
      display: flex;
      align-items: center;
      font-size: 14px;
      color: #ffffff;
      font-weight: 500;
      .item_box {
        width: 100px;
        height: 36px;
        background: rgba(30, 55, 147, 0.5);
        border-radius: 2px 2px 2px 2px;
        border: 1px solid #1e3793;
        text-align: center;
        line-height: 36px;
        margin-right: 16px;
        font-size: 14px;
        color: #ffffff;
        font-weight: 500;
      }
    }
  }
}
:deep(.el-scrollbar__view){
  width: 100%;
  height: 100%;
}
</style>
