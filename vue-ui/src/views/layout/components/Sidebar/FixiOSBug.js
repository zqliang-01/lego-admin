import { onMounted, ref } from 'vue'
import { useStore } from 'vuex'

export default {
  setup() {
    const store = useStore()
    const subMenu = ref(null)

    const device = computed(() => store.state.app.device)

    const fixBugIniOS = () => {
      if (subMenu.value) {
        const handleMouseleave = subMenu.value.handleMouseleave
        subMenu.value.handleMouseleave = (e) => {
          if (device.value === 'mobile') {
            return
          }
          handleMouseleave(e)
        }
      }
    }

    onMounted(() => {
      fixBugIniOS()
    })

    return {
      subMenu
    }
  }
}