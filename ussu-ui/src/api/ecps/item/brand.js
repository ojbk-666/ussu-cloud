import request from '@/utils/request'

export function getList(query) {
  return request({
    url: '/item/brand',
    method: 'get',
    params: query
  })
}

export function getListByCatId(id) {
  return request({
    url: '/item/brand/catId/' + id,
    method: 'get'
  })
}

export function detail(id) {
  return request({
    url: '/item/brand/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/item/brand',
    method: 'put',
    data
  })
}

export function edit(data) {
  return request({
    url: '/item/brand',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '/item/brand/' + ids,
    method: 'delete'
  })
}

