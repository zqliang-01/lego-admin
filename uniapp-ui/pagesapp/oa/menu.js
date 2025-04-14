module.exports = {
	app: {
		code: 'oa',
		name: '任务审批',
		list: [
			{ code: 'oa_start', name: '发起审批', icon: 'top', path: 'pagesapp/oa/start/index' },
			{ code: 'oa_owner', name: '我的流程', icon: 'my-task', path: 'pagesapp/oa/owner/index' },
			{ code: 'oa_undo', name: '待办任务', icon: 'contract', path: 'pagesapp/oa/undo/index' },
			{ code: 'oa_unclaimed', name: '待签任务', icon: 'icon-related-tasks', path: 'pagesapp/oa/claim/index' },
			{ code: 'oa_finished', name: '已办任务', icon: 'icon-task-state', path: 'pagesapp/oa/finished/index' }
		]
	},
	pages: [
		{
			"path": "oa/start/index",
			"style": {
				"navigationBarTitleText": "发起审批",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "oa/start/startForm",
			"style": {
				"navigationBarTitleText": "发起审批表单"
			}
		},
		{
			"path": "oa/owner/index",
			"style": {
				"navigationBarTitleText": "我的流程",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "oa/undo/index",
			"style": {
				"navigationBarTitleText": "待办任务",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "oa/claim/index",
			"style": {
				"navigationBarTitleText": "待签任务",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "oa/finished/index",
			"style": {
				"navigationBarTitleText": "已办任务",
				"enablePullDownRefresh": true
			}
		}
	]
}