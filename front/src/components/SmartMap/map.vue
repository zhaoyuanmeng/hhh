<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-23 01:26:30
 * @LastEditors: Alex
-->
<template>
  <div class="mapMain">
    <div id="player"></div>
    <buildingExplode v-if="showPoint" ref="floors" />
    <NoFloorModel v-if="showFloor" />
    <SysTools v-if="showTool" />
    <!-- 社区详情 -->
    <CommunityInfo v-if="communityBol" />
    <!-- 招商详情/产业招商详情 -->
    <AttractPage  v-if="attractBol"/>
  </div>
</template>

<script setup name="smartMap">
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  watch,
  defineProps
} from "vue";
import { ElMessage } from "element-plus";
import CommunityInfo from "../../views/componenets/taskNew/communityInfo.vue";
import AttractPage from '../../views/componenets/taskNew/attractInfo.vue'
import { handleTree } from "@/utils";
import SysTools from "../../views/SysTools/index.vue"; // 地图工具栏
import useSettingStore from "@/store/modules/settingStore";
import buildingExplode from "./componenets/buildingExplode";
import NoFloorModel from "./componenets/noFloorModel.vue";
import usePlanTaskStore from "@/store/modules/planTask";
import OnReady from "./js/onReady";
import OnEvent from "./js/Event";
import useTaskStore from '@/store/modules/taskStore';
import useUserStore from "@/store/modules/user";
import useCameraStore from "@/store/modules/cameraSet";
import useBasicStore from "@/store/modules/basicData";
import useFloorStore from '@/store/modules/floorStore'
import { initTools } from './js/addPlot'
import { closeFloors, hideGSGT, floorModalInit, getCoodr, drawGsGtData } from './js/utils'
// import config from './public/static/config'
import useWorkCockpitStore from '@/store/modules/workCockpit'
const settingStore = useSettingStore();
const userStore = useUserStore();
const showPoint = ref(false);
const showFloor = computed(() => useFloorStore().nofloorNumber);
const communityBol = computed(() => useTaskStore().communityBol)
const attractBol = computed(()=>useTaskStore().attractBol)
const floors = ref(null)
import { init } from "./js/map";
let showTool = computed(() => settingStore.showTool);
const clearModal = computed(() => useTaskStore().clearModal)
let routerName = computed(() => userStore.routerIndex);
watch(
  () => routerName,
  (val) => {
    if (val) {
      if (val.value) {
        layerInitData(val.value).then(async()=>{
        useTaskStore().SET_clearModal(true)
        usePlanTaskStore().set_checkInfo({});
        usePlanTaskStore().set_checkDetails(false);
        let g = window.__g;
        g.marker.deleteByGroupId("gsgl");
        g.marker.deleteByGroupId("gstl");
        g.marker.deleteByGroupId("xcda");
        g.marker.deleteByGroupId("resource");
        //初始化、不传参等同于1;1：清除所有接口添加的对象 2：重置用户设置 4：复位相机到初始位置
        // g.reset(4);
        g.tag.clear();
        g.marker.clear()
        initTools()
        closeFloors() // 关闭楼层炸开
        useWorkCockpitStore().set_tabsOne('rcap')
        useWorkCockpitStore().set_tabsTwo('')
       
        })
      }
    }
  },
  { deep: true }
);
// let showChildTool = computed(() => settingStore.showToolOne);
//let layerData = computed(()=>settingStore.layerList)
const props = defineProps({
  loadAfter: {
    type: Function,
    default: () => { },
  },
});

