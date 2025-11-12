import axios from 'axios';
import store from '@/store/index'
const newAxios = axios.create({
	baseURL: severUrl,
	transformRequest: [
		function (data) {
			let ret = ""
			for (let it in data) {
				ret += encodeURIComponent(it) + "=" + encodeURIComponent(data[it]) + "&"
			}
			return ret
		}
	]
})


// newAxios.interceptors.request.use((config) => {
// 	// let token = store.state.common.token;
// 	if (store.state.common && store.state.common.token) { // 判断是否存在token，如果存在的话，则每个http header都加上token
// 		config.headers.Authorization = store.state.common.token;
// 	}
// 	return {
// 		...config
// 	}
// }, (err) => Promise.reject(err))


newAxios.interceptors.response.use((res) => {
	// console.log(res, "响应拦截 ！！！")
	return res.data;
}, (err) => Promise.reject(err))
export default newAxios;