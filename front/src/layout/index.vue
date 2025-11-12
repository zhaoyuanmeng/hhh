<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-07 09:15:20
 * @LastEditTime: 2024-06-20 19:24:24
 * @LastEditors: Alex
-->
<template>
  <div class="dataScreen-container">
    <div class="dataScreen-content" ref="dataScreenRef">
      <div class="dataScreen-header">
        <div class="header-lf"></div>
        <div class="header-ct">
          <div class="menuList">
            <div v-for="(menu, index) in tabListNew" v-bind:class="{
              'menu-active': activeMenu(menu.path, menu.title),
            }" :key="index" class="menu_item" @click="jumpRouter(menu)">
              <router-link :to="menu.path">
                <span class="link_text">{{ menu.title }}</span>
              </router-link>
            </div>
          </div>
        </div>
        <div class="header-ri">
          <div class="h_left" v-if="false">
            <div class="h_left_content">
              <div class="h_right_text">晴</div>
              <div class="h_right_text">18­°C</div>
            </div>
            <div class="h_left_content" style="margin-left: 4px">
              <div class="h_right_text">
                风速<span class="heard_span">3级</span>
              </div>
              <div class="h_right_text">
                空气质量<span class="heard_span">良好</span>
              </div>
            </div>
          </div>
          <div class="h_center">
            <div class="h_right_text h_center_top">
              <div>{{ sf }}</div>
              <div class="week">{{ week }}</div>
            </div>
            <div class="h_right_text">{{ ymd }}</div>
          </div>
          <div class="h_right">
            <!-- <div class="h_right_text" style="font-size: 14px">
              上午好，张警官
            </div>
            <div>
              <el-icon>
                <CaretBottom />
              </el-icon>
            </div> -->
            <el-dropdown style="color: #fff;">
              <span class="el-dropdown-link">
                {{ name }}
                <el-icon>
                  <CaretBottom />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="loginOut">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
      <div class="dataScreen-main">
        <router-view/>
        <SmartMap ref="smartMapRef" :loadAfter="loadAfterFun" v-if="userStore.routerIndex !== 'System'" />
        <!-- <VectorRoadMap v-if="userStore.routerIndex !== 'System'&&loadShow" :styles="style"/> -->
      </div>
    </div>
  </div>
</template>

<script setup name="ScreenLayout">
import useUserStore from "@/store/modules/user";
import useSettingStore from "@/store/modules/settingStore";
import useScreenStore from "@/store/modules/screenStore";
import useFloorStore from '@/store/modules/floorStore'
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import usePlanTaskStore from "@/store/modules/planTask";
import useBasicStore from "@/store/modules/basicData";
import SmartMap from "@/components/SmartMap";
import VectorRoadMap from "@/components/vectorRoadMap"
import { drawGsGtData } from "@/components/SmartMap/js/utils";
import { ref, onMounted, onBeforeUnmount, watch,computed,nextTick  } from "vue";
import {getProjectShowData} from '@/api/basic/index'
import { useRouter } from "vue-router";
import { useRoute } from "vue-router";
import {addCommunityMerchant} from '@/components/SmartMap/js/addMarkers'
import dayjs from "dayjs";
const router = useRouter();
const route = useRoute();
const showPage = ref(false)
const userStore = useUserStore();
const style = ref({
  bottom:'0px',
  right:'0px'
})
// 监听当前路由的name变化
watch(
  () => router.currentRoute.value.name,
  (newRouterName) => {
    useBasicStore().set_roadMap(false)
    useSettingStore().setShowTool(false)
    userStore.setRouterName(newRouterName)
    useScreenStore().set_editScreen(false)
    usePlanTaskStore().setCurPage(false)
    if(newRouterName==='System'){
      useWorkCockpitStore().set_loadStatus(false)
    }
    if(newRouterName==='WorkCockpit'){
      style.value.bottom = '240px'
      style.value.right = '340px'
    }else{
      style.value.bottom = '10px'
      style.value.right = '30px'
    }

  },
  { immediate: true }
)

const smartMapRef = ref(null);
const dataScreenRef = ref(null)
const rightMenuList = ref([
  { path: "/index", title: "基础数据" },
  { path: "/task", title: "任务推演" },
  { path: "/system", title: "后台管理" },
]);
let tabListNew = computed(() => userStore.menuList)
let name = computed(()=>userStore.userInfo.realName)
const loadShow = computed(()=>useWorkCockpitStore().loadStatus)
const ymd = ref(dayjs().format("YYYY年MM月DD日"));
const sf = ref(dayjs().format("HH:mm"));
const week = ref(null);
const activeMenu = (path, name) => {
  if (path === route.path) {
    return true;
  }
};
const jumpRouter = (menu) => {
  console.log(menu,route.path)
}
// 加载成功后的回调
const loadAfterFun = () => {
  console.log(router)
  useWorkCockpitStore().set_loadStatus(true)
  console.log("地图加载完后回调事件");
  // 获取全局展示的社区 楼栋数据
  getProjectShowData().then(res=>{
    useFloorStore().set_projectShowData(res.data)
    nextTick(()=>{
      addCommunityMerchant()
    })
  })
  if(router.currentRoute.value.name!=='Task'){
    let  g = window.__g
    g.infoTree.show('8C6F67F84F71F11130C1BEACD0BAF612')
    g.infoTree.show('A3B755BF43736C15455300A0FB44761F')
    g.infoTree.show('8B6725594EA91CCA8431338C12C4399F')
    drawGsGtData(1)
  }else{
    drawGsGtData(0)
  }
};
onMounted(() => {
  // if (dataScreenRef.value) {
  //   dataScreenRef.value.style.transform = `scale(${getScale()}) translate(-50%, -50%)`;
  //   dataScreenRef.value.style.width = `1920px`;
  //   dataScreenRef.value.style.height = `1080px`;
  // }
  // window.addEventListener("resize", resize);
});

// 设置响应式
const resize = () => {
  // if (dataScreenRef.value) {
  //   dataScreenRef.value.style.transform = `scale(${getScale()}) translate(-50%, -50%)`;
  // }
};

// 根据浏览器大小推断缩放比例
const getScale = (width = 1920, height = 1080) => {
  // let ww = window.innerWidth / width;
  // let wh = window.innerHeight / height;
  // return ww < wh ? ww : wh;
};
// 退出
const loginOut = () => {
  userStore.set_Token()
  const path = window.location.pathname;
  location.href = path+'#/login'
  // location.href = path
}
// 获取当前时间
let timer = null;
let date = new Date();
week.value = "星期" + "日一二三四五六".charAt(date.getDay());
timer = setInterval(() => {
  sf.value = dayjs().format("HH:mm");
  ymd.value = dayjs().format("YYYY年MM月DD日");
  week.value = "星期" + "日一二三四五六".charAt(date.getDay());
}, 1000);

onBeforeUnmount(() => {
  // window.removeEventListener("resize", resize);
  clearInterval(timer);
});
</script>
<style lang="scss" scoped>
@import "./index.scss";
</style>
