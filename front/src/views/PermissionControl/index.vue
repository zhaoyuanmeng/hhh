<!-- 权限控制组件 -->
<template>
  <div class="mainbox">
    <div class="leftmeun">
      <nav class="navbar">
        <div @click="changefun('usershow')" class="navbar-button" :class="{ active: activename == 'usershow' }">
          <div class="itemMenu"><i class="icon_img"></i>
            用户管理
          </div>
          <div class="left_style" v-if="activename == 'usershow'"></div>
          <div class="triangle-right" v-if="activename == 'usershow'"></div>
        </div>
        <div @click="changefun('roleshow')" class="navbar-button" :class="{ active: activename == 'roleshow' }">
          <div class="itemMenu"><i class="icon_img icon1"></i>
            角色管理
          </div>
          <div class="left_style" v-if="activename == 'roleshow'"></div>
          <div class="triangle-right" v-if="activename == 'roleshow'"></div>
        </div>
        <div @click="changefun('groupshow')" class="navbar-button" :class="{ active: activename == 'groupshow' }">
          <div class="itemMenu"><i class="icon_img icon2"></i>
            部门管理
          </div>
          <div class="left_style" v-if="activename == 'groupshow'"></div>
          <div class="triangle-right" v-if="activename == 'groupshow'"></div>
        </div>
        <div @click="changefun('centerShow')" class="navbar-button" :class="{ active: activename == 'centerShow' }">
          <div class="itemMenu"><i class="icon_img icon3"></i>
            个人信息
          </div>
          <div class="left_style" v-if="activename == 'centerShow'"></div>
          <div class="triangle-right" v-if="activename == 'centerShow'"></div>
        </div>
        <div @click="changefun('Library')" class="navbar-button" :class="{ active: activename == 'Library' }">
          <div class="itemMenu"><el-icon style=" margin-right: 8px;font-size: 18px;"><Grid /></el-icon>
            问题清单库
          </div>
          <div class="left_style" v-if="activename == 'Library'"></div>
          <div class="triangle-right" v-if="activename == 'Library'"></div>
        </div>
        <el-menu
        default-active="activeIndex"
        ref="menuRef"
        class="el-menu-vertical-demo custom_menu"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon><Tickets /></el-icon>
            <span>数据管理</span>
          </template>
          <el-menu-item-group >
            <el-menu-item index="1-1" @click="getTaskData('1-1')" :class="{'activeClass':activeIndex==='1-1'}">任务数据</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group >
            <el-menu-item index="1-2" @click="getTaskData('1-2')" :class="{'activeClass':activeIndex==='1-2'}">数据更新</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
      </el-menu>
      
      </nav>
    </div>
    <div class="rightmain">
      <UserManage v-if="activename == 'usershow'"></UserManage>
      <RoleManage v-if="activename == 'roleshow'"></RoleManage>
      <GroupManage v-if="activename == 'groupshow'"></GroupManage>
      <CenterInfo v-if="activename == 'centerShow'" />
      <TaskData v-if="activename==='taskData'"/>
      <DataUpdate v-if="activename==='dataUpdate'"/>
      <ProblemLibrary v-if="activename==='Library'"/>
    </div>
  </div>
</template>
<script setup>
import { ref } from "vue";
import UserManage from "./usermanage.vue";
import RoleManage from "./rolemanage.vue";
import GroupManage from "./groupmanage.vue";
import CenterInfo from "./centerInfo.vue"
import TaskData from "./taskData.vue";
import DataUpdate from './dataUpdate.vue'
import ProblemLibrary from "./problemLibrary.vue";
let activename = ref("usershow");
let type = ref(0);
const activeIndex = ref('')
const menuRef= ref(null)
function changefun(name) {
  activename.value = name;
  type.value = 0;
  menuRef.value&&menuRef.value.close('1');
  activeIndex.value = '1'
}
function setyb(name, typeid) {
  type.value = typeid;
  activename.value = name;
}
const getTaskData = (e) => {
activeIndex.value = e
if(e==='1-1'){
  activename.value = 'taskData';
}
if(e==='1-2'){
  activename.value = 'dataUpdate';
}
}
const handleOpen = (key, keyPath) => {
  console.log(key, keyPath)
}
const handleClose = (key, keyPath) => {
  console.log(key, keyPath)
}
</script>
<style lang="scss" scoped>
.mainbox {
  position: absolute;
  display: flex;
  top: 60px;
  width: 100%;
  height: calc(100% - 60px);
  z-index: 30;
  background: #000000;
}

