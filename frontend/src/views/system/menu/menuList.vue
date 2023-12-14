<template>
  <el-main>
    <!-- 新增按钮 -->
    <el-button type="success" size="small" @click="openAddWindow()" icon="el-icon-plus" v-if="hasPermission('system:menu:add')">新增</el-button>
    <!--表格-->
    <el-table
      :data="menuList"
      style="width: 100%;margin-top: 20px;"
      :height="tableHeight"
      row-key="mno"
      border stripe
      default-expand-all
      :tree-props="{children: 'children'}">
      <el-table-column prop="label" label="菜单名称" sortable width="180"></el-table-column>
      <el-table-column prop="pname" label="父菜单名称" sortable width="180"></el-table-column>
      <el-table-column prop="type" label="菜单类型" sortable width="180" align="center">
        <template v-slot="scope">
          <el-tag size="normal" v-if="scope.row.type === 0">目录</el-tag>
          <el-tag size="normal" type="success" v-else-if="scope.row.type === 1">菜单</el-tag>
          <el-tag size="normal" type="warning" v-else-if="scope.row.type === 2">按钮</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="icon" label="菜单图标" sortable width="180" align="center">
        <template v-slot="scope">
          <i :class="scope.row.icon" v-if="scope.row.icon.includes('el-icon')"></i>
          <svg-icon v-else :icon-class="scope.row.icon"></svg-icon>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="菜单路由" sortable ></el-table-column>
      <el-table-column prop="url" label="菜单地址" sortable ></el-table-column>
      <el-table-column label="操作" sortable width="200" align="center">
        <template v-slot="scope">
          <el-button type="primary" size="mini" @click="onEdit(scope.row)" icon="el-icon-edit" v-if="hasPermission('system:menu:update')">编辑</el-button>
          <el-button type="danger" size="mini" @click="onDelete(scope.row)" icon="el-icon-delete" v-if="hasPermission('system:menu:delete')">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <!--新增或修改的窗口-->
    <system-dialog :title="menuDialog.title" :visible="menuDialog.visible" :width="menuDialog.width" :height="menuDialog.height"
                   @onClose="onClose()" @onConfirm="onConfirm()">
      <div slot="content">
        <el-form ref="menu" :model="menuForm" :rules="rules" label-width="80px" :inline="true" size="small">
          <el-col :span="24">
            <el-form-item label="菜单类型" prop="type">
              <el-radio-group v-model="menuForm.type" >
                <el-radio :label="0">目录</el-radio>
                <el-radio :label="1">菜单</el-radio>
                <el-radio :label="2">按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-form-item label="所属菜单" size="small" prop="pname">
            <el-input v-model="menuForm.pname" :readonly="true" @click.native="selectParentMenu()"/>
          </el-form-item>
          <el-form-item label="菜单名称" size="small" prop="label">
            <el-input v-model="menuForm.label"/>
          </el-form-item>
          <el-form-item label="路由名称" size="small" prop="name" v-if="menuForm.type === 1">
            <el-input v-model="menuForm.name"/>
          </el-form-item>
          <el-form-item label="路由地址" size="small" prop="path" v-if="menuForm.type !== 2">
            <el-input v-model="menuForm.path"/>
          </el-form-item>
          <el-form-item label="组件路径" size="small" prop="url" v-if="menuForm.type === 1">
            <el-input v-model="menuForm.url"/>
          </el-form-item>
          <el-form-item label="权限字段" size="small" prop="code">
            <el-input v-model="menuForm.code"/>
          </el-form-item>
          <el-form-item label="菜单图标" size="small">
            <my-icon @selecticon="setIcon" ref="child"></my-icon>
          </el-form-item>
        </el-form>
      </div>
    </system-dialog>

    <!-- 选择上级菜单 -->
    <system-dialog :title="parentDialog.title" :width="parentDialog.width" :visible="parentDialog.visible"
                   @onClose="onParentClose()" @onConfirm="onParentConfirm()">
      <div slot="content">
        <el-tree style="font-size: 14px;" ref="parentTree" :data="parentMenuList" :props="defaultProps" empty-text="暂无数据" node-key="mno"
                 :show-checkbox="false" default-expand-all :highlight-current="true" :expand-on-click-node="false"
          @node-click="handleNodeClick">
          <template v-slot="{node, data}">
            <div class="custom-tree-node">
            <span v-if="data.children.length === 0">
                <i :class="data.icon" v-if="data.icon.includes('el-icon')" style="color: rgb(128,128,128);font-size: 18px"></i>
            </span>
              <span v-else @click.stop="changIcon(data)">
              <svg-icon v-if="data.open" icon-class="add-s"/>
              <svg-icon v-else icon-class="sub-s"/>
            </span>
              <span style="margin-left: 3px">{{node.label}}</span>
            </div>
          </template>
        </el-tree>
      </div>

    </system-dialog>
  </el-main>
