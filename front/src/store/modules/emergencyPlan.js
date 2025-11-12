import { defineStore } from "pinia"

const useEmergencyStore = defineStore("emergencyPlan", {
  state: () => ({
    showEmergencyPlan:false, // 是否显示应急预案页面
    infoBol:false,// 预案详情
    indexBol:true,// 预案首页
    detailsInfo:{},// 点击详情时的数据
    isShowInfo:false,// 详情面板布尔值
    YAInfo:{},// 预案信息
    editYA:false,// 预案编辑操作
    editInfo:{},// 点击某条数据查询详情信息
  }),
  actions: {
    set_showEmergencyPlan(bool){
      this.showEmergencyPlan=bool
    },
    set_infoBol(bol){
      this.infoBol = bol
    },
    set_indexBol(bol){
      this.indexBol = bol
    },
    set_detailsInfo(obj){
      this.detailsInfo = obj
    },
    setDetailsPage(bol){
      this.isShowInfo = bol
    },
    set_editYA(bol){
      this.editYA = bol
    },
    set_YAInfo(item){
      this.YAInfo = item
    },
    set_editInfo(obj){
      this.editInfo = obj
    }
  }
});

export default useEmergencyStore;
