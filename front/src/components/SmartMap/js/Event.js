/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-13 10:09:19
 * @LastEditTime: 2024-06-22 17:14:47
 * @LastEditors: Alex
 */
import { useRouter } from "vue-router";
import {
  keyBuilding,
  keyBuilding3,
} from "@/components/SmartMap/componenets/buildingExplode/markerEvent.js";
import emitter from "@/utils/emitter";
import useSettingStore from "@/store/modules/settingStore";
import useMarkerStore from "@/store/modules/markerStore";
import useFloorStore from "@/store/modules/floorStore";
import useUserStore from "@/store/modules/user";
import useScreenStore from "@/store/modules/screenStore";
import useBasicStore from "@/store/modules/basicData";
import useTaskStore from "@/store/modules/taskStore";
import { useSysToolsCimStore } from "@/store/modules/sysToolsCim";
import useCameraStore from "@/store/modules/cameraSet";
import {
  shapeFileLayerShow,
  clickePointAndAddStyle,
  closeFloors,
} from "./utils";
import { markerClick, hideFloorMarkersFun } from "./addMarkers";
import { hideMarker } from "./map";
import { ref, computed, nextTick } from "vue";
import useWorkCockpitStore from "@/store/modules/workCockpit";
import { getCommunityInfo, getCommunityFloorsData } from "@/api/basic/index";
import { ElMessage, ElMessageBox } from "element-plus";
import { ctrlMoveToSetMarkerMode } from "./resource";
const router = useRouter();
const routerName = computed(() => useUserStore().routerIndex);
const showSceneDialog= computed(()=> useWorkCockpitStore().showSceneDialog)
const OnEvent = async (event) => {
  console.log(event, "所有事件名");
  //事件类型 参考交互事件类型枚举对象
  let eventType = event.eventtype; //事件类型 参考交互事件类型枚举对象
  var layerType = event.Type; //图层类型
  var layerId = event.Id || event.ID; //图层id
  var GroupID = event.GroupID; //GroupID
  var MarkerLayeId = event.MarkerId;
  // //点击ActorId
  var objectId = event.ObjectID;
  let oId = event.OID;
  let modelName = event.ModelName;
  let propertyName = event.PropertyName;
  let name = event.Name;
  let type = event.Type;
  let userData = event.UserData;
  let objectLocation = event.MouseClickPoint; //当前点击位置
  let coordinates = event.coordinates; //绘制//平面
  let coordinate = event.Coordinate; //坐标
  let perimeter = event.Perimeter; //周长//平面
  let area = event.Area; //地面
  let startCoordinate = event.StartCoordinate; //垂直起点//直线
  let endCoordinate = event.EndCoordinate; //垂直终点//直线
  let verticalCoordinate = event.VerticalCoordinate; //垂直直角点
  let horizontalLength = event.HorizontalLength;
  let verticalHeight = event.VerticalHeight;
  let length = event.Length; //直线长度
  let g = window.__g;
  // const { eventtype, MouseClickPoint, Id, ObjectID, Type, UserData } = e
  // 工具交互使用
  if (
    eventType === "LeftMouseButtonClick" ||
    eventType === "CameraChanged" ||
    eventType === "ViewShed" ||
    eventType === "Skyline" ||
    eventType === "Sunshine" ||
    eventType === "VisibilityAnalysis" ||
    eventType === "cutFillAnalysis" ||
    eventType === "Measurement" ||
    eventType === "EditHelperFinished" ||
    eventType === "StartMoveFinished" ||
    eventType === "CameraTourStop" ||
    eventType === "CameraTourFinished"
  ) {
    useSysToolsCimStore().SET_EVENTSEALAPI(event);
  }
  switch (eventType) {
    case "LeftMouseButtonClick": // 鼠标左键点击时触发方法
      if (event.Type === "marker" && (event.GroupID === "scenePlace"||event.GroupID==="roadLineMarker")) {
        if(event.GroupID==="roadLineMarker"){
          ElMessage.warning("改点属于路线的起点或终点，无法进入场景");
        }
        return false;
      } 
      // 获取经纬度
      if (useBasicStore().locationBol) {
        let point = event.MouseClickPoint;
        let location = await g.coord.pcs2gcs(point);
        if (location.result === 0) {
          let msg = `经度：${location.coordinates[0][0]}  纬度：${location.coordinates[0][1]}`;
          ElMessageBox.confirm(msg, "点击经纬度", {
            confirmButtonText: "复制",
            cancelButtonText: "取消",
          })
            .then(async () => {
              try {
                // 尝试将内容写入剪贴板
                await navigator.clipboard.writeText(
                  `${location.coordinates[0][0]},${location.coordinates[0][1]}`
                );
                ElMessage("内容已复制");
                useBasicStore().set_locationBol(false);
              } catch (err) {
                // 如果出错，则捕获异常
                ElMessage.error("复制失败，请手动复制");
              }
            })
            .catch(() => {
              useBasicStore().set_locationBol(false);
            });
        }
      }
      // 点位纠偏
      if (useWorkCockpitStore().changeLocation) {
        let point = event.MouseClickPoint;
        let location = await g.coord.pcs2gcs(point);
        if (location.result === 0) {
          let obj = {
            jingdu: location.coordinates[0][0], // 经度
            weidu: location.coordinates[0][1], // 纬度
            height: location.coordinates[0][2], // 高度
          };
          useWorkCockpitStore().set_newLocation(obj);
        } else {
          ElMessage.warning("点位获取有误");
        }
      }
      // 标会数据点击地图获取新增点位信息
      if (useBasicStore().addPoint) {
        useBasicStore().setEventMapData(event);
      }
      useBasicStore().setRightPanel(true);
      // 漫游弹框 漫游点击点位
      if (
        (useScreenStore().showPlay ||
          useWorkCockpitStore().curPage === "taskPlay") &&
        (event.GroupID === "basic" ||
          event.GroupID === "police" ||
          event.GroupID === "fbaj" ||
          event.GroupID === "car")
      ) {
        useWorkCockpitStore().setShowMarkerInfo(true);
        useWorkCockpitStore().setEventMapData(event);
      }
      // 驾驶舱点击点位
      if (event.GroupID === "resource") {
        useWorkCockpitStore().setShowMarkerInfo(true);
        if (layerType === "MarkerLayer") {
          useWorkCockpitStore().setEventMapData({
            GroupID: event.GroupID,
            Id: event.MarkerId,
          });
        } else {
          useWorkCockpitStore().setEventMapData(event);
        }
      } else {
        // 任务一张图点击点位
        if (useScreenStore().showPicture) {
          useWorkCockpitStore().setEventMapData(event);
          if (
            GroupID === "police" ||
            GroupID === "car" ||
            GroupID === "basic" ||
            GroupID === "uav" ||
            GroupID === "fbaj"
          ) {
            useWorkCockpitStore().set_showPoliceBasic(true);
          }
        }
        // 驾驶舱点击其他要素
        if (
          event.Type === "marker" ||
          event.Type === "MarkerLayer" ||
          event.Type === "CustomObj" ||
          event.Type === "marker3d"
        ) {
          if (
            GroupID !== "basicMarkerLayer" &&
            GroupID !== "linesName" &&
            GroupID !== "gs" &&
            GroupID !== "gt" &&
            GroupID !== "community" &&
            GroupID !== "room" &&
            GroupID !== "business" &&
            (useWorkCockpitStore().curPage === "taskDetail" ||
              useWorkCockpitStore().curPage === "sceenDetail")
          ) {
            useWorkCockpitStore().setEventMapData(event);
            if (
              GroupID === "police" ||
              GroupID === "car" ||
              GroupID === "basic" ||
              GroupID === "uav" ||
              GroupID === "fbaj"
            ) {
              useWorkCockpitStore().set_showPoliceBasic(true);
            } else {
              useTaskStore().SET_SCREENMODALINFO({ id: event.GroupID });
              useWorkCockpitStore()
                .getSceneData(event.UserData)
                .then(() => {
                  if (useWorkCockpitStore().sceneList?.length <= 1) {
                    useWorkCockpitStore().setCurPage("");
                    useSettingStore().setShowTool(true);
                    useWorkCockpitStore().openHouse(false);
                    nextTick(() => {
                      useWorkCockpitStore().setCurPage("sceenDetail");
                    });
                  } else {
                    // console.log(useWorkCockpitStore().curPage)
                    // let page = useWorkCockpitStore().curPage
                    // if(page==='sceenDetail'){
                    //   useWorkCockpitStore().setShowSceneDialog(true);
                    // }else{
                      useWorkCockpitStore().setCurPage("taskDetail");
                      useWorkCockpitStore().openHouse(false);
                      useWorkCockpitStore().setCancelData({});
                      useWorkCockpitStore().setShowSceneDialog(true);
                    // }
                  }
                });
            }
          } else {
            if (
              GroupID !== "basicMarkerLayer" &&
              GroupID !== "linesName" &&
              GroupID !== "gs" &&
              GroupID !== "gt" &&
              GroupID !== "community" &&
              GroupID !== "room" &&
              GroupID !== "business" &&
              routerName.value === "Task"
            ) {
              emitter.emit("ToTaskDetails", event);
            }
          }
        }
      }
      if (
        layerType === "marker" &&
        (userData === "会展中心" ||
          userData === "国酒会议中心" ||
          userData === "国际酒店" ||
          userData === "公务接待楼" ||
          userData === "办公业态3#" ||
          userData === "办公业态4#" ||
          userData === "办公业态5#" ||
          userData === "综合商业6#" ||
          userData === "雄安站" ||
          userData === "计算中心" ||
          userData === "中国星网" ||
          userData === "中国中化大厦" ||
          userData === "中国电信" ||
          userData === "规划展示中心" ||
          userData === "雄安会展中心酒店")
      ) {
        // 设置工具栏显示
        if (!useScreenStore().showPicture) {
          useSettingStore().setShowTool(true);
        }
        closeFloors(); // 先关闭楼层
        // 隐藏其余楼层炸开数据
         hideFloorMarkersFun();
        setTimeout(() => {
          if(!showSceneDialog.value){
            keyBuilding(userData);
            useFloorStore().setOpenFloor(true);
            useFloorStore().set_floorsId(layerId);
            // 隐藏楼栋marker标签
            if (layerId) {
              window.__g.marker.hide(layerId);
            }
            useBasicStore().setRightPanel(false);
          }
        }, 800);
      }else{
        // if(GroupID !== "room"){
        //    closeFloors(); // 先关闭楼层
        // }
      }
      // 楼层不能抽出的
      if (
        layerType === "marker" &&
        GroupID === "business" &&
        userData === "CANT"
      ) {
        closeFloors(); // 先关闭楼层
        // 隐藏其余楼层炸开数据
        hideFloorMarkersFun();
        keyBuilding3(layerId);
      }
      // 楼层商户的详细信息
      if (
        (layerType === "marker" || layerType === "marker3d") &&
        GroupID === "room"
      ) {
        // 设置工具栏false
        if (userData === "no") {
          ElMessage.warning("未签约");
        } else {
          useTaskStore().set_roomCode(layerId);
          useTaskStore().set_attractBol(true);
        }
      }
      // 社区弹框详情
      if (layerType === "marker" && GroupID === "community") {
        let params = {
          buildingId: layerId,
        };
        getCommunityInfo(params).then((res) => {
          useTaskStore().set_communityInfo(res.data);
          useTaskStore().set_communityBol(true);
        });
      }
      // 市民中心
      // if (layerType === "marker3d" && layerId === window.keybuildName1) {
      //   g.camera.set(
      //     492075.363281,
      //     4323555.916563,
      //     171.621699,
      //     -29.448523,
      //     -96.42614,
      //     0.5
      //   );
      // }
      if (
        layerType === "marker" &&
        (GroupID === "gs" ||
          GroupID === "gt" ||
          GroupID === "xc" ||
          GroupID === "zd" ||
          GroupID === "city") &&
        routerName.value !== "WorkCockpit"
      ) {
        let cam = await g.marker.get(layerId);
        if (cam.data[0].text === "会展中心" && layerType === "marker") {
        } else {
          clickePointAndAddStyle(cam.data[0].id, cam.data[0].coordinate);
        }
        useWorkCockpitStore().setShowMarkerInfo(true);
        useWorkCockpitStore().setEventMapData(event);
        useMarkerStore().setMarkerInfo(cam.data[0]);
        useMarkerStore().setMarkerId(layerId);
        g.marker.focus(layerId, 150, 0.1);
      }
      // 新增的markerLayer
      if (
        layerType === "MarkerLayer" &&
        (GroupID === "basicMarkerLayer" ||
          GroupID === "xc" ||
          GroupID === "zd") &&
        routerName.value !== "WorkCockpit"
      ) {
        useWorkCockpitStore().setShowMarkerInfo(true);
        useWorkCockpitStore().setEventMapData({
          GroupID: "gs",
          Id: MarkerLayeId,
        });
        useMarkerStore().setMarkerInfo(event);
        useMarkerStore().setMarkerId(MarkerLayeId);
        // g.markerLayer.focusByMarkerId(layerId, MarkerLayeId,10, 0.1);
      }
      if (
        layerType === "shapefilelayer" &&
        layerId === window.shepfileCommunity
      ) {
        // shapeFileLayerShow(layerId);
        let params = {
          buildingName: event.Fields.name2,
        };
        getCommunityFloorsData(params).then((res) => {
          if (res.data) {
            useTaskStore().set_communityInfo(res.data);
            useTaskStore().set_communityBol(true);
          } else {
            ElMessage.warning("暂无数据");
          }
        });
      }
      // if (
      //   layerType === "TileLayer" &&
      //   (propertyName === "高速线" || propertyName === "铁路矢量")
      // ) {
      //   markerClick(layerId, propertyName);
      // }
      break;
    // 可视域分析结束回调事件
    case "ViewShed":
      console.log("触发事件类型：结束透视，eventType：" + eventType, event);
      break;
    //鼠标悬停时触发此事件
    //注意需提前开启鼠标拾取：fdapi.settings.setMousePickMask(7);
    case "MouseHovered":
      // console.log('触发事件类型：鼠标悬停，eventType：' + eventType);
      break;
    //鼠标移动时触发此事件
    //注意需提前开启鼠标拾取：fdapi.settings.setMousePickMask(7);
    case "MouseMoved":
      // console.log('触发事件类型：鼠标移动，eventType：' + eventType);
      break;
    case "CameraStartMove":
      // console.log('触发事件类型：相机开始飞行，eventType：' + eventType);
      break;
    //相机正在移动时触发此监听事件
    //注意需先开启事件：fdapi.settings.setEnableCameraMovingEvent(true);
    case "CameraMoving":
      // console.log('触发事件类型：相机正在飞行，eventType：' + eventType);
      break;
    //相机停止移动时触发此监听事件
    //注意需先开启事件：fdapi.settings.setEnableCameraMovingEvent(true);
    case "CameraStopMove":
      // ctrlMoveToSetMarkerMode(false)
      // console.log('触发事件类型：相机停止飞行，eventType：' + eventType);
      break;
    //对象执行focus()或相机执行set()/lookAt()/lookAtBBox()方法时触发
    case "CameraChanged":
      // console.log('触发事件类型：相机位置发生变化，eventType：' + eventType);
      break;
    //进入测量模式后，测量完成时触发此事件并返回测量结果
    case "Measurement":
      // console.log('触发事件类型：测量完成，eventType：' + eventType);
      break;
    //播放导览结束触发此事件
    //fdapi.camera.playAnimation(id)和导览对象播放导览结束__g.cameraTour.play(id)均触发此事件
    case "CameraTourStop":
      // console.log("停止漫游1");
      // ctrlMoveToSetMarkerMode(false)
      // useTaskStore().SET_ENDPLAY(true)
      break;
    case "StartMoveFinished":
      break;
    case "CameraTourFinished":
      // console.log("漫游完成");
      // ctrlMoveToSetMarkerMode(false)
      // console.log(
      //   "触发事件类型：播放导览结束，eventType：",
      //   useCameraStore().sceneFrame
      // );
      // let location = useCameraStore().sceneFrame;
      // window.__g.camera.set(
      //   location.location[0],
      //   location.location[1],
      //   location.location[2],
      //   location.rotation[0],
      //   location.rotation[1],
      //   0.5
      // );
      // useScreenStore().set_showPlay(false);
      // useWorkCockpitStore().setCurPage('overview')
      // window.__g.cameraTour.stop();
      useTaskStore().SET_ENDPLAY(true);
      break;
    default:
      break;
  }
  hideMarker(event);
};

export default OnEvent;
