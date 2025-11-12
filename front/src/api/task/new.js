import request from '@/utils/request'
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
export function addScreenNew(params) {
  return request({
    url: '/scene-info/add',
    method: 'post',
    data: params
  })
}
// 编辑场景
export function updateScreenNew(params) {
  return request({
    url: '/scene-info/update',
    method: 'post',
    data: params
  })
}
// 删除场景
export function deleteScreenNew(id) {
  return request({
    url: '/scene-info/delete',
    method: 'get',
    params: id
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

// 根据任务id查询场景列表
export function searchScreenForId(id) {
    return request({
      url: '/scene-info/getListByTaskId',
      method: 'get',
      params: id
    })
  }