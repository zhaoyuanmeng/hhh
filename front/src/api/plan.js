import request from '@/utils/request'

// 获取方案数量
export function getPlanNumber() {
    return request({
        url: '/scene-info/getSchemeNum',
        method: 'get',
    })
}

// 新增方案
export function addPlanApi(datas) {
    return request({
        url: '/scene-info/addScheme',
        method: 'post',
        data: datas
    })
}

// 编辑方案
export function updatePlanApi(datas) {
    return request({
        url: '/scene-info/updateScheme',
        method: 'post',
        data: datas
    })
}
// 获取方案数据
export function getPlanListApi(datas) {
  return request({
      url: '/scene-info/getSchemeList',
      method: 'post',
      data: datas
  })
}

// 删除常备方案数据
export function delPlanApi(params) {
  return request({
      url: '/scene-info/deleteScheme',
      method: 'get',
     params
  })
}
// 复制常备方案数据
export function copyPlanApi(params) {
    return request({
        url: '/scene-info/copyScheme',
        method: 'get',
       params
    })
  }
// 获取方案统计
export function getPlanStaticData() {
    return request({
        url: '/scene-info/getSchemeStatistics',
        method: 'get',
    })
  }
  // 通过id查询场景详细信息
  export function getPlanInfoForId(params) {
    return request({
        url: '/scene-info/getById',
        method: 'get',
        params
    })
  }
