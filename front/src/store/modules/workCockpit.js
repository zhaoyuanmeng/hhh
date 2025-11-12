import { defineStore } from "pinia"
import {getSceneListByTaskIdAndSceneName} from '@/api/workCockpit/index.js'

const useWorkCockpitStore = defineStore("WorkCockpit", {
  state: () => ({
    changeLocation:false,// 点位纠偏的布尔值
    levelColorMap: { // 任务等级文本颜色
      '一级加强': 'color-enhance',
      '一级任务': 'color-level-one',
      '二级任务': 'color-level-two',
      '三级任务': 'color-level-three',
      '保卫任务': 'color-level-secure',
      '非等级任务': 'color-level-three',
    },
    isFullScreen: false, // 是否全屏展开显示
    curPage: 'overview', // 当前页面
    loadStatus:false,// 驾驶舱当前页面需要等地图加载完后再出现
    taskItem: null, // 当前任务
    showSceneDetail: false,
    sceneItem: null, // 当前任务场景
    showTaskDetail: true, // 任务详情是否全屏展开显示
    eventMapData:{} , // 地图节点数据
    curTourId:'',// 当前场景的漫游id
    showMarkerInfo:false, //是否显示地图节点信息弹窗
    // showPlayTour:false, // 任务漫游
    taskPlanNode:'',
    showSceenRightCard:true,
    taskSceneData:[],// 任务下路线/现场/住地
    canceMarkerlData:{}, // 要取消的高亮marker
    cancelLineIds:[], // 要取消的高亮lines
    sceneIds:[],
    sceneList:[],
    openHouseBol:false,// 一个场景是否打开楼层
    showSceneDialog:false,
    roadDrawList:[], // 路线标绘数据
    taskScreenLines:[],// 任务下场景的路线数据
    siteDrawList:[], // 现场标绘数据
    residenceDrawList:[], // 住地标绘数据
    tabsOne:'rcap',// 当前二级页面选中的tab默认日程安排
    tabsTwo:'',// 当前二级页面选中的子tab
    tabsName:'',// 日程安排不显示资源
    showResouce:true,// 是否显示三级页面的资源
    threePageType:'',// 三级页面的类型
    policeCheckBox:false,// 警力
    carCheckBox:false,// 警车
    linesCheckBox:false,//路线
    policeLineCheckBox:true,// 警戒线
    bussinessCheckBox:false,// 商业
    communityCheckBox:false,// 社区
    twoPageSource:'',// 二级任务部署的资源
    showPolicDialog:false,
    showCarDialog:false,
    showBasicDialog:false,
    showUavDialog:false,
    showPoliceBasic:false,// 是否显示警力以及基础信息要素弹框
    showPlayVideo:false,
    showMp4:false,
    basicTaskId:'',// 基础数据请求的id
    basicSceneId:'',// 基础数据请求的id
    newLocation:{},// 新的位置对象
    taskManagement:[],// 一对多任务长显
  }),
  actions: {
    set_changeLocation(bol){
      this.changeLocation = bol
    },
    set_newLocation(obj){
      this.newLocation = obj
    },
    set_basicIds(id,type){
      if(type===0){
        this.basicTaskId =id
        this.basicSceneId =''
      }else{
        this.basicSceneId =id
        this.basicTaskId = ''
      }
    },
    set_showPlayVideo(bol){
      this.showPlayVideo = bol
    },
    set_showMp4(bol){
      this.showMp4 = bol
    },
    set_loadStatus(bol){
      this.loadStatus = bol
    },
    set_showPoliceBasic(bol){
      this.showPoliceBasic = bol
    },
    set_showPolicDialog(type,bol){
      if(type==='police'){
        this.showPolicDialog = bol
      }
      if(type==='car'){
        this.showCarDialog = bol
      }
      if(type==='basic'){
        this.showBasicDialog = bol
      }
      if(type==='uav'){
        this.showUavDialog = bol
      }
      if(type==='all'){
        this.showPolicDialog = false
        this.showCarDialog = false
        this.showBasicDialog = false
        this.showUavDialog = false
      }
    },
    set_threePageType(type){
      this.threePageType = type
    },
    setCurTourId(id){
      this.curTourId = id
    },
    set_twoPageSource(str){
      this.twoPageSource = str
    },
    set_checkBox(type,val){
      if(type==='police'){
        this.policeCheckBox = val
      }
      if(type==='car'){
        this.carCheckBox = val
      }
      if(type==='line'){
        this.linesCheckBox = val
      }
      if(type==='cordon'){
        this.policeLineCheckBox = val
      }
      if(type==='business'){
        this.bussinessCheckBox = val
      }
      if(type==='community'){
        this.communityCheckBox = val
      }
      if(type==='init'){
        this.policeCheckBox = false
        this.carCheckBox = false
        this.linesCheckBox = false
        this.policeLineCheckBox = true
        this.bussinessCheckBox = false
        this.communityCheckBox = false
      }
    },
    set_tabsOne(str){
      this.tabsOne = str
    },
    set_showResouce(bol){
      this.showResouce = bol
    },
    setTabsName(name){
      this.tabsName = name
    },
    set_tabsTwo(str){
      this.tabsTwo = str
    },
    setFullScreen(flag) {
      this.isFullScreen = flag
    },
    setShowTaskDetail(flag) {
      this.showTaskDetail = flag
    },
    setCurPage(page) {
      this.curPage = page
    },
    setTaskItem(item) {
      this.taskItem = item
    },
    setShowSceneDetail(bool) {
      this.showSceneDetail = bool
    },
    setSceneItem(item) {
      console.log('任务信息：', item)
      this.sceneItem = item
    },
    setShowMarkerInfo(bool){
      this.showMarkerInfo=bool
    },
    setEventMapData(data){
      this.eventMapData=data
    },
    setTaskPlanNode(node){
      this.taskPlanNode=node
    },
    setShowSceenRightCard(bool){
      this.showSceenRightCard=bool
    },
    setCancelData(data){
      this.canceMarkerlData=data
    },
    setCancelLineIds(arr){
      this.cancelLineIds=arr
    },
    setSceneIds(arr){
      this.sceneIds=arr
    },
    setTaskSceneData(data){
      this.taskSceneData = data
    },
    getSceneData(sceneName){
     return getSceneListByTaskIdAndSceneName({taskId:this.taskItem.id,sceneName}).then(res=>{
      this.sceneList=res.data || []
     })
    },
    setShowSceneDialog(bool){
      this.showSceneDialog=bool
    },
    setRoadDrawList(list){
      this.roadDrawList=list
    },
    setSiteDrawList(list){
      this.siteDrawList=list
    },
    setResidenceDrawList(list){
      this.residenceDrawList=list
    },
    set_taskLines(list){
      this.taskScreenLines =list
    },
    setTaskManagement(list){
      this.taskManagement = list
    },
    openHouse(bol){
      this.openHouseBol = bol
    }


  }
});

export default useWorkCockpitStore;
