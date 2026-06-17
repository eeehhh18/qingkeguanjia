import request from '../utils/request'

// 分页查询用户
export function getUserPage(params) {
  return request.get('/user/page', { params })
}

// 新增用户
export function addUser(data) {
  return request.post('/user', data)
}

// 修改用户
export function updateUser(data) {
  return request.put('/user', data)
}

// 删除用户
export function deleteUser(id) {
  return request.delete(`/user/${id}`)
}

// 上传头像
export function uploadAvatar(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request.post('/user/upload', formData)
}
