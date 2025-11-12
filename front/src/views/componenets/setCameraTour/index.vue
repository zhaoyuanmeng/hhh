<!--
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 18:03:15
 * @LastEditTime: 2024-06-22 11:20:59
 * @LastEditors: Alex
-->
<template>
  <div class="taskStart_content">
    <transition name="slide" v-if="isShowList">
      <div class="left_box">
        <div class="heard">{{ taskInfo.taskName }}</div>
        <div class="taskInfo">
          <div class="top_info">{{ taskInfo.taskName }}</div>
          <div class="task_box">
            <div class="item_box">
              <div class="left">任务等级：</div>
              <div class="right" style="display:flex">
                <div class="point"></div>
                <div class="text">{{ taskInfo.taskLevel }}</div>
              </div>
            </div>
            <div class="item_box">
              <div class="left">开始时间：</div>
              <div class="right">{{ taskInfo.taskStartTime }}</div>
            </div>
            <div class="item_box">
              <div class="left">结束时间：</div>
              <div class="right">{{ taskInfo.taskEndTime }}</div>
            </div>
            <div class="item_box">
              <div class="left">总负责人：</div>
              <div class="right">{{ taskInfo.head }}</div>
            </div>
            <div class="item_box">
              <div class="left">联系电话：</div>
              <div class="right">{{ taskInfo.phone }}</div>
            </div>
            <div class="item_box">
              <div class="left">任务简介：</div>
              <div class="right">{{ taskInfo.taskDesc }}</div>
            </div>
          </div>
        </div>
        <div class="content">
          <div class="item_box">
            <div class="importantPoint" v-for="(item, index) in taskInfo.sceneList" :key="index"
              :class="item.id === screenInfo.id ? 'active_box' : ''">
              <div class="top_set">
                <div class="name">{{ item.sceneName }}</div>
                <div class="del" @click.stop="handleCommand(3, item, index)"><el-icon>
                    <Delete />
                  </el-icon></div>
              </div>
              <div class="btn_action">
                <div class="sub_btn" @click.stop="handleCommand(1, item, index)">
                  <el-icon>
                    <View />
                  </el-icon><span class="span1">漫游预览</span>
                </div>
                <div class="sub_btn" style="margin-left:16px" @click.stop="handleCommand(0, item, index)">
                  <el-icon>
                    <EditPen />
                  </el-icon><span class="span1">漫游编辑</span>
                </div>
              </div>
              <div class="model" @click.stop="changeVidew(item)"></div>
              <img class="camera-img" :src="item.sceneFrame.image" />
            </div>
            <div class="addTask" @click="add" v-if="!saveBol||taskInfo.sceneList.length===0">
              <img :src="addIcon" />
            </div>
          </div>
        </div>
        <div class="save_box">
          <div @click="gobackTo" class="sub_btn">返回</div>
          <div class="sub_btn sure_btn" @click="submitBiaoHui" v-if="saveBol&&taskInfo.sceneList.length!==0">保存</div>
          <!-- <div class="sub_btn sure_btn" @click="submitBiaoHui" v-if="saveBol">保存</div> -->
        </div>
      </div>
    </transition>
    <!-- 导览列表 -->
    <cameraTourTmpl v-if="!isShowList" />
  </div>
</template>

<script setup>
import {
  onBeforeUnmount,
  onMounted,
  onUnmounted,
  ref,
  computed,
  reactive,
  onBeforeMount,
  getCurrentInstance,
  watch,
  nextTick
} from "vue";
import { ElMessage, ElMessageBox } from 'element-plus';
import useCameraStore from "@/store/modules/cameraSet";
import useSettingStore from "@/store/modules/settingStore";
import addIcon from "@/assets/panel/Plus_add.png";
import cameraTourTmpl from "./CameraTour.tmpl.vue";
import CameraTourBIZ from "./cameraTour";
import { sessionStorage } from '@/utils/storage'
import { addPlot, clearPlot, initTools } from '@/components/SmartMap/js/addPlot'
import { addScreen, queryTaskInfo, deleteScreen, updateScreen } from '@/api/task/index'
import useFloorStore from '@/store/modules/floorStore'
import {openFloors,closeFloors} from '@/components/SmartMap/js/utils'
import SealAirCityAPI from "../../SysTools/resourceCatalog/SealAirCityAPI";
const { proxy } = getCurrentInstance();
window._cameraTour = new CameraTourBIZ();
const SettingStore = useSettingStore();
const cameraStore = useCameraStore()
const floorStore = useFloorStore()
let showPanle = computed(() => SettingStore.showTaskPanle);
let saveBol = computed(() => cameraStore.saveBtn)
let isShowList = computed(() => cameraStore.isShowList)
let screenInfo = computed(() => cameraStore.screenInfo)
let playtimer = ref(null);
let isNew = ref(false)
let tourBol = ref(false)
let selectId = ref(null)
//导览列表数组
let Tourlist = computed(() => cameraStore.tourList);
// 详情信息数据
let taskInfo = computed(() => cameraStore.taskInfo)
let explodebuildname = computed(() => floorStore.explodebuildname); //当前点击的楼栋名称
onMounted(() => {
  cameraStore.setisShowListShow(true)
  cameraStore.setSaveBtn(false)
  cameraStore.setSceneFrame({})
  let obj = {
    id: '',
    name: ''
  }
  cameraStore.setScreenInfo(obj)
  initTools()
  isNew.value = false
  selectId.value = null
});

