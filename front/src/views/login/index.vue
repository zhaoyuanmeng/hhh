<template>
  <div class="login_bg">
    <div class="projectName">基础工作与实战指挥平台</div>
    <div class="login">
      <div class="loginName">登录</div>
      <div class="login_content">
        <div class="top_type">
          <div class="login_type">
            账号密码登录
          </div>
          <div class="line">
            <div class="bottom_line"></div>
          </div>
        </div>
        <div class="username">
          <!-- <input v-model="username" type="text" placeholder="请输入用户名" /> -->
          <el-input v-model="username" type="text" size="large" style="height:50px" auto-complete="off"
            placeholder="请输入用户名" clearable @input="changeInput">
            <template #prefix><i class="img_yhm"></i></template>
          </el-input>
        </div>
        <div class="password">
          <!-- <input v-model="password" :type="pswtype" placeholder="请输入密码" /> -->
          <el-input v-model="password" type="password" size="large" auto-complete="off" style="height:50px"
            placeholder="请输入密码" @keyup.enter="logining" clearable @input="changeInput">
            <template #prefix><i class="img_i"></i></template>
          </el-input>
        </div>
        <div class="error-info">
          {{ state.errorInfo }}
        </div>
        <input class="login_action" type="button" value="登录" @click="logining" />
        <div class="call">联系管理员，添加账号</div>
      </div>
    </div>
  </div>
</template>

<script setup>
// import { sessionStorage } from '@/utils/storage'
import { setToken } from '@/utils/auth'
import { login } from '@/api/login'
import { reactive, ref, nextTick } from "vue";
import { useRouter } from "vue-router";
import useUserStore from '@/store/modules/user'
import { ElMessageBox } from 'element-plus'
const router = useRouter();
const username_cache = localStorage.getItem("username");
const password_cache = localStorage.getItem("password");

const username = ref("");
const password = ref("");
const state = reactive({ errorInfo: "" });
let pswtype = ref("password");
let imgtype = ref("pswhide");

if (username_cache) {
  username.value = username_cache;
}

if (password_cache) {
  password.value = password_cache;
}

const changeInput = () => {
  state.errorInfo = ''
}
const moveToFront = (value) => {
  if (this.items.includes(value)) {
    this.items.unshift(value);
  }
}
function logining() {
  if (username.value === "" || password.value === "") {
    state.errorInfo = "用户名或者密码不能为空";
    return;
  }
  let parm = { userName: username.value, password: password.value };
  login(parm).then(res => {
    console.log(res)
    if (res.code === 0) {
      localStorage.setItem('token', res.data.token)
      setToken(res.data.token)
      console.log(res)
      useUserStore().set_userInfo(res.data)
      if (res.data.resources) {
        let names = res.data.resources.split(',')
        let routers = []
        let ogList = []
        names.forEach(item => {
          if (item === '工作驾驶舱') {
            ogList.unshift(item);
          } else {
            ogList.push(item);
          }
        })
        ogList.forEach((item) => {
          if (item === '工作驾驶舱') {
            routers.push({ path: "/work-cockpit", title: "工作驾驶舱", name: 'WorkCockpit' })
          }
          if (item === '基础数据') {
            routers.push({ path: "/index", title: "基础数据", name: 'Home' })
          }
          if (item === '任务推演') {
            routers.push({ path: "/task", title: "任务管理", name: 'Task' })
          }
          // if (item === '应急预案') {
          //   routers.push({ path: "/emergency-plan", title: "应急预案", name: 'EmergencyPlan' })
          // }
          if (item === '后台管理') {
            routers.push({ path: "/system", title: "后台管理", name: 'System' })
          }
        })
        console.log(routers)
        router.push({ name: routers[0].name })
        useUserStore().set_menuList(routers)
      } else {
        ElMessageBox.alert('暂无菜单权限请更换账号登录', '提示', {
          confirmButtonText: '确定',
          showClose: false,
          callback: (action) => {
            useUserStore().set_Token()
            router.push({ name: 'Login' })
          }
        })
      }
    }
  })

}
</script>

