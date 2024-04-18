<template>
  <lego-popover
    :disabled="disabled"
    :value="resultList"
    @value-change="valueChange"
    @confirm="handleConfirm">
    <div class="dept-content">
      <div
        v-loading="loading"
        class="select-input">
        <el-input
          v-model="searchInput"
          placeholder="搜索部门"
          size="small"
          style="margin-bottom:10px;"
          prefix-icon="el-icon-search"
          class="search-input"
          @input="searchChange" />
        <div class="search-list">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item
              v-for="(item, index) in parentDeptList"
              :key="index">
              <a
                href="javascript:;"
                @click="handleParentDeptClick(item, index)">{{ item.name }}</a>
            </el-breadcrumb-item>
          </el-breadcrumb>
          <div class="checkout-boxs">
            <template v-for="(item, index) in deptList">
              <div
                v-if="!item.hidden"
                :key="index"
                class="checkout-box">
                <el-checkbox
                  v-model="item.isCheck"
                  @change="deptCheckboxChange(item, index)" />
                <div @click="enterDeptChildren(item)">
                  <span>{{ item.name }}</span>
                  <span class="el-icon-arrow-right" />
                </div>
              </div>
            </template>
          </div>
        </div>
      </div>
      <div class="checked-content">
        <div class="checked-top">
          <span class="select-info">已选择<span class="select-info--num">{{ this.selectedList.length }}</span>项</span>
          <el-button
            type="text"
            @click="clearAll">清空</el-button>
        </div>
        <div class="select-content">
          <flexbox
            justify="stretch"
            wrap="wrap">
            <div
              v-for="(item, index) in selectedList"
              :key="index"
              class="select-item text-one-line">
              {{ item.name }}<i class="el-icon-close delete-icon" @click="selectDelete(index)" />
            </div>
          </flexbox>
        </div>
      </div>
    </div>
  </lego-popover>
</template>
<script>
import { deptChildrenListAPI } from '@/api/admin/dept'
import PinyinMatch from 'pinyin-match'
import LegoPopover from '@/components/lego/LegoPopover'

