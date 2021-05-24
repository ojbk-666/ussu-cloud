import request from '@/utils/request'

export function listType(param) {
  return request({
    url: '/system/sys-dict-type',
    method: 'get',
    params: param
  })
}

export function listTypeAll() {
  return request({
    url: '/system/sys-dict-type/all',
    method: 'get'
  })
}

export function getType(typeId) {
  return request({
    url: '/system/sys-dict-type/'+typeId,
    method: 'get'
  })
}

// 新增
export function addType(data) {
  return request({
    url: '/system/sys-dict-type',
    method: 'put',
    data
  })
}

// 更新
export function updateType(data) {
  return request({
    url: '/system/sys-dict-type',
    method: 'post',
    data
  })
}

// 删除参数配置
export function delType(configId) {
  return request({
    url: '/system/sys-dict-type/' + configId,
    method: 'delete'
  })
}

export function changeRoleStatus(id, status) {
  return request({
    url: '/system/sys-dict-type/changeStatus',
    method: 'post',
    data: {
      id: id,
      status: status
    }
  })
}
