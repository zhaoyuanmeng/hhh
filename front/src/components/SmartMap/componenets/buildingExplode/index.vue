<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-13 10:13:10
 * @LastEditTime: 2024-06-20 19:49:47
 * @LastEditors: Alex
-->
<template>
  <!-- 右侧楼层号名称 -->
  <div class="floornumber" v-if="isfloornumberShow">
    <div class="floornumcontainer commnonscroll flex-col">
      <div v-if="explodebuildname === '雄安站'">
        <div class="keuybuilding-item" @click="selectStation(2)" :class="{active: selectNum == 2}">
          <span style="font-size:12px">二楼候车厅</span>
        </div>
        <div class="keuybuilding-item" @click="selectStation(1.5)" :class="{active: selectNum == 1.5}">
          <span style="font-size:12px">站台层</span>
        </div>
        <div class="keuybuilding-item" @click="selectStation(1)"  :class="{active: selectNum == 1}">
          <span style="font-size:12px">一楼候车厅</span>
        </div>
      </div>
      <div v-else-if="explodebuildname === '中国星网'">
        <div class="keuybuilding-item" v-for="(n,index) in customData" :key="index" :class="{ active: isActive ==n.num  }" @click="select($event, n.num)">
        <span :style="{fontSize:n.name==='地下室'?'14px':'18px'}">{{ n.name }}</span>
      </div>
      </div>
      <div v-else-if="explodebuildname === '计算中心'">
        <div class="keuybuilding-item" v-for="(n,index) in customData2" :key="index" :class="{ active: isActive ==n.num  }" @click="select($event, n.num)">
        <span>{{ n.name }}</span>
      </div>
      </div>
      <div v-else-if="explodebuildname === '雄安会展中心酒店'">
        <div class="keuybuilding-item" v-for="(n,index) in customData1" :key="index" :class="{ active: isActive ==n.num  }" @click="select($event, n.num)">
        <span>{{ n.name }}</span>
      </div>
      </div>
      <div v-else>
        <div class="keuybuilding-item" v-for="n in floorsTotalNum" :key="n" :class="{ active: isActive == floorsTotalNum - n + 1 }" @click="select($event, floorsTotalNum - n + 1)">
        <span
          v-if="
            explodebuildname === '国酒会议中心' ||
            explodebuildname === '公务接待楼'
          "
          >{{ floorsTotalNum - n === 0 ? "-1" : floorsTotalNum - n }}F</span
        >
        <span v-else-if="explodebuildname==='中国中化大厦'">21F</span>
        <span v-else>{{ floorsTotalNum - n + 1 }}F</span>
      </div>
      <div
        class="keuybuilding-item"
        :class="{ active: activeDown === -1 }"
        v-if="explodebuildname === '会展中心'"
        @click="underground(-1)"
      >
        <span>-1F</span>
      </div>
      <div
        class="keuybuilding-item"
        :class="{ active: activeDown === -2 }"
        v-if="explodebuildname === '会展中心'"
        @click="underground(-2)"
      >
        <span>-2F</span>
      </div>
      </div>
    </div>
    <div class="buttonresetcontainer" @click="floorReset(1)">
      <div class="resettext">复原</div>
    </div>
  </div>
</template>
<script setup>
import {
  ref,
  onMounted,
  computed,
  onUnmounted,
  onBeforeUnmount,
  watch,
  nextTick,
} from "vue";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";

