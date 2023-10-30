import http from "@/utils/request";

export default {
  //查询菜单列表
  async getMenuList(params){
    return http.get("/api/menu/list", params);
  },
  async getParentMenuList(params){
    return http.get("/api/menu/parent/list", params);
  },
  async addMenu(params){
    return http.post("/api/menu/add", params);
  },
  async updateMenu(params){
    return http.put("/api/menu/update", params);
  },
  async deleteMenu(params){
    return http.delete("/api/menu/delete", params);
  },
  async checkMenu(params){
    return http.getRestApi("/api/menu/check", params);
  }
}

