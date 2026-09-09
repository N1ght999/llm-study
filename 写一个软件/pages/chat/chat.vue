<template>
    <view class="chat-container">
        <!-- 顶部状态栏装饰 -->
        <view class="tech-header">
            <view class="header-glow"></view>
            <view class="header-line"></view>
            <text class="header-title">NEURAL LINK</text>
            <view class="header-line"></view>
            <view class="header-glow"></view>
        </view>

        <scroll-view class="msg-list" scroll-y :scroll-top="scrollTop" scroll-with-animation>
            <!-- 背景网格装饰 -->
            <view class="grid-bg"></view>

            <view v-for="(item, index) in msgList" :key="index" class="msg-item" :class="item.isSelf ? 'self' : 'ai'">
                <view class="avatar">
                    <view class="avatar-ring"></view>
                    <text class="avatar-text">{{ item.isSelf ? '我' : 'AI' }}</text>
                </view>
                <view class="bubble-wrapper">
                    <view class="bubble">{{ item.content }}</view>
                    <view class="bubble-glow"></view>
                </view>
            </view>

            <!-- 底部占位 -->
            <view style="height: 20rpx;"></view>
        </scroll-view>

        <view class="input-bar">
            <view class="input-wrapper">
                <view class="input-icon">⌘</view>
                <input class="input" v-model="inputText" placeholder="输入消息..." confirm-type="send" @confirm="sendMsg"
                    placeholder-class="input-placeholder" />
                <!-- <view class="input-cursor"></view> -->
            </view>
            <button class="send-btn" @click="sendMsg" :disabled="!inputText.trim()">
                <text class="send-btn-text">发送</text>
                <view class="send-btn-glow"></view>
            </button>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            msgList: [],
            inputText: '666',
            scrollTop: 0,
            baseUrl: 'http://localhost:8080/api/chat'
        }
    },
    methods: {
        sendMsg() {
            const text = this.inputText.trim()
            if (!text) return

            this.msgList.push({
                content: text,
                isSelf: true
            })
            this.inputText = ''
            this.scrollToBottom()

            uni.request({
                url: this.baseUrl,
                method: 'POST',
                data: {
                    message: text
                },
                header: {
                    'Content-Type': 'application/json'
                },
                success: (res) => {
                    this.msgList.push({
                        content: res.data.data,
                        isSelf: false
                    })
                    this.scrollToBottom()
                },
                fail: () => {
                    uni.showToast({
                        title: '网络错误',
                        icon: 'none'
                    })
                }
            })
        },
        scrollToBottom() {
            this.$nextTick(() => {
                this.scrollTop = 999999
            })
        }
    }
}
</script>

<style scoped>
/* ============ 全局容器 ============ */
.chat-container {
    height: 93vh;
    display: flex;
    flex-direction: column;
    background-color: #0a0e17;
    position: relative;
    overflow: hidden;
}

