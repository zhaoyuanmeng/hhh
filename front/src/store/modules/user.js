import { defineStore } from 'pinia'
import { getToken, setToken, removeToken } from '@/utils/auth'
const useUserStore = defineStore(
  'user',
  {
    state: () => ({
      token: getToken(),
      routerIndex: 'Home',// 默认展示一张图的路由name
      userInfo: {}, // 登录信息
      menuList: [],
    }),
    // 使用插件提供的 persist 选项来配置持久化
  persist: {
    enabled: true, // 开启持久化
    strategies: [
      {
        key: 'userInfo', // 在 localStorage 中存储时的 key
        storage: localStorage, // 默认为 localStorage，还可以是 sessionStorage
        paths: ['userInfo','menuList','token','routerIndex'] // 指定要持久化的状态，可以是一个数组
      }
    ]
  },
    actions: {
      setRouterName(name) {
        this.routerIndex = name
      },
      set_Token(){
        this.token = ''
        removeToken()
        localStorage.removeItem('token')
        this.menuList = []
        this.userInfo = {}
        localStorage.clear();
      },
      set_userInfo(Obj){
        this.userInfo = Obj
      },
      set_menuList(list){
        this.menuList = list
      }
    },
    getters: {},
  })

export default useUserStore
