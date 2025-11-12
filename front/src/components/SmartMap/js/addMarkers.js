/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-13 15:19:45
 * @LastEditTime: 2024-06-23 01:40:04
 * @LastEditors: Alex
 */
import { computed } from "vue";
import { loadPicture } from "@/utils";
import gsglConfig from "../config/gsglJson"; // 高速公路数据
import gstlConfig from "../config/gstlJson"; // 高速铁路数据
import useFloorStore from '@/store/modules/floorStore'
import useTaskStore from '@/store/modules/taskStore';
import { quertListForTypeId} from "@/api/basic/index";
import {flattenTreeData,getParentTree,getImageForName} from '@/utils'
import {closeFloors} from './utils'
import { sessionStorage } from "@/utils/storage";
import { ElMessage } from "element-plus";
let floorMarkers = computed(() => useFloorStore().floorMarkers);
let businessFloorsMarker = computed(() => useFloorStore().businessFloorsMarker);
// 添加点位
export async function addMarkerPoint(data, title) {
  let g = window.__g;
  // if (title === "会展中心") {
  //   g.camera.set(
  //     window.camera.hzzxCam[0],
  //     window.camera.hzzxCam[1],
  //     window.camera.hzzxCam[2],
  //     window.camera.hzzxCam[3],
  //     window.camera.hzzxCam[4],
  //     window.camera.hzzxCam[5]
  //   );
  //   return;
  // }
  // if (title === "市民服务中心") {
  //   g.camera.set(
  //     window.camera.sfzxCam[0],
  //     window.camera.sfzxCam[1],
  //     window.camera.sfzxCam[2],
  //     window.camera.sfzxCam[3],
  //     window.camera.sfzxCam[4],
  //     window.camera.sfzxCam[5]
  //   );
  //   return;
  // }

  await g.marker.deleteByGroupId('basic');
  await g.markerLayer.clear();
  sessionStorage.remove("layerMarkers"); // 清除markerLayer下面的markers
  addCommunityMerchant()
  let pointData = [];
  data.point.forEach((element) => {
    if (element.children) {
      pointData.push(element.children);
    }
  });
  let allPointData = pointData.reduce(
    (prev, current) => prev.concat(current),
    []
  );
  let datas = []
    if(allPointData.length>0){
      allPointData.forEach(item=>{
        if(item.data.jingdu&&item.data.weidu&&Number(item.data.jingdu)>110&&Number(item.data.weidu)<50){
          datas.push(item)
        }
      })
    }
  let markerarr = [];
  for (const item of datas) {
    let z
    if(item.data.gaodu){
      z =Number(item.data.gaodu)+0.5
    }else{
      z = 4.5
    }
    let location = {y:Number(item.data.weidu),x:Number(item.data.jingdu),z:z};
    let fatherId = getParentTree(item.id,data.point)
    let imgName = getImageForName(fatherId[0].name)
    let o = {
      id: item.id,
      groupId: data.type,
      coordinate: [Number(location.x),Number(location.y),Number(location.z)],//坐标位置
      text: item.data.mingcheng||item.data.title, //显示的文字
      imagePath: loadPicture(`./imgs/${imgName}.png`),
      imageSize: [30, 34],
      hoverImagePath: loadPicture(`./imgs/${imgName}.png`),// 鼠标悬停时显示的图片路径
      hoverImageSize: [30, 34],//鼠标悬停时显示的图片尺寸
    }
    markerarr.push(o);
  }
  // let newDatas = markerarr.filter(item=>item.text)
  // console.log(newDatas)
  if (markerarr.length > 0) {
    let o = {
      id: `basicDataLayer`,
      groupId: 'basicMarkerLayer',
      coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      range: [0,0.1,100000,200000],
      viewHeightRange :[0,100000],
      autoHeight: false,
      radius: 0.1,
      fixedSize: false,
      anchors: [-15, 34],//锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
      useTextAnimation: false,//关闭文字展开动画效果 打开会影响效率
      textRange: [1, 100000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
      fontSize: 8, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      displayMode: 1,//智能显示模式  开发过程中请根据业务需求判断使用四种显示模式 
      clusterByImage: false,// 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
      priority: 0,//避让优先级
      occlusionCull: true,//是否参与遮挡剔除
      markers:markerarr,
  }
  g.markerLayer.add(o)
  sessionStorage.set("layerMarkers",markerarr); // markerLayer下面的markers
  }else{
    sessionStorage.set("layerMarkers",[]); // markerLayer下面的markers
  }
  //  绘制现场住地点位
  if(data.xczdPoint&&Object.keys(data.xczdPoint).length !== 0){
      if(!data.xczdPoint.jd||!data.xczdPoint.wd){
        return ElMessage.warning('无数据，无法定位！')
      }
      let o = {
        id: `${data.type}${new Date().getTime()}`,
        groupId: 'basic',
        // userData: data.title,
        userData: '',
        coordinate: [data.xczdPoint.jd,data.xczdPoint.wd,2], //坐标位置
        coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
        anchors: [-20, 68],
        range: [0, 1500000], //可视范围
        imageSize: [40, 68], //图片的尺寸,//图片的尺寸
        imagePath: loadPicture(`./imgs/${data.title==='白洋淀站'||data.title==='雄安站'?'gt.png':data.type==='zd'?'hotel.png':data.type==='city'?'gl.png':'jz.png'}`),
        fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
        text: data.title, //显示的文字
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
      await g.marker.add(o)
      g.marker.focus(o.id,200)
  }
}

// // 高速高铁事件的点击事件
export const markerClick = (layerId, typeName) => {
  let obj = null;
  if (typeName === "高速线") {
    gsglConfig.gsglData.list.forEach((item) => {
      if (layerId === item.id) {
        obj = item;
      }
    });
  }
  if (typeName === "铁路矢量") {
    gstlConfig.gstlData.list.forEach((item) => {
      if (layerId === item.id) {
        obj = item;
      }
    });
  }
};

// 添加车辆
export const customCarToMove = async (pointArrs) => {
  let pointArr = [
    [492068.893672, 4324766.303125, 2.399219],
    [492062.862422, 4324779.38, 2.1966989999999997],
    [492075.576875, 4324800.169375, 2.3082809999999996],
    [492102.070547, 4324836.17875, 2.618184],
    [492119.087891, 4324863.226875, 2.3981049999999997],
    [492127.257188, 4324860.089375, 2.3649799999999996],
    [492131.041563, 4324878.365, 2.29248],
    [492133.168164, 4324900.590625, 14.674121],
    [492130.415742, 4324925.645625, 2.399844],
    [492130.169961, 4324938.599375, 2.340703],
    [492121.227617, 4324938.71125, 2.419355],
    [492119.112383, 4324893.5575, 2.262402],
    [492107.584688, 4324891.971875, 2.362695],
    [492088.744414, 4324893.621875, 2.549063],
    [492074.176016, 4324898.508125, 2.7085549999999996],
    [492069.223984, 4324920.605, 1.942676],
    [492078.970703, 4324937.77125, 1.688223],
    [492078.714453, 4324955.675, 8.16832],
    [492123.714336, 4324992.703125, 2.2319139999999997],
    [492134.907344, 4324989.074375, 2.3241799999999997],
    [492149.434727, 4325018.334375, 2.3469529999999996],
    [492129.78457, 4325023.104375, 2.3379489999999996],
    [492130.768789, 4325049.668125, 2.401992],
    [492124.852773, 4325061.65625, 2.4013669999999996],
    [492147.132344, 4325064.900625, 2.4013479999999996],
    [492144.792227, 4325085.98125, 2.4147659999999997],
    [492147.146133, 4325102.31625, 2.899648],
    [492171.169102, 4325120.058125, 2.423516],
    [492176.40418, 4325073.6725, 2.3863869999999996],
    [492166.995312, 4325044.090625, 2.421582],
    [492150.798789, 4325000.28625, 3.9029299999999996],
    [492160.322695, 4324993.46875, 3.954707],
    [492174.250195, 4325002.25625, 6.881758],
    [492175.069648, 4325015.618125, 2.8351949999999997],
    [492186.489023, 4325021.016875, 2.7595899999999998],
    [492193.861133, 4325035.503125, 2.7895309999999998],
    [492204.633789, 4325043.2525, 2.811191],
    [492217.439805, 4325038.709375, 2.8337109999999996],
    [492220.935938, 4325023.9225, 2.798438],
    [492214.340938, 4325009.04375, 3.7920119999999997],
    [492207.635, 4325007.43875, 5.709883],
    [492214.041758, 4324996.645, 2.722012],
    [492234.721719, 4324990.465, 2.69041],
    [492238.611563, 4324970.460625, 2.518887],
    [492238.763945, 4324945.886875, 2.3996679999999997],
    [492236.521836, 4324930.87875, 2.445801],
    [492226.664297, 4324924.713125, 2.1811909999999997],
    [492213.991992, 4324925.579375, 2.48709],
    [492208.604063, 4324916.16375, 2.3705659999999997],
    [492195.540469, 4324915.465625, 2.4030859999999996],
    [492196.954961, 4324923.411875, 2.3032419999999996],
  ];
  let g = window.__g;
  //添加前清空所有customObject 防止id重复
  g.customObject.clear();
  //投影坐标
  let co_location = pointArr[0];
  let o = {
    id: "o1", //自定义对象唯一id
    pakFilePath: "@path:DTS6.0_Library_08.25.pak", //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
    assetPath: "/JC_CustomAssets/VehicleLibrary/Exhibition/轿车_08", //资源目录，自定义对象在pak文件资源包里的相对路径
    location: co_location, //位置坐标
    coordinateType: 0, // 坐标系类型
    rotation: [0, 0, 0], // 世界坐标系旋转
    localRotation: [0, 90, 0], //模型自身旋转
    scale: [1, 1, 1], //模型缩放
    smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
  };
  await g.customObject.add(o);
  //构造移动路径点数组
  let pathPointArr = [];
  for (let i = 0; i < pointArr.length; i++) {
    //构造数组元素 每1秒移动一次
    let elementPoint = { time: i * 2, coordinate: pointArr[i] };
    pathPointArr.push(elementPoint);
  }
  //设置相机自动跟随
  //__g.customObject.focus('o1', -1);
  //设置自定义相机跟随
  g.customObject.focus("o1", 5, 0, [-30, 4, 0], ActionMode.Follow);
  //车辆按GPS轨迹移动
  g.customObject.startMove("o1", 0, pathPointArr);
};

// 先取消全局展示框
const deleteInfoModal = () => {
  useTaskStore().set_communityBol(false)
  useTaskStore().set_attractBol(false)
  // closeFloors()
}
// 全局添加社区和商户的标签
export const addCommunityMerchant = async () => {
  deleteInfoModal()
  // let data = useFloorStore().projectShowData
  // let businessData = data.commercialList
  // let communityData = data.residentialList
  // // let communityBox = data.regionList
  // if(businessData?.length||communityData?.length){
  //   // drawMarkers(businessData,communityData)
  // }
}

const drawMarkers = async (arr,communityList) => {
  let g = window.__g
  let markerarr = []
  let communityArr = []
  // let comBox = []
  for (let item of arr) {
    let o = {
      id: item.id,
      groupId: 'business',
      userData:item.pullOutFlag===1?item.buildingName:'CANT',
      coordinate: [Number(item.x),Number(item.y),2], //坐标位置
      coordinateType: 0,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-15, 34],
      range: [0, 1000], //可视范围
      imageSize: [30, 34], //图片的尺寸,//图片的尺寸
      imagePath: '',
      fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.buildingName, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [1,0.62,0.47,0.85], //文本背景颜色
      fontSize: 12, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: true, // 自动判断下方是否有物体
      displayMode: 1, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true
    };
    markerarr.push(o)
  }
  for (let item of communityList) {
    let o = {
      id: item.id,
      groupId: 'community',
      userData:item.buildingName,
      coordinate: [Number(item.x),Number(item.y),2], //坐标位置
      coordinateType: 0,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-15, 34],
      range: [0, 1000], //可视范围
      imageSize: [30, 34], //图片的尺寸,//图片的尺寸
      imagePath: '',
      fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.buildingName, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [0,0.5,0.5,0.85], //文本背景颜色
      fontSize: 8, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: true, // 自动判断下方是否有物体
      displayMode: 1, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true
    };
    communityArr.push(o)
  }
  // for (let item of communityBox) {
  //   let o = {
  //     id: item.id,
  //     groupId: 'community',
  //     userData:item.buildingName,
  //     coordinate: [Number(item.x),Number(item.y),2], //坐标位置
  //     coordinateType: 0,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
  //     anchors: [-15, 34],
  //     range: [0, 1000], //可视范围
  //     imageSize: [30, 34], //图片的尺寸,//图片的尺寸
  //     imagePath: '',
  //     fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
  //     text: item.buildingName, //显示的文字
  //     useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
  //     textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
  //     textOffset: [0, 0], // 文本偏移
  //     textBackgroundColor: [0,1,1,0.85], //文本背景颜色
  //     fontSize: 12, //字体大小
  //     fontOutlineSize: 1, //字体轮廓线大小
  //     fontColor: Color.White, //字体颜色
  //     fontOutlineColor: Color.Black, //字体轮廓线颜色
  //     autoHeight: true, // 自动判断下方是否有物体
  //     displayMode: 1, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
  //     priority: 0, //避让优先级
  //     occlusionCull: true, //是否参与遮挡剔除
  //     clusterByImage: true
  //   };
  //   comBox.push(o)
  // }
  if (markerarr.length > 0) {
    g.marker.add(markerarr)
  }
  if (communityArr.length > 0) {
    g.marker.add(communityArr)
  }
  // if (comBox.length > 0) {
  //   g.marker.add(comBox)
  // }
}

