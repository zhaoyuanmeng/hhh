import { handleTree, flattenTreeData,loadPicture } from "@/utils";
import { init } from "./map";
import useFloorStore from '@/store/modules/floorStore'
import useBasicStore from "@/store/modules/basicData";
import useSettingStore from '@/store/modules/settingStore'
import {samples_floorMovePull,samples_floorMoveDown,getleftStatus,} from "../componenets/buildingExplode/dismantle";
import { keyBuilding,openFloorsModal } from "@/components/SmartMap/componenets/buildingExplode/markerEvent.js";
import {nextTick} from "vue";
import {addCommunityMerchant} from '@/components/SmartMap/js/addMarkers'
/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-14 17:45:23
 * @LastEditTime: 2024-06-22 16:31:14
 * @LastEditors: Alex
 */
//控制相机移动
export async function moveCamera(data) {
  //(x,y,z,pitch,yaw,flyTime)

  //pitch	上下旋转角度（俯仰），单位是度。 此参数可选，如果没有设置或者设置为0，系统会自动设置默认值。

  //yaw	左右旋转角度（航向），单位是度。 此参数可选，如果没有设置或者设置为0，系统会自动设置默认值。

  //flyTime	可选参数，相机飞行的时间，取值范围：[0~任意正数]，单位：秒，默认值2秒
  let g = window.__g;
  g.camera.set(data);
}
// 开始透视分析
export async function startAnalysis() {
  let g = window.__g;
  let options = {
    fov_h: 150,//横向视角，取值范围：[1°~150°]，默认值：60
    fov_v: 45,//纵向视角，取值范围：[1°~150°]，默认值：30
    height: 1.8,//视点高度（距离场景交互所拾取点的高度），默认值：0
    visibleColor: Color.Green,//可见区域的颜色，默认值：红色 Color.Red
    invisibleColor: Color.Red,//不可见区域的颜色，默认值：绿色 Color.Green
    interactiveLock: false //是否开启交互锁定
  }
  g.tools.startViewshedAnalysis(options);
}

// 关闭透视分析
export async function stopAnalysis() {
  let g = window.__g;
  g.tools.stopViewshedAnalysis();
}

// 开始测量
export async function startMeasure() {
  let g = window.__g;
  //测量模式配置选项参数
  let options = {
    'pointSize': 8.0,
    'textSize': 10.0,
    'textColor': Color.Yellow,
    'pointColor': [0, 0, 1, 0.3],
    'lineColor': Color.Blue,
    'areaColor': [0, 1, 0, 0.3],
    'showCoordinateText': true
  };
  //设置测量模式，详情参考文档内MeasurementMode枚举 支持以下6类： 1坐标测量 2直线测量 3水平测量 4垂直测量 5多边形测量 6地表面积
  g.tools.setMeasurement(5, options);
  //开始测量 注意：5.3支持右键结束交互
  g.tools.startMeasurement();
}

// 关闭测量
export async function closeMeasure() {
  let g = window.__g;
  g.tools.stopMeasurement();
}


// 根据shapeID显示高亮对应的shape
export async function shapeFileLayerShow(id, fid = null) {
  let g = window.__g;
  g.shapeFileLayer.hide(id);
  if (fid) {
    g.shapeFileLayer.focusFeature(id, fid);
    g.shapeFileLayer.highlightFeature(id, fid);
  } else {
    g.shapeFileLayer.focus(id);
  }

}

