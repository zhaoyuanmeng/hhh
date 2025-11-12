import { sessionStorage } from "@/utils/storage";
import { nextTick, computed } from "vue";
import { loadPicture } from "@/utils";
import useWorkCockpitStore from "@/store/modules/workCockpit";
import useFloorStore from "@/store/modules/floorStore";
import { hideMarker } from "@/components/SmartMap/js/map";
import useUserStore from "@/store/modules/user";
import { getCompanyBusinessFloorsDatas } from "@/api/basic/index";
import { addScenePlacePoint } from "@/components/SmartMap/js/addMarkers";
import { getDrawDataOfTask } from "@/api/workCockpit/index.js";
import $modal from "@/utils/modal";
const curPage = computed(() => useWorkCockpitStore().curPage);
const routerName = computed(() => useUserStore().routerIndex);
const taskItem = computed(() => useWorkCockpitStore().taskItem);
const canceMarkerlData = computed(() => useWorkCockpitStore().canceMarkerlData);
// 绘制所有资源数据
export const drawResourceDataAll = async (drawList, status = false) => {
  let haveGroup = drawList.filter((item) => item.groupId);
  let noGroup = drawList.filter((item) => !item.groupId);
  // 先清在添加
  let g = window.__g;
  g.marker.clear(); // 清除marker
  g.polyline.clear(); // 清除线
  g.polygon.clear(); // 清除面
  g.customObject.clear(); // 清除自定义对象
  g.polygon3d.clear(); // 清除警戒线
  g.tag.clear(); // 清除标签
  g.marker3d.clear(); // 清除marker
  g.markerLayer.clear(); // 清除markerLayer
  g.radiationPoint.clear(); // 清除所有辐射点位样式
  g.tools.stopViewshedAnalysis(); // 停止视域分析
  if (!status) {
    drawTaskScenePointAndLine(useWorkCockpitStore().taskSceneData);
    const isTask = curPage.value !== "taskDetail";
    if (isTask) {
      addScenePlacePoint(useWorkCockpitStore().taskSceneData);
    }
  }
  sessionStorage.remove("QXZS_pointLine"); // 清除缓存的线
  sessionStorage.remove("QXZS_polygon"); // 清除缓存的面
  sessionStorage.remove("QXZS_polygon3d"); // 清除缓存的多边形
  sessionStorage.remove("policeData"); // 清除缓存的警力
  sessionStorage.remove("carData"); // 清除缓存的警车
  sessionStorage.remove("AJData"); // 清除缓存的安检数据
  sessionStorage.remove("basicData"); // 清除缓存的基础数据
  sessionStorage.remove("uavData"); // 清除缓存的无人机数据
  sessionStorage.remove("BasicGSGT"); // 清除高速高铁数据
  sessionStorage.remove("lineName"); // 清除路线名称
  sessionStorage.remove("companyData"); // 企业数据
  sessionStorage.remove("businessData"); // 商业数据
  sessionStorage.remove("communityData"); // 社区数据
  sessionStorage.remove("gsgtcityData"); // 勾选的基础点位数据
  sessionStorage.remove("layerMarkers");
  // 存缓存
  if (drawList && drawList.length > 0) {
    let car = []; // 警车
    let aj = []; // 安检
    let police = []; //警力
    let lines = []; // 线路
    let basic = []; // 基础数据
    let cordon = []; // 警戒线
    let uav = []; // 无人机
    let gsgt = []; // 高速高铁
    let linesName = []; // 路线名称
    if (haveGroup?.length) {
      for (const item of haveGroup) {
        if (item.groupId === "police") {
          police.push(item.data);
        }
        if (item.groupId === "car") {
          car.push(item.data);
        }
        if (item.groupId === "basic") {
          basic.push(item.data);
        }
        if (item.groupId === "uav") {
          uav.push(item.data);
        }
        if (item.groupId === "fbaj") {
          aj.push(item.data);
        }
      }
    }
    if (noGroup?.length) {
      for (const item of noGroup) {
        if (item.data && item.data.jingdu && item.data.weidu) {
          gsgt.push(item);
        }

        if (item.type === "场景路线" || item.type === "应急路线") {
          lines.push(item.data);
          if (item.middlePoint && item.middlePoint.coordinates) {
            let obj = {
              name: item.name,
              id: item.id,
              coordinates: item.middlePoint.coordinates,
            };
            linesName.push(obj);
          }
        }

        if (item.type === "警戒线") {
          cordon.push(item.data);
        }
        if (item.type === "企业数据") {
          if (status) {
            drawCompanyBusinessCommunity("company", true, false);
          } else {
            drawCompanyBusinessCommunity("company");
          }
        }

        if (item.type === "商业数据") {
          if (status) {
            drawCompanyBusinessCommunity("business", true, false);
          } else {
            drawCompanyBusinessCommunity("business");
          }
        }

        if (item.type === "社区数据") {
          if (status) {
            drawCommunityFloors(false);
          } else {
            drawCommunityFloors(true);
          }
        }
      }
    }
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
      let cordons = cordon.map((item) => {
        return {
          ...item,
          generateTop: false,
          generateSide: true,
          generateBottom: false,
        };
      });
      sessionStorage.set("QXZS_polygon3d", cordons);
    }
    if (uav.length > 0) {
      sessionStorage.set("uavData", uav);
    }
    if (gsgt.length > 0) {
      sessionStorage.set("BasicGSGT", gsgt);
    }
    if (linesName?.length) {
      sessionStorage.set("lineName", linesName);
    }
    // 绘制数据
    let fileterNode = drawList;
    if (fileterNode?.length) {
      let markers = [];
      let markerCustomObject = []; // 警力警车模型
      let lines = [];
      let gsgtArrs = [];
      let ydsLines = [];
      let policeLine = [];
      let linesName = [];
      for (const item of fileterNode) {
        if (item.data.marker) {
          if (
            item.data.marker.groupId === "police" ||
            item.data.marker.groupId === "car" ||
            item.data.marker.groupId === "uav"
          ) {
            markerCustomObject.push(item.data);
          } else {
            markers.push(item.data.marker);
          }
        }
        if (item.data.info && Object.keys(item.data.info).length !== 0) {
          if (
            item.data.info.lineData &&
            Object.keys(item.data.info.lineData).length !== 0
          ) {
            if (item.data.info.sfkqLine) {
              ydsLines.push(item.data.info.lineData);
            }
          }
        }
        if (item.type === "场景路线" || item.type === "应急路线") {
          lines.push(item.data);
          if (item.middlePoint && item.middlePoint.coordinates) {
            let obj = {
              name: item.name,
              id: item.id,
              coordinates: item.middlePoint.coordinates,
            };
            linesName.push(obj);
          }
        }
        if (item.type === "警戒线") {
          policeLine.push(item.data);
        }
        // 高速高铁道路数据
        if (item.data.jingdu && item.data.weidu) {
          let z;
          if (item.data.gaodu) {
            z = Number(item.data.gaodu) + 0.5;
          } else {
            z = 4.5;
          }
          let o = {
            id: item.id,
            groupId: "resource",
            coordinate: [Number(item.data.jingdu), Number(item.data.weidu), z], //坐标位置
            text: item.name, //显示的文字
            imagePath: loadPicture(`./images/resource/${item.type}.png`),
            imageSize: [30, 34],
            hoverImagePath: loadPicture(`./images/resource/${item.type}.png`), // 鼠标悬停时显示的图片路径
            hoverImageSize: [30, 34], //鼠标悬停时显示的图片尺寸
          };
          gsgtArrs.push(o);
        }
      }
      // 绘制路线
      if (lines?.length) {
        let linesData = settingLineCoor(lines);
        g.polyline.add(linesData);
        hideMarker({ eventtype: "CameraStopMove" });
      }
      // 绘制路线名称
      if (linesName?.length) {
        drawLinesName(linesName, status);
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
        let g = window.__g;
        let markers = [];
        let objects = [];
        let uavs = [];
        let counter = 0;
        for (const item of markerCustomObject) {
          let marker3dObj = {
            id: item.marker.id,
            userData: item.marker.userData,
            groupId: item.marker.groupId,
            coordinate: item.marker.coordinate, //坐标位置
            coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
            text: item.marker.text, //显示的文字
            // textSize: 20,//3D标注显示文字大小
            // textColor: '#000080',//3D标注显示文字颜色
            textSize: 120, //3D标注显示文字大小
            textColor: "#000080", //3D标注显示文字颜色
            textOutlineSize: 1, //3D标注显示文字轮廓大小
            textOutlineColor: Color.Black, // 3D标注显示文字轮廓颜色
            textFixed: false, // 3D标注显示文字是否固定文本朝向
            fixedSize: true, // 默认尺寸 非近大远小
            textVisible: true, //3D标注显示文字是否显示文本
            textLocation: [0, 0, 0.1], // 文字位置
            textRotation: [0, 90, 0], // 文字旋转
            textScale: [1, 1, 1], // 文字缩放
            pointName:
              "/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A", //3D标注展示的特效名称
            pointVisible: true, //3D标注是否显示
            // pointScale: 5,//3D标注整体缩放比例
            pointScale: 1, //3D标注整体缩放比例
            range: [0, 500], //3D标注的可视距离范围：[min,max]，单位：米
            autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
            collision: true, //默认开启碰撞
          };
          markers.push(marker3dObj);
          if (item.customData) {
            let path, pak;
            if (item.marker.groupId === "police") {
              if (item.marker.userData === "交通哨") {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警";
              } else if (
                item.marker.userData === "快反力量" ||
                item.marker.userData === "机动力量"
              ) {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵";
              } else if (item.marker.userData === "固定哨") {
                pak = "@path:DTS_Library_6.1_240731.pak";
                path =
                  "/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1";
              } else {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察";
              }
            } else {
              pak = item.customData.pakFilePath;
              path = item.customData.assetPath;
            }
            item.customData.id = item.marker.id;
            item.customData.location = item.marker.coordinate;
            item.customData.pakFilePath = pak;
            item.customData.assetPath = path;
            item.customData.scale =
              item.marker.groupId === "police" ? [1.3, 1.3, 1.3] : [1, 1, 1];
            objects.push(item.customData);
          } else {
            let path, pak, localtion;
            if (item.marker.groupId === "police") {
              if (item.marker.userData === "交通哨") {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警";
              } else if (
                item.marker.userData === "快反力量" ||
                item.marker.userData === "机动力量"
              ) {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵";
              } else if (item.marker.userData === "固定哨") {
                pak = "@path:DTS_Library_6.1_240731.pak";
                path =
                  "/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1";
              } else {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察";
              }
              localtion = [0, 90, 0];
            } else if (item.marker.groupId === "car") {
              if (item.marker.userData === "船舶") {
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/船/船_3";
              } else {
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/特种车辆/警车_1";
              }
              pak = "@path:DTS_Library_6.1_240731.pak";
              localtion = [0, 0, 0];
            } else {
              path =
                "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/其他/无人机_1";
              pak = "@path:DTS_Library_6.1_240731.pak";
              localtion = [0, 0, 0];
            }
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
              scale:
                item.marker.groupId === "police" ? [1.3, 1.3, 1.3] : [1, 1, 1], //模型缩放
              isEffectRotation: true, //是否开启旋转效果
              smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
              supportAttach: true, //不支持贴画贴合
              visible: true, //模型加载后默认是否显示
            };
            objects.push(obj);
          }
          if (item.marker.groupId === "uav") {
            if (item.uavData) {
              item.uavData.coordinate = item.marker.coordinate;
              uavs.push(item.uavData);
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
              });
            }
          }
        }
        g.radiationPoint.add(uavs);
        await g.marker3d.add(markers);
        await g.customObject.add(objects);
        // 进行贴合
        let markerAndObject = [];
        // 固定哨修改
        let changeMove = [];
        markers.forEach((item, index) => {
          if (item.userData === "固定哨") {
            changeMove.push({
              id: objects[index].id,
              functionName: "动画类型",
              parameters: [{ paramType: 5, paramValue: "站立" }],
            });
          }
          markerAndObject.push({
            marker3dId: item.id, //标注id
            objectId: objects[index].id, //自定义对象id
            offset: item.groupId === "uav" ? [0, 0, 1] : [0, 0, 2], //偏移量
          });
        });
        if (markerAndObject?.length) {
          g.marker3d.setAttachCustomObject(markerAndObject);
        }
        if (changeMove?.length) {
          g.customObject.callBPFunction(changeMove);
        }
      }
      if (ydsLines?.length) {
        let linesData = settingLineCoor(ydsLines);
        g.polyline.add(linesData);
      }
      if (policeLine?.length) {
        let cordons = policeLine.map((item) => {
          return {
            ...item,
            generateTop: false,
            generateSide: true,
            generateBottom: false,
          };
        });
        let cordonData = cordons.map((item) => {
          if (item.info) {
            return { ...item };
          } else {
            return { ...item, info: { fangxian: "" } };
          }
        });
        g.polygon3d.add(cordonData);
      }
      if (gsgtArrs?.length) {
        let o = {
          id: `gsgtcity`,
          groupId: "resource",
          coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
          range: [0, 0.1, 10000, 200000],
          viewHeightRange: [0, 10000],
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
          markers: gsgtArrs,
        };
        g.markerLayer.add(o);
        sessionStorage.set("gsgtcityData", gsgtArrs);
      }
    }
  }
  // nextTick(() => {
  //   if (!status) {
  //     drawTaskScenePointAndLine(useWorkCockpitStore().taskSceneData);
  //     const isTask = curPage.value !== "taskDetail"
  //     if(isTask){
  //       addScenePlacePoint(useWorkCockpitStore().taskSceneData)
  //     }
  //     hideMarker({ eventtype: "CameraStopMove" });
  //   }
  // });
};
// 隐藏所有资源数据
export const drawResourceDataHide = async (drawList) => {
  // 添加任务路线以及任务基础点位信息
  const isTask = curPage.value !== "sceenDetail" && routerName.value !== "Task";
  let taskLine = drawList.filter((item) => item.type === "场景路线");
  let haveGroup = drawList.filter((item) => item.groupId);
  let noGroup = drawList.filter((item) => !item.groupId);
  // 先清在添加
  let g = window.__g;
  g.reset();
  // 添加任务路线以及任务基础点位信息
  if (isTask) {
    getTaskAllShowPointAndLine();
  }
  // g.polyline.clear(); // 清除线
  // g.polygon.clear(); // 清除面
  // g.customObject.clear(); // 清除自定义对象
  // g.polygon3d.clear(); // 清除警戒线
  // g.tag.clear(); // 清除标签
  // g.marker.clear(); // 清除marker
  // g.marker3d.clear();// 清除marker
  g.markerLayer.clear(); // 清除markerLayer
  // g.radiationPoint.clear(); // 清除所有辐射点位样式
  g.tools.stopViewshedAnalysis(); // 停止视域分析
  sessionStorage.remove("QXZS_pointLine"); // 清除缓存的线
  sessionStorage.remove("QXZS_polygon"); // 清除缓存的面
  sessionStorage.remove("QXZS_polygon3d"); // 清除缓存的多边形
  sessionStorage.remove("policeData"); // 清除缓存的警力
  sessionStorage.remove("carData"); // 清除缓存的警车
  sessionStorage.remove("AJData"); // 清除缓存的安检数据
  sessionStorage.remove("basicData"); // 清除缓存的基础数据
  sessionStorage.remove("uavData"); // 清除缓存的无人机数据
  sessionStorage.remove("BasicGSGT"); // 清除高速高铁数据
  sessionStorage.remove("lineName"); // 清除路线名称
  sessionStorage.remove("companyData"); // 企业数据
  sessionStorage.remove("businessData"); // 商业数据
  sessionStorage.remove("communityData");
  sessionStorage.remove("gsgtcityData"); // 勾选的基础点位数据
  sessionStorage.remove("layerMarkers");
  // 存缓存
  if (drawList && drawList.length > 0) {
    let car = []; // 警车
    let aj = []; // 安检
    let police = []; //警力
    let lines = []; // 线路
    let basic = []; // 基础数据
    let cordon = []; // 警戒线
    let uav = []; // 无人机
    let gsgt = []; // 高速高铁
    let linesName = []; // 路线名称
    if (haveGroup?.length) {
      for (const item of haveGroup) {
        if (item.groupId === "police") {
          police.push(item.data);
        }
        if (item.groupId === "car") {
          car.push(item.data);
        }
        if (item.groupId === "basic") {
          basic.push(item.data);
        }
        if (item.groupId === "uav") {
          uav.push(item.data);
        }
        if (item.groupId === "fbaj") {
          aj.push(item.data);
        }
      }
    }
    if (noGroup?.length) {
      for (const item of noGroup) {
        if (item.data && item.data.jingdu && item.data.weidu) {
          gsgt.push(item);
        }

        if (item.type === "场景路线" || item.type === "应急路线") {
          lines.push(item.data);
          if (item.middlePoint && item.middlePoint.coordinates) {
            let obj = {
              name: item.name,
              id: item.id,
              coordinates: item.middlePoint.coordinates,
            };
            linesName.push(obj);
          }
        }

        if (item.type === "警戒线") {
          cordon.push(item.data);
        }
      }
    }
    drawCompanyBusinessCommunity("company", false);
    drawCompanyBusinessCommunity("business", false);
    drawCommunityFloors(false);
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
      let cordons = cordon.map((item) => {
        return {
          ...item,
          generateTop: false,
          generateSide: true,
          generateBottom: false,
        };
      });
      sessionStorage.set("QXZS_polygon3d", cordons);
    }
    if (uav.length > 0) {
      sessionStorage.set("uavData", uav);
    }
    if (gsgt.length > 0) {
      sessionStorage.set("BasicGSGT", gsgt);
    }
    if (linesName?.length) {
      sessionStorage.set("lineName", linesName);
    }
  }
  // 添加任务路线以及任务基础点位信息
  // // const isTask = curPage.value !== "sceenDetail" && routerName.value !== "Task";
  // if (isTask) {
  //   setTimeout(()=>{
  //     // getTaskAllShowPointAndLine()
  //       // if (taskLine?.length) {
  //       //   let lines = [];
  //       //   let linesName = [];
  //       //   for (const item of taskLine) {
  //       //     lines.push(item.data);
  //       //     if (item.middlePoint && item.middlePoint.coordinates) {
  //       //       let obj = {
  //       //         name: item.name,
  //       //         id: item.id,
  //       //         coordinates: item.middlePoint.coordinates,
  //       //       };
  //       //       linesName.push(obj);
  //       //     }
  //       //   }
  //       //   drawLinesName(linesName);
  //       //   let linesData = settingLineCoor(lines);
  //       //   g.polyline.add(linesData);
  //       // }
  //       // drawTaskScenePointAndLine(useWorkCockpitStore().taskSceneData);
  //       // if(curPage.value !== "taskDetail" ){
  //       //   addScenePlacePoint(useWorkCockpitStore().taskSceneData)
  //       // }
  //       // hideMarker({ eventtype: "CameraStopMove" });
  //   },100)
  // }
};

