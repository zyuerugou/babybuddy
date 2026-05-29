import request from '@/utils/request'

export function listBehavior(query) {
  return request({ url: '/baby/behavior/list', method: 'get', params: query })
}

export function getBehavior(id) {
  return request({ url: '/baby/behavior/' + id, method: 'get' })
}

export function addBehavior(data) {
  return request({ url: '/baby/behavior', method: 'post', data })
}

export function updateBehavior(data) {
  return request({ url: '/baby/behavior', method: 'put', data })
}

export function delBehavior(id) {
  return request({ url: '/baby/behavior/' + id, method: 'delete' })
}