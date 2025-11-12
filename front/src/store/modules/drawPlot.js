
import { defineStore } from "pinia";

export const useDrawPlotStore = defineStore({
    id: "drawPlot",
    state: () => ({
      Line:[],// 线的数据包括箭头,
      Area:[],// 面的数据
    }),

    actions: {
      async setLineData(data){
        this.$patch({
          Line: data,
      });
      },
      async setAreaData(data){
        this.$patch({
          Area: data,
      });
      }
    },
});

