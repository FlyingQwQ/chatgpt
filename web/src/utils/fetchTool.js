import axios from 'axios'

axios.defaults.timeout = 60000

let fetch = {
  get: (url, params = {}) => {
    return new Promise((resolve, reject) => {
      let headers = {};
      axios.get(url,{
        headers: headers,
        params: params
      }).then(response => {
        resolve(response.data);
      }).catch(error => {
        reject(error);
      })
    });
  }
}

export default fetch;