/* ============ 科技感顶部装饰条 ============ */
.tech-header {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 16rpx 30rpx;
    background: linear-gradient(180deg, #0d1525 0%, #0a0e17 100%);
    border-bottom: 1rpx solid rgba(0, 212, 255, 0.3);
    position: relative;
    z-index: 10;
    flex-shrink: 0;
}

.header-glow {
    width: 12rpx;
    height: 12rpx;
    border-radius: 50%;
    background: #00d4ff;
    box-shadow: 0 0 20rpx #00d4ff, 0 0 40rpx rgba(0, 212, 255, 0.5), 0 0 80rpx rgba(0, 212, 255, 0.3);
    animation: headerPulse 2s ease-in-out infinite;
}

.header-glow:last-child {
    animation-delay: 1s;
}

@keyframes headerPulse {
    0%,
    100% {
        opacity: 1;
        transform: scale(1);
    }
    50% {
        opacity: 0.4;
        transform: scale(0.7);
    }
}

.header-line {
    flex: 1;
    height: 1rpx;
    background: linear-gradient(90deg, transparent, rgba(0, 212, 255, 0.6), transparent);
    margin: 0 20rpx;
}

.header-title {
    font-size: 22rpx;
    letter-spacing: 8rpx;
    color: #00d4ff;
    font-weight: 300;
    text-shadow: 0 0 20rpx rgba(0, 212, 255, 0.8), 0 0 60rpx rgba(0, 212, 255, 0.4);
    white-space: nowrap;
}

/* ============ 消息列表 ============ */
.msg-list {
    flex: 1;
    padding: 30rpx 24rpx;
    box-sizing: border-box;
    position: relative;
    z-index: 1;
}

/* 背景科技网格 */
.grid-bg {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;
    z-index: 0;
    background-image:
        linear-gradient(rgba(0, 212, 255, 0.03) 1rpx, transparent 1rpx),
        linear-gradient(90deg, rgba(0, 212, 255, 0.03) 1rpx, transparent 1rpx);
    background-size: 60rpx 60rpx;
    animation: gridMove 20s linear infinite;
}

@keyframes gridMove {
    0% {
        background-position: 0 0, 0 0;
    }
    100% {
        background-position: 60rpx 60rpx, 60rpx 60rpx;
    }
}

/* ============ 消息项 ============ */
.msg-item {
    display: flex;
    margin-bottom: 36rpx;
    align-items: flex-start;
    position: relative;
    z-index: 1;
    animation: msgAppear 0.35s ease-out forwards;
}

@keyframes msgAppear {
    from {
        opacity: 0;
        transform: translateY(20rpx);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.msg-item.self {
    flex-direction: row-reverse;
}

/* ============ 头像 ============ */
.avatar {
    width: 72rpx;
    height: 72rpx;
    border-radius: 50%;
    background: linear-gradient(135deg, #0d1a2d 0%, #0f2237 50%, #0d1a2d 100%);
    color: #00d4ff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 26rpx;
    flex-shrink: 0;
    position: relative;
    border: 1rpx solid rgba(0, 212, 255, 0.4);
    box-shadow:
        0 0 20rpx rgba(0, 212, 255, 0.2),
        inset 0 0 20rpx rgba(0, 212, 255, 0.1);
    animation: avatarGlow 3s ease-in-out infinite;
}

@keyframes avatarGlow {
    0%,
    100% {
        box-shadow: 0 0 20rpx rgba(0, 212, 255, 0.2), inset 0 0 20rpx rgba(0, 212, 255, 0.1);
    }
    50% {
        box-shadow: 0 0 35rpx rgba(0, 212, 255, 0.4), inset 0 0 30rpx rgba(0, 212, 255, 0.2);
    }
}

.avatar-ring {
    position: absolute;
    top: -4rpx;
    left: -4rpx;
    right: -4rpx;
    bottom: -4rpx;
    border-radius: 50%;
    border: 1rpx dashed rgba(0, 212, 255, 0.3);
    animation: ringSpin 8s linear infinite;
}

@keyframes ringSpin {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}

.avatar-text {
    position: relative;
    z-index: 1;
    text-shadow: 0 0 10rpx rgba(0, 212, 255, 0.8);
}

.msg-item.self .avatar {
    background: linear-gradient(135deg, #0d1a2d 0%, #1a0f2e 50%, #0d1a2d 100%);
    border-color: rgba(168, 85, 247, 0.4);
    color: #a855f7;
    box-shadow:
        0 0 20rpx rgba(168, 85, 247, 0.2),
        inset 0 0 20rpx rgba(168, 85, 247, 0.1);
    animation: avatarGlowSelf 3s ease-in-out infinite;
}

@keyframes avatarGlowSelf {
    0%,
    100% {
        box-shadow: 0 0 20rpx rgba(168, 85, 247, 0.2), inset 0 0 20rpx rgba(168, 85, 247, 0.1);
    }
    50% {
        box-shadow: 0 0 35rpx rgba(168, 85, 247, 0.4), inset 0 0 30rpx rgba(168, 85, 247, 0.2);
    }
}

.msg-item.self .avatar-ring {
    border-color: rgba(168, 85, 247, 0.3);
}

.msg-item.self .avatar-text {
    text-shadow: 0 0 10rpx rgba(168, 85, 247, 0.8);
}

/* ============ 气泡 ============ */
.bubble-wrapper {
    position: relative;
    max-width: 500rpx;
    margin: 0 20rpx;
}

.bubble {
    max-width: 500rpx;
    padding: 22rpx 28rpx;
    border-radius: 16rpx;
    font-size: 30rpx;
    line-height: 1.55;
    word-break: break-all;
    position: relative;
    z-index: 2;
    letter-spacing: 0.5rpx;
}

.bubble-glow {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    border-radius: 16rpx;
    z-index: 1;
    filter: blur(20rpx);
    opacity: 0.5;
    animation: bubbleGlowPulse 4s ease-in-out infinite;
}

@keyframes bubbleGlowPulse {
    0%,
    100% {
        opacity: 0.3;
    }
    50% {
        opacity: 0.7;
    }
}

/* 自己发送的气泡 - 紫色系 */
.msg-item.self .bubble {
    background: linear-gradient(135deg, rgba(88, 28, 135, 0.85) 0%, rgba(49, 12, 80, 0.85) 100%);
    color: #e9d5ff;
    border: 1rpx solid rgba(168, 85, 247, 0.5);
    box-shadow:
        0 4rpx 30rpx rgba(168, 85, 247, 0.25),
        inset 0 1rpx 0 rgba(255, 255, 255, 0.08);
    border-top-right-radius: 4rpx;
}

.msg-item.self .bubble-glow {
    background: rgba(168, 85, 247, 0.3);
}

/* AI气泡 - 青色系 */
.msg-item.ai .bubble {
    background: linear-gradient(135deg, rgba(8, 47, 73, 0.85) 0%, rgba(6, 25, 42, 0.85) 100%);
    color: #a5f3fc;
    border: 1rpx solid rgba(0, 212, 255, 0.4);
    box-shadow:
        0 4rpx 30rpx rgba(0, 212, 255, 0.2),
        inset 0 1rpx 0 rgba(255, 255, 255, 0.06);
    border-top-left-radius: 4rpx;
}

.msg-item.ai .bubble-glow {
    background: rgba(0, 212, 255, 0.25);
}

/* ============ 输入栏 ============ */
.input-bar {
    display: flex;
    align-items: center;
    padding: 20rpx 24rpx;
    background: linear-gradient(180deg, #0a0e17 0%, #0d1525 100%);
    border-top: 1rpx solid rgba(0, 212, 255, 0.25);
    padding-bottom: calc(20rpx + env(safe-area-inset-bottom));
    position: relative;
    z-index: 10;
    flex-shrink: 0;
    box-shadow: 0 -10rpx 40rpx rgba(0, 0, 0, 0.5);
}

.input-wrapper {
    flex: 1;
    display: flex;
    align-items: center;
    height: 76rpx;
    padding: 0 20rpx;
    background: rgba(13, 20, 36, 0.9);
    border: 1rpx solid rgba(0, 212, 255, 0.3);
    border-radius: 12rpx;
    position: relative;
    transition: all 0.3s ease;
    box-shadow: inset 0 0 20rpx rgba(0, 212, 255, 0.05);
}

.input-wrapper:focus-within {
    border-color: rgba(0, 212, 255, 0.7);
    box-shadow:
        0 0 20rpx rgba(0, 212, 255, 0.15),
        inset 0 0 30rpx rgba(0, 212, 255, 0.08);
}

.input-icon {
    font-size: 28rpx;
    color: rgba(0, 212, 255, 0.6);
    margin-right: 16rpx;
    text-shadow: 0 0 10rpx rgba(0, 212, 255, 0.5);
}

.input {
    flex: 1;
    height: 76rpx;
    font-size: 28rpx;
    color: #c8d6e5;
    background: transparent;
    border: none;
    outline: none;
}

.input-placeholder {
    color: rgba(100, 130, 160, 0.7);
    letter-spacing: 1rpx;
}

.input-cursor {
    width: 2rpx;
    height: 36rpx;
    background: #00d4ff;
    animation: cursorBlink 1.2s step-end infinite;
    box-shadow: 0 0 8rpx rgba(0, 212, 255, 0.8);
}

@keyframes cursorBlink {
    0%,
    100% {
        opacity: 1;
    }
    50% {
        opacity: 0;
    }
}

/* ============ 发送按钮 ============ */
.send-btn {
    margin-left: 20rpx;
    height: 76rpx;
    padding: 0 36rpx;
    background: linear-gradient(135deg, #0d3a5c 0%, #0a2a44 50%, #0d3a5c 100%);
    color: #00d4ff;
    border: 1rpx solid rgba(0, 212, 255, 0.5);
    border-radius: 12rpx;
    font-size: 28rpx;
    position: relative;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    letter-spacing: 2rpx;
    box-shadow:
        0 4rpx 20rpx rgba(0, 212, 255, 0.2),
        inset 0 1rpx 0 rgba(255, 255, 255, 0.08);
}

.send-btn-glow {
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(0, 212, 255, 0.15) 0%, transparent 60%);
    animation: btnShine 3s ease-in-out infinite;
    pointer-events: none;
}

@keyframes btnShine {
    0%,
    100% {
        transform: translate(0, 0) scale(1);
    }
    50% {
        transform: translate(20%, 20%) scale(1.3);
    }
}

.send-btn-text {
    position: relative;
    z-index: 2;
    text-shadow: 0 0 10rpx rgba(0, 212, 255, 0.6);
}

.send-btn:active {
    transform: scale(0.95);
    box-shadow:
        0 2rpx 10rpx rgba(0, 212, 255, 0.3),
        inset 0 1rpx 0 rgba(255, 255, 255, 0.05);
}

.send-btn[disabled] {
    background: linear-gradient(135deg, #1a1a2e 0%, #12121f 50%, #1a1a2e 100%);
    color: #3a3a4e;
    border-color: rgba(60, 60, 80, 0.5);
    box-shadow: none;
    text-shadow: none;
}

.send-btn[disabled] .send-btn-glow {
    animation: none;
    opacity: 0;
}

.send-btn[disabled] .send-btn-text {
    text-shadow: none;
}

/* ============ 滚动条美化 ============ */
.msg-list ::-webkit-scrollbar {
    width: 4rpx;
}

.msg-list ::-webkit-scrollbar-track {
    background: transparent;
}

.msg-list ::-webkit-scrollbar-thumb {
    background: rgba(0, 212, 255, 0.3);
    border-radius: 4rpx;
}

/* ============ 消息入场动画延迟 ============ */
.msg-item:nth-child(1) {
    animation-delay: 0s;
}
.msg-item:nth-child(2) {
    animation-delay: 0.05s;
}
.msg-item:nth-child(3) {
    animation-delay: 0.1s;
}
.msg-item:nth-child(4) {
    animation-delay: 0.15s;
}
.msg-item:nth-child(5) {
    animation-delay: 0.2s;
}
</style>