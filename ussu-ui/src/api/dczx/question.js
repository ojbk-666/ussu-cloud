import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/dczx/paper-question',
    method: 'get',
    params: param
  })
}