// 取消/选中勾选显示隐藏资源
export const drawResourceDataShowHide = async (arr, bol) => {
  $modal.loading("");
  let g = window.__g;
  if (bol) {
    let datas = sessionStorage.get("gsgtcityData");
    await g.markerLayer.delete("gsgtcity");
    if (datas?.length) {
      let gsgtArrs = datas;
      let o = {
        id: `gsgtcity`,
        groupId: "resource",
        coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
        range: [0, 0.1, 10000, 200000],
        viewHeightRange: [0, 10000],
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
        markers: gsgtArrs,
      };
      g.markerLayer.add(o);
    }
    let newData = [];
    let floornumberShow = useFloorStore().floornumberShow;
    let openFloors = useFloorStore().floornumberShow;
    let floornum = useFloorStore().floornum;
    let floorName = useFloorStore().explodebuildname;
    for (let items of arr) {
      if (items.floorNum && items.buildName) {
        if (
          floornumberShow &&
          openFloors &&
          Number(floornum) === Number(items.floorNum) &&
          floorName === items.buildName
        ) {
          newData.push(items);
        }
      } else {
        newData.push(items);
      }
    }
    let fileterNode = newData;
    if (fileterNode?.length) {
      let markers = [];
      let lines = [];
      let gsgtArrs = [];
      let ydsLines = [];
      let policeLine = [];
      let linesName = [];
      let markerCustomObject = [];
      for (const item of fileterNode) {
        if (item.data.marker) {
          if (
            item.data.marker.groupId === "police" ||
            item.data.marker.groupId === "car" ||
            item.data.marker.groupId === "uav"
          ) {
            markerCustomObject.push(item.data);
          } else {
            markers.push(item.data.marker);
          }
        }
        if (item.data.info && Object.keys(item.data.info).length !== 0) {
          if (
            item.data.info.lineData &&
            Object.keys(item.data.info.lineData).length !== 0
          ) {
            if (item.data.info.sfkqLine) {
              ydsLines.push(item.data.info.lineData);
            }
          }
        }
        if (item.type === "场景路线" || item.type === "应急路线") {
          lines.push(item.data);
          if (item.middlePoint && item.middlePoint.coordinates) {
            let obj = {
              name: item.name,
              id: item.id,
              coordinates: item.middlePoint.coordinates,
            };
            linesName.push(obj);
          }
        }
        if (item.type === "警戒线") {
          policeLine.push(item.data);
        }
        // 高速高铁道路数据
        if (item.data.jingdu && item.data.weidu) {
        }
        if (item.type === "企业数据") {
          drawCompanyBusinessCommunity("company");
        }
        if (item.type === "商业数据") {
          drawCompanyBusinessCommunity("business");
        }
        if (item.type === "社区数据") {
          drawCommunityFloors(true);
        }
      }
      // 自定义对象模型
      if (markerCustomObject?.length) {
        let g = window.__g;
        let markers = [];
        let objects = [];
        let uavs = [];
        for (const item of markerCustomObject) {
          let marker3dObj = {
            id: item.marker.id,
            userData: item.marker.userData,
            groupId: item.marker.groupId,
            coordinate: item.marker.coordinate, //坐标位置
            coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
            text: item.marker.text, //显示的文字
            // textSize: 20,//3D标注显示文字大小
            // textColor: '#000080',//3D标注显示文字颜色
            textSize: 120, //3D标注显示文字大小
            textColor: "#000080", //3D标注显示文字颜色
            textOutlineSize: 1, //3D标注显示文字轮廓大小
            textOutlineColor: Color.Black, // 3D标注显示文字轮廓颜色
            textFixed: false, // 3D标注显示文字是否固定文本朝向
            fixedSize: true, // 默认尺寸 非近大远小
            textVisible: true, //3D标注显示文字是否显示文本
            textLocation: [0, 0, 0.1], // 文字位置
            textRotation: [0, 90, 0], // 文字旋转
            textScale: [1, 1, 1], // 文字缩放
            // textScale: [1, 1, 1],// 文字缩放
            pointName:
              "/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A", //3D标注展示的特效名称
            pointVisible: true, //3D标注是否显示
            pointScale: 1, //3D标注整体缩放比例
            range: [0, 500], //3D标注的可视距离范围：[min,max]，单位：米
            autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
            collision: true, //默认开启碰撞
          };
          markers.push(marker3dObj);
          if (item.customData) {
            let path, pak;
            if (item.marker.groupId === "police") {
              if (item.marker.userData === "交通哨") {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警";
              } else if (
                item.marker.userData === "快反力量" ||
                item.marker.userData === "机动力量"
              ) {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵";
              } else if (item.marker.userData === "固定哨") {
                pak = "@path:DTS_Library_6.1_240731.pak";
                path =
                  "/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1";
              } else {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察";
              }
            } else {
              pak = item.customData.pakFilePath;
              path = item.customData.assetPath;
            }
            item.customData.id = item.marker.id;
            item.customData.location = item.marker.coordinate;
            item.customData.pakFilePath = pak;
            item.customData.assetPath = path;
            item.customData.scale =
              item.marker.groupId === "police" ? [1.3, 1.3, 1.3] : [1, 1, 1];
            objects.push(item.customData);
          } else {
            let path, pak, localtion;
            if (item.marker.groupId === "police") {
              if (item.marker.userData === "交通哨") {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/交警";
              } else if (
                item.marker.userData === "快反力量" ||
                item.marker.userData === "机动力量"
              ) {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/特种兵";
              } else if (item.marker.userData === "固定哨") {
                pak = "@path:DTS_Library_6.1_240731.pak";
                path =
                  "/JC_CustomAssets/RoleLibrary/Exhibition/动态人物/男角色_1";
              } else {
                pak = "@path:人物打包.pak";
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/人物跟随/警察";
              }
              localtion = [0, 90, 0];
            } else if (item.marker.groupId === "car") {
              if (item.marker.userData === "船舶") {
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/船/船_3";
              } else {
                path =
                  "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/特种车辆/警车_1";
              }
              pak = "@path:DTS_Library_6.1_240731.pak";
              localtion = [0, 0, 0];
            } else {
              path =
                "/JC_CustomAssets/ObjectLibrary/Exhibition/交通工具/其他/无人机_1";
              pak = "@path:DTS_Library_6.1_240731.pak";
              localtion = [0, 0, 0];
            }
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
              scale:
                item.marker.groupId === "police" ? [1.3, 1.3, 1.3] : [1, 1, 1], //模型缩放
              isEffectRotation: true, //是否开启旋转效果
              smoothMotion: 1, //1: 平滑移动，0: 跳跃移动
              supportAttach: true, //不支持贴画贴合
              visible: true, //模型加载后默认是否显示
            };
            objects.push(obj);
          }
          if (item.marker.groupId === "uav") {
            if (item.uavData) {
              item.uavData.coordinate = item.marker.coordinate;
              uavs.push(item.uavData);
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
              });
            }
          }
        }
        g.radiationPoint.add(uavs);
        await g.marker3d.add(markers);
        await g.customObject.add(objects);
        // 进行贴合
        let markerAndObject = [];
        // 固定哨修改
        let changeMove = [];
        markers.forEach((item, index) => {
          if (item.userData === "固定哨") {
            changeMove.push({
              id: objects[index].id,
              functionName: "动画类型",
              parameters: [{ paramType: 5, paramValue: "站立" }],
            });
          }
          markerAndObject.push({
            marker3dId: item.id, //标注id
            objectId: objects[index].id, //自定义对象id
            offset: item.groupId === "uav" ? [0, 0, 1] : [0, 0, 2], //偏移量
          });
        });
        if (markerAndObject?.length) {
          g.marker3d.setAttachCustomObject(markerAndObject);
        }
        if (changeMove?.length) {
          g.customObject.callBPFunction(changeMove);
        }
      }
      // 其它打点图层
      if (markers?.length) {
        // 添加marker图层
        let newData = [];
        for (const item of markers) {
          let obj = {
            id: item.id,
            groupId: item.groupId,
            coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
            range: [0, 0.1, 10000, 20000],
            viewHeightRange: [0, 10000],
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
      if (lines?.length) {
        let localLine = sessionStorage.get("QXZS_pointLine");
        let linesData = settingLineCoor(lines);
        g.polyline.add(linesData);
      }
      if (ydsLines?.length) {
        let linesData = settingLineCoor(ydsLines);
        g.polyline.add(linesData);
      }
      if (policeLine?.length) {
        let cordons = policeLine.map((item) => {
          return {
            ...item,
            generateTop: false,
            generateSide: true,
            generateBottom: false,
          };
        });
        let cordonData = cordons.map((item) => {
          if (item.info) {
            return { ...item };
          } else {
            return { ...item, info: { fangxian: "" } };
          }
        });
        g.polygon3d.add(cordonData);
      }
      if (gsgtArrs?.length) {
      }
      if (linesName?.length) {
        drawLinesName(linesName);
      }
    }
  } else {
    let datas = sessionStorage.get("gsgtcityData");
    await g.markerLayer.delete("gsgtcity");
    if (datas?.length) {
      let gsgtArrs = datas;
      let o = {
        id: `gsgtcity`,
        groupId: "resource",
        coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
        range: [0, 0.1, 10000, 200000],
        viewHeightRange: [0, 10000],
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
        markers: gsgtArrs,
      };
      g.markerLayer.add(o);
    }
    // 隐藏
    let fileterNode = arr;
    if (fileterNode?.length) {
      let markersIds = [];
      let linesIds = [];
      let policeLine = [];
      let customObjectIDs = [];
      let linesNameIds = [];
      for (const item of fileterNode) {
        if (item.data.marker) {
          markersIds.push(item.id);
        }
        if (item.data.jingdu && item.data.weidu) {
          //
        }
        if (item.data.info && Object.keys(item.data.info).length !== 0) {
          if (
            item.data.info.lineData &&
            Object.keys(item.data.info.lineData).length !== 0
          ) {
            linesIds.push(item.data.info.lineData.id);
          }
          if (
            item.data.info.custom &&
            Object.keys(item.data.info.custom).length !== 0
          ) {
            customObjectIDs.push(item.data.info.custom.id);
          }
        }
        if (item.data.uav) {
          g.radiationPoint.delete(item.data.uav.id);
        }
        if (item.type === "场景路线" || item.type === "应急路线") {
          linesIds.push(item.data.id);
          if (item.middlePoint && item.middlePoint.coordinates) {
            linesNameIds.push(`${item.id}${item.name}`);
          }
        }
        if (item.type === "警戒线") {
          policeLine.push(item.data.id);
        }
        if (item.type === "企业数据") {
          drawCompanyBusinessCommunity("company", false);
        }
        if (item.type === "商业数据") {
          drawCompanyBusinessCommunity("business", false);
        }
        if (item.type === "社区数据") {
          drawCommunityFloors(false);
        }
      }
      if (markersIds?.length) {
        g.marker.delete(markersIds);
        g.markerLayer.delete(markersIds);
        g.marker3d.delete(markersIds);
        g.customObject.delete(markersIds);
        g.radiationPoint.delete(markersIds);
      }
      if (linesIds?.length) {
        g.polyline.delete(linesIds);
      }
      if (linesNameIds?.length) {
        g.marker.delete(linesNameIds);
        g.marker3d.delete(linesNameIds);
      }
      if (policeLine?.length) {
        g.polygon3d.delete(policeLine);
      }
    }
  }
  nextTick(() => {
    $modal.closeLoading();
    hideMarker({ eventtype: "CameraStopMove" });
  });
};

