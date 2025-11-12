import { sessionStorage } from "@/utils/storage";
import { nextTick, computed } from "vue";
import { flattenTreeData, loadPicture } from "@/utils";
import useFloorStore from '@/store/modules/floorStore'
import { addCommunityMerchant } from '@/components/SmartMap/js/addMarkers'
import useWorkCockpitStore from '@/store/modules/workCockpit.js'
import { drawLinesName } from '@/views/WorkCockpit/utils'
import { drawBussinessUser } from '@/components/SmartMap/componenets/buildingExplode/markerEvent'
import $modal from '@/utils/modal'
// const { proxy } = getCurrentInstance();
let showResouce = computed(() => useWorkCockpitStore().showResouce)
let threePage = computed(() => useWorkCockpitStore().threePageType) // 当前三级页面的类型
// // 控制标绘数据显示隐藏
export const drawContrlData = async (bol, type) => {
  // bol true 代表显示 false 隐藏 type 有 police 警力、car警车、line路线  cordon 警戒线 business 商业 community 社区
  let g = window.__g
  let car = sessionStorage.get("carData") || []; // 警车
  let police = sessionStorage.get("policeData") || []; //警力
  let lines = sessionStorage.get("QXZS_pointLine") || []; // 线路
  let cordon = sessionStorage.get("QXZS_polygon3d") || []; // 警戒线
  let uav = sessionStorage.get("uavData") || []; // 无人机
  let basicData = sessionStorage.get("basicData") || [] //基础数据
  let lineNames = sessionStorage.get("lineName") || [] //线路名字
  let businessData = sessionStorage.get("businessData") || [] //商业数据
  let floorsData = sessionStorage.get("floorsData") || [] //楼层炸开数据
  if (!bol) {
    // 隐藏
    if (type === 'police') {
      if (police?.length) {
        g.marker.hideByGroupId('police')
        let ydsLines = []
        for (const item of police) {
          if (item.info && Object.keys(item.info).length !== 0) {
            if (item.info.lineData && Object.keys(item.info.lineData).length !== 0) {
              ydsLines.push(item.info.lineData.id)

            }
          }
        }
        g.polyline.delete(ydsLines);
      }
      if (uav?.length) {
        g.marker.hideByGroupId('uav')
        let ids = uav.map(item => item.uav.id)
        g.radiationPoint.hide(ids) // 隐藏无人机辐射点
      }
      if (floorsData?.length) {
        let lineIds = []
        for (const item of floorsData) {
          if (item.data.info && item.data.info.lineData) {
            lineIds.push(item.data.info.lineData.id)
          }
        }
        g.polyline.delete(lineIds);
      }
    }
    if (type === 'car') {
      if (car?.length) {
        g.marker.hideByGroupId('car')
      }
    }

    if (type === 'line') {
      if (lines?.length) {
        let ids = lines.map(item => item.id)
        g.polyline.hide(ids)
      }
      if (lineNames?.length) {
        g.marker.hideByGroupId('linesName')
      }
      if (floorsData?.length) {
        let lineIds = []
        for (const item of floorsData) {
          if (item.type === 'lines') {
            lineIds.push(item.id)
          }
        }
        g.polyline.hide(lineIds)
      }
    }
    if (type === 'cordon') {
      if (cordon?.length) {
        let ids = cordon.map(item => item.id)
        g.polygon3d.hide(ids)
      }
    }
    if (type === 'business') {
      if (businessData?.length) {
        g.marker.hideByGroupId('room')
        g.marker3d.hideByGroupId('room')
      }
    }

    if (type === 'community') {
      let datas = useFloorStore().projectShowData
      if (datas.residentialList && datas.residentialList?.length) {
        g.marker.hideByGroupId('community')
      }
    }
    if (type === 'basic') {
      if (basicData?.length) {
        g.marker.hideByGroupId('basic')
      }
    }


  } else {
    // 显示
    if (type === 'police') {
      if (police?.length) {
        g.marker.showByGroupId('police')
        let ydsLines = []
        for (const item of police) {
          if (item.info && Object.keys(item.info).length !== 0) {
            if (item.info.lineData && Object.keys(item.info.lineData).length !== 0) {
              ydsLines.push(item.info.lineData)
            }
          }
        }
        g.polyline.add(ydsLines);
      }
      if (floorsData?.length) {
        let lineIds = []
        for (const item of floorsData) {
          if (item.data.info && item.data.info.lineData) {
            lineIds.push(item.data.info.lineData)
          }
        }
        g.polyline.add(lineIds)
      }
      if (uav?.length) {
        g.marker.showByGroupId('uav')
        let ids = uav.map(item => item.uav.id)
        g.radiationPoint.show(ids) // 隐藏无人机辐射点
      }
    }
    if (type === 'car') {
      if (car?.length) {
        g.marker.showByGroupId('car')
      }
    }
    if (type === 'line') {

      if (lines?.length) {
        let ids = lines.map(item => item.id)
        g.polyline.show(ids)
      }
      // if (lineNames?.length) {
      //   await g.marker.deleteByGroupId('linesName')
      //   drawLinesName(lineNames)
      // }
      if (floorsData?.length) {
        let lineIds = []
        for (const item of floorsData) {
          if (item.type === 'lines') {
            lineIds.push(item.id)
          }
        }
        g.polyline.show(lineIds)
      }
    }
    if (type === 'cordon') {
      if (cordon?.length) {
        let ids = cordon.map(item => item.id)
        g.polygon3d.show(ids)
      }
    }
    if (type === 'business') {
      if (businessData?.length) {
        g.marker.showByGroupId('room')
        g.marker3d.showByGroupId('room')
        // drawBussinessUser(businessData)
      }
      // let datas = useFloorStore().projectShowData
      // console.log(datas)
      // if(datas.commercialList&&datas.commercialList?.length){
      //   g.marker.showByGroupId('business')
      // }
    }

    // if(type==='community'){
    //   let datas = useFloorStore().projectShowData
    //   if(datas.residentialList&&datas.residentialList?.length){
    //     g.marker.showByGroupId('community')
    //   }
    // }

  }
};
const extractImagePath =(url)=> { 
  const parts = url.split('/images/'); 
  return parts.length > 1 ? '/images/' + parts[1] : null;
 }
