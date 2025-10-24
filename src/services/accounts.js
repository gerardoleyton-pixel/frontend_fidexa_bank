import api from './api'

const accountsService = {
  async getAll(){
    const res = await api.get('/accounts')
    return res.data || []
  },
  async getByUser(userId){
    const res = await api.get(`/accounts/by-user?userId=${userId}`)
    return res.data || []
  },
  async getById(id){
    const res = await api.get(`/accounts/${id}`)
    return res.data
  },
  async create(payload){
    const res = await api.post('/accounts', payload)
    return res.data
  },
  async update(id, payload){
    const res = await api.put(`/accounts/${id}`, payload)
    return res.data
  }
}

export default accountsService
