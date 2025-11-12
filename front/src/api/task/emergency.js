import request from '@/utils/request'

// 应急预案查看详情获取列表接口
export function getEmcyListdata(datas) {
  return request({
    url: '/emergency-plan/getList',
    method: 'post',
    data: datas
  })
}

// 应急预案新增
export function addEmcy(datas) {
  return request({
    url: '/emergency-plan/save',
    method: 'post',
    data: datas
  })
}

// 应急预案修改
export function updateEmcy(datas) {
  return request({
    url: '/emergency-plan/update',
    method: 'post',
    data: datas
  })
}

// 应急预案删除
export function delEmcy(datas) {
  return request({
    url: '/emergency-plan/delete',
    method: 'get',
    params: datas
  })
}

// 查预案详情
export function getEmcyInfo(datas) {
  return request({
    url: '/emergency-plan/getDetail',
    method: 'get',
    params: datas
  })
}

// 导入标绘数据
export function importYAData(params) {
  return request({
    url:'/draw-data/batchImport',
    method:'post',
    data:params
  })
}