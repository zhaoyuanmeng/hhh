import request from '@/utils/request'

// 任务推演查询任务列表
export function queryTaskList({ }) {
  return request({
    url: '/task/getList',
    method: 'post',
    data: {}
  })
}

// 新增任务列表
export function addTask(datas) {
  return request({
    url: '/task/add',
    method: 'post',
    data: datas
  })
}

// 修改任务列表
export function updateTask(datas) {
  return request({
    url: '/task/update',
    method: 'post',
    data: datas
  })
}
// 删除任务
export function deleteTask(id) {
  return request({
    url: '/task/delete',
    method: 'get',
    params: id
  })
}

// 点击查询任务详情
export function queryTaskInfo(id) {
  return request({
    // url: '/task/getById',
    url:'/task/getTaskDetail',
    method: 'get',
    params: id
  })
}

// 新增场景
export function addScreen(params) {
  return request({
    url: '/scene/add',
    method: 'post',
    data: params
  })
}
// 编辑场景
export function updateScreen(params) {
  return request({
    url: '/scene/update',
    method: 'post',
    data: params
  })
}
// 删除场景
export function deleteScreen(id) {
  return request({
    url: '/scene/delete',
    method: 'get',
    params: id
  })
}

// 保存漫游数据
export function saveRoamData(params) {
  return request({
    url: '/scene/update',
    method: 'post',
    data: params
  })
}

// 查询场景列表数据
export function queryScreenList(params) {
  return request({
    url: '/scene/getList',
    method: 'post',
    data: params
  })
}

// 查询场景列表数据
export function downLoadTaskLines(params) {
  return request({
    url: '/scene-info/exportSceneResourcesToZip',
    method: 'post',
    responseType: 'blob',
    data: params
  })
}