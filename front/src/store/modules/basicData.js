import { defineStore } from 'pinia'
// 基础数据仓库
const useBasicStore = defineStore(
  'basic',
  {
    state: () => ({
       // 封装API的JS回调数据
      eventSealAPI: {},
      addPoint:false,// 能否点击地图新增点位
      rightPanel:true,// 右侧面板默认展开
      saveBtn:false,// 添加场景保存重置按钮
      lineId:'',// 点击左侧面板存储的id
      // 所有可见图层数据
      layerData:[],
      // 右侧面板数据
      rightPop:{
        title:'',// 面板名称
        info:'', // 面板详细信息
        zddwNum: 0, // 面板的整个点位数
        point: [] // 面板下面的树形结构数据点
      },
      type:'',// 当前操作的类型 参数type代表上传基础数据的大类，1高速 2高铁 3 现场 4 住地
      activeBasicInfoList:[], //目标档案列表
      locationBol:false,
      roadMapBol:false
    }),
    actions: {
      set_locationBol(e){
        this.locationBol = e
      },
      set_roadMap(bol){
        this.roadMapBol = bol
      },
      SET_ACTIVE_BASICINFO_LIST(list){
        this.activeBasicInfoList = list
      },
      setAddPoint(bol){
        this.addPoint = bol
      },
      setEventMapData(obj){
        this.eventSealAPI = obj
      },
      //设置可见图层数据
      setLayerData(data){
        this.layerData = data
      },
      // 设置右侧面板数据
      setRightPop(data){
        this.rightPop = data
      },
      // 设置当前点击的高铁、高速线路的id
      setLineId(id){
        this.lineId = id
      },
      setSaveBtn(bol){
        this.saveBtn = bol
      },
      setRightPanel(bol){
        this.rightPanel = bol
      },
      SET_TYPE(type){
        this.type = type
      }
    },
    getters: {},
  })

export default useBasicStore
