const { defineConfig } = require('@vue/cli-service')
const path = require('path')

function resolve(dir) {
  return path.join(__dirname, dir)
}

const port = process.env.port || 9527
module.exports = defineConfig({
  publicPath: '/',
  transpileDependencies: false,
  runtimeCompiler: true,
  parallel: true,
  productionSourceMap: false,
  devServer: {
    host: '0.0.0.0',
    port: port,
    client: {
      overlay: false
    },
    proxy: {
      [process.env.VUE_APP_BASE_API]: {
        target: process.env.VUE_APP_PROXY_TARGET,
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          ['^' + process.env.VUE_APP_BASE_API]: ''
        }
      }
    }
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': resolve('src')
      },
      fallback: {
        path: require.resolve('path-browserify')
      }
    }
  },
  
  chainWebpack(config) {
    config.module.rule('svg').exclude.add(resolve('src/components/Bpmn/bpmn-icons')).end()
    config.module
      .rule('icons')
      .test(/\.svg$/)
      .include.add(resolve('src/components/Bpmn/bpmn-icons'))
      .end()
      .use('svg-sprite-loader')
      .loader('svg-sprite-loader')
      .options({
        symbolId: '[name]'
      })
      .end()
    config.module.rule('scss').oneOfs.store.forEach((item) => {
      item
        .use('sass-resources-loader')
        .loader('sass-resources-loader')
        .options({
          resources: ['src/styles/xr-theme.scss']
        })
        .end()
    })
  }
})
