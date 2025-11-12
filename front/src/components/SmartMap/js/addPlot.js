import { sessionStorage } from "@/utils/storage";
import useEmergencyStore from "@/store/modules/emergencyPlan";
import useScreenStore from "@/store/modules/screenStore";
import { addCommunityMerchant } from "@/components/SmartMap/js/addMarkers";
// 添加标绘数据
export async function addPlot(data) {
  var g = window.__g;
  let lines = data.line || [];
  let areas = data.area || [];
  let moreAreas = data.moreArea || [];
  let markers = data.marker || [];
  if (lines && lines.length > 0) {
    lines.forEach((item) => {
      g.polyline.add(item);
    });
  }
  if (areas && areas.length > 0) {
    areas.forEach((item1) => {
      g.polygon.add(item1);
    });
  }

  if (moreAreas && moreAreas.length > 0) {
    moreAreas.forEach((item2) => {
      g.polygon3d.add(item2);
    });
  }

  if (markers && markers.length > 0) {
    for (const item of markers) {
      await g.marker.add(item);
    }
  }
}

// 清除标绘数据
export async function clearPlot(data) {
  let g = window.__g;
  await g.polyline.clear(); // 清除线
  await g.polygon.clear(); // 清除面
  await g.tag.clear(); // 清除标签
  await g.marker.deleteByGroupId("3dMarkers");
  await g.marker.deleteByGroupId("police");
  await g.marker.deleteByGroupId("car");
  let moreAreas = data.moreArea || sessionStorage.get("QXZS_polygon3d");
  let Markers = data.marker || sessionStorage.get("QXZS_3dMarker");
  if (moreAreas && moreAreas.length > 0) {
    moreAreas.forEach((item2) => {
      g.polygon3d.delete(item2.id);
    });
  }
  sessionStorage.remove("QXZS_pointLine");
  sessionStorage.remove("QXZS_polygon");
  sessionStorage.remove("QXZS_polygon3d");
  sessionStorage.remove("QXZS_3dMarker");
}

//初始化工具栏以及标绘数据

export async function initTools() {
  let moreAreas = sessionStorage.get("QXZS_polygon3d"); // 三维多边形
  let markers = sessionStorage.get("QXZS_3dMarker"); // 三维标数据
  let g = window.__g;
  g.polyline.clear(); // 清除线
  g.polygon.clear(); // 清除面
  g.tag.clear(); // 清除标签
  g.marker.clear(); // 清除marker
  g.marker3d.clear();
  g.markerLayer.clear(); // 清除markerLayer
  g.customObject.clear(); // 清除自定义对象
  g.radiationPoint.clear(); // 清除所有辐射点位样式
  g.marker.deleteByGroupId("3dMarkers");
  g.marker.deleteByGroupId("police");
  g.marker.deleteByGroupId("car");
  if (moreAreas && moreAreas.length > 0) {
    moreAreas.forEach((item2) => {
      g.polygon3d.delete(item2.id);
    });
  }
  sessionStorage.remove("QXZS_pointLine");
  sessionStorage.remove("QXZS_polygon");
  sessionStorage.remove("QXZS_polygon3d");
  sessionStorage.remove("QXZS_3dMarker");
  useEmergencyStore().set_showEmergencyPlan(false); // 取消应急预案弹框
  useScreenStore().set_showPicture(false);
  useScreenStore().set_showPlay(false);
  addCommunityMerchant();
}

// 路线设定自动漫游
export const setAutoCameraTour = async (list,type) => {
  // 原始数据
  let curOa = {
    id: "camera" + Date.now(),
    name: type===1?"第一视角自动漫游":"第三视角自动漫游",
    time: 0,
    keyFrames: [],
  };
  // 执行合并
  const mergedArray = processCoordinates(list);
  console.log(mergedArray)
    // 方法一：使用reduce快速获取最大值
const maxZ = mergedArray.reduce((max, point) => 
  Math.max(max, point[2]), -Infinity
);
  const lineLength = Math.floor(calculate3DLength(mergedArray))
  let threeData = getEquidistantPoints(mergedArray,Math.floor(lineLength/100));
  threeData[0] = mergedArray[0];
  const dividedPoints = type===1?mergedArray:threeData
  if (dividedPoints?.length) {
    curOa.time = dividedPoints.length * 3 - 3;
    for (const [i, item] of dividedPoints.entries()) {
      let height = type===1?3:36
      // let rotations= type===3?[ -50,90,0]:null
      let obj = {
        index: i,
        time: 3 * i,
        location: [item[0], item[1], item[2] + height],
        // rotation:rotations
      };
      curOa.keyFrames.push(obj);
    }
  }
  return curOa;
};

// 合并处理器
const processCoordinates = (data) => {
  // 1. 扁平化数组
  const flattened = data.flat();

  // 2. 精度标准化
  return flattened.map(
    (point) => point.map((coord) => Number(coord.toFixed(6))) // 保留6位小数
  );
};

const getEquidistantPoints = (coords, segments) => {
  // 1. 计算相邻点间距及累积距离
  const distances = [];
  let totalDistance = 0;

  for (let i = 1; i < coords.length; i++) {
    const [x1, y1] = coords[i - 1];
    const [x2, y2] = coords[i];
    const dx = x2 - x1;
    const dy = y2 - y1;
    const dist = Math.sqrt(dx * dx + dy * dy);

    distances.push({
      start: coords[i - 1],
      end: coords[i],
      length: dist,
      accum: (totalDistance += dist), // 累积距离
    });
  }

  // 2. 计算等分参数
  const interval = totalDistance / segments;
  const result = [coords]; // 包含起点
  let currentDist = 0;

  // 3. 生成等分点
  for (let i = 1; i <= segments; i++) {
    const targetDist = i * interval;

    // 查找目标距离所在的线段
    const segment = distances.find((d) => d.accum >= targetDist);
    if (!segment) break;

    // 计算插值比例
    const segmentStartDist = segment.accum - segment.length;
    const t = (targetDist - segmentStartDist) / segment.length;

    // 三维线性插值
    const [x1, y1, z1] = segment.start;
    const [x2, y2, z2] = segment.end;
    const x = x1 + t * (x2 - x1);
    const y = y1 + t * (y2 - y1);
    const z = z1 + t * (z2 - z1);

    result.push([x, y, z]);
  }

  // 精度处理 (保留6位小数)
  return result.map((p) => p.map((v) => Number(Number(v).toFixed(6))));
};
// 三维空间距离计算（包含Z轴）
const calculate3DLength = (points) => {
  let total = 0;
  for (let i = 0; i < points.length - 1; i++) {
    const [x1, y1, z1] = points[i];
    const [x2, y2, z2] = points[i + 1];
    total += Math.hypot(x2 - x1, y2 - y1, z2 - z1);
  }
  return total;
};
