<template xmlns:white-space="http://www.w3.org/1999/xhtml">
  <el-row>
  <el-col :span="9">
    <el-row>
      <el-button type="success" @click="() => getAll()" round>
        查询所有商品
      </el-button>
    </el-row>
    <br/>
    <el-row>
      <el-button type="success" @click="() => getGoodsById()" round>
        按编号查询
      </el-button>
      &nbsp;&nbsp;&nbsp;<el-input v-model="id" placeholder="请输入要查找的输入商品编号" clearable />
    </el-row>
    <br/>
    <el-row>
      <el-button type="success" @click="() => getGoodsByName()" round>
        按名称查询
      </el-button>
      &nbsp;&nbsp;&nbsp;<el-input v-model="name" placeholder="请输入要查找的输入商品名称" clearable />
    </el-row>
    <br/>
    <el-row>
      <el-button type="success" @click="() => getGoodsByBrand()" round>
        按品牌查询
      </el-button>
      &nbsp;&nbsp;&nbsp;<el-input v-model="brand" placeholder="请输入要查找的商品品牌" clearable />
    </el-row>
    <br/>
    <el-row>
      <el-button type="success" @click="() => getGoodsByType()" round>
        按类型查询
      </el-button>
      &nbsp;&nbsp;&nbsp;<el-input v-model="type" placeholder="请输入要查找的商品类型" clearable />
    </el-row>
    <br/>
    <div class="goodsAdd" >
      <GoodsAddView class="goodsadd"/> &nbsp;&nbsp;&nbsp;
      <GoodsDeleteView class="goodsdelete"/>&nbsp;&nbsp;&nbsp;
      <GoodsUpdateView class="goodsupdate"/>&nbsp;&nbsp;&nbsp;
      <GoodsExpireView class="goodssetexprie"/>
    </div>
  </el-col>

  <el-col :span="12">
    <el-col :span="18">
      <div style="width: 110%;">
        <el-card style="width: 110%;height: 300px" >
<!--          <div style="text-align: center;font-size: 15px;margin-bottom: 5px"> 111  </div>-->
          <div style="width: 100%;height: 230px;border:1px solid #000000;border-radius: 5px;overflow-y:auto;margin-bottom: 10px">
            <div v-for="(item,index) in msgList" :key="index" white-space:pre-wrap>
              {{ item }}
            </div>
          </div>
          <div style="text-align: center;">
            <GoodsSubView @child-click="zCfadd"/> <GoodsUnSubView @child-click="zCfdel"/> <GoodsPubView @child-click="zCfpub"/>
          </div>
        </el-card>
      </div>
    </el-col>
  </el-col>
  </el-row>

  <el-table :data="tableData" stripe style="width: 83.4%"  border max-height="440" v-model = tableData>
    <el-table-column prop="id" label="编号" width="180" />
    <el-table-column prop="name" label="名称" width="180" />
    <el-table-column prop="brand" label="品牌" width="180"/>
    <el-table-column prop="type" label="类型" width="180"/>
    <el-table-column prop="unit" label="单位" width="180"/>
    <el-table-column prop="price" label="价格" width="180"/>
    <el-table-column prop="time" label="过期时间" width="180"/>
  </el-table>
</template>

<script lang="ts" setup>
import GoodsAddView from "@/views/Goods/GoodsAddView.vue"
import GoodsUpdateView from "@/views/Goods/GoodsUpdateView.vue"
import GoodsExpireView from "@/views/Goods/GoodsExpireView.vue"
import GoodsDeleteView from "@/views/Goods/GoodsDeleteView.vue"
import GoodsSubView from "@/views/Goods/GoodsSubView.vue"
import GoodsUnSubView from "@/views/Goods/GoodsUnSubView.vue"
import GoodsPubView from "@/views/Goods/GoodsPubView.vue"
import {SYNC_GET} from "@/scripts/Axios";
import { ref} from "vue";
import {ElMessage} from "element-plus";

let id = ref('')
let name = ref('');
let brand = ref('')
let type = ref('')

let flag = ref(0)
let num = ref(0)

let addChannel = ref('')
let delChannel = ref('')
let pubMsg = ref('')

