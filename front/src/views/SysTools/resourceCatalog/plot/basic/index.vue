<!--
 * @FileDescription: 基础工作
 * @Author: yuanhaijun
 * @Date: 2022.11.28
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2022.11.28
 -->
<template>
  <div>
    <div v-show="!isShowAdd" class="cloud-func tag">
      <div class="func-title">
        基础工作
        <el-icon class="" @click.stop="handleClose" style="cursor: pointer">
          <CircleClose />
        </el-icon>
      </div>
      <div class="func-warp">
        <div class="left_input">
                <el-input
                  v-model="fileter"
                  placeholder="根据名称模糊搜索"
                  clearable
                  size="default"
                  style="width: 312px;"
                />
              </div>
        <el-scrollbar>
          <draggable :list="filteredList" item-key="name" @start="drag = true" @end="onDragEnd">
            <template #item="{ element }">
              <div class="material-list">
                <ul @click.stop="clickPoint(element)" :style="{ 'border': element.select ? '2px solid #274eef' : 'none' }">
                  <img :src="element.src" alt="" srcset="" />
                  <div class="name" style="padding-top: 12px">
                    {{ element.name }}
                  </div>
                </ul>
              </div>
            </template>
          </draggable>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref, getCurrentInstance, computed, watch,defineEmits } from "vue";
