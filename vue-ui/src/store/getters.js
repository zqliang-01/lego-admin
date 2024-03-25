const getters = {
  userInfo: state => state.user.userInfo,
  lang: state => state.app.lang,
  app: state => state.app,
  logo: state => state.app.logo,
  name: state => {
    if (state.app.name) {
      return state.app.name
    }
    return 'LegoAdmin'
  },
  collapse: state => state.app.sidebar.collapse,
  activeIndex: state => state.app.sidebar.activeIndex,
  navActiveIndex: state => state.app.navbar.activeIndex,
  headerModule: state => state.app.headerModule,
  // 权限
  allAuth: state => state.user.allAuth,
  manage: state => state.user.manage,
  authList: state => state.user.authList,

  // 路由
  allRouters: state => state.permission.allRouters,
  menuRouters: state => state.permission.menuRouters,
  manageRouters: state => state.permission.manageRouters,
  imageCache: state => state.app.imageCache,

  // bpmn图形编辑器
  getEditor: state => state.bpmn.editor,
  getProcessDef: state => ({
    processName: state.bpmn.editor.processName,
    processId: state.bpmn.editor.processId
  }),
  getProcessEngine: state => state.bpmn.editor.processEngine,
  getEditorConfig: state => {
    return Object.keys(state.bpmn.editor).reduce((config, key) => {
      if (!['processName', 'processId', 'processEngine'].includes(key)) {
        config[key] = state.bpmn.editor[key]
      }
      return config
    }, {})
  },
  getModeler: state => state.bpmn.data._modeler,
  getModeling: state => (state.bpmn.data._modeler ? state.bpmn.data._modeler.get('modeling') : undefined),
  getActive: state => state.bpmn.activeElement
}
export default getters