// 绘制长显任务下基础点位
export const drawTaskScenePointAndLine = async (arr, bol = false) => {
  let g = window.__g;
  let taskMarker = [];
  if (arr && arr.length > 0) {
    let lineMarkers = [];
    for (const item of arr) {
      if (item.type === "2" || item.type === "3") {
        // 画驻地和现场
        if (item.pointInfoList?.length) {
          let obj = {
            id: item.id,
            name: item.sceneName,
            coor: item.pointInfoList[0].geojson.coordinates,
            type: item.type,
          };
          taskMarker.push(obj);
          // drawMarkers(item.id, item.sceneName, item.pointInfoList);
        }
      } else {
        // 画线的marker
        if (item.drawInfoList?.length) {
          const name = item.sceneName ? item.sceneName : item.name;
          // 找到路线中间数据
          const length = item.drawInfoList.length;
          const middleIndex = Math.floor((length - 1) / 2);
          let zhongjianNum = item.drawInfoList[middleIndex]; // 返回中间的元素
          let coordinates = zhongjianNum.middlePoint.coordinates;
          let obj = {
            id: item.id,
            name: item.sceneName,
            coor: coordinates,
            type: item.type,
          };
          if (!bol) {
            taskMarker.push(obj);
          }
          // 绘制线路的marker
          // let o = {
          //   id: item.drawInfoList[0].id,
          //   groupId: item.id,
          //   userData: name,
          //   coordinate: [coordinates[0], coordinates[1], 1], //坐标位置
          //   coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
          //   anchors: [-20, 62], //锚点，设置Marker的整体偏移，取值规则和imageSize设置的宽高有关，图片的左上角会对准标注点的坐标位置。示例设置规则：x=-imageSize.width/2，y=imageSize.height
          //   imageSize: [40, 62], //图片的尺寸
          //   range: [0, 150000], //可视范围
          //   imagePath: loadPicture(`./images/cockpit/gl.png`),
          //   fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
          //   text: name, //显示的文字
          //   useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
          //   textRange: [1, 15000], //文本可视范围[近裁距离, 远裁距离]
          //   textOffset: [0, -20], // 文本偏移
          //   fontSize: 8, //字体大小
          //   fontColor: Color.White, //字体颜色
          //   textBackgroundColor: "#2a4cac", //文本背景颜色    showLine: true, //标注点下方是否显示垂直牵引线
          //   autoHeight: false, // 自动判断下方是否有物体
          //   displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
          //   priority: 0, //避让优先级
          //   occlusionCull: true, //是否参与遮挡剔除
          //   clusterByImage: true,
          // };
          // lineMarkers.push(o);
        }
      }
    }
    // if (lineMarkers?.length) {
    //   if (!bol) {
    //     g.marker.add(lineMarkers);
    //   }
    // }
  }
  if (taskMarker?.length) {
    drawTaskMarkers(taskMarker);
  }
};

