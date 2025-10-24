import api from './api'

const authService = {
  async login({email, password}){
    const res = await api.post('/auth/login', { email, password })
    return res.data
  }
}

export default authService
