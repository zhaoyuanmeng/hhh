import projectShpData from "/public/geojson/规划";
import countyShpData from "/public/geojson/县";
import useUserStore from "@/store/modules/user";
import useMapStore from "@/store/modules/mapStore.js";
import useWorkCockpitStore from "@/store/modules/workCockpit.js";
import useSettingStore from "@/store/modules/settingStore";
import useScreenStore from "@/store/modules/screenStore";
import useTaskStore from "@/store/modules/taskStore";
import { ref, computed } from "vue";
import { drawGsGtData } from "./utils";
const curPage = computed(() => useWorkCockpitStore().curPage);
const roadDrawList = computed(() => useWorkCockpitStore().roadDrawList);
const taskScreenLineList = computed(()=>useWorkCockpitStore().taskScreenLines)
const routerName = computed(() => useUserStore().routerIndex);
const sceneBol = computed(() => useTaskStore().sceneBol);
let canNot = computed(() => useSettingStore().canNot);
let mapType = computed(() => useMapStore().mapType);
let lineId = ref([]);
let markerId = ref([]);
let polygon3d = ref([]);
// 规划片区
let prjLineId = ref([]);
let prjMarkerId = ref([]);
let prjPolygon3d = ref([]);
// 三县
let ctyLineId = ref([]);
let ctyMarkerId = ref([]);
let ctyPolygon3d = ref([]);

const color = {
  容西: "#0ca7d3",
  容东: "#0ca7d3",
  起步区: "#fbc118",
  启动区: "#f9db00",
  寨里: "#0ca7d3",
  雄东: "#0ca7d3",
  昝岗: "#0ca7d3",
  容城县: "#53769b",
  安新县: "#53769b",
  雄县: "#53769b",
};

const coordinate = {
  // '容西': [484677.84375, 4324910, 1000.199375033378601],
  // '容东': [492751.90625, 4325294, 1000.2000000476837158],
  // '启动区': [495422.65625, 4320342.5, 1000.2000000476837158],
  // '寨里': [481371.125, 4313781, 1000.199375033378601],
  // '雄东': [512415.25, 4317806.5, 1000.2000000476837158],
  起步区: [489291.90750000003, 4318970.88, 1000.2000000476837158],
  昝岗: [515180.2675, 4324288, 1000.2000000476837158],
};
const countyCoordinate = {
  雄县: [515424.09375, 4319622.5, 1.1974999904632568],
  容城: [489114.75, 4325842, 1.2024999856948853],
  安新: [486947.375, 4301920.5, 1.2000000476837158],
};

export const init = async () => {
  let g = window.__g
   g.reset(4);
  let res = await __g.camera.get();
  if (res.camera[2] <= 25000) {
    __g.infoTree.hide("8C6F67F84F71F11130C1BEACD0BAF612");
    __g.infoTree.hide("A3B755BF43736C15455300A0FB44761F");
    __g.infoTree.hide("8B6725594EA91CCA8431338C12C4399F");
    drawGsGtData(0);
  } else {
    __g.infoTree.show("8C6F67F84F71F11130C1BEACD0BAF612");
    __g.infoTree.show("A3B755BF43736C15455300A0FB44761F");
    __g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
    drawGsGtData(1);
  }
  // addLine("规划片区");
  // const res = await __g.polygon3d.get(polygon3d.value[0].id);
  // if (res.resultMessage == "OK") {
  //   await __g.polyline.show(lineId.value.map((item) => item.id));
  //   await __g.marker3d.show(markerId.value.map((item) => item.id));
  //   await __g.polygon3d.show(polygon3d.value.map((item) => item.id));
  //   return;
  // }
  // __g.camera.set(
  //   498122.95125,
  //   4315668.141875,
  //   20185.36375,
  //   -84.405487,
  //   -92.408638,
  //   0.000053
  // );
  // await __g.polyline.add(lineId.value);
  // await __g.marker3d.add(markerId.value);
  // await __g.polygon3d.add(polygon3d.value);
};

