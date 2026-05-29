import request from '@/utils/request'

export function listMeasurement(query) {
  return request({ url: '/baby/measurement/list', method: 'get', params: query })
}

export function getMeasurement(id) {
  return request({ url: '/baby/measurement/' + id, method: 'get' })
}

export function addMeasurement(data) {
  return request({ url: '/baby/measurement', method: 'post', data })
}

export function updateMeasurement(data) {
  return request({ url: '/baby/measurement', method: 'put', data })
}

export function delMeasurement(id) {
  return request({ url: '/baby/measurement/' + id, method: 'delete' })
}