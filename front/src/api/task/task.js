import request from '@/utils/request'
// 任务推演统计
export function getTaskStatic() {
  return request({
    url: '/task/statisticalTotalAndNumOfLevel',
    method: 'get',
  })
}
// 查询任务规划下节点信息
export function searchNodePlan(data) {
    return request({
      url: '/task-plan/getTaskPlanNode',
      method: 'get',
      params: data
    })
  }

// 任务规划保存
export function saveTaskPlan(params) {
  return request({
    url: '/task-plan/save',
    method: 'post',
    data: params
  })
}

// 创建场景所需位置
export function getScreenPosition(data) {
  return request({
    url: '/point-info/getBasicDataNameList',
    method: 'get',
    params: data
  })
}

// 创建场景
export function addScreen(params) {
  return request({
    url: '/scene-info/add',
    method: 'post',
    data: params
  })
}
// 编辑场景
export function updateScreen(params) {
  return request({
    url: '/scene-info/update',
    method: 'post',
    data: params
  })
}
// 查询场景规划下节点信息
export function searchNodePlanToScreen(data) {
  return request({
    url: '/scene-plan/getScenePlanNode',
    method: 'get',
    params: data
  })
}

// 场景规划保存
export function saveScreenPlan(params) {
return request({
  url: '/scene-plan/save',
  method: 'post',
  data: params
})
}

export function saveScreenPlanFX(params) {
  return request({
    url: '/scene-plan-ext/save',
    method: 'post',
    data: params
  })
  }
// 场景标绘数据单个保存
export function saveScreenDraw(params) {
  return request({
    url: '/draw-data/save',
    method: 'post',
    data: params
  })
  }

  // 通过场景id去查询数据
export function getDrawDataForScreen(id) {
  return request({
    url: '/scene-info/getDrawDataOfScene',
    method: 'get',
    params: id
  })
}

  // 通过任务id去查询数据
  export function getDrawDataForTask(id) {
    return request({
      url: '/task/getDrawDataOfTask',
      method: 'get',
      params: id
    })
  }

  // 通过标绘数据id去查询数据
  export function deleteDrawData(id) {
    return request({
      url: `/draw-data/delete?id=${id}`,
      method: 'get',
    })
  }
  
// 通过场景id去查询漫游数据
export function getTourDataForScreen(id) {
  return request({
    url: '/scene-info/getSceneDetail',
    method: 'get',
    params: id
  })
}

// 通过id查询警力部署哨位统计数据
export function getWhistleData(id) {
  return request({
    url: '/task/getPostPoliceDataOfTask',
    method: 'get',
    params: id
  })
}

// 通过id查询路线数据
export function getPrcLineData(id) {
  return request({
    url: '/task/getRoadPlanOfTask',
    method: 'get',
    params: id
  })
}

// 通过id查询应急力量数据
export function getPrcYJData(id) {
  return request({
    url: '/task/getEmergencyForcesOfTask',
    method: 'get',
    params: id
  })
}


// 通过id查询警力部署哨位统计数据
export function getWhistleDataScene(id) {
  return request({
    url: '/scene-info/getPostPoliceDataOfScene',
    method: 'get',
    params: id
  })
}

// 通过场景id查询资源列表
export function getReourceDataForScene(id) {
  return request({
    url: '/scene-info/getSceneResources',
    method: 'get',
    params: id
  })
}

// 查询楼层炸开的标绘数据
export function getMarkersForFloor(query) {
  return request({
    url: '/draw-data/getList',
    method: 'post',
    data: query
  })
}

// 查询楼层炸开的所有数据
export function getMarkersForFloorAllData(datas) {
  return request({
    url: '/draw-data/getDrawDataListOfBuilding',
    method: 'get',
    params: datas
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
// 查询场景基本情况路线统计数据
export function searchBasicLinesStatic(params) {
  return request({
    url: '/scene-info/getSceneLineData',
    method: 'get',
    params
  })
}
// 查询分组数据
export function searchGroupList(params) {
  return request({
    url: '/draw-data/getPoliceGroup',
    method: 'get',
    params
  })
}

// 查询场景规划数据
export function searchPlanNodeList(params) {
  return request({
    url: '/scene-plan/getList',
    method: 'get',
    params
  })
}

// 删除自定义节点
export function delPlanNodeList(params) {
  return request({
    url: '/scene-plan/delete',
    method: 'get',
    params
  })
}

// 更新警力分组
export function updatePoliceGroup(params) {
  return request({
    url: '/draw-data/updatePoliceGroup',
    method: 'post',
    data:params
  })
}

// 查询场景树
export function getSceneTreeData(data) {
  return request({
    url: '/scene-info/getSceneTree',
    method: 'get',
    params:data
  })
}

// 获取当前高速、高铁的基础要素列表
export function getGSGtBasicList(data) {
  return request({
    url: '/point-info/getFeatureTypeData',
    method: 'get',
    params:data
  })
}

// 快速布警
export function quickToSetPolice(data) {
  return request({
    url: '/scene-info/deployPolice',
    method: 'post',
    data
  })
}

// 获取解析的文档内容
export function getWordContent(params) {
  return request({
    url: '/task/getArchivesData',
    method: 'get',
    params
  })
}

// 清空文档解析
export function clearWordContent(params) {
  return request({
    url: '/task/deleteArchives',
    method: 'get',
    params
  })
}

// 路线排序
export function setLinesSort(data) {
  return request({
    url: '/draw-data/saveLineSort',
    method: 'post',
    data
  })
}

// 获取所有常规路线
export function getLineListData(data) {
  return request({
    url: '/draw-data/getLineList',
    method: 'post',
    data
  })
}

// 路线导入
export function sceneLineImport(data) {
  return request({
    url: '/draw-data/importDrawData',
    method: 'post',
    data
  })
}
export function importLineSave(data){
  return request({
    url: '/draw-data/saveImport',
    method: 'post',
    data
  })
}