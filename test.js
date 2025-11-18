/**
 * 按公里数范围分组处理警力数据
 * @param {Array} rawData - 原始数据数组
 * @param {Array} distanceGroups - 公里数分组规则列表，格式：[{name, start, end}, ...]
 * @returns {Array} 处理后的完整数据
 */
let processByDistanceGroups = (rawData, distanceGroups) => {
  // 兼容 rawData 为空或非数组的情况
  if (!rawData || !Array.isArray(rawData)) {
    return rawData ? JSON.parse(JSON.stringify(rawData)) : [];
  }

  // 深拷贝数据，避免修改原数据（Vue3 响应式安全）
  const processedData = JSON.parse(JSON.stringify(rawData));

  processedData.forEach((item) => {
    // 跳过非对象类型的项
    if (item === null || typeof item !== 'object') return;

    // 安全获取 policeData（避免 item.policeData 不存在导致的报错）
    const policeData = item.policeData || {};

    // 只处理 policeData.fangxian 为空的对象（兼容 policeData.fangxian 不存在的情况）
    if (policeData.fangxian !== '') return;

    // 安全获取 groupData（默认空数组，避免 undefined 报错）
    const { groupData = [] } = policeData;
    // 确保 groupData 是数组（兼容非数组的异常情况）
    const safeGroupData = Array.isArray(groupData) ? groupData : [];

    // 分离：需要重新分组的项（group 为空）和保留项（group 非空）
    const emptyGroupItems = safeGroupData.filter(g => {
      // 兼容 g 不是对象的情况
      if (g === null || typeof g !== 'object') return false;
      return g.group === '';
    });
    const keepGroupItems = safeGroupData.filter(g => {
      if (g === null || typeof g !== 'object') return false;
      return g.group !== '';
    });

    // 收集所有待分组的警力部署项（兼容 jinglibushu 非数组的情况）
    const allJinglibushu = emptyGroupItems.flatMap(g => {
      if (g?.jinglibushu && Array.isArray(g.jinglibushu)) {
        return g.jinglibushu;
      }
      return []; // 非数组则视为空
    });
    if (allJinglibushu.length === 0) return;

    // 按公里数范围分组（先初始化所有分组和一个未匹配组）
    const groupMap = {};
    // 兼容 distanceGroups 非数组的情况
    const safeDistanceGroups = Array.isArray(distanceGroups) ? distanceGroups : [];
    // 初始化规则分组
    safeDistanceGroups.forEach(group => {
      // 确保分组规则是有效的对象（包含 name/start/end）
      if (group && typeof group === 'object' && group.name !== undefined) {
        groupMap[group.name] = [];
      }
    });
    // 初始化未匹配组（group 为空字符串）
    groupMap[''] = [];

    // 遍历警力项，分配到对应分组
    allJinglibushu.forEach(jl => {
      // 跳过非对象的警力项
      if (jl === null || typeof jl !== 'object') return;

      // 从 weizhi 中提取公里数（匹配数字+公里格式，如 "83.871公里..."）
      // 兼容 weizhi 不存在或非字符串的情况
      const weizhi = typeof jl.weizhi === 'string' ? jl.weizhi : '';
      const kmMatch = weizhi.match(/(\d+(\.\d+)?)公里/);
      if (!kmMatch) {
        // 未匹配到公里数，放入未匹配组
        groupMap[''].push(jl);
        return;
      }

      const km = Number(kmMatch[1]); // 提取公里数值
      // 兼容非数字的情况
      if (isNaN(km)) {
        groupMap[''].push(jl);
        return;
      }

      let matched = false;

      // 匹配公里数范围分组
      safeDistanceGroups.forEach(group => {
        // 确保分组规则的 start 和 end 是数字
        if (
          group && typeof group === 'object' &&
          typeof group.start === 'number' &&
          typeof group.end === 'number'
        ) {
          // 范围规则：start <= km < end
          if (km >= group.start && km < group.end) {
            groupMap[group.name].push(jl);
            matched = true;
          }
        }
      });

      // 未匹配任何范围，放入未匹配组
      if (!matched) {
        groupMap[''].push(jl);
      }
    });

    // 转换分组结果为原数据结构的 groupData 格式
    const newGroupItems = [];
    Object.entries(groupMap).forEach(([groupName, jinglibushuList]) => {
      // 跳过空分组（避免生成无数据的组）
      if (!jinglibushuList.length) return;

      // 计算分组内的统计数据（与原结构保持一致）
      // 1. 按 leixing 统计警力类型数量
      const policeTypeOfGroup = jinglibushuList.reduce((stats, item) => {
        // 兼容 item 非对象或 leixing/num 异常的情况
        if (item === null || typeof item !== 'object') return stats;
        const post = typeof item.leixing === 'string' ? item.leixing : '未知类型';
        const num = typeof item.num === 'number' ? item.num : 0;

        const existing = stats.find(s => s.post === post);
        if (existing) {
          existing.num += num;
        } else {
          stats.push({ post, num });
        }
        return stats;
      }, []);

      // 2. 计算分组总警力数
      const policeNum = policeTypeOfGroup.reduce((total, item) => total + (item.num || 0), 0);

      // 3. 生成分组项（保持原结构字段）
      newGroupItems.push({
        jinglibushu: jinglibushuList,
        groupDesc: '', // 可根据需求自定义描述
        policeNum,
        policeTypeOfGroup,
        group: groupName // 分组名称（规则中的 name 或空字符串）
      });
    });

    // 合并保留项和新分组项，更新 groupData
    item.policeData = {
      ...policeData, // 保留原有其他属性
      groupData: [...keepGroupItems, ...newGroupItems]
    };
  });

  return processedData;
};

