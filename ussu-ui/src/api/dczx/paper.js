import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/dczx/paper',
    method: 'get',
    params: param
  })
}
