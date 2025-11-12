/*
 * @Description:
 * @Author: Alex
 * @Date: 2024-06-06 17:49:38
 * @LastEditTime: 2024-06-06 19:11:23
 * @LastEditors: Alex
 */
import { createPinia } from "pinia";
import piniaPluginPersist from 'pinia-plugin-persist'
const store = createPinia();
store.use(piniaPluginPersist)
export default store;