const drawTaskMarkers = async (arr) => {
  let g = window.__g;
  let markerarr = [];
  let newArr = arr.filter(item=>Number(item.coor[0])>110&&Number(item.coor[1])<50)
  useWorkCockpitStore().setTaskManagement(newArr) 
  for (const item of newArr) {
    let o = {
      id: item.id,
      groupId: item.id,
      userData: item.name,
      coordinate: [item.coor[0], item.coor[1], 1], //坐标位置
      coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 68],
      range: [0, 150000], //可视范围
      imageSize: [40, 68], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(
        `./imgs/${
          item.name === "白洋淀站" || item.name === "雄安站"
            ? "gt"
            : item.type === "3" || item.type === "住地基本情况"
            ? "hotel"
            : item.type === "2" || item.type === "现场基本情况"
            ? "jz"
            : "gl"
        }.png`
      ),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
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
    markerarr.push(o);
  }
  if (markerarr.length > 0) {
    g.marker.add(markerarr);
    if(canceMarkerlData.value.id&&canceMarkerlData.value.name&&curPage.value === "taskDetail"&&routerName.value !== "Task"){
      let item = canceMarkerlData.value
      let other =newArr.filter(item=>item.id!==canceMarkerlData.value.id&&item.name===canceMarkerlData.value.name)
      const text = `${item.name}\n安全隐患：${item.dangerNum}\n警力部署：${item.policeData?.onduty}\n应急力量：${item.policeData?.emergency}`;
      g.marker.setText(item.id, text);
      if(other?.length){
        let ids = other.map(item=>item.id)
        g.marker.hide(ids)
      }
    }
  }
};
const drawMarkers = async (sceneId, sceneName, arr) => {
  let g = window.__g;
  let markerarr = [];
  for (const item of arr) {
    let o = {
      id: item.id,
      groupId: sceneId,
      userData: sceneName,
      coordinate: [item.geojson.coordinates[0], item.geojson.coordinates[1], 1], //坐标位置
      coordinateType: 1, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-20, 68],
      range: [0, 150000], //可视范围
      imageSize: [40, 68], //图片的尺寸,//图片的尺寸
      imagePath: loadPicture(
        `./imgs/${
          item.name === "白洋淀站" || item.name === "雄安站"
            ? "gt"
            : item.type === "3" || item.type === "住地基本情况"
            ? "hotel"
            : "jz"
        }.png`
      ),
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.name, //显示的文字
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
    markerarr.push(o);
  }
  if (markerarr.length > 0) {
    g.marker.add(markerarr);
  }
};