.leftmeun {
  width: 200px;
  height: 100%;
  margin-top: 10px;
  background: linear-gradient(360deg, rgba(0, 20, 70, 0.8) 0%, rgba(0, 39, 105, 0.7088) 100%);
  margin-top: 10px;

  .title {
    margin-left: 27px;
    margin-top: 18px;
    font-size: 20px;
    color: #000000;
    font-weight: bold;
  }

  .navbar {

    // margin-top: 15px;
    // background-color: #f0f2f5;
    .itemMenu {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      padding-left: 34px;

      .icon_img {
        width: 16px;
        height: 16px;
        background: url('../../assets/images/userM.png') no-repeat;
        background-size: 100% 100%;
        margin-right: 8px;
      }

      .icon1 {
        background: url('../../assets/images/roleM.png') no-repeat;
        background-size: 100% 100%;
      }

      .icon2 {
        background: url('../../assets/images/orgM.png') no-repeat;
        background-size: 100% 100%;
      }

      .icon3 {
        background: url('../../assets/images/personM.png') no-repeat;
        background-size: 100% 100%;
      }
    }

  }

  .navbar-button {
    width: 100%;
    height: 40px;
    display: flex;
    justify-content: center;
    /* 水平居中 */
    align-items: center;
    /* 垂直居中 */
    // background-color: #ffffff;
    cursor: pointer;
    transition: background-color 0.3s;
    font-size: 18px;
    color: #fff;
    font-family: Source Han Sans, Source Han Sans;
    font-weight: 400;
    font-size: 16px;
    position: relative;

    &:hover {
      // background-color: #e6e6e6;
      background: linear-gradient(90deg, #004ADB 0%, rgba(0, 40, 117, 0) 100%);
    }

    &.active {
      background: linear-gradient(90deg, #004ADB 0%, rgba(0, 40, 117, 0) 100%);
      color: #fff;
      // background-color: rgba(10, 111, 255, 0.12);
      // color: #053eff;
    }
    .left_style{
      position: absolute;
      left: 0;
      width: 1px;
      height: 100%;
      background: #fff;
    }
    .triangle-right {
      position: absolute;
      left: 1px;
      width: 0;
      height: 0;
      border-top: 5px solid transparent;
      border-left: 6px solid #fff; /* 黄色三角形 */
      border-bottom: 5px solid transparent;
}
  }
}

.rightmain {
  width: calc(100% - 200px);
  /* 占据剩余空间 */
  padding: 10px;

}

:deep(.my_Dialog) {
  background: url("../../assets/panel/弹窗bg.png") no-repeat;
  background-size: 100% 100%;
  padding: 20px 32px 37px 32px;

  .heard_name {
    background: url("../../assets/panel/right_panel.png") no-repeat;
    background-size: contain;
    height: 28px;

    // display: flex;
    // align-items: center;
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

  .el-dialog__headerbtn:active {
    background-color: transparent !important;
    outline: none !important;
    box-shadow: none !important;
  }

  :deep(.el-dialog__headerbtn) {
    position: absolute;
    top: 10px;
    right: 20px;

    .el-dialog__close {
      color: #fff;
      font-size: 20px;
    }
  }

  :deep(.el-dialog__headerbtn :focus, .el-dialog__headerbtn:hover) {
    outline: none;
    box-shadow: none;
  }

  /* 解决按钮黑边框bug */
  :deep(.el-button:focus) {
    outline: none;
  }

  :deep(.el-dialog__body) {
    padding-right: 30px;
  }

}

:deep(.dialog-footer) {
  display: flex;
  justify-content: end;
  padding-right: 20px;

  .sub_btn {
    width: 88px;
    height: 36px;
    line-height: 36px;
    text-align: center;
    background: rgba(31, 76, 152, 0.87);
    border-radius: 4px 4px 4px 4px;
    cursor: pointer;
  }

  .sure_btn {
    margin-left: 16px;
    background: #274eef;
  }
}

:deep(.customForm) {
  .el-form-item__label {
    font-weight: 400;
    font-size: 14px;
    color: #ffffff;
    opacity: 0.8;
  }

  .el-form-item__content {
    .el-input {
      border: 1px solid #5b6799;
      border-radius: 2px;

      .el-input__wrapper {
        background: rgba(0, 12, 78, 0.5);
        box-shadow: none;
      }

      .el-input__inner {
        // height: 36px;
        // line-height: 36px;
        font-size: 14px;
        color: #ffffff;
        opacity: 0.8;
      }
    }
  }

  .el-select__wrapper {
    background: rgba(0, 12, 78, 0.5);
    box-shadow: none;
    border: 1px solid #5b6799;

    .el-select__selected-item {
      color: #ffffff;
      opacity: 0.8;
    }

    .is-transparent {
      color: #a8abb2;
      // opacity: 0.8;
    }
  }

  .el-textarea__inner {
    background: rgba(0, 12, 78, 0.5);
    box-shadow: none;
    border: 1px solid #5b6799;
    color: #fff;
    opacity: 0.8;
  }
}

:deep(button:focus, button:focus-visible) {
  outline: none;
}
:deep(.custom_menu){
  height: 40px;
    align-items: center;
    justify-content: center;
    display: flex;
    background: none;
    border: none;
    font-size: 16px;
    color: #fff;
    .el-menu-item:hover {
  background-color: transparent !important;
}
.el-sub-menu__title:hover{
  background-color: transparent !important;
}
.el-menu-item:hover {
  color: #fff;
}
.el-menu-item{
  color: #fff;
}
.activeClass{
  // color: #409EFF;
  background: linear-gradient(90deg, #004ADB 0%, rgba(0, 40, 117, 0) 100%);
    color: #fff;
    height: 40px;
}
.el-menu-item-group__title{
  display: none;
}
.el-menu{
  background-color: transparent !important;
}
    .el-sub-menu{
      height: 40px;
      .el-sub-menu__title{
        color: #fff;
        font-size: 16px;
        padding-left: 0;
      }
    }
}
</style>
