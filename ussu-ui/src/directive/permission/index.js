// import permission from './permission'
import perm from './perm'

const install = function(Vue) {
  // Vue.directive('permission', permission)
  Vue.directive('perm', perm)
}

if (window.Vue) {
  // window['permission'] = permission
  window['perm'] = perm
  Vue.use(install); // eslint-disable-line
}

// permission.install = install
// export default permission
perm.install = install
export default perm