const addLine = async (name) => {
  // 规划片区
  projectShpData.features.forEach((item, index) => {
    item.geometry.coordinates[0][0].forEach((i) => {
      i[2] = 3;
    });
    const o = {
      id: `shp_line_new_${name}_${index}`, //折线唯一标识id
      coordinates: item.geometry.coordinates[0][0], //构成折线的坐标点数组
      // range: [4600, 19000], //可视范围：[近裁距离, 远裁距离]，取值范围: [任意负值, 任意正值]
      range: [1, 6900000], //3D标注的可视距离范围：[min,max]，单位：米
      color: "#0070f9", //折线颜色
      style: 6, //折线样式 参考样式枚举：PolylineStyle
      thickness: 110, //折线宽度
      intensity: 0.2, //亮度
      flowRate: 0.2, //流速
      tiling: 0.9, //材质贴图平铺比例
      shape: 0, //折线类型 0：直线， 1：曲线
      depthTest: false, //是否做深度检测
    };

    const polygon3dO = {
      id: `shp_Polygon_PlanningArea_${name}_${index}`,
      coordinates: item.geometry.coordinates[0][0],
      coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
      color: color[item.properties.MC], //颜色值
      height: 300, //3D多边形的高度
      intensity: 0.1, //亮度
      style: 1, //3DPolygon的样式 请参照API开发文档选取枚举
      tillingX: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图横向平铺
      tillingY: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图纵向平铺
      generateTop: true, //是否生成顶面
      generateSide: true, //是否生成侧面
      generateBottom: false, //是否生成底面
    };
    const polygon3dBorder = {
      id: `shp_Polygon_border_PlanningArea_${name}_${index}`,
      coordinates: item.geometry.coordinates[0][0],
      coordinateType: 0, //坐标系类型，取值范围：0为Projection类型，1为WGS84类型，2为火星坐标系(GCJ02)，3为百度坐标系(BD09)，默认值：0
      color: color[item.properties.MC], //颜色值
      height: 500, //3D多边形的高度
      intensity: 0.01, //亮度
      style: 3, //3DPolygon的样式 请参照API开发文档选取枚举
      tillingX: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图横向平铺
      tillingY: 0, //可选，仅当3DPolygon的样式支持贴图显示，贴图纵向平铺
      generateTop: false, //是否生成顶面
      generateSide: true, //是否生成侧面
      generateBottom: false, //是否生成底面
    };

    const marker3d = {
      id: "marker3d_" + `_${name}_${index}_${item.properties.MC}`,
      groupId: item.properties.MC,
      text: item.properties.MC, //3D标注显示文字
      textSize: 92, //3D标注显示文字大小
      textOutlineSize: 3, //3D标注显示文字轮廓大小
      // 'textColor': '#000',//3D标注显示文字颜色
      // 'textOutlineColor': '#F00',// 3D标注显示文字轮廓颜色
      textColor: "#fff", //3D标注显示文字颜色
      textOutlineColor: "#fff", // 3D标注显示文字轮廓颜色
      // 'textFixed': false,// 3D标注显示文字是否固定文本朝向
      textFixed: true, // 3D标注显示文字是否固定文本朝向
      textVisible: true, //3D标注显示文字是否显示文本
      textLocation: [0, 0, -200], // 文字位置
      textRotation: [90, 90, 0], // 文字旋转
      textScale: [140, 140, 140], // 文字缩放
      pointName: "", //3D标注展示的特效名称
      pointVisible: false, //3D标注是否显示
      pointScale: 400, //3D标注整体缩放比例
      // 'pointScale': 100,//3D标注整体缩放比例
      coordinate: coordinate[item.properties.MC]
        ? coordinate[item.properties.MC]
        : getPolygonCenter(item.geometry.coordinates[0][0]), //3D标注的坐标位置 注意：若坐标Z设置高度为0时 autoHeight=true则会显示在物体上方
      // 'coordinate': getPolygonCenter(item.geometry.coordinates[0][0]), //3D标注的坐标位置 注意：若坐标Z设置高度为0时 autoHeight=true则会显示在物体上方
      coordinateType: 0, //坐标系类型
      // 'range': [4600, 19000], //3D标注的可视距离范围：[min,max]，单位：米
      range: [1, 6900000], //3D标注的可视距离范围：[min,max]，单位：米
      autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
    };

    prjLineId.value.push(o);
    prjMarkerId.value.push(marker3d);
    prjPolygon3d.value.push(polygon3dO, polygon3dBorder);
  });
  // 三县
  countyShpData.features.forEach((item, index) => {
    item.geometry.coordinates[0][0].forEach((i) => {
      i[2] = 3;
    });
    const o = {
      id: `shp_line_new_${name}_${index}_${item.properties.MC}`, //折线唯一标识id
      coordinates: item.geometry.coordinates[0][0], //构成折线的坐标点数组
      // range: [19000, 69000], //可视范围：[近裁距离, 远裁距离]，取值范围: [任意负值, 任意正值]
      range: [1, 6900000], //3D标注的可视距离范围：[min,max]，单位：米
      color: "#3EAEBE", //折线颜色
      style: PolylineStyle.Normal, //折线样式 参考样式枚举：PolylineStyle
      // style: 1, //折线样式 参考样式枚举：PolylineStyle
      thickness: 150, //折线宽度
      intensity: 1, //亮度
      flowRate: 0.2, //流速
      tiling: 0.9, //材质贴图平铺比例
      shape: 0, //折线类型 0：直线， 1：曲线
      depthTest: true, //是否做深度检测
    };

    let o1 = {
      id: `shp_line_${name}_${index}_${item.properties.MC}` + "_Beam", //折线唯一标识id
      coordinates: item.geometry.coordinates[0][0], //构成折线的坐标点数组
      // range: [19000, 69000], //可视范围：[近裁距离, 远裁距离]，取值范围: [任意负值, 任意正值]
      range: [1, 6900000], //3D标注的可视距离范围：[min,max]，单位：米
      // color: [0, 0, 1, 1],//折线颜色
      color: "#3EAEBE", //折线颜色
      style: PolylineStyle.Beam, //折线样式 参考样式枚举：PolylineStyle
      thickness: 150, //折线宽度
      intensity: 50, //亮度
      // flowRate: 0.3, //流速
      flowRate: 0.1, //流速
      tiling: 0, //材质贴图平铺比例
      shape: 0, //折线类型 0：直线， 1：曲线
      depthTest: true, //是否做深度检测
    };

    let marker3d = {
      id: "county_marker3d_" + `_${name}_${index}_${item.properties.MC}`,
      groupId: item.properties.MC,
      text: item.properties.MC, //3D标注显示文字
      textSize: 72, //3D标注显示文字大小
      textColor: "#f1d70a", //3D标注显示文字颜色
      textOutlineSize: 3, //3D标注显示文字轮廓大小
      textOutlineColor: Color.Black, // 3D标注显示文字轮廓颜色
      textFixed: true, // 3D标注显示文字是否固定文本朝向
      textVisible: true, //3D标注显示文字是否显示文本
      textLocation: [0, 0, 1000], // 文字位置
      textRotation: [90, 90, 0], // 文字旋转
      textScale: [100, 100, 100], // 文字缩放
      pointName: "", //3D标注展示的特效名称
      pointVisible: false, //3D标注是否显示
      pointScale: 1300, //3D标注整体缩放比例
      coordinate: countyCoordinate[item.properties.MC]
        ? countyCoordinate[item.properties.MC]
        : getPolygonCenter(item.geometry.coordinates[0][0]), //3D标注的坐标位置 注意：若坐标Z设置高度为0时 autoHeight=true则会显示在物体上方
      coordinateType: 0, //坐标系类型
      range: [1, 6900000], //3D标注的可视距离范围：[min,max]，单位：米
      autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
    };
    // ctyLineId.value.push(o, o1);
    ctyLineId.value.push(o);
    ctyMarkerId.value.push(marker3d);
  });

  markerId.value.push(...prjMarkerId.value, ...ctyMarkerId.value);
  lineId.value.push(...prjLineId.value, ...ctyLineId.value);
  polygon3d.value.push(...prjPolygon3d.value, ...ctyPolygon3d.value);
};