// 绘制每段路线对应名称marker3d
const drawLinesName = async (arrs, type = false) => {
  let g = window.__g;
  let markerarr = [];
  for (let item of arrs) {
    let o = {
      id: `${item.id}${item.name}`,
      groupId: "linesName",
      userData: "linesName",
      coordinate: [item.coordinates[0], item.coordinates[1]], //3D标注的坐标位置 注意：若坐标Z设置高度为0时 autoHeight=true则会显示在物体上方
      coordinateType: 1, //坐标系类型
      text: item.name, //3D标注显示文字
      textSize: 120, //3D标注显示文字大小
      textColor: "#000080", //3D标注显示文字颜色
      textOutlineSize: 1, //3D标注显示文字轮廓大小
      textOutlineColor: Color.Black, // 3D标注显示文字轮廓颜色
      textFixed: false, // 3D标注显示文字是否固定文本朝向
      fixedSize: true, // 默认尺寸 非近大远小
      textVisible: true, //3D标注显示文字是否显示文本
      textLocation: [0, 0, 1], // 文字位置
      textRotation: [0, 90, 0], // 文字旋转
      textScale: [1, 1, 1], // 文字缩放
      pointName: "/JC_CustomAssets/EffectLibrary/Exhibition/Point/Point_A", //3D标注展示的特效名称
      pointVisible: true, //3D标注是否显示
      pointScale: 1, //3D标注整体缩放比例
      range: [0, 5000], //3D标注的可视距离范围：[min,max]，单位：米
      autoHeight: true, //自动判断下方是否有物体，设置正确高度，默认值：false
      collision: true, //默认开启碰撞
    };
    markerarr.push(o);
  }
  if (markerarr.length > 0) {
    g.marker3d.add(markerarr);
  }
};

