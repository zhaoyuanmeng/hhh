/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-17 14:39:18
 * @LastEditTime: 2024-06-17 15:19:05
 * @LastEditors: Alex
 */
import { defineStore } from 'pinia'
const useMarkerStore = defineStore(
  'marker',
  {
    state: () => ({
      markerId:'',
      parentId:'',// 重点点位的父节点id
      markerInfo:{
      }// 点击的时候marker的详细信息
    }),
    actions: {
      // 楼层的显示隐藏
      setMarkerInfo(info){
        this.markerInfo = info
      },
       // 楼层的显示隐藏
       setMarkerId(id){
        this.markerId = id
      },
      //设置父节点id
      setParentId(id){
        this.parentId = id
      }
    },
    getters: {},
  })

export default useMarkerStore
