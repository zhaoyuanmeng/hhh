/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-19 10:14:23
 * @LastEditTime: 2024-06-20 15:44:07
 * @LastEditors: Alex
 */
import { defineAsyncComponent } from "vue"
// import {loadPicture} from '@/utils'
import { loadPicture } from "@/utils";
//  loadPicture(`./imgs/${element.imgName}.png`)
/**
 * @FileDescription: 高渲染-功能菜单
 * @Author: yuanhaijun
 * @Date: 2023.03.16
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.03.16
 */
export const DTSTree = [
    {
        id: 0,
        label: '地下模式',
        src: loadPicture('./images/cloud/地下模式.png'),
        show: true,
        children: []
    },
    // {
    //     id: 4,
    //     label: '地图框选',
    //     src: loadPicture('./images/cloud/地图框选.png'),
    //     show: true,
    //     mode: 5,
    //     children: []
    // },
    {
        id: 3,
        label: '辅助分析',
        src: loadPicture('./images/cloud/辅助分析.png'),
        show: true,
        children: [
            {
                id: 301,
                label: '可视域',
                name: 'analyse/Viewshed',
                show: true,
                pid: 3,
            },
            {
                id: 305,
                label: '通视',
                name: 'analyse/Perspective',
                show: true,
                pid: 3,
            },
            {
                id: 306,
                label: '直线',
                pid: 3,
                name: 'measurement/Measurement',
                mode: 2,
                show: true,
            },
            {
                id: 307,
                label: '地表面积',
                pid: 3,
                name: 'measurement/Measurement',
                mode: 7,
                show: true,
            },
            {
                id: 308,
                label: '坐标',
                pid: 3,
                name: 'measurement/Measurement',
                mode: 1,
                show: true,
            }

        ],
    },
    {
        id: 8,
        label: '基础工作',
        src: loadPicture('./images/cloud/标绘.png'),
        show: true,
        children: []
    },
    // {
    //     id: 5,
    //     label: '标绘',
    //     src: loadPicture('./images/cloud/标绘.png'),
    //     show: true,
    //     children: [
    //         {
    //             id: 501,
    //             label: '画线',
    //             name: 'plot/DrawLine',
    //             show: true,
    //             pid: 5,
    //         },
    //         // {
    //         //     id: 508,
    //         //     label: '三维箭头',
    //         //     name: 'plot/DrawLineArrows',
    //         //     show: true,
    //         //     pid: 5,
    //         // },
    //         {
    //             id: 502,
    //             label: '画面',
    //             name: 'plot/DrawSurface',
    //             show: true,
    //             pid: 5,
    //         },
    //         {
    //             id: 503,
    //             label: '三维多边形',
    //             name: 'plot/PullSurface',
    //             show: true,
    //             pid: 5,
    //         },
    //         {
    //             id: 505,
    //             label: '三维标注',
    //             name: 'plot/tag/Tag3D',
    //             show: true,
    //             pid: 5,
    //         },
    //     ]
    // },
    {
        id: 1,
        label: '警力',
        src: loadPicture('./images/cloud/警用模型.png'),
        name: 'plot/police/police',
        show: true,
        children: []
    },
    {
        id: 2,
        label: '警车',
        src: loadPicture('./images/cloud/警车.png'),
        name: 'plot/car/policeCar',
        show: true,
        children: []
    },
    {
        id: 66,
        label: '防爆安检',
        src: loadPicture('./images/cloud/fbaj.png'),
        name: 'plot/check/index',
        show: true,
        children: []
    },
    {
        id: 6,
        label: '警戒线',
        src: loadPicture('./images/cloud/道路线.png'),
        name: 'plot/PullSurface',
        show: true,
        children: []
    },
    {
        id: 508,
        label: '路线',
        src: loadPicture('./images/cloud/动态线条.png'),
        name: 'plot/DrawLineArrows',
        show: true,
        children: []
    },
    // {
    //     id: 9,
    //     label: '地图框选',
    //     src: loadPicture('./images/cloud/地图框选.png'),
    //     name: 'BoxSelect/index',
    //     show: true,
    //     children: []
    // },
    // {
    //     id: 7,
    //     label: '车辆巡航',
    //     src: loadPicture('./images/cloud/警车.png'),
    //     show: true,
    //     children: []
    // },
]

export const dynamicComponent = {

}
DTSTree.forEach((item) => {
    item.children.forEach((children) => {
        children.iconImg = loadPicture(`./images/XAXCCimTools/${item.label}/${children.label}.png`)
        children.activeIconImg = loadPicture(`./images/XAXCCimTools/${item.label}/${children.label}选中.png`)
        dynamicComponent[children.name] = defineAsyncComponent(() => import(`@/views/SysTools/resourceCatalog/${children.name}.vue`))
    })
})

export default dynamicComponent


