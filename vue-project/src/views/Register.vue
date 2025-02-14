<template>
    <div class="login-container">
        <div class="login-box">
            <div style="padding: 50px 30px; background-color: white; margin-left: 100px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.2)">
                <el-form ref="formRef" :rules="data.rules" :model="data.form" style="width: 350px;">
                    <div style="margin-bottom: 30px; font-size: 24px; text-align: center; color: #0742b1; font-weight: bold;">欢迎注册</div>
                    <el-form-item prop="username">
                        <el-input size="large" v-model="data.form.username" placeholder="请输入账号" prefix-icon="User"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input show-password size="large" v-model="data.form.password" placeholder="请输入密码" prefix-icon="Lock"></el-input>
                    </el-form-item>
                    <el-form-item prop="confirmpassword">
                        <el-input show-password size="large" v-model="data.form.confirmpassword" placeholder="请确认密码" prefix-icon="Lock"></el-input>
                    </el-form-item>
                    <div style="margin-bottom: 20px;">
                        <el-button @click="register"  size="large" style="width: 100%" type="primary">注册</el-button>
                    </div>
                    <div style="text-align: right;">已有账号？请 <a style="color: #0742b1; text-decoration: none" href="/login">登录</a></div>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive,ref } from 'vue';
import { User,Lock } from '@element-plus/icons-vue';
import  request  from '@/utils/request'

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入密码'))
  } else if (value != data.form.password) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const data = reactive({
    form:{},
    rules:{
        username: [
            { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        confirmpassword: [
            { validator: validatePass, trigger: 'blur' }
        ]
    }
})

const formRef = ref(null)

const register = () => {
    formRef.value.validate((valid) => {
        if (valid) {
            request.post('/register',data.form).then(res => {
                if(res.code == 200){
                    ElMessage.success('登录成功')
                    setTimeout(() => {
                        location.href = '/login'
                    }, 500);
                } else {
                    ElMessage.error(res.msg)
                }
            })
        } else {
            return false;
        }
    });
}

</script>

<style scoped>
.login-container {
    height: 100vh;
    overflow: hidden;
    background-image: url("/src/assets/下载.jpg");
    background-size: cover;
    background-position: 0 -180px;
}
.login-box{
    position:absolute;
    width: 70%;
    height: 100%;
    display: flex;
    align-items: center;
    right: 0;
}
</style>