import useFloorStore from "@/store/modules/floorStore";
import useScreenStore from "@/store/modules/screenStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import { getMarkersForFloor } from "@/api/task/task";
import { getFloorsBoomDataApi } from "@/api/basic/index";
import useTaskStore from "@/store/modules/taskStore";
import { floorModalInit } from "@/components/SmartMap/js/utils";
import {
  keyBuilding,
  drawBussinessUser,
  openFloorsModal,
} from "./markerEvent.js";
import { sessionStorage } from "@/utils/storage";
import {
  samples_floorMovePull,
  samples_floorMoveDown,
  getleftStatus,
  samples_floorMoveUp,
  samples_floorMovePullHide,
} from "./dismantle.js";
import useSettingStore from "@/store/modules/settingStore";
const floorStore = useFloorStore();
let selectNum = ref(-1)
let checkedPolice = computed(() => useWorkCockpitStore().policeCheckBox); // 警力
let checkedLine = computed(() => useWorkCockpitStore().linesCheckBox); // 路线
import emitter from "@/utils/emitter";
//当前建筑楼层数量
let floorsTotalNum = computed(() => floorStore.floorsTotalNum);
// 当期楼栋id
let floorsId = computed(() => floorStore.floorsId);
let isfloornumberShow = computed(() => floorStore.floornumberShow);
let floorMarkers = computed(() => floorStore.floorMarkers);
let businessFloorsMarker = computed(() => floorStore.businessFloorsMarker);
let sceneInfo = computed(() => useScreenStore().screenInfo);
let yuanInfo = computed(() => useEmergencyStore().YAInfo);
let editScreen = computed(() => floorStore.isShowFloor);
let isActive = computed(() => floorStore.isActive);
let cusTomActive = ref(0)
const customData = ref([{name:'5F',num:7},{name:'4F',num:6},{name:'1F',num:4},{name:'地下室',num:1}])
const customData2 = ref([{name:'2F',num:5},{name:'1F',num:4},{name:'-1F',num:3},{name:'-2F',num:2}])
const customData1 = ref([{name:'17F',num:6},{name:'16F',num:5},{name:'3F',num:3},{name:'2F',num:2},{name:'1F',num:1}])
// 会展中心图层包括地面、建筑、植物
const d_m = ref([
  "23DE18604BB525C068EA3DA11C76CFC5",
  "838E04FF4A85B02D2D643A86CE8078B0",
  "6C5F180E48757E29D7088BA1CDD5E011",
  "033778894069F4FF69887998DB5A2926",
  "6D6C753E4356603BB3B3B98D25C90F13",
  "2779CC584DF5D34970223E91E372EF90",
]);
const d_x_1 = ref(["127050144DBF70FA684EDAA9A244DF9B"]); // 会展中心地面一层图层
const d_x_2 = ref(["791A38E84EF10CEB14062F8837357CFC"]); // 会展中心地面二层图层
const d_m_3d = ref("033778894069F4FF69887998DB5A2926"); // 会展中心3dmax
const d_m_title = ref("6D6C753E4356603BB3B3B98D25C90F13"); // 商服倾斜
const house_id = ref("2779CC584DF5D34970223E91E372EF90"); // 会展中心房子id
//当前点击的楼栋名称
let explodebuildname = computed(() => floorStore.explodebuildname);
//点击的楼层号
let floornum = computed(() => floorStore.floornum);
let activeDown = ref(0);
onMounted(() => {
  activeDown.value = 0;
});
watch(
  () => isfloornumberShow,
  (val) => {
    if (val) {
      activeDown.value = 0;
    }
  },
  { immediate: true, deep: true }
);
onUnmounted(() => {
  activeDown.value = 0;
});
// 切换地下图层
const underground = async (num) => {
  let g = window.__g;
  samples_floorMoveDown(); // 楼层下降
  useFloorStore().setIsActive(-1); // 选中状态
  openFloorsModal("", false);
  g.infoTree.hide("033778894069F4FF69887998DB5A2926"); // 会展中心3dmax
  g.infoTree.show("6D6C753E4356603BB3B3B98D25C90F13"); // 商服倾斜
  g.infoTree.hide("2779CC584DF5D34970223E91E372EF90"); // 图层id
  activeDown.value = num;
  g.infoTree.hide(d_m.value); // 隐藏地面
  g.customObject.clear(); // 清除对象
  if (num === -1) {
    g.infoTree.show(d_x_1.value); // 显示一层
    g.infoTree.hide(d_x_2.value); // 隐藏2曾
    openFloorsModal("负一楼", true);
  }
  if (num === -2) {
    g.infoTree.show(d_x_2.value);
    g.infoTree.hide(d_x_1.value);
    openFloorsModal("负一楼", true);
  }
  // emitter.emit("refreshResource",'reset')
  hideFloorMarkers().then(() => {
    useFloorStore().setFloornum(num);
    setTimeout(() => {
      getFloorPointMarker(num);
      // if (num === -1) {
      //   let params = {
      //     buildingId: "127050144DBF70FA684EDAA9A244DF9B",
      //     floorNumber: -1,
      //   };
      //   getFloorsBoomDataApi(params).then((res) => {
      //     useFloorStore().set_businessFloorsMarker(res.data);
      //     sessionStorage.set("businessData", res.data);
      //     // 绘制商户信息
      //     drawBussinessUser(res.data);
      //   });
      // }
    }, 800);
  });
  let cameraData =[492067.439063, 4324154.02, 420.519766, -57.98032, -94.446503, 0.000006]
  g.camera.set(cameraData, 1);
};
//楼层号隐藏复位
const floorReset = (num) => {
  activeDown.value = 0;
  selectNum.value = -1
  floorStore.setFloornumberShow(false);
  floorStore.setOpenFloor(false); // 关闭楼层炸开
  floorStore.set_isOpenBol(false);
  samples_floorMoveDown();
  useFloorStore().setIsActive(-1);
  if (floorsId.value) {
    window.__g.marker.show(floorsId.value);
  }
  // emitter.emit("refreshResource",'reset')
  hideFloorData().then(() => {
    useFloorStore().set_floorMarkers([]);
    useFloorStore().set_businessFloorsMarker([]);
    sessionStorage.set("businessData", []);
    sessionStorage.set("floorsData", []);
  });
  floorModalInit(1); // 图层初始化
  openFloorsModal("", false);
};
//楼层隐藏上面
const samples_floorMovePullHideCustom = async(floor)=> {
//   二楼候车厅
// 顶层 6A93DD113057FDDA5A77089F74680FB6+340707 隐藏
// 二层候车厅 6A93DD113057FDDA5A77089F74680FB6+408900
// 站台和二层候车厅楼梯  6A93DD113057FDDA5A77089F74680FB6+407245
// 站台 6A93DD113057FDDA5A77089F74680FB6+524946
// 夹层C46D85955C3243143A1E06FAF6EC18AB+531132 
// 站台和一层楼梯 6A93DD113057FDDA5A77089F74680FB6+516870 
// 周边 6A93DD113057FDDA5A77089F74680FB6+343907 隐藏
// 站台层
// 顶层 6A93DD113057FDDA5A77089F74680FB6+340707 隐藏
// 二层候车厅 6A93DD113057FDDA5A77089F74680FB6+408900 隐藏
// 站台和二层候车厅楼梯  6A93DD113057FDDA5A77089F74680FB6+407245
// 站台 6A93DD113057FDDA5A77089F74680FB6+524946
// 夹层C46D85955C3243143A1E06FAF6EC18AB+531132 
// 站台和一层楼梯 6A93DD113057FDDA5A77089F74680FB6+516870
// 周边 6A93DD113057FDDA5A77089F74680FB6+343907 隐藏
// 一楼候车厅
// 顶层 6A93DD113057FDDA5A77089F74680FB6+340707 隐藏
// 二层候车厅 6A93DD113057FDDA5A77089F74680FB6+408900 隐藏
// 站台和二层候车厅楼梯  6A93DD113057FDDA5A77089F74680FB6+407245 隐藏
// 站台 6A93DD113057FDDA5A77089F74680FB6+524946 隐藏
// 夹层C46D85955C3243143A1E06FAF6EC18AB+531132 隐藏
// 站台和一层楼梯 6A93DD113057FDDA5A77089F74680FB6+516870
// 周边 6A93DD113057FDDA5A77089F74680FB6+343907 隐藏

// 顶 983FDB03E27D07FC2A256F937BA8DB74+6
// 高架层 983FDB03E27D07FC2A256F937BA8DB74+15
// 站台+贵宾室 983FDB03E27D07FC2A256F937BA8DB74+16  和37223707454D3AFD49E406BBCB5449E5和8407B61443EE821814D46BB3D8DC2696
// 高架层楼梯 983FDB03E27D07FC2A256F937BA8DB74+10
// 夹层 983FDB03E27D07FC2A256F937BA8DB74+2
// 站台层楼梯 983FDB03E27D07FC2A256F937BA8DB74+9

    let g = window.__g
    if(floor===1){
      // 一楼候车厅
      let showIds = ['6A93DD113057FDDA5A77089F74680FB6+516870']
      let hideIds = ['983FDB03E27D07FC2A256F937BA8DB74+6','983FDB03E27D07FC2A256F937BA8DB74+15','983FDB03E27D07FC2A256F937BA8DB74+16',
      '983FDB03E27D07FC2A256F937BA8DB74+10','983FDB03E27D07FC2A256F937BA8DB74+2','983FDB03E27D07FC2A256F937BA8DB74+9']
      // g.customObject.show(showIds)
      g.customObject.hide(hideIds) 
      g.infoTree.hide(['37223707454D3AFD49E406BBCB5449E5','8407B61443EE821814D46BB3D8DC2696']) 
    }
    if(floor===1.5){
      // 站台层
      let showIds = ['983FDB03E27D07FC2A256F937BA8DB74+16',
      '983FDB03E27D07FC2A256F937BA8DB74+10','983FDB03E27D07FC2A256F937BA8DB74+2','983FDB03E27D07FC2A256F937BA8DB74+9']
      let hideIds = ['983FDB03E27D07FC2A256F937BA8DB74+6','983FDB03E27D07FC2A256F937BA8DB74+15']
      g.customObject.show(showIds)
      g.customObject.hide(hideIds)
      g.infoTree.show(['37223707454D3AFD49E406BBCB5449E5','8407B61443EE821814D46BB3D8DC2696'])  
    }
    if(floor===2){
      // 二楼候车厅
      let showIds = ['983FDB03E27D07FC2A256F937BA8DB74+15','983FDB03E27D07FC2A256F937BA8DB74+16',
      '983FDB03E27D07FC2A256F937BA8DB74+10','983FDB03E27D07FC2A256F937BA8DB74+2','983FDB03E27D07FC2A256F937BA8DB74+9']
      let hideIds = ['983FDB03E27D07FC2A256F937BA8DB74+6']
      g.customObject.show(showIds)
      g.customObject.hide(hideIds)
      g.infoTree.hide(['37223707454D3AFD49E406BBCB5449E5','8407B61443EE821814D46BB3D8DC2696'])   
    }
}

