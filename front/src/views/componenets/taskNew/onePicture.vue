<!--
 * @Description: 任务-场景一张图
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 11:20:59
 * @LastEditors: Alex
-->
<template>
  <div class="task_scnce_prc">
    <!-- 操作按钮 -->
    <div class="out_download">
      <div class="action_btn" @click="out">退出</div>
      <div class="action_btn" @click="download">下载</div>
    </div>
    <ResourceList/>
  </div>
  <PoliceBasicDialog v-if="showPolicDialog"/>

    <!-- marker信息 -->
    <InfoDialog v-if="showMarkerInfo" />
</template>

<script setup>
import {
  onBeforeUnmount,
  onMounted,
  ref,
  computed,
  onBeforeMount,
  getCurrentInstance,
  onUnmounted,
  watch,
} from "vue";
import { drawScreenData, clearDraw } from "./util";
import { ElMessage, ElMessageBox } from "element-plus";
import { sessionStorage } from "@/utils/storage";
import useScreenStore from "@/store/modules/screenStore";
import {getRelatedFeatureDataOfTask} from '@/api/workCockpit/index.js'
import useTaskStore from "@/store/modules/taskStore";
import useSettingStore from "@/store/modules/settingStore";
import {drawTaskScenePointAndLine} from '../../WorkCockpit/utils'
import { getDrawDataOfTask} from '@/api/workCockpit/index.js'
import ResourceList from "@/views/WorkCockpit/taskDetail/components/ResourceList.vue"
import PoliceBasicDialog from '@/views/WorkCockpit/taskDetail/components/policeBasicDialog.vue'
import InfoDialog from '@/views/WorkCockpit/taskDetail/components/InfoDialog.vue'
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
const checkList = ref([]);
let queryName = computed(() => useScreenStore().pictureName);
const showPolicDialog = computed(() =>useWorkCockpitStore().showPoliceBasic)
const showMarkerInfo = computed(() => useWorkCockpitStore().showMarkerInfo)
const basicHtml = ref('')
const yjPwo = ref('0')
const orgForm = ref({
  zzh:{name:'',job:'',phone:''},
  fzzh:{name:'',job:'',phone:''},
  member:[]
})
const sceneDataList = ref([])
const bxdlines = ref([])
onMounted(() => {});
// 退出
const out = () => {
  clearDraw();
  useTaskStore().SET_clearModal(true);
  useScreenStore().set_showPicture(false);
  useSettingStore().setShowTool(false);
};
//获取当前图片
const getCurCameraImage = () => {
  var video = document.getElementById("player").querySelector("video");
  var canvas = document.createElement("canvas");
  var scale = video.videoWidth / video.videoHeight;
  canvas.width = 1000;
  canvas.height = parseInt(1000 / scale);
  canvas.getContext("2d").drawImage(video, 0, 0, canvas.width, canvas.height);
  const base64 = canvas.toDataURL("image/png", 1);
  return base64;
};
// 下载
const download = () => {
  const base64Image = getCurCameraImage(); // 你的 base64 图片字符串
  const byteString = atob(base64Image.split(",")[1]);
  const mimeString = base64Image.split(",")[0].split(":")[1].split(";")[0];
  const ab = new ArrayBuffer(byteString.length);
  const ia = new Uint8Array(ab);

  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }

  const blob = new Blob([ab], { type: mimeString });
  const url = URL.createObjectURL(blob);

  // 创建一个 a 标签用于下载
  const link = document.createElement("a");
  link.href = url;
  link.download = `${queryName.value}.png`; // 指定下载的文件名
  document.body.appendChild(link);
  link.click();

  // 清理和释放 URL 对象
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
};
//删除计时器
onBeforeUnmount(() => {});
onUnmounted(() => {});
</script>

<style lang="scss" scoped>
.task_scnce_prc {
  .out_download {
    position: absolute;
    right: 20px;
    top: 80px;
    display: flex;
    flex-direction: column;
    z-index: 1;
    .action_btn {
      width: 92px;
      height: 36px;
      background: #2e5aff;
      text-align: center;
      line-height: 36px;
      color: #fff;
      cursor: pointer;
      font-size: 14px;
      margin-bottom: 12px;
    }
  }
  .checkbox_list {
    position: absolute;
    top: 50%;
    right: 20px;
    transform: translateY(-50%);
    z-index: 1;
    background: linear-gradient(
      180deg,
      #0a1d64 0%,
      rgba(21, 30, 73, 0.6984) 100%
    );
    width: 135px;
    .title {
      height: 39px;
      line-height: 39px;
      width: 100%;
      background: linear-gradient(
        180deg,
        rgba(4, 36, 170, 0) 0%,
        #0424aa 87%,
        #0550d1 100%
      );
      text-align: center;
      font-size: 20px;
      color: #fff;
    }
    .check_item {
      padding: 10px 24px;
      :dee(.el-checkbox, .el-checkbox--small) {
        height: 30px;
      }
      :deep(.el-checkbox__label) {
        color: #fff;
        font-size: 14px;
      }
    }
  }
  .basic_box {
    width: 300px;
    height: 220px;
    position: absolute;
    bottom: -16px;
    left: -20px;
    z-index: 1;
    scale: 0.8;
    background: linear-gradient(
      180deg,
      #0a1d64 0%,
      rgba(21, 30, 73, 0.6984) 100%
    );
    .head_top {
      width: 100%;
      height: 36px;
      line-height: 36px;
      text-align: center;
      color: #fff;
      font-size: 20px;
      background: linear-gradient(
        180deg,
        rgba(4, 36, 170, 0) 0%,
        #0424aa 87%,
        #0550d1 100%
      );
    }
    .content_box {
      height: calc(100% - 56px);
      overflow: auto;
      padding: 10px 20px;
      font-size: 14px;
      .item{
        display: flex;
        align-items: center;
        height: 24px;
        .left{
          flex: 1;
        }
        .right{
          flex: 1;
        }
      }
      .police_box{
        margin-bottom: 10px;
        .item{
          height: 20px;
        }
        .color_name{
          color: aqua;
        }
      }
    }
    .police_content_box{
      // max-height: ;
    }
  }
  .org {
    position: absolute;
    left: 240px;
  }
  .police {
    position: absolute;
    left: 500px;
  }
  .lines{
    position: absolute;
    left: 760px;
  }
  .pwo{
    position: absolute;
    left: 1020px;
  }
  .legend_box{
    scale: 0.8;
    position: absolute;
    bottom: 0px;
    right: -20px;
    z-index: 1;
    width: 347px;
    height: 190px;
    background: url('./img/legendIcon.png') no-repeat center center;
  }
}
</style>