export default {
  name: 'DeptSelect',
  components: {
    LegoPopover
  },
  props: {
    value: {
      type: Array,
      default: () => {
        return []
      }
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      searchInput: '',
      loading: false,
      deptList: [],
      parentDeptList: [],
      selectedList: [],
      resultList: []
    }
  },
  computed: {
  },
  watch: {
    value: {
      handler() {
        this.initInfo()
      },
      deep: true,
      immediate: true
    },
    selectedList: {
      handler() {
        this.deptList.forEach(item => {
          item.isCheck = this.getItemCheckInfo(item)
        })
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    initInfo() {
      // 用户列表
      this.parentDeptList = []
      this.parentDeptList.push({ name: '全部' })
      this.selectedList = this.value.map(item => {
        return item
      })
      this.resultList = this.value.map(item => {
        return item
      })
      this.getDeptList()
    },
    searchChange(val) {
      this.deptList = this.deptList.map(item => {
        item.hidden = !PinyinMatch.match(item.name, val)
        return item
      })
    },
    getDeptList(parentCode) {
      this.loading = true
      deptChildrenListAPI({ parentCode: parentCode }).then(res => {
        this.deptList = res.data.map((item, index, array) => {
          item.hidden = !PinyinMatch.match(item.name, this.searchInput)
          item.isCheck = this.getItemCheckInfo(item)
          return item
        })
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    enterDeptChildren(deptItem) {
      this.getDeptList(deptItem.code)
      this.parentDeptList.push(deptItem)
    },
    // 面包屑点击事件
    handleParentDeptClick(item, index) {
      if (index + 1 <= this.parentDeptList.length - 1) {
        this.parentDeptList.splice(index + 1, this.parentDeptList.length - 1)
      }
      this.getDeptList(item.code)
    },
    // 部门勾选
    deptCheckboxChange(item) {
      var removeIndex = -1
      for (let index = 0; index < this.selectedList.length; index++) {
        const element = this.selectedList[index]
        if (item.code == element.code) {
          removeIndex = index
        }
      }
      if (removeIndex == -1) {
        this.selectedList.push(item)
      } else {
        this.selectedList.splice(removeIndex, 1)
      }
    },
    getItemCheckInfo(item) {
      return this.selectedList.some(selected => selected.code === item.code)
    },
    selectDelete(index) {
      this.selectedList.splice(index, 1)
      this.deptList = this.deptList.map((item, index, array) => {
        item.isCheck = this.getItemCheckInfo(item)
        return item
      })
    },
    valueChange(index) {
      if (index >= 0 && this.resultList.length > 0) {
        this.resultList.splice(index, 1)
      }
      this.selectedList = this.resultList.map(item => {
        return item
      })
      const codes = this.resultList.map(item => {
        return item.code
      })
      this.$emit('value-change', codes)
    },
    handleConfirm() {
      this.resultList = this.selectedList.map(item => {
        return item
      })
      const codes = this.resultList.map(item => {
        return item.code
      })
      this.$emit('value-change', codes)
    },
    // 清空按钮
    clearAll() {
      this.selectedList = []
      this.deptList = this.deptList.map(item => {
        item.isCheck = false
        return item
      })
    },

    /**
     * 修改全部数据的状态啊
     */
    updateCheckInfoByWatch() {
      if (this.deptList.length) {
        this.deptList.forEach(item => {
          item.isCheck = this.getItemCheckInfo(item)
        })
      }
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/styles/mixin.scss';
.dept-content {
  display: flex;
  .select-input {
    flex: 1;
    padding: 10px;
    overflow: hidden;
    border-right: 1px solid $xr-border-line-color;
    .select-input > .el-input {
      margin: 10px;
    }

    .el-checkbox {
      margin-left: 0;
      margin-right: 10px;
    }
    .el-breadcrumb {
      margin-bottom: 15px;
    }
    .checkout-box {
      display: flex;
      margin-bottom: 10px;
    }
    .checkout-box > div {
      flex: 1;
      .el-icon-arrow-right {
        float: right;
      }
      span {
        cursor: pointer;
      }
    }
  }

  .checked-content {
    flex: 1;
    .checked-top {
      padding: 0 12px;
      height: 40px;
      line-height: 40px;
      margin-bottom: 12px;
      border-bottom: 1px solid $xr-border-line-color;
      .title {
        color: #999999;
      }
    }
    .select-content {
      padding: 0 12px;
      height: 220px;
      overflow: auto;
      .select-item {
        position: relative;
        flex-shrink: 0;
        background-color: #f3f7ff;
        height: 28px;
        line-height: 28px;
        max-width: 80px;
        font-size: 12px;
        padding: 0 15px 0 5px;
        border-radius: $xr-border-radius-base;
        color: #333;
        margin: 0 10px 10px 0;

        i {
          cursor: pointer;
          color: #666;
          margin-left: 5px;
        }

        i:hover {
          color: $xr-color-primary;
        }
      }
    }
  }
}

/* 选择员工 */
.search-img {
  vertical-align: middle;
  margin-right: 8px;
}
.search-list {
  margin: 5px;
  height: 180px;
  overflow: auto;
  margin-right: -10px;
  padding-right: 10px;
}

.checkout-boxs {
  height: 150px;
  overflow-y: auto;
  overflow-x: hidden;
}

.colleagues-list {
  padding: 5px 0;
}

// 选择信息
.select-info {
  display: inline-block;
  width: calc(100% - 40px);

  &--num {
    color: $xr-color-primary;
  }
}

// check样式
.el-checkbox {
  ::v-deep .el-checkbox__label {
    color: #333;
  }
}

.all-check {
  display: inline-block;
  padding: 5px 0;
}

.search-input {
  ::v-deep .el-input__inner {
    background-color: #f4f4f4;
    border: none;
  }
}

.delete-icon {
  color: #999;
  cursor: pointer;
  position: absolute;
  top: 8px;
  right: 2px;

  &:hover {
    color: $xr-color-primary;
  }
}
</style>