// 点击点位添加样式
export async function clickePointAndAddStyle(id,coordinates) {
  // let g = window.__g;
  // g.radiationPoint.clear();
  // let o = {
  //     id: id,
  //     coordinate: coordinates,//辐射圈坐标位置
  //     coordinateType: 0,//坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0 
  //     radius: 15,//辐射半径
  //     rippleNumber: 5,//波纹数量
  //     color: [1, 0, 0, 0.8],//颜色
  //     intensity: 0.8,//亮度
  //     autoHeight: true//自动判断下方是否有物体
  // }
  //  g.radiationPoint.add(o);
}
export async function showHideLayer(bol) {
  // bol true开启地下模式 false 关闭地下
  let g = window.__g
  let res =  await g.infoTree.get()
  let treeLayer = handleTree(res.infotree, 'index', 'parentIndex')[0].children
  let initData = treeLayer.filter((item) => item.visiblity)
  let dxData = treeLayer.filter((item) => item.name === '地下空间')
  // let dsData = flattenTreeData(initData)
  let dsData = initData
  // let hideData = flattenTreeData(dxData)
  let hideData = dxData
  let hideLayer = useSettingStore().allLayer
  // console.log(hideLayer)
  if (bol) {
    for (const item of dsData) {
       g.infoTree.hide(item.iD)
    }
    for (const item1 of hideData) {
       g.infoTree.show(item1.iD)
    }
    await g.marker3d.clear();
    await g.marker.clear(); // 清除所有的marker
    await g.markerLayer.clear();// 清除markerLayer 
    await g.customObject.clear();
    await g.tag.clear() // 清楚所以Tag
    await g.polyline.clear() // 清除线
    await g.polygon.clear();// 清除面
    await g.polygon3d.clear();// 清除面
    await g.marker3d.clear()// 清除
    
    g.infoTree.show(window.floorLay)
     g.infoTree.hide(window.shapeFile) // 隐藏shepfile点位
     g.camera.set(493389.413523, 4323697.050156, 3365.91625, -74.007416, -87.579132, 1.8)
     useSettingStore().setDXModel(true)
  }
  if (!bol) {
    for (const item of dsData) {
      await g.infoTree.show(item.iD)
    }
    for (const item1 of hideData) {
      await g.infoTree.hide(item1.iD)
    }
    await g.reset(1 | 2 | 4);
    await g.infoTree.hide(window.shapeFile) // 隐藏shepfile点位
    useSettingStore().setDXModel(false)
     init().then(async()=>{
      await g.infoTree.hide('8C6F67F84F71F11130C1BEACD0BAF612') //隐藏高速 高铁
      await g.infoTree.hide('A3B755BF43736C15455300A0FB44761F') //隐藏高速 高铁
      await g.infoTree.hide('8B6725594EA91CCA8431338C12C4399F') //隐藏r1线
      drawGsGtData(0)
     })
     let gsgt = ['1F4A467A4C36DD1C74F760A6A0EFADF3','CDA8AF954AAB3987E64689BC9D3D0338']
     await g.infoTree.hide(gsgt)
  }
  addCommunityMerchant()
}

export  async function closeFloors(){ // 关闭楼层炸开
  useFloorStore().setFloornumberShow(false)
  useFloorStore().set_nofloorNumber(false)
  useFloorStore().setOpenFloor(false)// 关闭楼层炸开
  useFloorStore().set_isOpenBol(false)
  useFloorStore().set_floorName('')
  if(useFloorStore().floorsId){
    window.__g.marker.show(useFloorStore().floorsId)
    nextTick(()=>{
      useFloorStore().set_floorsId('')
    })
  }
  openFloorsModal('',false)
  useFloorStore().set_noFloorsId('')
  samples_floorMoveDown();
  useFloorStore().setIsActive('')
  useFloorStore().setFloornum(null)
  useSettingStore().setShowPanle(true)
  floorModalInit(1)
}


export async function openFloors(name,n){// 打开楼层
   keyBuilding(name,n)
  useSettingStore().setShowPanle(true);
  useSettingStore().setRightCard();
  useFloorStore().setOpenFloor(true)
  if (n != useFloorStore().isActive) {
    useFloorStore().setIsActive(n)
    useFloorStore().setFloornum(n)
  } else {
    useFloorStore().setIsActive(-1)
  }
}

// 初始化隐藏高速和高铁

export async function hideGSGT(){
  let g = window.__g;
  let res = await g.infoTree.get();
  let layer = res.infotree.filter((item) => item.visiblity); // 加载场景后请求说有的可见图层数
}

