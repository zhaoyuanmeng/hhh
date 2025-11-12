<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-13 10:13:10
 * @LastEditTime: 2024-06-20 19:49:47
 * @LastEditors: Alex
-->
<template>
  <!-- 右侧楼层号名称 -->
  <div class="floornumber">
    <div class="floornumcontainer commnonscroll flex-col">
      <div
        class="keuybuilding-item"
        v-for="n in floorsTotalNum"
        :key="n"
        :class="{ active: isActive == floorsTotalNum - n + 1 }"
        @click="select($event, floorsTotalNum - n + 1)"
      >
        <span >{{ floorsTotalNum - n + 1 }}F</span>
      </div>
    </div>
    <div class="buttonresetcontainer" @click="floorReset(1)">
      <div class="resettext">复原</div>
    </div>
  </div>
   <!-- 招商详情/产业招商详情 -->
   <HouseInfo v-if="showDetails" :dialogData="infoData" @closeD="closeBox"/>
</template>
<script setup>
import { ref, onMounted, computed, onUnmounted, onBeforeUnmount,watch,nextTick } from "vue";
import useFloorStore from "@/store/modules/floorStore";
import HouseInfo from '@/views/componenets/taskNew/houseInfo.vue'
import { getFloorsUserInfoApi } from "@/api/basic/index";
import {closeFloors} from "@/components/SmartMap/js/utils";
import { ElMessage } from "element-plus";
const floorStore = useFloorStore();
//当前建筑楼层数量
let floorsTotalNum = computed(() => floorStore.noFloorNum);
let isActive = ref(-1);
let showDetails = ref(false)
let infoData = ref([])
// 会展中心图层包括地面、建筑、植物
//当前点击的楼栋id
let floorsId = computed(() => floorStore.noFloorsId);
onMounted(() => {
});
//楼层号隐藏复位
const floorReset = () => {
  floorStore.set_nofloorNumber(false)
  showDetails.value = false
  floorStore.set_noFloorsId('')
};
const closeBox = () => {
  showDetails.value = false
  isActive.value = -1
}
// 点击选择楼层
const select = (event, n) => {
  if(n!==isActive.value){
    isActive.value = n
    let params = {buildingId:floorsId.value,floorNumber:n}
    getFloorsUserInfoApi(params).then(res=>{
      if(res.data&&res.data.length>0){
        nextTick(()=>{
        showDetails.value = true
      })
      infoData.value = res.data
      }else{
        ElMessage.warning('暂无信息')
        showDetails.value = false
      }
     
    })
  }else{
    isActive.value = -1
    showDetails.value = false
  }
};

onBeforeUnmount(() => {
});
</script>

<style lang="scss" scoped>
.floornumber {
  position: absolute;
  right: 340px;
  z-index: 30;
  bottom: 7%;
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
