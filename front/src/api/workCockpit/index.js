import request from '@/utils/request'

// 查询全部任务列表
export function getTaskList(data) {
  return request({
    url: '/task/getList',
    method: 'post',
    data
  })
}


// 查询执行中的任务列表
export function getExecutingList() {
  return request({
    url: '/task/getExecutingList',
    method: 'get',
  })
}

// 查询已执行的任务列表
export function getExecutedList() {
  return request({
    url: '/task/getExecutedList',
    method: 'get',
  })
}

// 统计全部任务的等级分类
export function statisticalAllLevel() {
  return request({
    url: '/task/statisticalAllLevel',
    method: 'get',
  })
}

// 统计执行中任务的等级分类
export function statisticalExecutingLevel() {
  return request({
    url: '/task/statisticalExecutingLevel',
    method: 'get',
  })
}

// 统计已执行任务的等级分类
export function statisticalExecutedLevel() {
  return request({
    url: '/task/statisticalExecutedLevel',
    method: 'get',
  })
}

// 统计任务数量
export function statisticalQuantity() {
  return request({
    url: '/task/statisticalQuantity',
    method: 'get',
  })
}

// 获取任务基本规划数据
export function getBasicPlanOfTask(params) {
  return request({
    url: '/task/getBasicPlanOfTask',
    method: 'get',
    params
  })
}

// 获取任务下警力数据
export function getPolicePresenceOfTask(params) {
  return request({
    url: '/task/getPolicePresenceOfTask',
    method: 'get',
    params
  })
}

// 查询任务规划节点数据
export function getTaskPlanNode(params) {
  return request({
    url: '/task-plan/getTaskPlanNode',
    method: 'get',
    params
  })
}

// 获取任务下标绘数据
export function getDrawDataOfTask(params) {
  return request({
    url: '/task/getScenePlotDataOfTask',
    method: 'get',
    params
  })
}

// 获取任务时间线数据
export function getDailyScheduleOfTask(params) {
  return request({
    url: '/task/getDailyScheduleOfTask',
    method: 'get',
    params
  })
}

// 场景下经理部署统计
export function getPoliceStatistics(params) {
  return request({
    url: '/scene-plan/getPoliceStatisticsOfScenePlanNode',
    method: 'get',
    params
  })
}

// 通过id查询
export function getById(params) {
  return request({
    url: '/draw-data/getById',
    method: 'get',
    params
  })
}

// 获取任务资源树
export function getTaskResources(params) {
  return request({
    url: '/task/getTaskResources',
    method: 'get',
    params
  })
}

// 获取场景资源树
export function getSceneResources(params) {
  return request({
    url: '/scene-info/getSceneResources',
    method: 'get',
    params
  })
}

// 获取地图点位信息
export function getPointInfo(params) {
  return request({
    url: '/point-info/getDetail',
    method: 'get',
    params
  })
}

// 根据多场景ID查询资源树
export function getMultiSceneResources(params) {
  return request({
    url: '/scene-info/getMultiSceneResources',
    method: 'get',
    params
  })
}

// 根据任务ID和场景名称获取场景数据
export function getSceneListByTaskIdAndSceneName(params) {
  return request({
    url: '/scene-info/getSceneListByTaskIdAndSceneName',
    method: 'get',
    params
  })
}

// 根据场景ID查询当前场景信息
export function getSceneDataForIdAndToDtaw(data) {
  return request({
    url: '/scene-info/getBasicDrawDataOfScene',
    method: 'get',
    params:data
  })
}

// 获取指定场景类型的标绘数据
export function getRelatedFeatureDataOfTask(data) {
  return request({
    url: '/task/getRelatedFeatureDataOfTask',
    method: 'get',
    params:data
  })
}