let tableData= ref<any[]>([])
async function getGoodsById(){
  await (SYNC_GET('/goods/getGoodsById', {
    id:id.value
  }, async (response) => {
    console.log("id:"+id.value)
    console.log("getById")
    if (response.status === 200 && response.data.statusMsg === 'success') {
      console.log("success")
      tableData.value = []
      if(response.data.goods != null) {
        tableData.value.push(response.data.goods)
      }
      flag.value = 1;
      if(response.data.goods != null) {
        num.value = 1
      }
      else{
        num.value = 0
      }
      suc()
      console.log(tableData.value)
    } else {
      flag.value = 0;
      err()
      console.log("error!")
      console.log(response)
    }
  }))
}
async function getAll(){
  await (SYNC_GET('/goods/getAll', {
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      tableData.value = response.data.goods
      flag.value = 1;
      if(response.data.goods != null) {
        num.value = response.data.goods.length
      }
      else{
        num.value = 0
      }
      suc()
      console.log(tableData.value)
    } else {
      flag.value = 0;
      err()
      console.log(response)
    }
  }))
}

async function getGoodsByName(){
  await (SYNC_GET('/goods/getGoodsByName', {
    name:name.value
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      tableData.value = response.data.goods
      flag.value = 1;
      if(response.data.goods != null) {
        num.value = response.data.goods.length
      }
      else{
        num.value = 0
      }
      suc()
      console.log(tableData.value)
    } else {
      flag.value = 0;
      err()
      console.log(response)
    }
  }))
}
async function getGoodsByBrand(){
  await (SYNC_GET('/goods/getGoodsByBrand', {
    brand:brand.value
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      tableData.value = response.data.goods
      flag.value = 1;
      if(response.data.goods != null) {
        num.value = response.data.goods.length
      }
      else{
        num.value = 0
      }
      suc()
      console.log(tableData.value)
    } else {
      flag.value = 0
      err()
      console.log(response)
    }
  }))
}
async function getGoodsByType(){
  await (SYNC_GET('/goods/getGoodsByType', {
    type:type.value
  }, async (response) => {
    if (response.status === 200 && response.data.statusMsg === 'success') {
      tableData.value = response.data.goods
      flag.value = 1;
      if(response.data.goods != null) {
        num.value = response.data.goods.length
      }
      else{
        num.value = 0
      }
      suc()
      console.log(tableData.value)
    } else {
      flag.value = 0
      err()
      console.log(response)
    }
  }))
}

const zCfadd = (value: string) => {
  console.log("增加订阅的频道："+addChannel.value)
  addChannel.value = value;
  sendAdd()
}

const zCfdel = (value: string) => {
  delChannel.value = value;
  console.log("取消订阅的频道："+delChannel.value)
  sendDel()
}

const zCfpub = (value: string) => {
  pubMsg.value = value;
  console.log("发布消息："+pubMsg.value)
  msgList.value.push(pubMsg.value)
}

let message = ref('')

let msgList = ref<any[]>([])

  let socketUrl = "ws://localhost:8080/websocket/root"

  // 开启一个websocket服务
  let socket = new WebSocket(socketUrl);
  //打开事件
  socket.onopen = function () {
    console.log("websocket已打开");
  };
  //  浏览器端收消息，获得从服务端发送过来的文本消息
  socket.onmessage = function (msg) {
    console.log("收到数据====" + msg.data)
    message.value = msg.data
    msgList.value.push(msg.data)
  };
  //关闭事件
  socket.onclose = function () {
    console.log("websocket已关闭");
  };
  //发生了错误事件
  socket.onerror = function () {
    console.log("websocket发生了错误");
  }


//发送消息
function sendAdd(){
  //用户名 + 消息
  var msg = "addSub:" + addChannel.value;
  if(socket != null){
    socket.send(msg);
    msgList.value.push("--- 订阅频道：" + addChannel.value +" ---")
  }

  // onmessage();
}

function sendDel(){
  //用户名 + 消息
  var msg = "unSub:" + delChannel.value;
  if(socket != null){
    socket.send(msg);
    msgList.value.push("--- 取消订阅：" + delChannel.value +" ---")
  }
}

const suc = () => {
  ElMessage({
    message: '查询成功！商品数量：' + num.value,
    type: 'success',
  })
}
const err = () => {
  ElMessage.error('查询失败！')
}
</script>


<style scoped>
.el-button--text {
  margin-right: 15px;
}
.el-select {
  width: 300px;
}
.el-input {
  width: 300px;
}
.dialog-footer button:first-child {
  margin-right: 10px;
}

div{
  white-space:pre-wrap;
}
</style>