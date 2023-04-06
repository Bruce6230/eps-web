<template>
  <div class="main-container">
    <div class="chat-container">
		
      <div class="chat-header">
        <h4>ChatGPT-3.5</h4>
      </div>
	  
      <div class="chat-messages">
        <ul class="chat-message-container">
          <li
            class="chat-message"
            v-for="(item, i) in items"
            :class="item.user === 'bot' ? 'chat-reply' : 'chat-question'"
            :key="i"
          >
            <div v-html="item.html ? item.html : item.message || ''"></div>
          </li>
        </ul>
      </div>
      <div class="chat-input">
        <input
          type="text"
          v-model="message"
          class="message-input"
          @keydown.enter="onSendMessage"
          placeholder="请输入您的消息..."
        />
        <button
          type="submit"
          class="send-btn"
          @click="onSendMessage"
          :disabled="generating"
        >
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatGPT',
  data() {
    return {
      message: '',
      items: [
        { user: 'bot', messageType: 'TEXT', message: '欢迎使用ChatGPT', html: '' },
      ],
      generating: false,
    };
  },
  methods: {
    getUuid() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = (Math.random() * 16) | 0,
          v = c == 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
      });
    },
    getUser() {
      return localStorage.getItem('user');
    },
    async onSendMessage() {
      console.log('message:', this.message);
      if (!this.message) {
        alert('请输入内容');
        return;
      }
      if (this.generating) {
        alert('请等待生成完毕');
        return;
      }
      let sse = new EventSource(`http://localhost:8090/eps-web/openai/completions/stream?user=${this.getUser()}&prompt=${this.message}`);
      this.generating = true;
      this.items.push({ user: 'user', messageType: 'TEXT', message: this.message });
      this.message = '';
      sse.addEventListener(
        'open',
        function () {
          console.log('open');
          this.generating = true;
          this.items.push({ user: 'bot', messageType: 'TEXT', message: '' });
        }.bind(this)
      );
      sse.addEventListener(
        'message',
        function (res) {
          let resJson = JSON.parse(res.data);
          console.log('resJson', resJson);
          if (resJson.messageType === 'TEXT') {
            if (resJson.end === true) {
              sse.close();
              this.generating = false;
            } else {
              let last = this.items[this.items.length - 1];
              last.message += resJson.message;
              let words = last.message.split('');
              let html = words.join('');
            }
          } else {
                   let url_html = '';
                   resJson.message.split(',').forEach((url) => {
                     url_html += `<img class="chat-img" src="${url}"/>`;
                   });
                   this.items[this.items.length - 1] = { user: 'bot', messageType: 'IMAGE', message: url_html };
                   sse.close();
                   this.generating = false;
                 }
               }.bind(this)
             );
           
             sse.addEventListener('error', function () {
               console.log('error');
               this.generating = false;
           
               alert('服务器错误，请查看日志');
             }.bind(this));
           },
		},
		mounted() {
		console.log('mounted');
		let user = this.getUser();
		if (!user) {
		console.log('No user found, creating new user');
		localStorage.setItem('user', this.getUuid());
		}
		console.log('user:', this.getUser());
	},
};
</script>
		
<style scoped>
.chat-reply {
  background: #f5f5f5;
  border-radius: 10px;
  padding: 10px;
  margin-bottom: 10px;
}

.chat-question {
  text-align: right;
  margin-bottom: 10px;
}

.chat-message-container {
  list-style: none;
  margin: 0;
  padding: 0;
}

.chat-img {
  width: 200px;
  height: auto;
}

.main-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.chat-input {
  display: flex;
  margin-top: 20px;
}

.message-input {
  flex: 1;
  border: none;
  border-bottom: 2px solid #4f4f4f;
  margin-right: 10px;
  padding: 10px 0;
  font-size: 16px;
  outline: none;
}

.send-btn {
  border: none;
  background: #4f4f4f;
  color: #fff;
  font-size: 16px;
  padding: 10px 20px;
  border-radius: 5px;
  cursor: pointer;
  outline: none;
}

.send-btn:hover {
  background: #5f5f5f;
}
</style>

