import { host } from "./platform"

export default {
  connect: `${host}/chatgpt/connect`, //连接GPT SSE
  close: `${host}/chatgpt/close`, //关闭GPT SSE
  sendmsg: `${host}/chatgpt/sendmsg`, //发送信息给GPT
  msgitems: `${host}/chatgpt/msgitems`, //获取聊天历史气泡
  remove: `${host}/chatgpt/remove`, //删除聊天对话
}