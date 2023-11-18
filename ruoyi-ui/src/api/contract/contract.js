import request from "../../utils/request";

export function saveContract(data) {
  return request({
    url: '/contract/save',
    method: 'post',
    data: data
  })
}

export function submitContract(data) {
  return request({
    url: '/contract/submit',
    method: 'post',
    data: data
  })
}

export function getContractList(data) {
  return request({
    url: '/contract/list',
    method: 'post',
    data: data
  })
}

export function getContractDetail(query) {
  return request({
    url: '/contract/detail',
    method: 'get',
    params: query
  })
}

export function getExecutionList(query) {
  return request({
    url: '/contract/execution/list',
    method: 'get',
    params: query
  })
}
