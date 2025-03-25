<template>
  <div class="table-head-container">
    <flexbox
      v-show="selectionList.length == 0"
      class="th-container">
      <slot name="custom"/>
      <template v-if="formCode">
        <div>场景：</div>
        <el-popover
          v-model="showScene"
          trigger="click"
          popper-class="no-padding-popover"
          width="180">
          <el-input
            slot="reference"
            v-model="sceneName"
            :readonly="true"
            class="scene-select">
            <i slot="suffix" :class="['el-input__icon', 'el-icon-' + iconClass]" />
          </el-input>
          <lego-scene-list
            ref="sceneList"
            :form-code="formCode"
            @scene="sceneSelect"
            @scene-handle="sceneHandle"
            @hidden-scene="showScene=false" />
        </el-popover>
      </template>
      <el-button
        v-if="showFilterView"
        :icon="'screening' | iconPre"
        type="primary"
        class="filter-button"
        @click="showFilter = true">高级筛选</el-button>
      <el-button
        v-if="sortData && sortData.order && sortData.column"
        type="primary"
        plain
        @click="handleCallBack({type: 'clear-sort'})">
        {{ `${sortData.column.label}${{ascending: '升序', descending: '降序'}[sortData.order]}` }}<i style="margin-left: 5px;" class="el-icon-close"/>
      </el-button>
      <lego-filter-form
        v-if="showFilterView"
        :field-list="fieldList"
        :dialog-visible.sync="showFilter"
        :filter-obj="filterObj"
        :save-scene="saveScene"
        @filter="handleFilter" />
      <slot />
    </flexbox>
    <flexbox
      v-if="selectionList.length > 0"
      class="selection-bar">
      <div class="selected—title">已选中 <span class="selected—count">{{ selectionList.length }}</span> 项</div>
      <flexbox class="selection-items-box">
        <el-button
          v-for="(item, index) in menuList"
          :icon="item.icon | iconPre"
          :key="index"
          type="primary"
          @click.native="selectionBarClick(item.type)">{{ item.name }}</el-button>
      </flexbox>
    </flexbox>
    <lego-filter-content
      v-if="filterObj.form && filterObj.form.length > 0"
      :filter-obj="filterObj"
      @delete="handleDeleteField" />

    <lego-scene-set
      :field-list="fieldList"
      :dialog-visible.sync="showSetScene"
      :form-code="formCode"
      @save-success="updateSceneList" />

    <lego-scene-create
      :field-list="fieldList"
      :form-code="formCode"
      :dialog-visible.sync="showCreateScene"
      @save-success="updateSceneList" />
  </div>
</template>

<script type="text/javascript">
import { sceneAddAPI } from '@/api/scene'

import LegoFilterForm from './LegoFilter'
import LegoFilterContent from './LegoFilter/Content'
import LegoSceneList from './LegoScene/List'
import LegoSceneSet from './LegoScene/Set'
import LegoSceneCreate from './LegoScene/Create'
import { Loading } from 'element-ui'
import { isArray, isEmpty } from '@/utils/types'
import { getMenuAuth } from '@/utils/auth'

