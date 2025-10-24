import api from './api'

const transactionsService = {
  async listByAccount(accountId){
    const res = await api.get(`/transactions/by-account?accountId=${accountId}`)
    return res.data || []
  },
  async deposit({accountId, amount}){
    const res = await api.post('/transactions/deposit', { accountId, amount })
    return res.data
  },
  async withdraw({accountId, amount}){
    const res = await api.post('/transactions/withdraw', { accountId, amount })
    return res.data
  },
  async transfer({fromAccountId, toAccountId, amount}){
    const res = await api.post('/transactions/transfer', { fromAccountId, toAccountId, amount })
    return res.data
  }
}

export default transactionsService
