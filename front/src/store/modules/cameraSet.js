import { defineStore } from 'pinia'
// 任务推演相机视角、推演、场景
const useCameraStore = defineStore(
  'camera',
  {
    state: () => ({
      screenInfo:{
        id:'',// 场景id
        name:'',// 场景名
      },
      taskInfo:{},// 详情数据
      isShowList: true, //显示导览列表，关闭详情
      taskInfoBool:false,// 任务详情
      taskObj:{},// 任务信息,
      tourList:[],//存放导览场景数组
      cName:'',// 新建导览名
      saveBtn:false,// 添加场景保存重置按钮
      sceneFrame:{},// 点击导览的时候场景初始视角
    }),
    actions: {
      setSceneFrame(obj){
        this.sceneFrame = obj
      },
      setSaveBtn(bol){
        this.saveBtn = bol
      },
    // 任务详情
      setShowTaskInfo(bol){
        this.taskInfoBool =bol
      },
      setisShowListShow(val) {
        this.isShowList = val;
      },
      setTourList(data){
        this.tourList = data
      },
      setScreenId(id){
        this.cName = id
      },
      setTaskInfo(obj){
        this.taskInfo = obj
      },
      setScreenInfo(val){
        this.screenInfo = val
      }
    },
    getters: {},
  })

export default useCameraStore
