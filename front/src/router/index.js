/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 17:40:46
 * @LastEditTime: 2024-06-20 19:24:59
 * @LastEditors: Alex
 */
import { createRouter, createWebHashHistory } from "vue-router";
import Layout from "@/layout";
const routes = [
  {
    path: "",
    component: Layout,
    redirect: "/Login",
    children: [
      {
        path: "/work-cockpit",
        component: () => import("@/views/WorkCockpit/index"),
        name: "WorkCockpit",
        meta: { title: "工作驾驶舱", icon: "dashboard", affix: true },
      },
      {
        path: "/index",
        component: () => import("@/views/index"),
        name: "Home",
        meta: { title: "基础数据", icon: "dashboard", affix: true },
      },
      {
        path: "/task",
        component: () => import("@/views/taskLaunch"),
        name: "Task",
        props: true, // 启用 props 将 params 传递给组件
        meta: { title: "任务推演", icon: "dashboard", affix: true },
      },
      // {
      //   path: '/inference',
      //   component: () => import('@/views/taskInference'),
      //   name: 'Inference',
      //   meta: { title: '任务推演', icon: 'dashboard', affix: true }
      // },
      {
        path: "/replay",
        component: () => import("@/views/taskReplay"),
        name: "Replay",
        meta: { title: "任务复盘", icon: "dashboard", affix: true },
      },
      {
        path: "/system",
        component: () => import("@/views/PermissionControl/index"),
        name: "System",
        meta: { title: "后台管理", icon: "dashboard", affix: true },
      },
      {
        path: "/emergency-plan",
        component: () => import("@/views/EmergencyPlan/index"),
        name: "EmergencyPlan",
        meta: { title: "应急预案", icon: "dashboard", affix: true },
      },
    ],
  },
  {
    path: "/Login",
    name: "Login",
    component: () => import("../views/login/index.vue"),
  },
];
const router = createRouter({
  history: createWebHashHistory(), // 使用hash模式
  routes,
});

export default router;
