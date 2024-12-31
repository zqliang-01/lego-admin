<template>
  <flexbox
    v-loading="loading"
    align="flex-start"
    justify="flex-start"
    class="fields-index body">
    <flexbox-item class="body-left">
      <div class="body-left_title">
        字段库
        <div class="switch">
          <el-switch v-model="showBorder"/>
          <span>显示分割线</span>
        </div>
      </div>
      <ul>
        <draggable
          :list="fieldLibList"
          v-bind="dragLeftConfig"
          :clone="dragLeftMove"
          class="lib-wrapper"
          @end="dragLeftEnd">
          <div
            v-for="item in fieldLibList"
            :key="item.id"
            class="lib-item"
            @click="handleLibFieldClick(item)">
            <i :class="item.icon | iconPre" class="lib-item-icon" />
            <span>{{ item.name }}</span>
          </div>
        </draggable>
      </ul>
    </flexbox-item>

    <div class="body-content">
      <flexbox
        align="flex-start"
        justify="flex-start"
        direction="column"
        class="body-content-warp">
        <el-header>
          <div class="title">编辑自定义字段（{{ tableName }}）</div>
          <div>
            <template v-if="errorMsg == null">
              <el-button v-debounce="handleReset" type="primary">初始化</el-button>
              <el-button v-debounce="handleSave" type="primary">保存</el-button>
            </template>
            <el-button @click="handleCancel">返回</el-button>
          </div>
        </el-header>
        <flexbox-item style="margin-left: 0" class="body-content-main">
          <div v-if="errorMsg != null">
            {{ errorMsg }}
          </div>
          <el-main>
            <draggable
              :list="fieldList"
              v-bind="dragListConfig"
              class="field-list"
              @end="dragListEnd">
              <flexbox
                v-for="(childArr, fatherIndex) in fieldList"
                :key="fatherIndex"
                align="flex-start"
                justify="flex-start">
                <component
                  v-for="(field, childIndex) in childArr"
                  ref="fieldItem"
                  :key="childIndex"
                  :is="field.componentName"
                  :field="field"
                  :field-list="fieldList"
                  :show-border="showBorder"
                  :point="[fatherIndex, childIndex]"
                  :active-point="selectedPoint"
                  @action="handleAction"
                  @click="handleSelect([fatherIndex, childIndex])" />
              </flexbox>
            </draggable>
          </el-main>
        </flexbox-item>
      </flexbox>
    </div>

    <flexbox-item style="margin-left: 0" class="body-right">
      <setting-field
        v-if="selectedField"
        :field="selectedField"
        :point="selectedPoint"
        :field-list="fieldList"
        :column-list="columnList"
        @update-width="handleUpdateFieldWidth" />
    </flexbox-item>
  </flexbox>
</template>

<script>
import {
  customFieldModifyAPI,
  customFieldInitListAPI,
  customFieldListAPI
} from '@/api/admin/formField'
import { isEmpty } from '@/utils/types'
import { objDeepCopy } from '@/utils/index'

import Field from './field'
import draggable from 'vuedraggable'
import FieldTypeLib from './fieldTypeLib'
import SettingField from './components/SettingField'

const allFields = require.context('./components/FieldItem', true, /\.vue$/)
const fieldComponent = {}
allFields.keys().forEach((item) => {
  const comp = allFields(item)
  const name = comp.default.name
  fieldComponent[name] = comp.default
})

