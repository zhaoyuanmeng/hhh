/*
 * @Author: your name
 * @Date: 2021-12-01 15:52:58
 * @LastEditTime: 2024-06-19 11:37:31
 * @LastEditors: Alex
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \vue3_ts_init\src\plugins\HTTP.ts
 */
import axios from "axios";
import { getToken } from "@/http/auth";

const request = axios.create({
    timeout: 100000,
});

const TOKEN = getToken(); // 获取token

// 请求拦截器
request.interceptors.request.use(
    (config) => {
        if (TOKEN) {
            config.headers["Authorization"] = TOKEN;
        }

        return config;
    },
    (error) => {
        console.log(error);
        return Promise.reject(error);
    }
);

// 响应拦截器
request.interceptors.response.use(
    (response) => {

        return response.data;
    },
    (error) => {
        const data = error.response.data;
        const status = error.response.status;

        // 对不同状态码进行管理
        if (status === 401) {
            console.log("登录已过期");
        } else if (status === 500) {
            console.log("服务器错误");
        } else {
            console.log("请求错误状态码:", status);
        }
        return Promise.reject(data.error);
    }
);

export default request;
