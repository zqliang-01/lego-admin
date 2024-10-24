import request from '@/utils/request'
import config from '@/config'

export const filePreviewUrl = config.apiUrl + '/back-end/sys-file/download/';

// 图片上传
export const image = (files) => {
  // 文件上传大小, 2M
  const maxSize = 1024 * 1024 * 2;
  // 执行上传
  return request.urlFileUpload({
		files, maxSize,
		data: {
			permissionCode: 'manage',
			entityCode: 'picture'
		}
	})
}
