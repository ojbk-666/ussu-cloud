import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/dczx/course',
    method: 'get',
    params: param
  })
}
