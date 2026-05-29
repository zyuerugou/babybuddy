import request from '@/utils/request'

export function listBaby(query) {
  return request({ url: '/baby/list', method: 'get', params: query })
}

export function getBaby(babyId) {
  return request({ url: '/baby/' + babyId, method: 'get' })
}

export function addBaby(data) {
  return request({ url: '/baby', method: 'post', data })
}

export function updateBaby(data) {
  return request({ url: '/baby', method: 'put', data })
}

export function delBaby(babyId) {
  return request({ url: '/baby/' + babyId, method: 'delete' })
}
