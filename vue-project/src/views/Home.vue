<template>
  <div>
    <div class="card">
      这是我的第一个前后端分离demo
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { RouterLink } from 'vue-router';
import request from '@/utils/request'
const a = ref(1)
let b = ref(2)
const data = reactive({
  employeelist : []
})
request.get('employee/selectAll').then(res => {
  console.log(res)
  data.employeelist = res.data
})
</script>