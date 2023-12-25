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

  // 路由
  addRouters: state => state.permission.addRouters,
  crmRouters: state => state.permission.crmRouters,
  manageRouters: state => state.permission.manageRouters,
  imageCache: state => state.app.imageCache
}
export default getters
