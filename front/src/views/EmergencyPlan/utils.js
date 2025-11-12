import { sessionStorage } from "@/utils/storage";
import { nextTick } from "vue";
import { loadPicture } from "@/utils"
import { addCommunityMerchant } from '@/components/SmartMap/js/addMarkers'
// 绘制场景标绘数据
export const drawScreenData = async (drawList) => {
  // 先清在添加
  let g = window.__g;
  g.polyline.clear(); // 清除线
  g.polygon.clear(); // 清除面
  g.customObject.clear();// 清除自定义对象
  g.polygon3d.clear();// 清除警戒线
  g.tag.clear(); // 清除标签
  g.marker.clear(); // 清除marker
  g.marker3d.clear(); // 清除marker
  g.markerLayer.clear();// 清除markerLayer 
  g.radiationPoint.clear(); // 清除所有辐射点位样式
  g.tools.stopViewshedAnalysis(); // 停止视域分析
  sessionStorage.remove("QXZS_pointLine"); // 清除缓存的线
  sessionStorage.remove("QXZS_polygon"); // 清除缓存的面
  sessionStorage.remove("QXZS_polygon3d"); // 清除缓存的多边形
  sessionStorage.remove("policeData"); // 清除缓存的警力
  sessionStorage.remove("carData"); // 清除缓存的警车
  sessionStorage.remove("AJData"); // 清除缓存的警车
  sessionStorage.remove("basicData"); // 清除缓存的基础数据
  sessionStorage.remove("uavData"); // 清除缓存的无人机数据
  addCommunityMerchant()
  if (drawList && drawList.length > 0) {
    let car = []; // 警车
    let aj = [];// 安检
    let police = []; //警力
    let lines = []; // 线路
    let basic = []; // 基础数据
    let cordon = []; // 警戒线
    let uav = []; // 无人机
    let ydsLines = [];
    // 绘制数据的idList
    let lineList = []
    // 绘制应急点路线名称
    let lineNameMarker = []
    let markers = []
    let markerCustomObject = []
    for (const item of drawList) {
      if (item.type === "police") {
        police.push(item.data);
      }
      if (item.type === "car") {
        car.push(item.data);
      }
      if (item.type === "fbaj") {
        aj.push(item.data);
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
        if (
          item.data.marker.groupId === "police" ||
          item.data.marker.groupId === "car" || item.data.marker.groupId === "uav"
        ) {
          markerCustomObject.push(item.data)
        } else {
          markers.push(item.data.marker);
        }
      }
      if (item.data.info && Object.keys(item.data.info).length !== 0) {
        if (
          item.data.info.lineData &&
          Object.keys(item.data.info.lineData).length !== 0
        ) {
          if(item.data.info.sfkqLine){
            ydsLines.push(item.data.info.lineData);
          }
        }
      }
      if (item.type === "lines") {
        lineList.push(item.data)
        // let nameObj = {
        //   ...item.middlePoint,
        //   ...{name:item.name}
        // }
        // lineNameMarker.push(nameObj)
      }
      if (item.type === "road") {
        lineList.push(item.data)
      }

      if (item.type === "cordon") {
        item.data.generateTop = false
        item.data.generateSide = true
        item.data.generateBottom = false
        if (item.data.info) {
          item.data.info = item.data.info
        } else {
          item.data.info = { fangxian: '' }
        }
        g.polygon3d.add(item.data);
      }
    }
    // 基础marker
    if (markers?.length) {
      // 添加marker图层
      let newData = [];
      for (const item of markers) {
        let obj = {
          id: item.id,
          groupId: item.groupId,
          coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
          range: [0, 0.1, 1000, 20000],
          viewHeightRange: [0, 1000],
          autoHeight: false,
          radius: 0.1,
          fixedSize: false,
          anchors: [-15, 34], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
          useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
          textRange: [1, 1000], //文本可视范围[近裁距离, 远裁距离]
          textOffset: [0, 0], // 文本偏移
          textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
          fontSize: 8, //字体大小
          fontOutlineSize: 1, //字体轮廓线大小
          fontColor: Color.White, //字体颜色
          displayMode: 0, //智能显示模式  开发过程中请根据业务需求判断使用四种显示模式
          clusterByImage: false, // 聚合时是否根据图片路径分类，即当多个marker的imagePath路径参数相同时按路径对marker分类聚合
          priority: 0, //避让优先级
          occlusionCull: true, //是否参与遮挡剔除
          markers: [
            {
              id: item.id,
              groupId: item.groupId,
              coordinate: item.coordinate, //坐标位置
              text: item.text, //显示的文字
              imagePath: "C:\\teqin" + extractImagePath(item.imagePath),
              imageSize: [30, 34],
              hoverImagePath: "C:\\teqin" + extractImagePath(item.imagePath), // 鼠标悬停时显示的图片路径
              hoverImageSize: [30, 34], //鼠标悬停时显示的图片尺寸
            },
          ],
        };
        newData.push(obj);
      }
      g.markerLayer.add(newData);
    }
    // 模型对象
    if (markerCustomObject?.length) {
      let g = window.__g
      let markers = []
      let objects = []
      let uavs = []
      for (const item of markerCustomObject) {
        let marker3dObj = {
          id: item.marker.id,
          userData: item.marker.userData,
          groupId: item.marker.groupId,
          coordinate: item.marker.coordinate, //坐标位置
          coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
          text: item.marker.text, //显示的文字
          textSize: 120,//3D标注显示文字大小
          textColor: '#000080',//3D标注显示文字颜色
          textOutlineSize: 1,//3D标注显示文字轮廓大小
          textOutlineColor: Color.Black,// 3D标注显示文字轮廓颜色
          textFixed: false,// 3D标注显示文字是否固定文本朝向
          fixedSize: true,// 默认尺寸 非近大远小
          textVisible: true,//3D标注显示文字是否显示文本
          textLocation: [0, 0, 0.1],// 文字位置
          textRotation: [0, 90, 0],// 文字旋转
          textScale: [1, 1, 1],// 文字缩放
          pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A',//3D标注展示的特效名称
          pointVisible: true,//3D标注是否显示
          pointScale: 1,//3D标注整体缩放比例
          range: [1, 2000], //3D标注的可视距离范围：[min,max]，单位：米
          autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
          collision: true //默认开启碰撞
        }
        // item.marker.anchors = [-5, 10]
        // item.marker.coordinateType = 0
        // item.marker.range = [0, 200] //可视范围
        // item.marker.imageSize = [10, 10] //图片的尺寸,//图片的尺寸
        // item.marker.imagePath = ""
        // item.marker.fixedSize = false //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
        // item.marker.textRange = [1, 20] //文本可视范围[近裁距离, 远裁距离]
        // item.marker.textOffset = [0, 0] // 文本偏移
        // item.marker.fontSize = 12 //字体大
        // item.marker.autoHeight = false // 自动判断下方是否有物体
        // item.marker.displayMode = 2 //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
        // item.marker.priority = 0 //避让优先级
        // item.marker.occlusionCull = true //是否参与遮挡剔除
        // item.marker.showLine = false //是否参与遮挡剔除
        // item.marker.clusterByImage = true
        // item.marker.textBackgroundColor = [1, 0, 0, 0.8] //图片的尺寸,//图片的尺寸
        // item.marker.fontOutlineColor =  Color.Black
        markers.push(marker3dObj)
        if (item.customData) {
          let path, pak
          if (item.marker.groupId === 'police') {
            if (item.marker.userData === '交通哨') {
              pak = "@path:人物打包.pak"
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警"
            } else if (item.marker.userData === '快反力量' || item.marker.userData === '机动力量') {
              pak = "@path:人物打包.pak"
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵"
            } else if (item.marker.userData === '固定哨') {
              pak = "@path:DTS_Library_6.1_240731.pak"
              path = "/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1"
            } else {
              pak = "@path:人物打包.pak"
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察"
            }
          }else{
            pak = item.customData.pakFilePath
            path = item.customData.assetPath
          }
          item.customData.id = item.marker.id
          item.customData.location = item.marker.coordinate
          item.customData.pakFilePath = pak
          item.customData.assetPath = path
          item.customData.scale = item.marker.groupId === 'police'?[1.3, 1.3, 1.3]:[1, 1, 1]
          objects.push(item.customData);
        } else {
          let path, pak, localtion
          if (item.marker.groupId === 'police') {
            if (item.marker.userData === '交通哨') {
              pak = "@path:人物打包.pak"
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警"
            } else if (item.marker.userData === '快反力量' || item.marker.userData === '机动力量') {
              pak = "@path:人物打包.pak"
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵"
            } else if (item.marker.userData === '固定哨') {
              pak = "@path:DTS_Library_6.1_240731.pak"
              path = "/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1"
            } else {
              pak = "@path:人物打包.pak"
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察"
            }
            localtion = [0, 90, 0]
          } else if (item.marker.groupId === 'car') {
            if (item.marker.userData === '船舶') {
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/船/船_3"
            } else {
              path = "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/特种车辆/警车_1"
            }
            pak = "@path:DTS_Library_6.1_240731.pak"
            localtion = [0, 0, 0]
          } else {
            path = "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/其他/无人机_1"
            pak = "@path:DTS_Library_6.1_240731.pak"
            localtion = [0, 0, 0]
          }
          // let src = item.marker.groupId==='police'?'/JC_CustomAssets/RoleLibrary/Exhibition/静态人物/男性_3':item.marker.groupId==='car'?'/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/特种车辆/警车_1':'/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/其他/无人机_1'
          let obj = {
            id: item.marker.id, //自定义对象唯一id
            pakFilePath: pak, //资源库pak文件路径,推荐使用cloud内置的文件资源管理器加载pak并使用@path方式传入参数
            assetPath: path, //资源目录，自定义对象在pak文件资源包里的相对路径
            location: item.marker.coordinate, //位置坐标
            coordinateType: 0, // 坐标系类型
            rotation: [0, 0, 0], // 世界坐标系旋转
            range: [0, 10000], //可见范围
            groupId: item.marker.groupId,
            userData: item.marker.id,
            localRotation: localtion, //模型自身旋转
            scale: item.marker.groupId === 'police'?[1.3, 1.3, 1.3]:[1, 1, 1], //模型缩放
            isEffectRotation: true, //是否开启旋转效果
            smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
            supportAttach: true, //不支持贴画贴合
            visible: true, //模型加载后默认是否显示
          };
          objects.push(obj)
        }
        if (item.marker.groupId === 'uav') {
          if (item.uavData) {
            item.uavData.location = item.marker.coordinate
            uavs.push(item.uavData)
          } else {
            uavs.push({
              id: item.marker.id,
              coordinate: item.marker.coordinate, //辐射圈坐标位置
              coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
              radius: 100, //辐射半径
              rippleNumber: 5, //波纹数量
              color: "#130FEB", //颜色
              intensity: 0.5, //亮度
              autoHeight: false, //自动判断下方是否有物体
              userData: "无人机",
              groupId: "uav",
            })
          }
        }
      }
      g.radiationPoint.add(uavs)
      await g.marker3d.add(markers)
      await g.customObject.add(objects)
      // 进行贴合
      let markerAndObject = []
      // 固定哨修改
      let changeMove = []
      markers.forEach((item, index) => {
        if (item.userData === '固定哨') {
          changeMove.push({
            id: objects[index].id,
            functionName: '动画类型',
            parameters: [
              { "paramType": 5, "paramValue": "站立" },
            ]
          })
        }
        markerAndObject.push({
          marker3dId: item.id, //标注id
          objectId: objects[index].id, //自定义对象id
          offset: item.groupId === 'uav' ? [0, 0, 1] : [0, 0, 2], //偏移量
        })
      })
      if (markerAndObject?.length) {
        g.marker3d.setAttachCustomObject(markerAndObject)
      }
      if (changeMove?.length) {
        g.customObject.callBPFunction(changeMove);
      }
    }
    if (lineList?.length) {
      let datas = settingLineCoor(lineList)
      g.polyline.add(datas)
    }
    if (ydsLines?.length) {
      let linesData = settingLineCoor(ydsLines);
      g.polyline.add(linesData);
    }
    // if(lineNameMarker?.length){
    //   console.log(lineNameMarker)
    // }
    if (car.length > 0) {
      sessionStorage.set("carData", car);
    }
    if (aj.length > 0) {
      sessionStorage.set("AJData", aj);
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
      let cordons = cordon.map(item => {
        return { ...item, generateTop: false, generateSide: true, generateBottom: false }
      })
      sessionStorage.set("QXZS_polygon3d", cordons);
    }
    if (uav.length > 0) {
      sessionStorage.set("uavData", uav);
    }
  }
};
const extractImagePath = (url) => {
  const parts = url.split('/images/');
  return parts.length > 1 ? '/images/' + parts[1] : null;
}
// 绘制现场和住地的marker
export const drawBasicMarker = async (data, opne = null) => {
  console.log(data.type)
  let g = window.__g
  await g.marker.deleteByGroupId('yjya')
  let o = {
    id: data.id,
    groupId: 'yjya',// 应急预案基础点
    userData: opne ? data.title : '',
    coordinate: [data.geo[0], data.geo[1], 1], //坐标位置
    coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    anchors: [-20, 62],
    range: [0, 1500000], //可视范围
    imageSize: [40, 62], //图片的尺寸,//图片的尺寸
    imagePath: loadPicture(`./images/cockpit/${data.type}.png`),
    fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    text: data.title, //显示的文字
    useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    textRange: [1, 15000], //文本可视范围[近裁距离, 远裁距离]
    textOffset: [2, 0], // 文本偏移
    textBackgroundColor: '#2a4cac', //文本背景颜色
    fontSize: 12, //字体大小
    fontColor: Color.White, //字体颜色
    autoHeight: true, // 自动判断下方是否有物体
    displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
    priority: 0, //避让优先级
    occlusionCull: true, //是否参与遮挡剔除
    clusterByImage: true
  };
  await g.marker.add(o);
  g.marker.focus(data.id, 400, 0.3)
}

