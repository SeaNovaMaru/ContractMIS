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