const layerInitData = async(valval)=>{
  let g = window.__g
  g.cameraTour.stop();// 停止漫游
  // let res =  await g.infoTree.get()
  // let treeLayer = handleTree(res.infotree, 'index', 'parentIndex')[0].children
  // let initData = treeLayer.filter((item) => item.visiblity)
  // console.log(initData)
  // let dxData = treeLayer.filter((item) => item.name === '地下空间')
  // let dsData = initData
  // let hideData = dxData
  //   for (const item of dsData) {
  //      g.infoTree.show(item.iD)
  //   }
  //   for (const item1 of hideData) {
  //      g.infoTree.hide(item1.iD)
  //   }
     g.reset(1 | 2 | 4);
     useWorkCockpitStore().setShowSceneDialog(false);
    //  g.infoTree.hide(window.shepfileCommunity) //隐藏所有的shepfile
    g.infoTree.hide(window.shapeFile) // 隐藏shepfile点位
    if (valval === 'Task') {
          useCameraStore().setShowTaskInfo(false);
          useWorkCockpitStore().setCurPage('overview')
          usePlanTaskStore().setCurPage(false)
          g.infoTree.hide('8C6F67F84F71F11130C1BEACD0BAF612')
          g.infoTree.hide('A3B755BF43736C15455300A0FB44761F')
          g.infoTree.hide('8B6725594EA91CCA8431338C12C4399F')
          g.polygon3d.clear()
          g.marker.clear()
          g.polygon.clear();// 清除面
          g.infoTree.hide('AEFF1352474AC1F3BD7E4581724F6C5C')// 隐藏矢量高速高铁
          drawGsGtData(0)
        } else {
          usePlanTaskStore().setCurPage(false)
          init()
        }
}
const initPlayer = () => {
  let host = HostConfig.Manager; //视频流
  let options = {
    iid: HostConfig.iid,
    //必选参数，网页显示视频流的dom节点id
    domId: "player",
    //必选参数，二次开发时必须指定，否则无法进行二次开发
    apiOptions: {
      //必选回调函数，视频流加载完成回调函数
      onReady: OnReady,
      //可选回调函数，视频流事件交互回调函数
      onEvent: OnEvent,
    },
    ui: {
      startupInfo: true, //默认值为true，初始化过程中是否显示左上角详细信息（如果不需要，直接去掉此属性即可）
      statusIndicator: true, //默认值为true，左上角闪烁的状态指示灯，可以从不同的颜色看出当前的状态
      statusButton: false, //默认值为false，是否在左下角显示信息按钮（如果不需要，直接去掉此属性即可）
      fullscreenButton: false, //默认值为false，是否在右下角显示全屏按钮（如果不需要，直接去掉此属性即可）
      homeButton: true, //默认值为false，是否在左下角显示“回到初始位置”按钮（如果不需要，直接去掉此属性即可）
      taskListBar: 1, //默认值为1，是否在下方显示“任务队列（API调用队列）”信息（0: 永不显示；1: 执行比较耗时的操作时显示；2: 一直显示）
      debugEventsPanel: false, //是否显示事件详细信息（仅用于调试）
      mainUI: false, //是否显示Cloud的UI工具栏，如果设置为true就显示，如果设置为false就隐藏，如果没有设置，就保持原状。
      campass: false, //是否显示指北针，如果设置为true就显示，如果设置为false就隐藏，如果没有设置，就保持原状。
    },
    keyEventTarget: "video",
    useBuiltinCursors: true, //默认值为true，是否使用内置光标。
    //可选参数 自定义事件交互
    events: {
      //鼠标、键盘交互事件的回调，后面可以随时通过setActionEventEnabled进行开关
      mouseKeyListener: {
        onKeyDown: (e) => console.info(`KeyDown: ${e.code}`),
      },
      //当视频流加载成功后触发
      onVideoLoaded: () => loadAfterFunction(),

      //服务连接断开的回调函数
      onConnClose: (e) => console.info(`Connection closed: ${e.code}`),
    },
  };
  window.__g = new DigitalTwinPlayer(host, options).getAPI();
};
// 定义一个函数来通过id查找节点
function findNodeById(tree, id) {
  // 遍历树形结构的每个节点
  for (let i = 0; i < tree.length; i++) {
    let node = tree[i];
    // 如果找到了匹配的id，返回这个节点
    if (node.iD === id) {
      return node;
    }
    // 如果节点有子节点，递归查找子节点
    if (node.children) {
      let result = findNodeById(node.children, id);
      // 如果在子节点中找到了匹配的id，返回这个结果
      if (result) {
        return result;
      }
    }
  }
  // 如果没有找到，返回null
  return null;
}
const loadAfterFunction = async () => {
  // settingStore.setShowTool(true)
  let g = window.__g;
  let res = await g.infoTree.get();
  g.infoTree.hide(window.shapeFile) // 隐藏shepfile点位
  g.shapeFileLayer.hideAll() // 隐藏所有的shepfile
  // g.infoTree.hide(window.shepfileCommunity) //隐藏所有的shepfile
  let treeLayer = handleTree(res.infotree, 'index', 'parentIndex')[0].children
  console.log(treeLayer)
  let dxData = treeLayer.filter((item) => item.name === '地下空间') // 隐藏地下图层
  for (const item1 of dxData) {
    g.infoTree.hide(item1.iD)
  }
  // 加入
  // init();
  let layer = res.infotree.filter((item) => item.visiblity); // 加载场景后请求说有的图层数
  useBasicStore().setLayerData(layer);
  settingStore.setLayerTree(layer);
  // settingStore.setGSlayerId(gsData);
  // settingStore.setAllLayer(hidePoint);
  props.loadAfter();
  showPoint.value = true;
  floorModalInit(1)
  getCoodr()
  // g.infoTree.hide('033778894069F4FF69887998DB5A2926')// 商服中心
  // g.infoTree.show('6D6C753E4356603BB3B3B98D25C90F13') // 商服
  // g.infoTree.show('5F2D2A104B9735917B9E0F90CE67244E') // 会展marker中心
  if (routerName.value === 'Task') {
    setTimeout(() => {
      window.__g.infoTree.hide('8C6F67F84F71F11130C1BEACD0BAF612')
      window.__g.infoTree.hide('A3B755BF43736C15455300A0FB44761F')
      window.__g.infoTree.hide('8B6725594EA91CCA8431338C12C4399F')
      g.polygon3d.clear()
      g.polygon.clear();// 清除面
    }, 500)
  } else {
    drawGsGtData(1)
    // 加入
    init();
  }
};
onMounted(() => {
  // 初始化视频流场景
  initPlayer();
});
onBeforeUnmount(() => {
  window.__g.destroy();
});
onUnmounted(() => {
  window.__g.destroy();
});
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
