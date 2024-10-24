//生成随机命名
export const randomChar = function(l, url = "") {
	const x = "0123456789qwertyuioplkjhgfdsazxcvbnm";
	let tmp = "";
	let time = new Date();
	for (let i = 0; i < l; i++) {
		tmp += x.charAt(Math.ceil(Math.random() * 100000000) % x.length);
	}
	return "file/" + url + time.getTime() + tmp;
}
//图片选择
export const chooseImage = function(data) {
	return new Promise((resolve, reject) => {
		uni.chooseImage({
			count: data.count || 9, //默认9
			sizeType: data.sizeType || ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
			sourceType: data.sourceType || ['album', 'camera'], //从相册选择
			success: function(res) {
				resolve(res.tempFiles);
			},
			fail: err => {
				reject({
					msg: err.errMsg,
					code: err.errCode
				});
			}
		});
	});
}
//视频选择
export const chooseVideo = function(data) {
	return new Promise((resolve, reject) => {
		uni.chooseVideo({
			sourceType: data.sourceType || ['album', 'camera'], //从相册选择
			compressed: data.compressed || false, //是否压缩所选的视频源文件，默认值为 true，需要压缩。
			maxDuration: data.maxDuration || 60, //拍摄视频最长拍摄时间，单位秒。最长支持 60 秒。
			camera: data.camera || 'back', //'front'、'back'，默认'back'
			success: function(res) {
				let files = [{
					path: res.tempFilePath
				}];
				// #ifdef APP-PLUS || H5 || MP-WEIXIN
				files[0].duration = res.duration;
				files[0].size = res.size;
				files[0].height = res.height;
				files[0].width = res.width;
				// #endif
				// #ifdef H5
				files[0].name = res.name;
				// #endif
				resolve(files);
			},
			fail: err => {
				reject({
					msg: err.errMsg,
					code: err.errCode
				});
			}
		});
	});
}
// 服务器URL上传
export const urlUpload = function(requestInfo, dataFactory) {
	return new Promise((resolve, reject) => {
		// 本地文件上传去掉默认Content-Type
		if (requestInfo.header['Content-Type']) {
			delete requestInfo.header['Content-Type'];
		}
		// 本地文件上传去掉默认Content-Type
		if (requestInfo.header['content-type']) {
			delete requestInfo.header['content-type'];
		}
		if (Array.isArray(requestInfo.files)) {
			const len = requestInfo.files.length - 1;
			let fileList = new Array;
			fileUpload(0);

			function fileUpload(i) {
				let item = requestInfo.files[i];
				let fileData = {
					fileIndex: i,
					files: requestInfo.files,
					...item
				};
				let config = {
					url: requestInfo.url,
					filePath: item.path,
					header: requestInfo.header, //加入请求头
					name: requestInfo.name || "file",
					success: (response) => {
						//是否用外部的数据处理方法
						if (requestInfo.isFactory && dataFactory) {
							//数据处理
							dataFactory({
								...requestInfo,
								response: response,
							}).then(data => {
								fileList.push(data);
								requestInfo.onEachUpdate && requestInfo.onEachUpdate({
									data: data,
									...fileData
								});
								if (len <= i) {
									resolve(fileList);
								} else {
									fileUpload(i + 1);
								}
							}, err => {
								reject(err);
							});
						} else {
							requestInfo.onEachUpdate && requestInfo.onEachUpdate({
								data: response,
								...fileData
							});
							fileList.push(response);
							if (len <= i) {
								resolve(fileList);
							} else {
								fileUpload(i + 1);
							}
						}
					},
					fail: (err) => {
						reject(err);
					}
				};
				if (requestInfo.data) {
					config.formData = requestInfo.data;
				}
				const uploadTask = uni.uploadFile(config);
				uploadTask.onProgressUpdate(res => {
					requestInfo.onProgressUpdate && requestInfo.onProgressUpdate(Object.assign({}, fileData, res));
				});
			}
		} else {
			reject({
				code: 0,
				msg: "files 必须是数组类型"
			});
		}
	});
}