import request from '@/utils/request'

// 登录方法
export function login(datas) {
    return request({
      url: '/user/login',
      headers: {
        isToken: false
      },
      method: 'post',
      data: datas
    })
  }