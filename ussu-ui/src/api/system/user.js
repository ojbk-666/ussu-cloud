import request from '@/utils/request'

export function listUser(param) {
  return request({
    url: '/system/sys-user',
    method: 'get',
    params: param
  })
}
export function getUser(id) {
  return request({
    url: '/system/sys-user/'+id,
    method: 'get'
  })
}

// 新增
export function addUser(data) {
  return request({
    url: '/system/sys-user',
    method: 'put',
    data
  })
}

// 更新
export function updateUser(data) {
  return request({
    url: '/system/sys-user',
    method: 'post',
    data
  })
}

// 更新
export function changeUserStatus(data) {
  return request({
    url: '/system/sys-user/changeStatus',
    method: 'post',
    data
  })
}

export function resetUserPwd(data) {
  return request({
    url: '/system/sys-user/resetUserPwd',
    method: 'post',
    data
  })
}

// 删除参数配置
export function delUser(configId) {
  return request({
    url: '/system/sys-user/' + configId,
    method: 'delete'
  })
}

export function updateUserProfile(data) {
  return request({
    url: '/system/sys-user/profile',
    method: 'post',
    data
  })
}

export function updateUserPwd(data) {
  return request({
    url: '/system/sys-user/updatePwd',
    method: 'post',
    data
  })
}
