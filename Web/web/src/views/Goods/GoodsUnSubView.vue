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

// let id = ref('')
// let time = ref('')

let flag = ref(0);
let channel = ref('')
// let channelList = ref('')

// async function delChannel(){
//   const index = channelList.value.indexOf(channel.value + ",")
//   channelList.value = channelList.value.substring(0,index);
// }

const unsub = async () => {
  console.log("unsub")
  //to do:获取token测试
  await SYNC_POST("/goods/unsub", {
    channel:channel.value
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      console.log("unsubscribe successfully!")
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
    message: '取消订阅成功！',
    type: 'success',
  })
}
const err = () => {
  ElMessage.error('取消订阅失败！')
}

async function unSub() {
  dialogFormVisible.value = false
  await unsub()
  if (flag.value === 1) {
    suc()
  } else {
    err()
  }
}

const emit = defineEmits(['child-click'])
const ziChuanFu = () => {
  emit('child-click',channel.value)
}
</script>

<template>
  <el-button type="primary" @click="() => {dialogFormVisible = true}" round>
    取消订阅
  </el-button>

  <el-dialog v-model="dialogFormVisible" title="取消订阅" center width = "35%" draggable>
    <el-form
        :label-position="labelPosition"
        label-width="140px"
        :model="formLabelAlign"
        style="max-width: 460px"
    >
      <el-form-item label="要取消订阅的频道：">
        <el-input v-model="channel" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click = "() => {unSub();ziChuanFu()}">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<style scoped>

</style>