let rawData = [
  {
    "id": "36e92cf805177e26d30c22587a5fc5b3",
    "planNode": "警力部署",
    "data": {
      "features": [
        {
          "userData": "固定哨",
          "featureTypName": "高铁基站",
          "num": 4,
          "source": "auto",
          "featureTypeId": "1bad4da452494a9cb6ee2f1799984ce9"
        }
      ]
    },
    "sceneId": "12a0c92e3d7d8b0e5bc6907d6c0ca7ce",
    "policeData": {
      "postStatistics": [
        {
          "post": "固定哨",
          "num": 37
        },
        {
          "post": "交通哨",
          "num": 1
        }
      ],
      "policeTypeOfLine": {
        "onduty": 38,
        "total": 38,
        "emergency": 0
      },
      "groupData": [
        {
          "jinglibushu": [
            {
              "leixing": "固定哨",
              "weizhi": "83.871公里高铁基站位置",
              "buildname": "",
              "num": 4,
              "groupid": "police",
              "id": "159b8c8f6690a20e1453b52d8a1711dc",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "78.295公里高铁基站位置",
              "buildname": "",
              "num": 4,
              "groupid": "police",
              "id": "d7d004536d49945d7ad9b5de0e82244c",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "90公里高铁基站位置",
              "buildname": "",
              "num": 4,
              "groupid": "police",
              "id": "e28744ec4ab91812d03884441f3f631b",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "102.75公里高铁基站位置",
              "buildname": "",
              "num": 4,
              "groupid": "police",
              "id": "539206094b2cfe585175eee1fb075e07",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "109.25公里高铁基站位置",
              "buildname": "",
              "num": 4,
              "groupid": "police",
              "id": "75a1ea8ad346896b4fa6c49b027ef891",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "114.8公里高铁基站位置",
              "buildname": "",
              "num": 4,
              "groupid": "police",
              "id": "133cdaea093740abb1f952016da84d00",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "121.58公里高铁基站位置",
              "buildname": "",
              "num": 4,
              "groupid": "police",
              "id": "5d1ad996358f14c8ad421ef593634ebb",
              "floornum": "",
              "fangxian": ""
            }
          ],
          "groupDesc": "",
          "policeNum": 28,
          "policeTypeOfGroup": [
            {
              "post": "固定哨",
              "num": 28
            }
          ],
          "group": ""
        },
        {
          "jinglibushu": [
            {
              "leixing": "固定哨",
              "weizhi": "90公里高铁基站位置",
              "buildname": "",
              "num": 3,
              "groupid": "police",
              "id": "79eff233d0a02e0d9d3fa18ae0907b72",
              "floornum": "",
              "fangxian": ""
            }
          ],
          "groupDesc": "",
          "policeNum": 3,
          "policeTypeOfGroup": [
            {
              "post": "固定哨",
              "num": 3
            }
          ],
          "group": "123333"
        },
        {
          "jinglibushu": [
            {
              "leixing": "交通哨",
              "weizhi": "123",
              "buildname": "",
              "num": 1,
              "groupid": "police",
              "id": "police1763018331495",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "83.871公里高铁基站位置",
              "buildname": "",
              "num": 3,
              "groupid": "police",
              "id": "5f2f0ce6e13b4dcdb7bb6da861f0f2d0",
              "floornum": "",
              "fangxian": ""
            },
            {
              "leixing": "固定哨",
              "weizhi": "78.295公里高铁基站位置",
              "buildname": "",
              "num": 3,
              "groupid": "police",
              "id": "a4c7146a337374e50c2fc11c26394dd7",
              "floornum": "",
              "fangxian": ""
            }
          ],
          "groupDesc": "",
          "policeNum": 7,
          "policeTypeOfGroup": [
            {
              "post": "固定哨",
              "num": 6
            },
            {
              "post": "交通哨",
              "num": 1
            }
          ],
          "group": "22"
        }
      ],
      "fangxian": ""
    },
    "placePoliceData": [
      {
        "weizhi": "123",
        "data": [
          {
            "leixing": "交通哨",
            "buildName": "",
            "num": 1,
            "groupId": "",
            "id": "police1763018331495",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "83.871公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 3,
            "groupId": "",
            "id": "5f2f0ce6e13b4dcdb7bb6da861f0f2d0",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "78.295公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 3,
            "groupId": "",
            "id": "a4c7146a337374e50c2fc11c26394dd7",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "90公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 3,
            "groupId": "",
            "id": "79eff233d0a02e0d9d3fa18ae0907b72",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "83.871公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 4,
            "groupId": "",
            "id": "159b8c8f6690a20e1453b52d8a1711dc",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "78.295公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 4,
            "groupId": "",
            "id": "d7d004536d49945d7ad9b5de0e82244c",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "90公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 4,
            "groupId": "",
            "id": "e28744ec4ab91812d03884441f3f631b",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "102.75公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 4,
            "groupId": "",
            "id": "539206094b2cfe585175eee1fb075e07",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "109.25公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 4,
            "groupId": "",
            "id": "75a1ea8ad346896b4fa6c49b027ef891",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "114.8公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 4,
            "groupId": "",
            "id": "133cdaea093740abb1f952016da84d00",
            "floorNum": ""
          }
        ]
      },
      {
        "weizhi": "121.58公里高铁基站位置",
        "data": [
          {
            "leixing": "固定哨",
            "buildName": "",
            "num": 4,
            "groupId": "",
            "id": "5d1ad996358f14c8ad421ef593634ebb",
            "floorNum": ""
          }
        ]
      }
    ],
    "statisticalData": {
      "onduty": 38,
      "total": 38,
      "emergency": 0
    }
  }
]

let distanceGroups = [
  { name: '0-50公里', start: 0, end: 50 },
  { name: '50-100公里', start: 50, end: 100 },
  { name: '100-150公里', start: 100, end: 150 }
];

console.log(processByDistanceGroups(rawData, distanceGroups));