export default {
  name: 'LegoTableHead',
  components: {
    LegoFilterForm,
    LegoFilterContent,
    LegoSceneList,
    LegoSceneCreate,
    LegoSceneSet
  },
  props: {
    title: {
      type: String,
      default: ''
    },
    formCode: {
      type: String,
      default: ''
    },
    menuCode: {
      type: String,
      default: ''
    },
    fieldList: {
      type: Array,
      default: []
    },
    menuList: {
      type: Array,
      default: []
    },
    // 排序信息
    sortData: Object,
    // 自定义操作
    handleFun: Function
  },
  data() {
    return {
      loading: false, // loading
      loadingObj: null, // loading 对象
      showScene: false, // 场景操作
      showFilter: false, // 控制筛选框
      filterObj: { form: [] }, // 筛选确定数据
      sceneData: { id: '', bydata: '', name: '' },
      showSetScene: false, // 展示场景设置
      showCreateScene: false, // 展示场景添加
      /** 勾选操作数据 */
      selectionList: []
    }
  },
  computed: {
    auth() {
      return getMenuAuth(this.menuCode)
    },
    iconClass() {
      return this.showScene ? 'arrow-up' : 'arrow-down'
    },
    sceneName() {
      return this.sceneData.name || this.getDefaultSceneName()
    },
    // 展示筛选
    showFilterView() {
      return true
    },
    saveScene() {
      return !isEmpty(this.formCode)
    }
  },
  watch: {
    loading(val) {
      if (val) {
        this.loadingObj = Loading.service({
          target: document.querySelector('#main-container')
        })
      } else {
        this.loadingObj && this.loadingObj.close()
      }
    }
  },
  methods: {
    /** 高级筛选选择 */
    handleFilter(data) {
      this.filterObj = data
      this.showFilter = false
      if (data.saveChecked) {
        sceneAddAPI({
          formCode: this.formCode,
          name: data.saveName,
          data: JSON.stringify(data.scenes)
        }).then(res => {
          this.$message.success('操作成功')
          this.updateSceneList()
        })
      }
      this.$emit('filter', data.filterList)
    },
    // 删除已选择的查询条件
    handleDeleteField(data) {
      this.filterObj = data.filterObj
      this.$emit('filter', this.filterObj.filterList)
    },
    // 场景操作
    /** 选择了场景 */
    sceneSelect(data) {
      this.sceneData = data
      let sceneList = []
      if (data.bydata) {
        sceneList = JSON.parse(data.bydata)
        sceneList.map(scene => {
          if (!isArray(scene.values)) {
            scene.values = [scene.values]
          }
        })
      }
      this.$emit('scene', sceneList)
    },
    sceneHandle(data) {
      if (data.type == 'set') {
        this.showSetScene = true
      } else if (data.type == 'add') {
        this.showCreateScene = true
      }
    },
    /**  创建保存成功 */
    updateSceneList() {
      this.$refs.sceneList.getSceneList()
    },
    /** 勾选后的表头操作 */
    headSelectionChange(array) {
      this.selectionList = array
    },
    /** 操作 */
    selectionBarClick(type) {
      // 传出selection操作
      if (this.handleFun) {
        this.handleFun(type)
        return
      }
      this.handleCallBack({ type: type })
    },
    // 子组件 回调的 结果
    handleCallBack(data) {
      if (data.type === 'delete') {
        this.$confirm('此操作将永久删除所选择的数据，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          this.$emit('handle', data)
        })
      } else {
        this.$emit('handle', data)
      }
    },
    // 获取默认场景名字
    getDefaultSceneName() {
      return '查询全部'
    }
  }
}
</script>
<style lang="scss" scoped>
.table-head-container {
  border-bottom: 1px solid #e6e6e6;
}

.th-container {
  font-size: 13px;
  height: 50px;
  padding: 0 20px;

  .scene-select {
    width: 180px;
    ::v-deep .el-input__inner {
      cursor: pointer;
      height: 34px;
      font-size: 13px;
    }
  }

  .filter-button {
    margin-left: 20px;
    padding: 8px 12px;
    ::v-deep i {
      font-size: 14px;
      margin-right: 5px;
    }
  }
}
/** 场景和筛选 */
.condition_title {
  cursor: pointer;
}
.condition_title:hover {
  color: $xr-color-primary;
}

.m-arrow {
  margin: 0 8px;
}
.c-filtrate {
  margin: 0 10px 0 30px;
  width: 12px;
}

/** 勾选操作 */
.selection-bar {
  font-size: 12px;
  height: 50px;
  padding: 0 20px;
  color: #777;

  .selected—title {
    flex-shrink: 0;
    padding-right: 20px;
    color: #333;
    .selected—count {
      color: $xr-color-primary;
    }
  }
}

.selection-items-box {
  overflow-x: auto;
  overflow-y: hidden;
  padding: 0 15px;

  .el-button {
    color: #666;
    background-color: #f9f9f9;
    border-color: #e5e5e5;
    font-size: 12px;
    height: 28px;
    border-radius: 4px;
    padding: 8px 12px;
    ::v-deep i {
      font-size: 12px;
      margin-right: 5px;
    }
  }

  .el-button--primary:hover {
    background: $xr-color-primary;
    border-color: $xr-color-primary;
    color: #ffffff;
  }

  .el-button + .el-button {
    margin-left: 15px;
  }
}
</style>