//删除计时器
onBeforeUnmount(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
});
onBeforeMount(() => {
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
})
const gobackTo = () => {
  initTools()// 清除场景数据
  closeFloors()// 清除楼层炸开数据
  cameraStore.setScreenInfo({ name: '', id: '' })
  cameraStore.setShowTaskInfo(false)
  SettingStore.setShowTaskPanle(true);
  SettingStore.setshowTaskFun(true)
}
//编辑播放删除等
function handleCommand(type, single, index) {
  cameraStore.setSaveBtn(false)
 
  let item = cloneDeep(single);
  cameraStore.setTourList(item.keyFrames || [])
  let obj = {
    id: item.id,
    name: item.sceneName
  }
  cameraStore.setScreenInfo(obj)
  if (playtimer.value) {
    clearTimeout(playtimer.value);
    playtimer.value = null;
  }
  if (type == 0) {
    //编辑
    if (item.keyFrames && item.keyFrames.length > 0) {
      let obj = {
        id: item.id,
        name: item.sceneName,
        time: item.time,
        keyFrames: item.keyFrames,
      }
      window._cameraTour.curOa = obj;
    } else {
      window._cameraTour.start();
    }
    changeVidew(single)
    cameraStore.setisShowListShow(false)
  } else if (type == 1) {
    console.log(single)
    if (!single.keyFrames || single.keyFrames.length === 0) {
      return ElMessage({
        message: "当前没有漫游数据，请先编辑添加",
        type: "warning",
      });
    }
    cameraStore.setSceneFrame(single.sceneFrame)
    // if(single.keyFrames.length===1){
    //   return ElMessage({
    //   message: "当前只有一个场景",
    //   type: "warning",
    // });
    // }
    
    // tourBol.value = true
    //播放
    Tourlist.value[index].play = true;
    // window._cameraTour.curOa = item;
    let obj = {
      id: item.id,
      name: item.sceneName,
      time: item.time,
      keyFrames: item.keyFrames,
    }
    window._cameraTour.curOa = obj;
    window._cameraTour.play_biz();
    playtimer.value = setTimeout(() => {
      Tourlist.value[index].play = false;
      playtimer.value = null;
      // changeVidew(single)
    }, Number(item.time) * 1000);
  } else if (type == 2) {
    //停止
    window._cameraTour.curOa = item;
    window._cameraTour.stop_biz();
    Tourlist.value[index].play = false;
  } else if (type == 3) {
    //删除
    deleteGuide(item);
  }
}
// 点击场景切换视角
const changeVidew = async (item) => {
  // tourBol.value = false
  if(saveBol.value){
    proxy.$modal.msgWarning('请先保存该场景！')
    return 
  }
  cameraStore.setScreenInfo({id: item.id,name: item.sceneName})
  if(item.id!==selectId.value){
    if(item.floors&&item.floors.length>0){
    item.floors.forEach(item=>{
      openFloors(item.name,item.num)
    })
    selectId.value = item.id
  }else{
    closeFloors()
  }
  }
  isNew.value = true
  await clearPlot(item.drawData)
  await addPlot(item.drawData)
  cameraStore.setSaveBtn(true)
  window.__g.camera.set(
    item.sceneFrame.location[0],
    item.sceneFrame.location[1],
    item.sceneFrame.location[2],
    item.sceneFrame.rotation[0],
    item.sceneFrame.rotation[1],
    0.5
  );
  
  nextTick(() => {
    if (item.drawData.line) {
      sessionStorage.set('QXZS_pointLine', item.drawData.line)
    }
    if (item.drawData.area) {
      sessionStorage.set('QXZS_polygon', item.drawData.area)
    }
    if (item.drawData.moreArea) {
      sessionStorage.set('QXZS_polygon3d', item.drawData.moreArea)
    }
    if (item.drawData.marker) {
      sessionStorage.set('QXZS_3dMarker', item.drawData.marker)
    }
  })

}
// 添加场景
const add = async () => {
  ElMessageBox.prompt('请输入场景名称', '', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^[\s\S]*.*[^\s][\s\S]*$/,
    inputErrorMessage: '不能为空',
  })
    .then(({ value }) => {
      SettingStore.setShowTool(false)
      window._cameraTour.start();
      let lines = sessionStorage.get('QXZS_pointLine') // 线数据包含箭头
      let areas = sessionStorage.get('QXZS_polygon') // 面数据
      let moreAreas = sessionStorage.get('QXZS_polygon3d') // 三维多边形
      let markers = sessionStorage.get('QXZS_3dMarker') // 三维标数据
      console.log(floorStore.floornum,floorStore.openFloor)
      let floorData = []
      if(floorStore.openFloor){
        floorData = [{name:explodebuildname.value,num:floorStore.floornum}]
      }else{
        floorData =[]
      }
      window._cameraTour.setKeyFrames(1).then(() => {
        let obj = {
          sceneName: value,
          taskId: taskInfo.value.id,
          sceneFrame: {
            location: window._cameraTour.curOa.keyFrames[0].location,
            rotation: window._cameraTour.curOa.keyFrames[0].rotation,
            image: getCurCameraImage()
          },
          drawData: {
            line: lines,
            area: areas,
            moreArea: moreAreas,
            marker: markers
          },
          floors:floorData
        }
        addScreen(obj).then(res => {
          if (res.code === 0) {
            cameraStore.setSaveBtn(true)
            proxy.$modal.msgSuccess("新增成功！");
            SettingStore.setShowTool(true)
            isNew.value = false
            // clearPlot(obj.drawData)// 清除场景数据
            queryTaskInfo({ id: taskInfo.value.id }).then(res => {
              cameraStore.setTaskInfo(res.data)
              window._cameraTour.finish();
            })
          }
        })
      });
    })
}
//获取当前图片
function getCurCameraImage() {
  var video = document.getElementById("player").querySelector("video");
  var canvas = document.createElement("canvas");
  var scale = video.videoWidth / video.videoHeight;
  canvas.width = 300;
  canvas.height = parseInt(300 / scale);
  canvas.getContext("2d").drawImage(video, 0, 0, canvas.width, canvas.height);
  const base64 = canvas.toDataURL("image/png", 0.7);
  return base64;
}