// 绘制场景标绘数据
export const drawScreenData = async (drawList, status = null) => {
  // 先清在添加
  let g = window.__g;
  g.polyline.clear(); // 清除线
  g.polygon.clear(); // 清除面
  g.customObject.clear();// 清除自定义对象
  g.polygon3d.clear();// 清除警戒线
  g.tag.clear(); // 清除标签
  g.marker.clear(); // 清除marker
  g.marker3d.clear();
  g.radiationPoint.clear(); // 清除所有辐射点位样式
  g.tools.stopViewshedAnalysis(); // 停止视域分析
  sessionStorage.remove("QXZS_pointLine"); // 清除缓存的线
  sessionStorage.remove("QXZS_polygon"); // 清除缓存的面
  sessionStorage.remove("QXZS_polygon3d"); // 清除缓存的多边形
  sessionStorage.remove("policeData"); // 清除缓存的警力
  sessionStorage.remove("carData"); // 清除缓存的警车
  sessionStorage.remove("basicData"); // 清除缓存的基础数据
  sessionStorage.remove("uavData"); // 清除缓存的无人机数据
  addCommunityMerchant()
  if (drawList && drawList.length > 0) {
    let car = []; // 警车
    let police = []; //警力
    let lines = []; // 线路
    let basic = []; // 基础数据
    let cordon = []; // 警戒线
    let uav = []; // 无人机
    // 绘制数据的idList
    let markerList = []
    let lineList = []
    let customObjectList = []
    let radiationPointList = []
    let polygon3dList = []
    for (const item of drawList) {
      if (item.type === "police") {
        police.push(item.data);
      }
      if (item.type === "car") {
        car.push(item.data);
      }
      if (item.type === "lines") {
        lines.push(item.data);
      }
      if (item.type === "basic") {
        basic.push(item.data);
      }
      if (item.type === "cordon") {
        cordon.push(item.data);
      }
      if (item.type === "uav") {
        uav.push(item.data);
      }
      if (item.data.marker) {
        item.data.marker.displayMode = 2
        item.data.marker.anchors = [-15, 34]
        item.data.marker.imageSize = [30, 34]
        item.data.marker.textOffset = [-34, -30]
        item.data.marker.fixedSize = true
        item.data.marker.fontSize = 8
        item.data.marker.lineSize = [1, 5]
        item.data.marker.lineOffset = [15, 0]
        item.data.marker.textBackgroundColor = [0, 0, 0, 0.75]
        item.data.marker.fontColor = Color.White
        item.data.marker.imagePath = 'C:\\teqin' +extractImagePath(item.data.marker.imagePath)
          markerList.push(item.data.marker)
      }
      if (item.data.info && Object.keys(item.data.info).length !== 0) {
        if (item.data.info.lineData && Object.keys(item.data.info.lineData).length !== 0) {
        }
        if (item.data.info.custom && Object.keys(item.data.info.custom).length !== 0) {
          g.customObject.add(item.data.info.custom);
        }
      }
      if (item.data.uav) {
        g.radiationPoint.add(item.data.uav);
      }
      if (item.type === "lines") {
        if (status) {
          item.data.style = 4
          item.data.thickness = 30
        }
        lineList.push(item.data)
      }
      if (item.type === "road") {
        lineList.push(item.data)
      }

      if (item.type === "cordon") {
        g.polygon3d.add(item.data);
      }
    }
    if (markerList?.length) {
      g.marker.add(markerList)
    }
    if (lineList?.length) {
      g.polyline.add(lineList)
    }
    if (car.length > 0) {
      sessionStorage.set("carData", car);
    }
    if (lines.length > 0) {
      sessionStorage.set("QXZS_pointLine", lines);
    }
    if (police.length > 0) {
      sessionStorage.set("policeData", police);
    }
    if (basic.length > 0) {
      sessionStorage.set("basicData", basic);
    }
    if (cordon.length > 0) {
      sessionStorage.set("QXZS_polygon3d", cordon);
    }
    if (uav.length > 0) {
      sessionStorage.set("uavData", uav);
    }

    nextTick(() => {
      if (!showResouce.value) {
        drawContrlData(false, 'police')
        drawContrlData(false, 'car')
        drawContrlData(false, 'basic')
        if (threePage.value === 'roadLine') {
          drawContrlData(true, 'line')
          drawContrlData(false, 'cordon')
        } else {
          drawContrlData(true, 'cordon')
          drawContrlData(false, 'line')
        }
      }
    })
  }

};
// 清除标绘数据
export async function clearDraw() {
  $modal.loading("");
  let g = window.__g;
  g.polyline.clear(); // 清除线
  g.polygon.clear(); // 清除面
  g.tag.clear(); // 清除标签
  g.marker.clear();
  g.marker3d.clear();
  g.markerLayer.clear();// 清除markerLayer 
  g.customObject.clear();// 清除自定义对象
  g.polygon3d.clear();// 清除警戒线
  // await g.marker.deleteByGroupId("3dMarkers");
  // await g.marker.deleteByGroupId("police");
  // await g.marker.deleteByGroupId("car");
  // await g.marker.deleteByGroupId("basic");
  // await g.marker.deleteByGroupId("uav");
  // await g.customObject.clear();
  g.radiationPoint.clear(); // 清除所有辐射点位样式
  g.tools.stopViewshedAnalysis(); // 停止视域分析
  sessionStorage.remove("QXZS_pointLine");
  sessionStorage.remove("QXZS_polygon");
  sessionStorage.remove("QXZS_polygon3d");
  sessionStorage.remove("QXZS_3dMarker");
  sessionStorage.remove("policeData"); // 清除缓存的警力
  sessionStorage.remove("carData"); // 清除缓存的警车
  sessionStorage.remove("basicData"); // 清除缓存的基础数据
  sessionStorage.remove("uavData"); // 清除无人机数据
  $modal.closeLoading();
  // addCommunityMerchant()
}

