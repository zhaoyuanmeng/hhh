<template>
  <div class="childbox">
    <div class="line1">
      <div class="heard_name">
        <div class="d_name">用户管理</div>
      </div>
      <div class="box1">
        <el-button @click="Adduser" type="primary" class="add_btn"><el-icon>
            <Plus />
          </el-icon> 新增</el-button>
        <el-input v-model="query.entity.keyword" placeholder="搜索用户名/真实姓名" class="serach" prefix-icon="Search">
          <template #append>
            <div @click="SearchEvent" class="search_input">搜索</div>
          </template>
        </el-input>
      </div>
    </div>
    <div class="tablebox">
      <el-table :data="tableData" highlight-current-row style="width: 100%; height: 80%;background: #021B4F;"
        :header-cell-style="{
          fontFamily: 'Arial',
          color: '#fff',
          fontWeight: '500',
          fontSize: '14px',
        }">
        <el-table-column type="index" label="序号" min-width="4%" align="center" />
        <el-table-column prop="realName" label="姓名" min-width="12%" align="center" />
        <el-table-column prop="roleId" label="角色" min-width="12%" align="center">
          <template #default="{ row }">
            <span>{{ getRoleName(row.roleId) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="用户名" min-width="15%" align="center" />
        <el-table-column prop="sex" label="性别" min-width="5%" align="center">
          <template #default="{ row }">
            <span>{{ row.sex === 0 ? "男" : "女" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" min-width="10%" align="center" />
        <el-table-column prop="orgFullName" label="部门" min-width="13%" align="center">
          <template #default="{ row }">
            <span>{{ getOrgName(row.orgId) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="账户状态" min-width="6%" align="center">
          <template #default="{ row }">
            <span>{{ row.status === 0 ? "正常" : "停用" }}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="15%" align="center">
          <template #default="scope">
            <el-button @click="showAuthorityList($event, scope.row)" style="color: #00CEFF;" link type="primary">
              权限
            </el-button>
            <el-button @click="edit(scope.row)" style="color: #00CEFF;" link type="primary">
              编辑
            </el-button>
            <el-button @click="deleterow(scope.row)" style="color: #00CEFF;" link type="primary">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="margin-top: 20px" background :current-page="query.page.pageNo"
        :page-size="query.page.pageSize" :total="total" @current-change="handlePageChange"
        layout="total,prev, pager, next"></el-pagination>
    </div>
  </div>

  <el-dialog v-model="addUser" width="714px" @close="cancel" align-center :destory-on-close="true"
    :close-on-click-modal="false" class="my_Dialog">
    <template #header>
      <div class="heard_name">
        <div class="d_name">{{ titleType === 'add' ? "新增用户" : "编辑用户" }}</div>
      </div>
    </template>
    <el-form ref="customForm" :inline="true" :model="form" :label-suffix="'：'" :label-width="100" class="customForm"
      size="large">
      <el-row>
        <el-col :span="12">
          <el-form-item label="姓名" prop="realName" :rules="[
            { required: true, message: '必填', trigger: 'blur' },
          ]" style="width: 100%">
            <el-input v-model="form.realName" placeholder="请输入..."></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="性别" style="width: 100%">
            <el-radio-group v-model="form.sex" class="ml-4">
              <el-radio :value="0" size="large">男</el-radio>
              <el-radio :value="1" size="large">女</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="用户名" style="width: 100%" prop="userName" :rules="[
            { required: true, message: '必填', trigger: 'blur' },
          ]">
            <el-input v-model="form.userName" placeholder="请输入..."></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电话" style="width: 100%" prop="phone" :rules="[
            { required: true, message: '必填', trigger: 'blur' },
          ]">
            <el-input v-model="form.phone" placeholder="请输入...">
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="角色" style="width: 100%" prop="roleId" :rules="[
            { required: true, message: '必填', trigger: 'change' },
          ]">
            <el-select v-model="form.roleId" placeholder="选择" size="large" style="width: 100%">
              <el-option v-for="item in rolearr" :key="item.id" :label="item.roleName" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="机构" prop="orgId" style="width: 100%" :rules="[
            { required: true, message: '必填', trigger: 'change' },
          ]">
            <el-select v-model="form.orgId" placeholder="选择" size="large" style="width: 100%">
              <el-option v-for="item in grouparr" :key="item.id" :label="item.shortName" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="titleType === 'add'">
          <el-form-item label="密码" prop="password" :rules="[
            { required: true, message: '必填', trigger: 'blur' },
          ]" style="width: 100%">
            <el-input v-model="form.password"  type="password" show-password></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="titleType === 'add'">
          <el-form-item label="确认密码" prop="passwordOk" :rules="[
            { required: true, message: '必填', trigger: 'blur' },
          ]" style="width: 100%">
            <el-input v-model="form.passwordOk" type="password" show-password></el-input>
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


  <el-dialog v-model="userRole" width="680px"  align-center :destory-on-close="true"
    :close-on-click-modal="false" class="my_Dialog">
    <template #header>
      <div class="heard_name">
        <div class="d_name">用户权限</div>
      </div>
    </template>
    <div class="role_Box">
        <div class="item">
          <div class="label_class">姓名</div>
          <div class="right_data" >{{ selectRole.name }}</div>
        </div>
        <div class="item">
          <div class="label_class">权限范围</div>
          <div class="right_data" >
            <div class="item_box" v-for="(item,index) in selectRole.roleList" :key="index">{{ item }}</div>
          </div>
        </div>
    </div>
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
  nextTick
} from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { getUserList, addUserApi, getOrgList, getRoleList, deleteUser,updateUser } from '@/api/system'
const { proxy } = getCurrentInstance()
const query = reactive({
  page: {
    pageNo: 1,
    pageSize: 15
  },
  entity: {
    keyword: ""
  }
})
let total = ref(0)
let tableData = ref([]);
let addUser = ref(false)
let userRole = ref(false)
let customForm = ref(null)
let titleType = ref('')
// 新增参数
let form = reactive({
  id: null,
  realName: "",
  roleId: "",
  userName: "",
  phone: "",
  sex: 0,
  orgId: "",
  status: 0,
  password: "",
  passwordOk: '',
});
const selectRole = reactive({
  name:'',
  roleList:[]
})
//角色数组
let rolearr = ref([]);
//机构数组
let grouparr = ref([]);
const getRoleName = (roleId) => {
  if(rolearr.value.length>0){
      let name = ''
      rolearr.value.forEach(item=>{
        if(item.id===roleId){
          name = item.roleName
        }
      })
      return name
    }else{
      return ''
    }
}
const getOrgName = (orgId) =>{
  if(grouparr.value.length>0){
      let name = ''
      grouparr.value.forEach(item=>{
        if(item.id===orgId){
          name = item.shortName
        }
      })
      return name
    }else{
      return ''
    }
}
const cancel = () => {
  resetForm('customForm')
};
const submit = async () => {
  proxy.$refs["customForm"].validate(valid => {
    if (valid) {
      if (titleType.value === 'edit') {
        // 编辑
        updateUser(form).then(res => {
          if (res.code === 0) {
            proxy.$modal.msgSuccess("修改成功！");
            resetForm('customForm')
            getUserListData()
          }
        })
      } else {
        // 新增
        if (form.passwordOk !== form.password) {
        proxy.$modal.msgWarning('密码不一致，请重新输入')
        form.password = ''
        form.passwordOk = ''
        return false
      }
        addUserApi(form).then(res => {
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
// 表单重置
const resetForm = (refName) => {
  if (proxy.$refs[refName]) {
    proxy.$refs[refName].resetFields();
    addUser.value = false
  }
}
//新增用户
function Adduser() {
  form.realName = "";
  form.roleId = "";
  form.userName = "";
  form.phone = "";
  form.sex = 0;
  form.orgId = "";
  form.status = 0;
  form.password = "";
  form.passwordOk = "";
  form.id = null
  titleType.value = 'add'
  addUser.value = true
}
function handlePageChange(newPage) {
  query.page.pageNo = newPage
  nextTick(() => {
    getUserListData()
  })
}

async function getuserlist() {
  let roleresult = await getRoleList();
  let groupresult = await getOrgList();

  const newroleData = roleresult.data.map((item) => {
    return {
      id: item.id,
      roleName: item.roleName,
    };
  });
  const newgroupData = groupresult.data.map((item) => {
    return {
      id: item.id,
      shortName: item.fullName,
    };
  });
  rolearr.value = newroleData;
  grouparr.value = newgroupData;
}
onMounted(() => {
  getuserlist();
  getUserListData()
});
const getUserListData = () => {
  getUserList(query).then(res => {
    console.log(res)
    if (res.code === 0) {
      tableData.value = res.data.records
      total.value = res.data.totalCount
    }
  })
}
//执行查询，更新表格
function SearchEvent() {
  getUserListData();
}
//查看当前用户角色的权限
function showAuthorityList(event, row) {
  console.log(row)
  selectRole.name = row.realName
  selectRole.roleList = row.resources.split(',')
  userRole.value = true
}
//编辑用户信息
function edit(row) {
  console.log(row)
  form.realName = row.realName
  form.roleId = row.roleId
  form.userName = row.userName
  form.phone = row.phone
  form.sex = row.sex;
  form.orgId = row.orgId
  form.status = row.status
  form.password = row.password
  titleType.value = 'edit'
  addUser.value = true
}
//删除某一行
function deleterow(row) {
  console.log(row)
  ElMessageBox.confirm("确认要删除此用户信息吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(async () => {
      let result = await deleteUser({ id: row.id });
      if (result.code === 0) {
        getUserListData();
        ElMessage({
          type: "success",
          message: "已删除完成",
        });
      }
    })
    .catch(() => {
   
    });
}

onBeforeUnmount(() => {
});
</script>
<style lang="scss" scoped>
.childbox {
  width: calc(100% - 40px);
  height: calc(100% - 20px);
  /* 减去上下 padding 之和 */
  background: #021B4F;
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
    justify-content: start;
    width: auto;
    height: 68px;

    .add_btn {
      width: 88px;
      height: 36px;
      background: #274EEF;
      border-radius: 2px 2px 2px 2px;
      border: 0px;
      font-size: 14px;
    }

    .serach {
      height: 36px;
      margin-left: 20px;

    }

    :deep(.el-input-group__append) {
      background: #274EEF;
      color: #fff;
      box-shadow: none;
      cursor: pointer;
      padding: 0 8px;
    }

    :deep(.el-input__wrapper) {
      background: linear-gradient(360deg, rgba(0, 20, 70, 0.8) 0%, rgba(0, 39, 105, 0.7088) 100%);
      box-shadow: none;
      border: 1px solid #0424AA;
    }

    :deep(.el-input__inner) {
      color: #fff;
    }
  }
}

.tablebox {
  width: 100%;
  height: calc(100% - 96px);
  overflow: auto;

  :deep(.el-table) {
    --el-table-border-color: #071336;
  }

  :deep(.el-table__header) {
    height: 48px;
  }

  :deep(.el-table .el-table__header tr th) {
    background: rgba(31, 76, 152, 0.8);
    color: #fff;
  }

  :deep(.el-table .el-table__row .el-table__cell) {
    background: rgba(31, 76, 152, 0.16);
    // height: 40px;
  }

  :deep(.el-table tr) {
    background: #021B4F;
    height: 48px;
  }

  :deep(.el-table .cell) {
    color: #fff;
  }

  :deep(.el-table__row:hover) {
    background-color: rgba(0, 206, 255, 0.2) !important;
    /* 使用您想要的颜色替换 #your-color-code */
  }

  :deep(.el-pagination) {
    display: flex;
    align-items: center;
    justify-content: center;

    .el-pagination__total {
      color: rgba(255, 255, 255, 0.7);
    }

    .el-pager {
      .is-active {
        background: #274EEF;
      }
    }

    .btn-prev,
    .btn-next {
      background: none;

      .el-icon {
        font-size: 16px;
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
.role_Box{
  .item{
    display: flex;
    align-items: center;
    justify-content: start;
    height: 38px;
    .label_class{
      font-weight: 500;
      font-size: 14px;
      color: #FFFFFF;
      width: 80px;
    }
    .right_data{
      flex: 1;
      display: flex;
      align-items: center;
      font-size: 14px;
        color: #FFFFFF;
        font-weight: 500;
      .item_box{
        width: 100px;
        height: 36px;
        background: rgba(30,55,147,0.5);
        border-radius: 2px 2px 2px 2px;
        border: 1px solid #1E3793;
        text-align: center;
        line-height: 36px;
        margin-right: 16px;
        font-size: 14px;
        color: #FFFFFF;
        font-weight: 500;
      }
    }
  }
}
</style>
