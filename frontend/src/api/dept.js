import request from '../utils/request'

// 分页查询部门
export function getDeptPage(params) {
  return request.get('/dept', { params })
}

// 新增部门
export function addDept(data) {
  return request.post('/dept', data)
}

// 修改部门
export function updateDept(data) {
  return request.put('/dept', data)
}

// 删除部门
export function deleteDept(id) {
  return request.delete(`/dept/${id}`)
}