export default {
  name: 'FieldsIndex',
  components: {
    SettingField,
    draggable,
    ...fieldComponent
  },
  data() {
    return {
      loading: false,
      showBorder: false,
      errorMsg: null,
      tableName: '',
      formCode: '',
      columnList: [],
      fieldLibList: FieldTypeLib, // 字段库
      dragLeftConfig: {
        group: {
          pull: 'clone',
          put: false,
          name: 'libList'
        },
        forceFallback: true,
        sort: false
      },
      movedField: null, // 被拖拽的字段库字段
      dragListConfig: {
        delay: 100,
        group: {
          name: 'list',
          put: ['libList'],
          pull: true
        },
        forceFallback: true,
        fallbackClass: 'draggingStyle'
      },
      fieldList: [],
      rejectHandle: true, // 请求未获取前不能操作
      selectedPoint: [null, null],
      selectedField: null
    }
  },
  created() {
    this.formCode = this.$route.params.formCode
    this.getFieldList()
  },
  methods: {
    /**
     * 获取字段列表
     */
    getFieldList() {
      this.loading = true
      customFieldListAPI({
        formCode: this.formCode
      }).then(res => {
        this.fieldList = res.data.fields || []
        this.tableName = res.data.tableName
        this.columnList = res.data.columns
        if (this.fieldList.length > 0) {
          this.handleSelect([0, 0])
        }
        this.rejectHandle = false
        this.loading = false
        this.errorMsg = null
      }).catch(res => {
        this.errorMsg = res.msg
        this.loading = false
      })
    },
    /**
     * 点击左侧字段库进行添加
     */
    handleLibFieldClick(field) {
      this.movedField = field
      this.dragLeftEnd()
    },
    /**
     * 左侧字段库拖拽
     */
    dragLeftMove(field) {
      this.movedField = field
    },
    /**
     * 左侧字段库拖拽结束
     */
    dragLeftEnd(evt) {
      if (this.rejectHandle) {
        this.$message.error('表单未初始化，无法编辑！')
        return
      }
      const newField = new Field({
        name: this.movedField.name,
        formType: this.movedField.formType,
        stylePercent: 100,
        componentName: this.movedField.componentName
      })
      if (this.movedField.formType === 'select') {
        newField.optionDataType = 'dict'
      }
      if (this.movedField.formType === 'descText') {
        newField.name = ''
      }
      let rowNum = 0
      if (evt && evt.pullMode === 'clone' && !isEmpty(evt.newIndex)) {
        rowNum = evt.newIndex
      } else if (this.selectedPoint[0]) {
        rowNum = this.selectedPoint[0] + 1
      }
      this.fieldList.splice(rowNum, 0, [newField])
      this.handleSelect([rowNum, 0])
    },

    /**
     * 拖动 list
     */
    dragListEnd(evt) {
      this.selectedPoint.splice(0, 1, evt.newIndex)
    },

    /**
     * 字段操作
     * @param action {String} 动作
     * @param point {Array} 字段的坐标
     */
    handleAction(action, point) {
      switch (action) {
        case 'top':
          this.handleActionMoveTop(point)
          break
        case 'bottom':
          this.handleActionMoveBottom(point)
          break
        case 'left':
          this.handleActionExchange(point, -1)
          break
        case 'right':
          this.handleActionExchange(point, 1)
          break
        case 'copy':
          this.handleActionCopy(point)
          break
        case 'delete':
          this.handleDelete(point)
      }
    },

    /**
     * 上移字段
     * @param point {Array} 字段的坐标
     */
    handleActionMoveTop(point) {
      const row = this.fieldList[point[0] - 1]
      if (!row || row.length === 4) return
      const field = this.fieldList[point[0]][point[1]]
      // 给新行追加字段
      row.push(objDeepCopy(field))
      let config = this.getWidth(row.length)
      row.forEach(o => {
        o.stylePercent = config.stylePercent
      })
      this.$set(this.fieldList, point[0] - 1, row)

      // 把字段从原来的行中删除
      const oldRow = this.fieldList[point[0]]
      oldRow.splice(point[1], 1)
      if (oldRow.length === 0) {
        this.fieldList.splice(point[0], 1)
      } else {
        config = this.getWidth(oldRow.length)
        oldRow.forEach(o => {
          o.stylePercent = config.stylePercent
        })
        this.$set(this.fieldList, point[0], oldRow)
      }
      this.handleSelect([point[0] - 1, row.length - 1])
    },

    /**
     * 下移字段
     * @param point {Array} 字段的坐标
     */
    handleActionMoveBottom(point) {
      const field = this.fieldList[point[0]][point[1]]
      const row = this.fieldList[point[0]]
      if (row.length === 1) {
        [this.fieldList[point[0] + 1], this.fieldList[point[0]]] = [this.fieldList[point[0]], this.fieldList[point[0] + 1]]
        this.handleSelect([point[0] + 1, 0])
        return
      }
      field.stylePercent = 100
      // 把字段放到新行
      this.fieldList.splice(point[0] + 1, 0, [field])
      // 把字段从原来的行删除
      this.fieldList[point[0]].splice(point[1], 1)
      // 修改原来行的字段占比
      const config = this.getWidth(row.length)
      row.forEach(o => {
        o.stylePercent = config.stylePercent
      })
      this.$set(this.fieldList, point[0], row)
      this.handleSelect([point[0] + 1, 0])
    },

    /**
     * 左右移动交换位置
     * @param point {Array} 字段的坐标
     * @param step {Number} 1 向右移动 -1 向左移动
     */
    handleActionExchange(point, step) {
      const row = this.fieldList[point[0]]
      const field = this.fieldList[point[0]][point[1]]
      row.splice(point[1], 1)
      row.splice(point[1] + step, 0, field)
      this.handleSelect([point[0], point[1] + step])
    },

    /**
     * 拷贝字段
     * @param point {Array} 字段的坐标
     */
    handleActionCopy(point) {
      const field = this.fieldList[point[0]][point[1]]
      const copyField = objDeepCopy(field)
      delete copyField.code
      delete copyField.name
      delete copyField.fieldCode
      delete copyField.relevant
      if (copyField.formType === 'descText') {
        copyField.name = ''
      }
      this.fieldList.splice(point[0] + 1, 0, [copyField])
      this.handleSelect([point[0] + 1, point[1]])
    },

    /**
     * 修改字段占比
     */
    handleUpdateFieldWidth() {
      const row = this.fieldList[this.selectedPoint[0]]

      // 本行占比大于100% 溢出到下一行
      const arr = []
      let child = [] // 行
      let totalWidth = 0
      for (let i = 0; i < row.length; i++) {
        const item = row[i]
        totalWidth += item.stylePercent
        if (totalWidth < 100) {
          // 长度小于 100%
          child.push(item)
        } else if (totalWidth > 100) {
          // 长度大于 100%
          arr.push(objDeepCopy(child))
          child = []
          child.push(item)
          totalWidth = item.stylePercent
        } else {
          // 长度等于 100%
          child.push(item)
          arr.push(objDeepCopy(child))
          child = []
          totalWidth = 0
        }
      }
      if (child.length > 0) {
        arr.push(child)
      }

      // 如果从一行变成多行
      if (arr.length > 1) {
        let rowNum = this.selectedPoint[0] // 行坐标
        let columnNum = this.selectedPoint[1] // 列坐标
        this.fieldList.splice(rowNum, 1, ...arr)
        let step = 0
        for (let i = 0; i < arr.length; i++) {
          step += arr[i].length
          if (step >= columnNum + 1) {
            rowNum += i
            columnNum = columnNum - step + arr[i].length
            break
          }
        }
        this.handleSelect([rowNum, columnNum])
      }
    },

    getWidth(length) {
      if (length === 1) return { stylePercent: 100 }
      if (length === 2) return { stylePercent: 50 }
      if (length > 2) return { stylePercent: 25 }
    },

    /**
     * 删除字段
     * @param point {Array} 字段的坐标
     */
    handleDelete(point) {
      this.$confirm('确定删除该自定义字段吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.selectedPoint = [null, null]
        this.selectedField = null

        this.fieldList[point[0]].splice([point[1]], 1)
        // 如果当前行已经没有元素则删除
        if (this.fieldList[point[0]].length === 0) {
          this.fieldList.splice(point[0], 1)
        }
      }).catch(() => {})
    },

    /**
     * 字段列表点击选择
     * @param point {Array} 字段的坐标
     * @param field {Object} 字段
     */
    handleSelect(point, field = null) {
      this.selectedPoint = point
      this.selectedField = field || this.fieldList[point[0]][point[1]]
    },

    checkFormField(arr) {
      const limitFields = 'select|update|union|and|or|delete|insert|trancate|char|substr|ascii|declare|exec|count|master|into|drop|execute'.split('|')
      const fieldCodes = []
      for (let i = 0; i < arr.length; i++) {
        const item = arr[i]
        const positionStr = `第${item.xAxis + 1}行第${item.yAxis + 1}列【` + item.name + `】`
        item.name = (item.name || '').trim()
        if (item.formType === 'descText') {
          // 描述文字
          if (!isEmpty(item.defaultValue) && item.defaultValue.length > 2000) {
            this.$message.error(positionStr + '描述文字类型字段最多设置2000字')
            return false
          }
          continue
        }
        if (item.formType === 'entity' && isEmpty(item.relativeFormCode)) {
          this.$message.error(positionStr + '关联表字段需要选择所关联的表单')
          return false
        }
        if (item.formType === 'select' && isEmpty(item.optionDictType)) {
          this.$message.error(positionStr + '单选字段需要选择字典类型')
          return false
        }
        if (!item.fieldCode) {
          this.$message.error(positionStr + '自定义字段，标识编码不能为空')
          return false
        }
        if (!item.name) {
          this.$message.error(positionStr + '自定义字段，标识名不能为空')
          return false
        }
        if (limitFields.includes(item.fieldCode)) {
          this.$message.error(positionStr + '自定义字段标识名称[' + item.fieldCode + ']与系统字段重复，请使用其他字段！')
          return false
        }
        if (fieldCodes.includes(item.fieldCode)) {
          this.$message.error(positionStr + '自定义字段标识编码[' + item.fieldCode + ']重复')
          return false
        }
        fieldCodes.push(item.fieldCode)
      }
      return true
    },
    handleReset() {
      this.loading = true
      customFieldInitListAPI({
        formCode: this.formCode
      }).then(res => {
        this.fieldList = res.data.fields || []
        this.tableName = res.data.tableName
        this.columnList = res.data.columns
        if (this.fieldList.length > 0) {
          this.handleSelect([0, 0])
        }
        this.rejectHandle = false
        this.loading = false
        this.errorMsg = null
        this.$message.success('初始化成功！')
      }).catch(() => {
        this.loading = false
      })
    },
    /**
     * 保存
     */
    handleSave() {
      if (this.rejectHandle) {
        this.$message.error('表单未初始化，保存失败！')
        return
      }
      let sn = 0
      const arr = []
      objDeepCopy(this.fieldList).forEach((father, fatherIndex) => {
        father.forEach((child, childIndex) => {
          child.sn = sn++
          if (child.relativeForm) {
            child.relativeFormCode = child.relativeForm.code
          }
          arr.push({
            ...child,
            xAxis: fatherIndex,
            yAxis: childIndex
          })
        })
      })

      if (!this.checkFormField(arr)) {
        return
      }
      if (arr.length == 0) {
        this.$message.error('表单内容不能为空，表单保存失败！')
        return
      }
      this.loading = true
      customFieldModifyAPI({
        data: arr,
        formCode: this.formCode
      }).then(() => {
        this.$message.success('操作成功')
        this.loading = false
        this.getFieldList()
      }).catch(() => {
        this.loading = false
      })
    },

    /**
     * 返回
     */
    handleCancel() {
      this.$router.go(-1)
    }
  }
}
</script>

