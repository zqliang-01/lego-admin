import request from "../request.js";
const {
	chooseImage,
	chooseVideo,
	urlUpload
} = require("./utils");
import {
	mergeConfig
} from "../utils.js";
export default class fileUpload extends request {
	constructor(props) {
		// 调用实现父类的构造函数
		super(props);
	}
	//本地服务器图片上传
	async urlImgUpload() {
		let options = {};
		if (arguments[0]) {
			if (typeof(arguments[0]) == "string") {
				options.url = arguments[0];
			} else if (typeof(arguments[0]) == "object") {
				options = Object.assign(options, arguments[0]);
			}
		}
		if (arguments[1] && typeof(arguments[1]) == "object") {
			options = Object.assign(options, arguments[1]);
		}
		try {
			options.files = await chooseImage(options);
			// 选择完成回调
			options.onSelectComplete && options.onSelectComplete(options.files);
		} catch (err) {
			this.requestError && this.requestError(err);
			return Promise.reject(err);
		}
		if (options.files) {
			return this.urlFileUpload(options);
		}
	}
	//本地服务器上传视频
	async urlVideoUpload() {
		let options = {};
		if (arguments[0]) {
			if (typeof(arguments[0]) == "string") {
				options.url = arguments[0];
			} else if (typeof(arguments[0]) == "object") {
				options = Object.assign(options, arguments[0]);
			}
		}
		if (arguments[1] && typeof(arguments[1]) == "object") {
			options = Object.assign(options, arguments[1]);
		}
		try {
			options.files = await chooseVideo(options);
			// 选择完成回调
			options.onSelectComplete && options.onSelectComplete(options.files);
		} catch (err) {
			this.requestError && this.requestError(err);
			return Promise.reject(err);
		}
		if (options.files) {
			return this.urlFileUpload(options);
		}
	}
	//本地服务器文件上传方法
	async urlFileUpload() {
		let requestInfo = {
			method: "FILE"
		};
		if (arguments[0]) {
			if (typeof(arguments[0]) == "string") {
				requestInfo.url = arguments[0];
			} else if (typeof(arguments[0]) == "object") {
				requestInfo = Object.assign(requestInfo, arguments[0]);
			}
		}
		if (arguments[1] && typeof(arguments[1]) == "object") {
			requestInfo = Object.assign(requestInfo, arguments[1]);
		}
		if (!requestInfo.url && this.defaultUploadUrl) {
			requestInfo.url = this.defaultUploadUrl;
		}
		// 请求数据
		// 是否运行过请求开始钩子
		let runRequestStart = false;
		try {
			if (!requestInfo.url) {
				throw {
					code: 9999,
					msg: "【request】文件上传缺失数据url",
					data: requestInfo.data,
					method: requestInfo.method,
					header: requestInfo.header,
					url: requestInfo.url,
				}
			}
			// 数据合并
			requestInfo = mergeConfig(this, requestInfo);
			// 代表之前运行到这里
			runRequestStart = true;
			//请求前回调
			if (this.requestStart) {
				let requestStart = this.requestStart(requestInfo);
				if (typeof requestStart == "object") {
					let changekeys = ["data", "header", "isPrompt", "load", "isFactory", "files"];
					changekeys.forEach(key => {
						requestInfo[key] = requestStart[key];
					});
				} else {
					throw {
						code: 9999,
						msg: "【request】请求开始拦截器未通过",
						data: requestInfo.data,
						method: requestInfo.method,
						header: requestInfo.header,
						url: requestInfo.url,
					}
				}
			}
			let requestResult = await urlUpload(requestInfo, this.dataFactory);
			return Promise.resolve(requestResult);
		} catch (err) {
			this.requestError && this.requestError(err);
			return Promise.reject(err);
		} finally {
			if (runRequestStart) {
				this.requestEnd && this.requestEnd(requestInfo);
			}
		}
	}
}