import request from '@/utils/request'

// 文件列表
export function list(param) {
  return request({
    url: '/system/files',
    method: 'get',
    params: param
  })
}

// 文件上传
export function upload(data) {
  return request({
    url: '/system/files/upload',
    method: 'post',
    data
  })
}

export function mkdir(data) {
  return request({
    url: '/system/files',
    method: 'put',
    data
  })
}

export function rename(data) {
  return request({
    url: '/system/files',
    method: 'post',
    data
  })
}

export function del(data) {
  return request({
    url: '/system/files',
    method: 'delete',
    data
  })
}
