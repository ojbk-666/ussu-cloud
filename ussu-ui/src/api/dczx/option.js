import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/dczx/question-option',
    method: 'get',
    params: param
  })
}
