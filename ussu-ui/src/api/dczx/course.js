import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/dczx/course',
    method: 'get',
    params: param
  })
}

export function allIdTitle() {
  return request({
    url: '/dczx/course/all',
    method: 'get'
  })
}
