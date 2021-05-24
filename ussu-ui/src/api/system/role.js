import request from '@/utils/request'

export function listRole(param) {
  return request({
    url: '/system/sys-role',
    method: 'get',
    params: param
  })
}

export function listAllRole() {
  return request({
    url: '/system/sys-role/all',
    method: 'get'
  })
}

// 新增
export function addRole(data) {
  return request({
    url: '/system/sys-role',
    method: 'put',
    data
  })
}

// 更新
export function updateRole(data) {
  return request({
    url: '/system/sys-role',
    method: 'post',
    data
  })
}

// 删除参数配置
export function delRole(configId) {
  return request({
    url: '/system/sys-role/' + configId,
    method: 'delete'
  })
}

export function changeRoleStatus(id, status) {
  return request({
    url: '/system/sys-role/changeStatus',
    method: 'post',
    data: {
      id: id,
      status: status
    }
  })
}