//区域中心点
const getPolygonCenter = (polygon) => {
  let totalArea = 0;
  let centerX = 0;
  let centerY = 0;

  // 计算重心
  for (let i = 0; i < polygon.length; i++) {
    centerX += polygon[i][0];
    centerY += polygon[i][1];
  }
  centerX /= polygon.length;
  centerY /= polygon.length;

  // 计算面积和重心坐标
  for (let i = 0; i < polygon.length; i++) {
    const x1 = polygon[i][0];
    const y1 = polygon[i][1];
    const x2 = polygon[(i + 1) % polygon.length][0];
    const y2 = polygon[(i + 1) % polygon.length][1];
    const area = (x1 * y2 - x2 * y1) / 2;
    totalArea += area;
    centerX += (area * (x1 + x2)) / 3;
    centerY += (area * (y1 + y2)) / 3;
  }

  // 计算中心点坐标
  centerX /= totalArea;
  centerY /= totalArea;

  return [centerX, centerY, 1000];
};

// 高度监听
let timer = null;
export const hideMarker = async function (e) {
  if (
    [
      "CameraStopMove",
      "CameraStartMove",
      "CameraMoving",
      "CameraChanged",
    ].includes(e.eventtype) &&
    !timer
  ) {
    const res = await __g.camera.get();
    // const lay = await __g.infoTree.get()
    // console.log(res.camera[2],routerName.value)
    //高度控制
    // rangeHeight.forEach((item) => {
    //   if (res.camera[2] <= item.range[1] && res.camera[2] >= item.range[0]) {
    //     if(routerName.value!=='Task'){
    //       __g.marker3d.show(markerIdInfo.value[item.name]);
    //       __g.polygon3d.show(polygon3dIdInfo.value[item.name]);
    //       __g.polyline.show(polylineIdInfo.value[item.name]);
    //     }else{
    //       __g.marker3d.hide(markerIdInfo.value[item.name]);
    //       __g.polygon3d.hide(polygon3dIdInfo.value[item.name]);
    //       __g.polyline.hide(polylineIdInfo.value[item.name]);
    //     }
    //   } else {
    //     __g.marker3d.hide(markerIdInfo.value[item.name]);
    //     __g.polygon3d.hide(polygon3dIdInfo.value[item.name]);
    //     __g.polyline.hide(polylineIdInfo.value[item.name]);
    //   }
    // });
    if (
      routerName.value !== "Task" &&
      mapType.value !== "2D" &&
      curPage.value !== "taskDetail" &&
      curPage.value !== "sceenDetail" &&
      !canNot.value
    ) {
      // 隐藏高速标和测试路径
      if (res.camera[2] <= 25000) {
        __g.infoTree.hide("8C6F67F84F71F11130C1BEACD0BAF612");
        __g.infoTree.hide("A3B755BF43736C15455300A0FB44761F");
        __g.infoTree.hide("8B6725594EA91CCA8431338C12C4399F");
        drawGsGtData(0);
      } else {
        __g.infoTree.show("8C6F67F84F71F11130C1BEACD0BAF612");
        __g.infoTree.show("A3B755BF43736C15455300A0FB44761F");
        __g.infoTree.show("8B6725594EA91CCA8431338C12C4399F");
        drawGsGtData(1);
      }
    }
    if (routerName.value == "Task") {
      __g.infoTree.hide("8C6F67F84F71F11130C1BEACD0BAF612");
      __g.infoTree.hide("A3B755BF43736C15455300A0FB44761F");
      __g.infoTree.hide("8B6725594EA91CCA8431338C12C4399F")
    }
    // 驾驶舱路线宽度设置
    if (
      (routerName.value === "WorkCockpit" &&
      mapType.value !== "2D" &&
      curPage.value === "taskDetail")||(routerName.value == "Task"&&useScreenStore().showPicture)
      ||(routerName.value == "WorkCockpit"&&curPage.value === "sceenDetail")
    ) {
      if (roadDrawList.value?.length) {
        let g = window.__g;
        g.polyline.updateBegin();
        for (const item of roadDrawList.value) {
          if(item.drawLineList&&item.drawLineList.length>0){
            for (const line of item.drawLineList) {
              if (res.camera[2] >= 20000) {
                g.polyline.setThickness(line.id, 400);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 20000 && res.camera[2] >= 15000) {
                g.polyline.setThickness(line.id, 300);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 15000 && res.camera[2] >= 10000) {
                g.polyline.setThickness(line.id, 200);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 10000 && res.camera[2] >= 5000) {
                g.polyline.setThickness(line.id, 100);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 5000 && res.camera[2] >= 2000) {
                g.polyline.setThickness(line.id, 60);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 2000 && res.camera[2] >= 1000) {
                g.polyline.setThickness(line.id, 40);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 1000 && res.camera[2] >= 500) {
                g.polyline.setThickness(line.id, 20);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, true)
              }
              if (res.camera[2] < 500&& res.camera[2] >= 300) {
                g.polyline.setThickness(line.id, 15);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, true)
              }
              if (res.camera[2] <100) {
                g.polyline.setThickness(line.id, 10);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, true)
              }
            }
          }
          if(item.drawInfoList&&item.drawInfoList.length>0){
            for (const line of item.drawInfoList) {
              if (res.camera[2] >= 20000) {
                g.polyline.setThickness(line.id, 400);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 20000 && res.camera[2] >= 15000) {
                g.polyline.setThickness(line.id, 300);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 15000 && res.camera[2] >= 10000) {
                g.polyline.setThickness(line.id, 200);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 10000 && res.camera[2] >= 5000) {
                g.polyline.setThickness(line.id, 100);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 5000 && res.camera[2] >= 2000) {
                g.polyline.setThickness(line.id, 60);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 2000 && res.camera[2] >= 1000) {
                g.polyline.setThickness(line.id, 40);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, false)
              }
              if (res.camera[2] < 1000 && res.camera[2] >= 500) {
                g.polyline.setThickness(line.id, 20);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, true)
              }
              if (res.camera[2] < 500&& res.camera[2] >= 300) {
                g.polyline.setThickness(line.id, 15);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, true)
              }
              if (res.camera[2] <100) {
                g.polyline.setThickness(line.id, 10);
                g.polyline.setBrightness(line.id, 1)
                g.polyline.setDepth(line.id, true)
              }
            }
          }
        }
        g.polyline.updateEnd(function () {
        });
      }
    }

     // 任务三级页面路线设定
     if (routerName.value == "Task"&&sceneBol.value) {
      if (taskScreenLineList.value?.length) {
        let g = window.__g;
        g.polyline.updateBegin();
          for (const line of taskScreenLineList.value) {
            if (res.camera[2] >= 20000) {
              g.polyline.setThickness(line.id, 400);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, false)
            }
            if (res.camera[2] < 20000 && res.camera[2] >= 15000) {
              g.polyline.setThickness(line.id, 300);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, false)
            }
            if (res.camera[2] < 15000 && res.camera[2] >= 10000) {
              g.polyline.setThickness(line.id, 200);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, false)
            }
            if (res.camera[2] < 10000 && res.camera[2] >= 5000) {
              g.polyline.setThickness(line.id, 100);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, false)
            }
            if (res.camera[2] < 5000 && res.camera[2] >= 2000) {
              g.polyline.setThickness(line.id, 60);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, false)
            }
            if (res.camera[2] < 2000 && res.camera[2] >= 1000) {
              g.polyline.setThickness(line.id, 40);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, false)
            }
            if (res.camera[2] < 1000 && res.camera[2] >= 500) {
              g.polyline.setThickness(line.id, 20);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, true)
            }
            if (res.camera[2] < 500&& res.camera[2] >= 300) {
              g.polyline.setThickness(line.id, 15);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, true)
            }
            if (res.camera[2] <100) {
              g.polyline.setThickness(line.id, 10);
              g.polyline.setBrightness(line.id, 1)
              g.polyline.setDepth(line.id, true)
            }
          }
        g.polyline.updateEnd(function () {
        });
      }
    }

    timer = setTimeout(() => {
      timer = null;
    }, 100);
  }
};
const markerIdInfo = ref({
  //规划片区
  PlanningArea: computed(() => prjMarkerId.value.map((item) => item.id)),
  //三县
  County: computed(() => ctyMarkerId.value.map((item) => item.id)),
});
const polylineIdInfo = ref({
  //规划片区
  PlanningArea: computed(() => prjLineId.value.map((item) => item.id)),
  //三县
  County: computed(() => ctyLineId.value.map((item) => item.id)),
});
const polygon3dIdInfo = ref({
  //规划片区
  PlanningArea: computed(() => prjPolygon3d.value.map((item) => item.id)),
  //三县
  County: computed(() => ctyPolygon3d.value.map((item) => item.id)),
});

export const rangeHeight = [
  {
    name: "PlanningArea",
    range: [25000, 40516],
  },
  {
    name: "County",
    range: [40516, 92947.33],
  },
];
