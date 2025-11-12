import { defineStore } from "pinia";
const useTaskStore = defineStore("task", {
  state: () => ({
    isOpenDraw: false, // 是否开启预案绘制
    drawType: 0, //预案绘制类型 0 路线 1现场 2 住地,
    keyFrames:[],// 导览列表第一视角数据
    thirdKeyFrames:[],// 导览列表第三视角数据
    keyFramesName:'',// 编辑视角名称
    isShowFrames:false,// 是否显示导览列表
    tourList:[],//存放导览场景数组
    taskInfo:{},// 任务详情信息
    addFormData:{},// 新增场景的基本数据
    taskVideoList:[],// 任务推演整个流程
    endPlay:false,// 导览是否结束
    edit:false,// 场景是否编辑
    drawEdit:{},// 标绘编辑数据
    taskPlan:false,// 任务规划弹框
    screenPlan:false,// 场景规划弹框
    screenPlanTitle:'',// 场景规划弹框文字
    clearModal:true,// 任务模块所有弹框
    sceneBol:false,// 普通任务场景详情
    screenModalName:'',//
    screenModalInfo:{},// 编辑拿到的场景信息
    taskDrawData:[],// 任务下的标绘数据
    communityBol:false,// 社区详情弹框
    attractBol:false,// 招商详情弹框
    roomCode:'',// 当前楼层炸开的房间id
    communityInfo:{},// 社区居民楼信息
  }),
  actions: {
    SET_ISOPENDRAW(val) {
      this.isOpenDraw = val;
    },
    SET_DRAWTYPW(val) {
      this.drawType = val;
    },
    SET_KEYFRAMES(list){
        this.keyFrames = list
    },
    SET_THIRDKEYFRAMES(LIST){
      this.thirdKeyFrames = LIST
    },
    set_keyFramesName(name){
      this.keyFramesName = name
    },
    SET_ISSHOWFRAMES(VAL){
        this.isShowFrames = VAL
    },
    SET_TOURLIST(val){
        this.tourList = val
    },
    SET_TASKINFO(OBJ){
        this.taskInfo = OBJ
    },
    SET_ADDFORMDATA(OBJ){
        this.addFormData = OBJ
    },
    SET_TASKVIDEOLIST(LIST){
      this.taskVideoList = LIST
    },
    SET_ENDPLAY(BOL){
      this.endPlay = BOL
    },
    SET_EDIT(bol){
      this.edit = bol
    },
    SET_DRAWEDIT(obj){
      this.drawEdit = obj
    },
    SET_TASKPLAN(bol){
      this.taskPlan = bol
    },
    SET_clearModal(BOL){
      this.clearModal = BOL
    },
    SET_SCREENPLAN(bol){
      this.screenPlan = bol
    },
    SET_SCREENPLANTITLE(name){
      this.screenPlanTitle = name
    },
    SET_SCREENMODALNAME(name){
      this.screenModalName = name
    },
    SET_SCREENMODALINFO(obj){
      this.screenModalInfo = obj
    },
    set_sceneBol(bol){
      this.sceneBol = bol
    },
    set_taskDrawData(arr){
      this.taskDrawData = arr
    },
    set_communityBol(bol){
      this.communityBol = bol
    },
    set_attractBol(bol){
      this.attractBol = bol
    },
    set_roomCode(code){
      this.roomCode = code
    },
    set_communityInfo(obj){
      this.communityInfo = obj
    }
  },
  getters: {},
});

export default useTaskStore;
