import request from '@/utils/request'

// 基础数据点位新增
export function addPoint(params) {
  return request({
    url: '/point-info/add',
    method: 'post',
    data: params
  })
}

// 获取点位类型接口 根据类型 不类型调用不同接口
export function getPointType(type) {
  return request({
    url: type==='gt'?'/point-info/getRailwayPointType':type==='xc'?'/point-info/getSitePointType':type==='zd'?'/point-info/getResidencePointType':'/point-info/getHighwayPointType',
    method: 'get'
  })
}

// 下载导入模板
export function downloadTemplte(datas) {
  return request({
    url: '/point-info/downloadTemplate',
    method: 'get',
    params:datas
  })
}
// 上传接口
export function deleteTask(id) {
  return request({
    url: '/task/delete',
    method: 'get',
    params: id
  })
}

// 单条高速的列表查询
export function quertListForTypeId(type) {
  return request({
    url:  type==='gt'?'/point-info/getRailwayList':type==='xc'?'/point-info/getSiteList':type==='zd'?'/point-info/getResidenceList':type==='city'?'/point-info/getRoadList':'/point-info/getHighwayList',
    method: 'get',
  })
}

// 数据统计
export function getDataStatistics(type) {
  return request({
    url: type==='gt'?'/point-info/getRailwayStatistics':type==='xc'?'/point-info/getSiteStatistics':type==='zd'?'/point-info/getResidenceStatistics':type==='city'?'/point-info/getRoadStatistics':'/point-info/getHighwayStatistics',
    method: 'get',
  })
}
// 数据详情
export function getPointInfo(type,datas) {
  return request({
    url: type==='gt'?'/point-info/getRailwayPointData':type==='xc'?'/point-info/getSitePointData':type==='zd'?'/point-info/getResidencePointData':type==='city'?'/point-info/getRoadPointData':'/point-info/getHighwayPointData',
    method: 'get',
    params: datas
  })
}
// 基础数据点位修改
export function updatePoint(params) {
    return request({
      url: '/point-info/update',
      method: 'post',
      data: params
    })
  }

// 点位数据查询
export function searchPointForName(name) {
  return request({
    url: '/point-info/getPointsByName',
    method: 'get',
    params: name
  })
}

// 点位纠正
export function updatePointLocation(data) {
  return request({
    url: '/point-info/updatePosition',
    method: 'get',
    params: data
  })
}

// 地图框选
export function queryIntersectsData(params) {
  return request({
    url: '/point-info/queryIntersectsData',
    method: 'post',
    data: params
  })
}

// 获取社区/楼栋标签
export function getProjectShowData() {
  return request({
    url: '/building-info/getBuildingData',
    method: 'get',
  })
}
// 获取楼栋楼层数
export function getFloorsDataApi(data) {
  return request({
    url: '/building-info/getFloorNumber',
    method: 'get',
    params:data
  })
}
// 获取楼层租赁数据 不可炸开楼层
export function getFloorsBoomDataApi(data) {
  return request({
    url: '/building-info/getRoomLeaseData',
    method: 'get',
    params:data
  })
}
// 获取租户数据 可炸开楼层
export function getFloorsNoBoomDataApi(data) {
  return request({
    url: '/building-info/getTenantData',
    method: 'get',
    params:data
  })
}
// 获取获取楼层租户详细信息 可炸开楼层
export function getFloorsUserInfoApi(data) {
  return request({
    url: '/building-info/getFloorTenantData',
    method: 'get',
    params:data
  })
}
//获取居民楼信息
export function getCommunityInfo(data) {
  return request({
    url: '/building-info/getResidentialBuildingInfo',
    method: 'get',
    params:data
  })
}

//获取居民楼用户信息
export function getCommunityUserInfo(data) {
  return request({
    url: '/building-info/getHouseResidenceData',
    method: 'get',
    params:data
  })
}

//获取任务统计数据
export function getTaskStatisticsData(data) {
  return request({
    url: '/task-statistics/getStatisticsData',
    method: 'get',
    params:data
  })
}

//获取居民数据
export function getPeopleApiData(data) {
  return request({
    url: '/building-info/getResidenceData',
    method: 'get',
    params:data
  })
}

//获取商业数据
export function getBusinessApiData(data) {
  return request({
    url: '/building-info/getMerchantData',
    method: 'get',
    params:data
  })
}

//新增任务统计数据
export function addTaskStaticData(params) {
  return request({
    url: '/task-data/add',
    method: 'post',
    data:params
  })
}
//编辑任务统计数据
export function updateTaskStaticData(params) {
  return request({
    url: '/task-data/update',
    method: 'post',
    data:params
  })
}
//删除任务统计数据
export function delTaskStaticData(data) {
  return request({
    url: '/task-data/delete',
    method: 'get',
    params:data
  })
}

//按年份获取任务统计数据
export function getTaskDataForYear(data) {
  return request({
    url: '/task-data/statisticsByYear',
    method: 'get',
    params:data
  })
}
// 任务统计分页效果
export function getTaskDataListPage(params) {
  return request({
    url: '/task-data/getPage',
    method: 'post',
    data:params
  })
}
//按类型获取任务统计数据
export function getIndexTaskData(data) {
  return request({
    url: '/task-data/statisticsByType',
    method: 'get',
    params:data
  })
}
//获取楼栋以及商业数据
export function getCompanyBusinessFloorsDatas(data) {
  return request({
    url: '/building-info/getBuildingAndEnterpriseData',
    method: 'get',
    params:data
  })
}

//获取社区楼栋数据
export function getCommunityDatas() {
  return request({
    url: '/building-info/getResidentialBuildingData',
    method: 'get',
  })
}
//获取数据导入记录列表
export function getDataImportList() {
  return request({
    url: '/import-records/getList',
    method: 'post',
    data:{}
  })
}
//问题清单列表
export function getProblemList(data) {
  return request({
    url: '/issues-data/getList',
    method: 'get',
    params:data
  })
}

//新增问题数据
export function addProblem(data) {
  return request({
    url: '/issues-data/add',
    method: 'post',
    data
  })
}

//编辑问题数据
export function editProblem(data) {
  return request({
    url: '/issues-data/update',
    method: 'post',
    data
  })
}

//查看问题数据
export function checkProblemInfo(data) {
  return request({
    url: '/issues-data/getById',
    method: 'get',
    params:data
  })
}

//问题数量
export function getProblemNum() {
  return request({
    url: '/issues-data/getIssuesNum',
    method: 'get',
  })
}
//删除问题数据
export function delProblem(params) {
  return request({
    url: '/issues-data/delete',
    method: 'get',
    params
  })
}

//获取井盖数据
export function getJGdata(params) {
  return request({
    url: '/cover-info/getList ',
    method: 'get',
    params
  })
}

//获取社区楼栋数据
export function getCommunityFloorsData(params) {
  return request({
    url: '/building-info/getResidentialBuilding',
    method: 'get',
    params
  })
}