//  绘制商业企业社区数据
const drawCompanyBusinessCommunity = async (
  type,
  status = true,
  types = true
) => {
  let g = window.__g;
  if (type === "company") {
    // 请求数据 -存缓存-绘制-判断是否打开楼层
    getCompanyBusinessFloorsDatas({ type: 1 }).then((res) => {
      sessionStorage.set("companyData", res.data);
      if (status) {
        drawMarkersCompanyBusiness(
          res.data.buildingData,
          res.data.enterpriseData,
          types
        ); // 企业
      } else {
        let ids = res.data.buildingData.map((item) => item.id);
        let idsD = res.data.enterpriseData.map((item) => item.roomCode);
        g.marker.delete(ids);
        g.marker.delete(idsD);
        g.marker3d.delete(idsD);
      }
    });
  }

  if (type === "business") {
    getCompanyBusinessFloorsDatas({ type: 2 }).then((res) => {
      sessionStorage.set("businessData", res.data);
      if (status) {
        drawMarkersCompanyBusiness(
          res.data.buildingData,
          res.data.enterpriseData,
          types
        ); // 商业
      } else {
        let ids = res.data.buildingData.map((item) => item.id);
        let idsD = res.data.enterpriseData.map((item) => item.roomCode);
        g.marker.delete(ids);
        g.marker.delete(idsD);
        g.marker3d.delete(idsD);
      }
    });
  }

  if (type === "community") {
  }
};

