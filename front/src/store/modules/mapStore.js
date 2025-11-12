import { defineStore } from 'pinia'
const useMapStore = defineStore(
  'map',
  {
    state: () => ({
      mapType: '3D',// 默认地图模型是3dmax
    }),
    actions: {
     SETMAPTYPE(TYPE){
      this.mapType = TYPE
     }
    },
    getters: {},
  })

export default useMapStore