import draggable from "vuedraggable";
import useBasicStore from "@/store/modules/basicData";
import useScreenStore from "@/store/modules/screenStore";
import useTaskStore from "@/store/modules/taskStore";
import useFloorStore from "@/store/modules/floorStore";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import { loadPicture } from "@/utils";
import { CircleClose } from "@element-plus/icons-vue";
import { sessionStorage } from "@/utils/storage";
import { queryTaskInfo } from "@/api/task/index";
import useWorkCockpitStore from '@/store/modules/workCockpit'
import { saveScreenDraw,getMarkersForFloorAllData } from "@/api/task/task";
import {getEmcyInfo} from "@/api/task/emergency";
import { ElMessage, ElMessageBox } from "element-plus";
import emitter from "@/utils/emitter"
const { proxy } = getCurrentInstance()
const emit = defineEmits(["close"]);
let eventMap = computed(() => useBasicStore().eventSealAPI);
let screenInfo = computed(() => useScreenStore().screenInfo);
const editScreen = computed(()=>useScreenStore().editScreen)
const openFloor = computed(()=>useFloorStore().openFloor) // 楼层是否炸开
const floornum = computed(()=>useFloorStore().floornum) // 当前楼层点击楼层数
//当前点击的楼栋名称
let explodebuildname = computed(() => useFloorStore().explodebuildname);
const y_a_Info = computed(()=>useEmergencyStore().YAInfo) // 预案信息
const y_aEdit = computed(()=>useEmergencyStore().editYA) // 预案是否调用接口
const isShowAdd = ref(false);
const drag = ref(false);
const addLine = ref(false);
const lineList = ref([]);
const items = ref({})
const fileter = ref('')
let filteredList = computed(() => {
    return labelList.value.filter((item) => {
      if (fileter.value) {
        return item.name.toLowerCase().includes(fileter.value.toLowerCase());
      } else {
        return item;
      }
    });
});
const labelList = ref([
  // {
  //   src: loadPicture("./images/basic/gt_cz.png"),
  //   name: "高铁车站",
  // },
  {
    src: "./images/resource/学校.png",
    name: "学校",
    type:"学校"
  },
  {
    src: "./images/resource/工地.png",
    name: "工地",
    type:"工地"
  },
  {
    src: "./images/resource/限高.png",
    name: "限高",
    type:"限高"
  },
  {
    src: "./images/resource/十字路口.png",
    name: "十字路口",
    type:"十字路口"
  },
  {
    src: "./images/resource/跨线桥.png",
    name: "跨线桥",
    type:"跨线桥"
  },
  {
    src: "./images/resource/公交站.png",
    name: "公交站",
    type:"公交站"
  },
  {
    src: "./images/resource/红绿灯.png",
    name: "红绿灯",
    type:"红绿灯"
  },
  {
    src: "./images/resource/其他情况.png",
    name: "其他情况",
    type:"其他情况"
  },
  {
    src: "./images/resource/复杂地段.png",
    name: "复杂地段",
    type:"复杂地段"
  },
  {
    src: "./images/resource/主线桥.png",
    name: "主线桥",
    type:"主线桥"
  },
  {
    src: loadPicture("./images/basic/jtjg.png"),
    name: "井盖",
    type:"井盖"
  },
  {
    src: loadPicture("./images/basic/yjyy.png"),
    name: "医院",
    type:"医院"
  },
  {
    src: "./images/resource/丁字路口.png",
    name: "丁字路口",
    type:"丁字路口"
  },
  {
    src: "./images/resource/危爆场所.png",
    name: "危爆场所",
    type:"危爆场所"
  },
  {
    src: loadPicture("./images/basic/zgd.png"),
    name: "制高点",
    type:"制高点"
  },
  {
    src: loadPicture("./images/basic/fzcl.png"),
    name: "复杂村庄",
    type:"复杂村庄"
  },
  // {
  //   src: "./images/resource/复杂地段.png",
  //   name: "复杂地段",
  //   type:"复杂地段"
  // },
  {
    src: "./images/resource/高铁低路基路段.png",
    name: "高铁低路基",
    type:"高铁低路基路段"
  },
  {
    src: "./images/resource/高铁复杂路段.png",
    name: "高铁复杂路段",
    type:"高铁复杂路段"
  },
  {
    src: "./images/resource/高铁两侧危爆物品厂库.png",
    name: "高铁两侧危爆物品厂库",
    type:"高铁两侧危爆物品厂库"
  },
  {
    src: "./images/resource/高铁下穿道路.png",
    name: "高铁下穿道路",
    type:"高铁下穿道路"
  },
  {
    src: "./images/resource/高铁沿线复杂村镇.png",
    name: "高铁沿线复杂村镇",
    type:"高铁沿线复杂村镇"
  },
  {
    src: "./images/resource/高铁基站.png",
    name: "高铁基站",
    type:"高铁基站"
  },
  {
    src: "./images/resource/高铁四电所.png",
    name: "高铁四电所",
    type:"高铁四电所"
  },
  {
    src: "./images/resource/高铁道口.png",
    name: "高铁道口",
    type:"高铁道口"
  },
  {
    src: "./images/resource/高铁涵洞.png",
    name: "高铁涵洞",
    type:"高铁涵洞"
  },
  {
    src: "./images/resource/高铁平安道口.png",
    name: "高铁平安道口",
    type:"高铁平安道口"
  },
  {
    src: "./images/resource/高铁桥梁.png",
    name: "高铁桥梁",
    type:"高铁桥梁"
  },
  {
    src: "./images/resource/高铁隧道.png",
    name: "高铁隧道",
    type:"高铁隧道"
  },
  {
    src: "./images/resource/高铁制高点.png",
    name: "高铁制高点",
    type:"高铁制高点"
  },
  {
    src: "./images/resource/高速制高点.png",
    name: "高速制高点",
    type:"高速制高点"
  },
  {
    src: "./images/resource/高速桥梁.png",
    name: "高速桥梁",
    type:"高速桥梁"
  },
  {
    src: "./images/resource/高速互通.png",
    name: "高速互通",
    type:"高速互通"
  },
  {
    src: "./images/resource/高速涵洞.png",
    name: "高速涵洞",
    type:"高速涵洞"
  },
  {
    src: "./images/resource/高速服务区.png",
    name: "高速服务区",
    type:"高速服务区"
  },
  {
    src: "./images/resource/高速出入收费站.png",
    name: "高速出入收费站",
    type:"高速出入收费站"
  },
  {
    src: "./images/resource/高速出入口.png",
    name: "高速出入口",
    type:"高速出入口"
  },
  {
    src: "./images/resource/高速基站.png",
    name: "高速基站",
    type:"高速基站"
  },
  {
    src: "./images/resource/消防车.png",
    name: "消防车",
    type:"消防车"
  },
  // {
  //   src: loadPicture("./images/basic/gs.png"),
  //   name: "高速公路",
  // },
  // {
  //   src: loadPicture("./images/basic/gt.png"),
  //   name: "高速铁路",
  // },
  // {
  //   src: loadPicture("./images/basic/pttl.png"),
  //   name: "普通铁路",
  // },

  // {
  //   src: loadPicture("./images/basic/city_gd.png"),
  //   name: "工地",
  //   type:"工地"
  // },
  // {
  //   src: loadPicture("./images/basic/fzld.png"),
  //   name: "复杂地段",
  //   type:"复杂地段"
  // },
  // {
  //   src: loadPicture("./images/basic/gs_crk.png"),
  //   name: "高速出入口",
  // },
  // {
  //   src: loadPicture("./images/basic/gs_fwq.png"),
  //   name: "高速服务区",
  // },
  // {
  //   src: loadPicture("./images/basic/gs_jhb.png"),
  //   name: "高速结合部",
  // },
  // {
  //   src: loadPicture("./images/basic/gs_ql.png"),
  //   name: "高速桥梁",
  // },
  // {
  //   src: loadPicture("./images/basic/gt_czdj.png"),
  //   name: "高铁登记处",
  // },
  // {
  //   src: loadPicture("./images/basic/gt_jhb.png"),
  //   name: "高铁结合部",
  // },
  // {
  //   src: loadPicture("./images/basic/gt_jz.png"),
  //   name: "高铁基站",
  // },
  // {
  //   src: loadPicture("./images/basic/gt_sds.png"),
  //   name: "高铁四电所",
  // },
  // {
  //   src: loadPicture("./images/basic/hd.png"),
  //   name: "涵洞",
  // },
  // {
  //   src: loadPicture("./images/basic/jclk.png"),
  //   name: "交叉路口",
  //   type: "交叉路口",
  // },
  // {
  //   src: loadPicture("./images/basic/ql.png"),
  //   name: "高铁桥梁",
  // },
  // {
  //   src: loadPicture("./images/basic/sd.png"),
  //   name: "高速隧道",
  // },
  // {
  //   src: loadPicture("./images/basic/tl_sd.png"),
  //   name: "铁路隧道",
  // },
  // {
  //   src: loadPicture("./images/basic/sst.png"),
  //   name: "疏散梯",
  // },
  // {
  //   src: loadPicture("./images/basic/wxwck.png"),
  //   name: "危险物仓库",
  // },
  // {
  //   src: loadPicture("./images/basic/xc.png"),
  //   name: "现场",
  // },
  // {
  //   src: loadPicture("./images/basic/xcdl.png"),
  //   name: "下穿道路",
  // },
  // {
  //   src: loadPicture("./images/basic/yxjk.png"),
  //   name: "沿线监控",
  // },
  {
    src: loadPicture("./images/basic/zdbw.png"),
    name: "其他重要部位",
    type: "其他重要部位"
  },
  {
    src: loadPicture("./images/basic/bxd.png"),
    name: "避险点",
    node:"应急避险点"
  },
  // {
  //   src: loadPicture("./images/basic/city.png"),
  //   name: "城市中心",
  // },
  // {
  //   src: loadPicture("./images/basic/fqcf.png"),
  //   name: "废弃厂房",
  // },
  // {
  //   src: loadPicture("./images/basic/fzcz.png"),
  //   name: "复杂社区",
  //   type:'复杂社区'
  // },
  // {
  //   src: loadPicture("./images/basic/gd.png"),
  //   name: "国道",
  // },
  // {
  //   src: loadPicture("./images/basic/gl.png"),
  //   name: "公路",
  // },
  // {
  //   src: loadPicture("./images/basic/gs_fwqm.png"),
  //   name: "高速服务区名称",
  // },
  // {
  //   src: loadPicture("./images/basic/gs_ht.png"),
  //   name: "高速互通",
  // },
  // {
  //   src: loadPicture("./images/basic/gs_sfz.png"),
  //   name: "高速收费站",
  // },
  // {
  //   src: loadPicture("./images/basic/gs_sfzm.png"),
  //   name: "高速收费站名称",
  // },
  // {
  //   src: loadPicture("./images/basic/gt_dlj.png"),
  //   name: "高铁低路基",
  // },
  // {
  //   src: loadPicture("./images/basic/jjqy.png"),
  //   name: "警戒区域",
  // },
  // {
  //   src: loadPicture("./images/basic/kfll.png"),
  //   name: "快反力量",
  // },
  // {
  //   src: loadPicture("./images/basic/kxq.png"),
  //   name: "跨线桥",
  // },
  // {
  //   src: loadPicture("./images/basic/mbd.png"),
  //   name: "目标点",
  // },
  // {
  //   src: loadPicture("./images/basic/qjjk.png"),
  //   name: "球机监控",
  // },
  // {
  //   src: loadPicture("./images/basic/shengj.png"),
  //   name: "省界线",
  // },
  // {
  //   src: loadPicture("./images/basic/sj.png"),
  //   name: "市界线",
  // },
  // {
  //   src: loadPicture("./images/basic/xj.png"),
  //   name: "县界线",
  // },
  // {
  //   src: loadPicture("./images/basic/sxcd.png"),
  //   name: "上下车点",
  // },
  // {
  //   src: loadPicture("./images/basic/tdlk.png"),
  //   name: "铁路道口",
  // },
  // {
  //   src: loadPicture("./images/basic/wbwck.png"),
  //   name: "危爆物品厂库",
  //   type:"危爆厂库"
  // },
  // {
  //   src: loadPicture("./images/basic/wrjfzz.png"),
  //   name: "无人机反制组",
  // },
  // {
  //   src: loadPicture("./images/basic/xqgd.png"),
  //   name: "辖区管段",
  // },
  // {
  //   src: loadPicture("./images/basic/yjd.png"),
  //   name: "预警点",
  // },
  // {
  //   src: loadPicture("./images/basic/yjyy.png"),
  //   name: "医院",
  //   type:"医院"
  // },

  // {
  //   src: loadPicture("./images/basic/zbz.png"),
  //   name: "指北针",
  // },
  // {
  //   src: loadPicture("./images/basic/zdry.png"),
  //   name: "重点人员",
  //   type:'重点人员'
  // },
  // {
  //   src: loadPicture("./images/basic/zhudiqk.png"),
  //   name: "住地情况",
  // },
  // {
  //   src: loadPicture("./images/basic/zjgc.png"),
  //   name: "在建工厂",
  // },
  // {
  //   src: loadPicture("./images/basic/zxq.png"),
  //   name: "主线桥",
  // },
  // {
  //   src: loadPicture("./images/basic/jyz.png"),
  //   name: "加油站",
  //   type:"加油站"
  // },
  {
    src: loadPicture("./images/basic/gljg.png"),
    name: "管理机构",
    type:"管理机构"
  },
  {
    src: loadPicture("./images/basic/qtnsjg.png"),
    name: "其他内设机构",
    type:"其他内设机构"
  },
  {
    src: loadPicture("./images/basic/wy.png"),
    name: "物业",
    type:"物业"
  },
  {
    src: loadPicture("./images/basic/ba.png"),
    name: "保安",
    type:"保安"
  },
  // {
  //   src: loadPicture("./images/basic/zyjz.png"),
  //   name: "主要建筑",
  //   type:"主要建筑"
  // },
  {
    src: loadPicture("./images/basic/zyjz.png"),
    name: "其他主要建筑",
    type:"其他主要建筑"
  },
  {
    src: loadPicture("./images/basic/slqk.png"),
    name: "四邻情况",
    type:"四邻情况"
  },
  {
    src: loadPicture("./images/basic/tcc.png"),
    name: "停车场",
    type:"停车场"
  },
  {
    src: loadPicture("./images/basic/yjyy.png"),
    name: "应急医院",
    node:"应急医院"
  },
  {
    src: loadPicture("./images/basic/crk_b.png"),
    name: "出入口",
    type:"出入口"
  },
  {
    src: loadPicture("./images/basic/kf_b.png"),
    name: "客房",
    type:"客房"
  },

  {
    src: loadPicture("./images/basic/ct_b.png"),
    name: "餐厅",
    type:"餐厅"
  },
  {
    src: loadPicture("./images/basic/hys_b.png"),
    name: "会议室",
    type:"会议室"
  },
  {
    src: loadPicture("./images/basic/dt_b.png"),
    name: "电梯",
    type:"电梯"
  },
  {
    src: loadPicture("./images/basic/bxt_b.png"),
    name: "步行梯",
    type:"步行梯"
  },
  {
    src: loadPicture("./images/basic/ft_b.png"),
    name: "扶梯",
    type:"扶梯"
  },
  {
    src: loadPicture("./images/basic/cf_b.png"),
    name: "厨房",
    type:"厨房"
  },
  {
    src: loadPicture("./images/basic/jksxt_b.png"),
    name: "监控摄像头",
    type:"监控摄像头"
  },
  {
    src: loadPicture("./images/basic/xfxtk_b.png"),
    name: "新风外部进风口",
    type:"新风外部进风口"
  },
  {
    src: loadPicture("./images/basic/rqxtfm_b.png"),
    name: "燃气系统阀门",
    type:"燃气系统阀门"
  },
  // {
  //   src: loadPicture("./images/basic/zdbw_b.png"),
  //   name: "其他重点部位",
  //   type:"重点部位"
  // },
  // {
  //   src: loadPicture("./images/basic/zbzgd_b.png"),
  //   name: "周边制高点",
  //   type:"周边制高点"
  // }
]);
watch(
  () => eventMap.value,
  (nV, oV) => {
    if (nV) {
      if (nV.eventtype === 'LeftMouseButtonClick') {
        addPoint(nV.MouseClickPoint, items.value)
      }
    }
  },
  { deep: true, immediate: false }
);
const clickPoint = (el) => {
  labelList.value.forEach(item => {
    if (el.name === item.name) {
      item.select = true
      useBasicStore().setAddPoint(true)
      items.value = item
      proxy.$modal.msg('请点击地图选择点位')
    } else {
      item.select = false
    }
  })
}
const extractImagePath =(url)=> { const parts = url.split('/images/'); return parts.length > 1 ? '/images/' + parts[1] : null; }
// 绘制场景标绘数据
const keepImagesAndAfter = (url) => {
  return extractImagePath(url)
  // const regex = /^https?:\/\/[^\/]+(\/images\/.*)$/i;
  // return url.replace(regex, "$1");
  
//   const regex = /^(https?:\/\/|localhost:?\d*\/)([^\/]+\/images\/.*)$/i;
//       const match = url.match(regex);
//       return match ? match[2] : null; // 返回第二个捕获组的内容，如果没有匹配则返回 null
};
function removeCommonById(array1, array2) {
  // 创建一个Set来存储array2中所有对象的id
  const ids = new Set(array2.map((item) => item.id));
  // 使用filter方法过滤掉array1中id在ids Set中的对象
  return array1.filter((item) => !ids.has(item.id));
}
const onDragEnd = async (event) => {
};
const addPoint = (arr, obj) => {
  console.log(arr, obj)
  let g = window.__g;
  var tagObject = {
    id: `basic${new Date().getTime()}`,
    userData: obj.name,
    groupId: "basic",
    coordinate: arr, //坐标位置
    anchors: [-15, 34], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
    imageSize: [30, 34], //图片的尺寸
    range: [0, 180000], //可视范围
    imagePath: HostConfig.ImagePath + keepImagesAndAfter(obj.src),
    fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    text: obj.name, //显示的文字
    useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    textRange: [1, 180000], //文本可视范围[近裁距离, 远裁距离]
    textOffset: [-34, -30], // 文本偏移
    textBackgroundColor: [0,0,0,0.75], //文本背景颜色
    fontSize: 8, //字体大小
    fontOutlineSize: 1, //字体轮廓线大小
    fontColor: Color.White, //字体颜色
    showLine: false, //标注点下方是否显示垂直牵引线
    lineSize: [1, 5], //垂直牵引线宽度和高度[width, height]
    lineColor: Color.SpringGreen, //垂直牵引线颜色
    lineOffset: [15, 0], //垂直牵引线偏移
    autoHeight: false, // 自动判断下方是否有物体
    displayMode: 2, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
    priority: 0, //避让优先级
    occlusionCull: false, //是否参与遮挡剔除
  };
  g.marker.add(tagObject);
  let allObj = {
    id: tagObject.id,
    info: obj.type?{featureType:obj.type}:{},
    marker: tagObject,
  };
  if(openFloor.value&&floornum.value){
    allObj.info.buildName = explodebuildname.value
    allObj.info.floorNum = floornum.value
    }
  // useScreenStore().SET_watchBol(false);
  if(editScreen.value){
    let query = {
          taskId: screenInfo.value.taskId,
          sceneId: screenInfo.value.id,
          planNode: obj.node?obj.node:"1",
          type: "basic",
          policeType: "1",
          data: allObj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            // 更新资源
            emitter.emit("refreshResource",query.data.id)
            if(openFloor.value&&floornum.value){
              getMarkersForFloorAllData({buildName:explodebuildname.value}).then(res=>{
                if(res.data?.length){
                  useFloorStore().set_floorMarkers(res.data);
                }
              })
            }
            // if(allObj.info.featureType){
            //       useWorkCockpitStore().set_showResouce(true)
            //       useScreenStore().SET_watchBol(true);
            //     }else{
            //       useScreenStore().SET_watchBol(false);
            //     }
            if(screenInfo.value.taskId){
              queryTaskInfo({ id: screenInfo.value.taskId }).then((res) => {
              if (res.code === 0) {
                useTaskStore().SET_TASKINFO(res.data);
              }
            });
            }
          }
        });
  }
  if(y_aEdit.value){
    let query = {
          sceneId: y_a_Info.value.id,
          // planNode: obj.node?obj.node:"1",
          type: "basic",
          data: allObj,
        };
        saveScreenDraw(query).then((res) => {
          if (res.code === 0) {
            emitter.emit("refreshResource",query.data.id)
            if(openFloor.value&&floornum.value){
              getMarkersForFloorAllData({buildName:explodebuildname.value}).then(res=>{
                if(res.data?.length){
                  useFloorStore().set_floorMarkers(res.data);
                }
              })
            }
            // queryTaskInfo({ id: screenInfo.value.taskId }).then((res) => {
            //   if (res.code === 0) {
            //     useTaskStore().SET_TASKINFO(res.data);
            //     if(allObj.info.featureType){
            //       useScreenStore().SET_watchBol(true);
            //     }else{
            //       useScreenStore().SET_watchBol(false);
            //     }
            //   }
            // });
          }
        });
  }
  let data1 = sessionStorage.get("basicData");
  if (data1 && data1.length > 0) {
    let newData = data1.filter((item) => item.id !== allObj.id);
    newData.push(allObj);
    sessionStorage.set("basicData", newData);
  } else {
    sessionStorage.set("basicData", [allObj]);
  }
  clearAddPoint()
}
const handleClose = async () => {
  clearAddPoint()
  emit("close");
};
const clearAddPoint = () => {
  items.value = {}
  useBasicStore().setAddPoint(false)
  labelList.value.forEach(item => {
    item.select = false
  })
}
</script>
  
