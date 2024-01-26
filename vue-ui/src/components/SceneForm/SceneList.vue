<template>
  <div class="scene-container">
    <div class="scene-list">
      <div
        v-for="(item, index) in sceneList"
        :key="index"
        :class="{'scene-list-item-select':item.code == sceneSelectId}"
        class="scene-list-item"
        @click="selectScene(item, index)">
        {{ item.name }}
      </div>
    </div>
    <div class="handle-interval">
      <flexbox
        class="handle-button"
        @click.native="addScene">
        <i :class="'add handle-button-icon' | iconPre"/>
        <div class="handle-button-name">新建场景</div>
      </flexbox>
      <flexbox
        class="handle-button"
        @click.native="setScene">
        <i :class="'manage handle-button-icon' | iconPre"/>
        <div class="handle-button-name">管理</div>
      </flexbox>
    </div>
  </div>
</template>

<script type="text/javascript">
import { mapGetters } from 'vuex'
import { crmSceneVisibleListAPI } from '@/api/scene'

export default {
  name: 'SceneList', // 客户管理下 重要提醒 回款计划提醒
  components: {
  },
  props: {
    formCode: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      // 场景列表
      sceneSelectId: 'all',
      sceneList: [{ id: 'all', name: '查询全部', bydata: '' }]
    }
  },
  computed: {
    ...mapGetters(['crm'])
  },
  watch: {
    formCode() {
      this.getSceneList()
    }
  },
  methods: {
    getSceneList() {
      if (!this.formCode) {
        return
      }
      crmSceneVisibleListAPI({ formCode: this.formCode }).then(res => {
        this.sceneList = [{ id: 'all', name: '查询全部', bydata: '' }].concat(res.data)
        const defaultScenes = res.data.filter(function(item, index) {
          return item.current === 1
        })
        if (defaultScenes && defaultScenes.length > 0) {
          const defaultScene = defaultScenes[0]
          this.sceneSelectId = defaultScene.code
          this.$emit('scene', {
            id: defaultScene.code,
            name: defaultScene.name,
            bydata: defaultScene.data || ''
          })
          return
        }
        this.sceneSelectId = ''
      })
        .catch(() => {
          this.$emit('scene', { id: '', name: '', bydata: '' })
        })
    },

    // 选择场景、
    selectScene(item, index) {
      this.sceneSelectId = item.code
      this.$emit('scene', {
        id: item.code,
        name: item.name,
        bydata: item.data
      })
      this.$emit('hidden-scene')
    },
    // 添加场景
    addScene() {
      this.$emit('scene-handle', { type: 'add' })
    },
    // 设置场景
    setScene() {
      this.$emit('scene-handle', { type: 'set' })
    }
  }
}
</script>
<style lang="scss" scoped>
.scene-container {
  position: relative;
  width: 180px;
}

.scene-list {
  max-height: 240px;
  overflow-y: auto;
  font-size: 12px;
  margin-bottom: 10px;
  .scene-list-item {
    color: #333;
    padding: 10px 15px;
    cursor: pointer;
    background-color: white;
  }
  .scene-list-item:hover {
    background-color: #f7f8fa;
    color: $xr-color-primary;
  }

  .scene-list-item-select {
    color: $xr-color-primary;
    background-color: #f7f8fa;
  }
}

.handle-button {
  padding: 6px 20px;
  font-size: 12px;
  cursor: pointer;
  color: $xr-color-primary;
  .handle-button-icon {
    margin-right: 8px;
    margin-top: 3px;
  }
  .handle-button-name {
    font-size: 12px;
  }
}
.handle-button:hover {
  .handle-button-name {
    text-decoration: underline;
  }
}

.handle-interval {
  border-top: 1px solid #EFEFEF;
}
</style>
