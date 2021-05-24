import request from '@/utils/request'

export function listMenu() {
  return request({
    url: '/system/sys-menu/all',
    method: 'get'
  })
}

// 新增
export function getMenu(data) {
  return request({
    url: '/system/sys-menu',
    method: 'put',
    data
  })
}

// 新增
export function addMenu(data) {
  return request({
    url: '/system/sys-menu',
    method: 'put',
    data
  })
}

// 更新
export function updateMenu(data) {
  return request({
    url: '/system/sys-menu',
    method: 'post',
    data
  })
}

// 删除参数配置
export function delMenu(configId) {
  return request({
    url: '/system/sys-menu/' + configId,
    method: 'delete'
  })
}

export const treeselect = () => {
  return request({
    url: '/system/sys-menu/treeselect',
    method: 'get'
  })
}

export function roleMenuTreeselect(roidId) {
  return request({
    url: '/system/sys-menu/roleMenuTreeselect/'+roidId,
    method: 'get'
  })
}
