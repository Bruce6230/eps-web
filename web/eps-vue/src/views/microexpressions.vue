<template>
	<a-row type='flex'>
		<a-col>
		  <div class='contentBody'>
			<div style='height: 170px;'>
			  <div
				style='width:130px;height:165px; float: left; margin-top: 10px;padding-left:15px;'>
				<div class='zoom'>
				  <img class='zoomOut' title='开启' 
					   src='../assets/video/on.png' @click="getCompetence()">
				  <span style='width: 39px;text-align: center;'>开启</span>
				</div>
				
				<div class='zoom'>
				  <img class='zoomOut' title='关闭' 
					   src='../assets/video/off.png' @click="record()">
				  <span style='width: 39px;text-align: center;'>关闭</span>
				</div>
				<div class='zoom'>
				  <img class='zoomOut' title='运行'
					   src='../assets/video/server.png' @click="submit()">
				  <span style='width: 39px;text-align: center;'>运行</span>
				</div>
			  </div>
			</div>
		  </div>
		</a-col>
		<a-col>
			<div>
				<video id="video" autoplay ref="videos" style="width: 400px;height: 400px;" muted></video>
			</div>
		</a-col>
		<a-col>
			<video style="width: 400px;height: 400px;" id="videosreplay" src="" ref="videosreplay"></video>
		</a-col>
	</a-row>