//隐藏所有炸开数据
export const hideFloorMarkersFun = async () => {
  if (floorMarkers.value && floorMarkers.value.length > 0) {
    let g = window.__g;
    let markers = []
    let lines = []
    for (const item of floorMarkers.value) {
      // await g.marker.delete(item.id);
      markers.push(item.id)
      if(item.data.info&&item.data.info.lineData){
        // await g.polyline.delete(item.data.info.lineData.id);
        lines.push(item.data.info.lineData.id)
      }
      if(item.type==='lines'){
        //  g.polyline.delete(item.data.id);
        lines.push(item.data.id)
      }
    }
    if(markers?.length){
      g.marker.delete(markers)
      g.marker3d.delete(markers)
    }
    if(lines?.length){
      g.polyline.delete(lines)
    }
    // setTimeout(() => {
    //   useFloorStore().set_floorMarkers([]);
    // }, 1000);
  }
  if(businessFloorsMarker.value&&businessFloorsMarker.value.length>0){
    let g = window.__g;
    let businessArr = []
    for (let bussies of businessFloorsMarker.value) {
      // await g.marker.delete(bussies.roomCode);
      businessArr.push(bussies.roomCode)
    }
    if(businessArr?.length){
      g.marker.delete(businessArr);
      g.marker3d.delete(businessArr)
    }
    // setTimeout(() => {
    //   useFloorStore().set_floorMarkers([]);
    // }, 1000);
  }
};

