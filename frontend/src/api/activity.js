import request from '../utils/request'

// 分页查询活动
export function getActivityPage(params) {
  return request({
    url: '/activity/page',
    method: 'get',
    params
  })
}

// 新增活动
export function addActivity(data) {
  return request({
    url: '/activity',
    method: 'post',
    data
  })
}

// 修改活动
export function updateActivity(data) {
  return request({
    url: '/activity',
    method: 'put',
    data
  })
}

// 删除活动
export function deleteActivity(id) {
  return request({
    url: `/activity/${id}`,
    method: 'delete'
  })
}