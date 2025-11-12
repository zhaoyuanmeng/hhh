import request from "@/utils/request";

// 用户列表
export const getList = (data,token) => {
  return request({
    url: `/user/getList`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
//编辑信息
export const updateUser = (data,token) => {
  return request({
    url: `/user/update`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
//新增用户
export const addUser = (data,token) => {
  return request({
    url: `/user/add`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
// 删除用户
export const deleteUser = (params,token) => {
  return request({
    url: `/user/delete`,
    method: "get",
    params,
    headers: {
      'x-auth-token': token
    }
  });
};

//新增机构
export const addGroup = (data,token) => {
  return request({
    url: `/org/add`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
//编辑机构
export const updateGroup = (data,token) => {
  return request({
    url: `/org/update`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//机构列表
export const getGroup = (data,token) => {
  return request({
    url: `/org/getList`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
//删除机构
export const deleteGroup = (params,token) => {
  return request({
    url: `/org/delete`,
    method: "get",
    params,
    headers: {
      'x-auth-token': token
    }
  });
};

//新增角色
export const addRole = (data,token) => {
  return request({
    url: `/role/add`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
//编辑角色
export const updateRole = (data,token) => {
  return request({
    url: `/role/update`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//角色列表
export const getRole = (data,token) => {
  return request({
    url: `/role/getList`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
//删除角色
export const deleteRole = (params,token) => {
  return request({
    url: `/role/delete`,
    method: "get",
    params,
    headers: {
      'x-auth-token': token
    }
  });
};

// 获取委办局联系人列表
export const getCommissionList = (data,token) => {
  return request({
    url: `/commission/getList`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//新增委办局联系人
export const addCommission = (data,token) => {
  return request({
    url: `/commission/add`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//编辑编辑委办局信息
export const updateCommission = (data,token) => {
  return request({
    url: `/commission/update`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//删除委办局信息
export const deleteCommission = (params,token) => {
  return request({
    url: `/commission/delete`,
    method: "get",
    params,
    headers: {
      'x-auth-token': token
    }
  });
};

// 获取乡镇列表
export const getTownshipList = (data,token) => {
  return request({
    url: `/township/getList`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//新增乡镇信息
export const addTownship = (data,token) => {
  return request({
    url: `/township/add`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//编辑乡镇信息
export const updateTownship = (data,token) => {
  return request({
    url: `/township/update`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};
// 获取行政村信息
export const getVillageList = (data,token) => {
  return request({
    url: `/village/getList`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};


//编辑行政村信息
export const updateVillage= (data,token) => {
  return request({
    url: `/village/update`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};

//获取移动端访问记录
export const getCount= (data) => {
  return request({
    url: `/accessLog/count`,
    method: "post",
    data,
  });
};

//查询中短期预报
export const getWeatherInfo = (params) => {
  return request({
    url: `/weather-forcast/info`,
    method: "get",
    params
  });
};
//更新中短期预报
export const updateWeatherInfo = (data,token) => {
  return request({
    url: `/weather-forcast/update`,
    method: "post",
    data,
    headers: {
      'x-auth-token': token
    }
  });
};