// 删除store中导览
async function deleteGuide(index) {
  ElMessageBox.confirm(
    '您确定要删除该场景并删除场景下的漫游数据吗?',
    'Warning',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  )
    .then(() => {
      deleteScreen({ id: index.id }).then(res => {
        if (res.code === 0) {
          proxy.$modal.msgSuccess('删除成功')
          clearPlot(index.drawData)// 清除场景数据
          queryTaskInfo({ id: index.taskId }).then(res => {
            cameraStore.setTaskInfo(res.data)
          })
        }
      })
    })
}
//深拷贝对象
function cloneDeep(source, hash = new WeakMap()) {
  if (typeof source !== "object" || source === null) {
    return source;
  }
  if (hash.has(source)) {
    return hash.get(source);
  }
  const target = Array.isArray(source) ? [] : {};
  Reflect.ownKeys(source).forEach((key) => {
    const val = source[key];
    if (typeof val === "object" && val != null) {
      target[key] = cloneDeep(val, hash);
    } else {
      target[key] = val;
    }
  });
  return target;
}
// 添加标会数据
const submitBiaoHui = async () => {
  SettingStore.setShowTool(false)
  selectId.value = ''
  // if(tourBol.value){
  //   proxy.$modal.msgWarning('请先点击视角回到初始位置。')
  //   return 
  // }
  
  if (isNew.value) {
    window._cameraTour.start();
    let lines = sessionStorage.get('QXZS_pointLine') // 线数据包含箭头
    let areas = sessionStorage.get('QXZS_polygon') // 面数据
    let moreAreas = sessionStorage.get('QXZS_polygon3d') // 三维多边形
    let markers = sessionStorage.get('QXZS_3dMarker') // 三维标数据
    let floorData = []
      if(floorStore.openFloor){
        floorData = [{name:explodebuildname.value,num:floorStore.floornum}]
      }else{
        floorData =[]
      }
    window._cameraTour.setKeyFrames(1).then(() => {
      let obj = {
        id: screenInfo.value.id,
        sceneName: screenInfo.value.name,
        taskId: taskInfo.value.id,
        sceneFrame: {
          location: window._cameraTour.curOa.keyFrames[0].location,
          rotation: window._cameraTour.curOa.keyFrames[0].rotation,
          image: getCurCameraImage()
        },
        drawData: {
          line: lines,
          area: areas,
          moreArea: moreAreas,
          marker: markers
        },
        floors:floorData
      }
      updateScreen(obj).then(res => {
        if (res.code === 0) {
          cameraStore.setSaveBtn(false)
          SettingStore.setShowTool(true)
          window._cameraTour.finish();
          // proxy.$modal.msgSuccess("修改成功！");
          clearPlot(obj.drawData)// 清除场景数据
          closeFloors()
          cameraStore.setScreenInfo({ name: '', id: '' })
          queryTaskInfo({ id: taskInfo.value.id }).then(res => {
            cameraStore.setTaskInfo(res.data)
          })
        }
      })
    });
  } else {
    cameraStore.setSaveBtn(false)
    cameraStore.setScreenInfo({ name: '', id: '' })
    let lines = sessionStorage.get('QXZS_pointLine') // 线数据包含箭头
    let areas = sessionStorage.get('QXZS_polygon') // 面数据
    let moreAreas = sessionStorage.get('QXZS_polygon3d') // 三维多边形
    let markers = sessionStorage.get('QXZS_3dMarker') // 三维标数据
    let drawData = {
      line: lines,
      area: areas,
      moreArea: moreAreas,
      marker: markers
    }
    await clearPlot(drawData)// 清除场景数据
    SettingStore.setShowTool(true)
    closeFloors()
  }

}
</script>

<style lang="scss" scoped>
@import "./index.scss";
</style>
