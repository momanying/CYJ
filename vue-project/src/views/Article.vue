<template>
    <div>
        <div class="card" style="margin-bottom: 5px;">
            <el-input  style="width: 240px; margin-right: 10px" v-model="data.title" placeholder="请输入标题查询" :prefix-icon="Search"></el-input>
            <el-button type="primary" @click="load">查询</el-button>
            <el-button type="warning" @click="reset">重置</el-button>
        </div>
        <div class="card" style="margin-bottom: 5px;">
            <el-button type="primary" @click="handleAdd">新增</el-button>
            <el-button type="danger" @click="delBatch">批量删除</el-button>
        </div>
        <div class="card" style="margin-bottom: 5px; ">
            <div>
                <el-table :data="data.tableData" stripe @Selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" />
                <el-table-column label="标题" prop="title"/>
                <el-table-column label="封面">
                    <template #default="scope">
                        <el-image v-if="scope.row.img" :preview-src-list=[scope.row.img] preview-teleported :src="scope.row.img" style="width: 100px; height: 60px;" />
                    </template>
                </el-table-column>/>
                <el-table-column label="简介" prop="description" show-overflow-tooltip/>
                <el-table-column label="内容" prop="content">
                    <template #default="scope">
                        <el-button type="primary" @click="view(scope.row.content)">查看内容</el-button>
                    </template>
                </el-table-column>
                <el-table-column label="发布时间" prop="time"/>
                <el-table-column label="操作">
                    <template #default="scope">
                        <div style="align-items: center; justify-content: space-between;">
                            <el-button @click="handleEdit(scope.row)" type="primary" :icon="Edit"></el-button>
                            <el-button @click="Del(scope.row.id)" type="danger" :icon="Delete"></el-button>
                         </div>
                    </template>
                </el-table-column>
                </el-table>
            </div>
            <div style="margin-top: 10px ;">
                <el-pagination
                    @size-change="load"
                    @current-change="load"
                    v-model:current-page="data.pageNum"
                    v-model:page-size="data.pageSize"
                    :page-Sizes="[5, 10, 15, 20]"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    :total = "data.total"
                />
            </div>
        </div>
            <el-dialog v-model="data.FormVisible" title="文章信息" width="50%" destroy-on-close>
            <el-form ref="formRef" :model="data.form"  label-width = "80px" style="padding-right: 20px; padding-top: 20px;">
                <el-form-item label="标题" prop="title">
                    <el-input v-model="data.form.title" autocomplete="off" placeholder = "请输入标题" />
                </el-form-item>
                <el-form-item label="封面">
                    <el-upload
                    action="http://localhost:9090/files/upload"
                    list-type="picture"
                    :on-success="handleImgSuccess"
                    >
                    <el-button type="primary">上传封面</el-button>
                    </el-upload>
                </el-form-item>
                <el-form-item label="简介" prop="description">
                    <el-input type="textarea" :rows="3" v-model="data.form.description" autocomplete="off" placeholder = "请输入简介" />
                </el-form-item>
                <el-form-item label="内容" prop="content">
                    <div style="border: 1px solid #ccc">
                        <Toolbar
                        style="border-bottom: 1px solid #ccc"
                        :editor="editorRef"
                        :defaultConfig="toolbarConfig"
                        :mode="mode"
                        />
                        <Editor
                            style="height: 500px; overflow-y: hidden;"
                            v-model="data.form.content"
                            :defaultConfig="editorConfig"
                            :mode="mode"
                            @onCreated="handleCreated"
                        />
                    </div>
                </el-form-item>
            </el-form>
            <template #footer>
            <div class="dialog-footer">
                <el-button @click="data.FormVisible = false">取消</el-button>
                <el-button type="primary" @click="save">保存</el-button>
            </div>
            </template>
            </el-dialog>

            <el-dialog title="内容" v-model="data.viewVisible" width="50%" :close-on-click-model="false" destroy-on-close>
                <div class="editor-content-view" style="padding: 20px" v-html="data.content"></div>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="data.viewVisible = false">关闭</el-button>
                    </span>
                </template>
            </el-dialog>
    </div>
</template>

<script setup>
import {reactive} from 'vue'
import { Edit,Delete,Search } from '@element-plus/icons-vue'
import request from '@/utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref } from 'vue'
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { onBeforeUnmount, shallowRef, onMounted } from 'vue'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

const data = reactive({
    title: null,
    tableData: [],
    pageNum : 1,
    pageSize : 10,
    total : 0,
    FormVisible: false,
    form: {},
    ids: [],
    username: [],
    viewVisible: false,
    content : null
})

const view = (content) =>{
    data.content = content
    data.viewVisible = true
}

const baseUrl = 'http://localhost:9090'
const editorRef = shallowRef()
const mode = 'default'
const editorConfig = { MENU_CONF: {} }
editorConfig.MENU_CONF['uploadImage'] = {
    server: baseUrl + '/files/wang/upload',
    fieldName : 'file'
}
onBeforeUnmount(()=>{
    const editor = editorRef.value
    if(editor==null) return
    editor.destroy()
})

const handleCreated = (editor) =>{
    editorRef.value = editor
}

const handleImgSuccess = (res) => {
    data.form.img = res.data
}

const load = () => {
    request.get('/article/selectPage', {
        params: {
            pageNum: data.pageNum,
            pageSize: data.pageSize,
            name: data.name
        }
    }).then(res => {
        data.tableData = res.data.list
        data.total = res.data.total
    })
}

load()

const reset = () => {
    data.name = null
    load()
}

const handleAdd = () => {
    data.FormVisible = true
    data.form = {}
}

const save = () => {
    data.form.id ? update() : add()
}

const add = () => {
    request.post('/article/add', data.form).then(res => {
        if(res.code === '200') {
            data.FormVisible = false
            ElMessage.success('保存成功')
            load()
        }else {
            ElMessage.error('保存失败')
        }
    })
}

const update = () => {
    request.put('/article/update', data.form).then(res => {
        if(res.code === '200') {
            data.FormVisible = false
            ElMessage.success('保存成功')
            load()
        }else {
            ElMessage.error('保存失败')
        }
    })
}

const handleEdit = (row) => {
    data.FormVisible = true
    data.form = JSON.parse(JSON.stringify(row))
}

const Del = (id) => {
    ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {
        type: 'warning'
    }).then(() => {
        request.delete('/article/delete/' + id).then(res => {
        if(res.code === '200') {
            ElMessage.success('删除成功')
            load()
        }else {
            ElMessage.error(res.msg)
        }
    })
    }).catch(() => {
        ElMessage.info('取消删除')
    })
}

const handleSelectionChange = (rows) => {
    data.ids = rows.map(row => row.id)
}

const delBatch = () => {
    if(data.ids.length === 0) {
        ElMessage.warning('请选择要删除的数据')
        return
    }
    ElMessageBox.confirm('删除数据后无法恢复', '删除确认', {
        type: 'warning'
    }).then(() => {
        request.delete('/article/deleteBatch', {
            data: data.ids
        }).then(res => {
            if(res.code === '200') {
                ElMessage.success('删除成功')
                load()
            }else {
                ElMessage.error(res.msg)
            }
        })
    }).catch(() => {
        ElMessage.info('取消删除')
    })
}
</script>