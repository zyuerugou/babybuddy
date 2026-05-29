import request from '@/utils/request'

export function getGrowthReport(params) {
  return request({ url: '/baby/report/growth', method: 'get', params })
}

export function getFeedingInterval(params) {
  return request({ url: '/baby/report/feeding-interval', method: 'get', params })
}

export function getDailyFeeding(params) {
  return request({ url: '/baby/report/daily-feeding', method: 'get', params })
}