<style lang="scss" scoped>
.login_bg {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  background: url("../../assets/images/login/bg_login.png") center center no-repeat;
  background-size: 100% 100%;
  font-weight: normal;
}

.login {
  width: 500px;
  height: 600px;
  position: absolute;
  top: 50%;
  right: 200px;
  transform: translateY(-50%);
  // background-color: #fff;
  background: url("../../assets/images/login/login_box.png") center center no-repeat;
  background-size: 100% 100%;
  color: #fff;
  font-size: 16px;

  .loginName {
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: YouSheBiaoTiHei;
    font-weight: 400;
    font-size: 30px;
    color: #FFFFFF;
    letter-spacing: 4px;
    margin-top: 40px;
  }

  .login_content {
    padding: 0 80px;

    .top_type {
      margin-top: 100px;

      .login_type {
        width: 108px;
        height: 30px;
        font-family: Source Han Sans, Source Han Sans;
        font-weight: 700;
        font-size: 18px;
        color: #274EEF;
        line-height: 30px;
        text-align: left;
        font-style: normal;
        text-transform: none;
      }

      .line {
        margin: 20px 0 30px 0;
        width: 100%;
        height: 1px;
        background: rgba(73, 106, 247, 0.6);
        position: relative;

        .bottom_line {
          width: 108px;
          height: 3px;
          background: #274EEF;
          position: absolute;
          bottom: -1px;
          left: 0;
        }
      }
    }

    :deep(.el-input__wrapper) {
      background: #0C184B;
      border-radius: 2px 2px 2px 2px;
      border: 1px solid #1E3793;
      box-shadow: none;
    }

    :deep(.el-input__inner) {
      color: #fff;
      background: none !important;
    }
  }
}

input {
  width: 300px;
  background: #0C184B;
  border-radius: 2px 2px 2px 2px;
  border: 1px solid #1E3793;
  height: 50px;
  color: black;
  font-size: 16px;
  padding-left: 40px;
}

input:focus {
  outline: none;
}

input::-webkit-input-placeholder {
  color: rgb(104, 103, 103);
}

.username {
  margin-bottom: 30px;
  background: #0C184B;
  border-radius: 2px 2px 2px 2px;
  border: 1px solid #1E3793;
  // margin-top: 174px;
  // margin-left: 551px;
}

.password {
  // margin-top: 20px;
  // margin-left: 551px;
  position: relative;

  .flagtype {
    position: absolute;
    top: 7px;
    right: 80px;
    width: 24px;
    height: 24px;
  }
}

.username input {
  background: url("../../assets/images/login/user.png") left center no-repeat white;
  background-size: 30px 30px;
}

.password input {
  background: url("../../assets/images/login/password.png") left center no-repeat white;
  background-size: 30px 30px;
}

.login_action {
  // margin-left: 551px;
  background: #2c60ff;
  height: 50px;
  width: 100%;
  color: #fff;
  border: none;
  cursor: pointer;
  padding: 0;
  background: #274EEF;
  font-size: 18px;
  letter-spacing: 2px;
  border-radius: 4px;
}

.error-info {
  margin-top: 10px;
  height: 40px;
  font-size: 14px;
  line-height: 20px;
  color: #f00;
}

.img_i {
  width: 24px;
  height: 24px;
  background: url('../../assets/images/login/psw.png') center center no-repeat;
  background-size: 100% 100%;
}

.img_yhm {
  width: 24px;
  height: 24px;
  background: url('../../assets/images/login/yhm.png') center center no-repeat;
  background-size: 100% 100%;
}

.call {
  font-family: Source Han Sans CN, Source Han Sans CN;
  font-weight: 400;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.5);
  text-align: left;
  font-style: normal;
  text-transform: none;
  margin-top: 10px;
}

.projectName {
  height: 36px;
  font-family: YouSheBiaoTiHei;
  font-weight: 400;
  font-size: 48px;
  color: #FFFFFF;
  line-height: 36px;
  text-align: left;
  font-style: normal;
  text-transform: none;
  position: absolute;
  left: 200px;
  top: 18%;
}
</style>
<style>
input:-internal-autofill-selected{
  background:none !important;
}
</style>
