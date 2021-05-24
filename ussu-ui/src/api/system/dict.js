import request from '@/utils/request'

export function listDict(param) {
  return request({
    url: '/system/sys-dict',
    method: 'get',
    params: param
  })
}

// 根据类型获取列表
export function listByTypeCode(typeCode) {
  return request({
    url: '/system/sys-dict/typecode/' + typeCode,
    method: 'get'
  })
}

// 新增
export function addDict(data) {
  return request({
    url: '/system/sys-dict',
    method: 'put',
    data
  })
}

// 更新
export function updateDict(data) {
  return request({
    url: '/system/sys-dict',
    method: 'post',
    data
  })
}

// 删除参数配置
export function delDict(id) {
  return request({
    url: '/system/sys-dict/' + id,
    method: 'delete'
  })
}
