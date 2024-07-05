<script setup lang="ts">
import {SYNC_POST} from "@/scripts/Axios";
import {reactive, ref} from "vue";

const dialogFormVisible = ref(false)

import type {FormProps} from 'element-plus'

const labelPosition = ref<FormProps['labelPosition']>('right')

const formLabelAlign = reactive({
  name: '',
  region: '',
  type: '',
})



let flag = ref(0);

let channel = ref('')
let message = ref('')
const publish = async () => {
  console.log("publish")
  //to do:获取token测试
  await SYNC_POST("/goods/publish", {
    channel: channel.value,
    message:message.value
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      console.log("publish successfully!")
      flag.value = 1;
      console.log("flag=" + flag.value);
    }else {
      flag.value = 0;
      console.log("error!")
      console.log("flag=" + flag.value);
    }
  })
}
import { ElMessage } from 'element-plus'

const suc = () => {
  ElMessage({
    message: '发布信息成功！',
    type: 'success',
  })
}
const err = () => {
  ElMessage.error('发布信息失败！')
}

async function onPublishe() {
  dialogFormVisible.value = false
  await publish()
  if (flag.value === 1) {
    suc()
  } else {
    err()
  }
}

const emit = defineEmits(['child-click'])
const ziChuanFu = () => {
  var pubMsg = "--- 发送消息：" + message.value + "    到频道：" + channel.value + " ---"
  emit('child-click',pubMsg)
  console.log("传给父节点：" + pubMsg)
}
</script>

<template>
  <el-button type="primary" @click="() => {dialogFormVisible = true}" round>
    发布消息
  </el-button>

  <el-dialog v-model="dialogFormVisible" title="发布消息" center width = "35%" draggable>
    <el-form
        :label-position="labelPosition"
        label-width="140px"
        :model="formLabelAlign"
        style="max-width: 460px"
    >
      <el-form-item label="频道">
        <el-input v-model="channel" />
      </el-form-item>
      <el-form-item label="消息">
        <el-input v-model="message" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click=" () => {onPublishe();ziChuanFu();}" >
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>