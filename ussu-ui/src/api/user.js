import request from '@/utils/request'

export function login(data) {
  return request({
    // url: '/vue-element-admin/user/login',
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    // url: '/vue-element-admin/user/info',
    url: '/auth/info',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    // url: '/vue-element-admin/user/logout',
    url: '/auth/logout',
    method: 'post'
  })
}

export function getCaptcha(uuid) {
  return request({
    url: '/code',
    method: 'get',
    params: { uuid }
  })
}

export function loginThirdAlipay(data) {
  return request({
    url: '/auth/login/third/alipay',
    method: 'post',
    data
  })
}

export function loginThirdGitee(data) {
  return request({
    url: '/auth/login/third/gitee',
    method: 'post',
    data
  })
}