const drawMarkersCompanyBusiness = async (floors, datas, type) => {
  // 商业、企业楼栋数据 datas楼栋内商业、企业数据
  let g = window.__g;
  let markerFloors = [];
  let dataList = [];
  let concatData = [];
  let openFloors = useFloorStore().floornumberShow;
  let floornum = useFloorStore().floornum;
  let floorName = useFloorStore().explodebuildname;
  let ranges;
  let textRanges;
  if (type) {
    ranges = [0, 15000];
    textRanges = [0, 15000];
  } else {
    ranges = [0, 500];
    textRanges = [0, 500];
  }
  for (let item of floors) {
    let o = {
      id: item.id,
      groupId: "business",
      userData: item.pullOutFlag === 1 ? item.buildingName : "CANT",
      coordinate: [Number(item.x), Number(item.y), 2], //坐标位置
      coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-15, 34],
      range: ranges, //可视范围
      imageSize: [30, 34], //图片的尺寸,//图片的尺寸
      imagePath: "",
      fixedSize: true, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.buildingName, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: textRanges, //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [1, 0.62, 0.47, 0.85], //文本背景颜色
      fontSize: 12, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: true, // 自动判断下方是否有物体
      displayMode: 1, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true,
    };
    markerFloors.push(o);
  }
  
  for (const data of datas) {
    if(floorName==='会展中心'){
      if (
        openFloors &&
        Number(floornum) === Number(data.floorNumber)&&data.buildingName === "商业环廊"
      ) {
        concatData.push(data);
      }
    }else{
      if (
        openFloors &&
        Number(floornum) === Number(data.floorNumber)&&floorName===data.buildingName
      ) {
        concatData.push(data);
      }
    }
  
  }
  let nums;
  if (floorName === "综合商业6#") {
    nums = 45;
  } else if(floorName==="会展中心") {
    nums = 8;
  }else{
    nums = 37;
  }
  if (concatData?.length) {
    for (let item of concatData) {
      // let o = {
      //   id: item.roomCode,
      //   groupId: "room",
      //   userData: item.status === 128 ? "no" : "ok",
      //   coordinate: [Number(item.x), Number(item.y), 1], //坐标位置
      //   coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      //   anchors: [-15, 34],
      //   range: [0, 10000], //可视范围
      //   imageSize: [30, 34], //图片的尺寸,//图片的尺寸
      //   imagePath: "",
      //   fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      //   text: `${item.roomTitle}\n${item.tenant ? item.tenant : "待租"}`, //显示的文字
      //   useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      //   textRange: [0, 10000], //文本可视范围[近裁距离, 远裁距离]
      //   textOffset: [0, 0], // 文本偏移
      //   textBackgroundColor:
      //     item.status === 128
      //       ? [0.43, 0.5, 0.56, 0.85]
      //       : [0.23, 0.7, 0.44, 0.85], //文本背景颜色
      //   fontSize: 10, //字体大小
      //   fontOutlineSize: 1, //字体轮廓线大小
      //   fontColor: Color.White, //字体颜色
      //   fontOutlineColor: Color.Black, //字体轮廓线颜色
      //   autoHeight: true, // 自动判断下方是否有物体
      //   displayMode: 2, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      //   priority: 0, //避让优先级
      //   occlusionCull: true, //是否参与遮挡剔除
      //   clusterByImage: true,
      // };
      let marker3dObj = {
        id: item.roomCode,
        userData: item.status === 128 ? "no" : "ok",
        groupId: "room",
        coordinate: [Number(item.x), Number(item.y), Number(item.z) + nums], //坐标位置
        coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
        text: `${item.roomTitle}-${item.tenant ? item.tenant : "待租"}`, //显示的文字
        textSize: 30, //3D标注显示文字大小
        textColor: [0, 1, 0, 1], //3D标注显示文字颜色
        textOutlineSize: 1, //3D标注显示文字轮廓大小
        textOutlineColor: Color.Black, // 3D标注显示文字轮廓颜色
        textFixed: true, // 3D标注显示文字是否固定文本朝向
        fixedSize: true, // 默认尺寸 非近大远小
        textVisible: false, //3D标注显示文字是否显示文本
        textLocation: [0, 0, 0], // 文字位置
        textRotation: [0, 90, 0], // 文字旋转
        textScale: [0, 0, 0], // 文字缩放
        pointName:
          "/JC_CustomAssets/EffectLibrary/Exhibition/Sign/Sign_BQ_A_1_4", //3D标注展示的特效名称
        pointVisible: true, //3D标注是否显示
        pointScale: 2, //3D标注整体缩放比例
        range: [0, 2000], //3D标注的可视距离范围：[min,max]，单位：米
        autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
        collision: true, //默认开启碰撞
        // textSize: 20,//3D标注显示文字大小
        // textFixed: false,// 3D标注显示文字是否固定文本朝向
        // fixedSize: false,// 默认尺寸 非近大远小
        // textVisible: false,//3D标注显示文字是否显示文本
        // textLocation: [0, 0, 0],// 文字位置
        // textRotation: [0, 90, 0],// 文字旋转
        // textScale: [10, 10, 10],// 文字缩放
        // pointName: '/JC_CustomAssets/EffectLibrary/Exhibition/Sign/Sign_BQ_A_1_1',//3D标注展示的特效名称
        // pointVisible: true,//3D标注是否显示
        // pointScale: 1.3,//3D标注整体缩放比例
        // range: [1, 1000], //3D标注的可视距离范围：[min,max]，单位：米
        // autoHeight: false, //自动判断下方是否有物体，设置正确高度，默认值：false
        // collision: true //默认开启碰撞
      };
      dataList.push(marker3dObj);
      // dataList.push(o)
    }
  }
  if (markerFloors.length > 0) {
    g.marker.add(markerFloors);
  }
  if (dataList.length > 0) {
    await g.marker3d.add(dataList);
    let arrs = [];
    for (const item of dataList) {
      let text = {
        id: item.id,
        functionName: "文字",
        parameters: [
          { paramType: 5, paramValue: item.text },
          { paramType: 6, paramValue: [0, 0, 0, 1] },
          { paramType: 2, paramValue: 50 },
        ],
      };
      let bg = {
        id: item.id,
        functionName: "背景",
        parameters: [
          {
            paramType: 6,
            paramValue:
              item.userData === "no" ? [1, 0.85, 0.39, 1] : [1, 0.85, 0.39, 1],
          },
        ],
      };
      arrs.push(text);
      // arrs.push(bg)
    }
    g.marker3d.callBPFunction(arrs);
  }
};

