/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-19 11:36:32
 * @LastEditTime: 2024-06-19 11:39:47
 * @LastEditors: Alex
 */
//axios实例封装

import axios from "axios";
//引入elementul的弹框提醒组件后面要用来报错
const service = axios.create({
    //基础路径URL配置
    // baseURL: "http://localhost:8001",
    //五秒未响应提示
    timeout: 1000 * 60,
})


//请求拦截
service.interceptors.request.use(config => {
    return config
}, error => {
    return Promise.reject(error)
})


//响应拦截器
service.interceptors.response.use((response) => {
    //只返回config内的data的数据，其他的不展示
    const res = response.data
    return res
}, (error) => {
    return Promise.reject(error)
})

export default service
