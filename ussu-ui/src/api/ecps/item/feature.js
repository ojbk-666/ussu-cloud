import request from '@/utils/request'

export function getList(query) {
  return request({
    url: '/item/feature',
    method: 'get',
    params: query
  })
}

export function getAllByCatId(catId, query) {
  return request({
    url: `/item/feature/catId/${catId}`,
    method: 'get',
    params: query
  })
}

export function detail(id) {
  return request({
    url: '/item/feature/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/item/feature',
    method: 'put',
    data
  })
}

export function edit(data) {
  return request({
    url: '/item/feature',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '/item/feature/' + ids,
    method: 'delete'
  })
}

