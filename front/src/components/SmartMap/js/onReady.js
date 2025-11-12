/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-13 10:09:19
 * @LastEditTime: 2024-06-20 15:53:31
 * @LastEditors: Alex
 */
// import { marker_BuildName } from "@/utils/buildnameMaker.js";
// import {unattended} from "@/utils/idleModel.js"
// import { sessionStorage } from '@/utils/storage'
import {addCommunityMerchant} from '@/components/SmartMap/js/addMarkers'
import mapConfig from '../config'
const OnReady = async () => {
  let g = window.__g;
   g.cameraTour.stop();// 停止漫游
  //初始化、不传参等同于1;1：清除所有接口添加的对象 2：重置用户设置 4：复位相机到初始位置
  await g.reset(1 | 2 |4);
  let params = await g.weather.getParams()
  g.customObject.clear();
  g.tag.clear() // 清楚所以Tag
   g.marker.clear() // 清除所有marker
   g.marker3d.clear() // 清除所有marker
   g.markerLayer.clear();// 清除markerLayer 
   g.radiationPoint.clear(); // 清除所有辐射点位样式
   sessionStorage.clear();
   console.log('重置后的事件初始化11111')
  // 隐藏界面UI
  // g.settings.setMainUIVisibility(false);
  // 隐藏指北针
  // g.settings.setCampassVisible(false);
  //设置黑夜模式
  // g.weather.setDarkMode(false);
  //相机定位到项目初始位置
  // g.camera.set(...mapConfig.initLocaltion, 2);
  //添加楼宇标号
  // marker_BuildName();
  //开启监测无人值守模式
  // if(window.deviceType=="ispc"){
  //   unattended();
  // }
  g.settings.setEnableCameraMovingEvent(true);
  addCommunityMerchant()
};
export default OnReady;
