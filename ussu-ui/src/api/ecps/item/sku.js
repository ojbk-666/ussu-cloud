import request from '@/utils/request'

export function getList(query) {
  return request({
    url: '/item/sku',
    method: 'get',
    params: query
  })
}

export function detail(id) {
  return request({
    url: '/item/sku/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/item/sku',
    method: 'put',
    data
  })
}

export function edit(data) {
  return request({
    url: '/item/sku',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '/item/sku/' + ids,
    method: 'delete'
  })
}

export function up(ids) {
  return request({
    url: '/item/sku/up/' + ids,
    method: 'post'
  })
}