const selectStation = (n) => {
  // openFloorsModal(explodebuildname.value, true); // 打开对应的遮罩
  useTaskStore().set_attractBol(false);
  // 隐藏楼栋marker标签
  if (floorsId.value) {
    window.__g.marker.hide(floorsId.value);
  }
  floorStore.setOpenFloor(true); // 开启楼层炸开
  // 加载对应模型
    // window.__g.infoTree.show("033778894069F4FF69887998DB5A2926");
    // window.__g.infoTree.hide("6D6C753E4356603BB3B3B98D25C90F13");
  // 判断是否有点击地下楼层
  if (!getleftStatus()) {
    console.log("楼层切换点击太快了 请稍等。。");
    return;
  }
  hideFloorMarkers().then(() => {
    samples_floorMovePullHideCustom(n).then(() => {
      if (n != isActive.value) {
        selectNum.value = n
        useFloorStore().setFloornum(n);
        setTimeout(() => {
          getBusinessFloorsUserData(n);
        }, 100);
      }
    });
  });
}
// 点击选择楼层
const select = (event, n) => {
  openFloorsModal(explodebuildname.value, true); // 打开对应的遮罩
  useTaskStore().set_attractBol(false);
  // 隐藏楼栋marker标签
  if (floorsId.value) {
    window.__g.marker.hide(floorsId.value);
  }
  floorStore.setOpenFloor(true); // 开启楼层炸开
  if(explodebuildname.value==='计算中心'){
    if(n===2||n===3){
      window.__g.infoTree.hide('5DF2246A408080EBC44AD1925AC03F28')
      window.__g.infoTree.hide('5B27CE9040FEEAA466EFC6BC395A2364')
    }else{
      if(n===5){
        window.__g.infoTree.show('5B27CE9040FEEAA466EFC6BC395A2364')
      }else{
        window.__g.infoTree.hide('5B27CE9040FEEAA466EFC6BC395A2364')
      }
      window.__g.infoTree.show('5DF2246A408080EBC44AD1925AC03F28')
    }
  }
  if (
    explodebuildname.value === "会展中心" ||
    explodebuildname.value === "办公业态3#" ||
    explodebuildname.value === "办公业态4#" ||
    explodebuildname.value === "办公业态5#" ||
    explodebuildname.value === "综合商业6#"||
    explodebuildname.value === "雄安会展中心酒店"
  ) {
    window.__g.infoTree.show("033778894069F4FF69887998DB5A2926");
    window.__g.infoTree.hide("6D6C753E4356603BB3B3B98D25C90F13");
  }
  // 判断是否有点击地下楼层
  if (activeDown.value !== 0) {
    openFloorsModal("", false);
    keyBuilding("会展中心", n); //
    hideFloorMarkers();
    openFloorsModal("会展中心", true);
    activeDown.value = 0;
    floorStore.set_isOpenBol(true);
    if (n != isActive.value) {
      useFloorStore().setIsActive(n);
      useFloorStore().setFloornum(n);
      setTimeout(() => {
        getFloorPointMarker(n);
      }, 2000);
    } else {
      useFloorStore().setIsActive(-1);
      hideFloorMarkers();
    }
    return;
  }
  if (!getleftStatus()) {
    console.log("楼层切换点击太快了 请稍等。。");
    return;
  }
  hideFloorMarkers().then(() => {
    samples_floorMovePullHide(n).then(() => {
      if (n != isActive.value) {
        useFloorStore().setIsActive(n);
        useFloorStore().setFloornum(n);
        setTimeout(() => {
          getBusinessFloorsUserData(n);
        }, 100);
      } else {
        useFloorStore().setIsActive(-1);
        hideFloorMarkers();
      }
    });
  });
};
//隐藏所有炸开数据
const hideFloorMarkers = async () => {
  let g = window.__g
  if(businessFloorsMarker.value&&businessFloorsMarker.value?.length){
    let codes = businessFloorsMarker.value.map(item=>item.roomCode)
     g.marker.delete(codes);
     g.marker3d.delete(codes)
  }
  // 隐藏所有室内数据
  // 更新资源
  emitter.emit("refreshResource", "hide");

};
const hideFloorData = async () => {
  let g = window.__g
  if(businessFloorsMarker.value&&businessFloorsMarker.value?.length){
    let codes = businessFloorsMarker.value.map(item=>item.roomCode)
     g.marker.delete(codes);
     g.marker3d.delete(codes)
  }
  emitter.emit("refreshResource", "reset");

};

