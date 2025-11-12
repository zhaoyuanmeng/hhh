import { defineStore } from "pinia"

const usePlanTaskStore = defineStore("PlanTask", {
  state: () => ({
    planPage: false, // 常备方案页面
    planTaskInfo:{},// 常备方案数据
    routerGo:false,// 是否是从驾驶舱页面进行的跳转
    roamPage:false,//常备方案漫游页面
    checkInfo:{},
    checkDetails:false,
    
  }),
  actions: {
    setCurPage(page) {
      this.planPage = page
    },
    set_planTaskInfo(obj){
      this.planTaskInfo = obj
    },
    set_routerGo(bol){
      this.routerGo = bol
    },
    set_roamPage(bol){
      this.roamPage = bol
    },
    set_checkInfo(info){
      this.checkInfo = info
    },
    set_checkDetails(bol){
      this.checkDetails = bol
    }
  },
  getters: {},
});

export default usePlanTaskStore;