//楼层模型根据状态初始化楼层
export async function floorModalInit(type){
  // type = 1 初始化
  let g = window.__g;
  if(type===1){
    // 初始化
  g.infoTree.hide('033778894069F4FF69887998DB5A2926')// 会展中心3dmax
  g.infoTree.show('6D6C753E4356603BB3B3B98D25C90F13') // 商服倾斜
  // g.infoTree.show('5F2D2A104B9735917B9E0F90CE67244E') // 会展marker中心
  g.infoTree.hide('105D70174C8282E5DFD19BA2967C4C4D')// 国酒3dmax
  g.infoTree.show('2CABAF9944E024C206A1CEB0D922502A') // 国酒倾斜
  g.infoTree.hide('5DF2246A408080EBC44AD1925AC03F28') // 折现
  g.infoTree.hide('5B27CE9040FEEAA466EFC6BC395A2364')
  g.infoTree.hide('0AFBE44F4AFC4302C7A3E6BD717D768F')  // 超算3dmax
  g.infoTree.hide('E91B993641231B359BA88CB5F40E7F46') // 超算压平倾斜
    useFloorStore().setExplodebuildName('')
  //     // 显示地面植被和建筑
  // g.infoTree.show(['23DE18604BB525C068EA3DA11C76CFC5','838E04FF4A85B02D2D643A86CE8078B0','6C5F180E48757E29D7088BA1CDD5E011'])
  // // 隐藏会展中心地下一层和二层
  g.infoTree.hide(['127050144DBF70FA684EDAA9A244DF9B','791A38E84EF10CEB14062F8837357CFC'])
  g.infoTree.hide(['37223707454D3AFD49E406BBCB5449E5','8407B61443EE821814D46BB3D8DC2696']) // 隐藏雄安站
  g.infoTree.hide('E9E887FC4B5A5578DCD79EBBF4E69583') // 隐藏雄安站
  g.infoTree.show('09C10E364A84EDAD13F8E88C3C9F3D53') // 雄安站倾斜
  g.infoTree.hide('7602D058499FD7D2BF2E399578D95535')  // 星网3dmax
  g.infoTree.hide('305E823A4BEA68C20692668E5A703C89_1') // 星网倾斜
  g.infoTree.hide('5F081B404D874D74AC9C86B16BBC9F1A')  // 隐藏电信图层
  g.infoTree.hide('0A0A98734F39F1FAA50B3690A931D468_1_1_1')  // 隐藏电信图层
  g.infoTree.hide('45F4FC254F9FCE38415DE381831BA0DE')  // 隐藏规划中心图层
  g.infoTree.hide('D1A970F24B9AB98D4F5FDB8C618BF780_1')  // 隐藏规划中心图层
  g.infoTree.hide('648398474737F24D50F03E8BFE496F15')  // 隐藏会展中心酒店图层
  }

  // 会展中心
  if(type===2){
    g.infoTree.hide('033778894069F4FF69887998DB5A2926')
    g.infoTree.show('6D6C753E4356603BB3B3B98D25C90F13')
    // g.infoTree.hide('5F2D2A104B9735917B9E0F90CE67244E') // 隐藏3dmarker
    //  // 显示地面植被和建筑
    // g.infoTree.show(['23DE18604BB525C068EA3DA11C76CFC5','838E04FF4A85B02D2D643A86CE8078B0','6C5F180E48757E29D7088BA1CDD5E011'])
    // // 隐藏会展中心地下一层和二层
    // g.infoTree.hide(['127050144DBF70FA684EDAA9A244DF9B','791A38E84EF10CEB14062F8837357CFC'])
  }
   // 会议中心
   if(type===3){
    g.infoTree.hide('2CABAF9944E024C206A1CEB0D922502A')
    g.infoTree.show('105D70174C8282E5DFD19BA2967C4C4D')
    // g.infoTree.hide('5F2D2A104B9735917B9E0F90CE67244E') // 隐藏3dmarker
   }
   // 酒店
   if(type===4){
    g.infoTree.hide('2CABAF9944E024C206A1CEB0D922502A')
    g.infoTree.show('105D70174C8282E5DFD19BA2967C4C4D')
    // g.infoTree.hide('5F2D2A104B9735917B9E0F90CE67244E') // 隐藏3dmarker
   }
    // 公务接待楼
  if(type===5){
    g.infoTree.hide('2CABAF9944E024C206A1CEB0D922502A')
    g.infoTree.show('105D70174C8282E5DFD19BA2967C4C4D')
    // g.infoTree.hide('5F2D2A104B9735917B9E0F90CE67244E') // 隐藏3dmarker
  }
      // 雄安站
      if(type===6){
        // g.infoTree.hide('2CABAF9944E024C206A1CEB0D922502A')
       g.infoTree.show('E9E887FC4B5A5578DCD79EBBF4E69583')  
       g.infoTree.hide('09C10E364A84EDAD13F8E88C3C9F3D53') // 雄安站倾斜
    }
    // 计算中心
    if(type===7){
      // await g.infoTree.show('5DF2246A408080EBC44AD1925AC03F28') // 折现
      // await g.infoTree.show('5B27CE9040FEEAA466EFC6BC395A2364') // 折现
      g.infoTree.show('0AFBE44F4AFC4302C7A3E6BD717D768F')  // 3dmax
      g.infoTree.show('E91B993641231B359BA88CB5F40E7F46') // 压平倾斜
  }
  // 中国星网
  if(type===8){
    g.infoTree.show('7602D058499FD7D2BF2E399578D95535')  // 3dmax
    // g.infoTree.hide('830234F543BB7387AD8937BD9E24E3CE') // 星网倾斜
    g.infoTree.show('305E823A4BEA68C20692668E5A703C89_1')  // 3dmax
 }
   // 中国中化大厦
   if(type===9){
  }
  // 中国电信
  if(type===10){
    g.infoTree.show('5F081B404D874D74AC9C86B16BBC9F1A')  // 打开电信图层
    g.infoTree.show('0A0A98734F39F1FAA50B3690A931D468_1_1_1')  // 打开电信图层
  }
    // 规划展示中心
    if(type===11){
      g.infoTree.show('45F4FC254F9FCE38415DE381831BA0DE')  // 打开规划中心图层
      g.infoTree.show('D1A970F24B9AB98D4F5FDB8C618BF780_1')  // 打开规划中心图层
    }
     // 雄安会展中心酒店
     if(type===12){
      g.infoTree.show('648398474737F24D50F03E8BFE496F15')  // 打开7号楼
    }
}


