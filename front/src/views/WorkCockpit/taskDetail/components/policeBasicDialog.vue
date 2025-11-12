<template>
  <div class="content-box">
    <div class="content-header">
      <span v-if="eventMapData.GroupID === 'basic'">要素信息</span>
      <span v-else-if="eventMapData.GroupID === 'fbaj'">防爆安检信息</span>
      <span v-else>警力信息</span>
      <el-icon class="close" @click="handleClose">
        <Close />
      </el-icon>
    </div>
    <div class="content" v-if="eventMapData.GroupID === 'basic'">
      <p>名称：{{ basicObj.name }}</p>
      <p>位置：{{ `在${basicObj.refLineName}约${basicObj.position}公里处` }}</p>
      <p>备注：{{ basicObj.bz || "无" }}</p>
    </div>
    <div class="content" v-else-if="eventMapData.GroupID === 'fbaj'">
      <p>名称：{{ fbajObj.name }}</p>
      <div v-if="fbajObj.type==='安检员'">
        <p>位置：{{ fbajObj.wz }}</p>
      <p>数量：{{ fbajObj.num }}</p>
      <p>联系方式：{{ fbajObj.phone }}</p>
      </div>
      <p v-else>备注：{{ fbajObj.bz || "无" }}</p>
    </div>
    <div class="content" v-else>
      <p v-if="eventMapData.GroupID === 'uav'">
        名称：{{ policeObj.uav || "" }}
      </p>
      <p v-if="eventMapData.GroupID === 'uav'">
        备注：{{ policeObj.bz || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'police'">
        警力名称：{{ policeObj.name || "" }}
      </p>
      <p v-if="eventMapData.GroupID === 'police'">
        警力位置：{{ policeObj.wz || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'police'">
        部署数量：{{ policeObj.num || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'police'">
        分组名称：{{ policeObj.group || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'police'">
        部署防线：{{ policeObj.fx || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'police'">
        警力编号：{{ policeObj.jlbh || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'police'">
        联系方式：{{ policeObj.lxfs || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'car'">
        警车名称：{{ policeObj.name || "" }}
      </p>
      <p v-if="eventMapData.GroupID === 'car'">
        警车牌号：{{ policeObj.jcbh || "无" }}
      </p>
      <!-- <p v-if="eventMapData.GroupID==='car'">
          巡航路线：{{ policeObj.data.info.num?policeObj.data.info.num:'无' }}
      </p> -->
      <p v-if="eventMapData.GroupID === 'car'">
        警员名称：{{ policeObj.jymc || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'car'">
        警员编号：{{ policeObj.jybh || "无" }}
      </p>
      <p v-if="eventMapData.GroupID === 'car'">
        联系方式：{{ policeObj.lxfs || "无" }}
      </p>
    </div>
  </div>
</template>

<script setup>
import { ElMessage } from "element-plus";
import { Close } from "@element-plus/icons-vue";
import { ref, onMounted, computed } from "vue";
// 接口
import { getById } from "@/api/task/task";
// store
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
const WorkCockpitStore = useWorkCockpitStore();
const eventMapData = computed(() => WorkCockpitStore.eventMapData);
const taskId = computed(()=>WorkCockpitStore.basicTaskId)
const sceneId = computed(()=>WorkCockpitStore.basicSceneId)
const handleClose = () => {
  WorkCockpitStore.set_showPoliceBasic(false);
};
const basicObj = ref({
  name: "",
  bz: "",
  position: "",
  lineName: "",
});
const fbajObj = ref({
  type:'',
  name: "",
  bz: "",
  num: "",
  phone: "",
  wz:'',
});
const policeObj = ref({
  name: "",
  wz: "",
  num: "",
  fx: "",
  group:"",
  jlbh: "",
  jcbh: "",
  lxfs: "",
  uav: "",
  bz: "",
  jymc: "",
});
// 获取点位信息
const getInfo = () => {
  //,sceneId:sceneId.value,taskId:taskId.value
  getById({ id: eventMapData.value.Id,sceneId:sceneId.value,taskId:taskId.value }).then((res) => {
    if (eventMapData.value.GroupID === "basic") {
      basicObj.value = res.data;
      if (res.data.position) {
        basicObj.value.position = Number(res.data.position).toFixed(2);
      }
      basicObj.bz = res.data.data.info.bz || "无";
    } else if(eventMapData.value.GroupID === "fbaj") {
      let dataObj = res.data;
      fbajObj.value.type = dataObj.data.marker.userData
      fbajObj.value.name = dataObj.name;
      fbajObj.value.wz = dataObj.data.info.wz || "";
      fbajObj.value.num = dataObj.data.info.num || "";
      fbajObj.value.phone = dataObj.data.info.phone || "";
      fbajObj.value.bz = dataObj.data.info.bz || "";
    }else{
      let dataObj = res.data;
      policeObj.value.name = dataObj.name;
      policeObj.value.wz = dataObj.data.info.weizhi || "";
      policeObj.value.num = dataObj.data.info.num || "";
      policeObj.value.fx = dataObj.data.info.fangxian || "";
      policeObj.value.group = dataObj.data.info.group || "";
      policeObj.value.jlbh = dataObj.data.info.jlbh || "";
      policeObj.value.jlbh = dataObj.data.info.jlbh || "";
      policeObj.value.jcbh = dataObj.data.info.jcbh || "";
      policeObj.value.lxfs = dataObj.data.info.lxfs || "";
      policeObj.value.bz = dataObj.data.info.bz || "";
      policeObj.value.uav = dataObj.data.marker.text || "";
      policeObj.value.jymc = dataObj.data.info.jymc || "";
      policeObj.value.jybh = dataObj.data.info.jybh || "";
    }
  });
};

onMounted(() => {
  getInfo();
});
</script>

<style lang="scss" scoped>
.content-box {
  width: 392px;
  height: 520px;
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  position: absolute;
  right: 600px;
  bottom: 100px;
  z-index: 51;
  transform: scale(0.8);
  transform-origin: right center;

  .content-header {
    width: 100%;
    height: 48px;
    line-height: 48px;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    background: url("@/assets/workcockpit/header_bg.png") no-repeat;
    position: relative;

    .close {
      position: absolute;
      right: 12px;
      top: 8px;
      cursor: pointer;
    }
  }

  .content {
    height: calc(520px - 48px);
    padding: 25px 45px;
    box-sizing: border-box;
    font-size: 14px;
    line-height: 32px;
    overflow: auto;
  }
}
</style>
