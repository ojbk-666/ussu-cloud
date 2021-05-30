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