// 通过投影坐标拿到经纬度

export async function getCoodr() {
  let g = window.__g
  // let data = [
  //   {name:'国际酒店',coods: [491851.52625,4325660.8,24.486757812500002]},
  //   {name:'国酒会议中心',coods: [ 491850.865,
  //     4325441.92,
  //     35.82072265625]},
  //     {name:'索菲特酒店',coods: [ 491992.791875,
  //       4324567.36,
  //       80.203984375]},
  //       {name:'会展中心',coods: [  491997.598125,
  //         4324473.92,
  //         36.2090234375]},
  //         {name:'雄安会展中心酒店',coods: [ 492158.038125,
  //           4324399.04,
  //           60.86701171875]},
  //           {name:'市民服务中心',coods: [ 491994.53250000003,
  //             4323943.04,
  //             5.97078125]},
  //             {name:'公务接待楼',coods:[491701.985,
  //               4324984.64,
  //               24.9100390625]}
  // ]
  let data = [ 
  // {name:'京广高铁',wzs:[481731.59375,4327823,12.007031440734863],type:'jggt'},
  // {name:'京雄高速',wzs:[495767.75,4326933,12.007031440734863],type:'jxgs'},
  // {name:'津雄高速',wzs:[ 500951.5,4323753.5,1.343593716621399],type:'jxgs1'},
  // {name:'保津高铁',wzs:[502194.0625,4326460,12.007031440734863],type:'bjgt'},
  // {name:'京雄城际高铁',wzs:[511273.15625,4321096,1.340000033378601],type:'jxcjgt'},
  // {name:'大广高速',wzs:[520843.125,4322812.5,1.343593716621399],type:'dggs'},
  {name:'千年秀林',wzs:[503002.47125, 4318264.96, 12.862568359375],type:'qnxl'},
  {name:'启动区综合服务中心',wzs:[495967.44656250003, 4323235.84, -15.7036328125],type:'qdqzhfwzx'}
]
  let jwdArr = []
  for(const item of data){
   let location = await g.coord.pcs2gcs(item.wzs)
   
   let obj = {
    name:item.name,
    wz:location.coordinates
   }
   jwdArr.push(obj)
  }
  // let  test = await g.coord.gcs2pcs(jwdArr[0].wz, 1)
  // console.log(test)
  // console.log(let co_location = await g.coord.gcs2pcs(item.jwd, 1))
}


