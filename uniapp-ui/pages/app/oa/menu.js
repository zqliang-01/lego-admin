module.exports = {
	app: {
		code: 'oa',
		name: '任务审批',
		list: [
			{ code: 'oa_start', name: '发起审批', icon: 'top', path: 'pages/app/oa/start/index' },
			{ code: 'oa_owner', name: '我的流程', icon: 'my-task', path: 'pages/app/oa/owner/index' },
			{ code: 'oa_undo', name: '待办任务', icon: 'contract', path: 'pages/app/oa/undo/index' },,
			{ code: 'oa_unclaimed', name: '待签任务', icon: 'icon-related-tasks', path: 'pages/app/oa/claim/index' },
			{ code: 'oa_finished', name: '已办任务', icon: 'icon-task-state', path: 'pages/app/oa/finished/index' }
		]
	},
	pages: [
		{
			"path": "pages/app/oa/start/index",
			"style": {
				"navigationBarTitleText": "发起审批",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "pages/app/oa/start/startForm",
			"style": {
				"navigationBarTitleText": "发起审批表单"
			}
		},
		{
			"path": "pages/app/oa/owner/index",
			"style": {
				"navigationBarTitleText": "我的流程",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "pages/app/oa/undo/index",
			"style": {
				"navigationBarTitleText": "待办任务",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "pages/app/oa/claim/index",
			"style": {
				"navigationBarTitleText": "待签任务",
				"enablePullDownRefresh": true
			}
		},
		{
			"path": "pages/app/oa/finished/index",
			"style": {
				"navigationBarTitleText": "已办任务",
				"enablePullDownRefresh": true
			}
		}
	]
}