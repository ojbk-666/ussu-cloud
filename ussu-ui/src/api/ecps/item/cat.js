import request from '@/utils/request'

export function getListTree(query) {
  return request({
    url: '/item/eb-cat/list/tree',
    method: 'get',
    params: query
  })
}

export function listCatAll(query) {
  return request({
    url: '/item/eb-cat/all',
    method: 'get',
    params: query
  })
}
