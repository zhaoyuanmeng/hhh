/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-13 22:27:15
 * @LastEditTime: 2024-06-22 20:13:22
 * @LastEditors: Alex
 */
import { defineStore } from "pinia";
const useSettingStore = defineStore("setting", {
  state: () => ({
    allLayer: [],// 所有需隐藏的id
    gsLayer: [],// 高速图层id
    showTask: true,// 任务列表
    showPanle: false, // 重点信息面板默认收起
    showTaskPanle: true,//任务发起面板
    gsglInfo: false, // 高速公路详情面板信息
    gstlInfo: false, // 高速铁路详情面板信息
    xcdaInfo: false, // 现场档案详情面板信息
    zddaInfo: false,// 住地档案详情面板信息
    cityInfo: false,// 城市道路详情面板信息
    rightCard: false,// 右侧面板信息
    showTool: false,//工具栏信息
    showToolOne: false,// 框选工具栏,
    layerList: [],// 图层树,
    rightPanleData: [],//右侧面板数据,
    markerGroupId: '',// 标注的分组id
    dxModel: false,// 地下模式返回开关
    selectMenu: {},// 当前工具栏的点击
    isClickTools: false,// 关闭下面的工具栏
    canNot:false,
  }),
  actions: {
    setCanNot(bol){
      this.canNot = bol
    },
    set_isClickTools(bol) {
      this.isClickTools = bol
    },
    set_selectMenu(obj) {
      this.selectMenu = obj
    },
    setDXModel(BOL) {
      this.dxModel = BOL
    },
    setMarkerGroupId(id) {
      this.markerGroupId = id
    },
    setGSlayerId(id) {
      this.gsLayer = id
    },
    setAllLayer(id) {
      this.allLayer = id
    },
    setshowTaskFun(bol) {
      this.showTask = bol
    },
    // 面板信息显示隐藏
    setShowPanle(bol) {
      if (bol) {
        this.showPanle = true
      } else {
        this.showPanle = !this.showPanle;
      }

    },
    setShowTaskPanle(bol) {
      if (bol) {
        this.showTaskPanle = true
      } else {
        this.showTaskPanle = !this.showTaskPanle
      }
    },
    // 设置几种详情面板的显示隐藏
    setLeftInfo(type) {
      // 约定类型0公路1铁路2档案以此类推
      switch (type) {
        case "0":
          this.gsglInfo = !this.gsglInfo;
          this.gstlInfo = false;
          this.xcdaInfo = false;
          this.zddaInfo = false
          this.cityInfo = false
          break;
        case "1":
          this.gstlInfo = !this.gstlInfo;
          this.gsglInfo = false;
          this.xcdaInfo = false;
          this.zddaInfo = false
          this.cityInfo = false
          break;
        case "2":
          this.xcdaInfo = !this.xcdaInfo;
          this.gstlInfo = false;
          this.gsglInfo = false;
          this.zddaInfo = false
          this.cityInfo = false
          break;
        case "3":
          this.zddaInfo = !this.zddaInfo;
          this.gstlInfo = false;
          this.gsglInfo = false;
          this.xcdaInfo = false
          this.cityInfo = false
          break;
        case "4":
          this.zddaInfo = false
          this.gstlInfo = false;
          this.gsglInfo = false;
          this.xcdaInfo = false
          this.cityInfo = !this.cityInfo
          break;
        default:
          this.xcdaInfo = false;
          this.gstlInfo = false;
          this.gsglInfo = false;
          this.zddaInfo = false;
          this.cityInfo = false
          break;
      }
    },
    setRightCard(bol) {
      if (bol) {
        this.rightCard = true
      } else {
        this.rightCard = !this.rightCard
      }
    },
    setShowTool(bol) {
      this.showTool = bol
    },
    setShowChildTool(bol) {
      this.showToolOne = bol
    },
    setLayerTree(data) {
      this.layerList = data
    },
    //设置右侧面板数据
    setRightPanleData(data) {
      this.rightPanleData = data
    }
  },
  getters: {},
});

export default useSettingStore;
