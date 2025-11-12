/**
 * @FileDescription: 资源-粒子
 * @Author: yuanhaijun
 * @Date: 2023.04.11
 * @LastEditors: yuanhaijun
 * @LastEditTime: 2023.04.11
 */
const particles = [
    {
        name: '水',
        children: [
            {
                name: '喷水_1',
            },
            {
                name: '喷泉_1',
                "params": [{ "functionName": "数量", "functionParams": [{ "name": "V_数量", "type": 3, "defaultValue": 0.6 }] }, { "functionName": "扩散", "functionParams": [{ "name": "V_角度", "type": 3, "defaultValue": 0.3 }] }, { "functionName": "力度", "functionParams": [{ "name": "V_力度", "type": 3, "defaultValue": 0.7 }] }]
            },
            {
                name: '喷泉_4',
            },
            {
                name: '喷泉_5',
            },
            {
                name: '喷泉_6',
            },
            {
                name: '喷泉_7',
            },
            {
                name: '气泡_1',
                "params": [{ "functionName": "颜色", "functionParams": [{ "name": "v颜色", "type": 6, "defaultValue": [0.727431, 0.727431, 0.727431, 1] }] }, { "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 1 }] }]
            },
            {
                name: '气泡_2',
                "params": [{ "functionName": "高级参数", "functionParams": [{ "name": "v持续时间", "type": 3, "defaultValue": 0.24 }, { "name": "v粒子间隔", "type": 3, "defaultValue": 1 }] }, { "functionName": "颜色", "functionParams": [{ "name": "v颜色", "type": 6, "defaultValue": [1, 1, 1, 1] }] }, { "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 1 }] }]
            },
            {
                name: '气泡_3',
                "params": [{ "functionName": "高级参数", "functionParams": [{ "name": "v持续时间", "type": 3, "defaultValue": 0.4 }, { "name": "v粒子间隔", "type": 3, "defaultValue": 0.1 }] }, { "functionName": "大小", "functionParams": [{ "name": "v大小", "type": 3, "defaultValue": 0.3 }] }, { "functionName": "范围", "functionParams": [{ "name": "v范围", "type": 3, "defaultValue": 0.7 }] }, { "functionName": "颜色", "functionParams": [{ "name": "v颜色", "type": 6, "defaultValue": [1, 1, 1, 1] }] }, { "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 0.5 }] }]
            },
            {
                name: '气泡_4',
                "params": [{ "functionName": "高级参数", "functionParams": [{ "name": "v持续时间", "type": 3, "defaultValue": 0.4 }, { "name": "v粒子间隔", "type": 3, "defaultValue": 1 }] }, { "functionName": "范围", "functionParams": [{ "name": "v范围", "type": 3, "defaultValue": 0.7 }] }, { "functionName": "颜色", "functionParams": [{ "name": "v颜色", "type": 6, "defaultValue": [0.713542, 0.713542, 0.713542, 1] }] }, { "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 1 }] }]
            },
            {
                name: '气泡_5',
                "params": [{ "functionName": "高级参数", "functionParams": [{ "name": "v持续时间", "type": 3, "defaultValue": 0.4 }, { "name": "v粒子间隔", "type": 3, "defaultValue": 1 }] }, { "functionName": "范围", "functionParams": [{ "name": "v范围", "type": 3, "defaultValue": 0.7 }] }, { "functionName": "颜色", "functionParams": [{ "name": "v颜色", "type": 6, "defaultValue": [0.713542, 0.713542, 0.713542, 1] }] }, { "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 1 }] }]
            },
            {
                name: '水帘_2',
            },
            {
                name: '水流_1',
            },
            {
                name: '水流_2',
            },
        ]
    },
    {
        name: '火',
        children: [
            {
                name: '火星_1',
            },
            {
                name: '火星_2',
            },
            {
                name: '火星_3',
            },
            {
                name: '火星_4',
            },
            {
                name: '火星_5',
            },
            {
                name: '火星_6',
            },
            {
                name: '火焰_1',
            },
            {
                name: '火焰_2',
            },
            {
                name: '火焰_3',
            },
            {
                name: '火焰_4',
            },
            {
                name: '火焰_5',
            },
            {
                name: '火焰_6',
            },
            {
                name: '火焰_7',
            },
            {
                name: '火焰_8',
            },
            {
                name: '火焰_9',
            },
            {
                name: '火焰_10',
            },
            {
                name: '火焰_11',
            }
        ]
    },
    {
        name: '烟雾',
        children: [
            {
                name: '烟雾_1',
            },
            {
                name: '烟雾_2',
            },
            {
                name: '烟雾_3',
            },
            {
                name: '烟雾_4',
            },
            {
                name: '烟雾_5',
            },
            {
                name: '烟雾_6',
            },
            {
                name: '烟雾_7',
            },
            {
                name: '烟雾_8',
            }
        ]
    },
    {
        name: '自然',
        children: [
            {
                name: '落叶',
                "params": [
                    {
                        "functionName": "半径",
                        "functionParams": [
                            {
                                "name": "V半径",
                                "type": 3,
                                "defaultValue": 10
                            }
                        ]
                    },
                    {
                        "functionName": "数量",
                        "functionParams": [
                            {
                                "name": "V数量",
                                "type": 3,
                                "defaultValue": 0.3
                            }
                        ]
                    },
                    {
                        "functionName": "种类",
                        "functionParams": [
                            {
                                "name": "V种类",
                                "type": 16,
                                "defaultValue": ""
                            }
                        ]
                    }
                ]
            },
            {
                name: '蒲公英',
            },
            {
                name: '闪电_1',
                "params": [{ "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 0.7 }] }]
            },
            {
                name: '闪电_2',
                "params": [{ "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 0.7 }] }]
            },
            {
                name: '闪电_3',
                "params": [{ "functionName": "速率", "functionParams": [{ "name": "v速率", "type": 3, "defaultValue": 0.7 }] }]
            },
            {
                name: '雾气',
            },

        ]
    }
]

export default particles;