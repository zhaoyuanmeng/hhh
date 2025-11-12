<template>
  <Police v-if="showPolicDialog" @closeRightBox="closeRightBoxFun"></Police>
  <PoliceCar v-if="showCarDialog" @closeRightBox="closeRightBoxFun" />
  <basicEdit v-if="showBasicDialog" @closeRightBox="closeRightBoxFun" />
  <uavEdit v-if="showUavDialog" @closeRightBox="closeRightBoxFun" />
</template>

<script setup>
import { computed, onMounted ,nextTick,watch,ref} from 'vue'
// 驾驶舱页面组件
import Police from "@/views/SysTools/resourceCatalog/PoliceAndCar/police.vue"; // 警力编辑
import PoliceCar from "@/views/SysTools/resourceCatalog/PoliceAndCar/car.vue"; // 警车编辑
import uavEdit from "@/views/SysTools/resourceCatalog/PoliceAndCar/uav.vue"; // 无人机编辑
import basicEdit from "@/views/SysTools/resourceCatalog/plot/basic/edit.vue"; // 基础工作编辑
import SealAirCityAPI from "@/views/SysTools/resourceCatalog/SealAirCityAPI.js";
// store
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const WorkCockpitStore = useWorkCockpitStore()
const SysToolsCimStore = useSysToolsCimStore();
const mapEvent = computed(() => SysToolsCimStore.eventSealAPI);
const showPolicDialog = computed(() =>WorkCockpitStore.showPolicDialog)
const showCarDialog = computed(() =>WorkCockpitStore.showCarDialog)
const showBasicDialog = computed(() =>WorkCockpitStore.showBasicDialog)
const showUavDialog = computed(() =>WorkCockpitStore.showUavDialog)
// 漫游页面退回
const closeRightBoxFun = (val) => {
  WorkCockpitStore.set_showPolicDialog('all',false)
};
watch(
  () => mapEvent.value,
  (nV, oV) => {
    // 任务和驾驶舱三级页面显示
    if (nV) {
      if (
        (nV.GroupID === "police" ||
          nV.GroupID === "car" ||
          nV.GroupID === "basic" || nV.GroupID === 'uav') &&
        nV.eventtype === "LeftMouseButtonClick" &&
        nV.Type === "marker"
      ) {
        if (nV.GroupID === "police"  ) {
          SysToolsCimStore.SET_MARKEREVENT(nV);
        }
        if (nV.GroupID === "car"  ) {
          SysToolsCimStore.SET_MARKEREVENT(nV);
        }
        if (nV.GroupID === "basic" ) {
          SysToolsCimStore.SET_MARKEREVENT(nV);
        }
        if (nV.GroupID === 'uav' ) {
          SysToolsCimStore.SET_MARKEREVENT(nV);
        }
      }
    }
  },
  { deep: true, immediate: false }
);
onMounted(()=>{
  window.sealAPI = new SealAirCityAPI();
  window.sealAPI.onReady();
})


</script>

<style lang="scss" scoped>

</style>