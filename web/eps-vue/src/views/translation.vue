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
					   src='../assets/video/off.png' @click="stopNavigator()">
				  <span style='width: 39px;text-align: center;'>关闭</span>
				</div>
				<div class='zoom'>
				  <img class='zoomOut' title='运行'
					   src='../assets/video/server.png' @click="server()">
				  <span style='width: 39px;text-align: center;'>运行</span>
				</div>
			  </div>
			</div>
		  </div>
		</a-col>
		<a-col>
			<div>
				<video class="videoPlayer" id="videoCamera" :width="videoWidth" :height="videoHeight" autoplay></video>
			</div>
		</a-col>
		<a-col>
			<canvas style="display:none;" class="imagePlayer" id="canvasCamera"></canvas>
			<div v-if="serverVideo" class="img_bg_camera">
			<img :src="serverVideo" alt class="imagePlayer" />
			</div>
		</a-col>
	</a-row>
</template>

<script>
export default {
  data() {
    return {
      videoWidth: 250,
      videoHeight: 350,
      serverVideo: "",
      thisCancas: null,
      thisContext: null,
      thisVideo: null,
      openVideo:false,
	  intervalId: null,
    };
  },
  mounted(){
    this.getCompetence()//进入页面就调用摄像头
  },
  methods: {
    // 调用权限（打开摄像头功能）
    getCompetence() {
      var _this = this;
      _this.thisCancas = document.getElementById("canvasCamera");
      _this.thisContext = this.thisCancas.getContext("2d");
      _this.thisVideo = document.getElementById("videoCamera");
      _this.thisVideo.style.display = 'block';
      // 获取媒体属性，旧版本浏览器可能不支持mediaDevices，我们首先设置一个空对象
      if (navigator.mediaDevices === undefined) {
        navigator.mediaDevices = {};
      }
      // 一些浏览器实现了部分mediaDevices，我们不能只分配一个对象
      // 使用getUserMedia，因为它会覆盖现有的属性。
      // 这里，如果缺少getUserMedia属性，就添加它。
      if (navigator.mediaDevices.getUserMedia === undefined) {
        navigator.mediaDevices.getUserMedia = function(constraints) {
          // 首先获取现存的getUserMedia(如果存在)
          var getUserMedia =
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.getUserMedia;
          // 有些浏览器不支持，会返回错误信息
          // 保持接口一致
          if (!getUserMedia) {//不存在则报错
            return Promise.reject(
              new Error("getUserMedia is not implemented in this browser")
            );
          }
          // 否则，使用Promise将调用包装到旧的navigator.getUserMedia
          return new Promise(function(resolve, reject) {
            getUserMedia.call(navigator, constraints, resolve, reject);
          });
        };
      }
      var constraints = {
        audio: false,
        video: {
          width: this.videoWidth,
          height: this.videoHeight,
          transform: "scaleX(-1)"
        }
      };
      navigator.mediaDevices
        .getUserMedia(constraints)
        .then(function(stream) {
          // 旧的浏览器可能没有srcObject
          if ("srcObject" in _this.thisVideo) {
            _this.thisVideo.srcObject = stream;
          } else {
            // 避免在新的浏览器中使用它，因为它正在被弃用。
            _this.thisVideo.src = window.URL.createObjectURL(stream);
          }
          _this.thisVideo.onloadedmetadata = function(e) {
            _this.thisVideo.play();
          };
        })
        .catch(err => {
          console.log(err);
        });
    },
	server() {
		// 每2秒钟拍一张照片
		this.intervalId = setInterval(() => {
			this.setImage();
		}, 1000);
	},
    //  绘制图片（拍照功能）
    setImage() {
      var _this = this;
      // canvas画图
      _this.thisContext.drawImage(
        _this.thisVideo,
        0,
        0,
        _this.videoWidth,
        _this.videoHeight
      );
      // 获取图片base64链接
      var image = _this.thisCancas.toDataURL("image/png");
	  _this.$http("video/fatigueDetection","POST",{photo: image},true,function(resp) {
		  _this.image = resp.photo
		  console.log(resp.photo)
	  })
      _this.serverVideo = _this.image;//赋值并预览图片
    },
    // 关闭摄像头
    stopNavigator() {
      this.thisVideo.srcObject.getTracks()[0].stop();
    }
  }
};
</script>

<style lang="less" scoped="scoped">
@import url('translation.less');
</style>
