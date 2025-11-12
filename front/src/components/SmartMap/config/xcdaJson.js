/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-17 18:49:29
 * @LastEditTime: 2024-06-17 19:32:04
 * @LastEditors: Alex
 */
export default {
  xcdaData: {
    // 高速铁路左侧面板数据
    xcdasl: 2,
    zddw: 2,
    name: "现场档案",
    id: "8374ec30c3724f179074c9186bd716a9",
    lcqc: 2400,
    list: [
      {
        qidian: "石家庄",
        zddwgs: 1,
        id: "5F2D2A104B9735917B9E0F90CE67244E",
        zhidian: "雄安",
        title: "会展中心",
        changdu: 2400,
        info: "雄安新区会展中心包括xxx、xxx、xxx",
      },
      {
        qidian: "石家庄",
        zddwgs: 1,
        id: "469F349543004ABEBACB3389963D4EC5",
        zhidian: "雄安",
        title: "市民服务中心",
        changdu: 2400,
        info: "雄安新区市民服务中心包括xxx、xxx、xxx",
      }
    ],
  },
  // 右侧面板详情数据根据id拿到对应的数据 目前模拟了两条数据
  '5F2D2A104B9735917B9E0F90CE67244E': [
    {
      count: 1,
      id: "5F2D2A104B9735917B9E0F90CE67244E",
      name: "重点部位",
      children: [
        {
          id: "xcda1",
          group: "xcda",
          name: "会展中心",
          imgName: "重点部位",
          sffc:true,
          juli: "9",
          coordinates: [492009.645625,4324414.72,68.5759375],
          info:{
              "fzr":"负责人",
              "dh":"电话",
               "jbqk":"所属县区",
              "czwt":"经纬度",
              "bz":"备注"
          },
          data: {
              "fzr":"老总",
              "dh":"15936276789",
               "jbqk":"基本情况信息xxx",
               "czwt":"存在的问题是什么情况",
              "bz":"备注"
          },
        }
      ],
    },
  ],
  "469F349543004ABEBACB3389963D4EC5":[
    {
      count: 1,
      id: "469F349543004ABEBACB3389963D4EC5",
      name: "重点部位",
      children: [
        {
          id: "xcda122",
          group: "xcda",
          name: "会展中心",
          imgName: "重点部位",
          sffc:true,
          juli: "9",
          coordinates: [492009.645625,4324414.72,68.5759375],
          info:{
              "fzr":"负责人",
              "dh":"电话",
               "jbqk":"所属县区",
              "czwt":"经纬度",
              "bz":"备注"
          },
          data: {
              "fzr":"老总",
              "dh":"15936276789",
               "jbqk":"基本情况信息xxx",
               "czwt":"存在的问题是什么情况",
              "bz":"备注"
          },
        }
      ],
    }
  ]
}
