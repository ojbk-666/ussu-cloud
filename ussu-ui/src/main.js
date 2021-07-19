import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'

import '@/styles/index.scss' // global css
import '@/assets/style/ussu.scss'

import App from './App'
import store from './store'
import router from './router'

import i18n from './lang' // internationalization
import './assets/icons' // icon
import './permission' // permission control

// 挂载全局方法
import { parseTime, resetForm, handleTree, selectDictLabel, deepClone } from '@/utils/ussu'
import { listByTypeCode } from '@/api/system/dict'
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.handleTree = handleTree
Vue.prototype.getDicts = listByTypeCode
Vue.prototype.selectDictLabel = selectDictLabel

import './utils/error-log' // error log
import Pagination from '@/components/Pagination'
import RightToolbar from '@/components/RightToolbar'

import * as filters from './filters' // global filters
import perm from '@/directive/permission/index'

Vue.prototype.msgSuccess = function(msg) {
  this.$message({ showClose: true, message: msg, type: "success" });
}

Vue.prototype.msgError = function(msg) {
  this.$message({ showClose: true, message: msg, type: "error" });
}

Vue.prototype.msgInfo = function(msg) {
  this.$message.info(msg);
}

Vue.prototype.resetObj = function(obj) {
  // 清空对象属性，不递归
  for (const key in obj) {
    if (obj[key] instanceof Array) {
      obj[key] = [];
    } else if (obj[key] in Object) {
      obj[key] = {};
    } else {
      obj[key] = undefined
    }
  }
}

Vue.prototype.deepClone = function(obj) {
  return deepClone(obj);
}

Vue.prototype.showImg = function(path) {
  return process.env.VUE_APP_FILE_SERVER + path;
}

// 挂载全局组件
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */
if (process.env.NODE_ENV === 'production') {
  // const { mockXHR } = require('../mock')
  // mockXHR()
}

Vue.use(perm)

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