export async function drawGsGtData(type) {
  let g = window.__g
  let gtB = 'CB5E7FAF46372CD46CD8A5A22311185A' // 高铁标主Id
  let gsB = '1EC420E74C93A5E45ACCF5B38F900F86'// 高速标主Id
  let jxgs = '00F49A7A4529EAFFE08625899BFD468A' // 京雄高速
  let dggs = '1BC3B8D14ABB0C8BDD6A88B5CBE4B211' // 大广高速
  let jxgs1 = 'C5C8FC964FD84D6724F7CFB7DE166B23' // 津雄高速
  let cjgt = '56F238084FEAB963E708E8851143C07A' // 城际高铁
  let bjgt = '619F33E34A16926F63A8E684DABB0480' // 保津高铁
  let jggt = 'FBCD430A455840F1EB9C1C850E5F4AAD' // 京广高铁
  let allLyaer = ['00F49A7A4529EAFFE08625899BFD468A']
  let allBs = [gtB,gsB]
    if(type===1){ // 显示全部
      g.infoTree.show(allBs)
    }
    // 隐藏全部
    if(type===0){
      g.infoTree.hide(allBs)
    }
}

export async function layerInit() {
  // bol true开启地下模式 false 关闭地下
  let g = window.__g
  // let res =  await g.infoTree.get()
  // let treeLayer = handleTree(res.infotree, 'index', 'parentIndex')[0].children
  // let initData = treeLayer.filter((item) => item.visiblity)
  // let dxData = treeLayer.filter((item) => item.name === '地下空间')
  // let dsData = initData
  // let hideData = dxData
  //   for (const item of dsData) {
  //     await g.infoTree.show(item.iD)
  //   }
  //   for (const item1 of hideData) {
  //     await g.infoTree.hide(item1.iD)
  //   }
  useBasicStore().set_roadMap(false)
    await g.reset(1|2 | 4);
    g.infoTree.hide(window.shapeFile) // 隐藏shepfile点位
    await g.infoTree.hide(window.shepfileCommunity) //隐藏所有的shepfile
}
const drawMarkers = async (arr) => {
  let g = window.__g
  let markerarr = [];
  for (const item of arr) {
    let o = {
      id:item.type,
      groupId: 'threeGsGt',
      userData: item.name,
      coordinate: item.wzs, //坐标位置
      coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 55],
      range: [0, 1500000], //可视范围
      imageSize: [40, 45], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(`./images/cockpit/gl.png`),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 1850000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
      fontSize: 12, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
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


// 漫游资源数据添加
export async function addTourResouce(arr){
  let g = window.__g
  let markerarr = []
  for (const item of arr) {
    let o = {
      id: item.id,
      groupId: 'resource',
      coordinate: [item.coordinates[0],item.coordinates[1],0], //坐标位置
      coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 50],
      range: [0, 10000], //可视范围
      imageSize: [40, 50], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(`./images/resource/${item.type}.png`),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 15], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
      fontSize: 12, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: false, // 自动判断下方是否有物体
      displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true
    };
    markerarr.push(o)
  }
  if (markerarr.length > 0) {
     g.marker.add(markerarr)
  }
}

