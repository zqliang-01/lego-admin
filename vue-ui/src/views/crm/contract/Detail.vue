<template>
  <slide-view
    v-empty="!canShowDetail"
    :listener-ids="listenerIDs"
    :no-listener-ids="noListenerIDs"
    :no-listener-class="noListenerClass"
    :body-style="{padding: 0, height: '100%'}"
    xs-empty-icon="nopermission"
    xs-empty-text="暂无权限"
    @afterEnter="viewAfterEnter"
    @close="hideView">
    <div
      v-loading="loading"
      ref="detailMain"
      class="detail-main">
      <flexbox
        v-if="canShowDetail"
        direction="column"
        align="stretch"
        class="d-container">
        <lego-detail-head
          :action-types="actionTypes"
          :detail="detailData"
          :menu-code="menuCode"
          :head-field-list="headFieldList"
          :page-list="pageList"
          type-name="合同"
          @pageChange="pageChange"
          @handle="actionHandle"
          @close="hideView" />
        <flexbox class="d-container-bd" align="stretch">
          <el-tabs
            v-model="tabCurrentName"
            type="border-card"
            class="d-container-bd--left">
            <el-tab-pane
              label="详细资料"
              name="LegoEditBaseInfo"
              lazy>
              <lego-edit-base-info
                :code="code"
                :detail="detailData"
                :field-list="fieldList"
                :system-field-list="systemFieldList"
                @handle="actionHandle"
                @clickEntity="handleEntityClick" />
            </el-tab-pane>
            <el-tab-pane
              v-for="(item, index) in tabNames"
              :key="index"
              :label="item.label"
              :name="item.name"
              lazy>
              <component
                :is="item.name"
                :code="code"
                :menu-code="menuCode"
                :detail="detailData"
                @handle="actionHandle" />
            </el-tab-pane>
          </el-tabs>
          <transition name="slide-fade">
            <el-tabs
              v-show="showImportInfo"
              value="chiefly-contacts"
              type="border-card"
              class="d-container-bd--right">
              <el-tab-pane
                label="重要信息"
                name="chiefly-contacts"
                lazy>
                <import-info :list="importList" :detail="detailData" class="import-info" />
              </el-tab-pane>
            </el-tabs>
          </transition>
        </flexbox>
      </flexbox>
      <!-- 新建 -->
      <create
        v-if="isCreate"
        :field-list="fieldList"
        :action="{type: 'update', detailData: detailData}"
        @close="isCreate = false"
        @handle="actionHandle"
      />
    </div>
    <el-button
      class="firse-button"
      @click="showImportInfo= !showImportInfo">
      重<br>要<br>信<br>息<br>
      <i :class="{ 'is-reverse': !showImportInfo }" class="el-icon-arrow-right el-icon--right" />
    </el-button>
    <div
      v-show="visible"
      class="full-container">
      <component
        v-for="(item, index) in entityDetailList"
        v-if="item.show"
        :key="index"
        :is="item.name"
        :code.sync="item.detailCode"
        :menu-code="item.menuCode"
        :field-list="item.fieldList"
        class="d-view"
        @hide-view="closeEntityDetail(item)"/>
    </div>
  </slide-view>
</template>

<script>
import {
  contractGetAPI,
  contractUpdateAPI,
  contractDeleteAPI
} from '@/api/crm/contract'

import Create from './Create'
import CrmLeadDetail from '@/views/crm/lead/Detail'
import CrmCustomerDetail from '@/views/crm/customer/Detail'
import DetailMixin from '@/components/Mixins/LegoDetail'
import RelativeHandle from '@/components/Relative/RelativeHandle'
import RelativeFile from '@/components/Relative/RelativeFile'

export default {
  name: 'CrmContractDetail',
  components: {
    CrmLeadDetail,
    CrmCustomerDetail,
    Create,
    RelativeHandle,
    RelativeFile
  },
  mixins: [DetailMixin],
  props: {
    code: [String, Number],
    fieldList: Array
  },
  data() {
    return {
      isCreate: false,
      updateRequest: contractUpdateAPI,
      detailRequest: contractGetAPI,
      deleteRequest: contractDeleteAPI,
      entityDetailList: [
        { code: 'lead', name: 'CrmLeadDetail', show: false },
        { code: 'customer', name: 'CrmCustomerDetail', show: false }
      ]
    }
  },
  computed: {
    /** 明细tab页 */
    tabNames() {
      return [
        { label: '操作日志', name: 'RelativeHandle' },
        { label: '附件', name: 'RelativeFile' }
      ]
    },
    /** 更多操作 */
    actionTypes() {
      return [
        { name: '测试', type: 'test', icon: 'transfer' }
      ]
    }
  },
  methods: {
    /** 更多操作事件回调 */
    doActionHandler(data) {
      if (data.type === 'delete') {
        this.$confirm('此操作将永久删除[' + this.code + ']，是否继续?', '提示', {
          type: 'warning'
        }).then(() => {
          const loading = Loading.service({ fullscreen: true, text: '删除中...' })
          this.deleteRequest([this.code]).then(res => {
            this.getMainTable().clearSelection()
            this.getList()
            loading.close()
          })
            .catch(() => {
              loading.close()
            })
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/detail.scss';
</style>
