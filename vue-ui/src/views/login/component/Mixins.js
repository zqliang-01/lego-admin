export default {
  props: {
  },
  data() {
    return {
      focusKey: null
    }
  },
  methods: {
    checkFromItem(key, value = null) {
      this.focusKey = null
      if (!key) return false
      this.clearError()
      if (!Object.prototype.hasOwnProperty.call(this.rules, key)) return true
      const ruleArr = this.rules[key]
      for (let i = 0; i < ruleArr.length; i++) {
        const rule = ruleArr[i]
        if (Object.prototype.hasOwnProperty.call(rule, 'required') && rule.required) {
          if (!value) {
            this.setError(key, rule.msg)
            return false
          }
        }
        if (Object.prototype.hasOwnProperty.call(rule, 'reg') && rule.reg) {
          if (rule.required) {
            if (!(rule.reg.test(value))) {
              this.setError(key, rule.msg)
              return false
            }
          } else {
            if (value && !(rule.reg.test(value))) {
              this.setError(key, rule.msg)
              return false
            }
          }
        }
        if (rule.validator) {
          if (!rule.validator()) {
            this.setError(key, rule.msg)
            return false
          }
        }
      }
      return true
    },

    setError(key, msg) {
      // this.$refs[key].focus()
      setTimeout(() => {
        this.validateRes[key] = false
        this.errorInfo = msg
      }, 200)
    }
  }
}
