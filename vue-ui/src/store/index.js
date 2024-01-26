import Vue from 'vue'
import Vuex from 'vuex'
import bpmn from './modules/bpmn'
import app from './modules/app'
import user from './modules/user'
import permission from './modules/permission'
import getters from './getters'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    bpmn,
    user,
    permission
  },
  getters
})

export default store