// 标绘数据动效显示
export async function moveDrawAction(data) {
  console.log(data)
  let carList = [] // 警车
  let policeList = [] // 警力
  data.forEach(item => {
    if (item.data.marker) {
      if (item.data.marker.groupId === 'car') {
        carList.push(item.data)
      }
      if (item.data.marker.groupId === 'police') {
        policeList.push(item.data)
      }
    }
  })
  let g = window.__g;
  // let carList = data.car || []; // 警车
  // let lineS = data.line; // 线路
  // let policeList = data.police; // 警力
  //1》 判断警车是否开启了视域以及开启了周边和移动
  carList.forEach((car) => {
    if (car.info.sfkqzb) {
      drawRadiationPoint(car.id, car.marker.coordinate, car.info.sszb);
    }
    // if (car.info.sfkqsy) {
    //   let obj = {
    //     fov_h: car.info.fov_h, //横向视角，取值范围：[1°~150°]，默认值：60
    //     fov_v: car.info.fov_v, //纵向视角，取值范围：[1°~150°]，默认值：30
    //     height: car.info.height, //视点高度（距离场景交互所拾取点的高度），默认值：0
    //     visibleColor: car.info.visibleColor, //可见区域的颜色，默认值：红色 Color.Red
    //     invisibleColor: car.info.invisibleColor, //不可见区域的颜色，默认值：绿色 Color.Green
    //   };
    //   drawViewAnalys(car.marker.coordinate, obj);
    // }
  });
  //1》 判断警力是否开启了视域以及开启了周边和移动
  policeList.forEach((police) => {
    if (police.info.sfkqzb) {
      drawRadiationPoint(police.id, police.marker.coordinate, police.info.sszb);
    }
    // if (police.info.sfkqsy) {
    //   let obj = {
    //     fov_h: police.info.fov_h, //横向视角，取值范围：[1°~150°]，默认值：60
    //     fov_v: police.info.fov_v, //纵向视角，取值范围：[1°~150°]，默认值：30
    //     height: police.info.height, //视点高度（距离场景交互所拾取点的高度），默认值：0
    //     visibleColor: police.info.visibleColor, //可见区域的颜色，默认值：红色 Color.Red
    //     invisibleColor: police.info.invisibleColor, //不可见区域的颜色，默认值：绿色 Color.Green
    //   };
    //   drawViewAnalys(police.marker.coordinate, obj);
    // }
  });
}
// 绘制实时周边 id 坐标点、半径
async function drawRadiationPoint(id, coordinates, radius) {
  let g = window.__g;
  let o = {
    id: id,
    coordinate: coordinates, //辐射圈坐标位置
    coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
    radius: radius, //辐射半径
    rippleNumber: 1, //波纹数量
    color: [1, 0, 0, 0.8], //颜色
    intensity: 0.8, //亮度
    autoHeight: true, //自动判断下方是否有物体
  };
  await g.radiationPoint.add(o);
}