// 绘制社区楼栋数据
const drawCommunityFloors = async (type) => {
  // 请求数据 -存缓存-绘制-判断是否打开楼层
  if (type) {
    openShepFileFloors(true);
  } else {
    openShepFileFloors(false);
  }
  sessionStorage.set("communityData", []);
};
// 打开社区楼栋shepfile
const openShepFileFloors = async (type) => {
  let g = window.__g;
  if (type) {
    await g.infoTree.show(window.shepfileCommunity); //打开楼栋的shepfile图层
    //  let datas = await g.shapeFileLayer.get('8799E55A4277C828439AD09F810F9B81')
    //  console.log(datas,'图层文件')
  } else {
    g.infoTree.hide(window.shepfileCommunity); //隐藏楼栋的shepfile
  }
};
const drawCommunityMarkers = async (communityList) => {
  let g = window.__g;
  let communityArr = [];
  for (let item of communityList) {
    let o = {
      id: item.id,
      groupId: "community",
      userData: item.buildingName,
      coordinate: [Number(item.x), Number(item.y), 2], //坐标位置
      coordinateType: 0, //默认0是投影坐标系，也可以设置为经纬度空间坐标系值为1
      anchors: [-15, 34],
      range: [0, 100000], //可视范围
      imageSize: [30, 34], //图片的尺寸,//图片的尺寸
      imagePath: "",
      fixedSize: false, //图片固定尺寸，取值范围：false 自适应，近大远小，true 固定尺寸，默认值：false
      text: item.buildingName, //显示的文字
      useTextAnimation: false, //关闭文字展开动画效果 打开会影响效率
      textRange: [1, 100000], //文本可视范围[近裁距离, 远裁距离]
      textOffset: [0, 0], // 文本偏移
      textBackgroundColor: [0, 0.5, 0.5, 0.85], //文本背景颜色
      fontSize: 8, //字体大小
      fontOutlineSize: 1, //字体轮廓线大小
      fontColor: Color.White, //字体颜色
      fontOutlineColor: Color.Black, //字体轮廓线颜色
      autoHeight: true, // 自动判断下方是否有物体
      displayMode: 1, //0：相机移动不显示，参与避让聚合;1：相机移动时显示，参与避让聚合; 2：相机移动时显示，不参与避让聚合;3：相机移动时不显示，不参与避让聚合
      priority: 0, //避让优先级
      occlusionCull: true, //是否参与遮挡剔除
      clusterByImage: true,
    };
    communityArr.push(o);
  }
  if (communityArr.length > 0) {
    g.marker.add(communityArr);
  }
};

const extractImagePath = (url) => {
  const parts = url.split("/images/");
  return parts.length > 1 ? "/images/" + parts[1] : null;
};

const settingLineCoor = (arr) => {
  arr.forEach((item, index) => {
    item.depthTest = true;
    item.coordinates.forEach((child, i) => {
      const length = child.length;
      if (length > 2) {
        child[2] = child[2] + 1;
      } else {
        child[2] = 0.5;
      }
    });
  });
  return arr;
};

// 控制相机漫游停止、飞行时候控制图标的显示类型
export const ctrlMoveToSetMarkerMode = async (bol) => {
  // bol代表true表示漫游中 false 表示停止了漫游
  let g = window.__g;
  let policeData = sessionStorage.get("policeData"); // 警力数据
  let lineNameData = sessionStorage.get("lineName"); // 路线名称
  let markerIds = [];
  let lineIds = [];
  // // 修改警力模式
  if (policeData?.length) {
    for (const item of policeData) {
      if (item.marker) {
        markerIds.push(item.marker.id);
      }
    }
  }
  if (lineNameData?.length) {
    for (const item of lineNameData) {
      lineIds.push(`${item.id}${item.name}`);
    }
  }
  if (bol) {
    g.marker.updateBegin();
    for (let i = 0; i < markerIds; i++) {
      g.marker.setRange(i, [0, 50]);
    }
    for (let i = 0; i < lineIds; i++) {
      g.marker.setRange(i, [0, 50]);
    }
    g.marker.updateEnd(function () {
      console.log("设置可见范围变小");
    });
  } else {
    g.marker.updateBegin();
    for (let i = 0; i < markerIds; i++) {
      g.marker.setRange(i, [0, 200]);
    }
    for (let i = 0; i < lineIds; i++) {
      g.marker.setRange(i, [0, 1000]);
    }
    g.marker.updateEnd(function () {
      console.log("设置可见范围变大");
    });
  }
};

export const saveBasicData = async (arr) => {
  let newData = [];
  let floornumberShow = useFloorStore().floornumberShow;
  let openFloors = useFloorStore().floornumberShow;
  let floornum = useFloorStore().floornum;
  let floorName = useFloorStore().explodebuildname;
  if (arr?.length) {
    for (let items of arr) {
      if (items.floorNum && items.buildName) {
        if (
          floornumberShow &&
          openFloors &&
          Number(floornum) === Number(items.floorNum) &&
          floorName === items.buildName
        ) {
          newData.push(items);
        }
      } else {
        newData.push(items);
      }
    }
    let fileterNode = newData;
    if (fileterNode?.length) {
      let datas = [];
      for (const item of fileterNode) {
        if (item.data.jingdu && item.data.weidu) {
          let z;
          if (item.data.gaodu) {
            z = Number(item.data.gaodu) + 0.5;
          } else {
            z = 4.5;
          }
          let o = {
            id: item.id,
            groupId: "resource",
            coordinate: [Number(item.data.jingdu), Number(item.data.weidu), z], //坐标位置
            text: item.name, //显示的文字
            imagePath: loadPicture(`./images/resource/${item.type}.png`),
            imageSize: [30, 34],
            hoverImagePath: loadPicture(`./images/resource/${item.type}.png`), // 鼠标悬停时显示的图片路径
            hoverImageSize: [30, 34], //鼠标悬停时显示的图片尺寸
          };
          datas.push(o);
        }
      }
      sessionStorage.set("gsgtcityData", datas);
    }
  } else {
    sessionStorage.set("gsgtcityData", []);
  }
};

// 根据任务id查询任务下的所有长显图标和路线

const getTaskAllShowPointAndLine = async () => {
  let g = window.__g;
  let datas = useWorkCockpitStore().taskSceneData;
  drawTaskScenePointAndLine(datas);
  let lines = [];
  datas.forEach((item, index) => {
    if (item.type === "1") {
      if (item.drawInfoList?.length) {
        item.drawInfoList.forEach((child, i) => {
          lines.push(child);
        });
      }
    }
  });
  if (lines?.length) {
    let linesArr = [];
    let linesName = [];
    for (const item of lines) {
      linesArr.push(item.data);
      if (item.middlePoint && item.middlePoint.coordinates) {
        let obj = {
          name: item.name,
          id: item.id,
          coordinates: item.middlePoint.coordinates,
        };
        linesName.push(obj);
      }
    }
    drawLinesName(linesName);
    let linesData = settingLineCoor(linesArr);
    g.polyline.add(linesData);
  }

  if (curPage.value !== "taskDetail") {
    addScenePlacePoint(datas);
  }
  hideMarker({ eventtype: "CameraStopMove" });
};
