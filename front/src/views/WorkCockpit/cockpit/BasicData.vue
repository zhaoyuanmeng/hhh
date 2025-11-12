<template>
  <div class="content-box">
    <div class="content-header">基础数据统计</div>
    <div class="tabs_box">
      <div class="item_tab" style="margin-right: 30px;" :class="{'active':tabVal===0}" @click="changeTabs(0)">基础工作</div>
      <div class="item_tab" :class="{'active':tabVal===1}" @click="changeTabs(1)">政务外网</div>
    </div>
    <el-scrollbar>
      <div class="content" v-if="tabVal===0">
        <div
          class="data-item"
          v-for="item in dataList"
          :key="item.key"
          :class="{ active: item.active === true }"
          @click="handleClickData(item)"
        >
          <p class="title">{{ item.title }}</p>
          <div class="count-box">
            <img :src="item.icon" alt="icon" />
            <b class="count">{{ item.data.num }}</b>
            <span class="unit" v-if="item.key === 'xc' || item.key === 'zd'"
              >个</span
            >
            <span class="unit" v-else>条</span>
          </div>
          <div class="info-box" v-if="item.key === 'xc' || item.key === 'zd'">
            <span class="label">总面积：</span>
            <p class="info">
              <span class="long">{{ item.data.area }}</span>
              <span class="unit">㎡</span>
            </p>
          </div>
          <div class="info-box" v-else>
            <span class="label">全长：</span>
            <p class="info">
              <span class="long">{{ item.data.length }}</span>
              <span class="unit">公里</span>
            </p>
          </div>
          <div class="info-box mt-12">
            <span class="label">重点点位：</span>
            <p class="info">
              <span class="long">{{ item.data.pointNum }}</span>
              <span class="unit">&nbsp;&nbsp;个&nbsp;&nbsp;</span>
            </p>
          </div>
        </div>
      </div>
      <div class="content" v-else>
        <div
          class="data-item"
         style="width:100%">
          <p class="title">居民数据</p>
          <div class="count_box_custom">
            <div class="item_box">
              <div class="top_data">
              <img :src="people" alt="icon" class="imgs"/>
            <b class="count">{{ peopleObj.residencenum }}</b>
            <!-- <b class="count">{{ juminData.ren }}</b> -->
            <span class="unit">人</span>
              </div>
              <div class="bottom_data">
                <!-- {{ peopleObj.communitynum }} -->
                  社区：<span style="font-size: 14px;margin: 0 8px;">{{ peopleObj.communitynum }}</span>个
              </div>
            </div>
            <div class="item_box">
              <div class="top_data">
              <img :src="people1" alt="icon" class="imgs"/>
            <!-- <b class="count">{{ peopleObj.residencenum }}</b> -->
            <b class="count">{{ peopleObj.householdnum }}</b>
            <span class="unit">户</span>
              </div>
              <div class="bottom_data">
                <!-- {{ peopleObj.estatenum }} -->
                  小区：<span style="font-size: 14px;margin: 0 8px;">{{ peopleObj.estatenum }}</span>个
              </div>
            </div>
          </div>
        </div>
        <div
          class="data-item"
          v-for="item in dataList1"
          :key="item.key"
          :class="{ active: item.active === true }"
          @click="handleClickData(item)"
        >
          <p class="title">{{ item.title }}</p>
          <div class="count_box_custom">
            <div class="item_box">
              <div class="top_data">
              <img :src="item.icon" alt="icon" class="imgs"/>
            <b class="count">{{ item.data }}</b>
            <span class="unit">户</span>
              </div>
              <div class="bottom_data">
                  行业类型：<span style="font-size: 14px;margin: 0 8px;">{{ item.typeData }}</span>种
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-scrollbar>
  </div>
  <div class="viewReset" @click="viewResetFun">
    <div class="icon_name"></div>
  </div>
  <div class="viewReset" @click="play" style="position: absolute;bottom: 20px;left:340px">
    <div class="icon_name_play"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, reactive } from "vue";
