import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/system/sys-dept',
    method: 'get',
    params: param
  })
}

export function listDeptAll(params) {
  return request({
    url: '/system/sys-dept/all',
    method: 'get',
    params: params
  })
}

export function listAllDeptExcludeChild(ids) {
  return request({
    url: '/system/sys-dept/all/exclude',
    method: 'get',
    params: {
      ids: ids
    }
  })
}

export function treeselect() {
  return request({
    url: '/system/sys-dept/treeselect',
    method: 'get'
  })
}

// 新增
export function addDept(data) {
  return request({
    url: '/system/sys-dept',
    method: 'put',
    data
  })
}

// 更新
export function updateDept(data) {
  return request({
    url: '/system/sys-dept',
    method: 'post',
    data
  })
}

// 删除参数配置
export function delDept(id) {
  return request({
    url: '/system/sys-dept/' + id,
    method: 'delete'
  })
}

