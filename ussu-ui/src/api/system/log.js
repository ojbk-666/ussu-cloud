import request from '@/utils/request'

export function getSysLogList(param) {
  return request({
    url: '/admin/system/sys-log',
    method: 'get',
    params: param
  })
}

