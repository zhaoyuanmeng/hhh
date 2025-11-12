<template>
  <div class="childbox">
    <div class="line1">
      <div class="heard_name">
        <div class="d_name">个人信息</div>
      </div>
      <div class="box1">
        <el-form ref="customForm" :inline="true" :model="form" :label-suffix="'：'" :label-width="100" class="customForm"
          size="large">
          <el-row>
            <el-col :span="12">
              <el-form-item label="姓名" style="width: 100%">
                {{ form.realName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="性别" style="width: 100%">
                {{ form.sex === 0 ? '男' : '女' }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用户名" style="width: 100%">
                {{ form.userName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="电话" style="width: 100%">
                {{ form.phone }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="角色" style="width: 100%">
                {{ form.roleName }}
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="机构" prop="orgId" style="width: 100%">
                {{ form.orgFullName }}
              </el-form-item>
            </el-col>
            <div class="changePsw" @click="changPsw = true" v-if="!changPsw">修改密码</div>
            <el-col :span="12" v-if="changPsw">
              <el-form-item label="密码" prop="password" :rules="[
                { required: true, message: '必填', trigger: 'blur' },
              ]" style="width: 100%">
                <el-input v-model="form.password" type="password" show-password></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12" v-if="changPsw">
              <el-form-item label="确认密码" prop="passwordOk" :rules="[
                { required: true, message: '必填', trigger: 'blur' },
              ]" style="width: 100%">
                <el-input v-model="form.passwordOk" type="password" show-password></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div class="dialog-footer" style="justify-content:center;margin-top: 50px;" v-if="changPsw">
          <div @click="cancel" class="sub_btn">取消</div>
          <div class="sub_btn sure_btn" @click="submit">保存</div>
        </div>
      </div>
    </div>
  </div>
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
import { getUserInfo, updateUser } from '@/api/system'
import useUserStore from "@/store/modules/user";
const { proxy } = getCurrentInstance()
let form = ref({
  realName: '',
  sex: 0,
  userName: '',
  phone: '',
  roleName: '',
  orgFullName: ''
})
const changPsw = ref(false)
onMounted(() => {
  getUserInfo().then(res => {
    console.log(res)
    if (res.code === 0) {
      form.value = res.data
      form.value.password = ''
      form.value.passwordOk = ''
    }

  })
})
const cancel = () => {
  form.value.password = ''
  form.value.passwordOk = ''
  changPsw.value = false
}
const submit = () => {
  proxy.$refs["customForm"].validate(valid => {
    if (valid) {
      // 新增
      if (form.value.passwordOk !== form.value.password) {
        proxy.$modal.msgWarning('密码不一致，请重新输入')
        form.password = ''
        form.passwordOk = ''
        return false
      }
      // 编辑
      updateUser(form.value).then(res => {
        if (res.code === 0) {
          ElMessageBox.alert('密码修改成功！请重新登录', '提示', {
            confirmButtonText: '确定',
            showClose: false,
            callback: (action) => {
              useUserStore().set_Token()
              location.href = '/#/login'
            }
          })

        }
      })
    }
  });
}
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
    width: 50%;
    padding: 20px 0;

    .changePsw {
      width: 100px;
      text-align: right;
      color: aqua;
      cursor: pointer;
      font-size: 14px;
    }
  }
}
</style>
  