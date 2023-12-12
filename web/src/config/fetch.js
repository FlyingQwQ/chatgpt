import fetchTool from '@/utils/fetchTool'
import api from "./api";

let fetch = {};

for(let key in api) {
  fetch[key] = (params) => fetchTool.get(api[key], params);
}

export default fetch;