// 根据条件全局加载所有的现场和住地点位
export const addScenePlacePoint = async (arr) =>{
// arr 代表当前任务下有哪些现场和住地的数据
//1.先查询有哪些现场和住地
  // 现场
  let res1 = await quertListForTypeId('xc')
  let res2 = await quertListForTypeId('zd')
  let allData = [...res1.data, ...res2.data];
  let allPoint = allData.map(item=>{
    return { id: item.id,
      name: item.data.title,
      jwd: [Number(item.data.jingdu), Number(item.data.weidu)],
      }
  })
  if(arr?.length){
    let tasks = arr.filter(item=>item.type === "2" || item.type === "3")
     // 生成排除名称集合（自动去重）
    const excludeNames = new Set(tasks.map(s => s.sceneName));
    let newData = allPoint.filter(item => 
      !excludeNames.has(item.name)
    )
    showScenePlaceMarker(newData)
  }else{
    showScenePlaceMarker(allPoint)
  }
}

//绘制长显现场住地的marker
const showScenePlaceMarker = async (arrs) => {
  let newData = arrs.filter(item=>item.name !== undefined&&Number(item.jwd[0])>110&&Number(item.jwd[1])<50)
  let markerarr = []
  for (let item of newData) {
    let o = {
      id: `${item.id}`,
      groupId: 'scenePlace',
      userData:'scenePlaceMarker',
      coordinate: [Number(item.jwd[0]),Number(item.jwd[1]),1], //坐标位置
      coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-15, 34],
      range: [0, 50000], //可视范围
      imageSize: [30, 34], //图片的尺寸,//图片的尺寸
      imagePath: '',
      fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [0, 50000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
      fontSize: 8, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: true, // 自动判断下方是否有物体
      displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true
    };
    markerarr.push(o)
  }
  if (markerarr.length > 0) {
    let g = window.__g
     g.marker.add(markerarr)
  }
}

