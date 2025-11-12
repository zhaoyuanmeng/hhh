export const archiverData = {
  高速公路档案: {
    高速基本情况: [
      { prop: 'title', label: '单位\\内容(名称)' },
      { prop: 'qidian', label: '起点(公里)' },
      { prop: 'zhidian', label: '止点(公里)' },
      { prop: 'TotalLengthOfMileage', label: '全长(公里)' },
      {
        label: '出入口',
        children: [
          { prop: '1', label: '高速互通' },
          { prop: '2', label: '收费站' }
        ]
      },
      { prop: '3', label: '服务区' },
      {
        label: '桥梁',
        children: [
          { prop: '4', label: '上跨桥' },
          { prop: '5', label: '其他桥' }
        ]
      },
      {
        label: '涵洞',
        children: [
          { prop: '6', label: '车涵' },
          { prop: '7', label: '人涵' },
          { prop: '8', label: '水涵' }
        ]
      },
      { prop: '9', label: '隧道' },
      { prop: '10', label: '制高点' },
      {
        label: '危爆物品厂库',
        children: [
          { prop: '11', label: '油库' },
          { prop: '12', label: '加油站' },
          { prop: '13', label: '液化气站' },
          { prop: '14', label: '爆炸物品' }
        ]
      },
      { prop: '15', label: '复杂路段' },
      { prop: '16', label: '复杂村镇' },
      { prop: '17', label: '在册无人机' },
      { prop: '18', label: '无人机飞手' },
      { prop: '19', label: '无人机销售维修培训网点' },
      { prop: '20', label: '重点人口' },
      { prop: '21', label: '涉恐重点人' },
      { prop: '22', label: '常住外籍人员' },
      { prop: 'beizhu', label: '备注' }
    ],
    高速出入口: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'suoshuxiaqu', label: '所属辖区' },
      { prop: 'gaosuguanxiadadui', label: '高速管辖大队' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速服务区: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'suoshuxiaqu', label: '所属辖区' },
      { prop: 'shifouhutong', label: '是否互通' },
      { prop: 'gaosuguanxiadadui', label: '高速管辖大队' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速桥梁: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'changdu', label: '长度(米)' },
      { prop: 'leibie', label: '类别' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速涵洞: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'leibie', label: '类别' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速隧道: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'zhangdu', label: '长度(米)' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速两侧制高点: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      {
        label: '与高速关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'gaodu', label: '高度(米)' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速两侧危爆物品厂库: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'chuliang', label: '储量' },
      {
        label: '与高速关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'suoshudanwei', label: '所属单位' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速复杂路段: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'leibie', label: '类别' },
      {
        label: '与高速关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速沿线复杂村镇: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      {
        label: '负责人',
        children: [
          { prop: 'xingming', label: '姓名' },
          { prop: 'lianxifangshi', label: '联系方式' }
        ]
      },
      {
        label: '与高速关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速在册无人机: [
      { prop: 'pinpaixinghao', label: '品牌型号' },
      { prop: 'suoshuquxian', label: '所属区县' },
      { prop: 'suoshudanwei', label: '所属单位' },
      { prop: 'shiyongrenxingming', label: '使用人姓名' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    '高速无人机飞手(驾驶员)': [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'suoshudanweihuogeren', label: '所属单位或个人' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    '高速无人机销售、维修、培训网点': [
      { prop: 'wangdianmingcheng', label: '网点名称' },
      { prop: 'leibie', label: '类别' },
      { prop: 'dianzhuxingming', label: '店主姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'wangdiandizhi', label: '网点地址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高速重点人员汇总: [
      { prop: 'leibie', label: '类别' },
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    高速重点人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    高速涉恐人员汇总: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'jiguan', label: '籍贯' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    高速涉恐人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    高速沿线常住外籍人员汇总: [
      { prop: 'waiwenxingming', label: '外文名' },
      { prop: 'zhongwenxingming', label: '中文名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'shoujihaoma', label: '手机号码' },
      { prop: 'beizhu', label: '备注' }
    ],
    高速沿线常住外籍人员登记: [
      { prop: 'zhongwenxingming', label: '中文姓名' },
      { prop: 'waiwenxingming', label: '外文姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'rujingshijian', label: '入境时间' },
      { prop: 'ruzhushijian', label: '入住时间' },
      { prop: 'laihuashiyou', label: '来华事由' },
      { prop: 'qianzhengzhonglei', label: '签证种类' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'qianzhengdanwei', label: '签证单位' },
      { prop: 'qianzhengyouxiaoqi', label: '签证有效期' },
      { prop: 'jiedaidanwei', label: '接待单位' },
      { prop: 'nilikaishijian', label: '拟离开时间' },
      { prop: 'zhuzhi', label: '住址' }
    ],
    高速执勤人员政审: [
      { prop: 'danwei', label: '单位' },
      { prop: 'zhengshenqingkuang', label: '政审情况' }
    ],
    高速警卫力量: [
      { prop: 'jingweiguige', label: '警卫规格' },
      { prop: 'zubie', label: '组别' },
      {
        label: '警力部署',
        children: [
          { prop: 'zhihuililiang', label: '指挥力量' },
          { prop: 'gudingshao', label: '固定哨' },
          { prop: 'youdongshao', label: '游动哨' },
          { prop: 'shekongshao', label: '社控哨' },
          { prop: 'xunluoshao', label: '巡逻哨' },
          { prop: 'zhigaodianshao', label: '制高点哨' },
          { prop: 'kuaifanliliang', label: '快反力量(机动)' },
          { prop: 'wurenjifanzhililiang', label: '无人机反制力量' },
          { prop: 'heji', label: '合计' }
        ]
      },
      {
        label: '负责人',
        children: [
          { prop: 'xingming', label: '姓名' },
          { prop: 'zhiwu', label: '职务' },
          { prop: 'shoujihao', label: '手机号' }
        ]
      }
    ],
    高速交界结合部协议: [
      { prop: 'mingcheng', label: '高速名称' },
      { prop: 'xieyishuangfangmingcheng', label: '协议双方(单位)名称' },
      { prop: 'jiehebuweizhi', label: '结合部位置(公里处)' },
      { prop: 'jiaojiedidian', label: '交接地点' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'qiandingshijian', label: '签订时间' },
      { prop: 'beizhu', label: '备注' }
    ],
    高速档案修订: [
      { prop: 'xiudingshijian', label: '修订时间' },
      { prop: 'xiudingneirong', label: '修订内容' },
      { prop: 'xiudingren', label: '修订人' },
      { prop: 'beizhu', label: '备注' }
    ]
  },
  高速铁路档案: {
    高铁基本情况: [
      { prop: 'title', label: '单位\\内容(名称)' },
      { prop: 'qidian', label: '起点(公里)' },
      { prop: 'zhidian', label: '止点(公里)' },
      { prop: 'TotalLengthOfMileage', label: '全长(公里)' },
      { prop: 'shiguan', label: '实管(公里)' },
      { prop: 'chezhan', label: '车站' },
      {
        label: '桥梁',
        children: [
          { prop: '1', label: '上跨桥' },
          { prop: '2', label: '其他桥' }
        ]
      },
      {
        label: '涵洞',
        children: [
          { prop: '3', label: '车涵' },
          { prop: '4', label: '人涵' },
          { prop: '5', label: '水涵' }
        ]
      },
      { prop: '6', label: '隧道' },
      { prop: '7', label: '下穿道路' },
      { prop: '8', label: '制高点' },
      {
        label: '危爆物品厂库',
        children: [
          { prop: '9', label: '油库' },
          { prop: '10', label: '加油站' },
          { prop: '11', label: '液化气站' },
          { prop: '12', label: '爆炸物品' }
        ]
      },
      { prop: '13', label: '复杂路段' },
      { prop: '14', label: '复杂村镇' },
      { prop: '15', label: '低路基路段' },
      { prop: '16', label: '四电所' },
      { prop: '17', label: '基站' },
      { prop: '18', label: '疏散梯' },
      { prop: '19', label: '视频监控点位' },
      { prop: '20', label: '在册无人机' },
      { prop: '21', label: '无人机飞手' },
      { prop: '22', label: '无人机销售维修培训网点' },
      { prop: '23', label: '无人机反制点' },
      { prop: '24', label: '重点人口' },
      { prop: '25', label: '涉恐重点人' },
      { prop: '26', label: '常住外籍人员' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁车站: [
      { prop: 'mingcheng', label: '站名' },
      { prop: 'weizhi', label: '位置(公里)' },
      {
        label: '起止点',
        children: [
          { prop: 'qidian', label: '起点(公里)' },
          { prop: 'zhidian', label: '止点(公里)' }
        ]
      },
      { prop: 'zhanneijuli', label: '站内距离(公里)' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'suoshuxianqu', label: '所属县区' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁桥梁: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'changdu', label: '长度(米)' },
      { prop: 'leibie', label: '类别' },
      { prop: 'shudigonganjiguan', label: '管辖公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁涵洞: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'huwangneiwai', label: '护网内外' },
      { prop: 'leibie', label: '类别' },
      { prop: 'shudigonganjiguan', label: '管辖公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁隧道: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'zhangdu', label: '长度(米)' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁下穿道路: [
      { prop: 'mingcheng', label: '道路名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'kanshouqingkuang', label: '看守情况' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁两侧制高点: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      {
        label: '与铁路关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'gaodu', label: '高度(米)' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁两侧危爆物品厂库: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'chuliang', label: '储量' },
      { prop: 'leibie', label: '类别' },
      {
        label: '与铁路',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'suoshudanwei', label: '所属单位' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁复杂路段: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'leibie', label: '类别' },
      {
        label: '与铁路关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁沿线复杂村镇: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      {
        label: '负责人',
        children: [
          { prop: 'xingming', label: '姓名' },
          { prop: 'lianxifangshi', label: '联系方式' }
        ]
      },
      {
        label: '与铁路关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁低路基路段: [
      { prop: 'qidian', label: '起点(公里)' },
      { prop: 'zhidian', label: '止点(公里)' },
      { prop: 'zhangdu', label: '长度(米)' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' }
    ],
    高铁四电所: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'kanshouqingkuang', label: '看守情况' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁基站: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'kanshouqingkuang', label: '看守情况' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁疏散梯: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'kanshouqingkuang', label: '看守情况' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁沿线监控设施: [
      { prop: 'weizhi', label: '位置(公里)' },
      {
        label: '与铁路关系',
        children: [
          { prop: 'fangxiang', label: '方向' },
          { prop: 'juli', label: '距离(米)' }
        ]
      },
      { prop: 'leibie', label: '类别' },
      { prop: 'xinghao', label: '型号' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁在册无人机: [
      { prop: 'pinpaixinghao', label: '品牌型号' },
      { prop: 'suoshuquxian', label: '所属区县' },
      { prop: 'suoshudanwei', label: '所属单位' },
      { prop: 'shiyongrenxingming', label: '使用人姓名' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    '高铁无人机飞手(驾驶员)': [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'suoshudanweihuogeren', label: '所属单位或个人' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    '高铁无人机销售、维修、培训网点': [
      { prop: 'wangdianmingcheng', label: '网点名称' },
      { prop: 'leibie', label: '类别' },
      { prop: 'dianzhuxingming', label: '店主姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'wangdiandizhi', label: '网点地址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    高铁无人机反制点: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'leibie', label: '类别' },
      { prop: 'shebeifangkongpinduan', label: '设备防控频段' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    高铁重点人员汇总: [
      { prop: 'leibie', label: '类别' },
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    高铁重点人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    高铁涉恐人员汇总: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'jiguan', label: '籍贯' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    高铁涉恐人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    高铁沿线常住外籍人员汇总: [
      { prop: 'waiwenxingming', label: '外文名' },
      { prop: 'zhongwenxingming', label: '中文名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'shoujihaoma', label: '手机号码' },
      { prop: 'beizhu', label: '备注' }
    ],
    高铁沿线常住外籍人员登记: [
      { prop: 'zhongwenxingming', label: '中文姓名' },
      { prop: 'waiwenxingming', label: '外文姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'rujingshijian', label: '入境时间' },
      { prop: 'ruzhushijian', label: '入住时间' },
      { prop: 'laihuashiyou', label: '来华事由' },
      { prop: 'qianzhengzhonglei', label: '签证种类' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'qianzhengdanwei', label: '签证单位' },
      { prop: 'qianzhengyouxiaoqi', label: '签证有效期' },
      { prop: 'jiedaidanwei', label: '接待单位' },
      { prop: 'nilikaishijian', label: '拟离开时间' },
      { prop: 'zhuzhi', label: '住址' }
    ],
    高铁执勤人员政审: [
      { prop: 'danwei', label: '单位' },
      { prop: 'zhengshenqingkuang', label: '政审情况' }
    ],
    高铁警卫力量: [
      { prop: 'jingweiguige', label: '警卫规格' },
      { prop: 'zubie', label: '组别' },
      {
        label: '警力部署',
        children: [
          { prop: 'zhihuililiang', label: '指挥力量' },
          { prop: 'gudingshao', label: '固定哨' },
          { prop: 'youdongshao', label: '游动哨' },
          { prop: 'shekongshao', label: '社控哨' },
          { prop: 'xunluoshao', label: '巡逻哨' },
          { prop: 'zhigaodianshao', label: '制高点哨' },
          { prop: 'kuaifanliliang', label: '快反力量(机动)' },
          { prop: 'wurenjifanzhililiang', label: '无人机反制力量' },
          { prop: 'heji', label: '合计' }
        ]
      },
      {
        label: '负责人',
        children: [
          { prop: 'xingming', label: '姓名' },
          { prop: 'zhiwu', label: '职务' },
          { prop: 'shoujihao', label: '手机号' }
        ]
      }
    ],
    高铁交界结合部协议: [
      { prop: 'mingcheng', label: '高速名称' },
      { prop: 'xieyishuangfangmingcheng', label: '协议双方(单位)名称' },
      { prop: 'jiehebuweizhi', label: '结合部位置(公里处)' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'qiandingshijian', label: '签订时间' },
      { prop: 'beizhu', label: '备注' }
    ],
    高铁档案修订: [
      { prop: 'xiudingshijian', label: '修订时间' },
      { prop: 'xiudingneirong', label: '修订内容' },
      { prop: 'xiudingren', label: '修订人' },
      { prop: 'beizhu', label: '备注' }
    ]
  },
  城市道路档案: {
    城市道路基本情况: [
      { prop: 'title', label: '单位\\内容(名称)' },
      { prop: 'qidian', label: '起点(公里)' },
      { prop: 'zhidian', label: '止点(公里)' },
      { prop: 'TotalLengthOfMileage', label: '全长(公里)' },
      { prop: '1', label: '出入口' },
      { prop: '2', label: '复杂路段' },
      { prop: '3', label: '复杂村镇' },
      { prop: '4', label: '在册无人机' },
      { prop: '5', label: '无人机飞手' },
      { prop: '6', label: '无人机销售维修培训网点' },
      { prop: '7', label: '重点人口' },
      { prop: '8', label: '涉恐重点人' },
      { prop: '9', label: '常住外籍人员' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    // [
    //   { prop: 'title', label: '单位\\内容(名称)' },
    //   { prop: 'qidian', label: '起点(公里)' },
    //   { prop: 'zhidian', label: '止点(公里)' },
    //   { prop: 'TotalLengthOfMileage', label: '全长(公里)' },
    //   { prop: 'churukou', label: '出入口' },
    //   { prop: 'fuzaluduan', label: '复杂路段' },
    //   { prop: 'fuzacunzhen', label: '复杂村镇' },
    //   { prop: 'zaicewurenji', label: '在册无人机' },
    //   { prop: 'wurenjifeishou', label: '无人机飞手' },
    //   { prop: 'wurenjixiaoshouweixiupeixunwangdian', label: '无人机销售维修培训网点' },
    //   { prop: 'zhongdianrenkou', label: '重点人口' },
    //   { prop: 'shekongzhogndianren', label: '涉恐重点人' },
    //   { prop: 'changzhuwaijirenyuan', label: '常住外籍人员' },
    //   { prop: 'beizhu', label: '备注' }
    // ],
    十字路口:[
      { prop: 'title', label: '名称' },
      { prop: 'leixing', label: '类型' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      { prop: 'suoshupaichusuo', label: '所属派出所' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    医院:[
      { prop: 'title', label: '名称' },
      { prop: 'leixing', label: '类型' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      { prop: 'suoshupaichusuo', label: '所属派出所' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    复杂村庄:[
      { prop: 'title', label: '名称' },
      { prop: 'leixing', label: '类型' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      { prop: 'suoshupaichusuo', label: '所属派出所' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    工地:[
      { prop: 'title', label: '名称' },
      { prop: 'leixing', label: '类型' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      { prop: 'suoshupaichusuo', label: '所属派出所' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    复杂地段:[
      { prop: 'title', label: '名称' },
      { prop: 'leixing', label: '类型' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      { prop: 'suoshupaichusuo', label: '所属派出所' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    制高点:[
      { prop: 'title', label: '名称' },
      { prop: 'leixing', label: '类型' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'linjiejianzhushuliang', label: '临街建筑数量' },
      { prop: 'linjieshangpuqingkuang', label: '临街商铺情况' },
      { prop: 'guanxiagonganjiguan', label: '管辖公安机关' },
      { prop: 'suoshupaichusuo', label: '所属派出所' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    其他情况:[
      { prop: 'title', label: '名称' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    丁字路口:[
      { prop: 'title', label: '名称' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    红绿灯:[
      { prop: 'title', label: '名称' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    公交站:[
      { prop: 'title', label: '名称' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    危爆场所:[
      { prop: 'title', label: '名称' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    主线桥:[
      { prop: 'title', label: '名称' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    跨线桥:[
      { prop: 'title', label: '名称' },
      { prop: 'xiangao', label: '限高' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    限高:[
      { prop: 'title', label: '名称' },
      { prop: 'gaodu', label: '高度' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    学校:[
      { prop: 'title', label: '名称' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    重点单位情况登记表: [
      { prop: 'title', label: '名称' },
      { prop: 'guanxiapaichusuo', label: '管辖派出所' },
      { prop: 'danweixingzhi', label: '单位性质' },
      { prop: 'lishuguanxi', label: '隶属关系' },
      { prop: 'leixing', label: '类型' },
      { prop: 'faren', label: '法人' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zongrenshu', label: '总人数' },
      { prop: 'dizhi', label: '地址' },
      { prop: 'zhandimianji', label: '占地面积(㎡)' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      }
    ],
    大路口情况登记表: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'weizhi', label: '位置(公里)' },
      { prop: 'suoshuxiaqu', label: '所属辖区' },
      { prop: 'shudipaichusuo', label: '属地派出所' },
      { prop: 'leixing', label: '类型' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    小路口情况登记表: [
      { prop: 'title', label: '名称' },
      { prop: 'weizhi(gongli)', label: '位置(公里)' },
      { prop: 'suoshuxiangzhen', label: '所属乡镇' },
      { prop: 'shudipaichusuo', label: '属地派出所' },
      { prop: 'leixing', label: '类型' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    危爆场所登记表: [
      { prop: 'title', label: '名称' },
      { prop: 'suochuweizhi', label: '所处位置' },
      { prop: 'jibenqingkuang', label: '基本情况' },
      { prop: 'cunzaiwenti', label: '存在问题' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    危爆物品厂房登记表: [
      { prop: 'title', label: '名称' },
      { prop: 'suochuweizhi', label: '所处位置' },
      { prop: 'jibenqingkuang', label: '基本情况' },
      { prop: 'cunzaiwenti', label: '存在问题' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' }
    ],
    公交站情况登记表: [
      { prop: 'zhanming', label: '站名' },
      { prop: 'cheliangshu', label: '车辆数' },
      { prop: 'sijishu', label: '司机数' },
      { prop: 'xianlushu', label: '线路数' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxifangshi', label: '联系电话' },
      { prop: 'suoshuxianqu', label: '所属县区' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'beizhu', label: '备注' }
    ]
  },
  现场档案: {
    内设机构: [
      { prop: 'mingcheng', label: '部门' },
      { prop: 'renshu', label: '人数' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxidianhua', label: '联系电话' }
    ],
    重要部位: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'suochuweizhi', label: '所处位置' },
      { prop: 'anquansheshi', label: '安全设施' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxidianhua', label: '联系电话' }
    ],
    四邻情况: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'fangwei', label: '方位' },
      { prop: 'zhigaodian', label: '制高点' },
      { prop: 'zhongdianren', label: '重点人' }
    ],
    停车场: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'chewei', label: '车位' }
    ],
    主要建筑: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'jianzhumianji', label: '建筑面积' },
      { prop: 'cengshu', label: '层数' },
      { prop: 'kefang', label: '客房' },
      { prop: 'rongnarenshu', label: '容纳人数' },
      {
        label: '贵宾休息室',
        children: [
          { prop: 'guibinxiuxishi.mingcheng', label: '名称' },
          { prop: 'guibinxiuxishi.zuowei', label: '座位' }
        ]
      },
      {
        label: '会议室',
        children: [
          { prop: 'huiyishi.mingcheng', label: '名称' },
          { prop: 'huiyishi.zuowei', label: '座位' }
        ]
      },
      {
        label: '餐厅',
        children: [
          { prop: 'canting.mingcheng', label: '名称' },
          { prop: 'canting.zuowei', label: '座位' }
        ]
      }
    ],
    现场基本情况: [
      { prop: 'title', label: '名称' },
      { prop: 'guanxiapaichusuo', label: '管辖派出所' },
      { prop: 'danweixingzhi', label: '单位性质' },
      { prop: 'lishuguanxi', label: '隶属关系' },
      { prop: 'faren', label: '法人' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zongrenshu', label: '总人数' },
      { prop: 'dizhi', label: '地址' },
      { prop: 'zhandimianji', label: '占地面积(㎡)' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    现场重点部位: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'suochuweizhi', label: '所处位置' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'jibenqingkuang', label: '基本情况' },
      { prop: 'cunzaiwenti', label: '存在问题' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],

    现场在册无人机: [
      { prop: 'pinpaixinghao', label: '品牌型号' },
      { prop: 'suoshuquxian', label: '所属区县' },
      { prop: 'suoshudanwei', label: '所属单位' },
      { prop: 'shiyongrenxingming', label: '使用人姓名' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    '现场无人机飞手(驾驶员)': [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'suoshudanweihuogeren', label: '所属单位或个人' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    '现场无人机销售、维修、培训网点': [
      { prop: 'wangdianmingcheng', label: '网点名称' },
      { prop: 'leibie', label: '类别' },
      { prop: 'dianzhuxingming', label: '店主姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'wangdiandizhi', label: '网点地址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    现场重点人员汇总: [
      { prop: 'leibie', label: '类别' },
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    现场重点人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    现场涉恐重点人员汇总: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'jiguan', label: '籍贯' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    现场涉恐人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    现场周边常住外籍人员汇总: [
      { prop: 'waiwenxingming', label: '外文名' },
      { prop: 'zhongwenxingming', label: '中文名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'shoujihaoma', label: '手机号码' },
      { prop: 'beizhu', label: '备注' }
    ],
    现场周边常住外籍人员登记: [
      { prop: 'zhongwenxingming', label: '中文姓名' },
      { prop: 'waiwenxingming', label: '外文姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'rujingshijian', label: '入境时间' },
      { prop: 'ruzhushijian', label: '入住时间' },
      { prop: 'laihuashiyou', label: '来华事由' },
      { prop: 'qianzhengzhonglei', label: '签证种类' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'qianzhengdanwei', label: '签证单位' },
      { prop: 'qianzhengyouxiaoqi', label: '签证有效期' },
      { prop: 'jiedaidanwei', label: '接待单位' },
      { prop: 'nilikaishijian', label: '拟离开时间' },
      { prop: 'zhuzhi', label: '住址' }
    ],
    现场人员政审汇总: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'gongzuodanweijizhiwu', label: '工作单位及职务' },
      { prop: 'shenfenzhenghaoma', label: '身份证号码' },
      { prop: 'shoujihaoma', label: '手机号码' }
    ],
    现场人员政审登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生年月' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'minzu', label: '民族' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'canjiagongzuoshijian', label: '参加工作时间' },
      { prop: 'shenfenzhenghaoma', label: '身份证号码' },
      { prop: 'jiguan', label: '籍贯' },
      { prop: 'hujisuozaidi', label: '户籍所在地' },
      { prop: 'xianzhuzhi', label: '现住址' }
    ],
    现场执勤人员政审: [
      { prop: 'danwei', label: '单位' },
      { prop: 'zhengshenqingkuang', label: '政审情况' }
    ],
    现场警卫力量: [
      { prop: 'jingweiguige', label: '警卫规格' },
      { prop: 'zubie', label: '组别' },
      { prop: 'zhizerenwu', label: '职责任务' },
      { prop: 'shaoweileibie', label: '哨位类别' },
      { prop: 'zhezhuangjingli', label: '着装警力' },
      { prop: 'bianyijingli', label: '便衣警力' },
      { prop: 'jinglizongshu', label: '警力总数' },
      { prop: 'paichudanwei', label: '派出单位' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'zhiwu', label: '职务' },
      { prop: 'dianhua', label: '电话' }
    ],
    现场处突力量: [
      { prop: 'danwei', label: '单位' },
      { prop: 'renyuan', label: '人员' },
      { prop: 'zhuangbei', label: '装备' },
      { prop: 'daimingdidian', label: '待命地点' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'zhiwu', label: '职务' },
      { prop: 'lianxidianhua', label: '联系电话' }
    ],
    现场警卫工作制度: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'zhidingshijian', label: '制定时间' },
      { prop: 'beizhu', label: '备注' }
    ],
    现场有关人员通讯联络: [
      { prop: 'danwei', label: '单位' },
      { prop: 'xingming', label: '姓名' },
      { prop: 'zhiwu', label: '职务' },
      { prop: 'dianhua', label: '电话' }
    ],
    现场档案修订: [
      { prop: 'xiudingshijian', label: '修订时间' },
      { prop: 'xiudingneirong', label: '修订内容' },
      { prop: 'xiudingren', label: '修订人' },
      { prop: 'beizhu', label: '备注' }
    ]
  },
  住地档案: {
    内设机构: [
      { prop: 'mingcheng', label: '部门' },
      { prop: 'renshu', label: '人数' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxidianhua', label: '联系电话' }
    ],
    重要部位: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'suochuweizhi', label: '所处位置' },
      { prop: 'anquansheshi', label: '安全设施' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'lianxidianhua', label: '联系电话' }
    ],
    四邻情况: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'fangwei', label: '方位' },
      { prop: 'zhigaodian', label: '制高点' },
      { prop: 'zhongdianren', label: '重点人' }
    ],
    停车场: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'chewei', label: '车位' }
    ],
    主要建筑: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'jianzhumianji', label: '建筑面积' },
      { prop: 'cengshu', label: '层数' },
      { prop: 'kefang', label: '客房' },
      { prop: 'rongnarenshu', label: '容纳人数' },
      {
        label: '贵宾休息室',
        children: [
          { prop: 'guibinxiuxishi.mingcheng', label: '名称' },
          { prop: 'guibinxiuxishi.zuowei', label: '座位' }
        ]
      },
      {
        label: '会议室',
        children: [
          { prop: 'huiyishi.mingcheng', label: '名称' },
          { prop: 'huiyishi.zuowei', label: '座位' }
        ]
      },
      {
        label: '餐厅',
        children: [
          { prop: 'canting.mingcheng', label: '名称' },
          { prop: 'canting.zuowei', label: '座位' }
        ]
      }
    ],
    住地基本情况: [
      { prop: 'title', label: '名称' },
      { prop: 'guanxiapaichusuo', label: '管辖派出所' },
      { prop: 'danweixingzhi', label: '单位性质' },
      { prop: 'lishuguanxi', label: '隶属关系' },
      { prop: 'faren', label: '法人' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zongrenshu', label: '总人数' },
      { prop: 'dizhi', label: '地址' },
      { prop: 'zhandimianji', label: '占地面积(㎡)' },
      {
        label: '经纬度',
        children: [
          { prop: 'jingdu', label: '经度' },
          { prop: 'weidu', label: '纬度' }
        ]
      }
    ],
    住地重点部位: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'suochuweizhi', label: '所处位置' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'jibenqingkuang', label: '基本情况' },
      { prop: 'cunzaiwenti', label: '存在问题' },
      { prop: 'beizhu', label: '备注' }
    ],
    住地在册无人机: [
      { prop: 'pinpaixinghao', label: '品牌型号' },
      { prop: 'suoshuquxian', label: '所属区县' },
      { prop: 'suoshudanwei', label: '所属单位' },
      { prop: 'shiyongrenxingming', label: '使用人姓名' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    '住地无人机飞手(驾驶员)': [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'suoshudanweihuogeren', label: '所属单位或个人' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    '住地无人机销售、维修、培训网点': [
      { prop: 'wangdianmingcheng', label: '网点名称' },
      { prop: 'leibie', label: '类别' },
      { prop: 'dianzhuxingming', label: '店主姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'nianling', label: '年龄' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'lianxifangshi', label: '联系方式' },
      { prop: 'wangdiandizhi', label: '网点地址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    住地重点人员汇总: [
      { prop: 'leibie', label: '类别' },
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    住地重点人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    住地涉恐重点人员汇总: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'jiguan', label: '籍贯' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'beizhu', label: '备注' }
    ],
    住地涉恐人员登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'shenfenzhenghao', label: '身份证号' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'jianguanren', label: '监管人' },
      { prop: 'danwei', label: '单位' },
      { prop: 'lianxifangshi', label: '联系方式' }
    ],
    住地周边常住外籍人员汇总: [
      { prop: 'waiwenxingming', label: '外文名' },
      { prop: 'zhongwenxingming', label: '中文名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'shudigonganjiguan', label: '属地公安机关' },
      { prop: 'shoujihaoma', label: '手机号码' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地周边常住外籍人员登记: [
      { prop: 'zhongwenxingming', label: '中文姓名' },
      { prop: 'waiwenxingming', label: '外文姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生日期' },
      { prop: 'guoji', label: '国籍' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'rujingshijian', label: '入境时间' },
      { prop: 'ruzhushijian', label: '入住时间' },
      { prop: 'laihuashiyou', label: '来华事由' },
      { prop: 'qianzhengzhonglei', label: '签证种类' },
      { prop: 'zhengjianhaoma', label: '证件号码' },
      { prop: 'qianzhengdanwei', label: '签证单位' },
      { prop: 'qianzhengyouxiaoqi', label: '签证有效期' },
      { prop: 'jiedaidanwei', label: '接待单位' },
      { prop: 'nilikaishijian', label: '拟离开时间' },
      { prop: 'zhuzhi', label: '住址' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地人员政审汇总: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'gongzuodanweijizhiwu', label: '工作单位及职务' },
      { prop: 'shenfenzhenghaoma', label: '身份证号码' },
      { prop: 'shoujihaoma', label: '手机号码' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地人员政审登记: [
      { prop: 'xingming', label: '姓名' },
      { prop: 'xingbie', label: '性别' },
      { prop: 'chushengriqi', label: '出生年月' },
      { prop: 'cengyongming', label: '曾用名' },
      { prop: 'minzu', label: '民族' },
      { prop: 'wenhuachengdu', label: '文化程度' },
      { prop: 'zhengzhimianmao', label: '政治面貌' },
      { prop: 'canjiagongzuoshijian', label: '参加工作时间' },
      { prop: 'shenfenzhenghaoma', label: '身份证号码' },
      { prop: 'jiguan', label: '籍贯' },
      { prop: 'hujisuozaidi', label: '户籍所在地' },
      { prop: 'xianzhuzhi', label: '现住址' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地执勤人员政审: [
      { prop: 'danwei', label: '单位' },
      { prop: 'zhengshenqingkuang', label: '政审情况' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地警卫力量: [
      { prop: 'jingweiguige', label: '警卫规格' },
      { prop: 'zubie', label: '组别' },
      { prop: 'zhizerenwu', label: '职责任务' },
      { prop: 'shaoweileibie', label: '哨位类别' },
      { prop: 'zhezhuangjingli', label: '着装警力' },
      { prop: 'bianyijingli', label: '便衣警力' },
      { prop: 'jinglizongshu', label: '警力总数' },
      { prop: 'paichudanwei', label: '派出单位' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'zhiwu', label: '职务' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地处突力量: [
      { prop: 'danwei', label: '单位' },
      { prop: 'renyuan', label: '人员' },
      { prop: 'zhuangbei', label: '装备' },
      { prop: 'daimingdidian', label: '待命地点' },
      { prop: 'fuzeren', label: '负责人' },
      { prop: 'zhiwu', label: '职务' },
      { prop: 'lianxidianhua', label: '联系电话' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地警卫工作制度: [
      { prop: 'mingcheng', label: '名称' },
      { prop: 'zhidingshijian', label: '制定时间' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地有关人员通讯联络: [
      { prop: 'danwei', label: '单位' },
      { prop: 'xingming', label: '姓名' },
      { prop: 'zhiwu', label: '职务' },
      { prop: 'dianhua', label: '电话' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ],
    住地档案修订: [
      { prop: 'xiudingshijian', label: '修订时间' },
      { prop: 'xiudingneirong', label: '修订内容' },
      { prop: 'xiudingren', label: '修订人' },
      { prop: 'beizhu', label: '备注' },
      { prop: 'photo', label: '图片' },
      { prop: 'video', label: '视频' }
    ]
  }
};