// 绘制视域
async function drawViewAnalys(coordinates, obj) {
  let g = window.__g;
  let options = {
    startPoint: coordinates, //起点坐标
    endPoint: [coordinates[0] + 20, coordinates[1] + 20, coordinates[2]], //终点坐标
    fov_h: obj.fov_h, //横向视角，取值范围：[1°~150°]，默认值：60
    fov_v: obj.fov_v, //纵向视角，取值范围：[1°~150°]，默认值：30
    height: obj.height, //视点高度（距离场景交互所拾取点的高度），默认值：0
    visibleColor: obj.visibleColor, //可见区域的颜色，默认值：红色 Color.Red
    invisibleColor: obj.invisibleColor, //不可见区域的颜色，默认值：绿色 Color.Green
    interactiveLock: true, //是否开启交互锁定
  };
  g.tools.startViewshedAnalysis(options);
}

// 绘制现场和住地的marker

export const drawMarkerScreen = async (sceneId, sceneName, arr) => {
  let g = window.__g
  let markerarr = [];
  for (const item of arr) {
    let o = {
      id: item.id,
      groupId: sceneId,
      userData: sceneName,
      coordinate: [item.geojson.coordinates[0], item.geojson.coordinates[1], 1], //坐标位置
      coordinateType: item.geojson.coordinates[2] ? 0 : 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 68],
      range: [0, 1500000], //可视范围
      imageSize: [40, 68], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(`./imgs/${item.name === '白洋淀站'||item.name==='雄安站' ? 'gt' : (item.type === '3' || item.type === '住地基本情况') ? 'hotel' : 'jz'}.png`),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 1500], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [2, 0], // 文本偏移
      // textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
      // textBackgroundColor: item.type==='现场基本情况'?Color.Yellow:Color.Crimson, //文本背景颜色
      textBackgroundColor: '#2a4cac', //文本背景颜色
      fontSize: 12, //字体大小
      // fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      // fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: true, // 自动判断下方是否有物体
      displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true
    };
    markerarr.push(o);
  }
  if (markerarr.length > 0) {
    g.marker.add(markerarr);
  }
}