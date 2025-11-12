import { sessionStorage } from "@/utils/storage";
import { nextTick } from "vue";
import { loadPicture } from "@/utils"
import {addCommunityMerchant} from '@/components/SmartMap/js/addMarkers'
// 绘制每段路线对应名称
export const drawLinesName = async(lines) => {
  let arrs = []
  let g = window.__g
  if(lines?.length){
      for(let item of lines){
        if(item.middlePoint&&!item.buildName&&!item.floorNum){
          arrs.push({id:item.id,name:item.name,coordinates:item.middlePoint.coordinates})
        }
      }
  }
  if(arrs&&arrs.length>0){
    let markerarr = []
    for (let item of arrs) {
      let o = {
        id: `${item.id}${item.name}`,
        groupId: 'linesName',
        userData:'linesName',
        coordinate: [item.coordinates[0],item.coordinates[1],2], //坐标位置
        coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
        anchors: [-15, 34],
        range: [0, 100000], //可视范围
        imageSize: [30, 34], //图片的尺寸,//图片的尺寸
        imagePath: '',
        fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
        text: item.name, //显示的文字
        useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
        textRange: [1, 100000], //文本可视范围[近裁距离, 远裁距离]
        textOffset: [0, 0], // 文本偏移
        textBackgroundColor: [0, 0, 0, 0.75], //文本背景颜色
        fontSize: 10, //字体大小
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
       g.marker.add(markerarr)
    }
  }
}

// 点击任务查看绘制任务下场景点以及路线
export const drawTaskScenePointAndLine = async (arr) => {
  let g = window.__g
  // clearDraw()
  if (arr && arr.length > 0) {
    for (const item of arr) {
      if(item.type==='2'||item.type==='3'){
         // 画驻地和现场
      if (item.pointInfoList?.length) {
        drawMarkers(item.id, item.sceneName, item.pointInfoList)
      }
      }else{
        // 画线
      if (item.drawInfoList?.length) {
        drawLines(item.id, item.sceneName, item.drawInfoList)
        const name=item.sceneName?item.sceneName:item.name
        // 找到路线中间数据
        const length = item.drawInfoList.length;
        const middleIndex = Math.floor((length - 1) / 2);
        let zhongjianNum =  item.drawInfoList[middleIndex]; // 返回中间的元素
        console.log(zhongjianNum)
        let coordinates = zhongjianNum.middlePoint.coordinates
        // 绘制线路的marker
        drawLineMarker(item.id,coordinates,item.drawInfoList[0].id,name)
      }
      }

    }
  }
}
function getMiddleElement(arr) {
  const middleIndex = Math.floor(arr.length / 2);
  
  // 如果数组长度是奇数，返回中间的元素
  if (arr.length % 2 !== 0) {
    return arr[middleIndex];
  }
  // 如果数组长度是偶数，返回中间两个元素中的第一个
  else {
    return arr[middleIndex - 1];
  }
}

// 绘制任务下的驻地和现场
export const drawTaskMarkers=(arr)=>{
  if (arr && arr.length > 0) {
    for (const item of arr) {
      if (item.basicDataList?.length) {
        drawMarkers(item.sceneInfo.id, item.sceneInfo.sceneName, item.basicDataList)
      }
    }
  }
}

// 绘制任务下的线
export const drawTaskLines=(arr)=>{
  if (arr && arr.length > 0) {
    for (const item of arr) {
      if (item.drawLineList?.length) {
        drawLines(item.sceneInfo.id, item.sceneInfo.sceneName, item.drawLineList)
         // 找到路线中间数据
         const length = item.drawLineList.length;
         const middleIndex = Math.floor((length - 1) / 2);
         let zhongjianNum =  item.drawLineList[middleIndex]; // 返回中间的元素
         let coordinates = zhongjianNum.middlePoint.coordinates
        // 绘制线路的marker
        drawLineMarker(item.sceneInfo.id,coordinates,item.drawLineList[0].id,item.sceneInfo.sceneName)
      }
    }
  }
}

// 绘制现场和住地的marker
export const drawMarkers = async (sceneId, sceneName, arr) => {
  let g = window.__g
  let markerarr = [];
  for (const item of arr) {
    let o = {
      id: item.id,
      groupId: sceneId,
      userData: sceneName,
      coordinate: [item.geojson.coordinates[0],item.geojson.coordinates[1],1], //坐标位置
      coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 68],
      range: [0, 1500000], //可视范围
      imageSize: [40, 68], //图片的尺寸,//图片的尺寸
      // imagePath: loadPicture(`./images/cockpit/${item.type}.png`),
      imagePath:loadPicture(`./imgs/${item.name==='白洋淀站'||item.name==='雄安站'?'gt':(item.type==='3'||item.type==='住地基本情况')?'hotel':'jz'}.png`),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 150000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, -20], // 文本偏移
      // textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
      // textBackgroundColor: item.type==='现场基本情况'?Color.Yellow:Color.Crimson, //文本背景颜色
      textBackgroundColor: '#2a4cac', //文本背景颜色
      fontSize: 8, //字体大小
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
const settingLineCoor = (arr) => {
  arr.forEach((item,index) => {
    item.depthTest = true
    item.coordinates.forEach((child,i)=>{
      // const last = item.coordinates[0][2] 
      const length =child.length 
      if(length>2){
        child[2] = child[2]+0.5
      }else{
        child[2] = 0.5
      }
    })
  });
  return arr
} 
// 绘制线
const drawLines = async (id, name, arr) => {
  let g = window.__g
  let ids = []
  let lines = []
  for (const item of arr) {
    // await g.polyline.delete(item.id)
    // await g.marker.delete(item.id)
    item.data.userData = id
    item.data.style = 0
    item.data.thickness = 100
    // g.polyline.add(item.data);
    lines.push(item.data)
    ids.push(item.id)
  }
  if(lines?.length){
    let datas = settingLineCoor(lines)
    g.polyline.add(datas)
  }
}
export const  drawLinesAndeMarker = async(arr,name,showText=null) => {
    let g = window.__g
    let lines = []
    for (const item of arr) {
      item.data.userData = item.sceneId
      item.data.style = 0
      item.data.brightness = 1
      item.data.depthTest = true
      item.data.thickness = 40
      item.data.color = Color.Yellow
      lines.push(item.data)
      // g.polyline.add(item.data);
    }
    if(lines?.length){
      let datas = settingLineCoor(lines)
      g.polyline.add(datas)
    }
          // 找到路线中间数据
          const length = arr.length;
          const middleIndex = Math.floor((length - 1) / 2);
          let zhongjianNum =  arr[middleIndex]; // 返回中间的元素
          let coordinates = zhongjianNum.middlePoint.coordinates
    drawLineMarker(arr[0].sceneId,coordinates,arr[0].id,name,showText)
}
const drawLineMarker = async (sceneId, coord, id, name,showText) => {
  console.log(sceneId, coord, id, name)
  coord[2] = 10
  let g = window.__g
  // await g.marker.delete(id)
  let o = {
    id: id,
    groupId: sceneId,
    userData: name,
    coordinate: coord, //坐标位置
    coordinateType: 1,//默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
    anchors: [-20, 62], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
    imageSize: [40, 62], //图片的尺寸
    range: [0, 150000], //可视范围
    imagePath: loadPicture(`./images/cockpit/gl.png`),
    fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
    text: !showText?`${name}`:'', //显示的文字
    useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
    textRange: [1, 150000], //文本可视范围[近裁距离, 远裁距离]
    textOffset: [0, -20], // 文本偏移
    // textBackgroundColor: [0, 0, 0, 0.6], //文本背景颜色
    fontSize: 8, //字体大小
    // fontOutlineSize: 1, //字体轮廓线大小
    fontColor: Color.White, //字体颜色
    // fontOutlineColor: Color.Black, //字体轮廓线颜色
    textBackgroundColor: '#2a4cac', //文本背景颜色    showLine: true, //标注点下方是否显示垂直牵引线
    // lineSize: [1, 10], //垂直牵引线宽度和高度[width, height]
    // lineColor: Color.SpringGreen, //垂直牵引线颜色
    // lineOffset: [20, 0], //垂直牵引线偏移
    autoHeight: true, // 自动判断下方是否有物体
    displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
    priority: 0, //避让优先级
    occlusionCull: true, //是否参与遮挡剔除
    clusterByImage: true
  };
   g.marker.add(o);
}

// 绘制线路高亮
export const drawLinesHightLight = async (arrId) => {
  let g = window.__g
  g.polyline.updateBegin()
  for (const item of arrId) {
     g.polyline.setBrightness(item, 1);
     g.polyline.setDepth(item, true);
    // g.polyline.setStyle(item,4);
     g.polyline.setThickness(item, 40)
     g.polyline.setColor(item, Color.Yellow)
  }
  g.polyline.updateEnd()
}

// 取消线路高亮
export const cancelLinesHightLight = async (arrId) => {
  let g = window.__g
  g.polyline.updateBegin()
  for (const item of arrId) {
     g.polyline.setBrightness(item, 0.3);
     g.polyline.setDepth(item, true);
    // g.polyline.setStyle(item,0);
     g.polyline.setThickness(item, 20)
     g.polyline.setColor(item, [1,0.133,0,1])
  }
  g.polyline.updateEnd()
}

// 绘制现场住地高亮
export const drawMarkersHightLight = async (arrId) => {
  let g = window.__g
  g.marker.updateBegin()
  if (arrId) {
    await g.marker.setImageSize(arrId, [60, 65])
    g.marker.focus(arrId, 500, 0.01)
  }
  g.marker.updateEnd()
}

// 取消现场住地高亮
export const cancelMarkersHightLight = async (item) => {
  let g = window.__g
  g.marker.updateBegin()
  if (item.basicDataId) {
     g.marker.setImageSize(item.basicDataId, [40, 45])
  }
  g.marker.updateEnd()
}

// 驾驶舱高亮高铁和高速
export const heightLightGSorGt = async (gsIds,gtIds,type) => {
  let g = window.__g
  let gtB = 'CB5E7FAF46372CD46CD8A5A22311185A' // 高铁标主Id
  let gsB = '1EC420E74C93A5E45ACCF5B38F900F86'// 高速标主Id
  await g.reset(4);
  // g.infoTree.show('8C6F67F84F71F11130C1BEACD0BAF612') // 高速
  // g.infoTree.show('A3B755BF43736C15455300A0FB44761F') // 高铁
  if(type==='gs'){
    g.infoTree.show(gsIds)
    g.infoTree.hide(gtIds)
    g.infoTree.hide('A3B755BF43736C15455300A0FB44761F')
    g.infoTree.hide('8C6F67F84F71F11130C1BEACD0BAF612')
    g.infoTree.hide('8B6725594EA91CCA8431338C12C4399F')
    g.infoTree.show(gsB)
    g.infoTree.hide(gtB)
  }
  if(type==='gt'){
    g.infoTree.hide(gsIds)
    g.infoTree.show(gtIds)
    g.infoTree.hide('A3B755BF43736C15455300A0FB44761F')
    g.infoTree.hide('8C6F67F84F71F11130C1BEACD0BAF612')
    g.infoTree.hide('8B6725594EA91CCA8431338C12C4399F')
    g.infoTree.hide(gsB)
    g.infoTree.show(gtB)
  }
}
// 隐藏所有高亮视角复位
export const hideAllLayerReset = async () => {
  let g = window.__g
  let gsgt = ['1F4A467A4C36DD1C74F760A6A0EFADF3','CDA8AF954AAB3987E64689BC9D3D0338']
  g.infoTree.hide(gsgt)
  // g.infoTree.show('A3B755BF43736C15455300A0FB44761F')
  // g.infoTree.show('8C6F67F84F71F11130C1BEACD0BAF612')
  // g.reset(4);
}


// 绘制单个路线以及点位
export const drawLineAlong = async (arr) => {
    // 画点
    let g = window.__g
    // clearDraw()
    if (arr && arr.length > 0) {
      for (const item of arr) {
        // 画驻地和现场
        if (item.pointInfoList?.length) {
          let ids = item.pointInfoList.map(item=>item.id)
          // g.marker.hide(ids)
          // g.polyline.hide(ids)
          g.marker.delete(ids)
          g.polyline.delete(ids)
        }
        // 画线
        if (item.drawInfoList?.length) {
          // 绘制线路的marker
          let ids = item.drawInfoList.map(item=>item.id)
          // g.polyline.hide(ids)
          // g.marker.hide(item.drawInfoList[0].id)
          g.polyline.delete(ids)
          g.marker.delete(item.drawInfoList[0].id)
        }
      }
    }
}