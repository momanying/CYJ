<template>
    <div class="card" style="padding: 40px 20px;">
        <el-form ref="formRef" :rules="data.rules" :model="data.form"  label-width = "100px" style="padding-right: 20px; padding-top: 20px;">
            <el-form-item label="原密码" prop="password">
                <el-input show-password v-model="data.form.password" autocomplete="off" placeholder = "请输入原密码" />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input show-password v-model="data.form.newPassword" autocomplete="off" placeholder = "请输入新密码" />
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmPassword" required>
                <el-input show-password v-model="data.form.confirmPassword" autocomplete="off" placeholder = "请再次确认新密码" />
            </el-form-item>
            <div style="text-align: center;">
                    <el-button type="danger" @click="updatePassword" style="padding: 20px 30px;">
                        保存
                    </el-button>
            </div>
        </el-form>

    </div>
</template>

<script setup>
import { reactive } from 'vue';
import { ref } from 'vue';
import request from '@/utils/request';

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次确认密码'))
  } else if (value != data.form.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const formRef = ref(null)

const data = reactive({
    user: JSON.parse(localStorage.getItem('xmuser')),
    form:{},
    rules: {
        password: [
            { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
            { required: true, message: '请输入新密码', trigger: 'blur' }
        ],
        confirmPassword: [
            { validator: validatePass, trigger: 'blur' }
        ],
    }
})

const updatePassword = () => {
    data.form.id = data.user.id
    data.form.role = data.user.role
    formRef.value.validate((valid) => {
        if (valid) {
            request.put('updatePassword',data.form).then(res => {
                if (res.code == 200) {
                    ElMessage.success('修改成功')
                    localStorage.removeItem('xmuser')
                    setTimeout(() => {
                        location.href = '/login'
                    }, 500)
                } else {
                    ElMessage.error(res.msg)
                }
            })
        }
    })
}
</script>
