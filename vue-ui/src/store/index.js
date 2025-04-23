import { createStore } from 'vuex'
import getters from './getters'
import app from './modules/app'
import bpmn from './modules/bpmn'
import permission from './modules/permission'
import user from './modules/user'

const store = createStore({
  modules: {
    app,
    bpmn,
    user,
    permission
  },
  getters
})

export default store
