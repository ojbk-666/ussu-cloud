import request from '@/utils/request'

// 获取路由
export const getRouters = () => {
  return request({
    url: '/system/profile/routers',
    method: 'get'
  })
}

// 更新用户信息
export function updateUserProfile(data) {
  return request({
    url: '/system/profile',
    method: 'post',
    data
  })
}

// 修改密码
export function updateUserPwd(data) {
  return request({
    url: '/system/profile/password',
    method: 'post',
    data
  })
}

// 更新用户头像
export function uploadAvatar(data) {
  return request({
    url: '/system/profile/avatar',
    method: 'post',
    data
  })
}
