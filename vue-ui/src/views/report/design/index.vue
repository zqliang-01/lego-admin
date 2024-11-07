<template>
  <div v-loading="loading" class="lego-design">
    <div class="lego-design__body">
      <edit-view
        v-for="(item, index) in sortTop"
        :key="index"
        :height="300"
        :data="item"
        @onClick="handleNodeClick">
        <top-report
          :data="item"
          :rate-text="rateText"
        />
      </edit-view>
      <edit-view :data="{position: 'top', enable: true}" :height="100" empty tips="添加组件" @onClick="handleNodeClick" />
      <flexbox class="section" align="stretch">
        <div class="left">
          <draggable
            v-model="sortLeft"
            v-bind="{ forceFallback: false }"
            @end="handleDraggableEnd('sortLeft')">
            <edit-view
              v-for="(item, index) in sortLeft"
              :key="index"
              :height="300"
              :data="item"
              @onClick="handleNodeClick">
              <component :data="item" :is="item.chartType" class="left-content" />
            </edit-view>
          </draggable>
          <edit-view :data="{position: 'left', enable: true}" :height="100" empty tips="添加组件" @onClick="handleNodeClick" />
        </div>
        <div class="right">
          <draggable
            v-model="sortRight"
            v-bind="{ forceFallback: false }"
            @end="handleDraggableEnd('sortRight')">
            <edit-view
              v-for="(item, index) in sortRight"
              :key="index"
              :height="300"
              :data="item"
              @onClick="handleNodeClick">
              <component :data="item" :is="item.chartType" class="right-content" />
            </edit-view>
          </draggable>
          <edit-view :data="{position: 'right', enable: true}" :height="100" empty tips="添加组件" @onClick="handleNodeClick" />
        </div>
      </flexbox>
    </div>
    <Create
      :visible="isCreate"
      :action="action"
      @handle="handleCreate"
      @close="isCreate = false"
    />
  </div>
</template>

<script>
import {
  designListAPI,
  designSortModifyAPI
} from '@/api/report/design'
import draggable from 'vuedraggable'
import Create from './Create'
import EditView from '../components/editView'
import TopReport from '../../home/components/TopReport'

const allCharts = require.context('../../home/charts', true, /\.vue$/)
const res_charts = {}
allCharts.keys().forEach((item) => {
  const comp = allCharts(item)
  const name = comp.default.name
  res_charts[name] = comp.default
})

export default {
  name: 'HomeCenter',
  components: {
    EditView,
    TopReport,
    Create,
    draggable,
    ...res_charts
  },
  data() {
    return {
      loading: false,
      rateText: '本月',
      isCreate: false,
      action: {
        type: 'save',
        position: 'top'
      },
      sortTop: [],
      sortLeft: [],
      sortRight: [],
      chartList: [],
      setSortShow: false
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      designListAPI().then(res => {
        this.sortTop = []
        this.sortLeft = []
        this.sortRight = []
        res.data.forEach(data => {
          if (data.position === 'top') {
            this.sortTop.push(data)
          } else if (data.position === 'left') {
            this.sortLeft.push(data)
          } else if (data.position === 'right') {
            this.sortRight.push(data)
          }
        })
      })
    },
    timeTypeChange(data) {
      this.rateText = data.label
    },
    handleNodeClick(data) {
      this.action.detailCode = data.code
      this.action.position = data.position
      this.action.type = data.code ? 'update' : 'save'
      this.isCreate = true
    },
    handleCreate(data) {
      if (data.type === 'save-success') {
        this.init()
      }
    },
    handleDraggableEnd() {
      this.loading = true
      designSortModifyAPI({
        leftSort: this.sortLeft.map(left => left.code),
        rightSort: this.sortRight.map(right => right.code)
      }).then(() => {
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style scoped lang="scss">
.lego-design {
  width: 100%;
  min-width: 1000px;
  height: 100%;
  padding: 15px 15px 10px 10px;
  position: relative;

  &__body {
    height: 100%;
    overflow: auto;
  }
  .section {
    display: flex;
    flex-wrap: wrap;
    .left {
      width: calc(60.5% - 20px);
      margin-right: 20px;
      &-content {
        height: 400px;
      }
    }
    .right {
      width: 39.5%;
      &-content {
        height: 400px;
      }
    }

    .left-content + .left-content,
    .right-content + .right-content {
      margin-top: 18px;
    }
  }
}
</style>
