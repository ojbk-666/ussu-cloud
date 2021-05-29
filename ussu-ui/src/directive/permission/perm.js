import store from '@/store'

function checkPerm(el, binding) {
  const { value } = binding
  const perms = store.getters && store.getters.perms

  if (value && value instanceof Array) {
    if (value.length > 0) {
      const permArr = value

      const hasPerm = perms.some(perm => {
        return permArr.includes(perm) || permArr.includes("*:*:*")
      })

      if (!hasPerm) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  } else {
    throw new Error(`need perms! Like v-perm="['a:select','a:delete']"`)
  }
}

export default {
  inserted(el, binding) {
    checkPerm(el, binding)
  },
  update(el, binding) {
    checkPerm(el, binding)
  }
}