<style lang="scss" scoped>
@import "@/styles/func3.scss";

.list {
  display: flex;
  align-items: center;
  margin-bottom: 16px;

  .list-left {
    margin-left: 16px;
  }
}

.cloud-func {
  // height: 250px !important;
}

.tag .func-warp {
  width: 370px;
  padding: 10px 12px;

  :deep(.el-scrollbar__wrap) {
    max-height: 720px;
    margin-bottom: 0 !important;
    overflow-x: hidden;

    .label-warp-list {
      margin-bottom: 12px;
      height: 164px;
      position: relative;
      background: #4b5059;
      box-shadow: 0px 0px 0px 0px rgba(255, 255, 255, 0.15),
        0px 0px 0px 0px rgba(0, 0, 0, 0.5);
      border-radius: 16px;
      border: 2px solid #4b5059;
      display: flex;
      flex-direction: column;
      justify-content: space-between;

      &.active {
        border-color: #02b2ff;
      }

      .warp-list-top {
        width: 100%;
        height: 100%;
        padding: 18px;
        padding-bottom: 0;
        box-sizing: border-box;
        text-align: center;
        overflow: hidden;

        li {
          font-size: 18px;
          font-weight: 400;
          color: #ffffff;
          text-align: left;
        }

        img {
          width: auto;
          height: auto;
        }
      }

      .el-dropdown {
        position: absolute;
        top: 18px;
        right: 18px;
      }
    }
  }

  .warp-footer {
    display: flex;
    justify-content: center;
    margin-top: 10px;

    div {
      width: 100%;
      line-height: 38px;
      cursor: pointer;
      border-radius: 5px;
      border: 1px solid #029eff;
      text-align: center;
    }
  }

  :deep(.el-scrollbar__view) {
    height: 500px;
  }

  .material-list {
    cursor: pointer;
    width: 100px;
    height: 100px;
    margin-bottom: 5px;
    margin-right: 6px;
    background: #363840;
    border-radius: 9px;
    border: 1px solid #63646a;
    display: inline-block;
    text-align: center;
    padding: 5px;
    box-sizing: border-box;
    overflow: hidden;

    ul {
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;

      img {
        width: 40px;
        height: 48px;
      }

      .name {
        font-size: 14px;
        // padding: 10px 0;
      }
    }
  }
}</style>
  