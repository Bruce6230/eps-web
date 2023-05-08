<template>
  <div>
    <form @submit.prevent>
      <label>文本</label>
      <input type="text" v-model="message" placeholder="这里输入消息" />
      <br />
      <br />
      <input type="button" value="发送chatRoom消息" @click="send()" />
    </form>
    <hr color="black" />
    <h3>服务端返回的应答消息</h3>
    <textarea v-model="responseText" style="width: 1024px; height: 300px;"></textarea>
  </div>
</template>

<script>
export default {
  data() {
    return {
      message: '',
      responseText: '',
      socket: null
    };
  },
  created() {
    if (window.WebSocket) {
      this.socket = new WebSocket('ws://localhost:8888/ws');
      this.socket.onmessage = event => {
        this.responseText += event.data + '\r\n';
      };
      this.socket.onopen = event => {
        this.responseText = 'Netty-WebSocket服务器。。。。。。连接  \r\n';
      };
      this.socket.onclose = event => {
        this.responseText = 'Netty-WebSocket服务器。。。。。。关闭 \r\n';
      };
    } else {
      alert('您的浏览器不支持WebSocket协议！');
    }
  },
  methods: {
    send() {
      if (!window.WebSocket) return;
      if (this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(this.message);
      } else {
        alert('WebSocket 连接没有建立成功！');
      }
    }
  }
};
</script>
