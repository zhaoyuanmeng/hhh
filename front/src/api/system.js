import request from '@/utils/request'

// 查询用户列表
export function getUserList(datas) {
    return request({
        url: '/user/getPage',
        method: 'post',
        data: datas
    })
}

// 添加用户
export function addUserApi(datas) {
    return request({
        url: '/user/add',
        method: 'post',
        data: datas
    })
}

// 编辑用户
export function updateUser(datas) {
    return request({
        url: '/user/update',
        method: 'post',
        data: datas
    })
}

// 删除用户
export function deleteUser(data) {
    return request({
        url: `/user/delete`,
        method: 'get',
        params: data
    })
}


//查询部门列表
export function getOrgList() {
    return request({
        url: `/org/getList`,
        method: 'post',
        data: { keyword: "" }
    })
}

//查询角色列表
export function getRoleList() {
    return request({
        url: `/role/getList`,
        method: 'post',
        data: {}
    })
}


//查询部门分页列表
export function getOrgPageList(params) {
    return request({
        url: `/org/getPage`,
        method: 'post',
        data: params
    })
}
// 新增部门
export function addOrg(params) {
    return request({
        url: `/org/add`,
        method: 'post',
        data: params
    })
}

// 编辑部门
export function updateOrg(params) {
    return request({
        url: `/org/update`,
        method: 'post',
        data: params
    })
}

// 删除部门
export function deleteOrg(data) {
    return request({
        url: `/org/delete`,
        method: 'get',
        params: data
    })
}

// 获取角色分页列表
export function getRolePageList(params) {
    return request({
        url: `/role/getPage`,
        method: 'post',
        data: params
    })
}

// 新增角色
export function addRole(params) {
    return request({
        url: `/role/add`,
        method: 'post',
        data: params
    })
}

// 编辑角色
export function updateRole(params) {
    return request({
        url: `/role/update`,
        method: 'post',
        data: params
    })
}

// 删除角色
export function delRole(data) {
    return request({
        url: `/role/delete`,
        method: 'get',
        params: data
    })
}
//获取当前用户i西南西
export function getUserInfo() {
    return request({
        url: `/user/current`,
        method: 'get',
    })
}