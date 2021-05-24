import request from '@/utils/request'

export function getSystemInfo() {
  return request({
    url: '/system/systemInfo',
    method: 'get'
  })
}

