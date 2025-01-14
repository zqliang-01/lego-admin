import util from '@/utils/util'

export default {
	props: {
    item: Object,
		value: [String, Number, Boolean, Object],
    fieldForm: Object,
    disabled: Boolean,
		borderBottom: {
			type: Boolean,
			default: true
		}
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
		required() {
			return this.item.required && !this.isDisabled
		},
		clearable() {
			if (this.disabled || this.item.disabled) {
				return false
			}
			return true
		},
		placeholder() {
			if (!this.isDisabled) {
				return this.item.placeholder || this.actionTitle
			}
			return ''
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
