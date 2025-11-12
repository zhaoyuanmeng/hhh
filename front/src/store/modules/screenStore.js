/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-17 14:39:18
 * @LastEditTime: 2024-06-17 15:19:05
 * @LastEditors: Alex
 */
import { defineStore } from 'pinia'
const useScreenStore = defineStore(
  'screen',
  {
    state: () => ({
      screenModal:false,//标绘弹框
      screenInfo:{},// 标绘保存时候的数据
      editScreen:false,// 是否能编辑场景
      screenDrawData:{},// 当前场景标绘的单个数据
      screenLineData:{},//警力游动哨的数据
      showPicture:false,// 是否展示一张图
      pictureId:'',// 任务一张图所需id进行查询
      pictureName:'',//任务一张图图片名称
      pictureType:'',// 一张图类型
      showPlay:false,// 漫游播放
      sceneLine:{},// 创建场景路线
      sceneCardInfo:[],// 场景面板详情
      resourceBol:false,// 资源列表弹框
      watchBol:false,// 刷新资源列表
      colorWidth:{},// 新增线路的颜色和宽度
    }),
    actions: {
      // 警力部署弹框
      set_screenModal(bol){
        this.screenModal = bol
      },
      set_colorWidth(obj){
        this.colorWidth = obj
      },
       // 警力部署提交数据
       set_screenInfo(obj){
        this.screenInfo = obj
      },
      set_editScreen(bol){
        this.editScreen = bol
      },
      set_screenDrawData(obj){
        this.screenDrawData = obj
      },
      set_screenLineData(obj){
        this.screenLineData = obj
      },
      set_showPicture(bol){
        this.showPicture = bol
      },
      set_pictureId(id){
        this.pictureId = id
      },
      set_pictureName(name){
        this.pictureName = name
      },
      set_pictureType(type){
        this.pictureType = type
      },
      set_showPlay(bol){
        this.showPlay = bol
      },
      set_sceneLine(obj){
        this.sceneLine = obj
      },
      set_sceneCardInfo(arr){
        this.sceneCardInfo = arr
      },
      set_resourceBol(bol){
        this.resourceBol = bol
      },
      SET_watchBol(bol){
        this.watchBol = bol
      }
    },
    getters: {},
  })

export default useScreenStore
