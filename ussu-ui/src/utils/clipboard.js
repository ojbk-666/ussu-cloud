import Vue from 'vue'
import Clipboard from 'clipboard'

function clipboardSuccess(successMessage) {
  Vue.prototype.$message({
    message: successMessage,
    type: 'success',
    duration: 1500
  })
}

function clipboardError(errorMessage) {
  Vue.prototype.$message({
    message: errorMessage,
    type: 'error'
  })
}

export default function handleClipboard(text, event, successMessage = "已复制", errorMessage= "复制失败") {
  const clipboard = new Clipboard(event.target, {
    text: () => text
  })
  clipboard.on('success', () => {
    clipboardSuccess(successMessage)
    clipboard.destroy()
  })
  clipboard.on('error', () => {
    clipboardError(errorMessage)
    clipboard.destroy()
  })
  clipboard.onClick(event)
}
