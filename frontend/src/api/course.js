import request from '../utils/request'

// 分页查询课程
export function getCoursePage(params) {
  return request({
    url: '/course/page',
    method: 'get',
    params
  })
}

// 获取所有课程列表
export function getCourseList() {
  return request({
    url: '/course/list',
    method: 'get'
  })
}

// 新增课程
export function addCourse(data) {
  return request({
    url: '/course',
    method: 'post',
    data
  })
}

// 修改课程
export function updateCourse(data) {
  return request({
    url: '/course',
    method: 'put',
    data
  })
}

// 删除课程
export function deleteCourse(id) {
  return request({
    url: `/course/${id}`,
    method: 'delete'
  })
}