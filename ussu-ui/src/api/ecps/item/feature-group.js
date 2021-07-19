import request from '@/utils/request'

export function getList(query) {
  return request({
    url: '/item/feature-group',
    method: 'get',
    params: query
  })
}

export function getListByCatId(id, query) {
  return request({
    url: `/item/feature-group/catId/${id}`,
    method: 'get',
    params: query
  })
}

export function detail(id) {
  return request({
    url: '/item/feature-group/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/item/feature-group',
    method: 'put',
    data
  })
}

export function edit(data) {
  return request({
    url: '/item/feature-group',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: '/item/feature-group/' + ids,
    method: 'delete'
  })
}

export function link(data) {
  return request({
    url: '/item/feature-group/link',
    method: 'post',
    data
  })
}
