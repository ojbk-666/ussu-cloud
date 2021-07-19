import request from '@/utils/request'

export function getList(query) {
  return request({
    url: '/item/item',
    method: 'get',
    params: query
  })
}

export function detail(id) {
  return request({
    url: '/item/item/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/item/item',
    method: 'put',
    data
  })
}

export function edit(data) {
  return request({
    url: '/item/item',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '/item/item/' + ids,
    method: 'delete'
  })
}