import { loadPicture } from "@/utils";
import { addCommunityMerchant } from "@/components/SmartMap/js/addMarkers";
// 图标
import IconGSGL from "@/assets/workcockpit/icon_gsgl.png";
import IconGSTL from "@/assets/workcockpit/icon_gstl.png";
import IconXCDA from "@/assets/workcockpit/icon_xcda.png";
import IconZDDA from "@/assets/workcockpit/icon_zdda.png";
import IconCSDL from "@/assets/workcockpit/icon_csdl.png";
import people from "@/assets/workcockpit/容器@1x2.png";
import people1 from "@/assets/workcockpit/容器@1x3.png";
import people2 from "@/assets/workcockpit/容器@1x4.png";
import people3 from "@/assets/workcockpit/容器@1x5.png";
import { heightLightGSorGt, hideAllLayerReset } from "../utils";
// 接口
import { getDataStatistics, quertListForTypeId,getPeopleApiData,getBusinessApiData } from "@/api/basic/index";
// 仓库
import useSettingStore from "@/store/modules/settingStore";
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import { drawGsGtData } from "@/components/SmartMap/js/utils";
let layerData = computed(() => useSettingStore().layerList); // 所有可见图层
const tabVal = ref(0)
const hideIds = ref([]);
const markerID = ref([]);
const juminData = window.people
const gsIds = ref(["CDA8AF954AAB3987E64689BC9D3D0338"]);
const gtIds = ref(["1F4A467A4C36DD1C74F760A6A0EFADF3"]);
const xcIds = ref([]);
const zdIds = ref([]);
const dataList = ref([
  { key: "gs", title: "高速公路", icon: IconGSGL, active: false, data: {} },
  { key: "gt", title: "高速铁路", icon: IconGSTL, active: false, data: {} },
  { key: "xc", title: "现场档案", icon: IconXCDA, active: false, data: {} },
  { key: "zd", title: "住地档案", icon: IconZDDA, active: false, data: {} },
  // { key: "city", title: "城市道路", icon: IconCSDL, active: false, data: {} },
]);
const dataList1 = ref([
  { key: "qy", title: "企业数据",  active: false, data: '3201',icon: people2,typeData:0 },
  { key: "sy", title: "商业数据",  active: false, data: '203',icon: people3,typeData:0 },
  // { key: "jm", title: "居民数据",  active: false, data: '10234' },
]);
const peopleObj = ref({
  communitynum:0,
  estatenum:0,
  householdnum:0,
  residencenum:0
})
const videwCamera = reactive({
  xc: [
    492160.0975, 4321785.455625, 3522.73875, -58.338009, -100.275475, 0.000007,
  ],
  zd: [
    492860.465625, 4323389.339062, 764.297109, -36.367401, -116.200348,
    0.000002,
  ],
});
const changeTabs = (e) => {
    tabVal.value = e
}
// 点击数据
const handleClickData = async (item) => {
  item.active = !item.active;
  dataList.value.forEach((el) => {
    if (el.key !== item.key) el.active = false;
  });
  console.log(item, gtIds.value);
  let g = window.__g;
  // g.marker.updateBegin()
  if (item.key === "gt") {
    if (!item.active) {
      showHideDatas('gt',false)
      return;
    }
    showHideDatas('gt',true)
  }
  if (item.key === "gs") {
    if (!item.active) {
      showHideDatas('gs',false)
      return;
    }
    showHideDatas('gs',true)
  }
  if (item.key === "xc") {
    if (!item.active) {
      showHideDatas('xc',false)
      return;
    }
    showHideDatas('xc',true)
    g.camera.set(videwCamera.xc, 2);
  }
  if (item.key === "zd") {
    if (!item.active) {
      // 取消选中回到整体视角
      showHideDatas('zd',false)
      return;
    }
    g.camera.set(videwCamera.zd, 2);
    showHideDatas('zd',true)
  }
  if (item.key === "city") {
    let g = window.__g;
    let cood = await g.camera.get();
    console.log(cood);
  }
};
// 隐藏数据点击哪个展示哪个
  const showHideDatas = (type,bol) => {
    let g= window.__g
    g.marker.updateBegin()
    if(bol){
    if(type==='gs'||type==='gt'){
      useSettingStore().setCanNot(true);
      g.marker.hideByGroupId('zd')
      g.marker.hideByGroupId('xc')
      if(type==='gs'){
        heightLightGSorGt(gsIds.value, gtIds.value, "gs");
      }
      if(type==='gt'){
        heightLightGSorGt(gsIds.value, gtIds.value, "gt");
      }
    }
    if(type==='xc'){
      hideAllLayerReset();
      useSettingStore().setCanNot(false)
      g.marker.hideByGroupId('zd')
      g.marker.showByGroupId('xc')
    }
    if(type==='zd'){
      useSettingStore().setCanNot(false)
      hideAllLayerReset();
      g.marker.hideByGroupId('xc')
      g.marker.showByGroupId('zd')
    }
    }else{
    viewResetFun()
    }
    g.marker.updateEnd()
  }