// 游动哨修改
export const moveMarker = async () => {
  let g = window.__g
  await g.marker.clear();
  await g.marker3d.clear();
  await g.markerLayer.clear();// 清除markerLayer 
  await g.customObject.clear();
  //投影坐标
  let co_location = [495843.484687,4326594.63625,2];
  let o = {
      id: 'o1',//自定义对象唯一id
      pakFilePath: '@path:DTS_Library_V6.0.0306.pak',//资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
      assetPath: '/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_B_1',//资源目录，自定义对象在pak文件资源包里的相对路径
      location: co_location,//位置坐标
      coordinateType: 0,// 坐标系类型 
      rotation: [0, 0, 0],// 世界坐标系旋转
      range: [0, 1000000],//可见范围
      groupId: "coGroup",//分组id
      localRotation: [0, 0, 0],//模型自身旋转
      scale: [1, 1, 1],//模型缩放
      isEffectRotation: true,//是否开启旋转效果
      smoothMotion: 1,   //1: 平滑移动，0: 跳跃移动
      supportAttach: false, //不支持贴画贴合
      visible: false,//模型加载后默认是否显示
  };
  const marker = {
      id: 'm1',
      groupId: 'markerAdd',
      coordinate: co_location,//坐标位置
      coordinateType: 0,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-25, 50],//锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
      imageSize: [50, 50],//图片的尺寸
      range: [1, 100000],//可视范围
      imagePath: loadPicture(`./images/cloud/police/icon-6.png`),//显示图片路径
      fixedSize: true,//图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false 
      text: '测试游动哨',//显示的文字 
      useTextAnimation: false,//关闭文字展开动画效果 打开会影响效率
      textRange: [1, 100000],//文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0],// 文本偏移
      textBackgroundColor: Color.SpringGreen,//文本背景颜色
      fontSize: 12,//字体大小
      fontOutlineSize: 1,//字体轮廓线大小
      fontColor: Color.White,//字体颜色
      fontOutlineColor: Color.Black,//字体轮廓线颜色
      showLine: true,//标注点下方是否显示垂直牵引线
      lineSize: [2, 100],//垂直牵引线宽度和高度[width, height]
      lineColor: Color.SpringGreen,//垂直牵引线颜色
      lineOffset: [0, 0],//垂直牵引线偏移
      autoHeight: false,// 自动判断下方是否有物体
      displayMode: 2,//智能显示模式  开发过程中请根据业务需求判断使用四种显示模式 
      clusterByImage: true,// 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
      priority: 0,//避让优先级
      occlusionCull: false//是否参与遮挡剔除
  }
  await g.marker.add(marker)
  await g.customObject.add(o);
  g.marker.focus('m1',200)

  await g.marker.setAttachCustomObject([{
      markerId: marker.id, //标注id
      objectId: o.id, //自定义对象id
      offset: [0, 0.5, 0] //偏移量
  }]);

  // 平移动画路径
  const paths = [
    [
      495843.484687,
      4326594.63625,
      2
    ],
    [
      495958.661406,
      4326283.9075,
      2
    ],
    [
      496033.984375,
      4325920.46125,
      2
    ],
    [
      496139.700469,
      4325302.765625,
      2
    ]
  ]

  //构造移动路径点数组
  const pathPointArr = [];

  paths.forEach((ii, index) => {
      //构造数组元素 每1秒移动一次
      let elementPoint = { 'time': (index) * 10, 'coordinate': ii };
      pathPointArr.push(elementPoint);
  })
  console.log(pathPointArr)
  //车辆按GPS轨迹移动
  g.customObject.startMove(o.id, 0, pathPointArr);

}