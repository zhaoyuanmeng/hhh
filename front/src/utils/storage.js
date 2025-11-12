class Storage {
    constructor(name) {
      this.storage = window[name]
      this.prefix = ''
    }
  
    set(key, value, fn) {
      value = JSON.stringify(value)
      this.storage.setItem(this.prefix + key, value)
  
      fn && fn()
    }
  
    get(key) {
      if (!key) {
        throw new Error('没有找到key。')
      }
      if (typeof key === 'object') {
        throw new Error('key不能是一个对象。')
      }
      var value = this.storage.getItem(this.prefix + key)
      if (value !== null && value !== 'undefined') {
        value = JSON.parse(value)
      }
      return value
    }
  
    remove(key) {
      this.storage.removeItem(this.prefix + key)
    }
  }
  export const sessionStorage = new Storage('sessionStorage')
  