// 复位
const viewResetFun = () => {
  let g = window.__g;
  dataList.value.map((el) => {
    el.active = false;
  });
  g.marker.showAll()
  hideAllLayerReset();
  g.infoTree.show("A3B755BF43736C15455300A0FB44761F");
  g.infoTree.show("8C6F67F84F71F11130C1BEACD0BAF612");
  g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
  g.infoTree.show("CB5E7FAF46372CD46CD8A5A22311185A");
  g.infoTree.show("1EC420E74C93A5E45ACCF5B38F900F86");
  useSettingStore().setCanNot(false);
  g.reset(4);
  g.marker.updateBegin()
  g.marker.updateEnd()
};
const showXCandZD = async()=>{
    let g = window.__g
    await g.marker.clear();
    let res1 =await quertListForTypeId('xc')
    let res2 =await quertListForTypeId('zd')
    let data1 = res1.data
    let data2 = res2.data
    let arr1= data1.map((item)=>{
      return {...item,PointType:'xc'}
    })
    let arr2= data2.map((item)=>{
      return {...item,PointType:'zd'}
    })
    let arrs = [...arr1,...arr2]
    if (arrs?.length) {
      let markers = [];
      arrs.forEach((item) => {
        let jd = item.data&&item.data.jingdu?item.data.jingdu:0
        let wd = item.data&&item.data.weidu?item.data.weidu:0
        let obj = {
          id: item.id,
          name: item.data.title,
          jwd: [Number(jd), Number(wd)],
          leixing: item.PointType,
        };
        markers.push(obj);
      });
      // xcIds.value = markers.map((item) => item.id);
      if(markers?.length){
        drawMarkers(markers);
        addCommunityMerchant();
      }
      
    }
}
const showAllData = async () => {
  for (const item of dataList.value) {
    handleClickData1(item);
  }
};
const handleClickData1 = (item) => {
  quertListForTypeId(item.key).then((res) => {
    showDatas1(res.data, item.key);
  });
};
const showDatas1 = async (arr, type) => {
  let g = window.__g;
  if (type === "xc") {
    if (arr && arr.length > 0) {
      let g = window.__g;
      await g.marker.deleteByGroupId("xc");
      let markers = [];
      arr.forEach((item) => {
        let obj = {
          id: item.id,
          name: item.data.title,
          jwd: [Number(item.data.jingdu), Number(item.data.weidu)],
          leixing: type,
        };
        markers.push(obj);
      });
      xcIds.value = markers.map((item) => item.id);
      drawMarkers(markers, type);
      addCommunityMerchant();
    }
  }
  if (type === "zd") {
    if (arr && arr.length > 0) {
      let g = window.__g;
      await g.marker.deleteByGroupId("zd");
      let markers = [];
      arr.forEach((item) => {
        let obj = {
          id: item.id,
          name: item.data.title,
          jwd: [Number(item.data.jingdu), Number(item.data.weidu)],
          leixing: type,
        };
        markers.push(obj);
      });
      zdIds.value = markers.map((item) => item.id);
      drawMarkers(markers, type);
      addCommunityMerchant();
    }
  }
};

// 绘制现场和住地的marker
const drawMarkers = async (arr) => {
  let g = window.__g;
  let markerarr = [];
  let newArr = arr.filter(item=>Number(item.jwd[0])>110&&Number(item.jwd[1])<50) 
  for (const item of newArr) {
    let o = {
      id: item.id,
      groupId: item.leixing,
      userData: item.name,
      coordinate: [Number(item.jwd[0]),Number(item.jwd[1]),1], //坐标位置
      coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 68],
      range: [0, 1500000], //可视范围
      imageSize: [40, 68], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(`./imgs/${item.name==='白洋淀站'||item.name==='雄安站'?'gt.png':item.leixing==='zd'?'hotel.png':'jz.png'}`),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 1500], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [2, 0], // 文本偏移
      textBackgroundColor: '#2a4cac', //文本背景颜色
      fontSize: 10, //字体大小
      fontColor: Color.White,
      autoHeight: false, // 自动判断下方是否有物体
      displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true,
    };
    markerarr.push(o);
  }
  if (markerarr.length > 0) {
    await g.marker.add(markerarr);
    // g.marker.focus(markerarr[1].id, 1000, 0);
  }
};

// 查询基础数据
const getBasicData = async() => {
  // 现场住地点位
  showXCandZD()
  //高速
  getDataStatistics("gs").then((res) => {
    dataList.value[0].data = res.data;
  });
  //高铁
  getDataStatistics("gt").then((res) => {
    dataList.value[1].data = res.data;
  });
  // 现场
  getDataStatistics("xc").then((res) => {
    dataList.value[2].data = res.data;
  });
  //住地
  getDataStatistics("zd").then((res) => {
    dataList.value[3].data = res.data;
  });
  // 城市道路
  // getDataStatistics("city").then((res) => {
  //   dataList.value[4].data = res.data;
  // });
};
const play = () => {
  useWorkCockpitStore().set_showPlayVideo(true)
}
// 获取商业居民数据
const getPeopleBusinessData = () => {
  getPeopleApiData().then(res=>{
    peopleObj.value = res.data
  })
  getBusinessApiData().then(res=>{
    dataList1.value[0].data = res.data.enterprisenum
    dataList1.value[0].typeData = res.data.enterprisetype
    dataList1.value[1].data = res.data.businessnum
    dataList1.value[1].typeData = res.data.businesstype
  })
}
onMounted(() => {
  getBasicData()
  getPeopleBusinessData()
  window.__g.infoTree.show(window.shepfileCommunity)
});
</script>