// 楼层打开后请求楼层里面的marker
const getFloorPointMarker = async (n) => {
  // 显示当前室内数据并勾选上
  // 更新资源
  emitter.emit("refreshResource", "show");
};
// 请求商业楼层里面租户信心
const getBusinessFloorsUserData = async (n) => {
  if (
    explodebuildname.value === "办公业态3#" ||
    explodebuildname.value === "办公业态4#" ||
    explodebuildname.value === "办公业态5#" ||
    explodebuildname.value === "综合商业6#"
  ) {
    getFloorPointMarker(n);
  } else {
    getFloorPointMarker(n);
  }
};
onBeforeUnmount(() => {
  floorStore.set_isOpenBol(false);
});
</script>

<style lang="scss" scoped>
.floornumber {
  position: absolute;
  right: 340px;
  z-index: 30;
  bottom: 10%;
  // transform: translateY(-50%);
  pointer-events: auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: rgba(4, 35, 84, 0.83);
}

.floornumcontainer {
  //height: var(--cssStyle);
  max-height: 480px;
  overflow-y: auto;
  padding-top: 10px;
}

.keuybuilding-item {
  width: 60px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  font-size: 18px;
  color: #ffffff;
  cursor: pointer;

  &:hover {
    // background: url(~@/assets/img/btn/lc-hover.png) no-repeat;
    // background-size: 100% 100%;
    color: aqua;
  }

  &.active {
    color: aqua;
  }
}

.buttonresetcontainer {
  display: flex;
  flex-direction: column;
  text-align: center;
  margin-bottom: 10px;
}

.buttonreset {
  width: 30px;
  height: 30px;
  background: transparent;
  border: 0;
  // background-image: url('@/assets/img/buiding/floor_btn_reset.png');
  background-repeat: no-repeat;
  background-size: 100% 100%;
  margin: auto;
}

.resettext {
  cursor: pointer;
  color: white;
  font-size: 14px;
  height: 30px;
  line-height: 30px;
  font-family: SourceHanSansCN-Bold;
}

.resettext:hover {
  color: aqua;
}

.flex-col {
  display: flex;
  flex-direction: column;
}

.commnonscroll::-webkit-scrollbar {
  /*滚动条整体样式*/
  width: 0;
  /*高宽分别对应横竖滚动条的尺寸*/
  height: 1px;
}

.commnonscroll::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 1px;
  background: rgba(20, 68, 88, 0);
}

.commnonscroll::-webkit-scrollbar-thumb:hover {
  /*滚动条里面小方块*/
  border-radius: 1px;
  background: rgba(28, 89, 121, 0);
}

.commnonscroll::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  border-radius: 1px;
  background: rgba(5, 33, 44, 0);
}
</style>
