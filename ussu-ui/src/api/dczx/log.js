import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/dczx/interface-log',
    method: 'get',
    params: param
  })
}
