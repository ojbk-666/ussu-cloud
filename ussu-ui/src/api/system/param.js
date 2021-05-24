import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/system/sys-param',
    method: 'get',
    params: param
  })
}

// 新增
export function addParam(data) {
  return request({
    url: '/system/sys-param',
    method: 'put',
    data
  })
}

// 更新
export function updateParam(data) {
  return request({
    url: '/system/sys-param',
    method: 'post',
    data
  })
}

// 删除参数配置
export function delParam(configId) {
  return request({
    url: '/system/sys-param/' + configId,
    method: 'delete'
  })
}