<style lang="scss" scoped>
.content-box {
  width: 400px;
  height: calc(100% - 335px);
  background: linear-gradient(
    180deg,
    #0a1d64 0%,
    rgba(21, 30, 73, 0.6984) 100%
  );
  position: absolute;
  left: 10px;
  bottom: 20px;
  z-index: 21;
  transform: scale(0.8);
  transform-origin: left bottom;

  .content-header {
    width: 100%;
    height: 48px;
    line-height: 48px;
    text-align: center;
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    background: url("@/assets/workcockpit/header_bg.png") no-repeat;
  }
  .tabs_box{
    height: 48px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0 20px;
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    font-family: YouSheBiaoTiHei;
    font-size: 24px;
    .item_tab{
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      opacity: 0.7;
      cursor: pointer;
    }
    .active{
      opacity: 1;
      border-bottom:2px solid #00ceff;
    }
  }
  .content {
    padding: 20px 20px 0;
    box-sizing: border-box;
    display: flex;
    flex-wrap: wrap;
    gap: 16px;

    .data-item {
      width: 172px;
      height: 168px;
      background: url("@/assets/workcockpit/border.png") no-repeat center;
      padding: 8px 12px 12px;
      box-sizing: border-box;
      cursor: pointer;
      background-size: 100% 100%;
      &:hover {
        // background: url("@/assets/workcockpit/border_press.png") no-repeat center;
      }

      .title {
        font-family: YouSheBiaoTiHei;
        font-size: 18px;
      }

      .count-box {
        display: flex;
        align-items: flex-end;
        justify-content: center;
        margin: 12px 0 24px;

        .count {
          font-family: DIN;
          font-size: 40px;
          line-height: 30px;
          color: #00ceff;
          margin: 0 4px;
        }

        .unit {
          font-family: D-DIN;
          font-size: 14px;
        }
      }
      .count_box_custom{
        display: flex;
        align-items: center;
        justify-content: center;
        height: calc(100% - 20px);
        .item_box{
          flex: 1;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          .top_data{
            display: flex;
            align-items: center;
            justify-content: center;
            height: 32px;
            .imgs{
              width: 21px;
              height: 21px;
            }
            .count{
              font-family: DIN;
              font-size: 40px;
              line-height: 30px;
              color: #00ceff;
              margin: 0 4px;
            }
            .unit {
              font-family: D-DIN;
              font-size: 14px;
            }
          }
          .bottom_data{
            margin-top: 22px;
            color: #fff;
            font-size: 12px;
            width: 100%;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(270deg, rgba(26, 49, 129, 0) 0%, rgba(26, 49, 129, 0.62) 50%, rgba(26, 49, 129, 0) 100%);
          }
        }
      }

      .info-box {
        height: 24px;
        background: linear-gradient(
          270deg,
          rgba(26, 49, 129, 0) 0%,
          rgba(26, 49, 129, 0.62) 50%,
          rgba(26, 49, 129, 0) 100%
        );
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-family: D-DIN;
        font-size: 12px;

        .long {
          font-family: DIN;
          font-size: 14px;
          margin-right: 4px;
        }
      }

      .mt-12 {
        margin-top: 12px;
      }
    }

    .active {
      background: url("@/assets/workcockpit/border_press.png") no-repeat center;
    }
  }
}

:deep(.el-scrollbar) {
  height: calc(100% - 100px);
}
.viewReset {
  position: absolute;
  bottom: 70px;
  right: 340px;
  z-index: 20;
  width: 42px;
  height: 42px;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 4px;
  // padding: 5px;
  cursor: pointer;
  border: 1px solid #5B759B;
  background: linear-gradient(180deg, rgba(10, 29, 100, 0.7) 0%, rgba(21, 30, 73, 0.6984) 100%);
  // background: url("@/assets/basic/viewEye.png") no-repeat;
  // background-size: 100% 100%;
  .icon_name{
    width: 24px;
    height: 24px;
    background: url('@/assets/basic/容器@1x.png') no-repeat;
    background-size: 100% 100%;
  }
  .icon_name_play{
    width: 16px;
    height: 18px;
    background: url('@/assets/basic/路径@play.png') no-repeat;
    background-size: 100% 100%;
  }
}
.viewReset:hover{
    background: rgba(38, 68, 173, 0.5);
    box-sizing: border-box;
    border: 0.61px solid rgba(38, 68, 173, 0.3);
  }
</style>
