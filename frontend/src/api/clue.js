import request from '../utils/request'

// 分页查询线索
export function getCluePage(params) {
  return request.get('/clue/page', { params })
}

// 新增线索
export function addClue(data) {
  return request.post('/clue', data)
}

// 分配线索
export function assignClue(clueId, userId) {
  return request.put('/clue/assign', null, { params: { clueId, userId } })
}

// 线索跟进
export function trackClue(clueId, data) {
  return request.post('/clue/track', data, { params: { clueId } })
}

// 线索转商机
export function convertClue(id) {
  return request.put(`/clue/convert/${id}`)
}
