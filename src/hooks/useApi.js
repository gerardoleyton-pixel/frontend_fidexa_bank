import { useState, useCallback } from 'react'
import api from '../services/api'

// useApi: wrapper around api calls with loading/error handling
export default function useApi(){
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const call = useCallback(async (fn, ...args) => {
    setLoading(true)
    setError(null)
    try{
      const res = await fn(...args)
      setLoading(false)
      return res
    }catch(e){
      setError(e.message || 'Error')
      setLoading(false)
      throw e
    }
  }, [])

  const get = useCallback((url, config) => call(api.get, url, config), [call])
  const post = useCallback((url, data, config) => call(api.post, url, data, config), [call])
  const put = useCallback((url, data, config) => call(api.put, url, data, config), [call])
  const del = useCallback((url, config) => call(api.delete, url, config), [call])

  return { loading, error, get, post, put, del }
}