// 绘制每段路线对应名称
export const drawLinesName = async (lines) => {
  let arrs = []
  let g = window.__g
  if (lines?.length) {
    for (let item of lines) {
      if (item.middlePoint && !item.buildName && !item.floorNum) {
        arrs.push({ id: item.id, name: item.name, coordinates: item.middlePoint.coordinates })
      }
    }
  }
  if (arrs && arrs.length > 0) {
    let markerarr = []
    for (let item of arrs) {
      // let o = {
      //   id: `${item.id}${item.name}`,
      //   groupId: 'linesName',
      //   userData: 'linesName',
      //   coordinate: [item.coordinates[0], item.coordinates[1], 2], //坐标位置
      //   coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      //   anchors: [-15, 34],
      //   range: [0, 1000], //可视范围
      //   imageSize: [30, 34], //图片的尺寸,//图片的尺寸
      //   imagePath: '',
      //   fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      //   text: item.name, //显示的文字
      //   useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      //   textRange: [1, 100000], //文本可视范围[近裁距离, 远裁距离]
      //   textOffset: [0, 0], // 文本偏移
      //   textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
      //   fontSize: 10, //字体大小
      //   fontOutlineSize: 1, //字体轮廓线大小
      //   fontColor: Color.White, //字体颜色
      //   fontOutlineColor: Color.Black, //字体轮廓线颜色
      //   autoHeight: true, // 自动判断下方是否有物体
      //   displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      //   priority: 0, //避让优先级
      //   occlusionCull: true, //是否参与遮挡剔除
      //   clusterByImage: true
      // };
      let o = {
        id: `${item.id}${item.name}`,
        groupId: "linesName",
        userData: "linesName",
        text: item.name,//3D标注显示文字
        textSize: 120,//3D标注显示文字大小
        textColor: '#000080',//3D标注显示文字颜色
        textOutlineSize: 1,//3D标注显示文字轮廓大小
        textOutlineColor: Color.Black,// 3D标注显示文字轮廓颜色
        textFixed: false,// 3D标注显示文字是否固定文本朝向
        fixedSize: false,// 默认尺寸 非近大远小
        textVisible: true,//3D标注显示文字是否显示文本
        textLocation: [0, 0, 0.8],// 文字位置
        textRotation: [0, 90, 0],// 文字旋转
        textScale: [1, 1, 1],// 文字缩放
        pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A',//3D标注展示的特效名称
        pointVisible: true,//3D标注是否显示
        pointScale: 1,//3D标注整体缩放比例
        coordinate: [item.coordinates[0], item.coordinates[1]], //3D标注的坐标位置 注意：若坐标Z设置高度为0时 autoHeight=true则会显示在物体上方
        coordinateType: 1, //坐标系类型 
        range: [1, 10000], //3D标注的可视距离范围：[min,max]，单位：米
        autoHeight: true, //自动判断下方是否有物体，设置正确高度，默认值：false
        collision: true //默认开启碰撞
      }
      markerarr.push(o)
    }
    if (markerarr.length > 0) {
      // g.marker.add(markerarr)
      g.marker3d.add(markerarr)
    }
  }
}

const settingLineCoor = (arr) => {
  arr.forEach((item, index) => {
    item.depthTest = true
    item.coordinates.forEach((child, i) => {
      // const last = item.coordinates[0][2] 
      const length = child.length
      if (length > 2) {
        child[2] = child[2] + 0.5
      } else {
        child[2] = 0.5
      }
    })
  });
  return arr
} 