</template>
<script>
import fixWebmDuration from 'webm-duration-fix'
var startTime, mediaRecorder, mediaStream, stopRecordCallback, recorderFile;
export default {
name: "Microexpressions",
	data () {
	  return {
		progress: 0,
		replayVideo: false,
		recordtype: "BEGIN",
		showReplay: true,
		timer: 0,
		recordtime: 0,
		second: 0,
		minute: 0,
		hour: 0,
		playtime: 0,
		playtimer: 0,
		yy_score: 0,
		cnt_sum: 0,
		ansMaxTime: 0,
		ansBeginTime: 0,
		ansMaxBeginTime: 0,

	  }
	},
	methods: {
	  // 调用摄像头
	  getCompetence () {
		let _this = this;
		MediaUtils.getUserMedia(true, true, function (error, stream) {
		  if (error) {
			throw error;
		  } else {
			// 通过 MediaRecorder 记录获取到的媒体流
			const mimeType = 'video/webm;codecs=vp8,opus';
			mediaRecorder = new MediaRecorder(stream, {
			  // mimeType: "video/webm;codecs=vp9",
			  mimeType: mimeType,
			});
			mediaStream = stream;
			var chunks = []
			var video = _this.$refs.videos;
			video["srcObject"] = stream;
			video.play();// 播放实时画面
			mediaRecorder.ondataavailable = function (e) {
			  mediaRecorder.blobs.push(e.data);
			  chunks.push(e.data);
			};
			mediaRecorder.blobs = [];

			mediaRecorder.onstop = async () => {
			  recorderFile = await fixWebmDuration(new Blob(chunks, { type: mimeType }));
			  console.log(recorderFile);
			  var url = URL.createObjectURL(recorderFile)
			  var videosreplay = _this.$refs.videosreplay;
			  videosreplay.setAttribute("src", url);
			  console.log('url', url)
			  chunks = [];
			  if (null != stopRecordCallback) {
				stopRecordCallback();
			  }
			};
			_this.record()
		  }
		});
	  },
	  record () {
		if (this.recordtype == "ING") {
		  this.stopRecord(() => {
			console.log("结束录制");
			this.toggleReplayVideo()
		  });
		}
		else if (this.recordtype == "BEGIN") {
		  console.log("开始录制");
		  this.startAudio();
		  mediaRecorder.start();
		  startTime = Date.now();
		  this.recordtype = "ING";
		}
	  },

	  // 对录像时长进行记录
	  startAudio () {
		this.timer = setInterval(() => {
		  this.recordtime += 1000;
		  if (this.recordtime == 1000000) {
			this.stopRecord();
		  }
		  this.second++;
		  if (this.second >= 60) {
			this.second = 0;
			this.minute = this.minute + 1;
		  }

		  if (this.minute >= 60) {
			this.minute = 0;
			this.hour = this.hour + 1;
		  }
		  console.log(this.recordtime)
		}, 1000);
	  },

	  // 停止录像时终止录制器，关闭媒体流并清除时长记录定时器
	  stopRecord (callback) {
		this.recordtype = "END";
		this.showReplay = true;
		stopRecordCallback = callback;
		clearInterval(this.timer);
		// 终止录制器
		mediaRecorder.stop();
		// 关闭媒体流
		MediaUtils.closeStream(mediaStream);
		var videosreplay = this.$refs.videosreplay;
		videosreplay.onended = () => {
		  this.playtime = 0;
		  this.replayVideo = false;
		  clearInterval(this.playtimer);
		};
		videosreplay.onclick = () => {
		  this.showReplay = !this.showReplay;
		};
	  },
	  // 回放
	  toggleReplayVideo () {
		console.log('播放中...')
		this.replayVideo = !this.replayVideo;
		this.showReplay = false;
		var videosreplay = this.$refs.videosreplay;
		if (this.replayVideo) {
		  videosreplay.play().catch(err => {
			this.$message.error(err.message);
			console.log(err);
		  });
		  this.playtimer = setInterval(() => {
			this.playtime += 1000;
		  }, 1000);
		} else {
		  videosreplay.pause();
		  clearInterval(this.playtimer);
		}
	  },
	  // 下载视频
	  download () {
		var url = URL.createObjectURL(recorderFile)
		console.log("URL", url)
		const a = document.createElement("a");
		document.body.appendChild(a);
		a.style.display = "none";
		a.href = url;
		if (this.fileName) {
		  a.download = this.fileName + ".mp4";
		} else {
		  a.download = new Date() + ".mp4";
		}
		a.click();
		window.URL.revokeObjectURL(url);
	  },
	  // 下载或上传
	  submit () {
		let _this = this;
		console.log(recorderFile)
		// 下载
		this.download()
		let file = new File(
		  [recorderFile],
		  "msr-" + new Date().toISOString().replace(/:|\./g, "-") + ".mp4",
		  {
			type: "video/mp4",
		  }
		);
		let config = {
		  headers: { "Content-Type": "multipart/form-data" }
		}
		console.log('file', file)
		const formdata = new FormData()
		formdata.append("file", file);
		// 传给后端
		_this.$http("video/microExpressions","POST",formdata,true,function(resp){
			_this.yy_score = parseInt(response.data.data + 0.5)
			_this.progress = response.data.data * 1.0 / 23 * 100
		})
	  },
	}
	}
	var MediaUtils = {
	/**
	 * 获取用户媒体设备(处理兼容的问题)
	 * @param videoEnable {boolean} - 是否启用摄像头
	 * @param audioEnable {boolean} - 是否启用麦克风
	 * @param callback {Function} - 处理回调
	 */
	getUserMedia: function (videoEnable, audioEnable, callback) {
	  navigator.getUserMedia =
		navigator.getUserMedia ||
		navigator.webkitGetUserMedia ||
		navigator.mozGetUserMedia ||
		navigator.msGetUserMedia ||
		window.getUserMedia;
	  var constraints = { video: videoEnable, audio: audioEnable };
	  if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
		navigator.mediaDevices
		  .getUserMedia(constraints)
		  .then(function (stream) {
			callback(false, stream);
		  })
		["catch"](function (err) {
		  callback(err);
		});
	  } else if (navigator.getUserMedia) {
		navigator.getUserMedia(
		  constraints,
		  function (stream) {
			callback(false, stream);
		  },
		  function (err) {
			callback(err);
		  }
		);
	  } else {
		callback(new Error("Not support userMedia"));
	  }
	},

	/**
	 * 关闭媒体流
	 * @param stream {MediaStream} - 需要关闭的流
	 */
	closeStream: function (stream) {
	  if (typeof stream.stop === "function") {
		stream.stop();
	  } else {
		let trackList = [stream.getAudioTracks(), stream.getVideoTracks()];

		for (let i = 0; i < trackList.length; i++) {
		  let tracks = trackList[i];
		  if (tracks && tracks.length > 0) {
			for (let j = 0; j < tracks.length; j++) {
			  let track = tracks[j];
			  if (typeof track.stop === "function") {
				track.stop();
			  }
			}
		  }
		}
	  }
	},
};
</script>