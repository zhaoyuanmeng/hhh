import request from '@/utils/request';

// 查询分组规则列表
export function getGroupRules(sceneId) {
  return request({
    url: '/api/groupRule/list',
    method: 'get',
    params: { sceneId }
  });
}

// 保存分组规则（批量）
export function saveGroupRules(sceneId, rules) {
  return request({
    url: '/api/groupRule/save',
    method: 'post',
    params: { sceneId },
    data: rules
  });
}

// 删除分组规则
export function deleteGroupRule(id) {
  return request({
    url: '/api/groupRule/delete',
    method: 'delete',
    params: { id }
  });
}