import request from '@/utils/request'

export function getList(param) {
  return request({
    url: '/dczx/paper-question-topic',
    method: 'get',
    params: param
  })
}

export function allIdTitle() {
  return request({
    url: '/dczx/paper-question-topic/all',
    method: 'get'
  })
}
