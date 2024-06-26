import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken, setTokeTime} from '@/utils/auth'
import router, { resetRouter } from '@/router'

const state = {
  token: getToken(),

  username: '',
  avatar: '',
  introduction: '',
  roles: [],
  uno:'',
  phone:'',
  email:'',
  realname:''

}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_NAME: (state, name) => {
    state.username = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  },
  SET_USERUID: (state, uno) => {
    state.uno= uno
  },
  SET_PHONE: (state, phone) => {
    state.phone= phone
  },
  SET_EMAIL: (state, email) => {
    state.email= email
  },
  SET_REALNAME: (state, realname) => {
    state.realname= realname
  }

}

const actions = {
  // user/login 用户登录
  login({ commit }, userInfo) {
    //从用户信息中解构出用户名和密码
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      //传递用户名和密码
      login({ username: username.trim(), password: password }).then(response => {
        //解构出后端返回的数据
        console.log(response)
        const { token, expires } = response
        //将返回的token保存到store中
        commit('SET_TOKEN', token)
        //设置token
        setToken(token)
        setTokeTime(expires)
        resolve() //放行
      }).catch(error => {
        reject(error) //拒绝访问
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

      const { roles, username, avatar, remark, uno, phone, email, realname } = data
        console.log(data)
        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!')
        }

        commit('SET_ROLES', roles)
        commit('SET_NAME', username)
        commit('SET_AVATAR', avatar)
        commit('SET_INTRODUCTION', remark)
        commit('SET_USERUID', uno)
        commit('SET_PHONE', phone)
        commit('SET_EMAIL', email)
        commit('SET_REALNAME', realname)

        sessionStorage.setItem("codeList", JSON.stringify(roles))
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
