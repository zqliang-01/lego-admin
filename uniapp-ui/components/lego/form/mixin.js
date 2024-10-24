import util from '@/utils/util'

export default {
	props: {
    item: Object,
		value: [String, Number, Boolean],
    fieldForm: Object,
    disabled: Boolean
	},
  data() {
    return {}
  },
	computed: {
		actionTitle() {
			if (this.isDisabled) {
				return ''
			}
			return '请选择' + this.item.name
		},
		isDisabled() {
			return this.disabled || this.item.disabled
		},
		clearable() {
			if (this.disabled || this.item.disabled) {
				return false
			}
			return true//this.item.clearable
		}
	},
  methods: {
    commonChange(item, value) {
      this.$emit('change', item, value)
			this.$nextTick(() => {
				uni.$u.formValidate(this, 'change')
			})
    }
  }
}