<style scoped lang="scss">
@import '@/styles/mixin.scss';

.fields-index {
  &.body {
    position: relative;
    width: 100%;
    height: 100%;
    user-select: none;
    overflow: hidden;

    .body-left {
      width: 265px;
      max-width: 265px;
      height: 100%;
      overflow-y: auto;
      background-color: white;
      .body-left_title {
        border-bottom: 1px solid #dbdbdb;
        .switch {
          margin-right: 10px;
          float: right;
          span {
            margin-left: 5px;
            font-weight: normal;
            font-size: 13px;
          }
        }
        font-weight: bold;
        font-size: 14px;
        padding: 20px 20px 15px;
        margin-bottom: 15px;
      }

      .lib-wrapper {
        width: 100%;
        display: flex;
        flex-wrap: wrap;
        padding: 0 10px;
        .lib-item {
          flex: 0 0 50%;
          font-size: 13px;
          background-color: white;
          padding: 5px 10px;
          line-height: 22px;
          cursor: pointer;
          border-radius: 4px;
          margin-bottom: 13px;

          .lib-item-icon {
            display: inline-block;
            color: #999;
            margin-right: 8px;
          }

          &:hover {
            background-color: #f7f8fa;
          }
        }
      }
    }

    .body-content {
      padding: 15px;
      flex: 1;
      height: 100%;

      .body-content-warp {
        max-width: 900px;
        margin: 0 auto;
        height: 100%;
        box-shadow: 0 2px 12px 0 rgba($color: #000, $alpha: 0.1);
        border-radius: $xr-border-radius-base;
        overflow: hidden;
        background-color: white;
        .el-header {
          width: 100%;
          // border-bottom: 1px solid $xr-border-line-color;
          display: flex;
          justify-content: space-between;
          align-items: center;
          .title {
            font-size: 16px;
            font-weight: 600;
            color: #333;
          }
        }
        .body-content-main {
          width: 100%;
          height: 100%;
          overflow-y: auto;
          padding: 0px 16px 30px;
          .el-main {
            .no-list {
              margin: 200px 0;
              color: #ccc;
              @include center;
            }
          }
        }
      }
    }

    .body-right {
      width: 280px;
      max-width: 280px;
      height: 100%;
      overflow-y: auto;
      background-color: white;
    }
  }
}
</style>