// 绘制任务路线起点-终点图标
export const drawRoadLineMarker = async (arrs,viewData) => {
  console.log(arrs)
  let newData = arrs.filter(item=>item.sceneName !== undefined&&Number(item.coor[0])>110&&Number(item.coor[1])<50)
  let markerarr = []
  for (let item of newData) {
    let o = {
      id: `${item.id}`,
      groupId: 'roadLineMarker',
      userData:'roadLineMarkerPoint',
      coordinate: [Number(item.coor[0]),Number(item.coor[1]),1], //坐标位置
      coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 68],
      range: [0, 150000], //可视范围
      imageSize: [40, 68], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(
        `./imgs/${
          item.sceneName === "白洋淀站" || item.sceneName === "雄安站"
            ? "gt"
            : item.type === "3" || item.type === "住地基本情况"
            ? "hotel"
            : item.type === "2" || item.type === "现场基本情况"
            ? "jz"
            : "gl"
        }.png`
      ),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.sceneName, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 15000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, -20], // 文本偏移
      textBackgroundColor: "#2a4cac", //文本背景颜色
      fontSize: 8, //字体大小
      fontColor: Color.White, //字体颜色
      autoHeight: false, // 自动判断下方是否有物体
      displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true,
    };
    markerarr.push(o)
  }
  if (markerarr.length > 0) {
    let g = window.__g
     await g.marker.add(markerarr)
     if (viewData) {
      g.camera.set(viewData.camera, 2);
    }else{
      g.marker.focus(markerarr[0].id,500)
    }
  }
}