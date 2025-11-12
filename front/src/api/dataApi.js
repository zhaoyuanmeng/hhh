/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-19 11:05:22
 * @LastEditTime: 2024-06-19 11:05:54
 * @LastEditors: Alex
 */
import request from "@/http/HTTP";
import http from '@/http/axiosHttp'



export const getPoiData = () => {
    return request.get(window.origin + '/geojson/POI/POI.json')
}
// 获取各社区范围点位数据
export const getCommunityData = () => {
    return request.get('')
}
// 获取各IOT poi的点位数据
export const getPoiByTypeData = (params) => {
    return http.get('', params)
}
export const getCameraByCode = (params) => {
    return http.get('', params)
}
//获取环境相关传感器信息
export const getEvInFoByEvParams = (params) => {
    return http.get('', params)
}
//构件信息
export const getLayerObjectInfoByObjectInfo = (params) => {
    return http.get('', params)
}
//监控投影
// cameraIndexCode监控id
export const getCameraIndexCode = (params) => {
    return http.get('', params)
}
/**
 * 视点
 * @param params
 * @returns
 */
// 获取视点列表
export const getBsCameraLocationList = (params) => {
    return http.get('', params)
}
// 保存视点
export const saveBsCameraLocation = (params) => {
    return http.post('', params)
}
// 删除视点
export const delByIdsBsCameraLocation = (params) => {
    return http.post('', params)
}

/**
 * 导览
*/
//获取导览列表
export const getBsGuideInfoList = (params) => {
    return http.get('', params)
}
//保存导览
export const saveBsGuideInfo = (params) => {
    return http.post('', params)
}
//删除导览
export const delByIdsBsGuideInfo = (params) => {
    return http.post('', params)
}
//编辑导览
export const updateByIdBsGuideInfo = (params) => {
    return http.post('', params)
}

export const Login = (params) => {
    return http.post('', params)
}



