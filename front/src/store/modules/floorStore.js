import { defineStore } from 'pinia'
const useFloorStore = defineStore(
  'floor',
  {
    state: () => ({
      roomArr:[],// 当前点击楼层的所有房间对象
      floornumberShow: false,// 显示隐藏楼层工具栏
      floorsTotalNum: 0,// 建筑楼层数量
      explodebuildname:'',
      floornum:null,// 点击的楼层号
      isActive:"",// 默认选中哪一层
      openFloor:false,// 楼层是否点击炸开
      floorMarkers:[],// 楼层炸开上的标绘数据
      isShowFloor:false,
      isOpenBol:false,// 楼层还没抽出
      nofloorNumber:false,// 楼层不能炸开的楼层号
      floorName:'',// 不能抽出的楼层名称
      noFloorNum:0,//不能抽出的楼层号
      projectShowData:{},// 项目展示的社区、楼栋标签
      businessFloorsMarker:[],// 楼层里面商户数据
      floorsId:'',// 当前点击炸开的楼栋id
      noFloorsId:'',

    }),
    actions: {
      set_isOpenBol(bol){
        this.isOpenBol = bol
      },
      // 楼层的显示隐藏
      setFloornumberShow(show){
        this.floornumberShow = show
      },
      setExplodebuildName(name){
        this.explodebuildname = name
      },
      setfloorsTotalNum(num){
      this.floorsTotalNum = num
      },
      setFloornum(num){
        this.floornum = num
      },
      setIsActive(n){
        this.isActive = n
      },
      setOpenFloor(bol){
        this.openFloor = bol
      },
      set_floorMarkers(list){
        this.floorMarkers = list
      },
      set_isShowFloor(bol){
        this.isShowFloor = bol
      },
      set_nofloorNumber(bol){
        this.nofloorNumber = bol
      },
      set_floorName(name){
        this.floorName = name
      },
      set_noFloorNum(num){
        this.noFloorNum = num
      },
      set_projectShowData(arr){
        this.projectShowData = arr
      },
      set_businessFloorsMarker(arr){
        this.businessFloorsMarker = arr
      },
      set_floorsId(id){
        this.floorsId = id
      },
      set_roomArr(arr){
        this.roomArr = arr
      },
      set_noFloorsId(id){
        this.noFloorsId = id
      }
    },
    getters: {},
  })

export default useFloorStore
