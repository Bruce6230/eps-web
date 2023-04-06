<template>
  <div>
    <video ref="video" autoplay playsinline></video>
    <button @click="startRecording">开始录制</button>
    <button @click="stopRecording">停止录制</button>
  </div>
</template>

<script>
import RecordRTC from 'recordrtc';

export default {
  data() {
    return {
      recordRTC: null,
    };
  },
  methods: {
    async startRecording() {
      const stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
      this.$refs.video.srcObject = stream;
      this.recordRTC = RecordRTC(stream, { type: 'video' });
      this.recordRTC.startRecording();
    },
    async stopRecording() {
      this.recordRTC.stopRecording(async () => {
        const videoBlob = this.recordRTC.getBlob();
        this.$refs.video.srcObject.getTracks().forEach((track) => track.stop());

        // 将视频Blob转换为Base64编码
        const reader = new FileReader();
        reader.readAsDataURL(videoBlob);
        reader.onloadend = async () => {
          const base64Data = reader.result;
          console.log('视频的Base64编码:', base64Data);

          // 将Base64编码的视频数据发送到后端
          await this.sendVideoToBackend(base64Data);
        };
      });
    },
    async sendVideoToBackend(base64Data) {
		var _this = this;
		_this.$http("video/fireDetection","POST",{video: base64Data},true,function(resp) {
			console.log(resp.video)
		})
    },
  },
};
</script>