</template>

<script>
import systemDialog from "@/components/system/systemDialog.vue";
import menu from "@/api/menu";
import {style} from "svgo/lib/svgo/jsAPI";
import myIcon from "@/components/system/myIcon.vue";
import hasPermission from "@/permission/index";


  export default {
    name: "menuList",
    computed: {

    },
    components:{
      systemDialog,
      myIcon
    },
    data(){
      return{
        menuList:[],
        tableHeight:0,
        menuDialog:{
          title:"新增菜单",
          visible:false,
          width:630,
          height:270
        },
        rules: {
          type:[{required: true, message: '请选择菜单类型', trigger: 'change'}],
          pname:[{required: true, message: '请选择所属菜单名称', trigger: 'change'}],
          label:[{required: true, message: '请输入菜单名称', trigger: 'blur'}],
          path:[{required: true, message: '请输入路由路径', trigger: 'blur'}],
          url:[{required: true, message: '请输入组件路径', trigger: 'blur'}],
          code:[{required: true, message: '请输入权限字段', trigger: 'blur'}],
          name:[{required: true, message: '请输入路由名称', trigger: 'blur'}],
          icon:[{required: true, message: '请输入图标', trigger: 'blur'}],
        },
        menuForm:{
          mno:"",
          type:"",
          pid:"",
          pname:"",
          label:"",
          path:"",
          icon:"",
          url:"",
          code:"",
          name:""
        },
        parentDialog:{
          title: "选择所属菜单",
          width: 280,
          height: 450,
          visible: false
        },
        defaultProps:{
          children:'children',
          label:'label'
        },
        parentMenuList:[],

      }
    },
    created() {
      this.search()

    },
    mounted() {
      this.$nextTick(() => {
        this.tableHeight = window.innerHeight - 180
      })
    },
    //查询菜单列表
    methods:{
      hasPermission,
      style() {
        return style
      },
      openAddWindow(){
        this.$resetForm("menu",this.menuForm);
        this.menuDialog.title = "新增菜单";
        this.menuDialog.visible = true;
        this.$nextTick(() => {
          this.$refs["child"].userChooseIcon = ""
        })
      },

      async selectParentMenu(){
        this.parentDialog.visible = true;
        let res = await menu.getParentMenuList();
        if (res.success){
          this.parentMenuList = res.data;
        }
        console.log(res);
        console.log(this.parentMenuList);
      },
      async search(){
        let res = await menu.getMenuList();
        if (res.success){
          this.menuList = res.data;
          console.log(this.menuList);
        }else {
          console.log('3');
        }
      },
      onClose(){
        this.menuDialog.visible = false;
      },
      onConfirm(){
        //表单验证
        this.$refs.menu.validate(async (valid) => {
          if (valid) {
            let res = null;
            //判断当前是新增还是修改
            if (this.menuForm.mno === "") {
              //发生添加请求
              res = await menu.addMenu(this.menuForm);
            } else {
              //发生修改请求
              res = await menu.updateMenu(this.menuForm)
            }

            if (res.success) {
              this.$message.success(res.message);
              await this.search();
              this.menuDialog.visible = false;
            } else {
              this.$message.error(res.message);
            }
            this.menuDialog.visible = false;
          }
        })
      },
      onParentClose(){
        this.parentDialog.visible = false;
        console.log(2);
      },
      onParentConfirm(){
        console.log(1);
        this.parentDialog.visible = false;
      },
      //切换图标
      changIcon(data){
        data.open = !data.open
        this.$refs.parentTree.store.nodesMap[data.mno].expanded = !data.open
      },
      handleNodeClick(data){
        this.menuForm.pid = data.mno;
        this.menuForm.pname = data.label;
      },
      setIcon(icon){
        this.menuForm.icon = icon;
      },
      onEdit(row){
        //回现数据
        this.$objCopy(row, this.menuForm);
        this.menuDialog.title = "编辑菜单";
        this.menuDialog.visible = true;
        this.$nextTick(() => {
          this.$refs["child"].userChooseIcon = row.icon
        })
      },
      async onDelete(row) {
        //判断是否存在子菜单
        let res = await menu.checkMenu(row.mno);
        if(!res.success){
          //提示不能删除
          this.$message.warning(res.message);
        }else {
          let confirm = await this.$myconfirm("确定要删除吗？");
          if(confirm){
            //发送删除请求
            let res = await menu.deleteMenu(row.mno);
            //判断是否成功
            if (res.success){
              this.$message.success(res.message);
              await this.search();
            }else {
              this.$message.error(res.message);
            }
          }
        }
      }
    }
  }
</script>

<style scoped lang="scss">

</style>
